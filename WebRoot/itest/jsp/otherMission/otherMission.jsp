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
	<div class="input-field" style="width: 160px;">
		<span>任务名称：</span>
		<input id="missionName" class="form-control indent-4-5" placeholder="名称"/>
	</div>
	<!-- <div class="input-field" style="width: 145px;">
		<span>快速查询：</span>
		<input id="projectNum" class="form-control indent-4-5" placeholder="项目编号"/>
	</div>
	
	<div id="depart" class="input-field" style="width: 145px;">
		<span>研发部门：</span>
		<input id="department" class="form-control indent-4-5" placeholder="部门名称"/>
	</div> -->
	<div class="input-field" style="width: 180px;">
	    <select id="projectNa" class="form-control chzn-select">
	    	
	    </select>
	</div>
	<div class="input-field" style="width: 180px;">
	    <select id="statu" class="form-control chzn-select">
	    	<option value="">-请选择状态-</option>
	    	<option value="0">分配</option>
	    	<option value="1">进行中</option>
	    	<option value="2">完成</option>
	    	<option value="3">终止</option>
	    	<option value="4">暂停</option>
	    </select>
	</div>
	
	<button type="button" class="btn btn-default" style="background: #1E7CFB;border: 1px solid #1E7CFB;color: #ffffff;" onclick="searchOtherMission()"><i class="glyphicon glyphicon-search"></i>查询</button>
	<!-- <button id="resetCon" type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="resetInfo()"><i class="glyphicon glyphicon-trash"></i>重置</button> -->
	<!-- <div style="float:right"> -->
		<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showAddWin()"><i class="glyphicon glyphicon-plus"></i>创建</button>
		<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showEditWin()"><i class="glyphicon glyphicon-pencil"></i>修改</button>
		<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showdelConfirm()"><i class="glyphicon glyphicon-remove"></i>删除</button>
	<!-- </div> -->
</div><!--/.top tools area-->
<!-- 其他任务显示列表 -->
<table id="missionDg" data-options="
	fitColumns: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	pageList:[10,30,50],
	layout:['list','first','prev','manual','next','last','refresh','info']
"></table>

<!-- PM模态窗 -->
<!-- <div id="selectPmWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 550,
	minimizable:false,
	maximizable:false,
	closed:true">
	<table class="form-table">
		<form id="pMForm" method="post">
			<tr class="hidden">
				<td>
					<input id="missionId" name="dto.otherMission.missionId"/>
				</td>
			</tr>
	    
	    	<tr>
	    		<th>用户组：</th>
	    		<td><input class="exui-combobox" id="userGroupM" name="dto.group.id" data-options="
	    			prompt:'-请选择-'" style="width:160px"/></td>
	    			
	    		<th style="padding-left: 21px;">姓名：</th>
	    		<td><input class="exui-textbox" name="dto.userName" data-options="required:true,validateOnCreate:false" style="width:160px"/></td>
	    		
	    		<td><button style="margin-left: 15px;" type="button" onclick="" class="btn btn-primary">查询</button></td>
	    	</tr>
			
	    </form>
	</table>
	
	<table id="pMList" data-options="
	fitColumns: true,
	rownumbers: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 8,
	layout:['list','first','prev','manual','next','last','refresh','info']
	
"></table>
	
</div> -->

<!-- 新增/修改单其他任务模态窗 -->
<div id="addOrEditWin" class="exui-window" style="display:none;width:700px;" data-options="
	modal:true,
	width: 580,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true">
	<form id="addOrEditForm" method="post">
		<table class="form-table">
		    <!--  隐藏字段，新增，修改提交时传到后台 -->
			<tr class="hidden">
				<td>
					<input id="missioId" name="dto.otherMission.missionId"/>
					<input id="actualWorkload" name="dto.otherMission.actualWorkload"/>
					<input id="completionDegree" name="dto.otherMission.completionDegree"/>
					<input id="projectType" name="dto.otherMission.projectType"/>
					<input id="createTime" name="dto.otherMission.createTime"/>
					<input id="updateTime" name="dto.otherMission.updateTime"/>
					<input id="missionStatus" name="dto.otherMission.status"/>
					<input id="createUserId" name="dto.otherMission.createUserId"/>
					<input id="missionNum" name="dto.otherMission.missionNum"/>
				</td>
			</tr>
			<tr>
	    		<th><sup>*</sup>任务名称：</th>
	    		<td><input class="exui-textbox missionNa" name="dto.otherMission.missionName" data-options="required:true,validateOnCreate:false" style="width:180px"/></td>
	    	</tr>
	    	<tr>
	    		<th><sup>*</sup>任务描述：</th>
	    		<td><span style="width: 180px; height: 80px;display: inline-block;position: relative;"><textarea name="dto.otherMission.description" style="resize:none;width:475px;height: 80px;"></textarea></span></td>
	    	</tr>
			<tr>
				<th><sup>*</sup>执行人员：</th>
	    		<td><input id="peopleList" class="exui-combobox peopleList" data-options="
	    			required:true,
	    			validateOnCreate:false,
	    			valueField:'id',
	    			textField:'name',
	    			multiple:true,
	    			editable:false,
	    			prompt:'-请选择-'" style="width:180px"/></td>
	    			<th style="text-align:left"><img class="searchLogo" src="<%=request.getContextPath()%>/itest/images/mSearch.png" onclick="showSeletctPeopleWindow('peopleList')" style="cursor:pointer;margin-left:5px" title="选择人员"><!-- <a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="showSeletctPeopleWindow('peopleList')">选择人员</a> --></th>
	    			<!-- <th><sup>*</sup>负责人：</th>
	    			<td><input id="inchargePeople" class="exui-combobox inchargePeople" name="dto.otherMission.chargePersonId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			valueField:'id',
		    			textField:'name',
	    				editable:false,
		    			prompt:'-请选择-'" style="width:180px"/></td> -->
	    	</tr>
	    	<tr>
				<th><sup>*</sup>负责人：</th>
	    			<td><input id="inchargePeople" class="exui-combobox inchargePeople" name="dto.otherMission.chargePersonId" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			valueField:'id',
		    			textField:'name',
	    				editable:false,
		    			prompt:'-请选择-'" style="width:180px"/></td>
	    	</tr>
	    	<tr>
				<th>关注者：</th>
	    		<td><input id="concernList" class="exui-combobox concernList" data-options="
	    			validateOnCreate:false,
	    			valueField:'id',
	    			textField:'name',
	    			multiple:true,
	    			editable:false,
	    			prompt:'-请选择-'" style="width:180px"/></td>
	    		<th style="text-align:left"><img class="searchLogo" src="<%=request.getContextPath()%>/itest/images/mSearch.png" onclick="showSeletctPeopleWindow('concernList')" style="cursor:pointer;margin-left:5px" title="选择人员"><!-- <a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="showSeletctPeopleWindow('concernList')">选择人员</a> --></th>
	    	</tr>
	    	<tr>
				<th>任务类别：</th>
	    		<td><input id="missionCategory" class="exui-combobox missionCategory" name="dto.otherMission.missionCategory" data-options="
	    			validateOnCreate:false,
	    			valueField:'typeId',
	    			textField:'typeName',
	    			editable:false,
	    			prompt:'-请选择-'" style="width:180px"/>
	    		</td>
	    		<!-- <th>任务类型：</th>
	    		<td><input id="missionType" class="exui-combobox" name="dto.otherMission.missionType" data-options="
	    			required:true,
	    			validateOnCreate:false,
	    			valueField:'value',
	    			textField:'desc',
	    			prompt:'-请选择-',
	    			data:[{desc:'其他任务',value:'0'}]" style="width:180px"/>
	    		</td> -->
	    	</tr>
	    	<tr>
				<th>所属项目：</th>
	    		<td>
		    		<!-- <input id="projectIds" class="exui-combobox projectIds" name="dto.otherMission.projectId" data-options="
		    			validateOnCreate:false,
		    			valueField:'projectId',
		    			textField:'projectName',
		    			editable:false,
		    			prompt:'-请选择-'" style="width:180px"/> -->
		    		<select id="projectIds" class="form-control chzn-select projectIds" name="dto.otherMission.projectId" onchange="changeProject(this)">
	    	
	    			</select>
	    		</td>
	    		<th><a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="addProject()">新增项目</a></th>
	    	</tr>
	    	<tr>
				<th>紧急程度：</th>
	    		<td><input id="emergencyDegree" class="exui-combobox emergencyDegree" name="dto.otherMission.emergencyDegree" data-options="
	    			validateOnCreate:false,
	    			valueField:'typeId',
	    			textField:'typeName',
	    			editable:false,
	    			prompt:'-请选择-'" style="width:180px"/>
	    		</td>
	    		<th>难易程度：</th>
	    		<td><input id="difficultyDegree" class="exui-combobox difficultyDegree" name="dto.otherMission.difficultyDegree" data-options="
	    			validateOnCreate:false,
	    			valueField:'typeId',
	    			textField:'typeName',
	    			editable:false,
	    			prompt:'-请选择-'" style="width:180px"/>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th>预计开始日期：</th>
	    		<td><input class="exui-datebox" data-options="validateOnCreate:false,prompt:'-请选择-',editable:false" name="dto.otherMission.predictStartTime" style="width:180px"/></td>
	    		<th>预计结束日期：</th>
	    		<td><input class="exui-datebox" data-options="validateOnCreate:false,prompt:'-请选择-',editable:false" name="dto.otherMission.predictEndTime" style="width:180px"/></td>
	    	</tr>
	    	<tr>
	    		<th>标准工作量(小时)：</th>
	    		<td><input class="exui-textbox" name="dto.otherMission.standardWorkload" data-options="validateOnCreate:false,validType:'zhengshu',prompt:'请填写非负整数'" style="width:180px"/></td>
	    	</tr>
		</table>
	</form>
	<footer style="padding:5px;text-align:right">
		<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submit()">保存</a>
		<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeWin()" style="border: 1px solid #1e7cfb;color: #1e7cfb;">取消</a>
	</footer>
</div>


<!-- 新增项目模态窗 -->
<div id="addProject" class="exui-window" style="display:none;width:300px;" data-options="
	modal:true,
	width: 180,
	minimizable:false,
	maximizable:false,
	closed:true">
	<form id="addProjectForm" method="post">
		<table class="form-table">
			<tr>
				<th><sup>*</sup>项目名称：</th>
	    		<td><input class="exui-textbox" name="dto.project.projectName" data-options="required:true,validateOnCreate:false" style="width:130px"/></td>
	    	</tr>
		</table>
	</form>
	<footer style="padding:5px;text-align:right">
		<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submitProject()">保存</a>
		<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeProjectWin()" style="border: 1px solid #1e7cfb;color: #1e7cfb;">取消</a>
	</footer>
</div>
<!-- 引入多选页面 -->
<%@include file="/itest/jsp/chooseMuiltPeople/chooseMuiltPeople.jsp" %>


<script src="<%=request.getContextPath()%>/itest/js/otherMission/otherMission.js" type="text/javascript" charset="utf-8"></script>