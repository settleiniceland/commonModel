<template>
	<view style="margin: 0 5%; max-height: 90vh; overflow-y: auto;">
		<view class="uni-box-head">
			<uni-title type="h1" align="center" :title="commonDetail.inspectionPlanName"></uni-title>
		</view>
		<uni-section title="基础信息" type="line">
			<uni-card>
				<uni-forms :modelValue="commonDetail">
					<uni-forms-item label="设备">
						<input class="input-border" type="text" v-model="commonDetail.equipName" disabled/>
					</uni-forms-item>
					<uni-forms-item label="点检日期" name="inspectionDate">
						<button class="input-border" @click="showDatePicker = !showDatePicker">{{commonDetail.inspectionDate?commonDetail.inspectionDate:"请选择时间"}}</button>
					</uni-forms-item>
					<uni-forms-item label="点检人" name="inspectionUsers">
						<input class="input-border" type="text" v-model="commonDetail.inspectionUsers" placeholder="请输入点检人" />
					</uni-forms-item>
					<uni-forms-item label="区域负责人" name="dutyUsers">
						<input class="input-border" type="text" v-model="commonDetail.dutyUsers" placeholder="请输入区域负责人" disabled/>
					</uni-forms-item>
					<uni-forms-item label="具体设备" name="equipProfileId">
						<button class="input-border" @click="showPicker">{{commonDetail.equipProfileId?commonDetail.equipCode+commonDetail.equipProfileName:'请选择设备'}}</button>
					</uni-forms-item>
					<uni-forms-item label="是否停机" name="isStop">
						<uni-data-checkbox mode="button" style="background-color: bisque;" v-model="commonDetail.isStop" :localdata="statusType"/>
					</uni-forms-item>
				</uni-forms>
			</uni-card>
		</uni-section>
		<uni-section title="结果信息" type="line">
			<uni-card>
				<view v-for="(item, index) in planDetailProfileList" :key="index">
					<uni-forms :modelValue="item">
						<uni-forms-item label="点检内容" name="details">
							<text>{{item.details}}</text>
						</uni-forms-item>
						<uni-forms-item label="结果" name="result">
							<uni-data-checkbox mode="button" style="background-color: bisque;" v-model="item.result" :localdata="resultType"/>
						</uni-forms-item>
						<uni-forms-item v-if="commonDetail.equipCode !== ''" label="现场图片" name="resultPhotos">
							<uni-section title="调用相机拍照上传图片" type="line" style="background-color: bisque;">
								<!-- todo 此块需要安卓机实测，较浪费时间，最后搞 -->
								<view class="example-body">
									<uni-file-picker 
										ref="filePicker" 
										limit="9" 
										title="最多选择9张图片"
										:auto-upload="false"
										@select="(event) => addfile(event, item.id)"
										@delete="(event) => delfile(event, item.id)"
									/>
								</view>
							</uni-section>
						</uni-forms-item>
						<uni-forms-item label="结果详情" name="resultDetail">
							<textarea rows="9" class="textarea-border" v-model="item.resultDetail" placeholder="请输入详情"></textarea>
						</uni-forms-item>
					</uni-forms>
					<hr><br>
				</view>
			</uni-card>
		</uni-section>
		<button type="primary" @click="submit">Submit</button>
		<ba-tree-picker ref="treePicker" :multiple='false' @select-change="selectChange" title="选择具体设备"
			valueKey="id" textKey2="equipName" textKey1="code" textKey3="equipAttribute" childrenKey="children" />
		<mx-date-picker :show="showDatePicker" type="datetime" :showHoliday="false" :show-tips="true" 
			:show-seconds="true" @confirm="ed" @cancel="ed" />
	</view>
</template>

<script>
	import baTreePicker from "@/components/ba-tree-picker/ba-tree-picker.vue"
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import * as InspectPlanApi from '@/api/pages/inspectplan.js';
	import { handleTreeForString } from "@/utils/ruoyi"
	import { initResumableAndUpload } from "@/utils/equip.js"
	import DateTools from "@/utils/mx-datepicker-dateTools.js"
	export default{
		components: {baTreePicker,MxDatePicker},
		data(){
			return {
				planDetailProfileList: [],
				commonDetail:{
					planExecuteCount: undefined,
					equipId: undefined,
					equipName: undefined,
					inspectionPlanId: undefined,
					inspectionPlanName: undefined,
					inspectionType: undefined,
					inspectionCycle: undefined,
					equiplocationId: undefined,
					equiplocationName: undefined,
					equipProfileId: undefined,
					equipProfileName: undefined,
					equipCode: undefined,
					equipAttribute: undefined,
					isStop: 1,
					inspectionDate:undefined,
					inspectionUsers:undefined,
					dutyUsers:undefined,
				},
				showDatePicker: false,
				resultType:[
					{value:1,text:'正常'},
					{value:2,text:'异常'}
				],
				statusType:[
					{value:1,text:'开机'},
					{value:2,text:'停机'}
				],
				equipProfileTree: [],//待选项设备档案树形结构（由于这里都是单体设备，所以最后一定是list数组格式）
				selectedFilesMap: new Map([]),//要上传的图片文件[key是内容编码，value就是图片文件数组]
			}
		},
		onShow() {
		    // 使用 i18n 设置导航栏标题
		    uni.setNavigationBarTitle({
		      title: this.$t("messages.words.text16")
		    });
		},
		async created(){
			this.commonDetail.planExecuteCount = uni.getStorageSync("plan").planExecuteCount;
			this.commonDetail.equipId = uni.getStorageSync("plan").equipId;
			this.commonDetail.equipName = uni.getStorageSync("plan").equipName;
			this.commonDetail.inspectionPlanId = uni.getStorageSync("plan").id;
			this.commonDetail.inspectionPlanName = uni.getStorageSync("plan").name;
			this.commonDetail.inspectionType = uni.getStorageSync("plan").inspectionType;
			this.commonDetail.inspectionCycle = uni.getStorageSync("plan").inspectionCycle;
			this.commonDetail.equiplocationId = uni.getStorageSync("plan").equiplocationId;
			this.commonDetail.equiplocationName = uni.getStorageSync("plan").equiplocationName;
			this.commonDetail.inspectionDate = DateTools.format(new Date(),"yyyy-mm-dd hh:ii:ss");
			this.commonDetail.dutyUsers = uni.getStorageSync("plan").equiplocationDutyName;
			this.planDetailProfileList = await this.buildPlanDetailProfile(uni.getStorageSync("planDetail"));
			const epRes = await InspectPlanApi.getEquipmentProfile(this.commonDetail.equiplocationId,this.commonDetail.equipId);
			this.equipProfileTree = handleTreeForString(epRes.data,'id','supId');
		},
		methods: {
			// 显示选择器
			showPicker() {
				this.$refs.treePicker._show(undefined,this.equipProfileTree);
			},
			//监听选择
			selectChange(item,index) {
				this.commonDetail.equipProfileId = item.id;
				this.commonDetail.equipProfileName = item.equipName;
				this.commonDetail.equipCode = item.code;
				this.commonDetail.equipAttribute = item.attribute
			},
			//选择
			ed(e) {
				this.showDatePicker = false;
				if(e) {
					this.commonDetail.inspectionDate = e.value;
				}
			},
			async buildPlanDetailProfile(planDetail){
				for(let i=0;i<planDetail.length;i++){
					this.$set(planDetail[i],'result','');
					this.$set(planDetail[i],'resultPhotos','');
					this.$set(planDetail[i],'resultDetail','');
				}
				return planDetail;
			},
			addfile(e,did){
				if(this.selectedFilesMap.has(did)){
					this.selectedFilesMap.get(did).push(...e.tempFiles);
				}else{
					this.selectedFilesMap.set(did,e.tempFiles);
				}
			},
			delfile(e,did){
				if(this.selectedFilesMap.has(did)&&this.selectedFilesMap.get(did).length!==0){
					this.selectedFilesMap.set(
						did,
						this.selectedFilesMap.get(did).filter(item => item.uuid !== e.tempFile.uuid)
					);
				}else{
					this.$modal.showToast({
						title: 'Some thing error in del file',
						icon: 'error',
						duration: 2000,
					});
				}
			},
			async submit(){
				const inspectionProfileList = [];
				for (const planDetailProfile of this.planDetailProfileList) {
					let rp='';
					let errorMsg='未填写完整，请填写完整';
					if(this.selectedFilesMap.has(planDetailProfile.id)&&this.selectedFilesMap.get(planDetailProfile.id).length!==0){
						rp='baik baik saja';
					}
					const inspectionProfile = {
						planExecuteCount: this.commonDetail.planExecuteCount,
						inspectionPlanId: this.commonDetail.inspectionPlanId,
						inspectionPlanName: this.commonDetail.inspectionPlanName,
						inspectionType: this.commonDetail.inspectionType,
						inspectionCycle: this.commonDetail.inspectionCycle,
						equiplocationId: this.commonDetail.equiplocationId,
						equiplocationName: this.commonDetail.equiplocationName,
						inspectionDetailId: planDetailProfile.id,
						inspectionDetail: planDetailProfile.details,
						equipProfileId: this.commonDetail.equipProfileId,
						equipProfileName: this.commonDetail.equipProfileName,
						equipCode: this.commonDetail.equipCode,
						equipAttribute: this.commonDetail.equipAttribute,
						resultPhotos: rp,
						isStop: this.commonDetail.isStop,
						result: planDetailProfile.result,
						resultDetail: planDetailProfile.resultDetail,
						inspectionDate: this.commonDetail.inspectionDate,
						inspectionUsers: this.commonDetail.inspectionUsers,
						dutyUsers: this.commonDetail.dutyUsers,
					};
					if(Object.values(inspectionProfile).some((value, index) => {
						const key = Object.keys(inspectionProfile)[index];
						if(key === "resultDetail"){//如果点检结果异常的话，点检详情必须填相关信息
							if((value === null || value === '' || value === undefined)
								&&
								inspectionProfile.result === 2){
								errorMsg = "<"+inspectionProfile.inspectionDetail+">点检结果异常,请填写详情";
								return true;
							}
						}else if(key === "resultPhotos"){//如果是拆检或有点巡检项结果异常的话，必须拍照片
							if((value === null || value === '' || value === undefined)
								&&
								(inspectionProfile.inspectionType ===4 || inspectionProfile.result === 2)){
								errorMsg = inspectionProfile.inspectionPlanName+":为拆检计划或点检项<"+inspectionProfile.inspectionDetail+">结果异常，必须拍照";
								return true;
							}
						}else{//其他情况均不能为空
							if(value === null || value === '' || value === undefined){
								return true;
							}
						}
						return false;
					})){
						uni.showToast({
						  title: errorMsg,
						  icon: 'none',
						  duration: 10000
						});
						return;
					}else{
						inspectionProfileList.push(inspectionProfile);
					}
				}
				if(inspectionProfileList.length === 0){
					uni.showToast({
					  title: errorMsg,
					  icon: 'none',
					  duration: 10000
					});
					return;
				}
				try{
					for(const inspectionProfile of inspectionProfileList){
						//更改原逻辑，只有当全form都校验通过时才上传图片，节省文件服务器空间
						if(this.selectedFilesMap.has(inspectionProfile.inspectionDetailId)&&this.selectedFilesMap.get(inspectionProfile.inspectionDetailId).length!==0){
							let rp = '';
							let warn = '';
							await initResumableAndUpload(this.selectedFilesMap.get(inspectionProfile.inspectionDetailId),inspectionProfile.equipCode)
								.then(results => {
									results.forEach(res => {
										if(res.code === 0){
											rp += res.value + '-_-';
										}else{
											warn += res.name + ",";
									}
								});
							});
							if(warn !== ''){
								uni.showToast({
								    title: '图片{' + warn + '}上传失败',
								    icon: 'none', // 使用 'none' 不显示图标
								});
							}
							inspectionProfile.resultPhotos = rp;
						}
					}
					InspectPlanApi.addInspectProfileList(inspectionProfileList);
				}finally{
					await new Promise((resolve) => {
						uni.showToast({
						  title: "添加成功",
						  icon: 'success',
						  duration: 2000 
						});
						setTimeout(() => {
						  resolve();
						}, 2000);
					});
					this.$tab.reLaunch('/pages/index')
				}
			},
		}
	}
</script>

<style>
	.input-border {
		border: 1px solid #ccc;
		height: 40px;
		border-radius: 4px;
		width: 100%;
		box-sizing: border-box;
	}
	.textarea-border {
		border: 1px solid #ccc;
		border-radius: 4px;
		width: 100%;
		box-sizing: border-box;
	}
</style>