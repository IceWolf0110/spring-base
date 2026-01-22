<script setup lang="ts">
	import { useAuthStore } from '@/stores/auth.ts'

	const store = useAuthStore()

	const isLoggedIn = store.isLoggedIn
</script>

<template>
	{{ isLoggedIn ? 'hehehehehehe' : 'huhuhuhuhu' }}
</template>

<style scoped></style>
