import i18n from '../i18n/i18n.js'
export default {
  // 消息提示
  msg(content) {
    uni.showToast({
      title: content,
      icon: 'none'
    })
  },
  // 错误消息
  msgError(content) {
    uni.showToast({
      title: content,
      icon: 'error'
    })
  },
  // 成功消息
  msgSuccess(content) {
    uni.showToast({
      title: content,
      icon: 'success'
    })
  },
  // 隐藏消息
  hideMsg(content) {
    uni.hideToast()
  },
  // 弹出提示
  alert(content) {
    uni.showModal({
      title: i18n.t("messages.words.text15"),
      content: content,
      showCancel: false
    })
  },
  // 确认窗体
  confirm(content) {
    return new Promise((resolve, reject) => {
      uni.showModal({
        title: i18n.t("messages.words.text12"),
        content: content,
        cancelText: i18n.t("messages.words.text14"),
        confirmText: i18n.t("messages.words.text13"),
        success: function(res) {
          if (res.confirm) {
            resolve(res.confirm)
          }
        }
      })
    })
  },
  // 提示信息
  showToast(option) {
    if (typeof option === "object") {
      uni.showToast(option)
    } else {
      uni.showToast({
        title: option,
        icon: "none",
        duration: 2500
      })
    }
  },
  // 打开遮罩层
  loading(content) {
    uni.showLoading({
      title: content,
      icon: 'none'
    })
  },
  // 关闭遮罩层
  closeLoading() {
    uni.hideLoading()
  }
}
