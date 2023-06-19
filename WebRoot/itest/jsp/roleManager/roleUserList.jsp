<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--top tools area-->
<div class="tools">
	<button type="button" class="btn btn-default" onclick="showQueryWin()"><i class="glyphicon glyphicon-search"></i>查询</button> 
    <button type="button" class="btn btn-default" onclick="closeRoleUserListWin()"><i class="glyphicon glyphicon-off"></i>关闭</button>
</div><!--/.top tools area-->
<!--账号列表开始-->
<table id="roleUserListTb" data-options="
	fitColumns: true,
	rownumbers: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info'],
	pageList: [10,30,50,100]
"></table>
<!-- 账号列表结束 -->

<!-- 查询账号弹出框开始 -->
<div id="queryUserWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 580,
	footer:'#queryUserFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<table class="form-table" style="width:100%">
		<form id="queryUserForm" method="post">
			<tr class="hidden">
			  <td><input class="exui-textbox"  name="dto.role.roleId"/></td>
			</tr>
			<tr>
				<th style="width:25%">登录账号：</th>
	    		<td><input class="exui-textbox" name="dto.user.loginName"  style="width:75%"/></td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">员工编号：</th>
	    		<td>
	    		   <textarea class="exui-textbox"  name="dto.user.employeeId" style="width:75%"></textarea>
	    		</td>
	    	</tr>
	    
	    	<tr>
	    		<th  style="width:25%">办公电话：</th>
	    		<td>
	    		  <textarea class="exui-textbox" name="dto.user.officeTel" style="width:75%"></textarea>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">用户状态：</th>
	    		<td>
	    		  <select class="exui-combobox" name="dto.user.status" id="roleUserStatus" style="width:75%">
	    		  </select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">所属组：</th>
	    		<td>
	    		  <select class="exui-combobox" name="dto.user.groupIds" id="roleUsergroupIds"  style="width:75%"></select>
	    		</td>
	    	</tr>
	    </form>
	</table>
</div>
<div id="queryUserFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" id="queryUser">查询</a>
	<a class="exui-linkbutton" style=" border: 1px solid #1E7CFB;color: #1E7CFB;" data-options="btnCls:'default',size:'xs'" id="closeQueryUserDlg">重置</a>
</div>
<!-- 查询账号弹出框结束 -->

<script src="<%=request.getContextPath()%>/itest/js/roleManager/roleUserList.js" type="text/javascript" charset="utf-8"></script>