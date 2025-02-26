<template>
	<view class="help-container content">
		<image style="width: 500rpx;" src="/static/logo.png" mode="widthFix"></image>
		<!-- 点巡检计划 -->
		<view class="text-title">
			<uni-icons type="navigate-filled" size="20"/>
			{{$t("messages.words.text39")}}
		</view>
		<view class="childList">
			<view v-for="(item, zindex) in inspectplanList" 
					:key="zindex" 
					class="question" 
					hover-class="hover" 
					@click="openSelectPup(item)"
					:style="{ backgroundColor: item.timeUp ? '#ff5e9f' : 'transparent' }">
				<view class="text-item">{{ item.name }}</view>
			</view>
		</view>
		<br>
		<!-- 维修计划 -->
		<view class="text-title">
			<uni-icons type="gear" size="20"/>
			{{$t("messages.words.text40")}}
		</view>
		<view class="childList">
2222
		</view>
		<br>
		<!-- 保养计划 -->
		<view class="text-title">
			<uni-icons type="map-pin-ellipse" size="20"/>
			{{$t("messages.words.text41")}}
		</view>
		<view class="childList">
			<view v-for="(item, zindex) in maintainplanList"
					:key="zindex" 
					class="question" 
					hover-class="hover" 
					@click="handleMaintainplan(item)">
				<view class="text-item">{{ item.name }}</view>
			</view>
		</view>
		<!-- 点巡检计划录入选择 -->
		<uni-popup ref="planSelectDialog" type="dialog" @maskClick="closeSelectPup">
			<view class="popup-content">
				<uni-title type="h3" :title="inspectPlan.name" align="center"/>
				<button style="margin-bottom: 30px;" @click="handleInspectplan(0)" type="primary">继续上轮点检</button>
				<button @click="handleInspectplan(1)" type="primary">开启新一轮点检</button>
			</view>
		</uni-popup>
		<!-- 点巡检计划详情弹出框 -->
		<uni-popup ref="inspectPlanPopup" class="my-popul">
			<view class="my-popul-view">
				<uni-title type="h1" :title="inspectPlan.name" align="center" style="background-color: aliceblue;"/>
				<uni-section class="mb-10" title='第几次执行此计划' :sub-title="String(inspectPlan.planExecuteCount)"/>
				<uni-section class="mb-10" title='此轮执行开始时间' :sub-title="inspectPlan.planStartTime"/>
				<uni-section class="mb-10" :title='$t("messages.words.text42")' :sub-title="inspectPlan.equiplocationName"/>
				<uni-section class="mb-10" title='设备' :sub-title="inspectPlan.equipName"/>
				<uni-section class="mb-10" :title='$t("messages.words.text43")' :sub-title="String(inspectPlan.inspectionCycle)"/>
				<uni-section class="mb-10" :title='$t("messages.words.text44")' :sub-title="inspectionTypeText"/>
				<uni-section class="mb-10" :title='$t("messages.words.text45")' :sub-title="inspectPlan.detail"/>
				<uni-section class="mb-10" :title='$t("messages.words.text46")' :sub-title='inspectPlan.timeUp ? $t("messages.words.text48") : $t("messages.words.text49")'/>
				<uni-section class="mb-10" :title='$t("messages.words.text47")'>
					<view v-for="(item, zindex) in inspectplanSubstanceList" 
						:key="zindex" 
						class="question" 
						hover-class="hover">
						<uni-section class="my-xxplan-detail mb-10" :title="item.equipName" :sub-title="item.details"/>
					</view>
				</uni-section>
				<button class="button" type="primary" @click="addProfile">{{$t("messages.words.text50")}}</button>
			</view>
		</uni-popup>
		<!-- 保养计划详情弹出框 -->
		<uni-popup ref="maintainPlanPopup" class="my-popul">
			<view class="my-popul-view">
				<uni-title type="h1" :title="maintainPlan.name" align="center" style="background-color: aliceblue;"/>
				<uni-section class="mb-10" title='执行部门名称' :sub-title="maintainPlan.executeDeptName"/>
				<uni-section class="mb-10" title='保养地区' :sub-title="maintainPlan.equiplocationName"/>
				<uni-section class="mb-10" title='备注' :sub-title="maintainPlan.remark"/>
				<view v-for="(item, zindex) in maintainDetailList"
					:key="zindex" 
					class="question" 
					hover-class="hover">
					<uni-section class="my-xxplan-detail mb-10" :title="item.details" :sub-title="item.maintainCycle+'(h)'">
						<view class="detail-label">是否特殊设备：</view>
						<view>{{ item.isSpecialName }}</view><br>
						<view class="detail-label">是否有参照对象：</view>
						<view>{{ item.isRefertoName }}</view><br>
						<view class="detail-label">设备：</view>
						<button size="mini" @click="openEquipprofilePup(item.equipprofileCodes,item.equipName,item.details)">查看详情</button><br>
						<view class="detail-label">设备规格：</view>
						<view>{{ item.equipSpecification }}</view><br>
						<button class="button" type="primary" @click="addMaintainProfile(item)">{{$t("messages.words.text50")}}</button>
					</uni-section>
				</view>
			</view>
		</uni-popup>
		<!-- 保养设备详情弹出框 -->
		<uni-popup ref="equipprofileDialog" type="dialog" @maskClick="closeEquipprofilePup">
			<view class="popup-content popup-content-equipprofile">
				<uni-title type="h3" :title="maintainDetailSubstance" align="center"/>
				<view v-for="(item, index) in equipprofilenameList" :key="index">
					{{item}}
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	import * as InspectPlanApi from '@/api/pages/inspectplan.js';
	import * as MaintainPlanApi from '@/api/pages/maintainplan.js'
	import DateTools from "@/utils/mx-datepicker-dateTools.js"
	export default {
		onLoad: function() {},
		data() {
			return {
				InspectplanQueryParams: {
					timeUp: false,
					inspectionType: undefined,
					inspectionCycle: undefined,
					name: undefined,
					equiplocationId: undefined,
					equiplocationName: undefined,
					detail: undefined
				},
				MaintainplanQueryParams: {
					name: undefined,
					executeDeptName: undefined,
					status: 0,
					remark: undefined,
				},
				inspectplanList: [],
				inspectPlan: {},
				inspectplanSubstanceList: [],
				maintainplanList: [],
				maintainPlan: {},
				maintainDetailList: [],
				equipprofilenameList: [],
				maintainDetailSubstance: undefined,
			}
		},
		onShow() {
		    // 使用 i18n 设置导航栏标题
		    uni.setNavigationBarTitle({
		      title: this.$t("messages.words.text18")
		    });
		},
		computed: {
		    inspectionTypeText() {
		      switch (this.inspectPlan.inspectionType) {
		        case 1:
		          return '普通点检';
		        case 2:
		          return '重点点检';
		        case 3:
		          return '专项点检';
		        case 4:
		          return '拆检';
		        default:
		          return '未知点检类型'; 
		      }
		    }
		},
		created() {
			this.getList();
		},
		methods: {
			openSelectPup(item){
				this.inspectPlan = item;
				this.$refs.planSelectDialog.open();
			},
			closeSelectPup(){
				this.inspectPlan = {};
				this.$refs.planSelectDialog.close();
			},
			async handleInspectplan(addNum) {
				const planProfile = await InspectPlanApi.getPlanNewestExecuteTimeByPlanId(this.inspectPlan.id);
				let nowNum = 0;
				if(planProfile.data === null || planProfile.data === '' || planProfile.data === undefined){
					if(addNum === 0){
						this.$modal.showToast('该计划无上一轮');
						return;
					}else{
						nowNum = addNum;
					}
				}else{
					nowNum = addNum + planProfile.data.planExecuteCount;
				}
				this.$set(this.inspectPlan,'planExecuteCount',nowNum);
				this.$set(this.inspectPlan,'planStartTime', addNum === 0 ? DateTools.format(new Date(planProfile.data.inspectionDate),"yyyy-mm-dd hh:ii:ss") : '新一轮计划执行，暂无执行时间')
				const substanceList = await InspectPlanApi.getInspectSubstanceListByPlanId(this.inspectPlan.id);
				this.inspectplanSubstanceList = substanceList.data;
				this.$refs.inspectPlanPopup.open('left');
			},
			async getList(){
				const inspectplanRes = await InspectPlanApi.getInspectPlanList(this.InspectplanQueryParams);
				this.inspectplanList = inspectplanRes.data;
				const maintainplanRes = await MaintainPlanApi.getMaintainPlanList(this.MaintainplanQueryParams);
				this.maintainplanList = maintainplanRes.data;
			},
			addProfile(){
				uni.setStorageSync("plan",this.inspectPlan);
				uni.setStorageSync("planDetail",this.inspectplanSubstanceList);
				this.$tab.navigateTo('/pages/oam/inspectProfileForm');
			},
			async handleMaintainplan(item) {
				this.maintainPlan = item;
				const params = {
					equipMaintainPlanId: item.id,
				}
				const maintaindetailRes = await MaintainPlanApi.getMaintainDetailList(params);
				this.maintainDetailList = maintaindetailRes.data;
				this.$refs.maintainPlanPopup.open('left');
			},
			openEquipprofilePup(codes,name,detail){
				this.equipprofilenameList = [];
				this.maintainDetailSubstance = undefined;
				const codeList = codes.split("-_-");
				codeList.forEach(c => {
					this.equipprofilenameList.push(c+name);
				})
				this.maintainDetailSubstance = detail;
				this.$refs.equipprofileDialog.open();
			},
			closeEquipprofilePup(){
				this.$refs.equipprofileDialog.close();
			},
			addMaintainProfile(maintainDetail){
				this.$modal.showToast('模块建设中~')
				uni.setStorageSync("maintainPlan",this.maintainPlan);
				uni.setStorageSync("maintainDetail",maintainDetail);
				this.$tab.navigateTo('/pages/oam/maintainProfileForm');
			}
		}
	}
</script>

<style>
  .content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .logo {
    height: 200rpx;
    width: 200rpx;
    margin-top: 200rpx;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 50rpx;
  }

  .text-area {
    display: flex;
    justify-content: center;
  }

  .title {
    font-size: 36rpx;
    color: #8f8f94;
  }
  
  .help-container {
    margin-bottom: 100rpx;
    padding: 30rpx;
  }
  
  .list-title {
    margin-bottom: 30rpx;
  }
  
  .childList {
    background: #ffffff;
    box-shadow: 0px 0px 10rpx rgba(193, 193, 193, 0.2);
    border-radius: 16rpx;
    margin-top: 10rpx;
	width: 700rpx;
	max-height: 300rpx; 
	overflow-y: auto;
  }
  
  .line {
    width: 100%;
    height: 1rpx;
    background-color: #F5F5F5;
  }
  
  .text-title {
    color: #303133;
    font-size: 32rpx;
    font-weight: bold;
    margin-left: 10rpx;
  
    .iconfont {
      font-size: 16px;
      margin-right: 10rpx;
    }
  }
  
  .text-item {
    font-size: 28rpx;
    padding: 24rpx;
  }
  
  .question {
    color: #606266;
    font-size: 28rpx;
  }
  
  .my-popul {
	border-radius: "0 20px 20px 0 !important";
	background-color: "#fff";
  }
  
  .my-popul-view {
	width: 600rpx;
	max-height: 90vh;
	overflow-y: auto;
  }
  
  .my-xxplan-detail {
	background-color: #ddf0d8;
	border: 1px solid #ddd;
	padding: 10px;
	margin-bottom: 10px;
  }
  
  .detail-label {
	font-weight: bold; /* 字体加粗 */
	font-family: "KaiTi", "楷体", serif; /* 设置楷体字体 */
	font-size: 18px; /* 假设普通的view字号是14px，这里比普通view大4px */
  }
  
  .popup-content {
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    text-align: center;
	width: 50vw;
	margin: 0 auto;
  }
  
  .popup-content.popup-content-equipprofile {
	text-align: left;
	width: 77vw;
  }
  .popup-content.popup-content-equipprofile > view {
    margin-bottom: 10px; /* 每行间距 */
  }
</style>