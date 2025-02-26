import request from '@/utils/request'

// 创建保养内容表
export function createMaintainDetail(data) {
  return request({
    url: '/oam/maintain-detail/create',
    method: 'post',
    data: data
  })
}

// 更新保养内容表
export function updateMaintainDetail(data) {
  return request({
    url: '/oam/maintain-detail/update',
    method: 'put',
    data: data
  })
}

// 删除保养内容表
export function deleteMaintainDetail(id) {
  return request({
    url: '/oam/maintain-detail/delete?id=' + id,
    method: 'delete'
  })
}

// 获得保养内容表
export function getMaintainDetail(id) {
  return request({
    url: '/oam/maintain-detail/get?id=' + id,
    method: 'get'
  })
}

// 获得保养内容表分页
export function getMaintainDetailPage(params) {
  return request({
    url: '/oam/maintain-detail/page',
    method: 'get',
    params
  })
}

// 获得保养内容表list
export function getMaintainDetailList(params) {
  return request({
    url: '/oam/maintain-detail/list',
    method: 'get',
    params
  })
}

// 导出保养内容表 Excel
export function exportMaintainDetailExcel(params) {
  return request({
    url: '/oam/maintain-detail/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
