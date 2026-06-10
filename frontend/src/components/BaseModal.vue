<script setup>
defineProps({
  show: { type: Boolean, default: false },
  title: { type: String, default: '' },
  width: { type: String, default: '480px' },
})
const emit = defineEmits(['close'])
</script>

<template>
  <transition name="fade">
    <div v-if="show" class="modal-mask" @click.self="emit('close')">
      <div class="modal-box" :style="{ maxWidth: width }">
        <header class="modal-head">
          <h3 class="serif">{{ title }}</h3>
          <button class="modal-close" @click="emit('close')">×</button>
        </header>
        <div class="modal-body">
          <slot />
        </div>
        <footer v-if="$slots.footer" class="modal-foot">
          <slot name="footer" />
        </footer>
      </div>
    </div>
  </transition>
</template>

<style scoped>
.modal-mask {
  position: fixed;
  inset: 0;
  background: rgba(31, 29, 26, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}
.modal-box {
  width: 100%;
  background-color: var(--surface);
  border-radius: 14px;
  box-shadow: var(--shadow-md);
  overflow: hidden;
}
.modal-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px 22px;
  border-bottom: 1px solid var(--border);
}
.modal-head h3 {
  font-size: 18px;
  font-weight: 600;
}
.modal-close {
  font-size: 24px;
  line-height: 1;
  color: var(--muted);
}
.modal-close:hover {
  color: var(--foreground);
}
.modal-body {
  padding: 22px;
  max-height: 70vh;
  overflow-y: auto;
}
.modal-foot {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 16px 22px;
  border-top: 1px solid var(--border);
}
</style>
