import request from '@/utils/request'

// 创建设备生产厂家信息
export function createManufacturer(data) {
  return request({
    url: '/property/manufacturer/create',
    method: 'post',
    data: data
  })
}

// 更新设备生产厂家信息
export function updateManufacturer(data) {
  return request({
    url: '/property/manufacturer/update',
    method: 'put',
    data: data
  })
}

// 删除设备生产厂家信息
export function deleteManufacturer(id) {
  return request({
    url: '/property/manufacturer/delete?id=' + id,
    method: 'delete'
  })
}

// 获得设备生产厂家信息
export function getManufacturer(id) {
  return request({
    url: '/property/manufacturer/get?id=' + id,
    method: 'get'
  })
}

// 获得设备生产厂家信息分页
export function getManufacturerPage(params) {
  return request({
    url: '/property/manufacturer/page',
    method: 'get',
    params
  })
}

// 获得所有状态正常的设备生产厂家信息
export function getManufacturerList() {
  return request({
    url: '/property/manufacturer/list',
    method: 'get',
  })
}

// 导出设备生产厂家信息 Excel
export function exportManufacturerExcel(params) {
  return request({
    url: '/property/manufacturer/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
