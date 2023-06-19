var objs = {
	$menuTreeSwitchBtn: $("#menu_tree_switch_btn"),
	$userList001: $("#userList001"),
	$addOrEditWin: $("#addOrEditWinUser"),
	$addOrEditForm: $("#addOrEditFormUser"),
	$userGroup: $("#userGroup"),
	$pm: $("#pm"),
	$uploadForm: $("#uploadForm"),
	$testFlowWin: $("#testFlowWin"),
	$workflowForm: $("#workflowForm"),
	$versionMaint:$("#versionMaintenance")
};
var pwdFlag = '0';
var oldPwdFlag01 = '0';
var groupList = [];
var haveUploaded = "0";
$(function() {
	$.parser.parse();
	loadUserInfo();
	getLoginUserPower(); 
})
	
//获取当前用户的权限
function getLoginUserPower(){
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

/*$("#rePwd01").textbox('textbox').bind("click", function () { 
	$("#oldPwd").textbox("setValue","");
})*/


function loadUserInfo(){
	objs.$userList001.xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!userListLoad.action',
		method: 'get',
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
		columns:[[
			{field:'id',title:'选择', checkbox:true,align:'center'},
			{field:'name',title:'真实姓名',width:'10%',align:'center'},
			//{field:'employeeId',title:'员工编号',width:'18%',align:'center',halign:'center'},
			{field:'loginName',title:'登陆账号',width:'12%',align:'center',halign:'center'},
			{field:'tel',title:'联系电话',width:'12%',align:'center'},
			{field:'officeTel',title:'办公电话',width:'12%',align:'center',halign:'center'},
			{field:'email',title:'电子邮箱',width:'17%',align:'center'},
			{field:'headShip',title:'职务',width:'10%',align:'center'},
			{field:'status',title:'状态',width:'8%',align:'center',formatter:statusFormat},
			{field:'isAdmin',title:'是否为管理员',width:'12%',align:'center',formatter:isAdminFormat},
			{field:'employeeId',title:'员工编号',width:'8%',align:'center',halign:'center'}
		]],
		onClickRow:function (index, row) { 
			if(row.isAdmin=='0'){
				$("#adminBtn").css('display','');
				$("#adminBtn").text('设为管理员');
			} else if(row.isAdmin=='2'){
				$("#adminBtn").css('display','');
				$("#adminBtn").text('撤销管理员身份');
			}else{
				$("#adminBtn").css('display','none');
			} 
			     
			  },
			  onLoadSuccess: function (data) {
				    /*if (data.total == 0) {
				        var body = $(this).data().datagrid.dc.body2;
				        body.find('table tbody').append('<tr><td width="' + body.width() + '" style="height: 35px; text-align: center;"><h3>暂无数据</h3></td></tr>');
				    }*/
				} 
 
})	
	
}
$.extend($.fn.validatebox.defaults.rules,{  
    NotEmpty : { // 非空字符串验证。 easyui 原装required 不能验证空格  
                validator : function(value, param) {  
                    return $.trim(value).length>0;  
                },   
                message : '该输入项为必输项'  
            },  
    integer : {// 验证整数  
                validator : function(value) {  
                    return /^[+]?[0-9]+\d*$/i.test(value);  
                },  
                message : '请输入整数'  
            },            
     english : {// 验证英语  
                validator : function(value) {  
                    return /^[A-Za-z]+$/i.test(value);  
                },  
                message : '请输入英文'  
            } ,
            equals: {
                validator: function(value,param){
                    return value == $(param[0]).val();
                },
                message: 'Field do not match.'
            }
});
function resetInput(){
	$("input").val("");
	objs.$userList001.xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!userListLoad.action',
		method: 'get',
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
		queryParams:{
			  'dto.user.name': '', 
			  'dto.isAjax':true,
			  'dto.pageSize':'20',
			  'dto.user.loginName':'',
			  'dto.getPageNo':'1'
		},
		columns:[[
			{field:'id',title:'选择', checkbox:true,align:'center'},
			{field:'name',title:'真实姓名',width:'10%',align:'center'},
			//{field:'employeeId',title:'员工编号',width:'18%',align:'center',halign:'center'},
			{field:'loginName',title:'登陆账号',width:'12%',align:'center',halign:'center'},
			{field:'tel',title:'联系电话',width:'12%',align:'center'},
			{field:'officeTel',title:'办公电话',width:'12%',align:'center',halign:'center'},
			{field:'email',title:'电子邮箱',width:'17%',align:'center'},
			{field:'headShip',title:'职务',width:'10%',align:'center'},
			{field:'status',title:'状态',width:'8%',align:'center',formatter:statusFormat},
			{field:'isAdmin',title:'是否为管理员',width:'12%',align:'center',formatter:isAdminFormat},
			{field:'employeeId',title:'员工编号',width:'8%',align:'center',halign:'center'}
		]],
		onClickRow:function (index, row) { 
			if(row.isAdmin=='0'){
				$("#adminBtn").css('display','');
				$("#adminBtn").text('设为管理员');
			} else if(row.isAdmin=='2'){
				$("#adminBtn").css('display','');
				$("#adminBtn").text('撤销管理员身份');
			}else{
				$("#adminBtn").css('display','none');
			} 
			     
			  },
			  onLoadSuccess: function (data) {
				    /*if (data.total == 0) {
				        var body = $(this).data().datagrid.dc.body2;
				        body.find('table tbody').append('<tr><td width="' + body.width() + 'px"  colspan="10" style="height: 35px; text-align: center;"><h3>暂无数据</h3></td></tr>');
				    }*/
				} 
 
})
}
function statusFormat(value,row,index) {
	switch (value) {
	 
	case 1:
		return '启用';
	case 0:
		return '禁用';
	default:
		return '-';
	}
}
function isAdminFormat(value,row,index) {
	switch (value) {
	 
	case 1:
		return '是';
	case 0:
		return '否';
	case 2:
		return '是';
	default:
		return '-';
	}
}

//关闭弹窗
function closeWin(){
	objs.$addOrEditWin.xwindow('close');
	objs.$userList001.xdatagrid('reload');
}
function submit(param){
	var id = $("#id01").val();
/*//	var valid = $("#addOrEditWin").xform('validate');
//	if (!valid) {
//		return;
//	}
	 var pwd = $("#password").textbox('getValue');
	var rePwd = $("#rePwd").textbox('getValue'); 
	var loginName = $("#loginName").textbox('getValue'); 
	var pwd = $('input[name="dto.user.oldPwd"]').value;
	var rePwd = $('input[name="dto.user.password"]').value;
 
	var loginName = $('input[name="dto.user.loginName"]').value;
	//['0'].children['1'].value;.prev()['1'].value;
	 
	var name = $('input[name="dto.user.name"]').value;*/
	
	//获取表单数据 
	var objData = objs.$addOrEditWin.xserialize(); 
	var length = objData["dto.user.loginName"].length;
	if(length>0&&length<4){
		$.xalert({title:'提示',msg:"登陆账号不得少于4位！"});	
		return
	}else if(length>20){
		$.xalert({title:'提示',msg:"登陆账号不得超过20位！"});	
		return
	}
	if(!objData["dto.user.loginName"]){
		$.xalert({title:'验证失败',msg:'请输入登陆号！'});
        return
	} 
	if(!objData["dto.user.name"]){
		$.xalert({title:'验证失败',msg:'请输入真实姓名！'});
        return
	}
	if(!objData["dto.user.password"]){
		$.xalert({title:'验证失败',msg:'请输入密码！'});
        return
	}
	if(!objData["repwd"]){
		$.xalert({title:'验证失败',msg:'请确认密码！'});
        return
	} 
	
	if(objData["repwd"]==objData["dto.user.password"]&&objData["dto.user.password"]!=""){
		if(id!=""){//upd
			if(pwdFlag=='1'){
				objData["dto.user.chgPwdFlg"]=id;
			}
			if(pwdFlag=='0'&&oldPwdFlag01=='1'){
				objData["dto.user.chgPwdFlg"]='2';
			}
			if(pwdFlag=='0'&&oldPwdFlag01=='0'){
				objData["dto.user.chgPwdFlg"]="";
			}
			/*if(pwdFlag=='0'&&oldPwdFlag01=='1'){
				objData["dto.user.chgPwdFlg"]='0';
			}
			else{objData["dto.user.chgPwdFlg"]=null;
			}*/
			$.post(  
					baseUrl + "/userManager/userManagerAction!userMaintence.action?dto.isAjax=true",
					objData,
					function(data) { 
						if(data.indexOf("success")>-1){
							$.xalert({title:'修改成功',msg:'修改用户成功！'});
							objs.$addOrEditWin.xwindow('close');
	                        objs.$userList001.xdatagrid('reload');
						}else if(data.indexOf("原密码不正确")>-1){
							$.xalert({title:'修改失败',msg:'原密码不正确！'});
						}else if(data.indexOf("existed")>-1){
							$.xalert({title:'修改失败',msg:'该登陆名已存在，请勿重复添加！'});
						}else{
							$.xalert({title:'修改失败',msg:'修改用户失败！'});
						}
					},"text"
				);	
		}else{
			$.post(
					baseUrl + "/userManager/userManagerAction!addUser.action?dto.isAjax=true",
					objs.$addOrEditWin.xserialize(),
					function(data) { 
						if(data.indexOf("success")>-1){
							$.xalert({title:'新增成功',msg:'新增用户成功！'});

							if(param=='continue'){ 
								objs.$addOrEditWin.xform('clear');
							}else{
								objs.$addOrEditWin.xwindow('close');
		                        objs.$userList001.xdatagrid('reload');	
							}
							 
						}else if(data.indexOf("existed")>-1){
							$.xalert({title:'新增失败',msg:'该登陆名已存在，请勿重复添加！'});
						}else{
							$.xalert({title:'新增失败',msg:'新增用户失败！'});
						}
					},"text"
				);
		}
	
	}else{
		$.xalert({title:'验证失败',msg:'两次输入的密码不一致！'});
        return
	}
	
}
//删除用户
function showdelConfirm(){
	var row = objs.$userList001.xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'提示',msg:'请选择要删除的一条记录'});
		/*$.xnotify('请选择要删除的一条记录', {type:'warning'});*/
		return;
	} 
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			if(row.isAdmin=='2'||row.isAdmin=='1'){
				$.xalert({title:'提示',msg:'管理员不可删除！'});
				/*$.xnotify('管理员不可删除！', {type:'warning'});*/
				return;
			}
			 $.post(
					baseUrl + "/userManager/userManagerAction!ldeleteUser.action",
				{'dto.user.id': row.id,
					'dto.isAjax':true},
				function(data) {
						if(data.indexOf("success")>-1){
							  $.xalert({title:'成功',msg:'删除成功！'}); 
							  objs.$userList001.xdatagrid('reload'); 
						  }else if(data=="haveBug"){
							  $.xalert({title:'失败',msg:'该用户有bug未处理，不能删除！'}); 
						  }else{
							  $.xalert({title:'失败',msg:'删除失败！'});
						  }
				}
		    ,'text');
		}
	});
	
}
function showAddWin(){
	objs.$addOrEditWin.xform('clear');

	objs.$addOrEditWin.parent().css("border","none");
	objs.$addOrEditWin.prev().css({ color: "#ffff", background: "#101010" });
	$(".addShow").css('display','inline');
	$(".editShow").css('display','none');
/*	$("#groupNames01").css('display','none');*/
	/*$("#loginName").removeAttr("readonly");
	$("#loginName").removeAttr("disabled"); */
	/*$('#loginName').xtextbox('textbox').attr('readonly',true);*/
	//查询组名
	checkGroups();
	$('input[name="dto.user.loginName"]').prev().attr('disabled',false);
	$("#continueBtn").css('display','inline-block');
	objs.$addOrEditWin.xwindow('setTitle','新建用户').xwindow('open');
	
}
function checkGroups(){
	$.post(
			 baseUrl + "/userManager/userManagerAction!choiseGroups.action",
			 {'dto.isAjax':true},
			  function(data){ 
				 if(data.length>0){  
					 groupList = data;
			    		$("#groupNames01").xcombobox({
							data:groupList
						});	
			    	}else{ 
			    	} 
			  },"json"
			) 
}
function showEditWin(){
	pwdFlag='0';
	oldPwdFlag01='0';
	var row = objs.$userList001.xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'提示',msg:'请选择要修改的一条记录！'});
		/*$.xnotify('请选择要修改的一条记录', {type:'warning'});*/
		return;
	}
	objs.$addOrEditWin.xform('clear');
	$.getJSON(
	baseUrl + "/userManager/userManagerAction!userInitData.action",
	{'dto.user.id': row.id,
		'dto.isAjax':true},       
	function(data) {
		var groupIdList = [];
		objs.$addOrEditWin.xdeserialize(data);
		
		$("#id").val(row.id);
		objs.$addOrEditWin.parent().css("border","none");
		objs.$addOrEditWin.prev().css({ color: "#ffff", background: "#101010" });
		$(".addShow").css('display','none');
		$(".editShow").css('display','block');
		/*$("#password").val('4567KK6slygjk');
		$("#oldPwd").val('4567KK6slygjk');*/
		$("#password01").xtextbox("setValue", '4567KK6slygjk');
		$("#oldPwd01").xtextbox("setValue", '4567KK6slygjk');
		$("#rePwd02").xtextbox("setValue",'4567KK6slygjk');
		/*$("#groupNames01").css('display','inline');
		$("#groupNames01").attr('readonly','readonly');*/ 
		$('input[name="dto.user.loginName"]').prev().attr('disabled','disabled');
		checkGroups();
		if(data['dto.user'].groupIds!=null){
			if(data['dto.user'].groupIds.length>0){
				groupIdList = data['dto.user'].groupIds.split(",");
				$("#groupNames01").xcombobox("setValues",groupIdList);	
			}
		 }
		$("#continueBtn").css('display','none');
		objs.$addOrEditWin.xwindow('setTitle','修改用户信息').xwindow('open');
	}
);
	
	
}


//设置or撤销管理员身份
function setAsAdmin(){
	var row = objs.$userList001.xdatagrid('getSelected');
	$.post(
	 baseUrl + "/userManager/userManagerAction!chgPersonMgr.action",
	 {'dto.userIds': row.id,
	  'dto.user.isAdmin':row.isAdmin,
	  'dto.isAjax':true},
	  function(data){ 
		  if(data.indexOf("success")>-1){
			  $.xalert({title:'成功',msg:'设置成功！'}); 
			  objs.$userList001.xdatagrid('reload'); 
		  }else if(data.indexOf("deny")>-1){
			  $.xalert({title:'失败',msg:'只有超级管理员才有此权限！'}); 
		  }else{
			  $.xalert({title:'失败',msg:'设置失败！'});
		  }
	  },"text"
	)
}
//切换状态
function toggoleStatus(){
	var row = objs.$userList001.xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'提示',msg:'请选择记录'});/*
		$.xnotify('请选择记录', {type:'warning'});*/
		return;
	}
	if(row.status=='1'){
		$.xconfirm({
			msg:'您确定禁用选择的用户吗?',
			okFn: function() {
				$.post(
						 baseUrl + "/userManager/userManagerAction!swUserStatus.action",
						 {'dto.user.id': row.id,
						  'dto.user.status':'0',
						  'dto.isAjax':true},
						  function(data){ 
							  if(data.indexOf("success")>-1){
								  $.xalert({title:'成功',msg:'设置成功！'}); 
								  objs.$userList001.xdatagrid('reload'); 
							  }else{
								  $.xalert({title:'失败',msg:'设置失败！'}); 
							  }
						  },"text"
						)
			}
		});
		 
	}else{
		$.xconfirm({
			msg:'您确定启用选择的用户吗?',
			okFn: function() {
				$.post(
						 baseUrl + "/userManager/userManagerAction!swUserStatus.action",
						 {'dto.user.id': row.id,
						  'dto.user.status':'1',
						  'dto.isAjax':true},
						  function(data){ 
							  if(data.indexOf("success")>-1){
								  $.xalert({title:'成功',msg:'设置成功！'}); 
								  objs.$userList001.xdatagrid('reload'); 
							  }else{
								  $.xalert({title:'失败',msg:'设置失败！'}); 
							  }
						  },"text"
						)
			}
		}); 
	}

	
	 
}

//密码重置 
function resetPassword(){
	var row = objs.$userList001.xdatagrid('getSelected'); 
	if (!row) {
		$.xalert({title:'提示',msg:'请选择要重置的记录'});/*
		$.xnotify('请选择要重置的记录', {type:'warning'});*/
		return;
	}
		$.xconfirm({
			msg:'您确定要重置所选用户密码为"itest"吗?',
			okFn: function() {
				$.post(
						 baseUrl + "/userManager/userManagerAction!update2Init.action",
						 {'dto.userIds': row.id, 
						  'dto.isAjax':true},
						  function(data){ 
							  if(data.indexOf("success")>-1){
								  $.xalert({title:'成功',msg:'重置密码成功！'}); 
								  objs.$userList001.xdatagrid('reload'); 
							  }else{
								  $.xalert({title:'失败',msg:'重置密码失败！'}); 
							  }
						  },"text"
						)
			}
		});
		 
}
//查询用户信息
function searchUserInfo(){ 
	var loginName = $("#loginNameSearch").val();
	var userName = $("#userNameSearch").val();
	objs.$userList001.xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!findUsers.action',
		method: 'post',
		queryParams:{
			  'dto.user.name': userName, 
			  'dto.isAjax':true,
			  'dto.pageSize':'20',
			  'dto.user.loginName':loginName,
			  'dto.getPageNo':'1'
		},
		height: mainObjs.tableHeight,
		columns:[[
			{field:'id',title:'选择', checkbox:true,align:'center'},
			{field:'name',title:'真实姓名',width:'10%',align:'center'},
			//{field:'employeeId',title:'员工编号',width:'18%',align:'center',halign:'center'},
			{field:'loginName',title:'登陆账号',width:'12%',align:'center',halign:'center'},
			{field:'tel',title:'联系电话',width:'12%',align:'center'},
			{field:'officeTel',title:'办公电话',width:'12%',align:'center',halign:'center'},
			{field:'email',title:'电子邮箱',width:'17%',align:'center'},
			{field:'headShip',title:'职务',width:'10%',align:'center'},
			{field:'status',title:'状态',width:'8%',align:'center',formatter:statusFormat}, 
			{field:'isAdmin',title:'是否为管理员',width:'12%',align:'center',formatter:isAdminFormat},
			{field:'employeeId',title:'员工编号',width:'8%',align:'center',halign:'center'}
		]], 
		onClickRow:function (index, row) { 
			if(row.isAdmin=='0'){
				$("#adminBtn").css('display','');
				$("#adminBtn").text('设为管理员');
			} else if(row.isAdmin=='2'){
				$("#adminBtn").css('display','');
				$("#adminBtn").text('撤销管理员身份');
			}else{
				$("#adminBtn").css('display','none');
			} 
			     
			  }
 
})
	
} 


$("#password01").textbox('textbox').bind("change", function () { 
	pwdFlag = '1'; 
})
function changeFlag(){
pwdFlag = '1';
}

 function pwdClear(){
	 
} 
$("#rePwd02").textbox('textbox').bind("change", function () { 
if(pwdFlag=='1'){
		$("#oldPwd01").xtextbox('setValue','');
} 
})

$("#oldPwd01").textbox('textbox').bind("change", function () { 
	oldPwdFlag01 = '1'; 
})
document.getElementById('userManageTool').onkeydown=function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0]; 
    if (e && e.keyCode == 13) {//keyCode=13是回车键；数字不同代表监听的按键不同
    	 searchUserInfo(); 
    		 
   	  /*roleManaObjs.$roleListTb.xdatagrid('load',data);*/
    }
};

//打开从excel导入测试用例弹窗
function upload(){
	haveUploaded = "0";
	$("#uploadForm input").val("");
	$("#uploadFromExcelWin").xwindow('setTitle','导入').xwindow('open');
	$("#importFile").fileinput("destroy");
	$("#importFile").fileinput({
		theme: 'fa',
		language:'zh',
		uploadUrl:baseUrl + '/userManager/userManagerAction!importUsers.action', // you must set a valid URL here else you will get an error
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
		if(haveUploaded == "0"){
			$("#uploadFromExcelWin").xwindow('close');
			closeUploadWin(previewId);
			objs.$userList001.xdatagrid('reload'); 
			haveUploaded = "1";
		}
	}).on('filebatchuploaderror', function(event, data,  previewId, index) {
		if(haveUploaded == "0"){
			$("#uploadFromExcelWin").xwindow('close');
			closeUploadWin(previewId);
			objs.$userList001.xdatagrid('reload'); 
			haveUploaded = "1";
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

//确认导入（Excel）
function sureToUpload(){
	if(!$(".file-caption-name").val()){
		$.xalert({title:'提示',msg:'请选择需要导入的Excel文件！'});
		return;
	}
	$("#importFile").fileinput("upload");
}

//关闭从excel导入测试用例弹窗
function closeUploadFromExcelWin(){
	$("#uploadForm input").val("");
	$("#uploadFromExcelWin").xwindow('close');
}

//关闭导入弹窗
function closeUploadWin(data){
	$("#uploadFromExcelWin").xwindow('close');
	if(data.split("<pre>")[1].split("</pre>")[0] != "success"){
		$.xalert({title:'提示',msg:data.split("<pre>")[1].split("</pre>")[0]});
	}else{
		$.xalert({title:'提示',msg:"导入成功！"});
	}
}

//下载Excel导入摸板
function downloadExcelModel(obj){
	var href = baseUrl + '/documents/userTemplet.xls';
	$(obj).prop('href', href);
}
 
//# sourceURL=userManager.js