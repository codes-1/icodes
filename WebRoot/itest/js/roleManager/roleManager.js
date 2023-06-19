var roleManaObjs = {
		$roleListTb:$("#roleListTb"),
		$addOrEditRoleWin:$("#addOrEditRoleWin"),
		$addOrEditRoleForm:$("#addOrEditRoleForm"),
		$queryRoleWin:$("#queryRoleWin")
}

$(function(){
	
	$.parser.parse();
	getRoleManagerAuth();
	//初始化角色列表
	roleManaObjs.$roleListTb.xdatagrid({
		url: baseUrl + '/role/roleAction!loadRoleList.action',
		method: 'post',
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
		columns:[[
			{field:'roleId',title:'选择',checkbox:true,align:'center'},
			{field:'roleName',title:'角色名',width:'18%',align:'center',halign:'center'},
			{field:'userVoSet',title:'人员',width:'48%',align:'center',halign:'center',formatter:proNameFormat},
			{field:'remark',title:'备注',width:'14%',align:'center'},
			{field:'operate',title:'操作',width:'19%',align:'center',enable:false,formatter:operatFormat},
		]],
		onLoadSuccess : function (data) {								
			/*if (data.total==0) {
				  var body = $(this).data().datagrid.dc.body2;
			        body.find('table tbody').append('<tr><td>&nbsp;</td><td width="' + body.width() + '" style="height: 35px; text-align: center;"><h3>暂无数据</h3></td></tr>');
			}*/
			roleManaObjs.$roleListTb.xdatagrid("resize");
		}
	});
	
});

//获取当前用户的权限
function getRoleManagerAuth(){
	var controlButton = $('button[schkUrl]');
	$.each(controlButton,function(i,n){
		var controId = controlButton[i].id;
		var controlUrl = $(controlButton[i]).attr('schkUrl');
		if(privilegeMap[controlUrl]!="1"){
			$("#"+controId).hide(); 
		}
	});
}

//显示'人员'列的数据
function proNameFormat(value,row,index){
	var contextTemp = "";
	var contextStr = "";
	if(value != null && value != ""){
		for(var i=0;i<value.length;i++){
			contextTemp = contextTemp + value[i].name + "、";
		}
		contextTemp = contextTemp.substr(0,contextTemp.length-1);
		if(contextTemp.length>43){
			contextStr = contextTemp.substr(0,43) + "...";
		}else{
			contextStr = contextTemp;
		}	
	}else{
		return ;
	}
		
	return "<div title='" + contextTemp + "'>" + contextStr + "</div>";
}

//"操作"列
function operatFormat(value,row,index){
	var columnStr = "<div>";
	
	if(privilegeMap['roleAction!userManager']!="1" && privilegeMap['roleAction!roleUserList']!="1"){
		columnStr +="-";
	}else{
		if(privilegeMap['roleAction!roleUserList']=="1"){
			columnStr += "<a type='button' id='roleInfBtn' onclick='showRoleInfDlg(\""+ row.roleId + "\")' style='cursor:pointer;padding:2px 5px!important;margin: 5px 20px 5px 0;color:#1e7cfb' schkUrl='roleAction!roleUserList'>" +
	        "人员明细</a>" ;
		}
		if(privilegeMap['roleAction!userManager']=="1"){
			columnStr +=  "<a type='button' id='editRoleInfBtn' onclick='showEditRoleInfDlg(\""+ row.roleId + "\")'  style='cursor:pointer;padding:2px 5px!important;margin: 5px 0px 5px 0;color:#1e7cfb' schkUrl='roleAction!userManager' >" +
	        "人员维护</a>";
		}
	}
	 columnStr += "</div>";
	return columnStr;
}

//打开新增角色信息弹窗
function showAddRoleWin() {
	roleManaObjs.$addOrEditRoleWin.xform('clear');
	roleManaObjs.$addOrEditRoleWin.parent().css("border","none");
	roleManaObjs.$addOrEditRoleWin.prev().css({ color: "#ffff", background: "#101010" });
	roleManaObjs.$addOrEditRoleWin.xwindow('setTitle','新建角色').xwindow('open');
}

//新增或修改角色信息
function submitRole(){

	var roleName = $("#roleName").textbox('getValue');
	var accessIp =  $("#accessIp").textbox('getValue');
	if(roleName ==""){
		$.xalert({title:'消息提示',msg:'请输入角色名称！'});
        return;
	}
	
	/*if(accessIp ==""){
		$.xalert({title:'消息提示',msg:'请输入可访问Ip！'});
        return;
	}else if(!ipChk()){
		return;
	}*/
	
	var urlRuestq = "";
	var tipStr = "" ;
	var errorTipStr = "";
	//通过roleId判断是新增还是修改角色信息
	var roleId =  $("#roleId").textbox('getValue');
	if($.trim(roleId)!=null && $.trim(roleId)!=""){
		urlRuestq = baseUrl + "/role/roleAction!updRole.action?dto.isAjax=true";
		tipStr = "修改角色信息成功";
		errorTipStr = "修改角色信息失败，请稍后重试";
	}else{
		urlRuestq = baseUrl + "/role/roleAction!addRole.action?dto.isAjax=true";
		tipStr = "新增角色信息成功";
		errorTipStr = "新增角色信息失败，请稍后重试";
	}
	
	//提交角色信息
	postRoleInfo(urlRuestq,tipStr,errorTipStr); 

}


//提交角色信息
function postRoleInfo(urlStr,tipStr,errTipStr){
	var roleName = $("#roleName").textbox('getValue');
	$.post(
			urlStr,
			roleManaObjs.$addOrEditRoleWin.xserialize(),
			function(dataObj) {
		      if(dataObj.indexOf("success")>=0){
		    	    $.xalert({title:'消息提示',msg:tipStr});
					roleManaObjs.$addOrEditRoleWin.xwindow('close');
					roleManaObjs.$roleListTb.xdatagrid('reload');
				}else{
					if(dataObj.indexOf("reName")>=0){
						$.xalert({title:'消息提示',msg:"角色名称'" + roleName + "'已存在,请更换"});
					}else{
						$.xalert({title:'消息提示',msg:errTipStr});
					}
					
				}
			},"text"
		);
}

function ipChk(){
	var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	var accessIp=$("#accessIp").textbox('getValue')
	var ips = accessIp.split(';');
	if(ips.length==1){
		var reg = accessIp.match(exp);
		if(reg==null){
			$.xalert({title:'消息提示',msg:'非法的IP地址！'});
			return false;
		}else{
			return true;
		}		
	}
	for(var i=0; i<ips.length; i++){
	 	var ip = ips[i];
	 	if(ip.match(exp)==null){
	 		$.xalert({title:'消息提示',msg:"第" +(i+1) +"个IP为非法地址"});
	 		return false;
	 	}
	}
	return true;
} 
//打开修改角色信息弹窗
function showEditRoleWin() {
	var row = roleManaObjs.$roleListTb.xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'消息提示',msg:'请选择要修改的一条记录'});
		return;
	}
	
	$.getJSON(
		baseUrl + "/role/roleAction!updRoleInit.action?dto.isAjax=true",
		{'dto.role.roleId': row.roleId},
		function(data) {
			roleManaObjs.$addOrEditRoleWin.xdeserialize(data);
	        roleManaObjs.$addOrEditRoleWin.parent().css("border","none");
	        roleManaObjs.$addOrEditRoleWin.prev().css({ color: "#ffff", background: "#101010" });
	        roleManaObjs.$addOrEditRoleWin.xwindow('setTitle','修改角色信息').xwindow('open');
		}
	);
}


//删除某行角色信息
function showDelRoleConfirm(){
	var row = roleManaObjs.$roleListTb.xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'消息提示',msg:'请选择要删除的一条记录'});
		return;
	}
	
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			$.post(
				baseUrl + "/role/roleAction!deleteRole.action?dto.isAjax=true",
				{'dto.role.roleId': row.roleId},
				function(data) {
					if (data.indexOf("success")>=0) {
						roleManaObjs.$roleListTb.xdatagrid('reload');
						$.xalert({title:'消息提示',msg:'删除成功'});
					} else {
						$.xalert({title:'消息提示',msg:'删除失败，请稍后再试'});
					}
				},"text"
			);
		}
	});
}

//打开人员明细对话框
function showRoleInfDlg(rId){
	    $("<div></div>").xdialog({
	    	id:'roleInfDlg',
	    	title:"查看账户列表",
	    	width : 1000,
            height : 600,
	    	modal:true,
	    	href:baseUrl + "/role/roleAction!roleUserList.action",
	    	queryParams:{'roleId' :rId},
            onClose : function() {
                $(this).dialog('destroy');
                //账号管理子页面中的弹出框需要手动删除，否则会导致重复出现id=queryUserFooter的div
                var roleUserWin = document.getElementById('queryUserFooter').parentNode;
                document.body.removeChild(roleUserWin);
            	
            }
	    });
	   
}

//打开人员维护对话框
function showEditRoleInfDlg(rId){
    $("<div></div>").xdialog({
    	id:'roleUserEditDlg',
    	title:"维护账户列表",
    	width : 1000,
        height : 600,
    	modal:true,
    	href:baseUrl + "/role/roleAction!userManager.action",
    	queryParams:{'roleId' :rId},
        onClose : function() {
            $(this).dialog('destroy');
            //账号管理子页面中的弹出框需要手动删除，否则会导致重复出现id=queryUserManaFooter的div
            var userInnerWin = document.getElementById('queryUserInnerFooter').parentNode;
            document.body.removeChild(userInnerWin);
            var userOuterWin = document.getElementById('queryUserOuterFooter').parentNode;
            document.body.removeChild(userOuterWin);
            roleManaObjs.$roleListTb.xdatagrid('reload');
        }
    });
    
}

//关闭'新增或修改角色信息'弹出框
function closeRoleWin(){
	roleManaObjs.$addOrEditRoleWin.xwindow('close');
}

//权限查看
document.getElementById('authDetail').addEventListener('click',function(){
	var row = roleManaObjs.$roleListTb.xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'消息提示',msg:'请选择要查看的一条记录'});
		return;
	}
	  $("<div></div>").xdialog({
	    	id:'authDetailDlg',
	    	title:"权限查看",
	    	width : 400,
	        height : 450,
	    	modal:true,
	    	href:baseUrl + "/role/roleAction!browserAuth.action",
	    	queryParams:{'roleId' :row.roleId},
	        onClose : function() {
	            $(this).dialog('destroy');
	        }
	    });
});

//权限维护
document.getElementById('authEdit').addEventListener('click',function(){
	var row = roleManaObjs.$roleListTb.xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'消息提示',msg:'请选择要查看的一条记录'});
		return;
	}
	  $("<div></div>").xdialog({
	    	id:'authEditDlg',
	    	title:"权限维护",
	    	width : 400,
	        height : 450,
	    	modal:true,
	    	href:baseUrl + "/role/roleAction!authGrant.action",
	    	queryParams:{'roleId' :row.roleId},
	        onClose : function() {
	            $(this).dialog('destroy');
	        }
	    });
});

//显示查询弹出框
/*document.getElementById('queryRole').addEventListener('click',function(){
	 var data = {
			  "dto.role.roleName": document.getElementById("queryParam").value  
	  };
	  roleManaObjs.$roleListTb.xdatagrid('load',data);
});*/

document.getElementById('roleManageTool').onkeydown=function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0]; 
    if (e && e.keyCode == 13) {//keyCode=13是回车键；数字不同代表监听的按键不同
    	 var data = {
   			  "dto.role.roleName": document.getElementById("queryParam").value  
   	  };
   	  roleManaObjs.$roleListTb.xdatagrid('load',data);
    }
};

//@ sourceURL=roleManager.js