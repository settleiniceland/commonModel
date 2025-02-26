import request from '@/utils/request'

// 创建保养日志表
export function createMaintainProfile(data) {
  return request({
    url: '/oam/maintain-profile/create',
    method: 'post',
    data: data
  })
}

// 更新保养日志表
export function updateMaintainProfile(data) {
  return request({
    url: '/oam/maintain-profile/update',
    method: 'put',
    data: data
  })
}

// 删除保养日志表
export function deleteMaintainProfile(id) {
  return request({
    url: '/oam/maintain-profile/delete?id=' + id,
    method: 'delete'
  })
}

// 获得保养日志表
export function getMaintainProfile(id) {
  return request({
    url: '/oam/maintain-profile/get?id=' + id,
    method: 'get'
  })
}

// 获得保养日志表分页
export function getMaintainProfilePage(params) {
  return request({
    url: '/oam/maintain-profile/page',
    method: 'get',
    params
  })
}
// 导出保养日志表 Excel
export function exportMaintainProfileExcel(params) {
  return request({
    url: '/oam/maintain-profile/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}