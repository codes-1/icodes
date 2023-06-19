var roleUserManaObj = {
         $roleInnerListTb:$("#roleInnerListTb"),
         $roleOuterListTb:$("#roleOuterListTb"),
         $queryUserInnerWin:$("#queryUserInnerWin"),
         $queryUserOuterWin:$("#queryUserOuterWin")
}
var userLis = "";
var groupName,groupRemark,companyId,insertDate,updateDate,adminFlag;
var rId = getQueryParam("roleUserEditDlg","groupId");
$(function(){
	//初始化账号内列表
	loadUserInnerTable();
	//tab初始化grid是用的innerListTb 查询弹窗用的是queryUserOuterWin grid上的按钮分别是queryOuterData addOuterData closeRoleUserWin
	/*
	//账号内【用户状态】、【所属组】下拉框数据
	loadSelData('roleUser_Status','roleUsergroup_Ids');*/
	//账号外【用户状态】、【所属组】下拉框数据
	/*loadSelData('roleUserStatus','roleUsergroupIds');*/
	
});


//加载账号内列表
function loadUserInnerTable(){
	roleUserManaObj.$roleInnerListTb.xdatagrid({
		
		url: baseUrl + "/userManager/userManagerAction!notInGroup.action",
		
		queryParams:{'dto.group.id': rId,
			'dto.isAjax':true},
		method: 'get',
		singleSelect:false,
		height: mainObjs.tableHeight-140,
		idField:'id',
		columns:[[
            {field:'ck',checkbox:true},
			{field:'id',hidden:true},
			{field:'name',title:'姓名',width:'18%',height:'50px',align:'center',halign:'center'},
			{field:'employeeId',title:'员工编号',width:'18%',height:'50px',align:'center',halign:'center'},
			{field:'loginName',title:'登录账号',width:'14%',height:'50px',align:'center'},
			{field:'tel',title:'联系电话',width:'14%',height:'50px',align:'center'},
			{field:'officeTel',title:'办公电话',width:'14%',height:'50px',align:'center'},
			{field:'privilege',title:'职务',width:'14%',height:'50px',align:'center'},
			{field:'status',title:'状态',width:'9%',height:'50px',align:'center',formatter:statusFormat},
		]],
		onLoadSuccess : function (data) {								
			if (data.total==0) {
			/*	$("#roleUserEditDlg #roleInnerDiv .datagrid-view").css("height","100px");*/
			/*	$("#roleUserEditDlg #roleInnerDiv .datagrid-body").css("height","50px");*/
				$('#roleUserEditDlg #roleInnerDiv tr[id^="datagrid-row-r"]').parent().append('<div style="width: 900px; text-align: center;">暂无数据</div>');
			}
		}
	});
}

function statusFormat(value,row,index){
	if(value === "" || value == null){
		return ;
	}
	
	var status = value==1? "启用":"禁止";
	return status;
}

//【用户状态】、【所属组】下拉框数据
function loadSelData(roleUserStatus,roleUserGroud){
	$.post(
			 baseUrl + '/userManager/userManagerAction!groupSel.action',
			 null,
			 function(dataObj) {
				    dataObj = JSON.parse(dataObj);
					 dataObj.push({'keyObj':'-1','valueObj':'所有'});

					$('#' + roleUserGroud).xcombobox({
					    data: dataObj,
					    valueField:'keyObj',
					    textField:'valueObj'
					});
				},"text"
			);
		
	    var statusDataObj = [];
	    if("roleUserStatus" === roleUserStatus){
	         statusDataObj = [
	                 	    	{label:'1',value:'活动'}
	                 	    ];
	    }else {
	    	 statusDataObj = [
	    	          	    	{label:'-1',value:'所有'},
	    	          	    	{label:'1',value:'活动'},
	    	          	    	{label:'0',value:'禁止'}	
	    	          	    ];
	    }
	    
		$('#' + roleUserStatus).xcombobox({
			data:statusDataObj,
		    valueField:'label',
		    textField:'value'
		});
}

//选项卡"角色内账户"的单击事件
document.getElementById('roleInnerTab').addEventListener("click",function(){
	document.getElementById('roleOuterTab').setAttribute('class','tab_li_rt');
	document.getElementById('roleInnerTab').setAttribute('class','tab_li_lf active');
	document.getElementById('roleOuterDiv').style.display="none";
	document.getElementById('roleInnerDiv').style.display="block";
	loadUserInnerTable();
});


//角色外账号：添加到角色中
document.getElementById('addOuterData').addEventListener('click',function(){
	
	var rows = roleUserManaObj.$roleInnerListTb.xdatagrid('getSelections');
	if (rows.length<=0) {
		$.xalert({title:'消息提示',msg:'请选择要添加到角色中的账号！'});
		return;
	}
	//获取本组原来的成员id
	$.getJSON(
			baseUrl + "/userManager/userManagerAction!groupUpInit.action",
			{'dto.group.id': rId,
				'dto.isAjax':true},
			function(data) { 
				objs.$addOrEditGroupWin.xform('clear');
				objs.$addOrEditGroupWin.xdeserialize(data);
				var user = data.user;
				groupName = data.name;
				groupRemark = data.remark;
				companyId = data.companyId;
				insertDate = data.insertDate ;
				updateDate = data.updateDate;
				adminFlag = data.adminFlag;
				for(i=0;i<user.length;i++){
					userLis += user[i].id+" ";
				}
				for(var row in rows){
					userLis += rows[row].id + " ";
				}
				userLis = userLis.substr(0,userLis.length-1);
				addUserToGroup();
				}); 
});
function addUserToGroup(){
	$.post(
			baseUrl + "/userManager/userManagerAction!addGroup.action?dto.isAjax=true",
			{"dto.group.userIds":userLis,
			"dto.group.id":rId,
			"dto.group.name":groupName,
			"dto.group.remark":groupRemark ,
			"dto.group.companyId":companyId,
			"dto.group.insertDate":insertDate,
			"dto.group.updateDate":updateDate,
			"dto.group.adminFlag":adminFlag },
			function(data) {
				if(data.indexOf("success")>-1){
					roleUserManaObj.$roleInnerListTb.xdatagrid('reload');
					$.xalert({title:'成功',msg:'操作成功！'}); 
					objs.$userGroupList001.xdatagrid('reload'); 
				}else if(data.indexOf("reName")>-1){
					$.xalert({title:'失败',msg:'该组名已存在，请勿重复添加！'});
				}else{
					$.xalert({title:'失败',msg:'操作失败！'});
				}
			},"text"
		); 
}
//角色外账号查询弹出框
 document.getElementById('queryOuterData').addEventListener('click',function(){
	roleUserManaObj.$queryUserOuterWin.xform('clear');
	roleUserManaObj.$queryUserOuterWin.parent().css("border","none");
	roleUserManaObj.$queryUserOuterWin.prev().css({ color: "#ffff", background: "#101010" });
	roleUserManaObj.$queryUserOuterWin.xwindow('setTitle','组外账号查询').xwindow('open');
	
	$('#roleUserStatus').xcombobox('setValue', '1');
	$('#roleUsergroupIds').xcombobox('setValue','-1');
}); 

//角色外账号查询重置
document.getElementById('resetUserOuterDlg').addEventListener('click',function(){
	roleUserManaObj.$queryUserOuterWin.xform('clear');
	
	roleUserManaObj.$queryUserOuterWin.xdeserialize({'dto.group.id' : rId});
	$('#roleUserStatus').xcombobox('setValue', '1');
	$('#roleUsergroupIds').xcombobox('setValue','-1');
});

//账号外角色查询
document.getElementById('queryUserOuter').addEventListener('click',function(){
	 var data = roleUserManaObj.$queryUserOuterWin.xserialize();
	    roleUserManaObj.$queryUserOuterWin.xwindow('close');
	    data['dto.group.id']=rId;
	    roleUserManaObj.$roleInnerListTb.xdatagrid('reload',data);
}); 



function closeRoleUserWin(){
	 /*var userInnerWin = document.getElementById('queryUserInnerFooter').parentNode;
     document.body.removeChild(userInnerWin);*/
     var userOuterWin = document.getElementById('queryUserOuterFooter').parentNode;
     document.body.removeChild(userOuterWin); 
	 closeDlg('roleUserEditDlg');
} 
function closeDlg(dlgId){
	  $('#' + dlgId).dialog('destroy');
}
//dlgName:对话框id;name:传参名称
function getQueryParam(dlgName,name){
	var obj = $('#'+dlgName).dialog("options");
	var queryParams = obj["queryParams"];
	
	return queryParams[name];
}
function getInnerUsers(){
	
}


/*//加载账号外列表
function loadUserOuterTable(){
	
	roleUserManaObj.$roleOuterListTb.xdatagrid({
		url: baseUrl + '/role/roleAction!selingUerList.action?',
		queryParams:{'dto.role.roleId' : rId},
		method: 'get',
		singleSelect:false,
		height: mainObjs.tableHeight-140,
		idField:'id',
		columns:[[
		    {field:'ck',checkbox:true},
			{field:'id',hidden:true},
			{field:'name',title:'姓名',width:'18%',height:'50px',align:'center',halign:'center'},
			{field:'employeeId',title:'员工编号',width:'18%',height:'50px',align:'center',halign:'center'},
			{field:'loginName',title:'登录账号',width:'14%',height:'50px',align:'center'},
			{field:'tel',title:'联系电话',width:'14%',height:'50px',align:'center'},
			{field:'officeTel',title:'办公电话',width:'14%',height:'50px',align:'center'},
			{field:'privilege',title:'职务',width:'14%',height:'50px',align:'center'},
			{field:'status',title:'状态',width:'9%',height:'50px',align:'center',formatter:statusFormat},
		]],
		onLoadSuccess : function (data) {								
			if (data.total==0) {
				$("#roleUserEditDlg #roleOuterDiv .datagrid-view").css("height","100px");
				$("#roleUserEditDlg #roleOuterDiv .datagrid-body").css("height","50px");
				$('#roleUserEditDlg #roleOuterDiv tr[id^="datagrid-row-r"]').parent().append('<div style="width: 900px; text-align: center;">暂无数据</div>');
			}
		}
	});
}*/
//选项卡"角色外账户"的单击事件
/*document.getElementById('roleOuterTab').addEventListener("click",function(){
	document.getElementById('roleOuterTab').setAttribute('class','tab_li_rt active');
	document.getElementById('roleInnerTab').setAttribute('class','tab_li_lf ');
	document.getElementById('roleInnerDiv').style.display="none";
	document.getElementById('roleOuterDiv').style.display="block";
	loadUserOuterTable();
});*/

//角色内账号删除
/*document.getElementById('delInnerData').addEventListener('click',function(){
	var rows = 	roleUserManaObj.$roleInnerListTb.xdatagrid('getSelections');
	if (rows.length<=0) {
		$.xalert({title:'消息提示',msg:'请选择要从角色中删除的帐户！'});
		return;
	}
	
	var userIds = "";
	for(var index in rows){
		userIds += rows[index].id + ",";
	}
	
	userIds.substr(0,userIds.length-1);
	
	$.xconfirm({
		msg:'您确定要把选择的帐户从到角色中删除?',
		okFn: function() {
			$.post(
					baseUrl + "/role/roleAction!delUsrFromRole.action",
				{'dto.role.roleId': rId,
				 'dto.userIds':	userIds	},
				function(data) {
					if (data.indexOf("success")>=0) {
						roleUserManaObj.$roleInnerListTb.xdatagrid('reload');
						$.xalert({title:'消息提示',msg:'删除成功！'});
					} else {
						$.xalert({title:'消息提示',msg:'删除失败，请稍后再试！'});
					}
				},"text"
			);
		}
	});
});*/
//角色内账号查询弹出框
/*document.getElementById('queryInnerData').addEventListener('click',function(){
	roleUserManaObj.$queryUserInnerWin.xform('clear');
	roleUserManaObj.$queryUserInnerWin.parent().css("border","none");
	roleUserManaObj.$queryUserInnerWin.prev().css({ color: "#ffff", background: "#101010" });
	roleUserManaObj.$queryUserInnerWin.xwindow('setTitle','角色内账号查询').xwindow('open');
	
	roleUserManaObj.$queryUserInnerWin.xdeserialize({'dto.role.roleId' : groupId});
	$('#roleUser_Status').xcombobox('setValue', '-1');
	$('#roleUsergroup_Ids').xcombobox('setValue','-1');
});

//角色内账号查询重置
document.getElementById('resetUserInnerDlg').addEventListener('click',function(){
	roleUserManaObj.$queryUserInnerWin.xform('clear');
	
	roleUserManaObj.$queryUserInnerWin.xdeserialize({'dto.role.roleId' : groupId});
	$('#roleUser_Status').xcombobox('setValue', '-1');
	$('#roleUsergroup_Ids').xcombobox('setValue','-1');
});*/

//账号内角色查询
/*document.getElementById('queryUserInner').addEventListener('click',function(){
	 var	data = roleUserManaObj.$queryUserInnerWin.xserialize();
	 roleUserManaObj.$queryUserInnerWin.xwindow('close');
	   roleUserManaObj.$roleInnerListTb.xdatagrid('reload',data);
});*/
//@ sourceURL=userGroupMember.js