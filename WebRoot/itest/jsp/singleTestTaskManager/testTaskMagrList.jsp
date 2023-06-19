<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/singleTestTaskManager/testTaskMagrList.css"/>
<%@include file="/itest/jsp/chooseMuiltPeople/chooseMuiltPeople.jsp" %>

<!--top tools area-->
<div class="tools">
	<div class="input-field" style="width: 170px;">
		<span>项目名称：</span>
		<input id="projectName" class="form-control indent-4-5" placeholder="名称+回车键"/>
	</div>
	<div id="fastSear" class="input-field" style="width: 200px;">
		<span>编号查询：</span>
		<input id="projectNum" class="form-control indent-4-5" placeholder="项目编号+回车键"/>
	</div>
	
	<div id="depart" class="input-field" style="width: 200px;">
		<span>研发部门：</span>
		<input id="department" class="form-control indent-4-5" placeholder="部门名称+回车键"/>
	</div>
	
	<div id="statusSty" class="input-field" style="width: 180px;">
	    <select id="projectStatus" name="dto.singleTest.status" class="form-control chzn-select" onchange="searchTestProject()">项目状态 </select>
	</div>
	
	<button id="searchCon" type="button" schkUrl="singleTestTaskAction!magrTaskListLoad" class="btn btn-default" style="background: #1E7CFB;border: 1px solid #1E7CFB;color: #ffffff;" onclick="searchTestProject()"><i class="glyphicon glyphicon-search"></i>查询</button>
	<button id="resetCon" type="button" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="resetInfo()"><i class="glyphicon glyphicon-repeat"></i>重置</button>
	<button id="addCon" type="button" schkUrl="singleTestTaskAction!add" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showAddWin()"><i class="glyphicon glyphicon-plus"></i>增加</button>
	<button id="editCon" type="button" schkUrl="singleTestTaskAction!update" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showEditWin()"><i class="glyphicon glyphicon-pencil"></i>修改</button>
	<button id="deleteCon" type="button" schkUrl="singleTestTaskAction!delete" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showdelConfirm()"><i class="glyphicon glyphicon-remove"></i>删除</button>
	<button id="flwSetInit" type="button" class="btn btn-default hoverBu" schkUrl="testTaskManagerAction!flwSetInit1" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="setTestFlow()"><i class="glyphicon glyphicon-link"></i>设置测试流程</button>
</div><!--/.top tools area-->

<table id="testTaskDg" data-options="
	fitColumns: true,
	rownumbers: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info']
"></table>

<!-- PM模态窗 -->
<!-- <div id="selectPmWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 550,
	minimizable:false,
	maximizable:false,
	closed:true">
	<table class="form-table">
		<form id="pMForm" method="post">
			<tr class="hidden">
				<td>
					<input id="taskId" name="dto.singleTest.taskId"/>
				</td>
			</tr>
	    
	    	<tr>
	    		<th>用户组：</th>name="dto.group.id"
	    		<td><input class="exui-combobox" id="userGroupM" data-options="
	    			prompt:'-请选择-'" style="width:160px"/></td>
	    			
	    		<th style="padding-left: 21px;">姓名：</th>
	    		<td><input class="exui-textbox" name="dto.userName" data-options="required:true,validateOnCreate:false" style="width:160px"/></td>
	    		
	    		<td><button style="margin-left: 15px;" type="button" onclick="" class="btn btn-primary">查询</button></td>
	    	</tr>
			
	    </form>
	</table>
	
	<table id="pMList" data-options="
	fitColumns: true,
	rownumbers: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info']
	
"></table>
	
</div> -->

<!-- 新增/修改单井模态窗 -->
<div id="addOrEditWinSin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 700,
	height:300,
	footer:'#addOrEditSinFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
		<form id="addOrEditSinForm" method="post">
		  <table class="form-table">
			<tr class="hidden"><td>
				<input id="taskId" name="dto.singleTest.taskId"/>
				<input id="insDate" name="dto.singleTest.insDate"/>
				<input id="updDate" name="dto.singleTest.updDate"/>
				<input id="createId" name="dto.singleTest.createId"/>
				<input id="status" name="dto.singleTest.status"/>
				<input id="attachUrl" name="dto.singleTest.planDocName"/>
			</td></tr>
			<tr>
				<th><sup>*</sup>项目编号：</th>
	    		<td><input class="exui-textbox projectNums" name="dto.singleTest.proNum" data-options="required:true,validateOnCreate:false" style="width:180px"/></td>
	    		<th id="project_name" style="padding-left: 12px;"><sup>*</sup>项目名称：</th>
	    		<td>
	    			<input class="exui-textbox hadProject" name="dto.singleTest.proName" data-options="required:true,validateOnCreate:false" style="width:180px"/>
	    			<button id="selectNowPro" type="button" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;vertical-align: middle;padding:2px;" ><!-- onclick="selectNowPro()" -->
	    				<i class="glyphicon glyphicon-th-list"></i>已有项目</button>
	    		</td>
	    		
	    		<td style="display: none;">
	    			<select class="exui-combobox elseTaskPro" name="dto.singleTest.proName" data-options="required:true,validateOnCreate:false,prompt:'-请选择-',editable:false" style="width:180px"></select>
	    			<button id="addNewPro" type="button" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;vertical-align: middle;padding:2px;" ><!-- onclick="addNewPro()" -->
	    				<i class="glyphicon glyphicon-plus"></i>新增项目</button>
	    		</td>
	    	</tr>
	    
	    	<tr>
	    		<!-- <th><sup>*</sup>测试阶段：</th>
	    		<td><input class="exui-combobox" name="dto.singleTest.testPhase" data-options="
	    			required:true,
	    			validateOnCreate:false,
	    			valueField:'value',
	    			editable:false,
	    			textField:'desc',
	    			prompt:'-请选择-',
	    			data:[{desc:'',value:-1},{desc:'单元测试',value:0},{desc:'集成测试',value:1},{desc:'系统测试',value:2}]" style="width:180px"/></td> -->
	    		<th><sup>*</sup>研发部门：</th>
	    		<td><input class="exui-textbox developmentDep" name="dto.singleTest.devDept" data-options="required:true,validateOnCreate:false" style="width:180px"/></td>
	    		
	    		<th><sup>*</sup>PM姓名：</th>
	    		<td>
	    			<input type="hidden" id="psmId" name="dto.singleTest.psmId"/>
	    			<input type="hidden" id="pmId" name="dto.singleTest.psmId"/>
	    			<!-- <input id="pm" class="exui-combobox relaUser" name="dto.singleTest.psmName" data-options="prompt:'-请选择-'" placeholder="" style="width:180px"/> -->
	    			<select id="pm" class="proStName" onchange="searchPm()" style="width:180px"></select>
	    		</td>
	    	</tr>
	    	
	    	<tr>
	    		<th><sup>*</sup>开始日期：</th>
	    		<td><input class="exui-datebox startTime" data-options="required:true,editable:false,validateOnCreate:false,prompt:'-请选择-'" name="dto.singleTest.planStartDate" style="width:180px"/></td>
	    		<th><sup>*</sup>结束日期：</th>
	    		<td><input class="exui-datebox endTime" data-options="required:true,editable:false,validateOnCreate:false,prompt:'-请选择-'" name="dto.singleTest.planEndDate" style="width:180px"/></td>
	    	</tr>
	    	
	    	<tr id="editProStatus" style="display: none;">
	    		<th><!-- <sup>*</sup> -->项目状态：</th>
	    		<td><input class="exui-combobox editSta" name="dto.singleTest.status" style="width:180px"/></td>
	    	</tr>
	    	
	    	<!-- <tr>
	    		<th>用户组：</th>
	    		<td>
	    			<input id="userGroup" name="dto.group.id" class="exui-combobox" data-options="
	    			prompt:'-请选择-'" style="width:180px"/>
	    		</td>
	    		
	    		<th>测试计划：</th>
				<input type="hidden" id="testPhase" />
	    		<td><input class="exui-filebox" name="dto.testTaskManage.testPhase" style="width:180px"/></td>
	    		
	    	</tr> -->
	      </table>
	    </form>
	   <!--  <form id="uploadForm" enctype="multipart/form-data" method="post">
			<tr>
				<th>测试计划：</th>
				<input type="hidden" id="testPhase" />
	    		<td><input class="exui-filebox" name="dto.testTaskManage.testPhase" style="width:180px"/></td>
			</tr>
		</form> -->
</div>
<div id="addOrEditSinFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" style="background-color: #42a5f5;" onclick="submit()">保存</a>
	<a id="saveAndProjectFlow" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" style="background-color: #42a5f5;" onclick="saveAndProjectFlow()" schkUrl="testTaskManagerAction!flwSetInit2">保存并设置流程</a>
	<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="closeWin()">取消</a>
</div>

<!-- 新增/修改单井模态窗 -->
<div id="newCreateVersion" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 540,
	footer:'#VersionFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<table class="form-table">
		<form id="addOrEditVersionForm" method="post">
			<tr class="hidden">
				<td>
					<input id="taId" name="dto.softVer.taskid"/>
					<input id="versionId" name="dto.softVer.versionId"/>
					<input id="verStatus" name="dto.softVer.verStatus"/>
				</td>
			</tr>
			
			<tr>
				<th><sup>*</sup>名称：</th>
	    		<td><input class="exui-textbox" name="dto.softVer.versionNum" data-options="required:true,validateOnCreate:false" style="width:180px"/></td>
	    		<th><sup>*</sup>序号：</th>
	    		<td><input class="exui-textbox" name="dto.softVer.seq" data-options="required:true,validateOnCreate:false" style="width:180px"/></td>
	    	</tr>
	    	
	    	<tr>
				<th>备注：</th>
	    		<td colspan="3"><input class="exui-textbox" name="dto.softVer.remark" data-options="required:true,validateOnCreate:false" style="width:180px"/></td>
	    	</tr>
	    	
	    </form>
	</table>
</div>
<div id="VersionFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="saveOrEditVersion()">保存</a>
	<a class="exui-linkbutton" style="border: 1px solid #1E7CFB;color: #1E7CFB;" data-options="btnCls:'default',size:'xs'" onclick="cancleVers()">取消</a>
</div>

<!-- 新增/修改单井模态窗 -->
<div id="testFlowWin" class="exui-window" style="display: none;padding: 20px 25px 25px;" data-options="
	modal:true,
	width: 680,
	footer:'#testFlowFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<ul class="nav nav-lattice">
		<li class="active"><a href="#workflow" data-toggle="tab">流程设置</a></li>
		<li><a href="#version" data-toggle="tab">版本维护</a></li>
	</ul>
	<div class="tab-content" style="margin-top:20px;height: 450px;"> 
		<div id="workflow" class="tab-pane fade in active">
			<input type="hidden" id="initFlowStr" name="dto.testFlowStr"/>
			<input type="hidden" id="testFlowStr" name="dto.initFlowStr"/>
			<form id="workflowForm" method="post" class="form-horizontal">
				<input type="hidden" id="taskType" name="dto.taskType"/>
				<input type="hidden" id="taskIds" name="dto.detail.taskId"/>
				<input type="hidden" id="testPhase" name="dto.detail.testPhase"/>
				<input type="hidden" id="relaTaskId" name="dto.detail.relaTaskId"/>
				<input type="hidden" id="outlineState" name="dto.detail.outlineState"/>
				<input type="hidden" id="taskState" name="dto.detail.taskState"/>
				<input type="hidden" id="customCase" name="dto.detail.customCase"/>
				<input type="hidden" id="customBug" name="dto.detail.customBug"/>
				<input type="hidden" id="testerId" name="dto.testerId"/>
				<input type="hidden" id="testerConfId" name="dto.testerConfId"/>
				<input type="hidden" id="analyserId" name="dto.analyserId"/>
				<input type="hidden" id="assignationId" name="dto.assignationId"/>
				<input type="hidden" id="programmerId" name="dto.programmerId"/>
				<input type="hidden" id="empolderAffirmantId" name="dto.empolderAffirmantId"/>
				<input type="hidden" id="empolderLeaderId" name="dto.empolderLeaderId"/>
				<input type="hidden" id="testLeadId" name="dto.testLeadId"/>
				<input type="hidden" id="testTaskState" name="dto.detail.testTaskState"/>
				<input type="hidden" id="projectId" name="dto.detail.projectId"/>
				<input type="hidden" id="caseReviewId" name="dto.caseReviewId"/>
				<input type="hidden" id="testSeq" name="dto.detail.testSeq"/>
				<input type="hidden" id="proRelaPersonId" name="dto.proRelaPersonId"/>
				<table>
					<tr>
						<th style="font-size: 16px;color:#353535;">任务名称：</th>
						<th id="taskName" colspan="2" style="font-size: 14px;">框架测试</td>
					</tr>
					
					<tr>
						<th>负责人设置：</th>
						<th>
							<label style="padding-top: 5px;font-size: 14px;">测试负责人<sup>*</sup>：</label>
							<!-- <input class="exui-textbox" name="dto.testLead" data-options="required:true,validateOnCreate:false"/> -->
							<!-- <input id="comboTestLead" class="exui-combo" name="dto.testLead"/> -->
							<input id="comboTestLead" class="exui-combobox test_Lead testLead comboTestLead" name="dto.testLead" style="width: 327px;float:right">
						</th>
						<th>
						  <img class="selImg0" alt="选择人员" title="选择人员" onclick="showSeletctPeopleWindow('comboTestLead')" style="padding-left: 5px;cursor: pointer;" src="<%=request.getContextPath()%>/itest/images/mSearch.png">
						</th>
					</tr>
					
				</table>
				
				<div class="lineS"></div>
				
				<table id="secondTable">
					<tr>
						<th>流程设置:</th>
					</tr>
					
					<!-- <tr>
						<td>
							<input type="checkbox" name="dto.caseReview" />&nbsp;&nbsp;
							<span class="fontColor">用例Review</span>
						</td>
						
						<td style="padding-left: 20px;">
							<span class="fontCs">Review人:&nbsp;&nbsp;</span>
							<input class="exui-combobox" name="dto.caseReviewer">
							<input class="exui-textbox" name="dto.caseReviewer" data-options="required:true,validateOnCreate:false"/>
						</td>
					</tr> -->
					
					<tr>
						<td>
							<input id="testBugPerson" type="checkbox" name="dto.reportBug" disabled="disabled"/>&nbsp;&nbsp;
							<span class="fontColor">提交问题</span>
						</td>
						
						<td style="padding-left: 15px;">
							<span class="fontCs">测试人员<sup>*</sup>:&nbsp;&nbsp;</span><!-- prompt:'zhangsan(张三)', -->
							<input id="dTesterId" class="exui-combobox testerId testLead dTesterId" name="dto.tester" style="width: 337px;">
							<!-- <input class="exui-textbox" name="dto.tester" data-options="required:true,validateOnCreate:false"/> -->
						</td>
						<td>
						  <img class="selImg1" alt="选择人员" title="选择人员" onclick="showSeletctPeopleWindow('dTesterId')" style="padding-left: 5px;cursor: pointer;" src="<%=request.getContextPath()%>/itest/images/mSearch.png">
						</td>
					</tr> 
					
					<tr>
						<td>
							<input type="checkbox" name="dto.testerBugConfirm" />&nbsp;&nbsp;
							<span class="fontColor">测试交互</span>
						</td>
						
						<td style="padding-left: 25px;">
							<span class="fontCs">互验人员:&nbsp;&nbsp;</span>
							<input id="testerConf" class="exui-combobox testerConfId testLead testerConf" name="dto.testerConf" style="width: 337px;">
							<!-- <input class="exui-textbox" name="dto.testerConf" data-options="required:true,validateOnCreate:false"/> -->
						</td>
						<td>
						  <img class="selImg2" alt="选择人员" title="选择人员" onclick="showSeletctPeopleWindow('testerConf')" style="padding-left: 5px;cursor: pointer;" src="<%=request.getContextPath()%>/itest/images/mSearch.png">
						</td>
					</tr>
					
					<tr>
						<td>
							<input type="checkbox" name="dto.analyse" />&nbsp;&nbsp;
							<span class="fontColor">分析问题</span>
						</td>
						
						<td style="padding-left: 38px;">
							<span class="fontCs">分析人:&nbsp;&nbsp;</span>
							<input id="analyser" class="exui-combobox analyserId testLead analyser" name="dto.analyser" style="width: 337px;">
							<!-- <input class="exui-textbox" name="dto.analyser" data-options="required:true,validateOnCreate:false"/> -->
						</td>
						<td>
						  <img class="selImg3" alt="选择人员" title="选择人员" onclick="showSeletctPeopleWindow('analyser')" style="padding-left: 5px;cursor: pointer;" src="<%=request.getContextPath()%>/itest/images/mSearch.png">
						</td>
					</tr>
					
					<tr>
						<td>
							<input type="checkbox" name="dto.assignation" />&nbsp;&nbsp;
							<span class="fontColor">分配问题</span>
						</td>
						
						<td style="padding-left: 38px;">
							<span class="fontCs">分配人:&nbsp;&nbsp;</span>
							<input id="assigner" class="exui-combobox assignationId testLead assigner" name="dto.assigner" style="width: 337px;">
							<!-- <input class="exui-textbox" name="dto.assigner" data-options="required:true,validateOnCreate:false"/> -->
						</td>
						<td>
						  <img class="selImg4" alt="选择人员" title="选择人员" onclick="showSeletctPeopleWindow('assigner')" style="padding-left: 5px;cursor: pointer;" src="<%=request.getContextPath()%>/itest/images/mSearch.png">
						</td>
					</tr>
					
					<tr>
						<td>
							<input id="fixBugPerson" type="checkbox" name="dto.devFixBug" disabled="disabled"/>&nbsp;&nbsp;
							<span class="fontColor">修改问题</span>
						</td>
						
						<td style="padding-left: 28px;">
							<span class="fontCs">修改人<sup>*</sup>:&nbsp;&nbsp;</span>
							<input id="programmer" class="exui-combobox programmerId testLead programmer" name="dto.programmer" style="width: 337px;">
							<!-- <input class="exui-textbox" name="dto.programmer" data-options="required:true,validateOnCreate:false"/> -->
						</td>
						<td>
						  <img class="selImg5" alt="选择人员" title="选择人员" onclick="showSeletctPeopleWindow('programmer')" style="padding-left: 5px;cursor: pointer;" src="<%=request.getContextPath()%>/itest/images/mSearch.png">
						</td>
					</tr>
					
					<tr>
						<td>
							<input type="checkbox" name="dto.devConfirmFix" />&nbsp;&nbsp;
							<span class="fontColor">开发互检</span>
						</td>
						
						<td style="padding-left:38px;">
							<span class="fontCs">互检人:&nbsp;&nbsp;</span>
							<input id="empolderAffirmant" class="exui-combobox empolderAffirmantId testLead empolderAffirmant" name="dto.empolderAffirmant" style="width: 337px;">
							<!-- <input class="exui-textbox" name="dto.empolderAffirmant" data-options="required:true,validateOnCreate:false"/> -->
						</td>
						<td>
						  <img class="selImg6" alt="选择人员" title="选择人员" onclick="showSeletctPeopleWindow('empolderAffirmant')" style="padding-left: 5px;cursor: pointer;" src="<%=request.getContextPath()%>/itest/images/mSearch.png">
						</td>
					</tr>
					
					<tr>
						<td>
							<input id="arbPerson" type="checkbox" name="dto.arbitrateBug" disabled="disabled"/>&nbsp;&nbsp;
							<span class="fontColor">分歧仲裁</span>
						</td>
						
						<td style="padding-left: 28px;">
							<span class="fontCs">仲裁人<sup>*</sup>:&nbsp;&nbsp;</span>
							<input id="empolderLeader" class="exui-combobox empolderLeaderId testLead empolderAffirmant" name="dto.empolderLeader" style="width: 337px;">
							<!-- <input class="exui-textbox" name="dto.empolderLeader" data-options="required:true,validateOnCreate:false" placeholder="sss"/> -->
						</td>
						<td>
						  <img class="selImg7" alt="选择人员" title="选择人员" onclick="showSeletctPeopleWindow('empolderLeader')" style="padding-left: 5px;cursor: pointer;" src="<%=request.getContextPath()%>/itest/images/mSearch.png">
						</td>
					</tr>
					
					<tr>
						<td>
							<input type="checkbox" name="dto.proRelaPersonFlg"/>&nbsp;&nbsp;
							<span class="fontColor">项目关注</span>
						</td>
						
						<td style="padding-left: 38px;">
							<span class="fontCs">关注人:&nbsp;&nbsp;</span>
							<input id="proRelaPerson" class="exui-combobox proRelaPersonId testLead proRelaPerson" name="dto.proRelaPerson" style="width: 337px;">
							<!-- <input class="exui-textbox" name="dto.proRelaPerson" data-options="required:true,validateOnCreate:false"/> -->
						</td>
						<td>
						  <img class="selImg8" alt="选择人员" title="选择人员" onclick="showSeletctPeopleWindow('proRelaPerson')" style="padding-left: 5px;cursor: pointer;" src="<%=request.getContextPath()%>/itest/images/mSearch.png">
						</td>
					</tr>
					
					
					<tr>
						<td>
							<input id="testSure" type="checkbox" name="dto.testerVerifyFix" disabled="disabled"/>&nbsp;&nbsp;
							<span class="fontColor">测试确认</span>
						</td>
						
					</tr>
					
				</table>
				
				<div class="lineS"></div>
				
				<table style="margin-top: 10px;">
					<tr>
						<th>消息设置:</th>
					</tr>
					
					<tr>
						<td style="padding-top:5px;">
							<input type="checkbox" name="dto.detail.mailOverdueBug" />&nbsp;&nbsp;
							<span class="fontColor">通知项目关注人</span>
						</td>
						
						<td style="padding:5px 0 0 20px;">
							<input type="checkbox" name="dto.detail.mailDevFix" />&nbsp;&nbsp;
							<span class="fontColor">bug指派时通知修改人</span>
						</td>
						
					</tr>
					
					<tr>
						<td style="padding-top:5px;">
							<input type="checkbox" name="dto.detail.mailVerdict" />&nbsp;&nbsp;
							<span class="fontColor">需仲裁时通知仲裁人</span>
						</td>
						
						<td style="padding:5px 0 0 20px;">
							<input type="checkbox" name="dto.detail.mailReport" />&nbsp;&nbsp;
							<span class="fontColor">每日通知开发负责人,测试负责人测试报告</span>
						</td>
						
					</tr>
					
				</table>
			</form>
			
			<div class="buttonSt">
				<button type="button" onclick="submitInfo()" id="saveBtn" schkUrl="testTaskManagerAction!update" class="btn btn-primary">确认</button>
				<button id="resetting" onclick="resetting()" style="border: 1px solid #1E7CFB;color: #1E7CFB;" type="button" class="btn btn-default">重置</button>
				<button type="button" onclick="returnWin()" style="border: 1px solid #1E7CFB;color: #1E7CFB;" class="btn btn-default" style="background-color: #b2bdcb;color: #ffff;">取消</button>
			</div>
			
		</div>
		
		<div id="version" class="tab-pane fade">
			<div class="buttonPos">
				<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="newCreateOrEditVer('')"><i class="glyphicon glyphicon-plus"></i>新建版本</button>
				<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="newCreateOrEditVer('edit')"><i class="glyphicon glyphicon-pencil"></i>修改版本</button>
				<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="deleteVer()"><i class="glyphicon glyphicon-remove"></i>删除</button>
				<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="startVersion()"><i class="glyphicon glyphicon-play"></i>启用</button>
				<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;display: none;" onclick="stopVersion()"><i class="glyphicon glyphicon-stop"></i>停用</button>
			</div>
	
		</div>
		
		<table id="versionMaintenance" data-options="
					fitColumns: true,
					rownumbers: true,
					singleSelect: true,
					pagination: true,
					pageNumber: 1,
					pageSize: 10,
					layout:['list','first','prev','manual','next','last','refresh','info']">
	 	</table>
  </div>
</div>


<div id="allData" style="display: none;"></div>
<div id="hiddenVal" style="display: none;"></div>
<div id="testPersonI" style="display: none;"></div>
<div id="updateTaskId" style="display: none;"></div>
<input type="hidden" id="projectId"/>
<input type="hidden" id="saveFlow"/>
<script type="text/javascript">
	$.parser.parse();
</script>
<script src="<%=request.getContextPath()%>/itest/js/singleTestTaskManager/testTaskMagrList.js" type="text/javascript" charset="utf-8"></script>