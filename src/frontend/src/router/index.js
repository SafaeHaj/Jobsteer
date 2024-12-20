import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/store/user';
import HomeView from '@/views/HomeView.vue';
import AboutView from '@/views/AboutView.vue';
import UploadResumeView from '@/views/UploadResumeView.vue';
import ProfileView from '@/views/ProfileView.vue';
import RecruiterDashboardView from '@/views/RecruiterDashboardView.vue';

const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/about', name: 'about', component: AboutView },
  { path: '/upload-resume', name: 'upload-resume', component: UploadResumeView, meta: { requiresAuth: true, requiredRole: 'jobseeker' } },
  { path: '/profile', name: 'profile', component: ProfileView, meta: { requiresAuth: true } },
  { path: '/recruiter-dashboard', name: 'recruiter-dashboard', component: RecruiterDashboardView, meta: { requiresAuth: true, requiredRole : 'recruiter'}}
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  const user = userStore.user;
  const isAuthenticated = !!user;
  const requiredRole = to.meta.requiredRole;

  if (to.meta.requiresAuth && !isAuthenticated) {
    return next({ name: 'home' });
  }

  if (isAuthenticated && requiredRole && user.role !== requiredRole) {
    return next({ name: 'home' });
  }

  next();
});

export default router;
