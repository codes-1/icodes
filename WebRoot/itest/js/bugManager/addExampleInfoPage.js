var fromIteraPage ="";
$(function(){
	$.parser.parse();
	fromIteraPage = getQueryParam("exampleListInfoId");
});

function getQueryParam(name) {
    var obj = $('#addExampleWindow').xwindow('options');
    var queryParams = obj["queryParams"]; 
    return queryParams[name];
}

//增加关联用例
function addExampleSubmit(){
	if(fromIteraPage!="fromIterationPage"){
		$("#moduleIds").val($("#moduleIdH").data("moduleId"));
	}else{
		$("#moduleIds").val($("#module_IdH").data("moduleId"));
	}
	var url=baseUrl+"/caseManager/caseManagerAction!addCase.action";
	$.ajax({
		  url: url,
		  cache: false,
		  async: false,
		  type: "POST",
//		  dataType:"json",
		  dataType:"text",
		  data: $("#addExampleWindow").xserialize(),
		  success: function(data){
			  if(data.indexOf("success") >=0){
				  //关闭弹窗
				  canlceWin();
//				  showExampleInfo();
				  if(fromIteraPage!="fromIterationPage"){
					  $("#exampleListInfo").xdatagrid('reload');
				  }else{
					  $("#example_ListInfo").xdatagrid('reload');
				  }
//				  loadTestProject();
			  }else{
				  $.xalert({title:'提示',msg:'系统问题！'});
			  }
		  }
		});
}

//显示用例信息
//function showExampleInfo(){
//	$("#exampleListInfo").xdatagrid({
//		url: baseUrl + '/bugManager/relaCaseAction!loadRelaCase.action?dto.moduleId='+$("#moduleIdH").data("moduleId")+'&dto.bugId='+$("#bugIds").data("bugId"),
//		method: 'get',
//		striped:true,
//		height: mainObjs.tableHeight,
//		columns:[[//,formatter:statusFormat
//		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
//			{field:'testCaseId',title:'编号',width:'15%',align:'center',formatter:function(value,row,index){
//				if(value==null || value=="null"){
//					return "";
//				}else{
//					return "<span id="+value+">" + value + "</span>";
//				}
//			}},
//			{field:'testCaseDes',title:'用例描述',width:'30%',align:'center',halign:'center'},
//			{field:'testStatus',title:'状态',width:'15%',align:'center',halign:'center'},
//			{field:'typeName',title:'类型',width:'20%',align:'center',halign:'center'},
//			{field:'priName',title:'优先级',width:'23%',align:'center'}
//		]],
//		onLoadSuccess : function (data) {
//			var dataRela = data.relaCaseList;
//			if(dataRela!=undefined){
//				if (data.total>0) {
//					var testCaseIds = data.testCaseIds;
//					$("#exampleListInfo").xdatagrid('loadData',dataRela);
//					$("#"+testCaseIds).parents().eq(1).prev().find($('input[name="checkId"]')).attr('checked',true);
//					
//				}else{
//					$('#exampleListInfo').prev().find('div.datagrid-body table tr').after('<label style="height: 40px;width: 530px; text-align: center;">没有数据!</label>');
//				}
//			}
////			$("#exampleListInfo").xdatagrid('resize');$('#exampleListInfo').prev().find('div.datagrid-view2 div.datagrid-body table tr')
//		}
//	
//	});
////	$("#exampleListInfo").xdatagrid('resize');
//}


//重置用例弹窗的信息
function addExampleReset(){
	$('#addExampleWindow').xform('clear');
}
//关闭弹窗
function canlceWin(){
	addExampleReset();
	$('#addExampleWindow').xwindow('close');
}

//# sourceURL=addExampleInfoPage.js