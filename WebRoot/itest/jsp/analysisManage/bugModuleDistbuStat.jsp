<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div>
	 <!-- ehcarts -->
    <div id="bugModuleNumEcharts" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
     <table id="bugModuleNumTable" class="table table-bordered" style="width:700px;margin: 20px auto">
	   <thead style="text-align:center;background-color:#dce9eb">
	     <tr>
	       <th>测试需求项名称</th>
	       <th>BUG数</th>
	     </tr>
	   </thead>
       <tbody id="bugModuleNumTbody"></tbody>
    </table>
    <div id="bugModuleEchartsForLevel" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
     <table id="bugModuleTableForLevel" class="table table-bordered" style="width:700px;margin: 20px auto">
	  
    </table>
    <div id="bugModuleEchartsForType" style="margin:50px auto 20px;width: 700px; height:400px;"></div>
     <table id="bugModuleTableForType" class="table table-bordered" style="width:700px;margin: 20px auto">
    </table>
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/analysisManage/bugModuleDistbuStat.js"></script>
