
$(function(){
	$.parser.parse();
	//加载ehcarts
	var itemId="";
	itemId = $("#analyitemId").val(); 
	
	//获取时间
	getBugDate(itemId);
});

function getBugDate(id){
	var url = baseUrl + "/singleTestTask/singleTestTaskAction!getBugDateLimit.action";
	$.post(
			url,
			{	"dto.singleTest.taskId":id
				},
			function(data){
				if(data.length>0){
					var dateArray = data.split("_");
					$("#startDate").datebox("setValue",dateArray[0]); 
					$("#endDate").datebox("setValue",dateArray[1]);
					loadNewFixCloseBugSummary(id,dateArray[0],dateArray[1]);
					loadBeforeOpenBugSummary(id,dateArray[0],dateArray[1]);
				}
				},'text');
}

//简要统计--时间段内新增、修改和关闭BUG概况
function loadNewFixCloseBugSummary(itemId,startDate,endDate){
	var url = baseUrl + "/analysis/analysisAction!getNewFixCloseBugSummary.action";
	
	$.post(
			url,
			{	"analysisDto.taskId":itemId,
				"analysisDto.startDate":startDate,
				"analysisDto.endDate":endDate
				},
			function(data){
			   if(data.length>0){
				   loadNewFixCloseBugTable(data);
			   }
			},'json');
}

//简要统计--截止到时间末BUG状态分布情况
function loadBeforeOpenBugSummary(itemId,startDate,endDate){
	var url = baseUrl + "/analysis/analysisAction!getBeforeOpenBugSummary.action";
	
	$.post(
			url,
			{	"analysisDto.taskId":itemId,
				"analysisDto.startDate":startDate,
				"analysisDto.endDate":endDate
				},
			function(data){
			   if(data.length>0){
				   loadBeforeOpenBugTable(data);
			   }
			},'json');
}

function loadNewFixCloseBugTable(data){
	 
	  var thead = "<tr>";
	  var tbody = "<tr>";
	  for(var i=0;i<data.length;i++){
		 switch(data[i][3]){
		    case 1:
		    	thead += "<th style='text-align:center;'>修改 </th>";
		    	tbody += "<td style='text-align:center;'>" + data[i][0] + "</td>";
		    	break;
		    case 2:
		    	thead += "<th style='text-align:center;'>关闭</th>";
		    	tbody += "<td style='text-align:center;'>" + data[i][1] + "</td>";
		    	break;
		    case 3:
		    	thead += "<th style='text-align:center;'>新增</th>";
		    	tbody += "<td style='text-align:center;'>" + data[i][2] + "</td>";
		    	break; 
		 }
	  } 
	  thead += "</tr>";
	  tbody += "</tr>";
	 $("#bugNewFixCloseSummaryThead").html(thead);
	 $("#bugNewFixCloseSummaryTbody").html(tbody);
}

function loadBeforeOpenBugTable(data){
	 document.getElementById("bugBeforeOpenSummaryTbody").innerHTML = "";
	  var tbody = "";
	  for(var i=0;i<data.length;i++){
		  tbody += "<tr><td style='text-align:center;'>" + data[i][0] + "</td>" + 
		           "<td style='text-align:center;'>" + data[i][1] + "</td></tr>" ;
	  } 
	 $("#bugBeforeOpenSummaryTbody").html(tbody);
}

//查看报表
document.getElementById("viewReport").addEventListener('click',function(){
	var itemId = "",startDate="",endDate="";
	itemId = $("#analyitemId").val(); 
	var startDate = $("#startDate").datebox('getValue'); 
	var endDate = $("#endDate").datebox('getValue'); 
	if(null == startDate || startDate == ""){
		$.xalert("请选择开始日期");
		return;
	}
	
	if(null == endDate || endDate == ""){
		$.xalert("请选择结束日期日期");
		return;
	}
	
	loadNewFixCloseBugSummary(itemId,startDate,endDate);
	loadBeforeOpenBugSummary(itemId,startDate,endDate);
});

//重置输入框
document.getElementById("resetInp").addEventListener('click',function(){
	$('#startDate').datebox('setValue', '');	
	$('#endDate').datebox('setValue', '');	
});
//@ sourceURL=bugSummary.js
