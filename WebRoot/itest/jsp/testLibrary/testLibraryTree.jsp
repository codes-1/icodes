<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/singleTestTaskManager/testTaskMagrList.css"/>
<style type="text/css">
th{
 	font-weight:400;
}
</style>
<!--top tools area-->
<div class="tools">
	<!-- <div class="input-field" style="width: 160px;">
		<span>任务名称：</span>
		<input id="missionName" class="form-control indent-4-5" placeholder="名称"/>
	</div> -->
	<!-- <div class="input-field" style="width: 145px;">
		<span>快速查询：</span>
		<input id="projectNum" class="form-control indent-4-5" placeholder="项目编号"/>
	</div>
	
	<div id="depart" class="input-field" style="width: 145px;">
		<span>研发部门：</span>
		<input id="department" class="form-control indent-4-5" placeholder="部门名称"/>
	</div> -->
	
	<!-- <div class="input-field" style="width: 180px;">
	    <select id="statu" class="form-control chzn-select">
	    	<option value="">-请选择状态-</option>
	    	<option value="0">分配</option>
	    	<option value="1">接受</option>
	    	<option value="2">完成</option>
	    	<option value="3">终止</option>
	    </select>
	</div> -->
	
	<!-- <button id="searchCon" type="button" schkUrl="singleTestTaskAction!magrTaskListLoad" class="btn btn-default" style="background: #1E7CFB;border: 1px solid #1E7CFB;color: #ffffff;" onclick="searchOtherMission()"><i class="glyphicon glyphicon-search"></i>查询</button> -->
	<!-- <button id="resetCon" type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="resetInfo()"><i class="glyphicon glyphicon-trash"></i>重置</button> -->
	<!-- <div style="float:right"> -->
		<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showAddWin()"><i class="glyphicon glyphicon-plus"></i>新增一级用例类型</button>
		<!-- <button type="button" class="btn btn-default managerAdmin" style="border: 1px solid #1E7CFB;color: #1E7CFB;display:none" onclick="showExamineWin()"><i class="glyphicon glyphicon-pencil"></i>审核</button> -->
		<!-- <button id="editCon" type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showEditWin()"><i class="glyphicon glyphicon-pencil"></i>修改</button>
		<button id="deleteCon" type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showdelConfirm()"><i class="glyphicon glyphicon-remove"></i>删除</button> -->
	<!-- </div> -->
</div><!--/.top tools area-->
<!-- 测试用例分类显示列表 -->
<table id="testLibraryDg"></table>

<!-- 新增/修改用例类型模态窗 -->
<div id="addOrEditWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	minimizable:false,
	maximizable:false,
	collapsible:false,
	closed:true">
	<form id="addOrEditForm" method="post">
		<table class="form-table">
		    <!--  隐藏字段，新增，修改提交时传到后台 -->
			<tr class="hidden">
				<td>
					<input name="dto.testCaseLibrary.libraryId"/>
					<input name="dto.testCaseLibrary.createTime"/>
					<input name="dto.testCaseLibrary.updateTime"/>
					<input id="libraryCode" name="dto.testCaseLibrary.libraryCode"/>
					<input id="parId" name="dto.testCaseLibrary.parentId"/>
					<input id="createUserId" name="dto.testCaseLibrary.createUserId"/>
				</td>
			</tr>
			<tr>
	    		<th><sup>*</sup>用例类型：</th>
	    		<td><input class="exui-textbox" name="dto.testCaseLibrary.testcaseType" data-options="required:true,validateOnCreate:false" style="width:180px"/></td>
	    	</tr>
		</table>
	</form>
	<footer id="addOrEditFooter" style="padding:5px;text-align:right">
		<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submit()">保存</a>
		<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeWin()" style="border: 1px solid #1e7cfb;color: #1e7cfb;">取消</a>
	</footer>
</div>


<!-- 审核弹窗 -->
<div id="examineWin" class="exui-window" style="display:none;" data-options="
	width:600,
	modal:true,
	minimizable:false,
	maximizable:false,
	closed:true">
	<table id="examineDg" data-options="
	fitColumns: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info']
"></table>
</div>
<script src="<%=request.getContextPath()%>/itest/js/testLibrary/testLibraryTree.js" type="text/javascript" charset="utf-8"></script>