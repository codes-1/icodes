<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/outlineManager/testRequirementManager.css"/>
<div class="exui-layout"  style="width:100%; height: 100%;">
	<div id="testRequirementTreeDiv" data-options="region:'west'" title="需求分解树" style="width:20%;padding:5px;">
				<ul id="testRequirementTree" class="exui-tree"></ul>
	</div>
	<div data-options="region:'center'" style="width: 80%; height: 100%; overflow: hidden;">
		<div class="tools">
			<input type="hidden" id="isCommit" name="dto.isCommit" value="${dto.isCommit}"/>
			<button type="button" class="btn btn-default" id="disabledBtn" style="display: none; border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="enableAndDisabledTestRequirement()" schkUrl="outLineAction!switchState"><i class="glyphicon glyphicon-minus"></i>停用</button>
			<button type="button" class="btn btn-default" id="enableBtn" style="display: none; border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="enableAndDisabledTestRequirement()" schkUrl="outLineAction!switchState"><i class="glyphicon glyphicon-plus"></i>启用</button>
			<button type="button" class="btn btn-default" id="submitTestBtn" style="display: none; border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="submitTestRequirement()" schkUrl="outLineAction!submitModule"><i class="glyphicon glyphicon-ok"></i>提交测试</button>
			<button type="button" class="btn btn-default" id="addTestRequirementBtn" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showAddTestRequirementWin()" schkUrl="outLineAction!addNodes"><i class="glyphicon glyphicon-plus"></i>增加测试需求</button>
			<button type="button" class="btn btn-default" id="assignPeopleBtn" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showSeletctPeopleWin()" schkUrl="outLineAction!assignPeople"><i class="glyphicon glyphicon-user"></i>人员分配</button>
			<!-- <button type="button" class="btn btn-default" onclick="initSelectTestRequirementTab()"><i class="glyphicon glyphicon-pencil"></i>切换项目</button> -->
		</div>
		<table id="testRequirementTab" data-options="
			fitColumns: true,
			rownumbers: true,
			singleSelect: false,
			pagination: true,
			pageNumber: 1,
			pageSize: 10,
			layout:['list','first','prev','manual','next','last','refresh','info']">
		</table>
	</div>
</div>

<!-- 选择测试项目 -->
<!-- <div id="selectTestRequirementDiv" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 980,
	minimizable:false,
	maximizable:false,
	closed:true">
	<div class="tab-content" style="margin-top:20px;height: 450px;">
		<table id="selectTestRequirementTab" data-options="
			fitColumns: true,
			rownumbers: true,
			singleSelect: true,
			pagination: true,
			pageNumber: 1,
			pageSize: 10,
			layout:['list','first','prev','manual','next','last','refresh','info']
		"></table>
	</div>
</div> -->

<div id="addTestRequirementFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="addTestRequirementNode('closed')">确定</a>
	<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style="border: 1px solid #1e7cfb; color: #1e7cfb;" onclick="addTestRequirementNode('continue')">确定并继续</a>
	<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style="border: 1px solid #1e7cfb; color: #1e7cfb;" onclick="javascript:$('#addTestRequirementDiv').xwindow('close');">返回</a>
</div>

<div id="testRequirementDistrDiv" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 580,
	height: 450,
	minimizable:false,
	maximizable:false,
	closed:true">

	<div class="tab-content">
		<div id="dlg-toolbar">
			<form action="#" method="post" id="assignForm" name="assignForm">
				<input type="hidden" id="assignNIds" name="dto.assignNIds"/>
				<input type="hidden" id="userIds" name="dto.userIds"/>
				<input type="hidden" id="parentIdes" name="dto.parentIdes"/>
				<input type="hidden" id="assignType" name="dto.reqType" value="5"/>
				<table style="width:100%;">
					<tr>
						<td style="width: 50%;">
							<label>分配：</label>
							<input type="radio" checked="checked" id="dev" name="selectPeople" onclick="javascript:$('#assignType').val('5');initAssignUser();"/><label style="margin-left: 10px;">开发人员</label>
							<input type="radio" id="allo" name="selectPeople" onclick="javascript:$('#assignType').val('4');initAssignUser();"/><label style="margin-left: 10px;">分配人员</label>
						</td>
						<td style="width: 50%; text-align:right">
							<a href="#" class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style="border: 1px solid #1e7cfb; color: #1e7cfb;" onclick="initAssignUser();">刷新备选人员</a>
							<a href="#" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submitSelectedPeople();">确定</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div style="width: 50%; float: left; padding-top: 10px;">
			<table id="selectPepoleTab" data-options="
				fitColumns: true,
				singleSelect: true,
				pagination: false,
				layout:['list','first','prev','manual','next','last','refresh','info']
				"></table>		
		</div>
		<div style="width: 50%; float: left; padding-top: 10px;">
			<table id="selectedPepoleTab" data-options="
				fitColumns: true,
				singleSelect: true,
				pagination: false,
				layout:['list','first','prev','manual','next','last','refresh','info']
				"></table>
		</div>
	</div>

</div>
<!-- 增加测试需求 -->
<div id="addTestRequirementDiv" class="exui-window" style="display: none;" data-options="
	modal:true,
	width: 680,
	footer:'#addTestRequirementFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<div class="tab-content" style="height: 450px;">
		<div class="addTestRequirementDescDiv">
			<span>一次性可批量增加1到10个需求项</span>
		</div>
		<div class="addTestRequirementFormDiv">
			<form action="#" method="post" id="createForm" name="createForm">
				<input type="hidden" id="taskId" name="dto.taskId"/>
				<input type="hidden" id="currNodeId" name="dto.currNodeId"/>
				<input type="hidden" id="command" name="dto.command" value="addchild"/>
				<input type="hidden" id="parentNodeId" name="dto.parentNodeId"/>
				<input type="hidden" id="currLevel" name="dto.currLevel"/>
				<input type="hidden" id="requireType" name="dto.reqType" value="0"/>
				<input type="hidden" id="moduleState" name="dto.moduleState"/>
				<table class="addTestRequirementNameTab" id="testRequirementCreateTab">
					<tr>
						<th style="width:40%">
							<input type="radio" checked="checked" id="requirementFunc" name="requirClass" value="func" onclick="javascript:$('#requireType').val('0');adjustCreTable('fun');"/><label style="margin-left: 10px;">功能</label>
							<input type="radio" id="requirementPref" name="requirClass" value="pref" onclick="javascript:$('#requireType').val('1');adjustCreTable('perf');"/><label style="margin-left: 10px;">性能</label>
							<label>&nbsp;&nbsp;&nbsp;测试需求项</label>
						</th>
						<th style="border-left: 1px solid white;text-align: center; width:15%;">难度系数</th>
						<th style="border-left: 1px solid white;text-align: center; width:15%;">预估用例数</th>
						<th style="display:none; border-left: 1px solid white;text-align: center; width:15%;">预估脚本数</th>
						<th style="display:none; border-left: 1px solid white;text-align: center; width:15%;">预估场景数</th>
					</tr>
					<tr>
						<td style="width:40%; padding-right: 0px;">
							<input type="text" name="dto.moduleData[0]" id="module1"/><!--  class="exui-textbox" data-options="required:true" -->
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[0]" id="module1_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[0]" id="module1_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[0]" id="module1_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[0]" id="module1_4"/>
						</td>
					</tr>
					<tr>
						<td style="width:15%;">
							<input type="text" name="dto.moduleData[1]" id="module2"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[1]" id="module2_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[1]" id="module2_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[1]" id="module2_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[1]" id="module2_4"/>
						</td>
					</tr>
					<tr>
						<td style="width:40%;">
							<input type="text" name="dto.moduleData[2]" id="module3"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[2]" id="module3_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[2]" id="module3_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[2]" id="module3_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[2]" id="module3_4"/>
						</td>
					</tr>
					<tr>
						<td style="width:40%;">
							<input type="text" name="dto.moduleData[3]" id="module4"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[3]" id="module4_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[3]" id="module4_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[3]" id="module4_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[3]" id="module4_4"/>
						</td>
					</tr>
					<tr>
						<td style="width:40%;">
							<input type="text" name="dto.moduleData[4]" id="module5"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[4]" id="module5_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[4]" id="module5_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[4]" id="module5_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[4]" id="module5_4"/>
						</td>
					</tr>
					<tr>
						<td style="width:40%;">
							<input type="text" name="dto.moduleData[5]" id="module6"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[5]" id="module6_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[5]" id="module6_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[5]" id="module6_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[5]" id="module6_4"/>
						</td>
					</tr>
					<tr>
						<td style="width:40%;">
							<input type="text" name="dto.moduleData[6]" id="module7"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[6]" id="module7_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[6]" id="module7_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[6]" id="module7_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[6]" id="module7_4"/>
						</td>
					</tr>
					<tr>
						<td style="width:40%;">
							<input type="text" name="dto.moduleData[7]" id="module8"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[7]" id="module8_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[7]" id="module8_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[7]" id="module8_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[7]" id="module8_4"/>
						</td>
					</tr>
					<tr>
						<td style="width:40%;">
							<input type="text" name="dto.moduleData[8]" id="module9"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[8]" id="module9_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[8]" id="module9_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[8]" id="module9_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[8]" id="module9_4"/>
						</td>
					</tr>
					<tr>
						<td style="width:40%;">
							<input type="text" name="dto.moduleData[9]" id="module10"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.quotiety[9]" id="module10_1" style="text-align: center;" value="1"/>
						</td>
						<td style="width:15%; padding-left: 1px;">
							<input type="text" name="dto.caseCount[9]" id="module10_2" style="text-align: center;" value="3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.scrpCount[9]" id="module10_3"/>
						</td>
						<td style="display:none; width:15%; padding-left: 1px;">
							<input type="text" name="dto.sceneCount[9]" id="module10_4"/>
						</td>
					</tr>
				</table>
				<table class="form-table">
					<tr>
						<td>
							<input type="radio" checked="checked" name="requir" id="addchildRd" value="addchild" onclick="javascript:$('#command').val('addchild');"/><label style="margin-left: 10px;">子需求</label>
							<input type="radio" name="requir" id="addBroRd" value="addBro" onclick="javascript:$('#command').val('addBro');"/><label style="margin-left: 10px;">同级需求</label><!-- checkIsParent(); -->
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</div>

<script src="<%=request.getContextPath()%>/itest/js/outlineManager/testRequirementManager.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/itest/js/outlineManager/MapUtil.js" type="text/javascript" charset="utf-8"></script>