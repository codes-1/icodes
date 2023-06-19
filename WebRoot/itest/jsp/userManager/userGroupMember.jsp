<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/roleManager/roleUserManager.css"/>
<style>
  .datagrid-header-check>input{
    display:none
  }
</style>
<!--tab area-->
  <div class="tab_div">
   <ul class="tab_menu" >
       <li class="tab_li_lf active" id="roleInnerTab" ><a >组外账户</a></li><!-- <li class="tab_li_rt " id="roleOuterTab"><a >角色外账户</a></li> -->
    </ul>
  </div>
<!--/tab area-->

<!-- tabContext -->
<div class="tabwapper" >
	<!-- 角色内账户 -->
	<div id="roleInnerDiv" >
		<!--top tools area-->
		<div class="tools" style="padding-top: 10px;">
		    <button type="button" class="btn btn-default" id="queryOuterData"><i class="glyphicon glyphicon-search"></i>查询</button> 
			<button type="button" class="btn btn-default" id="addOuterData"><i class="glyphicon glyphicon-plus"></i>添加到组</button>
			<button type="button" class="btn btn-default" onclick="closeRoleUserWin()"><i class="glyphicon glyphicon-off"></i>关闭</button>
		</div>
		<!--/.top tools area-->
		<table id="roleInnerListTb" data-options="
			fitColumns: true,
			rownumbers: false,
			singleSelect: true,
			pagination: true,
			pageNumber: 1,
			pageSize: 10,
			layout:['list','first','prev','manual','next','last','refresh','info'],
			pageList: [10,30,50,100]
		"></table>
	</div>
	<!-- /角色内账户 -->
	
	<!-- 角色外账户 -->
    <div id="roleOuterDiv" style="display:none" >
    <!--top tools area-->
		<div class="tools" style="padding-top: 10px;">
<!-- 		 <button type="button" class="btn btn-default" id="queryOuterData"><i class="glyphicon glyphicon-search"></i>查询</button>  
			<button type="button" class="btn btn-default" id="addOuterData"><i class="glyphicon glyphicon-plus"></i>添加到角色中</button>
			<button type="button" class="btn btn-default" onclick="closeRoleUserWin()"><i class="glyphicon glyphicon-off"></i>关闭</button> -->
		</div>
		<!--/.top tools area-->
		<table id="roleOuterListTb" data-options="
			fitColumns: true,
			rownumbers: true,
			singleSelect: false,
			pagination: true,
			pageNumber: 1,
			pageSize: 10,
			layout:['list','first','prev','manual','next','last','refresh','info'],
			pageList: [10,30,50,100]
		"></table>
	  </div> 
	 <!-- /角色外账户 -->
</div>
<!-- /tabContext -->

<!-- 查询账号内弹出框开始 -->
<div id="queryUserInnerWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 580,
	footer:'#queryUserInnerFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<table class="form-table" style="width:100%">
		<form id="queryUserInnerForm" method="post">
			<tr class="hidden">
			  <td><input class="exui-textbox"  name="dto.role.roleId"/></td>
			</tr>
			<tr>
				<th style="width:25%">登录账号：</th>
	    		<td><input class="exui-textbox" name="dto.user.loginName" style="width:75%"/></td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">员工编号：</th>
	    		<td>
	    		   <textarea class="exui-textbox"  name="dto.user.employeeId"  style="width:75%"></textarea>
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
	    		  <select class="exui-combobox" name="dto.user.status" id="roleUser_Status" style="width:75%">
	    		  </select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">所属组：</th>
	    		<td>
	    		  <select class="exui-combobox" name="dto.user.groupIds" id="roleUsergroup_Ids" style="width:75%"></select>
	    		</td>
	    	</tr>
	    </form>
	</table>
</div>
<div id="queryUserInnerFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" id="queryUserInner">查询</a>
	<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style=" border: 1px solid #1E7CFB;color: #1E7CFB;" id="resetUserInnerDlg">重置</a>
</div>
<!-- /查询账号内弹出框结束 -->

<!-- 查询账号外弹出框开始 -->
<div id="queryUserOuterWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 580,
	footer:'#queryUserOuterFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<table class="form-table" style="width:100%">
		<form id="queryUserOuterForm" method="post">
			<tr class="hidden">
			  <td><input class="exui-textbox"  name="dto.group.id"/></td>
			</tr>
			<tr>
				<th style="width:25%">登录账号：</th>
	    		<td><input class="exui-textbox" name="dto.user.loginName"  style="width:75%"/></td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">员工编号：</th>
	    		<td>
	    		   <textarea class="exui-textbox"  name="dto.user.employeeId"  style="width:75%"></textarea>
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
	    		  <option value="">所有</option>
	    		  <option value="1">启用</option>
	    		  <option value="0">禁止</option>
	    		  </select>
	    		</td>
	    	</tr>
	    	<!-- <tr>
	    		<th  style="width:25%">所属组：</th>
	    		<td>
	    		  <select class="exui-combobox" name="dto.user.groupIds" id="roleUsergroupIds"  style="width:75%"></select>
	    		</td>
	    	</tr> -->
	    </form>
	</table>
</div>
<div id="queryUserOuterFooter" align="right" style="padding:5px;text-align:right">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" id="queryUserOuter">查询</a>
	<a class="exui-linkbutton" style=" border: 1px solid #1E7CFB;color: #1E7CFB;" data-options="btnCls:'default',size:'xs'" id="resetUserOuterDlg">重置</a>
</div>
<!-- /查询账号外弹出框结束 -->

 
<script src="<%=request.getContextPath()%>/itest/js/userManager/userGroupMember.js" type="text/javascript" charset="utf-8"></script>