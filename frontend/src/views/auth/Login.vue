<script setup lang="ts">
import { toTypedSchema } from '@vee-validate/zod'
import { useForm, Field as VeeField } from 'vee-validate'
import { z } from 'zod'

import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent, CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import {
  Field, FieldDescription,
  FieldError,
  FieldGroup,
  FieldLabel,
} from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import Auth from "@/components/layers/Auth.vue";
import DarkModeSwitcher from "@/components/buttons/DarkModeSwitcher.vue";

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
    }),
)

const { handleSubmit } = useForm({
  validationSchema: formSchema,
  initialValues: {
    username: '',
    password: '',
  },
})

const onSubmit = handleSubmit((data) => {
  console.log(data);
})
</script>

<template>
  <auth>
    <div class="flex flex-col gap-6">
      <Card>
        <CardHeader>
          <div class="flex justify-between items-center">
            <CardTitle>Login to your account</CardTitle>
            <dark-mode-switcher
                variant="ghost"
                class="rounded-full w-8 h-8"
            />
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
                  <FieldLabel for="username">
                    Username
                  </FieldLabel>
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
                    <FieldLabel for="password">
                      Password
                    </FieldLabel>
                    <router-link
                      :to="{ name: 'home' }"
                      class="ml-auto text-sm underline-offset-4 hover:underline"
                    >
                      Forgot your password?
                    </router-link>
                  </div>
                  <Input
                      id="password"
                      v-bind="field"
                      placeholder="password"
                      :aria-invalid="!!errors.length"
                      type="password"
                      required
                  />
                  <FieldError v-if="errors.length" :errors="errors" />
                </Field>
              </VeeField>
            </FieldGroup>
          </form>
        </CardContent>
        <CardFooter>
          <Field>
            <Button type="submit" form="loginForm">
              Login
            </Button>
            <FieldDescription class="text-center">
              Don't have an account?
              <router-link :to="{ name: 'home' }">
                Sign up
              </router-link>
            </FieldDescription>
          </Field>
        </CardFooter>
      </Card>
    </div>
  </auth>
</template>