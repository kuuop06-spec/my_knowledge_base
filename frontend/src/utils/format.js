// 日期格式化：后端返回的 LocalDateTime 形如 "2024-05-10T10:00:00" 或数组/字符串
export function formatDate(input) {
  if (!input) return ''
  let d
  if (Array.isArray(input)) {
    // [year, month, day, hour, minute, second]
    const [y, mo, day, h = 0, mi = 0, s = 0] = input
    d = new Date(y, (mo || 1) - 1, day || 1, h, mi, s)
  } else {
    d = new Date(input)
  }
  if (isNaN(d.getTime())) return String(input)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(
    d.getHours()
  )}:${pad(d.getMinutes())}`
}

export function formatDateShort(input) {
  const full = formatDate(input)
  return full ? full.split(' ')[0] : ''
}

// 文件大小格式化
export function formatSize(bytes) {
  if (bytes == null) return '-'
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / 1024 / 1024).toFixed(2) + ' MB'
}
