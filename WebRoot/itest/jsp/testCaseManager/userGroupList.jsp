<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<style type="text/css">
	#comboboxdiv .textbox.combo {position: static;}
</style>
<!-- 用户选择框 -->
<div id="selectUserWin" class="exui-window" style="display:none; " data-options="
	modal:true,
	width: 650,
	footer:'#selectUserFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<div class="tools" data-options="">
		<div id="comboboxdiv"  class="input-field" style="width: 145px;">
			<!-- <span>选择组：</span> -->
			<input id="userGroup" class="exui-combobox" name="dto.group.id" 
			data-options="
   			validateOnCreate:false,
   			editable:false,
   			prompt:'-选择组-' " style="width:145px;position: static;"/>
   			<input id="userGroupId" type="hidden">
		</div>
	    <div class="input-field" style="width: 145px;"> 
		<input class="form-control " id="selectUserName" placeholder="请输入组员姓名"/>
		</div>
		<button type="button" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" style="padding: 4px 12px;" onclick="searchUsersF()"><i class="glyphicon glyphicon-search"></i>查询</button>
    </div>
    <div  style="width:100%">
 
    <div style="width:50%;float:left">
	<table id="userDefAllSelect" data-options="
	    height: 400,
		fitColumns: true,
		rownumbers: true,
		singleSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 100,
		layout:['first','prev','links','next','last','refresh']
	"></table>
    </div>
    
    
    
    <div style="width:47%;float:left; margin-left:15px">
    <table id="userSelected" data-options="
	    height: 400,
		fitColumns: true,
		rownumbers: true,
		singleSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 100,
		layout:['first','prev','links','next','last','refresh']
	"></table>  
    </div>
			
</div>

</div>

<div id="selectUserFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submitSelectUser()">确定</a>
	<a class="exui-linkbutton bntcss" data-options="btnCls:'default',size:'xs'" onclick="cancleUserWin()">取消</a>
</div>
<script src="<%=request.getContextPath()%>/itest/js/testCaseManager/userGroupList.js" type="text/javascript" charset="utf-8"></script>