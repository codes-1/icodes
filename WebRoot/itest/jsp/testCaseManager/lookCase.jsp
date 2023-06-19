<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
	<ul class="nav nav-lattice">
		<li class="active"><a href="#caseDetailInfo" data-toggle="tab">基本信息</a></li>
		<li onclick="caseHistory()"><a href="#caseHistory" data-toggle="tab">执行历史</a></li>
		<li id="casefileLi" onclick="lookCaseFile()"><a href="#caseLookFiles" data-toggle="tab">附件信息</a><img id="messageImg" class="messagecss" src="<%=request.getContextPath()%>/itest/images/fj.png" style="display: none;"><span id="fileCount" class="fileCount"></span></li>
	</ul>
	<div class="tab-content" style="margin-top:20px;height: 450px;"> 
		<div id="caseDetailInfo" class="tab-pane fade in active">
			<form id="lookForm" method="post">
				<table class="form-table">
					<tr class="hidden"><td>
						<input type="hidden" name="mdPath" value="" />
						<input itype="hidden" name="dto.testCaseInfo.taskId"/>
						<input type="hidden" name="dto.testCaseInfo.moduleId"/>
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
					<tr >
						<td class="left_td"><sup>*</sup>类别:</td>
						<td >
							<input class="exui-combobox caseTypeId" name="dto.testCaseInfo.caseTypeId" data-options="
			    			required:true,
			    			readonly:true,
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'typeId',
    						textField:'typeName',
			    			prompt:'-请选择-'" style="width:120px"/>
						</td>
						<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>优先级:</td>
						<td >
							<input class="exui-combobox priId" name="dto.testCaseInfo.priId" data-options="
			    			required:true,
			    			readonly:true,
			    			validateOnCreate:false,
			    			editable:false,
			    			valueField:'typeId',
	    					textField:'typeName',
			    			prompt:'-请选择-'" style="width:120px"/>
						</td>
						<td class="left_td"> &nbsp;&nbsp;<sup>*</sup>执行成本: </td>
						<td>
							<input name="dto.testCaseInfo.weight"  class="exui-numberbox" data-options="readonly:true,min:1,max:10,required:true" style="width: 120px;">
							<font color="orange" style="font-size: 11px;">一成本单位代表5分钟</font>
						</td>
					</tr>
					<tr >
						<td class="left_td"><sup>*</sup>用例描述:</td>
						<td class="dataM_left" colspan="5">
							<input name="dto.testCaseInfo.testCaseDes" class="exui-textbox" data-options="readonly:true,required:true" style="width: 100%;">
						</td>
					</tr>
					<tr >
						<td  class="left_td">前置条件:</td>
						<td colspan="5">
							<input name="dto.testCaseInfo.prefixCondition" class="exui-textbox" style="width: 100%;" data-options="readonly:true,multiline:false,prompt:'前置条件'" />
	   			      </td>
					</tr>
					<tr >
						<td class="left_td"><sup>*</sup>过程及数据:</td>
						<td colspan="5" style="background-color: #f7f7f7;">
							<!-- <textarea name="dto.testCaseInfo.operDataRichText" id="operDataRichText"
								cols="50" rows="15" style="width: 100%;">
		   			      </textarea> -->
		   			      <input name="dto.testCaseInfo.operDataRichText" class="exui-textbox" style="width: 100%;height: 160px;" data-options="readonly:true,multiline:true,required:true,prompt:'请填写过程及数据'" />
	   			      	</td>
					</tr>
					<tr >
						<td  class="left_td"><sup>*</sup>期望结果:</td>
						<td class="dataM_left" colspan="5">
							<input name="dto.testCaseInfo.expResult" class="exui-textbox" style="width: 100%;height: 160px;" data-options="readonly:true,multiline:true,required:true,prompt:'请填写期望结果'" />
	   			      </td>
					</tr>
					<tr >
						<td class="left_td">备注:</td>
						<td colspan="5">
							<input name="dto.testCaseInfo.remark" class="exui-textbox" style="width: 100%;" data-options="readonly:true,multiline:false,prompt:'备注'" />
	   			      </td>
					</tr>
				</table>
		   </form>
		</div>
		
		<div id="caseLookFiles" class="tab-pane fade" style="padding-bottom: 1em;">
			<h4 id="lookMsg" align="center" style="display: none;">暂无附件信息</h4>
			<form id="lookFilesForm" enctype="multipart/form-data">
		        <div class="form-group">
		            <div class="file-loading">
		                <input id="caseLookFileId" type="file" multiple class="file">
		            </div>
		        </div>
		    </form>
		</div>
		
		<div id="caseHistory" class="tab-pane fade">
		</div>
		<table id="caseHistoryList" class="exui-datagrid" data-options=""></table>
	</div>
	
	<script src="<%=request.getContextPath()%>/itest/js/testCaseManager/lookCase.js" type="text/javascript" charset="utf-8"></script>