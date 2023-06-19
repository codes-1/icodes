var objs = {
	$missionDg: $("#missionDg")
	/*$addOrEditWin: $("#addOrEditWin"),
	$addOrEditForm: $("#addOrEditForm"),*/
};
//存放所有可执行任务的人员
var peopleList = [];
//存放所有项目
var projectList = [];
//存放所有任务类别
var missionCategoryList = [];
//存放所有任务紧急程度
var emergencyDegreeList = [];
//存放所有任务难易程度
var difficultyDegreeList = [];
//存放任务id
var missiId = "";
$(function() {
	/*$.parser.parse();*/
	//加载所有项目
	loadProjectList();
	//加载所有可执行任务的员工
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
});

//查询任务信息
function searchOtherMission(){
	objs.$missionDg.xdatagrid({
		url: baseUrl + '/otherMission/otherMissionAction!otherMissionListLoad.action',
		method: 'post',
		queryParams: {
			"dto.otherMission.missionName":$("#missionName").val(),
			"dto.concernId":$("#accountId").text(),
			"dto.otherMission.status":$("#statu").val(),
			"dto.otherMission.projectId":$("#projectNa").val()
		},
		height: mainObjs.tableHeight,
		emptyMsg:"无数据",
		columns:[[
		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'missionName',title:'任务名称',width:"9%",align:'center'},
			/*{field:'missionCategory',title:'任务类别',align:'center',halign:'center',formatter:missionCategoryFormat},
			{field:'missionType',title:'任务类型',align:'center',halign:'center',formatter:missionTypeFormat},*/
			{field:'projectId',title:'所属项目',width:"9%",align:'center',formatter:projectFormat},
			{field:'missionId',title:'任务参与者',width:"9%",align:'center',formatter:missionPersonFormat},
			{field:'chargePersonId',title:'任务负责人',width:"9%",align:'center',halign:'center',formatter:chargePersonFormat},
			/*{field:'emergencyDegree',title:'紧急程度',align:'center',formatter:emergencyDegreeFormat},
			{field:'difficultyDegree',title:'难易程度',align:'center',formatter:difficultyDegreeFormat},
			{field:'standardWorkload',title:'标准工作量',align:'center'},*/
			{field:'actualWorkload',title:'实际工作量(小时)',width:"11%",align:'center'},
			{field:'description',title:'任务描述',width:"8%",align:'center',formatter:descriptionFormat},
			{field:'completionDegree',title:'任务进度(%)',width:"9%",align:'center'},
			{field:'status',title:'状态',width:"9%",align:'center',formatter:statusFormat},
			/*{field:'createUserId',title:'发起人',align:'center'},*/
			{field:'predictStartTime',title:'预计开始时间',width:"9%",align:'center',formatter:predictStartTimeFormat},
			{field:'predictEndTime',title:'预计完成时间',width:"9%",align:'center',formatter:predictEndTimeFormat},
			{field:'ttt',title:'操作',align:'center',width:"9.3%",formatter:operationFormat}
			/*{field:'createTime',title:'创建时间',align:'center'}*/
		]],
		onSelect : function(index,row){
			$(".datagrid-row-checked").css({"background":"none","color":"#404040"});
		},
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
			"dto.concernId":$("#accountId").text()
		},
		height: mainObjs.tableHeight,
		emptyMsg:"无数据",
		columns:[[
		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'missionName',title:'任务名称',width:"9%",align:'center'},
			/*{field:'missionCategory',title:'任务类别',align:'center',halign:'center',formatter:missionCategoryFormat},
			{field:'missionType',title:'任务类型',align:'center',halign:'center',formatter:missionTypeFormat},*/
			{field:'projectId',title:'所属项目',width:"9%",align:'center',formatter:projectFormat},
			{field:'missionId',title:'任务参与者',width:"9%",align:'center',formatter:missionPersonFormat},
			{field:'chargePersonId',title:'任务负责人',width:"9%",align:'center',halign:'center',formatter:chargePersonFormat},
			/*{field:'emergencyDegree',title:'紧急程度',align:'center',formatter:emergencyDegreeFormat},
			{field:'difficultyDegree',title:'难易程度',align:'center',formatter:difficultyDegreeFormat},
			{field:'standardWorkload',title:'标准工作量',align:'center'},*/
			{field:'actualWorkload',title:'实际工作量(小时)',width:"11%",align:'center'},
			{field:'description',title:'任务描述',width:"8%",align:'center',formatter:descriptionFormat},
			{field:'completionDegree',title:'任务进度(%)',width:"9%",align:'center'},
			{field:'status',title:'状态',width:"9%",align:'center',formatter:statusFormat},
			/*{field:'createUserId',title:'发起人',align:'center'},*/
			{field:'predictStartTime',title:'预计开始时间',width:"9%",align:'center',formatter:predictStartTimeFormat},
			{field:'predictEndTime',title:'预计完成时间',width:"9%",align:'center',formatter:predictEndTimeFormat},
			{field:'ttt',title:'操作',align:'center',width:"9.3%",formatter:operationFormat}
			/*{field:'createTime',title:'创建时间',align:'center'}*/
		]],
		onSelect : function(index,row){
			$(".datagrid-row-checked").css({"background":"none","color":"#404040"});
		},
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
				$.xalert({title:'提示',msg:'系统错误！'});
			}
	   }
	});
	return "<span title='"+userNames+"'>"+userNames.substring(0,5)+"....</span>";
}
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
//操作
function operationFormat(value,row,index){
	var html = "<a style='cursor:pointer;' onclick='showDetail("+JSON.stringify(row)+")'>详情</a>";
	return html;
}
// 提交修改的记录
/*function submit() {
	var saveOrUpdateUrl = baseUrl + "/otherMission/otherMissionAction!updateStatus.action";
	
	//获取表单数据
	var objData = objs.$addOrEditWin.xserialize();
	
	$.post(
		saveOrUpdateUrl,
		objData,
		function(data) {
			if (data =="success") {
				objs.$addOrEditWin.xform('clear');
				objs.$addOrEditWin.xwindow('close');
				loadOtherMission();
			} else {
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		}, "text");
}*/

// 打开修改弹窗
/*function showEditWin() {
	var row = objs.$missionDg.xdatagrid('getSelected');
	if (!row) {
		$.xnotify('请选择要修改的一条记录', {type:'warning'});
		return;
	}
	if(row.chargePersonId != $("#accountId").text()){
		$.xnotify('你不是任务负责人，无权修改任务', {type:'warning'});
		return;
	}
	var fillData = {};
	fillData["dto.otherMission"] = row;
	objs.$addOrEditWin.xdeserialize(fillData);
	objs.$addOrEditWin.parent().css("border","none");
	objs.$addOrEditWin.prev().css({ color: "#ffff", background: "#101010" });
	objs.$addOrEditWin.xwindow('setTitle','修改其他任务').xwindow('open');
}*/

//取消提交并关闭弹窗
/*function closeWin() {
	objs.$addOrEditWin.xwindow('close');
}*/
//加载所属项目下拉菜单
function loadProjectList(){
	$.post(
		baseUrl + "/otherMission/otherMissionAction!getProjectLists.action",
		null,
		function(dat) {
			if (dat != null) {
				projectList = dat.rows;
				$(".projectIds").xcombobox({
					data:dat.rows,
					onChange:function(newValue,oldValue){
						proJ = newValue;
					}
				});
				//加载下拉菜单选项(为管理员时)
				if($("#isAdmin").text() == "2" || $("#isAdmin").text() == "1"){
					var opti = '<option value="">-请选择项目-</option>';
					if(dat.rows.length > 0){
						for(var i=0;i<dat.rows.length;i++){
							opti = opti + '<option value="'+dat.rows[i].projectId+'">'+dat.rows[i].projectName+'</option>';
						}
					}
					$(".searchable-select").remove();
					$("#projectNa").html(opti);
					$('#projectNa').searchableSelect();
				}else{
					//加载下拉菜单选项(非管理员时)
					var opti = '<option value="">-请选择项目-</option>';
					$.post(baseUrl + "/otherMission/otherMissionAction!getProjectListsRelated.action?dto.related=concern",null,function(datt) {
						if(datt.rows.length > 0){
							for(var p=0;p<datt.rows.length;p++){
								opti = opti + '<option value="'+datt.rows[p].projectId+'">'+datt.rows[p].projectName+'</option>';
							}
						}
						$(".searchable-select").remove();
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
//加载可执行任务员工列表
function loadPeopleLists(){
	$.post(
			baseUrl + "/otherMission/otherMissionAction!getPeopleLists.action",
			null,
			function(dat) {
				if (dat != null) {
					peopleList = dat.rows;
				} else {
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}
//查看详情
function showDetail(row){
	$(".nav-lattice").show();
	missiId = row.missionId;
	$(".nav-lattice").children().addClass("active");
	$(".nav-lattice").children().next().removeClass("active");
	$("#xiangqing").addClass("in active");
	$("#rizhi").removeClass("in active");
	var uu = 0;
	var ii = 0;
	var pp = 0;
	for(var i=0;i<projectList.length;i++){
		if(row.projectId == projectList[i].projectId){
			row.projectId = projectList[i].projectName;
			break;
		}
	}
	
	for(var t=0;t<peopleList.length;t++){
		if(row.chargePersonId == peopleList[t].id){
			row.chargePersonId = peopleList[t].name;
			break;
		}
	}
	if(missionCategoryList.length > 0){
		for(var p=0;p<missionCategoryList.length;p++){
			if(row.missionCategory == missionCategoryList[p].typeId){
				row.missionCategory = missionCategoryList[p].typeName;
				break;
			}else{
				pp = pp + 1;
			}
		}
		if(pp == missionCategoryList.length){
			row.missionCategory = "";
		}
	}else{
		row.missionCategory = "";
	}
	if(emergencyDegreeList.length > 0){
		for(var q=0;q<emergencyDegreeList.length;q++){
			if(row.emergencyDegree == emergencyDegreeList[q].typeId){
				row.emergencyDegree = emergencyDegreeList[q].typeName;
				break;
			}else{
				uu = uu + 1;
			}
		}
		if(uu == emergencyDegreeList.length){
			row.emergencyDegree = "";
		}
	}else{
		row.emergencyDegree = "";
	}
	
	if(difficultyDegreeList.length > 0){
		for(var h=0;h<difficultyDegreeList.length;h++){
			if(row.difficultyDegree == difficultyDegreeList[h].typeId){
				row.difficultyDegree = difficultyDegreeList[h].typeName;
				break;
			}else{
				ii = ii + 1;
			}
		}
		if(ii == difficultyDegreeList.length){
			row.difficultyDegree = "";
		}
	}else{
		row.difficultyDegree = "";
	}
	
	if(row.status == "0"){
		row.status = "分配";
	}else if(row.status == "1"){
		row.status = "进行中";
	}else if(row.status == "2"){
		row.status = "完成";
	}else if(row.status == "3"){
		row.status = "终止";
	}else if(row.status == "4"){
		row.status = "暂停";
	}else{
		row.status = "";
	}
	if(row.predictStartTime){
		row.predictStartTime = row.predictStartTime.substring(0,10);
	}else{
		row.predictStartTime = "";
	}
	if(row.predictEndTime){
		row.predictEndTime = row.predictEndTime.substring(0,10);
	}else{
		row.predictEndTime = "";
	}
	if(!row.stopReason){
		row.stopReason = "";
	}
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
				  row.peoples = data;
				  $.post(baseUrl + '/otherMission/otherMissionAction!getConcernNames.action',{'dto.otherMission.missionId':row.missionId},function(da){
					  if(da != null){
						  if(da == "failed"){
							  row.concerns = "";
						  }else{
							  row.concerns = da;
						  }
						  $("#detailTable").empty().append("<tr>"
						    		+"<th style='text-align:right'>任务名称：</th>"
						    		+"<td style='width:160px'>"+row.missionName+"</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务描述：</th>"
						    		+"<td style='width:160px;' title='"+row.description+"'>"+row.description.substring(0,10)+"...</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务类别：</th>"
						    		+"<td style='width:160px'>"+row.missionCategory+"</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		/*+"<th style='text-align:right'>任务类型：</th>"
						    		+"<td style='width:160px'>其他任务</td>"*/
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>所属项目：</th>"
						    		+"<td style='width:160px'>"+row.projectId+"</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务参与者：</th>"
						    		+"<td style='width:160px'>"+row.peoples+"</td>"
						    		+"<th style='text-align:right'>任务负责人：</th>"
						    		+"<td style='width:160px'>"+row.chargePersonId+"</td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务关注者：</th>"
						    		+"<td style='width:160px'>"+row.concerns+"</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>紧急程度：</th>"
						    		+"<td style='width:160px'>"+row.emergencyDegree+"</td>"
						    		+"<th style='text-align:right'>难易程度：</th>"
						    		+"<td style='width:160px'>"+row.difficultyDegree+"</td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>标准工作量(小时)：</th>"
						    		+"<td style='width:160px'>"+row.standardWorkload+"</td>"
						    		+"<th style='text-align:right'>实际工作量(小时)：</th>"
						    		+"<td style='width:160px'>"+row.actualWorkload+"</td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务进度(%)：</th>"
						    		+"<td style='width:160px'>"+row.completionDegree+"</td>"
						    		+"<th style='text-align:right'>任务状态：</th>"
						    		+"<td style='width:160px'>"+row.status+"</td>"
						    		+"</tr>"
						    		+"<tr class='klklkl'>"
						    		+"<th style='text-align:right'>终止原因：</th>"
						    		+"<td style='width:160px;' title='"+row.stopReason+"'>"+row.stopReason.substring(0,10)+"...</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr class='lkl'>"
						    		+"<th style='text-align:right'>暂停原因：</th>"
						    		+"<td style='width:160px;' title='"+row.stopReason+"'>"+row.stopReason.substring(0,10)+"...</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>预计开始时间：</th>"
						    		+"<td style='width:160px'>"+row.predictStartTime+"</td>"
						    		+"<th style='text-align:right'>预计结束时间：</th>"
						    		+"<td style='width:160px'>"+row.predictEndTime+"</td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务发起人：</th>"
						    		+"<td style='width:160px'>"+row.createUserId+"</td>"
						    		+"<th style='text-align:right'>任务创建时间：</th>"
						    		+"<td style='width:160px'>"+row.createTime+"</td>"
						    		+"</tr>");
						  if(row.status == "终止"){
								$(".klklkl").show();
								$(".lkl").hide();
							}else if(row.status == "暂停"){
								$(".klklkl").hide();
								$(".lkl").show();
							}else{
								$(".klklkl").hide();
								$(".lkl").hide();
							}
					  }
				  },'text');
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
		   }
		});
	$("#detailWin").parent().css("border","none");
	$("#detailWin").prev().css({ color: "#ffff", background: "#101010" });
	/*loadOtherMissionLog(row.missionId);*/
	$("#xiangqing").show();
	$("#xiangqing").next().hide();
	$("#detailWin").xwindow('setTitle','详情').xwindow('open');
	$("#detailWin").xwindow("vcenter");
}
//显示日志datagrid
function loadOtherMissionLog(id){
	$("#rizhi").xdatagrid({
		url: baseUrl + '/otherMission/otherMissionAction!getMissionLog.action',
		method: 'post',
		queryParams: {
			"dto.otherMission.missionId":id
		},
		emptyMsg:"无数据",
		fitColumns: true,
		singleSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		pageList:[10,30,50],
		layout:['list','first','prev','manual','next','last','refresh','info'],
		columns:[[
			{field:'operateTime',title:'操作时间',width:"25%",align:'center',formatter:operateTimeFormat},
			{field:'operatePerson',title:'操作者',width:"25%",align:'center'},
			{field:'operateType',title:'操作类型',width:"25%",align:'center',formatter:operateTypeFormat},
			{field:'operateDetail',title:'操作详情',align:'center',width:"25%",formatter:operateDetailFormat}
		]],
		onSelect : function(index,row){
			$(".datagrid-row-checked").css({"background":"none","color":"#404040"});
		},
		onLoadSuccess : function (data) {								
			
		}
	});
}
function operateTimeFormat(value,row,index){
	if(value){
		return "<span title='"+value+"'>"+value.substring(0,10)+"</span>";
	}
	return "";
}
function operateTypeFormat(value,row,index){
	if(value == "1"){
		return "新建任务并分配";
	}else if(value == "2"){
		return "修改状态";
	}else if(value == "3"){
		return "填写进度";
	}else if(value == "4"){
		return "修改工作量";
	}
}
function operateDetailFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,20)+".....</span>";
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
					missionCategoryList = dat.rows;
				} else {
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
					emergencyDegreeList = dat.rows;
				} else {
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
					difficultyDegreeList = dat.rows;
				} else {
					$.xalert({title:'提示',msg:'系统错误！'});
				}
				loadOtherMission();
			}, "json");
}
$('a[href="#rizhi"]').click(function(){
	$("#xiangqing").hide();
	loadOtherMissionLog(missiId);
	$("#xiangqing").next().show();
});
$('a[href="#xiangqing"]').click(function(){
	$("#xiangqing").show();
	$("#xiangqing").next().hide();
});
//关闭详情页
function closeDetailWin(){
	$("#detailWin").xwindow('close');
}
//# sourceURL=toMeConcern.js