<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html>
 
<html lang="zh-CN">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
	<meta content="Codes 数据区动效能，智能管理研发" name="description">
	<title>codes</title>
	<link rel="shortcut icon"  type="image/x-icon" href="<%=request.getContextPath()%>/itest/images/icon.svg" />
	<link rel="Bookmark" type="image/x-icon"  href="<%=request.getContextPath()%>/itest/images/icon.svg" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/bootstrap-material-design.min.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/login.css"/>
</head>
<body>
	<div class="left">
		<header>
			<img src="<%=request.getContextPath()%>/itest/images/logo1.svg"/>
			<div id="switch_btn" class="switch-btn"></div>
		</header>
		<div class="login">
			<div class="login-form">
				<p class="title">Codes</p>
				<span class="outline"></span>
				<p>数据智动效能，智能管理研发</p>
				<form id="loginForm">
					<!--<div class="form-group">
						<label for="account" class="bmd-label-floating">请输入登录名</label>
    					<input type="email" class="form-control" id="account">
    					<span class="bmd-help">如果无登录名，请联系管理员创建。</span>
					</div>
					<div class="form-group">
    					<label for="password">请输入密码</label>
    					<input type="password" class="form-control" id="password" placeholder="请输入密码">
  					</div>-->
  					<div class="form-group">
						<label for="account" class="bmd-label-floating">请输入登录名</label>
    					<input type="text" class="form-control" id="loginlName" name="dto.user.loginName">
    					<span class="bmd-help">如果无登录名，请联系管理员创建。</span>
					</div>
					<div class="form-group">
    					<label for="password" class="bmd-label-floating">请输入密码</label>
    					<input type="password" class="form-control" id="loginPwd" name="dto.user.password">
  					</div>
  					<button type="button" class="btn btn-blue btn-block" onclick="login()">登录</button>
				</form>
			</div>
		</div>
		<footer>
			<p>强烈推荐Firefox3+,支持Chrome4+,IE10+</p>
		</footer>
	</div>
	<div class="right"></div>
	
	<!-- <script src="../exui/jquery/jquery-3.2.1.slim.min.js" type="text/javascript" charset="utf-8"></script> -->
	<script src="<%=request.getContextPath()%>/itest/exui/jquery/jquery-1.11.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/plugins/popper.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/plugins/bootstrap-material-design.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/easyui/exuiloader.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/exui.js" type="text/javascript" charset="utf-8"></script>
	<script>
		var baseUrl = '<%=request.getContextPath()%>';
		$(document).ready(function() {
			$('body').bootstrapMaterialDesign();
		});
	</script>
	<script src="<%=request.getContextPath()%>/itest/js/login.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>