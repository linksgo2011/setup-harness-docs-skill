import axios from 'axios'

const http = axios.create({
  baseURL: '/api/v1',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' },
})

http.interceptors.request.use(cfg => {
  const token = localStorage.getItem('token')
  if (token) cfg.headers.Authorization = `Bearer ${token}`
  return cfg
})

http.interceptors.response.use(
  r => r,
  e => {
    if (e.response?.status === 401) { localStorage.removeItem('token'); localStorage.removeItem('user'); window.location.href = '/login' }
    return Promise.reject(e)
  }
)

export default http
