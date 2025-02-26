import request from '@/utils/request'

// 获取所有启动的点检计划
export function getInspectPlanList(params){
	return request({
		url:'/oam/inspectplan/list',
		'method': 'POST',
		'data': params
	})
}

//根据是计划id获取对应的点检内容
export function getInspectSubstanceListByPlanId(id){
	return request({
		url: '/oam/inspection-substance/pageByPlanId?id=' + id,
		'method':'get'
	})
}

//根据查询条件查出设备档案列表
export function getEquipmentProfile(locationId,equipId){
	return request({
		url: '/property/equipmentprofile/list?locationId=' + locationId + '&equipId=' +equipId,
		'method':'get'
	})
}

//添加一阵个计划的点检日志
export function addInspectProfileList(data){
	return request({
		url: '/oam/inspection-profile/addEquipInspectionProfileList',
		'method': 'POST',
		'data':data
	})
}

//根据计划Id获取该计划最新轮次首次录入的信息
export function getPlanNewestExecuteTimeByPlanId(planId){
	return request({
		url: '/oam/inspection-profile/getPlanNewestExecuteTime?planId=' + planId,
		'method':'get'
	})
}