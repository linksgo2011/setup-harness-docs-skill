<template>
  <div>
    <h1>我的预约</h1>
    <div v-if="loading">加载中...</div>
    <div v-else-if="appointments.length === 0" class="empty"><p>暂无预约记录</p><router-link to="/user/book" class="btn btn-primary">预约咨询</router-link></div>
    <div v-else class="appointment-list">
      <div v-for="a in appointments" :key="a.id" class="card appointment-card">
        <div>
          <span class="status" :class="a.status.toLowerCase()">{{ label(a.status) }}</span>
          <p class="date">{{ a.appointmentDate }} {{ a.startTime }}-{{ a.endTime }}</p>
          <p class="meta">咨询师ID: {{ a.consultantId.substring(0,8) }}... | 时长: {{ a.durationMinutes }}分钟</p>
          <p v-if="a.note" class="note">备注: {{ a.note }}</p>
          <p v-if="a.cancelReason" class="cancel">取消原因: {{ a.cancelReason }}</p>
        </div>
        <button v-if="a.status === 'PENDING' || a.status === 'CONFIRMED'" class="btn btn-danger btn-sm" @click="doCancel(a.id)">取消</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import http from '@/utils/request'
import type { Appointment } from '@/types'

const router = useRouter()
const auth = useAuthStore()
if (!auth.isLoggedIn) router.push('/login')

const appointments = ref<Appointment[]>([])
const loading = ref(true)

function label(s: string) {
  const m: Record<string, string> = { PENDING: '待确认', CONFIRMED: '已确认', COMPLETED: '已完成', CANCELLED: '已取消' }
  return m[s] || s
}

async function doCancel(id: string) {
  if (!confirm('确定取消？')) return
  await http.put(`/appointments/${id}/cancel`, { reason: '用户取消' })
  await fetch()
}

async function fetch() {
  loading.value = true
  try { const r = await http.get('/appointments'); appointments.value = r.data } finally { loading.value = false }
}

onMounted(fetch)
</script>

<style scoped>
h1 { margin-bottom: 1.5rem; }
.empty { text-align: center; padding: 3rem; color: var(--gray-500); }
.appointment-list { display: flex; flex-direction: column; gap: 1rem; }
.appointment-card { display: flex; justify-content: space-between; align-items: flex-start; }
.status { display: inline-block; padding: 0.125rem 0.5rem; border-radius: 1rem; font-size: 0.75rem; font-weight: 500; margin-bottom: 0.5rem; }
.pending { background: #FEF3C7; color: #92400E; }
.confirmed { background: #DBEAFE; color: #1E40AF; }
.completed { background: #D1FAE5; color: #065F46; }
.cancelled { background: #FEE2E2; color: #991B1B; }
.date { font-weight: 600; }
.meta { font-size: 0.875rem; color: var(--gray-500); }
.note { font-size: 0.875rem; color: var(--gray-600); }
.cancel { font-size: 0.875rem; color: var(--danger); }
.btn-sm { padding: 0.375rem 0.75rem; font-size: 0.8125rem; }
</style>
