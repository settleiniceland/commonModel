import request from '@/utils/request'

// 创建设备类别
export function createEquiptype(data) {
  return request({
    url: '/property/equiptype/create',
    method: 'post',
    data: data
  })
}

// 更新设备类别
export function updateEquiptype(data) {
  return request({
    url: '/property/equiptype/update',
    method: 'put',
    data: data
  })
}

// 删除设备类别
export function deleteEquiptype(id) {
  return request({
    url: '/property/equiptype/delete?id=' + id,
    method: 'delete'
  })
}

// 获得设备类别
export function getEquiptype(id) {
  return request({
    url: '/property/equiptype/get?id=' + id,
    method: 'get'
  })
}

// 获得设备类别列表
export function getEquiptypeList(params) {
  return request({
    url: '/property/equiptype/list',
    method: 'get',
    params
  })
}
// 导出设备类别 Excel
export function exportEquiptypeExcel(params) {
  return request({
    url: '/property/equiptype/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}