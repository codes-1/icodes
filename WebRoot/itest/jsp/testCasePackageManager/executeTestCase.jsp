<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
   <div id="execBtnTool" class="tools" >
	<button type="button" class="btn btn-default" onclick="executeCaseWin()"><i class="glyphicon glyphicon-asterisk"></i>执行</button>
    <button type="button" class="btn btn-default" onclick="closeExecWin()"><i class="glyphicon glyphicon-off"></i>关闭</button>
  </div><!--/.top tools area-->
  <div style="width:100%;display:inline-block">
    <table id="executeTestCaseList" class="exui-datagrid" title="" style="width:100%;height: auto;" data-options="fitColumns: true,
	rownumbers: false,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info']
	"></table>
  </div>
  <div style="border:1px solid #eee;margin:10px 0;clear:both"></div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/testCasePackageMananger/executeTestCase.js" charset="utf-8"></script>
