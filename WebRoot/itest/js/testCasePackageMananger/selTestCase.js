var packageId = '';
var taskId="";
var currNodeId="";
var rootNodeId;
var switchFlag=0;
var loadCount=0;

//获取页面url参数
function getQueryParam(name) {
       var obj = $('#testCaseListDlg').dialog('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

$(function(){
	$.parser.parse();
    taskId = $("#taksIdmain").val();
	packageId = getQueryParam('testCasePackageId');
	caseTree(packageId);
});

//测试需求
function caseTree(){
	$('#selCaseTree').xtree({
	    url:baseUrl+'/caseManager/caseManagerAction!loadTree.action?dto.taskId='+taskId+'&dto.command=',
	    method:'get',
		animate:true,
		lines:true,
//		dnd:true,
		onLoadSuccess:function(node,data){  
			var mid="";
			if(node==null){
				currNodeId=rootNodeId = data[0].id;
				if(loadCount==0){
					getCaseList(data[0].id);
					loadCount=1;
				}
				var nodeDep = $('#selCaseTree').xtree('find',data[0].id);  
				if (null != nodeDep && undefined != nodeDep){  
					$('#selCaseTree').xtree('select',nodeDep.target);  
				}  
				$(nodeDep.target).tooltip({
				    position: 'right',
				    content: '<span style="color:#fff">root节点测试需求，不能增加测试用例.</span>',
				    hideDelay:100,
				    onShow: function(){
						$(this).tooltip('tip').css({
							backgroundColor: '#666',
							borderColor: '#666'
						});
				    }
				});
				mid = data[0].id;
				if(data[0].children!=null){
					for ( var it in data[0].children) {
						mid+='_'+data[0].children[it].id;
					}
				}
				
			}else{
				var nodeDep = $('#selCaseTree').xtree('find',node.id);  
				if (null != nodeDep && undefined != nodeDep){  
					$('#selCaseTree').xtree('select',nodeDep.target);  
				}  
				
				for ( var it in data) {
					mid+='_'+data[it].id;
				}
			}
			
			if(switchFlag==1){
				$.get(
					baseUrl + "/caseManager/caseManagerAction!loadNodedetalData.action",
					{'dto.remark': 'onlyNormal',
					 'dto.countStr':mid
					},
					function(data,status,xhr){
						if(status=='success'){
							for ( var c in data) {
								var node = $('#selCaseTree').xtree('find', data[c].moduleId);
								var text =  '('+data[c].caseCount+'/'+data[c].scrpCount+')';
								$('#selCaseTree').xtree('update', {
									target: node.target,
									text: '<span title="斜杠前后为用例数和bug数">'+node.text+text+'</span>'
								});
							}
						}else{
							$.xalert({title:'提示',msg:'系统错误！'});
						}
					},
				"json");
			}
			
			//获取bug信息
		/*	caseCountInfo();*/
		 }, 
		onClick: function(node){
			currNodeId = node.id;
			getCaseList(node.id);
			/*caseCountInfo();*/
		},
		onContextMenu:function(e,node){
			e.preventDefault();
			$(this).xtree('select', node.target);
		}
	});
}
//测试列表
function getCaseList(currNodeId){
	$("#selCaseList").xdatagrid({
		url: baseUrl + '/caseManager/caseManagerAction!loadCaseList.action?dto.command=simple'+'&dto.taskId='+taskId+'&dto.currNodeId='+currNodeId,
		method: 'get',
		height: mainObjs.tableHeight-140,
//		rownumbers: true,
		scrollbarSize:100,
		fitColumns:true,
		singleSelect:false,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		idField:'testCaseId',
	    layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],
		pageList: [10,30,50,100],
		showPageList:true,
		columns:[[
			{field:'',title:'选择',checkbox:true,align:'center'},
			{field:'testCaseId',title:'编号',width:'5%',align:'center'},
			{field:'taskName',title:'项目名称',width:'10%',align:'center',halign:'center'},
			{field:'testCaseDes',title:'用例描述',width:'30%',align:'left',halign:'center',formatter:caseDetail},
			{field:'testStatus',title:'最新状态',width:'10%',align:'center',formatter:testStatusFormat},
			{field:'typeName',title:'类别',width:'5%',align:'left',halign:'center'},
			{field:'priName',title:'优先级',width:'10%',align:'center'},
			{field:'auditerNmae',title:'最近处理人',width:'12%',align:'center'},
			{field:'authorName',title:'编写人',width:'10%',align:'center',formatter:caseHistory},
			{field:'weight',title:'成本',width:'5%',align:'center'},
			{field:'creatdate',title:'编写日期',width:'12%',align:'left'}
		]],
		onLoadSuccess : function (data) {								
			if (data.total==0) {
				$('#selCaseList').parent().find(".datagrid-view2 .datagrid-body").append('<div style="font-size:16px; text-align: center;">暂无数据</div>');
			}
			checkedSelectedTestCase();

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
		return '-';
	}
}

//用例详情
function caseDetail(value,row,index) {
	return "<a style=\"cursor: default;color:#000\" title=\"用例详情：--"+value+"\" href=\"javascript:;\">" + value + "</a>";
}
//用例历史
function caseHistory(value,row,index) {
	if(value=="" || value=="null" ||value==null){
		value=="--";
	}
	return "<span style=\"cursor: default\" title=\"" + value + "\" href=\"javascript:;\" >" + value + "</span>";
}

function operaTypeFormat(value,row,index){
	switch (value) {
	case 0:
		return '待审核';
	case 1:
		return '未测试';
	case 2:
		return '修改';
	case 3:
		return '未通过';
	case 4:
		return '执行';
	case 5:
		return '阻塞';
	case 6:
		return '待修正';
	default:
		return '-';
	}
}

//显示关联列表时如果有已选的关联用例，则显示选中关联用例
function checkedSelectedTestCase() {
	$.get(
			baseUrl +  '/testCasePkgManager/testCasePackageAction!getSelTestCasesByPkgId.action',
			{"dto.testCasePackage.packageId" : packageId},
			function(data,status,xhr){
				if(data.length > 0){
					data.map(function(value,index){
						$("#selCaseList").xdatagrid("selectRecord",value.testCaseId);
					});
				}
			},"json");

}

//实例统计信息
/*function caseCountInfo(){
	$.get(
			baseUrl + "/caseManager/caseManagerAction!getCaseStatInfo.action",
			{'dto.currNodeId': currNodeId},
			function(data,status,xhr){
				if(status=='success'){
					$("#caseCountInfo").text(data);
					$("#caseCountDiv").show();
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			},"json");
}*/

function submitSelTestCase(){
	var selItems = 	$("#selCaseList").xdatagrid('getSelections');
/*	if(selItems.length <= 0){
		$.xalert({title:'提示',msg:'请选择测试用例！'});
		return;
	}else{*/
		selTestCaseIds = selItems.map(function(value){
			return value.testCaseId
		})
/*	}*/
	
	$.post(
			baseUrl + "/testCasePkgManager/testCasePackageAction!saveTestCase_CasePkg.action",
			{"dto.selectedTestCaseIds": selTestCaseIds.toString(),
			 "dto.testCasePackage.packageId":packageId},
			function(dataObj) {
				if(dataObj.indexOf("success") === -1){
					$.xalert({title:'提示',msg:'保存失败，请稍后再试！'});
				}else{
					$.xalert({title:'提示',msg:'保存成功'});
					$('#testCaseListDlg').dialog('destroy');
				}
			},"text"
		);
}

function closeSelTestCaseWin(){
	 $('#testCaseListDlg').dialog('destroy');
}

//@ sourceURL=selTestCase.js