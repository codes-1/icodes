var taskId="";
var currNodeId="";
var rootNodeId;
var switchFlag=0;
var loadCount=0;
var singleFlag=false;
var hshshss = "0";
//var queryFlag=0;
$(function(){
	$.parser.parse();
	getuserPower();
//	caseTree();
//	var currTaksId = $("#currTaksId").val();
	var currTaksId = $("#taksIdmain").val();
//	getTaskCaseNodeCount(currTaksId);
	if(currTaksId!='null'&&currTaksId!=''){
		taskId = currTaksId;
		caseTree();
	}
	/*else{
		caseItems();
	}*/
//	$('#caseTreeDiv').panel({
//		onBeforeOpen:function (){
//			var html='<div><span>测试需求 &nbsp;</span><a id="switchModule" href="#" class="exui-linkbutton" ondblclick="switchModule();"><span>普通视图</span><span class="spancolor">(双击切换)</span></a></div>';
//			$('#caseTreeDiv').panel('setTitle',html);
//		}
//	});
	
//	loadCaseCategory();
//	loadCaseYXJ();
});

//获取当前用户的权限
function getuserPower(){
	var controlButton = $('button[schkUrl]');
	$.each(controlButton,function(i,n){
		var controId = controlButton[i].id;
		var controlUrl = $(controlButton[i]).attr('schkUrl');
		if(privilegeMap[controlUrl]!="1"){
			//$("#"+controlID).removeClass("hide"); 
//			$("[schkurl='"+schkUrl+"']").removeClass("hide");
			//$(":button[schkUrl]").removeClass("hide");  
//			$("#"+controId).hide(); 
			$(controlButton[i]).hide();
		}
	});
}

function switchModule(){
//	var text = $('#switchModule').linkbutton('options').text;
	if(switchFlag==0){
		$('#switchModule').linkbutton({
			 plain:true,
			 text:'<span style="color:#2196f3;">bug用例统计视图</span>'
		 });
		switchFlag=1;
		caseTree();
	}else{
		$('#switchModule').linkbutton({
			 plain:true,
			 text:'<span>普通视图</span><span class="spancolor">(双击切换)</span>'
		 });
		switchFlag=0;
		caseTree();
	}
}

//测试需求
function caseTree(){
	$('#caseTree').xtree({
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
				}else{
					getCaseList(data[0].id);
				}
				var nodeDep = $('#caseTree').xtree('find',data[0].id);  
				if (null != nodeDep && undefined != nodeDep){  
					$('#caseTree').xtree('select',nodeDep.target);  
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
				var nodeDep = $('#caseTree').xtree('find',node.id);  
				if (null != nodeDep && undefined != nodeDep){  
					$('#caseTree').xtree('select',nodeDep.target);  
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
								var node = $('#caseTree').xtree('find', data[c].moduleId);
								var text =  '('+data[c].caseCount+'/'+data[c].scrpCount+')';
								$('#caseTree').xtree('update', {
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
//			caseCountInfo();
		 }, 
		onClick: function(node){
			currNodeId = node.id;
//			getCaseList(node.id);
			getCaseList();
//			caseCountInfo();
//			$(this).xtree('beginEdit',node.target);
		},
		onDblClick:function(node){
//			$(this).xtree('beginEdit',node.target);
		},
		onContextMenu:function(e,node){
			e.preventDefault();
			$(this).xtree('select', node.target);
		}
	});
}

//测试列表
function getCaseList(){
	$("#caseList").xdatagrid({
		url: baseUrl + '/caseManager/caseManagerAction!loadCaseList.action?dto.command=simple'+'&dto.taskId='+taskId+'&dto.currNodeId='+currNodeId,
		method: 'get',
		queryParams:$("#queryWin").xserialize(),
//		height: mainObjs.tableHeight-140,
		height: mainObjs.tableHeight-40,
		singleSelect:false,
//		checkOnSelect:false,
//		selectOnCheck:false,
//		rownumbers: true,
//		scrollbarSize:100,
		fitColumns:true,
		resizeHandle:'right',
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
	    layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],
		pageList: [10,30,50,100],
		showPageList:true,
		columns:[[
			{field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'testCaseId',title:'编号',width:'8%',align:'center'},
			{field:'taskName',title:'项目名称',width:'10%',align:'center',halign:'center'},
			{field:'testCaseDes',title:'用例描述',width:'30%',align:'left',halign:'center',formatter:caseDetail},
			{field:'testStatus',title:'最新状态',width:'10%',align:'center',formatter:testStatusFormat},
			{field:'typeName',title:'类别',width:'5%',align:'center',halign:'center'},
			{field:'priName',title:'优先级',width:'10%',align:'center'},
			/*{field:'auditerNmae',title:'最近处理人',width:'12%',align:'center'},*/
			{field:'authorName',title:'编写人',width:'10%',align:'center'},
			{field:'weight',title:'成本',width:'5%',align:'center'},
			{field:'creatdate',title:'编写日期',width:'11.5%',align:'center'}
		]],
		onLoadSuccess:function(data){

		}
		
	});
	if(!singleFlag){
		$("#caseList").xdatagrid({singleSelect: false});
	}else{
		$("#caseList").xdatagrid({singleSelect: true});
	}
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
	return "<a style=\"cursor: pointer;\" title=\"用例详情：--"+value+"\" href=\"javascript:;\" onclick=\"caseLookWindow('"+row.testCaseId+"')\">" + value + "</a>";
}
//用例历史
function caseHistory(value,row,index) {
	if(value=="" || value=="null" ||value==null){
		value=="--";
	}
	return "<a style=\"cursor: pointer;\" title=\"查看用例历史\" href=\"javascript:;\" onclick=\"caseHistoryList('"+row.testCaseId+"')\">" + value + "</a>";
}

//增加
function caseAddWindow(){
	var selectedData = $('#caseTree').xtree('getSelected');
	if(selectedData.rootNode){
		$.xalert({title:'提示',msg:'请选择非root节点测试需求进行增加测试用例！'});
		$(selectedData.target).tooltip("show");
		setTimeout(function(){
			$(selectedData.target).tooltip("hide");
		}, 3000);
		return;
	}
	var moudleId=selectedData.id;
	 $("<div></div>").xwindow({
    	id:'addOrEditWindown',
    	title:"新增测试用例",
    	width : 900,
        height : 600,
    	modal:true,
    	footer:'#addOrEditFoot',
    	collapsible:false,
		minimizable:false,
		maximizable:false,
    	href:baseUrl + '/caseManager/caseManagerAction!caseAdd.action',
    	queryParams: {
			"moudleId": moudleId,
			"taskId": taskId
    	},
        onClose : function() {
            $(this).xwindow('destroy');
        }
    });
}

//修改
function caseEditWindow(){
	var checkData = $("#caseList").xdatagrid('getSelections');
	if (checkData!=null && checkData.length!=1) {
		$.xalert({title:'提示',msg:'请选择要修改的一条记录！'});
		return;
	}
	var caseId = checkData[0].testCaseId;
	 $("<div></div>").xwindow({
    	id:'editCaseWindown',
    	title:"修改测试用例",
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
    			'operaType':'edit',
    			'flag':'',
    			'testcasepkgId':''
    		},
        onClose : function() {
            $(this).xwindow('destroy');
        }
    });
}

//执行
function caseExecWindow(){
	var checkData = $("#caseList").xdatagrid('getSelections');
	if (checkData!=null && checkData.length!=1) {
		$.xalert({title:'提示',msg:'请选择要执行的一条记录！'});
		return;
	}
	var caseId = checkData[0].testCaseId;
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
    			'flag':'',
    			'testcasepkgId':''
    			
    		},
        onClose : function() {
            $(this).xwindow('destroy');
        }
    });
}

//查看
function caseLookWindow(testCaseId){
	 $("<div></div>").xwindow({
    	id:'lookCaseWindown',
    	title:"测试用例详情",
    	width : 900,
        height : 600,
    	modal:true,
    	footer:'',
    	collapsible:false,
		minimizable:false,
		maximizable:false,
    	href:baseUrl + '/caseManager/caseManagerAction!caseLook.action',
    	queryParams: {
			'caseId':testCaseId
		},
        onClose : function() {
            $(this).xwindow('destroy');
        }
    });
}

//打开删除确认弹窗
function delConfirm() {
	var checkData = $("#caseList").xdatagrid('getSelections');
	
	if (checkData!=null && checkData.length==0) {
		$.xalert({title:'提示',msg:'请选择要删除的记录！'});
		return;
	}
	var caseIds = [];
	for(var i=0;i<checkData.length;i++){
		caseIds.push(checkData[i].testCaseId);
	}
	/*checkData[0].testCaseId;*/
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			$.post(
					baseUrl + "/caseManager/caseManagerAction!delCase.action",
				{'dto.caseIds': caseIds.toString()},
				function(data) {
					if (data == "success") {
//						$.xnotify("删除成功！", {type:'success'});
						$.xalert({title:'提示',msg:'删除成功！'});
						getCaseList(currNodeId);
					} else {
//						$.xnotify(data, {type:'warning'});
						$.xalert({title:'提示',msg:data});
						getCaseList(currNodeId);
					}
				}, "text");
		}
		
	});
	
}


document.getElementById('caseId').onkeydown=function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0]; 
    if (e && e.keyCode == 13) {//keyCode=13是回车键；数字不同代表监听的按键不同
    	 findCase("caseId");
    }
};

document.getElementById('case_Id').onkeydown=function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0]; 
    if (e && e.keyCode == 13) {//keyCode=13是回车键；数字不同代表监听的按键不同
    	 findCase("case_Id");
    }
};

//enter事件
function onKeyEnter(target){
//	var lKeyCode = (navigator.appname=="Netscape")?event.which:window.event.keyCode;
	e = event ? event :(window.event ? window.event : null);
	var lKeyCode=0;
	lKeyCode=e.keyCode||e.which||e.charCode;

	if ( lKeyCode == 13 ){
		var caseId = $("#"+target).val();
		if(caseId){
			$.get(
			baseUrl + "/caseManager/caseManagerAction!quickQuery.action",
			{
				'dto.testCaseInfo.testCaseId': caseId,
//				'dto.testCaseInfo.moduleId': currNodeId,
				'dto.testCaseInfo.taskId': taskId
			},
			function(data,status,xhr){
				if(status=='success'){
					if(data!=null && data!=""){
						$("#caseList").xdatagrid('loadData',data);
					}else{
						 $.xalert({title:'提示',msg:'没有查到相关记录'});
					}
					setTimeout(function(){
						$("#"+target).val("");
					}, 2000);
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			},
			"json");
		}
     }
}

function findCase(target){
	var caseId = $("#"+target).val();
	if(caseId){
		$.get(
		baseUrl + "/caseManager/caseManagerAction!quickQuery.action",
		{
			'dto.testCaseInfo.testCaseId': caseId,
//			'dto.testCaseInfo.moduleId': currNodeId,
			'dto.testCaseInfo.taskId': taskId
		},
		function(data,status,xhr){
			if(status=='success'){
				if(data!=null && data!=""){
					$("#caseList").xdatagrid('loadData',data);
				}else{
					 $.xalert({title:'提示',msg:'没有查到相关记录'});
				}
				setTimeout(function(){
					$("#"+target).val("");
				}, 2000);
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},
		"json");
	}
}


//重置
function clearInput(){
//	$("#caseId").val("");
//	$("#case_Id").val("");
//	clearQueryForm();
	getCaseList();
}

//查询
function quickQuery(){
	loadCaseCategory();
	loadCaseYXJ();
	var selectedData = $('#caseTree').xtree('getSelected');
	$("#queryWin").window({title:"查询--》当前测试需求："+selectedData.text});
	$("#queryWin").xwindow('open');
	
}
//查询提交
function querySubmit(){
	getCaseList();
	$("#queryWin").xwindow('close');
//	$.get(
//		baseUrl + '/caseManager/caseManagerAction!loadCaseList.action?dto.taskId='+taskId+'&dto.currNodeId='+currNodeId,
//		$("#queryWin").xserialize(),
//		function(data,status,xhr){
//			if(status=='success'){
//				if(data!=null || data!=""){
//					$("#caseList").xdatagrid('loadData',data);
//					$("#queryWin").xwindow('close');
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
function clearQueryForm(){
	$("#queryWin").xform('clear');
	createrIds = new Array();
	createrNames = new Array();
	auditIds = new Array();
	auditNames = new Array();
}
//返回查询框
function closeQueryWin(){
	$("#queryWin").xwindow('close');
}

//用例历史
function caseHistoryList(testCaseId){
	$("#caseHistoryList").xdatagrid({
		url: baseUrl + '/caseManager/caseManagerAction!viewCaseHistory.action?dto.testCaseInfo.testCaseId='+testCaseId,
		method: 'get',
//		height: mainObjs.tableHeight,
		fitColumns:true,
		singleSelect:true,
		pagination: true,
		pageNumber: 1,
		pageSize: 16,
		pageList: [16,30,50,100],
		layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],
		onLoadSuccess:function(data){
			$("#caseHistoryList").xdatagrid('resize');
		},
		rowStyler: function(index,row){
			if (row.status=='0'){
//				return 'background-color:#bddecc;color:#fff;font-weight:bold;';
//				return 'background-color:#e5fff1;';
			}
		},
		columns:[[
			{field:'testActorName',title:'处理人',width:'15%',align:'center',formatter:function(value,row,index){
				if(value=="" || value=="null" || value ==null){
					return " ";
				}else{
					return "<p title=\""+value+"\">" + value + "</p>";
				}
			}},
			{field:'operaType',title:'操作类型',width:'10%',align:'center',formatter:operaTypeFormat},
			{field:'testResult',title:'用例状态',width:'10%',align:'center',formatter:convCase},
			{field:'testVerNum',title:'版本',width:'15%',align:'center',},
			{field:'exeDate',title:'处理日期',width:'25%',align:'center'},
			{field:'remark',title:'备注',width:'25%',align:'center',formatter:function(value,row,index){
				if(value=="" || value=="null" || value ==null){
					return "";
				}else{
					return "<p title=\""+value+"\">" + value + "</p>";
				}
			}}
		]]
	});
	$("#caseHistoryWin").window({title:"用例历史列表"});
	$("#caseHistoryWin").window("vcenter");
	$("#caseHistoryWin").window("open");
	$("#caseHistoryList").xdatagrid('resize');
}

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

//实例统计信息
function caseCountInfo(){
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
}

function proNameFormat(value,row,index) {
	return "<a style=\"cursor: pointer;\" title=\""+value+"\" href=\"javascript:;\" onclick=\"changeTask('"+row.taskId+"')\">" + value + "</a>";
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

function psmNameFormat(value,row,index) {
	return '<a href="javascript:;" title="不纳入项目管理的独立测试项目--'+value+'">' + value + '</a>';
}

function planDocNameFormat(value,row,index) {
	if (value) {
		return '<a href="javascript:;">@</a>';
	}
}

function statusFormat(value,row,index) {
	switch (value) {
	case 0:
		return '进行';
	case 1:
		return '完成';
	case 2:
		return '结束';
	case 3:
		return '准备';
	default:
		return '-';
	}
}

function psmNameFormat(value,row,index) {
	var html = new Array();
	if (row.testLdVos.length) {
		for (var i = 0; i < row.testLdVos.length; i++) {
			html.push(row.testLdVos[i].loginName + '(' + row.testLdVos[i].name + ')');
		}
	}
	return html.join(',');
}

//推荐测试用例到用例库
function toLibrary(){
	var row = $("#caseList").xdatagrid('getSelections');
	if (row.length == 0) {
		$.xalert({title:'提示',msg:'请选择要推荐到用例库的用例！'});
		return;
	}
	$("#toLibraryTree").xtree({
	    url:baseUrl+'/testLibrary/testLibraryAction!loadTreeList.action',
	    method:'post',
		animate:true,
		lines:true,
		formatter:function(node){
			return node.testcaseType;
		},
		onLoadSuccess:function(node, data){
			if(data.length == 0){
				$.xalert({title:'提示',msg:'请联系管理员维护用例类别！'});
				return;
			}else{
				$(".toLibraryReason").val("");
				$("#toLibraryWin").xwindow('setTitle','推荐到测试用例库').xwindow('open');
			}
		}
	});
}
//关闭推荐到测试用例库弹窗
function closeLibraryWin(){
	$("#toLibraryWin").xwindow('close');
}
//确定推荐到测试用例库
function sureToLibrary(){
	var rows = $("#caseList").xdatagrid('getSelections');
	var caseIds = [];
	for(var i=0;i<rows.length;i++){
		caseIds.push(rows[i].testCaseId);
	}
	
	var selectedData = $('#toLibraryTree').xtree('getSelected');
	if(!selectedData || !$(".toLibraryReason").val()){
		$.xalert({title:'提示',msg:'请选择用例类型，填写推荐理由！'});
		return;
	}
	$.post(baseUrl+'/testLibrary/testLibraryAction!saveTestLibraryDetails.action',{
		'dto.libraryId':selectedData.libraryId,
		'dto.libraryCode':selectedData.libraryCode,
		'dto.caseIdS':caseIds.toString(),
		'dto.recommendUserId':$("#loginName").text(),
		'dto.recommendReason':$(".toLibraryReason").val()
	},function(data){
		if(data == "success"){
			$("#toLibraryWin").xwindow('close');
			$.xalert({title:'提示',msg:'推荐成功！'});
		}
	},'text');
}
//打开导入弹窗
function upload(){
	$("#uploadWin").xwindow('setTitle','导入').xwindow('open');
}
//关闭导入弹窗
function closeUploadWin(data){
	$("#uploadWin").xwindow('close');
	if(data.split("<pre>")[1].split("</pre>")[0] != "success"){
		$.xalert({title:'提示',msg:data.split("<pre>")[1].split("</pre>")[0]});
	}else{
		$.xalert({title:'提示',msg:"导入成功！"});
	}
}
//打开从测试用例库导入测试用例弹窗
function uploadFromLibrary(){
	var nnode = $('#caseTree').xtree("getSelected");
	if(!nnode.moduleNum){
		$.xalert({title:'提示',msg:'请选择非根节点需求！'});
		return;
	}
	$("#uploadFromLibraryWin").xwindow('setTitle','从测试用例库导入').xwindow('open');
	loadCaseTable("");
	$('#uploadFromLibraryTree').xtree({
		url: baseUrl + '/testLibrary/testLibraryAction!loadTreeList1.action',
	    method:'post',
		animate:true,
		lines:true,
		formatter:function(node){
			return node.testcaseType;
		},
		onLoadSuccess:function(node,data){  
			$("#uploadFromLibraryTree li:eq(0)").find("div").addClass("tree-node-selected");   //设置第一个节点高亮  
            var n = $("#uploadFromLibraryTree").xtree("getSelected");  
            if(n!=null){  
                $("#uploadFromLibraryTree").xtree("select",n.target);    //相当于默认点击了一下第一个节点，执行onSelect方法  
            }
		}, 
		onClick: function(node){
			loadCaseTable(node.libraryCode);
		},
		onDblClick:function(node){
//			$(this).xtree('beginEdit',node.target);
		},
		onContextMenu:function(e,node){
			
		}
	});
}
//显示测试用例类型节点用例
function loadCaseTable(libraryCode){
	$("#uploadFromLibraryCaseList").xdatagrid({
		url: baseUrl + '/testLibrary/testLibraryAction!testCaseDetailLoad.action',
		method: 'post',
		queryParams: {
			'dto.testCaseLibraryDetail.libraryCode':libraryCode,
			'dto.testCaseLibraryDetail.examineStatus':'1'
		},
		emptyMsg:"无数据",
		columns:[[
		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'testCaseDes',title:'用例描述',width:"96%",align:'left',formatter:testCaseDesFormat},
			/*{field:'recommendReason',title:'推荐理由',width:"32%",align:'left',formatter:recommendReasonFormat},
			{field:'recommendUserId',title:'推荐人',width:"32%",align:'center'}*/
		]],
		onLoadSuccess : function (data) {								
			
		}
	});
}

function testCaseDesFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,50)+"</span>";
}

/*function recommendReasonFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,10)+"....</span>";
}*/
//确认导入测试用例（从测试用例库）
function agreeToUpload(){
	var nnode = $('#caseTree').xtree("getSelected");
	var rows = $("#uploadFromLibraryCaseList").xdatagrid('getSelections');
	if (rows.length == 0) {
		$.xalert({title:'提示',msg:'请选择需要导入的用例！'});
		return;
	}
	var testCaseIds = [];
	for(var i=0;i<rows.length;i++){
		testCaseIds.push(rows[i].number);
	}
	$.post(baseUrl + '/testLibrary/testLibraryAction!addTestLibraryDetailsToCaseInfo.action',{
		'dto.numbers':testCaseIds.toString(),
		'dto.moduleId':nnode.id,
		'dto.moduleNum':nnode.moduleNum,
		'dto.createrId':$("#accountId").text(),
		'dto.taskId':taskId
	},function(data){
		if(data == "success"){
			disagreeToUpload();
			//closeUploadWin("导入成功");
			$("#uploadWin").xwindow('close');
			$.xalert({title:'提示',msg:"导入成功！"});
			$("#caseList").xdatagrid("reload");
			//$.xalert({title:'提示',msg:'导入成功！'});
		}
	},'text');
}
//关闭导入测试用例弹窗（从测试用例库）
function disagreeToUpload(){
	$("#uploadFromLibraryWin").xwindow('close');
}
//打开从excel导入测试用例弹窗
function uploadFromExcel(){
	hshshss = "0";
	$("#uploadForm input").val("");
	$("#uploadFromExcelWin").xwindow('setTitle','导入').xwindow('open');
	$("#importFile").fileinput("destroy");
	$("#importFile").fileinput({
		theme: 'fa',
		language:'zh',
		uploadUrl:baseUrl + '/impExpMgr/caseImpAction!impCase.action', // you must set a valid URL here else you will get an error
		allowedFileExtensions: ['xlsx', 'xls'],
		showPreview:false,
		showClose:false,
		overwriteInitial: true,
		uploadAsync:false,
		autoReplace: true,
		showUploadedThumbs:false,
		maxFileSize: 6000,
		maxFileCount:1,
		imageMaxWidth : 200,
		imageMaxHeight : 100,
		enctype: 'multipart/form-data',
		//msgFilesTooMany :"选择图片超过了最大数量", 
		showRemove:false,
		showClose:false,
		showUpload:false,
		showDownload: false,
		allowedPreviewTypes:['xlsx', 'xls'],
		dropZoneEnabled:false,
		initialPreviewAsData: false,
		/*slugCallback: function (filename) {
		  return filename.replace('(', '_').replace(']', '_');
		}*/
	}).on("filebatchselected", function(event, files) {
		//选择文件后处理
		//$(this).fileinput("upload");
	}).on('filebatchuploadsuccess', function(event, data, previewId, index) {
		if(hshshss == "0"){
			$("#uploadFromExcelWin").xwindow('close');
			closeUploadWin(previewId);
			caseTree();
			hshshss = "1";
		}
	}).on('filebatchuploaderror', function(event, data,  previewId, index) {
		if(hshshss == "0"){
			$("#uploadFromExcelWin").xwindow('close');
			closeUploadWin(previewId);
			caseTree();
			hshshss = "1";
		}
    	//$.xalert({title:'提示',msg:'导入成功！'});
	}).on('filepreupload', function(event, data, previewId, index) {    
	}).on('fileerror', function(event, data, msg) {
		/*$("#uploadFromExcelWin").xwindow('close');
		closeUploadWin();
		caseTree();
    	$.xalert({title:'提示',msg:'导入成功！'});*/
	}).on("fileuploaded", function(event, data, previewId, index) {
		//上传成功后处理方法
		/*$("#uploadFromExcelWin").xwindow('close');
		closeUploadWin();
		caseTree();
    	$.xalert({title:'提示',msg:'导入成功！'});*/
	});
}
//关闭从excel导入测试用例弹窗
function closeUploadFromExcelWin(){
	$("#uploadForm input").val("");
	$("#uploadFromExcelWin").xwindow('close');
}
//选择excel文件事件监听
/*function fileChange(obj) {
	$("#excelField").val($(obj).val());
}*/
//确认导入（Excel）
function sureToUpload(){
	if(!$(".file-caption-name").val()){
		$.xalert({title:'提示',msg:'请选择需要导入的Excel文件！'});
		return;
	}
	$("#importFile").fileinput("upload");
	/*$("#uploadForm").ajaxSubmit({
		type: 'post',
		url: baseUrl + '/impExpMgr/caseImpExpAction!impCase.action',
		data: '#uploadForm',
		dataType: 'json',
		beforeSubmit: function() {
			
		},
		success: function(data) {
			$("#uploadFromExcelWin").xwindow('close');
			closeUploadWin();
			caseTree();
	    	$.xalert({title:'提示',msg:'导入成功！'});
		},
		error: function () {
			$("#uploadFromExcelWin").xwindow('close');
			closeUploadWin();
			caseTree();
	    	$.xalert({title:'提示',msg:'导入成功！'});
        }
	});*/
}
//导出用例
function downloadd(obj){
	var nnode = $('#caseTree').xtree("getSelected");
	var href = baseUrl + '/impExpMgr/caseImpExpAction!expCase.action?dto.currNodeId='+nnode.id+'&dto.taskId='+taskId+'dto.command=simple';
	var objData = $("#queryWin").xserialize();
	if(objData["dto.testCaseInfo.caseTypeId"]){
		href = href + '&dto.testCaseInfo.caseTypeId='+objData["dto.testCaseInfo.caseTypeId"];
	}
	if(objData["dto.testCaseInfo.priId"]){
		href = href + '&dto.testCaseInfo.priId='+objData["dto.testCaseInfo.priId"];
	}
	if(objData["dto.testCaseInfo.status"]){
		href = href + '&dto.testCaseInfo.status='+objData["dto.testCaseInfo.status"];
	}
	if(objData["dto.testCaseInfo.createrId"]){
		href = href + '&dto.testCaseInfo.createrId='+objData["dto.testCaseInfo.createrId"];
	}
	if(objData["dto.testCaseInfo.auditId"]){
		href = href + '&dto.testCaseInfo.auditId='+objData["dto.testCaseInfo.auditId"];
	}
	if(objData["dto.testCaseInfo.testCaseDes"]){
		href = href + '&dto.testCaseInfo.testCaseDes='+objData["dto.testCaseInfo.testCaseDes"];
	}
	if(objData["dto.testCaseInfo.weight"]){
		href = href + '&dto.testCaseInfo.weight='+objData["dto.testCaseInfo.weight"];
	}else{
		href = href + '&dto.testCaseInfo.weight=';
	}
	$(obj).prop('href', href);
}
//下载Excel导入摸板
function downloadExcelModel(obj){
	var href = baseUrl + '/documents/caseimp_07.xlsx';
	$(obj).prop('href', href);
}

//加载用例类别列表
function loadCaseCategory(){
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!loadTestBaseSetList.action",
			{
				"page": 1,
				"rows": 80,
				"dto.subName": "用例类型",
				"dto.flag": "1"
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
				"dto.subName": "用例优先级",
				"dto.flag": "1"
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
//# sourceURL=testCaseManager.js