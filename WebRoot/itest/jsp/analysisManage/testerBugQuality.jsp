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
    <div id="testerBugQualityEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
   <table id="testerBugQualityTable" class="table table-bordered" style="width:700px;margin: 20px auto">
   <thead style="text-align:center;background-color:#dce9eb">
     <tr>
       <th>测试人员</th>
       <th>总BUG数</th>
       <th>总交流数</th>
       <th>平均交流成本</th>
     </tr>
   </thead>
   <tbody id="testerBugQualityTbody"></tbody>
    </table>
   </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/testerBugQuality.js"></script>
