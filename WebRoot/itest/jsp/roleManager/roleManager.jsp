<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
.tools button ,.tools button:hover{
  border: 1px solid #1E7CFB;
    color: #1E7CFB;
}

.datagrid-header {
	height: 40px;
}
.datagrid-htable{
	height: 40px;
}
.datagrid-body td{
    padding: 11px 0;
}

div[class="datagrid-header-check"] input{
  display:none
}

.indent-60{
   text-indent: 65px;
}

</style>
<!--top tools area-->
<div class="tools" id="roleManageTool">
	<div class="input-field" style="width: 210px;">
	<span>快速查询：</span>
		<input class="form-control indent-60" id="queryParam" placeholder="角色名+回车键"/>
	</div>
<!--  	<button type="button" class="btn btn-default" id="queryRole" ><i class="glyphicon glyphicon-search"></i>查询</button>
 -->	<button type="button" class="btn btn-default" id="addRoleBtn" onclick="showAddRoleWin()"  schkUrl="roleAction!new"><i class="glyphicon glyphicon-plus"></i>增加</button>
	<button type="button" class="btn btn-default" id="updateRoleBtn" onclick="showEditRoleWin()"  schkUrl="roleAction!update"><i class="glyphicon glyphicon-pencil"></i>修改</button>
	<button type="button" class="btn btn-default" id="deleteRoleBtn" onclick="showDelRoleConfirm()"  schkUrl="roleAction!delete"><i class="glyphicon glyphicon-remove"></i>删除</button>
	
	<button type="button" class="btn btn-default" style="margin-left: 24px;" id="authDetail" schkUrl="roleAction!browserAuth"><i class="glyphicon glyphicon-search"></i>权限详情</button>
	<button type="button" class="btn btn-default" id="authEdit" schkUrl="roleAction!grantRoleAuth"><i class="glyphicon glyphicon-pencil"></i>权限维护</button>
</div><!--/.top tools area-->

<table id="roleListTb" data-options="
	fitColumns: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info'],
	pageList: [10,30,50,100]
"></table>


<!-- 新增/修改单井模态窗开始 -->
<div id="addOrEditRoleWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 580,
	footer:'#addOrEditRoleFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<table class="form-table" style="width:100%">
		<form id="addOrEditRoleForm" method="post">
			<tr class="hidden">
			  <td><input class="exui-textbox"  name="dto.role.roleId" id="roleId"/></td>
			</tr>
			<tr>
				<th style="width:25%"><sup>*</sup>角色名称：</th>
	    		<td><input class="exui-textbox" name="dto.role.roleName" id="roleName" style="width:75%"/></td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">可访问IP：</th>
	    		<td>
	    		   <textarea class="exui-textbox"  name="dto.role.accessIp" id="accessIp" data-options="prompt:'多个IP用分号(;)隔开'" style="width:75%"></textarea>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">备注：</th>
	    		<td>
	    		  <textarea class="exui-textbox" name="dto.role.remark"  style="width:75%"></textarea>
	    		</td>
	    	</tr>
	    </form>
	</table>
</div>
<div id="addOrEditRoleFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submitRole()">保存</a>
	<a class="exui-linkbutton" style=" border: 1px solid #1E7CFB;color: #1E7CFB;" data-options="btnCls:'default',size:'xs'" onclick="closeRoleWin()">关闭</a>
</div>
<!-- 新增/修改单井模态窗结束 -->

<script src="<%=request.getContextPath()%>/itest/js/roleManager/commonForRole.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/itest/js/roleManager/roleManager.js" type="text/javascript" charset="utf-8"></script>