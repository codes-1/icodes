<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>

#testerDayCommitWrapper th{
  font-size: 13px;
    text-align: center;
 }
 
#testerDayCommitWrapper tr{
  width:100%;
 }
 
#testerDayCommitWrapper tr td{
   text-align:center;
 }
 
  .tools button,.tools button:hover{
  border: 1px solid #1E7CFB;
    color: #1E7CFB;
}

.tools button:first-of-type{
  background: #1E7CFB;
    border: 1px solid #1E7CFB;
    color: #ffffff;
}
 
</style>

<div id="testerDayCommitWrapper">   
<div class="tools" style="width:700px;margin:20px auto">  
	<lable>开始日期：</lable>
	<div class="input-field" style="width:175px;height:29px"> 
		<input class="exui-datebox" id="startDate" editable="false"/>
	</div>
	<lable>结束日期：</lable>
	    
	<div class="input-field" style="width:175px;height:29px"> 
		<input class="exui-datebox" id="endDate" editable="false"/>
	</div>
 
	<button type="button" style="margin-top:7px" class="btn btn-default" id="viewReport"><i class="glyphicon glyphicon-search"></i>查看报表</button>
	<button type="button" style="margin-top:7px" class="btn btn-default" id="resetInp"><i class="glyphicon glyphicon-pencil"></i>重置</button>

</div>
   <div id="testerDay" style="position:relative">
   <!-- ehcarts -->
    <div id="testerDayEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
  
    <div style="margin: 0 auto;width: 700px; ">
    <div id="testerDayTable" ></div>
   </div>
   </div>
   
   <div id="testerDayForClosed">
    <div style="margin-top: 100px;">
   <!-- ehcarts -->
    <div id="testerDayForClosedEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
    <div id="testerDayForClosedTable" style="margin: 0 auto;width: 700px; "></div>
   </div>
  </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/testerDayCommitTrend.js"></script>
