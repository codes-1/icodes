<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<style>
.lineHeight{
margin:10px 0 30px 0
}
.boxLine{
border:0.5px solid rgb(221,221,221);
height: 100%
}
.m-r-30{
margin-right:30px
}
.m-l-13{
margin-left:13px
}
</style>  
 <div id="addOrEditWin" class="boxLine" style="font-size:1.2em">
<!-- 	<input class="exui-textbox" name="dto.role.roleName" data-options="required:true,validateOnCreate:false,prompt:'多个IP用分号(;)隔开'" style="width:75%"/> -->
	<div class="container" style="position: relative; top:10em;left: 8em">
		<form id="addOrEditForm" method="post" style="margin-top:10px;margin-left:10px">
			<div class="hidden "><div>
				<input id="id" name="dto.user.id"/>
				<input id="isAdmin" name="dto.user.isAdmin"/>
				<input id="delFlag" name="dto.user.delFlag"/>
				<input id="chgPwdFlg" name="dto.user.chgPwdFlg"/>
				<input id="status" name="dto.user.status"/>
				<!-- <input id="attachUrl" name="dto.user.planDocName"/>  -->
			</div></div>
		 <div class="lineHeight">
			<span class="m-r-30">
				<sup>*</sup>登陆账号：
	    		<input class="exui-textbox" disabled="true" id="loginName" data-options="required:true,validateOnCreate:false"  name="dto.user.loginName"
	    		    style="width:180px"/>
	        </span>
	        <span class="m-r-30">
	    		<sup>*</sup>登陆密码：
	    		<input id="password" type="password" class="exui-textbox" name="dto.user.oldPwd" data-options="required:true,validateOnCreate:false" style="width:180px"/>
	    	</span>
	    	   <span>
	    		<sup>*</sup>确认密码：
	    		<input id="rePwd" class=" exui-textbox" type="password"  name="dto.user.password" data-options="required:true,validateOnCreate:false"  
	    		    style="width:180px"/>
	    		    </span>
	    </div>
	    
	     <div class="lineHeight">
	    	<span class="m-r-30">
	    		<sup>*</sup>真实姓名：
	    		<input id="name" class=" exui-textbox" name="dto.user.name" data-options="required:true,validateOnCreate:false" style="width:180px"/>
	    	</span>
	    	 <span class="m-r-30 m-l-13"   >电子信箱：
	    		<input id="email" class="exui-textbox" data-options="validateOnCreate:false " name="dto.user.email" style="width:180px"/>
	    		</span>
	    		<span class="m-l-13"  >
	    		员工编号：
	    		<input id="employeeId" class="exui-textbox" data-options="validateOnCreate:false " name="dto.user.employeeId" style="width:180px"/>
	    	</span>
	    </div>
	     
	      <div class="lineHeight">
	    	<span class="m-r-30 m-l-13"  >
	    		联系电话：
	    		<input id="tel" class="exui-textbox" style="width:180px" name="dto.user.tel"/>
	    		</span>
	    		<span class="m-r-30 m-l-13">
	    		办公电话：
	    		<input id="officeTel" class="exui-textbox" name="dto.user.officeTel" data-options=" validateOnCreate:false,validateOnBlur:true" style="width:180px"/>
	    		</span>
	    	 <span style="margin-left:47px">
	    		职务：&nbsp;<input id="headShip" style="width:180px;" class="exui-textbox" name="dto.user.headShip"/>
	    		</span>
	    </div>
	    
	     <div class="lineHeight">
	    		<span class="m-r-30 " style="margin-left: 28px">
	    		所属组：&nbsp;<input id="groupNames" disabled="true" class="exui-textbox" name="dto.user.groupNames" data-options="validateOnCreate:false,validateOnBlur:true" style="width:180px"/>
	    		</span>
	     
	     
	    	<span class="m-r-30 m-l-13">
	    		安全问题：
	    			<input id="question" style="width:180px" class="exui-textbox" name="dto.user.question"/></span>
	    	<span style="margin-left:50px">		
	    		答案：
	    			<input id="answer" style="width:180px"class="exui-textbox" name="dto.user.answer"/></span>
	    		
	     
	    	 
	    	  <!-- 	<div style="position:relative;bottom:10px;width:50%;text-align:center">（找回密码时使用）</div> -->
	    </div>		
	    	 
	    </form>

 
<div id="addOrEditFooter" align="center" style="padding:5px;margin-left: -150px">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submitUserInfo()" style="width: 80px;height:36px;margin-right: 40px">保存</a>
	<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="resetInfo()" style="width: 80px;height:36px; ">重置</a>
</div>
</div>
</div>

<script src="<%=request.getContextPath()%>/itest/js/userManager/updateUserInfo.js" type="text/javascript" charset="utf-8"></script>