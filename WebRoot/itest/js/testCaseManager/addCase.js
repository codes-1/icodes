var fileInfos = new Array();
var iterationFlag="";
$(function(){
	$.parser.parse();
	loadCaseCategory();
	loadCaseYXJ();
	var taskId = getQueryParam('taskId');
	var moudleId = getQueryParam('moudleId');
	iterationFlag=getQueryParam('flag');
	addWindow(taskId,moudleId);
	
});

//获取页面url参数
function getQueryParam(name) {
       var obj = $('#addOrEditWindown').xwindow('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

//增加
function addWindow(taskId,moudleId){
	$("#addOrEditCaseForm").xform('clear');
	$("#caseWeight").xnumberbox("setValue",2);
	$("#task_Id").val(taskId);
	$("#moduleId").val(moudleId);
	caseUpload();
}


//提交保存新增或修改
function submitForm(submitId) {
	var valid = $("#addOrEditCaseForm").xform('validate');
	if (!valid) {
		return;
	}
	if(submitId=="2"){
		if($("#exeVerId").xcombobox("getValue")==""){
			$.xalert({title:'提示',msg:'请选择版本！'});
			return;
		}
	}
	var fileParam = {'dto.fileInfos':JSON.stringify(fileInfos)};
	var dataParams = Object.assign($("#addOrEditCaseForm").xserialize(), fileParam);
	$('#submit_'+submitId).linkbutton('disable');
	var testCaseId = $("#testCaseId").val();
	var url;
	if(testCaseId==""){
		url = baseUrl + "/caseManager/caseManagerAction!addCase.action";
	}else{
		url=baseUrl + "/caseManager/caseManagerAction!upCase.action"
	}
	$.post(
		url,
//		$("#addOrEditWindown").xserialize(),
		dataParams,
		function(data) {
			if (data =="success") {
				closeCaseWin();
				if(iterationFlag!="iterationPage"){
					getCaseList(currNodeId);
				}
				$('#submit_1').linkbutton('enable');
				if(testCaseId==""){
					 $.xalert({title:'提示',msg:'增加成功！'});
				}else{
					if(submitId=="2"){
						 $.xalert({title:'提示',msg:'保存成功！'});
					}else{
						 $.xalert({title:'提示',msg:'修改成功！'});
					}
				}
				
			} else {
				$('#submit_'+submitId).linkbutton('enable');
				 $.xalert({title:'提示',msg:'系统错误！'});
			}
		}, "text");
}

function caseUpload(){
	$("#caseUploadFileId").fileinput('reset');
	$("#caseUploadFileId").fileinput('destroy');
	$("#caseUploadFileId").fileinput({
		theme: 'fa',
		language:'zh',
		uploadUrl:baseUrl + "/uploader?type=case", // you must set a valid URL here else you will get an error
		deleteUrl:'',
//		allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx','txt','pdf','doc','docx','object'],
		showPreview:true,
		overwriteInitial: false,
		uploadAsync:false,
		autoReplace: true,
//		showUploadedThumbs:false,
		maxFileSize: 6000,
		maxFileCount: 5,
		imageMaxWidth : 200,
		imageMaxHeight : 100,
		enctype: 'multipart/form-data',
		showRemove:false,
		showClose:false,
//		showUpload:false,
		allowedPreviewTypes:['image', 'html', 'text', 'video', 'pdf', 'flash','object'],
		dropZoneEnabled:false,
		fileActionSettings:{
	        showUpload: false,
	        showRemove:true,
//			url: baseUrl + "/uploader?type=del",// 删除url 
	    },
		slugCallback: function (filename) {
			return filename.replace('(', '_').replace(']', '_');
		}
	}).on("filebatchselected", function(event, files) {
//		if(files!=null && files.length>0){
//			$(this).fileinput("upload");
//		}
	}).on('filebatchuploadsuccess', function(event, data, previewId, index) {
		fileInfos = fileInfos.concat(data.response);
		setTimeout(function(){
			$(".kv-upload-progress").hide();
		}, 2000);
	}).on('filebatchuploaderror', function(event, data,  previewId, index) {
	}).on('fileloaded', function(event, data,  previewId, index) {
		var filesFrames = $('#caseUploadFileId').fileinput('getFrames');
		if(filesFrames.length>5){
			$.xalert({title:'提示',msg:'最多上传5个文件！'});
			$("#"+previewId).remove();
			return;
		}
		
		
	}).on("fileuploaded", function(event, data, previewId, index) {
		//上传成功后处理方法
	});
}

function loadCaseCategory(){
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!loadTestBaseSetList.action",
			{
				"page": 1,
				"rows": 80,
				"dto.subName": "用例类型",
				"dto.flag": "1"
			},
			function(dat) {
				if (dat != null) {
					$(".caseTypeId").xcombobox({
						data:dat.rows
					});
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}

//加载优先级列表
function loadCaseYXJ(){
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!loadTestBaseSetList.action",
			{
				"page": 1,
				"rows": 80,
				"dto.subName": "用例优先级",
				"dto.flag": "1"
			},
			function(dat) {
				if (dat != null) {
					$(".priId").xcombobox({
						data:dat.rows
					});
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}

//取消
function closeCaseWin(){
	$("#addOrEditCaseForm").xform('clear');
	$("#caseWeight").numberbox("setValue",2);
	$("#addOrEditWindown").xwindow('close');
}

//# sourceURL=addCase.js