<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
#bugSummaryWrapper th{
  font-size: 13px;
    text-align: center;
 }
 
#bugSummaryWrapper tr td,#bugSummaryWrapper caption{
   text-align:center;
 }
 
#bugSummaryWrapper  div.tips{
   width:700px;
   margin:22px auto;
 }
 
#bugSummaryWrapper p{
   font-size:15px;
   color:blue;
   font-weight:bold
 }
 
#bugSummaryWrapper caption{
   font-size:15px;
   color:blue;
   font-weight:bold
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

<div id="bugSummaryWrapper">   
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
	<div >
	   <table id="bugNewFixCloseSummaryTable" class="table table-bordered" style="width:700px;margin: 20px auto">
	   <caption>时间段内新增、修改和关闭BUG概况</caption>
	   <thead style="background-color:#dce9eb" id="bugNewFixCloseSummaryThead">
	   </thead>
	   <tbody id="bugNewFixCloseSummaryTbody"></tbody>
	    </table>
  </div>
  <div >
	   <table id="bugBeforeOpenSummaryTable" class="table table-bordered" style="width:700px;margin: 20px auto">
	   <caption>截止到时间末BUG状态分布情况</caption>
	   <thead style="background-color:#dce9eb">
	    <tr> 
	      <td style='text-align:center;'> BUG状态</td>
	      <td style='text-align:center;'>BUG数</td>
	    </tr>
	   </thead>
	   <tbody id="bugBeforeOpenSummaryTbody"></tbody>
	    </table>
  </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/bugSummary.js"></script>
