<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<style>
.m-b-10{
margin-bottom:10px
}
.m-l-11{
margin-left:11px
}
.m-l-20{
margin-left:20px
}
.m-l-30{
margin-left:30px
} 

.tools button ,.tools button:hover{
  border: 1px solid #1E7CFB;
    color: #1E7CFB;
}

/* .tools button:first-of-type{
  background: #1E7CFB;
    border: 1px solid #1E7CFB;
    color: #ffffff;
}  */ 
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
<!--top tools area-->
<div class="tools" id="userManageTool">
<!-- 	<button id="menu_tree_switch_btn" type="button" class="btn btn-default">
		<span class="switch-btn-arrow"></span>
		<span class="switch-btn"></span>
	</button>  -->
	<!-- 查询： -->
	<!-- <div class="input-field" style="width: 145px;">
		<input class="form-control " id="userNameSearch" placeholder="请输入真实姓名"/>
	</div>
	
	<div class="input-field" style="width: 145px;"> 
		<input class="form-control " id="loginNameSearch" placeholder="请输入登陆账号"/>
	</div> -->
	<div class="input-field" style="width: 210px;">
		<span>快速查询：</span>
		<input id="userNameSearch" placeholder="真实姓名+回车键" class="form-control indent-4-5" />
	</div>
	<div class="input-field" style="width: 210px;">
		<span>快速查询：</span>
		<input id="loginNameSearch" placeholder="登陆账号+回车键" class="form-control indent-4-5" />
	</div>
	<!-- <button type="button" class="btn btn-default" onclick="searchUserInfo()"><i class="glyphicon glyphicon-search"></i>查询</button> -->
	
	<button type="button" id="addButton" class="btn btn-default" schkUrl="userManagerAction!addUser" onclick="showAddWin()"><i class="glyphicon glyphicon-plus"></i>增加</button>
	<button type="button" class="btn btn-default" onclick="upload();" schkUrl="userManagerAction!addUser"><i class="glyphicon glyphicon-arrow-up"></i>导入</button>
	<a type="button" style="border: 1px solid #1e7cfb;color: #1e7cfb;" class="btn btn-default" onclick="downloadExcelModel(this);" schkUrl="userManagerAction!addUser"><i class="glyphicon glyphicon-arrow-down"></i>下载Excel导入摸板</a>
	<button type="button" id="resetButton" class="btn btn-default" onclick="resetInput()"><i class="glyphicon glyphicon-repeat"></i>重置</button>
	<button type="button" id="chgButton"class="btn btn-default" schkUrl="userManagerAction!updUser" onclick="showEditWin()"><i class="glyphicon glyphicon-pencil"></i>修改</button>
	<button type="button" id="deleteButton"class="btn btn-default" schkUrl="userManagerAction!ldeleteUser" onclick="showdelConfirm()"><i class="glyphicon glyphicon-remove"></i>删除</button>
	<button type="button" id="rePasswordButton" class="btn btn-default" schkUrl="userManagerAction!update2Init" onclick="resetPassword()"><i class="glyphicon glyphicon-repeat"></i>密码重置</button>
	<button type="button" id="adminBtn" class="btn btn-default" style="display:none" onclick="setAsAdmin()"><i class="glyphicon glyphicon-remove"></i></button>
	<button type="button" id="swichButton" class="btn btn-default" schkUrl="userManagerAction!swUserStatus" onclick="toggoleStatus()"><i class="glyphicon glyphicon-retweet"></i>切换状态</button>
</div><!--/.top tools area-->

<div id="userList001" data-options="
	fitColumns: true, 
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info']
"></div>

<!-- 新增/修改单井模态窗 -->
<div id="addOrEditWinUser" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 610,
	footer:'#addOrEditFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
<!-- 	<input class="exui-textbox" name="dto.role.roleName" data-options="required:true,validateOnCreate:false,prompt:'多个IP用分号(;)隔开'" style="width:75%"/> -->
	<table class="form-table" style="width:100%">
		<form id="addOrEditFormUser" method="post" style="margin-top:10px;margin-left:10px">
			<div style="display:none"><div>
				<input id="id01" name="dto.user.id"/>
				<input id="isAdmin" name="dto.user.isAdmin"/>
				<input id="delFlag" name="dto.user.delFlag"/>
				<input id="chgPwdFlg" name="dto.user.chgPwdFlg"/>
				<input id="status" name="dto.user.status"/>
				<input id="myhome" name="dto.user.myHome"/>
				<!-- <input id="attachUrl" name="dto.user.planDocName"/>  -->
			</div></div>
			
			<div class="m-b-10 ">
			<span>
				<sup>*</sup>登陆账号：
	    		<input class="exui-textbox" id="loginName" data-options="required:true,validateOnCreate:false"  name="dto.user.loginName"
	    		    style="width:280px" />
	        </span>
	    	</div> 
	    	<div class="m-b-10 editShow">
			<span>
				<sup>*</sup>原始密码：
	    		<input class="exui-textbox" id="oldPwd01" type="password" data-options="required:true,validateOnCreate:false"  name="dto.user.oldPwd"
	    		    style="width:280px"/>
	        </span>
	        </div> 
	        
	    	<div class="m-b-10 ">
	    	<span >
	    		<sup>*</sup><p id="passwordText" style="display: inline; ">登陆密码：</p>
	    		<input type="password" class="exui-textbox" id="password01"  name="dto.user.password" data-options="required:true,validateOnCreate:false" style="width:180px"/>
	    	
	    		</span>
	    	    <span style="margin-left: 20px">
	    		<sup>*</sup>确认密码：
	    		<input id="rePwd02" type="password" class="exui-textbox" name="repwd" data-options="events:{blur:pwdClear},required:true,validateOnCreate:false" style="width:180px"/>
	    	    
	    		</span>
	    	</div>
	    	
	    	<div class="m-b-10 ">
	    	    <span >
	    		<sup>*</sup>真实姓名：
	    		<input id="name" class=" exui-textbox" name="dto.user.name" data-options="required:true,validateOnCreate:false" style="width:180px"/>
	    	    </span>
	    		<span style="margin-left: 32px">电子信箱：
	    		<input id="email" class="exui-textbox" data-options="validateOnCreate:false " name="dto.user.email" style="width:180px"/>
	    		</span>
	    	</div>
	    	<div class="m-b-10 m-l-11">
	    	
	    	    <span>
	    		联系电话：
	    		<input id="tel" class="exui-textbox" style="width:180px" name="dto.user.tel"/>
	    		</span>
	    		<span class="m-l-30">
	    		办公电话：
	    		<input id="officeTel" class="exui-textbox" name="dto.user.officeTel" data-options=" validateOnCreate:false,validateOnBlur:true" style="width:180px"/>
	    		</span>
	    	</div>
	    	
	    	<div class="m-b-10 m-l-11">
	    	<span  >
	    		员工编号：
	    		<input id="employeeId" class="exui-textbox" data-options="validateOnCreate:false " name="dto.user.employeeId" style="width:100px"/>
	    	</span>
	    	<span style="margin-left:10px">
	    		职务：&nbsp;<input id="headShip" style="width:100px;" class="exui-textbox" name="dto.user.headShip"/>
	    		</span>
	    		<span  style="margin-left:10px">
	    		所属组：&nbsp;
	    		<input class="exui-combobox " id="groupNames01" name="dto.user.groupIds" style="max-width:135px; " placeholder="请选择组" data-options="
				required:false,
				validateOnCreate:false,
				valueField:'keyObj',
				textField:'valueObj',
				multiple:true,
				editable:false,
				prompt:'请选择组'"/>
<!-- <input id="groupNames01" readonly="readonly" disabled="disabled" class="exui-textbox" name="dto.user.groupNames" data-options="validateOnCreate:false,validateOnBlur:true" style="width:135px; "/> -->
	    		</span>
	    	</div>
	    	<div class="m-b-10 m-l-11">
	    	<span>
	    		安全问题：
	    			<input id="question" style="width:180px" class="exui-textbox" name="dto.user.question"/></span>
	    	<span style="margin-left:57px">		
	    		答案：
	    			<input id="answer" style="width:180px"class="exui-textbox" name="dto.user.answer"/></span>
	    		
	    	</div>
	    	<div>
	    	  	<div style="position:relative;bottom:10px;width:50%;text-align:center">（找回密码时使用）</div>
	    		
	    		 
	    	</div> 
	    </form>
	    	
	</table>

</div>
<div id="addOrEditFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submit()">保存</a>
	<a id="continueBtn" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submit('continue')">保存并继续</a>
	<a class="exui-linkbutton btnClose" data-options="btnCls:'default',size:'xs'" onclick="closeWin()">关闭</a>
</div>

<!-- 从excel导入测试用例模态窗 -->
<div id="uploadFromExcelWin" class="exui-window" data-options="
	modal:true,
	width: 450,
	footer:'#uploadFromExcelFoot',
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true">
	<form id="uploadForm" class="form-horizontal" method="post" enctype="multipart/form-data" role="form">
		<div class="file-box">
			<input id="importFile" accept=".xls,.xlsx" name="dto.importUser" type="file">
			<br/><span style="color:red">请按照模板要求导入数据，否则导入将会不成功！</span>
		</div>
	</form>
</div>
<div id="uploadFromExcelFoot" align="right">
	<a href="#" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="sureToUpload()">确认导入</a>
	<a href="#" class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeUploadFromExcelWin()"  style="border: 1px solid #1e7cfb;color: #1e7cfb;">取消</a>
</div>



<script src="<%=request.getContextPath()%>/itest/js/userManager/userManager.js" type="text/javascript" charset="utf-8"></script>