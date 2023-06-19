var testSettingObj ={
		$testSettingList:$("#testSettingList"),
		$addOrEditBaseWin:$("#addOrEditBaseWin")
}

$(function(){
	$.parser.parse();
	getTestSettingAuth();
	var url = baseUrl + '/testBaseSet/testBaseSetAction!loadTestBaseSetList.action';
	
	loadData(url);
	
	displayDataSel(baseUrl + '/testBaseSet/testBaseSetAction!getSubList.action');
	
});

//获取当前用户的权限
function getTestSettingAuth(){
	var controlButton = $('button[schkUrl]');
	$.each(controlButton,function(i,n){
		var controId = controlButton[i].id;
		var controlUrl = $(controlButton[i]).attr('schkUrl');
		if(privilegeMap[controlUrl]!="1"){
			$("#"+controId).hide(); 
		}
	});
}

function loadData(urlParam,data){
	//初始化角色列表
	testSettingObj.$testSettingList.xdatagrid({
		url: urlParam,
		method: 'post',
		queryParams: data,
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
		columns:[[
			{field:'typeId',title:'选择',checkbox:true,align:'center'},
			{field:'subName',title:'数据类型',width:'20%',height:'50px',align:'center',halign:'center'},
			{field:'typeName',title:'数据名称',width:'30%',height:'50px',align:'center',halign:'center',formatter:typeNameFormat},
			{field:'status',title:'状态',width:'18%',height:'50px',align:'center',formatter:statusFormat},
			{field:'preference',title:'默认项',width:'10%',height:'50px',align:'center',formatter:preferenceFormat},
			{field:'remark',title:'备注',width:'22%',height:'50px',align:'center',formatter:remarkFormat}
		]],
		onCheck: function(rowIndex,rowData){
			var status = rowData.status;
			if(privilegeMap['testBaseSetAction!swStatus']=="1"){
				document.getElementById("showStopBaseDataWin").style.display = (status === "2")? "none":"inline-block";
				document.getElementById("showStartBaseDataWin").style.display = (status === "1")? "none":"inline-block";
		   }else{
			   document.getElementById("showStopBaseDataWin").style.display = "none";
				document.getElementById("showStartBaseDataWin").style.display = "none";;
		   }
		   },
		  onLoadSuccess: function (data) {
			    /*if (data.total == 0) {
			        var body = $(this).data().datagrid.dc.body2;
			        body.find('table tbody').append('<tr><td width="' + body.width() + '" style="height: 35px; text-align: center;"><h3>暂无数据</h3></td></tr>');
			    }*/
			}
	});
	
	$('#subName').xcombobox({
	    url: baseUrl + '/testBaseSet/testBaseSetAction!getSubList.action',
	    valueField:'keyObj',
	    textField:'valueObj'
	});
}

//显示“切换数据类型”的下拉框
function displayDataSel(urlStr){
	$('#dataTypeAll').xcombobox({
	    url: urlStr,
	    valueField:'keyObj',
	    textField:'valueObj',
	    onSelect:function(record){
	    	var dataParam = {'dto.subName':record.keyObj};
	    	testSettingObj.$testSettingList.xdatagrid('reload',dataParam);
	    }
	});
}

//"数据名称"
function typeNameFormat(value,row,index){
	var typeNameStr = "";
	if(row.isDefault == 1){
		typeNameStr = '<div style="color:blue">' + row.typeName + '</div>';
	}else{
		typeNameStr = '<div>' + row.typeName + '</div>';
	}
	return typeNameStr;
}

//"状态"列表数据转换
function statusFormat(value,row,index){
	if(value === "" || value == null){
		return ;
	}
	
	var status = value==1? "启用":"停用";
	return status;
}

//"默认项"
function preferenceFormat(value){
	
	var status = value===1? "是":"否";
	return status;
}
 
//'备注'列表
function remarkFormat(value){
	var remarkStr = "";
	if(value){
		if(value.length>20){
			remarkStr = '<div title="' + value + '">' + value.substr(0,20) + '...</div>';			
		}else{
			remarkStr = '<div  title="' + value + '">' + value + '</div>';			
		}
	}else{
		remarkStr = '-';
	}
	return remarkStr;
}

//"新增"
document.getElementById("showAddBaseDataWin").addEventListener("click",function(){
    testSettingObj.$addOrEditBaseWin.xform('clear');
	testSettingObj.$addOrEditBaseWin.parent().css("border","none");
	testSettingObj.$addOrEditBaseWin.prev().css({ color: "#ffff", background: "#101010" });
	testSettingObj.$addOrEditBaseWin.xwindow('setTitle','新增').xwindow('open');
	

	
	//新增时默认isDefault:0,status:1
	testSettingObj.$addOrEditBaseWin.xdeserialize({'dto.testBaseSet':{'isDefault':0,'status':1}});
	document.getElementById('testBaseStatus').innerHTML="启用";
	var dataType = $("#dataTypeAll").xcombobox("getValue");
	$('#subName').xcombobox('setValue', dataType);
	
});

//"修改"
document.getElementById("showEdBaseDataWin").addEventListener("click",function(){
	var row = testSettingObj.$testSettingList.xdatagrid('getSelected');
	if (!row) {
		$.xalert('请选择要修改的一条记录', {type:'warning'});
		return;
	}
	testSettingObj.$addOrEditBaseWin.xform('clear');
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!updInit.action",
			{'dto.testBaseSet.typeId': row.typeId},
			function(data) {
				if (data.indexOf("NotFind")>=0) {
					$.xalert('缺省数据不能修改');
				}else{
					data = JSON.parse(data);
					$('#subName').combobox('setValue', data['dto.testBaseSet'].subName);
					testSettingObj.$addOrEditBaseWin.xdeserialize(data);
					$("#initSubName").textbox('setValue',data['dto.testBaseSet'].subName);
					if(null != data&& null !=data['dto.testBaseSet'].status){
						if(data['dto.testBaseSet'].status == "1"){
							document.getElementById('testBaseStatus').innerHTML="启用";
						}else{
							document.getElementById('testBaseStatus').innerHTML="停用";
						}
					}
					testSettingObj.$addOrEditBaseWin.parent().css("border","none");
					testSettingObj.$addOrEditBaseWin.prev().css({ color: "#ffff", background: "#101010" });
					testSettingObj.$addOrEditBaseWin.xwindow('setTitle','修改').xwindow('open');
				}
			},"text"
		);
});

//"删除"
document.getElementById("showDelBaseDataWin").addEventListener("click",function(){
	var row = testSettingObj.$testSettingList.xdatagrid('getSelected');
	if (!row) {
		$.xalert('请选择要删除的一条记录', {type:'warning'});
		return;
	}
	
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!updInit.action",
			{'dto.testBaseSet.typeId': row.typeId},
			function(data) {
				if (data.indexOf("NotFind")>=0) {
					$.xalert('缺省数据不能修改');
				}else{
					$.xconfirm({
						msg:'您确定删除选择的记录吗?',
						okFn: function() {
							$.post(
								baseUrl + "/testBaseSet/testBaseSetAction!delete.action?dto.isAjax=true",
								{'dto.testBaseSet.typeId': row.typeId},
								function(data) {
									testSettingObj.$testSettingList.xdatagrid('reload');
									$.xalert('删除成功');
								},"text"
							);
						}
					});
				}
			},"text"
		);
	

});

//"保存"
document.getElementById("saveBaseDataBtn").addEventListener("click",function(){
	var subName =$('#subName').xcombobox('getValue');
	if(subName == ""){
		$.xalert({title:'消息提示',msg:'请选择数据类型！'});
		return;
	}
	
	var typeName = $("#typeName").textbox('getValue');
	if(typeName == ""){
		$.xalert({title:'消息提示',msg:'请输入数据名称！'});
		return;
	}
	
	 var typeId = document.getElementById('typeId').value;
	 var urlStr = "";
	 var tipStr = "";
	 if(typeId != null && typeId !== ""){
		 urlStr = baseUrl + "/testBaseSet/testBaseSetAction!update.action";
		 tipStr = "修改成功";
	 }else{
		 urlStr = baseUrl + "/testBaseSet/testBaseSetAction!add.action?dto.testBaseSet.isDefaul=0";
		 tipStr = "新增成功"
	 }
	$.post(
		urlStr,
		testSettingObj.$addOrEditBaseWin.xserialize(),
		function(data){
			if (data.indexOf("success")>=0) {
				testSettingObj.$addOrEditBaseWin.xwindow('close');
				testSettingObj.$testSettingList.xdatagrid('reload');
				$.xalert(tipStr);
			} else if (data.indexOf("reName")>=0) {
				$.xalert("【数据名称】重名，请重新输入", {type:'warning'});
			} else{
				$.xalert("操作失败，请稍后再试", {type:'warning'});
			}
		},"text"
	);
});

//"关闭"
document.getElementById("closeBaseDataBtn").addEventListener("click",function(){
	testSettingObj.$addOrEditBaseWin.xwindow('close');
});


//'停用'
document.getElementById('showStopBaseDataWin').addEventListener('click',function(){
	var row = testSettingObj.$testSettingList.xdatagrid('getSelected');
	if (!row) {
		$.xalert('请选择要切换状态的记录', {type:'warning'});
		return;
	}
	
	$.xconfirm({
		msg:'您确定停用选择的记录?',
		okFn: function() {
			$.post(
				baseUrl + "/testBaseSet/testBaseSetAction!swStatus.action",
				{
					'dto.testBaseSet.typeId':row.typeId,
				    'dto.testBaseSet.status':2
				},
				function(data) {
					testSettingObj.$testSettingList.xdatagrid('reload');
					$.xalert('更改成功');
				},"text"
			);
		}
	});
});

//'停用'
document.getElementById('showStartBaseDataWin').addEventListener('click',function(){
	var row = testSettingObj.$testSettingList.xdatagrid('getSelected');
	if (!row) {
		$.xalert('请选择要切换状态的记录', {type:'warning'});
		return;
	}
	
	$.xconfirm({
		msg:'您确定启用选择的记录?',
		okFn: function() {dataTypeAll
			$.post(
				baseUrl + "/testBaseSet/testBaseSetAction!swStatus.action",
				{
					'dto.testBaseSet.typeId':row.typeId,
				    'dto.testBaseSet.status':1
				},
				function(data) {
					testSettingObj.$testSettingList.xdatagrid('reload');
					$.xalert('更改成功');
				},"text"
			);
		}
	});
});

//设置"首选项"
document.getElementById("setPreference").addEventListener('click',function(){
	var row = testSettingObj.$testSettingList.xdatagrid('getSelected');
	if (!row) {
		$.xalert('请选择一条需要设置为【首选项】的记录', {type:'warning'});
		return;
	}
	
	$.post(
			baseUrl + "/testBaseSet/testBaseSetAction!setPreference.action",
			{'dto.testBaseSet.typeId': row.typeId,
             'dto.subName': row.subName},
			function(data) {
				if(data.indexOf("success")>=0){
					$.xalert("【首选项】修改成功");
					testSettingObj.$testSettingList.xdatagrid('reload');
				}else{
					$.xalert("【首选项】修改失败");
				}
			},"text"
		);
});

//@ sourceURL=testSettingManager.js