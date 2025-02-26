import request from '@/utils/request'

// 创建设备安装位置
export function createInstalllocation(data) {
  return request({
    url: '/property/installlocation/create',
    method: 'post',
    data: data
  })
}

// 更新设备安装位置
export function updateInstalllocation(data) {
  return request({
    url: '/property/installlocation/update',
    method: 'put',
    data: data
  })
}

// 删除设备安装位置
export function deleteInstalllocation(id) {
  return request({
    url: '/property/installlocation/delete?id=' + id,
    method: 'delete'
  })
}

// 获得设备安装位置
export function getInstalllocation(id) {
  return request({
    url: '/property/installlocation/get?id=' + id,
    method: 'get'
  })
}

// 获得设备安装位置列表
export function getInstalllocationList(params) {
  return request({
    url: '/property/installlocation/list',
    method: 'get',
    params
  })
}
// 导出设备安装位置 Excel
export function exportInstalllocationExcel(params) {
  return request({
    url: '/property/installlocation/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}