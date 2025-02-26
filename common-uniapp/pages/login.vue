<template>
	<view class="normal-login-container">
		<view class="logo-content align-center justify-center flex">
			<image style="width: 500rpx;" :src="globalConfig.appInfo.logo" mode="widthFix">
			</image>
		</view>
		<view class="login-form-content">
			<view class="input-item flex align-center">
				<view class="iconfont icon-user icon"></view>
				<input v-model="loginForm.username" class="input" type="text" :placeholder='$t("messages.words.text3")' maxlength="30" />
			</view>
			<view class="input-item flex align-center">
				<view class="iconfont icon-password icon"></view>
				<input v-model="loginForm.password" type="password" class="input" :placeholder='$t("messages.words.text4")' maxlength="20" />
			</view>
			<Verify @success="pwdLogin" :mode="'pop'" :captchaType="'blockPuzzle'"
				:imgSize="{ width: '330px', height: '155px' }" ref="verify"></Verify>
			<view class="action-btn">
				<button @click="handleLogin" class="login-btn cu-btn block bg-blue lg round">{{ $t("messages.words.login") }}</button>
			</view>
		</view>
	</view>
</template>

<script>
	import Verify from "@/components/verifition/Verify"
	export default {
		name: 'Login',
		components: {
			Verify
		},
		data() {
			return {
				captchaEnabled: true, // 验证码开关 TODO 
				globalConfig: getApp().globalData.config,
				loginForm: {
					username: "aaaaa",
					password: "12345",
					captchaVerification: ""
				}
			}
		},
		onShow() {
		    // 使用 i18n 设置导航栏标题
		    uni.setNavigationBarTitle({
		      title: this.$t("messages.words.text17")
		    });
		},
		methods: {
			// 登录方法
			async handleLogin(params) {
				if (this.loginForm.username === "") {
					this.$modal.msgError(this.$t("messages.words.text3"))
				} else if (this.loginForm.password === "") {
					this.$modal.msgError(this.$t("messages.words.text4"))
				} else {
          // 显示验证码
				if (this.captchaEnabled) {
					this.$refs.verify.show()
				  } else { // 直接登录
					await this.pwdLogin({})
				  }
				}
			},
			// 密码登录
			async pwdLogin(captchaParams) {
				this.$modal.loading(this.$t("messages.words.text5"))
				// 执行登录
				this.loginForm.captchaVerification = captchaParams.captchaVerification
				this.$store.dispatch('Login', this.loginForm).then(() => {
					this.$modal.closeLoading()
					this.loginSuccess()
				})
			},
			// 登录成功后，处理函数
			loginSuccess(result) {
				// 设置用户信息
				this.$store.dispatch('GetInfo').then(res => {
					this.$tab.reLaunch('/pages/index')
				})
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #ffffff;
	}

	.normal-login-container {
		width: 100%;

		.logo-content {
			width: 100%;
			font-size: 21px;
			text-align: center;
			padding-top: 15%;

			image {
				border-radius: 4px;
			}

			.title {
				margin-left: 10px;
			}
		}

		.login-form-content {
			text-align: center;
			margin: 20px auto;
			margin-top: 15%;
			width: 80%;

			.input-item {
				margin: 20px auto;
				background-color: #f5f6f7;
				height: 45px;
				border-radius: 20px;

				.icon {
					font-size: 38rpx;
					margin-left: 10px;
					color: #999;
				}

				.input {
					width: 100%;
					font-size: 14px;
					line-height: 20px;
					text-align: left;
					padding-left: 15px;
				}

			}

			.login-btn {
				margin-top: 40px;
				height: 45px;
			}

			.xieyi {
				color: #333;
				margin-top: 20px;
			}
		}

		.easyinput {
			width: 100%;
		}
	}

	.login-code-img {
		height: 45px;
	}
</style>
