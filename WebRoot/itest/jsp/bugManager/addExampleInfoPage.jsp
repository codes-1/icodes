<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<form id="lookForm" enctype="multipart/form-data" method="post">
	  <table class="form-table" style="font-size: 14px;">
	  		<input type="hidden" name="mdPath" value="" />
			<input type="hidden" name="dto.testCaseInfo.taskId"/>
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
				<input name="dto.testCaseInfo.weight"  class="exui-numberbox" data-options="min:1,max:100,required:true,prompt:'一个成本单位代表5分钟'" style="width: 100%;">
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
				<!-- <textarea name="dto.testCaseInfo.operDataRichText" id="operDataRichText"
					cols="50" rows="15" style="width: 100%;">
  			      </textarea> -->
  			      <input name="dto.testCaseInfo.operDataRichText" class="exui-textbox" style="width: 100%;height: 160px;" data-options="multiline:true,required:true,prompt:'请填写过程及数据'" />
 			      	</td>
		</tr> 
		
		<tr style="margin-top: 5px;">
			<td  class="left_td"><sup>*</sup>期望结果:</td>
			<td class="dataM_left" colspan="3">
				<input name="dto.testCaseInfo.expResult" class="exui-textbox" style="width: 100%;height: 160px;" data-options="multiline:true,required:true,prompt:'请填写期望结果'" />
 			      </td>
		</tr>
		
		<!-- <tr>
			<td  class="left_td">附件/插图片：</td>
    		<td colspan="3"><input class="exui-filebox" name="dto.testTaskManage.testPhase" style="width:250px"/></td>
		</tr> -->
		</table>
    </form>

<!-- <form id="uploadForm" enctype="multipart/form-data" method="post">
	<table>
		<tr>
			<td  class="left_td">附件/插图片：</td>
    		<td><input class="exui-filebox" name="dto.testTaskManage.testPhase" style="width:250px"/></td>
		</tr>
	</table>
</form> -->
<div class="buttonSt" style="margin-left: 60%;margin-top: 20px;">
	<button type="button" onclick="addExampleSubmit()" class="btn btn-primary">确认</button>
	<button id="resetting" onclick="addExampleReset()" type="button" style="border: 1px solid #1E7CFB;color: #1E7CFB;" class="btn btn-default">重置</button>
	<button type="button" onclick="canlceWin()" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;">取消</button>
</div>

<script src="<%=request.getContextPath()%>/itest/js/bugManager/addExampleInfoPage.js" type="text/javascript" charset="utf-8"></script>