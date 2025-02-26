import request from '@/utils/request'

// 创建设备档案数据
export function createEquipmentprofile(data) {
  return request({
    url: '/property/equipmentprofile/create',
    method: 'post',
    data: data
  })
}

// 更新设备档案数据
export function updateEquipmentprofile(data) {
  return request({
    url: '/property/equipmentprofile/update',
    method: 'put',
    data: data
  })
}

// 删除设备档案数据
export function deleteEquipmentprofile(id,code,equipAttribute) {
  return request({
    url: '/property/equipmentprofile/delete?id=' + id + '&code=' + code + '&equipAttribute=' +equipAttribute,
    method: 'delete'
  })
}

// 获得设备档案数据
export function getEquipmentprofile(id) {
  return request({
    url: '/property/equipmentprofile/get?id=' + id,
    method: 'get'
  })
}

// 获得设备档案数据列表
export function getEquipmentprofileList(params) {
  return request({
    url: '/property/equipmentprofile/list',
    method: 'get',
    params
  })
}
// 导出设备档案数据 Excel
export function exportEquipmentprofileExcel(params) {
  return request({
    url: '/property/equipmentprofile/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 查询所有部门列表
export function getAllDeptList() {
  return request({
    url: '/property/equipmentprofile/getAllDeptList',
    method: 'get',
  })
}

// 根据设备id查询设备档案列表
export function getListByEquipId(equipId) {
  return request({
    url: '/property/equipmentprofile/getListByEquipId?equipId='+equipId,
    method: 'get',
  })
}

//修改设备状态
export function updateStatusById(data){
  return request({
    url: '/property/equipmentprofile/update/status',
    method: 'put',
    data: data
  })
}