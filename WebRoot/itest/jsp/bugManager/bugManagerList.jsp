<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="cn.com.codes.common.util.StringUtils"%>
<%@page import="cn.com.codes.framework.security.filter.SecurityContextHolder"%>
<%@page import="cn.com.codes.framework.security.filter.SecurityContext"%>
<%@page import="cn.com.codes.framework.security.Visit"%>  
<%@page import="cn.com.codes.framework.security.VisitUser"%>

<%
SecurityContext sc = SecurityContextHolder.getContext();
Visit visit = sc.getVisit();
VisitUser user = visit.getUserInfo(VisitUser.class);

String accountId = (null != user.getId()) ? user.getId() : "";
String loginName = (null != user.getLoginName()) ? user.getLoginName() : "";
String userName= (null != user.getName()) ? user.getName() : "";
String currTaksId = visit.getTaskId();

if (StringUtils.isNullOrEmpty(userName)) {
	userName = loginName;
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>缺陷管理</title>
</head>
<body>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/bugManager/bugManagerList.css"/>
	<div id="bugLayout" class="exui-layout" style="width:100%; height:100%;">
		<div id="bugTreeDiv" data-options="region:'west'" title="<div><span>测试需求 &nbsp;</span></div>" style="width:230px;padding:10px;overflow-y: auto;">
			<ul id="bugTree" class="exui-tree"></ul>
		</div>
		<div data-options="region:'center'" style="overflow: hidden;" title="">
			<div id="generalTools" class="tools" data-options="">
				<div class="input-field" style="width: 200px;">
					<span style="color:#1E7CFB;left:10px;">快速查询：</span>
					<!-- <input id="bug_Id" class="form-control indent-4-5" style="border: 1px solid #1E7CFB;" onKeyPress="bugOnKeyEnter('bug_Id');" placeholder="bug编号+回车键"/> -->
					<input id="bug_Id" class="form-control indent-4-5" style="border: 1px solid #1E7CFB;" placeholder="bug编号+回车键"/>
				</div>
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="clearInput();"><i class="glyphicon glyphicon-refresh"></i>刷新</button>
				<!-- <button type="button" class="btn btn-default bntcss hoverBu" onclick="addBugWindow();" schkUrls="bugManagerAction!add"><i class="glyphicon glyphicon-plus"></i>增加</button> -->
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="bugAddWindow();" schkUrls="bugManagerAction!add"><i class="glyphicon glyphicon-plus"></i>增加</button>
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="bugEditWindow();" schkUrl="bugManagerAction!upInit"><i class="glyphicon glyphicon-pencil"></i>修改</button>
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="delBugConfirm();" schkUrl="bugManagerAction!delete"><i class="glyphicon glyphicon-remove"></i>删除</button>
				<a href="#" type="button" class="btn btn-default bntcss hoverBu" onclick="daochu(this);"><i class="glyphicon glyphicon-arrow-down"></i>导出</a>
				<button type="button" class="btn btn-primary bntsearchcss" onclick="bugQueryWin();" schkUrls="bugManagerAction!findBug"><i class="glyphicon glyphicon-search"></i>查询</button>
				<!-- <button type="button" class="btn btn-default bntsearchcss" onclick="fileUploadWin();"><i class="glyphicon glyphicon-search"></i>文件上传</button> -->
			</div><!--/.top tools area-->
			<table id="bugList" class="exui-datagrid" title="" style="width:100%;height: auto;" data-options=""></table>
			<div id="bugCountDiv" style="padding:5px;margin-top: 1em;color: #80a22e;border: 1px dashed #ccc;display: none;">
	      			 <p id="bugCountInfo" clasee=""></p>
	      			 <p style="color: orange;">因被停用测试需求下有用例，会使测试需求上标识的用例数与列表统计用例数不等</p>
	   		</div>
		</div>
	</div>
	
	<!--关联用例 -->
	<div id="lookBugWindown" class="exui-window" style="display: none;padding: 20px 25px 25px;" data-options="
		modal:true,
		width: 680,
		footer:'#testFlowFooter',
		minimizable:false,
		maximizable:false,
		closed:true">
		<ul class="nav nav-lattice">
			<li class="active" onclick="showExampleInfo()"><a href="#example" data-toggle="tab">用例信息</a></li>
			<li><a href="#baseInfo" data-toggle="tab">基本信息</a></li>
		</ul>
		<div class="tab-content" style="margin-top:20px;height: 450px;"> 
			<div id="baseInfo" class="tab-pane fade">
				<form id="baseInfoForm" method="post" class="form-horizontal">
					
					<table id="secondTable">
						<tr>
							<td>
								<span class="fontCs">测试需求:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="dto.moduleName" style="width:200px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">当前状态:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="dto.stateName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">作者:</span>
								<input class="exui-textbox" name="authorName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">测试人员:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="testName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs">开发人员:</span>
								<input class="exui-textbox" name="devName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">bug描述:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugDesc" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">类型:</span>
								<input class="exui-textbox" name="dto.bug.bugType.typeName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">等级:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugGradeName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs">再现平台:</span>
								<input class="exui-textbox" name="pltfomName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">来源:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="sourceName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">时机:</span>
								<input class="exui-textbox" name="occaName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">引入原因:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="geneCaseName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td> 
								<span class="fontCs" style="margin-left: 13px;">优先级:</span>
								<input class="exui-textbox" name="priName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">频率:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugFreqName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td colspan="2">
								<span class="fontCs" style="margin-left: 6px;">再现步骤:</span>
								<input class="exui-textbox" name="dto.bug.reProStep" style="width:525px;margin-left: 7px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td class="fontCs" colspan="2">
								<div class="first">
								<p>
									<span>发现日期:</span>
									<span id="reptDate"></span>
									
									<span style="margin-left: 40px;">发现版本:</span>
									<span id='bugReptVersion'></span>
									
									<span style="margin-left: 40px;">关闭版本:</span>
									<span id="currVer"></span>
								</p>
								</div>
								
							</td>
						</tr>
					
					</table>
				</form>
			</div>
			
			<div id="example" class="tab-pane fade in active">
				<div class="buttonPos" style="padding-bottom: 10px;">
					<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="newAddExample()"><i class="glyphicon glyphicon-plus"></i>新增用例</button>
					<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="relaExample()"><i class="glyphicon glyphicon-retweet"></i>关联用例</button>
				</div>
				
			</div>
			
			<table id="exampleListInfo" data-options="
						fitColumns: true,
						rownumbers: true,
						pagination: true,
						pageNumber: 1,
						pageSize: 10,
						layout:['list','first','prev','manual','next','last','refresh','info']">
		 	</table>
	  </div>
	</div>
	
	<!--意见交流 -->
	<div id="suggestionWindow" class="exui-window" style="display: none;padding: 20px 25px 25px;" data-options="
		modal:true,
		width: 680,
		minimizable:false,
		maximizable:false,
		closed:true">
		<ul class="nav nav-lattice">
			<li class="active" onclick="showSuggestInfo()"><a href="#example_sugg" data-toggle="tab">意见交流记录</a></li>
			<li><a href="#baseInfo_sugg" data-toggle="tab">基本信息</a></li>
		</ul>
		<div class="tab-content" style="margin-top:20px;height: 450px;"> 
			<div id="baseInfo_sugg" class="tab-pane fade">
				<form id="suggesInfoForm" method="post" class="form-horizontal">
					
					<table id="secondTables">
						<tr>
							<td style="padding-left:27px;">
								<span class="fontCs">模块:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="dto.moduleName" style="width:200px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">当前状态:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="dto.stateName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">作者:</span>
								<input class="exui-textbox" name="authorName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">测试人员:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="testName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs">开发人员:</span>
								<input class="exui-textbox" name="devName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">bug描述:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugDesc" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">类型:</span>
								<input class="exui-textbox" name="dto.bug.bugType.typeName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">等级:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugGradeName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs">再现平台:</span>
								<input class="exui-textbox" name="pltfomName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">来源:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="sourceName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<span class="fontCs" style="margin-left: 28px;">时机:</span>
								<input class="exui-textbox" name="occaName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs">引入原因:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="geneCaseName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td> 
								<span class="fontCs" style="margin-left: 13px;">优先级:</span>
								<input class="exui-textbox" name="priName" style="width:200px;margin-left: 7px;" readonly="readonly"/>
							</td>
							
							<td class="moveRight">
								<span class="fontCs" style="margin-left: 28px;">频率:&nbsp;&nbsp;</span>
								<input class="exui-textbox" name="bugFreqName" style="width:200px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td colspan="2">
								<span class="fontCs" style="margin-left: 6px;">再现步骤:</span>
								<input class="exui-textbox" name="dto.bug.reProStep" style="width:525px;margin-left: 7px;" readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td class="fontCs" colspan="2">
								<div style="margin-top: 15px;margin-left: 7px;">
								<p>
									<span>发现日期:</span>
									<span id="reptDate_sugg"></span>
									
									<span style="margin-left: 77px;">发现版本:</span>
									<span id='bugReptVersion_sugg'></span>
									
									<span style="margin-left: 77px;">关闭版本:</span>
									<span id="currVer_sugg"></span>
								</p>
								</div>
								
							</td>
						</tr>
					
					</table>
				</form>
			</div>
			
			<div id="example_sugg" class="tab-pane fade in active">
				<div class="buttonPos" style="padding-bottom: 10px;">
					<button type="button" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;" onclick="expressOpinion()"><i class="glyphicon glyphicon-plus"></i>发表意见</button>
				</div>
				
			</div>
			
			<table id="expressOpinion" data-options="
						fitColumns: true,
						rownumbers: true,
						singleSelect: true,
						pagination: true,
						pageNumber: 1,
						pageSize: 10,
						layout:['list','first','prev','manual','next','last','refresh','info']">
		 	</table>
	  </div>
	</div>
	
	
	<!--增加用例模态窗 -->
	<!-- <div id="addExample" title="" class="exui-window" data-options="
		modal:true,
		width: 600,
		minimizable:false,
		maximizable:false,
		closed:true">
			<input type="hidden" name="mdPath" value="" />
			<input itype="hidden" name="dto.testCaseInfo.taskId"/>
			<input id="moduleIds" type="hidden" name="dto.testCaseInfo.moduleId"/>
			<input type="hidden" name="dto.testCaseInfo.createrId"/>
			<input type="hidden" name="dto.testCaseInfo.testCaseId"/>
			<input type="hidden" name="dto.testCaseInfo.isReleased"/>
			<input type="hidden" name="dto.testCaseInfo.creatdate"/>
			<input type="hidden" name="dto.testCaseInfo.attachUrl"/>
			<input type="hidden" name="dto.testCaseInfo.auditId"/>
			<input type="hidden" name="dto.testCaseInfo.testStatus"/>
			<input type="hidden" name="dto.testCaseInfo.testData"/>
			<input type="hidden" name="dto.testCaseInfo.moduleNum"/>
	</div> -->
	<!--交流意见模态窗 -->
	<div id="expressOpinionNew" title="" class="exui-window" data-options="
		modal:true,
		width: 600,
		minimizable:false,
		maximizable:false,
		closed:true">
			<form id="expressForm" enctype="multipart/form-data" method="post">
			  <table class="form-table" style="font-size: 14px;">
				<input id="bugIdEx" name="dto.shortMsg.bugId" type="hidden"/>
				<tr>
					<td class="left_td"><sup>*</sup>我的意见:</td>
					<td >
	   			      <input name="dto.shortMsg.message" class="exui-textbox" style="width: 460px;height: 160px;" data-options="multiline:true,required:true,prompt:''" />
   			      	</td>
				</tr> 
				<tr>
					<td class="left_td"><sup>*</sup>发送给:</td>
					<td>
						<input id="hhjhfdghhhjhjj" class="exui-combobox" name="dto.shortMsg.recipCd" data-options="
		    			required:true,
		    			validateOnCreate:false,
		    			valueField:'value',
		    			textField:'desc',
		    			prompt:'-请选择-',
		    			data:[{desc:'项目所有成员',value:10},{desc:'测试人员',value:1},{desc:'分析人员',value:3},{desc:'分配人员',value:4},{desc:'开发人员',value:5},{desc:'开发负责人',value:7}]" style="width:230px;"/>
					</td>
				</tr>
			</table>
		    </form>
		<div class="buttonSt" style="margin-left: 60%;margin-top: 20px;">
			<button type="button" onclick="addExpressSubmit()" class="btn btn-primary">确认</button>
			<button onclick="addExpressReset()" type="button" style="border: 1px solid #1E7CFB;color: #1E7CFB;" class="btn btn-default">重置</button>
			<button type="button" onclick="canlceExpressWin()" class="btn btn-default" style="border: 1px solid #1E7CFB;color: #1E7CFB;">取消</button>
		</div>
	</div>
	
	<!-- 测试需求人员选择-->
	<div id="testUserWin" title="" class="exui-window" data-options="
		modal:true,
		width: 300,
		height:400,
		minimizable:false,
		maximizable:false,
		closed:true">
		<table id="testUserList" class="exui-datagrid" title="" style="width:100%;height: auto;" data-options=""></table>
	</div>
	<!-- 切换项目-->
	<div id="taskItemWin" title="切换项目" class="exui-window" data-options="
		modal:true,
		width: 900,
		minimizable:false,
		maximizable:false,
		closed:true">
		<table id="taskItemList" class="exui-datagrid" title="" style="width:100%;height: auto;" data-options=""></table>
	</div>
	<!-- 需求树-->
	<div id="needsTreeWin" title="选择需求" class="exui-window" style="overflow-y: auto;max-height: 600px;" data-options="
		modal:true,
		width: 400,
		minimizable:false,
		maximizable:false,
		closed:true">
		<ul id="needsTree" class="exui-tree" ></ul> 
	</div>
	<div id="bugIds" style="display: none;"></div>
	<div id="moduleIdH" style="display: none;"></div>
	<div id="bugReptVers" style="display: none;"></div>
	<input id="taskId" type="hidden">
	<script type="text/javascript">
		$(function(){
			$.parser.parse();
		});
	</script> 
	<script src="<%=request.getContextPath()%>/itest/js/bugManager/bugManagerList.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>