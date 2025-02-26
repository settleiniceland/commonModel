import request from '@/utils/request'

// 获得点检日志表
export function getInspectionProfile(id) {
  return request({
    url: '/oam/inspection-profile/get?id=' + id,
    method: 'get'
  })
}

// 获得点检日志表分页
export function getInspectionProfilePage(params) {
  return request({
    url: '/oam/inspection-profile/page',
    method: 'get',
    params
  })
}
// 导出点检日志表 Excel
export function exportInspectionProfileExcel(params) {
  return request({
    url: '/oam/inspection-profile/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
