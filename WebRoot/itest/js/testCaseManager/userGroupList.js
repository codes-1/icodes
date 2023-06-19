var objs = {
	$selectUserWin:$("#selectUserWin"),
	$userDefAllSelect: $("#userDefAllSelect"),
	$userSelected:$("#userSelected")

};

//$(function() {
////	$.parser.parse();
//	getUserGroupList();
//    loadSelectTable();
//    loadDefultTable();
//})
//选择组 combobox组件
function getUserGroupList(){
	$("#userGroup").xcombobox({
		url: baseUrl + '/userManager/userManagerAction!groupSel.action',
		valueField:'keyObj',
	    textField:'valueObj',
		onSelect: function(rec) {
			$("#userGroupId").val(rec.keyObj);
			searchUsersF();
		}
	});
}

function loadDefultTable(){
	   /* 候选人员列表*/
	objs.$userDefAllSelect.xdatagrid({
		url: baseUrl + '/userManager/userManagerAction!loadDefaultSelUser.action',
		method: 'post',
		height: 410,
		cache: false,
		pagination: true,
		pageNumber: 1,
		pageSize: 20,
	    layout:['list','first','prev',"manual", "sep",'next','last','refresh','info'],
		pageList: [12,30,50,100],
		columns:[[
			{field:'keyObj',title:'id',hidden:true,checkbox:true,align:'center'},
			{field:'valueObj',title:'备选人员--双击选择',width:'100%',align:'center'}

		]],
		onDblClickRow: function (index, row) {
//		   alert(JSON.stringify(row));
			 
		   var Ele = []//新建一个空的数组  
		   Ele.push(row);//把拆分好的数据用push的方法保存到数组Ele中。  
		   objs.$userDefAllSelect.xdatagrid('deleteRow',index)//根据索引删除对应的行。  
		   objs.$userSelected.xdatagrid('insertRow',{//在右边插入新行。  
		   index : 0,  
		   row : {  
			   'keyObj' : Ele[0].keyObj  ,
			   'valueObj' : Ele[0].valueObj
		    }  
		   })  
		  }
	});
}

function loadSelectTable(){
	/*已选人员列表*/
	objs.$userSelected.xdatagrid({
		user:'', 
		height: 410,
		cache: false,
		pagination: false,
		pageNumber: 1,
		pageSize: 12,
	    layout:['list','first','prev',"manual", "sep",'next','last','info'],
		pageList: [12,30,50,100],
		columns:[[
			{field:'keyObj',title:'id',hidden:true,checkbox:true,align:'center',formatter:userSelId},
			{field:'valueObj',title:'备选人员--双击删除',width:'100%',align:'center',formatter:userSelName}

		]],
		onDblClickRow: function (index, row) {
			   var Ele = []//新建一个空的数组  
			   Ele.push(row);//把拆分好的数据用push的方法保存到数组Ele中。  
			   objs.$userSelected.xdatagrid('deleteRow',index)//根据索引删除对应的行。  
			   objs.$userDefAllSelect.xdatagrid('insertRow',{//在右边插入新行。  
			   index : 0,  
			   row : {  
				   'keyObj' : Ele[0].keyObj  ,
				   'valueObj' : Ele[0].valueObj
			    }  
			   })  
			  }
		
	});
	
}

var createrIds = new Array();
var createrNames = new Array();
var auditIds = new Array();
var auditNames = new Array();
var targetType;
function selUsers(target){ 
	targetType = target;
	if($("#"+target).val()!=""){
		var valueId = $("#"+target).val();
		var valueName = $("#"+target+"_name").val();
		var valueIds = valueId.split(" ");
		var valueNames = valueName.split(",");
		if(target=="createrIds"){
			createrIds = valueIds;
			createrNames = valueNames;
		}else{
			auditIds = valueIds;
			auditNames = valueNames;
		}
	}
	objs.$selectUserWin.xform("clear");
	$("#userGroupId").val("");
	getUserGroupList();
    loadSelectTable();
    loadDefultTable();
    objs.$selectUserWin.xwindow("vcenter");
	objs.$selectUserWin.xwindow('setTitle','选择组员').xwindow('open');
	objs.$userDefAllSelect.xdatagrid({
//		url: baseUrl + '/userManager/userManagerAction!loadDefaultSelUserInAll.action?dto.isAjax=true',
		url: baseUrl + '/userManager/userManagerAction!loadDefaultSelUser.action?dto.isAjax=true',
		method: 'get',
		height: 410,
		cache: false,
		pagination: true,
		pageNumber: 1,
		pageSize: 20,
	    layout:['list','first','prev',"manual", "sep",'next','last','info'],
		pageList: [20,30,50,100],
		columns:[[
			{field:'keyObj',title:'id',hidden:true,checkbox:true,align:'center'},
			{field:'valueObj',title:'备选人员--双击选择',width:'100%',align:'center'}

		]],
		onDblClickRow: function (index, row) {
		   var Ele = []//新建一个空的数组  
		   Ele.push(row);//把拆分好的数据用push的方法保存到数组Ele中。  
		   objs.$userDefAllSelect.xdatagrid('deleteRow',index)//根据索引删除对应的行。  
		   objs.$userSelected.xdatagrid('insertRow',{//在右边插入新行。  
		   index : 0,  
		   row : {  
			   'keyObj' : Ele[0].keyObj  ,
			   'valueObj' : Ele[0].valueObj
		    }  
		   })  
		  },
		  onLoadSuccess:function(){
			  var item = objs.$userDefAllSelect.xdatagrid('getRows');
				var index01 = objs.$userDefAllSelect.xdatagrid('getRowIndex',item[0]);
				var item02 = objs.$userSelected.xdatagrid('getRows');
				if (item02.length>0) {  
			        for (var i = item02.length - 1; i >= 0; i--) {  
			            var index = objs.$userSelected.xdatagrid('getRowIndex', item02[i]);  
			            objs.$userSelected.xdatagrid('deleteRow', index);  
			        }  
			    }  
				if($("#"+target+"_name").val()!=="" && $("#"+target).val()!==""){
			    	var rows =[];
			        var Ele = []//新建一个空的数组  
			        if(target=="createrIds"){
			        	for(i=0;i<createrIds.length;i++){
			        		var row ={
			        				'keyObj':"",
			        				'valueObj':""
			        		};
			        		rows.push(row);
			        		rows[i].valueObj = createrNames[i];
			        		rows[i].keyObj = createrIds[i];
			        		
			        	}
			        }else{
			        	for(i=0;i<auditIds.length;i++){
			        		var row ={
			        				'keyObj':"",
			        				'valueObj':""
			        		};
			        		rows.push(row);
			        		rows[i].valueObj = auditNames[i];
			        		rows[i].keyObj = auditIds[i];
			        		
			        	}
			        }

			        for(j=0;j<rows.length;j++){
			        	var eee = rows[j].valueObj
			         	 objs.$userSelected.xdatagrid('insertRow',{//在右边插入新行。  
			      		   index : 0,  
			      		   row : {  
			      			   'keyObj' : rows[j].keyObj  ,
			      			   'valueObj' : rows[j].valueObj
			      		    }  
			      		   }) 
			      		   for(k=0;k<item.length;k++){
			      			   if(rows[j].keyObj==item[k].keyObj){
			      				//左边表对应删除这些用户
			      	      		var index01 = objs.$userDefAllSelect.xdatagrid('getRowIndex',item[k]);  
			      	            objs.$userDefAllSelect.xdatagrid('deleteRow', index01);   
			      			   }
			      		   }
			              
			        	
			        }
			    }
		  }
	});
	
}

function userSelId(value,row,index){
	if(value==""||value==null){
		return "-"
	}
	return value;
}

function userSelName(value,row,index){
	if(value==""||value==null){
		return "-"
	}return value;
}

//成员窗口点击确定
function submitSelectUser(){
	var rows = objs.$userSelected.xdatagrid("getRows");
	var userNames = new Array();
	var userIds = new Array();
	for(i=0;i<rows.length;i++){
		userNames.push(rows[i].valueObj);
		userIds.push(rows[i].keyObj);
	}
	var name = userNames.toString()
	var ids = userIds.toString().replace(/,/g, " ");
	if(targetType=="createrIds"){
		$("#createrIds_name").val(name);
		$("#createrIds_name").attr('title',name);
		$("#createrIds").val(ids);	
	}else{
		$("#auditIds_name").val(name);
		$("#auditIds_name").attr('title',name);
		$("#auditIds").val(ids);	
	}
	cancleUserWin();
	
}

function cancleUserWin() {
	objs.$selectUserWin.xform("clear");
	$("#userGroupId").val("");
	objs.$selectUserWin.xwindow('close');
}

function searchUsersF(){
	$.post(
		baseUrl + "/userManager/userManagerAction!selectUser.action",
		{
			'dto.userName':$("#selectUserName").val(),
//			'dto.group.id' : $("#userGroup").xcombobox("getValue"),
			'dto.group.id' :$("#userGroupId").val()
		},
		function(data,status,xhr){
			if(status=='success'){
				 objs.$userDefAllSelect.xdatagrid("loadData",data);
			}else{
				$.xnotify(xhr.error(), {type:'warning'});
			}
		},
	"json");
}

//# sourceURL=userGroupList.js