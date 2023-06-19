<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/bugManager/bugManagerList.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/iteration/iteration.css"/>
<style>
.tools button:first-of-type{
  background: #1E7CFB;
    border: 1px solid #1E7CFB;
    color: #ffffff;
}

#detail_Table tbody tr{
	height: 2em;
	border: 0.5px solid #DDDDDD;
}
#detail_Table tbody tr td{
	border-right: 0.5px solid #DDDDDD;
}
 th{
 	font-weight:400;
} 
#second_Table tr {
    line-height: 45px;
}
/* .searchable-select {
    min-width: 140px;
} */
</style>

<!--top tools area-->
<div class="tools">
	<div class="input-field" style="width: 180px;">
		<span style="width: 100px;">迭代：</span>
		<input id="iterationBagName" style="padding-left: 0px;" class="form-control indent-4-5" placeholder="名称+回车键"/>
	</div>
	<div class="input-field" style="width: 195px;">
		<span>创建人：</span>
		<input id="createPerson" class="form-control indent-4-5" placeholder="创建人+回车键"/>
	</div>
	<div class="input-field" style="width: 145px;">
		<select id="proAllName" class="proStName" onchange="searchIteration()"></select>
	</div>
	
	<button id="iterationSear" type="button" schkUrl="iterationAction!iterationDataListLoad" class="btn btn-default" style="background: #1E7CFB;border: 1px solid #1E7CFB;color: #ffffff;display:none" onclick="searchIteration()"><i class="glyphicon glyphicon-search"></i>查询</button>
	<button id="iterationReset" type="button" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="iterationReset()"><i class="glyphicon glyphicon-repeat"></i>重置</button>
	<button id="iterationAddCon" type="button" schkUrl="iterationAction!saveOrUpdateIteration" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showIterationAdd()"><i class="glyphicon glyphicon-plus"></i>增加</button>
	<button id="iterationEditCon" type="button" schkUrl="iterationAction!saveOrUpdateIteration" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showIterationEditWin()"><i class="glyphicon glyphicon-pencil"></i>修改</button>
	<button id="iterationDeleteCon" type="button" schkUrl="iterationAction!deleteIterationInfo" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showdelIteraConfirm()"><i class="glyphicon glyphicon-remove"></i>删除</button>
</div><!--/.top tools area-->


<table id="iterationBugList" data-options="
	fitColumns: true,
	rownumbers: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info']
"></table>

<table id="iterationTestCaseList" data-options="
	fitColumns: true,
	rownumbers: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info']
"></table>



<div id="iterationAccordion" class="exui-accordion">
    <div title="迭代列表  -- 双击查看明细" data-options="iconCls:'icon-save',selected:true" style="overflow:auto;padding:10px;height:600px;">
		<table id="iterationList" ></table>
    </div>
    <div title="迭代明细" data-options="iconCls:'icon-reload'" style="padding:10px;">
		<ul class="nav nav-lattice">
			<li class="active"><a href="#proBug" data-toggle="tab">项目bug</a></li>
			<li><a href="#proTask" data-toggle="tab">项目任务</a></li>
			<li onclick="projectTestCase()"><a href="#proTestcase" data-toggle="tab">测试包</a></li>
		</ul>
		
		<div style="padding: 15px 0;">
			<button id="addBug" type="button" schkUrl="" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;display: none;" onclick="iterBugLayout()"><i class="glyphicon glyphicon-plus"></i>分配Bug</button>
			<button id="addTask" type="button" schkUrl="" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;display: none;" onclick="iterationTaskLayout()"><i class="glyphicon glyphicon-plus"></i>分配任务</button>
			<button id="addTestCase" type="button" schkUrl="" class="btn btn-default hoverBu" style="border: 1px solid #1E7CFB;color: #1E7CFB;display: none;" onclick="iterationTestCaseLayout()"><i class="glyphicon glyphicon-plus"></i>分配测试包</button>
		</div>
		
		<div class="tab-content" style="height: 450px;">  
				
			<div id="proBug" class="tab-pane fade in active">
			</div>
			
			<div id="proTask" class="tab-pane fade">
			</div>
			
			<div id="proTestcase" class="tab-pane fade">
			</div>
		</div>
    </div>
</div>

<!-- 新增/修改迭代模态窗 -->
<div id="newCreateIteration" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 550,
	footer:'#IterationFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<table class="form-table">
		<form id="addOrEditIterationForm" method="post">
			<input id="iterationId" name="dto.iterationList.iterationId" type="hidden"/>
			<input id="taskIds" name="dto.iterationList.taskId" type="hidden"/>
			<input id="taskIdCl" type="hidden"/>
			<tr>
				<th>迭代包名称<sup>*</sup>：</th>
	    		<td><input id="iteraName" class="exui-textbox" name="dto.iterationList.iterationBagName" data-options="required:true,validateOnCreate:false" style="width:330px"/></td>
	    	</tr>
	    	
	    	<tr>
	    	  <th>关联项目：</th>
	    	  <td>
	    	  	<!-- 原来 -->
				<!-- <input id="relationPro" class="exui-combobox" name="dto.iterationList.associationProject" placeholder="-请选择-" style="width:330px;"/> -->
				<!-- 改后 -->
				<select id="relationPro" name="dto.iterationList.associationProject"></select>
			  </td>
	    	</tr>
	    	
	    	<!-- <tr>
	    	  <th><sup>*</sup>项目缺陷：</th>
	    	  <td><input class="exui-combobox" name="dto.bugBaseInfo.bugDesc" data-options="required:true,validateOnCreate:false" style="width:500px"/></td>
	    	</tr>
	    	
	    	<tr>
	    	  <th><sup>*</sup>项目任务：</th>
	    	  <td><input class="exui-combobox" name="dto.otherMission.missionName" data-options="required:true,validateOnCreate:false" style="width:500px"/></td>
	    	</tr>
	    	
	    	<tr>
	    	  <th><sup>*</sup>测试包：</th>
	    	  <td><input class="exui-combobox" name="dto.testCasePackage.packageName" data-options="required:true,validateOnCreate:false" style="width:500px"/></td>
	    	</tr> -->
	    	
	    	<tr>
	    	  <th>开始时间<sup>*</sup>：</th>
	    	  <td>
	    	  	<input class="exui-datebox startTime" data-options="required:true,editable:false,panelWidth:200,validateOnCreate:false,prompt:'-请选择-'" name="dto.iterationList.startTime" style="width:330px"/>
	    	  </td>
	    	</tr>
	    	
	    	<tr>
	    	  <th>结束时间<sup>*</sup>：</th>
	    	  <td>
	    	  	<input class="exui-datebox endTime" data-options="required:true,editable:false,panelWidth:200,validateOnCreate:false,prompt:'-请选择-'" name="dto.iterationList.endTime" style="width:330px"/>
	    	  </td>
	    	</tr>
	    	
	    	
	    	
	    	<tr>
	    	  <th>备注：</th>
	    	  <td><input id="iterationNote" class="exui-textbox" name="dto.iterationList.note" data-options="validateOnCreate:false" style="width:330px"/></td>
	    	</tr>
	    </form>
	</table>
</div>
<div id="IterationFooter" align="center" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" style="margin-left: 380px;" onclick="saveOrEditIteration()">保存</a>
	<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="cancleItera()">取消</a>
</div>

<!-- 新增/修改单其他任务模态窗 -->
<div id="addOrEditWin_itera" class="exui-window" style="display:none;width:420px;" data-options="
	modal:true,
	width: 580,
	footer:'#addOrEditFooter_itera',
	minimizable:false,
	maximizable:false,
	collapsible:false,
	resizable:false,
	closed:true">
	<form id="addOrEditForm_itera" method="post">
		<table class="form-table">
		    <!--  隐藏字段，新增，修改提交时传到后台 -->
			<tr class="hidden">
				<td>
					<input id="missionId_itera" name="dto.otherMission.missionId"/>
					<!-- <input id="actualWorkload" name="dto.otherMission.actualWorkload"/>
					<input id="completionDegree" name="dto.otherMission.completionDegree"/>
					<input id="createTime" name="dto.otherMission.createTime"/>
					<input id="updateTime" name="dto.otherMission.updateTime"/>
					<input id="missionStatus" name="dto.otherMission.status"/>
					<input id="createUserId" name="dto.otherMission.createUserId"/> -->
				</td>
			</tr>
			<tr>
	    		<th><sup>*</sup>实际工作量(小时)：</th>
	    		<td><input class="exui-textbox" name="dto.otherMission.actualWorkload" data-options="required:true,validateOnCreate:false" style="width:180px"/><span style="margin-top:5px">(累计)</span></td>
	    	</tr>
	    	<tr>
	    		<th><sup>*</sup>进度(%)：</th>
	    		<td><input class="exui-textbox" name="dto.otherMission.completionDegree" data-options="required:true,validateOnCreate:false,validType:'lessThan',prompt:'请填写0到100的数字',onChange:function(newValue,oldValue){
	    		changeMissionStatus(newValue)}" style="width:180px"/></td>
	    	</tr>
	    	<tr>
				<th><sup>*</sup>任务状态：</th>
	    		<td><input name="dto.otherMission.status" class="exui-combobox sss" data-options="
	    			required:true,
	    			validateOnCreate:false,
	    			valueField:'value',
	    			textField:'desc',
	    			editable:false,
	    			prompt:'-请选择-',
	    			data:[{desc:'分配',value:'0'},{desc:'进行中',value:'1'},{desc:'完成',value:'2'},{desc:'终止',value:'3'},{desc:'暂停',value:'4'}]" style="width:180px"/></td>
	    	</tr>
	    	<tr class="stopZone" style="display:none;">
	    		<th><sup>*</sup>终止原因：</th>
	    		<td>
	    			<textarea class="stopReason" name="stopReason" style="width:180px;resize:none;"/></textarea>
	    			<br>
	    			<span style="color:red">设为终止，不得再修改状态。</span>
	    		</td>
	    	</tr>
	    	<tr class="zantingZone" style="display:none;">
	    		<th><sup>*</sup>暂停原因：</th>
	    		<td>
	    			<textarea class="zantingReason" name="zantingReason" style="width:180px;resize:none;"/></textarea>
	    		</td>
	    	</tr>
		</table>
	</form>
</div>
<div id="addOrEditFooter_itera" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submit11()">保存</a>
	<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="closeWin_iter()">取消</a>
</div>

<!--意见交流 -->
<div id="suggestion_Window" class="exui-window" style="display: none;padding: 20px 25px 25px;" data-options="
		modal:true,
		width: 680,
		minimizable:false,
		maximizable:false,
		closed:true">
		<ul class="nav nav-lattice">
			<li class="active" onclick="showSuggest_Info()"><a href="#examplesuggs" data-toggle="tab">意见交流记录</a></li>
			<li><a href="#baseInfosuggs" data-toggle="tab">基本信息</a></li>
		</ul>
		<div class="tab-content" style="margin-top:20px;height: 450px;"> 
			<div id="baseInfosuggs" class="tab-pane fade">
				<form id="suggesInfoForms" method="post" class="form-horizontal">
					
					<table id="second_Tables">
						<tr>
							<td style="padding-left:27px;">
								<span class="fontCs">模块:</span>
								<input class="exui-textbox" name="dto.moduleName" style="width:200px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">当前状态:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="dto.stateName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">作者:</span>
								<input class="exui-textbox" name="authorName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">测试人员:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="testName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs">开发人员:</span>
								<input class="exui-textbox" name="devName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">bug描述:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugDesc" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">类型:</span>
								<input class="exui-textbox" name="dto.bug.bugType.typeName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">等级:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugGradeName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs">再现平台:</span>
								<input class="exui-textbox" name="pltfomName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">来源:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="sourceName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">时机:</span>
								<input class="exui-textbox" name="occaName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">引入原因:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="geneCaseName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td> 
								<span class="fontCs" style="margin-left: 13px;">优先级:</span>
								<input class="exui-textbox" name="priName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">频率:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugFreqName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td colspan="2">
								<span class="fontCs">再现步骤:</span>
								<input class="exui-textbox" name="dto.bug.reProStep" style="width:525px;margin-left: 7px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td class="fontCs" colspan="2">
								<div style="margin-top: 20px;margin-left: 7px;">
								<p>
									<span>发现日期:</span>
									<span id="reptDate_sugg"></span>
									
									<span style="margin-left: 77px;">发现版本:</span>
									<span id='bugReptVersion_sugg'></span>
									
									<span style="margin-left: 77px;">关闭版本:</span>
									<span id="currVer_sugg"></span>
								</p>
								</div>
								
							</td>
						</tr>
					
					</table>
				</form>
			</div>
			
			<div id="examplesuggs" class="tab-pane fade in active">
				<div class="buttonPos" style="padding-bottom: 10px;">
					<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="express_Opinion()"><i class="glyphicon glyphicon-plus"></i>发表意见</button>
					<button type="button" class="btn btn-default" id="closeSuggWin" style="border: 1px solid #1E7CFB;color: #1E7CFB;"><i class="glyphicon glyphicon-off"></i>关闭</button>
				</div>
				
			</div>
			
			<table id="express_Opinion" data-options="
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
	
<!--交流意见模态窗 -->
	<div id="express_OpinionNew" title="" class="exui-window" data-options="
		modal:true,
		width: 600,
		minimizable:false,
		maximizable:false,
		closed:true">
			<form id="express_Form" enctype="multipart/form-data" method="post">
				<table class="form-table" style="font-size: 14px;">
				<input id="bugId_Ex" name="dto.shortMsg.bugId" type="hidden"/>
				<tr>
					<td class="left_td"><sup>*</sup>我的意见:</td>
					<td >
	   			      <input name="dto.shortMsg.message" class="exui-textbox" style="width: 460px;height: 160px;" data-options="multiline:true,required:true,prompt:''" />
   			      	</td>
				</tr> 
				<tr>
					<td class="left_td"><sup>*</sup>发送给:</td>
					<td>
						<input id="hghghghh" class="exui-combobox" name="dto.shortMsg.recipCd" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			valueField:'value',
		    			textField:'desc',
		    			prompt:'-请选择-',
		    			data:[{desc:'项目所有成员',value:10},{desc:'测试人员',value:1},{desc:'分析人员',value:3},{desc:'分配人员',value:4},{desc:'开发人员',value:5},{desc:'开发负责人',value:7}]" style="width:230px;"/>
					</td>
				</tr>
				</table>
		    </form>
		<div class="buttonSt" style="margin-left: 60%;margin-top: 20px;">
			<button type="button" onclick="addExpress_Submit()" class="btn btn-primary">确认</button>
			<button onclick="addExpress_Reset()" type="button" style="border: 1px solid #1E7CFB;color: #1E7CFB;" class="btn btn-default">重置</button>
			<button type="button" onclick="canlce_ExpressWin()" style="border: 1px solid #1E7CFB;color: #1E7CFB;" class="btn btn-default">取消</button>
		</div>
	</div>
	
	<!-- 查看详情模态窗 -->
<div id="detail_Win" class="exui-window" data-options="
	modal:true,
	minimizable:false,
	maximizable:false,
	collapsible:false,
	resizable:false,
	closed:true">
	<ul class="nav nav-lattice" style="display:none">
		<li class="active"><a href="#xiang_qing" data-toggle="tab">任务详情</a></li>
		<li><a href="#ri_zhi" data-toggle="tab">任务日志</a></li>
	</ul>
	
	<!-- <button id="detailClose" onclick="close_DetailWin()" class="btn btn-default" style="margin: 20px 0 20px 0;border: 1px solid #1e7cfb;color: #1e7cfb;"><i class="glyphicon glyphicon-off"></i>关闭</button> -->
	
	<div class="tab-content" style="height: 400px;width:600px;">
		<div id="xiang_qing" class="tab-pane fade in active">
			<table id="detail_Table">
				
			</table>
		</div>
		<div id="ri_zhi" class="tab-pane fade">
			<!-- <table id="logDg" data-options="
				fitColumns: true,
				singleSelect: true,
				pagination: true,
				pageNumber: 1,
				pageSize: 10,
				pageList:[10,30,50],
				layout:['list','first','prev','manual','next','last','refresh','info']
			"></table> -->
		</div>
	</div>
	<footer style="padding:5px;text-align:right">
		<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="close_DetailWin()" style="border: 1px solid #1e7cfb;color: #1e7cfb;"><i class="glyphicon glyphicon-off"></i>关闭</a>
	</footer>
</div>

	<!--关联用例 -->
	<div id="lookBug_Windown" class="exui-window" style="display: none;padding: 20px 25px 25px;" data-options="
		modal:true,
		width: 680,
		footer:'#testFlow_Footer',
		minimizable:false,
		maximizable:false,
		closed:true">
		<ul class="nav nav-lattice">
			<li class="active" onclick="showExample_Info()"><a href="#example_" data-toggle="tab">用例信息</a></li>
			<li onclick="showBase_Info()"><a href="#base_Info" data-toggle="tab">基本信息</a></li>
		</ul>
		<div class="tab-content" style="margin-top:20px;height: 450px;"> 
			<div id="base_Info" class="tab-pane fade">
				<form id="baseInfoForm" method="post" class="form-horizontal">
					
					<table id="second_Table">
						<tr>
							<td>
								<span class="fontCs">测试需求:</span>
								<input class="exui-textbox" name="dto.moduleName" style="width:200px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">当前状态:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="dto.stateName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">作者:</span>
								<input class="exui-textbox" name="authorName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">测试人员:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="testName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs">开发人员:</span>
								<input class="exui-textbox" name="devName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">bug描述:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugDesc" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">类型:</span>
								<input class="exui-textbox" name="dto.bug.bugType.typeName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">等级:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugGradeName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs">再现平台:</span>
								<input class="exui-textbox" name="pltfomName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">来源:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="sourceName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">时机:</span>
								<input class="exui-textbox" name="occaName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">引入原因:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="geneCaseName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td> 
								<span class="fontCs" style="margin-left: 13px;">优先级:</span>
								<input class="exui-textbox" name="priName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">频率:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugFreqName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td colspan="2">
								<span class="fontCs">再现步骤:</span>
								<input class="exui-textbox" name="dto.bug.reProStep" style="width:525px;margin-left: 7px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td class="fontCs" colspan="2">
								<div class="first" style="padding-top: 20px;">
								<p>
									<span>发现日期:</span>
									<span id="reptDate"></span>
									
									<span style="margin-left: 40px;">发现版本:</span>
									<span id='bugReptVersion'></span>
									
									<span style="margin-left: 40px;">关闭版本:</span>
									<span id="currVer"></span>
								</p>
								</div>
								
							</td>
						</tr>
					
					</table>
				</form>
			</div>
			
			<div id="example_" class="tab-pane fade in active">
				<div class="buttonPos" style="padding-bottom: 10px;">
					<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="newAdd_Example()"><i class="glyphicon glyphicon-plus"></i>新增用例</button>
					<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="rela_Example()"><i class="glyphicon glyphicon-retweet"></i>关联用例</button>
				</div>
				
			</div>
			
			<table id="example_ListInfo" data-options="
						fitColumns: true,
						rownumbers: true,
						pagination: true,
						pageNumber: 1,
						pageSize: 10,
						layout:['list','first','prev','manual','next','last','refresh','info']">
		 	</table>
	  </div>
	</div>
	
	<!--增加用例模态窗 -->
	<!-- <div id="add_Example" title="" class="exui-window" data-options="
		modal:true,
		width: 600,
		minimizable:false,
		maximizable:false,
		closed:true">
		<table class="form-table" style="font-size: 14px;">
			<form id="lookForm" enctype="multipart/form-data" method="post">
				<tr class="hidden"><td>
					<input type="hidden" name="mdPath" value="" />
					<input itype="hidden" name="dto.testCaseInfo.taskId"/>
					<input id="moduleIds" type="hidden" name="dto.testCaseInfo.moduleId"/>
					<input type="hidden" name="dto.testCaseInfo.createrId"/>
					<input type="hidden" name="dto.testCaseInfo.testCaseId"/>
					<input type="hidden" name="dto.testCaseInfo.isReleased"/>
					<input type="hidden" name="dto.testCaseInfo.creatdate"/>
					<input type="hidden" name="dto.testCaseInfo.attachUrl"/>
					<input type="hidden" name="dto.testCaseInfo.auditId"/>
					<input type="hidden" name="dto.testCaseInfo.testStatus"/>
					<input type="hidden" name="dto.testCaseInfo.testData"/>
					<input type="hidden" name="dto.testCaseInfo.moduleNum"/>
				</td></tr>
				
				<tr>
					<td class="left_td"><sup>*</sup>类别:</td>
					<td>
						<input class="exui-combobox" name="dto.testCaseInfo.caseTypeId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			valueField:'value',
		    			textField:'desc',
		    			prompt:'-请选择-',
		    			data:[{desc:'体验',value:124},{desc:'功能',value:10},{desc:'可用性',value:12},{desc:'场景',value:74},{desc:'性能',value:11}]" style="width:180px;"/>
					</td>
					
					<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>优先级:</td>
					<td>
						<input class="exui-combobox" name="dto.testCaseInfo.priId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			valueField:'value',
		    			textField:'desc',
		    			prompt:'-请选择-',
		    			data:[{desc:'高',value:7},{desc:'中',value:8},{desc:'低',value:9}]" style="width:180px;"/>
					</td>
					
				</tr>
				
				<tr>
				    <td class="left_td"> &nbsp;&nbsp;<sup>*</sup>执行成本: </td>
					<td colspan="3" >
						<input name="dto.testCaseInfo.weight"  class="exui-numberbox" data-options="min:1,max:10,required:true,prompt:'一个成本单位代表5分钟'" style="width: 100%;">
					</td>
				</tr>
				
				<tr>
					<td class="left_td"><sup>*</sup>用例描述:</td>
					<td class="dataM_left" colspan="3">
						<input name="dto.testCaseInfo.testCaseDes" class="exui-textbox" data-options="required:true" style="width: 100%;">
					</td>
				</tr>
				
				<tr>
					<td class="left_td"><sup>*</sup>过程及数据:</td>
					<td colspan="3" >
						<textarea name="dto.testCaseInfo.operDataRichText" id="operDataRichText"
							cols="50" rows="15" style="width: 100%;">
	   			      </textarea>
	   			      <input name="dto.testCaseInfo.operDataRichText" class="exui-textbox" style="width: 100%;height: 160px;" data-options="multiline:true,required:true,prompt:'请填写过程及数据'" />
   			      	</td>
				</tr> 
				
				<tr style="margin-top: 5px;">
					<td  class="left_td"><sup>*</sup>期望结果:</td>
					<td class="dataM_left" colspan="3">
						<input name="dto.testCaseInfo.expResult" class="exui-textbox" style="width: 100%;height: 160px;" data-options="multiline:true,required:true,prompt:'请填写期望结果'" />
   			      </td>
				</tr>
				
				<tr>
					<td  class="left_td">附件/插图片：</td>
		    		<td colspan="3"><input class="exui-filebox" name="dto.testTaskManage.testPhase" style="width:250px"/></td>
				</tr>
				
		    </form>
		</table>
		
		<form id="uploadForm" enctype="multipart/form-data" method="post">
			<table>
				<tr>
					<td  class="left_td">附件/插图片：</td>
		    		<td><input class="exui-filebox" name="dto.testTaskManage.testPhase" style="width:250px"/></td>
				</tr>
			</table>
		</form>
		<div class="buttonSt" style="margin-left: 60%;margin-top: 20px;"> 
			<button type="button" onclick="addExample_Submit()" class="btn btn-primary">确认</button>
			<button id="resetting" onclick="addExample_Reset()" type="button" style="border: 1px solid #1E7CFB;color: #1E7CFB;" class="btn btn-default">重置</button>
			<button type="button" onclick="canlce_Win()" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;">取消</button>
		</div>
	</div> -->

<!-- 迭代报告弹窗 -->
<!-- 新增/修改单其他任务模态窗 -->
<div id="detailWin1" class="exui-window" style="display:none;width:700px;" data-options="
	modal:true,
	width: 580,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true">
	
	<div style="width:100%;height:400px">
		<div style="float:left;width:100%;padding:5px;border:#FFFFFF 1px solid;border-radius:2px;background-color:#FFFFFF;height:278px;">
			<div style="width:100%;padding:5px 10px">
				<p style="padding-left:10px;border-left:3px red solid;margin-bottom: 15px;font-weight: bold;">用例摘要</p>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="11" style="color:#1C86EE;font-size:2em">0</span><br/>总用例数
				</div>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="22" style="color:green;font-size:2em">0</span><br/>通过数
				</div>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="33" style="color:red;font-size:2em">0</span><br/>未通过数
				</div>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="44" style="color:#8B8378;font-size:2em">0</span><br/>阻塞数
				</div>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="55" style="color:#8B8378;font-size:2em">0</span><br/>未测试数
				</div>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="66" style="color:#8B8378;font-size:2em">0</span><br/>不适用数
				</div>
				<!-- <div style="width:12.5%;float:left;text-align:center">
					<span id="77" style="color:#8B8378;font-size:2em"></span><br/>待修正数
				</div>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="88" style="color:#8B8378;font-size:2em"></span><br/>待审核数
				</div> -->
				<br/>
				<p style="width:100%;border-top:1px #EAEAEA solid;margin-top: 70px;"></p>
				<p style="padding-left:10px;border-left:3px red solid;margin-bottom: 15px;font-weight: bold;">缺陷摘要</p>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="aa" style="color:#1C86EE;font-size:2em">0</span><br/>总BUG数
				</div>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="bb" style="color:green;font-size:2em">0</span><br/>有效BUG数
				</div>
				<div style="width:18%;float:left;text-align:center">
					<span id="cc" style="color:#8B8378;font-size:2em">0</span><br/>已关闭BUG数
				</div>
				<div style="width:15%;float:left;text-align:center">
					<span id="dd" style="color:#8B8378;font-size:2em">0</span><br/>待处理BUG数
				</div>
				<div style="width:15%;float:left;text-align:center">
					<span id="fixCount" style="color:#8B8378;font-size:2em">0</span><br/>已改未确认
				</div>
				<div style="width:15%;float:left;text-align:center">
					<span id="noBugCount" style="color:#8B8378;font-size:2em">0</span><br/>非错未确认
				</div>
				
				<p style="width:100%;border-top:1px #EAEAEA solid;margin-top: 100px;"></p>
				<p style="padding-left:10px;border-left:3px red solid;margin-bottom: 15px;font-weight: bold;">任务摘要</p>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="allMissions" style="color:#1C86EE;font-size:2em">0</span><br/>总任务数
				</div>
				<div style="width:12.5%;float:left;text-align:center">
					<span id="attributionMissions" style="color:#cdc1c5;font-size:2em">0</span><br/>分配
				</div>
				<div style="width:18%;float:left;text-align:center">
					<span id="runingMissions" style="color:#aaaaf6;font-size:2em">0</span><br/>进行中
				</div>
				<div style="width:15%;float:left;text-align:center">
					<span id="finishMissions" style="color:#a7daa7;font-size:2em">0</span><br/>完成
				</div>
				<div style="width:15%;float:left;text-align:center">
					<span id="terminationMissions" style="color:#f0bb7d;font-size:2em">0</span><br/>终止
				</div>
				<div style="width:15%;float:left;text-align:center">
					<span id="stopMissions" style="color:#ff82ab;font-size:2em">0</span><br/>暂停
				</div>
			</div>
		</div>
	</div>
</div>

<table id="bug_List" class="exui-datagrid" title="" style="width:100%;height: auto;" data-options=""></table>
	
<div id="module_IdH" style="display: none;"></div>
<div id="bugRept_Vers" style="display: none;"></div>
<div id="bug_Ids" style="display: none;"></div>
<input id="mission_id" type="hidden"/>
<input id="packageIds" type="hidden"/>
<input id="bugIds" type="hidden"/>
	<%-- <script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/fileinput.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/plugins/sortable.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/locales/zh.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/themes/explorer-fa/theme.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/themes/fa/theme.js" type="text/javascript"></script> --%>
<script src="<%=request.getContextPath()%>/itest/js/iteration/iteration.js" type="text/javascript" charset="utf-8"></script>