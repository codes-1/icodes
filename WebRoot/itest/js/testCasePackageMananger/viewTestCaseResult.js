//获取页面url参数
function getQueryParam(name) {
       var obj = $('#viewTestCaseResultDlg').dialog('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}


$(function(){
	var packageId = getQueryParam('testCasePackageId');
	var urlStr = baseUrl + '/testCasePkgManager/testCasePackageAction!getBugStaticsByPkgId.action';
	$.post(
			urlStr,
			{'dto.testCasePackage.packageId':packageId},
			function(dataObj) {
		        if(dataObj){
		        	dataObj = JSON.parse(dataObj);
		        	//未测试
		        	var noTestCount = dataObj.allCount-dataObj.passCount-dataObj.failedCount-dataObj.blockCount-dataObj.waitAuditCount-dataObj.invalidCount
		        	                  -dataObj.waitModifyCount-dataObj.waitModifyCount;
		        	$("#allCount").html(dataObj.allCount);
		        	$("#passCount").html(dataObj.passCount);
		        	$("#failedCount").html(dataObj.failedCount);
		        	$("#blockCount").html(dataObj.blockCount);
		       /* 	$("#waitAuditCount").html(dataObj.waitAuditCount);*/
		        	$("#invalidCount").html(dataObj.invalidCount);
		        /*	$("#waitModifyCount").html(dataObj.waitModifyCount);
		        	$("#waitModifyCount").html(dataObj.waitModifyCount);*/
		        	$("#noTestCount").html(noTestCount);
		        }
			},"text"
		);
});

function closeTestcaseResultWin(){
	 $('#viewTestCaseResultDlg').dialog('destroy');
}
//@ sourceURL=viewTestCaseResult.js