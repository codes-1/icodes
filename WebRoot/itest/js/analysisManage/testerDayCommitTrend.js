
$(function(){
	$.parser.parse();
	
	var itemId="",projectName="",startDate="",endDate="",proNum="";
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
					loadTesterDayCommitData(id,dateArray[0],dateArray[1]);
					loadTesterDayCommitDataClose(id,dateArray[0],dateArray[1])
				}
				},'text');
}

//测试人员日提交有效BUG趋势
function loadTesterDayCommitData(itemId,startDate,endDate){
	var url = baseUrl + "/analysis/analysisAction!getTesterDayCommitTrend.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId,
				"analysisDto.startDate":startDate,
				"analysisDto.endDate":endDate
			},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data);
				   loadEcharts('测试人员日提交有效BUG趋势','testerDayEcharts');
				   loadTable(data);
			   }
			},'json');
}

//测试人员日关闭BUG趋势
function loadTesterDayCommitDataClose(itemId,startDate,endDate){
	var url = baseUrl + "/analysis/analysisAction!getTesterDayCommitTrendForClosed.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId,
				"analysisDto.startDate":startDate,
				"analysisDto.endDate":endDate
			},
			function(dataObj){
			   if(dataObj.length>0){
				   //构造echarts数据
				   constrEchartsDataForClosed(dataObj);
				   loadEchartsForClosed('测试人员日关闭BUG趋势','testerDayForClosedEcharts');
				   loadTableForClosed(dataObj);
			   }else{
				   $("#testerDayForClosed").html("<div style='font-size: 25px;text-align: center;padding-top: 105px;'>暂无此项目报表数据</div>");
				}
			},'json');
}


//构造echarts图表数据
function constrEchartsData(object){
	
	getNameAndDate(object.concat());
	getSeriesData(object.concat());
}


//获取echarts的类别、日期
var names = [];
var itemDate = [];
function getNameAndDate(sourceData){
	names = [];
	itemDate = [];
	for(var i=0;i<sourceData.length;i++){
		if(names.indexOf(sourceData[i][2])==-1){
			if(sourceData[i][2] == null){
				sourceData[i][2] = "-";
			}
			names.push(sourceData[i][2]);
		}
		if(itemDate.indexOf(sourceData[i][1])==-1){
			if(sourceData[i][1] == null){
				sourceData[i][1] = "-";
			}
			itemDate.push(sourceData[i][1]);
		}
	}
}

//获取echarts的seriesData数据
var seriesData = [];
function getSeriesData(sourData){
	seriesData = [];
	for(var i=0;i<names.length;i++){
		var obj = {
			      name:names[i],
		          type:'line',
		          data:[]		
		}
		for(var j=0;j<itemDate.length;j++){
			//同一个用户、同一时间的bug总数
			var bugSum = 0; 
			//标识是否有相等值
			var flagValue = false;
			for(var h=0;h<sourData.length;h++){
				if(names[i]==sourData[h][2] && itemDate[j]==sourData[h][1]){
					bugSum +=sourData[h][0];
					flagValue = true;
				}
			}
			//false:没有;则该位置数据为0
			if(flagValue != true){
				obj.data.push(0);
			}else{
				obj.data.push(bugSum);
			}
		}
		seriesData.push(obj);
	}
}

function loadEcharts(title,echartsId){
	var myChart = echarts.init(document.getElementById(echartsId));

    // 指定图表的配置项和数据
	var option = {
		    title: {
		        text: title
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		        data: names,
		        x: 'right',
		        orient: 'vertical',
		        width: 250,
		    	top: 50
		    },
		    grid: {
		        left: '3%',
		        right: '14%',
		        bottom: '3%',
		        containLabel: true
		    },
		    toolbox: {
		        feature: {
		            saveAsImage: {}
		        }
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: itemDate
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: seriesData
		};

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

//加载表格
function loadTable(){
	canvasTable(itemDate)
}

function canvasTable(data){
	  var tbody = "<tbody>";
	  var tempArr = [];
	    for(var i=0;i<data.length;i++){
	    	var context = "<tr><td>" + data[i] + "</td>";
	    	var count = 0;
	    	for(var h=0;h<seriesData.length;h++){
	    		
	    		if(seriesData[h].data[i] != null){
	    			count += seriesData[h].data[i];
	    		}
	    		context += "<td>" + seriesData[h].data[i] + "</td>";
	    	}
	    	
	    	tbody += context + "<td style='background: #f0f1b3;'>" + count + "</td></tr>";
	    }
	    
	    var footerTable = '</tbody></table>';
	   $("#testerDayTable").html(getHeaderTable() + tbody + footerTable);

}

function getHeaderTable(){
	var thead = "<table class='table table-bordered' style='margin: 20px auto'><thead style='background-color:#dce9eb'><tr>";
	  thead +="<th>日期/测试人员</th>";
	  
	  for(var j=0;j<names.length;j++){
		  thead +="<th>" + names[j] + "</th>";
	  }
	  thead +="<th>合计</th></tr></thead>";
	  return thead;
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
	loadTesterDayCommitData(itemId,startDate,endDate);
	loadTesterDayCommitDataClose(itemId,startDate,endDate);
});

//重置输入框
document.getElementById("resetInp").addEventListener('click',function(){
	$('#startDate').datebox('setValue', '');	
	$('#endDate').datebox('setValue', '');	
});

//构造echarts图表数据
function constrEchartsDataForClosed(object){
	
	getNameAndDateForClosed(object.concat());
	getSeriesDataForClosed(object.concat());
}


//获取echarts的类别、日期
var namesForClosed = [];
var itemDateForClosed = [];
function getNameAndDateForClosed(sourceData){
	namesForClosed = [];
	itemDateForClosed = [];
	for(var i=0;i<sourceData.length;i++){
		if(namesForClosed.indexOf(sourceData[i][2])==-1){
			if(sourceData[i][2] == null){
				sourceData[i][2] = "-";
			}
			namesForClosed.push(sourceData[i][2]);
		}
		if(itemDateForClosed.indexOf(sourceData[i][1])==-1){
			if(sourceData[i][1] == null){
				sourceData[i][1] = "-";
			}
			itemDateForClosed.push(sourceData[i][1]);
		}
	}
}

//获取echarts的seriesData数据
var seriesDataForClosed = [];
function getSeriesDataForClosed(sourDataForClosed){
	seriesDataForClosed = [];
	for(var i=0;i<namesForClosed.length;i++){
		var obj = {
			      name:namesForClosed[i],
		          type:'line',
		          data:[]		
		}
		for(var j=0;j<itemDateForClosed.length;j++){
			//同一个用户、同一时间的bug总数
			var bugSum = 0; 
			//标识是否有相等值
			var flagValue = false;
			for(var h=0;h<sourDataForClosed.length;h++){
				if(namesForClosed[i]==sourDataForClosed[h][2] && itemDateForClosed[j]==sourDataForClosed[h][1]){
					bugSum +=sourDataForClosed[h][0];
					flagValue = true;
				}
			}
			//false:没有;则该位置数据为0
			if(flagValue != true){
				obj.data.push(0);
			}else{
				obj.data.push(bugSum);
			}
		}
		seriesDataForClosed.push(obj);
	}
}

function loadEchartsForClosed(title,echartsId){
	var myChartForClosed = echarts.init(document.getElementById(echartsId));

    // 指定图表的配置项和数据
	var option = {
		    title: {
		        text: title
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		        data: namesForClosed,
		        x: 'right',
		        orient: 'vertical',
		        width: 250,
		    	top: 50
		    },
		    grid: {
		        left: '3%',
		        right: '14%',
		        bottom: '3%',
		        containLabel: true
		    },
		    toolbox: {
		        feature: {
		            saveAsImage: {}
		        }
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: itemDateForClosed
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: seriesDataForClosed
		};

    // 使用刚指定的配置项和数据显示图表。
    myChartForClosed.setOption(option);
}


//加载表格
function loadTableForClosed(data){
	  var thead = "";
	  thead +="<table class='table table-bordered'><thead style='background-color:#dce9eb'><tr><th>日期/测试人员</th>";
	  
	  for(var j=0;j<namesForClosed.length;j++){
		  thead +="<th>" + namesForClosed[j] + "</th>";
	  }
	  thead +="<th>合计</th></tr></thead>";
	  
	  var tbody = "<tbody>";
	    for(var i=0;i<itemDateForClosed.length;i++){
	    	var context = "<tr><td>" + itemDateForClosed[i] + "</td>";
	    	var count = 0;
	    	for(var h=0;h<seriesDataForClosed.length;h++){
	    		
	    		if(seriesDataForClosed[h].data[i] != null){
	    			count += seriesDataForClosed[h].data[i];
	    		}
	    		context += "<td>" + seriesDataForClosed[h].data[i] + "</td>";
	    	}
	    	
	    	tbody += context + "<td style='background: #f0f1b3;'>" + count + "</td></tr>";
	    }
	    
	    var footerTable = '</tbody></table>';
	    $("#testerDayForClosedTable").html(thead + tbody + footerTable);
}
//@ sourceURL=testerDayCommitTrend.js
