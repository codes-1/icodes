<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/singleTestTaskManager/testTaskMagrList.css"/>
<style type="text/css">
th{
 	font-weight:400;
}
.left_td{
	width:20%;
}
.right_td{
	width:80%;
}
</style>
<div id="caseLayout" class="exui-layout" style="height: 600px;">
	<div id="caseTreeDiv" data-options="region:'west'" style="width:230px;padding:10px;">
		<%-- <input id="currTaksId" type="hidden" value="<%=currTaksId%>"> --%>
		<ul id="caseTypeTree" class="exui-tree"></ul>
	</div>
	<div data-options="region:'center'" style="overflow: scroll;" title="">
		<!-- <div class="tools" data-options="">
			<button style="padding:9.5px 18px;" type="button" class="btn btn-default" onclick="agreeLibrary();"><i class="glyphicon glyphicon-asterisk"></i>通过</button>
			<button style="padding:9.5px 18px;" type="button" class="btn btn-default" onclick="disagreeLibrary();"><i class="glyphicon glyphicon-plus"></i>不通过</button>
		</div> --><!--/.top tools area-->
		
		<table id="caseList" data-options="
			fitColumns: true,
			singleSelect: true,
			pagination: true,
			pageNumber: 1,
			pageSize: 10,
			pageList:[10,30,50],
			layout:['list','first','prev','manual','next','last','refresh','info']">
		</table>
	</div>
</div>

<!-- 查看详情 -->
<div id="lookDetailWin" class="exui-window" style="display:none;width: 900px" data-options="
	modal:true,
	minimizable:false,
	maximizable:false,
	collapsible:false,
	closed:true">
	<form id="lookDetailForm" method="post">
		<table class="form-table" style="width:100%">
			<!-- <tr class="hidden"><td>
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
			</td></tr> -->
			<tr >
				<td class="left_td"><sup>*</sup>用例描述:</td>
				<td class="right_td">
					<input name="dto.testCaseInfo.testCaseDes" class="exui-textbox" data-options="readonly:true,required:true" style="width: 100%;">
				</td>
			</tr>
			<tr >
				<td  class="left_td"><sup>*</sup>前置条件:</td>
				<td class="right_td">
					<input name="dto.testCaseInfo.prefixCondition" class="exui-textbox" style="width: 100%;" data-options="readonly:true,multiline:false" />
  			      </td>
			</tr>
			<tr >
				<td class="left_td"><sup>*</sup>过程及数据:</td>
				<td class="right_td" style="background-color: #f7f7f7;">
					<!-- <textarea name="dto.testCaseInfo.operDataRichText" id="operDataRichText"
						cols="50" rows="15" style="width: 100%;">
   			      </textarea> -->
   			      <input name="dto.testCaseInfo.operDataRichText" class="exui-textbox" style="width: 100%;height: 160px;" data-options="readonly:true,multiline:true,required:true" />
  			      	</td>
			</tr>
			<tr >
				<td  class="left_td"><sup>*</sup>期望结果:</td>
				<td class="right_td">
					<input name="dto.testCaseInfo.expResult" class="exui-textbox" style="width: 100%;height: 160px;" data-options="readonly:true,multiline:true,required:true" />
  			      </td>
			</tr>
			<tr >
				<td class="left_td"><sup>*</sup>备注:</td>
				<td class="right_td">
					<input name="dto.testCaseInfo.remark" class="exui-textbox" style="width: 100%;" data-options="readonly:true,multiline:false" />
  			      </td>
			</tr>
		</table>
	</form>
</div>
<script src="<%=request.getContextPath()%>/itest/js/testLibrary/caseLook.js" type="text/javascript" charset="utf-8"></script>