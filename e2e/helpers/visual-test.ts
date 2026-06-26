import { type Locator, type Page } from '@playwright/test'

interface VlmConfig {
  baseUrl: string
  model: string
  apiKey: string
  provider: 'openai' | 'anthropic'
}

interface AnalyzeOptions {
  prompt: string
  element?: Locator
  fullPage?: boolean
}

function getConfig(): VlmConfig | null {
  const apiKey = process.env.VLM_API_KEY
  if (!apiKey) return null

  const raw = (process.env.VLM_BASE_URL || 'https://api.openai.com/v1').replace(/\/+$/, '')
  const model = process.env.VLM_MODEL || 'gpt-4o-mini'

  const provider: 'openai' | 'anthropic' =
    raw.includes('anthropic') || process.env.VLM_PROVIDER === 'anthropic'
      ? 'anthropic'
      : 'openai'

  // normalize baseUrl: strip trailing /v1 for anthropic, then provider appends /v1/messages
  const baseUrl = provider === 'anthropic' ? raw.replace(/\/v1$/, '') : raw

  return { baseUrl, model, apiKey, provider }
}

async function screenshot(pageOrLocator: Page | Locator, options: AnalyzeOptions): Promise<string> {
  let buf: Buffer
  if (options.element) {
    buf = await options.element.screenshot()
  } else if ('context' in pageOrLocator) {
    buf = await (pageOrLocator as Page).screenshot({ fullPage: options.fullPage })
  } else {
    buf = await (pageOrLocator as Locator).screenshot()
  }
  return buf.toString('base64')
}

async function vlmFetch(url: string, init: RequestInit, retries = 3): Promise<Response> {
  for (let attempt = 1; attempt <= retries; attempt++) {
    const resp = await fetch(url, init)
    if (resp.ok) return resp
    const body = await resp.text()
    // retry on 429 (rate limit) and 5xx (server error)
    if ((resp.status === 429 || resp.status >= 500) && attempt < retries) {
      const delay = 1000 * Math.pow(2, attempt) + Math.random() * 1000
      console.log(`  [VLM] API error ${resp.status}, retrying in ${Math.round(delay)}ms (attempt ${attempt}/${retries})`)
      await new Promise(r => setTimeout(r, delay))
      continue
    }
    throw new Error(`VLM API error ${resp.status}: ${body}`)
  }
  throw new Error('VLM API max retries exceeded')
}

async function analyzeWithOpenAI(
  imageBase64: string, prompt: string, config: VlmConfig
): Promise<string> {
  const resp = await vlmFetch(`${config.baseUrl}/chat/completions`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${config.apiKey}`,
    },
    body: JSON.stringify({
      model: config.model,
      messages: [
        {
          role: 'user',
          content: [
            { type: 'text', text: prompt },
            { type: 'image_url', image_url: { url: `data:image/png;base64,${imageBase64}`, detail: 'high' } },
          ],
        },
      ],
      max_tokens: 1024,
    }),
  })
  const data = await resp.json()
  return data.choices?.[0]?.message?.content || ''
}

async function analyzeWithAnthropic(
  imageBase64: string, prompt: string, config: VlmConfig
): Promise<string> {
  const resp = await vlmFetch(`${config.baseUrl}/v1/messages`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'x-api-key': config.apiKey,
      'anthropic-version': '2023-06-01',
    },
    body: JSON.stringify({
      model: config.model,
      max_tokens: 1024,
      messages: [
        {
          role: 'user',
          content: [
            {
              type: 'image',
              source: { type: 'base64', media_type: 'image/png', data: imageBase64 },
            },
            { type: 'text', text: prompt },
          ],
        },
      ],
    }),
  })
  const data = await resp.json()
  const textBlock = data.content?.find((c: any) => c.type === 'text')
  return textBlock?.text || ''
}

export function visualTest() {
  const config = getConfig()

  async function analyze(pageOrLocator: Page | Locator, options: AnalyzeOptions): Promise<string> {
    if (!config) {
      throw new Error(
        'VLM not configured. Set VLM_API_KEY in environment.\n' +
        '  Examples:\n' +
        '    OpenAI:              VLM_API_KEY=sk-...  (default model: gpt-4o-mini)\n' +
        '    Ollama (local):      VLM_BASE_URL=http://localhost:11434/v1 VLM_MODEL=llava VLM_API_KEY=ollama\n' +
        '    Anthropic/Claude:    VLM_BASE_URL=https://api.anthropic.com VLM_MODEL=claude-3-5-sonnet-latest VLM_API_KEY=sk-...\n' +
        '    Zhipu/GLM-5V-Turbo:  VLM_BASE_URL=https://open.bigmodel.cn/api/anthropic VLM_MODEL=glm-5v-turbo VLM_PROVIDER=anthropic VLM_API_KEY=your-key\n' +
        '    ⚠️ DeepSeek API does NOT support vision/image input (tested via both OpenAI & Anthropic formats).'
      )
    }

    const b64 = await screenshot(pageOrLocator, options)

    if (config.provider === 'anthropic') {
      return analyzeWithAnthropic(b64, options.prompt, config)
    }
    return analyzeWithOpenAI(b64, options.prompt, config)
  }

  async function expectPrompt(
    pageOrLocator: Page | Locator,
    options: AnalyzeOptions & { contains?: string[]; notContains?: string[] }
  ): Promise<string> {
    const text = await analyze(pageOrLocator, options)
    const errors: string[] = []
    for (const substr of options.contains || []) {
      if (!text.includes(substr)) {
        errors.push(`Expected analysis to contain "${substr}", but got: "${text}"`)
      }
    }
    for (const substr of options.notContains || []) {
      if (text.includes(substr)) {
        errors.push(`Expected analysis NOT to contain "${substr}", but got: "${text}"`)
      }
    }
    if (errors.length > 0) throw new Error(errors.join('\n'))
    return text
  }

  return { analyze, expectPrompt, isConfigured: config !== null }
}
