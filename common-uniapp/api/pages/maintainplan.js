import request from '@/utils/request'

// 获取所有启动的保养计划
export function getMaintainPlanList(params){
	return request({
		url:'/oam/maintain-plan/list',
		'method': 'GET',
		'data': params
	})
}

// 根据条件获取保养计划详情
export function getMaintainDetailList(params){
	return request({
		url:'/oam/maintain-detail/list',
		'method': 'GET',
		'data': params
	})
}