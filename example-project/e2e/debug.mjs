import { chromium } from 'playwright';

const BASE = 'http://localhost:5173';

async function run() {
  const browser = await chromium.launch({ headless: false, slowMo: 200 });
  const context = await browser.newContext({ viewport: { width: 1280, height: 800 } });
  const page = await context.newPage();

  // Listen to console errors
  page.on('console', msg => { if (msg.type() === 'error') console.log('CONSOLE ERROR:', msg.text()); });
  page.on('response', resp => {
    if (resp.status() >= 400) console.log(`API ${resp.status()} ${resp.url()}`);
  });

  // 1. Homepage
  await page.goto(BASE, { waitUntil: 'networkidle' });
  await page.screenshot({ path: 'e2e/screenshots/01-homepage.png', fullPage: true });
  console.log('✓ Homepage loaded');

  // 2. Navigate to login
  await page.click('text=登录');
  await page.waitForURL('**/login');
  await page.screenshot({ path: 'e2e/screenshots/02-login.png', fullPage: true });
  console.log('✓ Login page loaded');

  // 3. Login as admin
  await page.fill('input[type="email"], input[name="email"], input[placeholder*="邮箱"]', 'admin@test.com');
  await page.fill('input[type="password"], input[name="password"], input[placeholder*="密码"]', 'password');
  await page.click('button[type="submit"], button:has-text("登录")');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: 'e2e/screenshots/03-after-login.png', fullPage: true });
  console.log('✓ Login submitted, current URL:', page.url());

  // 4. Navigate to /admin/dashboard
  await page.goto(BASE + '/admin/dashboard', { waitUntil: 'networkidle' });
  await page.screenshot({ path: 'e2e/screenshots/04-admin-dashboard.png', fullPage: true });
  console.log('✓ Admin dashboard, current URL:', page.url());

  // 5. Try different pages
  for (const path of ['/admin/users', '/admin/consultants', '/admin/appointments', '/admin/branches']) {
    await page.goto(BASE + path, { waitUntil: 'networkidle' });
    await page.screenshot({ path: `e2e/screenshots/05${path.replace(/\//g, '-')}.png`, fullPage: true });
    console.log(`✓ Page ${path}, URL: ${page.url()}`);
  }

  await browser.close();
  console.log('\nDone! Check e2e/screenshots/ for results.');
}

run().catch(err => { console.error(err); process.exit(1); });
