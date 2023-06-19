<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/singleTestTaskManager/testTaskMagrList.css"/>
<div style="width:100%;height:278px">
	<div style="float:left;width:65%;padding:5px;border:#FFFFFF 1px solid;border-radius:2px;background-color:#FFFFFF;height:278px;">
		<div style="width:100%;padding:5px 10px">
			<p style="padding-left:10px;border-left:3px red solid;margin-bottom: 30px;font-weight: bold;">用例摘要</p>
			<div style="width:12.5%;float:left;text-align:center">
				<span id="11" style="color:#1C86EE;font-size:2em"></span><br/>总用例数
			</div>
			<div style="width:12.5%;float:left;text-align:center">
				<span id="22" style="color:green;font-size:2em"></span><br/>通过数
			</div>
			<div style="width:12.5%;float:left;text-align:center">
				<span id="33" style="color:red;font-size:2em"></span><br/>未通过数
			</div>
			<div style="width:12.5%;float:left;text-align:center">
				<span id="44" style="color:#8B8378;font-size:2em"></span><br/>阻塞数
			</div>
			<div style="width:12.5%;float:left;text-align:center">
				<span id="55" style="color:#8B8378;font-size:2em"></span><br/>未测试数
			</div>
			<div style="width:12.5%;float:left;text-align:center">
				<span id="66" style="color:#8B8378;font-size:2em"></span><br/>不适用数
			</div>
			<!-- <div style="width:12.5%;float:left;text-align:center">
				<span id="77" style="color:#8B8378;font-size:2em"></span><br/>待修正数
			</div>
			<div style="width:12.5%;float:left;text-align:center">
				<span id="88" style="color:#8B8378;font-size:2em"></span><br/>待审核数
			</div> -->
			<br/>
			<p style="width:100%;border-top:1px #EAEAEA solid;margin-top: 70px;"></p>
			<p style="padding-left:10px;border-left:3px red solid;margin-bottom: 30px;font-weight: bold;">缺陷摘要</p>
			<div style="width:12.5%;float:left;text-align:center">
				<span id="aa" style="color:#1C86EE;font-size:2em"></span><br/>总BUG数
			</div>
			<div style="width:12.5%;float:left;text-align:center">
				<span id="bb" style="color:#1C86EE;font-size:2em"></span><br/>有效BUG数
			</div>
			<div style="width:18%;float:left;text-align:center">
				<span id="cc" style="color:#8B8378;font-size:2em"></span><br/>已关闭BUG数
			</div>
			<div style="width:15%;float:left;text-align:center">
				<span id="dd" style="color:#8B8378;font-size:2em"></span><br/>待处理BUG数
			</div>
			<div style="width:15%;float:left;text-align:center">
				<span id="fixCount" style="color:#8B8378;font-size:2em"></span><br/>已改未确认
			</div>
			<div style="width:15%;float:left;text-align:center">
				<span id="noBugCount" style="color:#8B8378;font-size:2em"></span><br/>非错未确认
			</div>
		</div>
	</div>
	<div style="float:right;width:33%;padding:5px;border:#FFFFFF 1px solid;border-radius:2px;background-color:#FFFFFF">
		<div style="width:100%;padding:5px 10px">
			<p style="padding-left:10px;border-bottom:1px #EAEAEA solid;height:30px;font-weight: bold;">任务摘要</p>
			<div style="width:100%;float:left;background-color:#F0F8FF;border-left:5px #BFEFFF solid;padding: 10px 20px;height: 40px;margin-bottom:20px">
				<span style="float:left">我负责的</span><div style="float:right; margin-top: -6px;"><span id="ee" style="color:#BFEFFF;font-size:2em"></span><span>  个</span></div>
			</div>
			<div style="width:100%;float:left;background-color:#EEE9E9;border-left:5px #9BCD9B solid;padding: 10px 20px;height: 40px;margin-bottom:20px">
				<span style="float:left">我创建的</span><div style="float:right; margin-top: -6px;"><span id="ff" style="color:#9BCD9B;font-size:2em"></span><span>  个</span></div>
			</div>
			<div style="width:100%;float:left;background-color:#CDC673;border-left:5px #CD8500 solid;padding: 10px 20px;height: 40px;margin-bottom:20px">
				<span style="float:left">我关注的</span><div style="float:right; margin-top: -6px;"><span id="gg" style="color:#CD8500;font-size:2em"></span><span>  个</span></div>
			</div>
			<div style="width:100%;float:left;background-color:#CDCDB4;border-left:5px #836FFF solid;padding: 10px 20px;height: 40px;">
				<span style="float:left">所有</span><div style="float:right; margin-top: -6px;"><span id="hh" style="color:#836FFF;font-size:2em"></span><span>  个</span></div>
			</div>
		</div>
	</div>
</div>
<!-- 其他任务显示列表 -->
<div style="width:100%;background-color:#FFFFFF;margin-top: 20px;">
	<p style="font-weight: bold;">项目人员摘要</p>
	<table id="bugWorkList"></table>
</div>

<script src="<%=request.getContextPath()%>/itest/js/overview/loadInformation.js" type="text/javascript" charset="utf-8"></script>