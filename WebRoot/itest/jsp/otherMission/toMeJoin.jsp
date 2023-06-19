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
	<div class="input-field" style="width: 160px;">
		<span>任务名称：</span>
		<input id="missionName" class="form-control indent-4-5" placeholder="名称"/>
	</div>
	<div class="input-field" style="width: 180px;">
	    <select id="projectNa" class="form-control chzn-select">
	    	
	    </select>
	</div>
	<div class="input-field" style="width: 180px;">
	    <select id="statu" class="form-control chzn-select">
	    	<option value="">-请选择状态-</option>
	    	<option value="0">分配</option>
	    	<option value="1">进行中</option>
	    	<option value="2">完成</option>
	    	<option value="3">终止</option>
	    	<option value="4">暂停</option>
	    </select>
	</div>
	<button id="searchCon" type="button" class="btn btn-default" style="background: #1E7CFB;border: 1px solid #1E7CFB;color: #ffffff;" onclick="searchOtherMission()"><i class="glyphicon glyphicon-search"></i>查询</button>
	<!-- <button id="resetCon" type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="resetInfo()"><i class="glyphicon glyphicon-trash"></i>重置</button> -->
	<!-- <div style="float:right">
		<button id="editCon" type="button" schkUrl="singleTestTaskAction!add" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="showEditWin()"><i class="glyphicon glyphicon-pencil"></i>修改</button>
	</div> -->
</div><!--/.top tools area-->
<!-- 其他任务显示列表 -->
<table id="missionDg" data-options="
	fitColumns: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	pageList:[10,30,50],
	layout:['list','first','prev','manual','next','last','refresh','info']
"></table>


<!-- 新增/修改单其他任务模态窗 -->
<!-- <div id="addOrEditWin" class="exui-window" style="display:none;width:400px;" data-options="
	modal:true,
	width: 580,
	footer:'#addOrEditFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<form id="addOrEditForm" method="post">
		<table class="form-table">
		     隐藏字段，新增，修改提交时传到后台
			<tr class="hidden">
				<td>
					<input id="missionId" name="dto.otherMission.missionId"/>
					<input id="actualWorkload" name="dto.otherMission.actualWorkload"/>
					<input id="completionDegree" name="dto.otherMission.completionDegree"/>
					<input id="createTime" name="dto.otherMission.createTime"/>
					<input id="updateTime" name="dto.otherMission.updateTime"/>
					<input id="missionStatus" name="dto.otherMission.status"/>
					<input id="createUserId" name="dto.otherMission.createUserId"/>
				</td>
			</tr>
			<tr>
	    		<th><sup>*</sup>实际工作量：</th>
	    		<td><input class="exui-textbox" name="dto.otherMission.actualWorkload" data-options="required:true,validateOnCreate:false" style="width:180px"/></td>
	    	</tr>
	    	<tr>
	    		<th><sup>*</sup>进度：</th>
	    		<td><input class="exui-textbox" name="dto.otherMission.completionDegree" data-options="required:true,validateOnCreate:false" style="width:180px"/></td>
	    	</tr>
	    	<tr>
				<th><sup>*</sup>任务状态：</th>
	    		<td><input name="dto.otherMission.status" class="exui-combobox" data-options="
	    			required:true,
	    			validateOnCreate:false,
	    			valueField:'value',
	    			textField:'desc',
	    			prompt:'-请选择-',
	    			data:[{desc:'分配',value:'0'},{desc:'进行中',value:'1'},{desc:'完成',value:'2'},{desc:'终止',value:'3'}]" style="width:180px"/></td>
	    	</tr>
		</table>
	</form>
</div>
<div id="addOrEditFooter" align="center" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="submit()">保存</a>
	<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeWin()">取消</a>
</div> -->

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
			<!-- <table id="logDg" data-options="
				fitColumns: true,
				singleSelect: true,
				pagination: true,
				pageNumber: 1,
				pageSize: 10,
				pageList:[10,30,50],
				layout:['list','first','prev','manual','next','last','refresh','info']
			"></table> -->
		</div>
	</div>
	<footer style="padding:5px;text-align:right">
		<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeDetailWin()" style="border: 1px solid #1e7cfb;color: #1e7cfb;"><i class="glyphicon glyphicon-off"></i>关闭</a>
	</footer>
</div>

<script type="text/javascript">
	$.parser.parse();
</script>
<script src="<%=request.getContextPath()%>/itest/js/otherMission/toMeJoin.js" type="text/javascript" charset="utf-8"></script>