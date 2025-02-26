import Vue from 'vue'
import App from './App'
import store from './store' // store
import plugins from './plugins' // plugins
import './permission' // permission
import i18n from './i18n/i18n'
Vue.use(plugins)

Vue.config.productionTip = false
Vue.prototype.$store = store

App.mpType = 'app'

const app = new Vue({
	i18n,
    ...App
})

app.$mount()
// 动态设置 tabBar 文本
function setTabBarText() {
  uni.setTabBarItem({
    index: 0,
    text: app.$t('messages.words.text25') // 首页
  })
  uni.setTabBarItem({
    index: 1,
    text: app.$t('messages.words.text19') // 工作台
  })
  uni.setTabBarItem({
    index: 2,
    text: app.$t('messages.words.text20') // 我的
  })
}
// 在应用启动时设置 tabBar 文本
setTabBarText()
// 在语言切换时重新设置 tabBar 文本
Vue.prototype.$setTabBarText = setTabBarText