import { defineStore } from 'pinia'
import axiosClient from '@/lib/axios-client.ts'
import router from '@/router'
import { useCookies } from '@vueuse/integrations/useCookies'
import { jwtDecode } from 'jwt-decode'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
	let accessToken
	let isLoggedIn = ref<boolean>(false)

	const cookies = useCookies()

	const login = async (
		username: string,
		password: string,
		rememberMe: boolean
	) => {
		cookies.remove('refreshToken')

		try {
			const response = await axiosClient.post('/auth/login', {
				username: username,
				password: password,
			})

			const data = response.data

			accessToken = data.accessToken

			const refreshToken = data.refreshToken

			const refreshTokenExpiration = jwtDecode(refreshToken)?.exp
			const defaultLoginDur = new Date(Date.now() + 60 * 60 * 1000)

			const expirationDate = rememberMe
				? refreshTokenExpiration !== undefined
					? new Date(refreshTokenExpiration * 1000)
					: defaultLoginDur
				: defaultLoginDur

			cookies.set('refreshToken', refreshToken, {
				expires: expirationDate,
			})

			await router.push({ name: 'home' })
		} catch (e) {
			console.log(e)
		}
	}

	const register = async (
		username: string,
		email: string | null,
		password: string
	) => {
		try {
			await axiosClient.post('/auth/register', {
				username: username,
				email: email,
				password: password,
			})

			await router.push({ name: 'login' })
		} catch (e) {
			console.log(e)
		}
	}

	const getRefreshToken = () => {
		const refreshToken = useCookies().get('refreshToken')

		if (!refreshToken)
			return ''

		return refreshToken
	}

	const isLoggedInCheck = async () => {
		const refreshToken = getRefreshToken()

		if (!refreshToken) {
			return
		}

		try {
			const response = await axiosClient.post('/auth/validate-refresh-token', {
				refreshToken: refreshToken,
			})

			isLoggedIn.value = !!response.data.isValidToken
		} catch (e) {
			console.log(e)
		}
	}

	return { accessToken, isLoggedIn, login, register, isLoggedInCheck }
})
