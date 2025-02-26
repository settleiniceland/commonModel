<template>
  <view class="setting-container" :style="{height: `${windowHeight}px`}">
    <view class="menu-list">
      <view class="list-cell list-cell-arrow" @click="handleToPwd">
        <view class="menu-item-box">
			<uni-icons type="locked-filled" size="25" color="blue"></uni-icons>
			<view>{{$t("messages.words.text6")}}</view>
        </view>
      </view>
      <view class="list-cell" @click="changeLanguage">
        <view class="menu-item-box">
			<uni-icons type="font" size="25" color="blue"></uni-icons>
			<view>{{$t("messages.words.text8")}}</view>
        </view>
      </view>
    </view>
    <view class="cu-list menu">
      <view class="cu-item item-box">
        <view class="content text-center" @click="handleLogout">
          <text class="text-black">{{$t("messages.words.text9")}}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
  export default {
    data() {
      return {
        windowHeight: uni.getSystemInfoSync().windowHeight
      }
    },
	onShow() {
	    // 使用 i18n 设置导航栏标题
	    uni.setNavigationBarTitle({
	      title: this.$t("messages.words.text24")
	    });
	},
    methods: {
      handleToPwd() {
        this.$tab.navigateTo('/pages/mine/pwd/index')
      },
      handleToUpgrade() {
        this.$modal.showToast('模块建设中~')
      },
      changeLanguage() {
		if(localStorage.lang!==null&&localStorage.lang === 'zh'){
			localStorage.setItem("lang",'id');
			this.$i18n.locale = 'id';
		}else{
			localStorage.setItem("lang",'zh');
			this.$i18n.locale = 'zh';
		}
		// 重新设置 tabBar 文本【直接跳转到“我的”页面，因为此页面没有tabBar，无法改其text】
		uni.reLaunch({
		    url: '/pages/mine/index',
			success: () => {
				this.$setTabBarText();
			}
		});
      },
      handleLogout() {
        this.$modal.confirm(this.$t("messages.words.text10")).then(() => {
          this.$store.dispatch('LogOut').then(() => {
            this.$tab.reLaunch('/pages/index')
          })
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .page {
    background-color: #f8f8f8;
  }

  .item-box {
    background-color: #FFFFFF;
    margin: 30rpx;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 10rpx;
    border-radius: 8rpx;
    color: #303133;
    font-size: 32rpx;
  }
</style>
