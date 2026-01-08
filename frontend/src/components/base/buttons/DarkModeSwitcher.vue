<script setup lang="ts">
import type { PrimitiveProps } from 'reka-ui'
import { Button, type ButtonVariants } from '@/components/shadcn/button'
import type { HTMLAttributes } from 'vue'
import { Icon } from '@iconify/vue'
import { useThemeStore } from '@/stores/theme.ts'

const store = useThemeStore()

interface Props extends PrimitiveProps {
	variant?: ButtonVariants['variant']
	size?: ButtonVariants['size']
	class?: HTMLAttributes['class']
}

const props = withDefaults(defineProps<Props>(), {
	as: 'button',
})
</script>

<template>
	<Button v-bind="props" @click="store.toggleDark()">
		<Icon
			icon="radix-icons:moon"
			class="h-[1.2rem] w-[1.2rem] scale-100 transition-all dark:scale-0"
		/>
		<Icon
			icon="radix-icons:sun"
			class="absolute h-[1.2rem] w-[1.2rem] scale-0 transition-all dark:scale-100"
		/>
	</Button>
</template>

<style scoped></style>
