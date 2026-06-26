<template>
  <div class="auth-page">
    <div class="card auth-card">
      <h2>登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group"><label>邮箱</label><input v-model="email" type="email" class="input" required placeholder="请输入邮箱" /></div>
        <div class="form-group"><label>密码</label><input v-model="password" type="password" class="input" required placeholder="请输入密码" /></div>
        <p v-if="error" class="error">{{ error }}</p>
        <button type="submit" class="btn btn-primary" style="width:100%" :disabled="loading">{{ loading ? '登录中...' : '登录' }}</button>
      </form>
      <p class="switch">还没有账号？<router-link to="/register">立即注册</router-link></p>
      <router-link to="/" class="back-link">← 返回首页</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()
const email = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function handleLogin() {
  error.value = ''; loading.value = true
  try {
    await auth.login(email.value, password.value)
    router.push(auth.isAdmin ? '/admin/dashboard' : '/user/dashboard')
  } catch (e: any) { error.value = e.response?.data?.errorMessage || '登录失败' }
  finally { loading.value = false }
}
</script>

<style scoped>
.auth-page { display: flex; justify-content: center; align-items: center; min-height: 100vh; background: var(--gray-50); }
.auth-card { width: 100%; max-width: 400px; }
.auth-card h2 { text-align: center; margin-bottom: 1.5rem; }
.error { color: var(--danger); font-size: 0.875rem; margin-bottom: 0.5rem; }
.switch { text-align: center; margin-top: 1rem; font-size: 0.875rem; color: var(--gray-500); }
.back-link { display: block; text-align: center; margin-top: 0.5rem; font-size: 0.875rem; }
</style>
