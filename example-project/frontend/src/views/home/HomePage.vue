<template>
  <div>
    <AppHeader />
    <section class="hero">
      <div class="container hero-content">
        <h1>{{ info.title }}</h1>
        <p>{{ info.description }}</p>
        <div class="hero-actions">
          <router-link v-if="!auth.isLoggedIn" to="/register" class="btn btn-primary btn-lg">免费注册</router-link>
          <router-link to="/user/book" class="btn btn-secondary btn-lg">立即预约</router-link>
        </div>
      </div>
    </section>
    <footer class="footer"><div class="container"><p>&copy; 2026 咨询预约平台</p></div></footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import AppHeader from '@/components/layout/AppHeader.vue'
import http from '@/utils/request'

const auth = useAuthStore()
const info = ref({ title: '', description: '' })
onMounted(async () => {
  try { const r = await http.get('/home/info'); info.value = r.data } catch {}
})
</script>

<style scoped>
.hero { background: linear-gradient(135deg, var(--primary), var(--primary-light)); color: white; padding: 4rem 0; }
.hero-content { text-align: center; }
.hero h1 { font-size: 2.5rem; margin-bottom: 1rem; }
.hero p { font-size: 1.125rem; opacity: 0.9; max-width: 600px; margin: 0 auto 2rem; }
.hero-actions { display: flex; gap: 1rem; justify-content: center; }
.btn-lg { padding: 0.75rem 2rem; font-size: 1rem; }
.footer { background: var(--gray-800); color: var(--gray-400); padding: 2rem 0; text-align: center; }
</style>
