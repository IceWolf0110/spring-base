<script setup lang="ts">
import { toTypedSchema } from '@vee-validate/zod'
import { useForm, Field as VeeField } from 'vee-validate'
import { z } from 'zod'

import { Button } from '@/components/shadcn/button'
import {
	Card,
	CardContent,
	CardDescription,
	CardFooter,
	CardHeader,
	CardTitle,
} from '@/components/shadcn/card'
import {
	Field,
	FieldDescription,
	FieldError,
	FieldGroup,
	FieldLabel,
} from '@/components/shadcn/field'
import { Input } from '@/components/shadcn/input'
import AuthLayout from '@/components/layouts/AuthLayout.vue'
import DarkModeSwitcher from '@/components/base/buttons/DarkModeSwitcher.vue'
import PasswordInput from '@/components/base/inputs/PasswordInput.vue'
import { Checkbox } from '@/components/shadcn/checkbox'
import { useAuthStore } from '@/stores/auth.ts'

const formSchema = toTypedSchema(
	z.object({
		username: z
			.string()
			.min(5, 'username must be at least 5 characters.')
			.max(32, 'username must be at most 32 characters.'),
		password: z
			.string()
			.min(8, 'password must be at least 8 characters.')
			.max(100, 'password must be at most 100 characters.'),
		rememberMe: z.boolean(),
	}),
)

const { handleSubmit } = useForm({
	validationSchema: formSchema,
	initialValues: {
		username: '',
		password: '',
		rememberMe: false,
	},
})

const store = useAuthStore()

const onSubmit = handleSubmit((data) => {
	const username = data.username
	const password = data.password
	const rememberMe = data.rememberMe

	store.login(username, password, rememberMe)
})
</script>

<template>
	<auth-layout>
		<div class="flex flex-col gap-6">
			<Card>
				<CardHeader>
					<div class="flex justify-between items-center">
						<CardTitle>Login to your account</CardTitle>
						<dark-mode-switcher variant="ghost" class="rounded-full w-8 h-8" />
					</div>
					<CardDescription>
						Enter your email below to login to your account
					</CardDescription>
				</CardHeader>
				<CardContent>
					<form id="loginForm" @submit="onSubmit">
						<FieldGroup>
							<VeeField v-slot="{ field, errors }" name="username">
								<Field :data-invalid="!!errors.length">
									<FieldLabel for="username"> Username </FieldLabel>
									<Input
										id="username"
										v-bind="field"
										placeholder="username"
										:aria-invalid="!!errors.length"
										required
									/>
									<FieldError v-if="errors.length" :errors="errors" />
								</Field>
							</VeeField>

							<VeeField v-slot="{ field, errors }" name="password">
								<Field :data-invalid="!!errors.length">
									<div class="flex items-center">
										<FieldLabel for="password"> Password </FieldLabel>
										<router-link
											:to="{ name: 'home' }"
											class="ml-auto text-foreground inline-block text-sm underline"
										>
											Forgot your password?
										</router-link>
									</div>
									<PasswordInput
										id="password"
										v-bind="field"
										placeholder="password"
										:aria-invalid="!!errors.length"
										type="password"
									/>
									<FieldError v-if="errors.length" :errors="errors" />
								</Field>
							</VeeField>
							<VeeField v-slot="{ field }" name="rememberMe">
								<Field>
									<div class="flex items-center space-x-2">
										<Checkbox
											id="rememberMe"
											:model-value="field.value"
											@update:model-value="field.onChange"
										/>
										<FieldLabel for="rememberMe" class="text-sm">
											Remember me
										</FieldLabel>
									</div>
								</Field>
							</VeeField>
						</FieldGroup>
					</form>
				</CardContent>
				<CardFooter>
					<Field>
						<Button type="submit" form="loginForm"> Login </Button>
						<FieldDescription class="text-center">
							Don't have an account?
							<router-link :to="{ name: 'register' }"> Register </router-link>
						</FieldDescription>
					</Field>
				</CardFooter>
			</Card>
		</div>
	</auth-layout>
</template>

<style scoped></style>
