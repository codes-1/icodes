var caseId;
$(function(){
	$.parser.parse();
	caseId = getQueryParam('caseId');
	lookFormWindow(caseId);
	haveFiles();
	//点击基本信息隐藏历史列表
	$('a[href="#caseDetailInfo"]').click(function(){
		$('#caseHistory').next().hide();
	});
	$('a[href="#caseLookFiles"]').click(function(){
		$('#caseHistory').next().hide();
	});
	
});


//获取页面url参数
function getQueryParam(name) {
       var obj = $('#lookCaseWindown').xwindow('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

//查看
function lookFormWindow(caseId){
	
	$('#caseHistory').next().hide();
	loadCaseCategory();
	loadCaseYXJ();
	$.get(
		baseUrl + "/caseManager/caseManagerAction!viewDetal.action",
		{'dto.testCaseInfo.testCaseId': caseId},
		function(data,status,xhr){
			if(status=='success'){
				$("#lookForm").xdeserialize(data);
//				lookCaseFile(testCaseId);
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},
		"json");
//	$("#lookCaseWindown")[0].style.maxHeight=mainObjs.tableHeight-40;
//	$("#lookCaseWindown").window({title:"用例详情"});
	$("#lookCaseWindown").window("vcenter");
//	$("#lookCaseWindown").window("open");
}

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

//判断是否有文件
function haveFiles(){
	$.get(
		baseUrl + "/fileInfo/fileInfoAction!getFileInfoByTypeId.action",
		{
			'dto.fileInfo.type':'case',
			'dto.fileInfo.typeId': caseId
		},
		function(data,status,xhr){
			if(status=='success'){
				if(data!=null && data.length>0){
					$("#messageImg").show();
					$("#fileCount").html(data.length);
					$("#casefileLi").mouseenter(function(){
						$("#fileCount").show();
					});
					$("#casefileLi").mouseleave(function(){
						$("#fileCount").hide();
					});
				}else{
					$("#messageImg").hide();
				}
				
			}else{
				$("#messageImg").hide();
			}
		},"json");
}


//查看文件
function lookCaseFile(){
	$('#caseHistory').next().hide();
	$.get(
			baseUrl + "/fileInfo/fileInfoAction!getFileInfoByTypeId.action",
			{
				'dto.fileInfo.type':'case',
				'dto.fileInfo.typeId': caseId
			},
			function(data,status,xhr){
				if(status=='success'){
//					$(".file-caption-main").hide();
					if(data!=null){
						$("#lookFilesForm").show();
						$("#lookMsg").hide();
						var fileData = new Array();
						var preConfigList = new Array(); 
						for ( var i in data) {
							fileData.push(baseUrl+data[i].filePath);
							var index=data[i].relativeName.lastIndexOf(".");
							var fileTypeName = data[i].relativeName.substring(index+1,data[i].relativeName.length);
							var configJson = {
									showDownload: true,
									showRemove:false,
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
						lookFileInput(fileData,preConfigList);
					}else{
						$("#lookFilesForm").hide();
						$("#caseLookFileId").fileinput('destroy');
						$("#lookMsg").show();
					}
				}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},"json");
}

function lookFileInput(fileJson,fileConfigJson){
	$("#caseLookFileId").fileinput('clear');
	$("#caseLookFileId").fileinput('destroy');
	$("#caseLookFileId").fileinput({
		theme: 'fa',
		language:'zh',
		uploadUrl:"",
//		allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx','txt','pdf','doc','docx','object','gdocs'],
		showPreview:true,
		showClose:false,
		overwriteInitial: true,
		uploadAsync:false,
		autoReplace: true,
		showUploadedThumbs:false,
		maxFileSize: 6000,
		maxFileCount:5,
		imageMaxWidth : 200,
		imageMaxHeight : 100,
		enctype: 'multipart/form-data',
		showClose:false,
		showRemove:false,
		showBrowse:false,
		showUpload:false,
		showCancel:false,
		showUploadedThumbs:false,
		showClose:false,
		dropZoneEnabled: false,//是否显示拖拽区域
		allowedPreviewTypes:['image', 'html', 'text', 'video', 'pdf', 'flash','object'],
		initialPreviewAsData: true,
		initialPreviewShowDelete:false,
		initialPreview:fileJson,
	    initialPreviewConfig:fileConfigJson,
		slugCallback: function (filename) {
		  return filename.replace('(', '_').replace(']', '_');
		}
	});
//	$("#caseLookFileId").fileinput('disable');
	$(".file-caption-main").hide();
}

//# sourceURL=lookCase.js
