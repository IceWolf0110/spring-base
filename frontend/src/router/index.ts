import { createRouter, createWebHistory, type NavigationGuardNext, type RouteLocationNormalizedGeneric } from 'vue-router'
import routes from '@/router/router.ts'
import { useAuthStore } from '@/stores/auth.ts'

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: routes,
})

router.beforeEach(
	async (
		to: RouteLocationNormalizedGeneric,
		_: RouteLocationNormalizedGeneric,
		next: NavigationGuardNext,
	): Promise<void> => {
		const store = useAuthStore()
		await store.isLoggedInCheck()

		const isLoggedIn = store.isLoggedIn

		if (to.meta?.requiresAuth && !isLoggedIn) {
			return next({ name: 'login' })
		}

		if (to.name == 'login' && isLoggedIn) {
			return next({ name: 'home' })
		}

		next()
	},
)

router.afterEach((to) => {
	document.title = (to.meta?.title as string) ?? 'test app'
})

export default router
