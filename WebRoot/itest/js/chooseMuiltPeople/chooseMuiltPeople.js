//存放传来的combobox的class
var targetClass = "";
$(function(){
	$("#groupList").xcombobox({
		url: baseUrl + '/userManager/userManagerAction!groupSel1.action',
		valueField:'keyObj',
	    textField:'valueObj'
	});
	$("#groupList").next("span").css({"left": "4px","position": "absolute","top": "-34px"});
});

//打开选择多人弹窗
function showSeletctPeopleWindow(id) {
	targetClass = id;
	$('#chooseSomePeopleForm')[0].reset();
	$("#groupList").xcombobox("setValue","");
	$("#chooseSomePeopleDiv").xwindow('setTitle','人员选择').xwindow('open');
	//初始化可选人员和已选人员
	initUser(id);
}
//初始化可选人员和已选人员
function initUser(id) {
	//加载已选择人员
	initSelectedPepoleDatas();
	//加载可选择人员
	initSelectPeopleDatas(id);
}
//加载已选择人员
function initSelectedPepoleDatas() {
	$("#selectedPepoleTa").xdatagrid({
		onDblClickRow: cannelSelectedDatas,
		height: "420px",
		columns:[[
			{field:'id',title:'id',hidden:'true', width:'100%',align:'center'},
			{field:'name',title:'已选人员--双击取消',width:'100%',align:'center'}
		]]
	});
	var row = $('#selectedPepoleTa').xdatagrid('getRows');
	for (var i = row.length - 1; i >=0; i--) {
		var index = $('#selectedPepoleTa').xdatagrid('getRowIndex', row[i]);
        $('#selectedPepoleTa').xdatagrid('deleteRow', index);  
	}
}
//加载可选择人员
function initSelectPeopleDatas(id) {
	$("#selectPepoleTa").xdatagrid({
		url: baseUrl + "/otherMission/otherMissionAction!getPeopleLists.action",
		method: 'post',
		queryParams: {
			'dto.filterStr' : "",
			'dto.groupId' : ""
		},
		onLoadSuccess: function(data){
			//已选的人员，selectPepoleTa去除，selectdPepoleTa加上
			var ids = $("."+id).xcombobox("getValues");
			if(ids.length > 0){
				var peopleSelected = ids;
				var rows = data.rows;
				for (var k = 0; k < peopleSelected.length; k++) {
					for(var i=0;i<data.rows.length;i++){
						if (peopleSelected[k] == data.rows[i].id) {
							$('#selectedPepoleTa').xdatagrid('appendRow',{
								id: data.rows[i].id,
								name: data.rows[i].name
							});
							var index = $('#selectPepoleTa').xdatagrid('getRowIndex', data.rows[i]);
					        $('#selectPepoleTa').xdatagrid('deleteRow', index);
					        break;
						}
						  
					}
				}
			}
		},
		onDblClickRow: appendDataToSelectedPepoleTa,
		pagination: false,
		height: "420px",
		columns:[[
			{field:'name',title:'备选人员--双击选择',width:'100%',align:'center'}
		]]
	});
}
//人员分配--从备选列添加到已选列
function appendDataToSelectedPepoleTa(rowIndex, rowData) {
	$('#selectedPepoleTa').xdatagrid('appendRow',{
		id: rowData.id,
		name: rowData.name
	});
	$('#selectPepoleTa').xdatagrid('deleteRow', rowIndex);
}
//取消添加的人员分配-从已选列表到备选列表
function cannelSelectedDatas(rowIndex, rowData) {
	$('#selectPepoleTa').xdatagrid('appendRow',{
		id: rowData.id,
		name: rowData.name
	});
	$('#selectedPepoleTa').xdatagrid('deleteRow', rowIndex);
}
//提交选择的人员，回填选择的人员
function submitSelectedPeoples(){
	var selectedPepoleRows = $('#selectedPepoleTa').xdatagrid('getRows');
	if (!selectedPepoleRows) {
		$.xalert({title:'提示',msg:'请选择人员！'});
		return;
	}
	var usersIds = [];
	for(var i=0;i<selectedPepoleRows.length;i++){
		usersIds.push(selectedPepoleRows[i].id);
	}
	$("#chooseSomePeopleDiv").xwindow('close');
	$("."+targetClass).xcombobox("setValues",usersIds);
}
//查询
function searchSeletctPeople(){
	$("#selectPepoleTa").xdatagrid({
		url: baseUrl + "/otherMission/otherMissionAction!getPeopleLists.action",
		method: 'post',
		queryParams: {
			'dto.filterStr' : $("#peopleNa").val(),
			'dto.groupId' : $("#groupList").xcombobox("getValue")
		},
		onLoadSuccess: function(data){
			//已选的人员，selectPepoleTa去除，selectdPepoleTa加上
			var selectedPepoleRows = $('#selectedPepoleTa').xdatagrid('getRows');
			if(selectedPepoleRows.length > 0){
				var rows = data.rows;
				for (var k = 0; k < selectedPepoleRows.length; k++) {
					for(var i=0;i<data.rows.length;i++){
						if (selectedPepoleRows[k].id == data.rows[i].id) {
							var index = $('#selectPepoleTa').xdatagrid('getRowIndex', data.rows[i]);
					        $('#selectPepoleTa').xdatagrid('deleteRow', index);
					        break;
						}
						  
					}
				}
			}
		},
		onDblClickRow: appendDataToSelectedPepoleTa,
		pagination: false,
		height: "420px",
		columns:[[
			{field:'name',title:'备选人员--双击选择',width:'100%',align:'center'}
		]]
	});
}
//取消
function closeSelectedPeoples(){
	$("#chooseSomePeopleDiv").xwindow('close');
}
//# sourceURL=chooseMuiltPeople.js