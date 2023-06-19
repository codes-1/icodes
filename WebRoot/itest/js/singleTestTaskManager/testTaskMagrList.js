var objs = {
	$menuTreeSwitchBtn: $("#menu_tree_switch_btn"),
	$testTaskDg: $("#testTaskDg"),
	$addOrEditWinSin: $("#addOrEditWinSin"),
	$addOrEditForm: $("#addOrEditSinForm"),
	$userGroup: $("#userGroup"),
	$pm: $("#pm"),
	$uploadForm: $("#uploadForm"),
	$testFlowWin: $("#testFlowWin"),
	$workflowForm: $("#workflowForm"),
	$versionMaint:$("#versionMaintenance"),
	$newCreateVer:$("#newCreateVersion"),
	$selectPmWin:$("#selectPmWin"),
	$pMList:$("#pMList"),
	$editProStatus:$("#editProStatus")
};

$(function() {
	/*$.parser.parse();*/
	
//	objs.$menuTreeSwitchBtn.on('click', function() {
//		dgResize();
//		mainObjs.$treeArea.toggleClass("collapse");
//	});

	//测试项目管理的数据
	loadTestProject();
	//用户组信息
	userGroup();
	//获取当前用户的权限
	getLoginUserPower();
	
	$('#userGroupM').xcombobox({
		valueField:'keyObj',
	    textField:'valueObj',
	    data:$("#allData").data("dataIn"),
		onSelect: function(rec) {
			var url = baseUrl + '/userManager/userManagerAction!selectUser.action?dto.isAjax=true&dto.group.id='+rec.keyObj;
			objs.$pm.xcombobox('reload', url);
		}
	});
	
	//加载项目状态
	loadProjectStatus();
	
	getAll_elseTaskrela();
	document.onkeydown=function(event){
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if(e && e.keyCode==13){ // 按 Esc
			searchTestProject();
		}
	};
});

//获取当前用户的权限
function getLoginUserPower(){
	var controlButton = $('button[schkUrl]');
	$.each(controlButton,function(i,n){
		var controId = controlButton[i].id;
		var controlUrl = $(controlButton[i]).attr('schkUrl');
		if(privilegeMap[controlUrl]!="1"){
			//$("#"+controlID).removeClass("hide"); 
//			$("[schkurl='"+schkUrl+"']").removeClass("hide");
			//$(":button[schkUrl]").removeClass("hide");  
			$("#"+controId).hide(); 
		}
	});
	var controlA = $('a[schkUrl]');
	$.each(controlA,function(i,n){
		var controId = controlA[i].id;
		var controlUrl = $(controlA[i]).attr('schkUrl');
		if(privilegeMap[controlUrl]!="1"){
			//$("#"+controlID).removeClass("hide"); 
//			$("[schkurl='"+schkUrl+"']").removeClass("hide");
			//$(":button[schkUrl]").removeClass("hide");  
			$("#"+controId).hide(); 
		}
	});
}

//保存并设置流程
function saveAndProjectFlow(){
	submit();
	var saveFlowV = $("#saveFlow").val();
	if(saveFlowV!=""){
		showTestFlowWin(saveFlowV);
	}
}

//加载项目状态
function loadProjectStatus(){
	var option = '<option value="">-请选择-</option><option value="0">进行中</option><option value="1">完成</option><option value="2">准备</option><option value="5">暂停</option><option value="6">终止</option>';
	$("#projectStatus").html(option);
}

//用户组信息
function userGroup(){
	var url = baseUrl + '/userManager/userManagerAction!groupSel.action';
	$.ajax({
		  url: url,
		  cache: false,
		  async: false,
		  type: "POST",
		  dataType:"json",
		  success: function(data){
			  if(data!=null){
					$("#allData").data("dataIn",data);
					/*$('#userGroupM').xcombobox({
						valueField:'keyObj',
					    textField:'valueObj',
					    data:$("#allData").data("dataIn"),
						onSelect: function(rec) {
							var url = baseUrl + '/userManager/userManagerAction!selectUser.action?dto.isAjax=true&dto.group.id='+rec.keyObj;
							objs.$pm.xcombobox('reload', url);
						}
					});*/
				}
		   }
		});
}

//查询项目的信息
function searchTestProject(){
	objs.$testTaskDg.xdatagrid({
		url: baseUrl + '/singleTestTask/singleTestTaskAction!magrTaskListLoad.action',
		method: 'post',
		striped:true,
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
		queryParams:{
			'dto.singleTest.proName':$("#projectName").val(),
			'dto.singleTest.proNum':$("#projectNum").val(),
			'dto.singleTest.devDept':$("#department").val(),
			'dto.singleTest.status':$('#projectStatus').val()
		},
		columns:[[
		        {field:'filterFlag',title:'过滤标志',hidden:true},
		      	{field:'proNum',title:'项目编号',width:'15%',align:'left'},
				{field:'proName',title:'项目名称',width:'20%',align:'center',halign:'center',formatter:proNameFormat},
				{field:'devDept',title:'研发部门',width:'10%',align:'center',halign:'center'},
//				{field:'testPhase',title:'测试阶段',width:'8%',align:'center',formatter:testPhaseFormat},
				{field:'psmName',title:'项目PM',width:'15%',align:'center',halign:'center',formatter:psmManagerName},
				{field:'planStartDate',title:'开始日期',width:'10%',align:'center'},
				{field:'planEndDate',title:'结束日期',width:'10%',align:'center'},
//				{field:'planDocName',title:'测试计划',width:'8%',align:'center',formatter:planDocNameFormat},
				{field:'status',title:'状态',width:'8%',align:'center',formatter:statusFormat},
				{field:'testLdVos',title:'测试负责人',width:'14%',align:'left',formatter:psmNameFormat}
				/*{field:'reportTime',title:'报告日期',width:'9%',align:'center'}*/
		]],
		onLoadSuccess : function (data) {								
			/*if (data.total==0) {
				$('tr[id^="datagrid-row-r"]').parent().append('<label style="height: 40px;width: 900px; text-align: center;">没有数据!</label>');
			}*/
//			$("#testTaskDg").xdatagrid('resize');
		}
	});
}

function psmManagerName(value,row,index){
//	var reg = /[\u4e00-\u9fa5]/g;   //获取汉字
	var reg = "\\((.+?)\\)";;
	var pmName = row.psmName;
	var name = pmName.match(reg);
	if(pmName == ""){
		pmName="";
	}
	return name[1];
}


//测试项目管理的数据
function loadTestProject(){
	objs.$testTaskDg.xdatagrid({
		url: baseUrl + '/singleTestTask/singleTestTaskAction!magrTaskListLoad.action',
		method: 'get',
		rownumbers: false,
		height: mainObjs.tableHeight, 
		emptyMsg:"暂无数据",
		striped:true,
		columns:[[
		    {field:'filterFlag',title:'过滤标志',hidden:true},
			{field:'proNum',title:'项目编号',width:'15%',align:'left'},
			{field:'proName',title:'项目名称',width:'20%',align:'center',halign:'center',formatter:proNameFormat},
			{field:'devDept',title:'研发部门',width:'10%',align:'center',halign:'center'},
//			{field:'testPhase',title:'测试阶段',width:'8%',align:'center',formatter:testPhaseFormat},
			{field:'psmName',title:'项目PM',width:'15%',align:'center',halign:'center',formatter:psmManagerName},
			{field:'planStartDate',title:'开始日期',width:'10%',align:'center'},
			{field:'planEndDate',title:'结束日期',width:'10%',align:'center'},
//			{field:'planDocName',title:'测试计划',width:'8%',align:'center',formatter:planDocNameFormat},
			{field:'status',title:'状态',width:'8%',align:'center',formatter:statusFormat},
			{field:'testLdVos',title:'测试负责人',width:'14%',align:'left',formatter:psmNameFormat}
			/*{field:'reportTime',title:'报告日期',width:'9%',align:'center'}*/
		]],
		onLoadSuccess : function (data) {								
			/*if (data.total==0) {
				$('tr[id^="datagrid-row-r"]').parent().append('<label style="height: 40px;width: 900px; text-align: center;">没有数据!</label>');
			}*/
			$("#testTaskDg").xdatagrid('resize');
		}
	});
}

function proNameFormat(value,row,index) {
	var taskId = row.taskId;
	var taskType = row.taskType;
	return '<a id="'+taskId+'_'+taskType+'" href="javascript:;" title="设置(查看)测试流程--'+value+'" onclick="showTestFlowWin(this.id)">' + value + '</a>';
}

function testPhaseFormat(value,row,index) {
	switch (value) {
	case 0:
		return '单元测试';
	case 1:
		return '集成测试';
	case 2:
		return '系统测试';
	default:
		return '-';
	}
}

/*function psmNameFormat(value,row,index) {
	return '<a href="javascript:;" title="不纳入项目管理的独立测试项目--'+value+'">' + value + '</a>';
}*/

function planDocNameFormat(value,row,index) {
	if (value) {
		return '<a href="javascript:;">@</a>';
	}
}

function statusFormat(value,row,index) {
	switch (value) {
	case 0:
		return '进行中';
	case 1:
		return '完成';
	case 2:
		return '结束';
	case 3:
		return '准备';
	case 5:
		return '暂停';
	case 6:
		return '终止';
	default:
		return '-';
	}
}

function psmNameFormat(value,row,index) { 
	var html = new Array();
	if (row.testLdVos!=null&&row.testLdVos.length) {
		for (var i = 0; i < row.testLdVos.length; i++) {
			html.push(row.testLdVos[i].name);
		}
	}
	return html.join(',');
}

//获取所有全部的项目
function getAll_elseTaskrela(){
	$('.elseTaskPro').xcombobox({
			url: baseUrl + '/otherMission/otherMissionAction!getProjectLists2.action',
			valueField:'projectId',
		    textField:'projectName',
		    editable:false,
			onSelect: function(rec) {
				var projectId = rec.projectId;
				$("#projectId").val(projectId);
			},
			//loadFilter:function(data){
				/*var data1 = [];
				for(var i=0;i<data.length;i++){
					var projectType = data[i].projectType;
					if(projectType==1){
						data1.push(data[i]);
					}
				}
				return data1;*/
			//}
		});
}

//获取已有项目
//function selectNowPro(){
$("#selectNowPro").click(function(){
	$("#selectNowPro").parent().hide();
	$("#addNewPro").parent().show();
	//getAll_elseTaskrela();
	//$('.elseTaskPro').xcombobox("reload",baseUrl + '/otherMission/otherMissionAction!getProjectLists2.action');
});

$("#addNewPro").click(function(){
	$("#selectNowPro").parent().show();
	$("#addNewPro").parent().hide();
});
	

// 提交保存新增的记录
function submit() {
	var urls ="";
	var taskIds = $("#updateTaskId").data('taskI');
	var proAttrId = $('#addNewPro').parent().attr('style');
	var addOrSelectVal = "";
	var filterFlag ="";
	//检测必填数据
	var flag = requiredTestData(proAttrId);
	if(flag!=true){
		return;
	}
	
	if(proAttrId.indexOf("none") != -1){
		filterFlag="0";
		//$("#projectId").val("");
		addOrSelectVal = $('input[name="dto.singleTest.proName"][value!=""]').prev().val();
		//$('input[name="dto.singleTest.proName"][value!=""]').prev().val()
	}else{
		filterFlag="1";
		addOrSelectVal = $('#addNewPro').prev().find($('input[type!="hidden"]')).val();
	}
	
	var dataMap = {
			"dto.singleTest.taskId":taskIds,
			"dto.singleTest.filterFlag":filterFlag,
			"dto.singleTest.proName":addOrSelectVal,
			"dto.singleTest.psmId":$("#psmId").val(),
			"dto.singleTest.status":$(".editSta").xcombobox('getValue'),
			"dto.singleTest.taskProjectId":$.trim($("#projectId").val()),
			"dto.singleTest.proNum":$('input[name="dto.singleTest.proNum"]').prev().val(),
			"dto.singleTest.devDept":$('input[name="dto.singleTest.devDept"]').prev().val(),
			"dto.singleTest.planEndDate":$('input[name="dto.singleTest.planEndDate"]').prev().val(),
			"dto.singleTest.planStartDate":$('input[name="dto.singleTest.planStartDate"]').prev().val(),
			"dto.singleTest.psmName":$('input[name="dto.singleTest.psmName"]').prev().find($('input[id^="_exui_textbox_input"]')).val(),
	};
	
	if(taskIds!=""&&taskIds!=undefined){
		urls = baseUrl + "/singleTestTask/singleTestTaskAction!update.action";
	}else{
		urls = baseUrl + "/singleTestTask/singleTestTaskAction!add.action";
	}
	$.post(
			urls,
			dataMap,
		function(data) {
			if (data !=null) {
				objs.$addOrEditWinSin.xform('clear');
				objs.$addOrEditForm.xform('clear');
				objs.$addOrEditWinSin.xwindow('close');
//				$("#addOrEditWinSin input").val("");
//				loadTestProject();
				objs.$testTaskDg.xdatagrid('reload');
//				saveAndProjectFlow(data.taskId,data.taskType);
				$("#saveFlow").val(data.taskId+"_"+data.taskType);
			} else {
//				$.xnotify("系统错误！", {type:'warning'});
//				tip("系统错误！");
//				if(data =="existed"){
					objs.$addOrEditForm.xform('clear');
					objs.$addOrEditWinSin.xwindow('close');
					$.xalert({title:'提示',msg:'该项目名称已存在!'});
//				}
			}
		}, "json");
}

//检测必填数据
function requiredTestData(param){
	if($('.projectNums').xtextbox('getValue')==""){
		$.xalert({title:'提示',msg:'请填写项目编号!'});
		return false;
	}
	
	if(param!=""){
		if($('.hadProject').xtextbox('getValue')==""){
			$.xalert({title:'提示',msg:'请填写项目名称!'});
			return false;
		}
	}else{
		if($('.elseTaskPro').xcombobox('getValue')==""){
			$.xalert({title:'提示',msg:'请填写项目名称!'});
			return false;
		}
	}
	
	if($('.developmentDep').xtextbox('getValue')==""){
		$.xalert({title:'提示',msg:'请填写研发部门!'});
		return false;
	}
	
	if($('.proStName').val()==""){
		$.xalert({title:'提示',msg:'请选择PM!'});
		return false;
	}
	
	var planStartDate = $('.startTime').xdatebox('getValue');
	var planEndDate = $('.endTime').xdatebox('getValue');
	
	var startTime = planStartDate.replace(/-/g,"/");//替换字符，变成标准格式
	var endTime = planEndDate.replace(/-/g,"/");//替换字符，变成标准格式
	
	var time1 = new Date(Date.parse(startTime));  
	var time2 = new Date(Date.parse(endTime));
	
	if(time1 > time2){
		$.xalert({title:'提示',msg:'开始日期大于结束日期!'});
		return false;
	}
	
	if(planStartDate==""){
		$.xalert({title:'提示',msg:'请选择开始日期!'});
		return false;
	}
	if(planEndDate==""){
		$.xalert({title:'提示',msg:'请选择结束日期!'});
		return false;
	}
	
	return true;
}
// 打开新增或修改弹窗
function showAddWin() {
	$("#updateTaskId").data('taskI',"");
	$("#addNewPro").parent().hide();
	$("#addNewPro").show();
	$("#selectNowPro").parent().show();
	$("#selectNowPro").show();
	objs.$editProStatus.hide();
	objs.$addOrEditWinSin.xform('clear');
	objs.$addOrEditForm.xform('clear');
	objs.$addOrEditWinSin.parent().css("border","none");
	objs.$addOrEditWinSin.prev().css({ color: "#ffff", background: "#101010" });
	//获取相关人员
	relaUser();
	objs.$addOrEditWinSin.xwindow('setTitle','新建测试项目').xwindow('open');
	$('.elseTaskPro').xcombobox("reload",baseUrl + '/otherMission/otherMissionAction!getProjectLists2.action');
	$(".elseTaskPro").xcombobox("readonly",false);
	//清空数据
//	clearData();
//	addNewPro();
}

//获取相关人员
function relaUser(){	
	$.post(
			baseUrl +'/userManager/userManagerAction!loadDefaultSelUser.action?dto.getPageNo=1&dto.pageSize=1000',
			null,
			function(data) {
				if (data != null) {
					var opti = '<option value="">-请选择项目-</option>';
					if(data.rows.length > 0){
						for(var i=0;i<data.rows.length;i++){
							opti = opti + '<option value="'+data.rows[i].keyObj+'">'+data.rows[i].valueObj+'</option>';
						}
					}
					$(".searchable-select").remove();
					$("#pm").html(opti);
					$('#pm').searchableSelect();
				} else {
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}

//获取相关人员,修改处用
function relaUserEdit(){	
	$.post(
			baseUrl +'/userManager/userManagerAction!loadDefaultSelUser.action?dto.getPageNo=1&dto.pageSize=1000',
			null,
			function(data) {
				if (data != null) {
					var opti = '<option value="">-请选择项目-</option>';
					if(data.rows.length > 0){
						for(var i=0;i<data.rows.length;i++){
							opti = opti + '<option value="'+data.rows[i].keyObj+'">'+data.rows[i].valueObj+'</option>';
						}
					}
					$(".searchable-select").remove();
					$("#pm").html(opti);
				} else {
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}

function searchPm(){
	var keyId = $("#pm option:selected").val();
	var valueText = $("#pm option:selected").text();
	$("#psmId").val(keyId);
	$("#pmId").val(valueText);
}

// 打开修改弹窗
function showEditWin() {
	var row = objs.$testTaskDg.xdatagrid('getSelected');
	if (!row) {
//		$.xnotify('请选择要修改的一条记录', {type:'warning'});
//		tip("请选择要修改的一条记录!");
		$.xalert({title:'提示',msg:'请选择要修改的一条记录！'});
		return;
	}

	$("#updateTaskId").data('taskI',row.taskId);
	$.post(
			baseUrl + "/singleTestTask/singleTestTaskAction!updInit.action",
			{'dto.singleTest.taskId': row.taskId},
			function(data) {
				if (data !=null) {
					relaUserEdit();
					var psmId = data['dto.singleTest'].psmId;
					$("#psmId").val(psmId);
					$('#pm').val(psmId);
					$('#pm').searchableSelect();
					objs.$addOrEditWinSin.parent().css("border","none");
					objs.$addOrEditWinSin.prev().css({ color: "#ffff", background: "#101010" });
					var descArr = new Array();
					if(data['dto.singleTest'].status=="3"){
						descArr.push({desc:'准备',value:3});
					}else{
						descArr.push({desc:'进行中',value:0},{desc:'完成',value:1},{desc:'结束',value:2},{desc:'暂停',value:5},{desc:'终止',value:6});
					}
					
					$('.editSta').xcombobox({
						required:false,
		    			validateOnCreate:false,
		    			valueField:'value',
		    			editable:false,
		    			textField:'desc',
		    			prompt:'-请选择-',
		    			data:descArr
					});
					
					objs.$editProStatus.show();
					objs.$addOrEditWinSin.xdeserialize(data);
					
					if(data['dto.singleTest'].filterFlag=="1"){
						$(".elseTaskPro").xcombobox("readonly",true);
					}
					
					$(".editSta").xcombobox('setValue',data['dto.singleTest'].status);
					objs.$addOrEditWinSin.xwindow('setTitle','修改测试项目').xwindow('open');

					//修改新增项目隐藏，已有项目呈现下拉列表，判断
					showTextBoxOrCombobox(data['dto.singleTest'].filterFlag,data['dto.singleTest'].proName,data['dto.singleTest'].taskProjectId);//filterFlg
				} else {
//					$.xnotify("系统错误！", {type:'warning'});
//					tip("系统错误！");
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}

//回填数据
function fillData(datas,psmName){
	var proNum = datas["dto.singleTest"].proNum;
	var proName = datas["dto.singleTest"].proName;
	var devDept = datas["dto.singleTest"].devDept;
//	var psmName = datas["dto.singleTest"].psmName;
	var planEndDate = datas["dto.singleTest"].planEndDate;
	var planStartDate = datas["dto.singleTest"].planStartDate;
	
	if(proNum!=""&&proNum!=null){
		$('input[name="dto.singleTest.proNum"]').prev().val(proNum);
	}else{
		$('input[name="dto.singleTest.proNum"]').prev().val("暂无");
	}
	
	if(proName!=""&&proName!=null){
		$('input[name="dto.singleTest.proName"]').prev().find('input[type!="hidden"]').val(proName);
	}else{
		$('input[name="dto.singleTest.proName"]').prev().find('input[type!="hidden"]').val("暂无");
	}
	
	if(devDept!=""&&devDept!=null){
		$('input[name="dto.singleTest.devDept"]').prev().val(devDept);
	}else{
		$('input[name="dto.singleTest.devDept"]').prev().val("暂无");
	}
	
	if(psmName!=""&&psmName!=null){
		$('input[name="dto.singleTest.psmName"]').prev().find('input[type!="hidden"]').val(psmName);
	}else{
		$('input[name="dto.singleTest.psmName"]').prev().find('input[type!="hidden"]').val("暂无");
	}
	
	if(planEndDate!=""&&planEndDate!=null){
		$('input[name="dto.singleTest.planEndDate"]').prev().val(planEndDate.split(" ")[0]);
	}else{
		$('input[name="dto.singleTest.planEndDate"]').prev().val("暂无");
	}
	
	if(planStartDate!=""&&planStartDate!=null){
		$('input[name="dto.singleTest.planStartDate"]').prev().val(planStartDate.split(" ")[0]);
	}else{
		$('input[name="dto.singleTest.planStartDate"]').prev().val("暂无");
	}
}

//修改新增项目隐藏，已有项目呈现下拉列表，判断
function showTextBoxOrCombobox(flag,name,projectId){
	if(flag!=0){
		$("#selectNowPro").parent().hide();
		$("#addNewPro").parent().show();
		$("#addNewPro").hide();
		//getAll_elseTaskrela();
		$('.elseTaskPro').xcombobox("reload",baseUrl + '/otherMission/otherMissionAction!getProjectLists2.action?dto.projectId='+projectId);
		$('#addNewPro').prev().find('input[type!="hidden"]').val(name);
		$('#addNewPro').prev().find('input[type="hidden"]').val(projectId);
	}else{
		$("#addNewPro").parent().hide();
		$("#selectNowPro").parent().show();
		$("#selectNowPro").hide();
		$('input[name="dto.singleTest.proName"]').prev().val(name);
	}
}

//重置查询项目信息
function resetInfo(){
	$("#projectName").val("");
	$("#projectNum").val("");
	$("#department").val("");
	$("#projectStatus").val("");
}

// 打开删除确认弹窗
function showdelConfirm() {
	var row = objs.$testTaskDg.xdatagrid('getSelected');
	if (!row) {
//		$.xnotify('请选择要删除的一条记录', {type:'warning'});
//		tip("请选择要删除的一条记录!");
		$.xalert({title:'提示',msg:'请选择要删除的一条记录！'});
		return;
	}
	
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			$.post(
				baseUrl + "/singleTestTask/singleTestTaskAction!delete.action",
				{'dto.singleTest.taskId': row.taskId},
				function(data) {
					if (data == "success") {
						loadTestProject();
					} else {
//						$.xnotify(data, {type:'warning'});
//						tip(data);
						$.xalert({title:'提示',msg:data});
					}
				}, "text");
		}
		
	});
	
}

//设置测试流程
function setTestFlow(){
	var row = objs.$testTaskDg.xdatagrid('getSelected');
	if (!row) {
//		$.xnotify('请选择要设置的一条记录', {type:'warning'});
//		tip("请选择要设置的一条记录!");
		$.xalert({title:'提示',msg:'请选择要设置的一条记录!'});
		return;
	}
	
	var task_Id_type = row.taskId+"_"+row.taskType;
	
	showTestFlowWin(task_Id_type);
}

// 打开流程设置弹窗
function showTestFlowWin(task_Id_type) {
	$('a[href="#version"]').parent().removeAttr('class');
	$('a[href="#workflow"]').parent().addClass('active');
	$("#workflow").addClass('tab-pane fade active in');
	$("#version").addClass('tab-pane fade');
	var windowEle = objs.$testFlowWin.prev();
	
//	objs.$testFlowWin.parent().css('width','603px');
	windowEle.parent().css("border","none");
	windowEle.css({ color: "#ffff", background: "#101010" });
	
	var taskVal = task_Id_type.split("_");
	var tasid = taskVal[0];
	var taskType = taskVal[1];

	//获取流程设置人员数据
	getPersonData();
	
	//获取流程设置的数据
	getFlowData(tasid,taskType);
	
}

//获取流程设置人员数据
function getPersonData(){
	var url = baseUrl +'/userManager/userManagerAction!loadDefaultSelUser.action?dto.getPageNo=1&dto.pageSize=100';
	$.ajax({
		  url: url,
		  cache: false,
		  async: false,
		  type: "POST",
		  dataType:"json",
		  success: function(data){
			if(data!=null){
				//获取人员名单
				getUserInfo(data);
			}
		  }
		});
}

//获取人员名单
function getUserInfo(datas){
//	 $("#workflowForm table").find($('input[class="textbox-value"][name^="dto."]')).prev().xcombobox({
	$(".testLead").xcombobox({
		valueField:'keyObj',
	    textField:'valueObj',
	    prompt:'-请选择-',
		multiple:true,
		editable:false,
		data:datas.rows,
		hasDownArrow:true
//		onSelect: function(rec){
//			var MemId = rec.keyObj;
//			var testMem = rec.valueObj;
//			if(testMem != ""){
//				var id =$(this).next().find($('input[id^="_exui_textbox_input"]')).attr('id');
//				$(this).next().find($('input[id^="_exui_textbox_input"]')).css('color', '#aaa');
//				var dtoNames = $("#"+id).parent().next().attr("name");
//				//增加或者取消隐藏域的值
//			    addAndCancleHiddenData(dtoNames,MemId,"1");
////				$("#"+id).parents()[3].children['0'].children['0'].checked=true;//根据选择了人，勾上checkbox
////				$("#"+id).parents()[3].children['0'].children['1'].className ='fontC';
////				$("#"+id).parents('td').prev().find('span').css('color','rgb(16, 16, 16)');
//			}
////			$(this).xcombobox('hidePanel');
//		},
//		onUnselect:function(rec){
//			var MemId = rec.keyObj;
//			var ids = $(this).next().find($('input[id^="_exui_textbox_input"]')).attr('id');
//			var dtosName = $("#"+ids).parent().next().attr('name');
//			//增加或者取消隐藏域的值
//		    addAndCancleHiddenData(dtosName,MemId,"2");
//			var selength = $("#"+ids).nextAll().length-1;
//			if(selength == "0"){
////				$("#"+ids).parents()[3].children['0'].children['0'].checked=false;取消人员，跟checkbox没关系
////				$("#"+ids).parents()[3].children['0'].children['1'].className ='fontColor';
//				$("#"+ids).parents('td').prev().find('span').css('color','#636363');
//			}
//			$(this).xcombobox('hidePanel');
//		},
//		onShowPanel:function(){
//			var inID = $(this)['0'].id;
//			var dtoNames = "";
//			var text = "";
//			var name = "";
//			if(inID == ""){
//				inID =$(this).next().find($('input[id^="_exui_textbox_input"]')).attr('id');
//				dtoNames = $("#"+inID).parent().next().attr('name');
//				name = dtoNames.split('.')[1];
//				text = $("#"+inID).val();
//				if(text==""){
//					text = $("#testPersonI").data("dataIn")[name];
//				}
//			}else{
//				dtoNames = $("#"+inID).nextAll()['1'].name;
//				name = dtoNames.split('.')[1];
//				text = $('input[name="dto.+'+name+'"]').prev().find('input[id^="_exui_textbox_input"]').val();
//				if(text==undefined){
//					text = $("#"+inID).next('span').find($('input[id^="_exui_textbox_input"]')).val();
//					if(text==""){
//						text = $("#testPersonI").data("dataIn")[name];
//					}
//				}
//			}
//			if(text!=""/*&&$("#"+inID).val()!=""*/){
//				$('input[name="'+dtoNames+'"]').prev().find($('input[id^="_exui_textbox_input"]')).val(text);
//				$('input[name="'+dtoNames+'"]').prev().find($('input[id^="_exui_textbox_input"]')).css('color','#aaa');
//			}
//			
//			
//		}
	});
}

//增加或者取消隐藏域的值
function addAndCancleHiddenData(dto,id,flg){
	var dtoVal = dto.split(".")['1'];
	if(flg!="2"){
		if(dtoVal =="tester"){
			var testerId = $("#testerId").val();
			if(testerId.indexOf(id)<0){
				var ctesterId = testerId+" "+id;
				$("#testerId").val("");
				$("#testerId").val($.trim(ctesterId));
			}
		}else if(dtoVal == "testLead"){
			var testLeadId =$("#testLeadId").val();
			if(testLeadId.indexOf(id)<0){
				var ctesterLeadId = testLeadId+" "+id;
				$("#testLeadId").val("");
				$("#testLeadId").val($.trim(ctesterLeadId));
			}
		}else if(dtoVal == "caseReviewer"){
			var caseReviewId =$("#caseReviewId").val();
			if(caseReviewId.indexOf(id)<0){
				var cCaseReviewId = caseReviewId+" "+id;
				$("#caseReviewId").val("");
				$("#caseReviewId").val($.trim(cCaseReviewId));
			}
		}else if(dtoVal == "testerConf"){
			var testerConfId =$("#testerConfId").val();
			if(testerConfId.indexOf(id)<0){
				var ctesterConfId = testerConfId+" "+id;
				$("#testerConfId").val("");
				$("#testerConfId").val($.trim(ctesterConfId));
			}
		}else if(dtoVal == "analyser"){
			var analyserId =$("#analyserId").val();
			if(analyserId.indexOf(id)<0){
				var cAnalyserId = analyserId+" "+id;
				$("#analyserId").val("");
				$("#analyserId").val($.trim(cAnalyserId));
			}
		}else if(dtoVal == "assigner"){
			var assignationId =$("#assignationId").val();
			if(assignationId.indexOf(id)<0){
				var cAssignationId = assignationId+" "+id;
				$("#assignationId").val("");
				$("#assignationId").val($.trim(cAssignationId));
			}
		}else if(dtoVal == "programmer"){
			var programmerId =$("#programmerId").val();
			if(programmerId.indexOf(id)<0){
				var cProgrammerId = programmerId+" "+id;
				$("#programmerId").val("");
				$("#programmerId").val($.trim(cProgrammerId));
			}
		}else if(dtoVal == "empolderAffirmant"){
			var empolderAffirmantId =$("#empolderAffirmantId").val();
			if(empolderAffirmantId.indexOf(id)<0){
				var cEmpolderAffirmantId = empolderAffirmantId+" "+id;
				$("#empolderAffirmantId").val("");
				$("#empolderAffirmantId").val($.trim(cEmpolderAffirmantId));
			}
		}else if(dtoVal == "empolderLeader"){
			var empolderLeaderId =$("#empolderLeaderId").val();
			if(empolderLeaderId.indexOf(id)<0){
				var cEmpolderLeaderId = empolderLeaderId+" "+id;
				$("#empolderLeaderId").val("");
				$("#empolderLeaderId").val($.trim(cEmpolderLeaderId));
			}
		}else{
			var proRelaPersonId =$("#proRelaPersonId").val();
			if(proRelaPersonId.indexOf(id)<0){
				var cProRelaPersonId = proRelaPersonId+" "+id;
				$("#proRelaPersonId").val("");
				$("#proRelaPersonId").val($.trim(cProRelaPersonId));
			}
		}
	}else{
		if(dtoVal == "tester"){
			var testerId = $("#testerId").val();
			//截取不需要的字符串
			var ptVar = minusChar(testerId,id);
			$("#testerId").val("");
			$("#testerId").val(ptVar);
		}else if(dtoVal == "testLead"){
			var testLeadId =$("#testLeadId").val();
			//截取不需要的字符串
			var ptVar = minusChar(testLeadId,id);
			$("#testLeadId").val("");
			$("#testLeadId").val(ptVar);
		}else if(dtoVal == "caseReviewer"){
			var caseReviewId =$("#caseReviewId").val();
			//截取不需要的字符串
			var ptVar = minusChar(caseReviewId,id);
			$("#caseReviewId").val("");
			$("#caseReviewId").val(ptVar);
		}else if(dtoVal == "testerConf"){
			var testerConfId =$("#testerConfId").val();
			//截取不需要的字符串
			var ptVar = minusChar(testerConfId,id);
			$("#testerConfId").val("");
			$("#testerConfId").val(ptVar);
		}else if(dtoVal == "analyser"){
			var analyserId =$("#analyserId").val();
			//截取不需要的字符串
			var ptVar = minusChar(analyserId,id);
			$("#analyserId").val("");
			$("#analyserId").val(ptVar);
		}else if(dtoVal == "assigner"){
			var assignationId =$("#assignationId").val();
			//截取不需要的字符串
			var ptVar = minusChar(assignationId,id);
			$("#assignationId").val("");
			$("#assignationId").val(ptVar);
		}else if(dtoVal == "programmer"){
			var programmerId =$("#programmerId").val();
			//截取不需要的字符串
			var ptVar = minusChar(programmerId,id);
			$("#programmerId").val("");
			$("#programmerId").val(ptVar);
		}else if(dtoVal == "empolderAffirmant"){
			var empolderAffirmantId =$("#empolderAffirmantId").val();
			//截取不需要的字符串
			var ptVar = minusChar(empolderAffirmantId,id);
			$("#empolderAffirmantId").val("");
			$("#empolderAffirmantId").val(ptVar);
		}else if(dtoVal == "empolderLeader"){
			var empolderLeaderId =$("#empolderLeaderId").val();
			//截取不需要的字符串
			var ptVar = minusChar(empolderLeaderId,id);
			$("#empolderLeaderId").val("");
			$("#empolderLeaderId").val(ptVar);
		}else{
			var proRelaPersonId =$("#proRelaPersonId").val();
			//截取不需要的字符串
			var ptVar = minusChar(proRelaPersonId,id);
			$("#proRelaPersonId").val("");
			$("#proRelaPersonId").val(ptVar);
		}
	
	}
	
}

//截取不需要的字符串
function minusChar(testId,ids){
	var ptVar="";
	if(testId.indexOf(ids)>=0){
		var jtest = testId.split(" ");
		$.each(jtest,function(i,v){
			if(jtest[i]!=ids){
				ptVar = ptVar+jtest[i]+" ";
			}
		});
	}
	return $.trim(ptVar);
}

//执行拼接或者截取去掉
function addOrSplit(){
	
}

//获取流程设置的数据
function getFlowData(taId,taType){
	var url = baseUrl + "/testTaskManager/testTaskManagerAction!flwSetInit.action?dto.taskId=" + taId +"&dto.taskType="+taType+"&dto.comeFrom=flwSetList";
	$.ajax({
		  url: url,
		  cache: false,
		  async: false,
		  type: "POST",
//		  dataType:"json",
		  dataType:"text",
		  success: function(data){
			  var dataJson ="";
			  if(data=="deny"){
				  $.xalert({title:'提示',msg:'无查看测试流程的权限！'});
				  return;
			  }else{
				 dataJson =  JSON.parse(data);
				 if(dataJson != null){
					 setHiddenData(dataJson);
					 $("#testPersonI").data("dataIn",data);
					 if(dataJson.taskName != ""&& dataJson.taskName != null){
						 $("#taskName").text(dataJson.taskName);
					 }else{
						 $("#taskName").text('暂无');
					 }
					 
					 if(dataJson.testLead != ""&& dataJson.testLead != null){
						 var testLeadId = dataJson.testLeadId.replace(/ /g, ",");
						 $(".testLead").xcombobox("setValues",testLeadId.split(","));
					 }else{
						 $(".testLead").xcombobox("setValues",[]);
					 }
					 
					 var inputNum = $("#workflowForm table:eq(1)").find($("input[class='textbox-value'][name^='dto.']"));
					 var inputNums = $("#workflowForm table:eq(1)").find($("input[type='checkbox'][name^='dto.']"));
					 var inputMess = $("#workflowForm table:eq(2)").find($("input[type='checkbox'][name^='dto.detail']"));
					 $.each(inputNum, function(i, n){
						  var dtoNameS = inputNum[i].name;
						  var writeValue = dtoNameS.split(".")[1];
						  var dtoNameF = "";
						  var textWord = "";
						  if(inputNums[i]!=undefined){
							  dtoNameF = inputNums[i].name;
//							  textWord = $($("input[textboxname='"+dtoNameS+"']").parents()['1']).find($("input[name='"+dtoNameF+"']"));
							  textWord = $("input[textboxname='"+dtoNameS+"']").parent().prev().find('input');
						  }
							 //设置人员与id关联，选中
							 setPersonAndIdSel(writeValue,dataJson);
//						 	 //是否勾上checkbox
						 	 isOrNOCheckBox(textWord,dtoNameF);
						  
						 var lastInput = inputNums[inputNums.length-1].name;
						 var dtoSuffix = lastInput.split(".")[1];
						 var testerVerifyFix = dtoSuffix + "=true";
						 if(dataJson.testFlwKeyValueStr!=null&&i<1){
							 var valueFix = dataJson.testFlwKeyValueStr.split("^");
							 for(var a in valueFix){
								 if(valueFix[a] == testerVerifyFix){
									 $('input[name="'+lastInput+'"]').next().css("color","#101010");
									 var loadposi=$('input[name="'+lastInput+'"][type="checkbox"]');
									 if(loadposi.length!=1){
										 loadposi['1'].checked=true;
									 }else{
										 loadposi['0'].checked=true;
									 }
								 }
							 }
						 }
						 
						 if(i < inputMess.length){
							 var messageSet = inputMess[i].name;
							 var sub1 = messageSet.split(".")[1];
							 var sub2 = messageSet.split(".")[2];
	 						 if(dataJson[sub1][sub2] == 1){
	 							 var messL = $('input[name="'+messageSet+'"][type="checkbox"]');
	 							 if(messL.length!=1){
	 								messL['1'].checked=true;
	 							 }else{
	 								messL['0'].checked=true;
	 							 }
							 	 $('input[name="'+messageSet+'"]').next().css("color","#101010");
							 }
						 }
						 
					});
				 }
				 objs.$testFlowWin.xwindow('setTitle','设置测试流程').xwindow('open');
					$('#testBugPerson').prop("checked","checked");
					$('#fixBugPerson').prop("checked","checked");
					$('#arbPerson').prop("checked","checked");
					$('#testSure').prop("checked","checked");
					
					$("#testFlowWin").parent().css('width','630px');
//					$("#workflowForm table[id='secondTable']").find($("input[class='textbox-value'][name^='dto.']")).prev().css({ width: "337px"});
					$("#workflowForm table[id='secondTable']").find($("input[class='textbox-value'][name^='dto.']")).prev().find('input').css({ width: "337px",height: "34px",borderRadius:"5px",fontWeight:"normal"});
			  }
			  
			 
		  }
		});
}

//是否勾上checkbox
function isOrNOCheckBox(paramdt,dtos){
	if(dtos=="dto.reportBug"){
		//比对值，是否勾中
		checkProperties('1',paramdt,dtos);
    }else if(dtos=="dto.testerBugConfirm"){
    	checkProperties('2',paramdt,dtos);
    }else if(dtos=="dto.analyse"){
    	checkProperties('3',paramdt,dtos);
    }else if(dtos=="dto.assignation"){
    	checkProperties('4',paramdt,dtos);
    }else if(dtos=="dto.devFixBug"){
    	checkProperties('5',paramdt,dtos);
    }else if(dtos=="dto.devConfirmFix"){
    	checkProperties('6',paramdt,dtos);
    }else if(dtos=="dto.arbitrateBug"){
    	checkProperties('7',paramdt,dtos);
    }else{
    	checkProperties('11',paramdt,dtos);
    }
	
 }

////比对值，是否勾中
function checkProperties(num,params,dtoN){
	var testFlow = $("#testFlowStr").val();
	var flowSplit=testFlow.split(",");
		if(flowSplit.indexOf(num)>=0){
			$("input[name='"+dtoN+"'][type='checkbox']").prop("checked","checked");
			$("input[name='"+dtoN+"'][type='checkbox']").val(num);
		}else{
			$("input[name='"+dtoN+"'][type='checkbox']").prop("checked","");
			$("input[name='"+dtoN+"'][type='checkbox']").val("");
		}
}


//设置人员与id关联，选中
function setPersonAndIdSel(val,data){
	 switch(val)
		{
		case "caseReviewer":
			selPerson('caseReviewId',data,val);
		    break;
		case "analyser":
			selPerson('analyserId',data,val);
			break;
		case "assigner":
			selPerson('assignationId',data,val);
			break;
		case "empolderAffirmant":
			selPerson('empolderAffirmantId',data,val);
			break;
		case "empolderLeader":
			selPerson('empolderLeaderId',data,val);
			break;
		case "proRelaPerson":
			selPerson('proRelaPersonId',data,val);
			break;
		case "programmer":
			selPerson('programmerId',data,val);
			break;
		case "tester":
			selPerson('testerId',data,val);
			break;
		case "testerConf":
			selPerson('testerConfId',data,val);
			break;
		default:
			break;
		}
}
//$("input[name='"+dtoNameS+"']").prev().find("input").val(data[writeValue]);
//在获取人员id，追加数据，回填的人员与下拉人员选中
function selPerson(pId,dat,dtoV){
//	 var content ="";
//	 if(dat[pId]!=""){
//		 var wId = dat[pId].split(" ");
//		 for(var i=0;i<wId.length;i++){
//			 content += '<input type="hidden" class="textbox-value" name="" value="'+wId[i]+'">';
//		 }
//	 }
//	 $('input[name="dto.'+dtoV+'"]').prev().find("input").after(content);
	if(dat[pId]){
		var wId = dat[pId].replace(/ /g, ",");
		$("."+pId).xcombobox("setValues",wId.split(","));
	}else{
		$("."+pId).xcombobox("setValues",[]);
//		$("."+pId).css('width','337px');
	}
}

//选中加黑提示
$("input[type='checkbox'][name^='dto.']").click(function(){
	var InputDto = this.name;
	var InputCh = this.checked;
	if(InputCh){
		$('input[name="'+InputDto+'"]').attr('checked',InputCh);
		$('input[name="'+InputDto+'"]').next().css("color","#101010");
	}else{
		$('input[name="'+InputDto+'"]').next().css("color","#636363");
		$('input[name="'+InputDto+'"]').attr('checked',false);
	}
});

function submitTip(paramDto,className){
//	var inputVal = $('input[name="'+paramDto+'"]')
//	.parent()
//	.next()
//	.find($('input[type="hidden"]'))
//	.prev().find($('input[id^="_exui_textbox_input"]')).val();
	if(className!=""){
		var classVal = $("."+className).xcombobox("getValues")[0];
		var classNameLen = $("."+className).xcombobox("getValues").length;
		switch(paramDto)
		{
		case "dto.reportBug":
			if(classNameLen==0){
				$.xalert({title:'提示',msg:'请选择测试人员！'});
				return false;
			}else{
				return true;
			}
			
			break;
		case "dto.testerBugConfirm":
			if(classNameLen==0){
				$.xalert({title:'提示',msg:'请选择互验人员！'});
				 return false;
			}else{
				return true;
			}
			break;
		case "dto.analyse":
			if(classNameLen==0){
				$.xalert({title:'提示',msg:'请选择分析人！'});
				 return false;
			}else{
				return true;
			}
			break;
		case "dto.assignation":
			if(classNameLen==0){
				$.xalert({title:'提示',msg:'请选择分配人！'});
				 return false;
			}else{
				return true;
			}
			break;
		case "dto.devFixBug":
			if(classNameLen==0){
				$.xalert({title:'提示',msg:'请选择修改人！'});
				 return false;
			}else{
				return true;
			}
			break;
		case "dto.devConfirmFix":
			if(classNameLen==0){
				$.xalert({title:'提示',msg:'请选择互检人！'});
				 return false;
			}else{
				return true;
			}
			break;
		case "dto.arbitrateBug":
			if(classNameLen==0){
				$.xalert({title:'提示',msg:'请选择仲裁人！'});	
				 return false;
			}else{
				return true;
			}
			break;
		case "dto.proRelaPersonFlg":
			if(classNameLen==0){
				$.xalert({title:'提示',msg:'请选择关注人！'});
				 return false;
			}else{
				return true;
			}
			break;
		default:
			break;
		}
	}else{
		return true;
	}
//	case "dto.testerVerifyFix":
//		if(inputVal==undefined){
//			return true;
//		}
//		break;
//	}
}
//校验如果勾选了左边的checkbox，右边的input就必须有值
function selectCheckedToInput(){
	var SelInputCh = $("#workflowForm table").find($("input[type='checkbox'][name^='dto.']")).not($("input[type='checkbox'][name*='dto.detail']"));
	var flagTip=true;
	var dtoNames ="";
	for(var i=0;i<SelInputCh.length;i++){
		  var inputChecks = SelInputCh[i].checked;
		  dtoNames =  SelInputCh[i].name;
		  if(inputChecks){
			    if(dtoNames=="dto.reportBug"){
			    	flagTip=submitTip("dto.reportBug","testerId");
			    }else if(dtoNames=="dto.testerBugConfirm"){
			    	flagTip=submitTip("dto.testerBugConfirm","testerConfId");
			    }else if(dtoNames=="dto.analyse"){
			    	flagTip=submitTip("dto.analyse","analyserId");
			    }else if(dtoNames=="dto.assignation"){
			    	flagTip=submitTip("dto.assignation","assignationId");
			    }else if(dtoNames=="dto.devFixBug"){
			    	flagTip=submitTip("dto.devFixBug","programmerId");
			    }else if(dtoNames=="dto.devConfirmFix"){
			    	flagTip=submitTip("dto.devConfirmFix","empolderAffirmantId");
			    }else if(dtoNames=="dto.arbitrateBug"){
			    	flagTip=submitTip("dto.arbitrateBug","empolderLeaderId");
			    }else if(dtoNames=="dto.proRelaPersonFlg"){
			    	flagTip=submitTip("dto.proRelaPersonFlg","proRelaPersonId");
			    }else{
			    	flagTip=submitTip("dto.testerVerifyFix","");
			    }
			    if(!flagTip){
			    	flagTip = false;
			    	break;
			    }
		  }
	}
	return flagTip;
}

//提交修改信息
function submitInfo(){
	var testFlowStr=$("#testFlowStr").val();//修改之后拼接的
	var initFlowStr=$("#initFlowStr").val();//修改之前的返回来的
	
	//校验数据
	var flag = checkWriteData();
	if(typeof flag != "undefined" &&(!flag)==true){
		return;
	}
	
	//校验如果勾选了左边的checkbox，右边的input就必须有值
	var check = selectCheckedToInput();
	if(!check){
		return;
	}
	
	$('input[name="dto.reportBug"]').checked=true;
	$('input[name="dto.devFixBug"]').checked=true;
	$('input[name="dto.arbitrateBug"]').checked=true;
	$('input[name="dto.testerVerifyFix"]').checked=true;
	if($('input[name="dto.detail.mailOverdueBug"]').attr('checked')=='checked'&&$('input[name="dto.proRelaPersonFlg"]').attr('checked')==undefined){
//		tip("因没勾选项目关注，通知项目关注人设置无法生效!");
		$.xalert({title:'提示',msg:'因没勾选项目关注，通知项目关注人设置无法生效!'});
		return;
	}
	var inputCh = $("#workflowForm table").find($("input[type='checkbox'][name^='dto.']"));
	$.each( inputCh, function(i, n){
		  var inputCheck = inputCh[i].checked;
		  var dtoName =  inputCh[i].name;
		  if(inputCheck == true){
				switch(dtoName)
				{
				case "dto.caseReview":
					$('input[name="'+dtoName+'"]').val('9');
				    break;
				case "dto.reportBug":
					$('input[name="'+dtoName+'"]').val('1');
					break;
				case "dto.testerBugConfirm":
					$('input[name="'+dtoName+'"]').val('2');
					break;
				case "dto.analyse":
					$('input[name="'+dtoName+'"]').val('3');
					break;
				case "dto.assignation":
					$('input[name="'+dtoName+'"]').val('4');
					break;
				case "dto.devFixBug":
					$('input[name="'+dtoName+'"]').val('5');
					break;
				case "dto.devConfirmFix":
					$('input[name="'+dtoName+'"]').val('6');
					break;
				case "dto.arbitrateBug":
					$('input[name="'+dtoName+'"]').val('7');
					break;
				case "dto.proRelaPersonFlg":
					$('input[name="'+dtoName+'"]').val('11');
					break;
				case "dto.testerVerifyFix":
					$('input[name="'+dtoName+'"]').val('8');
					break;
				case "dto.detail.mailOverdueBug":
					$('input[name="'+dtoName+'"]').val('1');
					break;
				case "dto.detail.mailDevFix":
					$('input[name="'+dtoName+'"]').val('1');
					break;
				case "dto.detail.mailVerdict":
					$('input[name="'+dtoName+'"]').val('1');
					break;
				case "dto.detail.mailReport":
					$('input[name="'+dtoName+'"]').val('1');
					break;
				default:
					break;
				}
		  }else{
			  $('input[name="'+dtoName+'"]').val('');
		  }
	});
	
	var testFlowVal = $("#workflowForm table").find($("input[type='checkbox'][name^='dto.']")).not($("input[type='checkbox'][name*='dto.detail']"));
	var realV =[];
	$.each(testFlowVal,function(i,n){
		var tesVal = testFlowVal[i].value;
//		var arryFlowstr = testFlowStr.split(",");
//		if(arryFlowstr.indexOf(tesVal)<0){
//			realV = testFlowStr+","+tesVal;
//		}
		if(tesVal!=""){
//			realV = tesVal+","+realV;
			realV.push(parseInt(tesVal));
		}
	});
	
	realV=realV.sort(compare);
	
	$("#testFlowStr").val("");
//	$("#initFlowStr").val("");
	$("#testFlowStr").val(realV.toString());
//	$("#initFlowStr").val(realV);
//	var dtoCaseReviewer = $('input[name="dto.caseReviewer"]').prev().find($('input[id^="_exui_textbox_input"]')).val();//有问题
	
//	var dtoTestLead = $('input[name="dto.testLead"]').prev().find($('input[id^="_exui_textbox_input"]')).val();
//	var dtoTester = $('input[name="dto.tester"]').prev().find($('input[id^="_exui_textbox_input"]')).val();
//	var dtoTesterConf = $('input[name="dto.testerConf"]').prev().find($('input[id^="_exui_textbox_input"]')).val();
//	var dtoAnalyser = $('input[name="dto.analyser"]').prev().find($('input[id^="_exui_textbox_input"]')).val();
//	var dtoAssigner = $('input[name="dto.assigner"]').prev().find($('input[id^="_exui_textbox_input"]')).val();
//	
//	var dtoProgrammer = $('input[name="dto.programmer"]').prev().find($('input[id^="_exui_textbox_input"]')).val();
//	var dtoEmpolderAffirmant = $('input[name="dto.empolderAffirmant"]').prev().find($('input[id^="_exui_textbox_input"]')).val();
//	var dtoEmpolderLeader = $('input[name="dto.empolderLeader"]').prev().find($('input[id^="_exui_textbox_input"]')).val();
//	var dtoProRelaPerson = $('input[name="dto.proRelaPerson"]').prev().find($('input[id^="_exui_textbox_input"]')).val();
	
	var dtoTestLead = $('.test_Lead').xcombobox("getValues").toString().replace(/,/g, " ");
	var dtoTester = $('.testerId').xcombobox("getValues").toString().replace(/,/g, " ");
	var dtoTesterConf = $('.testerConfId').xcombobox("getValues").toString().replace(/,/g, " ");
	var dtoAnalyser = $('.analyserId').xcombobox("getValues").toString().replace(/,/g, " ");
	var dtoAssigner = $('.assignationId').xcombobox("getValues").toString().replace(/,/g, " ");
	
	var dtoProgrammer = $('.programmerId').xcombobox("getValues").toString().replace(/,/g, " ");
	var dtoEmpolderAffirmant = $('.empolderAffirmantId').xcombobox("getValues").toString().replace(/,/g, " ");
	var dtoEmpolderLeader = $('.empolderLeaderId').xcombobox("getValues").toString().replace(/,/g, " ");
	var dtoProRelaPerson = $('.proRelaPersonId').xcombobox("getValues").toString().replace(/,/g, " ");
	
	//下拉选择后，把页面隐藏域的值跟新
	$("#testLeadId").val(dtoTestLead);
	$("#testerId").val(dtoTester);
	$("#testerConfId").val(dtoTesterConf);
	$("#analyserId").val(dtoAnalyser);
	$("#assignationId").val(dtoAssigner);
	$("#programmerId").val(dtoProgrammer);
	$("#empolderAffirmantId").val(dtoEmpolderAffirmant);
	$("#empolderLeaderId").val(dtoEmpolderLeader);
	$("#proRelaPersonId").val(dtoProRelaPerson);
	
	//把value值放成"1112 5478"的形式
//	setValId();
	var taskType="";
	if(taskType==""){
		taskType = "0";
	}else{
		taskType = $("#taskType").val();
	}
	
	var dataMap = {
			"dto.testFlowStr":realV.toString(),
			"dto.initFlowStr":initFlowStr,
			"dto.detail.taskId":$("#taskIds").val(),
			"dto.taskId":$("#taskIds").val(),
			"dto.taskType":taskType,
			
			"dto.testerId":$("#testerId").val(),
	        "dto.testerConfId":$("#testerConfId").val(),
		    "dto.analyserId":$("#analyserId").val(),
		    "dto.assignationId":$("#assignationId").val(),
		    "dto.programmerId":$("#programmerId").val(),
		    "dto.empolderAffirmantId":$("#empolderAffirmantId").val(),
			"dto.empolderLeaderId":$("#empolderLeaderId").val(),
		    "dto.testLeadId":$("#testLeadId").val(),
		    "dto.caseReviewId":$("#caseReviewId").val(),
		    "dto.proRelaPersonId":$("#proRelaPersonId").val(),
		    "dto.detail.outlineState":$("#outlineState").val(),
		    "dto.detail.testTaskState":$("#testTaskState").val(),
		    
		    "dto.testLead":dtoTestLead,
//		    "dto.caseReviewer":dtoCaseReviewer,
		    "dto.tester":dtoTester,
		    "dto.testerConf":dtoTesterConf,
		    "dto.analyser":dtoAnalyser,
		    "dto.assigner":dtoAssigner,
		    "dto.programmer":dtoProgrammer,
		    "dto.empolderAffirmant":dtoEmpolderAffirmant,
		    "dto.empolderLeader":dtoEmpolderLeader,
		    "dto.proRelaPerson":dtoProRelaPerson,
		    
//		    "dto.caseReview":$('input[name="dto.caseReview"]').val(),
		    "dto.reportBug":$('input[name="dto.reportBug"]').val(),
		    "dto.testerBugConfirm":$('input[name="dto.testerBugConfirm"]').val(),
		    "dto.analyse":$('input[name="dto.analyse"]').val(),//on
		    "dto.assignation":$('input[name="dto.assignation"]').val(),//on
		    "dto.devFixBug":$('input[name="dto.devFixBug"]').val(),
		    "dto.devConfirmFix":$('input[name="dto.devConfirmFix"]').val(),
		    "dto.arbitrateBug":$('input[name="dto.arbitrateBug"]').val(),
		    "dto.proRelaPersonFlg":$('input[name="dto.proRelaPersonFlg"]').val(),
		    "dto.testerVerifyFix":$('input[name="dto.testerVerifyFix"]').val(),
		    "dto.detail.mailOverdueBug":$('input[name="dto.detail.mailOverdueBug"]').val(),
		    "dto.detail.mailDevFix":$('input[name="dto.detail.mailDevFix"]').val(),
		    "dto.detail.mailVerdict":$('input[name="dto.detail.mailVerdict"]').val(),
		    "dto.detail.mailReport":$('input[name="dto.detail.mailReport"]').val()
	};
	
	var url=baseUrl+"/testTaskManager/testTaskManagerAction!update.action";
	$.ajax({
		  url: url,
		  cache: false,
		  async: false,
		  type: "POST",
//		  dataType:"json",
		  dataType:"text",
		  data: dataMap,
		  success: function(data){
			  if(data == "success"){
				  objs.$testFlowWin.xwindow('close');
				  objs.$testFlowWin.xform('clear');
				  loadTestProject();
//				  document.location.reload();
			  }else{
//				  $.xnotify('系统问题！', {type:'warning'});
//				  tip("系统问题！");
				  $.xalert({title:'提示',msg:'系统问题!'});
			  }
		  }
		});
}

function compare(x, y) {//比较函数
    if (x < y) {
        return -1;
    } else if (x > y) {
        return 1;
    } else {
        return 0;
    }
}

//把value值放成"1112 5478"的形式
function setValId(){
	var manyDto = $("#workflowForm table").find($("input[class='textbox-value'][name^='dto.']"));
	$.each(manyDto,function(i,n){
		var dName = manyDto[i].name;
		if(dName == "dto.testLead"){
			//拼接id值
			var leadVal = spliceId(dName);
			$("#testLeadId").val(leadVal);
		}else if(dName == "dto.caseReviewer"){
			//拼接id值
			var caseVal = spliceId(dName);
			$("#caseReviewId").val(caseVal);
		}else if(dName == "dto.tester"){
			var testerVal = spliceId(dName);
			$("#testerId").val(testerVal);
		}else if(dName == "dto.testerConf"){
			var confVal = spliceId(dName);
			$("#testerConfId").val(confVal);
		}else if(dName == "dto.analyser"){
			var anayVal = spliceId(dName);
			$("#analyserId").val(anayVal);
		}else if(dName == "dto.assigner"){
			var assigVal = spliceId(dName);
			$("#assignationId").val(assigVal);
		}else if(dName == "dto.programmer"){
			var programVal = spliceId(dName);
			$("#programmerId").val(programVal);
		}else if(dName == "dto.empolderAffirmant"){
			var empolVal = spliceId(dName);
			$("#empolderAffirmantId").val(empolVal);
		}else if(dName == "dto.empolderLeader"){
			var empolderVal = spliceId(dName);
			$("#empolderLeaderId").val(empolderVal);
		}else if(dName == "dto.proRelaPerson"){
			var proRelaVal = spliceId(dName);
			$("#proRelaPersonId").val(proRelaVal);
		}
	});
}

//拼接id值
function spliceId(name){
	var valP =""; 
	var hiddenNum = $('input[name="'+name+'"]').prev().find($('input[type="hidden"][class="textbox-value"]'));
	if(hiddenNum.length>0){
		for(var x=0;x<hiddenNum.length;x++){
			if(x!=hiddenNum.length-1){
				valP = hiddenNum[x].value+" ";
			}else{
				valP+=hiddenNum[x].value;
			}
		}
	}
	return valP;
}


//校验数据
function checkWriteData(){
//	if($('input[name="dto.testLead"]').prev().find($('input[id^="_exui_textbox_input"]')).val()=="暂无"){
////		$.xnotify("请选择测试负责人", {type:'warning'});
////		tip('请选择测试负责人!');
//		$.xalert({title:'提示',msg:'请选择测试负责人!'});
//		return false;
//	}else if(/*$("#testerId").val()!=""&&*/$('input[name="dto.reportBug"]')[0].checked!=true&&$('input[name="dto.tester"]').prev()['0'].children['1'].value=="暂无"){
////		tip('请选择测试人员!');
//		$.xalert({title:'提示',msg:'请选择测试人员!'});
//		return false;
//	}else if(/*$("#programmerId").val()!=""&&*/$('input[name="dto.devFixBug"]')[0].checked!=true&&$('input[name="dto.programmer"]').prev()['0'].children['1'].value=="暂无"){
////		tip('请选择修改人!');
//		$.xalert({title:'提示',msg:'请选择修改人!'});
//		return false;
//	}/*else if($('input[name="dto.caseReview"]')[0].checked!=true&&$('input[name="dto.caseReviewer"]').prev()['0'].children['1'].value=="暂无"){
//		tip('请选择用例Review人员!');
//		return false;
//	}else if($('input[name="dto.testerBugConfirm"]')[0].checked!=true&&$('input[name="dto.testerConf"]').prev()['0'].children['1'].value=="暂无"){
//		tip('请选择互验人员!');
//		return false;
//	}else if($('input[name="dto.analyse"]')[0].checked!=true&&$('input[name="dto.analyser"]').prev()['0'].children['1'].value=="暂无"){
//		tip('请选择分析人!');
//		return false;
//	}else if($('input[name="dto.assignation"]')[0].checked!=true&&$('input[name="dto.assigner"]').prev()['0'].children['1'].value=="暂无"){
//		tip('请选择分配人！');
//		return false;
//	}else if($('input[name="dto.devConfirmFix"]')[0].checked!=true&&$('input[name="dto.empolderAffirmant"]').prev()['0'].children['1'].value=="暂无"){
//		tip('请选择互检人！');
//		return false;
//	}*/else if($('input[name="dto.arbitrateBug"]')[0].checked!=true&& $('input[name="dto.empolderLeader"]').prev()['0'].children['1'].value=="暂无"){
////		tip('请选择仲载人！');
//		$.xalert({title:'提示',msg:'请选择仲载人!'});
//		return false;
//	}/*else if($('input[name="dto.proRelaPersonFlg"]')[0].checked!=true&&$('input[name="dto.proRelaPerson"]').prev()['0'].children['1'].value=="暂无"){
//		tip('请选择关注人！');
//		return false;
//	}*/
//	return true;
	
	if($('.test_Lead').xcombobox("getValues").length==0){
		$.xalert({title:'提示',msg:'请选择测试负责人!'});
		return false;
	}else if($('.testerId').xcombobox("getValues").length==0){
		$.xalert({title:'提示',msg:'请选择测试人员!'});
		return false;
	}else if($('.programmerId').xcombobox("getValues").length==0){
		$.xalert({title:'提示',msg:'请选择修改人!'});
		return false;
	}else if($('.empolderLeaderId').xcombobox("getValues").length==0){
		$.xalert({title:'提示',msg:'请选择仲载人!'});
		return false;
	}
	return true;
}

function tip(info){
	$.xconfirm({
		msg:info,
		okFn: function() {
			
		}
	});
}
var flg = "";
//新建或者修改版本信息
function newCreateOrEditVer(flag){
	$("#newCreateVersion").parent().css("left","410px");
	$("#newCreateVersion").parent().css("border","none");
	$("#newCreateVersion").prev().css({ color: "#ffff", background: "#101010" });
	if(flag == 'edit'){
		saveOrEditVersion(flag);
	}else{
		objs.$newCreateVer.xwindow('setTitle','新建版本').xwindow('open');
	}
}

//保存版本信息
function saveOrEditVersion(fl){
	if(fl == "edit"){
		var row = objs.$versionMaint.xdatagrid('getSelected');
		if (!row) {
//			$.xnotify('请选择要修改的一条记录', {type:'warning'});
//			tip("请选择要修改的一条记录!");
			$.xalert({title:'提示',msg:'请选择要修改的一条记录!'});
			return;
		}
		updateVerInfo(row);//修改版本信息
	}else{
		//检验必填数据
		var testFlag = requireTestInfo();
		if(testFlag!=true){
			return;
		}
		saveverInfo();//保存版本信息
	}
}

//检验必填数据
function requireTestInfo(){
//	var data = objs.$newCreateVer.xdeserialize();
	if($('input[name="dto.softVer.versionNum"]').prev().val()==""){
//		tip("请填写名称！");
		$.xalert({title:'提示',msg:'请填写名称!'});
		return false;
	}
	
	if($('input[name="dto.softVer.seq"]').prev().val()==""){
//		tip("请填写序号！");
		$.xalert({title:'提示',msg:'请填写序号!'});
		return false;
	}
	return true;
}

//启用版本信息
function startVersion(){
	var row = objs.$versionMaint.xdatagrid('getSelected');
	if (!row) {
//		$.xnotify('请选择要修改的一条记录', {type:'warning'});
//		tip("请选择要启用的记录!");
		$.xalert({title:'提示',msg:'请选择要启用的记录!'});
		return;
	}
	
	$.xconfirm({
		msg:'您确定启用选择的记录吗?',
		okFn: function() {
			$.post(
				baseUrl+"/testTaskManager/testTaskManagerAction!softVerSwStatus.action",
				{"dto.softVer.versionId":row.versionId,"dto.softVer.taskid":row.taskid,"dto.softVer.verStatus":"0"},
				function(data) {
					if (data.indexOf("success") >= 0) {
						 loadVersion();
					} else {
//						$.xnotify(data, {type:'warning'});
//						tip(data);
						$.xalert({title:'提示',msg:data});
					}
				}, "text");
		}
		
	});
}

//停用版本信息
function stopVersion(){
	var row = objs.$versionMaint.xdatagrid('getSelected');
	if (!row) {
//		$.xnotify('请选择要修改的一条记录', {type:'warning'});
//		tip("请选择要停用的记录!");
		$.xalert({title:'提示',msg:'请选择要停用的记录!'});
		return;
	}
	
	$.xconfirm({
		msg:'您确定停用选择的记录吗?',
		okFn: function() {
			$.post(
				baseUrl+"/testTaskManager/testTaskManagerAction!softVerSwStatus.action",
				{"dto.softVer.versionId":row.versionId,"dto.softVer.taskid":row.taskid,"dto.softVer.verStatus":"1"},
				function(data) {
					if (data.indexOf("success") >= 0) {
						 loadVersion();
					} else {
//						$.xnotify(data, {type:'warning'});
//						tip(data);
						$.xalert({title:'提示',msg:data});
					}
				}, "text");
		}
		
	});
}

//删除版本信息
function deleteVer(){
	var row = objs.$versionMaint.xdatagrid('getSelected');
	if (!row) {
//		$.xnotify('请选择要修改的一条记录', {type:'warning'});
//		tip("请选择要删除的一条记录!");
		$.xalert({title:'提示',msg:'请选择要删除的一条记录!'});
		return;
	}
	
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			$.post(
				baseUrl+"/testTaskManager/testTaskManagerAction!delSoftVer.action",
				{"dto.softVer.versionId":row.versionId,"dto.softVer.taskid":row.taskid},
				function(data) {
					if (data.indexOf("success") >= 0) {
						 loadVersion();
					} else {
//						$.xnotify(data, {type:'warning'});
//						tip(data);
						$.xalert({title:'提示',msg:data});
					}
				}, "text");
		}
		
	});
}

function updateVerInfo(rows){
	var url = baseUrl+"/testTaskManager/testTaskManagerAction!udateSoftVer.action";
	$.ajax({
		  url: url,
		  cache: false,
		  async: false,
		  type: "POST",
		  dataType:"json",
//		  dataType:"text",
		  data: {"dto.softVer.versionId":rows.versionId,"dto.softVer.versionNum":rows.versionNum},
		  success: function(data){
			  if(data!=null){
				  objs.$newCreateVer.xdeserialize(data);
				  $("#taId").val(data["dto.softVer"].taskid);
				  objs.$newCreateVer.xwindow('setTitle','修改版本').xwindow('open');
			  }else{
//				  $.xnotify('系统问题！', {type:'warning'});
//				  tip("系统问题！");
				  $.xalert({title:'提示',msg:'系统问题!'});
			  }
		  }
		});
}

function saveverInfo(){
	var urls = baseUrl+"/testTaskManager/testTaskManagerAction!addSoftVer.action";
	$.ajax({
		  url: urls,
		  cache: false,
		  async: false,
		  type: "POST",
//		  dataType:"json",
		  dataType:"text",
		  data: objs.$newCreateVer.xserialize(),
		  success: function(data){
			  if(data.indexOf("success^") >= 0){
				  cancleVers();
				  loadVersion();
			  }else if(data.indexOf("seqRep") >= 0){
//				  tip("重复版本信息！");
				  $.xalert({title:'提示',msg:'重复版本信息!'});
			  }else{
//				  tip("系统问题！");
				  $.xalert({title:'提示',msg:'系统问题!'});
			  }
		  }
		});
}

//点击X清空信息
$("#newCreateVersion").prev().find('div.panel-tool a[class="panel-tool-close"]').click(function(){
	cancleVers();
});

//取消保存版本信息
function cancleVers(){
	objs.$newCreateVer.xform('clear');
	objs.$newCreateVer.xwindow('close');
}

//设置隐藏域的值
function setHiddenData(datas){
	 $("#testFlowStr").val(datas.testFlowStr);
	 $("#initFlowStr").val(datas.initFlowStr);
	 $("#taskType").val(datas.taskType);
	 $("#taskIds").val(datas.detail.taskId);
	 $("#testPhase").val(datas.detail.testPhase);
	 $("#relaTaskId").val(datas.detail.relaTaskId);
	 $("#outlineState").val(datas.detail.outlineState);
	 $("#taskState").val(datas.detail.taskState);
	 $("#customCase").val(datas.detail.customCase);
	 $("#customBug").val(datas.detail.customBug);
	 $("#testerId").val(datas.testerId);
	 $("#testerConfId").val(datas.testerConfId);
	 $("#analyserId").val(datas.analyserId);
	 $("#assignationId").val(datas.assignationId);
	 $("#programmerId").val(datas.programmerId);
	 $("#empolderAffirmantId").val(datas.empolderAffirmantId);
	 $("#empolderLeaderId").val(datas.empolderLeaderId);
	 $("#testLeadId").val(datas.testLeadId);
	 $("#testTaskState").val(datas.detail.testTaskState);
	 $("#projectId").val(datas.detail.projectId);
	 $("#caseReviewId").val(datas.caseReviewId);
	 $("#testSeq").val(datas.detail.testSeq);
	 $("#proRelaPersonId").val(datas.proRelaPersonId);
}

//重置测试流程信息
function resetting(){
//	$("#taskName").text("");
	$(".testPerson").css("color","#353535");
//	$('input[class="textbox-value"][name^="dto."]').prev().val(""); 
	objs.$workflowForm.xform('clear');
	$('input[type="checkbox"][name^="dto."]').removeAttr('checked');
	$('input[class="textbox-f"][textboxname^="dto."]').prev().css("color","#353535");
	$('input[type="checkbox"][name^="dto."]').next().css("color","#636363");
//	var inputNm = $('input[type="checkbox"][name^="dto."]');
//	$.each(inputNm,function(i,n){
//		inputNm[i].checked="";
//	});
}

$("#testFlowWin").prev().find($('a[class="panel-tool-close"]')).click(function(){
	returnWin();
});

//关闭弹窗
function returnWin(){
	objs.$testFlowWin.xwindow('close');
	objs.$testFlowWin.xform('clear');
	loadTestProject();
//	document.location.reload();
}

$('a[href="#version"]').click(function(){
	$("div#workflow").attr('class','tab-pane fade');
	$("div#version").attr('class','tab-pane fade in active');
	loadVersion();
});

$('a[href="#workflow"]').click(function(){
	$("#version").next().hide();
//	$("#version")["0"].attributes[1].nodeValue = "tab-pane fade";
//	$($("#testFlowWin").find("div#version")[0]).attr('class','tab-pane fade');
});

//加载版本维护信息
function loadVersion(){
	var taskid = $('#taskIds').val();
	var urls = baseUrl + '/testTaskManager/testTaskManagerAction!softVerListLoad.action?dto.taskId='+taskid;
	objs.$versionMaint.xdatagrid({
		url: urls,
		method: 'get',
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
	    columns:[[
	        {field:'taskid',hidden:true},
			{field:'seq',title:'序号',width:'100',align:'center'},
			{field:'versionNum',title:'版本名称',width:'90',align:'left',halign:'center'},
			{field:'versionId',title:'顺序号',width:'110',align:'center',halign:'center'},
			{field:'remark',title:'备注',width:'100',align:'left',halign:'center'},
			{field:'verStatus',title:'状态',width:'120',align:'center',halign:'center',formatter:softverStatus}
	    ]],
	    onClickRow:function(rowIndex, rowData){
	    	var verStatusR = rowData.verStatus;
	    	if(verStatusR == "0"){
	    		$('button[onclick="stopVersion()"]').css('display','');
	    		$('button[onclick="startVersion()"]').css('display','none');
	    	}else{
	    		$('button[onclick="stopVersion()"]').css('display','none');
	    		$('button[onclick="startVersion()"]').css('display','');
	    	}
	    },
	    onLoadSuccess : function (data) {
//	    	$("#versionMaintenance").xdatagrid('resize');
	    	/*if (data.total==0) {
	    		$("#versionMaintenance").prev().find('tr[id^="datagrid-row-r"]').empty();
	    		$("#versionMaintenance").prev().find('tr[id^="datagrid-row-r"]').append('<label style="height: 40px;width: 450px;margin-top: 10px;text-align: center;">暂无数据!</label>');
//				$("#datagrid-row-r2-2-0").parent().append('<label style="height: 40px;width: 450px; text-align: center;">暂无版本数据!</label>');
			}*/
		}
	});
}

function softverStatus(value,row,index){
	var startOrStop ="";
	var verStatus = row.verStatus;
	if(verStatus == "1"){
		startOrStop = "停用";
	}else if(verStatus == "0"){
		startOrStop = "启用";
	}
	return startOrStop;
}

//点击X清空信息,修改测试流程信息 
$("#addOrEditWinSin").prev().find('div.panel-tool a[class="panel-tool-close"]').click(function(){
//	addNewPro();
	closeWin();
//	objs.$addOrEditWinSin.xwindow('destroy');
});

// 取消提交并关闭弹窗
function closeWin() {
	$("#updateTaskId").data('taskI',"");
//	addNewPro();
//	clearData();
	objs.$addOrEditWinSin.xwindow('close');
//	$("#addOrEditWinSin input").val("");
//	objs.$addOrEditWinSin.xwindow('destroy');
//	objs.$addOrEditWinSin.xform('clear');
}

//图片移动效果
$(".selImg0").hover(function(){
	$(".selImg0").attr('src',''+baseUrl+'/itest/images/mSearch1.png');
},function(){
	$(".selImg0").attr('src',''+baseUrl+'/itest/images/mSearch.png');
});

$(".selImg1").hover(function(){
	$(".selImg1").attr('src',''+baseUrl+'/itest/images/mSearch1.png');
},function(){
	$(".selImg1").attr('src',''+baseUrl+'/itest/images/mSearch.png');
});

$(".selImg2").hover(function(){
	$(".selImg2").attr('src',''+baseUrl+'/itest/images/mSearch1.png');
},function(){
	$(".selImg2").attr('src',''+baseUrl+'/itest/images/mSearch.png');
});

$(".selImg3").hover(function(){
	$(".selImg3").attr('src',''+baseUrl+'/itest/images/mSearch1.png');
},function(){
	$(".selImg3").attr('src',''+baseUrl+'/itest/images/mSearch.png');
});

$(".selImg4").hover(function(){
	$(".selImg4").attr('src',''+baseUrl+'/itest/images/mSearch1.png');
},function(){
	$(".selImg4").attr('src',''+baseUrl+'/itest/images/mSearch.png');
});

$(".selImg5").hover(function(){
	$(".selImg5").attr('src',''+baseUrl+'/itest/images/mSearch1.png');
},function(){
	$(".selImg5").attr('src',''+baseUrl+'/itest/images/mSearch.png');
});

$(".selImg6").hover(function(){
	$(".selImg6").attr('src',''+baseUrl+'/itest/images/mSearch1.png');
},function(){
	$(".selImg6").attr('src',''+baseUrl+'/itest/images/mSearch.png');
});

$(".selImg7").hover(function(){
	$(".selImg7").attr('src',''+baseUrl+'/itest/images/mSearch1.png');
},function(){
	$(".selImg7").attr('src',''+baseUrl+'/itest/images/mSearch.png');
});

$(".selImg8").hover(function(){
	$(".selImg8").attr('src',''+baseUrl+'/itest/images/mSearch1.png');
},function(){
	$(".selImg8").attr('src',''+baseUrl+'/itest/images/mSearch.png');
});
//# sourceURL=testTaskMagrList.js