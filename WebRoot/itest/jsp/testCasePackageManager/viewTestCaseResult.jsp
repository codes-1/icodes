<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
  .result-wrapper{
      height:155px;
      border-top:1px solid #eee;
      color:#fff;
  }
  
  .result-content{
    display:inline-block;
    width:140px;
    height:140px;
    border:1px solid #eee;
  }
  
   .result-content div{
     width:100%;
     height:50%;
     text-align: center;
     line-height: 60px;
  }
  
  .result-content div:nth-child(odd){
    
  }
</style>
<div class="result-wrapper" >
  <div class="result-content" >
    <div style=" background-color:#a3d3fe; font-size: 1.5em;">总用例数</div>
     <div style="color:#a3d3fe;font-size: 2em;" id="allCount">0</div>
  </div><div class="result-content" >
     <div style="background-color:#71CD71;font-size: 1.5em;">通过数</div>
     <div style="color:#71CD71;font-size: 2em;" id="passCount">0</div>
  </div><div class="result-content" >
     <div style="background-color:#ED8282;font-size: 1.5em;">未通过数</div>
     <div style="color:#ED8282;font-size: 2em;" id="failedCount">0</div>
  </div><div class="result-content" >
     <div style="background-color:#EEAA5A;font-size: 1.5em;">阻塞数</div>
     <div style="color:#EEAA5A;font-size: 2em; " id="blockCount">0</div>
  </div><div class="result-content" >
    <!--  <div style="background-color:#72D2EB;font-size: 1.5em;">待审核数</div>
     <div style="color:#72D2EB;font-size: 2em;" id="waitAuditCount">0</div>
  </div><div class="result-content" > -->
      <div style="background-color:#B78EEA;font-size: 1.5em;">不适用数</div>
     <div style="color:#B78EEA;font-size: 2em;" id="invalidCount">0</div>
  </div><div class="result-content" >
     <!--  <div style="background-color:#9797F0 ;font-size: 1.5em;">待修正数</div>
     <div style="color:#9797F0 ;font-size: 2em;" id="waitModifyCount">0</div>
  </div><div class="result-content" > -->
     <div style="background-color:#B7AB9C;font-size: 1.5em;">未测试数</div>
     <div style="color:#B7AB9C;font-size: 2em;" id="noTestCount">0</div>
  </div>
</div>
<div style="display: inline-block; float: right;  margin-top: 15px;margin-right: 10px;" >
    <button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="closeTestcaseResultWin()"><i class="glyphicon glyphicon-off"></i>关闭</button>
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/js/testCasePackageMananger/viewTestCaseResult.js"></script>