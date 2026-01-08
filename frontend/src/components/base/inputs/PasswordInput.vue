<script setup lang="ts">
import { type HTMLAttributes, ref, useModel } from 'vue'
import { Input } from '@/components/shadcn/input'
import { Button } from '@/components/shadcn/button'
import { Icon } from '@iconify/vue'
import { cn } from '@/lib/utils.ts'

const props = defineProps<{
	class?: HTMLAttributes['class']
	disabled?: boolean
	modelValue?: string
	placeholder?: string
}>()

const showModal = useModel(props, 'modelValue')

const showPassword = ref(false)
</script>

<template>
	<div class="relative">
		<Input
			v-model="showModal"
			:type="showPassword ? 'text' : 'password'"
			:class="cn('pr-10', props?.class)"
			:placeholder="props?.placeholder ? props.placeholder : 'Enter your password'"
			:disabled="props?.disabled"
			required
		/>
		<Button
			type="button"
			variant="ghost"
			size="icon"
			class="absolute text-foreground right-0 top-0 h-full px-2 py-2 hover:bg-transparent"
			:disabled="props?.disabled"
			@click="showPassword = !showPassword"
		>
			<Icon
				v-if="showPassword"
				icon="radix-icons:eye-closed"
				class="size-4"
				aria-hidden="true"
			/>
			<Icon
				v-if="!showPassword"
				icon="radix-icons:eye-open"
				class="size-4"
				aria-hidden="true"
			/>
			<span class="sr-only">
				{{ showPassword ? 'Show password' : 'Hide password' }}
			</span>
		</Button>
	</div>
</template>
