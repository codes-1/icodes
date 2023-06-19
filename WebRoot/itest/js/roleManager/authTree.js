var authTreeObj = {
		$authTree:$("#authTree")
}


$(function(){
	
	var roleIdObj = getQueryParam('authDetailDlg','roleId');
	
	$.post(
			baseUrl + '/role/roleAction!loadBrowserAuth.action',
			{'dto.role.roleId':roleIdObj},
			function(data){
				data = JSON.parse(data);
				if(data != "" && null!=data){
					//构造树结构数据
					var tree = constructTree(data);
					$('#authTree').xtree({
						lines:true,
						animate:true,
						data: tree
					});
				}else{
					$("#authTree").html("该组暂未分配权限");
				}
			},'text');
	
});

//@ sourceURL=authTree.js