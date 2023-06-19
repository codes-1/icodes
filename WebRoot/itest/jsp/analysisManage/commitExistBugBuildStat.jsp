<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
#commitExistBugBuildStat th{
  font-size: 13px;
    text-align: center;
 }
 
#commitExistBugBuildStat tr{
   width:100%
 }
 
#commitExistBugBuildStat tr td{
   text-align:center;
   width:44.5%;
 }
</style>

<div id="commitExistBugBuildStat" >   
   <!-- ehcarts -->
    <div id="commitExistEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
   <div style="width:700px;margin:20px auto">
    <div id="commitExistTable">
      <table id='" + pageNo + "' class='table table-bordered' >
        <thead style='text-align:center;background-color:#dce9eb'>
        <tr >
          <th>版本</th>
          <th>提交BUG</th>
         <th>BUG总数</th>
       </tr>
      </thead>
      <tbody id="commitExistBody"></tbody>
    </div>
   </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/commitExistBugBuildStat.js"></script>
