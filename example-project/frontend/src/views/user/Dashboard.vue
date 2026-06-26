<template>
  <div>
    <h1>用户中心</h1>
    <div class="stats-grid">
      <div class="card stat-card"><h3>{{ total }}</h3><p>总预约</p></div>
      <div class="card stat-card"><h3>{{ pending }}</h3><p>待确认</p></div>
      <div class="card stat-card"><h3>{{ completed }}</h3><p>已完成</p></div>
    </div>
    <div class="actions" style="margin-top:1rem">
      <router-link to="/user/book" class="btn btn-primary">预约咨询</router-link>
      <router-link to="/user/appointments" class="btn btn-secondary" style="margin-left:0.5rem">我的预约</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import http from '@/utils/request'
import type { Appointment } from '@/types'

const router = useRouter()
const auth = useAuthStore()
if (!auth.isLoggedIn) router.push('/login')

const appointments = ref<Appointment[]>([])
const total = computed(() => appointments.value.length)
const pending = computed(() => appointments.value.filter(a => a.status === 'PENDING').length)
const completed = computed(() => appointments.value.filter(a => a.status === 'COMPLETED').length)

onMounted(async () => { const r = await http.get('/appointments'); appointments.value = r.data })
</script>

<style scoped>
h1 { margin-bottom: 1.5rem; }
.stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 1rem; }
.stat-card { text-align: center; }
.stat-card h3 { font-size: 2rem; color: var(--primary); }
.stat-card p { color: var(--gray-500); font-size: 0.875rem; }
</style>
