<template>
  <div>
    <h1>用户管理</h1>
    <div v-if="loading">加载中...</div>
    <table v-else class="table"><thead><tr><th>邮箱</th><th>姓名</th><th>手机</th><th>角色</th><th>状态</th><th>操作</th></tr></thead>
      <tbody><tr v-for="u in users" :key="u.id"><td>{{ u.email }}</td><td>{{ u.name }}</td><td>{{ u.phone }}</td>
        <td>{{ u.role }}</td><td :class="u.status === 'ACTIVE' ? 'active' : 'disabled'">{{ u.status }}</td>
        <td><button class="btn btn-sm" :class="u.status === 'ACTIVE' ? 'btn-danger' : 'btn-primary'" @click="toggle(u.id)">{{ u.status === 'ACTIVE' ? '禁用' : '启用' }}</button></td>
      </tr></tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/request'
import type { User } from '@/types'

const users = ref<User[]>([]); const loading = ref(true)
async function toggle(id: string) { await http.put(`/admin/users/${id}/toggle-status`); await fetch() }
async function fetch() { loading.value = true; try { const r = await http.get('/admin/users'); users.value = r.data } finally { loading.value = false } }
onMounted(fetch)
</script>

<style scoped>
h1 { margin-bottom: 1rem; }
.table { width: 100%; border-collapse: collapse; }
.table th, .table td { text-align: left; padding: 0.75rem; border-bottom: 1px solid var(--gray-200); }
.active { color: #16a34a; font-weight: 500; }
.disabled { color: #dc2626; font-weight: 500; }
.btn-sm { padding: 0.25rem 0.75rem; font-size: 0.8125rem; }
</style>
