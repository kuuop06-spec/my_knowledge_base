<script setup>
import { useToastState } from '@/utils/toast'
const { state, remove } = useToastState()
</script>

<template>
  <div class="toast-wrap">
    <transition-group name="toast">
      <div
        v-for="t in state.list"
        :key="t.id"
        class="toast-item"
        :class="`toast-${t.type}`"
        @click="remove(t.id)"
      >
        {{ t.message }}
      </div>
    </transition-group>
  </div>
</template>

<style scoped>
.toast-wrap {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: center;
}
.toast-item {
  padding: 10px 18px;
  border-radius: var(--radius);
  font-size: 14px;
  color: #fff;
  background: var(--foreground);
  box-shadow: var(--shadow-md);
  cursor: pointer;
  max-width: 90vw;
}
.toast-success {
  background: var(--accent);
}
.toast-error {
  background: var(--danger);
}
.toast-info {
  background: var(--foreground);
}
.toast-enter-active,
.toast-leave-active {
  transition: all 0.25s ease;
}
.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
