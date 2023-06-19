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
			'dto.testCaseLibraryDetail.examineStatus':'1'
		},
		/*height: mainObjs.tableHeight,*/
		emptyMsg:"无数据",
		columns:[[
		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'testCaseDes',title:'用例描述',width:"98%",align:'left',formatter:testCaseDesFormat},
			/*{field:'recommendReason',title:'推荐理由',width:"16%",align:'left',formatter:recommendReasonFormat},
			{field:'recommendUserId',title:'推荐人',width:"16%",align:'center'},
			{field:'libraryId',title:'推荐类别',width:"16%",align:'center',formatter:libraryIdFormat},
			{field:'examineStatus',title:'审核状态',width:"16%",align:'center',formatter:examineStatusFormat},
			{field:'examineUserId',title:'审核人',width:"16%",align:'center'},*/
		]],
		onLoadSuccess : function (data) {								
			$("#caseLayout").xlayout('resize');
			$("#caseList").xdatagrid('resize');
			for(var i=0;i<$("#caseLayout tbody").find("tr").length;i++){
				$($("#caseLayout tbody").find("tr")[i]).attr("title","点击查看详情");
				$($("#caseLayout tbody").find("tr")[i]).css("cursor","pointer");
			}
		},
		onCheck:function(index,row){
			openDetailWin(row);
		}
	});
}

function testCaseDesFormat(value,row,index){
	return "<a title='"+value+"'>"+value.substring(0,50)+"</a>";
}

/*function recommendReasonFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,10)+"....</span>";
}*/

/*function libraryIdFormat(value,row,index){
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
				$.xnotify("系统错误！", {type:'warning'});
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
}*/
//打开详情弹窗
function openDetailWin(rowData){
	$("#lookDetailForm").xform('clear');
	$("#lookDetailWin").xwindow('setTitle','查看详情').xwindow('open');
	var fillData = {};
	fillData["dto.testCaseInfo"] = rowData;
	$("#lookDetailForm").xdeserialize(fillData);
}
//# sourceURL=caseLook.js