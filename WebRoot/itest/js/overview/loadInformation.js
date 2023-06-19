$(function() {
	$.parser.parse();
	//得到所有统计信息
	loadInformation();
	bugWorkInfo();
});
function loadInformation(){
	$.post(baseUrl+"/overview/overviewAction!getDetails.action",{
		'dto.taskId':$("#taksIdmain").val(),
		'dto.chargePersonId':$("#accountId").text(),
		'dto.createId':$("#loginNam").text(),
		'dto.joinId':$("#accountId").text()
	},function(data){
		$("#11").html(data.allCount);
		$("#22").html(data.passCount);
		$("#33").html(data.failedCount);
		$("#44").html(data.blockCount);
		$("#55").html(data.noTestCount);
		$("#66").html(data.invalidCount);
		/*$("#77").html(data.waitModifyCount);
		$("#88").html(data.waitAuditCount);*/
		$("#aa").html(data.bugAllCount);
		$("#bb").html(data.bugValidCout);
		$("#cc").html(data.bugResolCount);
		$("#dd").html(data.bugValidCout - data.bugResolCount);
		$("#ee").html(data.meCharge);
		$("#ff").html(data.meCreate);
		$("#gg").html(data.meJoin);
		$("#hh").html(data.meAll);
		$("#fixCount").html(data.fixNoConfirmCount);
		$("#noBugCount").html(data.noBugNoConfirmCount);
	},'json');
}
//测试人员工作面板
function bugWorkInfo(){
	$("#bugWorkList").xdatagrid({
		url: baseUrl + '/caseManager/caseManagerAction!loadCaseBoard.action',
		method: 'get',
		fitColumns:true,
		singleSelect:true,
		/*pagination: true,*/
		/*pageNumber: 1,
		pageSize: 10,*/
		height: mainObjs.tableHeight - 260,
		/*pageList: [10,30,50,100],
		layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],*/
		onLoadSuccess:function(data){
			$("#bugWorkList").xdatagrid('resize');
		},
		rowStyler: function(index,row){
			if (row.status=='0'){
//				return 'background-color:#bddecc;color:#fff;font-weight:bold;';
//				return 'background-color:#e5fff1;';
			}
		},
		//whCount 处理用例数 ，hCount 今日处理用例数
		columns:[[
			{field:'userName',title:'项目人员',width:'14%',align:'center'},
			{field:'teamActor',title:'项目分工',width:'14%',align:'center'},
			{field:'hcount',title:'今日处理用例数',width:'18%',align:'center'},
			{field:'whCount',title:'处理用例数',width:'18%',align:'center'},
			{field:'bhCount',title:'今日处理bug次数',width:'18%',align:'center'},
			{field:'bwhCount',title:'待处理bug数',width:'18%',align:'center'}
			
		]]
	});
}
//# sourceURL=loadInformation.js