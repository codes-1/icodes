<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
 
th{
 	font-weight:400;
}

.tools button,.tools button:hover{
  border: 1px solid #1E7CFB;
    color: #1E7CFB;
}

tr>th{
  font-size: 14px;
}
 
 #testCasePackageM .datagrid-header-check>input, #executeTestCaseDlg .datagrid-header-check>input{
    display:none
  } 

.indent-60{
   text-indent: 65px;
}
.datagrid-header {
	height: 40px;
}
.datagrid-htable{
	height: 40px;
}
.datagrid-body td{
    padding: 11px 0;
} 

.multPkgPeopleSel{
   display:inline-block;
   width:25px;
   height:25px;
   margin-left:5px;
   vertical-align:middle;
   background-image:url(<%=request.getContextPath()%>/itest/images/mSearch.png);
   background-repeat: no-repeat;
   background-size: contain;
   cursor: pointer;
   
}

.multPkgPeopleSel:hover{
 background-image:url(<%=request.getContextPath()%>/itest/images/mSearch1.png);
}
</style>

<%@include file="/itest/jsp/chooseMuiltPeople/chooseMuiltPeople.jsp" %>

<!--top tools area-->
<div id="testCasePackageM">
<div class="tools" id="testcasepkgTool">
	<div class="input-field" style="width: 200px;">
	    <span>快速查询：</span>
		<input class="form-control indent-60" id="queryParam" placeholder="用例包名称+回车键"/>
	</div>
<!--  	<button type="button" class="btn btn-default" id="queryCasePkg"><i class="glyphicon glyphicon-search"></i>查询</button>
 -->	<button type="button" class="btn btn-default" id="showAddCasePkgWin"  schkUrl="testCasePackageAction!addTestCasePkg"><i class="glyphicon glyphicon-plus"></i>增加</button>
	<button type="button" class="btn btn-default" id="showEditCasePkgWin"  schkUrl="testCasePackageAction!updateTestCasePkg"><i class="glyphicon glyphicon-pencil"></i>修改</button>
	<button type="button" class="btn btn-default" id="delCasePkg"  schkUrl="testCasePackageAction!deleteTestCasePkg"><i class="glyphicon glyphicon-remove"></i>删除</button>
	
</div><!--/.top tools area-->

<table id="testCasePkgTb" data-options="
	fitColumns: true,
	rownumbers: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info'],
	pageList: [10,30,50,100]
"></table>


<!-- 新增/修改单井模态窗开始 -->
<div id="addOrEditPkgWin" class="exui-window " style="display:none;" data-options="
	modal:true,
	width: 580,
	height:350,
	footer:'#addOrEditPkgFooter',
	minimizable:false,
	maximizable:false,
	cls:'pkg',
	closed:true">
	<table class="form-table" style="width:100%">
		<form id="addOrEditPkgForm" method="post">
			<tr class="hidden">
			  <td>
			    <input class="exui-textbox"  name="dto.testCasePackage.packageId" id="packageId"/>
			    <input class="exui-textbox"  name="dto.testCasePackage.createTime" id="createTime"/>
			    <input class="exui-textbox"  name="dto.testCasePackage.updateTime" id="updateTime"/>  
			    <input class="exui-textbox"  name="dto.testCasePackage.createrId" id="createrId"/>  
			  </td>
			</tr>
			<tr>
				<th style="width:26%"><sup>*</sup>测试用例包名：</th>
	    		<td><input class="exui-textbox" name="dto.testCasePackage.packageName" id="packageName" style="width:75%"/></td>
	    	</tr>
	    	<tr>
	    	<th  style="width:25%"><sup>*</sup>分配人：</th>
	    		<td><select id="executor" class="exui-combobox executor"  data-options="
	    			valueField:'id',
	    			textField:'name',
	    			multiple:true,
	    			editable:false,
	    			prompt:'-请选择-'" style="width:75%"></select>	<span class="multPkgPeopleSel" onclick="showSeletctPeopleWindow('executor');" title="选择人员" ></span>
	    			
	    		</td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">执行环境：</th>
	    		<td>
	    		  <textarea class="exui-textbox" name="dto.testCasePackage.execEnvironment" id="execEnvironment"  style="width:75%"></textarea>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">备注：</th>
	    		<td>
	    		  <textarea class="exui-textbox" name="dto.testCasePackage.remark"  style="width:75%"></textarea>
	    		</td>
	    	</tr>
	    </form>
	</table>
</div>
<div id="addOrEditPkgFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" id="submitPkgBtn">保存</a>
	<a class="exui-linkbutton"  data-options="btnCls:'default',size:'xs'" style=" border: 1px solid #1E7CFB;color: #1E7CFB;" id="closePkgWinBtn">取消</a>
</div>
<!-- 新增/修改单井模态窗结束 -->

<!-- 选择用例模态窗开始 -->
<div id="testCaseWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 580,
	footer:'#testCaseFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
</div>
<div id="testCaseFooter" align="center" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" id="testCaseOkBtn">确认</a>
	<a class="exui-linkbutton" style=" border: 1px solid #1E7CFB;color: #1E7CFB;" data-options="btnCls:'default',size:'xs'" id="closeTestCaseBtn">取消</a>
</div>
<!-- 选择用例模态窗结束 -->
</div>
<script src="<%=request.getContextPath()%>/itest/js/testCasePackageMananger/testCasePackageMananger.js" type="text/javascript" charset="utf-8"></script>
