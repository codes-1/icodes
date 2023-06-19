<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/singleTestTaskManager/testTaskMagrList.css"/>
<style type="text/css">
#detailTable tbody tr{
	height: 2em;
	border: 0.5px solid gray;
}
#detailTable tbody tr td{
	border-right: 0.5px solid gray;
}
th{
 	font-weight:400;
}
</style>
<!--top tools area-->
<div class="tools">
	<div class="input-field" style="width: 180px;">
	    <select id="projectNa" class="form-control chzn-select" onchange="loadOtherMission()">
	    	
	    </select>
	</div>
	<div class="input-field" style="width: 180px;display:none" id="peopleNameList">
	    <select id="peopleName" class="form-control chzn-select" onchange="loadOtherMission()">
	    	
	    </select>
	</div>
	<!-- <button type="button" class="btn btn-default" style="background: #1E7CFB;border: 1px solid #1E7CFB;color: #ffffff;" onclick="loadOtherMission()"><i class="glyphicon glyphicon-search"></i>查询</button> -->
</div><!--/.top tools area-->
<!-- 面板展示div -->
<div style="width: 100%; background-color: rgb(255, 255, 255); padding-top: 15px; height: 485px; overflow-y: auto;">
	<div style="width:20%;float:left;text-align:center;padding:2px;border-right: 1px solid #eeeed1;height:510px">
		<div style="font-family: PingFangSC-Medium;font-size: 18px;color: white;border-bottom:4px solid #cdc1c5;margin-bottom:5px;height: 40px;background-color:#cdc1c5;border-radius: 4px;padding-top: 6px;font-weight: bold;">分配(<span id="distributionCount">0</span>)</div>
		<div id="distribution" style="padding-left:7px;padding-right:7px">
			<!-- <div style="border:1px gray solid">
				<span>你好</span><br/>
				负责人：<span>我</span><a onclick="dianji()">填写进度</a>
			</div> -->
		</div>
	</div>
	<div style="width:20%;float:left;text-align:center;padding:2px;border-right: 1px solid #eeeed1;height:510px">
		<div style="font-family: PingFangSC-Medium;font-size: 18px;color: white;border-bottom:4px solid #AAAAF6;margin-bottom:5px;height: 40px;background-color:#AAAAF6;border-radius: 4px;padding-top: 6px;font-weight: bold;">进行中(<span id="acceptCount">0</span>)</div>
		<div id="accept" style="padding-left:7px;padding-right:7px">
			
		</div>
	</div>
	<div style="width:20%;float:left;text-align:center;padding:2px;border-right: 1px solid #eeeed1;height:510px">
		<div style="font-family: PingFangSC-Medium;font-size: 18px;color: white;border-bottom:4px solid #A7DAA7;margin-bottom:5px;height: 40px;background-color:#A7DAA7;border-radius: 4px;padding-top: 6px;font-weight: bold;">完成(<span id="finishCount">0</span>)</div>
		<div id="finish" style="padding-left:7px;padding-right:7px">
			
		</div>
	</div>
	<div style="width:20%;float:left;text-align:center;padding:2px;border-right: 1px solid #eeeed1;height:510px">
		<div style="font-family: PingFangSC-Medium;font-size: 18px;color: white;border-bottom:4px solid #F0BB7D;margin-bottom:5px;height: 40px;background-color:#F0BB7D;border-radius: 4px;padding-top: 6px;font-weight: bold;">终止(<span id="terminationCount">0</span>)</div>
		<div id="termination" style="padding-left:7px;padding-right:7px">
			
		</div>
	</div>
	<div style="width:20%;float:left;text-align:center;padding:2px;height:510px">
		<div style="font-family: PingFangSC-Medium;font-size: 18px;color: white;border-bottom:4px solid #ff82ab;margin-bottom:5px;height: 40px;background-color:#ff82ab;border-radius: 4px;padding-top: 6px;font-weight: bold;">暂停(<span id="pauseCount">0</span>)</div>
		<div id="pause" style="padding-left:7px;padding-right:7px">
			
		</div>
	</div>
</div>
<!-- 填写进度模态窗 -->
<div id="addOrEditWin" class="exui-window" style="display:none;width:420px;" data-options="
	modal:true,
	width: 580,
	minimizable:false,
	maximizable:false,
	collapsible:false,
	resizable:false,
	closed:true">
	<form id="addOrEditForm" method="post">
		<table class="form-table">
		    <!--  隐藏字段，新增，修改提交时传到后台 -->
			<tr class="hidden">
				<td>
					<input id="missionId" name="dto.otherMission.missionId"/>
				</td>
			</tr>
			<tr>
	    		<th><sup>*</sup>实际工作量(小时)：</th>
	    		<td><input class="exui-textbox" name="dto.otherMission.actualWorkload" data-options="required:true,validateOnCreate:false,validType:'zhengshu',prompt:'请填写非负整数'" style="width:180px"/><span style="margin-top:5px">(累计)</span></td>
	    	</tr>
	    	<tr>
	    		<th><sup>*</sup>进度(%)：</th>
	    		<td><input class="exui-textbox" name="dto.otherMission.completionDegree" data-options="required:true,validateOnCreate:false,validType:'lessThan',prompt:'请填写0到100的数字',onChange:function(newValue,oldValue){
	    		changeMissionStatus(newValue)}" style="width:180px"/></td>
	    	</tr>
	    	<tr>
				<th><sup>*</sup>任务状态：</th>
	    		<td><input name="dto.otherMission.status" class="exui-combobox sss" data-options="
	    			required:true,
	    			validateOnCreate:false,
	    			valueField:'value',
	    			textField:'desc',
	    			editable:false,
	    			prompt:'-请选择-',
	    			data:[{desc:'分配',value:'0'},{desc:'进行中',value:'1'},{desc:'完成',value:'2'},{desc:'终止',value:'3'},{desc:'暂停',value:'4'}]" style="width:180px"/></td>
	    	</tr>
	    	<tr class="stopZone" style="display:none;">
	    		<th><sup>*</sup>终止原因：</th>
	    		<td>
	    			<textarea class="stopReason" name="stopReason" style="width:180px;resize:none;"/></textarea>
	    			<br>
	    			<span style="color:red">设为终止，不得再修改状态。</span>
	    		</td>
	    	</tr>
	    	<tr class="zantingZone" style="display:none;">
	    		<th><sup>*</sup>暂停原因：</th>
	    		<td>
	    			<textarea class="zantingReason" name="zantingReason" style="width:180px;resize:none;"/></textarea>
	    		</td>
	    	</tr>
		</table>
	</form>
	<footer style="padding:5px;text-align:right">
		<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submit11()">保存</a>
		<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeWin()" style="border: 1px solid #1e7cfb;color: #1e7cfb;">取消</a>
	</footer>
</div>


<!-- 查看详情模态窗 -->
<div id="detailWin" class="exui-window" data-options="
	modal:true,
	minimizable:false,
	maximizable:false,
	resizable:false,
	collapsible:false,
	closed:true">
	<ul class="nav nav-lattice" style="display:none">
		<li class="active"><a href="#xiangqing" data-toggle="tab">任务详情</a></li>
		<li><a href="#rizhi" data-toggle="tab">任务日志</a></li>
		<!-- <button onclick="closeDetailWin()" class="btn btn-default" style="margin-left:15px;border: 1px solid #1e7cfb;color: #1e7cfb;"><i class="glyphicon glyphicon-off"></i>关闭</button> -->
	</ul>
	<div class="tab-content" style="width:600px">
		<div id="xiangqing" class="tab-pane fade in active">
			<table id="detailTable">
				
			</table>
		</div>
		<div id="rizhi" class="tab-pane fade">
			
		</div>
	</div>
	<footer style="padding:5px;text-align:right">
		<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeDetailWin()" style="border: 1px solid #1e7cfb;color: #1e7cfb;"><i class="glyphicon glyphicon-off"></i>关闭</a>
	</footer>
</div>

<script type="text/javascript">
	$.parser.parse();
</script>

<script src="<%=request.getContextPath()%>/itest/js/otherMission/overview.js" type="text/javascript" charset="utf-8"></script>