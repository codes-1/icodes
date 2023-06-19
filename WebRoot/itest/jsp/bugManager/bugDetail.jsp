<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<!-- <div id="bugDetailWin" class="exui-window" style="display: none;padding: 20px 25px 25px;" data-options="
		modal:true,
		width: 900,
		footer:'',
		minimizable:false,
		maximizable:false,
		closed:true"> -->
		<ul class="nav nav-lattice">
			<li class="active"><a href="#bugDetailInfo" data-toggle="tab">基本信息</a></li>
			<li onclick="bugDetailHistoryList()"><a href="#detailHistory" data-toggle="tab">操作历史</a></li>
			<li id="bugfilesLi" onclick="bugDetailFile()"><a href="#bugDetailFiles" data-toggle="tab">附件信息</a><img id="messageImgs" class="messagecss" src="<%=request.getContextPath()%>/itest/images/fj.png" style="display: none;"> <span id="fileCounts" class="fileCount"></span></li>
		</ul>
		<div class="tab-content" style="margin-top:20px;height: 450px;"> 
			<div id="bugDetailInfo" class="tab-pane fade in active">
				<form id="bugDetailForm_" method="post">
					<table class="form-table" style="width: 100%;display: inherit;">
						<tr >
							<td class="left_td">测试需求:</td>
							<td class="" colspan="5">
								<input id="moduleId_d" name="dto.bug.moduleId"  type="hidden">
								<input id="module_Name_d" name="dto.moduleName" type="hidden">
								<input id="taskModuled" class="exui-textbox" data-options="readonly:true" style="width: 100%;">
							</td>
						</tr>
						<tr >
							<td class="left_td">状态:</td>
							<td class="" >
								<input id="stateName_d" name="stateName" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
							<td class="left_td">发现人:</td>
							<td class="" >
								<input id="authorName_d" name="dto.bug.author.uniqueName" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
							<td class="left_td">处理人:</td>
							<td class="">
								<input id="currHanderName_d" name="dto.bug.currHander.uniqueName" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
						</tr>
						<tr >
							<td class="left_td">Bug描述:</td>
							<td class="" colspan="5">
								<input id="bugDesc_d" name="dto.bug.bugDesc" class="exui-textbox" data-options="readonly:true," style="width: 100%;">
							</td>
						</tr>
						<tr >
							<td class="left_td">类型:</td>
							<td >
								<input id="bugTypeId_d" style="width:180px;" class="exui-combobox" name="dto.bug.bugTypeId" data-options="
				    			readonly:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" />
							</td>
							<td class="left_td">等级:</td>
							<td >
								<input id="bugGradeId_d" class="exui-combobox" name="dto.bug.bugGradeId" data-options="
				    			readonly:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
							</td>
							<td class="left_td" style="padding-left: 0.5em;">平台:</td>
							<td >
								<input id="platformId_d" class="exui-combobox" name="dto.bug.platformId" data-options="
				    			readonly:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
							</td>
						</tr>
						<tr >
							<td class="left_td" style="padding-left: 0.5em;">来源:</td>
							<td >
								<input id="sourceId_d" class="exui-combobox" name="dto.bug.sourceId" data-options="
				    			readonly:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
							</td>
							<td class="left_td">发现时机:</td>
							<td >
								<input id="bugOccaId_d" class="exui-combobox" name="dto.bug.bugOccaId" data-options="
				    			readonly:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
							</td>
							<td id="geneCauseTd_d" class="left_td" style="padding-left: 0.5em;display: none;">测试时机:</td>
							<td id="geneCauseIdTd_d" style="display: none;">
								<input id="geneCauseId_d" class="exui-combobox" name="dto.bug.geneCauseId" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
							</td>
						</tr>
						<tr >
							<td class="left_td" style="padding-left: 0.5em;">优先级:</td>
							<td >
								<input id="priId_d" class="exui-combobox" name="dto.bug.priId" data-options="
				    			readonly:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
							</td>
							<td class="left_td" style="padding-left: 0.5em;">频率:</td>
							<td >
								<input id="bugFreqId_d" class="exui-combobox" name="dto.bug.bugFreqId" data-options="
				    			readonly:true,
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
							</td>
							<td id="reproTd_" class="left_td" style="display: none;">再现比例:</td>
							<td id="reproPersentTd_d" class="dataM_left" colspan="" style="display: none;">
								<input id="reproPersent_d" name="dto.bug.reproPersent" class="exui-textbox" style="width: 140px;">
							</td>
						</tr>
						<tr id="genePhaseTdd" style="display: none;">
							<td class="left_td" >引入原因:</td>
							<td >
								<input id="genePhaseId_d" class="exui-combobox" name="dto.bug.genePhaseId" data-options="
				    			validateOnCreate:false,
				    			editable:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
							</td>
						</tr>          
						<tr >
							<td class="left_td">再现步骤:</td>
							<td class="dataM_left" colspan="5">
								<input id="reProTxt_d" name="dto.bug.reProTxt" class="exui-textbox" data-options="multiline:true,readonly:true,prompt:'请填写再现步骤'" style="width: 100%;height: 140px;">
							</td>
						</tr>
						<tr>
							<td class="left_td">发现日期:</td>
							<td class="">
								<input id="reptDate_d" name="dto.bug.reptDate" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
							<td class="left_td">发现版本: </td>
							<td style="text-align: right;">
								<input id="bugReptVer_d" class="exui-combobox" name="dto.bug.bugReptVer" data-options="
				    			readonly:true,
				    			readonly:true,
				    			editable:false,
				    			validateOnCreate:false,
				    			valueField:'id',
				    			textField:'text',
				    			prompt:'-请选择-'" style="width:180px;"/>
							</td>
							<td id="verLableTdd" style="display: none;"  class="left_td">关闭在版本: </td>
							<td id="verLableValueTdd"  style="text-align: right;display: none;">
								<input id="versionLabled" name="currVerName" class="exui-textbox" data-options="readonly:true" style="width:180px;">
							</td>
						</tr>
						<tr id="currRemarkTr_">
							<td class="left_td">备注:</td>
							<td colspan="5">
								<input id="currRemark_d" name="dto.bug.currRemark" class="exui-textbox" style="width: 100%;" data-options="multiline:false,readonly:true,prompt:'备注'" />
		   			      </td>
						</tr>
				</table>
			</form>
		</div>
		<div id="detailHistory" class="tab-pane fade">
		</div>
		<table id="bugDetailHistoryList" class="exui-datagrid" data-options=""></table>
		
		<div id="bugDetailFiles" class="tab-pane fade">
			<h4 id="detailMsg" align="center" style="display: none;">暂无附件信息</h4>
			<form id="detailFileFrom" enctype="multipart/form-data">
		        <div class="form-group">
		            <div class="file-loading">
		                <input id="detailFileId" type="file" multiple class="file" data-overwrite-initial="false">
		            </div>
		        </div>
		    </form>
		</div>
	</div>
<!-- </div> -->

<script src="<%=request.getContextPath()%>/itest/js/bugManager/bugDetail.js" type="text/javascript" charset="utf-8"></script>
