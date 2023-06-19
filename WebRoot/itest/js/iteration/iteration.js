//存放所有可执行任务的人员
var peopleList = [];
//存放所有项目
var projectList = [];
var bugCardId ="";
var otherMissionS ="";
var testCaseP ="";
var currAccount_Id = $("#accountId").html();
//存放所有任务类别
var missionCategoryList = [];
//存放所有任务紧急程度
var emergencyDegreeList = [];
//存放所有任务难易程度
var difficultyDegreeList = [];
//存放任务id
var missiId = "";
//存放修改进度为终止时的标志
var stopFlag = "0";
//保存选中的一条记录
var missionRow = {};
var objs = {
	$proBug:$("#proBug"),
	$proTask:$("#proTask"),
	$proTestcase:$("#proTestcase"),
	$addOrEditWin_itera:$("#addOrEditWin_itera"),
	$newCreatIteration: $("#newCreateIteration"),
	$iterationList: $("#iterationList"),
	$iterationSear: $("#iterationSear"),
	$iterationAdd: $("#iterationAddCon"),
	$iterationReset: $("#iterationReset"),
	$iterationEdit: $("#iterationEditCon"),
	$iterationDele: $("#iterationDeleteCon"),
	$iterationBugList:$("#iterationBugList"),
	$iterationTestCaseList:$("#iterationTestCaseList"),
	$addOrEditIterationF: $("#addOrEditIterationForm")
};

$(function(){
	$.parser.parse();
	exuiloader.load('numberbox', null, true);
//	getLoginUserProManager();
	$('#iterationAccordion').xaccordion({
		animate:true,
		iconCls:'icon-reload',
		fit:true,
		onSelect:function(title,index){
			var iterationId = $("#iterationId").val();
			if(index==1){
				getBugDetail(iterationId);
				$('.tab-content > div[class="panel panel-default datagrid panel-htop"]').height(272);
				$('#proBug').prev().find($('div[class="datagrid-body"]')).height(190);
				$('.tab-content > div[class="panel panel-default datagrid panel-htop"]').find($('div[class="datagrid-view"]')).height(230);
			}else{
				$("#iterationAccordion").find($('div[class="panel-heading active"]')).children(".panel-title.panel-with-icon").html(''+title+'');
			}
		}
//	,
//		onBeforeExpand:function(){
//			var index = $('#accord').xaccordion('getSelected');
//		}
	});
	
	$('input[name="dto.bug.reProStep"]').prev().css({height:'100px',marginTop:'5px'});
	
	//加载可执行任务员工列表
	loadPeopleLists();
	
	//获取所有全部的项目
	getAll_ProjectName();
	
	//迭代列表
	loadIterationList();
	
	loadProjectList();
	
	$(".sss").xcombobox({
		onChange:function(newValue,oldValue){
			if(newValue == "3"){
				$(".stopReason").val("");
				$(".stopZone").show();
				$(".zantingReason").val("");
				$(".zantingZone").hide();
			}else if(newValue == "4"){
				$(".stopReason").val("");
				$(".stopZone").hide();
				$(".zantingReason").val("");
				$(".zantingZone").show();
			}else{
				$(".stopReason").val("");
				$(".stopZone").hide();
				$(".zantingReason").val("");
				$(".zantingZone").hide();
			}
		}
	});
	//回车事件
	document.onkeydown=function(event){
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if(e && e.keyCode==13){ // 按 Esc
			searchIteration();
		}
	};
	
	$(".panel-heading").click(function(){
		$(".searchable-select-dropdown").addClass("searchable-select-hide");
	});
});

//获取所有全部的项目
function getAll_ProjectName(){
	$.post(
			baseUrl + "/otherMission/otherMissionAction!getProjectLists.action",
			null,
			function(dat) {
				if (dat != null) {
					//加载下拉菜单选项(为管理员时)
					if($("#isAdmin").text() == "2" || $("#isAdmin").text() == "1"){
						var opti = '<option value="">-请选择项目-</option>';
						if(dat.rows.length > 0){
							for(var i=0;i<dat.rows.length;i++){
								opti = opti + '<option value="'+dat.rows[i].projectId+'">'+dat.rows[i].projectName+'</option>';
							}
						}
						$(".searchable-select").remove();
						$("#proAllName").html(opti);
						$('#proAllName').searchableSelect();
						$("#proAllName").next("div").css("min-width", "140px");
					}else{
						//加载下拉菜单选项(非管理员时)
						var opti = '<option value="">-请选择项目-</option>';
						$.post(baseUrl + "/otherMission/otherMissionAction!getProjectListsRelated.action?dto.related=charge",null,function(datt) {
							if(datt.rows.length > 0){
								for(var p=0;p<datt.rows.length;p++){
									opti = opti + '<option value="'+datt.rows[p].projectId+'">'+datt.rows[p].projectName+'</option>';
								}
							}
							$(".searchable-select").remove();
							$("#proAllName").html(opti);
							$('#proAllName').searchableSelect();
							$("#proAllName").next("div").css("min-width", "140px");
						},'json');
					}
				} else {
					$.xnotify("系统错误！", {type:'warning'});
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}

//获取当前用户的权限
function getLoginUserProManager(){
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


//加载迭代列表
function loadIterationList(){
	objs.$iterationList.xdatagrid({
//		url: baseUrl + '/iteration/iterationAction!iterationDataListLoad.action?dto.iterationList.status=0'+'&dto.iterationList.userId='+currAccount_Id,
		url: baseUrl + '/iteration/iterationAction!iterationDataListLoad.action?dto.iterationList.status=0',
		method: 'get',
		height: mainObjs.tableHeight-100, 
		striped:true,
		fitColumns: true,
		rownumbers: true,
		singleSelect: true,
		pagination: true,
		pageList:[10,20,30],
//		pagePosition:'top',
		pageNumber: 1,
		pageSize: 10,
		emptyMsg:"暂无数据",
		layout:['list','first','prev','manual','next','last','refresh','info'],
		columns:[[
		    /*{field:'checkId',title:'选择',checkbox:true,align:'center'},*/
		    {field:'iterationId',title:'迭代id',hidden:true},
			{field:'iterationBagName',title:'迭代',width:'15%',align:'left'},
			{field:'associationProject',title:'关联项目名称',width:'25%',align:'center',halign:'center',formatter:proReal},
//			{field:'createPerson',title:'创建人',width:'15%',align:'center',halign:'center',formatter:createPerson},
//			{field:'createTime',title:'创建时间',width:'15%',align:'center',halign:'center',formatter:createTime},
			{field:'startTime',title:'开始时间',width:'15%',align:'center',halign:'center',formatter:startTime},
			{field:'endTime',title:'结束时间',width:'15%',align:'center',halign:'center',formatter:endTime},	
			{field:'note',title:'备注',width:'20%',align:'center',formatter:notes},
			{field:'operate',title:'操作',width:'12%',align:'center',formatter:operateFormat1}
		]],
		onLoadSuccess : function (data) {								
			/*if (data.total==0) {
				$('tr[id^="datagrid-row-r"]').parent().append('<label style="height: 40px;width: 100px;margin-top: 8px;margin-left:550px;margin-top:10px;text-align: center;">没有数据!</label>');
			}*/
			$('#iterationAccordion').find('div[class="panel-body accordion-body"]:eq(0)').height(470);
			$('#iterationAccordion').xaccordion("resize");
			objs.$iterationList.xdatagrid("resize");
			for(var i=0;i<$("#iterationAccordion tbody").find("tr").length;i++){
				$($("#iterationAccordion tbody").find("tr")[i]).attr("title","双击查看迭代明细");
				$($("#iterationAccordion tbody").find("tr")[i]).css("cursor","pointer");
			}
		},
		onDblClickRow: function (rowIndex, rowData){
			//控制详细列表数据，及accordion的样式
			iterationStyleAndData(rowIndex,rowData);
		}
		
	});
}
//操作
function operateFormat1(value,row,index){
	var html = "<a style='cursor:pointer;' onclick='showDetail1("+JSON.stringify(row)+")'>迭代报告</a>";
	return html;
}
//迭代报告
function showDetail1(row){
	$("#detailWin1").xwindow({title:'迭代报告'}).xwindow('open');
	//将所有数目都设置为0
	$("#11").html(0);
	$("#22").html(0);
	$("#33").html(0);
	$("#44").html(0);
	$("#55").html(0);
	$("#66").html(0);
	$("#aa").html(0);
	$("#bb").html(0);
	$("#cc").html(0);
	$("#dd").html(0);
	$("#fixCount").html(0);
	$("#noBugCount").html(0);
	$("#attributionMissions").html(0);
	$("#runingMissions").html(0);
	$("#finishMissions").html(0);
	$("#terminationMissions").html(0);
	$("#stopMissions").html(0);
	$("#allMissions").html(0);
	$.post(baseUrl+"/overview/overviewAction!getIterationDetails.action",{
		'dto.iterationId':row.iterationId
	},function(data){
		if(data[0].length > 0){
			var allBugCounts = 0;
			for(var i=0;i<data[0].length;i++){
				if(data[0][i].status == 2){
					$("#22").html(data[0][i].countNum);
					allBugCounts = allBugCounts + data[0][i].countNum;
				}
				if(data[0][i].status == 3){
					$("#33").html(data[0][i].countNum);
					allBugCounts = allBugCounts + data[0][i].countNum;
				}
				if(data[0][i].status == 5){
					$("#44").html(data[0][i].countNum);
					allBugCounts = allBugCounts + data[0][i].countNum;
				}
				if(data[0][i].status == 1){
					$("#55").html(data[0][i].countNum);
					allBugCounts = allBugCounts + data[0][i].countNum;
				}
				if(data[0][i].status == 4){
					$("#66").html(data[0][i].countNum);
					allBugCounts = allBugCounts + data[0][i].countNum;
				}
			}
			$("#11").html(allBugCounts);
		}
		if(data[1].length > 0){
			$("#aa").html(data[1][0].allcount);
			$("#bb").html(data[1][0].validCout);
			$("#cc").html(data[1][0].closedCount);
			$("#dd").html(data[1][0].validCout - data[1][0].closedCount);
			$("#fixCount").html(data[1][0].fixCount);
			$("#noBugCount").html(data[1][0].noBugCount);
		}
		if(data[2].length > 0){
			var allCounts = 0;
			for(var i=0;i<data[2].length;i++){
				if(data[2][i].status == "0"){
					$("#attributionMissions").html(data[2][i].countNum);
					allCounts = allCounts + data[2][i].countNum;
				}
				if(data[2][i].status == "1"){
					$("#runingMissions").html(data[2][i].countNum);
					allCounts = allCounts + data[2][i].countNum;
				}
				if(data[2][i].status == "2"){
					$("#finishMissions").html(data[2][i].countNum);
					allCounts = allCounts + data[2][i].countNum;
				}
				if(data[2][i].status == "3"){
					$("#terminationMissions").html(data[2][i].countNum);
					allCounts = allCounts + data[2][i].countNum;
				}
				if(data[2][i].status == "4"){
					$("#stopMissions").html(data[2][i].countNum);
					allCounts = allCounts + data[2][i].countNum;
				}
			}
			$("#allMissions").html(allCounts);
		}
//		$("#11").html(data.allCount);
//		$("#22").html(data.passCount);
//		$("#33").html(data.failedCount);
//		$("#44").html(data.blockCount);
//		$("#55").html(data.noTestCount);
//		$("#66").html(data.invalidCount);
//		$("#77").html(data.waitModifyCount);
//		$("#88").html(data.waitAuditCount);
//		$("#aa").html(data.bugAllCount);
//		$("#bb").html(data.bugValidCout);
//		$("#cc").html(data.bugResolCount);
//		$("#dd").html(data.bugValidCout - data.bugResolCount);
//		$("#ee").html(data.meCharge);
//		$("#ff").html(data.meCreate);
//		$("#gg").html(data.meJoin);
//		$("#hh").html(data.meAll);
//		$("#fixCount").html(data.fixNoConfirmCount);
//		$("#noBugCount").html(data.noBugNoConfirmCount);
	},'json');
}
//备注
function notes(value,row,index){
	if(value==null || value==""){
		return "暂无";
	}else{
		return row.note;
	}
}
//创建人
function createPerson(value,row,index){
	if(value==null || value==""){
		return "暂无";
	}else{
		return row.createPerson;
	}
}
//创建时间
function createTime(value,row,index){
	if(value==null || value==""){
		return "暂无";
	}else{
		return row.createTime;
	}
}

//开始时间
function startTime(value,row,index){
	if(value==null || value==""){
		return "--";
	}else{
		return row.startTime.split(" ")[0];
	}
}

//结束时间
function endTime(value,row,index){
	if(value==null || value==""){
		return "--";
	}else{
		return row.endTime.split(" ")[0];
	}
}

//项目关联
function proReal(value,row,index){
	if(value==null || value==""){
		return "<span style=\"cursor: pointer;\" title=\"双击查看迭代明细\" href=\"javascript:;\">暂无</span>";
	}else{
		return "<span style=\"cursor: pointer;\" title=\"双击查看迭代明细\" href=\"javascript:;\">" + value + "</span>";
	}
}

//控制详细列表数据，及accordion的样式
function iterationStyleAndData(index,data){
	$('#iterationAccordion:first').find('div[class="panel-body accordion-body"]').css('display','none');
	$('#iterationAccordion:first').find('div[class="panel-tool"] a').eq(1).addClass('accordion-collapse accordion-expand');
	$('#iterationAccordion div[class="panel-heading active"]').next().find('tr[id^="datagrid-row-r"]').removeClass().addClass("datagrid-row");
	$('#iterationAccordion:last').find('div[class="panel panel-default accordion-header panel-htop panel-last"]').children('div[class="panel-body accordion-body"]').css('display','block');
	$('#iterationAccordion:last').find('div[class="panel panel-default accordion-header panel-htop panel-last"] div[class="panel-tool"] a').eq(1).removeClass().addClass('accordion-collapse');
	var rtaskId = data.taskId;
	var rIterId = data.iterationId;
	if(data.associationProject && data.associationProject != "暂无"){
		$("#iterationAccordion").find($('div[class="panel-heading active"]')).children(".panel-title.panel-with-icon").html('迭代列表 -- '+data.associationProject + ' -- '+ data.iterationBagName);
	}else{
		$("#iterationAccordion").find($('div[class="panel-heading active"]')).children(".panel-title.panel-with-icon").html('迭代列表 -- '+ data.iterationBagName);
	}
//	.attr('title','迭代列表--'+data.associationProject+'');
//	$("#iterationAccordion > div").attr('title','迭代列表--双击查看明细');
	$("#taskIdCl").val(rtaskId);
	$("#iterationId").val(rIterId);
	if(rtaskId!=""&&rtaskId!=null){
		//获取缺陷bug的详细
		getBugDetail(rIterId);
		//测试包的详细
//		getTestcaseDetail(rtaskId);
		//项目任务的详细
//		getProTaskDetail(rtaskId);
		
		$("#addBug").show();
		$("#addTask").hide();
		$("#addTestCase").hide();
		$('ul[class="nav nav-lattice"]').children().eq(0).removeClass();
		$('ul[class="nav nav-lattice"]').children().eq(0).addClass('active');
		$('ul[class="nav nav-lattice"]').children().eq(1).removeClass();
		$('ul[class="nav nav-lattice"]').children().eq(2).removeClass();
		$('div[class="tab-content"]').children().eq(1).hide();
		$('div[class="tab-content"]').children().eq(2).hide();
	}else{
		$('ul[class="nav nav-lattice"]').children().eq(0).removeClass();
		$('ul[class="nav nav-lattice"]').children().eq(1).addClass('active');
		$('ul[class="nav nav-lattice"]').children().eq(0).removeClass().addClass('disabled');
		$('ul[class="nav nav-lattice"]').children().eq(2).removeClass().addClass('disabled');
		$('div[class="tab-content"]').children().eq(0).hide();
		$('div[class="tab-content"]').children().eq(2).hide();
		var taskTaIds = $("#taskIdCl").val();
		$("#addTask").show();
		$("#addBug").hide();
		$("#addTestCase").hide();
		getProTaskDetail(rIterId);
	}
}

//点击缺陷bug
$('a[href="#proBug"]').click(function(){
	$("#addTask").hide();
	$("#addBug").show();
	$("#addTestCase").hide();
	var iterationId = $("#iterationId").val();
	$('div[class="tab-content"]').children().eq(1).hide();
	$('div[class="tab-content"]').children().eq(2).hide();
	getBugDetail(iterationId);
});

//点击项目任务
$('a[href="#proTask"]').click(function(){
	$("#addTask").show();
	$("#addBug").hide();
	$("#addTestCase").hide();
	var taskIterId = $("#iterationId").val();
	$('div[class="tab-content"]').children().eq(0).hide();
	$('div[class="tab-content"]').children().eq(2).hide();
	getProTaskDetail(taskIterId);
});

//点击测试包
function projectTestCase(){
	$("#addTestCase").show();
	$("#addTask").hide();
	$("#addBug").hide();
	var iteraId = $("#iterationId").val();
	$('div[class="tab-content"]').children().eq(0).hide();
	$('div[class="tab-content"]').children().eq(1).hide();
	$('div[class="tab-content"]').children().eq(2).show();
	getTestcaseDetail(iteraId);
}
//$('a[href="#proTestcase"]').click(function(){
//	
//});

//获取缺陷bug的详细
function getBugDetail(iterId){
	$("#addBug").show();
	$("#addTask").hide();
	$("#addTestCase").hide();
	objs.$proBug.xdatagrid({
		url: baseUrl + '/iteration/iterationAction!searchBugDetail.action',
//		url:baseUrl + '/bugManager/bugManagerAction!loadAllMyBugList.action?dto.bug.taskId='+taId+'&dto.taskFlag=1',
		method: 'get',
//		height: mainObjs.tableHeight-100, 
		striped:true,
		queryParams:{'dto.iterationList.iterationId':iterId},
		pagination: true,
		fitColumns: true,
		rownumbers: false,
		singleSelect: true,
		pageList:[4,8,12,16],
//		pagePosition:'top',
		pageNumber: 1,
		pageSize: 4,
		emptyMsg:"暂无数据",
		layout:['list','first','prev','manual','next','last','refresh','info'],
		columns:[[
//		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'bugId',title:'编号',width:'5%',align:'center',formatter:bugDetails},
			{field:'bugDesc',title:'bug描述',width:'30%',align:'left',halign:'center',formatter:bugTitles},
			{field:'stateName',title:'状态',width:'10%',align:'center',formatter:bug_Hands},
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
			{field:'devName',title:'开发人员',width:'14%',align:'center',formatter:developers},
			{field:'reptDate',title:'报告日期',width:'13%',align:'left'}
		]],
		onLoadSuccess : function (data) {	
			var bugIds="";
			if (data.total==0) {
//				var contentBug = $('div[class="tab-content"]').children().eq(0).find($('tr[id^="datagrid-row-r"]'));
//				contentBug.empty();
//				contentBug.append('<label style="height: 40px;width: 900px;margin-left: 16%;margin-top: 8px;text-align: center;">暂无数据!</label>');
			}else{
				for(var i=0; i<data.rows.length;i++){
					bugIds = bugIds+data.rows[i].bugId+" ";
				}
				$("#bugIds").val($.trim(bugIds));
			}
			$('.tab-content > div[class="panel panel-default datagrid panel-htop"]').height(272);
			$('#proBug').prev().find($('div[class="datagrid-body"]')).height(190);
			$('.tab-content > div[class="panel panel-default datagrid panel-htop"]').find($('div[class="datagrid-view"]')).height(230);
			$('#iterationAccordion').find('div[class="panel-body accordion-body"]:eq(0)').height(470);
		}
	/*,
		onDblClickRow: function (rowIndex, rowData){
			
		}*/
	});
}

//开发人员
function developers(value,row,index){
	if(value==null || value=="null"){
		return "";
	}else{
//		return "<a style=\"cursor: pointer;\" title=\"意见交流\" href=\"javascript:;\">" + value + "</a>";
		return "<a style=\"cursor: pointer;\" title=\"意见交流\" href=\"javascript:;\" onclick=\"exchange_Opinions('"+row.bugId+"')\">" + value + "</a>";
	}
}

//意见交流窗的打开
function exchange_Opinions(bugId){
	//查询点击bug的基本信息
//	exchangeOpinionInfo(bugId);
	$('#bug_Ids').data('bugId',bugId);
	$('#bugId_Ex').val(bugId);
	$("#suggestion_Window").xwindow({title:'意见交流'}).xwindow('open');
	$("#suggestion_Window").window("vcenter");
	$('a[href="#baseInfosuggs"]').parent().removeAttr('class');
	$("#examplesuggs").addClass('tab-pane fade in active');
	$("#baseInfosuggs").removeClass().addClass("tab-pane fade");
	showSuggest_Info();
//	$('#example_sugg').next().hide();
}

$('a[href="#baseInfosuggs"]').click(function(){
	var bugId = $('#bug_Ids').data('bugId');
	//查询点击bug的基本信息
	exchange_OpinionInfo(bugId);
	$('input[name="dto.bug.reProStep"]').prev().height(100);
	$('a[href="#baseInfosuggs"]').parent().attr('class','active');
	$('a[href="#examplesuggs"]').parent().removeAttr('class');
	$("#baseInfosuggs").addClass('tab-pane fade in active');
	$("#examplesuggs").removeClass().addClass("tab-pane fade");
	$('#examplesuggs').next().hide();
});

//查询点击bug的基本信息
function exchange_OpinionInfo(bugIds){
	$('#bug_Ids').data("bugId",bugIds);
	$("#bugId_Ex").val(bugIds);
	$.get(
			baseUrl + "/bugManager/bugManagerAction!viewBugDetal.action",
			{'dto.bug.bugId':bugIds,'dto.loadType':'2'},
			function(data,status,xhr){
				if(status=='success'){
					//回填信息
					backFill_Info(data,'2');
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
//					tip("系统错误！");
				}
			},
			"json");
}

//意见交流记录
function showSuggest_Info(){
	$("#express_Opinion").xdatagrid({
		url: baseUrl + '/bugManager/bugShortMsgAction!loadMsgList.action?dto.loadType='+'2'+'&dto.shortMsg.bugId='+$("#bug_Ids").data("bugId"),
		method: 'get',
		striped:true,
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
		columns:[[
//		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
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
				/*$('#express_Opinion').prev().find('div.datagrid-body').append('<label style="height: 40px;width: 530px; text-align: center;">没有数据!</label>');*/
			}
		}
	
	});
}

//打开意见交流弹窗
function express_Opinion(){
	$("#express_OpinionNew").xwindow({title:'发表新意见'}).xwindow('open');
	$("#express_OpinionNew").window("vcenter");
}

//重置意见交流弹窗
function addExpress_Reset(){
	$('#express_OpinionNew').xform('clear');
}
//关闭意见交流弹窗
function canlce_ExpressWin(){
	addExpress_Reset();
	$('#express_OpinionNew').xwindow('close');
}

$("#closeSuggWin").click(function(){
	$('#suggestion_Window').xform('clear');
	$('#suggestion_Window').xwindow('close');
});

//提示选择发送给的人
function tipSendPerson(){
	if($("#hghghghh").xcombobox("getValue")){
		return true;
	}else{
		return false;
	}
}

//增加新意见
function addExpress_Submit(){
	//提示选择发送给的人
	var flg = tipSendPerson();
	if(!flg){
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
		  data: $("#express_OpinionNew").xserialize(),
		  success: function(data){
			  if(data.indexOf("success") >=0){
				  //关闭弹窗
				  $("#express_OpinionNew").xwindow('close');
				  addExpress_Reset();
				 $('#express_Opinion').xdatagrid('reload');
			  }else{
//				  $.xnotify('系统问题！', {type:'warning'});
//				  tip("系统问题！");
				  //$.xalert({title:'提示',msg:'系统错误！'});
			  }
		  }
		});
}

//bug处理
function bug_Hands(value,row,index) {
	return "<a style=\"cursor: pointer;\" title=\"bug处理\" href=\"javascript:;\" onclick=\"bugHandOper('"+row.bugId+"','"+row.taskId+"')\">" + value + "</a>";
//	return "<a style=\"cursor: pointer;\" title=\"bug处理\" href=\"javascript:;\">" + value + "</a>";
}

//bug详情
function bugDetails(value,row,index) {
	return "<a style=\"cursor: pointer;\" title=\"关联用例\" href=\"javascript:;\" onclick=\"lookForm_Window('"+row.bugId+"','"+row.moduleId+"','"+row.bugReptVer+"')\">" + value + "</a>";
//	return "<a style=\"cursor: pointer;\" title=\"bug详情：--"+value+"\" href=\"javascript:;\">" + value + "</a>";
}

function bugTitles(value,row,index) {
	return "<p style=\"cursor: pointer;margin:0;\" title=\"bug详情：--"+value+"\">" + value + "</p>";
}

//查看
function lookForm_Window(bugIds,moduleId,bugReptVer,taskId){
	$("#bug_Ids").data("bugId",bugIds);
	$("#bugId_Ex").val(bugIds);
	$("#module_IdH").data("moduleId",moduleId);
	$("#bugRept_Vers").data("bugReptVer",bugReptVer);
	$.get(
		baseUrl + "/bugManager/bugManagerAction!viewBugDetal.action",
		{'dto.bug.bugId':bugIds,'dto.loadType':'2'},
		function(data,status,xhr){
			if(status=='success'){
//				$("#lookBugWindown").xdeserialize(data);
				setTimeout(function(){
					showExample_Info();
					//回填信息
					backFill_Info(data,'1');
				},500);
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},
		"json");

	$("#lookBug_Windown").xwindow({title:"关联用例"});
	$("#lookBug_Windown").xwindow("vcenter");
	$("#lookBug_Windown").xwindow("open");
	
	$('a[href="#example_"]').parent().attr('class','active');
	$('a[href="#base_Info"]').parent().removeAttr('class');
	$("#base_Info").removeClass().addClass("tab-pane fade");
	$("#example_").addClass('tab-pane fade in active');
	$("#example_").show();
}

//回填信息
function backFill_Info(data,param){
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

function showBase_Info(){
	$("#example_").next().hide();
}

//显示用例信息
function showExample_Info(){
	$("#example_ListInfo").xdatagrid({
		url: baseUrl + '/bugManager/relaCaseAction!loadRelaCase.action?dto.moduleId='+$("#module_IdH").data("moduleId")+'&dto.bugId='+$("#bug_Ids").data("bugId"),
		method: 'get',
		striped:true,
		checkOnSelect:true,
		selectOnCheck:true,
		singleSelect:false,
		height: mainObjs.tableHeight,
		emptyMsg:"暂无数据",
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
					var getBugId = $("#bug_Ids").data("bugId");
					//获取关联的实例，勾上
					getRelaExample_Check(getBugId);
//					var testCaseIds = data.testCaseIds;
//					$("#example_ListInfo").xdatagrid('loadData',dataRela);
//					$("#"+testCaseIds).parents().eq(1).prev().find($('input[name="checkId"]')).attr('checked',true);
					
				}else{
					//$('#example_ListInfo').prev().find('div.datagrid-body table tr').after('<label style="height: 40px;width: 530px; text-align: center;">没有数据!</label>');
				}
//			}
//			$("#exampleListInfo").xdatagrid('resize');$('#exampleListInfo').prev().find('div.datagrid-view2 div.datagrid-body table tr')
		}
	
	});
//	$("#exampleListInfo").xdatagrid('resize');
}

//获取关联的实例，勾上
function getRelaExample_Check(BugId){
	$.ajax({
		  url: baseUrl+"/bugManager/relaCaseAction!getBugRelaCaseData.action",
		  cache: false,
		  async: false,
		  type: "POST",
		  dataType:"json",
//		  dataType:"text",
		  data:{"dto.caseBugRela.bugId":BugId},
		  success: function(data){
			  if(data.length>0){
				  for(var i=0;i<data.length;i++){
						var testCaseIds = data[i].testCaseId;
						$("#"+testCaseIds).parents().eq(1).prev().find($('input[name="checkId"]')).attr('checked',true);
					}
				  //关闭弹窗
//				  $("#lookBugWindown").xwindow('close');
//				  getBugList();
//				  loadTestProject();
			  }
//			  else{
//				  $.xalert({title:'提示',msg:'系统问题！'});
//			  }
		  }
		});
}

//增加关联用例
function addExample_Submit(){
	$("#moduleIds").val($("#module_IdH").data("moduleId"));
	var url=baseUrl+"/caseManager/caseManagerAction!addCase.action";
	$.ajax({
		  url: url,
		  cache: false,
		  async: false,
		  type: "POST",
//		  dataType:"json",
		  dataType:"text",
		  data: $("#add_Example").xserialize(),
		  success: function(data){
			  if(data.indexOf("success") >=0){
				  //关闭弹窗
				  canlce_Win();
				  showExample_Info();
//				  loadTestProject();
			  }else{
				  //$.xalert({title:'提示',msg:'系统问题！'});
			  }
		  }
		});
}



//新增用例
function newAdd_Example(){
//	$("#add_Example").window({title:"增加用例"});
//	$("#add_Example").window("vcenter");
//	$("#add_Example").xwindow('open');
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
//    		"exampleListInfoId":"fromIterationPage"
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
				"moudleId": $("#module_IdH").data("moduleId"),
				"taskId": $("#taskIdCl").val(),
				"flag":"iterationPage"
	    	},
	        onClose : function() {
	            $(this).xwindow('destroy');
	            $("#example_ListInfo").xdatagrid('reload');
	        }
	    });
}

//重置用例弹窗的信息
function addExample_Reset(){
	$('#add_Example').xform('clear');
}
//关闭弹窗
function canlce_Win(){
	addExample_Reset();
	$('#add_Example').xwindow('close');
}

//获取所有选中的信息
function getSelections_Check(){
	var arrExam = [];
	var checkNum = $('#example_').next().find($('input[name="checkId"][type="checkbox"]'));
	var exampleNumV = "";
	$.each( checkNum, function(i, n){
		if(checkNum[i].checked){
			exampleNumV = $(checkNum).eq(i).parents('td').next().find('span').attr('id');
			arrExam.push(exampleNumV);
		}
	});
	return arrExam;
}

//关联用例
function rela_Example(){

	var arrExams = getSelections_Check();
	if (arrExams.length<=0) {
		$.xalert({title:'提示',msg:'请选择要关联的记录！'});
		return;
	}
	
	var bugReptVer = $("#bugRept_Vers").data("bugReptVer");
	if(bugReptVer=="null"){
		bugReptVer="";
	}
	
	var dataMap = {
			"dto.bugId":$("#bug_Ids").data("bugId"),
			"dto.moduleId":$("#module_IdH").data("moduleId"),
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
				  $("#lookBug_Windown").xwindow('close');
				  objs.$proBug.xdatagrid('reload');
			  }else{
//				  $.xnotify('系统问题！', {type:'warning'});
//				  $.xalert({title:'提示',msg:'系统问题！'});
			  }
		  }
		});
}

//bug处理窗口
function bugHandOper(bId,tId){
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
			"tId": tId,
			"fromPage":"iteration"
		},
        onClose : function() {
            $(this).xwindow('destroy');
            objs.$proBug.xdatagrid('reload');
        }
    });
}

//测试包的详细
function getTestcaseDetail(iterationId){
	objs.$proTestcase.xdatagrid({
		url:  baseUrl + '/iteration/iterationAction!searchTestCaseDetail.action',
		method: 'post',
//		height: mainObjs.tableHeight-140,
		queryParams:{'dto.iterationList.iterationId':iterationId},
		striped:true,
		pagination: true,
		fitColumns: true,
		rownumbers: false,
		pageList:[4,8,12,16],
//		pagePosition:'top',
		singleSelect: true,
		emptyMsg:"暂无数据",
		pageNumber: 1,
		pageSize: 4,
		layout:['list','first','prev','manual','next','last','refresh','info'],
		columns:[[
//	        {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'packageId',hidden:true},
			{field:'packageName',title:'测试包名称',width:'18%',height:'50px',align:'center',halign:'center'},
			{field:'execEnvironment',title:'执行环境',width:'12%',height:'50px',align:'center'},
			{field:'executor',title:'执行人',width:'25%',height:'50px',align:'center',halign:'center'},
			{field:'remark',title:'备注',width:'20%',height:'50px',align:'center'},
			{field:'operator',title:'操作',width:'28%',height:'50px',align:'center',formatter:operat_Format},
			{field:'testCaseNames',hidden:true}
		]],
		onLoadSuccess : function (data) {	
			var packageIds = "";
			if (data.total==0) {
//				var contentTestCase = $('div[class="tab-content"]').children().eq(2).find($('tr[id^="datagrid-row-r"]'));
//				contentTestCase.empty();
//				contentTestCase.append('<label style="height: 40px;width: 900px;margin-top: 8px;margin-left: 16%;text-align: center;">暂无数据!</label>');
			}else{
				for(var i=0; i<data.rows.length;i++){
					packageIds = packageIds+data.rows[i].packageId+" ";
				}
				$("#packageIds").val($.trim(packageIds));
			}
			
			$('.tab-content div[class="panel panel-default datagrid panel-htop"]').eq(2).height(272);
			$('#proTestcase').prev().find($('div[class="datagrid-body"]')).height(190);
			$('.tab-content div[class="panel panel-default datagrid panel-htop"]').eq(2).find($('div[class="datagrid-view"]')).height(230);
		}
	});
}

//"操作"列
function operat_Format(value,row,index){
	var isContainCurrUser = false; //当前登录这是否是测试用例包的执行人  false:不是
	var columnStr = "";
//	var columnStr = "<div>" +
//    "<a type='button' style='cursor:pointer; padding:2px 5px!important;margin: 5px 18px 5px 0;color:#1e7cfb' onclick='selTestCase(\""+ row.packageId + "\",\"" + row.packageName + "\")' >" +
//    "关联用例</a>";
	var viewBtnStr =  "<div><a type='button' style='cursor:pointer;padding:2px 5px!important;margin: 5px 0 5px 0;color:#1e7cfb'  onclick='viewTest_Case(\""+ row.packageId + "\",\"" + row.packageName + "\")' >" +
    "查看用例</a>";
	var execBtnStr =  "<div><a type='button'  style='cursor:pointer;padding:2px 5px!important;margin: 5px 0 5px 0;color:#1e7cfb' onclick='executeTest_Case(\""+ row.packageId + "\",\"" + row.packageName + "\")' >" +
    "执行用例</a>";
	if(null != row.userTestCasePkgs){
		var userIdsArr = row.userTestCasePkgs;
		for(var i=0;i<userIdsArr.length;i++){
			if(userIdsArr[i].userId === currAccount_Id){
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

function viewTest_Case(packageId,pkgName){
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

function executeTest_Case(packageId,pkgName){
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
//               var executeWin = document.getElementById('addOrEditFoot').parentNode;
//               document.body.removeChild(executeWin);
	        }
	    });
}

//查看结果
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
//项目任务的详细
function getProTaskDetail(iterationId){
	objs.$proTask.xdatagrid({
		url:  baseUrl + '/iteration/iterationAction!searchIteraTaskDetail.action',
		method: 'post',
//		height: mainObjs.tableHeight-140,
		queryParams:{'dto.iterationList.iterationId':iterationId},
		striped:true,
		emptyMsg:"暂无数据",
		pagination: true,
		fitColumns: true,
		rownumbers: false,
		singleSelect: true,
		pageList:[4,8,12,16],
//		pagePosition:'top',
		pageNumber: 1,
		pageSize: 4,
		layout:['list','first','prev','manual','next','last','refresh','info'],
		columns:[[
//		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'missionName',title:'任务名称',width:"10%",align:'center'},
			{field:'projectId',title:'所属项目',width:"10%",align:'center',formatter:projectFormat},
			{field:'missionId',title:'任务参与者',width:"15%",align:'center',formatter:missionPersonFormat},
			{field:'chargePersonId',title:'任务负责人',width:"10%",align:'center',halign:'center',formatter:chargePersonFormat},
			{field:'actualWorkload',title:'实际工作量(小时)',width:"12%",align:'center'},
			{field:'description',title:'任务描述',width:"15%",align:'center',formatter:descriptionFormat},
			{field:'completionDegree',title:'任务进度(%)',width:"10%",align:'center'},
			{field:'status',title:'状态',width:"8%",align:'center',formatter:status_Format},
//			{field:'predictStartTime',title:'预计开始时间',width:"10%",align:'center',formatter:predictStartTimeFormat},
//			{field:'predictEndTime',title:'预计完成时间',width:"10%",align:'center',formatter:predictEndTimeFormat},
			{field:'ttt',title:'操作',align:'center',width:"12%",formatter:operation_Formats}
		]],
		onLoadSuccess : function (data) {
			var mission_Id="";
			if (data.total==0) {
//				var contentTask = $('div[class="tab-content"]').children().eq(1).find($('tr[id^="datagrid-row-r"]'));
//				contentTask.empty();
//				contentTask.append('<label style="height: 40px;width: 900px;margin-top: 8px;margin-left: 16%;text-align: center;">暂无数据!</label>');
			}else{
				for(var i=0; i<data.rows.length;i++){
					mission_Id = mission_Id+data.rows[i].missionId+" ";
				}
			}
			$("#mission_id").val($.trim(mission_Id));
			$('.tab-content div[class="panel panel-default datagrid panel-htop"]').eq(1).height(272);
			$('#proTask').prev().find($('div[class="datagrid-body"]')).height(190);
			$('.tab-content div[class="panel panel-default datagrid panel-htop"]').eq(1).find($('div[class="datagrid-view"]')).height(230);
//			$("#testTaskDg").xdatagrid('resize');
		}
	});
}

//操作
function operation_Formats(value,row,index){
	if(row.status != "3" && row.chargePersonId == $("#accountId").text() && row.status != "2"){
		var html = "<a style='cursor:pointer;' onclick='showEdit_Wins("+JSON.stringify(row)+")'>填写进度</a><a style='margin-left:5px;cursor:pointer;' onclick='show_Detail("+JSON.stringify(row)+")'>详情</a>";
		return html;
	}else{
		var html = "<a style='cursor:pointer;' onclick='show_Detail("+JSON.stringify(row)+")'>详情</a>";
		return html;
	}
	
}

//查看详情
function show_Detail(row){
	$("#detail_Win .nav-lattice").show();
	missiId = row.missionId;
	$("#detail_Win .nav-lattice").children().addClass("active");
	$("#detail_Win .nav-lattice").children().next().removeClass("active");
	$("#xiang_qing").addClass("in active");
	$("#ri_zhi").removeClass("in active");
	var uu = 0;
	var ii = 0;
	var pp = 0;
	for(var i=0;i<projectList.length;i++){
		if(row.projectId == projectList[i].projectId){
			row.projectId = projectList[i].projectName;
			break;
		}
	}
	
	for(var t=0;t<peopleList.length;t++){
		if(row.chargePersonId == peopleList[t].id){
			row.chargePersonId = peopleList[t].name;
			break;
		}
	}
	if(missionCategoryList.length > 0){
		for(var p=0;p<missionCategoryList.length;p++){
			if(row.missionCategory == missionCategoryList[p].typeId){
				row.missionCategory = missionCategoryList[p].typeName;
				break;
			}else{
				pp = pp + 1;
			}
		}
		if(pp == missionCategoryList.length){
			row.missionCategory = "";
		}
	}else{
		row.missionCategory = "";
	}
	if(emergencyDegreeList.length > 0){
		for(var q=0;q<emergencyDegreeList.length;q++){
			if(row.emergencyDegree == emergencyDegreeList[q].typeId){
				row.emergencyDegree = emergencyDegreeList[q].typeName;
				break;
			}else{
				uu = uu + 1;
			}
		}
		if(uu == emergencyDegreeList.length){
			row.emergencyDegree = "";
		}
	}else{
		row.emergencyDegree = "";
	}
	
	if(difficultyDegreeList.length > 0){
		for(var h=0;h<difficultyDegreeList.length;h++){
			if(row.difficultyDegree == difficultyDegreeList[h].typeId){
				row.difficultyDegree = difficultyDegreeList[h].typeName;
				break;
			}else{
				ii = ii + 1;
			}
		}
		if(ii == difficultyDegreeList.length){
			row.difficultyDegree = "";
		}
	}else{
		row.difficultyDegree = "";
	}
	
	if(row.status == "0"){
		row.status = "分配";
	}else if(row.status == "1"){
		row.status = "进行中";
	}else if(row.status == "2"){
		row.status = "完成";
	}else if(row.status == "3"){
		row.status = "终止";
	}else if(row.status == "4"){
		row.status = "暂停";
	}else{
		row.status = "";
	}
	if(row.predictStartTime){
		row.predictStartTime = row.predictStartTime.substring(0,10);
	}else{
		row.predictStartTime = "";
	}
	if(row.predictEndTime){
		row.predictEndTime = row.predictEndTime.substring(0,10);
	}else{
		row.predictEndTime = "";
	}
	if(!row.stopReason){
		row.stopReason = "";
	}
	$.ajax({
		  url: baseUrl + "/otherMission/otherMissionAction!getUserNames.action",
		  cache: false,
		  async: false,
		  type: "POST",
		  data: {
			  'dto.otherMission.missionId':row.missionId
		  },
		  dataType:"text",
		  success: function(data){
			  if (data !=null) {
				  row.peoples = data;
				  $.post(baseUrl + '/otherMission/otherMissionAction!getConcernNames.action',{'dto.otherMission.missionId':row.missionId},function(da){
					  if(da != null){
						  if(da == "failed"){
							  row.concerns = "";
						  }else{
							  row.concerns = da;
						  }
						  $("#detail_Table").empty().append("<tr>"
						    		+"<th style='text-align:right'>任务名称：</th>"
						    		+"<td style='width:160px'>"+row.missionName+"</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务描述：</th>"
						    		+"<td style='width:160px;' title='"+row.description+"'>"+row.description.substring(0,10)+"...</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务类别：</th>"
						    		+"<td style='width:160px'>"+row.missionCategory+"</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		/*+"<th style='text-align:right'>任务类型：</th>"
						    		+"<td style='width:160px'>其他任务</td>"*/
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>所属项目：</th>"
						    		+"<td style='width:160px'>"+row.projectId+"</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务参与者：</th>"
						    		+"<td style='width:160px'>"+row.peoples+"</td>"
						    		+"<th style='text-align:right'>任务负责人：</th>"
						    		+"<td style='width:160px'>"+row.chargePersonId+"</td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务关注者：</th>"
						    		+"<td style='width:160px'>"+row.concerns+"</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>紧急程度：</th>"
						    		+"<td style='width:160px'>"+row.emergencyDegree+"</td>"
						    		+"<th style='text-align:right'>难易程度：</th>"
						    		+"<td style='width:160px'>"+row.difficultyDegree+"</td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>标准工作量(小时)：</th>"
						    		+"<td style='width:160px'>"+row.standardWorkload+"</td>"
						    		+"<th style='text-align:right'>实际工作量(小时)：</th>"
						    		+"<td style='width:160px'>"+row.actualWorkload+"</td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务进度(%)：</th>"
						    		+"<td style='width:160px'>"+row.completionDegree+"</td>"
						    		+"<th style='text-align:right'>任务状态：</th>"
						    		+"<td style='width:160px'>"+row.status+"</td>"
						    		+"</tr>"
						    		+"<tr class='klklkl'>"
						    		+"<th style='text-align:right'>终止原因：</th>"
						    		+"<td style='width:160px;' title='"+row.stopReason+"'>"+row.stopReason.substring(0,10)+"...</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr class='lkl'>"
						    		+"<th style='text-align:right'>暂停原因：</th>"
						    		+"<td style='width:160px;' title='"+row.stopReason+"'>"+row.stopReason.substring(0,10)+"...</td>"
						    		+"<th style='text-align:right'></th>"
						    		+"<td style='width:160px'></td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>预计开始时间：</th>"
						    		+"<td style='width:160px'>"+row.predictStartTime+"</td>"
						    		+"<th style='text-align:right'>预计结束时间：</th>"
						    		+"<td style='width:160px'>"+row.predictEndTime+"</td>"
						    		+"</tr>"
						    		+"<tr>"
						    		+"<th style='text-align:right'>任务发起人：</th>"
						    		+"<td style='width:160px'>"+row.createUserId+"</td>"
						    		+"<th style='text-align:right'>任务创建时间：</th>"
						    		+"<td style='width:160px'>"+row.createTime+"</td>"
						    		+"</tr>");
						  if(row.status == "终止"){
								$(".klklkl").show();
								$(".lkl").hide();
							}else if(row.status == "暂停"){
								$(".klklkl").hide();
								$(".lkl").show();
							}else{
								$(".klklkl").hide();
								$(".lkl").hide();
							}
					  }
				  },'text');
				} else {
					/*$.xnotify("系统错误！", {type:'warning'});*/
					$.xalert({title:'提示',msg:'系统错误！'});
				}
		   }
		});
	$("#detail_Win").parent().css("border","none");
	$("#detail_Win").prev().css({ color: "#ffff", background: "#101010" });
	/*loadOtherMissionLog(row.missionId);*/
	$("#xiang_qing").show();
	$("#xiang_qing").next().hide();
	$("#detail_Win").xwindow('setTitle','详情').xwindow('open');
}

//关闭详情页
function close_DetailWin(){
	$("#detail_Win").xwindow('close');
}

$('a[href="#ri_zhi"]').click(function(){
	$("#xiang_qing").hide();    
	$("#detailClose").hide();
	$("#detailClose").next().css('margin','20px 0 20px 0');
	loadOtherMissionLog(missiId);
	$("#xiang_qing").next().show();
});

$('a[href="#xiang_qing"]').click(function(){
	$("#xiang_qing").show();
	$("#detailClose").show();
	$("#detailClose").next().removeAttr('style').css({width:"600px",height:"400px"});
	$("#xiang_qing").next().hide();
});

//显示日志datagrid
function loadOtherMissionLog(id){
	$("#ri_zhi").xdatagrid({
		url: baseUrl + '/otherMission/otherMissionAction!getMissionLog.action',
		method: 'post',
		queryParams: {
			"dto.otherMission.missionId":id
		},
		emptyMsg:"无数据",
		fitColumns: true,
		singleSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
//		pageList:[10,30,50],
		layout:['list','first','prev','manual','next','last','refresh','info'],
		columns:[[
	        {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'operateTime',title:'操作时间',width:"25%",align:'center',formatter:operate_TimeFormat},
			{field:'operatePerson',title:'操作者',width:"25%",align:'center'},
			{field:'operateType',title:'操作类型',width:"25%",align:'center',formatter:operate_TypeFormat},
			{field:'operateDetail',title:'操作详情',align:'center',width:"25%",formatter:operate_DetailFormat}
		]],
		onSelect : function(index,row){
			$(".datagrid-row-checked").css({"background":"none","color":"#404040"});
		},
		onLoadSuccess : function (data) {								
			
		}
	});
}

function operate_TimeFormat(value,row,index){
	if(value){
		return "<span title='"+value+"'>"+value.substring(0,10)+"</span>";
	}
	return "";
}
function operate_TypeFormat(value,row,index){
	if(value == "1"){
		return "新建任务并分配";
	}else if(value == "2"){
		return "修改状态";
	}else if(value == "3"){
		return "填写进度";
	}else if(value == "4"){
		return "修改工作量";
	}
}
function operate_DetailFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,20)+".....</span>";
}

//打开填写进度弹窗
function showEdit_Wins(row) {
	missionRow = row;
	stopFlag = "0";
	var fillData = {};
	fillData["dto.otherMission"] = row;
	objs.$addOrEditWin_itera.xdeserialize(fillData);
	if(row.status == "4"){
		$(".zantingReason").val(row.stopReason);
	}
	if(row.status == "0"){
		$(".sss").xcombobox("readonly",true);
	}else{
		$(".sss").xcombobox("readonly",false);
	}
	objs.$addOrEditWin_itera.parent().css("border","none");
	objs.$addOrEditWin_itera.prev().css({ color: "#ffff", background: "#101010" });
	objs.$addOrEditWin_itera.xwindow('setTitle','修改其他任务').xwindow('open');
}
//改变任务状态
function changeMissionStatus(missionValue){
	if(missionRow.status == "0"){
		if(missionValue != "100" && missionValue != ""){
			$(".sss").xcombobox("setValue","1");
		}else if(missionValue == ""){
			$(".sss").xcombobox("setValue","0");
		}else{
			$(".sss").xcombobox("setValue","2");
		}
	}
}
//取消提交并关闭弹窗
function closeWin_iter() {
	objs.$addOrEditWin_itera.xwindow('close');
}

//提交修改的记录
function submit11(){
	//获取表单数据
	var objData = objs.$addOrEditWin_itera.xserialize();
	if(objData["dto.otherMission.status"] == "3" && stopFlag == "0"){
		$.xconfirm({
			msg:'修改状态为终止后，不能再填写进度，你确定吗？',
			okFn: function() {
				stopFlag = "1";
				submit_iter();
			}
		});
	}else if(objData["dto.otherMission.status"] == "3" && stopFlag == "1"){
		submit_iter();
	}else{
		submit_iter();
	}
}
// 提交修改的记录
function submit_iter() {
	var saveOrUpdateUrl = baseUrl + "/otherMission/otherMissionAction!updateStatus.action";
	
	//获取表单数据
	var objData = objs.$addOrEditWin_itera.xserialize();
	/*if(objData["dto.otherMission.status"] == "3"){
		objData["dto.otherMission.stopReason"] = objData["stopReason"];
	}
	if(objData["dto.otherMission.status"] == "4"){
		objData["dto.otherMission.stopReason"] = objData["zantingReason"];
	}*/
	/*原始记录为分配时，并且进度不是100，弄成进行中状态*/
	if(missionRow.status == "0"){
		if(objData["dto.otherMission.completionDegree"] != "100"){
			objData["dto.otherMission.status"] = "1";
		}else{
			objData["dto.otherMission.status"] = "2";
		}
	}else{
		if(objData["dto.otherMission.completionDegree"] != "100"){
			if(objData["dto.otherMission.status"] == "3"){
				objData["dto.otherMission.stopReason"] = objData["stopReason"];
			}
			if(objData["dto.otherMission.status"] == "4"){
				objData["dto.otherMission.stopReason"] = objData["zantingReason"];
			}
		}else{
			objData["dto.otherMission.status"] = "2";
		}
	}
	if(!objData["dto.otherMission.actualWorkload"] || !objData["dto.otherMission.completionDegree"] || !objData["dto.otherMission.status"]){
		$.xalert({title:'提交失败',msg:'请填写完整所有必填项！'});
		return;
	}
	var t1 = /^\d+$/;
	var t2 = /^100$|^(\d|[1-9]\d)(\.\d+)*$/;
	if(!t1.test(objData["dto.otherMission.actualWorkload"]) || !t2.test(objData["dto.otherMission.completionDegree"])){
		$.xalert({title:'提交失败',msg:'请正确填写完整所有必填项！'});
		return;
	}
	if(objData["dto.otherMission.status"] == "3" || objData["dto.otherMission.status"] == "4"){
		if(!objData["dto.otherMission.stopReason"]){
			$.xalert({title:'提交失败',msg:'请填写完整所有必填项！'});
			return;
		}
	}
	$.post(
		saveOrUpdateUrl,
		objData,
		function(data) {
			if (data =="success") {
				objs.$addOrEditWin_itera.xform('clear');
				objs.$addOrEditWin_itera.xwindow('close');
				objs.$proTask.xdatagrid('reload');
				$.xalert({title:'提示',msg:'操作成功！'});
			} else {
				/*$.xnotify("系统错误！", {type:'warning'});*/
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		}, "text");
}
function predictEndTimeFormat(value,row,index){
	if(value){
		return value.substring(0,10);
	}
	return "";
}

function predictStartTimeFormat(value,row,index){
	if(value){
		return value.substring(0,10);
	}
	return "";
}

function descriptionFormat(value,row,index){
	return "<span title='"+value+"'>"+value.substring(0,5)+"....</span>";
}

//获取关联项目
function getProjectData(){
	/*原来*/
	/*$('input[name="dto.iterationList.associationProject"]').prev().xcombobox({
		//valueField:'taskId',
		//textField:'proName',
		valueField:'projectId',
		textField:'projectName',
		method:'post',//请求方式  
//	    dataType:'json',
		editable:false,
		hasDownArrow:true,
		singleSelect: true,
	    //url: baseUrl + '/singleTestTask/singleTestTaskAction!getAddAllProject.action',
		url: baseUrl + '/otherMission/otherMissionAction!getProjectLists1.action',
	    onSelect:function(rec){
	    	//var taskId = rec.taskId;
			var taskId = rec.projectId;
			$("#taskIds").val(taskId);
			$('input[name="dto.iterationList.associationProject"]').prev().find($('input[id^="_exui_textbox_input"]')).val(rec.proName);
		}
		
//		groupFormatter: function(group){
//			var taskId = rec.taskId;
//			$("#taskIds").val(taskId);
//			return '<span style="color:red">' + group + '</span>';
//		}
	});*/
	/*改后*/
	$.post(baseUrl + '/otherMission/otherMissionAction!getProjectLists1.action',null,function(data){
		var opti = '<option value="">-请选择项目-</option>';
		if(data.rows.length > 0){
			for(var i=0;i<data.rows.length;i++){
				opti = opti + '<option value="'+data.rows[i].projectId+'">'+data.rows[i].projectName+'</option>';
			}
		}
		$("#relationPro").next("div.searchable-select").remove();
		$("#relationPro").html(opti);
		$('#relationPro').searchableSelect();
		$("#relationPro").next("div").css("min-width", "330px");
	},'json');
}

//项目缺陷
function getProjectBugs(taId){
	var	urls=baseUrl + '/bugManager/bugManagerAction!loadAllMyBugList.action?dto.taskId='+taId+'&dto.taskFlag=1';
	
	$('input[name="dto.bugBaseInfo.bugDesc"]').prev().xcombogrid({
		idField:'bugId',
		textField:'bugDesc',
		method:'post',//请求方式  
	    dataType:'json',
		striped:true,
		fitColumns: true,
		rownumbers: false,
		multiple:true,
		pagination: true,
		pageNumber: 1,
		pageSize: 5,
		layout:['list','first','prev','manual','next','last','refresh','info'],
	    mode: 'remote',
//	    panelWidth:450,
	    url:urls,
		columns:[[
		        {field:'checkId',title:'选择',checkbox:true,align:'center'},
		        {field:'bugId',title:'编号',width:'10%',align:'center',formatter:bugDetail},
				{field:'bugDesc',title:'bug描述',width:'60%',align:'left',halign:'center',formatter:bugTitle},
				{field:'stateName',title:'状态',width:'30%',align:'center',formatter:bugHand}
				/*,
				{field:'typeName',title:'等级',width:'20%',align:'left',halign:'center'}*/
		]],
		onLoadSuccess : function (data) {								
//			if (data.total==0) {
//				$('tr[id^="datagrid-row-r"]').after('<label style="height: 40px;width:450px;margin-top:8px;text-align: center;">没有数据!</label>');
//			}
		},
		onSelect:function(rowIndex,rowData){
			 bugCardId=bugCardId+rowData.bugId+" ";
		},
		onUnselect:function(rowIndex, rowData){
			var bugsId = rowData.bugId;
			var bugCard = bugCardId.split(" ");
			bugCardId="";
			for(var i=0;i<bugCard.length;i++){
				if(bugCard[i]!=bugsId){
					bugCardId=bugCardId+bugCard[i]+" ";
				}
			}
		}
	});
}

//bug详情
function bugDetail(value,row,index) {
	return "<a href=\"javascript:;\">" + value + "</a>";
}

function bugTitle(value,row,index) {
	return "<p>" + value + "</p>";
}
//bug处理
function bugHand(value,row,index) {
	return "<a href=\"javascript:;\">" + value + "</a>";
}

//任务
function getProjectTask(tasId){
	var urls="";
	if(tasId!=undefined){
		urls=baseUrl + '/otherMission/otherMissionAction!otherMissionListLoad.action?dto.otherMission.projectId='+tasId;
	}else{
		urls=baseUrl + '/otherMission/otherMissionAction!otherMissionListLoad.action';
	}
	
	$('input[name="dto.otherMission.missionName"]').prev().xcombogrid({
		idField:'missionId',
		textField:'missionName',
		method:'post',//请求方式  
	    dataType:'json',
		striped:true,
		fitColumns: true,
		rownumbers: true,
		multiple:true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		layout:['list','first','prev','manual','next','last','refresh','info'],
	    mode: 'remote',
//	    panelWidth:450,
	    url: urls,
		columns:[[
		    {field:'checkId',title:'选择',checkbox:true,align:'center'},
			{field:'missionName',title:'任务名称',width:"30%",align:'center'},
			{field:'projectId',title:'所属项目',width:"20%",align:'center',formatter:projectFormat},
			{field:'missionId',title:'任务参与者',width:"20%",align:'center',formatter:missionPersonFormat},
			{field:'chargePersonId',title:'任务负责人',width:"20%",align:'center',halign:'center',formatter:chargePersonFormat}
			/*{field:'actualWorkload',title:'实际工作量(小时)',width:"11%",align:'center'},
			{field:'description',title:'任务描述',width:"8%",align:'center',formatter:descriptionFormat},
			{field:'completionDegree',title:'任务进度',width:"9%",align:'center'},
			{field:'status',title:'状态',width:"9%",align:'center',formatter:statusFormat},
			{field:'predictStartTime',title:'预计开始时间',width:"9%",align:'center',formatter:predictStartTimeFormat},
			{field:'predictEndTime',title:'预计完成时间',width:"9%",align:'center',formatter:predictEndTimeFormat},
			{field:'ttt',title:'操作',align:'center',width:"9%",formatter:operationFormat}*/
		]],
		onLoadSuccess : function (data) {								
//			if (data.total==0) {
//				$('tr[id^="datagrid-row-r"]').after('<label style="height: 40px;width:450px;margin-top:8px;text-align: center;">没有数据!</label>');
//				$('div[class="panel panel-default combo-p panel-htop"]')
//				.filter('[style*="display: block;"]')
//				.find('tr[id^="datagrid-row-r"]').after('<label style="height: 40px;width:450px;margin-top:8px;text-align: center;">没有数据!</label>');
//			}
		},
		onSelect:function(rowIndex,rowData){
			otherMissionS=otherMissionS+rowData.missionId+" ";
		},
		onUnselect:function(rowIndex,rowData){
			var missionIds = rowData.missionId;
			var otherMiss = otherMissionS.split(" ");
			otherMissionS="";
			for(var i=0;i<otherMiss.length;i++){
				if(otherMiss[i]!=missionIds){
					otherMissionS=otherMissionS+otherMiss[i]+" ";
				}
			}
		}
	});
}

function chargePersonFormat(value,row,index){
	for(var i=0;i<peopleList.length;i++){
		if(value == peopleList[i].id){
			return peopleList[i].name;
		}
	}
	return "暂无";
}

//加载所属项目下拉菜单
function loadProjectList(){
	$.post(
		baseUrl + "/otherMission/otherMissionAction!getProjectLists.action",
		null,
		function(dat) {
			if (dat != null) {
				projectList = dat.rows;
			} else {
//				$.xnotify("系统错误！", {type:'warning'});
//				tip("系统错误！");
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		}, "json");
}
//加载可执行任务员工列表
function loadPeopleLists(){
	$.post(
			baseUrl + "/otherMission/otherMissionAction!getPeopleLists.action",
			null,
			function(dat) {
				if (dat != null) {
					peopleList = dat.rows;
				} else {
//					$.xnotify("系统错误！", {type:'warning'});
//					tip("系统错误！");
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			}, "json");
}

function projectFormat(value,row,index){
	if(value){
		for(var i=0;i<projectList.length;i++){
			if(value == projectList[i].projectId){
				return projectList[i].projectName;
			}
		}
	}
	return "暂无";
}

function missionPersonFormat(value,row,index){
	var userNames = "";
	$.ajax({
	  url: baseUrl + "/otherMission/otherMissionAction!getUserNames.action",
	  cache: false,
	  async: false,
	  type: "POST",
	  data: {
		  'dto.otherMission.missionId':row.missionId
	  },
	  dataType:"text",
	  success: function(data){
		  if (data !=null) {
			  userNames = data;
			} else {
//				$.xnotify("系统错误！", {type:'warning'});
//				tip("系统错误！");
				$.xalert({title:'提示',msg:'系统错误！'});
			}
	   }
	});
	return "<span title='"+userNames+"'>"+userNames.substring(0,5)+"....</span>";
}

//测试包
function getTestCasePackage(tId){
	var urls = "";
	if(tId!=undefined){
		urls=baseUrl + '/testCasePkgManager/testCasePackageAction!loadTestCasePackageList.action?dto.testCasePackage.taskId='+tId;
	}else{
		urls= baseUrl + '/testCasePkgManager/testCasePackageAction!loadTestCasePackageList.action';
	}
	
	$('input[name="dto.testCasePackage.packageName"]').prev().xcombogrid({
		idField:'packageId',
		textField:'packageName',
		method:'post',//请求方式  
	    dataType:'json',
		striped:true,
		fitColumns: true,
		rownumbers: true,
		multiple:true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		layout:['list','first','prev','manual','next','last','refresh','info'],
	    mode: 'remote',
//	    panelWidth:450,
	    url:urls,
		columns:[[
				{field:'packageId',title:'选择',checkbox:true,align:'center'},
				{field:'packageName',title:'测试用例包',width:'20%',align:'center',halign:'center'},
				{field:'executor',title:'执行人',width:'48%',align:'center',halign:'center'},
				{field:'execEnvironment',title:'执行环境',width:'20%',align:'center'}
		]],
		onLoadSuccess : function (data) {								
//			if (data.total==0) {
//				$('tr[id^="datagrid-row-r"]').after('<label style="height: 40px;width:450px;margin-top:8px;text-align: center;">没有数据!</label>');
//			}
		},
		onSelect:function(rowIndex,rowData){
			testCaseP=testCaseP+rowData.packageId+" ";
		},
		onUnselect:function(rowIndex,rowData){
			var testCasePs = rowData.testCaseP;
			var testCase = testCaseP.split(" ");
			testCaseP="";
			for(var i=0;i<testCase.length;i++){
				if(otherMiss[i]!=testCasePs){
					testCaseP=testCaseP+testCase[i]+" ";
				}
			}
		}
	});
}
	

function proNameFormat(value,row,index) {
	var taskId = row.taskId;
	var taskType = row.taskType;
//	return '<a id="'+taskId+'_'+taskType+'" href="javascript:;" title="设置(查看)测试流程--'+value+'" onclick="showTestFlowWin(this.id)">' + value + '</a>';
	return '<a id="'+taskId+'_'+taskType+'" href="javascript:;">' + value + '</a>';
}

function status_Format(value,row,index) {
	if(value == "0"){
		return "分配";
	}else if(value == "1"){
		return "进行中";
	}else if(value == "2"){
		return "完成";
	}else if(value == "3"){
		return "终止";
	}else if(value == "4"){
		return "暂停";
	}
	return "";
}

function psmNameFormat(value,row,index) { 
	var html = new Array();
	if (row.testLdVos!=null&&row.testLdVos.length) {
		for (var i = 0; i < row.testLdVos.length; i++) {
			html.push(row.testLdVos[i].name);
		}
	}
	return html.join(',');
}

//保存
function saveOrEditIteration(){
	if(!$("#iteraName").xtextbox("getValue")){
		 $.xalert({title:'提示',msg:"请填写完整必填项"});
		 return;
	}
	
	var startTime = $(".startTime").xdatebox('getValue');
	var endTime = $(".endTime").xdatebox('getValue');
	
	if(!startTime){
		 $.xalert({title:'提示',msg:"请选择开始时间！"});
		 return;
	}
	
	if(!endTime){
		 $.xalert({title:'提示',msg:"请选择结束时间！"});
		 return;
	}
	
	var start_Time = new Date(startTime).getTime();
	var end_Time = new Date(endTime).getTime();

	if(start_Time>end_Time){
		 $.xalert({title:'提示',msg:"结束时间必须大于开始时间！"});
		 return;
	}
	
	var url=baseUrl+"/iteration/iterationAction!saveOrUpdateIteration.action";
	
	/*新增下面代码*/
	var projectName00 = "";
	if($('#relationPro').val()){
		projectName00 = $("#relationPro option:selected").text();
	}else{
		projectName00 = "暂无";
	}
	
	$.ajax({
		  url: url,
		  cache: false,
		  async: false,
		  type: "POST",
//		  dataType:"json",
		  dataType:"text",
		  data: {
			  "dto.iterationList.iterationId":$("#iterationId").val(),
			  "dto.iterationList.iterationBagName":$('input[name="dto.iterationList.iterationBagName"]').prev().val(),
			  /*改后*/
			  "dto.iterationList.associationProject":projectName00,
			  /*原来*/
			  /*"dto.iterationList.associationProject":$('input[name="dto.iterationList.associationProject"]').prev().find($('input[id^="_exui_textbox_input"]')).val(),*/
//			  "dto.bugBaseInfo.bugDesc":$('input[name="dto.bugBaseInfo.bugDesc"]').prev().find($('input[id^="_exui_textbox_input"]')).val(),
//			  "dto.otherMission.missionName":$('input[name="dto.otherMission.missionName"]').prev().find($('input[id^="_exui_textbox_input"]')).val(),
//			  "dto.testCasePackage.packageName":$('input[name="dto.testCasePackage.packageName"]').prev().find($('input[id^="_exui_textbox_input"]')).val(),
			  "dto.iterationList.note":$('input[name="dto.iterationList.note"]').prev().val(),
			  /*原来*/
			  /*"dto.iterationList.taskId":$("#taskIds").val(),*/
			  /*改后*/
			  "dto.iterationList.taskId":$('#relationPro').val(),
			  "dto.iterationList.userId":$("#accountId").text(),
			  "dto.iterationList.createPerson":$("#loginName").text(),
			  "dto.bugCardId":$.trim(bugCardId),
			  "dto.otherMissionS":$.trim(otherMissionS),
			  "dto.testCaseP":$.trim(testCaseP),
			  "dto.iterationList.startTime":startTime,
			  "dto.iterationList.endTime":endTime
			  
		  },
		  success: function(data){
			  if(data.indexOf("success") >=0 ){
				  var iterNotice = $("#iterationId").val();
				  var noticeW = "";
				  if(iterNotice!=""){
					  noticeW ="修改迭代信息成功！";
				  }else{
					  noticeW ="新增迭代信息成功！";
				  }
				  $.xalert({title:'提示',msg:noticeW});
				  cancleItera();
				  loadIterationList();

			  }else{
//				  $.xnotify('系统问题！', {type:'warning'});
//				  tip("系统问题！");
				  //$.xalert({title:'提示',msg:'系统问题！'});
			  }
		  }
		});
}

function checkIterData(){
	if($('input[name="dto.iterationList.iterationBagName"]').prev().val()==""){
//		tip('请输入迭代包名称！');
		$.xalert({title:'提示',msg:'请输入迭代包名称！'});
		return false;
	}else if($('input[name="dto.bugBaseInfo.bugDesc"]').prev().find($('input[id^="_exui_textbox_input"]')).val()==""){
//		tip('请选择项目缺陷！');
		$.xalert({title:'提示',msg:'请选择项目缺陷！'});
		return false;
	}else if($('input[name="dto.otherMission.missionName"]').prev().find($('input[id^="_exui_textbox_input"]')).val()==""){
//		tip('请选择项目任务！');
		$.xalert({title:'提示',msg:'请选择项目任务！'});
		return false;
	}else{
//		tip('请选择测试包！');
		$.xalert({title:'提示',msg:'请选择测试包！'});
		return false;
	}
	return true;
}


function tip(info){
	$.xconfirm({
		msg:info,
		okFn: function() {
			
		},
		cancelFn: function(){
			
		}
	});
}

//迭代列表窗口
function showIterationAdd(){
	objs.$newCreatIteration.xform('clear');
	objs.$newCreatIteration.xwindow('setTitle','新建迭代').xwindow('open');
	//加载下拉菜单选项(为管理员时)
	if($("#isAdmin").text() == "2" || $("#isAdmin").text() == "1"){
		getProjectData();//获取关联项目
	}else{
		//加载下拉菜单选项(非管理员时)
		notAdminRelatedProject();
	}
}

//加载下拉菜单选项(非管理员时)
function notAdminRelatedProject(){
	/*原来*/
	/*$('input[name="dto.iterationList.associationProject"]').prev().xcombobox({
		//valueField:'taskId',
		//textField:'proName',
		valueField:'projectId',
		textField:'projectName',
		method:'post',//请求方式  
//	    dataType:'json',
		editable:false,
		hasDownArrow:true,
		singleSelect: true,
	    //url: baseUrl + '/singleTestTask/singleTestTaskAction!getAddAllProject.action',
		url: baseUrl + "/otherMission/otherMissionAction!getProjectListsRelated.action?dto.related=charge",
	    onSelect:function(rec){
	    	//var taskId = rec.taskId;
			var taskId = rec.projectId;
			$("#taskIds").val(taskId);
			$('input[name="dto.iterationList.associationProject"]').prev().find($('input[id^="_exui_textbox_input"]')).val(rec.proName);
		}
		
//		groupFormatter: function(group){
//			var taskId = rec.taskId;
//			$("#taskIds").val(taskId);
//			return '<span style="color:red">' + group + '</span>';
//		}
	});*/
	/*改后*/
	$.post(baseUrl + "/otherMission/otherMissionAction!getProjectListsRelated.action?dto.related=charge",null,function(data){
		var opti = '<option value="">-请选择项目-</option>';
		if(data.rows.length > 0){
			for(var i=0;i<data.rows.length;i++){
				opti = opti + '<option value="'+data.rows[i].projectId+'">'+data.rows[i].projectName+'</option>';
			}
		}
		$("#relationPro").next("div.searchable-select").remove();
		$("#relationPro").html(opti);
		$('#relationPro').searchableSelect();
		$("#relationPro").next("div").css("min-width", "330px");
	},'json');
}

//关闭窗口
function cancleItera(){
	objs.$newCreatIteration.xform('clear');
	objs.$newCreatIteration.xwindow('close');
}

//点击X,清空表单
objs.$newCreatIteration.prev().find('a[class="panel-tool-close"]').click(function(){
	cancleItera();
});

//重置查询条件
function iterationReset(){
	$("#iterationBagName").val("");
	$("#createPerson").val("");
	$("#proAllName").text("");
	getAll_ProjectName();
}

//修改
function showIterationEditWin(){
	var row = objs.$iterationList.xdatagrid('getSelected');
	if (!row) {
		$.xalert({title:'提示',msg:'请点击一条需要修改的记录！'});
		return;
	}
	$("#iterationId").val(row.iterationId);
	$("#taskIds").val(row.taskId);
	$.post(
			baseUrl + "/iteration/iterationAction!findUpdateIterationInfo.action",
			{'dto.iterationList.iterationId': row.iterationId},
			function(data) {
				if (data !=null) {
					//加载下拉菜单选项(为管理员时)
					if($("#isAdmin").text() == "2" || $("#isAdmin").text() == "1"){
						/*原来*/
						/*getProjectData();*///获取关联项目
						/*改后*/
						$.post(baseUrl + '/otherMission/otherMissionAction!getProjectLists1.action',null,function(dat){
							var opti = '<option value="">-请选择项目-</option>';
							if(dat.rows.length > 0){
								for(var i=0;i<dat.rows.length;i++){
									opti = opti + '<option value="'+dat.rows[i].projectId+'">'+dat.rows[i].projectName+'</option>';
								}
							}
							$("#relationPro").next("div.searchable-select").remove();
							$("#relationPro").html(opti);
							if(data.iterationLists[0].associationProject!=""){
								$("#relationPro").val(data.iterationLists[0].taskId);
							}else{
								$("#relationPro").val("");
							}
							$('#relationPro').searchableSelect();
							$("#relationPro").next("div").css("min-width", "330px");
						},'json');
						
					}else{
						//加载下拉菜单选项(非管理员时)
						/*原来*/
						/*notAdminRelatedProject();*/
						/*改后*/
						$.post(baseUrl + "/otherMission/otherMissionAction!getProjectListsRelated.action?dto.related=charge",null,function(dat){
							var opti = '<option value="">-请选择项目-</option>';
							if(dat.rows.length > 0){
								for(var i=0;i<dat.rows.length;i++){
									opti = opti + '<option value="'+dat.rows[i].projectId+'">'+dat.rows[i].projectName+'</option>';
								}
							}
							$("#relationPro").next("div.searchable-select").remove();
							$("#relationPro").html(opti);
							if(data.iterationLists[0].associationProject!=""){
								$("#relationPro").val(data.iterationLists[0].taskId);
							}else{
								$("#relationPro").val("");
							}
							$('#relationPro').searchableSelect();
							$("#relationPro").next("div").css("min-width", "330px");
						},'json');
					}
					//回填数据,并选中
					objs.$newCreatIteration.xwindow('setTitle','修改迭代').xwindow('open');
					PutIterationData(data);
				} else {
					tip("系统错误！");
				}
			}, "json");
}

//回填数据,并选中
function PutIterationData(datas){
	var iterBagName = datas.iterationLists[0].iterationBagName;
	var assocPro = datas.iterationLists[0].associationProject;
	var note = datas.iterationLists[0].note;
	var taskId = datas.iterationLists[0].taskId;
	var sTime = datas.iterationLists[0].startTime;
	var eTime = datas.iterationLists[0].endTime;

	if(iterBagName!=""){
		$("#iteraName").xtextbox("setValue",iterBagName);
	}else{
		$('input[name="dto.iterationList.iterationBagName"]').prev().val('暂无');
	}
	
	if(sTime!=""){
		$(".startTime").xdatebox("setValue",sTime);
	}else{
		$(".startTime").xdatebox("setValue","");
	}
	
	if(eTime!=""){
		$(".endTime").xdatebox("setValue",eTime);
	}else{
		$(".endTime").xdatebox("setValue","");
	}
	/*原来*/
	/*if(assocPro!=""){
		$('input[name="dto.iterationList.associationProject"]').prev().find($('input[id^="_exui_textbox_input"]')).val(assocPro);
		$("#relationPro").val(taskId);
		//$('input[name="dto.iterationList.associationProject"]').prev().find($('input[id^="_exui_textbox_input"]')).val(rec.proName);
		//$('input[name="dto.iterationList.associationProject"]').prev().xcombobox("setValue",datas.iterationLists[0].taskId);
		$('input[name="dto.iterationList.associationProject"]').prev().find($('input[id^="_exui_textbox_input"]')).after("<input type=\"hidden\" class=\"textbox-value\" name=\"\" value=\'"+taskId+"\'>");
		$("input[type=\"checkbox\"][name=\"taskId\"][value='"+taskId+"']").find('tr[id^="datagrid-row-r"]').addClass('datagrid-row datagrid-row-checked datagrid-row-selected');
	}else{
		$("#relationPro").val("");
		$('input[name="dto.iterationList.associationProject"]').prev().find($('input[id^="_exui_textbox_input"]')).val('暂无');
		//$('input[name="dto.iterationList.associationProject"]').prev().xcombobox("setValue","");
	}*/
	
	if(note!=""){
		//$('input[name="dto.iterationList.note"]').prev().val(note);
		$("#iterationNote").xtextbox("setValue",note);
	}else{
		$("#iterationNote").xtextbox("setValue","");
		//$('input[name="dto.iterationList.note"]').prev().val('暂无');
	}
	
}

//搜索查询
function searchIteration(){
	var iterationBagName = $("#iterationBagName").val();
	var createPerson=$("#createPerson").val();
	var proAllnameSel = $("#proAllName option:selected").text();
	if(proAllnameSel=="-请选择项目-"){
		proAllnameSel="";
	}
	objs.$iterationList.xdatagrid({
		url: baseUrl + '/iteration/iterationAction!iterationDataListLoad.action',
		method: 'post',
		height: mainObjs.tableHeight-100, 
		queryParams:{
			'dto.iterationList.iterationBagName':iterationBagName,
			'dto.iterationList.createPerson':createPerson,
			'dto.iterationList.status':"0",
			'dto.iterationList.associationProject':proAllnameSel
		},
		striped:true,
		fitColumns: true,
		rownumbers: true,
		singleSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		emptyMsg:"暂无数据",
		layout:['list','first','prev','manual','next','last','refresh','info'],
		columns:[[
	        {field:'checkId',title:'选择',checkbox:true,align:'center'},
		    {field:'iterationId',title:'迭代id',hidden:true},
		    {field:'iterationBagName',title:'迭代',width:'15%',align:'left'},
			{field:'associationProject',title:'关联项目名称',width:'25%',align:'center',halign:'center',formatter:proReals},
//			{field:'createPerson',title:'创建人',width:'15%',align:'center',halign:'center',formatter:createPersons},
//			{field:'createTime',title:'创建时间',width:'15%',align:'center',halign:'center',formatter:createTimes},
			{field:'startTime',title:'开始时间',width:'15%',align:'center',halign:'center',formatter:startTime},
			{field:'endTime',title:'结束时间',width:'15%',align:'center',halign:'center',formatter:endTime},	
			{field:'note',title:'备注',width:'20%',align:'center',formatter:notess},
			{field:'operate',title:'操作',width:'11%',align:'center',formatter:operateFormat1}
		]],
		onLoadSuccess : function (data) {								
			if (data.total==0) {
				//$('tr[id^="datagrid-row-r"]').parent().append('<label style="height: 40px;width: 900px;margin-top: 8px;text-align: center;">没有数据!</label>');
			}
//			$("#testTaskDg").xdatagrid('resize');
			$('#iterationAccordion').find('div[class="panel-body accordion-body"]:eq(0)').height(470);
			$('#iterationAccordion').xaccordion("resize");
			objs.$iterationList.xdatagrid("resize");
			for(var i=0;i<$("#iterationAccordion tbody").find("tr").length;i++){
				$($("#iterationAccordion tbody").find("tr")[i]).attr("title","双击查看迭代明细");
				$($("#iterationAccordion tbody").find("tr")[i]).css("cursor","pointer");
			}
		}
	});
}

function proReals(value,row,index){
	if(value==null || value==""){
		return "<span style=\"cursor: pointer;\" title=\"双击查看迭代明细\" href=\"javascript:;\">暂无</span>";
	}else{
		return "<span style=\"cursor: pointer;\" title=\"双击查看迭代明细\" href=\"javascript:;\">" + value + "</span>";
	}
}

function notess(value,row,index){
	if(value==null || value==""){
		return "暂无";
	}else{
		return row.note;
	}
}

function createPersons(value,row,index){
	if(value==null || value==""){
		return "暂无";
	}else{
		return row.createPerson;
	}
}

function createTimes(value,row,index){
	if(value==null || value==""){
		return "暂无";
	}else{
		return row.createTime;
	}
	
}

//删除迭代列表信息
function showdelIteraConfirm(){
	var row = objs.$iterationList.xdatagrid('getSelected');
	if (!row) {
//		$.xnotify('请选择要删除的一条记录', {type:'warning'});
//		tip("请选择要删除的一条记录!");
		$.xalert({title:'提示',msg:'请点击一条需要删除的记录!'});
		return;
	}
	
	$.xconfirm({
		msg:'您确定删除选择的记录吗?',
		okFn: function() {
			$.post(
				baseUrl + "/iteration/iterationAction!deleteIterationInfo.action",
				{'dto.iterationList.iterationId': row.iterationId},
				function(data) {
					if (data == "success") {
						loadIterationList();
					} else {
//						$.xnotify(data, {type:'warning'});
						tip(data);
					}
				}, "text");
		}
		
	});
}

//添加bug的弹出layout
function iterBugLayout(){
	var iterVal = $("#iterationId").val();
	var taskBId = $("#taskIdCl").val();
	if(iterVal!=""&&taskBId!=""){
		$("<div></div>").xdialog({
	    	id:'iterationBugLayout',
	    	title:"选择项目bug",
	    	width : 1300,
	        height : 600,
	    	modal:true,
	    	href:baseUrl + '/iteration/iterationAction!iterationBugLayout.action',
	    	queryParams: {
					"iterationListId": $("#iterationId").val(),
					"taskBugId": $("#taskIdCl").val(),
					"bugData":$("#bugIds").val()
				},
	        onClose : function() {
	        	objs.$proBug.xdatagrid('reload');
	            $(this).xdialog('destroy');
	        }
	    });
	}else{
		var tip = "请选择迭代记录！";
		notice(tip);
	}
	 
}

//提示
function notice(notice){
	$.xalert({title:'提示',msg:notice,okFn:function(){
		$('#iterationAccordion:first').find('div[class="panel-body accordion-body"]').css('display','block');
		$('#iterationAccordion:last').find('div[class="panel panel-default accordion-header panel-htop panel-last"]').children('div[class="panel-body accordion-body"]').css('display','none');
	}});
//	$.xconfirm({
//		msg:notice,
//		okFn: function() {
//			
//		},
//		cancleFn:function(){
//			
//		}
//	});
}

//选择测试包
function iterationTestCaseLayout(){
	var iterVal = $("#iterationId").val();
	var taskBId = $("#taskIdCl").val();
	if(iterVal!=""&&taskBId!=""){
	 $("<div></div>").xdialog({
	    	id:'iterationTestCaseLayout',
	    	title:"选择测试包",
	    	width : 1300,
	        height : 600,
	    	modal:true,
	    	href:baseUrl + '/iteration/iterationAction!iterationTestCaseLayout.action',
	    	queryParams: {
	    					"iterationListId": $("#iterationId").val(),
	    					"taskBugId": $("#taskIdCl").val(),
	    					"testCaseData": $("#packageIds").val()
	    					},
	        onClose : function() {
	        	objs.$proTestcase.xdatagrid('reload');
//	        	objs.$proTestcase.xdatagrid('load', {
//	        	    'dto.iterationList.iterationId': iterVal
//	        	});
	            $(this).xdialog('destroy');
	        }
	    });
	}else{
		var tip = "请选择迭代记录！";
		notice(tip);
	}
}

//选择任务
function iterationTaskLayout(){
	var iterVal = $("#iterationId").val();
	var taskBId = $("#taskIdCl").val();
	if(iterVal!=""){
	$("<div></div>").xdialog({
    	id:'iterationTaskLayout',
    	title:"选择任务",
    	width : 1300,
        height : 600,
    	modal:true,
    	href:baseUrl + '/iteration/iterationAction!iterationTaskLayout.action',
    	queryParams: {
    					"iterationListId": $("#iterationId").val(),
    					"taskBugId": $("#taskIdCl").val(),
    					"alreadyData":$("#mission_id").val()
    					},
        onClose : function() {
        	objs.$proTask.xdatagrid('reload');
            $(this).dialog('destroy');
        }
    });
  }else{
	  var tip = "请选择迭代记录！";
	  notice(tip);
  }
}


//# sourceURL=iteration.js