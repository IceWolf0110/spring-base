import { createRouter, createWebHistory, type NavigationGuardNext, type RouteLocationNormalizedGeneric } from 'vue-router'
import routes from '@/router/router.ts'

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
		document.title = (to.meta?.title as string) ?? 'test app'

		if (to.meta?.requiresAuth) {
			next({ name: 'login' })
		}

		next()
	},
)

export default router
