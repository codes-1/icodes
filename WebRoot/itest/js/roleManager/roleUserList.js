var roleUserListObj={
		$roleUserListTb:$("#roleUserListTb"),
		$queryUserWin:$("#queryUserWin")
}

var rIdtemp = getQueryParam("roleInfDlg","roleId");
$(function(){
	
	
	roleUserListObj.$roleUserListTb.xdatagrid({
		url:baseUrl + "/role/roleAction!loadRoleUserList.action",
		queryParams:{'dto.role.roleId' : rIdtemp},
		method: 'get',
		height: mainObjs.tableHeight-140,
		columns:[[
				{field:'id',hidden:true},
				{field:'name',title:'真实姓名',width:'20%',height:'50px',align:'center',halign:'center',formatter:nameFormat},
				{field:'employeeId',title:'员工编号',width:'15%',height:'50px',align:'center',halign:'center'},
				{field:'loginName',title:'登录账号',width:'18%',height:'50px',align:'center'},
				{field:'tel',title:'联系电话',width:'14%',height:'50px',align:'center'},
				{field:'officeTel',title:'办公电话',width:'15%',height:'50px',align:'center'},
				{field:'privilege',title:'职务',width:'10%',height:'50px',align:'center'},
				{field:'status',title:'状态',width:'10%',height:'50px',align:'center',formatter:statusFormat}
			 ]],
				onLoadSuccess : function (data) {								
					if (data.total==0) {
						/*$("#roleInfDlg .datagrid-view").css("height","100px");
						$("#roleInfDlg .datagrid-body").css("height","30px");*/
						$('#roleInfDlg tr[id^="datagrid-row-r"]').parent().append('<div style="width: 900px; text-align: center;">暂无数据</div>');
					}
				}
	});
	
	
	
	$.post(
		 baseUrl + '/userManager/userManagerAction!groupSel.action',
		 null,
		 function(dataObj) {
			    dataObj = JSON.parse(dataObj);
				 dataObj.push({'keyObj':'-1','valueObj':'所有'});

				$('#roleUsergroupIds').xcombobox({
				    data: dataObj,
				    valueField:'keyObj',
				    textField:'valueObj'
				});
			},"text"
		);
	
    var statusDataObj = [
    		{label:'-1',value:'所有'},
    		{label:'1',value:'活动'},
    		{label:'0',value:'禁止'}	
    ];
    
	$('#roleUserStatus').xcombobox({
		data:statusDataObj,
	    valueField:'label',
	    textField:'value'
	});
	
	
});

function nameFormat(value,row,index){
	return "<div title=" + value + ">" +value + "</div>";
}

function statusFormat(value,row,index){
	var statusTxt = "" ;
	if(value=='1'){
		statusTxt = "启用";
	}else{
		statusTxt = "禁用";
	}
	return statusTxt;
}

//显示查询弹出框
function showQueryWin(){
	roleUserListObj.$queryUserWin.xform('clear');
	roleUserListObj.$queryUserWin.xwindow('setTitle','查询').xwindow('open');
	roleUserListObj.$queryUserWin.xdeserialize({'dto.role.roleId' : rIdtemp});
	$('#roleUserStatus').xcombobox('setValue', '-1');
	$('#roleUsergroupIds').xcombobox('setValue','-1');
}

//查询
document.getElementById("queryUser").addEventListener("click",function(){
            var	data = roleUserListObj.$queryUserWin.xserialize();
			roleUserListObj.$roleUserListTb.xdatagrid('reload',data);
	
});

//重置
document.getElementById('closeQueryUserDlg').addEventListener('click',function(){
	roleUserListObj.$queryUserWin.xform('clear');
	roleUserListObj.$queryUserWin.xdeserialize({'dto.role.roleId' : rIdtemp});
	$('#roleUserStatus').xcombobox('setValue', '-1');
	$('#roleUsergroupIds').xcombobox('setValue','-1');
});

function closeRoleUserListWin(){
	 var queryUserFooter = document.getElementById('queryUserFooter').parentNode;
    document.body.removeChild(queryUserFooter);
	closeDlg('roleInfDlg');
}
//@ sourceURL=roleUserList.js