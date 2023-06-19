<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
#reptFixCloseWrapper th{
  font-size: 13px;
    text-align: center;
 }
 
#reptFixCloseWrapper tr td{
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

<div id="reptFixCloseWrapper"> 
<div class="tools" style="margin:20px auto;width:700px">  
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
   <div id="reptFixCloseDayTrend">
   <!-- ehcarts -->
    <div id="repFixEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
    <div>
       <table id="' + pageNo  + '" class="table table-bordered" style="width:700px;margin: 20px auto">' 
	     <thead style="text-align:center;background-color:#dce9eb">
	      <tr>
	       <th>日期</th>
	       <th>提交有效BUG数</th>
	       <th>打开BUG数</th>
	       <th>待处理BUG数</th>
	       <th>修改BUG数</th>
	       <th>关闭BUG数</th>
	       <th>处理BUG次数</th>
	       </tr> 
	     </thead>
	     <tbody  id="repFixTbody"></tbody>
	    </table>
    </div>
   </div>
   <div id="reptFixCloseDayMask" style="display:none">
      <div style='font-size: 25px;text-align: center;padding-top: 105px;'>暂无此项目报表数据</div>
   </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/reptFixCloseDayTrend.js"></script>
