var objs = {
	$missionDg: $("#missionDg"),
	$addOrEditWin: $("#addOrEditWin"),
	$addOrEditForm: $("#addOrEditForm"),
};
//存放所有可执行任务的人员
var peopleList = [];
//存放被选中的参与者
var selectedPeople = [];
//存放被选中的关注者
var selectedConcerns = [];
//存放所有项目
var projectList = [];
//存放选中的项目
var proJ = "";
//标志位，判定是参与者打开的多人弹窗（0）还是关注者打开的多人弹窗（1）
var flagPt = "";
$(function() {
	$.parser.parse();
	//加载所属项目下拉菜单
	loadProjectList();
	//加载可执行任务员工列表和所有可关注任务者列表
	loadPeopleLists();
	//加载任务类别列表
	loadMissionCategory();
	//加载任务紧急程度列表
	loadEmergencyDegree();
	//加载任务难易程度列表
	loadDifficultyDegree();
	
	/*setTimeout(function(){ 
		//加载其他任务列表数据
		loadOtherMission();
	},1500);*/
	/*$("#peopleList").xcombobox({
		data:peopleList,
		onChange:function(newValue,oldValue){
			var chargePeopleList = [];
			if(newValue.length > 0 && peopleList.length > 0){
				selectedPeople = newValue;
				for(var i=0;i<newValue.length;i++){
					for(var j=0;j<peopleList.length;j++){
						if(newValue[i] == peopleList[j].value){
							chargePeopleList.push(peopleList[j]);
						}
					}
				}
				$("#inchargePeople").xcombobox({
					data:chargePeopleList
				});
			}
		}
	});*/
	$(".searchLogo").hover(function(){
		$(this).attr("src",baseUrl+"/itest/images/mSearch1.png");
	},function(){
		$(this).attr("src",baseUrl+"/itest/images/mSearch.png");
	});
});

//查询任务信息
function searchOtherMission(){
	objs.$missionDg.xdatagrid({
		url: baseUrl + '/otherMission/otherMissionAction!otherMissionListLoad.action',
		method: 'post',
		queryParams: {
			"dto.otherMission.missionName":$("#missionName").val(),
			"dto.otherMission.createUserId":$("#loginName").text(),
			"dto.otherMission.status":$("#statu").val(),
			"dto.otherMission.projectId":$("#projectNa").val()
		},
		height: mainObjs.tableHeight,
		emptyMsg:"无数据",
		columns:[[
		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'missionName',title:'任务名称',width:"10%",align:'center'},
			/*{field:'missionCategory',title:'任务类别',align:'center',halign:'center',formatter:missionCategoryFormat},
			{field:'missionType',title:'任务类型',align:'center',halign:'center',formatter:missionTypeFormat},*/
			{field:'projectId',title:'所属项目',width:"10%",align:'center',formatter:projectFormat},
			{field:'missionId',title:'任务参与者',width:"10%",align:'center',formatter:missionPersonFormat},
			{field:'chargePersonId',title:'任务负责人',width:"10%",align:'center',halign:'center',formatter:chargePersonFormat},
			/*{field:'emergencyDegree',title:'紧急程度',align:'center',formatter:emergencyDegreeFormat},
			{field:'difficultyDegree',title:'难易程度',align:'center',formatter:difficultyDegreeFormat},
			{field:'standardWorkload',title:'标准工作量',align:'center'},*/
			{field:'actualWorkload',title:'实际工作量(小时)',width:"12%",align:'center'},
			{field:'description',title:'任务描述',width:"10%",align:'center',formatter:descriptionFormat},
			{field:'completionDegree',title:'任务进度(%)',width:"10%",align:'center'},
			{field:'status',title:'状态',width:"8%",align:'center',formatter:statusFormat},
			//{field:'createUserId',title:'发起人',align:'center'},
			{field:'predictStartTime',title:'预计开始时间',width:"10%",align:'center',formatter:predictStartTimeFormat},
			{field:'predictEndTime',title:'预计完成时间',width:"9.8%",align:'center',formatter:predictEndTimeFormat},
			/*{field:'createTime',title:'创建时间',align:'center'}*/
		]],
		onLoadSuccess : function (data) {								
			/*if (data.total==0) {
				$('tr[id^="datagrid-row-r"]').parent().append('<label style="height: 40px;width: 900px; text-align: center;">没有数据!</label>');
			}*/
		}
	});
}



//其他任务数据
function loadOtherMission(){
	objs.$missionDg.xdatagrid({
		url: baseUrl + '/otherMission/otherMissionAction!otherMissionListLoad.action',
		method: 'post',
		queryParams: {
			"dto.otherMission.createUserId":$("#loginName").text()
		},
		height: mainObjs.tableHeight,
		emptyMsg:"无数据",
		columns:[[
		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'missionName',title:'任务名称',width:"10%",align:'center'},
			/*{field:'missionCategory',title:'任务类别',align:'center',halign:'center',formatter:missionCategoryFormat},
			{field:'missionType',title:'任务类型',align:'center',halign:'center',formatter:missionTypeFormat},*/
			{field:'projectId',title:'所属项目',width:"10%",align:'center',formatter:projectFormat},
			{field:'missionId',title:'任务参与者',width:"10%",align:'center',formatter:missionPersonFormat},
			{field:'chargePersonId',title:'任务负责人',width:"10%",align:'center',halign:'center',formatter:chargePersonFormat},
			/*{field:'emergencyDegree',title:'紧急程度',align:'center',formatter:emergencyDegreeFormat},
			{field:'difficultyDegree',title:'难易程度',align:'center',formatter:difficultyDegreeFormat},
			{field:'standardWorkload',title:'标准工作量',align:'center'},*/
			{field:'actualWorkload',title:'实际工作量(小时)',width:"12%",align:'center'},
			{field:'description',title:'任务描述',width:"10%",align:'center',formatter:descriptionFormat},
			{field:'completionDegree',title:'任务进度(%)',width:"10%",align:'center'},
			{field:'status',title:'状态',width:"8%",align:'center',formatter:statusFormat},
			//{field:'createUserId',title:'发起人',align:'center'},
			{field:'predictStartTime',title:'预计开始时间',width:"10%",align:'center',formatter:predictStartTimeFormat},
			{field:'predictEndTime',title:'预计完成时间',width:"9.8%",align:'center',formatter:predictEndTimeFormat},
			/*{field:'createTime',title:'创建时间',align:'center'}*/
		]],
		onLoadSuccess : function (data) {								
			/*if (data.total==0) {
				$('tr[id^="datagrid-row-r"]').parent().append('<label style="height: 40px;width: 900px; text-align: center;">没有数据!</label>');
			}*/
		}
	});
}
/*function missionCategoryFormat(value,row,index){
	if(value == "0"){
		return "其他任务";
	}else{
		return "测试任务";
	}
}*/
/*function missionTypeFormat(value,row,index){
	if(value == "0"){
		return "其他任务";
	}else{
		return "测试任务";
	}
}*/
function missionPersonFormat(value,row,index){
	var userNames = "";
	$.ajax({
	  url: baseUrl + "/otherMission/otherMissionAction!getUserNames.action",
	  cache: false,
	  async: false,
	  type: "POST",
	  data: {
		  'dto.otherMission.missionId':row.missionId
	  },
	  dataType:"text",
	  success: function(data){
		  if (data !=null) {
			  userNames = data;
			} else {
				/*$.xnotify("系统错误！", {type:'warning'});*/
				$.xalert({title:'提示',msg:'系统错误！'});
			}
	   }
	});
	return "<span title='"+userNames+"'>"+userNames.substring(0,5)+"....</span>";
}
/*function emergencyDegreeFormat(value,row,index){
	if(value == "0"){
		return "一般";
	}else{
		return "紧急";
	}
}*/
/*function difficultyDegreeFormat(value,row,index){
	if(value == "0"){
		return "一般";
	}else{
		return "较难";
	}
}*/
function predictStartTimeFormat(value,row,index){
	if(value){
		return value.substring(0,10);
	}
	return "";
}
function predictEndTimeFormat(value,row,index){
	if(value){
		return value.substring(0,10);
	}
	return "";
}
function statusFormat(value,row,index){
	if(value == "0"){
		return "分配";
	}else if(value == "1"){
		return "进行中";
	}else if(value == "2"){
		return "完成";
	}else if(value == "3"){
		return "终止";
	}else if(value == "4"){
		return "暂停";
	}
	return "";
}
function projectFormat(value,row,index){
	if(value){
		for(var i=0;i<projectList.length;i++){
			if(value == projectList[i].projectId){
				return projectList[i].projectName;
			}
		}
	}
	return "暂无";
}

function chargePersonFormat(value,row,index){
	for(var i=0;i<peopleList.length;i++){
		if(value == peopleList[i].id){
			return peopleList[i].name;
		}
	}
	return "暂无";
}
function descriptionFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,5)+"....</span>";
}
// 提交保存新增或修改的记录
function submit() {
	//获取表单数据
	var objData = objs.$addOrEditWin.xserialize();
	//新增时，将状态设为0
	if(!objData["dto.otherMission.status"]){
		objData["dto.otherMission.status"] = "0";
	}
	//确定projectType，传到后台
	if(proJ){
		for(var i=0;i<projectList.length;i++){
			if(proJ == projectList[i].projectId){
				objData["dto.otherMission.projectType"] = projectList[i].projectType;
				break;
			}
		}
	}
	var saveOrUpdateUrl = "";
	if(objData["dto.otherMission.missionId"]){
		saveOrUpdateUrl = baseUrl + "/otherMission/otherMissionAction!update.action";
	}else{
		saveOrUpdateUrl = baseUrl + "/otherMission/otherMissionAction!add.action";
		objData["dto.otherMission.createUserId"] = $("#loginName").text();
	}
	
	//将选中的项目执行人拼接到表单数据，传到后台
	if(selectedPeople.length > 0){
		objData["dto.userIds"] = selectedPeople.toString();
	}
	//将选中的任务关注者拼接到表单数据，传到后台
	if(selectedConcerns.length > 0){
		objData["dto.concernIds"] = selectedConcerns.toString();
	}
	
//	for(var i=0;i<selectedPeople.length;i++){
//		objData["dto.userOtherMissions["+i+"].userId"] = selectedPeople[i];
//	}
	if(!objData["dto.otherMission.missionName"] || !objData["dto.otherMission.description"] || !objData["dto.otherMission.chargePersonId"] || selectedPeople.length == 0){
		$.xalert({title:'提交失败',msg:'请填写完整所有必填信息！'});
		return;
	}
	var t1 = /^\d+$/;
	if(objData["dto.otherMission.standardWorkload"] && !t1.test(objData["dto.otherMission.standardWorkload"])){
		$.xalert({title:'提交失败',msg:'请正确填写完整所有必填项！'});
		return;
	}
	//判断预计开始时间和预计结束时间前后关系
	if(objData["dto.otherMission.predictStartTime"] && objData["dto.otherMission.predictEndTime"]){
		if(objData["dto.otherMission.predictStartTime"] > objData["dto.otherMission.predictEndTime"]){
			$.xalert({title:'提交失败',msg:'预计结束时间必须在预计开始时间之后！'});
			return;
		}
	}
	$.post(
		saveOrUpdateUrl,
		objData,
		function(data) {
			if (data =="success") {
				objs.$addOrEditWin.xform('clear');
				objs.$addOrEditWin.xwindow('close');
				loadOtherMission();
				loadProjectList();
				$.xalert({title:'提示',msg:'操作成功！'});
			} else if(data =="existed"){
				$.xalert({title:'提交失败',msg:'该任务名称已存在，请勿重复添加！'});
			}else {
				$.xalert({title:'提交失败',msg:'系统错误！'});
			}
		}, "text");
		/*$.ajax({
		  url: saveOrUpdateUrl,
		  cache: false,
		  async: false,
		  type: "POST",
		  data: objData,
		  dataType:"text",
		  success: function(data){
			  if (data =="success") {
					objs.$addOrEditWin.xform('clear');
					objs.$addOrEditWin.xwindow('close');
					loadOtherMission();
				} else {
					$.xnotify("系统错误！", {type:'warning'});
				}
		   }
		});*/
}

// 打开新增弹窗
function showAddWin() {
	$(".inchargePeople").xcombobox({
		data:[]
	});
	objs.$addOrEditForm.xform('clear');
	$("#addOrEditForm input").val("");
	objs.$addOrEditWin.parent().css("border","none");
	objs.$addOrEditWin.prev().css({ color: "#ffff", background: "#101010" });
	$(".missionNa").next().children("input").css({"width":"475px"});
	$(".missionDe").next().children("input").css({"width":"475px","height":"74px"});
	objs.$addOrEditWin.xwindow('setTitle','创建任务').xwindow('open');
	loadProjectsAddMission();
}


// 打开修改弹窗
function showEditWin() {
	loadProjectsAddMissionEdit();
	var row = objs.$missionDg.xdatagrid('getSelected');
	if (!row) {
		/*$.xnotify('请选择要修改的一条记录', {type:'warning'});*/
		$.xalert({title:'提示',msg:'请选择要修改的一条记录！'});
		return;
	}
	objs.$addOrEditForm.xform('clear');
	$("#addOrEditForm input").val("");
	var fillData = {};
	fillData["dto.otherMission"] = row;
	$.post(
			baseUrl + "/otherMission/otherMissionAction!getUsers.action",
			{'dto.otherMission.missionId': row.missionId},
			function(data) {
				if (data !=null) {
					//回填表单数据
					objs.$addOrEditWin.xdeserialize(fillData);
					$('.projectIds').val(row.projectId);
					$('.projectIds').searchableSelect();
					//回填选中的执行人员和负责人
					$(".peopleList").xcombobox("setValues",data.split(","));
					//加载负责人下拉菜单
					var chargePeopleLists = [];
					if(data.split(",").length > 0 && peopleList.length > 0){
						for(var i=0;i<data.split(",").length;i++){
							for(var j=0;j<peopleList.length;j++){
								if(data.split(",")[i] == peopleList[j].id){
									chargePeopleLists.push(peopleList[j]);
								}
							}
						}
						$(".inchargePeople").xcombobox({
							data:chargePeopleLists
						}).xcombobox("setValue",row.chargePersonId);
					}
					//回填任务关注者
					$.post(baseUrl + "/otherMission/otherMissionAction!getConcerns.action",
					{'dto.otherMission.missionId': row.missionId},
					function(da) {
						if (da !=null && da !="failed") {
							//回填任务关注者
							$(".concernList").xcombobox("setValues",da.split(","));
						}else {
							/*$.xnotify("系统错误！", {type:'warning'});*/
							$.xalert({title:'提示',msg:'系统错误！'});
						}
					},'text');
					objs.$addOrEditWin.parent().css("border","none");
					objs.$addOrEditWin.prev().css({ color: "#ffff", background: "#101010" });
					$(".missionNa").next().children("input").css({"width":"475px"});
					$(".missionDe").next().children("input").css({"width":"475px","height":"74px"});
					objs.$addOrEditWin.xwindow('setTitle','修改任务').xwindow('open');
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "text");
}


// 打开删除确认弹窗
function showdelConfirm() {
	var row = objs.$missionDg.xdatagrid('getSelected');
	if (!row) {
		/*$.xnotify('请选择要删除的一条记录', {type:'warning'});*/
		$.xalert({title:'提示',msg:'请选择要删除的一条记录！'});
		return;
	}
	if(row.status != "0"){
		/*$.xnotify('只能删除状态为分配的任务！', {type:'warning'});*/
		$.xalert({title:'提示',msg:'只能删除状态为分配的任务！'});
		return;
	}
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			$.post(
				baseUrl + "/otherMission/otherMissionAction!delete.action",
				{'dto.otherMission.missionId': row.missionId},
				function(data) {
					if (data == "success") {
						loadOtherMission();
					} else {
						/*$.xnotify(data, {type:'warning'});*/
						$.xalert({title:'提示',msg:data});
					}
				}, "text");
		}
		
	});
	
}
//取消提交并关闭弹窗
function closeWin() {
	objs.$addOrEditWin.xwindow('close');
}
//新增项目
function addProject(){
	$("#addProjectForm").xform('clear');
	$("#addProject").parent().css("border","none");
	$("#addProject").prev().css({ color: "#ffff", background: "#101010" });
	$("#addProject").xwindow('setTitle','新增项目').xwindow('open');
}
//提交新增项目
function submitProject(){
	if(!$("#addProject").xserialize()["dto.project.projectName"]){
		$.xalert({title:'提交失败',msg:'请填写项目名称！'});
		return;
	}
	var objD = $("#addProject").xserialize();
	objD["dto.project.projectType"] = "1";
	objD["dto.project.createId"] = $("#accountId").text();
	$.post(
		baseUrl + "/otherMission/otherMissionAction!addProject.action",
		objD,
		function(data) {
			if (data =="success") {
				$("#addProjectForm").xform('clear');
				$("#addProject").xwindow('close');
				//加载所属项目下拉菜单
				loadProjectsAddMission();
				//加载输入框搜索项目下拉菜单
				loadProjectList();
				$.xalert({title:'提示',msg:'操作成功！'});
			} else {
				//$.xnotify("系统错误！", {type:'warning'});
				$.xalert({title:'提交失败',msg:'该项目已存在！'});
			}
		}, "text");
}
// 取消提交并关闭新增项目弹窗
function closeProjectWin(){
	$("#addProject").xwindow('close');
}
//加载所属项目下拉菜单
function loadProjectList(){
	$.post(
		baseUrl + "/otherMission/otherMissionAction!getProjectLists.action",
		null,
		function(dat) {
			if (dat != null) {
				projectList = dat.rows;
				/*$(".projectIds").xcombobox({
					data:dat.rows,
					onChange:function(newValue,oldValue){
						proJ = newValue;
					}
				});*/
				//加载下拉菜单选项(为管理员时)
				if($("#isAdmin").text() == "2" || $("#isAdmin").text() == "1"){
					var opti = '<option value="">-请选择项目-</option>';
					if(dat.rows.length > 0){
						for(var i=0;i<dat.rows.length;i++){
							opti = opti + '<option value="'+dat.rows[i].projectId+'">'+dat.rows[i].projectName+'</option>';
						}
					}
					$("#projectNa").next("div.searchable-select").remove();
					$("#projectNa").html(opti);
					$('#projectNa').searchableSelect();
				}else{
					//加载下拉菜单选项(非管理员时)
					var opti = '<option value="">-请选择项目-</option>';
					$.post(baseUrl + "/otherMission/otherMissionAction!getProjectListsRelated.action?dto.related=create",null,function(datt) {
						if(datt.rows.length > 0){
							for(var p=0;p<datt.rows.length;p++){
								opti = opti + '<option value="'+datt.rows[p].projectId+'">'+datt.rows[p].projectName+'</option>';
							}
						}
						$("#projectNa").next("div.searchable-select").remove();
						$("#projectNa").html(opti);
						$('#projectNa').searchableSelect();
					},'json');
				}
			} else {
				/*$.xnotify("系统错误！", {type:'warning'});*/
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		}, "json");
}
//加载新增任务时项目下拉菜单
function loadProjectsAddMission(){
	$.post(
			baseUrl + "/otherMission/otherMissionAction!getProjectLists.action",
			null,
			function(dat) {
				if (dat != null) {
					projectList = dat.rows;
					//加载下拉菜单选项(为管理员时)
					if($("#isAdmin").text() == "2" || $("#isAdmin").text() == "1"){
						var opti = '<option value="">-请选择项目-</option>';
						if(dat.rows.length > 0){
							for(var i=0;i<dat.rows.length;i++){
								opti = opti + '<option value="'+dat.rows[i].projectId+'">'+dat.rows[i].projectName+'</option>';
							}
						}
						$(".projectIds").next("div.searchable-select").remove();
						$(".projectIds").html(opti);
						$('.projectIds').searchableSelect();
					}else{
						//加载下拉菜单选项(非管理员时)
						var opti = '<option value="">-请选择项目-</option>';
						$.post(baseUrl + "/otherMission/otherMissionAction!getProjectListsRelated.action?dto.related=create",null,function(datt) {
							if(datt.rows.length > 0){
								for(var p=0;p<datt.rows.length;p++){
									opti = opti + '<option value="'+datt.rows[p].projectId+'">'+datt.rows[p].projectName+'</option>';
								}
							}
							$(".projectIds").next("div.searchable-select").remove();
							$(".projectIds").html(opti);
							$('.projectIds').searchableSelect();
						},'json');
					}
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}
//加载修改任务时项目下拉菜单
function loadProjectsAddMissionEdit(){
	$.post(
			baseUrl + "/otherMission/otherMissionAction!getProjectLists.action",
			null,
			function(dat) {
				if (dat != null) {
					projectList = dat.rows;
					//加载下拉菜单选项(为管理员时)
					if($("#isAdmin").text() == "2" || $("#isAdmin").text() == "1"){
						var opti = '<option value="">-请选择项目-</option>';
						if(dat.rows.length > 0){
							for(var i=0;i<dat.rows.length;i++){
								opti = opti + '<option value="'+dat.rows[i].projectId+'">'+dat.rows[i].projectName+'</option>';
							}
						}
						$(".projectIds").next("div.searchable-select").remove();
						$(".projectIds").html(opti);
						//$('.projectIds').searchableSelect();
					}else{
						//加载下拉菜单选项(非管理员时)
						var opti = '<option value="">-请选择项目-</option>';
						$.post(baseUrl + "/otherMission/otherMissionAction!getProjectListsRelated.action?dto.related=create",null,function(datt) {
							if(datt.rows.length > 0){
								for(var p=0;p<datt.rows.length;p++){
									opti = opti + '<option value="'+datt.rows[p].projectId+'">'+datt.rows[p].projectName+'</option>';
								}
							}
							$(".projectIds").next("div.searchable-select").remove();
							$(".projectIds").html(opti);
							//$('.projectIds').searchableSelect();
						},'json');
					}
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}
//加载可执行任务员工列表
function loadPeopleLists(){
	$.post(
			baseUrl + "/otherMission/otherMissionAction!getPeopleLists.action",
			null,
			function(dat) {
				if (dat != null) {
					peopleList = dat.rows;
					$(".peopleList").xcombobox({
						data:dat.rows,
						onChange:function(newValue,oldValue){
							selectedPeople = newValue;
							var chargePeopleList = [];
							var chargePeopleNameList = [];
							if(newValue.length > 0 && dat.rows.length > 0){
								for(var i=0;i<newValue.length;i++){
									for(var j=0;j<dat.rows.length;j++){
										if(newValue[i] == dat.rows[j].id){
											chargePeopleList.push(dat.rows[j]);
											chargePeopleNameList.push(dat.rows[j].name);
										}
									}
								}
								$(".peopleList").next().children().next().attr("title",chargePeopleNameList.toString());
								$(".inchargePeople").xcombobox({
									data:chargePeopleList
								});
								if(newValue.length == 1){
									$(".inchargePeople").xcombobox("setValue",newValue[0]);
								}
							}else{
								$(".inchargePeople").xcombobox({
									data:[]
								});
							}
						}
					});
					$(".concernList").xcombobox({
						data:dat.rows,
						onChange:function(newValue,oldValue){
							selectedConcerns = newValue;
							var chargePeopleNameList = [];
							if(newValue.length > 0 && dat.rows.length > 0){
								for(var i=0;i<newValue.length;i++){
									for(var j=0;j<dat.rows.length;j++){
										if(newValue[i] == dat.rows[j].id){
											chargePeopleNameList.push(dat.rows[j].name);
										}
									}
								}
								$(".concernList").next().children().next().attr("title",chargePeopleNameList.toString());
							}
						}
					});
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}
//加载任务类别列表
function loadMissionCategory(){
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!loadTestBaseSetList.action",
			{
				"page": 1,
				"rows": 30,
				"dto.subName": "任务类别",
				"dto.flag": "1",
			},
			function(dat) {
				if (dat != null) {
					$(".missionCategory").xcombobox({
						data:dat.rows
					});
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}
//加载任务紧急程度列表
function loadEmergencyDegree(){
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!loadTestBaseSetList.action",
			{
				"page": 1,
				"rows": 30,
				"dto.subName": "任务紧急程度",
				"dto.flag": "1",
			},
			function(dat) {
				if (dat != null) {
					$(".emergencyDegree").xcombobox({
						data:dat.rows
					});
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}
//加载任务难易程度列表
function loadDifficultyDegree(){
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!loadTestBaseSetList.action",
			{
				"page": 1,
				"rows": 30,
				"dto.subName": "任务难易程度",
				"dto.flag": "1",
			},
			function(dat) {
				if (dat != null) {
					$(".difficultyDegree").xcombobox({
						data:dat.rows
					});
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
				loadOtherMission();
			}, "json");
}
//选择项目改变事件
function changeProject(obj){
	proJ = obj.value;
}
//调用打开选择多人弹窗方法(参与者)
/*function showSeletctPeoplesWindowJoin(){
	flagPt = "0";
	var selectedPeos = $(".peopleList").xcombobox("getValues");
	if(selectedPeos.length == 0){
		showSeletctPeopleWindow("");
	}else{
		showSeletctPeopleWindow(selectedPeos.toString());
	}
}*/
//调用打开选择多人弹窗方法(关注者)
/*function showSeletctPeoplesWindowConcern(){
	flagPt = "1";
	var selectedPeos = $(".concernList").xcombobox("getValues");
	if(selectedPeos.length == 0){
		showSeletctPeopleWindow("");
	}else{
		showSeletctPeopleWindow(selectedPeos.toString());
	}
}*/

//提交选择的人员，返回选择人员的id
/*function submitSelectedPeoples(){
	var selectedPepoleRows = $('#selectedPepoleTa').xdatagrid('getRows');
	if (!selectedPepoleRows) {
		$.xalert({title:'提示',msg:'请选择人员！'});
		return;
	}
	var usersIds = [];
	for(var i=0;i<selectedPepoleRows.length;i++){
		usersIds.push(selectedPepoleRows[i].id);
	}
	$("#chooseSomePeopleDiv").xwindow('close');
	if(flagPt == "0"){
		$(".peopleList").xcombobox("setValues",usersIds);
	}else if(flagPt == "1"){
		$(".concernList").xcombobox("setValues",usersIds);
	}
}*/
//# sourceURL=otherMission.js