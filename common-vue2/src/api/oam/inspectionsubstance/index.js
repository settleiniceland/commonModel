import request from '@/utils/request'

// 创建点检内容
export function createInspectionSubstance(data) {
  return request({
    url: '/oam/inspection-substance/create',
    method: 'post',
    data: data
  })
}

// 更新点检内容
export function updateInspectionSubstance(data) {
  return request({
    url: '/oam/inspection-substance/update',
    method: 'put',
    data: data
  })
}

// 删除点检内容
export function deleteInspectionSubstance(id) {
  return request({
    url: '/oam/inspection-substance/delete?id=' + id,
    method: 'delete'
  })
}

// 获得点检内容
export function getInspectionSubstance(id) {
  return request({
    url: '/oam/inspection-substance/get?id=' + id,
    method: 'get'
  })
}

// 获得点检内容分页
export function getInspectionSubstancePage(params) {
  return request({
    url: '/oam/inspection-substance/page',
    method: 'get',
    params
  })
}

// 根据点检计划获得其点检内容分页
export function getInspectionSubstancePageByPlan(params) {
  return request({
    url: '/oam/inspection-substance/pageByPlanId',
    method: 'get',
    params
  })
}

// 根据点检计划获得与此计划无关的点检内容分页
export function getInspectionSubstanceAddPageByPlan(params) {
  return request({
    url: '/oam/inspection-substance/addPageByPlanId',
    method: 'get',
    params
  })
}

// 导出点检内容 Excel
export function exportInspectionSubstanceExcel(params) {
  return request({
    url: '/oam/inspection-substance/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
