<template>
  <div>
    <h1>管理仪表盘</h1>
    <div class="stats-grid">
      <div class="card stat-card"><h3>{{ stats.totalUsers }}</h3><p>用户数</p></div>
      <div class="card stat-card"><h3>{{ stats.totalConsultants }}</h3><p>咨询师</p></div>
      <div class="card stat-card"><h3>{{ stats.totalAppointments }}</h3><p>总预约</p></div>
      <div class="card stat-card"><h3>{{ stats.pendingAppointments }}</h3><p>待确认</p></div>
      <div class="card stat-card"><h3>{{ stats.totalBranches }}</h3><p>网点</p></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/request'

const stats = ref<any>({})
onMounted(async () => { const r = await http.get('/admin/dashboard'); stats.value = r.data })
</script>

<style scoped>
h1 { margin-bottom: 1.5rem; }
.stats-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)); gap: 1rem; }
.stat-card { text-align: center; }
.stat-card h3 { font-size: 2rem; color: var(--primary); }
.stat-card p { color: var(--gray-500); }
</style>
