<template xmlns="">
  <div class="container">
    <div class="logo"></div>
    <!-- 登录区域 -->
    <div class="content">
      <!-- 配图 -->
      <div class="pic"></div>
      <!-- 表单 -->
      <div class="field">
        <!-- [移动端]标题 -->
        <h2 class="mobile-title">
          <h3 class="title">通用模板</h3>
        </h2>

        <!-- 表单 -->
        <div class="form-cont">
          <!-- <el-tabs class="form" v-model="loginForm.loginType" style=" float:none;">
            <el-tab-pane label="账号密码登录" name="uname">
            </el-tab-pane>
          </el-tabs> -->
          <div>
            <el-form ref="loginForm" :model="loginForm" :rules="LoginRules" class="login-form">
              <el-form-item prop="tenantName" v-if="tenantEnable">
                <el-input v-model="loginForm.tenantName" type="text" auto-complete="off" placeholder='租户'>
                  <svg-icon slot="prefix" icon-class="tree" class="el-input__icon input-icon"/>
                </el-input>
              </el-form-item>
              <!-- 账号密码登录 -->
              <div >
                <el-form-item prop="username">
                  <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
                    <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
                  </el-input>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"
                            @keyup.enter.native="getCode">
                    <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
                  </el-input>
                </el-form-item>
                <el-checkbox v-model="loginForm.rememberMe" style="margin:0 0 25px 0;">记住密码</el-checkbox>
              </div>

              <!-- 下方的登录按钮 -->
              <el-form-item style="width:100%;">
                <el-button :loading="loading" size="medium" type="primary" style="width:100%;"
                    @click.native.prevent="getCode">
                  <span v-if="!loading">登 录</span>
                  <span v-else>登 录 中...</span>
                </el-button>
              </el-form-item>
            </el-form> 
          </div>
        </div>
      </div>
    </div>

    <!-- 图形验证码 -->
    <Verify ref="verify" :captcha-type="'blockPuzzle'" :img-size="{width:'400px',height:'200px'}"
            @success="handleLogin" />

    <!-- footer -->
    <div class="footer">
      Copyright © 2020-2022 iocoder.cn All Rights Reserved.
    </div>
  </div>
</template>

<script>
import {socialAuthRedirect} from "@/api/login";
import {getTenantIdByName} from "@/api/system/tenant";
import {SystemUserSocialTypeEnum} from "@/utils/constants";
import {getCaptchaEnable, getTenantEnable} from "@/utils/ruoyi";
import {
  getPassword,
  getRememberMe, getTenantName,
  getUsername,
  removePassword, removeRememberMe, removeTenantName,
  removeUsername,
  setPassword, setRememberMe, setTenantId, setTenantName,
  setUsername
} from "@/utils/auth";

import Verify from '@/components/Verifition/Verify';

export default {
  name: "Login",
  components: {
    Verify
  },
  data() {
    return {
      codeUrl: "",
      captchaEnable: true,
      tenantEnable: true,
      loginForm: {
        loginType: "uname",
        username: "admin",
        password: "admin123",
        captchaVerification: "",
        rememberMe: false,
        tenantName: "通用系统",
      },
      scene: 21,

      LoginRules: {
        username: [
          {required: true, trigger: "blur", message: "用户名不能为空"}
        ],
        password: [
          {required: true, trigger: "blur", message: "密码不能为空"}
        ],
        tenantName: [
          {required: true, trigger: "blur", message: "租户不能为空"},
          {
            validator: (rule, value, callback) => {
              // debugger
              getTenantIdByName(value).then(res => {
                const tenantId = res.data;
                if (tenantId && tenantId >= 0) {
                  // 设置租户
                  setTenantId(tenantId)
                  callback();
                } else {
                  callback('租户不存在');
                }
              });
            },
            trigger: 'blur'
          }
        ]
      },
      loading: false,
      redirect: undefined,
      // 枚举
      SysUserSocialTypeEnum: SystemUserSocialTypeEnum,
    };
  },
  created() {
    console.log("是这个页面")
    // 租户开关
    this.tenantEnable = getTenantEnable();
    if (this.tenantEnable) {
      getTenantIdByName(this.loginForm.tenantName).then(res => { // 设置租户
        const tenantId = res.data;
        if (tenantId && tenantId >= 0) {
          setTenantId(tenantId)
        }
      });
    }
    // 验证码开关
    this.captchaEnable = getCaptchaEnable();
    // 重定向地址
    this.redirect = this.$route.query.redirect ? decodeURIComponent(this.$route.query.redirect) : undefined;
    this.getCookie();
  },
  methods: {
    getCode() {
      // 情况一，未开启：则直接登录
      if (!this.captchaEnable) {
        this.handleLogin({})
        return;
      }

      // 情况二，已开启：则展示验证码；只有完成验证码的情况，才进行登录
      // 弹出验证码
      this.$refs.verify.show()
    },
    getCookie() {
      const username = getUsername();
      const password = getPassword();
      const rememberMe = getRememberMe();
      const tenantName = getTenantName();
      this.loginForm = {
        ...this.loginForm,
        username: username ? username : this.loginForm.username,
        password: password ? password : this.loginForm.password,
        rememberMe: rememberMe ? getRememberMe() : false,
        tenantName: tenantName ? tenantName : this.loginForm.tenantName,
      };
    },
    handleLogin(captchaParams) {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          // 设置 Cookie
          if (this.loginForm.rememberMe) {
            setUsername(this.loginForm.username)
            setPassword(this.loginForm.password)
            setRememberMe(this.loginForm.rememberMe)
            setTenantName(this.loginForm.tenantName)
          } else {
            removeUsername()
            removePassword()
            removeRememberMe()
            removeTenantName()
          }
          this.loginForm.captchaVerification = captchaParams.captchaVerification
          // 发起登陆
          // console.log("发起登录", this.loginForm);
          this.$store.dispatch(this.loginForm.loginType === "sms" ? "SmsLogin" : "Login", this.loginForm).then(() => {
            this.$router.push({path: this.redirect || "/"}).catch(() => {
            });
          }).catch(() => {
            this.loading = false;
          });
        }
      });
    },
    async doSocialLogin(socialTypeEnum) {
      // 设置登录中
      this.loading = true;
      let tenant = false;
      if (this.tenantEnable) {
        await this.$prompt('请输入租户名称', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(async ({value}) => {
          await getTenantIdByName(value).then(res => {
            const tenantId = res.data;
            tenant = true
            if (tenantId && tenantId >= 0) {
              setTenantId(tenantId)
            }
          });
        }).catch(() => {
          // 取消登录按钮 loading状态
          this.loading = false;

          return false
        });
      } else {
        tenant = true
      }
     if(tenant){
       // 计算 redirectUri
       const redirectUri = location.origin + '/social-login?'
         + encodeURIComponent('type=' + socialTypeEnum.type + '&redirect=' + (this.redirect || "/")); // 重定向不能丢
       // const redirectUri = 'http://127.0.0.1:48080/api/gitee/callback';
       // const redirectUri = 'http://127.0.0.1:48080/api/dingtalk/callback';
       // 进行跳转
       socialAuthRedirect(socialTypeEnum.type, encodeURIComponent(redirectUri)).then((res) => {
         // console.log(res.url);
         window.location.href = res.data;
       });
     }
    },
  }
};
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/login.scss";
</style>
