<!-- i18n -->
<template>
  <div>
    <el-dropdown trigger="click" @command="changeLanguage">
      <div>
        <!-- 默认显示图标 -->
        <svg-icon
          icon-class="language"
          style="color: #3f80ff; font-size: 23px; margin-top: 14px"
        />
      </div>
      <!-- 下拉菜单内容 -->
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="zh" :disabled="'zh' === $i18n.locale">
          中文
        </el-dropdown-item>
        <el-dropdown-item command="id" :disabled="'id' === $i18n.locale">
          Indonesia
        </el-dropdown-item>
        <!-- <el-dropdown-item command="en" :disabled="'en' === $i18n.locale">
          English
        </el-dropdown-item> -->
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  data () {
    return {
      selectValue: "",
      options: [
        {
          value: "zh",
          label: "中文",
        },
        {
          value: "id",
          label: "Indonesia",
        },
      ],
    }
  },
  created () {
    let that = this
    console.log("this time the language is：",localStorage.lang)
    that.selectValue =
      localStorage.lang == undefined ? "zh" : localStorage.lang
  },
  methods: {
    changeLanguage (command) {//语言切换
      localStorage.setItem("lang", command)
      this.$i18n.locale = command
      console.log("change the language to be：",localStorage.lang)
    },
  },
};
</script>
