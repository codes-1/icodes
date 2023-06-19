var objs = {
	$testLibraryDg: $("#testLibraryDg"),
	$addOrEditWin: $("#addOrEditWin"),
	$addOrEditForm: $("#addOrEditForm"),
};
//存放新增子菜单时被选中的节点数据
var selectedRow = {};
//存放flag，判断是否新增的是子类型
var flag = "0";
$(function() {
	$.parser.parse();
	//加载测试用例类型数据
	initTable();
	//管理员时，显示审核按钮
	if($("#loginName").text() == "admin"){
		$(".managerAdmin").show();
	}
});

//加载菜单树列表
function initTable() {
	objs.$testLibraryDg.xtreegrid({
		url: baseUrl + '/testLibrary/testLibraryAction!loadTreeList.action',
		height: mainObjs.tableHeight,
		method:'post',
	    animate:true,
	    rownumbers: true,
	    idField:'libraryId',
	    treeField:'testcaseType',
	    columns: [[
	  	    {field:'testcaseType', title:'用例类型', width:'25%', halign:'center', align:'left'},
	  		{field:'createTime', title:'创建时间', width:'25%', halign:'center', align:'center',formatter:createTimeFormat},
	  		{field:'updateTime', title:'更新时间', width:'25%', align:'center', align:'center',formatter:updateTimeFormat},
	  		{field:'operate', title:'操作', width:'25%', align:'center', formatter:operateFormat}
	  	]],
	  	onSelect : function(index,row){
			$(".datagrid-row-checked").css({"background":"none","color":"#404040"});
		},
		onLoadSuccess : function (data) {	
			objs.$testLibraryDg.xtreegrid('resize');
		}
	});
}
function createTimeFormat(value,row,index){
	if(value){
		return value.substring(0,10);
	}
	return "";
}
function updateTimeFormat(value,row,index){
	if(value){
		return value.substring(0,10);
	}
	return "";
}
//操作
function operateFormat(value,row,index){
	var html = "<a style='cursor:pointer;' onclick='showAddWin1("+JSON.stringify(row)+")'>新增子类型</a><a style='margin-left:5px;cursor:pointer;' onclick='showEditWin("+JSON.stringify(row)+")'>修改</a><a style='margin-left:5px;cursor:pointer;' onclick='showdelConfirm("+JSON.stringify(row)+")'>删除</a>";
	return html;
}
// 提交保存新增或修改的记录
function submit() {
	//获取表单数据
	var objData = objs.$addOrEditWin.xserialize();
	
	var saveOrUpdateUrl = "";
	if(objData["dto.testCaseLibrary.libraryId"]){
		saveOrUpdateUrl = baseUrl + "/testLibrary/testLibraryAction!update.action";
	}else{
		saveOrUpdateUrl = baseUrl + "/testLibrary/testLibraryAction!add.action";
		objData["dto.testCaseLibrary.createUserId"] = $("#loginNam").text();
		//新增子类型
		if(flag == "1"){
			objData["dto.testCaseLibrary.parentId"] = selectedRow.libraryId;
			objData["dto.testCaseLibrary.libraryCode"] = selectedRow.libraryCode + "0";
		}
	}
	
	
	if(!objData["dto.testCaseLibrary.testcaseType"]){
		$.xalert({title:'提交失败',msg:'请填写完整所有必填信息！'});
		return;
	}
	$.post(
		saveOrUpdateUrl,
		objData,
		function(data) {
			if (data =="success") {
				objs.$addOrEditWin.xform('clear');
				objs.$addOrEditWin.xwindow('close');
				objs.$testLibraryDg.xtreegrid('reload');
				$.xalert({title:'提示',msg:'操作成功！'});
			} else if(data =="existed"){
				$.xalert({title:'提交失败',msg:'该类型已存在，请勿重复添加！'});
			}else {
				$.xalert({title:'提交失败',msg:'系统错误！'});
			}
		}, "text");
}

// 打开新增弹窗(一级用例类型)
function showAddWin() {
	flag = "0";
	objs.$addOrEditForm.xform('clear');
	$("#addOrEditForm input").val("");
	objs.$addOrEditWin.parent().css("border","none");
	objs.$addOrEditWin.prev().css({ color: "#ffff", background: "#101010" });
	objs.$addOrEditWin.xwindow('setTitle','新增一级用例类型').xwindow('open');
}
// 打开新增弹窗(非一级用例类型)
function showAddWin1(row){
	objs.$addOrEditForm.xform('clear');
	$("#addOrEditForm input").val("");
	objs.$addOrEditWin.parent().css("border","none");
	objs.$addOrEditWin.prev().css({ color: "#ffff", background: "#101010" });
	objs.$addOrEditWin.xwindow('setTitle','新增子类型').xwindow('open');
	selectedRow = row;
	flag = "1";
	/*$("#parId").val(row.libraryId);
	$("#libraryCode").val(row.libraryCode + "0");*/
}

// 打开修改弹窗
function showEditWin(row) {
	objs.$addOrEditForm.xform('clear');
	$("#addOrEditForm input").val("");
	var fillData = {};
	fillData["dto.testCaseLibrary"] = row;
	//回填表单数据
	objs.$addOrEditWin.xdeserialize(fillData);
	objs.$addOrEditWin.parent().css("border","none");
	objs.$addOrEditWin.prev().css({ color: "#ffff", background: "#101010" });
	objs.$addOrEditWin.xwindow('setTitle','修改测试用例类型').xwindow('open');
				
}


// 打开删除确认弹窗
function showdelConfirm(row) {
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			$.post(
				baseUrl + "/testLibrary/testLibraryAction!delete.action",
				{'dto.testCaseLibrary.libraryCode': row.libraryCode},
				function(data) {
					if (data == "success") {
						objs.$testLibraryDg.xtreegrid('reload');
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
//打开审核弹窗，显示所有未审核用例
function showExamineWin(){
	$("#examineDg").xdatagrid({
		url: baseUrl + '/testLibrary/testLibraryAction!testCaseDetailLoad.action',
		method: 'post',
		/*queryParams: {
			"dto.otherMission.missionName":$("#missionName").val(),
			"dto.otherMission.createUserId":$("#loginName").text(),
			"dto.otherMission.status":$("#statu").val()
		},*/
		/*height: mainObjs.tableHeight,*/
		emptyMsg:"无数据",
		columns:[[
		    /*{field:'checkId',title:'选择',checkbox:true,align:'center'},*/
			{field:'testCaseDes',title:'用例描述',width:"20%",align:'left',formatter:testCaseDesFormat},
			{field:'recommendReason',title:'推荐理由',width:"20%",align:'left',formatter:recommendReasonFormat},
			{field:'recommendUserId',title:'推荐人',width:"20%",align:'center'},
			{field:'libraryId',title:'推荐类别',width:"20%",align:'center',formatter:libraryIdFormat},
			{field:'operate',title:'操作',width:"20%",align:'center',formatter:operateFormat1},
		]],
		onLoadSuccess : function (data) {								
			/*if (data.total==0) {
				$('tr[id^="datagrid-row-r"]').parent().append('<label style="height: 40px;width: 900px; text-align: center;">没有数据!</label>');
			}
			$("#examineDg").xdatagrid('resize');*/
		}
	});
	$("#examineWin").xwindow('setTitle','审核').xwindow('open');
}

function testCaseDesFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,10)+"</span>";
}

function recommendReasonFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,10)+"</span>";
}

function libraryIdFormat(value,row,index){
	var libraryName = "";
	$.ajax({
	  url: baseUrl + "/testLibrary/testLibraryAction!getLibraryTypeName.action",
	  cache: false,
	  async: false,
	  type: "POST",
	  data: {
		  'dto.testCaseLibrary.libraryId':row.libraryId
	  },
	  dataType:"text",
	  success: function(data){
		  if (data !=null && data !="failed") {
			  libraryName = data;
			} else {
				$.xalert({title:'提示',msg:'系统错误！'});
			}
	   }
	});
	return libraryName;
}
//操作
function operateFormat1(value,row,index){
	var html = "<a style='cursor:pointer;' onclick='agreeLibrary("+JSON.stringify(row)+")'>通过</a><a style='margin-left:5px;cursor:pointer;' onclick='disagreeLibrary("+JSON.stringify(row)+")'>不通过</a>";
	return html;
}
//通过审核
function agreeLibrary(row){
	$.post(baseUrl + '/testLibrary/testLibraryAction!updateTestLibraryDetailStatus.action',{
		'dto.testCaseLibraryDetail.testCaseId':row.testCaseId,
		'dto.testCaseLibraryDetail.examineUserId':$("#loginName").text()
	},function(data){
		if(data == "success"){
			$("#examineDg").xdatagrid("reload");
		}
	},'text');
}
//不通过审核
function disagreeLibrary(row){
	$.post(baseUrl + '/testLibrary/testLibraryAction!deleteTestLibraryDetail.action',{
		'dto.testCaseLibraryDetail.testCaseId':row.testCaseId
	},function(data){
		if(data == "success"){
			$("#examineDg").xdatagrid("reload");
		}
	},'text');
}
//# sourceURL=testLibraryTree.js