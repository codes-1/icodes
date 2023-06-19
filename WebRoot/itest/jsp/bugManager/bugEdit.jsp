<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<!-- <div id="editBugWindown" class="exui-window" style="display: none;padding: 20px 25px 25px;" data-options="
		modal:true,
		width: 900,
		footer:'',
		minimizable:false,
		maximizable:false,
		closed:true"> -->
		<ul class="nav nav-lattice">
			<li class="active"><a href="#bugInfo" data-toggle="tab">基本信息</a></li>
			<li onclick="editFile()"><a href="#bugEditFiles" data-toggle="tab">附件信息</a></li>
			<li onclick="bugHistoryList()"><a href="#opHistory" data-toggle="tab">操作历史</a></li>
		</ul>
		<div class="tab-content" style="margin-top:20px;height: 450px;"> 
			<div id="bugInfo" class="tab-pane fade in active">
				<form id="bugAddOrEditForm_" method="post">
					<table class="form-table" style="width: 100%;display: inherit;">
						<tbody style="width: 100%;display: inline-table;">
						<tr class="hidden">
							<td>
								<input id="bugId_" type="hidden" name="dto.bug.bugId"/>
								<input id="reProStep_" type="hidden" name="dto.bug.reProStep"/>
								<input id="bugReptId_" type="hidden" name="dto.bug.bugReptId"/>
								<input id="currHandlerId_" type="hidden" name="dto.bug.currHandlerId" />
								<input id="currHandlDate_" name="dto.bug.currHandlDate" type="hidden" />
								<input id="bugResoVer_" name="dto.bug.bugResoVer" type="hidden" />
								<input id="msgFlag_" name="dto.bug.msgFlag" type="hidden"/>
								<input id="bugAntimodDate_" name="dto.bug.bugAntimodDate" type="hidden" />
								<input id="planAmendHour_" name="dto.bug.planAmendHour" type="hidden" />
								<input id="relaCaseFlag_" name="dto.bug.relaCaseFlag" type="hidden" />
								<input id="initReProStep_" name="dto.initReProStep" type="hidden" />
								<input id="currStateId_" type="hidden" name="dto.bug.currStateId"/>
								<input id="nextFlowCd_" type="hidden" name="dto.bug.nextFlowCd"/>
								<input id="currFlowCd_" type="hidden" name="dto.bug.currFlowCd"/>
								<input id="creatdate_" type="hidden" name="dto.bug.creatdate"/>
								<input id="attachUrl_" type="hidden" name="dto.bug.attachUrl"/>
								<input id="nextOwnerId_" type="hidden" name="dto.bug.nextOwnerId"/>
								<input id="testOwnerId_" name="dto.bug.testOwnerId" type="hidden" />
								<input id="analyseOwnerId_" name="dto.bug.analyseOwnerId" type="hidden" />
								<input id="assinOwnerId_" name="dto.bug.assinOwnerId" type="hidden" />
								<input id="devOwnerId_" name="dto.bug.devOwnerId" type="hidden" />
								<input id="intercessOwnerId_" name="dto.bug.intercessOwnerId" type="hidden" />
								<input id="testPhase_" name="dto.bug.testPhase"/>
							</td>
						</tr>
						<tr style="display: none;">
							<td class="left_td"><sup>*</sup>测试项目:</td>
							<td class="" colspan="5">
								<input id="task_Id_" name="dto.bug.taskId"  type="hidden">
								<input id="task_Name_" name="taskName" class="exui-textbox" data-options="readonly:true,prompt:'请选择测试项目'" style="width: 100%;">
							</td>
						</tr>
						<tr >
							<td class="left_td"><sup>*</sup>测试需求:</td>
							<td class="" colspan="5">
								<input id="moduleId_" name="dto.bug.moduleId"  type="hidden">
								<input id="module_Name_" name="dto.moduleName" type="hidden">
								<input id="taskModule" name="" class="exui-textbox" data-options="readonly:true,prompt:'请选择测试需求'" style="width: 100%;">
								<!-- <p id="taskModule" style="font-size: 20px;"></p> -->
							</td>
						</tr>
						<tr >
							<td class="left_td">状态:</td>
							<td class="" >
								<input id="stateName_" name="stateName" class="exui-textbox" data-options="readonly:true" style="width:196px;">
							</td>
							<td class="left_td">发现人:</td>
							<td class="" >
								<input id="authorName_" name="dto.bug.author.uniqueName" class="exui-textbox" data-options="readonly:true" style="width:196px;">
							</td>
							<td class="left_td">处理人:</td>
							<td class="">
								<input id="currHanderName_" name="dto.bug.currHander.uniqueName" class="exui-textbox" data-options="readonly:true" style="width:196px;">
							</td>
						</tr>
						<tr >
							<td class="left_td"><sup>*</sup>Bug描述:</td>
							<td class="" colspan="5">
								<input id="bugDesc_" name="dto.bug.bugDesc" class="exui-textbox" data-options="required:true" style="width: 100%;">
							</td>
						</tr>
						<tr >
							<td class="left_td"><sup>*</sup>类型:</td>
							<td >
								<input id="bugTypeId_" style="width:196px;" class="exui-combobox" name="dto.bug.bugTypeId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" />
								<input id="bugTypeName_" name="dto.bug.dtoHelper.bugType.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td class="left_td"><sup>*</sup>等级:</td>
							<td >
								<input id="bugGradeId_" class="exui-combobox" name="dto.bug.bugGradeId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:196px;"/>
								<input id="bugGradeName_" name="dto.bug.dtoHelper.bugGrade.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>平台:</td>
							<td >
								<input id="platformId_" class="exui-combobox" name="dto.bug.platformId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:196px;"/>
								<input id="pltformName_" name="dto.bug.dtoHelper.occurPlant.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
						</tr>
						<tr >
							<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>来源:</td>
							<td >
								<input id="sourceId_" class="exui-combobox" name="dto.bug.sourceId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:196px;"/>
								<input id="sourceName_" name="dto.bug.dtoHelper.bugSource.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td class="left_td"><sup>*</sup>发现时机:</td>
							<td >
								<input id="bugOccaId_" class="exui-combobox" name="dto.bug.bugOccaId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:196px;"/>
								<input id="occaName_" name="dto.bug.dtoHelper.bugOpotunity.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td id="geneCauseTd_" class="left_td" style="padding-left: 0.5em;display: none;">测试时机:</td>
							<td id="geneCauseIdTd_" style="display: none;">
								<input id="geneCauseId_" class="exui-combobox" name="dto.bug.geneCauseId" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:196px;"/>
								<input id="geneCaseName_" name="dto.bug.dtoHelper.geneCause.typeName" class="exui-textbox" type="hidden" >
								<div id="geneCauseF_" style="display: none" >
							</td>
						</tr>
						<tr >
							<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>优先级:</td>
							<td >
								<input id="priId_" class="exui-combobox" name="dto.bug.priId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:196px;"/>
								<input id="priName_" name="dto.bug.dtoHelper.bugPri.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>频率:</td>
							<td >
								<input id="bugFreqId_" class="exui-combobox" name="dto.bug.bugFreqId" data-options="
				    			required:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:196px;"/>
								<input id="bugFreqName_" name="dto.bug.dtoHelper.bugFreq.typeName" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
							<td id="reproTd_" class="left_td" style="display: none;">再现比例:</td>
							<td id="reproPersentTd_" class="dataM_left" colspan="" style="display: none;">
								<input id="reproPersent_" name="dto.bug.reproPersent" class="exui-textbox" style="width: 140px;">
							</td>
						</tr>
						<tr style="display: none;">
							<td class="left_td" ><sup>*</sup>引入原因:</td>
							<td >
								<input id="genePhaseId_" class="exui-combobox" name="dto.bug.genePhaseId" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:196px;"/>
								<input id="genPhName_" name="genPhName" class="exui-textbox" type="hidden">
							</td>
						</tr>          
						<tr >
							<td class="left_td"><sup>*</sup>再现步骤:</td>
							<td class="dataM_left" colspan="5">
								<input id="reProTxt_" name="dto.bug.reProTxt" class="exui-textbox" data-options="multiline:true,required:true,prompt:'请填写再现步骤'" style="width: 100%;height: 140px;">
							</td>
						</tr>
						<tr id="currOwner_Tr">
							<td class="left_td" id="currOwnerText" ></td>
							<td colspan="1">
								<input id="currOwner_" name="dto.currOwner" class="exui-textbox" data-options="readonly:true" style="width:196px;">
							</td>
						</tr>
						<tr>
							<td class="left_td"><sup>*</sup>发现日期:</td>
							<td class="">
								<input id="reptDate_" name="dto.bug.reptDate" class="exui-textbox" data-options="readonly:true" style="width:196px;">
							</td>
							<td class="left_td"> &nbsp;&nbsp;<sup>*</sup>发现版本: </td>
							<td style="text-align: right;">
								<input id="bugReptVer_" class="exui-combobox" name="dto.bug.bugReptVer" data-options="
				    			required:true,
				    			readonly:true,
				    			editable:false,
				    			validateOnCreate:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:196px;"/>
								<input id="bugReptVerName_" name="dto.bug.reptVersion.versionNum" class="exui-textbox" type="hidden" data-options="required:true" >
							</td>
						</tr>
						<tr id="currRemarkTr_">
							<td class="left_td">备注:</td>
							<td colspan="5">
								<input id="currRemark_" name="dto.bug.currRemark" class="exui-textbox" style="width: 100%;" data-options="multiline:false,readonly:true,prompt:'备注'" />
		   			      </td>
						</tr>
					</tbody>
				</table>
			</form>
			<div id="" align="right" style="margin-bottom: 1em;padding: 0.5em;" >
				<a id="editSubmitBnt" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="editSubmitForm()">确定</a>
				<a class="exui-linkbutton bntcss " data-options="btnCls:'default',size:'xs'" onclick="closeEditBugWin()">返回</a>
			</div>
		</div>
		
		<div id="opHistory" class="tab-pane fade">
		</div>
		<table id="bugHistoryList" class="exui-datagrid" data-options=""></table>
		
		<div id="bugEditFiles" class="tab-pane fade" style="padding-bottom: 1em;">
			<h4 id="editMsg" align="center" style="display: none;">暂无附件信息</h4>
			<form id="editFileForm" enctype="multipart/form-data">
		        <div class="form-group">
		            <div class="file-loading">
		                <input id="editFileId" type="file" multiple class="file" data-overwrite-initial="false">
		            </div>
		        </div>
		    </form>
<!-- 		    <div id="" align="right" style="margin-bottom: 1em;padding: 0.5em;" >
				<a id="editFileSubmitBnt" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="editFileSubmitForm()">确定</a>
				<a class="exui-linkbutton bntcss " data-options="btnCls:'default',size:'xs'" onclick="closeEditBugWin()">取消</a>
			</div> -->
		</div>
		
	</div>
	<script src="<%=request.getContextPath()%>/itest/js/bugManager/bugEdit.js" type="text/javascript" charset="utf-8"></script>
