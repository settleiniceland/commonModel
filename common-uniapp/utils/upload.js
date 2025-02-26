import store from '@/store'
import config from '@/config'
import { getAccessToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { toast, showConfirm, tansParams } from '@/utils/common'
import i18n from '../i18n/i18n.js'
let timeout = 10000
const baseUrl = config.baseUrl + config.baseApi


const upload = config => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  config.header = config.header || {}
  if (getAccessToken() && !isToken) {
    config.header['Authorization'] = 'Bearer ' + getAccessToken()
  }
  // get请求映射params参数
  if (config.params) {
    let url = config.url + '?' + tansParams(config.params)
    url = url.slice(0, -1)
    config.url = url
  }
  // 设置租户 TODO 芋艿：强制 1 先
  config.header['tenant-id'] = '1';
  return new Promise((resolve, reject) => {
      uni.uploadFile({
        timeout: config.timeout || timeout,
        url: baseUrl + config.url,
        filePath: config.filePath,
        name: config.name || 'file',
        header: config.header,
        formData: config.formData,
        method: config.method || 'post',
        success: (res) => {
          let result = JSON.parse(res.data)
          const code = result.code || 200
          const msg = errorCode[code] || result.msg || errorCode['default']
          if (code === 200) {
            resolve(result)
          } else if (code == 401) {//,
            showConfirm(i18n.t("messages.words.text33")).then(res => {
              if (res.confirm) {
                store.dispatch('LogOut').then(res => {
                  uni.reLaunch({ url: '/pages/login/login' })
                })
              }
            })
            reject(i18n.t("messages.words.text34"))
          } else if (code === 500) {
            toast(msg)
            reject('500')
          } else if (code !== 200) {
            toast(msg)
            reject(code)
          }
        },
        fail: (error) => {
          let { message } = error
          if (message == 'Network Error') {
            message = i18n.t("messages.words.text35")
          } else if (message.includes('timeout')) {
            message = i18n.t("messages.words.text36")
          } else if (message.includes('Request failed with status code')) {
            message = i18n.t("messages.words.text37") + message.substr(message.length - 3) + i18n.t("messages.words.text38")
          }
          toast(message)
          reject(error)
        }
      })
  })
}

export default upload
