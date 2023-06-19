var caseId,operaType,flag,testcasepkgId;
var fileInfos = new Array();
$(function(){
	exuiloader.load('numberbox', null, true);
	$.parser.parse();
	loadCaseCategory();
	loadCaseYXJ();
	
	caseId = getQueryParam('caseId');
	operaType = getQueryParam('operaType');
	flag = getQueryParam('flag');
	testcasepkgId = getQueryParam('testcasepkgId');
	editOrExecWindow();
	
	//点击基本信息隐藏历史列表
	$('a[href="#caseInfo"]').click(function(){
		$('#caseHistory').next().hide();
	});
	$('a[href="#caseEditFiles"]').click(function(){
		$('#caseHistory').next().hide();
	});
});
//用例历史
function caseHistory(){
	$("#caseHistoryList").xdatagrid({
		url: baseUrl + '/caseManager/caseManagerAction!viewCaseHistory.action?dto.testCaseInfo.testCaseId='+caseId,
		method: 'get',
		height: mainObjs.tableHeight-100,
		fitColumns:true,
		singleSelect:true,
		pagination: true,
		pageNumber: 1,
		pageSize: 16,
		pageList: [16,30,50,100],
		layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],
		onLoadSuccess:function(data){
			$("#caseHistoryList").xdatagrid('resize');
		},
		columns:[[
			{field:'testActorName',title:'处理人',width:'15%',align:'center',formatter:function(value,row,index){
				if(value=="" || value=="null" || value ==null){
					return " ";
				}else{
					return "<p title=\""+value+"\">" + value + "</p>";
				}
			}},
			{field:'operaType',title:'操作类型',width:'10%',align:'center',formatter:operaTypeFormat},
			{field:'testResult',title:'用例状态',width:'10%',align:'center',formatter:convCase},
			{field:'testVerNum',title:'版本',width:'15%',align:'center',},
			{field:'exeDate',title:'处理日期',width:'25%',align:'center'},
			{field:'remark',title:'备注',width:'25%',align:'center',formatter:function(value,row,index){
				if(value=="" || value=="null" || value ==null){
					return "";
				}else{
					return "<p title=\""+value+"\">" + value + "</p>";
				}
			}}
		]]
	});
	$("#caseHistoryList").xdatagrid('resize');
}

//获取页面url参数
function getQueryParam(name) {
       var obj = $('#editCaseWindown').xwindow('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

//编辑
function editOrExecWindow(){
	$('#caseHistory').next().hide();
	$("#editCaseForm").xform('clear');
	$("#caseWeight").numberbox("setValue",2);
	var url ="";
	if(operaType=="exec"){
		url = baseUrl + "/caseManager/caseManagerAction!exeCaseInit.action";
	}else{
		url = baseUrl + "/caseManager/caseManagerAction!upInit.action";
	}
	
	$.get(url,
		{'dto.testCaseInfo.testCaseId':caseId},
		function(data,status,xhr){
			if(status=='success'){
				$("#editCaseForm").xdeserialize(data);
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},"json");
	$('#submit_1').linkbutton({
		text:'修改'
	});
	$("#execTr").hide();
	$("#addOrEditFoot").show();
	$("#operaDiv").show();
	$("#execDiv").hide();
	if(operaType=="exec"){
		$("#exeVerId").xcombobox({
			url: baseUrl + '/testTaskManager/testTaskManagerAction!loadVerSel.action',
			valueField: 'keyObj',
			textField: 'valueObj',
			onSelect: function(rec) {
				
			}
		});
		$("#execTr").show();
		$("#addOrEditFoot").show();
		$("#operaDiv").hide();
		$("#execDiv").show();
	}
//	$("#editCaseWindown")[0].style.maxHeight=mainObjs.tableHeight-40;
//	$("#editCaseWindown").window({title:"修改用例"});
	$("#editCaseWindown").window("vcenter");
//	$("#editCaseWindown").window("open");
	
}

//提交保存新增或修改
function editSubmit(submitId) {
	var valid = $("#editCaseForm").xform('validate');
	if (!valid) {
		return;
	}
	if(submitId=="2"){
		if($("#exeVerId").xcombobox("getValue")==""){
			$.xalert({title:'提示',msg:'请选择版本！'});
			return;
		}
	}
//	var fileParam = {'dto.fileInfos':JSON.stringify(fileInfos)};
//	var dataParams = Object.assign($("#editCaseForm").xserialize(), fileParam);
	$('#submit_'+submitId).linkbutton('disable');
	var testCaseId = $("#testCaseId").val();
	var url=baseUrl + "/caseManager/caseManagerAction!upCase.action"
	$.post(
		url,
		$("#editCaseForm").xserialize(),
//		dataParams,
		function(data) {
			if (data =="success") {
				closeEditCaseWin();
				if(flag === 'testCase'){
					$("#executeTestCaseList").xdatagrid('reload');
				}else{				
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

function closeEditCaseWin(){
	//取消
	$("#editCaseForm").xform('clear');
	$("#editCaseWindown").xwindow('close');
}


//执行用例提交
function execCaseSubmit(status){
	var valid = $("#editCaseForm").xform('validate');
	if (!valid) {
		return;
	}
	if($("#exeVerId").xcombobox("getValue")==""){
		$.xalert({title:'提示',msg:'请选择版本！'});
		return;
	}
	$('#exec_'+status).linkbutton('disable');
	var url=baseUrl + "/caseManager/caseManagerAction!exeCase.action";
	var params = {
			'dto.testCaseInfo.testCaseId':$("#testCaseId").val(),
			'dto.testCaseInfo.moduleId':$("#moduleId").val(),
			'dto.testCaseInfo.testStatus':status,
			'dto.testCasePackId':testcasepkgId,
			'dto.exeVerId':$("#exeVerId").val(),
			'dto.remark':$("#execRemark").val(),
			'dto.fileInfos':JSON.stringify(fileInfos)
	};
	$.post(url,params,function(data) {
		if (data =="success") {
			$("#editCaseForm").xform('clear');
			$("#editCaseWindown").xwindow('close');
			if(flag === 'testCase'){
				$("#executeTestCaseList").xdatagrid('reload');
			}else{				
				getCaseList(currNodeId);
			}
			$('#exec_'+status).linkbutton('enable');
			$.xalert({title:'提示',msg:'执行成功！'});
			
		} else {
			$('#exec_'+status).linkbutton('enable');
			$.xalert({title:'提示',msg:'系统错误！'});
		}
	}, "text");
	
}

//文件信息
function editCaseFile(){
	$('#caseHistory').next().hide();
	$.get(
			baseUrl + "/fileInfo/fileInfoAction!getFileInfoByTypeId.action",
			{
				'dto.fileInfo.type':'case',
				'dto.fileInfo.typeId': caseId
			},
			function(data,status,xhr){
				if(status=='success'){
					var fileData = new Array();
					var preConfigList = new Array(); 
					for ( var i in data) {
						fileData.push(baseUrl+data[i].filePath);
						var index=data[i].relativeName.lastIndexOf(".");
						var fileTypeName = data[i].relativeName.substring(index+1,data[i].relativeName.length);
						var configJson = {
								showDownload: true,
								showRemove:true,
								downloadUrl:baseUrl + data[i].filePath,
								caption: data[i].relativeName, // 展示的文件名 
								width: '120px', 
								url: baseUrl + "/fileInfo/fileInfoAction!delteById.action", // 删除url 
								key: data[i].fileId, // 删除是Ajax向后台传递的参数 
								extra: {"dto.fileInfo.fileId": data[i].fileId} 
						}; 
						
						if(fileTypeName=="text"){
							var addParam ={type:'text'};
							 Object.assign(configJson, addParam);
						}
						if(fileTypeName=="pdf"){
							var addParam ={type:'pdf'};
							 Object.assign(configJson, addParam);
						}
						if(fileTypeName=="doc" || fileTypeName=="docx"){
							var addParam ={type:'gdocs'};
							 Object.assign(configJson, addParam);
						}
						if(fileTypeName=="mp4"|| fileTypeName=="avi"||fileTypeName=="mov"||fileTypeName=="gif"||fileTypeName=="wmv"){
							var addParam ={type:'video',filetype: "video/mp4"};
							 Object.assign(configJson, addParam);
						}
						preConfigList.push(configJson);
					}
					initCaseFileInput(fileData,preConfigList);
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			},"json");
}

var uploadCount=0;
function initCaseFileInput(fileJson,fileConfigJson){
	$("#caseEditFileId").fileinput('clear');
	$("#caseEditFileId").fileinput('destroy');
	$("#caseEditFileId").fileinput({
		theme: 'fa',
		language:'zh',
		uploadUrl:baseUrl + "/uploader?type=case",
//		allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx','txt','pdf','doc','docx','object','gdocs'],
		showPreview:true,
		showClose:false,
		overwriteInitial: false,
		uploadAsync:false,
//		autoReplace: true,
		showUploadedThumbs:true,
		maxFileSize: 6000,
		maxFileCount:5,
		imageMaxWidth : 200,
		imageMaxHeight : 100,
		enctype: 'multipart/form-data',
		//msgFilesTooMany :"选择图片超过了最大数量", 
		showRemove:false,
		showClose:false,
		showUpload:true,
		showDownload: true,
		allowedPreviewTypes:['image', 'html', 'text', 'video', 'pdf', 'flash','object'],
		dropZoneEnabled:false,
		fileActionSettings:{showUpload: false,showDownload: true,},// 控制上传按钮是否显示
		initialPreviewAsData: true,
		initialPreview:fileJson,
	    initialPreviewConfig:fileConfigJson,
		slugCallback: function (filename) {
		  return filename.replace('(', '_').replace(']', '_');
		}
	}).on("fileselect", function(event, files) {
		//选择文件
		var filesCount = $('#uploadFileId').fileinput('getFilesCount');
		uploadCount=0;
	}).on("filebatchselected", function(event, files) {
		//选择文件后处理
//		if(files!=null && files.length>0){
//			$(this).fileinput("upload");
//		}
	}).on('filebatchuploadsuccess', function(event, data, previewId, index) {
		if(uploadCount==0){
			var fInfos = data.response;
			var params = {
				"dto.type":"case",
				"dto.typeId":caseId,
				"dto.fileInfos":JSON.stringify(fInfos)
			};
			uploadFileInfo(params);
			uploadCount=1;
			setTimeout(function(){
				$(".kv-upload-progress").hide();
			}, 2000);
		}
	}).on('fileloaded', function(event, data,  previewId, index) {
		var filesFrames = $('#uploadFileId').fileinput('getFrames');
		if(filesFrames.length>5){
			$.xalert({title:'提示',msg:'最多上传5个文件！'});
			$("#"+previewId).remove();
			return;
		}
	}).on('filebatchuploaderror', function(event, data,  previewId, index) {
	}).on('filepreupload', function(event, data, previewId, index) {    
	}).on('fileerror', function(event, data, msg) {
	}).on("fileuploaded", function(event, data, previewId, index) {
		//上传成功后处理方法
	});
	
}

//文件上传
function uploadFileInfo(params){
	$.post(
		baseUrl + "/fileInfo/fileInfoAction!addFileInfo.action",
		params,
		function(data) {
			if (data =="success") {
				$.xalert({title:'提示',msg:'附件上传成功！'});
				
			} else {
				 $.xalert({title:'提示',msg:'附件上传失败！'});
			}
		}, "json");
}

//# sourceURL=editCase.js