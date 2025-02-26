<template>
	<view style="margin: 0 5%; max-height: 90vh; overflow-y: auto;">
		<view class="uni-box-head">
			<uni-title type="h1" align="center" :title="formData.equipMaintainPlanName"></uni-title>
		</view>
		<uni-section title="基础信息" type="line">
			<uni-card>
				<uni-forms :modelValue="formData">
					<uni-forms-item label="计划名称" name="equipMaintainPlanName">
						<input class="input-border" type="text" v-model="formData.equipMaintainPlanName" disabled/>
					</uni-forms-item>
					<uni-forms-item label="保养内容" name="equipMaintainDetail">
						<input class="input-border" type="text" v-model="formData.equipMaintainDetail" disabled/>
					</uni-forms-item>
					<uni-forms-item label="保养周期(h)" name="maintainCycle">
						<input class="input-border" type="text" v-model="formData.maintainCycle" disabled/>
					</uni-forms-item>
					<uni-forms-item label="设备" name="equipName">
						<input class="input-border" type="text" v-model="formData.equipName" disabled/>
					</uni-forms-item>
					<uni-forms-item label="执行部门" name="executeDeptName">
						<input class="input-border" type="text" v-model="formData.executeDeptName" disabled/>
					</uni-forms-item>
					<uni-forms-item label="是否特殊设备" name="isSpecialName">
						<input class="input-border" type="text" v-model="formData.isSpecialName" disabled/>
					</uni-forms-item>
				</uni-forms>
			</uni-card>
		</uni-section>
		<uni-section title="录入信息" type="line">
			<uni-card>
				<uni-forms :modelValue="formData">
					<uni-forms-item label="设备" name="equipmentprofileId">
						<uni-section title="选中设备" type="line">
							<uni-data-select
								v-model="value"
								:localdata="equipProfileList"
								@change="changeEquip"/>
						</uni-section>
					</uni-forms-item>
					<uni-forms-item label="执行日期" name="maintainDate">
						<button class="input-border" @click="showDatePicker = !showDatePicker">{{formData.maintainDate?formData.maintainDate:"请选择时间"}}</button>
					</uni-forms-item>
					<uni-forms-item label="实际执行人" name="actualMaintainNames">
						<input class="input-border" type="text" v-model="formData.actualMaintainNames"/>
					</uni-forms-item>
					<uni-forms-item v-if="formData.equipmentprofileId !== undefined && formData.equipmentprofileId !== '' && formData.equipmentprofileId !== null" label="现场图片" name="resultPhotos">
						<uni-section title="调用相机拍照上传图片" type="line" style="background-color: bisque;">
							<!-- todo 此块需要安卓机实测，较浪费时间，最后搞 -->
							<view class="example-body">
								<uni-file-picker 
									ref="filePicker" 
									limit="9" 
									title="最多选择9张图片"
									:auto-upload="false"
									@select="(event) => addfile(event)"
									@delete="(event) => delfile(event)"
								/>
							</view>
						</uni-section>
					</uni-forms-item>
					<uni-forms-item label="备注" name="remark">
						<input class="input-border" type="text" v-model="formData.remark"/>
					</uni-forms-item>
				</uni-forms>
			</uni-card>
		</uni-section>
		<button type="primary" @click="submit">Submit</button>
		<mx-date-picker :show="showDatePicker" type="datetime" :showHoliday="false" :show-tips="true"
			:show-seconds="true" @confirm="ed" @cancel="ed" />
	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue"
	import { initResumableAndUpload } from "@/utils/equip.js"
	import { handleTreeForString } from "@/utils/ruoyi"
	import DateTools from "@/utils/mx-datepicker-dateTools.js"
	import * as InspectPlanApi from '@/api/pages/inspectplan.js';
	export default {
		components: {MxDatePicker},
		data() {
			return {
				formData: {
					equipMaintainPlanId: undefined,
					equipMaintainPlanName: undefined,
					equipMaintainDetailId: undefined,
					equipMaintainDetail: undefined,
					isSpecial: undefined,
					isSpecialName: undefined,
					equipmentprofileId: undefined,
					equipmentprofileCode: undefined,
					equipName: undefined,
					maintainCycle: undefined,
					resultPhotos: undefined,
					maintainDate: undefined,
					executeDeptId: undefined,
					executeDeptName: undefined,
					actualMaintainNames: undefined,
					remark: undefined,
				},
				showDatePicker: false,
				selectedFilesList: [],//要上传的图片文件[key是内容编码，value就是图片文件数组]
				equipProfileList: [],//待选设备
				value: undefined,
			}
		},
		async created(){
			this.initData();
		},
		methods: {
			//一些初始化操作
			initData(){
				this.equipProfileList = [];
				//初始属性赋值
				this.formData.equipMaintainPlanId = uni.getStorageSync("maintainDetail").equipMaintainPlanId;
				this.formData.equipMaintainPlanName = uni.getStorageSync("maintainDetail").equipMaintainPlanName;
				this.formData.equipMaintainDetailId = uni.getStorageSync("maintainDetail").id;
				this.formData.equipMaintainDetail = uni.getStorageSync("maintainDetail").details;
				this.formData.isSpecial = uni.getStorageSync("maintainDetail").isSpecial;
				this.formData.isSpecialName = uni.getStorageSync("maintainDetail").isSpecialName;
				this.formData.equipName = uni.getStorageSync("maintainDetail").equipName;
				this.formData.maintainCycle = uni.getStorageSync("maintainDetail").maintainCycle;
				this.formData.executeDeptId = uni.getStorageSync("maintainPlan").executeDeptId;
				this.formData.executeDeptName = uni.getStorageSync("maintainPlan").executeDeptName;
				this.formData.maintainDate = DateTools.format(new Date(),"yyyy-mm-dd hh:ii:ss");
				//构建待选列
				const ids = uni.getStorageSync("maintainDetail").equipprofileIds.split("-_-");
				const codes = uni.getStorageSync("maintainDetail").equipprofileCodes.split("-_-");
				for(let i=0;i<ids.length;i++){
					const item = {
						value: ids[i]+"-_-"+codes[i],
						text: this.formData.equipName + codes[i]
					}
					this.equipProfileList.push(item);
				}
			},
			//时间选择器
			ed(e) {
				this.showDatePicker = false;
				if(e) {
					this.formData.maintainDate = e.value;
				}
			},
			//文件上传相关
			addfile(e){
				this.selectedFilesList.push(...e.tempFiles);
			},
			//文件清除相关
			delfile(e){
				this.selectedFilesList = this.selectedFilesList.filter(item => item.uuid !== e.tempFile.uuid)
			},
			// 显示选择器
			showPicker() {
				this.$refs.treePicker._show(undefined,this.equipProfileTree);
			},
			changeEquip(item){
				this.formData.equipmentprofileId = item.split("-_-")[0];
				this.formData.equipmentprofileCode = item.split("-_-")[1];
			},
			async submit(){
				let errorMsg='未填写完整，请填写完整';
				if(this.selectedFilesList.length!==0){
					this.formData.resultPhotos='baik baik saja';
				}
				if(Object.values(this.formData).some((value, index) => {
					const key = Object.keys(this.formData)[index];
					if(key !== "remark" && (value === null || value === '' || value === undefined)){
						return true;
					}else{
						return false;
					}
				})){
					uni.showToast({
					  title: errorMsg,
					  icon: 'none',
					  duration: 10000
					});
					return;
				}
				let warn = '';
				this.formData.resultPhotos = "";
				await initResumableAndUpload(this.selectedFilesList,this.formData.equipmentprofileCode)
					.then(results => {
						results.forEach(res => {
							if(res.code === 0){
								this.formData.resultPhotos += res.value + '-_-';
							}else{
								warn += res.name + ",";
							}
						})
				});
				console.log("最终数据",this.formData);//通过
			},
		},
	}
</script>
<style>
</style>
