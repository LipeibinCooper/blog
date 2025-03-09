import axios from '@/axios'

// 获取灵感分页数据
export function getCategoryPageList (data) {
  return axios.post('/admin/category/list', data)
}

// 添加灵感
export function addCategory (data) {
  return axios.post('/admin/category/add', data)
}

// 删除灵感
export function deleteCategory (id) {
  return axios.post('/admin/category/delete', { id })
}

// 获取灵感 select 数据
export function getCategorySelectList () {
  return axios.post('/admin/category/select/list')
}
