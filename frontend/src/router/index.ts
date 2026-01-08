import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import LoginPage from '@/pages/auth/LoginPage.vue'
import RegisterPage from '@/pages/auth/RegisterPage.vue'

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: '/',
			name: 'home',
			component: HomePage,
		},
		{
			path: '/auth',
			children: [
				{
					path: 'login',
					name: 'login',
					component: LoginPage,
				},
				{
					path: 'register',
					name: 'register',
					component: RegisterPage,
				},
			],
		},
	],
})

export default router
