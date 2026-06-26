<template>
  <div>
    <header class="header">
      <div class="container header-inner">
        <router-link to="/" class="logo">咨询预约平台</router-link>
        <nav class="nav">
          <router-link to="/">首页</router-link>
          <template v-if="auth.isLoggedIn">
            <router-link to="/user/dashboard">用户中心</router-link>
            <router-link v-if="auth.isAdmin" to="/admin/dashboard">管理后台</router-link>
            <span class="user-name">{{ auth.user?.name }}</span>
            <button class="btn btn-secondary" @click="auth.logout(); router.push('/')">退出</button>
          </template>
          <template v-else>
            <router-link to="/login">登录</router-link>
            <router-link to="/register" class="btn btn-primary">注册</router-link>
          </template>
        </nav>
      </div>
    </header>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
const auth = useAuthStore()
const router = useRouter()
</script>

<style scoped>
.header { background: white; border-bottom: 1px solid var(--gray-200); position: sticky; top: 0; z-index: 100; }
.header-inner { display: flex; align-items: center; justify-content: space-between; height: 3.5rem; }
.logo { font-size: 1.25rem; font-weight: 700; color: var(--primary); }
.nav { display: flex; align-items: center; gap: 1rem; }
.user-name { color: var(--gray-600); font-size: 0.875rem; }
</style>
