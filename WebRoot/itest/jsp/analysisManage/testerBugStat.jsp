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
    <div id="testerBugStatEcharts" style="margin:50px auto;width: 800px; height:400px;"></div>
   <!-- ehcarts--pie -->
   <div id="testerBugStatPieEcharts" style="margin:50px auto;width: 800px; height:400px;"></div>
   <!-- 表格 -->
   <table id="testerBugStatTable" class="table table-bordered" style="width:700px;margin:50px auto">
   <thead style="text-align:center;background-color:#dce9eb"><tr id="testerBugStatThead"></tr></thead>
   <tbody id="testerBugStatTbody"></tbody>
    </table>
    
    <!--  -->
   <!-- ehcarts -->
    <div id="testerBugStatCloseEcharts" style="margin:50px auto;width: 800px; height:400px;"></div>
   <!-- ehcarts--pie -->
   <div id="testerBugStatClosePieEcharts" style="margin:50px auto;width: 800px; height:400px;"></div>
   <!-- 表格 -->
   <table id="testerBugStatCloseTable" class="table table-bordered" style="width:700px;margin:50px auto">
   <thead style="text-align:center;background-color:#dce9eb"><tr id="testerBugStatCloseThead"></tr></thead>
   <tbody id="testerBugStatCloseTbody"></tbody>
    </table>
   </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/testerBugStat.js"></script>
