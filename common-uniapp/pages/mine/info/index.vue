<template>
  <view class="container">
    <uni-list>
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'person-filled'}" :title='$t("messages.words.text26")' :rightText="user.nickname" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'phone-filled'}" :title='$t("messages.words.text27")' :rightText="user.mobile" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'email-filled'}" :title='$t("messages.words.text28")' :rightText="user.email" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'auth-filled'}" :title='$t("messages.words.text30")' :rightText="(user.posts || []).map(post => post.name).join(',')" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'staff-filled'}" :title='$t("messages.words.text31")' :rightText="(user.roles || []).map(role => role.name).join(',')" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'calendar-filled'}" :title='$t("messages.words.text32")' :rightText="this.parseTime(user.createTime)" />
    </uni-list>
  </view>
</template>

<script>
  import { getUserProfile } from "@/api/system/user"
  import { parseTime } from "@/utils/ruoyi"

  export default {
    data() {
      return {
        user: {}
      }
    },
	onShow() {
	    // 使用 i18n 设置导航栏标题
	    uni.setNavigationBarTitle({
	      title: this.$t("messages.words.text22")
	    });
	},
    onLoad() {
      this.getUser()
    },
    methods: {
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data
        })
      },
      parseTime(time) {
        return parseTime(time)
      }
    }
  }
</script>

<style lang="scss">
  page {
    background-color: #ffffff;
  }
</style>
