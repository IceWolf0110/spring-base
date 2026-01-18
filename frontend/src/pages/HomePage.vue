<script setup lang="ts">
	import WebLayout from '@/components/layouts/WebLayout.vue'
</script>

<template>
	<web-layout>
		<router-link :to="{ name: 'login' }">Login</router-link>
	</web-layout>
</template>

<style scoped></style>
