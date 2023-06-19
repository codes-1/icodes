<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="tools">
	
 	<button type="button" class="btn btn-default" id="saveGrant"><i class="glyphicon glyphicon-ok"></i>确认</button>
	<button type="button" class="btn btn-default" id="closeTreeGrantDlg"><i class="glyphicon glyphicon-off"></i>关闭</button>
	
</div><!--/.top tools area-->


<!-- 权限 -->
  <ul id="authGrantTree"></ul> 
<!-- /权限 -->

<script  type="text/javascript" src="<%=request.getContextPath()%>/itest/js/roleManager/authGrant.js"></script>