var casePkgObjs = {
	$testCasePkgTb:$("#testCasePkgTb"),
	$addOrEditPkgWin:$("#addOrEditPkgWin")
  
};
var currAccountId = $("#accountId").html();
var onselectF = false;
var oncheckF  = false;
$(function(){
	$.parser.parse();
	getTestCasePkgManagerAuth();
	var currTaksId = $("#taksIdmain").val();
	//初始化角色列表
	casePkgObjs.$testCasePkgTb.xdatagrid({
		url:  baseUrl + '/testCasePkgManager/testCasePackageAction!loadTestCasePackageList.action',
		method: 'post',
		height:  mainObjs.tableHeight,
		singleSelect:true,
		checkOnSelect:true,
		selectOnCheck:true,
		queryParams:{'dto.testCasePackage.taskId':currTaksId},
		columns:[[
			{field:'packageId',checkbox:true},
			{field:'packageName',title:'测试包名称',width:'18%',height:'50px',align:'center',halign:'center'},
			{field:'execEnvironment',title:'执行环境',width:'18%',height:'50px',align:'center'},
			{field:'executor',title:'执行人',width:'14%',height:'50px',align:'center',halign:'center'},
			{field:'remark',title:'备注',width:'20%',height:'50px',align:'center'},
			{field:'operator',title:'操作',width:'29.4%',height:'50px',align:'center',formatter:operatFormat},
			{field:'testCaseNames',hidden:true}
		]],
		onLoadSuccess : function (data) {								
			if (data.total==0) {
				$('#testCasePkgTb').parent().find(".datagrid-view2 .datagrid-body").append('<div style="font-size:16px; text-align: center;">暂无数据</div>');
			}
		},
		onSelect:function(index,data){
			var selectedRow = casePkgObjs.$testCasePkgTb.xdatagrid('getSelected');
			var selectIndex=null;
			if(selectedRow!=null){
				selectIndex = casePkgObjs.$testCasePkgTb.xdatagrid('getRowIndex',selectedRow);
			}
			if(onselectF==true && selectedRow!=null  && selectIndex == index){
				onselectF = false;
				casePkgObjs.$testCasePkgTb.xdatagrid('unselectAll');
			}else{
				onselectF = true;
			}
		},
		 onCheck:function(index,row){
			 	var check = casePkgObjs.$testCasePkgTb.xdatagrid('getChecked');
			 	if(check!=null && oncheckF==true && check[0].id == row.id){
			 		casePkgObjs.$testCasePkgTb.xdatagrid("uncheckAll");
					oncheckF = false;
				}else{
					oncheckF = true;
				}
			 }
	});
	
	 loadPeopleLists();
});

//获取当前用户的权限
function getTestCasePkgManagerAuth(){
	var controlButton = $('button[schkUrl]');
	$.each(controlButton,function(i,n){
		var controId = controlButton[i].id;
		var controlUrl = $(controlButton[i]).attr('schkUrl');
		if(privilegeMap[controlUrl]!="1"){
			$("#"+controId).hide(); 
		}
	});
}

//"操作"列
function operatFormat(value,row,index){
	var isContainCurrUser = false; //当前登录这是否是测试用例包的执行人  false:不是
	var columnStr = "<div>" +
    "<a type='button' style='cursor:pointer; padding:2px 5px!important;margin: 5px 18px 5px 0;color:#1e7cfb' onclick='selTestCase(\""+ row.packageId + "\",\"" + row.packageName + "\")' >" +
    "分配用例</a>";
	var viewBtnStr =  "<a type='button' style='cursor:pointer;padding:2px 5px!important;margin: 5px 0 5px 0;color:#1e7cfb'  onclick='viewTestCase(\""+ row.packageId + "\",\"" + row.packageName + "\")' >" +
    "查看用例</a>";
	var execBtnStr =  "<a type='button'  style='cursor:pointer;padding:2px 5px!important;margin: 5px 0 5px 0;color:#1e7cfb' onclick='executeTestCase(\""+ row.packageId + "\",\"" + row.packageName + "\")' >" +
    "执行用例</a>";
	if(null != row.userTestCasePkgs){
		var userIdsArr = row.userTestCasePkgs;
		for(var i=0;i<userIdsArr.length;i++){
			if(userIdsArr[i].userId === currAccountId){
				isContainCurrUser = true;
				break;
			} 
		}
	}
	
	columnStr += isContainCurrUser? execBtnStr:viewBtnStr;
	
	columnStr += "<a type='button' style='cursor:pointer;padding:2px 5px!important;margin: 5px 0 5px 18px;color:#1e7cfb'  onclick='viewTestCaseResult(\""+ row.packageId + "\",\"" + row.packageName + "\")'>" +
    "查看结果</a></div>";
	return columnStr;
}

//加载可分配人列表
function loadPeopleLists(){
	$.post(
			baseUrl + "/otherMission/otherMissionAction!getPeopleLists.action",
			null,
			function(dat) {
				if (dat != null) {
					peopleList = dat.rows;
					$("#executor").xcombobox({
						data:dat.rows,
						multiple:true,
						valueField:'id',
						textField:'name'
					});
				} else {
					$.xalert("系统错误！", {type:'warning'});
				}
			}, "json");
}

//显示新增用例包弹窗
document.getElementById("showAddCasePkgWin").addEventListener('click',function(){
	casePkgObjs.isAddTestCasePkg = true;
	casePkgObjs.$addOrEditPkgWin.xform('clear');
	casePkgObjs.$addOrEditPkgWin.parent().css("border","none");
	casePkgObjs.$addOrEditPkgWin.prev().css({ color: "#ffff", background: "#101010" });
	casePkgObjs.$addOrEditPkgWin.xwindow('setTitle','新增用例包').xwindow('open');
	
});

//显示修改用例包弹窗
document.getElementById("showEditCasePkgWin").addEventListener('click',function(){
	var row = casePkgObjs.$testCasePkgTb.xdatagrid('getSelected');
	if (!row) {
		$.xalert('请选择要修改的一条记录', {type:'warning'});
		return;
	}
	var fillData = {};
	fillData["dto.testCasePackage"] = row;
	casePkgObjs.isAddTestCasePkg = false;
	$.getJSON(
			baseUrl + '/testCasePkgManager/testCasePackageAction!getUserIdsByPackageId.action',
		{'dto.testCasePackage.packageId': row.packageId},
		function(data) {
			casePkgObjs.$addOrEditPkgWin.xform('clear');
			if(null!=data){
				$("#executor").xcombobox("setValues",data);
			}
			casePkgObjs.$addOrEditPkgWin.xdeserialize(fillData);
			casePkgObjs.$addOrEditPkgWin.parent().css("border","none");
			casePkgObjs.$addOrEditPkgWin.prev().css({ color: "#ffff", background: "#101010" });
			casePkgObjs.$addOrEditPkgWin.xwindow('setTitle','修改用例包').xwindow('open');
		}
	);
	
});

//提交新增用例包
document.getElementById("submitPkgBtn").addEventListener('click',function(){
	var packageId = $("#packageId").val();
	var urlStr = "";
	var tipStr = "";
	var packageName = $("#packageName").textbox('getValue');
	var executor =  $("#executor").xcombobox("getValues").toString();
	if(null == packageName || "" == packageName){
		$.xalert("请输入测试用例包名");
		return ;
	}
	if(null == executor || "" == executor){
		$.xalert("请选择分配人");
		return ;
	}
	if(null == packageId || "" == packageId){
	   urlStr = baseUrl + '/testCasePkgManager/testCasePackageAction!saveTestCasePackage.action';
	   tipStr = "新增成功";
	}else{
	   urlStr = baseUrl + '/testCasePkgManager/testCasePackageAction!updateTestCasePackage.action';
	   tipStr = "修改成功";
	}
	
	var data = casePkgObjs.$addOrEditPkgWin.xserialize();
	data["dto.selectedUserIds"] = $("#executor").xcombobox("getValues").toString();
	data["dto.testCasePackage.executor"] = $("#executor").xcombobox("getText");
	$.post(
			urlStr,
			data,
			function(dataObj) {
				if(dataObj.indexOf('reName') >= 0){
					$.xalert("测试包名称'" + packageName + "'已存在,请更换");
				}else{
					 $.xalert(tipStr);
				    $('#executor').combobox('clear');
		    	    casePkgObjs.$addOrEditPkgWin.xwindow('close');
		    	    casePkgObjs.$testCasePkgTb.xdatagrid('reload');
				}
			},"text"
		);
});

//删除选中的数据
document.getElementById("delCasePkg").addEventListener('click',function(){
	var row = casePkgObjs.$testCasePkgTb.xdatagrid('getSelected');
	if (!row) {
		$.xalert('请选择要删除的一条记录', {type:'warning'});
		return;
	}
	
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			$.post(
					baseUrl + '/testCasePkgManager/testCasePackageAction!deleteTestCasePkgById.action',
					{'dto.testCasePackage.packageId': row.packageId},
				function(data) {
					if (data.indexOf("success")>=0) {
						casePkgObjs.$testCasePkgTb.xdatagrid('reload');
						$.xalert('删除成功');
					} else {
						$.xalert("删除失败，请稍后再试", {type:'warning'});
					}
				},"text"
			);
		}
	});
});


function selTestCase(packageId,pkgName){
	 $("<div></div>").xdialog({
	    	id:'testCaseListDlg',
	    	title:pkgName +"用例包--关联用例",
	    	width : 1300,
	        height : 600,
	    	modal:true,
	    	href:baseUrl + '/testCasePkgManager/testCasePackageAction!selTestCase.action',
	    	queryParams: { "testCasePackageId": packageId },
	        onClose : function() {
	        	casePkgObjs.$testCasePkgTb.xdatagrid('reload');
	            $(this).dialog('destroy');
	        }
	    });
}

function viewTestCase(packageId,pkgName){
	
	 $("<div></div>").xdialog({
	    	id:'viewTestCaseDlg',
	    	title:pkgName +"用例包--查看用例",
	    	width : 1300,
	        height : 600,
	    	modal:true,
	    	href:baseUrl + '/testCasePkgManager/testCasePackageAction!viewTestCase.action',
	    	queryParams: { "testCasePackageId": packageId},
	        onClose : function() {
	            $(this).dialog('destroy');
	        }
	    });
}

function executeTestCase(packageId,pkgName){
	
	 $("<div></div>").xdialog({
	    	id:'executeTestCaseDlg',
	    	title:pkgName +"用例包--执行用例",
	    	width : 1300,
	        height : 600,
	    	modal:true,
	    	href:baseUrl + '/testCasePkgManager/testCasePackageAction!executeTestCase.action',
	    	queryParams: { "testCasePackageId": packageId},
	        onClose : function() {
	            $(this).dialog('destroy');
	            
	            //执行管理子页面中的弹出框需要手动删除，否则会导致重复出现id=addOrEditFoot的div
               /* var executeWin = document.getElementById('addOrEditFoot').parentNode;
                document.body.removeChild(executeWin);*/
	        }
	    });
}

function viewTestCaseResult(packageId,pkgName){
	 $("<div></div>").xdialog({
	    	id:'viewTestCaseResultDlg',
	    	title:pkgName +"用例包--查看结果",
	    	width : 880,
	        height : 300,
	    	modal:true,
	    	href:baseUrl + '/testCasePkgManager/testCasePackageAction!viewTestCaseResult.action',
	    	queryParams: { "testCasePackageId": packageId},
	        onClose : function() {
	            $(this).dialog('destroy');
	        }
	    });

}

document.getElementById("closePkgWinBtn").addEventListener('click',function(){
	 $('#executor').combobox('clear');
	 casePkgObjs.$addOrEditPkgWin.xwindow('close');
});

document.getElementById('testcasepkgTool').onkeydown=function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0]; 
    if (e && e.keyCode == 13) {//keyCode=13是回车键；数字不同代表监听的按键不同
    	var currTaksId = $("#taksIdmain").val();
    	var queryParam = document.getElementById("queryParam").value;
    	//模糊查询
    	casePkgObjs.$testCasePkgTb.xdatagrid('load',{'dto.queryParam': queryParam,'dto.testCasePackage.taskId':currTaksId});
    }
};

//@ sourceURL=testCasePackageManager.js