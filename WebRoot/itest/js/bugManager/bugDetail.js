$(function(){
	$.parser.parse();
	 var bId = getQueryParam('bId');
	 bugDetail(bId);
	 haveFile(bId);
//点击基本信息隐藏历史列表
	$('a[href="#bugDetailInfo"]').click(function(){
		$('#detailHistory').next().hide();
	});
	$('a[href="#bugDetailFiles"]').click(function(){
		$('#detailHistory').next().hide();
	});
});


//获取页面url参数
function getQueryParam(name) {
       var obj = $('#bugDetailWin').xwindow('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

//详情
function bugDetail(bugId){
	if(bugId==""){
		return;
	}
	$('#detailHistory').next().hide();
	$.get(
		baseUrl + "/bugManager/bugManagerAction!viewBugDetal.action",
		{
			'dto.bug.bugId':bugId,
//			'dto.loadType':1,
//			'dto.taskId':row.taskId
		},
		function(data,status,xhr){
			if(status=='success'){
				if(data!=null){
					$("#taskModuled").textbox("setValue", data.modelName);
					$("#stateName_d").textbox("setValue", data.stateName);
					$("#authorName_d").textbox("setValue", data.author.name);
					$("#currHanderName_d").textbox("setValue", data.author.name);
					$("#bugDesc_d").textbox("setValue", data.bugDesc);
					
					if(data.reptVersion!=null){
						var comboxList = new Array();
						var content = {"id":data.reptVersion.versionId,"text":data.reptVersion.versionNum};
						comboxList.push(content);
						$("#bugReptVer_d").xcombobox("loadData",comboxList);
						$("#bugReptVer_d").xcombobox("setValue",data.bugReptVer);
					}
					if(data.bugType!=null){
						var comboxList = new Array();
						var content = {"id":data.bugType.typeId,"text":data.bugType.typeName};
						comboxList.push(content);
						$("#bugTypeId_d").xcombobox("loadData",comboxList);
						$("#bugTypeId_d").xcombobox("setValue",data.bugTypeId);
					}
					if(data.bugGrade!=null){
						var comboxList = new Array();
						var content = {"id":data.bugGrade.typeId,"text":data.bugGrade.typeName};
						comboxList.push(content);
						$("#bugGradeId_d").xcombobox("loadData",comboxList);
						$("#bugGradeId_d").xcombobox("setValue",data.bugGradeId);
					}
					if(data.occurPlant!=null){
						var comboxList = new Array();
						var content = {"id":data.occurPlant.typeId,"text":data.occurPlant.typeName};
						comboxList.push(content);
						$("#platformId_d").xcombobox("loadData",comboxList);
						$("#platformId_d").xcombobox("setValue",data.platformId);
					}
					if(data.bugSource!=null){
						var comboxList = new Array();
						var content = {"id":data.bugSource.typeId,"text":data.bugSource.typeName};
						comboxList.push(content);
						$("#sourceId_d").xcombobox("loadData",comboxList);
						$("#sourceId_d").xcombobox("setValue",data.sourceId);
					}
					if(data.bugOpotunity!=null){
						var comboxList = new Array();
						var content = {"id":data.bugOpotunity.typeId,"text":data.bugOpotunity.typeName};
						comboxList.push(content);
						$("#bugOccaId_d").xcombobox("loadData",comboxList);
						$("#bugOccaId_d").xcombobox("setValue",data.bugOccaId);
					}
					if(data.geneCause!=null){
						var comboxList = new Array();
						var content = {"id":data.geneCause.typeId,"text":data.geneCause.typeName};
						comboxList.push(content);
						$("#geneCauseId_d").xcombobox("loadData",comboxList);
						$("#geneCauseId_d").xcombobox("setValue",data.geneCauseId);
						$("#geneCauseTd_").show();
						$("#geneCauseIdTd_").show();
					}else{
						$("#geneCauseTd_").hide();
						$("#geneCauseIdTd_").hide();
					}
					if(data.bugPri!=null){
						var comboxList = new Array();
						var content = {"id":data.bugPri.typeId,"text":data.bugPri.typeName};
						comboxList.push(content);
						$("#priId_d").xcombobox("loadData",comboxList);
						$("#priId_d").xcombobox("setValue",data.priId);
					}
					if(data.bugFreq!=null){
						var comboxList = new Array();
						var content = {"id":data.bugFreq.typeId,"text":data.bugFreq.typeName};
						comboxList.push(content);
						$("#bugFreqId_d").xcombobox("loadData",comboxList);
						$("#bugFreqId_d").xcombobox("setValue",data.bugFreqId);
					}
					if(data.importPhase!=null){
						var comboxList = new Array();
						var content = {"id":data.importPhase.typeId,"text":data.importPhase.typeName};
						comboxList.push(content);
						$("#genePhaseId_d").xcombobox("loadData",comboxList);
						$("#genePhaseId_d").xcombobox("setValue",data.genePhaseId);
						$("#genePhaseTdd").show();
					}else{
						$("#genePhaseTdd").hide();
					}
//					if(data.currOwner){
//						var dataStr =  data.currOwner.split("$");
//						if(dataStr[0]==" 处理人"){
//							$("#currHanderName_d").textbox("setValue",dataStr[1]);
//							$("#currOwner_Tr").hide();
//						}else{
//							$("#currOwnerText").html(dataStr[0]+":");
//							$("#currOwner_").textbox("setValue",dataStr[1]);
//							$("#currOwner_Tr").show();
//						}
//					}
					if(data.reProTxt){
						$("#reProTxt_d").textbox("setValue", data.reProTxt);
					}
					if(data.reptDate){
						$("#reptDate_d").textbox("setValue", data.reptDate);
					}
					if(data.currRemark){
						$("#currRemark_d").textbox("setValue", data.currRemark);
					}
					if(data.versionLable!=null && data.versionLable!=""){
						$("#verLableTdd").html(data.versionLable+":");
						$("#versionLabled").textbox("setValue", data.currVersion.versionNum);
						$("#verLableTdd").show();
						$("#verLableValueTdd").show();
					}else{
						$("#verLableTdd").hide();
						$("#verLableValueTdd").hide();
					}
				}
//				$("#bugDetailWin").xdeserialize(data);
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},"json");
//	$("#bugDetailWin").window({title:"修改软件问题报告"});
//	$("#bugDetailWin").window("vcenter");
//	$("#bugDetailWin").window("expand");
//	$("#bugDetailWin").window("open");
	$("#bugDetailHistoryList").xdatagrid('resize');
	
}

//返回
function closeDetailBugWin(){
	$("#detailBugWin").xform('clear');
	$("#detailBugWin").xwindow('close');
}

//处理历史
function bugDetailHistoryList(){
	var bugId = $('#withRepteId').val();
	$("#bugDetailHistoryList").xdatagrid({
		url: baseUrl + '/bugManager/bugManagerAction!loadHistory.action?dto.bug.bugId='+bugId,
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
			$("#bugDetailHistoryList").xdatagrid('resize');
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


//判断是否有文件
function haveFile(bId){
	$.get(
		baseUrl + "/bugManager/bugManagerAction!getFileInfoByTypeId.action",
		{
			'dto.fileInfo.type':'bug',
			'dto.fileInfo.typeId': bId
		},
		function(data,status,xhr){
			if(status=='success'){
				if(data!=null && data.length>0){
					$("#messageImgs").show();
					$("#fileCounts").html(data.length);
					$("#bugfilesLi").mouseenter(function(){
						$("#fileCounts").show();
					});
					$("#bugfilesLi").mouseleave(function(){
						$("#fileCounts").hide();
					});
				}else{
					$("#messageImgs").hide();
				}
				
			}else{
				$("#messageImgs").hide();
			}
		},"json");
}

//文件信息
function bugDetailFile(){
	var RepteId = $('#withRepteId').val();
//	var row = $("#bugList").xdatagrid('getSelected');
	$.get(
			baseUrl + "/bugManager/bugManagerAction!getFileInfoByTypeId.action",
			{
				'dto.fileInfo.type':'bug',
				'dto.fileInfo.typeId': RepteId
			},
			function(data,status,xhr){
				if(status=='success'){
//					$("#messageImgs").hide();
					if(data!=null){
						$("#detailFileFrom").show();
						$("#detailMsg").hide();
						var fileData = new Array();
						var preConfigList = new Array(); 
						for ( var i in data) {
							fileData.push(baseUrl+data[i].filePath);
							var index=data[i].relativeName.lastIndexOf(".");
							var fileTypeName = data[i].relativeName.substring(index+1,data[i].relativeName.length);
							var configJson = {
									downloadUrl:baseUrl + data[i].filePath,
									showRemove:false,
									caption: data[i].relativeName, // 展示的文件名 
									width: '120px', 
									url: '#', // 删除url 
									key: data[i].fileId, // 删除是Ajax向后台传递的参数 
									extra: {id: 100} 
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
						initFileInput(fileData,preConfigList);
					}else{
						$("#detailFileFrom").hide();
						$("#detailFileId").fileinput('destroy');
						$("#detailMsg").show();
					}
					
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			},"json");
}


function initFileInput(fileJson,fileConfigJson){
	$("#detailFileId").fileinput('destroy');
	$("#detailFileId").fileinput({
		theme: 'fa',
		language:'zh',
		uploadUrl:baseUrl + "/uploader?type=bug", // you must set a valid URL here else you will get an error
		deleteUrl:'#',
//		allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx','txt','pdf','doc','docx'],
		showPreview:true,
		overwriteInitial: false,
		uploadAsync:false,
		maxFileSize: 3000,
		maxFileCount:5,
		imageMaxWidth : 100,
		imageMaxHeight : 100,
		enctype: 'multipart/form-data',
		//msgFilesTooMany :"选择图片超过了最大数量",
		showClose:false,
		showRemove:false,
		showBrowse:false,
		showUpload:false,
		showCancel:false,
		showUploadedThumbs:false,
		showClose:false,
		dropZoneEnabled: false,//是否显示拖拽区域
		allowedPreviewTypes:['image', 'html', 'text', 'video', 'pdf', 'flash','object'],
		fileActionSettings:{showUpload: false},// 控制上传按钮是否显示
		initialPreviewAsData: true,
		initialPreviewShowDelete:false,
		initialPreview:fileJson,
	    initialPreviewConfig:fileConfigJson,
		previewSettings: {
	    },
		slugCallback: function (filename) {
		  return filename.replace('(', '_').replace(']', '_');
		}
	}).on("filebatchselected", function(event, files) {
		//选择文件后处理
	}).on('filebatchuploadsuccess', function(event, data, previewId, index) {
		fileInfos = data.response;
	}).on('filebatchuploaderror', function(event, data,  previewId, index) {
	}).on('filepreupload', function(event, data, previewId, index) {    
	}).on('fileerror', function(event, data, msg) {
	}).on("fileuploaded", function(event, data, previewId, index) {
		//上传成功后处理方法
	});
	
}

//# sourceURL=bugDetail.js