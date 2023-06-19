var authGrantTreeObj = {
		$authGrantTree:$("#authGrantTree")
}

var initFunctionIds ="";
//获取url传递过来的roleId
var roleIdTemp = getQueryParam('authEditDlg','roleId');
$(function(){
	
	$.post(
			baseUrl + '/role/roleAction!loadGrantAuthInit.action',
			{'dto.role.roleId':roleIdTemp},
			function(data){
				data = JSON.parse(data);
				if(null != data && null != data.authTree){
				   var treeGrant = constructTree(data.authTree);
				   authGrantTreeObj.$authGrantTree.xtree({
					checkbox :true,
					data: treeGrant,
					lines:true,
					animate:true,
					onLoadSuccess:function(node, dataObj){
						 if(null != data.functionIds){
							   initFunctionIds = data.functionIds;
							   var functionIdArr = data.functionIds.split(' ');
							   for(var i=0;i<functionIdArr.length;i++){
								   var node =   authGrantTreeObj.$authGrantTree.xtree('find', functionIdArr[i]);
								   authGrantTreeObj.$authGrantTree.xtree('check', node.target);
							   }
						   }
					}
				   });
				}
			},'text');
	
});

document.getElementById('saveGrant').addEventListener('click',function(){
	var selNodes = authGrantTreeObj.$authGrantTree.xtree('getChecked', ['checked','indeterminate']);
	if(selNodes.length <=0 && initFunctionIds == ""){
		$.xalert({title:'消息提示',msg:'请选择权限！'});
		return ;
	}
	
	var msgTips="";
	if(selNodes.length <=0 && initFunctionIds != ""){
		msgTips = '确定要收回当前角色权限！';
	}else{
		msgTips = '确定要授予当前角色所选权限?！';
	}
	
	var functionIds = "";
	for(var i=0;i<selNodes.length;i++){
		 functionIds += selNodes[i].id + " ";
	}
	
	$.xconfirm({
		msg:msgTips,
		okFn: function() {
			$.post(
					baseUrl+"/role/roleAction!grantRoleAuth.action",
					{
						'dto.role.roleId':roleIdTemp,
						'dto.functionIds':functionIds
					},
				function(data) {
					if (data.indexOf("success")>=0) {
						$.xalert({title:'消息提示',msg:'权限修改成功，请重新登陆后方可生效'});
						closeDlg('authEditDlg');
					} else {
						$.xalert({title:'消息提示',msg:'权限修改失败，请稍后再试！'});
					}
				},"text"
			);
		}
	});
	
});

document.getElementById('closeTreeGrantDlg').addEventListener('click',function(){
	closeDlg('authEditDlg');
});


//@ sourceURL=authGrant.js