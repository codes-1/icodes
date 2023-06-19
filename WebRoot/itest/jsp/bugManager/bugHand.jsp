<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<!-- <div id="handBugWindown" class="exui-window" style="display: none;padding: 20px 25px 25px;" data-options="
		modal:true,
		width: 900,
		footer:'',
		minimizable:false,
		maximizable:false,
		closed:true"> -->
		<ul class="nav nav-lattice">
			<li class="active"><a href="#bugHandInfo" data-toggle="tab">基本信息</a></li>
			<li onclick="bugOpHistoryList()"><a href="#opHandHistory" data-toggle="tab">操作历史</a></li>
			<li id="bugfileLi" onclick="bugFile()"><a href="#bugFiles" data-toggle="tab">附件信息</a><img id="messageImg" class="messagecss" src="<%=request.getContextPath()%>/itest/images/fj.png" style="display: none;"><span id="fileCount" class="fileCount"></span> </li>
		</ul>
		<div class="tab-content" style="margin-top:20px;height: 450px;"> 
			<div id="bugHandInfo" class="tab-pane fade in active">
				<input id="assignSelStr" type="hidden"/>
				<input id="analySelStr" type="hidden"/>
				<input id="testSelStr" type="hidden"/>
				<input id="devStr" type="hidden"/>
				<input id="devChkIdSelStr" type="hidden"/>
				<input id="testChkIdSelStr" type="hidden"/>
				<input id="testLdIdSelStr" type="hidden"/>
				<input id="interCesSelStr" type="hidden"/>
				<form id="bugHandForm" method="post">
					<table class="form-table" style="width: 100%;display: inherit;">
						<tbody style="width: 100%;display: table-row;">
						<tr class="hidden">
							<td>
								<input id="bugId__" type="hidden" name="dto.bug.bugId"/>
	     						<input id="processLog__" name="dto.bug.processLog" type="hidden"/>
								<input id="bugReptId__" type="hidden" name="dto.bug.bugReptId"/>
								<input id="currHandlerId__" type="hidden" name="dto.bug.currHandlerId" />
								<input id="currStateId__" type="hidden" name="dto.bug.currStateId"/>
								<input id="nextFlowCd__" name="dto.bug.nextFlowCd" type="hidden"/>
								<input id="currFlowCd__" name="dto.bug.currFlowCd" type="hidden"/>
								<input id="currHandlDate__" name="dto.bug.currHandlDate" type="hidden" />
								<input id="bugResoVer__" name="dto.bug.bugResoVer" type="hidden" />
								<input id="fixVer" name="dto.bug.fixVer" type="hidden"/>
								<input id="verifyVer" name="dto.bug.verifyVer" type="hidden"/>
								<input id="flwName" name="dto.bug.modelName" type="hidden"/>
								<input id="msgFlag__" name="dto.bug.msgFlag" type="hidden"/>
								<input id="relaCaseFlag__" name="dto.bug.relaCaseFlag" type="hidden" />
								
								<input id="withRepteId" name="dto.bug.withRepteId" type="hidden" />
								<input id="initState" name="dto.bug.initState" type="hidden" />
								
								<input id="initReProStep__" name="dto.initReProStep" type="hidden" />
								<input id="creatdate__" type="hidden" name="dto.bug.creatdate"/>
								<input id="attachUrl__" type="hidden" name="dto.bug.attachUrl"/>
								<input id="nextOwnerId__" type="hidden" name="dto.bug.nextOwnerId"/>
								<!-- <input id="testOwnerId__" name="dto.bug.testOwnerId" type="hidden" /> -->
								<input id="analyseOwnerId__" name="dto.bug.analyseOwnerId" type="hidden" />
								<input id="assinOwnerId__" name="dto.bug.assinOwnerId" type="hidden" />
								<!-- <input id="devOwnerId__" name="dto.bug.devOwnerId" type="hidden" /> -->
								<input id="intercessOwnerId__" name="dto.bug.intercessOwnerId" type="hidden" />
								<input id="testPhase__" name="dto.bug.testPhase" type="hidden"/>
								<input id="reProStep__" name="dto.bug.reProStep" type="hidden"/>
								<input id="pTestSelStr" type="hidden" />
								<input id="staFlwMemStr" type="hidden" />
							</td>
						</tr>
						<tr style="display: none;">
							<td class="left_td"><sup>*</sup>测试项目:</td>
							<td class="" colspan="5">
								<input id="task_Id__" name="dto.bug.taskId"  type="hidden">
								<input id="task_Name__" class="exui-textbox" data-options="readonly:true,prompt:'请选择测试项目'" style="width: 100%;">
							</td>
						</tr>
						<tr >
							<td class="left_td"><sup>*</sup>测试需求:</td>
							<td class="" colspan="5">
								<input id="moduleId__" name="dto.bug.moduleId"  type="hidden">
								<!-- <input id="module_Name__" class="exui-textbox"  name="dto.moduleName" type="hidden"> -->
								<input id="taskModule__" name="dto.moduleName" class="exui-textbox" data-options="readonly:true,prompt:'请选择测试需求'" style="width: 100%;">
								<!-- <p id="taskModule" style="font-size: 20px;"></p> -->
							</td>
						</tr>
						<tr >
							<td class="left_td">当前状态:</td>
							<td class="">
								<input id="stateName__" class="exui-textbox" data-options="required:true,readonly:true" style="width:180px;">
							</td>
							<td class="left_td" id="handText">处理人:</td>
							<td class="">
								<input id="currHanderName__" name="dto.bug.currHander.uniqueName" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
							<td class="left_td"><sup>*</sup>作者:</td>
							<td class="" >
								<input id="authorName__" name="dto.bug.author.uniqueName" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
						</tr>
						<tr >
							<td class="left_td">测试人员:</td>
							<td class="">
								<input id="testOwnerId__" class="exui-combobox" name="dto.bug.testOwnerId" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="testOwnName__" name="dto.bug.testOwner.uniqueName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td class="left_td">开发人员:</td>
							<td class="">
								<input id="devOwnerId__" class="exui-combobox" name="dto.bug.devOwnerId" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="devOwnerName__" name="dto.bug.devOwner.uniqueName" class="exui-textbox" type="hidden" >
							</td>
							<td class="left_td">责任人:</td>
							<td id="chargeOwnerTd" class="" >
								<inut id="chargeOwner" name="dto.bug.chargeOwner" type="hidden"  class="exui-textbox" />
								<input id="chargeOwnerName" name="dto.bug.chargeOwnerName" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
						</tr>
						<tr id="AntimodDateTr" style="display: none;">
							<td class="left_td">修改截止日期:</td>
							<td class="" style="vertical-align: middle;">
								<input id="bugAntimodDate__" name="dto.bug.bugAntimodDate" class="exui-datebox" data-options="editable:false,validateOnCreate:false,prompt:'-请选择-'" style="width:180px;">
							</td>
							<td class="left_td">工作量:</td>
							<td class="" style="vertical-align: middle;">
								<input id="planAmendHour__" name="dto.bug.planAmendHour" class="exui-numberbox" data-options="min:1,max:100,precision:1,readonly:true" style="width:100px;"><span style="line-height: 38px;">小时</span>
							</td>
						</tr>
						<tr >
							<td class="left_td"><sup>*</sup>Bug描述:</td>
							<td class="" colspan="5" style="vertical-align:middle;">
								<input id="bugDesc__" name="dto.bug.bugDesc" class="exui-textbox" data-options="required:true" style="width: 100%;">
							</td>
						</tr>
						<tr >
							<td class="left_td"><sup>*</sup>类型:</td>
							<td >
								<input id="bugTypeId__" style="width:180px;" class="exui-combobox" name="dto.bug.bugTypeId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" />
								<input id="bugTypeName__" name="dto.bug.bugType.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td class="left_td"><sup>*</sup>等级:</td>
							<td >
								<input id="bugGradeId__" class="exui-combobox" name="dto.bug.bugGradeId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="bugGradeName__" name="dto.bug.dtoHelper.bugGrade.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>平台:</td>
							<td >
								<input id="platformId__" class="exui-combobox" name="dto.bug.platformId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="pltformName__" name="dto.bug.occurPlant.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
						</tr>
						<tr >
							<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>来源:</td>
							<td >
								<input id="sourceId__" class="exui-combobox" name="dto.bug.sourceId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="sourceName__" name="dto.bug.bugSource.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td class="left_td"><sup>*</sup>发现时机:</td>
							<td >
								<input id="bugOccaId__" class="exui-combobox" name="dto.bug.bugOccaId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="occaName__" name="dto.bug.bugOpotunity.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td id="geneCauseTd__" class="left_td" style="padding-left: 0.5em;display: none;">测试时机:</td>
							<td id="geneCauseIdTd__" style="display: none;">
								<input id="geneCauseId__" class="exui-combobox" name="dto.bug.geneCauseId" data-options="
				    			validateOnCreate:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="geneCaseName__" name="dto.bug.geneCause.typeName" class="exui-textbox" type="hidden" >
								<div id="geneCauseF__" style="display: none" >
							</td>
						</tr>
						<tr >
							<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>优先级:</td>
							<td >
								<input id="priId__" class="exui-combobox" name="dto.bug.priId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="priName__" name="dto.bug.dtoHelper.bugPri.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>频率:</td>
							<td >
								<input id="bugFreqId__" class="exui-combobox" name="dto.bug.bugFreqId" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="bugFreqName__" name="dto.bug.dtoHelper.bugFreq.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td id="reproTd__" class="left_td" style="display: none;">再现比例:</td>
							<td id="reproPersentTd__" class="dataM_left" colspan="" style="display: none;">
								<input id="reproPersent__" name="dto.bug.reproPersent" class="exui-textbox" style="width: 180px;">
							</td>
						</tr>
						
						
						<tr id="genePhaseIdTr__" style="display: none;">
							<td class="left_td" ><sup>*</sup>引入原因:</td>
							<td id="geePhTd" colspan="5">
								<input id="genePhaseId__" class="exui-combobox" name="dto.bug.genePhaseId" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="genPhName__" name="genPhName" class="exui-textbox" type="hidden" data-options="readonly:true" style="width:180px;">
							</td>
						</tr>          
						<tr >
							<td class="left_td"><sup>*</sup>再现步骤:</td>
							<td class="dataM_left" colspan="5">
								<input id="reProTxt__" name="dto.bug.reProTxt" class="exui-textbox" data-options="multiline:true,required:true,prompt:'请填写再现步骤'," style="width: 100%;height: 140px;">
							</td>
						</tr>
						<tr id="changeStatus" style="display:none;">
							<td class="left_td">更改状态为:</td>
							<td >
								<input id="stateIdList" class="exui-combobox" name="stateIdList" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<!-- <input id="testOwnName_" name="testOwnName" class="exui-textbox" type="hidden" > -->
							</td>
							<td class="left_td" id="relaTd" align="left" style="border-right:0;display: none;">
							    <div id="repeBtn" style="display:none;" ><a class="graybtn" href="javascript: repeatRela();"><span> 关联重复</span></a>
							    </div>
							</td>
							<td id="repetBugTd"  align="center" style="border-right:0;display: none;">
								<div id="repeDetBtn" style="display:none;">
								 	 <a class="graybtn" href="javascript: bugDetailWin($('#withRepteId').val());"><span> 所重BUG明细</span></a>
								 </div>	
							</td>	
							<td id="hasstatus" colspan="2" style="border-right:0;display: none;">
							</td> 
							<td id="nostatus" colspan="3" style="border-right:0;display: none;">
								<div >无状态可选，表明只能修改测试人员或开发人员等信息</div>
								<div id="repeBtns" > </div>
							</td>  
						</tr>
						
						<tr id="nextOwnTr" style="display: none;">
							<td class="left_td" id="nextOwnTd1">转:</td>
							<td id="nextOwnTd2" >
								<input id="nextOwnName" name='nextOwnName'  class="exui-combobox" name="person" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<!-- <input id="initOwnName" name="testOwnName" class="exui-textbox" type="hidden" > -->
							</td>
							<td id="nextOwnTd3" style="text-align: center;">处理</td>
							<td id="assFromMdTd" colspan="3" style="border-right:0"></td>
						<tr>
							<td class="left_td"><sup>*</sup>发现日期:</td>
							<td class="">
								<input id="reptDate__" name="dto.bug.reptDate" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
							<td class="left_td">发现版本:</td>
							<td style="text-align: right;">
								<input id="bugReptVer__" class="exui-combobox" name="dto.bug.bugReptVer" data-options="
				    			readonly:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="bugReptVerName__" name="dto.bug.reptVersion.versionNum" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td id="verLableTd" style="display: none;"  class="left_td"><sup>*</sup>关闭在版本: </td>
							<td id="verLableValueTd"  style="text-align: right;display: none;">
								<input id="versionLable" name="currVerName" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
							<td style="display: none;" id="currVerTd" class="left_td"><sup>*</sup>检验在版本: </td>
							<td style="display: none;" id="currVerValueTd" style="text-align: right;">
								<input id="currVersion__" class="exui-combobox" name="dto.bug.bugReptVer" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
								<input id="currVersionName__" name="currVerName" class="exui-textbox" type="hidden" data-options="" >
							</td>
						</tr>
						<tr id="currRemarkTr__">
							<td class="left_td">备注:</td>
							<td colspan="5">
								<input id="currRemark__" name="dto.bug.currRemark" class="exui-textbox" style="width: 100%;" data-options="multiline:false,readonly:true,prompt:'备注'" />
		   			      </td>
						</tr>
					</tbody>
				</table>
			</form>
			<div id="" align="right" style="margin-bottom: 1em;padding: 0.5em;" >
				<a id="handSubmitBnt" class="exui-linkbutton bntsearchcss hoverBu" data-options="btnCls:'default',size:'xs'" onclick="handSubmitForm()">确定</a>
				<a class="exui-linkbutton bntcss" data-options="btnCls:'default',size:'xs'" onclick="closeHandWin()">返回</a>
			</div>
		</div>
		
		<div id="opHandHistory" class="tab-pane fade">
		</div>
		<table id="bugOpHistoryList" class="exui-datagrid" title="" style="width:100%;height: auto;" data-options=""></table>
		
		<div id="bugFiles" class="tab-pane fade">
			<h4 id="handMsg" align="center" style="display: none;">暂无附件信息</h4>
			<form id="handFileForm" enctype="multipart/form-data">
		        <div class="form-group">
		            <div class="file-loading">
		                <input id="handFileId" type="file" multiple class="file" data-overwrite-initial="false">
		            </div>
		        </div>
		    </form>
		</div>
	
	</div>
</div>

<div id="chooseUserWin" class="exui-window" style="display:none; " data-options="
	modal:true,
	width: 500,
	minimizable:false,
	maximizable:false,
	closed:true">
	<div class="tools" data-options="" style="margin-bottom: 0;">
		<div class="input-field" style="width: 145px;">
			<input id="userGroupList" class="exui-combobox" name="dto.group" 
			data-options="
   			validateOnCreate:false,
   			editable:false,
   			prompt:'-选择组-' " style="width:145px;position: static;"/>
   			<input id="userGroupListId" type="hidden">
		</div>
	    <div class="input-field" style="width: 145px;"> 
		<input class="form-control " id="chooseUserName" placeholder="请输入组员姓名"/>
		</div>
		<button type="button" class="btn btn-default bntcss" onclick="searchUsers()"><i class="glyphicon glyphicon-search"></i>查询</button>
    </div>
    
    <table id="userAll" data-options="
	    height: 400,
		fitColumns: true,
		rownumbers: true,
		singleSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 100,
		layout:['list','first','prev','manual', 'sep','next','last','refresh','info']">
	</table>
    
<!-- </div> -->

<div id="findBugWin" title="" class="exui-window" data-options="
		modal:true,
		width: 900,
		minimizable:false,
		maximizable:false,
		closed:true">
		<table id="findBugList" class="exui-datagrid" title="" style="width:100%;height: auto;" data-options=""></table>
</div>

<script src="<%=request.getContextPath()%>/itest/js/bugManager/bugHand.js" type="text/javascript" charset="utf-8"></script>
