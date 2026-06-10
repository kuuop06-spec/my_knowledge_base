import { reactive } from 'vue'

// 极简全局 toast 提示
const state = reactive({ list: [] })
let seed = 0

function push(message, type = 'info') {
  const id = ++seed
  state.list.push({ id, message, type })
  setTimeout(() => remove(id), 2600)
}

function remove(id) {
  const idx = state.list.findIndex((t) => t.id === id)
  if (idx > -1) state.list.splice(idx, 1)
}

export const toast = {
  success: (m) => push(m, 'success'),
  error: (m) => push(m, 'error'),
  info: (m) => push(m, 'info'),
}

export function useToastState() {
  return { state, remove }
}
