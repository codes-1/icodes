var objs = {
	$menuTreeSwitchBtn: $("#menu_tree_switch_btn"),
	$userGroupList001: $("#userGroupList001"),
	$addOrEditGroupWin: $("#addOrEditGroupWin"),
	$addOrEditGroupForm: $("#addOrEditGroupForm"),
	$userGroup: $("#userGroup"),
	$pm: $("#pm"),
	$uploadForm: $("#uploadForm"),
	$testFlowWin: $("#testFlowWin"),
	$workflowForm: $("#workflowForm"),
	$versionMaint:$("#versionMaintenance"),
	$addOrEditUserWin:$("#addOrEditUserWin"),
	$userAllSelect: $("#userAllSelect"),
	$userSelectAdd:$("#userSelectAdd")

}; 
var userList = [];
$(function() {
	$.parser.parse();
	loadGroupInfo(); 
	loadUserSelect();//combobox下拉数据加载
	getLoginUserPower();
	$(".searchLogo").hover(function(){
		$(this).attr("src",baseUrl+"/itest/images/mSearch1.png");
	},function(){
		$(this).attr("src",baseUrl+"/itest/images/mSearch.png");
	});
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

function loadUserSelect(){
	//获取user数据
	
    $.getJSON(baseUrl + '/userManager/userManagerAction!loadDefaultSelUserInAll.action?dto.getPageNo=1&dto.pageSize=100',
    	function(data){
    	if(data.total>0){
    		userList = data.rows;
    		$(".userNameWin").xcombobox({
				data:userList
			});
    		$(".userNameAddWin").xcombobox({
				data:userList
			});
    		
    	}else{
    		$("#userNameWin").xcombobox({
				data:[]
			});
    		$("#userNameAddWin").xcombobox({
				data:[]
			});
    	}
//    		var option="<option value="+""+">请选择组员</option>";
/*    	    var option="";
	        for(i=0;i<data.total;i++){
	        	option += "<option value="+data.rows[i].keyObj+">"
	        	+data.rows[i].valueObj+"</option>";
	        }
	        $("#userNameWin").html(option);
	        $("#userNameWin").chosen({
	        	search_contains:true,
	        	no_results_text:'没有该成员！',
	        	max_selected_options:2,
	        	disable_search_threshold:8
	        });
	        $("#userNameWin_chosen").find("input").attr('placeholder','请选择组员');
            $(".chosen-choices").css('overflow-y','scroll');
            $(".chosen-choices").css('max-height','100px');
	        $("#userNameWin").bind("chosen:maxselected",function (e) {
	        	$.xalert({title:'提示',msg:'最多只能选择两名组员！'});
	            return;
	        });*/
})	
}

function loadGroupInfo(){
	
	objs.$userGroupList001.xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!groupListLoad.action',
		method: 'get',
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
		columns:[[
			{field:'id',title:'选择', checkbox:true,align:'center'},
			{field:'name',title:'组名',width:'20%',align:'center'},
			{field:'user',title:'组员',width:'45%',align:'left',halign:'center',formatter:usersFormat},
			{field:'remark',title:'备注',width:'15%',align:'center',halign:'center'},
			{field:'operate',title:'操作',width:'20%',align:'center',enable:false,formatter:operatFormat},
			{field:'userIds',hidden:true,title:'备注',width:'15%',align:'center',halign:'center',formatter:userIdsFormat}
//			{field:'status',title:'状态',width:'8%',align:'center',formatter:statusFormat}
		]],
		  onLoadSuccess: function (data) {
			    /*if (data.total == 0) {
			        var body = $(this).data().datagrid.dc.body2;
			        body.find('table tbody').append('<tr><td>&nbsp;</td><td width="' + body.width() + '" style="height: 35px; text-align: center;"><h3>暂无数据</h3></td></tr>');
			    }*/
			} 
	});
}   

function usersFormat(value,row,index) {
	userIds= new Array();
	var user = value;
	if(user==null){
		return "-"
	}
	for(i=0;i<user.length;i++){
		userIds.push(value[i].id)
		user[i]=user[i].name;
		user[i] = JSON.stringify(user[i]).replace(/\"/g, "");
	}
	
	
	return value
}
function userIdsFormat(value,row,index) {
//	value = userIds;
	return userIds
	
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

function submit(){
	var valid = $("#addOrEditGroupWin").xform('validate');
	if (!valid) {
		return;
	}
	var objData = objs.$addOrEditGroupWin.xserialize(); 
	if(!objData["dto.group.name"]){
		$.xalert({title:'提交失败',msg:'请填写组名！'});
        return
	} 
	 if(objData["dto.group.userIds"]){
		objData["dto.group.userIds"] = objData["dto.group.userIds"].replace(/,/g, " "); 
		$.post(
				baseUrl + "/userManager/userManagerAction!addGroup.action?dto.isAjax=true",
				objData,
				function(data) {
					if(data.indexOf("success")>-1){
						$.xalert({title:'成功',msg:'操作成功！'});
						objs.$addOrEditGroupWin.xwindow('close');
						objs.$userGroupList001.xdatagrid('reload'); 
					}else if(data.indexOf("reName")>-1){
						$.xalert({title:'失败',msg:'该组名已存在，请勿重复添加！'});
					}else{
						$.xalert({title:'失败',msg:'操作失败！'});
					}
				},"text"
			);
	 }else{
		 $.xalert({title:'提交失败',msg:'请添加成员！'});
	 }
	 
	
} 
function showAddGroupWin(){
	objs.$addOrEditGroupWin.xform('clear');
	$("#addOrEditGroupWin input").val(""); 
	objs.$addOrEditGroupWin.parent().css("border","none");
	objs.$addOrEditGroupWin.prev().css({ color: "#ffff", background: "#101010" });
	 
	
    
   // 
	objs.$addOrEditGroupWin.xwindow('setTitle','新建组').xwindow('open');
	
}   
//取消提交并关闭弹窗
function closeWin() {
	objs.$addOrEditGroupWin.xwindow('close');
}

function closeUserWin() {
	objs.$addOrEditUserWin.xwindow('close');
} 

//删除
function showdelGroupConfirm(){
	var row = objs.$userGroupList001.xdatagrid('getSelected');
    if (!row) {
    	$.xalert({title:'提示',msg:'请选择一条要修改的记录'});/* 
		$.xnotify('请选择要修改的一条记录', {type:'warning'});*/
		return;
	}	
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() { 
			 $.post(
					baseUrl + "/userManager/userManagerAction!delGroup.action",
					{'dto.group.id': row.id,
						'dto.isAjax':true,
						'dto.group.name':row.name,
						'dto.group.userIds':''},
				function(data) {
						if(data.indexOf("success")>-1){
							  $.xalert({title:'成功',msg:'删除成功！'}); 
							  objs.$userGroupList001.xdatagrid('reload'); 
						  }else{
							  $.xalert({title:'失败',msg:'删除失败！'});
						  }
				}
		    ,'text');
		}
	});
	
	
}

//小窗口查找组员
function searchUsers(){
	var userName = $("#selUserName").val();
	  /* 候选人员列表*/
	    objs.$userAllSelect.xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!selectUser.action',
		method: 'get',
		height: 410,
		queryParams:{
			'dto.isAjax':true,
			'dto.userName':userName,
			'dto.pageSize':'20',
			'dto.getPageNo':'1'
		},
		columns:[[
			{field:'keyObj',title:'id',hidden:true,checkbox:true,align:'center'},
			{field:'valueObj',title:'备选人员--双击选择',width:'100%',align:'center'}

		]],
		onDblClickRow: function (index, row) {
//		   alert(JSON.stringify(row));
			 
		   var Ele = []//新建一个空的数组  
		   Ele.push(row);//把拆分好的数据用push的方法保存到数组Ele中。  
		   objs.$userAllSelect.xdatagrid('deleteRow',index)//根据索引删除对应的行。  
		   objs.$userSelectAdd.xdatagrid('insertRow',{//在右边插入新行。  
		   index : 0,  
		   row : {  
			   'keyObj' : Ele[0].keyObj  ,
			   'valueObj' : Ele[0].valueObj
		    }  
		   })  
		  }
	});	
}
//修改
function showEditGroupWin(){

	var row = objs.$userGroupList001.xdatagrid('getSelected');
    if (!row) {
    	$.xalert({title:'提示',msg:'请选择一条要修改的记录'});/*
		$.xnotify('请选择要修改的一条记录', {type:'warning'});*/
		return;
	}
  
	$.getJSON(
	baseUrl + "/userManager/userManagerAction!groupUpInit.action",
	{'dto.group.id': row.id,
		'dto.isAjax':true},
	function(data) { 
		objs.$addOrEditGroupWin.xform('clear');
		objs.$addOrEditGroupWin.xdeserialize(data);
		var user = data.user;
		var userLi = [];
		for(i=0;i<user.length;i++){
			userLi.push(user[i].id);
		}
		$(".userNameAddWin").xcombobox("setValues",userLi);
		$("#groupName").val(data.name); 
		$("#remark").val(data.remark); 
		$("#groupId01").val(data.id);
		objs.$addOrEditGroupForm.parent().css("border","none");
		objs.$addOrEditGroupWin.prev().css({ color: "#ffff", background: "#101010" });
		objs.$addOrEditGroupWin.xwindow('setTitle','修改组').xwindow('open');
			
		/*objs.$addOrEditGroupForm.xform('clear');
		$("#addOrEditGroupWin input").val("");  
		var user = data.user;  
	    objs.$addOrEditGroupForm.xdeserialize(data);
		objs.$addOrEditGroupForm.parent().css("border","none");
		objs.$addOrEditGroupWin.prev().css({ color: "#ffff", background: "#101010" });
		objs.$addOrEditGroupWin.xwindow('setTitle','修改组').xwindow('open');
		
		 
		$("#userNames").val(userIds01);
		$("#groupId").val(data.id);
		$("#groupName").val(data.name); 
		$("#remark").val(data.remark); 
		
		$("#userIds").val();*/
	}
);
}
function resetGroup(){
	$("input").val(""); 
	objs.$userGroupList001.xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!groupListLoad.action',
		method: 'get',
		queryParams:{
			  'dto.isAjax':true,
			  'dto.pageSize':'8',
			  'dto.group.name':'',
			  'dto.group.userIds':'',
			  'dto.getPageNo':'1'
		},
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
		columns:[[
			{field:'id',title:'选择', checkbox:true,align:'center'},
			{field:'name',title:'组名',width:'20%',align:'center'},
			{field:'user',title:'组员',width:'65%',align:'left',halign:'center',formatter:usersFormat},
			{field:'remark',title:'备注',width:'15%',align:'center',halign:'center'},
			{field:'userIds',hidden:true,title:'备注',width:'15%',align:'center',halign:'center',formatter:userIdsFormat}
//			{field:'status',title:'状态',width:'8%',align:'center',formatter:statusFormat}
		]],
		  onLoadSuccess: function (data) {
			  loadUserSelect();
			    /*if (data.total == 0) {
			        var body = $(this).data().datagrid.dc.body2;
			        body.find('table tbody').append('<tr><td>&nbsp;</td><td width="' + body.width() + '" style="height: 35px; text-align: center;"><h3>暂无数据</h3></td></tr>');
			    }*/
			} 
	});
	
}
function searchGroup(){
	var groupName =$("#groupNameWin").val();
//	var userIds =$("#userNameWin").find("option:selected").val();
	var userIds =$("#userNameWin").val();
	if(userIds==null){
		userIds = "";
	}else{
		userIds = userIds.toString().replace(/,/g, " ");	
	}

	
	objs.$userGroupList001.xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!groupListLoad.action',
		method: 'post',
		queryParams:{
			  'dto.isAjax':true,
			  'dto.pageSize':'8',
			  'dto.group.name':groupName,
			  'dto.group.userIds':userIds,
			  'dto.getPageNo':'1'
		},
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
		columns:[[
			{field:'id',title:'选择', checkbox:true,align:'center'},
			{field:'name',title:'组名',width:'20%',align:'center'},
			{field:'user',title:'组员',width:'65%',align:'left',halign:'center',formatter:usersFormat},
			{field:'remark',title:'备注',width:'15%',align:'center',halign:'center'},
			{field:'userIds',hidden:true,title:'备注',width:'15%',align:'center',halign:'center',formatter:userIdsFormat}
//			{field:'status',title:'状态',width:'8%',align:'center',formatter:statusFormat}
		]],
		onLoadSuccess: function (data) {
		    /*if (data.total == 0) {
		        var body = $(this).data().datagrid.dc.body2;
		        body.find('table tbody').append('<tr><td>&nbsp;</td><td width="' + body.width() + '" style="height: 35px; text-align: center;"><h3>暂无数据</h3></td></tr>');
		    }*/
		} 
	});
	
	
}

document.getElementById('userGroupTool').onkeydown=function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0]; 
    if (e && e.keyCode == 13) {//keyCode=13是回车键；数字不同代表监听的按键不同
    	searchGroup();
    }
};
document.getElementsByClassName('textbox combo').onkeydown=function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0]; 
    if (e && e.keyCode == 13) {//keyCode=13是回车键；数字不同代表监听的按键不同
    	searchGroup();
    }
};

//"操作"列
function operatFormat(value,row,index){
	var columnStr = "<div>"; 
	columnStr +=  "<a type='button' id='editRoleInfBtn' onclick='EditGropMember(\""+ row.id + "\")'  style='cursor:pointer;padding:2px 5px!important;margin: 5px 0px 5px 0;color:#1e7cfb' schkUrl='roleAction!userManager' >" +
    "人员维护</a>";
	 columnStr += "</div>"; 
	return columnStr;
} 
function EditGropMember(groupId){
//只查出不在该组的用户 
	   $("<div></div>").xdialog({
	    	id:'roleUserEditDlg',
	    	title:"维护账户列表",
	    	width : 1000,
	        height : 600,
	    	modal:true,
	    	href:baseUrl + "/userManager/userManagerAction!userGroupMemberList.action",
	    	queryParams:{'groupId' :groupId},
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
function selectMembers(id){
	var userIds =$("#"+id).val();
	if(userIds==null){
		userIds = "";
	}/*else{
		//userIds = userIds.toString().replace(/,/g, " ");	
	}*/
	showSeletctPeopleWindow(id);
}
//# sourceURL=userGroupList.js