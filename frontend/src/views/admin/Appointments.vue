<template>
  <div>
    <h1>预约管理</h1>
    <div v-if="loading">加载中...</div>
    <table v-else class="table"><thead><tr><th>ID</th><th>咨询师ID</th><th>日期</th><th>时间</th><th>状态</th><th>备注</th></tr></thead>
      <tbody><tr v-for="a in apps" :key="a.id"><td>{{ a.id.substring(0,8) }}...</td><td>{{ a.consultantId.substring(0,8) }}...</td>
        <td>{{ a.appointmentDate }}</td><td>{{ a.startTime }}-{{ a.endTime }}</td><td><span class="status" :class="a.status.toLowerCase()">{{ a.status }}</span></td><td>{{ a.note }}</td>
      </tr></tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/request'
import type { Appointment } from '@/types'

const apps = ref<Appointment[]>([]); const loading = ref(true)
onMounted(async () => { try { const r = await http.get('/admin/appointments'); apps.value = r.data } finally { loading.value = false } })
</script>

<style scoped>
h1 { margin-bottom: 1rem; }
.table { width: 100%; border-collapse: collapse; }
.table th, .table td { text-align: left; padding: 0.75rem; border-bottom: 1px solid var(--gray-200); }
.status { display: inline-block; padding: 0.125rem 0.5rem; border-radius: 1rem; font-size: 0.75rem; font-weight: 500; }
.pending { background: #FEF3C7; color: #92400E; }
.confirmed { background: #DBEAFE; color: #1E40AF; }
.completed { background: #D1FAE5; color: #065F46; }
.cancelled { background: #FEE2E2; color: #991B1B; }
</style>
