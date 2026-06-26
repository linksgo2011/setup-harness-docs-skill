export interface User {
  id: string; email: string; name: string; phone: string; role: string; status: string
}
export interface AuthResponse { token: string; user: User }
export interface Appointment {
  id: string; userId: string; consultantId: string; branchId: string;
  appointmentDate: string; startTime: string; endTime: string;
  durationMinutes: number; status: string; note: string; cancelReason: string
}
export interface Consultant {
  id: string; name: string; title: string; bio: string; specialties: string;
  avatar: string; branchId: string; status: string; sortOrder: number
}
export interface Branch {
  id: string; name: string; address: string; phone: string; status: string
}
export interface TimeSlot { startTime: string; endTime: string }
