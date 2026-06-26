<template>
  <div>
    <h1>咨询师管理</h1>
    <div v-if="loading">加载中...</div>
    <table v-else class="table"><thead><tr><th>姓名</th><th>职称</th><th>专长</th><th>网点ID</th><th>状态</th><th>排序</th></tr></thead>
      <tbody><tr v-for="c in consultants" :key="c.id"><td>{{ c.name }}</td><td>{{ c.title }}</td>
        <td>{{ c.specialties }}</td><td>{{ c.branchId ? c.branchId.substring(0,8)+'...' : '-' }}</td>
        <td :class="c.status === 'ACTIVE' ? 'active' : 'disabled'">{{ c.status }}</td><td>{{ c.sortOrder }}</td>
      </tr></tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/request'
import type { Consultant } from '@/types'

const consultants = ref<Consultant[]>([]); const loading = ref(true)
onMounted(async () => { try { const r = await http.get('/admin/consultants'); consultants.value = r.data } finally { loading.value = false } })
</script>

<style scoped>
h1 { margin-bottom: 1rem; }
.table { width: 100%; border-collapse: collapse; }
.table th, .table td { text-align: left; padding: 0.75rem; border-bottom: 1px solid var(--gray-200); }
.active { color: #16a34a; font-weight: 500; }
.disabled { color: #dc2626; font-weight: 500; }
</style>
