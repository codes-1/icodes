<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/analysisManage/analysisManage.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/singleTestTaskManager/testTaskMagrList.css"/>
<style>
  #viewReportBtn{
  background: #1E7CFB;
    border: 1px solid #1E7CFB;
    color: #ffffff;
 }
</style>
<div style="width:100%;height:100%;position:relative" id="container">
	
    <!-- 分析度量--treeGrid -->
    <div class="treewrapper" id="treewrap" >
        <div class="treebar">分析度量  
          <a href="javascript:;" class="layout-left-btn" id="layoutLeftBtn"></a>
        </div>
        <div class="treegrid" id="analysisManange">
        
        </div>
     </div>
   <!-- /分析度量--treeGrid -->
   
   <!-- echarts area -->
      <div class="contentarea" id="contentarea">
                 <div style="font-size: 25px;text-align: center;padding-top: 100px;">请点击左侧选择要查看的报表</div>
     </div >
   <!-- /echarts area --> 
   
   <!-- shade area -->
    <div class="shadewrapper" id="shadewrap">
      <div class="shadecontent">
              <a href="javascript:;" class="layout-right-btn" id="layoutRigthBtn"></a>
     </div>
  </div>
   <!-- /shade area -->
</div>


 <!-- 测试需求项BUG分布明细 -->
 <input id="moduleIds" value="" hidden/>
<div id="testCaseWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 480,
	height:500,
	top:90,
	footer:'#queryUserInnerFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<div style="margin-bottom: 15px;">
	   <button class="btn btn-default" id="viewReportBtn"><i class="glyphicon glyphicon-search"></i>报表查询</button>
	</div>
   <div>
     	<div id="itemDetailTree"></div>
   </div>
</div>
<!-- /测试需求项BUG分布明细 -->

<%-- <script src="<%=request.getContextPath()%>/itest/exui/jquery/jquery-1.11.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/bootstrap/bootstrap-3.3.7.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/plugins/popper.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/easyui/exuiloader.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/exui.js" type="text/javascript" charset="utf-8"></script> 

	<script src="<%=request.getContextPath()%>/itest/plug-in/ehcarts/echarts.js" type="text/javascript" charset="utf-8"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/analysisManage.js"></script>


