import { defineStore } from 'pinia'
import axiosClient from '@/lib/axios-client.ts'
import router from '@/router'
import { useCookies } from '@vueuse/integrations/useCookies'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
	const token = ref<string|null>(null)

	const cookies = useCookies()

	const login = async (
		username: string,
		password: string,
		rememberMe: boolean
	): Promise<void> => {
		await axiosClient
			.post('/auth/login', {
				username: username,
				password: password,
			})
			.then((res) => {
				console.log(res.data, rememberMe)

				const data = res.data

				token.value = data.accessToken
			})
			.catch((err) => {
				console.log(err.response.data)
			})
	}

	const register = async (
		username: string,
		email: string | null,
		password: string
	): Promise<void> => {
		await axiosClient
			.post('/auth/register', {
				username: username,
				email: email,
				password: password,
			})
			.then((res) => {
				console.log(res)
				router.push({ name: 'login' })
			})
	}

	return { token, login, register }
})
