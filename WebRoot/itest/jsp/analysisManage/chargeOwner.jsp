<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
#chargeOwner-Wrapper th{
  font-size: 13px;
    text-align: center;
 }
 
#chargeOwner-Wrapper tr td{
   text-align:center;
 }
 
#chargeOwner-Wrapper  div.tips{
   width:700px;
   margin:22px auto;
 }
 
#chargeOwner-Wrapper p{
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

<div id="chargeOwner-Wrapper">  
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
  <div id="chargeOwnerDiv" style="display:none">
   <!-- ehcarts -->
    <div id="chargeOwnerEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
   <table id="chargeOwnerTable" class="table table-bordered" style="width:700px;margin: 20px auto">
   <thead style="text-align:center;background-color:#dce9eb">
     <tr>
       <th>人员姓名</th>
       <th>BUG数</th>
     </tr>
   </thead>
   <tbody id="chargeOwnerTbody"></tbody>
    </table>
   </div>
   <div id="chargeOwnerMask" style="display:none">
     <div style='font-size: 25px;text-align: center;padding-top: 105px;'>暂无此项目报表数据</div>
   </div>
   <div class="tips">
     <p >下述状态的BUG不计为有效BUG:撤销、无效、重复、非错、关闭/撤销，修正/描述不当</p>
   </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/chargeOwner.js"></script>
