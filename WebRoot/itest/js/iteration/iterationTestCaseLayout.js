var iterationId="";
var taskId ="";
var currNodeId="";
var rootNodeId;
var switchFlag=0;
var loadCount=0;
var packageIds="";

//获取页面url参数
function getQueryParam(name) {
       var obj = $('#iterationTestCaseLayout').xdialog('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

$(function(){
	$.parser.parse();
	iterationId = getQueryParam('iterationListId');
	taskId = getQueryParam('taskBugId');
	packageIds = getQueryParam('testCaseData');
//	caseTree();
	getTestcaseDetails(taskId);
});

//测试包的详细
function getTestcaseDetails(){
	$("#TestCaseList").xdatagrid({
		url:  baseUrl + '/testCasePkgManager/testCasePackageAction!loadTestCasePackageList.action',
		method: 'post',
		height: mainObjs.tableHeight-140,
		queryParams:{
						'dto.testCasePackage.taskId':taskId,
						'dto.packageIdJoin':packageIds
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
			{field:'packageId',hidden:true},
			{field:'packageName',title:'测试包名称',width:'25%',height:'50px',align:'center',halign:'center'},
			{field:'execEnvironment',title:'执行环境',width:'25%',height:'50px',align:'center'},
			{field:'executor',title:'执行人',width:'25%',height:'50px',align:'center',halign:'center'},
			{field:'remark',title:'备注',width:'25%',height:'50px',align:'center'},
//			{field:'operator',title:'操作',width:'20%',height:'50px',align:'center',formatter:operatFormat},
			{field:'testCaseNames',hidden:true}
		]],
		onLoadSuccess : function (data) {								
			if (data.total==0) {
				var contentTestCase = $('div[class="tab-content"]').children().eq(2).find($('tr[id^="datagrid-row-r"]'));
				contentTestCase.empty();
				contentTestCase.append('<label style="height: 40px;width: 900px;margin-top: 8px;margin-left: 16%;text-align: center;">暂无数据!</label>');
			}
		}
	});
}





function submitTestCase(){
	var selItems = 	$("#TestCaseList").xdatagrid('getSelections');
	if(selItems.length <= 0){
//		$.xalert({title:'提示',msg:'请选择测试用例！'});
		tip("请选择测试用例！");
		return;
	}else{
		selTestCaseIds = selItems.map(function(value){
			return value.packageId;
		});
	}
	
	$.post(
			baseUrl + "/iteration/iterationAction!saveTestCasePackage.action",
			{"dto.iterationList.iterationId": iterationId,
			 "dto.testCaseP":selTestCaseIds.toString()},
			function(dataObj) {
				if(dataObj.indexOf("success") === -1){
//					$.xalert({title:'提示',msg:'保存失败，请稍后再试！'});
					tip("保存失败，请稍后再试！");
				}else{
					$.xalert({title:'提示',msg:'保存成功!',okFn:function(){
						$('#iterationTestCaseLayout').dialog('destroy');
						getTestcaseList();//获取得到关联的测试包
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
			$('#iterationTestCaseLayout').dialog('destroy');
			getTestcaseList();//获取得到关联的测试包
		}
	});
}

function getTestcaseList(){
	objs.$proTestcase.xdatagrid({
		url:  baseUrl + '/iteration/iterationAction!searchTestCaseDetail.action',
		method: 'post',
		height: mainObjs.tableHeight-140,
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
			{field:'packageId',hidden:true},
			{field:'packageName',title:'测试包名称',width:'20%',height:'50px',align:'center',halign:'center'},
			{field:'execEnvironment',title:'执行环境',width:'20%',height:'50px',align:'center'},
			{field:'executor',title:'执行人',width:'20%',height:'50px',align:'center',halign:'center'},
			{field:'remark',title:'备注',width:'20%',height:'50px',align:'center'},
			{field:'operator',title:'操作',width:'20%',height:'50px',align:'center',formatter:operat_Format},
			{field:'testCaseNames',hidden:true}
		]],
		onLoadSuccess : function (data) {
			var packageId_join="";
			if (data.total==0) {
				var contentTestCase = $('div[class="tab-content"]').children().eq(2).find($('tr[id^="datagrid-row-r"]'));
				contentTestCase.empty();
				contentTestCase.append('<label style="height: 40px;width: 900px;margin-top: 8px;margin-left: 16%;text-align: center;">暂无数据!</label>');
			}else{
				for(var i=0; i<data.total;i++){
					packageId_join = packageId_join+data.rows[i].packageId+" ";
				}
				$("#packageIds").val($.trim(packageId_join));
			}
			
			$('.tab-content div[class="panel panel-default datagrid panel-htop"]').eq(2).height(370);
			$('#proTestcase').prev().find($('div[class="datagrid-body"]')).height(282);
			$('.tab-content div[class="panel panel-default datagrid panel-htop"]').eq(2).find($('div[class="datagrid-view"]')).height(320);
		}
	});
}

//"操作"列
/*function operatFormat(value,row,index){
	var columnStr = "<div>" +
                     "<button type='button' class='btn btn-default' id='editRoleInfBtn' onclick='viewTestCase(\""+ row.packageId + "\")' >" +
                     "查看用例" +
                     "</button>"+
		             "</div>";
	return columnStr;
}*/

//关闭弹窗
$('#closeTestCaseWin').click(function(){
	objs.$proTestcase.xdatagrid('reload');
//	objs.$proTestcase.xdatagrid('load', {
//		 'dto.iterationList.iterationId': iterationId
//	});
    $('#iterationTestCaseLayout').xdialog('destroy');
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

//# sourceURL=iterationTestCaseLayout.js