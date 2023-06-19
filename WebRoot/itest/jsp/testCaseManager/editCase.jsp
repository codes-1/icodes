<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
    <ul class="nav nav-lattice">
		<li class="active"><a href="#caseInfo" data-toggle="tab">基本信息</a></li>
		<li onclick="editCaseFile()"><a href="#caseEditFiles" data-toggle="tab">附件信息</a></li>
		<li onclick="caseHistory()"><a href="#caseHistory" data-toggle="tab">执行历史</a></li>
	</ul>
	<div class="tab-content" style="margin-top:20px;height: 450px;"> 
		<div id="caseInfo" class="tab-pane fade in active">
			<form id="editCaseForm" method="post">
			<table class="form-table">
				<tr class="hidden"><td>
					<input type="hidden" id="mdPath" name="mdPath" value="" />
					<input id="task_Id" type="hidden" name="dto.testCaseInfo.taskId"/>
					<input id="moduleId" type="hidden" name="dto.testCaseInfo.moduleId"/>
					<input id="createrId" type="hidden" name="dto.testCaseInfo.createrId"/>
					<input id="testCaseId" type="hidden" name="dto.testCaseInfo.testCaseId"/>
					<input id="isReleased" type="hidden" name="dto.testCaseInfo.isReleased"/>
					<input id="creatdate" type="hidden" name="dto.testCaseInfo.creatdate"/>
					<input id="attachUrl" type="hidden" name="dto.testCaseInfo.attachUrl"/>
					<input id="auditId" type="hidden" name="dto.testCaseInfo.auditId"/>
					<input id="testStatus" type="hidden" name="dto.testCaseInfo.testStatus"/>
					<input id="testData" type="hidden" name="dto.testCaseInfo.testData"/>
					<input id="moduleNum" type="hidden" name="dto.testCaseInfo.moduleNum"/>
					<input id="expResultOld"/>
				</td></tr>
				<!-- <tr >
					<td colspan="5" class="tdtxt" align="center">
						<div id="cUMTxt" align="center" style="color: Blue; padding: 2px"></div>
					</td>
				</tr> -->
				<tr >
					<td class="left_td"><sup>*</sup>类别:</td>
					<td >
						<input id="caseTypeId" class="exui-combobox caseTypeId" name="dto.testCaseInfo.caseTypeId" data-options="
		    			required:true,
		    			valueField:'typeId',
	    				textField:'typeName',
		    			validateOnCreate:false,
		    			editable:false,
		    			prompt:'-请选择-'" style="width:120px"/>
					</td>
					<td class="left_td" style="padding-left: 0.5em;"><sup>*</sup>优先级:</td>
					<td >
						<input id="priId" class="exui-combobox priId" name="dto.testCaseInfo.priId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			valueField:'typeId',
	    				textField:'typeName',
		    			editable:false,
		    			prompt:'-请选择-'" style="width:120px"/>
					</td>
					<td class="left_td"> &nbsp;&nbsp;<sup>*</sup>执行成本: </td>
					<td>
						<input id="caseWeight" name="dto.testCaseInfo.weight"  class="exui-numberbox" value="2" data-options="min:1,max:10,required:true" style="width: 120px;">
						<font color="orange" style="font-size: 11px;">一成本单位代表5分钟</font>
					</td>
				</tr>
				<tr >
					<td class="left_td"><sup>*</sup>用例描述:</td>
					<td class="dataM_left" colspan="5">
						<input id="testCaseDes" name="dto.testCaseInfo.testCaseDes" class="exui-textbox" data-options="required:true" style="width: 100%;">
					</td>
				</tr>
				<tr >
					<td class="left_td">前置条件:</td>
					<td colspan="5">
						<input id="prefixCondition" name="dto.testCaseInfo.prefixCondition" class="exui-textbox" style="width: 100%;" data-options="multiline:false,prompt:'前置条件'" />
   			      </td>
				</tr>
				<tr >
					<td class="left_td"><sup>*</sup>过程及数据:</td>
					<td colspan="5" style="background-color: #f7f7f7;">
						<!-- <textarea name="dto.testCaseInfo.operDataRichText" id="operDataRichText"
							cols="50" rows="15" style="width: 100%;">
	   			      </textarea> -->
	   			      <input id="operDataRichText" name="dto.testCaseInfo.operDataRichText" class="exui-textbox" style="width: 100%;height: 160px;" data-options="multiline:true,required:true,prompt:'请填写过程及数据'" />
   			      	</td>
				</tr>
				<tr >
					<td class="left_td"><sup>*</sup>期望结果:</td>
					<td class="dataM_left" colspan="5">
						<input id="expResult" name="dto.testCaseInfo.expResult" class="exui-textbox" style="width: 100%;height: 160px;margin-top: 6px;" data-options="multiline:true,required:true,prompt:'请填写期望结果'" />
   			      </td>
				</tr>
				<tr id="remarkTr">
					<td class="left_td">备注:</td>
					<td colspan="5">
						<input id="testCaseRemark" name="dto.testCaseInfo.remark" class="exui-textbox" style="width: 100%;margin-top: 6px;" data-options="multiline:false,prompt:'备注'" />
   			      </td>
				</tr>
				<tr id="execTr" style="display: none;">
					<td class="left_td"><sup>*</sup>执行版本:</td>
					<td>
						<input id="exeVerId" class="exui-combobox" name="dto.exeVerId"  style="width:120px" data-options="
		    			required:false,
		    			validateOnCreate:false,
		    			prompt:'-请选择-'
		    			"/>
					</td>
					<td class="left_td"> &nbsp;&nbsp;执行备注: </td>
					<td colspan="3">
							<input id="execRemark" name="dto.remark" class="exui-textbox" style="width: 100%;min-width: 300px;" data-options="multiline:false,prompt:'请填写执行备注'" />
					</td>
				</tr>
			</table>
			</form>
		    <div id="addOrEditFoot" align="right" >
				<div id="operaDiv" class="" style="padding:5px;padding-bottom: 2em;">
					<a id="submit_1" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="editSubmit('1')">修改</a>
					<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeEditCaseWin()" style="border: 1px solid #1e7cfb;color: #1e7cfb;">取消</a>
				</div>
				<div id="execDiv" style="padding-bottom: 2em;">
					<a id="submit_2" href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="editSubmit('2')">保存</a>
					<a id="exec_2" href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="execCaseSubmit('2')">通过</a>
					<a id="exec_3" href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="execCaseSubmit('3')">未通过</a>
					<a id="exec_5" href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="execCaseSubmit('5')">阻塞</a>
					<a id="exec_4" href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="execCaseSubmit('4')">不适用</a>
					<a href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="closeEditCaseWin()">返回</a>
				</div>
			</div>	
		</div>
		<div id="caseEditFiles" class="tab-pane fade" style="padding-bottom: 1em;">
				<h4 id="editMsg" align="center" style="display: none;">暂无附件信息</h4>
				<form id="editFiles" enctype="multipart/form-data">
		        <div class="form-group">
		            <div class="file-loading">
		                <input id="caseEditFileId" multiple type="file" class="file">
		            </div>
		        </div>
		    </form>
		</div>
		
		<div id="caseHistory" class="tab-pane fade">
		</div>
		<table id="caseHistoryList" class="exui-datagrid" data-options=""></table>
	</div>
	
	<script src="<%=request.getContextPath()%>/itest/js/testCaseManager/editCase.js" type="text/javascript" charset="utf-8"></script>
