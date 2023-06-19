
$(function(){
	$.parser.parse();
	editBugWindow();
	//点击基本信息隐藏历史列表
	$('a[href="#bugInfo"]').click(function(){
		$('#opHistory').next().hide();
	});
	 $('a[href="#bugEditFiles"]').click(function(){
	 	$('#opHistory').next().hide();
	 });
});

//编辑
function editBugWindow(){
	var row = $("#bugList").xdatagrid('getSelected');
//	if (!row) {
//		$.xalert({title:'提示',msg:'请选择要修改的一条记录！'});
//		return;
//	}
	$('#opHistory').next().hide();
	
	$.get(
		baseUrl + "/bugManager/bugManagerAction!upInit.action",
		{
			'dto.bug.bugId': row.bugId,
			'dto.loadType':1,
			'dto.taskId':row.taskId
		},
		function(data,status,xhr){
			if(status=='success'){
				if(data!=null){
//					editFile();
					$("#taskModule").textbox("setValue", data.moduleName);
					$("#stateName_").textbox("setValue", data.stateName);
					$("#authorName_").textbox("setValue", data.bug.author.uniqueName);
					$("#bugDesc_").textbox("setValue", data.bug.bugDesc);
					
					if(data.bug.verSelStr!=null && data.bug.verSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.verSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#bugReptVer_").xcombobox("loadData",comboxList);
						$("#bugReptVer_").xcombobox({
							onSelect:function(param){
								$("#bugReptVerName_").textbox("setValue", param.text);
							}
						});
						if(data.bug.bugReptVer!=null && data.bug.bugReptVer!=""){
							$("#bugReptVer_").xcombobox("setValue",data.bug.reptVersion.versionId);
							$("#bugReptVerName_").textbox("setValue", data.bug.reptVersion.versionNum);
						}
					}
					if(data.bug.typeSelStr && data.bug.typeSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.typeSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#bugTypeId_").xcombobox("loadData",comboxList);
						$("#bugTypeId_").xcombobox({
							onSelect:function(param){
								$("#bugTypeName_").textbox("setValue", param.text);
							}
						});
						if(data.bug.bugTypeId!=null && data.bug.bugTypeId!=""){
							$("#bugTypeId_").xcombobox("setValue",data.bug.bugTypeId);
							$("#bugTypeName_").textbox("setValue", data.bug.bugType.typeName);
						}
					}
					if(data.bug.gradeSelStr && data.bug.gradeSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.gradeSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#bugGradeId_").xcombobox("loadData",comboxList);
						$("#bugGradeId_").xcombobox({
							onSelect:function(param){
								$("#bugGradeName_").textbox("setValue", param.text);
							}
						});
						if(data.bug.bugGradeId!=null && data.bug.bugGradeId!=""){
							$("#bugGradeId_").xcombobox("setValue",data.bug.bugGradeId);
							$("#bugGradeName_").textbox("setValue", data.bug.bugGrade.typeName);
						}
					}
					if(data.bug.plantFormSelStr && data.bug.plantFormSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.plantFormSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#platformId_").xcombobox("loadData",comboxList);
						$("#platformId_").xcombobox({
							onSelect:function(param){
								$("#pltformName_").textbox("setValue", param.text);
							}
						});
						if(data.bug.platformId!=null && data.bug.platformId!=""){
							$("#platformId_").xcombobox("setValue",data.bug.platformId);
							$("#pltformName_").textbox("setValue", data.bug.occurPlant.typeName);
						}
					}
					if(data.bug.sourceSelStr && data.bug.sourceSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.sourceSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#sourceId_").xcombobox("loadData",comboxList);
						$("#sourceId_").xcombobox({
							onSelect:function(param){
								$("#sourceName_").textbox("setValue", param.text);
							}
						});
						if(data.bug.sourceId!=null && data.bug.sourceId!=""){
							$("#sourceId_").xcombobox("setValue",data.bug.sourceId);
							$("#sourceName_").textbox("setValue", data.bug.bugSource.typeName);
						}
					}
					if(data.bug.occaSelStr && data.bug.occaSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.occaSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#bugOccaId_").xcombobox("loadData",comboxList);
						if(data.bug.bugOpotunity.typeName=="首次出现"){
							$("#geneCauseTd_").show();
							$("#geneCauseIdTd_").show();
						}else{
							$("#geneCauseTd_").hide();
							$("#geneCauseIdTd_").hide();
						}
						$("#bugOccaId_").xcombobox({
							onSelect:function(param){
								if(param.text=="首次出现"){
									$("#geneCauseTd_").show();
									$("#geneCauseIdTd_").show();
								}else{
									$("#geneCauseTd_").hide();
									$("#geneCauseIdTd_").hide();
								}
								$("#occaName_").textbox("setValue", param.text);
							}
						});
						if(data.bug.bugOccaId!=null && data.bug.bugOccaId!=""){
							$("#bugOccaId_").xcombobox("setValue",data.bug.bugOccaId);
							$("#occaName_").textbox("setValue", data.bug.bugOpotunity.typeName);
						}
					}
					if(data.bug.geCaseSelStr && data.bug.geCaseSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.geCaseSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#geneCauseId_").xcombobox("loadData",comboxList);
						$("#geneCauseId_").xcombobox({
							onSelect:function(param){
								$("#geneCaseName_").textbox("setValue", param.text);
							}
						});
						if(data.bug.geneCauseId!=null && data.bug.geneCauseId!=""){
							$("#geneCauseId_").xcombobox("setValue",data.bug.geneCauseId);
							$("#geneCaseName_").textbox("setValue", data.bug.geneCause.typeName);
						}
					}
					if(data.bug.priSelStr && data.bug.priSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.priSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#priId_").xcombobox("loadData",comboxList);
						$("#priId_").xcombobox({
							onSelect:function(param){
								$("#priName_").textbox("setValue", param.text);
							}
						});
						if(data.bug.priId!=null && data.bug.priId!=""){
							$("#priId_").xcombobox("setValue",data.bug.priId);
							$("#priName_").textbox("setValue", data.bug.bugPri.typeName);
						}
					}
					if(data.bug.freqSelStr && data.bug.freqSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.freqSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#bugFreqId_").xcombobox("loadData",comboxList);
						if(data.bug.bugFreq.typeNam=="偶尔出现"){
							$("#reproTd_").show();
							$("#reproPersentTd_").show();
						}else{
							$("#reproTd_").hide();
							$("#reproPersentTd_").hide();
						}
						$("#bugFreqId_").xcombobox({
							onSelect:function(param){
								if(param.text=="偶尔出现"){
									$("#reproTd_").show();
									$("#reproPersentTd_").show();
								}else{
									$("#reproTd_").hide();
									$("#reproPersentTd_").hide();
								}
								$("#bugFreqName_").textbox("setValue", param.text);
							}
						});
						
						if(data.bug.bugFreqId!=null && data.bug.bugFreqId!=""){
							$("#bugFreqId_").xcombobox("setValue",data.bug.bugFreqId);
							$("#bugFreqName_").textbox("setValue", data.bug.bugFreq.typeName);
						}
						$("#reproPersent_").textbox("setValue", data.bug.reproPersent);

					}
					if(data.bug.genePhaseSelStr && data.bug.genePhaseSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.genePhaseSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#genePhaseId_").xcombobox("loadData",comboxList);
						$("#genePhaseId_").xcombobox({
							onSelect:function(param){
								$("#genPhName_").textbox("setValue", param.text);
							}
						});
						if(data.bug.genePhaseId!=null && data.bug.genePhaseId!=""){
							$("#genePhaseId_").xcombobox("setValue",data.bug.genePhaseId);
							$("#genPhName_").textbox("setValue", data.bug.importPhase.typeName);
						}
					}
					if(data.currOwner){
						var dataStr =  data.currOwner.split("$");
						if(dataStr[0]==" 处理人"){
							$("#currHanderName_").textbox("setValue",dataStr[1]);
							$("#currOwner_Tr").hide();
						}else{
							$("#currOwnerText").html(dataStr[0]+":");
							$("#currOwner_").textbox("setValue",dataStr[1]);
							$("#currOwner_Tr").show();
						}
					}
					if(data.bug.moduleId){
						$("#moduleId_").val(data.bug.moduleId);
					}
					if(data.moduleName){
						$("#module_Name_").val(data.moduleName);
					}
					if(data.bug.currFlowCd){
						$("#currFlowCd_").val(data.bug.currFlowCd);
					}
					if(data.bug.nextFlowCd){
						$("#nextFlowCd_").val(data.bug.nextFlowCd);
					}
					if(data.bug.currStateId){
						$("#currStateId_").val(data.bug.currStateId);
					}
					if(data.bug.testPhase){
						$("#testPhase_").val(data.bug.testPhase);
					}
					if(data.bug.reProTxt){
						$("#reProTxt_").textbox("setValue", data.bug.reProTxt);
					}
					if(data.bug.bugId){
						$("#bugId_").val(data.bug.bugId);
					}
					if(data.bug.bugReptId){
						$("#bugReptId_").val(data.bug.bugReptId);
					}
					if(data.bug.currHandlerId){
						$("#currHandlerId_").val(data.bug.currHandlerId);
					}
					if(data.bug.currHandlDate){
						$("#currHandlDate_").val(data.bug.currHandlDate);
					}
					$("#msgFlag_").val(data.bug.msgFlag);
					$("#relaCaseFlag_").val(data.bug.relaCaseFlag);
					if(data.bug.planAmendHour){
						$("#planAmendHour_").val(data.bug.planAmendHour);
					}
					if(data.bug.nextOwnerId){
						$("#nextOwnerId_").val(data.bug.nextOwnerId);
					}
					
					if(data.bug.bugAntimodDate){
						$("#bugAntimodDate_").val(data.bug.bugAntimodDate);
					}
					if(data.bug.relaCaseFlag){
						$("#relaCaseFlag_").val(data.bug.relaCaseFlag);
					}
					if(data.initReProStep){
						$("#initReProStep_").val(data.initReProStep);
					}
					
					if(data.bug.testOwnerId){
						$("#testOwnerId_").val(data.bug.testOwnerId);
					}
					if(data.bug.analyseOwnerId){
						$("#analyseOwnerId_").val(data.bug.analyseOwnerId);
					}
					if(data.bug.assinOwnerId){
						$("#assinOwnerId_").val(data.bug.assinOwnerId);
					}
					if(data.bug.devOwnerId){
						$("#devOwnerId_").val(data.bug.devOwnerId);
					}
					if(data.bug.currHandlerId){
						$("#intercessOwnerId_").val(data.bug.intercessOwnerId);
					}
					if(data.bug.testPhase){
						$("#testPhase_").val(data.bug.testPhase);
					}
//					if(data.currOwner){
//						$("#currOwner_Tr").html(data.currOwner);
////						$("#currOwner_").textbox("setValue", data.currOwner);
//					}
					if(data.bug.reptDate){
						$("#reptDate_").textbox("setValue", data.bug.reptDate);
					}
					if(data.bug.currRemark){
						$("#currRemark_").textbox("setValue", data.bug.currRemark);
					}
				}
//				$("#editBugWindown").xdeserialize(data);
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},"json");
	
//	$("#editBugWindown").window({title:"修改软件问题报告"});
	$("#editBugWindown").window("vcenter");
//	$("#editBugWindown").window("open");
	$("#bugHistoryList").xdatagrid('resize');
	
}


//提交修改
function editSubmitForm() {
	var valid = $("#editBugWindown").xform('validate');
	if (!valid) {
		return;
	}
	if($("#reProTxt_").textbox('getValue')!=""){
		$("#reProStep_").val($("#reProTxt_").textbox('getValue'));
	}
	
	$('#editSubmitBnt').linkbutton('disable');
	var url=baseUrl + "/bugManager/bugManagerAction!update.action"
	$.post(
		url,
		$("#editBugWindown").xserialize(),
		function(data) {
			if (data =="success") {
				$("#editBugWindown").xform('clear');
				$("#editBugWindown").xwindow('close');
				getBugList();
				$.xalert({title:'提示',msg:'修改成功！'});
				
			} else {
				 $.xalert({title:'提示',msg:'系统错误！'});
			}
			$('#editSubmitBnt').linkbutton('enable');
		}, "text");
}

//返回
function closeEditBugWin(){
	$("#editBugWindown").xform('clear');
	$("#editBugWindown").xwindow('close');
}

//处理历史
function bugHistoryList(){
	var row = $("#bugList").xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'提示',msg:'请选择要查看的一条记录！'});
		return;
	}
	$("#bugHistoryList").xdatagrid({
		url: baseUrl + '/bugManager/bugManagerAction!loadHistory.action?dto.isAjax=true&dto.bug.bugId='+row.bugId,
		method: 'get',
		height: mainObjs.tableHeight-100,
		fitColumns:true,
		rownumbers: true,
		singleSelect:true,
		pagination: true,
		pageNumber: 1,
		pageSize: 16,
		pageList: [16,30,50,100],
		layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],
		onLoadSuccess:function(data){
			$("#bugHistoryList").xdatagrid('resize');
		},
		columns:[[
			{field:'handlerName',title:'处理人',width:'20%',align:'center'},
			{field:'handResult',title:'处理结果',width:'30%',align:'center',formatter:function(value,row,index){
				return "<p style=\"cursor: pointer;margin:0;\" title=\""+value+"\">" + value + "</p>";
			}},
			{field:'remark',title:'处理说明',width:'25%',align:'center',formatter:testStatusFormat},
			{field:'insDate',title:'处理日期',width:'25%',align:'center'},
		]]
	});
}

function testStatusFormat(value,row,index){
	return value;
}
//文件信息
function editFile(){
	$('#opHistory').next().hide();
	$("#editFileId").fileinput('clear');
	$("#editFileId").fileinput('destroy');
	var row = $("#bugList").xdatagrid('getSelected');
	$.get(
//			baseUrl + "/fileInfo/fileInfoAction!getFileInfoByTypeId.action",
			baseUrl + "/bugManager/bugManagerAction!getFileInfoByTypeId.action",
			{
				'dto.fileInfo.type':'bug',
				'dto.fileInfo.typeId': row.bugId
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
								downloadUrl:baseUrl + data[i].filePath,
								showRemove:true,
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
					initFileInput(fileData,preConfigList,row.bugId);
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			},"json");
}
var uploadCount=0;
function initFileInput(fileJson,fileConfigJson,bugId){
	$("#editFileId").fileinput('clear');
	$("#editFileId").fileinput('destroy');	
	$("#editFileId").fileinput({
		theme: 'fa',
		language:'zh',
		uploadUrl:baseUrl + "/uploader?type=bug", // you must set a valid URL here else you will get an error
//		allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx','txt','pdf','doc','docx'],
		showPreview:true,
		showClose:false,
		overwriteInitial: false,
		uploadAsync:false,
		maxFileSize: 3000,
		maxFileCount:5,
		autoReplace:true,
		imageMaxWidth : 100,
		imageMaxHeight : 100,
		enctype: 'multipart/form-data',
		//msgFilesTooMany :"选择图片超过了最大数量", 
		showRemove:false,
		allowedPreviewTypes:['image', 'html', 'text', 'video', 'pdf', 'flash','object'],
		dropZoneEnabled:false,
		fileActionSettings:{showUpload: false,showDownload: true,},// 控制上传按钮是否显示
		initialPreviewAsData: true,
		initialPreview:fileJson,
	    initialPreviewConfig:fileConfigJson,
		previewSettings: {
//	        image: {width: "100px", height: "100px"},
	    },
		slugCallback: function (filename) {
		  return filename.replace('(', '_').replace(']', '_');
		}
	}).on("fileselect", function(event, files) {
		//选择文件
		var filesCount = $('#uploadFileId').fileinput('getFilesCount');
		uploadCount=0;
	}).on("filebatchselected", function(event, files) {
		//选择文件后处理
	}).on('filebatchuploadsuccess', function(event, data, previewId, index) {
		if(uploadCount==0){
			var fInfos = data.response;
			var params = {
				"dto.type":"bug",
				"dto.typeId":bugId,
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
		//上传前
		
	}).on('fileerror', function(event, data, msg) {
	}).on("fileuploaded", function(event, data, previewId, index) {
		//上传成功后处理方法
	});
	
}

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

//# sourceURL=bugEdit.js