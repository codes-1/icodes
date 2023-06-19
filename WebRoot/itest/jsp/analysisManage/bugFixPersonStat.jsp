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

  <div>
   <!-- ehcarts -->
    <div id="bugFixPersonStatEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
    <div id="bugFixPersonPieEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <table id="bugFixPersonStatTable" class="table table-bordered" style="width:700px;margin: 20px auto">
   <thead style="text-align:center;background-color:#dce9eb"><tr id="bugFixPersonStatThead"></tr></thead>
   <tbody id="bugFixPersonStatTbody"></tbody>
    </table>
   </div>
   
    <div>
   <!-- ehcarts -->
    <div id="devFixDataSetEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
    <div id="devFixDataSetPieEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <table id="devFixDataSetTable" class="table table-bordered" style="width:700px;margin: 20px auto">
   <thead style="text-align:center;background-color:#dce9eb"><tr id="devFixDataSetThead"></tr></thead>
   <tbody id="devFixDataSetTbody"></tbody>
    </table>
   </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/bugFixPersonStat.js"></script>
