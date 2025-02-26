import request from '@/utils/request'

// 创建缺陷库
export function createFlaws(data) {
  return request({
    url: '/oam/flaws/create',
    method: 'post',
    data: data
  })
}

// 更新缺陷库
export function updateFlaws(data) {
  return request({
    url: '/oam/flaws/update',
    method: 'put',
    data: data
  })
}

// 删除缺陷库
export function deleteFlaws(id) {
  return request({
    url: '/oam/flaws/delete?id=' + id,
    method: 'delete'
  })
}

// 获得缺陷库
export function getFlaws(id) {
  return request({
    url: '/oam/flaws/get?id=' + id,
    method: 'get'
  })
}

// 获得缺陷库分页
export function getFlawsPage(params) {
  return request({
    url: '/oam/flaws/page',
    method: 'get',
    params
  })
}
// 导出缺陷库 Excel
export function exportFlawsExcel(params) {
  return request({
    url: '/oam/flaws/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}