import HomePage from '@/pages/HomePage.vue'
import LoginPage from '@/pages/auth/LoginPage.vue'
import RegisterPage from '@/pages/auth/RegisterPage.vue'
import RequiredAuthPage from '@/pages/RequiredAuthPage.vue'

const routes = [
	{
		path: '/',
		name: 'home',
		component: HomePage,
		meta: {
			title: 'Home',
			requiresAuth: false,
		},
	},
	{
		path: '/game',
		name: 'game',
		component: RequiredAuthPage,
		meta: {
			title: 'Game',
			requiresAuth: true,
		},
	},
	{
		path: '/auth',
		children: [
			{
				path: 'login',
				name: 'login',
				component: LoginPage,
				meta: {
					title: 'Login',
					requiresAuth: false,
				},
			},
			{
				path: 'register',
				name: 'register',
				component: RegisterPage,
				meta: {
					title: 'Game',
					requiresAuth: true,
				},
			},
		],
	},
]

export default routes
