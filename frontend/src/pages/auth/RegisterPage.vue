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
import { useAuthStore } from '@/stores/auth.ts'

const formSchema = toTypedSchema(
	z
		.object({
			username: z
				.string()
				.min(5, 'username must be at least 5 characters.')
				.max(32, 'username must be at most 32 characters.'),
			email: z.string().email('Invalid email address'),
			password: z
				.string()
				.min(8, 'password must be at least 8 characters.')
				.max(100, 'password must be at most 100 characters.'),

			passwordConfirmation: z
				.string()
				.min(8, 'password must be at least 8 characters.')
				.max(100, 'password must be at most 100 characters.'),
		})
		.refine((data) => data.password === data.passwordConfirmation, {
			message: "Password didn't match",
			path: ['passwordConfirmation'],
		}),
)

const { handleSubmit } = useForm({
	validationSchema: formSchema,
	initialValues: {
		username: '',
		email: '',
		password: '',
		passwordConfirmation: '',
	},
})

const store = useAuthStore()

const onSubmit = handleSubmit((data) => {
	const username = data.username
	const email = data.password
	const password = data.password

	store.register(username, email, password)
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
					<form id="registerForm" @submit="onSubmit">
						<FieldGroup>
							<VeeField v-slot="{ field, errors }" name="username">
								<Field :data-invalid="!!errors.length">
									<FieldLabel for="username"> Username </FieldLabel>
									<Input
										id="username"
										v-bind="field"
										placeholder="Username"
										:aria-invalid="!!errors.length"
										required
									/>
									<FieldError v-if="errors.length" :errors="errors" />
								</Field>
							</VeeField>

							<VeeField v-slot="{ field, errors }" name="email">
								<Field :data-invalid="!!errors.length">
									<FieldLabel for="email"> Email </FieldLabel>
									<Input
										id="email"
										v-bind="field"
										placeholder="Email"
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
									</div>
									<PasswordInput
										id="password"
										v-bind="field"
										placeholder="Password"
										:aria-invalid="!!errors.length"
									/>
									<FieldError v-if="errors.length" :errors="errors" />
								</Field>
							</VeeField>

							<VeeField v-slot="{ field, errors }" name="passwordConfirmation">
								<Field :data-invalid="!!errors.length">
									<div class="flex items-center">
										<FieldLabel for="passwordConfirmation">
											Password Confirmation
										</FieldLabel>
									</div>
									<PasswordInput
										id="passwordConfirmation"
										v-bind="field"
										placeholder="Password confirmation"
										:aria-invalid="!!errors.length"
									/>
									<FieldError v-if="errors.length" :errors="errors" />
								</Field>
							</VeeField>
						</FieldGroup>
					</form>
				</CardContent>
				<CardFooter>
					<Field>
						<Button type="submit" form="registerForm"> Register </Button>
						<FieldDescription class="text-center">
							Already have have an account?
							<router-link :to="{ name: 'login' }"> Login </router-link>
						</FieldDescription>
					</Field>
				</CardFooter>
			</Card>
		</div>
	</auth-layout>
</template>

<style scoped></style>
