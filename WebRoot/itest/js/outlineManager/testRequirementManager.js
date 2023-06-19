var currNodeId = "";
var taskId = "";
$(function() {
	$.parser.parse();
//	initSelectTestRequirementTab();
	taskId = $("#taksIdmain").val();
	loadTree(taskId);
	var isCommit = $("#isCommit").val();
	if (isCommit == "0") {
		$("#submitTestBtn").show();
	}
	getLoginUserPrivilege();
});

//初始化选择测试需求项表格
///*function initSelectTestRequirementTab() {
//	$("#selectTestRequirementDiv").parent().css("border","none");
//	$("#selectTestRequirementDiv").prev().css({ color: "#ffff", background: "#101010" });
//	$("#selectTestRequirementDiv").xwindow('setTitle','请选择测试项目').xwindow('open');
//	$("#selectTestRequirementTab").xdatagrid({
//		//url: baseUrl + '/itest/json/datagrid.json',
//		url: baseUrl + '/singleTestTask/singleTestTaskAction!swTestTaskList.action',
//		method: 'get',
//		pagination: true,
//		pageSize: 10,
//		pageNumber: 1,
//		columns:[[
//			{field:'proNum',title:'项目编号',width:'10%',align:'center'},
//			{field:'proName',title:'项目名称',width:'10%',align:'left',halign:'center', formatter:proNameFormat},
//			{field:'devDept',title:'研发部门',width:'10%',align:'left',halign:'center'},
//			{field:'testPhase',title:'测试阶段',width:'10%',align:'center'},
//			{field:'psmName',title:'项目PM',width:'10%',align:'left',halign:'center'},
//			{field:'planStartDate',title:'计划开始时间',width:'10%',align:'center'},
//			{field:'planEndDate',title:'计划结束时间',width:'10%',align:'center'},
//			{field:'planDocName',title:'测试计划',width:'10%',align:'center',formatter:planDocNameFormat},
//			{field:'status',title:'状态',width:'5%',align:'center',formatter:statusFormat},
//			{field:'testLdVos',title:'测试负责人',width:'15%',align:'center', formatter:testLdVosFormat}
//		]]
//	});
//	$("#selectTestRequirementDiv").xwindow('setTitle','请选择测试项目').xwindow('open');
//}*/

//获取当前用户的权限
function getLoginUserPrivilege(){
	var controlButton = $('button[schkUrl]');
	$.each(controlButton,function(i,n){
		var controId = controlButton[i].id;
		var controlUrl = $(controlButton[i]).attr('schkUrl');
		if(privilegeMap[controlUrl]!="1"){
			//$("#"+controlID).removeClass("hide"); 
//			$("[schkurl='"+schkUrl+"']").removeClass("hide");
			//$(":button[schkUrl]").removeClass("hide");  
			$("#"+controId).hide(); 
		}
	});
}

initTestRequirementManager = function(taskId) {
	loadTree(taskId);
	
};

//初始化测试需求项表格
function initTestRequirementTab(currNodeId) {
	$("#testRequirementTab").xdatagrid({
		url: baseUrl + '/outLineManager/outLineAction!loadPeople.action',
		method: 'get',
		queryParams: {
			'dto.currNodeId' : currNodeId
		},
		onClickCell: onClickCell,
        onAfterEdit: onAfterEdit,
		pagination: true,
		pageSize: 10,
		pageNumber: 1,
		rownumbers: false,
		//height: mainObjs.tableHeight,
		columns:[[
			{field:'',title:'',checkbox:true,align:'center'},
			{field:'moduleName',title:'需求项名称',width:'22%',align:'center'},
			/*{field:'klc',title:'一级需求KLoc值',width:'10%',align:'left',halign:'center'},*/
			{field:'quotiety',title:'难度系统数',width:'10%',align:'center',halign:'center',editor:'text',formatter: quotietyFormatter},
			{field:'caseCount',title:'预估用例数',width:'10%',align:'center', formatter: caseCountFormatter},
			{field:'userId',title:'开发人员Id',width:'10%',hidden: true, align:'center', formatter: devMemberUserIdFormatter},
			{field:'teamMember',title:'开发人员',width:'15%',align:'left',halign:'center', formatter: devMemberNameFormatter},
			{field:'assignUserId',title:'分配人员Id',width:'20%',hidden: true, align:'center', formatter: assignUserIdFormatter},
			{field:'assignUserName',title:'分配人员',width:'20%',align:'center', formatter: assignUserNameFormatter},
			{field:'scrpCount',title:'预估脚本数',width:'11%',align:'center', formatter: scrpCountFormatter},
			{field:'sceneCount',title:'预估场景数',width:'11%',align:'center', formatter: sceneCountFormatter}
		]]
	});
};

var editBeforeChildrenNode;
var editBeforeParentNode;
var broNodeNameArr;
var childrenNodeAttr;
//加载树结构
function loadTree(taskId) {
	var editBeforeNodeName;
	$('#testRequirementTree').xtree({
	    url:baseUrl + '/outLineManager/outLineAction!loadTree.action?dto.taskId=' + taskId, //+'&dto.currNodeId='+currNodeId,
	    method:'get',
		animate:true,
		lines:true,
		dnd:true,
		onLoadSuccess:function(node,data){
			if(currNodeId==""){
				var nodeDep = $('#testRequirementTree').xtree('find',data[0].id);  
				//alert(JSON.stringify(nodeDep));
				if (null != nodeDep && undefined != nodeDep){  
					$('#testRequirementTree').xtree('select',nodeDep.target);  
				}  
			}else{
				var nodeDep = $('#testRequirementTree').xtree('find',currNodeId);  
				if (null != nodeDep && undefined != nodeDep){  
					$('#testRequirementTree').xtree('select',nodeDep.target);
				}  
			}
			
			initTestRequirementTab(data[0].id);
		 }, 
		onClick: function(node){
			var moduleState = node.moduleState;
			if (moduleState == 0) {
				var parent = $('#testRequirementTree').tree('getParent', node.target);
				if (parent != null) {
					$("#disabledBtn").css("display", "inline-block");
				} else {
					$("#disabledBtn").css("display", "none");
				}
				$("#enableBtn").css("display", "none");
			} else if (moduleState == 1) {
				$("#disabledBtn").css("display", "none");
				$("#enableBtn").css("display", "inline-block");
			}
			$("#moduleState").val(moduleState);
			var currLevel = node.currLevel;
			$("#currLevel").val(currLevel);
			currNodeId = node.id;
			//alert(currNodeId);
			initTestRequirementTab(node.id);

		},
		onDblClick: function(node){
			var parent = $('#testRequirementTree').tree('getParent', node.target);
			if (parent == null) {
			} else {
				editBeforeNodeName = node.text;
				$(this).xtree('beginEdit',node.target);
			}
		},
		onContextMenu: function(e,node){
			e.preventDefault();
			$(this).xtree('select', node.target);
		},
		onBeforeEdit: function(node) {
			var parent = $(this).xtree('getParent', node.target);
			//var children = $('#testRequirementTree').tree('getChildren', parent.target);
			if (parent != null) {
				var broNodes = "";
				var children = $(this).xtree('getChildren', parent.target); //$('#testRequirementTree').tree('getChildren', parent.target); 
				for (var i = 0; i < children.length; i++) {
					if (broNodes == "") {
						broNodes = children[i].text;
					} else {
						broNodes += "," + children[i].text;
					}
					
				}
				broNodeNameArr = broNodes.split(",");
			}
		},
		onAfterEdit: function(node) {
			var parent = $('#testRequirementTree').tree('getParent', node.target);
			if (node.text == editBeforeNodeName) {
				return;
			}
			if (!updateNodeName(node, parent.id)){
				//$(this).xtree('cancelEdit',node.target);
				$(this).tree('update', {
					target: node.target,
					text: editBeforeNodeName
				});
			}
		}
	});
	
};

updateNodeName = function(node, parentNodeId) {
	var nodeName = node.text;
	if(includeSpeChar(nodeName)){
		$.xalert({title:'提示', msg: '测试需求项不能含特殊字符!'});
		return;
	}
	if (broNodeNameArr.length > 0) {
		for(var h = 0; h < broNodeNameArr.length; h++){
			var broName = broNodeNameArr[h];
			if(nodeName == broName){
				$.xalert({title:'提示', msg: '需求项: ' + nodeName +' 同级重名'});
				return false;
			}				
		}
	}

	var url = baseUrl + "/outLineManager/outLineAction!updateNode.action?dto.isAjax=true";
	$.ajax({
		type: "post",
		dataType: "text",
		url: url,
		data: {
			'dto.currNodeId': node.id,
			'dto.nodeName': nodeName,
			'dto.parentNodeId': parentNodeId
		},
		success: function(data) {
			if (data == "sucess") {
				$.xalert({title:'提示', msg: '修改需求节点名称成功！'});
				loadTree(taskId);
			} else {
				$.xalert({title:'提示', msg: '修改需求节点名称失败！'});
				return false;
			}
		}
	});
	
}

//显示添加测试需求窗口
showAddTestRequirementWin = function() {
	$('#createForm')[0].reset();
	$("#command").val("addchild");
	var selected = $('#testRequirementTree').tree('getSelected');
	$("#addTestRequirementDiv").parent().css("border","none");
	$("#addTestRequirementDiv").prev().css({ color: "#ffff", background: "#101010" });
	$("#addTestRequirementDiv").xwindow('setTitle','增加需求--当前需求项：' + selected.text).xwindow('open');
};

//打开分配人员窗口
showSeletctPeopleWin = function() {
	$('#assignForm')[0].reset();
	var row = $('#testRequirementTab').xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'提示', msg: '请选择要分配的需求项！'});
		return;
	}
	$("#testRequirementDistrDiv").parent().css("border","none");
	$("#testRequirementDistrDiv").prev().css({ color: "#ffff", background: "#101010" });
	$("#testRequirementDistrDiv").xwindow('setTitle','人员分配').xwindow('open');
	if ($('#allo').attr('checked')) {
		$('#assignType').val(4);
	} else {
		$('#assignType').val(5);
	}
	
	initAssignUser();
};

initAssignUser = function() {
	initSelectedPepoleData();
	initSelectPeopleData();
};

//初始化选择人的数据
initSelectPeopleData = function() {
	$("#selectPepoleTab").xdatagrid({
		url: baseUrl + '/outLineManager/outLineAction!selectMember.action',
		method: 'get',
		queryParams: {
			'dto.taskId' : taskId,
			'dto.reqType': $("#assignType").val()
		},
		onLoadSuccess: onLoadSuccess,
		onDblClickRow: appendDataToSelectedPepoleTab,
		pagination: false,
		height: mainObjs.tableHeight,
		columns:[[
			{field:'valueObj',title:'备选人员--双击选择',width:'100%',align:'center'}
		]]
	});
};

//人员分配时单选一个测试需求时，在分配窗口的选择列表中删除与已选择列表相同值
function onLoadSuccess(data) {
	var row = data.rows;
	var requirementRows = $("#testRequirementTab").xdatagrid('getSelections');
	var selectedPepoleRows = $('#selectedPepoleTab').xdatagrid('getRows');
	console.log(JSON.stringify(requirementRows));
	if (requirementRows.length == 1) {
		var teamMember = requirementRows[0].teamMember;
		var assignType = $("#assignType").val();
		for (var i = row.length - 1; i >= 0; i --) {
			var keyObj = row[i].keyObj;
			var valueObj = row[i].valueObj;
			for (var j = 0; j < teamMember.length; j++) {
				var userRole = teamMember[j].userRole;
				if (assignType == 5) {//开发人员
					if (teamMember[j].userId == keyObj) {
						if (userRole == 1) {
							var selectPeoplerows = $('#selectPepoleTab').xdatagrid('getRows');
							for (var k = selectPeoplerows.length - 1; k >=0; k--) {
								console.log("selectPeople:" + JSON.stringify(selectPeoplerows[k]));
								if (selectPeoplerows[k].keyObj == keyObj) {
									var index = $('#selectPepoleTab').xdatagrid('getRowIndex', selectPeoplerows[k]);
							        $('#selectPepoleTab').xdatagrid('deleteRow', index);
								}
								  
							}
						}
						
							
					}
				} else {//分配人员
					if (userRole == 3) {
						var selectPeoplerows = $('#selectPepoleTab').xdatagrid('getRows');
						for (var k = selectPeoplerows.length - 1; k >=0; k--) {
							console.log("selectPeople:" + JSON.stringify(selectPeoplerows[k]));
							if (selectPeoplerows[k].keyObj == keyObj) {
								var index = $('#selectPepoleTab').xdatagrid('getRowIndex', selectPeoplerows[k]);
						        $('#selectPepoleTab').xdatagrid('deleteRow', index);
							}
							  
						}
					}
				}
			}
		}
		
		 
	}
}

function selectPeopleValueFormatter(value,row,index) {
	var requirementRows = $("#testRequirementTab").xdatagrid('getSelections');
	var num = 0;
	var selectedNum = 0;
	var value = "";
	var userId = "";
	if (requirementRows.length == 1) {
		var teamMember = requirementRows[0].teamMember;
		
		console.log("row.keyObj:" + row.keyObj + "   " + "row.valueObj:" + row.valueObj);
		for (var i = 0; i < teamMember.length; i++) {
			console.log("teamMember[i].userId:" + teamMember[i].userId);
			if (userId == "") {
				userId = teamMember[i].userId;
			} else {
				userId += "_" + teamMember[i].userId;
			}
		}
		var keyObj = row.keyObj;
		var rows = $('#selectPepoleTab').xdatagrid('getRows');
		if (userId.indexOf(keyObj) != -1) {
			//$('#selectPepoleTab').xdatagrid('deleteRow', index);
		} else {
			return row.valueObj;
		}
	} else {
		return row.valueObj;
	}
	
	
}

//初始化已分配的人员
initSelectedPepoleData = function() {
	$("#selectedPepoleTab").xdatagrid({
		onDblClickRow: cannelSelectedData,
		height: mainObjs.tableHeight,
		columns:[[
			{field:'keyObj',title:'id',hidden:'true', width:'100%',align:'center'},
			{field:'valueObj',title:'已选人员--双击取消',width:'100%',align:'center'}
		]]
	});
	var row = $('#selectedPepoleTab').xdatagrid('getRows');
	for (var i = row.length - 1; i >=0; i--) {
		var index = $('#selectedPepoleTab').xdatagrid('getRowIndex', row[i]);
        $('#selectedPepoleTab').xdatagrid('deleteRow', index);  
	}
	var requirementRows = $("#testRequirementTab").xdatagrid('getSelections');
	if (requirementRows.length == 1) {
		var teamMember = requirementRows[0].teamMember;
		var assignType = $("#assignType").val();
		for (var j = 0; j < teamMember.length; j++) {
			var user = teamMember[j].user;
			var userRole = teamMember[j].userRole;
			if (assignType == 5) {
				if (userRole == 1) {
					$('#selectedPepoleTab').xdatagrid('appendRow',{
						keyObj: user.id,
						valueObj: user.name
					});
				}
			} else {
				if (userRole == 3) {
					$('#selectedPepoleTab').xdatagrid('appendRow',{
						keyObj: user.id,
						valueObj: user.name
					});
				}
			}
//			$('#selectedPepoleTab').xdatagrid('appendRow',{
//				keyObj: user.id,
//				valueObj: user.name
//			});
		}
	}
	
};
//验证新增测试需求
addTestRequirementNodeCheck = function() {
	if (!testRequirementNameBlankCheck()) {
		$.xalert({title:'提示', msg: '致少输入一需求项'});
		return false;
	} else if (!testRequirementNameReCheck()) {
		return false;
	}
	
	return true;
}

//验证新增测试需求时需求项是否为空
testRequirementNameBlankCheck = function() {
	var form = $("#createForm").get(0);
	var elements = form.elements;
	var element;
	for (var i = 0; i < elements.length; i++) {
	 	element = elements[i];
	 	if (element.id.indexOf("_") < 0 && element.type == "text" && ((element.value.replace(/(^\s*)|(\s*$)/g, ""))!="")){
			return true;
	 	}
	}
	
	return false;
}

//添加同级需求时，如果是根节点不能添加
checkIsParent = function() {
	var node = $('#testRequirementTree').tree('getSelected');
	var parent = $('#testRequirementTree').tree('getParent', node.target);
	if (parent == null) {
		$.xalert({title:'提示', msg: '不能对根节点添加同级需求'});
		return false;
	}
}
//验证新增测试需求时需求项名称是否重复
testRequirementNameReCheck = function() {
	var form = $("#createForm").get(0);
	var elements = form.elements;
	var element;
	var valuesStr = "";
	for (var i = 0; i < elements.length; i++) {
	 	element = elements[i];
	 	var value= element.value.replace(/(^\s*)|(\s*$)/g, "");
	 	if (element.id.indexOf("_") < 0 && element.type == "text" && value!=""){
			valuesStr = valuesStr +","+value;
	 	}
	}
	valuesStr = valuesStr.substring(1);
	var valuesArr = valuesStr.split(",");
	var node = $('#testRequirementTree').tree('getSelected');
	var command = $("#command").val();
	var broNodeArr;
	if(command == "addchild"){
		var nodes = "";
		var children = $('#testRequirementTree').tree('getChildren', node.target); 
		for (var i = 0; i < children.length; i++) {
			if (nodes == "") {
				nodes = children[i].text;
			} else {
				nodes += "," + children[i].text;
			}
			
		}
		broNodeArr = nodes.split(",");
	} else if (command == "addBro") {
		var nodes = "";
		var parent = $('#testRequirementTree').tree('getParent', node.target);
		if (parent != null) {
			var children = $('#testRequirementTree').tree('getChildren', parent.target); 
			for (var i = 0; i < children.length; i++) {
				if (nodes == "") {
					nodes = children[i].text;
				} else {
					nodes += "," + children[i].text;
				}
				
			}
			broNodeArr = nodes.split(",");
		}
		
	}
	for(var i=0; i<valuesArr.length; i++ ){
		var nName = valuesArr[i];
		//先校验当前输入的相互有无重名
		for(var l= i+1; l < valuesArr.length; l++){
			if(nName == valuesArr[l]){
				$.xalert({title:'提示', msg: '需求项: ' + nName +' 重名'});
				return false;
			}
		}
		
		//再校验树中兄弟节点有无重名
		var broName = "";
		for(var h = 0; h < broNodeArr.length; h++){
			broName = broNodeArr[h];
			if(nName == broName){
				$.xalert({title:'提示', msg: '需求项: ' + nName +' 同级重名'});
				return false;
			}				
		}
	}
	
	for (var k = 0; k < valuesArr.length; k++) {
		if (node.text == valuesArr[k]) {
			$.xalert({title:'提示', msg: '需求项: ' + node.text +' 与父需求重名'});
			return false;
		}
	}
	
	return true;
}

//输入需求项时触发
keyEntry = function(obj) {
	obj.onkeydown = function(e) {
		var html = $("#addTestRequirementWarn").html();
		if (html != "") {
			$("#addTestRequirementWarn").html("");
			$("#addTestRequirementWarn").hide();
		}
	}
}

//添加测试需求节点
addTestRequirementNode = function(eventType) {
	var node = $('#testRequirementTree').tree('getSelected');
	$("#currNodeId").val(node.id);
	var command = $("#command").val();
	if (command == 'addBro') {
		var parent = $('#testRequirementTree').tree('getParent', node.target); 
		if (parent == null)
			$.xalert({title:'提示', msg: '不能对根节点添加同级需求'});
			return;
		var temp;
	    if (parent != null) {
	        temp = parent;
	        parent = $('#testRequirementTree').tree('getParent', parent.target);//对该节点取父节点
	        $("#parentNodeId").val(temp.id);
	    }
	}
	if (!addTestRequirementNodeCheck()) {
		return;
	}
	$.ajax({
		type: "post",
		dataType: "text",
		url: baseUrl + '/outLineManager/outLineAction!addNodes.action?dto.isAjax=true',
		data: $("#createForm").serialize(),
		success: function(data) {
			if (eventType == 'closed') {
				$("#addTestRequirementDiv").xwindow('close');
			}
			$('#createForm')[0].reset();
			loadTree(taskId);
			initTestRequirementTab(node.id);
		}
	});
	//$("#createForm").xform('clear');
};

adjustCreTable = function(val) {
	var tb = document.getElementById("testRequirementCreateTab");
	var rows = tb.rows;
    for(var i = 0;i < rows.length; i++){
    	var cells = rows[i].cells;
        for(var j = 0;j < cells.length; j++){
        	if(j >= (cells.length-2)){
        		if(val == "fun"){
        		  cells[j].style.display = "none";
        		}else{
        		  cells[j].style.display = "";
        		}
        	}
        }
    }
};

//人员分配--从备选列添加到已选列
appendDataToSelectedPepoleTab = function(rowIndex, rowData) {
	$('#selectedPepoleTab').xdatagrid('appendRow',{
		keyObj: rowData.keyObj,
		valueObj: rowData.valueObj
	});
	$('#selectPepoleTab').xdatagrid('deleteRow', rowIndex);
};

//取消添加的人员分配-从已选列表到备选列表
cannelSelectedData = function(rowIndex, rowData) {
	$('#selectPepoleTab').xdatagrid('appendRow',{
		keyObj: rowData.keyObj,
		valueObj: rowData.valueObj
	});
	$('#selectedPepoleTab').xdatagrid('deleteRow', rowIndex);
	//initSelectPeopleData();
};

//提交分配人员
submitSelectedPeople = function() {
	var selectedPepoleRows = $('#selectedPepoleTab').xdatagrid('getRows');
	//alert(JSON.stringify(selectedPepoleRows));
	if (!selectedPepoleRows) {
		$.xalert({title:'提示',msg:'请选择人员！'});
		return;
	}
	var selectedPeopleId = "";
	var selectedPeopleName = "";
	for (var i = 0; i < selectedPepoleRows.length; i++) {
		if (selectedPeopleId == "") {
			selectedPeopleId = selectedPepoleRows[i].keyObj;
		} else {
			selectedPeopleId += "_" + selectedPepoleRows[i].keyObj;
		}
		if (selectedPeopleName == "") {
			selectedPeopleName = selectedPepoleRows[i].valueObj;
		} else {
			selectedPeopleName += " " + selectedPepoleRows[i].valueObj;
		}
	}
	$("#userIds").val(selectedPeopleId);
	var requirementRows = $("#testRequirementTab").xdatagrid('getSelections');
	//alert(JSON.stringify(requirementRows));
	if (!requirementRows) {
		$.xalert({title:'提示',msg:'请选择要分配的需求项！'});
		return;
	}
	var moduleId = "";
	for (var i = 0; i < requirementRows.length; i++) {
		if (moduleId == "") {
			moduleId = requirementRows[i].moduleId;
		} else {
			moduleId += "," + requirementRows[i].moduleId;
		}
	}
	
	var nid = validateSelectedPeople(moduleId, requirementRows, selectedPeopleId);
	
	$("#assignNIds").val(nid);
	//$("#assignType").val(loadType);
	$.ajax({
		type: "post",
		dataType: "text",
		url: baseUrl + '/outLineManager/outLineAction!assignPeople.action?dto.taskId=' + taskId + "&dto.reqType=" + $("#assignType").val(),
		data: $("#assignForm").serialize(),
		success: function(data) {
			if (data == "sucess") {
				$("#testRequirementDistrDiv").xwindow('close');
				$('#testRequirementTab').datagrid('reload');
			} else {
				$.xalert({title:'提示',msg:'加载数据发生错误！'});
			}
		}
	});
	
};

//验证选择分配的人员-是否重复
validateSelectedPeople = function(moduleId, rows, selectedUserIds) {
	var nid = "";
	var dataMap = new Map();
	for (var i = 0; i < rows.length; i++) {
		var id = rows[i].moduleId;
		var userIds = "";
		var teamMember = rows[i].teamMember;
		for (var j = 0; j < teamMember.length; j++) {
			var userId = teamMember[j].userId;
			if (userId != "") {
				if (userIds == "") {
					userIds = userId; 
				} else {
					userIds += "_" + userId;
				}
			}
			
		}
		dataMap.put(id, userIds);
	}
	console.log(JSON.stringify(dataMap));
	if (typeof(moduleId) == "string") {
		var moduleIdAttr = moduleId.split(",");
		for (var i = 0; i < moduleIdAttr.length; i++) {
			if (dataMap.get(moduleIdAttr[i]) != selectedUserIds) {
				if (nid == "") {
					nid = moduleIdAttr[i];
				} else {
					nid += "," + moduleIdAttr[i];
				}
			}
				
		}
	} else {
		nid = moduleId;
	}
	
	return nid;
};

//启用或停用测试需求
enableAndDisabledTestRequirement = function() {
	$("#taskId").val(taskId);
	var node = $('#testRequirementTree').xtree('getSelected');
	$("#currNodeId").val(node.id);
	var parent = $('#testRequirementTree').xtree('getParent', node.target); 
	var temp;
    while(parent != null){
        temp = parent;
        parent = $('#testRequirementTree').xtree('getParent', parent.target);//对该节点取父节点
    }
    $("#parentNodeId").val(temp.id);
	$("#command").val("swState");
	var moduleState = $("#moduleState").val();
	$.ajax({
		type: "post",
		dataType: "text",
		url: baseUrl + '/outLineManager/outLineAction!switchState.action?dto.isAjax=true',
		data: $("#createForm").serialize(),
		success: function(data) {
			if (data == 'sucess') {
				//$('#testRequirementTree').xtree('reload');
				var text = node.text;
				if (text.indexOf("red") >= 0) {
					text = text.substring(text.indexOf("\">")+2, text.indexOf("</span>"));
				} else if (text.indexOf("black") >= 0) {
					text = text.replace("black", "red");
				} else if (text.indexOf("red") < 0 || text.indexOf("black") < 0) {
					text = '<span style="color: red">'+ text +'</span>';
				}
				if (moduleState == "1") {
					$("#moduleState").val("0");
					$("#disabledBtn").css("display", "inline-block");
					$("#enableBtn").css("display", "none");
					$('#testRequirementTree').xtree('update', {  
	                    target: node.target,  
	                    text: text
					});
				} else {
					$("#moduleState").val("1");
					$("#disabledBtn").css("display", "none");
					$("#enableBtn").css("display", "inline-block");
					$('#testRequirementTree').xtree('update', {  
	                    target: node.target,  
	                    text: text
					});
				}
				
				
			}
		}
	});
};

//提交测试需求
submitTestRequirement = function() {
	///outLineManager/outLineAction!submitModule.action?dto.isAjax=true&dto.taskId="+$("taskId").value;
	$.xconfirm({
		msg:'提交测试后不能删除只能停用，您确定要提交吗?',
		okFn: function() {
			$.ajax({
				type: "post",
				dataType: "text",
				url: baseUrl + '/outLineManager/outLineAction!submitModule.action?dto.isAjax=true&dto.taskId=' + taskId,
				success: function(data) {
					if (data == "sucess") {
						$.xalert({title:'提示',msg:'提交测试成功！'});
						$("#submitTestBtn").hide();
						$("#outlineState").val("1");
					} else {
						$.xalert({title:'提示',msg:'提交测试失败！'});
					}
				}
			});
		}
	});
	
}


var editIndex = undefined;  
function endEditing() {//该方法用于关闭上一个焦点的editing状态 
    if (editIndex == undefined) {  
        return true;
    }  
    if ($('#testRequirementTab').datagrid('validateRow', editIndex)) {  
        $('#testRequirementTab').datagrid('endEdit', editIndex);
        editIndex = undefined;  
        return true;  
    } else {  
        return false;  
    }  
}  
//点击单元格事件：  
function onClickCell(index,field,value) {
    if (endEditing()) {
        if(field=="quotiety"){
            $(this).datagrid('beginEdit', index);
            var ed = $(this).datagrid('getEditor', {index:index,field:field});
            $(ed.target).focus();  
        }         
        editIndex = index;  
    }  
    //$('#testRequirementTab').datagrid('onClickRow');
}  
//单元格失去焦点执行的方法  
function onAfterEdit(index, row, changes) {  
    var updated = $('#testRequirementTab').datagrid('getChanges', 'updated');
    if (updated.length < 1) {  
        editRow = undefined;  
        $('#testRequirementTab').datagrid('unselectAll');  
        return;  
    } else {  
        // 传值  
        //submitForm(index, row, changes);  
    	
    }  
  
      
}  

function planDocNameFormat(value,row,index) {
	if (value) {
		return '<a href="javascript:void(0)">@</a>';
	}
}

function proNameFormat(value,row,index) {
	return "<a href='javascript:void(0)' title='进入项目："+ row.moduleName +" 测试需求管理' onclick=\"initTestRequirementManager('"+ row.taskId +"')\">" + value + "</a>";
}

function testLdVosFormat(value,row,index) {
	var testLdVos = row.testLdVos;
	var name = "";
	for (var i = 0; i < testLdVos.length; i++) {
		if (name == "") {
			name = testLdVos[i].name;
		} else {
			name += "," + testLdVos[i].name;
		}
	}
	return name;
}

//开发人员ID
function devMemberUserIdFormatter(value,row,index) {
	var teamMember = row.teamMember;
	var userIds = "";
	for (var i = 0; i < teamMember.length; i++) {
		var userRole = teamMember[i].userRole;
		if (userRole == 1) {
			if (userIds == "") {
				userIds = teamMember[i].userId;
			} else {
				userIds += "," + teamMember[i].userId;
			}
		}
		
	}
	
	return userIds;
}

//开发人员
function devMemberNameFormatter(value,row,index) {
	var teamMember = row.teamMember;
	var name = "";
	for (var i = 0; i < teamMember.length; i++) {
		var userRole = teamMember[i].userRole;
		if (userRole == 1) {
			var userId = teamMember[i].userId;
			var user = teamMember[i].user;
			if (name == "") {
				name = user.name;
			} else {
				name += "," + user.name;
			}
		}
		
	}
	return name;
}
//人员分配窗口分配人员ID
function assignUserIdFormatter(value,row,index) {
	var teamMember = row.teamMember;
	var userIds = "";
	for (var i = 0; i < teamMember.length; i++) {
		var userRole = teamMember[i].userRole;
		if (userRole == 3) {
			if (userIds == "") {
				userIds = teamMember[i].userId;
			} else {
				userIds += "," + teamMember[i].userId;
			}
		}
		
	}
	
	return userIds;
}
//人员分配窗口分配人员名称
function assignUserNameFormatter(value,row,index) {
	var teamMember = row.teamMember;
	var name = "";
	for (var i = 0; i < teamMember.length; i++) {
		var userRole = teamMember[i].userRole;
		if (userRole == 3) {
			var userId = teamMember[i].userId;
			var user = teamMember[i].user;
			if (name == "") {
				name = user.name;
			} else {
				name += "," + user.name;
			}
		}
		
	}
	return name;
}
//脚本数
function scrpCountFormatter(value,row,index) {
	if (row.isleafNode == 1) {
		return value;
	}
}

function sceneCountFormatter(value,row,index) {
	if (row.isleafNode == 1) {
		return value;
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

function quotietyFormatter(value,row,index) {
	//console.log(JSON.stringify(row))
	if (row.isleafNode == 1) {
		return value;
	}
	
}
//用例数
function caseCountFormatter(value,row,index) {
	if (row.isleafNode == 1) {
		return value;
	}
}

function isWhitespace(b) {
	return !(/\S/.test(b));
}

function includeSpeChar(f) {
	var g = "~ ` ! # $ % ^ & * ( ) [ ] { } ; ' : \" , \uff0c < >";
	var h = g.split(" ");
	if (typeof (f) == "undefined" || isWhitespace(f)) {
		return false;
	}
	for (var e = 0; e < h.length; e++) {
		if (f.indexOf(h[e]) >= 0) {
			return true;
		}
	}
	return false;
}
//# sourceURL=testRequirementManager.js