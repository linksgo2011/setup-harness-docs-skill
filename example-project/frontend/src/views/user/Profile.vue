<template>
  <div>
    <h1>个人信息</h1>
    <div class="card" style="max-width:500px">
      <div class="form-group"><label>邮箱</label><input :value="auth.user?.email" class="input" disabled /></div>
      <div class="form-group"><label>姓名</label><input v-model="name" class="input" /></div>
      <div class="form-group"><label>手机号</label><input v-model="phone" class="input" /></div>
      <p v-if="err" class="error">{{ err }}</p>
      <button class="btn btn-primary" @click="save" :disabled="saving">{{ saving ? '保存中...' : '保存' }}</button>
    </div>
    <h2 style="margin-top:2rem">修改密码</h2>
    <div class="card" style="max-width:500px">
      <div class="form-group"><label>原密码</label><input v-model="oldPw" type="password" class="input" /></div>
      <div class="form-group"><label>新密码</label><input v-model="newPw" type="password" class="input" minlength="8" /></div>
      <p v-if="pwErr" class="error">{{ pwErr }}</p>
      <button class="btn btn-primary" @click="changePw" :disabled="changing">{{ changing ? '修改中...' : '修改密码' }}</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import http from '@/utils/request'

const router = useRouter()
const auth = useAuthStore()
if (!auth.isLoggedIn) router.push('/login')

const name = ref(auth.user?.name || '')
const phone = ref(auth.user?.phone || '')
const err = ref(''); const saving = ref(false)
const oldPw = ref(''); const newPw = ref(''); const pwErr = ref(''); const changing = ref(false)

async function save() {
  saving.value = true; err.value = ''
  try { const r = await http.put('/users/me', { name: name.value, phone: phone.value }); auth.user = r.data; localStorage.setItem('user', JSON.stringify(r.data)); alert('保存成功') }
  catch (e: any) { err.value = e.response?.data?.errorMessage || '保存失败' }
  finally { saving.value = false }
}

async function changePw() {
  if (newPw.value.length < 8) { pwErr.value = '密码至少8位'; return }
  changing.value = true; pwErr.value = ''
  try { await http.put('/users/me/password', { oldPassword: oldPw.value, newPassword: newPw.value }); alert('密码修改成功'); oldPw.value = ''; newPw.value = '' }
  catch (e: any) { pwErr.value = e.response?.data?.errorMessage || '修改失败' }
  finally { changing.value = false }
}
</script>

<style scoped>
h1 { margin-bottom: 1.5rem; } h2 { margin-bottom: 1rem; }
.card { margin-bottom: 1rem; }
.error { color: var(--danger); font-size: 0.875rem; margin-bottom: 0.5rem; }
</style>
