<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
th{
 	font-weight:400;
}

.tools button,.tools button:hover {
  border: 1px solid #1E7CFB;
    color: #1E7CFB;
}


tr>th{
  font-size: 14px;
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

div[class="datagrid-header-check"] input{
  display:none
}
</style>
<!--top tools area-->
<div class="tools">
	<div style="width: 300px;display: inline-block;">
		<span>切换数据类型：</span>
		<select class="exui-combobox" id="dataTypeAll"  style="width:170px"/></select>
	</div>
	
	<button type="button" class="btn btn-default" id="showAddBaseDataWin" schkUrl="testBaseSetAction!add"><i class="glyphicon glyphicon-plus"></i>增加</button>
	<button type="button" class="btn btn-default" id="showEdBaseDataWin" schkUrl="testBaseSetAction!update"><i class="glyphicon glyphicon-pencil"></i>修改</button>
	<button type="button" class="btn btn-default" id="showDelBaseDataWin" schkUrl="testBaseSetAction!delete"><i class="glyphicon glyphicon-remove"></i>删除</button>
    <button type="button" class="btn btn-default" id="showStopBaseDataWin" schkUrl="testBaseSetAction!swStatus" style="display:none"><i class="glyphicon glyphicon-off"></i>停用</button>
    <button type="button" class="btn btn-default" id="showStartBaseDataWin" schkUrl="testBaseSetAction!swStatus" style="display:none"><i class="glyphicon glyphicon-open"></i>启用</button>
    <button type="button" class="btn btn-default" id="setPreference" schkUrl="testBaseSetAction!setPreference"><i class="glyphicon glyphicon-open"></i>设置为默认项</button>
    

</div><!--/.top tools area-->

<table id="testSettingList" data-options="
	fitColumns: true,
	singleSelect: true,
	pagination: true,
	pageNumber: 1,
	pageSize: 10,
	layout:['list','first','prev','manual','next','last','refresh','info'],
	pageList: [10,30,50,100]
"></table>

<!-- 新增/修改单井模态窗开始 -->
<div id="addOrEditBaseWin" class="exui-window" style="display:none;" data-options="
	modal:true,
	width: 580,
	footer:'#addOrEditBaseFooter',
	minimizable:false,
	maximizable:false,
	closed:true">
	<table class="form-table" style="width:100%">
		<form id="addOrEditBaseForm" method="post">
			<tr class="hidden">
			  <td>
			     <input class="exui-textbox"  name="dto.testBaseSet.typeId" id="typeId"/>
			     <input class="exui-textbox"  name="dto.testBaseSet.initSubName" id="initSubName"/>
			     <input class="exui-textbox"  name="dto.testBaseSet.isDefault" id="isDefault"/>
			     <input class="exui-textbox"  name="dto.testBaseSet.status" />
			  </td>
			</tr>
			<tr>
				<th style="width:25%"><sup>*</sup>数据类型：</th>
	    		<td><select class="exui-combobox" name="dto.testBaseSet.subName" id="subName"  style="width:75%"/></select>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%"><sup>*</sup>数据名称：</th>
	    		<td>
	    		   <textarea class="exui-textbox"  name="dto.testBaseSet.typeName" id="typeName" style="width:75%"></textarea>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">备注：</th>
	    		<td>
	    		  <textarea class="exui-textbox" name="dto.testBaseSet.remark"  style="width:75%"></textarea>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th  style="width:25%">状态：</th>
	    		<td>
	    		  <div id="testBaseStatus">启用</div>
	    		</td>
	    	</tr>
	    </form>
	</table>
</div>
<div id="addOrEditBaseFooter" align="right" style="padding:5px;">
	<a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" id="saveBaseDataBtn">保存</a>
	<a class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style=" border: 1px solid #1E7CFB;color: #1E7CFB;" id="closeBaseDataBtn">关闭</a>
</div>
<!-- 新增/修改单井模态窗结束 -->

<script src="<%=request.getContextPath()%>/itest/js/testBaseSettingManager/testSettingManager.js" type="text/javascript" charset="utf-8"></script>