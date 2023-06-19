$(function() {
	$.parser.parse();
	//加载测试用例类型数据
	initCaseTypeTree();
});
//加载测试用例类型数据
function initCaseTypeTree(){
	$('#caseTypeTree').xtree({
		url: baseUrl + '/testLibrary/testLibraryAction!loadTreeList1.action',
	    method:'post',
		animate:true,
		lines:true,
		formatter:function(node){
			return node.testcaseType;
		},
		onLoadSuccess:function(node,data){  
			$("#caseTypeTree li:eq(0)").find("div").addClass("tree-node-selected");   //设置第一个节点高亮  
            var n = $("#caseTypeTree").xtree("getSelected");  
            if(n!=null){  
                $("#caseTypeTree").xtree("select",n.target);    //相当于默认点击了一下第一个节点，执行onSelect方法  
            }  
			initTable("");
		}, 
		onClick: function(node){
			initTable(node.libraryCode);
		},
		onDblClick:function(node){
//			$(this).xtree('beginEdit',node.target);
		},
		onContextMenu:function(e,node){
			
		}
	});
}

//显示所有用例
function initTable(libraryCode){
	$("#caseList").xdatagrid({
		url: baseUrl + '/testLibrary/testLibraryAction!testCaseDetailLoad.action',
		method: 'post',
		queryParams: {
			'dto.testCaseLibraryDetail.libraryCode':libraryCode,
			'dto.testCaseLibraryDetail.examineStatus':'0'
		},
		/*height: mainObjs.tableHeight,*/
		emptyMsg:"无数据",
		columns:[[
		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'testCaseDes',title:'用例描述',width:"18%",align:'left',formatter:testCaseDesFormat},
			{field:'recommendReason',title:'推荐理由',width:"16%",align:'left',formatter:recommendReasonFormat},
			{field:'recommendUserId',title:'推荐人',width:"16%",align:'center'},
			{field:'libraryId',title:'推荐类别',width:"16%",align:'center',formatter:libraryIdFormat},
			{field:'examineStatus',title:'审核状态',width:"16%",align:'center',formatter:examineStatusFormat},
			{field:'examineUserId',title:'审核人',width:"16%",align:'center'},
		]],
		onLoadSuccess : function (data) {		
			$("#caseLayout").xlayout('resize');
			$("#caseList").xdatagrid('resize');
		}
	});
}

function testCaseDesFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,10)+"....</span>";
}

function recommendReasonFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,10)+"....</span>";
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
function examineStatusFormat(value,row,index){
	if(value == "0"){
		return "未审核";
	}else if(value == "1"){
		return "通过";
	}else{
		return "";
	}
}
//通过审核
function agreeLibrary(){
	var rows = $("#caseList").xdatagrid('getSelections');
	if (rows.length == 0) {
		$.xalert({title:'提示',msg:'请选择审核通过的用例！'});
		return;
	}
	var testCaseIds = [];
	for(var i=0;i<rows.length;i++){
		testCaseIds.push(rows[i].testCaseId);
	}
	$.post(baseUrl + '/testLibrary/testLibraryAction!updateTestLibraryDetailStatus.action',{
		'dto.testCaseIds':testCaseIds.toString(),
		'dto.testCaseLibraryDetail.examineUserId':$("#loginNam").text()
	},function(data){
		if(data == "success"){
			$("#caseList").xdatagrid("reload");
			$.xalert({title:'提示',msg:'审核成功！'});
		}
	},'text');
}
//不通过审核
function disagreeLibrary(){
	var rows = $("#caseList").xdatagrid('getSelections');
	if (rows.length == 0) {
		$.xalert({title:'提示',msg:'请选择审核不通过的用例！'});
		return;
	}
	var testCaseIds = [];
	for(var i=0;i<rows.length;i++){
		testCaseIds.push(rows[i].testCaseId);
	}
	$.post(baseUrl + '/testLibrary/testLibraryAction!deleteTestLibraryDetail.action',{
		'dto.testCaseIds':testCaseIds.toString()
	},function(data){
		if(data == "success"){
			$("#caseList").xdatagrid("reload");
			$.xalert({title:'提示',msg:'审核成功！'});
		}
	},'text');
}
//# sourceURL=caseExamine.js