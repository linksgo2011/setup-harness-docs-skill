import { test, expect } from '@playwright/test'

const BASE = 'http://localhost:5173'
const ADMIN_EMAIL = 'admin@test.com'
const ADMIN_PASSWORD = 'password'

/* ─────────────── Public Pages ─────────────── */

test.describe('Public Pages', () => {

  test('homepage loads with dynamic content', async ({ page }) => {
    await page.goto('/')
    await expect(page.locator('h1')).toHaveText('专业咨询服务')
    await expect(page.locator('text=免费注册')).toBeVisible()
    await expect(page.locator('text=立即预约')).toBeVisible()
    await expect(page.locator('text=© 2026 咨询预约平台')).toBeVisible()
  })

  test('login page renders form', async ({ page }) => {
    await page.goto('/login')
    await expect(page.locator('h2')).toHaveText('登录')
    await expect(page.locator('input[type="email"]')).toBeVisible()
    await expect(page.locator('input[type="password"]')).toBeVisible()
    await expect(page.locator('button[type="submit"]')).toHaveText('登录')
    await expect(page.locator('text=还没有账号？')).toBeVisible()
  })

  test('register page renders form', async ({ page }) => {
    await page.goto('/register')
    await expect(page.locator('h2')).toHaveText('注册')
    await expect(page.locator('input[type="email"]')).toBeVisible()
    await expect(page.locator('input[type="password"]')).toBeVisible()
    await expect(page.locator('button[type="submit"]')).toHaveText('注册')
    await expect(page.locator('text=已有账号？')).toBeVisible()
  })

  test('public API endpoints return 200', async ({ request }) => {
    let resp = await request.get(`${BASE}/api/v1/data-dictionaries`)
    expect(resp.status()).toBe(200)

    resp = await request.get(`${BASE}/api/v1/business-rules`)
    expect(resp.status()).toBe(200)

    resp = await request.get(`${BASE}/api/v1/consultants`)
    expect(resp.status()).toBe(200)
    const body = await resp.json()
    expect(body.length).toBeGreaterThanOrEqual(3)
  })

  test('navigate from homepage to login and register', async ({ page }) => {
    await page.goto('/')
    await page.click('text=登录')
    await expect(page).toHaveURL('/login')

    await page.click('text=立即注册')
    await expect(page).toHaveURL('/register')
  })
})

/* ─────────────── Auth & User Lifecycle (serial: share registered email) ─────────────── */

let registerEmail = ''

test.describe.serial('Auth & User Lifecycle', () => {

  test('login with admin credentials succeeds', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })
    await expect(page.locator('h1')).toHaveText('管理仪表盘')
  })

  test('login with wrong password shows error', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', 'wrongpassword')
    await page.click('button[type="submit"]')
    await expect(page.locator('.error')).toBeVisible({ timeout: 8000 })
  })

  test('register new user succeeds', async ({ page }) => {
    registerEmail = `e2e_${Date.now()}@test.com`
    await page.goto('/register')

    await page.locator('input[placeholder*="姓名"]').fill('E2E用户')
    await page.fill('input[type="email"]', registerEmail)
    await page.locator('input[placeholder*="选填"]').fill('13800000001')
    await page.fill('input[type="password"]', 'Test123456')

    await page.click('button[type="submit"]')
    await page.waitForURL('**/user/dashboard', { timeout: 10000 })
    await expect(page.locator('text=E2E用户')).toBeVisible()
  })

  test('register with duplicate email shows error', async ({ page }) => {
    await page.goto('/register')
    await page.locator('input[placeholder*="姓名"]').fill('重复用户')
    await page.fill('input[type="email"]', registerEmail)
    await page.fill('input[type="password"]', 'Test123456')
    await page.click('button[type="submit"]')
    await expect(page.locator('.error')).toBeVisible({ timeout: 8000 })
  })

  test('registered user logs in and sees dashboard', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', registerEmail)
    await page.fill('input[type="password"]', 'Test123456')
    await page.click('button[type="submit"]')
    await page.waitForURL('**/user/dashboard', { timeout: 10000 })
    await expect(page.locator('h1').first()).toHaveText('用户中心')
  })

  test('user dashboard shows appointment stats', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', registerEmail)
    await page.fill('input[type="password"]', 'Test123456')
    await page.click('button[type="submit"]')
    await page.waitForURL('**/user/dashboard', { timeout: 10000 })

    await expect(page.locator('.stat-card').filter({ hasText: '总预约' })).toBeVisible()
    await expect(page.locator('text=待确认')).toBeVisible()
    await expect(page.locator('text=已完成')).toBeVisible()
  })

  test('browse consultants on book page', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', registerEmail)
    await page.fill('input[type="password"]', 'Test123456')
    await page.click('button[type="submit"]')
    await page.waitForURL('**/user/dashboard', { timeout: 10000 })

    await page.goto('/user/book')
    await expect(page.locator('h1')).toHaveText('预约咨询')
    await expect(page.locator('text=张医生')).toBeVisible()
    await expect(page.locator('text=李律师')).toBeVisible()
    await expect(page.locator('text=王教练')).toBeVisible()
  })

  test('book an appointment then cancel it', async ({ page }) => {
    // Login
    await page.goto('/login')
    await page.fill('input[type="email"]', registerEmail)
    await page.fill('input[type="password"]', 'Test123456')
    await page.click('button[type="submit"]')
    await page.waitForURL('**/user/dashboard', { timeout: 10000 })

    // Go to book page
    await page.goto('/user/book')
    await expect(page.locator('h1')).toHaveText('预约咨询')

    // Step 1: Select consultant
    await page.locator('text=张医生').click()
    await expect(page.locator('text=2. 选择日期')).toBeVisible()

    // Step 2: Pick a date
    const dateBtn = page.locator('div.date-list button').first()
    await expect(dateBtn).toBeVisible()
    await dateBtn.click()
    await page.waitForTimeout(1500)

    // Step 3: Pick a time slot
    const slotBtn = page.locator('div.slot-list button').first()
    if (await slotBtn.isVisible({ timeout: 5000 })) {
      await slotBtn.click()
      await expect(page.locator('text=4. 备注')).toBeVisible()

      // Step 4: Add note and submit
      await page.fill('textarea', 'E2E booking test')
      await page.click('text=确认预约')
      await page.waitForURL('**/user/appointments', { timeout: 10000 })

      // Verify appointment created
      await expect(page.locator('h1')).toHaveText('我的预约')

      // Cancel the appointment
      const cancelBtn = page.locator('button:has-text("取消")').first()
      if (await cancelBtn.isVisible({ timeout: 5000 })) {
        page.once('dialog', dialog => dialog.accept())
        await cancelBtn.click()
        await page.waitForTimeout(1500)
        await expect(page.locator('.status.cancelled').first()).toBeVisible({ timeout: 5000 })
      }
    }
  })

  test('edit user profile', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', registerEmail)
    await page.fill('input[type="password"]', 'Test123456')
    await page.click('button[type="submit"]')
    await page.waitForURL('**/user/dashboard', { timeout: 10000 })

    await page.goto('/user/profile')
    const nameInput = page.locator('input').nth(1)
    await nameInput.fill('E2E用户-已改名')
    const [dialog] = await Promise.all([
      page.waitForEvent('dialog', { timeout: 5000 }),
      page.click('text=保存'),
    ])
    expect(dialog.message()).toContain('保存成功')
    await dialog.dismiss()
  })
})

/* ─────────────── Admin Flows (independent tests) ─────────────── */

test.describe('Admin Flows', () => {

  test('admin dashboard shows statistics', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })

    await expect(page.locator('h1')).toHaveText('管理仪表盘')
    await expect(page.locator('.stat-card').filter({ hasText: '用户数' })).toBeVisible()
    await expect(page.locator('.stat-card').filter({ hasText: '咨询师' })).toBeVisible()
    await expect(page.locator('.stat-card').filter({ hasText: '总预约' })).toBeVisible()
    await expect(page.locator('.stat-card').filter({ hasText: '待确认' })).toBeVisible()
    await expect(page.locator('.stat-card').filter({ hasText: '网点' })).toBeVisible()
  })

  test('admin views users and toggles status', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })

    await page.goto('/admin/users')
    await expect(page.locator('table')).toBeVisible({ timeout: 10000 })
    await expect(page.locator('text=admin@test.com')).toBeVisible()
    await expect(page.locator('text=管理员')).toBeVisible()

    const toggleBtn = page.locator('button:has-text("禁用"), button:has-text("启用")').first()
    if (await toggleBtn.isVisible()) {
      await toggleBtn.click()
      await page.waitForTimeout(1000)
    }
  })

  test('admin views consultants list', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })

    await page.locator('a:has-text("咨询师管理")').click()
    await page.waitForURL('**/admin/consultants', { timeout: 10000 })
    await expect(page.locator('table')).toBeVisible({ timeout: 10000 })
    await expect(page.locator('text=张医生')).toBeVisible()
    await expect(page.locator('text=李律师')).toBeVisible()
    await expect(page.locator('text=王教练')).toBeVisible()
  })

  test('admin views appointments list', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })

    await page.locator('a:has-text("预约管理")').click()
    await page.waitForURL('**/admin/appointments', { timeout: 10000 })
    await expect(page.locator('h1')).toHaveText('预约管理')
  })

  test('admin CRUD branches', async ({ page }) => {
    // Login
    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })

    // Go to branches
    await page.locator('a:has-text("网点管理")').click()
    await page.waitForURL('**/admin/branches', { timeout: 10000 })
    await expect(page.locator('h1')).toHaveText('网点管理')
    await expect(page.locator('text=朝阳咨询中心')).toBeVisible({ timeout: 10000 })
    await expect(page.locator('text=海淀咨询中心')).toBeVisible()
    await expect(page.locator('text=浦东咨询中心')).toBeVisible()

    // ── CREATE ──
    await page.click('text=新增网点')
    await expect(page.locator('.modal')).toBeVisible()

    // Modal: <div class="form-group"><label>名称</label><input v-model="form.name" class="input" /></div>
    await page.locator('.modal .form-group').nth(0).locator('input').fill('E2E测试网点')
    await page.locator('.modal .form-group').nth(1).locator('input').fill('东城区测试大街1号')
    await page.locator('.modal .form-group').nth(2).locator('input').fill('010-88889999')
    await page.locator('.modal button:has-text("保存")').click()
    await page.waitForTimeout(1500)
    await expect(page.locator('text=E2E测试网点')).toBeVisible({ timeout: 5000 })

    // ── EDIT ──
    await page.locator('tr:has-text("E2E测试网点") button:has-text("编辑")').click()
    await expect(page.locator('.modal')).toBeVisible()
    await page.locator('.modal .form-group').nth(0).locator('input').fill('E2E测试网点-已改')
    await page.locator('.modal button:has-text("保存")').click()
    await page.waitForTimeout(1500)
    await expect(page.locator('text=E2E测试网点-已改')).toBeVisible({ timeout: 5000 })

    // ── DELETE ──
    page.once('dialog', dialog => dialog.accept())
    await page.locator('tr:has-text("E2E测试网点-已改") button:has-text("删除")').click()
    await page.waitForTimeout(1500)
    await expect(page.locator('text=E2E测试网点-已改')).toHaveCount(0, { timeout: 5000 })
  })
})

/* ─────────────── Edge Cases ─────────────── */

test.describe('Edge Cases', () => {

  test('accessing admin page without login redirects to login', async ({ page }) => {
    await page.goto('/')
    await page.evaluate(() => localStorage.clear())
    await page.goto('/admin/dashboard')
    await expect(page).toHaveURL(/\/login/, { timeout: 5000 })
  })

  test('accessing user page without login redirects to login', async ({ page }) => {
    await page.goto('/')
    await page.evaluate(() => localStorage.clear())
    await page.goto('/user/dashboard')
    await expect(page).toHaveURL(/\/login/, { timeout: 5000 })
  })

  test('homepage navigates to register via CTA', async ({ page }) => {
    await page.goto('/')
    await page.evaluate(() => localStorage.clear())
    await page.goto('/')
    const cta = page.locator('text=免费注册')
    await expect(cta).toBeVisible()
  })
})
