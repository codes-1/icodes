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

  
<div id="bugExistDayStat">
   <!-- ehcarts -->
    <div id="testerDayEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
   <table id="testerDayTable" class="table table-bordered" style="width:700px;margin: 20px auto">
   <thead style="text-align:center;background-color:#dce9eb"><tr id="testerDayThead"></tr></thead>
   <tbody id="testerDayTbody"></tbody>
    </table>
</div>


<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/bugExistDayStat.js"></script>
