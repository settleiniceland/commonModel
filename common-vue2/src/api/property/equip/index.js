import request from '@/utils/request'

// 创建设备表
export function createEquip(data) {
  return request({
    url: '/property/equip/create',
    method: 'post',
    data: data
  })
}

// 更新设备表
export function updateEquip(data) {
  return request({
    url: '/property/equip/update',
    method: 'put',
    data: data
  })
}

// 删除设备表
export function deleteEquip(id) {
  return request({
    url: '/property/equip/delete?id=' + id,
    method: 'delete'
  })
}

// 获得设备表
export function getEquip(id) {
  return request({
    url: '/property/equip/get?id=' + id,
    method: 'get'
  })
}

// 获得设备表列表
export function getEquipList(params) {
  return request({
    url: '/property/equip/list',
    method: 'get',
    params
  })
}
// 导出设备表 Excel
export function exportEquipExcel(params) {
  return request({
    url: '/property/equip/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}