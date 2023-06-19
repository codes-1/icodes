<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
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
<div>
   
   
<div class="tools" style="width:700px;margin:20px auto">  
	<lable>开始日期：</lable>
	<div class="input-field" style="width:175px;height:29px"> 
		<input class="exui-datebox" id="startDate" editable="false" placeholder="请输入登陆账号"/>
	</div>
	<lable>结束日期：</lable>
	    
	<div class="input-field" style="width:175px;height:29px"> 
		<input class="exui-datebox" id="endDate" editable="false" placeholder="请输入登陆账号"/>
	</div>
 
	<button id="viewReport" type="button" style="margin-top:7px" class="btn btn-default" ><i class="glyphicon glyphicon-search"></i>查看报表</button>
	<button id="resetInp" type="button" style="margin-top:7px" class="btn btn-default" ><i class="glyphicon glyphicon-pencil"></i>重置</button>

</div>
<div style="margin-left:100px;display:none" id="nullData" >
<h3 style="margin:150px 0 150px 150px">暂无此项目报表数据</h3>
</div>
<div id="mainContent">
   <!-- ehcarts -->
    <div id="main" style="margin:50px auto;width: 700px; height:400px;"></div>
   <!-- 测试人员 -->
   <table id="testerTable" class="table table-bordered" style="width:700px;margin: 20px auto">
   <thead style="background-color:#dce9eb"><tr ><th id="testerThead"></th>
   </tr></thead>
   </table>
   <!-- 表格 -->
    <table id="testerExeTrendTable" class="table table-bordered" style="width:700px;margin: 20px auto">
    <tr id="testerExeTrendThead" style="text-align:center;background-color:#dce9eb">
    </tr>
    <tr id="testerExeTrendThead01" style="text-align:center;background-color:#dce9eb">
    </tr>
    <tbody id="testerExeTrendTbody" style="text-align:center"></tbody> 
   </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/testerExeCaseDayTrend.js"></script>
