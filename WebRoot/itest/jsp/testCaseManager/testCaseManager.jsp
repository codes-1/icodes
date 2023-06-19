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
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="renderer" content="webkit">
<meta content="iTest" name="description">
<title>iTest</title>
</head>
<body>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/itest/css/testCaseManager/testCaseManager.css"/>
	
	<div id="caseLayout" class="exui-layout" style="width:100%; height:100%;">
		<div id="caseTreeDiv" data-options="region:'west'" title="<div><span>测试需求 &nbsp;</span><a id='switchModule' href='#' class='exui-linkbutton' ondblclick='switchModule();'><span>普通视图</span><span class='spancolor'>(双击切换)</span></a></div>" style="width:230px;padding:10px;overflow-y: auto;">
			<%-- <input id="currTaksId" type="hidden" value="<%=currTaksId%>"> --%>
			<ul id="caseTree" class="exui-tree"></ul>
		</div>
		<div data-options="region:'center'" style="overflow: hidden;" title="">
			<div id="generalTools" class="tools" data-options="">
				<button type="button" title="切换到复制粘贴模式" style="padding:9.5px 18px;" class="btn btn-default bntcss hoverBu" onclick="changeModel('1');"><i class="glyphicon glyphicon-folder-open"></i></button>
				<div class="input-field" style="width: 200px;">
					<span style="color:#1E7CFB;left:10px;">快速查询：</span>
					<!-- <input id="caseId" class="form-control indent-4-5" style="border: 1px solid #1E7CFB;" onKeyPress="onKeyEnter('caseId');" placeholder="用例编号+回车键"/> -->
					<input id="caseId" class="form-control indent-4-5" style="border: 1px solid #1E7CFB;" placeholder="用例编号+回车键"/>
				</div>
				<!-- <button type="button" class="btn btn-default bntcss hoverBu" onclick="clearInput();"><i class="glyphicon glyphicon-refresh"></i>刷新</button> -->
				<!-- <button type="button" class="btn btn-default bntcss hoverBu" onclick="caseAddWindow();" schkUrl="caseManagerAction!addInit" ><i class="glyphicon glyphicon-plus"></i>增加</button>
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="editWindow();"schkUrl="caseManagerAction!upInit" ><i class="glyphicon glyphicon-pencil"></i>修改</button> -->
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="caseAddWindow();" schkUrl="caseManagerAction!addInit" ><i class="glyphicon glyphicon-plus"></i>增加</button>
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="caseEditWindow();"schkUrl="caseManagerAction!upInit" ><i class="glyphicon glyphicon-pencil"></i>修改</button>
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="delConfirm();"schkUrl="caseManagerAction!delCase" ><i class="glyphicon glyphicon-remove"></i>删除</button>
				<button style="padding:9.5px 18px;" type="button" class="btn btn-default bntcss hoverBu" onclick="testerWorkInfo();" schkUrl="caseManagerAction!loadCaseBoard" title="项目人员工作面板" ><i class="glyphicon glyphicon-user"></i></button>
				<button style="padding:9.5px 18px;" type="button" class="btn btn-default bntcss hoverBu" onclick="caseExecWindow();" schkUrl="caseManagerAction!exeCase" title="执行用例"><i class="glyphicon glyphicon-asterisk"></i></button>
				<button style="padding:9.5px 18px;" type="button" class="btn btn-default bntcss hoverBu" onclick="toLibrary();" title="推荐到用例库"><i class="glyphicon glyphicon-hand-right"></i></button>
				<button style="padding:9.5px 18px;" type="button" class="btn btn-default bntcss hoverBu" onclick="upload();" title="导入"><i class="glyphicon glyphicon-arrow-up"></i></button>
				<a style="padding:9.5px 18px;" href="#" type="button" class="btn btn-default bntcss hoverBu" onclick="downloadd(this);" title="导出"><i class="glyphicon glyphicon-arrow-down"></i></a>
				<button type="button" class="btn btn-primary bntsearchcss" onclick="quickQuery();"schkUrl="caseManagerAction!loadCase" ><i class="glyphicon glyphicon-search"></i>查询</button>
			</div><!--/.top tools area-->
			<div id="specialTools" class="tools" style="display: none;" data-options="">
				<button style="padding:9.5px 18px;" type="button" title="切换到普通模式" class="btn btn-default bntcss hoverBu" onclick="changeModel('2');"><i class="glyphicon glyphicon-duplicate"></i></button>
				
				<div class="input-field" style="width: 200px;">
					<span style="color:#1E7CFB;left:10px;">快速查询：</span>
					<!-- <input id="case_Id" class="form-control indent-4-5" style="border: 1px solid #1E7CFB;" onKeyPress="onKeyEnter('case_Id');" placeholder="用例编号+回车键"/> -->
					<input id="case_Id" class="form-control indent-4-5" style="border: 1px solid #1E7CFB;" placeholder="用例编号+回车键"/>
				</div>
				<!-- <button type="button" class="btn btn-default bntcss hoverBu" onclick="clearInput();" schkUrl="" ><i class="glyphicon glyphicon-refresh"></i>刷新</button> -->
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="copyCase();" schkUrl="caseManagerAction!addInit"><i class="glyphicon glyphicon-copy"></i>复制</button>
				<button type="button" class="btn btn-default bntcss hoverBu" onclick="cutCase();" schkUrl="caseManagerAction!addInit"><i class="glyphicon glyphicon-scissors"></i>剪切</button>
				<button id="pasteCase" type="button" class="btn btn-default bntcss hoverBu" onclick="pasteCase();" style="display: none;"><i class="glyphicon glyphicon-paste"></i>粘贴</button>
				<button style="padding:9.5px 18px;" type="button" class="btn btn-default bntcss hoverBu" onclick="caseExecWindow();" schkUrl="caseManagerAction!exeCase" title="执行用例"><i class="glyphicon glyphicon-asterisk"></i></button>
				<button style="padding:9.5px 18px;" type="button" class="btn btn-default bntcss hoverBu" onclick="toLibrary();" title="推荐到用例库"><i class="glyphicon glyphicon-hand-right"></i></button>
				<button style="padding:9.5px 18px;" type="button" class="btn btn-default bntcss hoverBu" onclick="upload();" title="导入"><i class="glyphicon glyphicon-arrow-up"></i></button>
				<a style="padding:9.5px 18px;"type="button"  href="#" class="btn btn-default bntcss hoverBu" onclick="downloadd(this);" title="导出"><i class="glyphicon glyphicon-arrow-down"></i></a>
				<button type="button" class="btn btn-primary bntsearchcss" onclick="quickQuery();" schkUrl="caseManagerAction!loadCase"><i class="glyphicon glyphicon-search"></i>查询</button>
			</div><!--/.top tools area-->
			
			<table id="caseList" class="exui-datagrid" title="" style="width:100%;height: auto;" data-options=""></table>
			<div id="caseCountDiv" style="padding:5px;margin-top: 1em;color: #80a22e;border: 1px dashed #ccc;display: none;">
       			 <p id="caseCountInfo" clasee=""></p>
       			 <p style="color: orange;">因被停用测试需求下有用例，会使测试需求上标识的用例数与列表统计用例数不等</p>
    		</div>
		</div>
	</div>
	
	<!-- 查询模态窗 -->
	<div id="queryWin" title="" class="exui-window" data-options="
		modal:true,
		width: 720,
		footer:'#queryFoot',
		minimizable:false,
		maximizable:false,
		closed:true">
		<table class="form-table">
			<form id="queryForm">
				<tr >
					<td class="left_td">类别:</td>
					<td >
						<input id="caseType_Id" class="exui-combobox caseTypeId" name="dto.testCaseInfo.caseTypeId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'typeId',
    					textField:'typeName',
		    			prompt:'-请选择-'" style="width:120px;"/>
					</td>
					<td class="left_td" style="padding-left: 0.5em;">优先级:</td>
					<td >
						<input class="exui-combobox priId" name="dto.testCaseInfo.priId" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'typeId',
    					textField:'typeName',
		    			prompt:'-请选择-'" style="width:120px;"/>
					</td>
					<td class="left_td" style="padding-left: 1em;">状态:</td>
					<td >
						<input id="test_status" class="exui-combobox" name="dto.testCaseInfo.testStatus" data-options="
		    			validateOnCreate:false,
		    			editable:false,
		    			valueField:'value',
		    			textField:'desc',
		    			prompt:'-请选择-',
		    			data:[{desc:'',value:''},{desc:'待审核',value:'0'},{desc:'未测试',value:1},{desc:'通过',value:2},{desc:'未通过',value:3},{desc:'不适用',value:4},{desc:'阻塞',value:5},{desc:'待修正',value:6}]" style="width:120px;"/>
					</td>
				</tr>
				<tr >
					<td  class="left_td">编写人:</td>
					<td colspan="5">
						<!-- <input id="crNames" onclick="selUsers('crNames');"  name="dto.crName" class="form-control" style="width: 100%;" data-options="multiline:false,prompt:'编写人'" /> -->
						<input id="createrIds_name" onclick="selUsers('createrIds');"  name="dto.crName" class="form-control" style="width: 100%;" readonly="readonly" placeholder="请选择编写人"/>
						<input id="createrIds" type="hidden" name="dto.testCaseInfo.createrId" />
   			      </td>
				</tr>
				<tr >
					<td  class="left_td">处理人:</td>
					<td colspan="5">
						<!-- <input id="audit_id" onclick="selUsers('audit_id');" name="dto.testCaseInfo.auditId" class="form-control" style="width: 100%;" data-options="multiline:false,prompt:'处理人'" /> -->
   			     		<input id="auditIds_name" onclick="selUsers('auditIds');" name="auditNameF" class="form-control" style="width: 100%;" readonly="readonly" placeholder="请选择处理人"/>
   			     		<input id="auditIds" type="hidden" name="dto.testCaseInfo.auditId" />
   			     		
   			      </td>
				</tr>
				<tr >
					<td class="left_td">用例描述:</td>
					<td class="dataM_left" colspan="5">
						<input name="dto.testCaseInfo.testCaseDes" class="exui-textbox" style="width:100%;" data-options="multiline:false,prompt:'用例描述'" >
					</td>
				</tr>
				<tr id="remarkTr">
					<td class="left_td">执行成本:</td> 
					<td>
						<input name="dto.testCaseInfo.weight"  class="exui-numberbox" data-options="min:1,max:10" style="width: 120px;">
					</td>
				</tr>
		    </form>
		</table>
	</div>
	<div id="queryFoot" align="right">
			<a id="exec_2" href="#" class="exui-linkbutton" data-options="toggle:true,group:'g1',btnCls:'primary',size:'xs'" onclick="querySubmit()">查询</a>
			<a id="exec_4" href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="clearQueryForm()">清空</a>
			<a href="#" class="exui-linkbutton bntcss" data-options="toggle:true,group:'g1',size:'xs'" onclick="closeQueryWin()">返回</a>
	</div>
	
	<!-- 推荐到测试用例模态窗 -->
	<div id="toLibraryWin" class="exui-window" data-options="
		modal:true,
		width: 600,
		footer:'#toLibraryFoot',
		minimizable:false,
		maximizable:false,
		resizable:false,
		closed:true">
		<table id="toLibraryTree" style="width:50%;float:left;height:240px"></table>
		<div style="float:right;width:50%">
			<sup>*</sup>推荐理由：
			<textarea class="toLibraryReason" style="width:260px;resize:none;height:200px"/></textarea>
		</div>
	</div>
	<div id="toLibraryFoot" align="right">
		<a href="#" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="sureToLibrary()">确定</a>
		<a href="#" class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeLibraryWin()"  style="border: 1px solid #1e7cfb;color: #1e7cfb;">取消</a>
	</div>
	
	<!-- 上传测试用例模态窗 -->
	<div id="uploadWin" class="exui-window" data-options="
		modal:true,
		width: 550,
		footer:'#uploadFoot',
		minimizable:false,
		maximizable:false,
		resizable:false,
		closed:true">
		<button style="padding:9.5px 18px;" type="button" class="btn btn-default bntcss hoverBu" onclick="uploadFromExcel();"><i class="glyphicon glyphicon-arrow-up"></i>从Excel导入</button>
		<button style="padding:9.5px 18px;margin-left:10px" type="button" class="btn btn-default bntcss hoverBu" onclick="uploadFromLibrary();"><i class="glyphicon glyphicon-arrow-up"></i>从测试用例库导入</button>
		<a style="padding:9.5px 18px;margin-left:10px" type="button" class="btn btn-default bntcss hoverBu" onclick="downloadExcelModel(this);"><i class="glyphicon glyphicon-arrow-down"></i>下载Excel导入摸板</a>
	</div>
	<div id="uploadFoot" align="right">
		<a href="#" class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeUploadWin()"  style="border: 1px solid #1e7cfb;color: #1e7cfb;">取消</a>
	</div>
	
	<!-- 从测试用例库导入测试用例模态窗 -->
	<div id="uploadFromLibraryWin" class="exui-window" data-options="
		modal:true,
		width: 600,
		minimizable:false,
		maximizable:false,
		resizable:false,
		closed:true">
		<div class="exui-layout" style="height: 400px;">
			<div id="uploadFromLibraryTreeDiv" data-options="region:'west'" style="width:230px;padding:10px;">
				<ul id="uploadFromLibraryTree" class="exui-tree"></ul>
			</div>
			<div data-options="region:'center'" style="overflow: scroll;" title="">
				<div class="tools" data-options="">
					<button style="padding:9.5px 18px;background: #1e7cfb none repeat scroll 0 0;border: 1px solid #1e7cfb;color: #ffffff;" type="button" class="btn btn-default" onclick="agreeToUpload();"><i class="glyphicon glyphicon-ok"></i>确认</button>
					<button style="padding:9.5px 18px;border: 1px solid #1e7cfb;color:#1e7cfb;" type="button" class="btn btn-default" onclick="disagreeToUpload();"><i class="glyphicon glyphicon-remove"></i>关闭</button>
				</div>
				<table id="uploadFromLibraryCaseList" data-options="
					fitColumns: true,
					singleSelect: false,
					pagination: true,
					pageNumber: 1,
					pageSize: 10,
					pageList:[10,30,50],
					layout:['list','first','prev','manual','next','last','refresh','info']">
				</table>
			</div>
		</div>
	</div>
	
	<!-- 从excel导入测试用例模态窗 -->
	<div id="uploadFromExcelWin" class="exui-window" data-options="
		modal:true,
		width: 450,
		footer:'#uploadFromExcelFoot',
		minimizable:false,
		maximizable:false,
		resizable:false,
		closed:true">
		<form id="uploadForm" class="form-horizontal" method="post" enctype="multipart/form-data" role="form">
			<div class="file-box">
				<!-- <input id="excelField" class="form-control pull-left" style="max-width: 380px;" readonly type="text">
				<button class="pull-left" type="button">浏览...</button>
				<input id="importFile" name="dto.importFile" style="width:5em;" size="15" onchange="fileChange(this)" type="file" accept=".xls,.xlsx"> -->
				<input id="importFile" accept=".xls,.xlsx" name="dto.importFile" type="file">
				<br/><span style="color:red">请按照模板要求导入数据，否则导入将会不成功！</span>
			</div>
		</form>
	</div>
	<div id="uploadFromExcelFoot" align="right">
		<a href="#" class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="sureToUpload()">确认导入</a>
		<a href="#" class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" onclick="closeUploadFromExcelWin()"  style="border: 1px solid #1e7cfb;color: #1e7cfb;">取消</a>
	</div>
	
	<%@ include file="userGroupList.jsp" %>
	<%-- <script src="<%=request.getContextPath()%>/itest/js/jquery.form.min.js"></script> --%>
	<script src="<%=request.getContextPath()%>/itest/js/testCaseManager/caseCopyModel.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=request.getContextPath()%>/itest/js/testCaseManager/testCaseManager.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>