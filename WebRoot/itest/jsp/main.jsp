<%@page import="java.util.ArrayList"%>
<%@page import="cn.com.codes.framework.common.JsonUtil"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="cn.com.codes.common.util.StringUtils"%>
<%@page import="cn.com.codes.framework.security.filter.SecurityContextHolder"%>
<%@page import="cn.com.codes.framework.security.filter.SecurityContext"%>
<%@page import="cn.com.codes.framework.security.Visit"%>  
<%@page import="cn.com.codes.framework.security.VisitUser"%>
<%@page import="cn.com.codes.framework.common.JsonUtil"%>
<%@page import="java.util.Set" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.lang.String" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<style>
.words{
margin-top:20px;

color:#DAE3EC;
}
.words p{
margin: 0 0 6px
}
</style>
<script type="text/javascript">
var privilegeMap = new Object();
<%
SecurityContext sc = SecurityContextHolder.getContext();
Visit visit = sc.getVisit();
VisitUser user =  null;
if(visit!=null){
	user = visit.getUserInfo(VisitUser.class);
}

String accountId = (null != user.getId()) ? user.getId() : "";
String loginName = (null != user.getLoginName()) ? user.getLoginName() : "";
String userName= (null != user.getName()) ? user.getName() : "";
String isAdmin = (null != user.getIsAdmin()) ? user.getIsAdmin().toString() : "";
String currTaksId = visit.getTaskId();
String projectFlag = visit.getFlag();
String haveProject = visit.getHaveProject();
String currPName = visit.getAnalyProjectName();
String currPNum = visit.getAnalyProNum();

if (StringUtils.isNullOrEmpty(userName)) {
	userName = loginName;
}
%>

</script>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
	<meta name="renderer" content="webkit">
	<meta content="Codes 数据区动效能，智能管理研发" name="description">
	<title>codes</title>
	<link rel="shortcut icon"  type="image/x-icon" href="<%=request.getContextPath()%>/itest/images/icon.svg"/>
	<link rel="Bookmark" type="image/x-icon"  href="<%=request.getContextPath()%>/itest/images/icon.svg"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/exui/bootstrap/bootstrap-3.3.7.css"/>
	<link href="<%=request.getContextPath()%>/itest/exui/fileinput/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/itest/exui/bootstrap/font-awesome/css/font-awesome.min.css" media="all" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/itest/exui/fileinput/themes/explorer-fa/theme.css" media="all" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/itest/plug-in/searchableSelect/jquery.searchableSelect.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/main.css"/>
	<style type="text/css">
	.btn-default.focus, .btn-default:focus {
    color: #757575/*#333*/;
    background-color: #e0e0e0/*#e6e6e6*/;
    border-color: #8c8c8c;
    
    outline: none;
}
	</style>
</head>
<body style="background-color: #fff;">
	<section id="sec">
		<header id="main_header" class="main-header">
		<!-- Logo -->
	    <a class="logo" onclick="logoEvent();" style="cursor: pointer;">
	      <img class="logo-min" src="<%=request.getContextPath()%>/itest/images/homeImage/logo.svg">
	      <img class="logo-lg" src="<%=request.getContextPath()%>/itest/images/homeImage/logo.svg">
	    </a>
	    
	    <!-- header navigation -->
    	<nav class="navbar navbar-fixed-top" role="navigation">
    	
    		<a href="javascript:void(0);" class="sidebar-toggle" data-toggle="offcanvas" role="button">
		        <span class="sr-only">Toggle navigation</span>
		        <img class="sidebary" data-show="1" src="<%=request.getContextPath()%>/itest/images/homeImage/sidebar.png">
		    </a>
		    
		  <%-- <div id="scalePage" class="navbar-left switchm">
		        <a href="javascript:void(0);" >
				  <img class="switch" src="<%=request.getContextPath()%>/itest/images/homeImage/switch.png">
				  <span class="switchSt">&nbsp;全部设置&nbsp;&nbsp;</span>
				</a>&nbsp;&nbsp;
				<div style="position:relative">
					  <ul id="addMenus" class="dropdown-menu addMenu" style="position:static" role="menu" aria-labelledby="dropdownMenu">
					    <li onclick="caseItems();"><a tabindex="-1" href="javascript:void(0);">切换项目</a></li>
					    <li class="divider"></li>
					    <li onclick="testItems();"><a tabindex="-1" href="javascript:void(0);">系统管理</a></li>
					    <li class="divider"></li>
					    <li onclick="testItems();"><a tabindex="-1" href="javascript:void(0);">其他任务</a></li>
				   	 </ul>
				</div>
		   </div> --%>
		    <div id="taskEl" class="navbar-left elseTask" style="margin-left:3%">
		        <a href="javascript:void(0);" onclick="switchToOtherMission();" style="line-height: 25px;display: inline-block;cursor: pointer;">
				  <img class="switch" id="otherMissionImg" src="<%=request.getContextPath()%>/itest/images/homeImage/elseTs.png">
				  <label id="otherMissionssss" style="color: rgb(233, 246, 255);font-size:16px;font-weight: normal;">项目任务&nbsp;&nbsp;</label>
				</a>&nbsp;&nbsp;
		   </div>
		   
		   <div id="switchP" class="navbar-left switchm" style="margin-left:0%">
		        <a href="javascript:void(0);" style="cursor: pointer;">
				  <img class="switch" src="<%=request.getContextPath()%>/itest/images/homeImage/switch.png">
				  <span class="switchSt">&nbsp;<label id="changePro" style="font-weight: normal;">项目测试</label>&nbsp;&nbsp;&emsp;
				  	<img class="arrows" src="<%=request.getContextPath()%>/itest/images/homeImage/arrow.png">
				  </span>	
				</a>&nbsp;&nbsp;
				<div style="position:relative;">
					  <ul id="addMenus" class="dropdown-menu addMenu" style="position:static;" role="menu" aria-labelledby="dropdownMenu">
					    <li onclick="caseItems();"><a tabindex="-1" href="javascript:void(0);">切换测试项目</a></li>
					    <li class="divider" style="background-color: #e5e5e5;margin-left: 0px;"></li>
					    <li id="proManager" onclick="testItems();" schkUrl="/singleTestTask/singleTestTaskAction!magrTaskListLoad.action"><a tabindex="-1" href="javascript:void(0);">测试项目维护</a></li>
					    <!-- <li id="proManager" onclick="testItems();" schkUrl=""><a tabindex="-1" href="javascript:void(0);">测试项目维护</a></li> -->
					    <li class="divider" style="background-color: #e5e5e5;margin-left: 0px;"></li>
					    <li onclick="testLibrary();"><a tabindex="-1" href="javascript:void(0);">测试用例库</a></li>
				   	 </ul>
				</div> 
				 
		   </div> 
		   
		
		   
		    <div id="iteration" class="navbar-left iterations" style="cursor: pointer;width: 130px;">
		        <a href="javascript:void(0);" onclick="switchToDiedai();">
				  <img class="switch" src="<%=request.getContextPath()%>/itest/images/homeImage/iterList.png">
				  <span class="switchSt">&nbsp;<label id="iterL" style="font-weight: normal;">项目迭代</label>&nbsp;&nbsp;&emsp;
				  </span>
				</a>
		   </div> 
		   
		   <div class="navbar-left systemM" id="systemMenu">
		        <a href="javascript:void(0);" style="cursor: pointer;/* padding-left: 20px; */padding-right: 20px;">
				  <img class="switch" src="<%=request.getContextPath()%>/itest/images/homeImage/systemM.png">
				  <span class="switchSt"><label id="systemManage" style="font-weight: normal;">系统设置</label>&nbsp;&nbsp;&emsp;
				  	<img class="arrows" src="<%=request.getContextPath()%>/itest/images/homeImage/arrow.png">
				  </span>
				</a>&nbsp;&nbsp;
				<div style="position:relative;">
					  <ul id="systemM" class="dropdown-menu systemMenu" style="position:static" role="menu" aria-labelledby="dropdownMenu">
					   
				   	  </ul>
			   	 </div>
		   </div>
            
		   <div id="systemC" class="pull-right " style=" cursor:pointer;/* position:relative */">
           
		   <div class="pull-right systemC" style=" cursor:pointer;width:auto;margin-right:20px;height:70px">
                  <span id="accountId" style="display:none;"><%=accountId %></span>
		          <span id="loginName" style="display:none;"><%=loginName %></span>
		          <span id="loginNam" style="display:none;"><%=userName %></span>
		          <span id="isAdmin" style="display:none;"><%=isAdmin %></span>
				  <span id="loginNames" class="switchSt">管理员,你好！</span>
				  <img id="arrowDown" class="switch"  style="width:12px;height:10px" src="<%=request.getContextPath()%>/itest/images/homeImage/arrow.png">
				 <%--  <img id="arrowUp" class="switch" style="display:none" onclick="selfInfoShow()" src="<%=request.getContextPath()%>/itest/images/homeImage/arrow1.png"> --%>
				 
				  <div style="position:relative">
				   <ul id="userInfoMenus" class="dropdown-menu userInfo" style=" " role="menu" aria-labelledby="dropdownMenu">
				    <li onclick="showAddWin01()" id="setMyInfo"  ><a tabindex="-1" href="javascript:void(0);"> 个人信息维护 </a></li> 
				    <li class="divider"></li>
					<li onclick="exitLogin()"><a tabindex="-1" href="javascript:void(0);">注销</a></li>
			   	 </ul>  
			   	 
			</div>
				 &nbsp;&nbsp;
		   </div>
		   
		   <%--  <div id="messageS" class="pull-right message" style="border-right: 1.5px solid #616976;position: relative;display: none;">
		   		 <a href="javascript:void(0);" style="line-height: 25px;">
				  <img class="switch" src="<%=request.getContextPath()%>/itest/images/homeImage/message.png">
				</a>&nbsp;&nbsp;&nbsp;
				<div class="messageRa" style="position:absolute;"><span style="color: #ffffff;">8</span></div>
		   </div>
		   
		   <div id="answerS" class="pull-right answer" style="display: none;">
		   		 <a href="javascript:void(0);">
				  <img class="switch" style="margin-top:1px;" src="<%=request.getContextPath()%>/itest/images/homeImage/answer.png">
				</a>&nbsp;&nbsp;
		   </div> --%>
		   </div>
		   <div class="pull-right new_icon" style="margin-top:20px;cursor:pointer;margin-right:20px;height:50px;">
           <img id="new_icon" class="switch"  style=" " src="<%=request.getContextPath()%>/itest/images/Group.png">
             <div style="position:relative">
				 <div id="new_icon_info" class="dropdown-menu" style="height: 250px;min-width: 460px;margin: 20px 0px 0px -300px;left:0;background-color:#2F3543" role="menu" aria-labelledby="dropdownMenu">
				   <div style="width:38%;float:left;text-align:center">
                   <img style="max-width:58%;margin-top:2.2em" src="<%=request.getContextPath()%>/itest/images/homeImage/logo.svg">
                   </div>
				   
				   <div style="width:60%;float:left;margin-top:2em"> 
				   <img style="max-width:80%;" src="">
				   <div class="words">
				   <p><b>QQ群</b>&nbsp;:&nbsp;797761290,2155613</p>
				   <p><b>官网</b>&nbsp;:&nbsp;icodes.work</p>
				   <p><b>联系我们</b>&nbsp;:&nbsp;12960457@qq.com</p>
				   <p style="line-height:25px">Codes采用apache2.0开源协议</p>
				   </div>
				   </div> 
			   	 </div>
			   	 
			</div>
           </div>
    	</nav>
	</header>
	 
	<!-- <div style="">
	  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu">
		  <li><a tabindex="-1" href="javascript:void(0);">切换项目</a></li>
		  <li><a tabindex="-1" href="javascript:void(0);">测试项目管理</a></li>
		  <li><a tabindex="-1" href="javascript:void(0);">Something else here</a></li>
		  <li class="divider"></li>
		  <li><a tabindex="-1" href="javascript:void(0);">Separated link</a></li>
	  </ul>
	</div> -->
	
	<!--left aside menu-->
	<aside class="l-menu" id="leftMenu">
		<ul id="menu" class="l-menu-box">
			<!-- 不要删除，后面还要用 -->
			<%-- <li class="treeview">
	      	 	<a href="javascript:void(0);" class="firstA">
	      	 		<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/projectMan1.png">&nbsp;&nbsp;&nbsp;
					<span class="">测试项目管理&nbsp;&nbsp;</span>
					<img class="marginPrev" src="<%=request.getContextPath()%>/itest/images/homeImage/upArr1.png">
	      	 	</a>
	      	 	
	      	 	<ul class="secondMenu" style="background: #2f3543;padding: 1em 0em 1em 3em;display: none;">
	      	 	  <li style="line-height: 3em;">
	      	 	    <a href="javascript:void(0);" class="childA">
	      	 	    	<img class="imgarr" src="<%=request.getContextPath()%>/itest/images/homeImage/smallAr1.png">&nbsp;&nbsp;&nbsp;
						<span class="childFont">测试项目流程&nbsp;&nbsp;</span>
	      	 		</a>
	      	 	  </li>
	      	 	  <li style="line-height: 3em;">
	      	 	    <a href="javascript:void(0);" class="childA">
	      	 	    	<img class="imgarr" src="<%=request.getContextPath()%>/itest/images/homeImage/smallAr1.png">&nbsp;&nbsp;&nbsp;
						<span class="childFont">测试项目流程&nbsp;&nbsp;</span>
	      	 		</a>
	      	 	  </li>
	      	 	</ul>
	      	 </li>
	      	 
	      	 <li class="treeview">
	      	 	<a href="javascript:void(0);" class="firstA">
	      	 		<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/testSin1.png">&nbsp;&nbsp;&nbsp;&nbsp;
					<span class="">测试用例管理&nbsp;&nbsp;</span>
	      	 	</a>
	      	 </li>
	      	 
	      	 <li class="treeview">
	      	 	<a href="javascript:void(0);" class="firstA">
	      	 		<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/defect1.png">&nbsp;&nbsp;&nbsp;
					<span class="">缺陷管理&nbsp;&nbsp;</span>
	      	 	</a>
	      	 </li>
	      	 
	      	 <li class="treeview">
	      	 	<a href="javascript:void(0);" class="firstA">
	      	 		<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/analysis1.png">&nbsp;&nbsp;&nbsp;
					<span class="">分析度量&nbsp;&nbsp;</span>
	      	 	</a>
	      	 </li> --%>
	      	 
			<!-- <li><div id="home_menu" class="home"><i class="glyphicon glyphicon-home"></i></div></li>
			<li class="selected-menu"><div class="glyphicon glyphicon-trash"></div><div>项目管理</div></li>
			<li><div class="glyphicon glyphicon-trash"></div><div>项目管理</div></li>
			<li><div class="glyphicon glyphicon-trash"></div><div>项目管理</div></li>
			<li><div class="glyphicon glyphicon-trash"></div><div>项目管理</div></li> -->
		</ul>
		
		<!-- 其他任务左侧菜单 -->
		<ul class="l-menu-box" id="otherMissionLeftMenu" style="display:none">
			<li id="m1" myUrl="/otherMission/otherMissionAction!overview.action?dto.id=123&dto.is=123" onclick="overview(this);" class="treeview otherMissionCl" data-info="68" style="background: rgb(61, 67, 81) none repeat scroll 0% 0%;">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/kanban1.png">
			<span class="aaaa" style="color: rgb(233, 246, 255);margin-left:8px">任务看板  </span>
			</a>
			</li>
			<li id="jkl" myUrl="/otherMission/otherMissionAction!toMeCharge.action?dto.id=123&dto.is=123" onclick="toMeCharge(this);" class="treeview otherMissionCl" data-info="68" style="background: rgb(61, 67, 81) none repeat scroll 0% 0%;">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/fuzhe1.png">
			<span class="aaaa" style="color: rgb(233, 246, 255);margin-left:8px">我负责的  </span>
			</a>
			</li>
			<li id="m2" myUrl="/otherMission/otherMissionAction!toMeJoin.action?dto.id=123&dto.is=123" onclick="toMeJoin(this);" class="treeview otherMissionCl" data-info="87" style="background: rgb(61, 67, 81) none repeat scroll 0% 0%;">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/canyu1.png">
			<span class="aaaa" style="color: rgb(233, 246, 255);margin-left:8px">我参与的  </span>
			</a>
			</li>
			<li id="m3" myUrl="/otherMission/otherMissionAction!otherMissionList.action?dto.id=123&dto.is=123" onclick="toMeCreate(this);" class="treeview otherMissionCl" data-info="107">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/chuangjian1.png">
			<span class="aaaa" style="margin-left:8px">我创建的  </span>
			</a>
			</li>
			<li id="m4" myUrl="/otherMission/otherMissionAction!toMeConcern.action?dto.id=123&dto.is=123" onclick="toMeConcern(this);" class="treeview otherMissionCl" data-info="127">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/guanzhu1.png">
			<span class="aaaa" style="margin-left:8px">我关注的  </span>
			</a>
			</li>
			<li id="m5" myUrl="/otherMission/otherMissionAction!allMissions.action?dto.id=123&dto.is=123" onclick="allMission(this);" class="treeview otherMissionCl" data-info="147">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/suoyou1.png">
			<span class="aaaa" style="margin-left:8px">所有任务  </span>
			</a>
			</li>
			<li id="m6" class="treeview otherMissionCl" data-info="187" onclick="setSelfHomePage1()" style="background: rgb(30, 124, 251) none repeat scroll 0% 0%;">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/myHome.png">
			<span class="aaaa" style="margin-left:8px">设置首页  </span>
			</a>
			</li>
		</ul>
		
		<!-- 测试用例库左侧菜单 -->
		<ul class="l-menu-box" id="caseTypeMenu" style="display:none">
			<li id="l1" myUrl="/testLibrary/testLibraryAction!loadTree.action" onclick="caseTypeEdit(this);" class="treeview yongli" data-info="68" style="background: rgb(61, 67, 81) none repeat scroll 0% 0%;">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/yongliweihu1.png">
			<span class="aaaa" style="color: rgb(233, 246, 255);margin-left:8px">用例类别维护  </span>
			</a>
			</li>
			<li id="l2" myUrl="/testLibrary/testLibraryAction!caseLook.action" onclick="caseLook(this);" class="treeview yongli" data-info="87" style="background: rgb(61, 67, 81) none repeat scroll 0% 0%;">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/yongliliulan1.png">
			<span class="aaaa" style="color: rgb(233, 246, 255);margin-left:8px">用例浏览  </span>
			</a>
			</li>
			<li id="l3" myUrl="/testLibrary/testLibraryAction!caseExamine.action" onclick="caseExamine(this);" class="treeview yongli" data-info="107" style="display:none">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/shenhe1.png">
			<span class="aaaa" style="margin-left:8px">入库审核  </span>
			</a>
			</li>
			<%-- <li id="m6" class="treeview otherMissionCl" data-info="187" onclick="setSelfHomePage1()" style="background: rgb(30, 124, 251) none repeat scroll 0% 0%;">
			<a class="firstA" href="javascript:void(0);">
			<img class="" src="<%=request.getContextPath()%>/itest/images/homeImage/myHome.png">
			<span class="aaaa" style="margin-left:8px">设置首页  </span>
			</a>
			</li> --%>
		</ul>
	</aside><!--/.left aside menu-->
	
	<!--content area-->
	<div class="content">
		<!--main coent body-->
		<div id="wrap-page" class="wrapper" style="width:100%;">
		
			<div id="noPro" style="background-color:#ffffff;height:100%;display: none;">
				<img alt="" src="<%=request.getContextPath()%>/itest/images/homeImage/noProject.png" style="margin: 19% 42%;">
				<span style="font-size: 22px;font-weight: bold;float: right;margin: -15% 10%;">您目前还没有参与任何测试项目，被设置到测试项目的流程中才被视为参与了该项目</span>
			</div>
		</div><!--/.main content body-->
	</div><!--/.content area-->
	
	<!--right menu-->
	<aside class="r-menu">
		<ul style="padding-left:2px">
			<li><img src="<%=request.getContextPath()%>/itest/images/homeImage/comment.png"/></li>
			<li><img src="<%=request.getContextPath()%>/itest/images/homeImage/mComment.png"/></li>
			<li><img src="<%=request.getContextPath()%>/itest/images/homeImage/video.png"/></li>
		</ul>
	</aside><!--/.right menu-->
	
	<!--用户信息修改弹窗  -->
	
	<div id="addOrEditWinUserInfo" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 610,
	footer:'#addOrEditFooterUserInfo',
	minimizable:false,
	maximizable:false,
	closed:true">
<!-- 	<input class="exui-textbox" name="dto.role.roleName" data-options="required:true,validateOnCreate:false,prompt:'多个IP用分号(;)隔开'" style="width:75%"/> -->
	
		<form id="addOrEditFormUserInfo" method="post" style="margin-top:10px;margin-left:10px">
					<div style="display:none"><div>
				<input id="id" name="dto.user.id"/>
				<input id="isAdmin" name="dto.user.isAdmin"/>
				<input id="delFlag" name="dto.user.delFlag"/>
				<input id="chgPwdFlg" name="dto.user.chgPwdFlg"/>
				<input id="status" name="dto.user.status"/>
				<input  name="dto.user.myHome"/>
				<!-- <input id="attachUrl" name="dto.user.planDocName"/>  -->
			</div></div>
			
			<div class="m-b-10 ">
			<span>
				<sup>*</sup>登陆账号：
	    		<input class="exui-textbox" id="loginName"  readonly="readonly" disabled="disabled" data-options="required:true,validateOnCreate:false"  name="dto.user.loginName"
	    		    style="width:280px"/>
	        </span>
	    	</div> 
	    	<div class="m-b-10 editShow">
			<span>
				<sup>*</sup>原始密码：
	    		<input class="exui-textbox" id="oldPwd" type="password" data-options="required:true,validateOnCreate:false"  name="dto.user.oldPwd"
	    		    style="width:280px"/>
	        </span>
	        </div> 
	        
	    	<div class="m-b-10 ">
	    	<span >
	    		<sup>*</sup><p id="passwordText" style="display: inline; ">登陆密码：</p>
	    		<input type="password" class="exui-textbox" id="password" name="dto.user.password" data-options="required:true,validateOnCreate:false" style="width:180px"/>
	    	
	    		</span>
	    	    <span style="margin-left: 20px">
	    		<sup>*</sup>确认密码：
	    		<input id="rePwd01" type="password" class="exui-textbox" name="repwd" data-options="required:true,validateOnCreate:false" style="width:180px"/>
	    	    
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
	    		所属组：&nbsp;<input id="groupNames" disabled="disabled"  readonly="readonly" class="exui-textbox" name="dto.user.groupNames" data-options="validateOnCreate:false,validateOnBlur:true" style="width:135px"/>
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
			<!-- <div class="hidden "><div>
				<input id="id" name="dto.user.id"/>
				<input id="isAdmin" name="dto.user.isAdmin"/>
				<input id="delFlag" name="dto.user.delFlag"/>
				<input id="chgPwdFlg" name="dto.user.chgPwdFlg"/>
				<input id="status" name="dto.user.status"/>
				<input id="attachUrl" name="dto.user.planDocName"/> 
			</div></div>
			<div class="m-b-10 ">
			<span>
				<sup>*</sup>登陆账号：
	    		<input class="exui-textbox" id="loginName" data-options="required:true,validateOnCreate:false"  name="dto.user.loginName"
	    		    style="width:180px"/>
	        </span>
	        <span class="m-l-20">
	    		<sup>*</sup>登陆密码：
	    		<input id="password" type="password" class="exui-textbox" name="dto.user.oldPwd" data-options="required:true,validateOnCreate:false" style="width:180px"/>
	    	</span>
	    	</div> 
	    	
	    	<div class="m-b-10 ">
	    	<span>
	    		<sup>*</sup>确认密码：
	    		<input id="rePwd" name="dto.user.password" class=" exui-textbox" type="password" data-options="required:true,validateOnCreate:false"  
	    		    style="width:180px"/>
	    		    </span>
	    	<span class="m-l-20">
	    		<sup>*</sup>真实姓名：
	    		<input id="name" class=" exui-textbox" name="dto.user.name" data-options="required:true,validateOnCreate:false" style="width:180px"/>
	    	</span>
	    	</div>
	    	
	    	<div class="m-b-10 m-l-11">
	    		<span>电子信箱：
	    		<input id="email" class="exui-textbox" data-options="validateOnCreate:false " name="dto.user.email" style="width:180px"/>
	    		</span>
	    		<span class="m-l-30">
	    		员工编号：
	    		<input id="employeeId" class="exui-textbox" data-options="validateOnCreate:false " name="dto.user.employeeId" style="width:180px"/>
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
	    	<span style="margin-left:27px">
	    		职务：&nbsp;<input id="headShip" style="width:180px;" class="exui-textbox" name="dto.user.headShip"/>
	    		</span>
	    		<span  style="margin-left:45px">
	    		所属组：&nbsp;<input id="groupNames" class="exui-textbox" name="dto.user.groupNames" disabled="true" data-options="validateOnCreate:false,validateOnBlur:true" style="width:180px"/>
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
	    		
	    		 
	    	</div>  -->
	    </form>

</div>
<div id="addOrEditFooterUserInfo" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submitUserInfo()"  >保存</a>
	<a class="exui-linkbutton" style="border: 1px solid #1E7CFB;color: #1E7CFB;"  data-options="btnCls:'default',size:'xs'" onclick="resetInfo01()"  >重置</a>
	<a class="exui-linkbutton" style="border: 1px solid #1E7CFB;color: #1E7CFB;"  data-options="btnCls:'default',size:'xs'" onclick="closeWin01()">关闭</a>
</div>

	<!-- 切换项目-->
	<div id="caseItemWin" title="切换项目" class="exui-window" data-options="
		modal:true,
		width: 900,
		minimizable:false,
		maximizable:false,
		closed:true">
		<table id="caseItemList" class="exui-datagrid" title="" style="width:100%;height: auto;" data-options=""></table>
	</div>
	<!-- 选择多个人弹窗 -->
	<!-- <div id="chooseSomePeopleDiv" class="exui-window" style="display:none;" data-options="
		modal:true,
		width: 580,
		height: 550,
		minimizable:false,
		maximizable:false,
		closed:true">
	
		<div class="tab-content">
			<div id="chooseSomePeople-toolbar">
				<form id="chooseSomePeopleForm" name="chooseSomePeopleForm">
					<table style="width:100%;">
						<tr>
							<td style="width: 50%;">
								<label style="float: left;">姓名：</label>
								<input type="text" id="peopleNa" class="form-control" style="float: left;width:40%;margin-right:5px"/>
								<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="searchSeletctPeople()">查询</a>
								<button type="button" class="btn btn-default" style="background: #1E7CFB;border: 1px solid #1E7CFB;color: #ffffff;" onclick="searchSeletctPeople()"><i class="glyphicon glyphicon-search"></i>查询</button>
							</td>
							<td style="width: 50%; text-align:right">
								<a href="#" class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style="border: 1px solid #1e7cfb; color: #1e7cfb;" onclick="initAssignUser();">刷新备选人员</a>
								<a href="#" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submitSelectedPeoples();">确定</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div style="width: 50%; float: left; padding-top: 10px;">
				<table id="selectPepoleTa" data-options="
					fitColumns: true,
					singleSelect: true,
					pagination: false,
					layout:['list','first','prev','manual','next','last','refresh','info']
					"></table>		
			</div>
			<div style="width: 50%; float: left; padding-top: 10px;">
				<table id="selectedPepoleTa" data-options="
					fitColumns: true,
					singleSelect: true,
					pagination: false,
					layout:['list','first','prev','manual','next','last','refresh','info']
					"></table>
			</div>
		</div>
	
	</div> -->
	
	</section>
	
	<div id="adds" style="display: none;"></div>
	<div id="dataUrl" style="display: none;"></div>
	<div id="menuUrl" style="display: none;"></div>
	<div id="navTop" style="display: none;"></div>
	<div id="navFlag" style="display: none;"></div>
	<!-- <div id="taksIdmain" style="display: none;"></div> -->
	<input type="hidden" id="projectFlag" value="<%=projectFlag%>"/>
	<input type="hidden" id="haveProject" value="<%=haveProject%>"/>
	<input type="hidden" id="userName" value="<%=userName%>"/>
	<input type="hidden" id="taksIdmain" value="<%=currTaksId%>"/>
	<input type="hidden" id="menuId" />
	<input type="hidden" id="homePageFlg" />
	
	<!-- 分析度量数据暂存-->
        <input type="text" id="analyitemId"  value="<%=currTaksId%>" hidden />
        <input type="text" id="analyprojectName"  value="<%=currPName %>" hidden />
        <input type="text" id="analyproNum"  value="<%=currPNum %>" hidden />
        <input type="hidden" id="outlineState"/>
        
	<!-- /分析度量数据 -->
	<%-- <input type="hidden" id="myhome" value="<%=myHome%>"/> --%>
	<div id="loginData" style="display: none;"></div>
	<div id="myhome" style="display: none;"></div>
	<script src="<%=request.getContextPath()%>/itest/exui/jquery/jquery-1.11.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/bootstrap/bootstrap-3.3.7.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/fileinput.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/plugins/sortable.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/locales/zh.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/themes/explorer-fa/theme.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/themes/fa/theme.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/plugins/popper.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/js/jquery.form.min.js"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/easyui/exuiloader.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/exui.js" type="text/javascript" charset="utf-8"></script> 
	<script src="<%=request.getContextPath()%>/itest/plug-in/searchableSelect/jquery.searchableSelect.js" type="text/javascript" charset="utf-8"></script> 
	<!-- echarts -->
	<script src="<%=request.getContextPath()%>/itest/plug-in/ehcarts/echarts.js" type="text/javascript" charset="utf-8"></script>
	<script>var baseUrl = '<%=request.getContextPath()%>';</script>
	<script src="<%=request.getContextPath()%>/itest/js/main.js" type="text/javascript" charset="utf-8"></script> 
</body>
</html>