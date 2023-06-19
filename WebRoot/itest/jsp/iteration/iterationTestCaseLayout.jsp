<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<div class="exui-layout" style="height: 600px;">
		<%-- <div id="TestCaseTreeDiv" data-options="region:'west'" title="<div><span>测试需求 &nbsp;</span></div>" style="width:230px;padding:10px;">
			<ul id="TestCaseTree" class="exui-tree"></ul>
		</div> --%>
		<div data-options="region:'center'" style="overflow: hidden;" title="">
			<div  class="tools" data-options="">
				<button type="button" class="btn btn-default" onclick="submitTestCase()"><i class="glyphicon glyphicon-ok"></i>确认</button>
				<button type="button" class="btn btn-default" id="closeTestCaseWin" style="border: 1px solid #1E7CFB;color: #1E7CFB;"><i class="glyphicon glyphicon-off"></i>关闭</button>
			</div><!--/.top tools area-->
		<!--/.top tools area-->
			<table id="TestCaseList" class="exui-datagrid"  style="width:100%;height: auto;" ></table>
			<!-- <div id="TestCaseCountDiv" style="padding:5px;margin-top: 1em;color: #80a22e;border: 1px dashed #ccc;display: none;">
       			 <p id="TestCaseCountInfo" clasee=""></p>
       			 <p style="color: orange;">因被停用测试需求下有用例，会使测试需求上标识的用例数与列表统计用例数不等</p>
    		</div> -->
		</div>
	</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/iteration/iterationTestCaseLayout.js"></script>