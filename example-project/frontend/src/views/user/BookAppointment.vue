<template>
  <div>
    <h1>预约咨询</h1>
    <div class="booking-steps">
      <div class="card step">
        <h3>1. 选择咨询师</h3>
        <div class="consultant-list">
          <div v-for="c in consultants" :key="c.id" class="consultant-option"
               :class="{ selected: selectedConsultant === c.id }" @click="pickConsultant(c.id)">
            <strong>{{ c.name }}</strong> <p>{{ c.title }}</p>
          </div>
        </div>
      </div>
      <div v-if="selectedConsultant" class="card step">
        <h3>2. 选择日期</h3>
        <div class="date-list">
          <button v-for="d in dates" :key="d" class="btn"
                  :class="selectedDate === d ? 'btn-primary' : 'btn-secondary'" @click="pickDate(d)">{{ d }}</button>
        </div>
      </div>
      <div v-if="selectedDate && slots.length" class="card step">
        <h3>3. 选择时间</h3>
        <div class="slot-list">
          <button v-for="s in slots" :key="s.startTime" class="btn"
                  :class="selectedSlot === s.startTime ? 'btn-primary' : 'btn-secondary'"
                  @click="selectedSlot = s.startTime">{{ s.startTime }}-{{ s.endTime }}</button>
        </div>
      </div>
      <div v-if="selectedSlot" class="card step">
        <h3>4. 备注</h3>
        <textarea v-model="note" class="input" rows="3" placeholder="描述您的问题..."></textarea>
        <button class="btn btn-primary" style="margin-top:1rem" @click="submit" :disabled="submitting">
          {{ submitting ? '提交中...' : '确认预约' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import http from '@/utils/request'
import { getWeekDates } from '@/utils/date'
import type { Consultant, TimeSlot } from '@/types'

const router = useRouter()
const auth = useAuthStore()
if (!auth.isLoggedIn) router.push('/login')

const consultants = ref<Consultant[]>([])
const selectedConsultant = ref('')
const selectedDate = ref('')
const selectedSlot = ref('')
const note = ref('')
const slots = ref<TimeSlot[]>([])
const dates = getWeekDates()
const submitting = ref(false)

onMounted(async () => { const r = await http.get('/consultants'); consultants.value = r.data })

watch(selectedConsultant, () => { selectedDate.value = ''; selectedSlot.value = ''; slots.value = [] })
watch(selectedDate, async (d) => {
  if (!d) return; selectedSlot.value = ''
  const r = await http.get(`/consultants/${selectedConsultant.value}/slots`, { params: { date: d } }); slots.value = r.data
})

function pickConsultant(id: string) { selectedConsultant.value = id }
function pickDate(d: string) { selectedDate.value = d }

async function submit() {
  submitting.value = true
  try {
    await http.post('/appointments', {
      consultantId: selectedConsultant.value, date: selectedDate.value,
      startTime: selectedSlot.value,
      endTime: `${String(Number(selectedSlot.value.split(':')[0]) + 1).padStart(2, '0')}:00`,
      note: note.value,
    })
    router.push('/user/appointments')
  } catch (e: any) { alert(e.response?.data?.errorMessage || '预约失败') }
  finally { submitting.value = false }
}
</script>

<style scoped>
h1 { margin-bottom: 1.5rem; }
.booking-steps { display: flex; flex-direction: column; gap: 1rem; max-width: 600px; }
.step h3 { margin-bottom: 0.75rem; }
.consultant-list { display: flex; flex-direction: column; gap: 0.5rem; }
.consultant-option { padding: 0.75rem; border: 1px solid var(--gray-200); border-radius: 0.375rem; cursor: pointer; }
.consultant-option:hover { border-color: var(--primary); }
.consultant-option.selected { border-color: var(--primary); background: rgba(79,70,229,0.05); }
.date-list, .slot-list { display: flex; flex-wrap: wrap; gap: 0.5rem; }
</style>
