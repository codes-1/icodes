<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/singleTestTaskManager/testTaskMagrList.css"/>
<style type="text/css">
th{
 	font-weight:400;
}
</style>
<div id="caseLayout" class="exui-layout" style="height: 600px;">
	<div id="caseTreeDiv" data-options="region:'west'" style="width:230px;padding:10px;">
		<%-- <input id="currTaksId" type="hidden" value="<%=currTaksId%>"> --%>
		<ul id="caseTypeTree" class="exui-tree"></ul>
	</div>
	<div data-options="region:'center'" style="overflow: scroll;" title="">
		<div class="tools" data-options="">
			<button style="padding:9.5px 18px;background: #1e7cfb none repeat scroll 0 0;border: 1px solid #1e7cfb;color: #ffffff;" type="button" class="btn btn-default" onclick="agreeLibrary();"><i class="glyphicon glyphicon-ok"></i>通过</button>
			<button style="padding:9.5px 18px;border: 1px solid #1e7cfb;color: #1e7cfb;" type="button" class="btn btn-default" onclick="disagreeLibrary();"><i class="glyphicon glyphicon-remove"></i>不通过</button>
		</div><!--/.top tools area-->
		
		<table id="caseList" data-options="
			fitColumns: true,
			singleSelect: false,
			pagination: true,
			pageNumber: 1,
			pageSize: 10,
			pageList:[10,30,50],
			layout:['list','first','prev','manual','next','last','refresh','info']">
		</table>
	</div>
</div>
<script src="<%=request.getContextPath()%>/itest/js/testLibrary/caseExamine.js" type="text/javascript" charset="utf-8"></script>