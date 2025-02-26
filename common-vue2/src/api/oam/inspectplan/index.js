import request from '@/utils/request'

// 创建点检计划表
export function createInspectplan(data) {
  return request({
    url: '/oam/inspectplan/create',
    method: 'post',
    data: data
  })
}

// 更新点检计划表
export function updateInspectplan(data) {
  return request({
    url: '/oam/inspectplan/update',
    method: 'put',
    data: data
  })
}

// 删除点检计划表
export function deleteInspectplan(id,status) {
  return request({
    url: '/oam/inspectplan/delete?id=' + id + '&status=' + status,
    method: 'delete'
  })
}

// 获得点检计划表
export function getInspectplan(id) {
  return request({
    url: '/oam/inspectplan/get?id=' + id,
    method: 'get'
  })
}

// 获得点检计划表分页
export function getInspectplanPage(params) {
  return request({
    url: '/oam/inspectplan/page',
    method: 'get',
    params
  })
}

// 导出点检计划表 Excel
// export function exportInspectplanExcel(params) {
//   return request({
//     url: '/oam/inspectplan/export-excel',
//     method: 'get',
//     params,
//     responseType: 'blob'
//   })
// }

// 为计划添加内容项
export function addSubstances(planId,substanceIds) {
  return request({
    url: '/oam/inspectplan/addSubstances',
    method: 'post',
    params: {planId},
    data: substanceIds
  })
}

// 为计划剔除内容项
export function removeSubstances(planId,substanceId) {
  return request({
    url: '/oam/inspectplan/delSubstance',
    method: 'get',
    params: {planId,substanceId},
  })
}

// 导出指定计划详情
export function exportSubstances(params) {
  return request({
    url: '/oam/inspectplan/export-plan-detail',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 查询所有点检计划
export function selectAllPlan() {
  return request({
    url: '/oam/inspectplan/list',
    method: 'get'
  })
}