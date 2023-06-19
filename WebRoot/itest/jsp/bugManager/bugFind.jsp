<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	
	<input id="containerList" type="hidden" val=""/>
    <!-- 查询模态窗 -->
	<!-- <div id="queryBugWin" title="" class="exui-window" data-options="
		modal:true,
		width: 1020,
		footer:'#queryBugFoot',
		minimizable:false,
		maximizable:false,
		closed:true"> -->
		<form id="bugQueryForm_" method="post">
			<table class="form-table" style="width: 100%;display: inherit;">
				<tbody style="width: 100%;display: table-row;">
					<!-- <tr style="display: none;">
						<td class="left_td">测试项目:</td>
						<td class="" colspan="7">
							<input id="taskIdF" name="dto.bug.taskId"  type="hidden">
							<input id="taskNameF" name="taskName" class="exui-textbox" data-options="readonly:true,prompt:'请选择测试项目'" style="width: 100%;">
						</td>
					</tr> -->
					<tr >
						<!-- <td class="left_td">测试需求:</td>
						<td class="" colspan="4">
							<input id="moduleIdF" name="dto.bug.moduleId"  type="hidden">
							<input id="moduleNameF" name="moduleNameF" class="exui-textbox" data-options="readonly:true,prompt:'请选择测试需求'" style="width: 100%;">
						</td>
						<td >
							<a id="clearModuleBnt" class="exui-linkbutton" style="border-radius: 20px;margin-left: 10px; display:none;" data-option="size:'small'" onclick="clearModule();">清空</a>
						</td> -->
						<td class="left_td">流转到我: </td>
						<td colspan="" align="left" style="text-align: left;">
							<input id="defBugId" name ="dto.defBug" type="checkbox" value="1" checked="true"/>	
						</td>
					</tr>
					<tr >
						<td class="left_td">描述/再现步骤:</td>
						<td class="" colspan="7">
							<input id="bugDescF" name="dto.bug.bugDesc" class="exui-textbox" data-options="" style="width: 100%;">
						</td>
					</tr>
					
					<tr >
						<td class="left_td">发现日期起:</td>
						<td >
							<input id="reptDateF" name="dto.bug.reptDate" style="width:140px;" class="exui-datebox" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			prompt:'-请选择-'"  style="width:140px;" />
						</td>
						<td class="left_td">发现日期止:</td>
						<td >
							<input id="reptDateFend" name="dto.reptDateEnd" class="exui-datebox" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			prompt:'-请选择-'" style="width:140px;"/>
						</td>
						<td class="left_td">发现版本: </td>
						<td style="text-align: right;">
							<input id="bugReptVerF" class="exui-combobox" name="dto.bug.bugReptVer" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" style="width:140px;"/>
			    			<input id="bugReptVerNameF" name="bugReptVerNameF" class="exui-textbox" type="hidden" >
						</td>
					</tr>
					
					<tr >
						<td class="left_td">状态:</td>
						<td >
							<input id="currStateIdF" style="width:140px;" class="exui-combobox" name="dto.stateIds" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			multiple:true,
			    			valueField:'keyObj',
			    			textField:'valueObj',
			    			prompt:'-请选择-'" />
						</td>
						<td class="left_td">测试所属:</td>
						<td >
							<input id="testOwnerIdF" class="exui-textbox" name="dto.bug.testOwnerId" type="hidden" data-options="" />
							<input id="testOwnNameF" name="testOwnNameF" class="exui-textbox"data-options="
							validateOnCreate:false,
			    			editable:false,
			    			prompt:'-请选择-'" style="width:140px;">
						</td>
						<td class="left_td" style="padding-left: 0.5em;">开发所属:</td>
						<td >
							<input id="devOwnerIdF" class="exui-textbox" name="dto.bug.devOwnerId" type="hidden"  data-options=" " />
							<input id="devOwnerNameF" name="devOwnerNameF" class="exui-textbox" data-options="
							validateOnCreate:false,
			    			editable:false,
			    			prompt:'-请选择-'" style="width:140px;" >
						</td>
						<td class="left_td" style="padding-left: 0.5em;">待处理人:</td>
						<td >
							<input id="nextOwnerIdF" class="exui-textbox" name="dto.bug.nextOwnerId" type="hidden"  data-options="" />
							<input id="nextOwnerIdFName" name="nextOwnerIdFName" class="exui-textbox" data-options="validateOnCreate:false,
			    			editable:false,
			    			prompt:'-请选择-'" style="width:140px;">
						</td>
					</tr>
					
					<tr >
						<td class="left_td">类型:</td>
						<td >
							<input id="bugTypeIdF" style="width:140px;" class="exui-combobox" name="dto.bug.bugTypeId" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" />
							<input id="bugTypeNameF" name="bugTypeNameF" class="exui-textbox" type="hidden" data-options="" >
						</td>
						<td class="left_td">等级:</td>
						<td >
							<input id="bugGradeIdF" class="exui-combobox" name="dto.bug.bugGradeId" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" style="width:140px;"/>
							<input id="bugGradeNameF" name="bugGradeNameF" class="exui-textbox" type="hidden" data-options="" >
						</td>
						<td class="left_td" style="padding-left: 0.5em;">平台:</td>
						<td >
							<input id="platformIdF" class="exui-combobox" name="dto.bug.platformId" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" style="width:140px;"/>
							<input id="pltfomNameF" name="pltfomNameF" class="exui-textbox" type="hidden" data-options="" >
						</td>
						<td class="left_td" style="padding-left: 0.5em;">来源:</td>
						<td >
							<input id="sourceIdF" class="exui-combobox" name="dto.bug.sourceId" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" style="width:140px;"/>
							<input id="sourceNameF" name="sourceNameF" class="exui-textbox" type="hidden" data-options="" >
						</td>
					</tr>
					<tr >
						<td class="left_td">发现时机:</td>
						<td >
							<input id="bugOccaIdF" class="exui-combobox" name="dto.bug.bugOccaId" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" style="width:140px;"/>
							<input id="occaNameF" name="occaNameF" class="exui-textbox" type="hidden" data-options="" >
						</td>
						<td class="left_td" style="padding-left: 0.5em;">测试时机:</td>
						<td>
							<input id="geneCauseIdF" class="exui-combobox" name="dto.bug.geneCauseId" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" style="width:140px;"/>
							<input id="geneCaseNameF" name="geneCaseNameF" class="exui-textbox" type="hidden" >
						</td>
						<td class="left_td" style="padding-left: 0.5em;">优先级:</td>
						<td >
							<input id="priIdF" class="exui-combobox" name="dto.bug.priId" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" style="width:140px;"/>
							<input id="priNameF" name="priNameF" class="exui-textbox" type="hidden" data-options="" >
						</td>
						<td class="left_td" style="padding-left: 0.5em;">频率:</td>
						<td >
							<input id="bugFreqIdF" class="exui-combobox" name="dto.bug.bugFreqId" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" style="width:140px;"/>
							<input id="bugFreqNameF" name="bugFreqNameF" class="exui-textbox" type="hidden" data-options="" >
						</td>
					</tr>
					<tr >
						<td class="left_td" >引入原因:</td>
						<td >
							<input id="genePhaseIdF" class="exui-combobox" name="dto.bug.genePhaseId" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'id',
			    			textField:'text',
			    			prompt:'-请选择-'" style="width:140px;"/>
			    			<input id="genPhNameF" name="genPhNameF" class="exui-textbox" type="hidden" data-options="" >
						</td>
						<td class="left_td" >关联用例:</td>
						<td >
							<input id="relaCaseFlagF" class="exui-combobox" name="dto.bug.relaCaseFlag" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'value',
			    			textField:'desc',
			    			prompt:'-请选择-',
			    			data:[{desc:'',value:''},{desc:'已关联',value:1},{desc:'未关联',value:28}]" style="width:140px;"/>
						</td>
						<td class="left_td" >责任人:</td>
						<td >
							<input id="chargeOwnerIdF" class="exui-textbox" name="dto.bug.chargeOwner" type="hidden" data-options=""/>
			    			<input id="chargeOwnerNameF" name="chargeOwnerNameF" class="exui-textbox" style="width:140px;" data-options="
			    			validateOnCreate:false,
			    			editable:false,
			    			prompt:'-请选择-'" >
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	<!-- </div> -->
	<div id="queryBugFoot" align="right" style="padding-top: 0.5em;border-top: 1px solid #ccc;">
			<a id="queryBnt" href="#" class="exui-linkbutton" data-options="toggle:true,group:'g1',btnCls:'primary',size:'xs'" onclick="queryBugSubmit()">查询</a>
			<a id="clearBnt" href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="clearBugQueryForm()">清空</a>
			<a href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="closeBugQueryWin()">返回</a>
	</div>
	
	<div id="findChooseUserWin" class="exui-window" style="display:none; " data-options="
	modal:true,
	width: 500,
	minimizable:false,
	maximizable:false,
	closed:true">
	<div class="tools" data-options="" style="margin-bottom: 0;">
		<div class="input-field" style="width: 145px;">
			<input id="findUserGroupList" class="exui-combobox" name="dto.group" 
			data-options="
   			validateOnCreate:false,
   			editable:false,
   			prompt:'-选择组-' " style="width:145px;position: static;"/>
   			<input id="findUserGroupId" type="hidden">
		</div>
	    <div class="input-field" style="width: 145px;"> 
		<input class="form-control" id="findChooseUserName" placeholder="请输入组员姓名"/>
		</div>
		<button type="button" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" style="padding: 4px 8px;" onclick="findSearchUsers()"><i class="glyphicon glyphicon-search"></i>查询</button>
		<button type="button" class="btn btn-default bntcss" onclick="clearFindSearchUsers()"><i class="glyphicon glyphicon-clear"></i>重置</button>
    </div>
    
    <table id="findUserAll" data-options="
	    height: 400,
		fitColumns: true,
		rownumbers: true,
		singleSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 100,
		layout:['list','first','prev','manual', 'sep','next','last','refresh','info']">
	</table>
    
</div>
	
<script src="<%=request.getContextPath()%>/itest/js/bugManager/bugFind.js" type="text/javascript" charset="utf-8"></script>
	