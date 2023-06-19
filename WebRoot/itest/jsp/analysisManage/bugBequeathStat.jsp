<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
 th{
  font-size: 13px;
    text-align: center;
 }
 
 tr td{
   text-align:center;
 }
</style>

<div>   

<div style="">
   <!-- ehcarts -->
    <div id="bugBequeathStatEcharts" style="margin:50px auto;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
   <table id="bugBequeathStatTable" class="table table-bordered" style="width:700px;margin:50px auto">
   <thead style="text-align:center;background-color:#dce9eb">
     <tr>
       <th>引入阶段</th>
       <th>BUG数</th>
     </tr>
   </thead>
   <tbody id="bugBequeathStatTbody"></tbody>
    </table>
   </div>
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/bugBequeathStat.js"></script>
