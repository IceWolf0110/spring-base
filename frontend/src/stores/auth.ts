import { defineStore } from 'pinia'
import axiosClient from '@/lib/axios-client.ts'
import router from '@/router'
import { useCookies } from '@vueuse/integrations/useCookies'
import { jwtDecode } from 'jwt-decode'

export const useAuthStore = defineStore('auth', () => {
	let accessToken
	let refreshToken

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
			refreshToken = data.refreshToken

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

	const isRefreshTokenValid = async () => {


		// await axiosClient
		// 	.post('/auth/validate-refresh-token', {
		// 		refreshToken: token,
		// 	})
		// 	.then(() => {
		//
		// 	})
	}

	const isLoggedIn = ()=> {
		return false
	}

	return { accessToken, refreshToken, login, register }
})
