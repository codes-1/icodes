var iterationId="";
var taskId ="";
var currNodeId="";
var rootNodeId;
var switchFlag=0;
var loadCount=0;
var missionIds="";

//存放所有可执行任务的人员
//var peopleList = [];
//存放被选中的参与者
var selectedPeople = [];
//存放被选中的关注者
var selectedConcerns = [];
//存放所有项目
//var projectList = [];

//获取页面url参数
function getQueryParam(name) {
       var obj = $('#iterationTaskLayout').dialog('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

$(function(){
	$.parser.parse();
	iterationId = getQueryParam('iterationListId');
	taskId = getQueryParam('taskBugId');
	missionIds=getQueryParam('alreadyData');
	getTaskDetail();
	/*loadProjectList();
	loadPeopleLists();*/
});

//项目任务的详细
function getTaskDetail(){
	$("#ProTaskList").xdatagrid({
		url:  baseUrl + '/otherMission/otherMissionAction!otherMissionListLoad.action',
		method: 'post',
		height: mainObjs.tableHeight-140,
		queryParams:{
					'dto.otherMission.projectId':taskId,
					'dto.relaMissionId':missionIds
						},
		striped:true,
		pagination: true,
		fitColumns: true,
		rownumbers: true,
		multiple:true,
		pageNumber: 1,
		pageSize: 10,
		layout:['list','first','prev','manual','next','last','refresh','info'],
		columns:[[
			{field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'missionName',title:'任务名称',width:"10%",align:'center'},
			{field:'projectId',title:'所属项目',width:"10%",align:'center',formatter:projectFormat},
			{field:'missionId',title:'任务参与者',width:"10%",align:'center',formatter:missionPersonFormat},
			{field:'chargePersonId',title:'任务负责人',width:"10%",align:'center',halign:'center',formatter:chargePersonFormat},
			{field:'actualWorkload',title:'实际工作量(小时)',width:"12%",align:'center'},
			{field:'description',title:'任务描述',width:"10%",align:'center',formatter:descriptionFormat},
			{field:'completionDegree',title:'任务进度(%)',width:"10%",align:'center'},
			{field:'status',title:'状态',width:"8%",align:'center',formatter:statusFormat},
			{field:'predictStartTime',title:'预计开始时间',width:"10%",align:'center',formatter:predictStartTimeFormat},
			{field:'predictEndTime',title:'预计完成时间',width:"10%",align:'center',formatter:predictEndTimeFormat},
		]],
		onLoadSuccess : function (data) {								
			if (data.total==0) {
				$('button[onclick="submitIteraTask()"]')
				.parent()
				.next()
				.find($('tr[id^="datagrid-row-r"]'))
				.empty()
				.append('<label style="height: 40px;width: 900px;margin-top: 8px;margin-left: 16%;text-align: center;">暂无数据!</label>');
			}
		}
	});
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
//				$.xnotify("系统错误！", {type:'warning'});
				tip("系统错误");
			}
	   }
	});
	return "<span title='"+userNames+"'>"+userNames.substring(0,5)+"....</span>";
}

function descriptionFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,5)+"....</span>";
}

function statusFormat(value,row,index){
	if(value == "0"){
		return "分配";
	}else if(value == "1"){
		return "接受";
	}else if(value == "2"){
		return "完成";
	}else if(value == "3"){
		return "终止";
	}
	return "";
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

function chargePersonFormat(value,row,index){
	for(var i=0;i<peopleList.length;i++){
		if(value == peopleList[i].id){
			return peopleList[i].name;
		}
	}
	return "暂无";
}

function submitIteraTask(){
	var selItems = 	$("#ProTaskList").xdatagrid('getSelections');
	if(selItems.length <= 0){
//		$.xalert({title:'提示',msg:'请选择项目任务！'});
		tip("请选择项目任务！");
		return;
	}else{
		selTestCaseIds = selItems.map(function(value){
			return value.missionId;
		});
	}
	
	$.post(
			baseUrl + "/iteration/iterationAction!saveTaskReal.action",
			{"dto.iterationList.iterationId": iterationId,
			 "dto.otherMissionS":selTestCaseIds.toString()},
			function(dataObj) {
				if(dataObj.indexOf("success") === -1){
//					$.xalert({title:'提示',msg:'保存失败，请稍后再试！'});
					tip("保存失败，请稍后再试！");
				}else{
					$.xalert({title:'提示',msg:'保存成功!',okFn: function() {
						$('#iterationTaskLayout').dialog('destroy');
						getTaskList();//获取得到关联的任务
					}});
//					tipSave();
					
				}
			},"text"
		);
}

function tipSave(){
	$.xconfirm({
		msg:"保存成功!",
		okFn: function() {
			$('#iterationTaskLayout').dialog('destroy');
			getTaskList();//获取得到关联的任务
		}
	});
}

function getTaskList(){
	objs.$proTask.xdatagrid({
		url:  baseUrl + '/iteration/iterationAction!searchIteraTaskDetail.action',
		method: 'post',
//		height: mainObjs.tableHeight-140,
		queryParams:{'dto.iterationList.iterationId':iterationId},
		striped:true,
		pagination: true,
		fitColumns: true,
		rownumbers: true,
		multiple:true,
		pageNumber: 1,
		pageSize: 10,
		layout:['list','first','prev','manual','next','last','refresh','info'],
		columns:[[
			{field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'missionName',title:'任务名称',width:"10%",align:'center'},
			{field:'projectId',title:'所属项目',width:"10%",align:'center',formatter:projectFormat},
			{field:'missionId',title:'任务参与者',width:"15%",align:'center',formatter:missionPersonFormat},
			{field:'chargePersonId',title:'任务负责人',width:"10%",align:'center',halign:'center',formatter:chargePersonFormat},
			{field:'actualWorkload',title:'实际工作量(小时)',width:"12%",align:'center'},
			{field:'description',title:'任务描述',width:"15%",align:'center',formatter:descriptionFormat},
			{field:'completionDegree',title:'任务进度(%)',width:"10%",align:'center'},
//			{field:'predictStartTime',title:'预计开始时间',width:"10%",align:'center',formatter:predictStartTimeFormat},
//			{field:'predictEndTime',title:'预计完成时间',width:"10%",align:'center',formatter:predictEndTimeFormat},
			{field:'status',title:'状态',width:"8%",align:'center',formatter:status_Format},
			{field:'ttt',title:'操作',align:'center',width:"9%",formatter:operation_Formats},
		]],
		onLoadSuccess : function (data) {
			var mission_id_join ="";
			if (data.total==0) {
				var contentTestCase = $('div[class="tab-content"]').children().eq(2).find($('tr[id^="datagrid-row-r"]'));
				contentTestCase.empty();
				contentTestCase.append('<label style="height: 40px;width: 900px;margin-top: 8px;margin-left: 16%;text-align: center;">暂无数据!</label>');
			}else{
				for(var i=0; i<data.total;i++){
					mission_id_join = mission_id_join+data.rows[i].missionId+" ";
				}
			}
			$("#mission_id").val($.trim(mission_id_join));
			$('.tab-content div[class="panel panel-default datagrid panel-htop"]').eq(1).height(370);
			$('#proTask').prev().find($('div[class="datagrid-body"]')).height(282);
			$('.tab-content div[class="panel panel-default datagrid panel-htop"]').eq(1).find($('div[class="datagrid-view"]')).height(320);
		}
	});
}

//"操作"列
function operatFormat(value,row,index){
	var columnStr = "<div>" +
                     "<button type='button' class='btn btn-default' id='editRoleInfBtn' onclick='viewTestCase(\""+ row.packageId + "\")' >" +
                     "查看用例" +
                     "</button>"+
		             "</div>";
	return columnStr;
}

//关闭弹窗
$('#closeTaskWin').click(function(){
	objs.$proTask.xdatagrid('reload');
    $('#iterationTaskLayout').xdialog('destroy');
});

function tip(info){
	$.xconfirm({
		msg:info,
		okFn: function() {
			
		},
		cancelFn: function(){
			
		}
	});
}
