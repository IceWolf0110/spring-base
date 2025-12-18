<script setup lang="ts">
  import ThemeSwitcherButton
    from '@/components/web/buttons/ThemeSwitcherButton/ThemeSwitcherButton.vue'
  import { Label } from 'reka-ui'
  import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
  } from '@/components/ui/card'
  import { Button } from '@/components/ui/button'
  import { Input } from '@/components/ui/input'
</script>

<template>
  <div class="min-h-screen flex items-center justify-center">
    <Card class="w-full max-w-sm">
      <CardHeader>
        <div class="flex justify-between items-center">
          <CardTitle>Login to your account</CardTitle>
          <theme-switcher-button variant="ghost" class="rounded-full"/>
        </div>
        <CardDescription> Enter your email below to login to your account </CardDescription>
      </CardHeader>
      <CardContent>
        <form>
          <div class="grid w-full items-center gap-4">
            <div class="flex flex-col space-y-1.5">
              <Label for="email">Email</Label>
              <Input id="email" type="email" placeholder="m@example.com" />
            </div>
            <div class="flex flex-col space-y-1.5">
              <div class="flex items-center">
                <Label for="password">Password</Label>
                <a href="#" class="ml-auto inline-block text-sm underline">
                  Forgot your password?
                </a>
              </div>
              <Input id="password" type="password" />
            </div>
          </div>
        </form>
      </CardContent>
      <CardFooter class="flex flex-col gap-2">
        <Button class="w-full"> Login </Button>
        <div class="text-sm mt-3">
          <span>Does not have an account? </span>
          <router-link to="/"> Sign Up </router-link>
        </div>
      </CardFooter>
    </Card>
  </div>
</template>

<style scoped></style>
