import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { AuthResponse, User } from '@/types'
import http from '@/utils/request'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref<User | null>(JSON.parse(localStorage.getItem('user') || 'null'))
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ROLE_ADMIN')

  function setAuth(t: string, u: User) {
    token.value = t; user.value = u
    localStorage.setItem('token', t); localStorage.setItem('user', JSON.stringify(u))
  }

  async function login(email: string, password: string) {
    const r = await http.post<AuthResponse>('/auth/login', { email, password })
    setAuth(r.data.token, r.data.user)
    return r.data
  }

  async function register(email: string, password: string, name: string, phone?: string) {
    const r = await http.post<AuthResponse>('/auth/register', { email, password, name, phone })
    setAuth(r.data.token, r.data.user)
    return r.data
  }

  function logout() {
    token.value = ''; user.value = null
    localStorage.removeItem('token'); localStorage.removeItem('user')
  }

  return { token, user, isLoggedIn, isAdmin, login, register, logout }
})
