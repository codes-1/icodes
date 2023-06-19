<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<!-- 新增/修改模态窗 -->
	<!-- <div id="bugAddOrEditWindown" title="" class="exui-window" data-options="
		modal:true,
		width: 800,
		footer:'#bugAddOrEditFoot',
		minimizable:false,
		maximizable:false,
		closed:true"> -->
		<form id="bugAddOrEditForm" method="post">
			<table class="form-table" style="width: 100%;display: inherit;">
			<tbody style="width: 100%;display:inline-table;">
				<tr class="hidden">
					<td>
						<input id="bugId" type="hidden" name="dto.bug.bugId"/>
						<input id="currStateId" type="hidden" name="dto.bug.currStateId"/>
						<input id="nextFlowCd" type="hidden" name="dto.bug.nextFlowCd"/>
						<input id="currFlowCd" type="hidden" name="dto.bug.currFlowCd"/>
						<input id="creatdate" type="hidden" name="dto.bug.creatdate"/>
						<input id="attachUrl" type="hidden" name="dto.bug.attachUrl"/>
						<input id="nextOwnerId" type="hidden" name="dto.bug.nextOwnerId"/>
						<input id="testPhase" name="dto.bug.testPhase"/>
					</td>
				</tr>
				<tr >
					<td colspan="6" align="left" style="text-align: left;">
						<input id="clearUpFlg" name ="clearUpFlg" type="checkbox" value="1" checked="true"/>不重置:	
					</td>
				</tr>
				<tr style="display: none;">
					<td class="left_td"><sup>*</sup>测试项目:</td>
					<td class="" colspan="5">
						<input id="task__Id" name="dto.bug.taskId"  type="hidden">
						<input id="task_Name" name="taskName" class="exui-textbox" data-options="readonly:true,prompt:'请选择测试项目'" style="width: 100%;">
					</td>
				</tr>
				<tr >
					<td class="left_td"><sup>*</sup>测试需求:</td>
					<td class="" colspan="5">
						<input id="module_Id" name="dto.bug.moduleId"  type="hidden">
						<input id="module_Name" name="dto.moduleName" class="exui-textbox" data-options="required:true,readonly:true,prompt:'请选择测试需求'" style="width: 100%;">
					</td>
				</tr>
				<tr >
					<td class="left_td"><sup>*</sup>状态:</td>
					<td class="">
						<input id="stateName" name="stateName" class="exui-textbox" data-options="required:true,readonly:true" style="width:196px;">
					</td>
					<td ></td>
					<td></td>
					<td class="left_td"><sup>*</sup>发现版本:</td>
					<td style="text-align: right;">
						<input id="bugReptVer" class="exui-combobox" name="dto.bug.bugReptVer" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="bugReptVerName" name="dto.bug.reptVersion.versionNum" class="exui-textbox" type="hidden" data-options="required:true" >
					</td>
				</tr>
				<tr >
					<td class="left_td"><sup>*</sup>Bug描述:</td>
					<td class="" colspan="5">
						<input id="bugDesc" name="dto.bug.bugDesc" class="exui-textbox" data-options="required:true" style="width: 100%;">
					</td>
				</tr>
				<tr >
					<td class="left_td"><sup>*</sup>类型:</td>
					<td >
						<input id="bugTypeId" style="width:196px;" class="exui-combobox" name="dto.bug.bugTypeId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" />
						<input id="bugTypeName" name="dto.bug.dtoHelper.bugType.typeName" class="exui-textbox" type="hidden" data-options="" >
					</td>
					<td class="left_td"><sup>*</sup>等级:</td>
					<td >
						<input id="bugGradeId" class="exui-combobox" name="dto.bug.bugGradeId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="bugGradeName" name="dto.bug.dtoHelper.bugGrade.typeName" class="exui-textbox" type="hidden" data-options="" >
					</td>
					<td class="left_td"><sup>*</sup>平台:</td>
					<td >
						<input id="platformId" class="exui-combobox" name="dto.bug.platformId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="pltformName" name="dto.bug.dtoHelper.occurPlant.typeName" class="exui-textbox" type="hidden" data-options="" >
					</td>
				</tr>
				<tr >
					<td class="left_td" ><sup>*</sup>来源:</td>
					<td >
						<input id="sourceId" class="exui-combobox" name="dto.bug.sourceId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="sourceName" name="dto.bug.dtoHelper.bugSource.typeName" class="exui-textbox" type="hidden" data-options="" >
					</td>
					<td class="left_td"><sup>*</sup>发现时机:</td>
					<td >
						<input id="bugOccaId" class="exui-combobox" name="dto.bug.bugOccaId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="occaName" name="dto.bug.dtoHelper.bugOpotunity.typeName" class="exui-textbox" type="hidden" data-options="" >
					</td>
					<td id="geneCauseTd" class="left_td" style="padding-left: 0.5em;display: none;">测试时机:</td>
					<td id="geneCauseIdTd" style="display: none;">
						<input id="geneCauseId" class="exui-combobox" name="dto.bug.geneCauseId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="geneCaseName" name="dto.bug.dtoHelper.geneCause.typeName" class="exui-textbox" type="hidden" >
						<div id="geneCauseF" style="display: none" >
					</td>
				</tr>
				<tr >
					<td class="left_td" ><sup>*</sup>优先级:</td>
					<td >
						<input id="priId" class="exui-combobox" name="dto.bug.priId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="priName" name="dto.bug.dtoHelper.bugPri.typeName" class="exui-textbox" type="hidden" data-options="" >
					</td>
					<td class="left_td" ><sup>*</sup>频率:</td>
					<td >
						<input id="bugFreqId" class="exui-combobox" name="dto.bug.bugFreqId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="bugFreqName" name="dto.bug.dtoHelper.bugFreq.typeName" class="exui-textbox" type="hidden" data-options="" >
					</td>
					<td id="reproTd" class="left_td" style="display: none;">再现比例:</td>
					<td id="reproPersentTd" class="dataM_left" colspan="" style="display: none;">
						<input id="reproPersent" name="dto.bug.reproPersent" class="exui-textbox" style="width: 140px;">
					</td>
				</tr>
				<tr id="genePhaseIdTr" style="display: none;">
					<td class="left_td" ><sup>*</sup>引入原因:</td>
					<td >
						<input id="genePhaseId" class="exui-combobox" name="dto.bug.genePhaseId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="genPhName" name="genPhName" class="exui-textbox" type="hidden">
					</td>
				</tr>          
				<tr >
					<td class="left_td"><sup>*</sup>再现步骤:</td>
					<td class="dataM_left" colspan="5">
						<input id="reProTxt" name="dto.bug.reProTxt" class="exui-textbox" data-options="multiline:true,required:true,prompt:'请填写再现步骤'" style="width: 98%;height: 140px;">
					</td>
				</tr>
				<tr id="testOwnerTr" style="display: none;">
					<td class="left_td"><sup>*</sup>转互验人:</td>
					<td >
						<input id="testOwnerId" class="exui-combobox" name="dto.bug.testOwnerId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="testOwnName" name="testOwnName" class="exui-textbox" type="hidden" >
					</td>
					<td class="tdtxt" align="left" width="480" colspan="4" style="border-right:0">
						<a style="display: none;" class="graybtn" href="javascript:void(0);" onclick="getMdPerson('','module_Id')"><span>从测试需求开发人员中指派</span> </a>
					</td>
				</tr>
				<tr id="analOwnerTr" style="display: none;">
					<td class="left_td"><sup>*</sup>转分析人 :</td>
					<td >
						<input id="analyseOwnerId" class="exui-combobox" name="dto.bug.analyseOwnerId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="analOwnerName" name="analOwnerName" class="exui-textbox" type="hidden" >
					</td>
					<td class="tdtxt" align="left" width="480" colspan="4" style="border-right:0">
						<a style="display: none;" class="graybtn" href="javascript:void(0);" onclick="getMdPerson('','module_Id')"><span>从测试需求开发人员中指派</span> </a>
					</td>
				</tr>
				<tr id="assignOwnerTr" style="display: none;">
					<td class="left_td"><sup>*</sup>转分配人:</td>
					<td >
						<input id="assignOwnerId" class="exui-combobox" name="dto.bug.assinOwnerId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="anasnOwnerName" name="anasnOwnerName" class="exui-textbox" type="hidden" >
					</td>
					<td class="tdtxt" align="left" width="480" colspan="4" style="border-right:0">
						<a class="graybtn" href="javascript:void(0);" onclick="getMdPerson('3','module_Id','assignOwnerId','anasnOwnerName')"><span>从测试需求指定人员中指派</span> </a>
					</td>
				</tr>
				<tr id="devOwnerTr" style="display: none;">
					<td class="left_td"><sup>*</sup>转修改人:</td>
					<td >
						<input id="devOwnerId" class="exui-combobox" name="dto.bug.devOwnerId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="devOwnerName" name="devOwnerName" class="exui-textbox" type="hidden" >
						<input type="hidden" id="moduleDevStr" name="moduleDevStr" value="" />
					</td>
					<td class="tdtxt" align="left" width="" colspan="4" style="border-right:0">
						<a  class="graybtn" href="javascript:void(0);" onclick="getMdPerson('1','module_Id','devOwnerId','devOwnerName')"><span>从测试需求指定人员中指派</span> </a>
					</td>
				</tr>
				<tr id="intercessOwnerTr" style="display: none;">
					<td class="left_td"><sup>*</sup>转仲裁人:</td>
					<td >
						<input id="intercessOwnerId" class="exui-combobox" name="dto.bug.intercessOwnerId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'id',
		    			textField:'text',
		    			prompt:'-请选择-'" style="width:196px;"/>
						<input id="intecsOwnerName" name="intecsOwnerName" class="exui-textbox" type="hidden">
					</td>
					<td class="tdtxt" align="left" width="480" colspan="4" style="border-right:0">
						<a style="display: none;" class="graybtn" href="javascript:void(0);" onclick="getMdPerson('','module_Id')"><span>从测试需求开发人员中指派</span> </a>
					</td>
				</tr>
				<tr id="currRemarkTr" style="display: none;">
					<td class="left_td">备注:</td>
					<td colspan="5">
						<input id="currRemark" name="dto.bug.currRemark" class="exui-textbox" style="width: 100%;" data-options="multiline:false,prompt:'备注'" />
   			      </td>
				</tr>
			</tbody>
		</table>
	</form>
	<!-- <form id="uploadForm" enctype="multipart/form-data" method="post">
		<table>
			<tr>
				<td class="left_td">附件：</td>
	    		<td>
	    			<input id="bugFileUpload" class="exui-filebox" name="currUpFile" multiple="multiple" style="width:250px" data-options="multiple:true,prompt:'选择文件'" />
	    			<input id="bugFileUrl" type="hidden"/> 
	    		</td>
			</tr>
		</table>
	</form> -->
	<form enctype="multipart/form-data">
        <div class="form-group">
            <div class="file-loading">
                <input id="uploadFileId" type="file" multiple class="file" data-overwrite-initial="false">
            </div>
        </div>
    </form>
	<!-- </div> -->
	<div id=bugAddOrEditFoot align="right" >
		<div id="operaDiv" class="" style="padding:5px;">
			<a id="submit_1" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="bugSubmitForm('1')">保存</a>
			<a id="submit_2" class="exui-linkbutton bntcss" data-options="btnCls:'default',size:'xs'" onclick="bugSubmitForm('2')">保存并继续</a>
			<a id="cancleBnt" class="exui-linkbutton bntcss" data-options="btnCls:'default',size:'xs'" onclick="closeBugWin()">取消</a>
		</div>
	</div>
		
	<script src="<%=request.getContextPath()%>/itest/js/bugManager/bugAdd.js" type="text/javascript" charset="utf-8"></script>
