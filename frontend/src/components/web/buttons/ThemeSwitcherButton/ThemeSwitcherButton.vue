<script setup lang="ts">
  import { useColorMode } from '@vueuse/core'
  import { Button, type ButtonVariants } from '@/components/ui/button'
  import { Icon } from '@iconify/vue'
  import type { PrimitiveProps } from 'reka-ui'
  import type { HTMLAttributes } from 'vue'

  enum ThemeType {
    DARK = 'dark',
    LIGHT = 'light',
    AUTO = 'auto',
  }

  const mode = useColorMode()

  const getPreferredScheme = () =>
    window?.matchMedia?.('(prefers-color-scheme:dark)')?.matches ? ThemeType.DARK : ThemeType.LIGHT

  const getCurrentColorMode = () => localStorage.getItem('vueuse-color-scheme')

  let isDarkMode =
    (getPreferredScheme() == ThemeType.DARK && getCurrentColorMode() == ThemeType.AUTO) ||
    getCurrentColorMode() == ThemeType.DARK

  const changeColorTheme = () => {
    isDarkMode = !isDarkMode

    mode.value = isDarkMode ? ThemeType.DARK : ThemeType.LIGHT
  }

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
  <Button v-bind="props" @click="changeColorTheme">
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
