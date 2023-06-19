$(function(){
	$.parser.parse();
	
	 var container = getQueryParam('queryContainer');
	 findFlag = container;
	 $("#containerList").val(container);
	 $('#reptDateFend').datebox({
			onSelect: function(date){
				var reptDateF = $("#reptDateF").datebox("getValue");
				var reptDateFend = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
				if(reptDateF!=""){
					if(!compareDate(reptDateF,reptDateFend)){
						$.xalert({title:'提示',msg:'发现日期止应该大于等于发现日期起！'});
						$(this).datebox("clear");
					}
				}
				
			}
		});
		
		$('#reptDateF').datebox({
			onSelect: function(date){
				var reptDateFend = $("#reptDateFend").datebox("getValue");
				var reptDateF = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
				if(reptDateFend!=""){
					if(!compareDate(reptDateF,reptDateFend)){
						$.xalert({title:'提示',msg:'发现日期起应该小于等于发现日期止！'});
						$(this).datebox("clear");
					}
				}
				
			}
		});
	//选择需求
	$("#moduleNameF").next('span').children('input:first-child').click(function(){
		findNeedsTree("moduleIdF","moduleNameF","clearModuleBnt");
	});
	$("#testOwnNameF").next('span').children('input:first-child').click(function(){
		openFindUserWin("testOwnerIdF","testOwnNameF","选择测试人员");
	});
	$("#devOwnerNameF").next('span').children('input:first-child').click(function(){
		openFindUserWin("devOwnerIdF","devOwnerNameF","选择开发人员");
	});
	$("#nextOwnerIdFName").next('span').children('input:first-child').click(function(){
		openFindUserWin("nextOwnerIdF","nextOwnerIdFName","选择待处理人");
	});
	$("#chargeOwnerNameF").next('span').children('input:first-child').click(function(){
		openFindUserWin("chargeOwnerIdF","chargeOwnerNameF","选择责任人");
	});
	findInit();
});

//获取页面url参数
function getQueryParam(name) {
       var obj = $('#queryBugWin').xwindow('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

//初始化下拉菜单
function findInit(){
	$.get(
		baseUrl + '/bugManager/bugManagerAction!findInit.action?dto.loadType=1',
		function(data,status,xhr){
			if(status=='success'){
				if(data!=null || data!=""){
					var str = data;
					var arr = data.split('^');
					for ( var key in arr) {
						var subKey = arr[key].substring(0,arr[key].indexOf('='));
						var subValue = arr[key].substring(arr[key].indexOf('=')+1,arr[key].length);
						if(subKey=="stateList"){
							$("#currStateIdF").xcombobox("loadData",JSON.parse(subValue));
						}
						
						if(subKey=="verSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#bugReptVerF").xcombobox("loadData",comboxList);
							$("#bugReptVerF").xcombobox({
								onSelect:function(param){
									$("#bugReptVerNameF").textbox("setValue", param.text);
								}
							});
							
						}
						if(subKey=="typeSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#bugTypeIdF").xcombobox("loadData",comboxList);
							$("#bugTypeIdF").xcombobox({
								onSelect:function(param){
									$("#bugTypeNameF").textbox("setValue", param.text);
								}
							});
						}
						if(subKey=="gradeSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#bugGradeIdF").xcombobox("loadData",comboxList);
							$("#bugGradeIdF").xcombobox({
								onSelect:function(param){
									$("#bugGradeNameF").textbox("setValue", param.text);
								}
							});
						}
						if(subKey=="plantFormSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#platformIdF").xcombobox("loadData",comboxList);
							$("#platformIdF").xcombobox({
								onSelect:function(param){
									$("#pltformNameF").textbox("setValue", param.text);
								}
							});
						}
						if(subKey=="sourceSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#sourceIdF").xcombobox("loadData",comboxList);
							$("#sourceIdF").xcombobox({
								onSelect:function(param){
									$("#sourceNameF").textbox("setValue", param.text);
								}
							});
						}
						if(subKey=="occaSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#bugOccaIdF").xcombobox("loadData",comboxList);
							$("#bugOccaIdF").xcombobox({
								onSelect:function(param){
									$("#occaNameF").textbox("setValue", param.text);
								}
							});
						}
						if(subKey=="geCaseSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#geneCauseIdF").xcombobox("loadData",comboxList);
							$("#geneCauseIdF").xcombobox({
								onSelect:function(param){
									$("#geneCaseNameF").textbox("setValue", param.text);
								}
							});
						}
						if(subKey=="priSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#priIdF").xcombobox("loadData",comboxList);
							$("#priIdF").xcombobox({
								onSelect:function(param){
									$("#priNameF").textbox("setValue", param.text);
								}
							});
						}
						if(subKey=="freqSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#bugFreqIdF").xcombobox("loadData",comboxList);
							$("#bugFreqIdF").xcombobox({
								onSelect:function(param){
									$("#bugFreqNameF").textbox("setValue", param.text);
								}
							});
						}
						if(subKey=="genePhaseSelStr" && subValue!=""){
							var comboxList = new Array();
							var dd = subValue.split("$");
							for ( var kk in dd) {
								var val = dd[kk].split(";");
								var content = {"id":val[0],"text":val[1]};
								comboxList.push(content);
							}
							$("#genePhaseIdF").xcombobox("loadData",comboxList);
							$("#genePhaseIdF").xcombobox({
								onSelect:function(param){
									$("#genPhNameF").textbox("setValue", param.text);
								}
							});
						}
						
//						if(subKey=="testSelStr" && subValue!=""){
//							var comboxList = new Array();
//							var dd = subValue.split("$");
//							for ( var kk in dd) {
//								var val = dd[kk].split(";");
//								var content = {"id":val[0],"text":val[1]};
//								comboxList.push(content);
//							}
//							$("#testOwnerIdF").xcombobox("loadData",comboxList);
//							$("#testOwnerIdF").xcombobox({
//								onSelect:function(param){
//									$("#testOwnNameF").textbox("setValue", param.text);
//								}
//							});
//						}
//						if(subKey=="devStr" && subValue!=""){
//							var comboxList = new Array();
//							var dd = subValue.split("$");
//							for ( var kk in dd) {
//								var val = dd[kk].split(";");
//								var content = {"id":val[0],"text":val[1]};
//								comboxList.push(content);
//							}
//							$("#devOwnerIdF").xcombobox("loadData",comboxList);
//							$("#devOwnerIdF").xcombobox({
//								onSelect:function(param){
//									$("#devOwnerNameF").textbox("setValue", param.text);
//								}
//							});
//						}
//						console.log(subKey+':'+subValue);
					}
					if(!$.isEmptyObject(queryParam)){
						$("#bugQueryForm_").xdeserialize(queryParam);
//						$("#reptDateF").datebox("setValue",new Date());
						var def=true;
						for ( var queryKey in queryParam) {
							if(queryKey=="dto.bug.reptDate"){
								$("#reptDateF").datebox("setValue",queryParam[queryKey]);
							}
							if(queryKey=="dto.reptDateEnd"){
								$("#reptDateFend").datebox("setValue",queryParam[queryKey]);
							}
							if(queryKey =="dto.defBug"){
								def = false;
							}
						}
						if(def==false){
							$("#defBugId").attr("checked",'true');//选 
						}else{
							$("#defBugId").removeAttr("checked");//取消
							
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

//查询提交
function queryBugSubmit(){
	var container = $("#containerList").val();
	queryParam = $("#bugQueryForm_").xserialize();
	if(container =="findBugList"){
		fingBugs();
//		$("#findBugList").xdatagrid('loadData',data);
	}else{
//		$("#bugList").xdatagrid('loadData',data);
		getBugList();
		$("#queryBugWin").xwindow('close');
	}
//	$.get(
//		baseUrl + '/bugManager/bugManagerAction!findBug.action?dto.taskId='+taskId,
//		$("#queryBugWin").xserialize(),
//		function(data,status,xhr){
//			if(status=='success'){
//				if(data!=null || data.total!=0){
//					if(container =="findBugList"){
//						fingBugs();
//						$("#findBugList").xdatagrid('loadData',data);
//					}else{
//						$("#bugList").xdatagrid('loadData',data);
//						$("#queryBugWin").xwindow('close');
//					}
//				}else{
//					$.xalert({title:'提示',msg:'没有查到相关记录！'});
//				}
//			}else{
//				$.xalert({title:'提示',msg:'系统错误！'});
//			}
//		},
//	"json");
}
//清空查询输入框
function clearBugQueryForm(){
	$("#queryBugWin").xform('clear');
	queryParam = $("#bugQueryForm_").xserialize();
	$("#clearModuleBnt").hide();
}
//返回查询框
function closeBugQueryWin(){
	queryParam = $("#bugQueryForm_").xserialize();
	$("#queryBugWin").xwindow('close');
}

//测试需求
function findNeedsTree(textId,nameId,clearBnt){
	if(taskId=="" || taskId==undefined){
		$.xalert({title:'提示',msg:'请先选择项目！'});
		return;
	}
	var rootName="";
	$('#needsTree').xtree({
	    url:baseUrl+'/caseManager/caseManagerAction!loadTree.action?dto.taskId='+taskId+'&dto.command=',
	    method:'get',
		animate:true,
		lines:true,
//		dnd:true,
		onLoadSuccess:function(node,data){  
			if(data!=null){
				if(node==null){
					rootName = data[0].text;
				}
				$("#needsTreeWin").window({title:"选择需求  <span style='font-size:13px;'>(双击选择)<span>"});
				$("#needsTreeWin").window("vcenter");
				$("#needsTreeWin").xwindow('open');
			}else{
				$.xalert({title:'提示',msg:'当前项目没提交测试需求,不能填写软件问题报告'});
			}
		 }, 
		onClick: function(node){
//			currNodeId = node.id;
		},
		onDblClick:function(node){
			if(node!=null && !node.rootNode){
				$("#"+textId).val(node.id);
				$("#"+nameId).textbox("setValue", node.text);
				$("#needsTreeWin").xwindow('close');
				$("#"+clearBnt).show();
			}
		},
		onContextMenu:function(e,node){
			e.preventDefault();
			$(this).xtree('select', node.target);
		}
	});
}
//清除需求
function clearModule(){
	$("#moduleIdF").val("");
	$("#moduleNameF").textbox("setValue","");
	$("#clearModuleBnt").hide();
}



//打开选择用户
function openFindUserWin(id,text,personTitle){
	getFindGroupList();
	getFindUsers(id,text);
	$("#findChooseUserWin").window({title:personTitle});
	$("#findChooseUserWin").window("vcenter");
	$("#findChooseUserWin").window("open");
	$("#findUserAll").xdatagrid("resize");
}

//选择组 combobox组件
function getFindGroupList(){
	$("#findUserGroupList").xcombobox({
		url: baseUrl + '/userManager/userManagerAction!groupSel.action',
		valueField:'keyObj',
	    textField:'valueObj',
		onSelect: function(rec) {
			$("#findUserGroupId").val(rec.keyObj);
			findSearchUsers();
		}
	});
}


/* 候选人员列表*/
function getFindUsers(id,text){
	$("#findUserAll").xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!loadDefaultSelUserInAll.action',
		method: 'post',
		height: 410,
		cache: false,
		pagination: true,
		pageNumber: 1,
		pageSize: 20,
	    layout:['list','first','prev',"manual", 'sep','next','last','refresh','info'],
		pageList: [12,30,50,100],
		columns:[[
			{field:'keyObj',title:'id',hidden:true,checkbox:true,align:'center'},
			{field:'valueObj',title:'备选人员--双击选择',width:'100%',align:'center'}

		]],
		onDblClickRow: function (index, row) {
			 $("#"+id).textbox("setValue", row.keyObj);
			 $("#"+text).textbox("setValue", row.valueObj);
			 $("#findUserGroupId").val("");
			 $("#findChooseUserWin").xform("clear");
			 $("#findChooseUserWin").window("close");
		  }
	});
}

function findSearchUsers(){
	$.post(
		baseUrl + "/userManager/userManagerAction!selectUserInAll.action",
		{
			'dto.userName':$("#findChooseUserName").val(),
//			'dto.group.id' : $("#findUserGroupList").xcombobox("getValue"),
			'dto.group.id' : $("#findUserGroupId").val()
		},
		function(data,status,xhr){
			if(status=='success'){
				$("#findUserAll").xdatagrid("loadData",data);
			}else{
				$.xalert({title:'提示',msg:xhr.error()});
			}
		},
	"json");
}
function clearFindSearchUsers(){
	$("#findUserGroupId").val("");
	$("#findChooseUserWin").xform("clear");
	findSearchUsers();
}


//判断日期，时间大小  
function compareDate(startDate, endDate) {   
	if (startDate.length > 0 && endDate.length > 0) {   
	    var startDateTemp = startDate.split(" ");   
	    var endDateTemp = endDate.split(" ");   
	                   
	    var arrStartDate = startDateTemp[0].split("-");   
	    var arrEndDate = endDateTemp[0].split("-");   
	  
		var allStartDate = new Date(arrStartDate[0], arrStartDate[1], arrStartDate[2]);   
		var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2]);   
		if (allStartDate.getTime() > allEndDate.getTime()) {   
		        return false;   
		}else{   
		    return true;   
		}   
	 } else {   
	    return false;   
	}   
}

//# sourceURL=bugFind.js