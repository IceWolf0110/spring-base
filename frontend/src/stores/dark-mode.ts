import { defineStore } from 'pinia'
import { useDark, useToggle } from '@vueuse/core'

export const useDarkModeStore = defineStore('dark-mode', () => {
	const isDark = useDark()
	const toggleDark = useToggle(isDark)

	return { isDark, toggleDark }
})
