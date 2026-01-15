import { defineStore } from 'pinia'
import axiosClient from '@/lib/axios-client.ts'
import router from '@/router'
import { useCookies } from '@vueuse/integrations/useCookies'
import { ref } from 'vue'
import { jwtDecode } from 'jwt-decode'

export const useAuthStore = defineStore('auth', () => {
	const token = ref<string|null>(null)

	const cookies = useCookies()

	const login = async (
		username: string,
		password: string,
		rememberMe: boolean
	) => {
		try {
			const response = await axiosClient.post('/auth/login', {
				username: username,
				password: password,
			})

			const data = response.data

			const accessToken = data.accessToken
			const refreshToken = data.refreshToken

			const refreshTokenExpiration = jwtDecode(refreshToken)?.exp
			const oneHourFromLoginTime = new Date(Date.now() + 60 * 60 * 1000)

			const expirationDate = rememberMe
				? refreshTokenExpiration !== undefined
					? new Date(refreshTokenExpiration * 1000)
					: oneHourFromLoginTime
				: oneHourFromLoginTime

			cookies.set('refreshToken', refreshToken, {
				expires: expirationDate,
			})

			token.value = accessToken
			await router.push({ name: 'home' })
		} catch (e) {
			console.log(e)
		}
	}

	const register = async (
		username: string,
		email: string | null,
		password: string
	): Promise<void> => {
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

	const isRefreshTokenValid = async (
		token: string
	): Promise<void>  => {
		await axiosClient
			.post('/auth/validate-refresh-token', {
				refreshToken: token,
			})
			.then(() => {

			})
	}

	const isLoggedIn = (): boolean => {
		return cookies.get('refreshToken') !== undefined
	}

	return { token, login, register }
})
