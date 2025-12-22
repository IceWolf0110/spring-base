import { defineStore } from 'pinia';
import { useDark, useToggle } from '@vueuse/core';

export const useThemeStore = defineStore('counter', () => {
  const isDark = useDark();
  const toggleDark = useToggle(isDark);

  return { isDark, toggleDark }
})
