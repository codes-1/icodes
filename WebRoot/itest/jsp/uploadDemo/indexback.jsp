<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Easyui 结合Pluplaod插件的上传演示</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="renderer" content="webkit">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%-- <link rel="stylesheet" href="<%=path%>/bootstrap/easyui.css" type="text/css"></link>
<script type="text/javascript" src="<%=path%>/easyui/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=path%>/easyui/jquery.easyui.min.js"></script> --%>

<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/exui/bootstrap/bootstrap-3.3.7.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/main.css"/>
<script src="<%=request.getContextPath()%>/itest/exui/jquery/jquery-1.11.3.min.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/itest/exui/bootstrap/bootstrap-3.3.7.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/easyui/exuiloader.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/itest/exui/exui.js" type="text/javascript" charset="utf-8"></script> --%>
<script src="<%=request.getContextPath()%>/itest/js/uploadDemo/index.js" type="text/javascript" charset="utf-8"></script>
</head>
<body style="width: 100%;height: 100%;overflow:hidden;margin: 0;padding: 0;">
	<h1>Easyui 结合Pluplaod插件的上传演示</h1>
	<hr />
	<a class="exui-linkbutton" href="javascript:makerUpload(false)">不分割文件上传</a>
	<a class="exui-linkbutton" href="javascript:makerUpload(true)">分割文件上传</a>
	<hr />
	<div id="res"></div>
	
	<div id="uploadWin" style="overflow: hidden;"></div>
</body>
</html>