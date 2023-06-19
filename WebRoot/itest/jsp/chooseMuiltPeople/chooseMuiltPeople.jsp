<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <style>
      #chooseSomePeopleForm span[class='textbox combo'], #chooseSomePeopleForm span[class='textbox combo textbox-focused']{
        position: static!important;
      }
    </style>
	<!-- 选择多个人弹窗 -->
	<div id="chooseSomePeopleDiv" class="exui-window" style="display:none;" data-options="
		modal:true,
		width: 580,
		height: 550,
		minimizable:false,
		maximizable:false,
		closed:true">
	
		<div class="tab-content">
			<div id="chooseSomePeople-toolbar">
				<form id="chooseSomePeopleForm" name="chooseSomePeopleForm">
					<table style="width:100%;">
						<tr>
							<td style="width: 70%;">
							<div class="input-field" style="width: 135px;">
								<input id="groupList" class="exui-combobox" 
									data-options="
						   			validateOnCreate:false,
						   			editable:false,
						   			prompt:'-选择组-' " style="width:135px;"/>
						   		</div>
						   		<div class="input-field" style="width: 105px;">
									<input type="text" id="peopleNa" class="form-control" style="width:100%;margin-right:5px"/>
								</div>
								<!-- <a class="exui-linkbutton" data-options="btnCls:'primary',size:'xs'" onclick="searchSeletctPeople()">查询</a> -->
								<button type="button" class="btn btn-default" style="background: #1E7CFB;border: 1px solid #1E7CFB;color: #ffffff;" onclick="searchSeletctPeople()"><i class="glyphicon glyphicon-search"></i>查询</button>
							</td>
							<td style="width: 30%; text-align:right">
								<!-- <a href="#" class="exui-linkbutton" data-options="btnCls:'default',size:'xs'" style="border: 1px solid #1e7cfb; color: #1e7cfb;" onclick="initAssignUser();">刷新备选人员</a> -->
								<button type="button" class="btn btn-default" onclick="submitSelectedPeoples();" style="border: 1px solid #1e7cfb;color: #1e7cfb;margin-top: -1.8px;">确定</button>
								<button type="button" class="btn btn-default" onclick="closeSelectedPeoples()" style="border: 1px solid #1e7cfb;color: #1e7cfb;margin-top: -1.8px;">取消</button>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div style="width: 50%; float: left; padding-top: 10px;">
				<table id="selectPepoleTa" data-options="
					fitColumns: true,
					singleSelect: true,
					pagination: false,
					layout:['list','first','prev','manual','next','last','refresh','info']
					"></table>		
			</div>
			<div style="width: 50%; float: left; padding-top: 10px;">
				<table id="selectedPepoleTa" data-options="
					fitColumns: true,
					singleSelect: true,
					pagination: false,
					layout:['list','first','prev','manual','next','last','refresh','info']
					"></table>
			</div>
		</div>
	
	</div>
		
	<script src="<%=request.getContextPath()%>/itest/js/chooseMuiltPeople/chooseMuiltPeople.js" type="text/javascript" charset="utf-8"></script>
