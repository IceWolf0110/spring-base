<script setup lang="ts">
  import { useColorMode } from '@vueuse/core'
  import { Button } from '@/components/ui/button'

  const mode = useColorMode();

  const getPreferredScheme = () => window?.matchMedia?.('(prefers-color-scheme:dark)')?.matches ? 'dark' : 'light';
  const getCurrentColorMode = () => localStorage.getItem('vueuse-color-scheme');

  let isDarkMode = getPreferredScheme() == 'dark' && getCurrentColorMode() == 'auto' || getCurrentColorMode() == 'dark';

  const changeColorTheme = () => {
    isDarkMode = !isDarkMode;

    mode.value = isDarkMode ? 'dark' : 'light';
  }
</script>

<template>
  <Button variant="outline" @click="changeColorTheme">
    Button
  </Button>
</template>

<style scoped>

</style>
