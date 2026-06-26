import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: () => import('@/views/home/HomePage.vue') },
    { path: '/login', name: 'login', component: () => import('@/views/auth/Login.vue') },
    { path: '/register', name: 'register', component: () => import('@/views/auth/Register.vue') },
    {
      path: '/user',
      component: () => import('@/components/layout/UserLayout.vue'),
      children: [
        { path: '', redirect: '/user/dashboard' },
        { path: 'dashboard', component: () => import('@/views/user/Dashboard.vue') },
        { path: 'appointments', component: () => import('@/views/user/MyAppointments.vue') },
        { path: 'book', component: () => import('@/views/user/BookAppointment.vue') },
        { path: 'profile', component: () => import('@/views/user/Profile.vue') },
      ],
    },
    {
      path: '/admin',
      component: () => import('@/components/layout/AdminLayout.vue'),
      children: [
        { path: '', redirect: '/admin/dashboard' },
        { path: 'dashboard', component: () => import('@/views/admin/Dashboard.vue') },
        { path: 'appointments', component: () => import('@/views/admin/Appointments.vue') },
        { path: 'consultants', component: () => import('@/views/admin/Consultants.vue') },
        { path: 'users', component: () => import('@/views/admin/Users.vue') },
        { path: 'branches', component: () => import('@/views/admin/Branches.vue') },
      ],
    },
  ],
})

export default router
