<template>
  <div>
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1rem">
      <h1>网点管理</h1>
      <button class="btn btn-primary" @click="showForm = true; editing = null; form = {name:'',address:'',phone:''}">新增网点</button>
    </div>
    <div v-if="loading">加载中...</div>
    <table v-else class="table"><thead><tr><th>名称</th><th>地址</th><th>电话</th><th>状态</th><th>操作</th></tr></thead>
      <tbody><tr v-for="b in branches" :key="b.id"><td>{{ b.name }}</td><td>{{ b.address }}</td><td>{{ b.phone }}</td>
        <td :class="b.status === 'ACTIVE' ? 'active' : 'disabled'">{{ b.status }}</td>
        <td><button class="btn btn-sm btn-secondary" @click="edit(b)">编辑</button><button class="btn btn-sm btn-danger" style="margin-left:0.25rem" @click="doDelete(b.id)">删除</button></td>
      </tr></tbody>
    </table>
    <div v-if="showForm" class="modal"><div class="modal-content card">
      <h3>{{ editing ? '编辑网点' : '新增网点' }}</h3>
      <div class="form-group"><label>名称</label><input v-model="form.name" class="input" /></div>
      <div class="form-group"><label>地址</label><input v-model="form.address" class="input" /></div>
      <div class="form-group"><label>电话</label><input v-model="form.phone" class="input" /></div>
      <div style="display:flex;gap:0.5rem;margin-top:1rem">
        <button class="btn btn-primary" @click="save" :disabled="saving">{{ saving ? '保存中...' : '保存' }}</button>
        <button class="btn btn-secondary" @click="showForm = false">取消</button>
      </div>
    </div></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/request'
import type { Branch } from '@/types'

const branches = ref<Branch[]>([]); const loading = ref(true)
const showForm = ref(false); const editing = ref<string | null>(null); const saving = ref(false)
const form = ref({ name: '', address: '', phone: '' })

function edit(b: Branch) { editing.value = b.id; form.value = { name: b.name, address: b.address, phone: b.phone }; showForm.value = true }

async function save() {
  saving.value = true
  try {
    if (editing.value) { await http.put(`/admin/branches/${editing.value}`, form.value) }
    else { await http.post('/admin/branches', form.value) }
    showForm.value = false; await fetch()
  } catch (e: any) { alert(e.response?.data?.errorMessage || '操作失败') }
  finally { saving.value = false }
}

async function doDelete(id: string) {
  if (!confirm('确定删除？')) return; await http.delete(`/admin/branches/${id}`); await fetch()
}

async function fetch() { loading.value = true; try { const r = await http.get('/admin/branches'); branches.value = r.data } finally { loading.value = false } }
onMounted(fetch)
</script>

<style scoped>
h1 { margin: 0; }
.table { width: 100%; border-collapse: collapse; }
.table th, .table td { text-align: left; padding: 0.75rem; border-bottom: 1px solid var(--gray-200); }
.active { color: #16a34a; font-weight: 500; }
.disabled { color: #dc2626; font-weight: 500; }
.modal { position: fixed; inset: 0; background: rgba(0,0,0,0.4); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal-content { width: 100%; max-width: 480px; }
.btn-sm { padding: 0.25rem 0.75rem; font-size: 0.8125rem; }
</style>
