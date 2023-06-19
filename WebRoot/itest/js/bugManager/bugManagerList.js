var taskId="";
var accountId="";
var nodeArr = new Array();
var currNodeId="";
var rootNodeId=false;
var switchFlag=0;
var loadCount=0;
var findFlag="";
var arrFpage =[];
var moduleNameM="";
var queryParam={'dto.defBug':1};

var onselectF = false;
var oncheckF  = false;
//存放查询条件
var queryMap = {};
var hysss = true;
$(function(){
//	$.parser.parse();
	exuiloader.load('numberbox', null, true);
	//设置用例弹窗的样式
	$('input[name="devName"]').parent().css("marginLeft","7px");
	$('input[name="priName"]').parent().css("marginLeft","7px");
	$('input[name="occaName"]').parent().css("marginLeft","7px");
	$('input[name="authorName"]').parent().css("marginLeft","7px");
	$('input[name="pltfomName"]').parent().css("marginLeft","7px");
	$('input[id^="_exui_textbox_input"]').css("borderRadius","4px");
	$('input[name="dto.bug.bugType.typeName"]').parent().css("marginLeft","7px");
	$('input[name="dto.bug.reProStep"]').prev().css({height:'100px',marginTop:'5px'});
	$('input[name="dto.bug.reProStep"]').parent().css({height:'100px'});
	getUserPower();
	accountId = $("#accountId").text();
	var currTaksId = $("#taksIdmain").val();
//	getTaskNodeCount(currTaksId);
	
	if(currTaksId!='null' && currTaksId!=''){
		taskId = currTaksId;
//		getBugList();
		bugTree();
	}else{
		bugTree();
	}
//	bugCountInfo();
});

//获取当前用户的权限
function getUserPower(){
	var controlButton = $('button[schkUrl]');
	$.each(controlButton,function(i,n){
		var controId = controlButton[i].id;
		var controlUrl = $(controlButton[i]).attr('schkUrl');
		if(privilegeMap[controlUrl]!="1"){
			$(controlButton[i]).hide();
		}
	});
}

//测试需求
function bugTree(){
	$('#bugTree').xtree({
	    url:baseUrl+'/bugManager/bugManagerAction!loadTree.action?dto.taskId='+taskId+'&dto.command=',
	    method:'get',
		animate:true,
		lines:true,
//		dnd:true,
		onLoadSuccess:function(node,data){  
			var mid="";
			if(node==null){
				rootNodeId = true;
				currNodeId = data[0].id;
				if(loadCount==0){
					loadCount=1;
				}
				getBugList();
				var nodeDep = $('#bugTree').xtree('find',data[0].id);  
				if (null != nodeDep && undefined != nodeDep){  
					$('#bugTree').xtree('select',nodeDep.target);  
				}  
				$(nodeDep.target).tooltip({
				    position: 'right',
				    content: '<span style="color:#fff">root节点测试需求，不能增加软件问题.</span>',
				    hideDelay:100,
				    onShow: function(){
						$(this).tooltip('tip').css({
							backgroundColor: '#666',
							borderColor: '#666'
						});
				    }
				});
				mid = data[0].id;
				if(data[0].children!=null){
					for ( var it in data[0].children) {
						mid+='_'+data[0].children[it].id;
					}
				}
			}else{
				rootNodeId = false;
				var nodeDep = $('#bugTree').xtree('find',node.id);  
				if (null != nodeDep && undefined != nodeDep){  
					$('#bugTree').xtree('select',nodeDep.target);  
				}  
				
				for ( var it in data) {
					mid+='_'+data[it].id;
				}
			}
			
		 }, 
		onClick: function(node){
			rootNodeId = node.rootNode; 
			currNodeId = node.id;
			getBugList();
			nodeArr = new Array();
			if(node!=null){
				if(!node.rootNode){
					moduleNameM=node.text;
					var parentNode = $(this).xtree('getParent',node.target);
					while(!parentNode.rootNode){
						moduleNameM = parentNode.text+"/"+moduleNameM;
						parentNode = $(this).xtree('getParent',parentNode.target);
					}
					moduleNameM = parentNode.text+"/"+moduleNameM;
				}
				
				var parentNode = $(this).xtree('getParent',node.target);
				while(parentNode!=null){
					nodeArr.push(parentNode.id);
					parentNode = $(this).xtree('getParent',parentNode.target);
				}
				
			}
			
		},
		onDblClick:function(node){
//			$(this).xtree('beginEdit',node.target);
		},
		onContextMenu:function(e,node){
			e.preventDefault();
			$(this).xtree('select', node.target);
		}
	});
}
//bug列表
function getBugList(){
	$("#bugList").xdatagrid({
		url: baseUrl + '/bugManager/bugManagerAction!loadMyBug.action?dto.isAllTestTask=false'+'&dto.taskId='+taskId+'&dto.bug.moduleId='+currNodeId,
		method: 'get',
		queryParams:queryParam,
//		height: mainObjs.tableHeight-145,
		height: mainObjs.tableHeight,
		singleSelect:true,
		checkOnSelect:true,
		selectOnCheck:true,
//		rownumbers: true,
//		scrollbarSize:100,
		fitColumns:true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
	    layout:['list','first','prev','manual', 'sep','next','last','refresh','info'],
		pageList: [10,30,50,100],
		showPageList:true,
		columns:[[
			{field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'bugId',title:'编号',width:'5%',align:'center',formatter:bugDetail},
			{field:'bugDesc',title:'bug描述',width:'30%',align:'left',halign:'center',formatter:bugTitle},
			{field:'stateName',title:'状态',width:'10%',align:'center',formatter:bugHand},
			{field:'typeName',title:'等级',width:'5%',align:'left',halign:'center',
				formatter:function(value,row,index){
					if(row.bugGrade!=null){
						return row.bugGrade.typeName;
					}
				}
			},
		/*	{field:'taskName',title:'项目名称',width:'10%',align:'center'},*/
			{field:'auditerNmae',title:'时机',width:'10%',align:'center',
				formatter:function(value,row,index){
					if(row.bugOpotunity!=null){
						return row.bugOpotunity.typeName;
					}
				}
			},
			{field:'authorName',title:'类型',width:'5%',align:'center',
				formatter:function(value,row,index){
					if(row.bugType!=null){
						return row.bugType.typeName;
					}
				}
			},
			{field:'weight',title:'优先级',width:'5%',align:'center',
				formatter:function(value,row,index){
					if(row.bugPri){
						return row.bugPri.typeName;
					}
				}
			},
			{field:'testName',title:'测试人员',width:'10%',align:'center'},
			{field:'devName',title:'开发人员',width:'14%',align:'center',formatter:developer},
			{field:'reptDate',title:'报告日期',width:'13%',align:'left'}
		]],
		onLoadSuccess:function(data){
			if(data.total==0 && findFlag=="" && rootNodeId==true){
				$.xalert({title:'提示',msg:'当前项目没有您要处理的bug，要查看其他bug请查询！'});
			}
		},
		onSelect:function(index,data){
			var selectedRow = $("#bugList").xdatagrid('getSelected');
			var selectIndex=null;
			if(selectedRow!=null){
				selectIndex = $("#bugList").xdatagrid('getRowIndex',selectedRow);
			}
			if(onselectF==true && selectedRow!=null  && selectIndex == index){
				onselectF = false;
				$("#bugList").xdatagrid('unselectAll');
			}else{
				onselectF = true;
			}
		},
		 onCheck:function(index,row){
		 	var check = $("#bugList").xdatagrid('getChecked');
		 	if(check!=null && oncheckF==true && check[0].id == row.id){
		 		$("#bugList").xdatagrid("uncheckAll");
				oncheckF = false;
			}else{
				oncheckF = true;
			}
		 }
	});
}

//开发人员
function developer(value,row,index){
	if(value==null || value=="null"){
		return "";
	}else{
		return "<a style=\"cursor: pointer;\" title=\"意见交流\" href=\"javascript:;\" onclick=\"exchangeOpinions('"+row.bugId+"')\">" + value + "</a>";
	}
}

//bug详情
function bugDetail(value,row,index) {
	return "<a style=\"cursor: pointer;\" title=\"关联用例\" href=\"javascript:;\" onclick=\"lookFormWindow('"+row.bugId+"','"+row.moduleId+"','"+row.bugReptVer+"','"+row.taskId+"')\">" + value + "</a>";
}

function bugTitle(value,row,index) {
	return "<p style=\"cursor: pointer;margin:0;\" title=\"bug详情："+value+"\">" + value + "</p>";
}
//bug处理
function bugHand(value,row,index) {
	return "<a style=\"cursor: pointer;\" title=\"bug处理\" href=\"javascript:;\" onclick=\"bugHandWindow('"+row.bugId+"','"+row.taskId+"')\">" + value + "</a>";
}

//用例历史
function caseHistory(value,row,index) {
	if(value=="" || value=="null" ||value==null){
		value=="--";
	}
	return "<a style=\"cursor: pointer;\" title=\"查看用例历史\" href=\"javascript:;\" onclick=\"caseHistoryList('"+row.testCaseId+"')\">" + value + "</a>";
}

//意见交流窗的打开
function exchangeOpinions(bugId){
	//查询点击bug的基本信息
//	exchangeOpinionInfo(bugId);
	$('#bugIds').data('bugId',bugId);
	$('#bugIdEx').val(bugId);
	$("#suggestionWindow").xwindow({title:'意见交流'}).xwindow('open');
	$("#suggestionWindow").window("vcenter");
	$('a[href="#baseInfo_sugg"]').parent().removeAttr('class');
	$("#example_sugg").addClass('tab-pane fade in active');
	$("#baseInfo_sugg").removeClass().addClass("tab-pane fade");
	showSuggestInfo();
//	$('#example_sugg').next().hide();
}

$('a[href="#baseInfo_sugg"]').click(function(){
	var bugId = $('#bugIds').data('bugId');
	//查询点击bug的基本信息
	exchangeOpinionInfo(bugId);
	$('a[href="#baseInfo_sugg"]').parent().attr('class','active');
	$('a[href="#example_sugg"]').parent().removeAttr('class');
	$("#baseInfo_sugg").addClass('tab-pane fade in active');
	$("#example_sugg").removeClass().addClass("tab-pane fade");
	$('#example_sugg').next().hide();
});

//查询点击bug的基本信息
function exchangeOpinionInfo(bugIds){
	$("#bugIds").data("bugId",bugIds);
	$("#bugIdEx").val(bugIds);
	$.get(
			baseUrl + "/bugManager/bugManagerAction!viewBugDetal.action",
			{'dto.bug.bugId':bugIds,'dto.loadType':'2'},
			function(data,status,xhr){
				if(status=='success'){
					//回填信息
					backFillInfo(data,'2');
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			},
			"json");
}

//意见交流记录
function showSuggestInfo(){
	$("#expressOpinion").xdatagrid({
		url: baseUrl + '/bugManager/bugShortMsgAction!loadMsgList.action?dto.loadType='+'2'+'&dto.shortMsg.bugId='+$("#bugIds").data("bugId"),
		method: 'get',
		striped:true,
		height: mainObjs.tableHeight,
		columns:[[
			{field:'msgId',title:'序号',width:'15%',align:'center'},
			{field:'pName',title:'发送人',width:'12%',align:'center',halign:'center'},
			{field:'message',title:'意见/问题',width:'40%',align:'center',halign:'center',formatter:function(value,row,index){
				if(value!=""){
					return "<span title='"+value+"'>"+value+"</span>";
				}else{
					return "<span>暂无</span>";
				}
			}},
			{field:'recipCd',title:'接收人',width:'15%',align:'center',halign:'center'},
			{field:'insDate',title:'发送日期',width:'20%',align:'center'}
		]],
		onLoadSuccess : function (data) {		
			if (data.total==0) {
				$('#expressOpinion').prev().find('div.datagrid-body').append('<label style="height: 40px;width: 530px; text-align: center;">没有数据!</label>');
			}
		}
	
	});
}

//打开意见交流弹窗
function expressOpinion(){
	$("#expressOpinionNew").xwindow({title:'发表新意见'}).xwindow('open');
	$("#expressOpinionNew").window("vcenter");
}

//重置意见交流弹窗
function addExpressReset(){
	$('#expressOpinionNew').xform('clear');
}
//关闭意见交流弹窗
function canlceExpressWin(){
	addExpressReset();
	$('#expressOpinionNew').xwindow('close');
}

//增加新意见
function addExpressSubmit(){
	if(!$("#hhjhfdghhhjhjj").xcombobox("getValue")){
		$.xalert({title:'提示',msg:'请选择发送给项目的成员！'});
		return;
	}
	$.ajax({
		  url: baseUrl+"/bugManager/bugShortMsgAction!sendMsg.action",
		  cache: false,
		  async: false,
		  type: "POST",
//		  dataType:"json",
		  dataType:"text",
		  data: $("#expressOpinionNew").xserialize(),
		  success: function(data){
			  if(data.indexOf("success") >=0){
				  //关闭弹窗
				  $("#expressOpinionNew").xwindow('close');
				  showSuggestInfo();
//				  getBugList();
//				  loadTestProject();
			  }else{
				  $.xalert({title:'提示',msg:'系统问题！'});
			  }
		  }
		});
}

//显示用例信息
function showExampleInfo(){
//	arrFpage =[];
	$("#exampleListInfo").xdatagrid({
		url: baseUrl + '/bugManager/relaCaseAction!loadRelaCase.action?dto.moduleId='+$("#moduleIdH").data("moduleId")+'&dto.bugId='+$("#bugIds").data("bugId"),
		method: 'get',
		striped:true,
		emptyMsg:'暂无数据',
		checkOnSelect:true,
		selectOnCheck:true,
		singleSelect:false,
		height: mainObjs.tableHeight,
		columns:[[//,formatter:statusFormat
		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'testCaseId',title:'编号',width:'15%',align:'center',formatter:function(value,row,index){
				if(value==null || value=="null"){
					return "";
				}else{
					return "<span id="+value+">" + value + "</span>";
				}
			}},
			{field:'testCaseDes',title:'用例描述',width:'30%',align:'center',halign:'center'},
			{field:'testStatus',title:'状态',width:'15%',align:'center',halign:'center'},
			{field:'typeName',title:'类型',width:'20%',align:'center',halign:'center'},
			{field:'priName',title:'优先级',width:'23%',align:'center'}
		]],
		onLoadSuccess : function (data) {
//			var dataRela = data.relaCaseList;
//			if(dataRela!=undefined){
				if (data.total>0) {
					var getBugId = $("#bugIds").data("bugId");
					//获取关联的实例，勾上
					getRelaExampleCheck(getBugId);
//					for(var i=0;i<data["rows"].length;i++){
//						var testCaseIds = data["rows"][i].testCaseIds;
//						$("#"+testCaseIds).parents().eq(1).prev().find($('input[name="checkId"]')).attr('checked',true);
//					}
					
//					$("#exampleListInfo").xdatagrid('loadData',dataRela);
					
				}else{
					$('#exampleListInfo').prev().find('div.datagrid-body table tr').after('<label style="height: 40px;width: 530px; text-align: center;">没有数据!</label>');
				}
//			}
//			$("#exampleListInfo").xdatagrid('resize');$('#exampleListInfo').prev().find('div.datagrid-view2 div.datagrid-body table tr')
		},
		onClickRow:function(rowIndex, rowData){
//			arrExam =[];
			var testCaseId = rowData.testCaseId;
			arrFpage.push(testCaseId);
		}
	});
//	$("#exampleListInfo").xdatagrid('resize');
}

//获取关联的实例，勾上
function getRelaExampleCheck(BugId){
	$.ajax({
		  url: baseUrl+"/bugManager/relaCaseAction!getBugRelaCaseData.action",
		  cache: false,
		  async: false,
		  type: "POST",
		  dataType:"json",
//		  dataType:"text",
		  data:{"dto.caseBugRela.bugId":BugId},
		  success: function(data){
			  if(data!=null){
				  for(var i=0;i<data.length;i++){
						var testCaseIds = data[i].testCaseId;
						$("#"+testCaseIds).parents().eq(1).prev().find($('input[name="checkId"]')).attr('checked',true);
					}
				  //关闭弹窗
//				  $("#lookBugWindown").xwindow('close');
//				  getBugList();
//				  loadTestProject();
			  }else{
				  $.xalert({title:'提示',msg:'系统问题！'});
			  }
		  }
		});
}

//点击基本信息隐藏列表
$('a[href="#baseInfo"]').click(function(){
	$('#example').next().hide();
});

//打开删除确认弹窗
function delBugConfirm() {
	var deleteTaskId="";
	var row = $("#bugList").xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'提示',msg:'请选择要删除的一条记录！'});
		return;
	}
	var deleteTaskId="";
	if(taskId ==""){
		deleteTaskId = row.taskId;
	}else{
		deleteTaskId = taskId;
	}
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			$.post(
					baseUrl + "/bugManager/bugManagerAction!delete.action",
				{
						'dto.bug.bugId': row.bugId,
						'dto.taskId':deleteTaskId
				},
				function(data) {
					if (data == "success") {
						$.xalert({title:'提示',msg:'删除成功！'});
						getBugList();
					} else {
						$.xalert({title:'提示',msg:'系统错误！'});
					}
				}, "text");
		}
		
	});
	
}

//查看
function lookFormWindow(bugIds,moduleId,bugReptVer,taskId){
	$("#bugIds").data("bugId",bugIds);
	$("#moduleIdH").data("moduleId",moduleId);
	$("#bugReptVers").data("bugReptVer",bugReptVer);
	$("#taskId").val(taskId);
	$.get(
		baseUrl + "/bugManager/bugManagerAction!viewBugDetal.action",
		{'dto.bug.bugId':bugIds,'dto.loadType':'2'},
		function(data,status,xhr){
			if(status=='success'){
//				$("#lookBugWindown").xdeserialize(data);
				setTimeout(function(){
					showExampleInfo();
					//回填信息
					backFillInfo(data,'1');
				},500);
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},
		"json");

	$("#lookBugWindown").xwindow({title:"关联用例"});
	$("#lookBugWindown").xwindow("vcenter");
	$("#lookBugWindown").xwindow("open");
	
	$('a[href="#example"]').parent().attr('class','active');
	$('a[href="#baseInfo"]').parent().removeAttr('class');
	$("#baseInfo").removeClass().addClass("tab-pane fade");
	$("#example").addClass('tab-pane fade in active');
	$("#example").show();
}

//回填信息
function backFillInfo(data,param){
	if(data.modelName!=null){
		$('input[name="dto.moduleName"]').prev().val(data.modelName);
	}else{
		$('input[name="dto.moduleName"]').prev().val('未知');
	}
	if(data.stateName!=null){
		$('input[name="dto.stateName"]').prev().val(data.stateName);
	}else{
		$('input[name="dto.stateName"]').prev().val('未知');
	}
	if(data.author.uniqueName!=null){
		$('input[name="authorName"]').prev().val(data.author.uniqueName);
	}else{
		$('input[name="authorName"]').prev().val('暂未指定');
	}
	
	if(data.testOwner.uniqueName!=null){
		$('input[name="testName"]').prev().val(data.testOwner.uniqueName);
	}else{
		$('input[name="testName"]').prev().val('暂未指定');
	}
	if(data.dtoHelper.devOwner!=null&&data.dtoHelper.devOwner.uniqueName!=null){
		$('input[name="devName"]').prev().val(data.dtoHelper.devOwner.uniqueName);
	}else{
		$('input[name="devName"]').prev().val('暂未指定');
	}
	if(data.bugDesc!=null){
		$('input[name="bugDesc"]').prev().val(data.bugDesc);
	}else{
		$('input[name="bugDesc"]').prev().val('未知');
	}
	if(data.bugType.typeName!=null){
		$('input[name="dto.bug.bugType.typeName"]').prev().val(data.bugType.typeName);
	}else{
		$('input[name="dto.bug.bugType.typeName"]').prev().val('未知');
	}
	if(data.bugGrade.typeName!=null){
		$('input[name="bugGradeName"]').prev().val(data.bugGrade.typeName);
	}else{
		$('input[name="bugGradeName"]').prev().val('未知');
	}
	if(data.occurPlant.typeName!=null){
		$('input[name="pltfomName"]').prev().val(data.occurPlant.typeName);
	}else{
		$('input[name="pltfomName"]').prev().val('未知');
	}
	if(data.bugSource.typeName!=null){
		$('input[name="sourceName"]').prev().val(data.bugSource.typeName);
	}else{
		$('input[name="sourceName"]').prev().val('未知');
	}
	if(data.bugOpotunity.typeName!=null){
		$('input[name="occaName"]').prev().val(data.bugOpotunity.typeName);
	}else{
		$('input[name="occaName"]').prev().val('未知');
	}
	if(data.dtoHelper.geneCause!=null){
		$('input[name="geneCaseName"]').prev().val(data.dtoHelper.geneCause.typeName);
	}else{
		$('input[name="geneCaseName"]').prev().val('未知');
	}
	if(data.dtoHelper.bugPri.typeName!=null){
		$('input[name="priName"]').prev().val(data.dtoHelper.bugPri.typeName);
	}else{
		$('input[name="priName"]').prev().val('未知');
	}
	if(data.dtoHelper.bugFreq.typeName!=null){
		$('input[name="bugFreqName"]').prev().val(data.dtoHelper.bugFreq.typeName);
	}else{
		$('input[name="bugFreqName"]').prev().val('未知');
	}
	if(data.reProStep!=null){
		$('input[name="dto.bug.reProStep"]').prev().val(data.reProStep);
	}else{
		$('input[name="dto.bug.reProStep"]').prev().val('未知');
	}
	if(param!='1'){
		if(data.reptDate!=null){
			$('#reptDate_sugg').html(data.reptDate);
		}else{
			$('#reptDate_sugg').html('无');
		}
		if(data.bugReptVer!=null||data.bugReptVer!=""){
			$('#bugReptVersion_sugg').html(data.bugReptVer);
		}else{
			$('#bugReptVersion_sugg').html('未知');
		}
		if(data.reptVersion.versionNum!=null){
			$('#currVer_sugg').html(data.reptVersion.versionNum);
		}else{
			$('#currVer_sugg').html('未知');
		}
	}else{
		if(data.reptDate!=null){
			$('#reptDate').html(data.reptDate);
		}else{
			$('#reptDate').html('无');
		}
		if(data.reptVersion.versionNum!=null||data.reptVersion.versionNum!=""){
			$('#bugReptVersion').html(data.reptVersion.versionNum);
		}else{
			$('#bugReptVersion').html('未知');
		}
		if(data.versionLable!=null){
			$('#currVer').show();
			$('#currVer').prev().show();
			$('#currVer').html(data.dtoHelper.currVersion.versionNum);
		}else{
			$('#currVer').hide();
			$('#currVer').prev().hide();
		}
//		if(data.reptVersion.versionNume!=null){
//			$('#currVer').html(data.reptVersion.versionNume);
//		}else{
//			$('#currVer').html('未知');
//		}
	}
}


document.getElementById('bug_Id').onkeydown=function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0]; 
    if (e && e.keyCode == 13) {//keyCode=13是回车键；数字不同代表监听的按键不同
    	 findQueryBug("bug_Id");
    }
};

//enter事件
function bugOnKeyEnter(target){
//	var lKeyCode = (navigator.appname=="Netscape")?event.which:window.event.keyCode;
	e = event ? event :(window.event ? window.event : null);
	var lKeyCode=0;
	lKeyCode=e.keyCode||e.which||e.charCode;
	if ( lKeyCode == 13 ){
		var bugId = $("#"+target).val();
		if(bugId!=""){
			$.get(
			baseUrl + "/bugManager/bugManagerAction!quickQuery.action",
			{
				'dto.bug.bugId': bugId,
			},
			function(data,status,xhr){
				if(status=='success'){
					if(data!=null && data!=""){
						$("#bugList").xdatagrid('loadData',data);
					}else{
						 $.xalert({title:'提示',msg:'没有查到相关记录'});
					}
					setTimeout(function(){
						$("#"+target).val("");
					}, 2000);
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			},
			"json");
		}else{
			 $.xalert({title:'提示',msg:'没有查到相关记录'});
		}
     }
}


function findQueryBug(target){
	var bugId = $("#"+target).val();
	if(bugId!=""){
		$.get(
		baseUrl + "/bugManager/bugManagerAction!quickQuery.action",
		{
			'dto.bug.bugId': bugId,
		},
		function(data,status,xhr){
			if(status=='success'){
				if(data!=null && data!=""){
					$("#bugList").xdatagrid('loadData',data);
				}else{
					 $.xalert({title:'提示',msg:'没有查到相关记录'});
				}
				setTimeout(function(){
					$("#"+target).val("");
				}, 2000);
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},
		"json");
	}else{
		 $.xalert({title:'提示',msg:'没有查到相关记录'});
	}
}

//重置
function clearInput(){
	$("#bug_Id").val("");
	getBugList();
}

//取消
function closeBugWin(){
	$("#bugAddOrEditWindown").xwindow('close');
}
//bug统计信息
function bugCountInfo(){
	$.get(
			baseUrl + "/bugManager/bugManagerAction!getBugStatInfo.action",
			{'dto.taskId': taskId},
			function(data,status,xhr){
				if(status=='success'){
					$("#bugCountInfo").text(data);
					$("#bugCountDiv").show();
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			},"text");
}

function testPhaseFormat(value,row,index) {
	switch (value) {
	case 0:
		return '单元测试';
	case 1:
		return '集成测试';
	case 2:
		return '系统测试';
	default:
		return '-';
	}
}

function planDocNameFormat(value,row,index) {
	if (value) {
		return '<a href="javascript:;">@</a>';
	}
}

function statusFormat(value,row,index) {
	switch (value) {
	case 0:
		return '进行';
	case 1:
		return '完成';
	case 2:
		return '结束';
	case 3:
		return '准备';
	default:
		return '-';
	}
}

function psmNameFormat(value,row,index) {
	var html = new Array();
	if (row.testLdVos.length) {
		for (var i = 0; i < row.testLdVos.length; i++) {
			html.push(row.testLdVos[i].loginName + '(' + row.testLdVos[i].name + ')');
		}
	}
	return html.join(',');
}

//测试需求
function needsTree(textId,nameId,reptId,sourceFlag){
	if(taskId=="" || taskId==undefined){
		$.xalert({title:'提示',msg:'请先选择项目！'});
		return;
	}
	
	var rootName="";
	$('#needsTree').xtree({
	    url:baseUrl+'/bugManager/bugManagerAction!loadTree.action?dto.taskId='+taskId+'&dto.command=',
	    method:'get',
		animate:true,
		lines:true,
//		dnd:true,
		onLoadSuccess:function(node,data){  
			if(data!=null){
				if(node==null){
					rootName = data[0].text;
					var nodeDep = $('#needsTree').xtree('find',data[0].id);  
					$(nodeDep.target).tooltip({
						position: 'right',
						content: '<span style="color:#fff">root节点测试需求，不能增加软件问题.</span>',
						hideDelay:100,
						onShow: function(){
							$(this).tooltip('tip').css({
								backgroundColor: '#666',
								borderColor: '#666'
							});
						}
					});
					
				}
				
				if(nodeArr.length > 0){
					for (var i = nodeArr.length-1; 0 <= i; i--) {
						var parentNode = $('#needsTree').xtree('find',nodeArr[i]);  
						$('#needsTree').xtree('expand',parentNode.target); 
					}
				}
				
				var nodeDep = $('#needsTree').xtree('find',currNodeId);  
				if (null != nodeDep && undefined != nodeDep){  
					$('#needsTree').xtree('select',nodeDep.target);  
				} 
				$("#needsTreeWin").window({title:"选择需求  <span style='font-size:13px;'>(双击选择)<span>"});
				$("#needsTreeWin").window("vcenter");
				$("#needsTreeWin").xwindow('open');
			}else{
				$.xalert({title:'提示',msg:'当前项目没提交测试需求,不能填写软件问题报告'});
			}
		 }, 
		onClick: function(node){
//			currNodeId = node.id;
		},
		onDblClick:function(node){
			if(node!=null){
				if(!node.rootNode){
					currNodeId = node.id;
					var moduleName=node.text;
					var parentNode = $(this).xtree('getParent',node.target);
					while(!parentNode.rootNode){
						moduleName = parentNode.text+"/"+moduleName;
						parentNode = $(this).xtree('getParent',parentNode.target);
					}
					moduleName = parentNode.text+"/"+moduleName;
					$("#"+textId).val(node.id);
					var oldModuleName = $("#"+nameId).textbox("getValue");
					$("#"+nameId).textbox("setValue", moduleName);
					var reText=  $("#"+reptId).textbox("getValue");
					if(reText==""){
						$("#"+reptId).textbox("setValue", moduleName+": ");
					}else{
						var newReText = reText.replace(oldModuleName,moduleName);
						$("#"+reptId).textbox("setValue", newReText);
					}
					$("#needsTreeWin").xwindow('close');
					
					if(sourceFlag=="add"){
						var assignOwnerIsShow = $("#assignOwnerTr")[0].style.display!="none"; 
						if(assignOwnerIsShow){
							getMdPersonDef('3','module_Id','assignOwnerId','anasnOwnerName');
						}
						var devOwnerTrIsShow = $("#devOwnerTr")[0].style.display!="none"; 
						if(devOwnerTrIsShow){
							getMdPersonDef('1','module_Id','devOwnerId','devOwnerName');
						}
					}
				}
				
				nodeArr = new Array();
				var parentNode = $(this).xtree('getParent',node.target);
				while(parentNode!=null){
					nodeArr.push(parentNode.id);
					parentNode = $(this).xtree('getParent',parentNode.target);
				}
			}
		},
		onContextMenu:function(e,node){
			e.preventDefault();
			$(this).xtree('select', node.target);
		}
	});
}

//新增用例
function newAddExample(){
//	$("#addExample").window({title:"增加用例"});
//	$("#addExample").window("vcenter");
//	$("#addExample").xwindow('open');
//	$("<div></div>").xwindow({
//    	id:'addExampleWindow',
//    	title:"增加用例",
//    	width : 600,
//        height : 600,
//    	modal:true,
////    	footer:'#bugAddOrEditFoot',
//    	collapsible:false,
//		minimizable:false,
//		maximizable:false,
//    	href:baseUrl + '/bugManager/bugManagerAction!addExampleInfoPage.action',
//    	queryParams: {
////    		"exampleListInfoId":"exampleListInfo"
//    	},
//        onClose : function() {
//            $(this).xwindow('destroy');
//            $("#exampleListInfo").xdatagrid('reload');
//        }
//    });
	
	 $("<div></div>").xwindow({
	    	id:'addOrEditWindown',
	    	title:"新增测试用例",
	    	width : 900,
	        height : 600,
	    	modal:true,
	    	footer:'#addOrEditFoot',
	    	collapsible:false,
			minimizable:false,
			maximizable:false,
	    	href:baseUrl + '/caseManager/caseManagerAction!caseAdd.action',
	    	queryParams: {
				"moudleId": $("#moduleIdH").data("moduleId"),
				"taskId": $("#taskId").val(),
				"flag":"iterationPage"
	    	},
	        onClose : function() {
	            $(this).xwindow('destroy');
	            $("#exampleListInfo").xdatagrid('reload');
	        }
	    });
}

//$('#example').next().find($('input[name="checkId"][type="checkbox"]')).click(function(){
//	var checkNum = $('#example').next().find($('input[name="checkId"][type="checkbox"]'));
//	var exampleNumV = "";
//	for(var i=0;i<checkNum.length;i++){
//		if(checkNum[i].checked){
//			exampleNumV = checkNum[i].parent().next().find('span').attr('id');
//			arrExam.push(exampleNumV);
//		}
//	}
//});

//获取所有选中的信息
function getSelectionsCheck(){
	var arrExam = [];
	var checkNum = $('#example').next().find($('input[name="checkId"][type="checkbox"]'));
	var exampleNumV = "";
	$.each( checkNum, function(i, n){
		if(checkNum[i].checked){
			exampleNumV = $(checkNum).eq(i).parents('td').next().find('span').attr('id');
			arrExam.push(exampleNumV);
		}
	});
//	for(var i=0;i<checkNum.length;i++){
//		if(checkNum[i].checked){
//			exampleNumV = checkNum.eq(i).parent().next().find('span').attr('id');
//			arrExam.push(exampleNumV);
//		}
//	}
	
	return arrExam;
}

//关联用例
function relaExample(){
//	var row = $("#exampleListInfo").xdatagrid('getSelections');
//	var testCaseId ="";
//	var arrss = arrFpage;
	//获取所有选中的信息
	var arrExams = getSelectionsCheck();
	if (arrExams.length<=0) {
		$.xalert({title:'提示',msg:'请选择要关联的记录！'});
		return;
	}
//	else{
//		testCaseId = row.map(function(value){
//			return value.testCaseId;
//		});
//	}
	
	var bugReptVer = $("#bugReptVers").data("bugReptVer");
	if(bugReptVer=="null"){
		bugReptVer="";
	}
	
	var dataMap = {
			"dto.bugId":$("#bugIds").data("bugId"),
			"dto.moduleId":$("#moduleIdH").data("moduleId"),
			"dto.testCaseIds":arrExams.toString(),
			"dto.bugReptVer":bugReptVer
	};
	
	$.ajax({
		  url: baseUrl+"/bugManager/relaCaseAction!bugRelaCase.action",
		  cache: false,
		  async: false,
		  type: "POST",
//		  dataType:"json",
		  dataType:"text",
		  data:dataMap,
		  success: function(data){
			  if(data.indexOf("success") >=0){
				  //关闭弹窗
				  $("#lookBugWindown").xwindow('close');
				  getBugList();
//				  loadTestProject();
			  }else{
				  $.xalert({title:'提示',msg:'系统问题！'});
			  }
		  }
		});
}

//增加
function bugAddWindow(){
	 $("<div></div>").xwindow({
	    	id:'bugAddOrEditWindown',
	    	title:"新增软件问题",
	    	width : 900,
	        height : 600,
	    	modal:true,
	    	footer:'#bugAddOrEditFoot',
	    	collapsible:false,
			minimizable:false,
			maximizable:false,
	    	href:baseUrl + '/bugManager/bugManagerAction!bugAdd.action',
	    	queryParams: {},
	        onClose : function() {
	            $(this).xwindow('destroy');
	        }
	    });
}
//修改
function bugEditWindow(){
	var row = $("#bugList").xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'提示',msg:'请选择要修改的一条记录！'});
		return;
	}
	 $("<div></div>").xwindow({
	    	id:'editBugWindown',
	    	title:"修改软件问题报告",
	    	width : 900,
	        height : 600,
	    	modal:true,
//	    	closed:true,
	    	collapsible:false,
			minimizable:false,
			maximizable:false,
	    	href:baseUrl + '/bugManager/bugManagerAction!bugEdit.action',
	    	queryParams: {},
	        onClose : function() {
	            $(this).xwindow('destroy');
	        }
	    });
}
//bug处理窗口
function bugHandWindow(bId,tId){
	$("<div></div>").xwindow({
    	id:'handBugWindown',
    	title:"处理软件问题",
    	width : 900,
        height : 600,
    	modal:true,
//    	closed:true,
    	collapsible:false,
		minimizable:false,
		maximizable:false,
    	href:baseUrl + '/bugManager/bugManagerAction!bugHandView.action',
    	queryParams: {
			"bId": bId,
			"tId": tId
		},
        onClose : function() {
            $(this).xwindow('destroy');
        }
    });
}

//查询框
function bugQueryWin(){
	 $("<div></div>").xwindow({
	    	id:'queryBugWin',
	    	title:"查询",
	    	width : 1020,
	        height : 470,
	    	modal:true,
//	    	closed:true,
	    	collapsible:false,
			minimizable:false,
			maximizable:false,
	    	href:baseUrl + '/bugManager/bugManagerAction!bugFind.action',
	    	queryParams: {'queryContainer':'bugList'},
	        onClose : function() {
	        	hysss = $("input[type='checkbox'][id='defBugId']").is(':checked');
	        	queryMap = $("#queryBugWin").xserialize();
	            $(this).xwindow('destroy');
	        }
	    });
}
//导出bug
function daochu(obj){
	var href = baseUrl + '/impExpMgr/bugImpExpAction!expBug.action?dto.isAllTestTask=false&dto.taskId='+taskId;
	if(hysss){
		href = href + '&dto.defBug=1'
	}
	for(var i in queryMap) {
		if(queryMap[i] && i != "dto.defBug"){
			href = href + "&" + i + "=" + queryMap[i];
		}
	}
	$(obj).prop('href', href);
}


//# sourceURL=bugManagerList.js