import request from '@/utils/request'

// 创建保养计划
export function createMaintainPlan(data) {
  return request({
    url: '/oam/maintain-plan/create',
    method: 'post',
    data: data
  })
}

// 更新保养计划
export function updateMaintainPlan(data) {
  return request({
    url: '/oam/maintain-plan/update',
    method: 'put',
    data: data
  })
}

// 删除保养计划
export function deleteMaintainPlan(id) {
  return request({
    url: '/oam/maintain-plan/delete?id=' + id,
    method: 'delete'
  })
}

// 获得保养计划
export function getMaintainPlan(id) {
  return request({
    url: '/oam/maintain-plan/get?id=' + id,
    method: 'get'
  })
}

// 获得保养计划分页
export function getMaintainPlanPage(params) {
  return request({
    url: '/oam/maintain-plan/page',
    method: 'get',
    params
  })
}
// 导出保养计划 Excel
export function exportMaintainPlanExcel(params) {
  return request({
    url: '/oam/maintain-plan/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}