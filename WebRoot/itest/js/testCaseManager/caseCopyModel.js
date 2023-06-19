var operationType,operationFlag;
var operaModeId;
var remark="";
function changeModel(index){
	if(index=="1"){
		$("#generalTools").hide();
		$("#specialTools").show();
		$("#pasteCase").hide();
		operationFlag=true;
		$("#caseList").xdatagrid({singleSelect: false});
		$("#caseList").xdatagrid('resize');
	}else{
		$("#generalTools").show();
		$("#specialTools").hide();
		operationFlag=false;
		$("#caseList").xdatagrid({singleSelect: true});
		$("#caseList").xdatagrid('resize');
	}
}
//复制
function copyCase(){
	var row = $("#caseList").xdatagrid('getChecked');
	if (row==null || row.length==0) {
		 $.xalert({title:'提示',msg:'请选择要复制的测试用例!'});
		return;
	}
	operationType=0;
	operaModeId=currNodeId;
	var row = $("#caseList").xdatagrid('getChecked');
	for ( var i in row) {
		if(i==row.length-1){
			remark+= row[i].testCaseId
		}else{
			remark+= row[i].testCaseId+'_'
		}
	}
	$("#pasteCase").show();
}
//剪切
function cutCase(){
	var row = $("#caseList").xdatagrid('getChecked');
	if (row==null || row.length==0) {
		 $.xalert({title:'提示',msg:'请选择要剪切的测试用例!'});
		return;
	}
	operationType=1;
	operaModeId=currNodeId;
	var row = $("#caseList").xdatagrid('getChecked');
	for ( var i in row) {
		if(i==row.length-1){
			remark+= row[i].testCaseId
		}else{
			remark+= row[i].testCaseId+'_'
		}
	}
	$("#pasteCase").show();
}
//粘贴
function pasteCase(){
	if(remark==""){
		 $.xalert({title:'提示',msg:'您还没选择测试用例!'});
		 return;
	}
	var selectedData = $('#caseTree').xtree('getSelected');
	if(selectedData.rootNode){
		 $.xalert({title:'提示',msg:'请选择非root节点测试需求进行粘贴测试用例!'});
		$(selectedData.target).tooltip("show");
		setTimeout(function(){
			$(selectedData.target).tooltip("hide");
		}, 3000);
	}/*else if(operaModeId==currNodeId){
		$.xalert({title:'提示',msg:'同级需求节点不能粘贴测试用例!'});
	}*/else{
		if(operationType==0){
			opeartionUrl = baseUrl + "/caseManager/caseManagerAction!pasteCase.action?dto.command=cp";
		}else if(operationType==1){
			opeartionUrl = baseUrl + "/caseManager/caseManagerAction!pasteCase.action?dto.command=ct";
		}else{
			return;
		}
		
		var params ={
			'dto.remark':remark,
			'dto.currNodeId':currNodeId,
		};
		$.post(
				opeartionUrl,
				params,
				function(data,status,xhr){
					if(status=='success'){
						if(data!=null && data!=""){
							remark="";
							getCaseList(currNodeId);
						}else{
							 $.xalert({title:'提示',msg:'操作失败！'});
						}
					}else{
						$.xnotify(xhr.error(), {type:'warning'});
					}
				},
				"json");
	}
}

//# sourceURL=caseCopyModel.js