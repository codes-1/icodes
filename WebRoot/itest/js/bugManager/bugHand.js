 var nextCd ="";
 var chkSelNextOwn="",appendVerStr="";
 var stateArr,memSel,ctrInfoArr,handName,selStr,ownIdField,popName,innerStr,initOwnName="",verFieldChk="";
 var testOwnName="",devOwnName="",anasName="",asinName="",inteOwnName="";
 var testOwnInit="",anasOwnInit="",asinOwnInit="",devOwnInit="'",inteOwnInit="",nextOwnerIdInit="",remarkInit="";
 var initStateName,verLableInit,verValInit,fixVerInit="",verifyVerInit="",dueDateInit="";
 var moduleInitId,bugDescInit,reProStep="";
 var chargeOwnerInit="",testOwnNameInit="",devOwnNameInit="",anasOwnNameInit="",asinOwnNameInit="",inteOwnNameInit="",chargeOwnerNameInit="";
 var nextFlwCdInit;
 var fromPage ="";
 var bId = "";
 var defList=new Array();
 $(function (){
	 $.parser.parse();
	 bId = getQueryParam('bId');
	 var tId = getQueryParam('tId');
	 fromPage = getQueryParam('fromPage');
	 bugHandF(bId,tId);
	 haveFiles();
	 $('#opHandHistory').next().hide();
	//点击基本信息隐藏历史列表
	 $('a[href="#bugHandInfo"]').click(function(){
	 	$('#opHandHistory').next().hide();
	 });
	 
	 $('a[href="#bugFiles"]').click(function(){
	 	$('#opHandHistory').next().hide();
	 });
	 
 });
 
//获取页面url参数
 function getQueryParam(name) {
        var obj = $('#handBugWindown').xwindow('options');
        var queryParams = obj["queryParams"]; 
        return queryParams[name];
 }
 
 //处理
function bugHandF(bugId,tId){
	$("#bugHandForm").xform('clear');
	$.get(
		baseUrl + "/bugManager/bugManagerAction!bugHand.action",
		{
			'dto.bug.bugId': bugId,
			'dto.loadType':1,
			'dto.taskId':tId
		},
		function(data,status,xhr){
			if(status=='success'){
				if(data!=null){
					
					$("#taskModule__").textbox("setValue", data.moduleName);
					if(data.bug.bugReptId==accountId || data.roleInTask.indexOf("1")>=0 || data.roleInTask.indexOf("2")>=0 || data.roleInTask.indexOf("8")>=0){
//						$("#taskModule__").textbox("readonly",false);
						//选择需求
						$("#taskModule__").next('span').children('input:first-child').click(function(){
							needsTree("moduleId__","taskModule__","reProTxt__","hand");
						})
					}else{
						$("#taskModule__").textbox("readonly",true);
						$("#taskModule__").next('span').children('input:first-child').unbind("click");
					}
					$("#stateName__").textbox("setValue", data.stateName);
					$("#authorName__").textbox("setValue", data.bug.author.uniqueName);
					
					if(data.bug.analyseOwnerId==accountId || data.roleInTask.indexOf("7")>=0 || data.roleInTask.indexOf("4")>=0 ){
						$("#AntimodDateTr").show();
					}else if(null!=data.bug.bugAntimodDate && data.bug.analyseOwnerId!=accountId && data.roleInTask.indexOf("7")<0 && data.roleInTask.indexOf("4")<0 ){
						$("#bugAntimodDate__").val(data.bug.bugAntimodDate);
						$("#AntimodDateTr").show();
					}else{
						$("#AntimodDateTr").hide();
					}
					if(data.bug.bugAntimodDate){
						dueDateInit = data.bug.bugAntimodDate.substring(0,10);
					}
					
					$("#bugDesc__").textbox("setValue", data.bug.bugDesc);
					bugDescInit = data.bug.bugDesc;
					if(data.bug.bugReptId==accountId || data.roleInTask.indexOf("1")>=0 || data.roleInTask.indexOf("2")>=0 || data.roleInTask.indexOf("8")>=0){
						$("#bugDesc__").textbox("readonly",false);
					}else if(data.bug.bugReptId!=accountId &&  data.roleInTask.indexOf("1")<0 && data.roleInTask.indexOf("2")<0 && data.roleInTask.indexOf("8")<0){
						$("#bugDesc__").textbox("readonly",true);
					}
					if(data.bug.chargeOwner){
						$("#chargeOwner").val(data.bug.chargeOwner);
						$("#chargeOwnerName").textbox("setValue", data.bug.chargeOwnerName);
					}else{
						$("#chargeOwnerName").textbox("setValue", "暂未指定");
					}
					chargeOwnerInit = data.bug.chargeOwner;
					if(data.bug.analyseOwnerId==accountId || data.roleInTask.indexOf("7")>=0 || data.roleInTask.indexOf("4")>=0 || data.bug.devOwnerId==accountId || data.roleInTask.indexOf("6")>=0){
						$("#chargeOwnerName").next('span').children('input:first-child').click(function(){
							openUserWin();
						})
					}else{
						$("#chargeOwnerName").next('span').children('input:first-child').unbind("click");
					}
					/*else {
						if(data.bug.chargeOwner){
							$("#chargeOwner").textbox("setValue", data.bug.chargeOwner);
							$("#chargeOwnerName").textbox("setValue", data.bug.chargeOwnerName);
						}else{
							$("#chargeOwnerName").textbox("setValue", "暂未指定");
						}
					}*/
					var comboxLists = new Array();
					var initCombox = new Array();
					if(data.bug.testLdIdSelStr && data.bug.testLdIdSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.testLdIdSelStr.split("$");
						var testFlag=0;
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
							comboxLists.push(content);
							if(val[0]==data.bug.testOwnerId){
								testFlag=1;
							}
						}
						$("#testOwnerId__").xcombobox("loadData",comboxList);
						$("#testOwnerId__").xcombobox({
							onSelect:function(param){
								$("#testOwnName__").textbox("setValue", param.text);
							}
						});
						$("#testOwnerId__").xcombobox("readonly",false);
						
						if(testFlag==1){
							if(data.bug.testOwnerId!=null && data.bug.testOwnerId!=""){
								$("#testOwnerId__").xcombobox("setValue",data.bug.testOwnerId);
								$("#testOwnName__").textbox("setValue", data.bug.testOwner.uniqueName);
								$("#testOwnerId__").next('span').children('span:first-child').unbind("click");
							}
						}else{
							if(data.bug.testOwnerId!=null && data.bug.testOwnerId!=""&&data.bug.testOwner!=null){
								var comboxList = new Array();
								var content = {"id":data.bug.testOwnerId,"text":data.bug.testOwner.uniqueName};
								comboxList.push(content);
								initCombox.push(content);
								$("#testOwnerId__").xcombobox("loadData",comboxList);
								$("#testOwnerId__").xcombobox("setValue",data.bug.testOwnerId);
								$("#testOwnName__").textbox("setValue", data.bug.testOwner.uniqueName);
							}
							
							$("#testOwnerId__").next('span').children('span:first-child').click(function(){
								var checkVal  = $("#testOwnerId__").xcombobox("getValue");
								if(checkVal == data.bug.testOwnerId){
									$("#testOwnerId__").xcombobox("clear");
								}
								$("#testOwnerId__").xcombobox("loadData",comboxLists);
							});
							$("#testOwnerId__").next('span').children('input').eq(0).blur(function(){
								if($("#testOwnerId__").xcombobox("getValue")==""){
									$("#testOwnerId__").xcombobox("loadData",initCombox);
									$("#testOwnerId__").xcombobox("setValue",data.bug.testOwnerId);
									$("#testOwnName__").textbox("setValue", data.bug.testOwner.uniqueName);
								}
							});
							if(data.isDetailFlag=="1"){
								$("#testOwnerId__").xcombobox("readonly",true);
							}
						}
					}else{
						if(data.bug.testOwnerId!=null && data.bug.testOwnerId!=""&&data.bug.testOwner!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.testOwnerId,"text":data.bug.testOwner.uniqueName};
							comboxList.push(content);
							$("#testOwnerId__").xcombobox("loadData",comboxList);
							$("#testOwnerId__").xcombobox("readonly",true);
							$("#testOwnerId__").xcombobox("setValue",data.bug.testOwnerId);
							$("#testOwnName__").textbox("setValue", data.bug.testOwner.uniqueName);
						}
					}
					if(data.bug.devStr && data.bug.devStr!=""){
						var comboxList = new Array();
						var dd = data.bug.devStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#devOwnerId__").xcombobox("loadData",comboxList);
						$("#devOwnerId__").xcombobox({
							onSelect:function(param){
								$("#devOwnerName__").textbox("setValue", param.text);
							}
						});
						$("#devOwnerId__").xcombobox("readonly",false);
						if(data.bug.devOwnerId!=null && data.bug.devOwnerId!=""){
							$("#devOwnerId__").xcombobox("setValue",data.bug.devOwnerId);
							$("#devOwnerName__").textbox("setValue", data.bug.devOwner.uniqueName);
						}else{
							var comboxList = new Array();
							var content = {"id":"","text":"暂未指定"};
							comboxList.push(content);
							$("#devOwnerId__").xcombobox("loadData",comboxList);
							$("#devOwnerId__").xcombobox("readonly",true);
							$("#devOwnerId__").xcombobox("setValue","");
						}
					}else{
						if(data.bug.devOwnerId!=null && data.bug.devOwnerId!=""&&data.bug.devOwner!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.devOwnerId,"text":data.bug.devOwner.uniqueName};
							comboxList.push(content);
							$("#devOwnerId__").xcombobox("loadData",comboxList);
							$("#devOwnerId__").xcombobox("readonly",true);
							$("#devOwnerId__").xcombobox("setValue",data.bug.devOwnerId);
							$("#devOwnerName__").textbox("setValue", data.bug.devOwner.uniqueName);
						}else{
							var comboxList = new Array();
							var content = {"id":"","text":"暂未指定"};
							comboxList.push(content);
							$("#devOwnerId__").xcombobox("loadData",comboxList);
							$("#devOwnerId__").xcombobox("readonly",true);
							$("#devOwnerId__").xcombobox("setValue","");
						}
					}
					if(data.bug.verSelStr!=null && data.bug.verSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.verSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#currVersion__").xcombobox("loadData",comboxList);
						$("#currVersion__").xcombobox({
							onSelect:function(param){
								$("#currVersionName__").textbox("setValue", param.text);
							}
						});
					}else{
						if(data.bug.bugReptVer!=null && data.bug.bugReptVer!=""&&data.bug.reptVersion!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.reptVersion.versionId,"text":data.bug.reptVersion.versionNum};
							comboxList.push(content);
							$("#bugReptVer__").xcombobox("loadData",comboxList);
							$("#bugReptVer__").xcombobox("setValue",data.bug.bugReptVer);
							$("#bugReptVerName__").textbox("setValue", data.bug.reptVersion.versionNum);
						}
					}
					
					if(data.bug.typeSelStr && data.bug.typeSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.typeSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#bugTypeId__").xcombobox("loadData",comboxList);
						$("#bugTypeId__").xcombobox({
							onSelect:function(param){
								$("#bugTypeName__").textbox("setValue", param.text);
							}
						});
						$("#bugTypeId__").xcombobox("readonly",false);
						if(data.bug.bugTypeId!=null && data.bug.bugTypeId!=""){
							$("#bugTypeId__").xcombobox("setValue",data.bug.bugTypeId);
							$("#bugTypeName__").textbox("setValue", data.bug.bugType.typeName);
						}
					}else{
						if(data.bug.bugTypeId!=null && data.bug.bugTypeId!=""&&data.bug.bugType!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.bugType.typeId,"text":data.bug.bugType.typeName};
							comboxList.push(content);
							$("#bugTypeId__").xcombobox("loadData",comboxList);
							$("#bugTypeId__").xcombobox("readonly",true);
							$("#bugTypeId__").xcombobox("setValue",data.bug.bugTypeId);
							$("#bugTypeName__").textbox("setValue", data.bug.bugType.typeName);
						}
					}
					if(data.bug.gradeSelStr && data.bug.gradeSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.gradeSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#bugGradeId__").xcombobox("loadData",comboxList);
						$("#bugGradeId__").xcombobox({
							onSelect:function(param){
								$("#bugGradeName__").textbox("setValue", param.text);
							}
						});
						$("#bugGradeId__").xcombobox("readonly",false);
						if(data.bug.bugGradeId!=null && data.bug.bugGradeId!=""){
							$("#bugGradeId__").xcombobox("setValue",data.bug.bugGradeId);
							$("#bugGradeName__").textbox("setValue", data.bug.bugGrade.typeName);
						}
					}else{
						if(data.bug.bugGradeId!=null && data.bug.bugGradeId!=""&&data.bug.bugGrade!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.bugGrade.typeId,"text":data.bug.bugGrade.typeName};
							comboxList.push(content);
							$("#bugGradeId__").xcombobox("loadData",comboxList);
							$("#bugGradeId__").xcombobox("readonly",true);
							$("#bugGradeId__").xcombobox("setValue",data.bug.bugGradeId);
							$("#bugGradeName__").textbox("setValue", data.bug.bugGrade.typeName);
						}
					}
					if(data.bug.plantFormSelStr && data.bug.plantFormSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.plantFormSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#platformId__").xcombobox("loadData",comboxList);
						$("#platformId__").xcombobox({
							onSelect:function(param){
								$("#pltformName__").textbox("setValue", param.text);
							}
						});
						$("#platformId__").xcombobox("readonly",false);
						if(data.bug.platformId!=null && data.bug.platformId!=""){
							$("#platformId__").xcombobox("setValue",data.bug.platformId);
							$("#pltformName__").textbox("setValue", data.bug.occurPlant.typeName);
						}
					}else{
						if(data.bug.platformId!=null && data.bug.platformId!=""&&data.bug.occurPlant!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.occurPlant.typeId,"text":data.bug.occurPlant.typeName};
							comboxList.push(content);
							$("#platformId__").xcombobox("loadData",comboxList);
							$("#platformId__").xcombobox("readonly",true);
							$("#platformId__").xcombobox("setValue",data.bug.platformId);
							$("#pltformName__").textbox("setValue", data.bug.occurPlant.typeName);
						}
					}
					if(data.bug.sourceSelStr && data.bug.sourceSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.sourceSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#sourceId__").xcombobox("loadData",comboxList);
						$("#sourceId__").xcombobox({
							onSelect:function(param){
								$("#sourceName__").textbox("setValue", param.text);
							}
						});
						$("#sourceId__").xcombobox("readonly",false);
						if(data.bug.sourceId!=null && data.bug.sourceId!=""){
							$("#sourceId__").xcombobox("setValue",data.bug.sourceId);
							$("#sourceName__").textbox("setValue", data.bug.bugSource.typeName);
						}
					}else{
						if(data.bug.sourceId!=null && data.bug.sourceId!=""&&data.bug.bugSource!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.bugSource.typeId,"text":data.bug.bugSource.typeName};
							comboxList.push(content);
							$("#sourceId__").xcombobox("loadData",comboxList);
							$("#sourceId__").xcombobox("readonly",true);
							$("#sourceId__").xcombobox("setValue",data.bug.sourceId);
							$("#sourceName__").textbox("setValue", data.bug.bugSource.typeName);
						}
					}
					if(data.bug.occaSelStr && data.bug.occaSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.occaSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#bugOccaId__").xcombobox("loadData",comboxList);
						if(data.bug.bugOpotunit!=null && data.bug.bugOpotunity.typeName=="首次出现"){
							$("#geneCauseTd__").show();
							$("#geneCauseIdTd__").show();
						}else{
							$("#geneCauseTd__").hide();
							$("#geneCauseIdTd__").hide();
						}
						$("#bugOccaId__").xcombobox({
							onSelect:function(param){
								if(param.text=="首次出现"){
									$("#geneCauseTd__").show();
									$("#geneCauseIdTd__").show();
								}else{
									$("#geneCauseTd__").hide();
									$("#geneCauseIdTd__").hide();
								}
								$("#occaName__").textbox("setValue", param.text);
							}
						});
						$("#bugOccaId__").xcombobox("readonly",false);
						if(data.bug.bugOccaId!=null && data.bug.bugOccaId!=""){
							$("#bugOccaId__").xcombobox("setValue",data.bug.bugOccaId);
							$("#occaName__").textbox("setValue", data.bug.bugOpotunity.typeName);
						}
					}else{
						if(data.bug.bugOccaId!=null && data.bug.bugOccaId!=""&&data.bug.bugOpotunity!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.bugOpotunity.typeId,"text":data.bug.bugOpotunity.typeName};
							comboxList.push(content);
							$("#bugOccaId__").xcombobox("loadData",comboxList);
							$("#bugOccaId__").xcombobox("readonly",true);
							$("#bugOccaId__").xcombobox("setValue",data.bug.bugOccaId);
							$("#occaName__").textbox("setValue", data.bug.bugOpotunity.typeName);
						}
					}
					if(data.bug.geCaseSelStr && data.bug.geCaseSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.geCaseSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#geneCauseId__").xcombobox("loadData",comboxList);
						$("#geneCauseId__").xcombobox({
							onSelect:function(param){
								$("#geneCaseName__").textbox("setValue", param.text);
							}
						});
						$("#geneCauseId__").xcombobox("readonly",false);
						if(data.bug.geneCauseId!=null && data.bug.geneCauseId!=""){
							$("#geneCauseId__").xcombobox("setValue",data.bug.geneCauseId);
							$("#geneCaseName__").textbox("setValue", data.bug.geneCause.typeName);
						}
					}else{
						if(data.bug.geneCauseId!=null && data.bug.geneCauseId!=""&&data.bug.geneCause!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.geneCause.typeId,"text":data.bug.geneCause.typeName};
							comboxList.push(content);
							$("#geneCauseId__").xcombobox("loadData",comboxList);
							$("#geneCauseId__").xcombobox("readonly",true);
							$("#geneCauseId__").xcombobox("setValue",data.bug.geneCauseId);
							$("#geneCaseName__").textbox("setValue", data.bug.geneCause.typeName);
						}
					}
					if(data.bug.priSelStr && data.bug.priSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.priSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#priId__").xcombobox("loadData",comboxList);
						$("#priId__").xcombobox({
							onSelect:function(param){
								$("#priName__").textbox("setValue", param.text);
							}
						});
						$("#priId__").xcombobox("readonly",false);
						if(data.bug.priId!=null && data.bug.priId!=""){
							$("#priId__").xcombobox("setValue",data.bug.priId);
							$("#priName__").textbox("setValue", data.bug.bugPri.typeName);
						}
					}else{
						if(data.bug.priId!=null && data.bug.priId!=""&&data.bug.bugPri!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.bugPri.typeId,"text":data.bug.bugPri.typeName};
							comboxList.push(content);
							$("#priId__").xcombobox("loadData",comboxList);
							$("#priId__").xcombobox("readonly",true);
							$("#priId__").xcombobox("setValue",data.bug.priId);
							$("#priName__").textbox("setValue", data.bug.bugPri.typeName);
						}
					}
					if(data.bug.freqSelStr && data.bug.freqSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.freqSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#bugFreqId__").xcombobox("loadData",comboxList);
						if(data.bug.bugFreq!=null && data.bug.bugFreq.typeNam=="偶尔出现"){
							$("#reproTd__").show();
							$("#reproPersentTd__").show();
						}else{
							$("#reproTd__").hide();
							$("#reproPersentTd__").hide();
						}
						$("#bugFreqId__").xcombobox({
							onSelect:function(param){
								if(param.text=="偶尔出现"){
									$("#reproTd__").show();
									$("#reproPersentTd__").show();
								}else{
									$("#reproTd__").hide();
									$("#reproPersentTd__").hide();
								}
								$("#bugFreqName__").textbox("setValue", param.text);
							}
						});
						$("#bugFreqId__").xcombobox("readonly",false);
						if(data.bug.bugFreqId!=null && data.bug.bugFreqId!=""){
							$("#bugFreqId__").xcombobox("setValue",data.bug.bugFreqId);
							$("#bugFreqName__").textbox("setValue", data.bug.bugFreq.typeName);
						}
					}else{
						if(data.bug.bugFreqId!=null && data.bug.bugFreqId!=""&&data.bug.bugFreq!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.bugFreq.typeId,"text":data.bug.bugFreq.typeName};
							comboxList.push(content);
							$("#bugFreqId__").xcombobox("loadData",comboxList);
							$("#bugFreqId__").xcombobox("readonly",true);
							$("#bugFreqId__").xcombobox("setValue",data.bug.bugFreqId);
							$("#bugFreqName__").textbox("setValue", data.bug.bugFreq.typeName);
						}
					}
					$("#reproPersent__").textbox("setValue", data.bug.reproPersent);
					if(data.bug.bugReptId==accountId || data.roleInTask.indexOf("1")>=0 || data.roleInTask.indexOf("2")>=0 || data.roleInTask.indexOf("8")>=0){
						$("#reproPersent__").textbox("readonly",false);
					}else if(data.bug.bugReptId!=accountId &&  data.roleInTask.indexOf("1")<0 && data.roleInTask.indexOf("2")<0 && data.roleInTask.indexOf("8")<0){
						$("#reproPersent__").textbox("readonly",true);
					}
					if(data.bug.genePhaseSelStr && data.bug.genePhaseSelStr!=""){
						var comboxList = new Array();
						var dd = data.bug.genePhaseSelStr.split("$");
						for ( var kk in dd) {
							var val = dd[kk].split(";");
							var content = {"id":val[0],"text":val[1]};
							comboxList.push(content);
						}
						$("#genePhaseId__").xcombobox("loadData",comboxList);
						$("#genePhaseId__").xcombobox({
							onSelect:function(param){
								$("#genPhName__").textbox("setValue", param.text);
							}
						});
						$("#genePhaseId__").xcombobox("readonly",false);
						if(data.bug.genePhaseId!=null && data.bug.genePhaseId!=""){
							$("#genePhaseId__").xcombobox("setValue",data.bug.genePhaseId);
							$("#genPhName__").textbox("setValue", data.bug.importPhase.typeName);
							$("#genePhaseIdTr__").show();
						}else{
							$("#genePhaseIdTr__").hide();
						}
					}else{
						if(data.bug.genePhaseId!=null && data.bug.genePhaseId!=""&&data.bug.importPhase!=null){
							var comboxList = new Array();
							var content = {"id":data.bug.importPhase.typeId,"text":data.bug.importPhase.typeName};
							comboxList.push(content);
							$("#genePhaseId__").xcombobox("loadData",comboxList);
							$("#genePhaseId__").xcombobox("readonly",true);
							$("#genePhaseId__").xcombobox("setValue",data.bug.genePhaseId);
							$("#genPhName__").textbox("setValue", data.bug.importPhase.typeName);
							$("#genePhaseIdTr__").show();
						}else{
							$("#genePhaseIdTr__").hide();
						}
					}
					if(data.bug.devOwnerId!=accountId){
						if(data.bug.genePhaseId){
							$("#genePhaseId__").xcombobox("readonly",false);
							$("#genePhaseIdTr__").show();
						}else{
							$("#genePhaseId__").xcombobox("readonly",true);
							$("#genePhaseIdTr__").hide();
						}
					}else{
						$("#genePhaseId__").xcombobox("readonly",false);
						$("#genePhaseIdTr__").show();
					}
					
					if(data.bug.devOwnerId==accountId && data.bug.testOwnerId!=accountId){
						$("#bugDesc__").xcombobox("readonly",true);
						$("#bugTypeId__").xcombobox("readonly",true);
						$("#bugGradeId__").xcombobox("readonly",true);
						$("#platformId__").xcombobox("readonly",true);
						$("#sourceId__").xcombobox("readonly",true);
						$("#bugOccaId__").xcombobox("readonly",true);
						$("#priId__").xcombobox("readonly",true);
						$("#bugFreqId__").xcombobox("readonly",true);
						$("#reproPersent__").xtextbox("readonly",true);
						$("#reProTxt__").xtextbox("readonly",true);
					}
					
					if(data.stateList.length>0){
						var comboxList = new Array();
						comboxList.push({"id":"","text":" "});
						var dd = data.stateList;
						for ( var kk in dd) {
							content = {"id":dd[kk].keyObj,"text":dd[kk].valueObj};
							comboxList.push(content);
						}
						$("#stateIdList").xcombobox("loadData",comboxList);
						$("#stateIdList").xcombobox({
							/*onSelect: function(param){
								var val = param.id
								if(val==""){
									$("#nextFow").hide();
									$("#currVerTd").hide();
									$("#currVerValueTd").hide();
									$("#verLableTd").show();
									$("#verLableValueTd").show();
									$("withRepteId").val("");
								}else{
									$("#nextFow").show();
									$("#currVerTd").show();
									$("#currVerValueTd").show();
									$("#verLableTd").hide();
									$("#verLableValueTd").hide();
								}
							},*/
//							onSelect: changeStatus
							onChange: changeStatus
						});
						$("#changeStatus").show();
						$("#relaTd").show();
						$("#repetBugTd").show();
						$("#hasstatus").show();
						$("#nostatus").hide();
					}else{
						$("#changeStatus").hide();
						$("#changeStatus").show();
						$("#relaTd").hide();
						$("#repetBugTd").show();
						$("#hasstatus").show();
						$("#nostatus").show();
					}
					
					if(data.bug.bugReptVer!=null && data.bug.bugReptVer!=""&&data.bug.reptVersion!=null){
						var comboxList = new Array();
						var content = {"id":data.bug.reptVersion.versionId,"text":data.bug.reptVersion.versionNum};
						comboxList.push(content);
						$("#bugReptVer__").xcombobox("loadData",comboxList);
						$("#bugReptVer__").xcombobox("readonly",true);
						$("#bugReptVer__").xcombobox("setValue",data.bug.bugReptVer);
						$("#bugReptVerName__").textbox("setValue", data.bug.reptVersion.versionNum);
					}
					
					if(data.stateName=="重复"){
						$("#repetBugTd").show();
						$("#repeDetBtn").show();
						$("#relaTd").hide();
						if(!isNullOrSpace(data.bug.withRepteId)){
							$("#withRepteId").val(data.bug.withRepteId);
						}
					}else{
						$("#repetBugTd").hide();
						$("#repeDetBtn").hide();
						$("#withRepteId").val("");
					}
					
					if(!isNullOrSpace(data.bug.moduleId)){
						$("#moduleId__").val(data.bug.moduleId);
						moduleInitId = data.bug.moduleId;
					}
					if(!isNullOrSpace(data.moduleName)){
						$("#module_Name__").val(data.moduleName);
					}
					if(data.bug.modelName){
						$("#flwName").val(data.bug.modelName);
					}
					if(!isNullOrSpace(data.bug.currFlowCd)){
						$("#currFlowCd__").val(data.bug.currFlowCd);
					}
					if(!isNullOrSpace(data.bug.nextFlowCd)){
						$("#nextFlowCd__").val(data.bug.nextFlowCd);
					}
					if(!isNullOrSpace(data.bug.currStateId)){
						$("#currStateId__").val(data.bug.currStateId);
					}
					if(!isNullOrSpace(data.bug.testPhase)){
						$("#testPhase__").val(data.bug.testPhase);
					}
					if(!isNullOrSpace(data.bug.reProTxt)){
						$("#reProTxt__").textbox("setValue", data.bug.reProTxt);
						reProStep = data.bug.reProTxt;
					}
					if(!isNullOrSpace(data.bug.bugId)){
						$("#bugId__").val(data.bug.bugId);
					}
					if(!isNullOrSpace(data.bug.bugReptId)){
						$("#bugReptId__").val(data.bug.bugReptId);
					}
					if(!isNullOrSpace(data.bug.currHandlerId)){
						$("#currHandlerId__").val(data.bug.currHandlerId);
					}
					if(!isNullOrSpace(data.bug.currHandlDate)){
						$("#currHandlDate__").val(data.bug.currHandlDate);
					}
					$("#msgFlag__").val(data.bug.msgFlag);
					$("#relaCaseFlag__").val(data.bug.relaCaseFlag);
					if(!isNullOrSpace(data.bug.planAmendHour)){
						$("#planAmendHour__").numberbox("setValue",data.bug.planAmendHour);
					}
					if(!isNullOrSpace(data.bug.nextOwnerId)){
						$("#nextOwnerId__").val(data.bug.nextOwnerId);
						nextOwnerIdInit = data.bug.nextOwnerId;
					}
					if(!isNullOrSpace(data.bug.relaCaseFlag)){
						$("#relaCaseFlag__").val(data.bug.relaCaseFlag);
					}
					if(!isNullOrSpace(data.initReProStep)){
						$("#initReProStep__").val(data.initReProStep);
					}
					
					if(!isNullOrSpace(data.bug.testOwnerId)){
						$("#testOwnerId__").val(data.bug.testOwnerId);
						testOwnInit=data.bug.testOwnerId;
					}
					if(!isNullOrSpace(data.bug.analyseOwnerId)){
						$("#analyseOwnerId__").val(data.bug.analyseOwnerId);
						anasOwnInit=data.bug.analyseOwnerId;
					}
					if(!isNullOrSpace(data.bug.assinOwnerId)){
						$("#assinOwnerId__").val(data.bug.assinOwnerId);
						asinOwnInit=data.bug.assinOwnerId;
					}
					if(!isNullOrSpace(data.bug.devOwnerId)){
						$("#devOwnerId__").val(data.bug.devOwnerId);
						devOwnInit=data.bug.devOwnerId;
					}
					if(!isNullOrSpace(data.bug.currHandlerId)){
						$("#intercessOwnerId__").val(data.bug.intercessOwnerId);
						inteOwnInit=data.bug.intercessOwnerId;
					}
					if(!isNullOrSpace(data.bug.testPhase)){
						$("#testPhase__").val(data.bug.testPhase);
					}
					if(!isNullOrSpace(data.currOwner)){
						var dataStr =  data.currOwner.split("$");
						$("#handText").html(dataStr[0]+":");
						$("#currHanderName__").textbox("setValue",dataStr[1]);
					}
					if(!isNullOrSpace(data.bug.reptDate)){
//						$("#reptDate__").textbox("setValue", data.bug.reptDate.substring(0,10));
						$("#reptDate__").textbox("setValue", data.bug.reptDate);
					}
					if(!isNullOrSpace(data.bug.currRemark)){
						$("#currRemark__").textbox("setValue", data.bug.currRemark);
						remarkInit = data.bug.currRemark;
						$("#currRemarkTr__").show();
					}else{
						$("#currRemarkTr__").hide();
						
					}
					if(!isNullOrSpace(data.bug.fixVer)){
						$("#fixVer").val(data.bug.fixVer);
					}
					if(!isNullOrSpace(data.bug.verifyVer)){
						$("#verifyVer").val(data.bug.verifyVer);
					}
					
					if(isNullOrSpace(data.bug.currRemark) && !isNullOrSpace(data.bug.testOwnerId) && !isNullOrSpace(data.bug.devOwnerId)){
						$("currRemarkTr__").hide();
					}
					if(!isNullOrSpace(data.bug.testOwnerId) || !isNullOrSpace(data.bug.devOwnerId)){
						$("#currRemark__").textbox("readonly",false);
					}else{
						$("#currRemark__").textbox("readonly",true);
					}
					
					$("#pTestSelStr").val(data.bug.testLdIdSelStr);
					if(!isNullOrSpace(data.bug.staFlwMemStr)){
						$("#staFlwMemStr").val(data.bug.staFlwMemStr);
					}
					if(!isNullOrSpace(data.bug.versionLable)){
						$("#verLableTd").html(data.bug.versionLable+":");
						$("#versionLable").textbox("setValue", data.bug.currVersion.versionNum);
						$("#verLableTd").show();
						$("#verLableValueTd").show();
					}else{
						$("#verLableTd").hide();
						$("#verLableValueTd").hide();
					}
					
					if(data.bug.testOwner){
						testOwnNameInit = data.bug.testOwner.name;
					}
					if(data.bug.devOwner){
						devOwnNameInit = data.bug.devOwner.name;
					}
					if(data.bug.analysOwner){
						anasOwnNameInit = data.bug.analysOwner.name;
					}
					if(data.bug.assinOwner){
						asinOwnNameInit = data.bug.assinOwner.name;
					}
					if(data.bug.intecesOwner){
						inteOwnNameInit = data.bug.intecesOwner.name;
					}
					if(data.bug.chargeOwnerName){
						chargeOwnerNameInit = data.bug.chargeOwnerName;
					}
					
					if(data.bug.testOwner){
						testOwnName =data.bug.testOwner.uniqueName;
					}
					if(data.bug.devOwner){
						devOwnName =data.bug.devOwner.uniqueName;
					}
					if(data.bug.analysOwner){
						anasName =data.bug.analysOwner.uniqueName;
					}
					if(data.bug.assinOwner){
						asinName =data.bug.assinOwner.uniqueName;
					}
					if(data.bug.intecesOwner){
						inteOwnName =data.bug.intecesOwner.uniqueName;
					}
					if(data.stateName){
						initStateName = data.stateName;
					}
					if(data.bug.initState){
						$("#initState").val(data.bug.initState);
					}
					if(data.bug.versionLable){
						verLableInit  = data.bug.versionLable;
					}
					if(data.bug.currVersion){
						verValInit=data.bug.currVersion.versionNum;
					}
					if(data.bug.fixVer){
						fixVerInit =data.bug.fixVer;						
					}
					if(data.bug.verifyVer){
						verifyVerInit =data.bug.verifyVer;
						$("#verifyVer").val(verifyVerInit);
					}
					nextFlwCdInit =data.bug.nextFlowCd;
					if(data.bug.assignSelStr!=null){
						$("#assignSelStr")[0].value  = data.bug.assignSelStr;
					}
					if(data.bug.analySelStr !=null){
						$("#analySelStr")[0].value  = data.bug.analySelStr;
					}
					if(data.bug.testLdIdSelStr !=null){
						$("#testSelStr")[0].value  = data.bug.testLdIdSelStr;
					}
					if(data.bug.devStr !=null){
						$("#devStr")[0].value  = data.bug.devStr;
					}
					if(data.bug.devChkIdSelStr !=null){
						$("#devChkIdSelStr")[0].value  = data.bug.devChkIdSelStr;
					}
					if(data.bug.testChkIdSelStr !=null){
						$("#testChkIdSelStr")[0].value  = data.bug.testChkIdSelStr;
					}
					if(data.bug.testLdIdSelStr !=null){
						$("#testLdIdSelStr")[0].value  = data.bug.testLdIdSelStr;
					}
					if(data.bug.interCesSelStr !=null){
						$("#interCesSelStr")[0].value  = data.bug.interCesSelStr;
					}
					
					if(data.isDetailFlag=="1"){
						$("#devOwnerId__").xcombobox("readonly",true);
						$("#bugDesc__").textbox("readonly",true);
						$("#currRemark__").textbox("readonly",true);
						$("#reProTxt__").textbox("readonly",true);
						$("#chargeOwnerName").next('span').children('input:first-child').unbind("click");
						$("#changeStatus").hide();
						$("#nextOwnTr").hide();
						$("#verLableTd").hide();
						$("#verLableValueTd").hide();
						$("#currVerTd").hide();
						$("#currVerValueTd").hide();
						$("#handSubmitBnt").hide();
					}else{
						$("#handSubmitBnt").show();
					}
				}
//					$("#handBugWindown").xdeserialize(data);
			}else{
				$.xalert({title:'提示',msg:'系统错误！'});
			}
		},"json");
//	$("#handBugWindown").window({title:"处理软件问题报告"});
//	$("#handBugWindown").window("vcenter");
//	$("#handBugWindown").window("expand");
//	$("#handBugWindown").window("open");
	$("#bugOpHistoryList").xdatagrid('resize');
	
}


function changeStatus(param){
	var val = param;
	if(val==""){
		
		$("#nextOwnTd1").hide();
		$("#nextOwnTd2").hide();
		$("#nextOwnTd3").hide();	
		$("#nextOwnTr").hide();
		$("#relaTd").hide();
		$("#repeBtn").hide();
		$("#testOwnerId__")[0].value=testOwnInit;	
		$("#analyseOwnerId__")[0].value=anasOwnInit;
		$("#assinOwnerId__")[0].value=asinOwnInit;
		$("#devOwnerId__")[0].value=devOwnInit;
		$("#intercessOwnerId__")[0].value=inteOwnInit;
		$("#currVerTd").hide();
		$("#currVerValueTd").hide();
		$("#currVersion__").xcombobox({
	       required:false
	    }); 
		$("#verLableTd").show();
		$("#verLableValueTd").show();
		$("#withRepteId").val("");
		
		if(verLableInit!=undefined){
			$("#verLableTd").html(verLableInit+":");
			$("#versionLable").textbox("setValue", verValInit);
		}else{
			$("#verLableTd").hide();
			$("#verLableValueTd").hide();
			$("#currVerTd").hide();
			$("#currVerValueTd").hide();
			$("#currVersion__").xcombobox({
		       required:false
		    }); 
		}
		$("#fixVer")[0].value=fixVerInit;
		$("#verifyVer")[0].value=verifyVerInit;
		chkSelNextOwn="";
		verFieldChk="";
		appendVerStr="";
		$("#nextOwnerId__").value=nextOwnerIdInit;
		nextCd="";
		if(remarkInit==""&&testOwnInit==$("#testOwnerId__").val()&&devOwnInit==$("#devOwnerId__").val()){
			$("#currRemarkTr__").hide();
		}
		if(testOwnInit!=$("#testOwnerId__").xcombobox("getValue") || devOwnInit!=$("#devOwnerId__").xcombobox("getValue")){
			$("#currRemark__").textbox("readonly",false);
		}else{
			$("#currRemark__").textbox("readonly",true);
			$("#currRemark__").textbox("setValue",remarkInit);
		}
		return;
	}
	/*else{
		$("#currVerTd").show();
		$("#currVerValueTd").show();
		$("#verLableTd").hide();
		$("#verLableValueTd").hide();
	}*/
	
	if(val=="3"){//重复
		$("#relaTd").show();
		$("#repeBtn").show();
		
//		$("#verLableTd").hide();
//		$("#verLableValueTd").hide();
//		$("#currVerTd").hide();
//		$("#currVerValueTd").hide();
	}else{
		$("#relaTd").hide();
		$("#repeBtn").hide();
		$("#withRepteId").val("");
	}
	if($("#withRepteId").val()!=""){
		$("#repetBugTd").show();
		$("#repeDetBtn").show();
	}else{
		$("#repetBugTd").hide();
		$("#repeDetBtn").hide();
	}
	
	if($("#genePhaseIdTr__")[0].style.display=="" && $("#genePhaseId__").xcombobox("getValue")=="" && val=="13"){
//		$("getValue")[0].style.color="red";	
	}else{
//		$("getValue").style.color="#055A78";
	}
	var matchF= false;
	$("#currRemarkTr__").show();
	$("#currRemark__").textbox("readonly",false);
	var ctrlInfo = $("#staFlwMemStr").val().split("$");
	for(var i=0 ;i<ctrlInfo.length; i++){
		ctrInfoArr = ctrlInfo[i].split("=");
		stateArr=ctrInfoArr[0].split(",");
		for(var l=0 ;l<stateArr.length ;l++){
			if(stateArr[l]==val){
				matchF=true;
				break;
			}
		}
		if(!matchF){
			continue;
		}
		nextCd=ctrInfoArr[1].split("^")[0];
		memSel=ctrInfoArr[1].split("^")[1];
		handName="处理";
		if(memSel!="0"){
			$("#nextOwnTd1").show();
			$("#nextOwnTd2").show();
			$("#nextOwnTd3").show();
			$("#nextOwnTr").show();
			if(nextCd=="1"){
				ownIdField="testOwnerId";
				selStr="testSelStr";
				popName="测试人员";
				var testOwnName = $("#testOwnName__").textbox("getValue");
				if(testOwnName!=""){
					initOwnName = testOwnName;
					//下面这个处理是为了处理，BUG提交人不是测试人员时，要转线测试人员处理
					if($("#pTestSelStr").val().indexOf(testOwnName)<0 && (val=="13" || val=="26")){
						initOwnName = "";
					}
				}else{
					initOwnName = "";
				}
				handName="确认/处理";
			}else if(nextCd=="2"){
				ownIdField="testOwnerId";
				selStr="testChkIdSelStr";
				handName="互验";
				popName="测试互验人员"
				//要重新指定互验人员，所以不设置缺省的
			}else if(nextCd=="3"){
				ownIdField="analyseOwnerId";
				selStr="analySelStr";
				handName="分析";
				popName="分析人员";
				if(anasName!=""){
					initOwnName = anasName;
				}else{
					initOwnName = "";
				}
			}else if(nextCd=="4"){
				ownIdField="assinOwnerId";
				selStr="assignSelStr";
				handName="分配";
				popName="分配人员";
				if(asinName!=""){
					initOwnName = asinName;
				}else{
					initOwnName = "";
				}
			}else if(nextCd=="5"){
				ownIdField="devOwnerId";
				selStr="devStr";
				handName="修改";
				popName="修改人员";
				if(devOwnName!=""){
					initOwnName = devOwnName;
				}else{
					initOwnName = "";
				}
			}else if(nextCd=="6"){
				ownIdField="devOwnerId";
				selStr="devChkIdSelStr";
				handName="互检";
				popName="开发互检人员";
				if(devOwnName!=""){
					initOwnName = devOwnName;
				}else{
					initOwnName = "";
				}
			}else if(nextCd=="7"){
				ownIdField="intercessOwnerId";
				selStr="interCesSelStr";
				handName="审批/仲裁";
				popName="仲裁人员";
				if(inteOwnName!=""){
					initOwnName = inteOwnName;
				}else{
					initOwnName = "";
				}
			}else if(nextCd=="8"){
				ownIdField="testOwnerId";
				selStr="testSelStr";
				handName="确认";
				popName="测试人员";
				if(testOwnName!=""){
					initOwnName = testOwnName;
					//下面这个处理是为了处理，BUG提交人不是测试人员时，要转线测试人员处理
					if($("#pTestSelStr").val().indexOf(testOwnName)<0 && (val=="13"||val=="26")){
						initOwnName = "";
					}
				}else{
					initOwnName = "";
				}
			}
			if(initOwnName!="")
				$("#nextOwnerId__").val($("#"+ownIdField+"__").val());
			else{
				$("#nextOwnerId__").val("");
			}
			$("#nextOwnTd3")[0].innerHTML=handName;
			
			var turnStr = $("#"+selStr).val();
			if(turnStr!=""){
				var comboxList = new Array();
				var dd = turnStr.split("$");
				var deVal="";
				for ( var kk in dd) {
					var contentText = dd[kk].split(";");
					var content = {"id":contentText[0],"text":contentText[1]};
					comboxList.push(content);
					defList.push(content);
					if(contentText[1]==initOwnName){
						deVal = contentText[0];
					}
				}
				
				if(deVal==""){
					var nextInitId=$("#nextOwnerId__").val();
					var comList = new Array();
					var content = {"id":nextInitId,"text":initOwnName};
					comList.push(content);
					$("#nextOwnName").xcombobox("loadData",comList);
					$("#nextOwnName").xcombobox({
						onSelect:function(param){
							$("#nextOwnerId__").val(param.id);
						}
					});
					$("#nextOwnName").xcombobox("setValue",nextInitId);
					
					$("#nextOwnName").next('span').children('span:first-child').click(function(){
						var checkVal  = $("#nextOwnName").xcombobox("getValue");
						if(checkVal == nextInitId){
							$("#nextOwnName").xcombobox("clear");
						}
						$("#nextOwnName").xcombobox("loadData",comboxList);
						$("#nextOwnName").xcombobox({
							onSelect:function(param){
								$("#nextOwnerId__").val(param.id);
							}
						});
					});
					
					$("#nextOwnName").next('span').children('input').eq(0).blur(function(){
						if($("#nextOwnName").textbox("getValue")==""){
							$("#nextOwnName").xcombobox("loadData",comList);
							$("#nextOwnName").xcombobox({
								onSelect:function(param){
									$("#nextOwnerId__").val(param.id);
								}
							});
							$("#nextOwnName").xcombobox("setValue",nextInitId);
						}
					});
					
				}else{
					$("#nextOwnName").xcombobox("loadData",comboxList);
					$("#nextOwnName").xcombobox({
						onSelect:function(param){
							$("#nextOwnerId__").val(param.id);
						}
					});
					$("#nextOwnName").xcombobox("setValue",deVal);
				}
			}
			
			if(handName=="修改"){
				$("#assFromMdTd")[0].innerHTML="<a class=\"graybtn\" href=\"javascript:void(0);\" onclick=\"getMdPerson('1','moduleId__','nextOwnerId__','nextOwnName')\"><span>从测试需求指定人员中指派</span> </a>";
				getMdPersonDef('1','moduleId__','nextOwnerId__','nextOwnName');
			}else if(handName=="分配"){
				$("#assFromMdTd")[0].innerHTML="<a class=\"graybtn\" href=\"javascript:void(0);\" onclick=\"getMdPerson('3','moduleId__','nextOwnerId__','nextOwnName')\"><span>从测试需求指定人员中指派</span> </a>";
				getMdPersonDef('3','moduleId__','nextOwnerId__','nextOwnName');
			}else{
				$("#assFromMdTd")[0].innerHTML="";
			}
			chkSelNextOwn="chk";
		}else{
			$("#nextOwnTd1").hide();
			$("#nextOwnTd2").hide();
			$("#nextOwnTd3").hide();	
			$("#nextOwnTr").hide();
			$("#testOwnerId__")[0].value=testOwnInit;	
			$("#analyseOwnerId__")[0].value=anasOwnInit;
			$("#assinOwnerId__")[0].value=asinOwnInit;
			$("#devOwnerId__")[0].value=devOwnInit;
			$("#intercessOwnerId__")[0].value=inteOwnInit;
			chkSelNextOwn="";	
			$("#nextOwnerId__").val("");
		}
		break;
	}
	
	$("#currStateId__")[0].value=val;
	verCtrl(val);
}

//测试需求开发人员
function getMdPersonDef(loadType,moduleId,nextid,nextNameid){
	if($("#"+moduleId).val()==""){
		return;
	}
	var url = baseUrl + "/bugManager/bugManagerAction!loadMdPerson.action?dto.currNodeId="+$("#"+moduleId).val()+"&dto.loadType="+loadType;

	$.get(
		url,
		function(data,status,xhr){
			if(data!=null && data.total==1){
				if($("#"+nextNameid).textbox("getValue")==""){
					$("#"+nextNameid).xcombobox("loadData",defList);
				}
//				$("#"+nextid).val(data.rows[0].valueObj);
				$("#"+nextid).val(data.rows[0].keyObj);
				$("#"+nextNameid).combobox("setValue", data.rows[0].keyObj);
			}
		},
	"json");
}

//测试需求开发人员
function getMdPerson(loadType,moduleId,nextid,nextNameid){
	if($("#"+moduleId).val()==""){
		$.xalert({title:'提示',msg:'当前没指定对应的测试需求'});
		return;
	}
	var url = baseUrl + "/bugManager/bugManagerAction!loadMdPerson.action?dto.currNodeId="+$("#"+moduleId).val()+"&dto.loadType="+loadType;
	$("#testUserList").xdatagrid({
		url: url,
		method: 'get',
		singleSelect:true,
		rownumbers: true,
//		scrollbarSize:100,
		fitColumns:true,
		pagination: false,
		pageNumber: 1,
		pageSize: 100,
	    layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],
		pageList: [10,30,50,100],
		showPageList:true,
		columns:[[
			{field:'valueObj',title:'单击选择',width:'100%',align:'center',halign:'center'},
		]],
		onLoadSuccess:function(data){
			if(data.total==0){
				if(loadType=="1"){
					$.xalert({title:'提示',msg:'当前测试需求没指定开发人员'});
				}else{
					$.xalert({title:'提示',msg:'当前测试需求没指定分配人员'});
				}
			}/*else if(data.total==1){
				if($("#"+nextNameid).textbox("getValue")==""){
					$("#"+nextNameid).xcombobox("loadData",defList);
				}
				$("#"+nextid).val(data.rows[0].valueObj);
				$("#"+nextNameid).combobox("setValue", data.rows[0].keyObj);
			}*/else{
				if(loadType=="1"){
					$("#testUserWin").window({title:"修改人"});
				}else{
					$("#testUserWin").window({title:"分配人"});
				}
				$("#testUserWin").window("vcenter");
				$("#testUserWin").xwindow('open');
				$("#testUserList").xdatagrid('resize');
			}
		},
		onClickRow: function (index, row) {
			if($("#"+nextNameid).textbox("getValue")==""){
				$("#"+nextNameid).xcombobox("loadData",defList);
			}
//			$("#"+nextid).val(row.valueObj);
			$("#"+nextid).val(row.keyObj);
			$("#"+nextNameid).combobox("setValue", row.keyObj);
			$("#testUserWin").xwindow('close');
	  }
		
	});
}

//版本处理
function verCtrl(stateVal){
	var verLable = "";
	verFieldChk="";
	if((initStateName!="分析"&&initStateName!="分配"&&initStateName!="重分配"&&initStateName!="分歧")&&(stateVal=="6"||stateVal=="7"||stateVal=="8"||stateVal=="9"||stateVal=="10"||stateVal=="11")){
		verLable = "校验在版本";
		verFieldChk="verifyVer";
	}else if(stateVal=="13"||stateVal=="18"||stateVal=="26"){
		verLable = "修改在版本";
		verFieldChk="fixVer";
	}else if(stateVal=="14"||stateVal=="15"||stateVal=="22"||stateVal=="22"){
		verLable = "关闭在版本";
		verFieldChk="verifyVer";
	}else if(stateVal=="20"||stateVal=="21"){
		verLable = "延迟到版本";
		verFieldChk="fixVer";
	}
	if(verFieldChk==""){
		
		$("#fixVer")[0].value=fixVerInit;
		$("#verifyVer")[0].value=verifyVerInit;	
		if(verLableInit!=undefined){
			$("#verLableTd").html(verLableInit+":");
			$("#versionLable").textbox("setValue", verValInit);
			$("#verLableTd").show();
			$("#verLableValueTd").show();
			if(fixVerInit==""){
				$("#verLableTd").hide();
				$("#verLableValueTd").hide();
			}
			$("#currVerTd").hide();
			$("#currVerValueTd").hide();
			$("#currVersion__").xcombobox({
		       required:false
		    }); 
		}else{
			$("#verLableTd").hide();
			$("#verLableValueTd").hide();
			$("#currVerTd").hide();
			$("#currVerValueTd").hide();
			$("#currVersion__").xcombobox({
		       required:false
		    }); 
		}
		appendVerStr="";
		return;	
	}
	
	$("#verLableTd").hide();
	$("#verLableValueTd").hide();
	$("#currVerTd").show();
	$("#currVerValueTd").show();
	$("#currVersion__").xcombobox({
       required:true
    }); 
	$("#"+verFieldChk)[0].value="";
	$("#currVerTd")[0].innerHTML=verLable+":";
	appendVerStr=verLable;
}


//处理提交
function handSubmitForm() {
	var valid = $("#handBugWindown").xform('validate');
	if (!valid) {
		return;
	}
	var nextOwerId = $("#nextOwnName").xcombobox("getValue");
	if(nextOwerId!=null && nextOwerId!=undefined){
		if(ownIdField=="testOwnerId" || ownIdField=="devOwnerId"){
			$("#"+ownIdField+"__").xcombobox("setValue",nextOwerId);
		}else{
			$("#"+ownIdField+"__").val(nextOwerId);
		}
	}
	$("#reProStep__").val(reProStep);
	if(handChk()){
	var processLog = constructLog();
	if(processLog!=""){
		$("#processLog__").val(processLog);
		//直接在URL中写操作日志,得要求tomcat配置url用utf8 改为表单提交就没这问题了
	}
//	alert(processLog);
	
//	if(processLog==""&&!chgChk()){
//		hintMsg("您没有做任何改动");
//		return; 
//	}
	$('#editSubmitBnt').linkbutton('disable');
	if($("#devOwnerName__").textbox("getValue")=="暂未指定"){
		$("#devOwnerName__").textbox("setValue","");
	}
	if($("#chargeOwnerName").textbox("getValue")=="暂未指定"){
		$("#chargeOwnerName").textbox("setValue","");
	}
	var currState = $("#stateIdList").xcombobox("getValue");
	if($("#genePhaseIdTr__")[0].style.display=="" && $("#genePhaseId__").xcombobox("getValue")=="" && currState=="13"){
		$.xalert({title:'提示',msg:'请选择引入原因'});
		return;
	}	
	
	if(chkSelNextOwn!="" && nextOwerId==""){
		$.xalert({title:'提示',msg:'请选择要转交的下一处理人'});
		return;
	}
	var nextP =  $("#nextOwnerId__").val();
	if(nextOwerId !=nextP){
		console.log("传入后台数据不对！请联系管理员");
	}
	var url=baseUrl + "/bugManager/bugManagerAction!handSub.action"
	$.post(
		url,
		$("#bugHandForm").xserialize(),
		function(data) {
			if (data =="success") {
				$("#bugHandForm").xform('clear');
				$("#handBugWindown").xwindow('close');
				getBugList();
				$.xalert({title:'提示',msg:'处理成功！'});
				
			} else {
				 $.xalert({title:'提示',msg:'系统错误！'});
			}reProStep
			$('#editSubmitBnt').linkbutton('enable');
		}, "text");
	}
}
//校验
function handChk(){
	if(!remarkChk()){
		return false ;
	}
	if($("#currStateId__").val()=="3"&&$("#withRepteId").val()==""){
		$.xalert({title:'提示',msg:'请关联重复的BUG！'});
		return false;			
	}
//	var reportId = parent.pmGrid.cells(parent.pmGrid.getSelectedId(),20).getValue();
//	if(myId=!reportId&&roleStr.indexOf("1")<0&&roleStr.indexOf("2")<0&&roleStr.indexOf("8")<0){
//		oEditor.SetData(reProStep) ;
//	}
//	if(!handSubCheck()){
//		return false;
//	}
//	if(verFieldChk!=""){
//		if($("#"+verFieldChk)[0].value==""){
//			hintMsg("请选择" +$("verLableTd").innerHTML);
//			return false;
//		}
//	}
	if(nextCd!=""&& accountId==$("#currHandlerId__").val()){//这时为本人修改之前的状态
		if($("#currFlowCd__").val()!=nextCd){
			$("#nextFlowCd__").val(nextCd);
		}
		if($("#currFlowCd__").val()!=nextCd&&nextCd!=nextFlwCdInit){
			$("#currFlowCd__").val(nextFlwCdInit);
		}	
	}else if(nextCd!=""&&accountId!=$("#currHandlerId__").val()){//处理别人处理过的问题
		$("#currFlowCd__").val(nextFlwCdInit);
		$("#nextFlowCd__").val(nextCd);
	}
	return true;
}

function remarkChk(){
//	var chgState = $("stateIdList").value;
	var chgState = $("#stateIdList").xcombobox("getValue");
	if(chgState==""){
		$.xalert({title:'提示',msg:'请选择更改状态'});
		return;
	}
	if(chgState=="2"||chgState=="4"||chgState=="5"||chgState=="6"||chgState=="8"||chgState=="9"||chgState=="11"||chgState=="12"||chgState=="15"||chgState=="16"||chgState=="17"||chgState=="19"||chgState=="20"||chgState=="22"||chgState=="23"){
		if(remarkInit==$("#currRemark__").textbox("getValue").replace(/\s+$|^\s+/g,"")){
//			$("currRemark").value="";
			$("#currRemark__").textbox("setValue","");
		}				
		if(isNullOrSpace($("#currRemark__").textbox("getValue"))){
//			$("#currRemark__").textbox("required",true);
			$.xalert({title:'提示',msg:'请填写备注！'});
			return false;			
		}	
	}
	$("#currRemark__").textbox("setValue",$("#currRemark__").textbox("getValue").replace(/\s+$|^\s+/g,""));
	return true;
}

//检测是否更改
function chgChk(){
	var reProTxt =$("#reProTxt__").textbox("getValue");
	if(reProTxt!=reProStep){
		return true;
	}else if(moduleInitId!=$("#moduleId__").val()){
		return true;
	}else if(remarkInit!=$("#currRemark__").textbox("getValue")){
		return true;
	}else if(bugDescInit!=$("#bugDesc__").textbox("getValue")){
		return true;
	}else if(impPhaInit!=$("#genePhaseId__").value){
		return true;
	}else if(reproPerInit!=$("reproPersent").value){
		return true;
	}else if(priIdInit!=$("priId").value){
		return true;
	}else if(bugTypeInit!=$("bugTypeId").value){
		return true;
	}else if(geneCauseInit!=$("geneCauseId").value){
		return true;
	}else if(bugOccaInit!=$("bugOccaId").value){
		return true;
	}else if(sourceInit!=$("sourceId").value){
		return true;
	}else if(platformInit!=$("platformId").value){
		return true;
	}else if(bugGradeInit!=$("bugGradeId").value){
		return true;
	}else if(chargeOwnerInit!=$("chargeOwner").value){
		return true;
	}
	return false;
}

//拼接日志信息
function constructLog(){
	var currStateName = $("#stateIdList").xcombobox("getText");
//	var currStateName = $("stateIdList").options[$("stateIdList").selectedIndex].text;
	var proLog = "";
	if(currStateName!=""&&currStateName!=initStateName){
		proLog+=";把状态从:"+initStateName+"修改为:"+currStateName;
	}
	if(chargeOwnerInit==""&& $("#chargeOwner").val()!="" && $("#chargeOwnerName").textbox("getValue")!="暂未指定"){
		proLog+=";设置责任人为:"+$("#chargeOwnerName").textbox("getValue");
	}else if(chargeOwnerInit!=$("#chargeOwner").val() && $("#chargeOwnerName").textbox("getValue")!="暂未指定"){
		proLog+=";把责任人从:"+chargeOwnerNameInit +"改为:"+$("#chargeOwnerName").textbox("getValue");
	}else if(testOwnInit==""&&$("#testOwnerId__").combobox("getValue")){
		proLog+=";设置测试人员为:"+getUserNameById($("#testOwnerId__").combobox("getText"),"testSelStr");
	}else if(testOwnInit!=$("#testOwnerId__").combobox("getValue")){
		proLog+=";把测试人员从:"+testOwnNameInit+"改为:"+getUserNameById($("#testOwnerId__").combobox("getText"),"testSelStr");
	}
	if(devOwnInit==""&&$("#devOwnerId__").combobox("getValue")!=""){
		proLog+=";设置开发人员为:"+getUserNameById($("#devOwnerId__").combobox("getText"),"devStr");
	}else if(devOwnInit!=$("#devOwnerId__").combobox("getValue")){
		proLog+=";把开发人员从:"+devOwnNameInit+"改为:"+getUserNameById($("#devOwnerId__").combobox("getText"),"devStr");
	}
	if(dueDateInit==""&&$("#bugAntimodDate__").datebox("getValue")!=""){
		proLog+=";设置修改截止日期为:"+$("#bugAntimodDate__").datebox("getValue")+" 工作量为:"+$("#planAmendHour__").numberbox("getValue");
	}else if(dueDateInit!=""&&$("#bugAntimodDate__").datebox("getValue")!=""&&$("#bugAntimodDate__").datebox("getValue")!=dueDateInit){
		proLog+=";把修改截止日期从:"+dueDateInit +"改为"+$("#bugAntimodDate__").datebox("getValue")+" 工作量为:"+$("#planAmendHour__").numberbox("getValue");
	}
	if(inteOwnInit==""&& $("#intercessOwnerId__").val()!=""){
		proLog+=";设置仲裁人为:"+getUserNameById($("#intercessOwnerId__").val(),"interCesSelStr");
	}else if(inteOwnInit!=$("#intercessOwnerId__").val()){
		proLog+=";把仲裁人从:"+inteOwnNameInit+"改为:"+getUserNameById($("#intercessOwnerId__").val(),"interCesSelStr");
	}
	if(anasOwnInit==""&&$("#analyseOwnerId__").val()!=""){
		proLog+=";设置分析人为:"+getUserNameById($("#analyseOwnerId__").val(),"analySelStr");
	}else if(anasOwnInit!=$("#analyseOwnerId__").val()){
		proLog+=";把分析人从:"+anasOwnNameInit+"改为:"+getUserNameById($("#analyseOwnerId__").val(),"analySelStr");
	}
	if(asinOwnInit==""&&$("#assinOwnerId__").val()!=""){
		proLog+=";设置分配人为:"+getUserNameById($("#assinOwnerId__").val(),"assignSelStr");
	}else if(asinOwnInit!=$("#assinOwnerId__").val()){
		proLog+=";把分配人从:"+asinOwnNameInit+"改为:"+getUserNameById($("#assinOwnerId__").val(),"assignSelStr");
	}
	if(proLog!=""){
		proLog = proLog.substring(1,proLog.length);
	}
	if(proLog!=""&&$("#flwName").val()!=""&&$("#flwName").val()!="处理问题"&&$("#flwName").val()!="处理人"){
		proLog="("+$("#flwName").val()+")" +proLog;
	}
	if(appendVerStr!=""){
		proLog ="("+appendVerStr+":" +$("#currVersionName__").textbox("getValue")+")"+proLog;
	}
	return proLog;
}

function getUserNameById(id,selStrName){
	var selStrValArr = $("#"+selStrName).val().split("$");
	for(var i=0; i<selStrValArr.length; i++){
		if(selStrValArr[i].indexOf(id)>=0){
			return selStrValArr[i].split(";")[1];
		}
	}
	return $("#nextOwnName").xcombobox("getValue");;
}
//关联bug
function repeatRela(){
//	bugQuery('findBugList');
	 $("<div></div>").xwindow({
    	id:'queryBugWin',
    	title:"查询",
    	width : 1020,
        height : 500,
    	modal:true,
    	collapsible:false,
		minimizable:false,
		maximizable:false,
    	href:baseUrl + '/bugManager/bugManagerAction!bugFind.action',
    	queryParams: {'queryContainer':'findBugList'},
        onClose : function() {
            $(this).xwindow('destroy');
        }
    });
}

function fingBugs(){
	$("#findBugList").xdatagrid({
		idField:'bugId',
		url: baseUrl + '/bugManager/bugManagerAction!loadMyBug.action?dto.isAllTestTask=false'+'&dto.taskId='+taskId,
		method: 'get',
		queryParams:queryParam,
		height: mainObjs.tableHeight-145,
		singleSelect:true,
//		rownumbers: true,
		scrollbarSize:100,
		checkOnSelect:false,
		fitColumns:true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
	    layout:['list','first','prev','manual', 'sep','next','last','refresh','info'],
		pageList: [10,30,50,100],
		showPageList:true,
		rowStyler: function(index,row){
			if (row.status=='0'){
//				return 'background-color:#bddecc;color:#fff;font-weight:bold;';
//				return 'background-color:#e5fff1;';
			}
		},
		columns:[[
			{field:'checkId',title:'选择',checkbox:true,align:'center'},
//			{field:'bugId',title:'编号',width:'5%',align:'center',formatter:bugDetail},
			{field:'bugId',title:'编号',width:'5%',align:'center'},
			{field:'bugDesc',title:'bug描述',width:'30%',align:'left',halign:'center',formatter:bugTitle},
			{field:'stateName',title:'状态',width:'10%',align:'center'},
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
			{field:'devName',title:'开发人员',width:'14%',align:'center'},
			{field:'reptDate',title:'报告日期',width:'15%',align:'left'}
//			{field:'attachUrl',title:'附件',width:'5%',align:'center'}
		]],
		onLoadSuccess:function(data){
			var bugId = $("#bugId__").val();
			if(bugId!=""){
				$("#findBugList").xdatagrid("clearChecked");
				var rowIndex  = $("#findBugList").xdatagrid("getRowIndex",bugId);
				if(rowIndex !="-1"){
					$("#findBugList").xdatagrid("selectRecord",bugId);
				}
			}
			$("#findBugWin").window({title:"重复bug选择--勾选关联"});
			$("#findBugWin").xwindow('open');
			$("#findBugWin").window("vcenter");
			$("#findBugWin").window("resize");
			$("#findBugList").xdatagrid("resize");
			
		},
		onCheck:function(index,row){
			if($("#bugId__").val()!="" && $("#bugId__").val()==row.bugId){
				$.xalert({title:'提示',msg:'不能选择自身！'});
				return;
			}
			var reptDate = $("#reptDate__").textbox("getValue");
			var currDate = row.reptDate;
			if(!compareTime(currDate,reptDate)){
				$.xalert({title:'提示',msg:'所选BUG发现日期在当前处理BUG发现日期之后，应反过来关联！'});
				return;
			}
			$("#withRepteId").val(row.bugId);
			$("#repetBugTd").show();
			$("#repeDetBtn").show();
			$("#findBugWin").xwindow('close');
			$("#queryBugWin").xwindow('close');
		},
		onClickRow:function(index,row){
//			if($("#bugId__").val()!="" && $("#bugId__").val()==row.bugId){
//				$.xalert({title:'提示',msg:'不能选择自身！'});
//				return;
//			}
//			var reptDate = $("#reptDate__").textbox("getValue");
//			var currDate = row.reptDate;
//			if(!compareTime(currDate,reptDate)){
//				$.xalert({title:'提示',msg:'所选BUG发现日期在当前处理BUG发现日期之后，应反过来关联！'});
//				return;
//			}
//			alert(row.bugId);
		},
		onDblClickRow: function(index,row) {
			
		}
		
	});
}

//判断日期，时间大小  
function compareTime(startDate, endDate) {   
	if (startDate.length > 0 && endDate.length > 0) {   
	    var startDateTemp = startDate.split(" ");   
	    var endDateTemp = endDate.split(" ");   
	                   
	    var arrStartDate = startDateTemp[0].split("-");   
	    var arrEndDate = endDateTemp[0].split("-");   
	  
	    var arrStartTime = startDateTemp[1].split(":");   
	    var arrEndTime = endDateTemp[1].split(":");   
	  
		var allStartDate = new Date(arrStartDate[0], arrStartDate[1], arrStartDate[2], arrStartTime[0], arrStartTime[1], arrStartTime[2]);   
		var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2], arrEndTime[0], arrEndTime[1], arrEndTime[2]);   
		if (allStartDate.getTime() >= allEndDate.getTime()) {   
		        return false;   
		}else{   
		    return true;   
		}   
	 } else {   
	    return false;   
	}   
}

//处理历史
function bugOpHistoryList(){
	$("#bugOpHistoryList").xdatagrid({
		url: baseUrl + '/bugManager/bugManagerAction!loadHistory.action?data.isAjax=true&dto.bug.bugId='+bId,
		method: 'get',
		height: mainObjs.tableHeight-100,
		fitColumns:true,
		rownumbers: true,
		singleSelect:true,
		pagination: true,
		pageNumber: 1,
		pageSize: 16,
		pageList: [16,30,50,100],
		layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],
		onLoadSuccess:function(data){
			$("#handBugWindown").xwindow('open');
			$("#bugOpHistoryList").xdatagrid('resize');
			$("#handBugWindown").xwindow('resize');
		},
		columns:[[
			{field:'handlerName',title:'处理人',width:'20%',align:'center'},
			{field:'handResult',title:'处理结果',width:'30%',align:'center',formatter:function(value,row,index){
				return "<p style=\"cursor: pointer;margin:0;\" title=\""+value+"\">" + value + "</p>";
			}},
			{field:'remark',title:'处理说明',width:'25%',align:'center',formatter:testStatusFormat},
			{field:'insDate',title:'处理日期',width:'25%',align:'center'},
		]]
	});
}


function testStatusFormat(value,row,index){
	return value;
}
//关闭处理弹窗
function closeHandWin(){
	$("#handBugWindown").xwindow('close');
}

//null或者空字符串
function isNullOrSpace(val){
	if(val==null || val=="" || val=="null"){
		return true;
	}else{
		return false;
	}
}


//打开选择用户
function openUserWin(){
	getGroupList();
	getUsers();
	clearSearchUserWin();
	$("#chooseUserWin").window({title:"选择责任人"});
	$("#chooseUserWin").window("vcenter");
	$("#chooseUserWin").window("open");
}

//选择组 combobox组件
function getGroupList(){
	$("#userGroupList").xcombobox({
		url: baseUrl + '/userManager/userManagerAction!groupSel.action',
		valueField:'keyObj',
	    textField:'valueObj',
		onSelect: function(rec) {
			$("#userGroupListId").val(rec.keyObj);
			searchUsers();
		}
	});
}


/* 候选人员列表*/
function getUsers(){
	$("#userAll").xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!loadDefaultSelUserInAll.action',
		method: 'post',
		height: 410,
		cache: false,
		pagination: true,
		pageNumber: 1,
		pageSize: 20,
	    layout:['list','first','prev',"manual", 'sep','next','last','refresh','info'],
		pageList: [12,30,50,100],
		columns:[[
			{field:'keyObj',title:'id',hidden:true,checkbox:true,align:'center'},
			{field:'valueObj',title:'备选人员--双击选择',width:'100%',align:'center'}

		]],
		onDblClickRow: function (index, row) {
			 $("#chargeOwner").val(row.keyObj);
			 $("#chargeOwnerName").textbox("setValue", row.valueObj);
			 clearSearchUserWin();
			 $("#chooseUserWin").window("close");
		  }
	});
}
//清除选择人员查询框
function clearSearchUserWin(){
	$("chooseUserWin").xform("clear");
	$("#userGroupListId").val("");
}

function searchUsers(){
	$.post(
		baseUrl + "/userManager/userManagerAction!selectUserInAll.action",
		{
			'dto.userName':$("#chooseUserName").val(),
//			'dto.group.id' : $("#userGroupList").xcombobox("getValue"),
			'dto.group.id' : $("#userGroupListId").val()
		},
		function(data,status,xhr){
			if(status=='success'){
				$("#userAll").xdatagrid("loadData",data);
			}else{
				$.xnotify(xhr.error(), {type:'warning'});
			}
		},
	"json");
}
//bug详情
function bugDetailWin(id){
	 $("<div></div>").xwindow({
    	id:'bugDetailWin',
    	title:"缺陷详情",
    	width : 900,
        height : 600,
    	modal:true,
    	collapsible:false,
		minimizable:false,
		maximizable:false,
    	href:baseUrl + '/bugManager/bugManagerAction!bugDetail.action',
    	queryParams: {'bId':id},
        onClose : function() {
            $(this).xwindow('destroy');
        }
    });
}

//判断是否有文件
function haveFiles(){
	$.get(
			baseUrl + "/bugManager/bugManagerAction!getFileInfoByTypeId.action",
			{
				'dto.fileInfo.type':'bug',
				'dto.fileInfo.typeId': bId
			},
			function(data,status,xhr){
				if(status=='success'){
					if(data!=null && data.length>0){
						$("#messageImg").show();
						$("#fileCount").html(data.length);
						$("#bugfileLi").mouseenter(function(){
							$("#fileCount").show();
						});
						$("#bugfileLi").mouseleave(function(){
							$("#fileCount").hide();
						});
					}else{
						$("#messageImg").hide();
					}
					
				}else{
					$("#messageImg").hide();
				}
			},"json");
}


//文件信息
function bugFile(){
	if(bId){
		
	}else{
		var row = $("#bugList").xdatagrid('getSelected');
		bId = row.bugId;
	}
	
	$.get(
			baseUrl + "/bugManager/bugManagerAction!getFileInfoByTypeId.action",
			{
				'dto.fileInfo.type':'bug',
				'dto.fileInfo.typeId': bId
			},
			function(data,status,xhr){
				$("#handFileId").hide();
				if(status=='success'){
//					$("#messageImg").hide();
					$("#handFileId").fileinput('destroy');
					if(data!=null){
						$("#handMsg").hide();
						$("#handFileForm").show();
						var fileData = new Array();
						var preConfigList = new Array(); 
						for ( var i in data) {
							fileData.push(baseUrl+data[i].filePath);
							var index=data[i].relativeName.lastIndexOf(".");
							var fileTypeName = data[i].relativeName.substring(index+1,data[i].relativeName.length);
							var configJson = {
									showDownload: true,
									downloadUrl:baseUrl + data[i].filePath,
									showRemove:false,
									caption: data[i].relativeName, // 展示的文件名 
									width: '120px', 
									url: '#', // 删除url 
									key: data[i].fileId, // 删除是Ajax向后台传递的参数 
									extra: {id: 100} 
							}; 
							if(fileTypeName=="text"){
								var addParam ={type:'text'};
								 Object.assign(configJson, addParam);
							}
							if(fileTypeName=="pdf"){
								var addParam ={type:'pdf'};
								 Object.assign(configJson, addParam);
							}
							if(fileTypeName=="doc" || fileTypeName=="docx"){
								var addParam ={type:'gdocs'};
								 Object.assign(configJson, addParam);
							}
							if(fileTypeName=="mp4"|| fileTypeName=="avi"||fileTypeName=="mov"||fileTypeName=="gif"||fileTypeName=="wmv"){
								var addParam ={type:'video',filetype: "video/mp4"};
								 Object.assign(configJson, addParam);
							}
							preConfigList.push(configJson);
						}
						initFileInput(fileData,preConfigList);
					}else{
						$("#handFileId").fileinput('destroy');
						$("#handMsg").show();
						$("#handFileForm").hide();
					}
					
				}else{
					$.xalert({title:'提示',msg:'系统错误！'});
				}
			},"json");
}


function initFileInput(fileJson,fileConfigJson){
	$("#handFileId").fileinput({
		theme: 'fa',
		language:'zh',
		uploadUrl:baseUrl + "/uploader?type=bug", // you must set a valid URL here else you will get an error
		deleteUrl:'#',
//		allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx','txt','pdf','doc','docx'],
		showPreview:true,
		overwriteInitial: false,
		uploadAsync:false,
		maxFileSize: 3000,
		maxFileCount:5,
		imageMaxWidth : 100,
		imageMaxHeight : 100,
		enctype: 'multipart/form-data',
		//msgFilesTooMany :"选择图片超过了最大数量", 
		showRemove:false,
		showClose:false,
		showBrowse:false,
		showUpload:false,
		showCancel:false,
		showUploadedThumbs:false,
		showClose:false,
		dropZoneEnabled: false,//是否显示拖拽区域
		allowedPreviewTypes:['image', 'html', 'text', 'video', 'pdf', 'flash','object'],
		dropZoneEnabled:false,
		fileActionSettings:{showUpload: false},// 控制上传按钮是否显示
		initialPreviewAsData: true,
		initialPreviewShowDelete:false,
		initialPreview:fileJson,
	    initialPreviewConfig:fileConfigJson,
		previewSettings: {
//	        image: {width: "100px", height: "100px"},
	    },
		slugCallback: function (filename) {
		  return filename.replace('(', '_').replace(']', '_');
		}
	}).on("filebatchselected", function(event, files) {
		//选择文件后处理
//		alert("111");
	}).on('filebatchuploadsuccess', function(event, data, previewId, index) {
		fileInfos = data.response;
		
	}).on('filebatchuploaderror', function(event, data,  previewId, index) {
	}).on('filepreupload', function(event, data, previewId, index) {    
	}).on('fileerror', function(event, data, msg) {
	}).on("fileuploaded", function(event, data, previewId, index) {
		//上传成功后处理方法
	});
	
}


//# sourceURL=bugHand.js