var fileInfos =  new Array();
var executeTaskId = "";
var exe_testpkgId="";
//获取页面url参数
function getQueryParam(name) {
       var obj = $('#executeTestCaseDlg').dialog('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}


$(function(){
	var packageId = getQueryParam('testCasePackageId');
	exe_testpkgId = packageId;
	//初始化加载测试用例列表
	loadTestCase(packageId);
	
});

function loadTestCase(packageId){
	
	$("#executeTestCaseList").xdatagrid({
		url: baseUrl + '/testCasePkgManager/testCasePackageAction!getSelTestCasesByPkgId.action?dto.testCasePackage.packageId=' + packageId ,
		method: 'post',
		singleSelect:true,
		height: mainObjs.tableHeight-140,
		fitColumns:true,
		columns:[[  
		            {field:'cp',title:'选择',checkbox:true},
		            {field: 'taskId',hidden:true},
					{field:'testCaseId',title:'编号',width:'5%',align:'center'},
					{field:'taskName',title:'项目名称',width:'10%',align:'center',halign:'center'},
					{field:'testCaseDes',title:'用例描述',width:'30%',align:'left',halign:'center',formatter:caseDetail},
					{field:'testStatus',title:'执行状态',width:'10%',align:'center',formatter:testStatusFormat},
					{field:'typeName',title:'类别',width:'5%',align:'left',halign:'center'},
					{field:'priName',title:'优先级',width:'5%',align:'center'},
					{field:'auditerNmae',title:'最近处理人',width:'12%',align:'center'},
					{field:'authorName',title:'编写人',width:'10%',align:'center',formatter:caseHistory},
					{field:'weight',title:'成本',width:'5%',align:'center'},
					{field:'creatdate',title:'编写日期',width:'12%',align:'left'}
				]],
			onLoadSuccess : function (data) {								
				if (data.total==0) {
					$('#executeTestCaseList').parent().find(".datagrid-view2 .datagrid-body").append('<div style="font-size:16px; text-align: center;">暂无数据</div>');
				}
			}
	         });
}


//用例状态
function testStatusFormat(value,row,index) {
	switch (value) {
	case 0:
		return '待审核';
	case 1:
		return '未测试';
	case 2:
		return '通过';
	case 3:
		return '未通过';
	case 4:
		return '不适用';
	case 5:
		return '阻塞';
	case 6:
		return '待修正';
	default:
		return '未测试';
	}
}

//用例详情
function caseDetail(value,row,index) {
	return "<span style='cursor:default' title='用例详情：--"+ value + "' href='javascript:;'>" + value + "</span>";
}
//用例历史
function caseHistory(value,row,index) {
	if(value=="" || value=="null" ||value==null){
		value=="--";
	}
	return "<span style=\"cursor: default\" title=\"" + value + "\" href=\"javascript:;\" >" + value + "</span>";
}


//执行用例
function executeCaseWin(){
	var row = $("#executeTestCaseList").xdatagrid('getSelected');

	if (!row) {
		$.xalert({title:'消息提示',msg:'请选择要执行的一条记录'});
		return;
	}
	var caseId = row.testCaseId;
	 executeTaskId = row.taskId;
	 $("<div></div>").xwindow({
	    	id:'editCaseWindown',
	    	title:"执行用例",
	    	width : 900,
	        height : 600,
	    	modal:true,
	    	footer:'#addOrEditFoot',
	    	collapsible:false,
			minimizable:false,
			maximizable:false,
	    	href:baseUrl + '/caseManager/caseManagerAction!caseEdit.action',
	    	queryParams: {
	    			'caseId':caseId,
	    			'operaType':'exec',
	    			'flag':'testCase',
	    			'testcasepkgId':exe_testpkgId
	    		},
	        onClose : function() {
	            $(this).xwindow('destroy');
	        }
	    });
}

//加载用例类别列表
function loadCaseCategory(){
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!loadTestBaseSetList.action",
			{
				"page": 1,
				"rows": 80,
				"dto.subName": "用例类型"
			},
			function(dat) {
				if (dat != null) {
					$(".caseTypeId").xcombobox({
						data:dat.rows
					});
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}
//加载优先级列表
function loadCaseYXJ(){
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!loadTestBaseSetList.action",
			{
				"page": 1,
				"rows": 80,
				"dto.subName": "用例优先级"
			},
			function(dat) {
				if (dat != null) {
					$(".priId").xcombobox({
						data:dat.rows
					});
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}


//执行用例--执行历史（列表初始化）
//操作转换
function operaTypeFormat(value,row,index){
	switch (value) {
	case 1:
		return '增加用例';
	case 2:
		return '修改用例';
	case 3:
		return '审核用例';
	case 4:
		return '执行用例';
	case 5:
		return '修正用例';
	default:
		return '-';
	}
}

//执行用例--执行历史（列表初始化）
//状态转换
function convCase(value,row,index){
	switch (value) {
	case 0:
		return '待审核';
	case 1:
		return '未测试';
	case 2:
		return '通过';
	case 3:
		return '未通过';
	case 4:
		return '不适用';
	case 5:
		return '阻塞';
	case 6:
		return '待修正';
	default:
		return '-';
	}
}

//执行用例---执行返回
function cancleCaseWin(){
	$("#executeTestCaseList").xdatagrid('reload');
	$("#editCaseForm").xform('clear');
	$("#editCaseWindown").xwindow('close');
}

function closeExecWin(){
	 $('#executeTestCaseDlg').dialog('destroy');
}

//@ sourceURL=executeTestCase.js