import store from '@/store'
import config from '@/config'
import { getAccessToken, getRefreshToken, setToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { toast, showConfirm, tansParams } from '@/utils/common'
import {refreshToken} from "@/api/login";
import i18n from '../i18n/i18n.js'
let timeout = 10000
// 是否正在刷新中
let isRefreshToken = false
// 请求队列
let requestList = []
const baseUrl = config.baseUrl + config.baseApi;

const request = config => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  config.header = config.header || {}
  if (getAccessToken() && !isToken) {
    config.header['Authorization'] = 'Bearer ' + getAccessToken()
  }
  // 设置租户 TODO 芋艿：强制 1 先
  config.header['tenant-id'] = '1';
  // get请求映射params参数
  if (config.params) {
    let url = config.url + '?' + tansParams(config.params)
    url = url.slice(0, -1)
    config.url = url
  }
  return new Promise((resolve, reject) => {
    uni.request({
        method: config.method || 'get',
        timeout: config.timeout ||  timeout,
        url: config.baseUrl || baseUrl + config.url,
        data: config.data,
        // header: config.header,
        header: config.header,
        dataType: 'json'
      }).then(async response => {
        let [error, res] = response
        if (error) {
          toast(i18n.t("messages.words.text35"))
          reject(i18n.t("messages.words.text35"))
          return
        }
        const code = res.data.code || 200
        const msg = errorCode[code] || res.data.msg || errorCode['default']
        if (code === 401) {
          if(!isRefreshToken){
			isRefreshToken = true;
			// 1. 如果获取不到刷新令牌，则只能执行登出操作
			if(!getRefreshToken()){
				return handleAuthorized();//重登
			}
			// 2. 进行刷新访问令牌
			try{
				const refreshTokenRes = await refreshToken();
				setToken(refreshTokenRes.data);
				requestList.forEach(cb => cb());
				return request(config).then(resolve).catch(reject); // 重新发起请求
			} catch (e){
				console.log("令牌刷新失败",e);
				return handleAuthorized();//重登
			} finally {
				isRefreshToken = false;
			}
			} else {
				return new Promise(resolve => {
					requestList.push(() => {
						config.headers['Authorization'] = 'Bearer ' + getAccessToken() // 让每个请求携带自定义token 请根据实际情况自行修改
						resolve(request(config))
					})
				})
			}
        } else if (code === 500) {
          toast(msg)
          reject('500')
        } else if (code !== 200) {
          toast(msg)
          reject(code)
        }
        resolve(res.data)
      })
      .catch(error => {
        let { message } = error
        if (message === 'Network Error') {
          message = i18n.t("messages.words.text35")
        } else if (message.includes('timeout')) {
          message = i18n.t("messages.words.text36")
        } else if (message.includes('Request failed with status code')) {
          message = i18n.t("messages.words.text37") + message.substr(message.length - 3) + i18n.t("messages.words.text38")
        }
        toast(message)
        reject(error)
      })
  })
}

function handleAuthorized() {//重新登录
	showConfirm(i18n.t("messages.words.text33")).then(res => {
	  if (res.confirm) {
	    store.dispatch('LogOut').then(res => {
	      uni.reLaunch({ url: '/pages/login' })
	    })
	  }
	})
	// reject(i18n.t("messages.words.text34"))
}

export default request
