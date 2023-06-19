var objs = {  
	$addOrEditWin: $("#addOrEditWin"),
	$addOrEditForm: $("#addOrEditForm") 
};
$(function(){
	$.parser.parse();
//	loadMyInfo();
})

function loadMyInfo(){
	
	$.getJSON(
			baseUrl + "/userManager/userManagerAction!setMyInfoInitLoad.action",
			{},       
			function(data) {
				objs.$addOrEditWin.xdeserialize(data);
		 
				}) 
	
}

function submitUserInfo(){
	var name = $("#name").textbox("getValue");
	var rePwd = $("#rePwd").textbox("getValue");
	var password = $("#password").textbox("getValue");
	if(name==""){
		$.xalert({title:'验证失败',msg:'请输入真实姓名！'});
		return
	}
	if(rePwd==""){
		$.xalert({title:'验证失败',msg:'请确认密码！'});
		return
	}
	if(password==""){
		$.xalert({title:'验证失败',msg:'请输入密码！'});
		return
	}
	if(password!=rePwd){
		$.xalert({title:'验证失败',msg:'两次输入的密码不一致！'});
		return
	}
	$.post(  
			baseUrl + "/userManager/userManagerAction!updateMyInfo.action?dto.isAjax=true",
			objs.$addOrEditWin.xserialize(),
			function(data) { 
				if(data.indexOf("success")>-1){
					$.xalert({title:'修改成功',msg:'修改个人信息成功！'});
					loadMyInfo();
					
				}else{
					$.xalert({title:'修改失败',msg:'修改个人信息失败！'});
				}
			},"text"
		);
}

function resetInfo(){
	$.getJSON(
			baseUrl + "/userManager/userManagerAction!setMyInfoInitLoad.action",
			{},       
			function(data) {
				objs.$addOrEditWin.xdeserialize(data);
		 
				}) 
}
//# sourceURL=updateUserInfo.js
