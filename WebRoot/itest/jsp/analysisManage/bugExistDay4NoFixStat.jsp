<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
#bugExistDay4Table th{
  font-size: 13px;
    text-align: center;
 }
 
#bugExistDay4Table tr td{
   text-align:center;
 }
 
#bugExistDay4Table div.tips{
   width:700px;
   margin:22px auto;
 }
 
#bugExistDay4Table p{
   font-size:15px;
   color:blue;
   font-weight:bold
 }
 
 
</style>

<div>   

  <!-- 待处理BUG按天龄期分析 -->
  <div style="">
   <!-- ehcarts -->
    <div id="bugExistDay4Echarts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
   <table id="bugExistDay4Table" class="table table-bordered" style="width:700px;margin: 20px auto">
   <thead style="text-align:center;background-color:#dce9eb"><tr id="bugExistDay4Thead"></tr></thead>
   <tbody id="bugExistDay4Tbody"></tbody>
    </table>
   </div>
    <!-- /待处理BUG按天龄期分析 -->
    
   <!-- 待处理BUG按天绝对龄期分析 -->
   <div style="">
   <!-- ehcarts -->
    <div id="bugExistDayAbsoluteEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
   <!-- 表格 -->
   
   <table id="bugExistDayAbsoluteTable" class="table table-bordered" style="width:700px;margin: 20px auto">
   <thead style="text-align:center;background-color:#dce9eb"><tr id="bugExistDayAbsoluteThead"></tr></thead>
   <tbody id="bugExistDayAbsoluteTbody"></tbody>
    </table>
    
   <div class="tips">
     <p >注：待处理龄期为停留在某个处理流程上的龄期;绝对龄期指从提交到现在的龄期重复，无效，撤销，关闭</p>
     <p >(己解决|不再现|撤销遗留)状态BUG不计为待处理BUG</p>
   </div>
  </div>
   <!-- /待处理BUG按天绝对龄期分析 -->
  
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/bugExistDay4NoFixStat.js"></script>
