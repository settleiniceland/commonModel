import Resumable from 'resumablejs';
import {getAccessToken} from '@/utils/auth'
import request from '@/utils/request'

/**使用resumablejs分片传输，专门对付大文件
 * @auther mcr
 */
export function initResumableAndUpload(files,code){
    return new Promise((resolve) => {
        const results = [];
        let uploadedCount = 0;
        function returnResult(){
            if(uploadedCount === files.length){
                resolve(results)
            }
        };
        //初始化Resumable对象
        const r = new Resumable({
            target: process.env.VUE_APP_BASE_API + '/admin-api/system/equip/profile/upload',//上传目标 URL
            chunkSize: 1024*512,//每块大小0.5mb
            simultaneousUploads: 1, // 同时上传的文件数
            testChunks: false, //是否测试文件块
            query: {
                equipCode: code
            },
            headers: {
                'Authorization': 'Bearer ' + getAccessToken()
            },
            withCredentials: true, // 是否带上凭证
        });
        //添加文件成功后直接上传
        r.on('fileAdded',(file) => {
            console.log(new Date().toISOString()+":add file successfully and start upload",file);
            r.upload();
        });
        r.on('fileSuccess',(file,msg) => {
            console.log("upload successfully;TIME NOW----->"+new Date().toISOString());
            results.push({name:file.fileName,value:JSON.parse(msg).data,code:JSON.parse(msg).code});
            uploadedCount++;
            returnResult();
        });
        r.on('fileError', (file, msg) => {
            console.log(new Date().toISOString()+":upload fail",file.name,msg);
            results.push({name:file.fileName,value:JSON.parse(msg).data,code:JSON.parse(msg).code});
            uploadedCount++;
            returnResult();
        });
        files.forEach(file => {
            r.addFile(file.raw); // file.raw 是文件对象
        });
    })
}
/**
 * 查询上传进度
 * @auther mcr 
 */
export function getUploadProgress() {
    return request({
        url: '/system/equip/profile/getUploadProgress',
        method: 'get'
    })
}
/**
 * 删除文件
 * @author mcr
 */
export function delFile(paths) {
    return request({
        url: '/system/equip/profile/delFile',
        method: 'post',
        data: paths
    })
}