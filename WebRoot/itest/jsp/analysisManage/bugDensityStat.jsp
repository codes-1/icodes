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
    <div id="bugDensityStat01Echarts" style="margin:50px auto;width: 700px; height:400px;"></div>
   <!-- ehcarts -->
    <div id="bugDensityStat02Echarts" style="margin:50px auto;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   <table id="bugDensityStatTable" class="table table-bordered" style="width:700px;margin:50px auto">
   <thead style="text-align:center">
     <tr>
       <th>一级测试需求</th>
       <th>BUG数</th>
       <th>Kloc</th>
       <th>密度</th>
       <th>所占百分比</th> 
     </tr>
   </thead>
   <tbody id="bugDensityStatTbody"></tbody>
    </table>
    <!-- ehcarts -->
    <div id="bugDensityStat03Echarts" style="margin:50px auto;width: 700px; height:400px;"></div>
    <!-- 表格 -->
   <table id="bugDensityStatTable01" class="table table-bordered" style="width:700px;margin:50px auto">
   <thead style="text-align:center;background-color:#dce9eb" id="bugDensityStatThead01" >
   </thead>
   <tbody id="bugDensityStatTbody01"></tbody>
    </table>
    
   <!-- ehcarts -->
    <div id="bugDensityStat04Echarts" style="margin:50px auto;width: 700px; height:400px;"></div>
    <!-- 表格 -->
   <table id="bugDensityStatTable02" class="table table-bordered" style="width:700px;margin:50px auto">
   <thead id="bugDensityStatThead02" style="text-align:center;background-color:#dce9eb">
   </thead>
   <tbody id="bugDensityStatTbody02"></tbody>
    </table>
    
   </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/bugDensityStat.js"></script>
