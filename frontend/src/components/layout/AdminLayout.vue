<template>
  <div class="admin-layout">
    <aside class="sidebar">
      <h2>管理后台</h2>
      <nav><router-link v-for="l in links" :key="l.path" :to="l.path" class="nav-link">{{ l.label }}</router-link></nav>
      <button class="btn btn-sm" style="margin-top:auto" @click="auth.logout()">退出</button>
    </aside>
    <main class="main"><router-view /></main>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
const auth = useAuthStore()
const router = useRouter()
if (!auth.isLoggedIn) router.push('/login')
const links = [
  { path: '/admin/dashboard', label: '仪表盘' },
  { path: '/admin/users', label: '用户管理' },
  { path: '/admin/consultants', label: '咨询师管理' },
  { path: '/admin/appointments', label: '预约管理' },
  { path: '/admin/branches', label: '网点管理' },
]
</script>

<style scoped>
.admin-layout { display: flex; min-height: 100vh; }
.sidebar { width: 220px; background: #1e293b; color: #fff; padding: 1.5rem; display: flex; flex-direction: column; }
.sidebar h2 { font-size: 1.125rem; margin-bottom: 1.5rem; }
.nav-link { display: block; padding: 0.5rem 0.75rem; color: #cbd5e1; text-decoration: none; border-radius: 0.25rem; margin-bottom: 0.25rem; }
.nav-link:hover, .nav-link.router-link-active { background: #334155; color: #fff; }
.main { flex: 1; padding: 2rem; background: #f8fafc; }
</style>
