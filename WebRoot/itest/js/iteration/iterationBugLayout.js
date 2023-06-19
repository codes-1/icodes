var taskId="";
var currNodeId="";
var rootNodeId;
var switchFlag=0;
var loadCount=0;
var iterationId="";
var bugIds="";
//获取页面url参数
function getQueryParam(name) {
       var obj = $('#iterationBugLayout').dialog('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

$(function(){
	$.parser.parse();
	taskId = getQueryParam('taskBugId');
	iterationId = getQueryParam('iterationListId');
	bugIds = getQueryParam('bugData');
	proBugTree();
});

//项目bug
function proBugTree(){
	$('#proBugTrees').xtree({
//	    url:baseUrl+'/caseManager/caseManagerAction!loadTree.action?dto.taskId='+taskId+'&dto.command=',
	    url:baseUrl + '/bugManager/bugManagerAction!loadLeftTree.action?dto.taskId='+taskId,
	    method:'get',
		animate:true,
		lines:true,
//		dnd:true,
		onLoadSuccess:function(node,data){  
			if(node==null){
				currNodeId=rootNodeId = data[0].id;
				getPorBugList(data[0].id);
			}
			
			var nodeDep = $('#proBugTrees').xtree('find',data[0].id);  
			if (null != nodeDep && undefined != nodeDep){  
				$('#proBugTrees').xtree('select',nodeDep.target);  
			}  
		 }, 
		onClick: function(node){
			currNodeId = node.id;
			getPorBugList(node.id);
		},
		onContextMenu:function(e,node){
			e.preventDefault();
			$(this).xtree('select', node.target);
		}
	});
}
//项目bug列表
function getPorBugList(currNodeId){
	$("#ProBugList").xdatagrid({
		url:baseUrl + '/bugManager/bugManagerAction!findBug.action',
		method: 'post',
		height: mainObjs.tableHeight-140,
		queryParams:{
			'dto.taskId':taskId,
			'dto.moduleName':'treeView',
			'dto.bug.moduleId':currNodeId,
			'dto.bugJoinId':bugIds
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
					{field:'bugId',title:'编号',width:'5%',align:'center',formatter:bugDetail},
					{field:'bugDesc',title:'bug描述',width:'30%',align:'left',halign:'center',formatter:bugTitle},
					{field:'stateName',title:'状态',width:'10%',align:'center',formatter:bugHand},
					{field:'typeName',title:'等级',width:'5%',align:'left',halign:'center',
						formatter:function(value,row,index){
							if(row.bugGrade!=null){
								return row.bugGrade.typeName;
							}
						}
					},
					{field:'auditerNmae',title:'时机',width:'10%',align:'center',
						formatter:function(value,row,index){
							if(row.bugOpotunity!=null){
								return row.bugOpotunity.typeName;
							}
						}
					},
					{field:'authorName',title:'类型',width:'5%',align:'center',
						formatter:function(value,row,index){
							if(row.bugType!=null){
								return row.bugType.typeName;
							}
						}
					},
					{field:'weight',title:'优先级',width:'5%',align:'center',
						formatter:function(value,row,index){
							if(row.bugPri){
								return row.bugPri.typeName;
							}
						}
					},
					{field:'testName',title:'测试人员',width:'10%',align:'center'},
					{field:'devName',title:'开发人员',width:'14%',align:'center',formatter:developer},
					{field:'reptDate',title:'报告日期',width:'13%',align:'left'}
				]],
		onLoadSuccess : function (data) {								
			if (data.total==0) {
				$('#ProBugList').parent().find(".datagrid-view2 .datagrid-body").append('<div style="font-size:16px; text-align: center;">暂无数据</div>');
			}
		},
		onLoadError: function(){
			$('#ProBugList').parent().find(".datagrid-view2 .datagrid-body").empty();
			$('#ProBugList').parent().find(".datagrid-view2 .datagrid-body").append('<div style="font-size:16px; text-align: center;">暂无数据</div>');
		}
	});
}

//开发人员
function developer(value,row,index){
	if(value==null || value=="null"){
		return "";
	}else{
		return "<span>" + value + "</span>";
	}
}

//bug详情
function bugDetail(value,row,index) {
	return "<span>" + value + "</span>";
}

function bugTitle(value,row,index) {
	return "<p style=\"cursor: pointer;margin:0;\" title=\"bug详情：--"+value+"\">" + value + "</p>";
}
//bug处理
function bugHand(value,row,index) {
	return "<span>" + value + "</span>";
}

function submitIteraBug(){
	var selItems = 	$("#ProBugList").xdatagrid('getSelections');
	if(selItems.length <= 0){
		$.xalert({title:'提示',msg:'请选择项目bug！'});
//		tip("请选择项目bug！");
		return;
	}else{
		selTestCaseIds = selItems.map(function(value){
			return value.bugId;
		});
	}
	
	$.post(
			baseUrl + "/iteration/iterationAction!saveBugReal.action",
			{"dto.iterationList.iterationId": iterationId,
			 "dto.bugCardId":selTestCaseIds.toString()},
			function(dataObj) {
				if(dataObj.indexOf("success") == -1){
					$.xalert({title:'提示',msg:'保存失败，请稍后再试！'});
//					tip("保存失败，请稍后再试！");
				}else{
					$.xalert({title:'提示',msg:'保存成功!',okFn:function(){
						$('#iterationBugLayout').dialog('destroy');
						getBugListSa();//获取得到关联的项目bug
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
			$('#iterationBugLayout').dialog('destroy');
			getBugListSa();//获取得到关联的项目bug
		}
	});
}

function getBugListSa(){
	objs.$proBug.xdatagrid({
		url:  baseUrl + '/iteration/iterationAction!searchBugDetail.action',
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
					{field:'bugId',title:'编号',width:'5%',align:'center',formatter:bugDetails},
					{field:'bugDesc',title:'bug描述',width:'30%',align:'left',halign:'center',formatter:bugTitles},
					{field:'stateName',title:'状态',width:'10%',align:'center',formatter:bug_Hands},
					{field:'typeName',title:'等级',width:'5%',align:'left',halign:'center',
						formatter:function(value,row,index){
							if(row.bugGrade!=null){
								return row.bugGrade.typeName;
							}
						}
					},
					{field:'auditerNmae',title:'时机',width:'10%',align:'center',
						formatter:function(value,row,index){
							if(row.bugOpotunity!=null){
								return row.bugOpotunity.typeName;
							}
						}
					},
					{field:'authorName',title:'类型',width:'5%',align:'center',
						formatter:function(value,row,index){
							if(row.bugType!=null){
								return row.bugType.typeName;
							}
						}
					},
					{field:'weight',title:'优先级',width:'5%',align:'center',
						formatter:function(value,row,index){
							if(row.bugPri){
								return row.bugPri.typeName;
							}
						}
					},
					{field:'testName',title:'测试人员',width:'10%',align:'center'},
					{field:'devName',title:'开发人员',width:'14%',align:'center',formatter:developers},
					{field:'reptDate',title:'报告日期',width:'13%',align:'left'}
				]],
		onLoadSuccess : function (data) {	
			var bugIdJ="";
			if (data.total==0) {
				var contentTestCase = $('div[class="tab-content"]').children().eq(2).find($('tr[id^="datagrid-row-r"]'));
				contentTestCase.empty();
				contentTestCase.append('<label style="height: 40px;width: 900px;margin-top: 8px;margin-left: 16%;text-align: center;">暂无数据!</label>');
			}else{
				for(var i=0; i<data.total;i++){
					bugIdJ = bugIdJ+data.rows[i].bugId+" ";
				}
				$("#bugIds").val("");
				$("#bugIds").val($.trim(bugIdJ));
			}
			
			$('.tab-content > div[class="panel panel-default datagrid panel-htop"]').height(370);
			$('#proBug').prev().find($('div[class="datagrid-body"]')).height(282);
			$('.tab-content > div[class="panel panel-default datagrid panel-htop"]').find($('div[class="datagrid-view"]')).height(320);
			$('#iterationAccordion').find('div[class="panel-body accordion-body"]:eq(0)').height(470);
		}
	});
}

//关闭弹窗
$('#closeBugWin').click(function(){
	objs.$proBug.xdatagrid('reload');
    $('#iterationBugLayout').xdialog('destroy');
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

//# sourceURL=iterationBugLayout.js