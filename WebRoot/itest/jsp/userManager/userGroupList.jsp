<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.css"/>
<!--top tools area-->
<%@include file="/itest/jsp/chooseMuiltPeople/chooseMuiltPeople.jsp" %>
<style> 

.tools button ,#addBtn,.tools button:hover{
  border: 1px solid #1E7CFB;
    color: #1E7CFB;
}

 .tools button:first-of-type,#addBtn{
  background: #1E7CFB;
    border: 1px solid #1E7CFB;
    color: #ffffff;
}   
.btnClose, .btnClose:hover {
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
</style>
<div class="tools" id="userGroupTool">
<!-- 	<button id="menu_tree_switch_btn" type="button" class="btn btn-default">
		<span class="switch-btn-arrow"></span>
		<span class="switch-btn"></span>
	</button>  -->
	<div class="input-field" style="width: 180px;">
		<span>快速查询：</span>
		<input id="groupNameWin" placeholder="组名+回车键" class="form-control indent-4-5" />
	</div>
	<!-- 查询：
	<div class="input-field" style="width: 145px;">
		<input class="form-control " id="groupNameWin" placeholder="请输入组名"/>
	</div> -->
	
	<div class="input-field" style="width: 160px;margin: -30px 5px 30px -5px;"> 
	<!-- <select style="overflow:scroll;max-height:100px;" multiple="multiple" id="userNameWin" class="form-control chzn-select">请选择组员 </select> -->
	<input class="exui-combobox userNameWin" id="userNameWin" style="max-width:300px;width:200px" placeholder="请选择组员" data-options="
	required:false,
	validateOnCreate:false,
	valueField:'keyObj',
	textField:'valueObj',
	multiple:true,
	editable:false,
	prompt:'请选择组员'"/>
	</div>
	<div class="input-field"  style="display:none"> 
		<input class="form-control " id="userIdsWin" placeholder="请选择组员" />
	</div>
	<img class="searchLogo" src="<%=request.getContextPath()%>/itest/images/mSearch.png" onclick="selectMembers('userNameWin')" style="cursor:pointer;margin-left:50px" title="选择人员">
    <!-- <button type="button" style="margin-left:50px" class="btn btn-default" onclick="selectMembers('userNameWin')"><i class=" "></i>选择人员</button>  -->
    <button  type="button" class="btn btn-default" onclick="searchGroup()"><i class="glyphicon glyphicon-search"></i>查询</button> 
	<button type="button" class="btn btn-default" onclick="resetGroup()"><i class="glyphicon glyphicon-repeat"></i>重置</button>
	<button type="button" id="addGroup" class="btn btn-default" onclick="showAddGroupWin()" schkUrl="userManagerAction!addGroup"><i class="glyphicon glyphicon-plus"></i>增加</button>
	<button type="button" id="updGroupBtn" class="btn btn-default" onclick="showEditGroupWin()"  schkUrl="userManagerAction!updGroup"><i class="glyphicon glyphicon-pencil"></i>修改</button>
	<button type="button" id="delGroupBtn" class="btn btn-default" onclick="showdelGroupConfirm()" schkUrl="userManagerAction!delGroup"><i class="glyphicon glyphicon-remove"></i>删除</button>
</div><!--/.top tools area-->

<table id="userGroupList001" data-options="
	fitColumns: true, 
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info'] 
"></table>
<!-- 	layout:['first','prev','links','next','last','refresh'] -->
<!-- 新增/修改单井模态窗 -->
<div id="addOrEditGroupWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 510,
	footer:'#addOrEditGroupFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
<!-- 	<input class="exui-textbox" name="dto.role.roleName" data-options="required:true,validateOnCreate:false,prompt:'多个IP用分号(;)隔开'" style="width:75%"/> -->
	
		<form id="addOrEditGroupForm" method="post" style="margin-top:10px;margin-left:60px">
			
	<div  style="width:100%;margin-bottom:20px">
		<div style="float:left;width:15%"><sup>*</sup>组名：</div><input class="form-control " name="dto.group.name" style="float:left;width:50%" id="groupName" placeholder="请输入组名"/>
	</div>
	
	<div  style="width:100%;float:left;margin:20px 0">
		<div style="float:left;width:15%"><sup>*</sup>组员：</div>
		<!-- <input class="form-control " name="userNames" style="float:left;width:50%" id="userNames" onclick="selUsers('')" placeholder="请选择组员"/> -->
	<input class="exui-combobox userNameAddWin" name="dto.group.userIds" id="userNameAddWin" style="max-width:300px;width:210px" placeholder="请选择组员" data-options="
	required:false,
	validateOnCreate:false,
	valueField:'keyObj',
	textField:'valueObj',
	multiple:true,
	editable:false,
	prompt:'请选择组员'"/>
	<img class="searchLogo" src="<%=request.getContextPath()%>/itest/images/mSearch.png" onclick="selectMembers('userNameAddWin')" style="cursor:pointer; " title="选择人员">
    
	<!-- <button id="addBtn" type="button" class="btn btn-default" onclick="selectMembers('userNameAddWin')"><i class=" "></i>选择人员</button>  -->
	 </div>
	
	<div  style="width:100%;float:left;margin-bottom:20px">
		<div style="float:left;width:15%">备注：</div><input class="form-control " name="dto.group.remark" style="float:left;width:50%" id="remark" placeholder="请输入备注"/>
	</div>
	<input  id="groupId01" style="display:none;width:280px" name="dto.group.id"  />
<!-- 	<input id="userIds" hidden="true" name="dto.group.userIds" type="text" style="width:380px"/> -->
 
			<div class="hidden "><div>
				<input id="id" name="dto.user.id"/> 
			</div></div>
	      
	    </form>

</div>
<div id="addOrEditGroupFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submit()">保存</a>
	<a class="exui-linkbutton btnClose " data-options="btnCls:'default',size:'xs'" onclick="closeWin()">关闭</a>
</div>


<!-- 用户选择框 -->
<div id="addOrEditUserWin" class="exui-window" style="display:none; " data-options="
	modal:true,
	width: 650,
	footer:'#addOrEditUserFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<div style="margin-bottom:10px">
	   <div class="input-field" style="width: 145px;"> 
		<input class="form-control " id="selUserName" placeholder="请输入组员姓名"/>
	</div>
	<button type="button" class="btn btn-default" onclick="searchUsers()"><i class="glyphicon glyphicon-search"></i>查询</button>
    </div>
    <div  style="width:100%">
 
    <div style="width:50%;float:left">
	<table id="userAllSelect" data-options="
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
    <table id="userSelectAdd" data-options="
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

<div id="addOrEditUserFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submitUser()">确定</a>
	<a class="exui-linkbutton btnClose" data-options="btnCls:'default',size:'xs'" onclick="closeUserWin()">关闭</a>
</div>
<script src="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/itest/js/userManager/userGroupList.js" type="text/javascript" charset="utf-8"></script>