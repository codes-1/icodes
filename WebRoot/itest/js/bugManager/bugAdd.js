var fileInfos = new Array();
var delFiles = new Array();
$(function(){
	$.parser.parse();
	addBugWindow();
	
//	//选择项目
//	$("#task_Name").next('span').children('input:first-child').click(function(){
//		taskItems();
//	})
	//选择需求
	$("#module_Name").next('span').children('input:first-child').click(function(){
		needsTree("module_Id","module_Name","reProTxt","add");
	})
	
	$("#bugReptVer").next('span').children('input:eq(0)').click(function(){
		if(taskId=="" || taskId==undefined){
			$.xalert({title:'提示',msg:'请先选择项目！'});
			return;
		}
	})
	$("#bugTypeId").next('span').children('input:eq(0)').click(function(){
		if(taskId=="" || taskId==undefined){
			$.xalert({title:'提示',msg:'请先选择项目！'});
			return;
		}
	})
	$("#bugGradeId").next('span').children('input:eq(0)').click(function(){
		if(taskId=="" || taskId==undefined){
			$.xalert({title:'提示',msg:'请先选择项目！'});
			return;
		}
	})
	$("#platformId").next('span').children('input:eq(0)').click(function(){
		if(taskId=="" || taskId==undefined){
			$.xalert({title:'提示',msg:'请先选择项目！'});
			return;
		}
	})
	$("#sourceId").next('span').children('input:eq(0)').click(function(){
		if(taskId=="" || taskId==undefined){
			$.xalert({title:'提示',msg:'请先选择项目！'});
			return;
		}
	})
	$("#bugOccaId").next('span').children('input:eq(0)').click(function(){
		if(taskId=="" || taskId==undefined){
			$.xalert({title:'提示',msg:'请先选择项目！'});
			return;
		}
	})
	$("#geneCauseId").next('span').children('input:eq(0)').click(function(){
		if(taskId=="" || taskId==undefined){
			$.xalert({title:'提示',msg:'请先选择项目！'});
			return;
		}
	})
	$("#priId").next('span').children('input:eq(0)').click(function(){
		if(taskId=="" || taskId==undefined){
			$.xalert({title:'提示',msg:'请先选择项目！'});
			return;
		}
	})
	$("#bugFreqId").next('span').children('input:eq(0)').click(function(){
		if(taskId=="" || taskId==undefined){
			$.xalert({title:'提示',msg:'请先选择项目！'});
			return;
		}
	})
});

//增加
function addBugWindow(){
	$('#clearUpFlg').prop("checked",true);
	$("#task__Id").val(taskId);
	if(!rootNodeId){
		$("#module_Id").val(currNodeId);
		$("#module_Name").textbox("setValue", moduleNameM);
		$("#reProTxt").textbox("setValue", moduleNameM+": ");
	}
	addInit(taskId);
//	$("#bugAddOrEditWindown").window({title:"新增软件问题报告"});
//	$("#bugAddOrEditWindown").window("vcenter");
//	$("#bugAddOrEditWindown").window("open");
}


//提交保存新增或修改
function bugSubmitForm(submitId) {
	var clearUpFlg = $('#clearUpFlg').is(':checked');
	var valid = $("#bugAddOrEditWindown").xform('validate');
	if (!valid) {
		return;
	}
	var testDisplayFlag=  $("#testOwnerTr")[0].style.display;
	var analDisplayFlag=  $("#analOwnerTr")[0].style.display;
	var assignDisplayFlag=  $("#assignOwnerTr")[0].style.display;
	var devDisplayFlag=  $("#devOwnerTr")[0].style.display;
	var interceDisplayFlag=  $("#intercessOwnerTr")[0].style.display;
	if(testDisplayFlag==""){
		var	value =$("#testOwnerId").xcombobox("getValue"); 
		if(value==""){
			 $.xalert({title:'提示',msg:'请选择互验人'});
			 return;
		}
	}
	if(analDisplayFlag==""){
		var	value =$("#analyseOwnerId").xcombobox("getValue"); 
		if(value==""){
			 $.xalert({title:'提示',msg:'请选择分析人'});
			 return;
		}
	}
	if(assignDisplayFlag==""){
		var	value =$("#assignOwnerId").xcombobox("getValue"); 
		if(value==""){
			 $.xalert({title:'提示',msg:'请选择分配人'});
			 return;
		}
	}
	if(devDisplayFlag==""){
		var	value =$("#devOwnerId").xcombobox("getValue"); 
		if(value==""){
			 $.xalert({title:'提示',msg:'请选择修改人'});
			 return;
		}
	}
	if(interceDisplayFlag==""){
		var	value =$("#intercessOwnerId").xcombobox("getValue"); 
		if(value==""){
			 $.xalert({title:'提示',msg:'请选择仲裁人'});
			 return;
		}
	}
	var bugId = $("#bugId").val();
	var url;
	var filesCount = $('#uploadFileId').fileinput('getFilesCount');
	if(filesCount>0){
		 $.xalert({title:'提示',msg:'您还有选中的文件没上传，请先上传!'});
		 return ;
	}
	$('#submit_'+submitId).linkbutton('disable');
	var fileParam = {'dto.fileInfos':JSON.stringify(fileInfos)};
	var dataParams = Object.assign($("#bugAddOrEditForm").xserialize(), fileParam);
	if(bugId==""){
		url = baseUrl + "/bugManager/bugManagerAction!add.action";
	}else{
		url=baseUrl + "/bugManager/bugManagerAction!update.action";
	}
	$.post(
		url,
		dataParams,
//		$("#bugAddOrEditForm").xserialize(),
		function(data) {
			if (data =="success") {
				fileInfos = new Array();
				$('#uploadFileId').fileinput('clear');
				getBugList();
				$('#submit_'+submitId).linkbutton('enable');
				if(bugId==""){
					if(submitId=="1"){
						 $.xalert({title:'提示',msg:'增加成功！',okFn:function(){
							 $("#bugAddOrEditWindown").xform('clear');
							 $("#bugAddOrEditWindown").xwindow('close');
						 }});
					}else if(submitId=="2"){
//						 $.xalert({title:'提示',msg:'增加成功！',okFn:function(){
//							 $("#bugDesc").textbox("setValue","");
//							 var oldModuleName = $("#module_Name").textbox("getValue");
//							 $("#reProTxt").textbox("setValue",oldModuleName+":");
//							 if(!clearUpFlg){
//								 $("#bugAddOrEditWindown").xform('clear');
//							 }
//						 }});
						$("#bugDesc").textbox("setValue","");
						 var oldModuleName = $("#module_Name").textbox("getValue");
						 $("#reProTxt").textbox("setValue",oldModuleName+":");
						 $('#uploadFileId').fileinput('clear');
						 if(!clearUpFlg){
							 $("#bugAddOrEditWindown").xform('clear');
						 }
					}
					
				}
				
			} else {
				fileInfos = new Array();
				
				$('#submit_'+submitId).linkbutton('enable');
				 $.xalert({title:'提示',msg:'系统错误！'});
			}
		}, "text");
}


//初始化下拉菜单
function addInit(){
	$.get(
		baseUrl + '/bugManager/bugManagerAction!addInit.action?dto.isAjax=true&dto.loadType=1&dto.taskId='+taskId,
		function(data,status,xhr){
			if(status=='success'){
				if(data!=null || data!=""){
					var str = data;
					var arr = data.split('^');
					for ( var key in arr) {
						var subKey = arr[key].substring(0,arr[key].indexOf('='));
						var subValue = arr[key].substring(arr[key].indexOf('=')+1,arr[key].length);
						if(subKey=="stateName"){
							$("#stateName").textbox("setValue", subValue);
						}
						if(subKey=="verSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#bugReptVer").xcombobox("loadData",comboxList);
							$("#bugReptVer").xcombobox({
								onSelect:function(param){
									$("#bugReptVerName").textbox("setValue", param.text);
								}
							});
							
						}
						if(subKey=="typeSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							var deft;
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var start  = val[0].indexOf("def_");
								if(start!=0){
									var content = {"id":val[0],"text":val[1]};
									comboxList.push(content);
								}else{
									deft = val;
								}
							}
							
							$("#bugTypeId").xcombobox("loadData",comboxList);
							$("#bugTypeId").xcombobox({
								onSelect:function(param){
									$("#bugTypeName").textbox("setValue", param.text);
								}
							});
							if(deft!=undefined){
								$("#bugTypeId").xcombobox("setValue",deft[0].replace("def_",""));
								$("#bugTypeName").textbox("setValue", deft[1]);
							}
							
						}
						if(subKey=="gradeSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							var defg;
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var start  = val[0].indexOf("def_");
								if(start!=0){
									var content = {"id":val[0],"text":val[1]};
									comboxList.push(content);
								}else{
									defg = val;
								}
							}
							$("#bugGradeId").xcombobox("loadData",comboxList);
							$("#bugGradeId").xcombobox({
								onSelect:function(param){
									$("#bugGradeName").textbox("setValue", param.text);
								}
							});
							
							if(defg!=undefined){
								$("#bugGradeId").xcombobox("setValue",defg[0].replace("def_",""));
								$("#bugGradeName").textbox("setValue", defg[1]);
							}
						}
						if(subKey=="plantFormSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							var defp;
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var start  = val[0].indexOf("def_");
								if(start!=0){
									var content = {"id":val[0],"text":val[1]};
									comboxList.push(content);
								}else{
									defp = val;
								}
							}
							$("#platformId").xcombobox("loadData",comboxList);
							$("#platformId").xcombobox({
								onSelect:function(param){
									$("#pltformName").textbox("setValue", param.text);
								}
							});
							if(defp!=undefined){
								$("#platformId").xcombobox("setValue",defp[0].replace("def_",""));
								$("#pltformName").textbox("setValue", defp[1]);
							}
						}
						if(subKey=="sourceSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							var defs;
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var start  = val[0].indexOf("def_");
								if(start!=0){
									var content = {"id":val[0],"text":val[1]};
									comboxList.push(content);
								}else{
									defs = val;
								}
							}
							$("#sourceId").xcombobox("loadData",comboxList);
							$("#sourceId").xcombobox({
								onSelect:function(param){
									$("#sourceName").textbox("setValue", param.text);
								}
							});
							if(defs!=undefined){
								$("#sourceId").xcombobox("setValue",defs[0].replace("def_",""));
								$("#sourceName").textbox("setValue", defs[1]);
							}
						}
						if(subKey=="occaSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							var defo;
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var start  = val[0].indexOf("def_");
								if(start!=0){
									var content = {"id":val[0],"text":val[1]};
									comboxList.push(content);
								}else{
									defo = val;
								}
							}
							$("#bugOccaId").xcombobox("loadData",comboxList);
							$("#bugOccaId").xcombobox({
								onSelect:function(param){
									if(param.text=="首次出现"){
										$("#geneCauseTd").show();
										$("#geneCauseIdTd").show();
									}else{
										$("#geneCauseTd").hide();
										$("#geneCauseIdTd").hide();
									}
									$("#occaName").textbox("setValue", param.text);
								}
							});
							if(defo!=undefined){
								$("#bugOccaId").xcombobox("setValue",defo[0].replace("def_",""));
								$("#occaName").textbox("setValue", defo[1]);
								if(defo[1]=="首次出现"){
									$("#geneCauseTd").show();
									$("#geneCauseIdTd").show();
								}else{
									$("#geneCauseTd").hide();
									$("#geneCauseIdTd").hide();
								}
							}
						}
						if(subKey=="geCaseSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							var defc;
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var start  = val[0].indexOf("def_");
								if(start!=0){
									var content = {"id":val[0],"text":val[1]};
									comboxList.push(content);
								}else{
									defc = val;
								}
							}
							$("#geneCauseId").xcombobox("loadData",comboxList);
							$("#geneCauseId").xcombobox({
								onSelect:function(param){
									$("#geneCaseName").textbox("setValue", param.text);
								}
							});
							
							if(defc!=undefined){
								$("#geneCauseId").xcombobox("setValue",defc[0].replace("def_",""));
								$("#geneCaseName").textbox("setValue", defc[1]);
							}
						}
						if(subKey=="priSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							var defps;
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var start  = val[0].indexOf("def_");
								if(start!=0){
									var content = {"id":val[0],"text":val[1]};
									comboxList.push(content);
								}else{
									defps = val;
								}
							}
							$("#priId").xcombobox("loadData",comboxList);
							$("#priId").xcombobox({
								onSelect:function(param){
									$("#priName").textbox("setValue", param.text);
								}
							});
							if(defps!=undefined){
								$("#priId").xcombobox("setValue",defps[0].replace("def_",""));
								$("#priName").textbox("setValue", defps[1]);
							}
						}
						if(subKey=="freqSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							var deff;
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var start  = val[0].indexOf("def_");
								if(start!=0){
									var content = {"id":val[0],"text":val[1]};
									comboxList.push(content);
								}else{
									deff = val;
								}
							}
							$("#bugFreqId").xcombobox("loadData",comboxList);
							$("#bugFreqId").xcombobox({
								onSelect:function(param){
									if(param.text=="偶尔出现"){
										$("#reproTd").show();
										$("#reproPersentTd").show();
									}else{
										$("#reproTd").hide();
										$("#reproPersentTd").hide();
									}
									$("#bugFreqName").textbox("setValue", param.text);
								}
							});
							if(deff!=undefined){
								$("#bugFreqId").xcombobox("setValue",deff[0].replace("def_",""));
								$("#bugFreqName").textbox("setValue", deff[1]);
								if(deff[1]=="偶尔出现"){
									$("#reproTd").show();
									$("#reproPersentTd").show();
								}else{
									$("#reproTd").hide();
									$("#reproPersentTd").hide();
								}
							}
						}
						if(subKey=="genePhaseSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							var defgp;
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var start  = val[0].indexOf("def_");
								if(start!=0){
									var content = {"id":val[0],"text":val[1]};
									comboxList.push(content);
								}else{
									defgp = val;
								}
							}
							$("#genePhaseId").xcombobox("loadData",comboxList);
							$("#genePhaseId").xcombobox({
								onSelect:function(param){
									$("#genPhName").textbox("setValue", param.text);
								}
							});
							if(defgp!=undefined){
								$("#genePhaseId").xcombobox("setValue",defgp[0].replace("def_",""));
								$("#genPhName").textbox("setValue", defgp[1]);
							}
						}
						if(subKey=="testSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#testOwnerId").xcombobox("loadData",comboxList);
//							$("#testOwnerId").xcombobox({required:true}); 
							$("#testOwnerId").xcombobox({
								onSelect:function(param){
									$("#testOwnName").textbox("setValue", param.text);
									$("#nextOwnerId").val(param.id);
								}
							});
							$("#testOwnerTr").show();
						}else{
//							$("#testOwnerId").xcombobox({required:false}); 
//							$("#testOwnerTr").hide();
						}
						if(subKey=="analySelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#analyseOwnerId").xcombobox("loadData",comboxList);
//							$("#analyseOwnerId").xcombobox({required:true}); 
							$("#analyseOwnerId").xcombobox({
								onSelect:function(param){
									$("#analOwnerName").textbox("setValue", param.text);
									$("#nextOwnerId").val(param.id);
								}
							});
							$("#analOwnerTr").show();
						}else{
//							$("#analyseOwnerId").xcombobox({required:false}); 
//							$("#analOwnerTr").hide();
						}
						if(subKey=="assignSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#assignOwnerId").xcombobox("loadData",comboxList);
//							$("#assignOwnerId").xcombobox({required:true}); 
							$("#assignOwnerId").xcombobox({
								onSelect:function(param){
									$("#anasnOwnerName").textbox("setValue", param.text);
									$("#nextOwnerId").val(param.id);
								}
							});
							$("#assignOwnerTr").show();
							getMdPersonDef('3','module_Id','assignOwnerId','anasnOwnerName');
						}else{
//							$("#assignOwnerId").xcombobox({required:false}); 
//							$("#assignOwnerTr").hide();
						}
						if(subKey=="devStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#devOwnerId").xcombobox("loadData",comboxList);
//							$("#devOwnerId").xcombobox("required",true);
							$("#devOwnerId").xcombobox({
								onSelect:function(param){
									$("#devOwnerName").textbox("setValue", param.text);
									$("#nextOwnerId").val(param.id);
								}
							});
							getMdPersonDef('1','module_Id','devOwnerId','devOwnerName');
							$("#devOwnerTr").show();
						}else{
//							$("#devOwnerId").xcombobox({required:false}); 
//							$("#devOwnerTr").hide();
						}
						if(subKey=="interCesSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#intercessOwnerId").xcombobox("loadData",comboxList);
//							$("#intercessOwnerId").xcombobox({required:true}); 
							$("#intercessOwnerId").xcombobox({
								onSelect:function(param){
									$("#intecsOwnerName").textbox("setValue", param.text);
									$("#nextOwnerId").val(param.id);
								}
							});
							$("#intercessOwnerTr").show();
						}else{
//							$("#intercessOwnerId").xcombobox({required:false}); 
//							$("#intercessOwnerTr").hide();
						}
						if(subKey=="currFlowCd"){
							$("#currFlowCd").val(subValue);
						}
						
						if(subKey=="nextFlowCd"){
							$("#nextFlowCd").val(subValue);
						}
						
						if(subKey=="currStateId"){
							$("#currStateId").val(subValue);
						}
						if(subKey=="testPhase"){
							if(subValue!="" && subValue!=null && subValue!="null"){
								$("#testPhase").val(subValue);
							}
						}
						
					}
				}else{
					$.xalert({title:'提示',msg:'没有查到相关记录！'});
				}
			}else if(data=="noOutLine"){
				$.xalert({title:'提示',msg:$("#analyprojectName").val()+'项目没有提交测试需求，请先提交测试！',okFn:locationToMenu});
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},
	"text");
}

//测试需求开发人员
function getMdPersonDef(loadType,moduleId,nextid,nextNameid){
	if($("#"+moduleId).val()==""){
//		$.xalert({title:'提示',msg:'当前没指定对应的测试需求'});
		return;
	}
	var url = baseUrl + "/bugManager/bugManagerAction!loadMdPerson.action?dto.currNodeId="+$("#"+moduleId).val()+"&dto.loadType="+loadType;

	$.get(
		url,
		function(data,status,xhr){
			if(data!=null ){
				if(data.total==0){
					$("#"+nextNameid).val("");
					$("#"+nextid).xcombobox("clear");
				}else if(data.total==1){
					$("#"+nextNameid).val(data.rows[0].valueObj);
					$("#"+nextid).combobox("setValue", data.rows[0].keyObj);
				}
				
			}
		},
	"json");
}


//测试需求开发人员
function getMdPerson(loadType,moduleId,nextid,nextNameid){
	if($("#"+moduleId).val()==""){
		$.xalert({title:'提示',msg:'当前没指定对应的测试需求'});
		return;
	}
	var url = baseUrl + "/bugManager/bugManagerAction!loadMdPerson.action?dto.currNodeId="+$("#"+moduleId).val()+"&dto.loadType="+loadType;
	$("#testUserList").xdatagrid({
		url: url,
		method: 'get',
		singleSelect:true,
		rownumbers: true,
//		scrollbarSize:100,
		fitColumns:true,
		pagination: false,
		pageNumber: 1,
		pageSize: 100,
	    layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],
		pageList: [10,30,50,100],
		showPageList:true,
		columns:[[
			{field:'valueObj',title:'单击选择',width:'100%',align:'center',halign:'center'},
		]],
		onLoadSuccess:function(data){
			if(data.total==0){
				if(loadType=="1"){
					$.xalert({title:'提示',msg:'当前测试需求没指定开发人员'});
				}else{
					$.xalert({title:'提示',msg:'当前测试需求没指定分配人员'});
				}
			}/*else if(data.total==1){
				$("#"+nextNameid).val(data.rows[0].valueObj);
				$("#"+nextid).combobox("setValue", data.rows[0].keyObj);
			}*/else{
				if(loadType=="1"){
					$("#testUserWin").window({title:"修改人"});
				}else{
					$("#testUserWin").window({title:"分配人"});
				}
				$("#testUserWin").window("vcenter");
				$("#testUserWin").xwindow('open');
				$("#testUserList").xdatagrid('resize');
			}
		},
		onClickRow: function (index, row) {
			$("#"+nextNameid).val(row.valueObj);
			$("#"+nextid).combobox("setValue", row.keyObj);
			$("#testUserWin").xwindow('close');
	  }
		
	});
}

////测试需求
//function needsTree(textId,nameId){
//	if(taskId=="" || taskId==undefined){
//		$.xalert({title:'提示',msg:'请先选择项目！'});
//		return;
//	}
//	var rootName="";
//	$('#needsTree').xtree({
//	    url:baseUrl+'/caseManager/caseManagerAction!loadTree.action?dto.taskId='+taskId+'&dto.command=',
//	    method:'get',
//		animate:true,
//		lines:true,
////		dnd:true,
//		onLoadSuccess:function(node,data){  
//			if(data!=null){
//				if(node==null){
//					rootName = data[0].text;
//					var nodeDep = $('#needsTree').xtree('find',data[0].id);  
//					$(nodeDep.target).tooltip({
//						position: 'right',
//						content: '<span style="color:#fff">root节点测试需求，不能增加软件问题.</span>',
//						hideDelay:100,
//						onShow: function(){
//							$(this).tooltip('tip').css({
//								backgroundColor: '#666',
//								borderColor: '#666'
//							});
//						}
//					});
//					
//				}
//				$("#needsTreeWin").window({title:"选择需求  <span style='font-size:13px;'>(双击选择)<span>"});
//				$("#needsTreeWin").window("vcenter");
//				$("#needsTreeWin").xwindow('open');
//			}else{
//				$.xalert({title:'提示',msg:'当前项目没提交测试需求,不能填写软件问题报告'});
//			}
//		 }, 
//		onClick: function(node){
////			currNodeId = node.id;
//		},
//		onDblClick:function(node){
//			if(node!=null && !node.rootNode){
//				var moduleName=node.text;
//				var parentNode = $(this).xtree('getParent',node.target);
//				while(!parentNode.rootNode){
//					moduleName = parentNode.text+"/"+moduleName;
//					parentNode = $(this).xtree('getParent',parentNode.target);
//				}
//				moduleName = parentNode.text+"/"+moduleName;
//				$("#"+textId).val(node.id);
//				var oldModuleName = $("#"+nameId).textbox("getValue");
//				$("#"+nameId).textbox("setValue", moduleName);
//				var reText=  $("#reProTxt").textbox("getValue");
//				if(reText==""){
//					$("#reProTxt").textbox("setValue", moduleName+": ");
//				}else{
//					var newReText = reText.replace(oldModuleName,moduleName);
//					$("#reProTxt").textbox("setValue", newReText);
//				}
//				$("#needsTreeWin").xwindow('close');
//			}
//		},
//		onContextMenu:function(e,node){
//			e.preventDefault();
//			$(this).xtree('select', node.target);
//		}
//	});
//}


$("#uploadFileId").fileinput({
	theme: 'fa',
	language:'zh',
	uploadUrl:baseUrl + "/uploader?type=bug",
//	allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx','txt','pdf','doc','docx'],
	showPreview:true,
	overwriteInitial: false,
	uploadAsync:false,
	maxFileSize: 3000,
	maxFileCount:5,
	autoReplace:true,
	imageMaxWidth : 100,
	imageMaxHeight : 100,
	enctype: 'multipart/form-data',
	showRemove:false,
	showClose:false,
//	showCaption:false,
	allowedPreviewTypes:['image', 'html', 'text', 'video', 'pdf', 'flash','object'],
	dropZoneEnabled:false,
	fileActionSettings:{
        showUpload: false,
        showRemove:true,
//		url: baseUrl + "/uploader?type=del",// 删除url 
    },
	slugCallback: function (filename) {
	  return filename.replace('(', '_').replace(']', '_');
	}
}).on("filebatchselected", function(event, files) {
	//选择文件后处理
//	$(this).fileinput("upload");
//	var filesFrames = $('#uploadFileId').fileinput('getFrames');
//	if(filesFrames.length>5){
//		$.xalert({title:'提示',msg:'最多上传5个文件！'});
////		$('#uploadFileId').fileinput('disable');
//		return;
//	}else{
////		$('#uploadFileId').fileinput('enable');
//	}
}).on('fileimageuploaded', function(event, data, previewId, index) {
	var filesCount = $('#uploadFileId').fileinput('getFilesCount');
}).on('filebatchuploadsuccess', function(event, data, previewId, index) {
	fileInfos = fileInfos.concat(data.response);
	setTimeout(function(){
		$(".kv-upload-progress").hide();
	}, 2000);
}).on('filebatchuploaderror', function(event, data,  previewId, index) {
}).on('filesuccessremove', function(event,id) {
	var aa =fileInfos;
//	alert(event+"=============="+id);
}).on('fileloaded', function(event, data,  previewId, index) {
	var filesFrames = $('#uploadFileId').fileinput('getFrames');
	if(filesFrames.length>5){
		$.xalert({title:'提示',msg:'最多上传5个文件！'});
		$("#"+previewId).remove();
		return;
	}
	
	
}).on("fileuploaded", function(event, data, previewId, index) {
	//上传成功后处理方法
});

//# sourceURL=bugAdd.js