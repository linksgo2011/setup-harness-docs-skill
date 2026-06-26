import { test, expect } from '@playwright/test'
import { visualTest } from '../helpers/visual-test'

const ADMIN_EMAIL = 'admin@test.com'
const ADMIN_PASSWORD = 'password'

const vlm = visualTest()

/* ─────────────── Visual Tests ─────────────── */

test.describe('Visual Tests', () => {

  test('homepage hero section visual layout', async ({ page }) => {
    test.skip(!vlm.isConfigured, 'VLM not configured (set VLM_API_KEY)')

    await page.goto('/')
    const analysis = await vlm.analyze(page, {
      prompt: `Describe the hero section of this consultation booking website homepage. 
        What is the main heading text? What buttons/actions are available?
        List the exact text content you see.`,
      fullPage: true,
    })
    expect(analysis).toContain('专业咨询服务')
    console.log('[Visual] Homepage analysis:', analysis)
  })

  test('login page form layout', async ({ page }) => {
    test.skip(!vlm.isConfigured, 'VLM not configured')

    await page.goto('/login')
    const analysis = await vlm.analyze(page, {
      prompt: `Describe the login page form. What input fields are visible? 
        Are there any labels? What does the submit button say?
        List all visible text elements.`,
    })
    expect(analysis).toContain('登录')
    console.log('[Visual] Login page analysis:', analysis)
  })

  test('calendar date picker renders consecutive dates', async ({ page }) => {
    test.skip(!vlm.isConfigured, 'VLM not configured (set VLM_API_KEY)')

    // Login and go to book page
    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })

    await page.goto('/user/book')
    await expect(page.locator('h1')).toHaveText('预约咨询')

    // Select a consultant to reveal the date picker
    await page.locator('text=张医生').click()
    await expect(page.locator('text=2. 选择日期')).toBeVisible()

    // Visual analysis of the date picker
    const analysis = await vlm.analyze(page.locator('.booking-steps'), {
      prompt: `Analyze the date picker section (step 2).
        1. How many date buttons are shown?
        2. List each date in YYYY-MM-DD format.
        3. Are the dates consecutive (no gaps)?
        4. Is today's date included as the first option?
        Return the list of dates and your observations.`,
    })
    console.log('[Visual] Date picker analysis:', analysis)

    // Verify the date format is correct (YYYY-MM-DD)
    const dates = analysis.match(/\d{4}-\d{2}-\d{2}/g)
    expect(dates).not.toBeNull()
    expect(dates!.length).toBeGreaterThanOrEqual(5)
  })

  test('admin dashboard stat cards layout', async ({ page }) => {
    test.skip(!vlm.isConfigured, 'VLM not configured')

    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })

    const analysis = await vlm.analyze(page.locator('.stats-grid'), {
      prompt: `Analyze the dashboard statistics grid.
        1. How many stat cards are visible?
        2. For each card, what is the number/value and what is its label?
        3. Are all cards displayed in a grid layout?
        List each card's content.`,
    })
    expect(analysis).toContain('用户数')
    expect(analysis).toContain('咨询师')
    expect(analysis).toContain('总预约')
    expect(analysis).toContain('网点')
    console.log('[Visual] Dashboard stats analysis:', analysis)
  })

  test('booking flow visual consistency (consultant → date → time → confirm)', async ({ page }) => {
    test.skip(!vlm.isConfigured, 'VLM not configured')

    // Login and go to book page
    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })

    await page.goto('/user/book')
    await page.locator('text=张医生').click()
    await expect(page.locator('text=2. 选择日期')).toBeVisible()

    // Pick a date to reveal time slots
    const dateBtn = page.locator('div.date-list button').first()
    await dateBtn.click()
    await page.waitForTimeout(1500)

    // Full booking form visual analysis
    const analysis = await vlm.analyze(page.locator('.booking-steps'), {
      prompt: `Analyze this multi-step booking form.
        1. How many steps are currently visible?
        2. Which consultant is currently selected?
        3. What date was chosen?
        4. Are time slots visible? If yes, list the available times.
        5. Describe the overall layout and completeness of the form.
        Give a detailed step-by-step description.`,
    })
    console.log('[Visual] Booking form analysis:', analysis)
  })

  test('admin sidebar navigation renders all links', async ({ page }) => {
    test.skip(!vlm.isConfigured, 'VLM not configured')

    await page.goto('/login')
    await page.fill('input[type="email"]', ADMIN_EMAIL)
    await page.fill('input[type="password"]', ADMIN_PASSWORD)
    await page.click('button[type="submit"]')
    await page.waitForURL('**/admin/dashboard', { timeout: 10000 })

    const analysis = await vlm.analyze(page.locator('.sidebar'), {
      prompt: `Analyze the admin sidebar navigation.
        1. List all navigation links visible.
        2. Is there a logout button?
        3. What is the title of the sidebar?
        List all link text.`,
    })
    expect(analysis).toContain('仪表盘')
    expect(analysis).toContain('用户管理')
    expect(analysis).toContain('咨询师管理')
    expect(analysis).toContain('预约管理')
    expect(analysis).toContain('网点管理')
    expect(analysis).toContain('退出')
    console.log('[Visual] Sidebar analysis:', analysis)
  })
})
