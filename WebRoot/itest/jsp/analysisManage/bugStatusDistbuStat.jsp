<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
#bugStatusDisbuStat th{
  font-size: 13px;
    text-align: center;
 }
 
#bugStatusDisbuStat tr td{
   text-align:center;
 }
 
#bugStatusDisbuStat  div.tips{
   width:700px;
   margin:22px auto;
 }
 
#bugStatusDisbuStat p{
   font-size:15px;
   color:blue;
   font-weight:bold
 }
</style>

<div>   
<div id="bugStatusDisbuStat">
   <!-- ehcarts -->
    <div id="bugStatusStatEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
   <table id="bugStatusStatTable" class="table table-bordered" style="width:700px;margin: 20px auto">
   <thead style="text-align:center;background-color:#dce9eb">
     <tr>
       <th>状态</th>
       <th>BUG数</th>
     </tr>
   </thead>
   <tbody id="bugStatusStatTbody"></tbody>
    </table>
    <div class="tips" style="margin:25px auto">
     <p >状态为:撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不计算在内</p>
   </div>
   </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/bugStatusDistbuStat.js"></script>
