<template>
  <view class="work-container">
    <!-- 轮播图 -->
    <uni-swiper-dot class="uni-swiper-dot-box" :info="data" :current="current" field="content">
      <swiper class="swiper-box" :current="swiperDotIndex" @change="changeSwiper">
        <swiper-item v-for="(item, index) in data" :key="index">
          <view class="swiper-item">
            <image :src="item.image" mode="aspectFill" :draggable="false" />
          </view>
        </swiper-item>
      </swiper>
    </uni-swiper-dot>

    <!-- 宫格组件 -->
    <uni-section :title='$t("messages.words.text18")' type="line"></uni-section>
    <view class="grid-body">
      <uni-grid :column="3" :showBorder="false">
        <uni-grid-item>
          <view class="grid-item-box" @click="changeGrid">
            <uni-icons type="person-filled" size="100rpx"></uni-icons>
            <text class="text">点检计划查看</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box" @click="changeGrid">
            <uni-icons type="staff-filled" size="100rpx"></uni-icons>
            <text class="text">维修计划查看</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box" @click="changeGrid">
            <uni-icons type="color" size="100rpx"></uni-icons>
            <text class="text">保养计划查看</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box" @click="changeGrid">
            <uni-icons type="settings-filled" size="100rpx"></uni-icons>
            <text class="text">点检日志</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box" @click="changeGrid">
            <uni-icons type="heart-filled" size="100rpx"></uni-icons>
            <text class="text">维修日志</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box" @click="changeGrid">
            <uni-icons type="bars" size="100rpx"></uni-icons>
            <text class="text">保养日志</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box" @click="changeGrid">
            <uni-icons type="gear-filled" size="100rpx"></uni-icons>
            <text class="text">缺陷库</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box" @click="changeGrid">
            <uni-icons type="chat-filled" size="100rpx"></uni-icons>
            <text class="text">停机库</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box" @click="scanEquipmentprofile">
            <uni-icons type="scan" size="100rpx"></uni-icons>
            <text class="text">设备查看</text>
          </view>
        </uni-grid-item>
		
		<uni-grid-item>
			<!-- todo 此块需要安卓机实测，较浪费时间，最后搞 -->
		  <view class="grid-item-box">
		    <uni-icons type="scan" size="100rpx"></uni-icons>
		    <text class="text">扫描结果：{{t1}}</text>
			<text class="text">类型是：{{t2}}</text>
		  </view>
		</uni-grid-item>
      </uni-grid>
    </view>
  </view>
</template>

<script>
  export default {
    data() {
      return {
        current: 0,
        swiperDotIndex: 0,
        data: [{
            image: '/static/images/banner/banner01.jpg'
          },
          {
            image: '/static/images/banner/banner02.jpg'
          },
          {
            image: '/static/images/banner/banner03.jpg'
          }
        ],
		t1:"初始值",
		t2:"初始",
      }
    },
	onShow() {
	    // 使用 i18n 设置导航栏标题
	    uni.setNavigationBarTitle({
	      title: this.$t("messages.words.text19")
	    });
	},
    methods: {
      changeSwiper(e) {
        this.current = e.detail.current
      },
      changeGrid(e) {
        this.$modal.showToast('模块建设中~--')
      },
	  scanEquipmentprofile(){
		  console.log("进来1")
		  uni.scanCode({
		  	success: function (res) {
				console.log("初始内容",res);
		  		console.log('条码类型：' + res.scanType);
		  		console.log('条码内容：' + res.result);
				this.t1 = res.result;
				this.t2 = res.scanType;
				console.log("现在t1是",this.t1)
				console.log("现在t2是",this.t2)
		  	}
		  });
	  }
    }
  }
</script>

<style lang="scss">
  /* #ifndef APP-NVUE */
  page {
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    background-color: #fff;
    min-height: 100%;
    height: auto;
  }

  view {
    font-size: 14px;
    line-height: inherit;
  }

  /* #endif */

  .text {
    text-align: center;
    font-size: 26rpx;
    margin-top: 10rpx;
  }

  .grid-item-box {
    flex: 1;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 15px 0;
  }

  .uni-margin-wrap {
    width: 690rpx;
    width: 100%;
    ;
  }

  .swiper {
    height: 300rpx;
  }

  .swiper-box {
    height: 150px;
  }

  .swiper-item {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #fff;
    height: 300rpx;
    line-height: 300rpx;
  }

  @media screen and (min-width: 500px) {
    .uni-swiper-dot-box {
      width: 400px;
      /* #ifndef APP-NVUE */
      margin: 0 auto;
      /* #endif */
      margin-top: 8px;
    }

    .image {
      width: 100%;
    }
  }
</style>
