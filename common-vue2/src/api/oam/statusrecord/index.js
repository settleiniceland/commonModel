import request from '@/utils/request'

// 删除停机表
export function deleteStop(id) {
  return request({
    url: '/oam/status/record/delete?id=' + id,
    method: 'delete'
  })
}

// 获得停机表
export function getStop(id) {
  return request({
    url: '/oam/status/record/get?id=' + id,
    method: 'get'
  })
}

// 获得停机表分页
export function getStopPage(params) {
  return request({
    url: '/oam/status/record/page',
    method: 'get',
    params
  })
}
// 导出停机表 Excel
export function exportStopExcel(params) {
  return request({
    url: '/oam/status/record/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
