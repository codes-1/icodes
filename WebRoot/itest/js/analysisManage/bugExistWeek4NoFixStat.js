
$(function(){
	$.parser.parse();
	//待处理BUG按周龄期分析
	var itemId="";
	itemId = $("#analyitemId").val(); 
	
	//待处理BUG按周龄期分析
	getBugExistDay4(itemId);
	//待处理BUG按天绝对龄期分析
	getBugExistDay4Absolute(itemId);

});

//待处理BUG按周龄期分析
function getBugExistDay4(itemId){
	var url = baseUrl + "/analysis/analysisAction!getBugExistWeek4NoFixStat.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data);
				   loadEcharts('待处理BUG按周龄期分析','bugExistWeek4Echarts');
				   loadTable(data,'bugExistWeek4Thead','bugExistWeek4Tbody');
			   }
			},'json');
}

//待处理BUG按天绝对龄期分析
function getBugExistDay4Absolute(itemId){
	var url = baseUrl + "/analysis/analysisAction!getBugExistWeek4NoFixStatAbsolute.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data);
				   loadEcharts('待处理BUG按天绝对龄期分析','bugExistWeekAbsoluteEcharts');
				   loadTable(data,'bugExistWeekAbsoluteThead','bugExistWeekAbsoluteTbody');
			   }
			},'json');
}

//构造echarts图表数据
function constrEchartsData(object){
	
	getNameAndDate(object.concat());
	getSeriesData(object.concat());
}


//获取echarts的类别、日期
var itemData = [];
var bugType = [];
function getNameAndDate(sourceData){
	itemData = [];
	bugType = [];
	for(var i=0;i<sourceData.length;i++){
		 if(bugType.indexOf(sourceData[i][1]) == -1 && sourceData[i][1] !== "80"){
			 bugType.push(sourceData[i][1]);
	  }
	  
	  if(itemData.indexOf(sourceData[i][0]) == -1){
		  itemData.push(sourceData[i][0]);
	  }
	}
}

//获取echarts的seriesData数据
var seriesData = [];
function getSeriesData(data){
	seriesData = [];
	for(var i=0;i<bugType.length;i++){
		var object = {
				name: bugType[i],
				type: 'bar', 
				stack:'bug',
				data:[]
		};
		for(var j=0;j<itemData.length;j++){
			var flagValue = false;
			for(var h=0;h<data.length;h++){
				if(bugType[i]==data[h][1]&&itemData[j]==data[h][0]){
					object.data.push(data[h][2]);
					flagValue = true;
					break;
				}
			}
			if(!flagValue){
				object.data.push(0);
			}
		}
		seriesData.push(object);
	}
}

function loadEcharts(title,echartsId){
	var myChart = echarts.init(document.getElementById(echartsId));

    // 指定图表的配置项和数据
	var option = {
		    title: {
		        text: title,
		        x:'center'
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		    	x: 'right',
		        data: bugType,
		        orient: 'vertical',
		    	width: 250,
		    	top: 50, 
		    	left:600
		    },
		    grid: {
		        left: '3%',
		        right: '15%',
		        bottom: '3%',
		        containLabel: true
		    },
		    toolbox: {
				show: true,
				feature: {
					mark: {
						show: false
					},
					dataView: {
						show: false,
						readOnly: false
					}, 
					restore: {
						show: true
					},
					saveAsImage: {
						show: false
					}
				},
				right:'20%'
			},
		    xAxis: {
		        type: 'category', 
		        data: itemData
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
function loadTable(data,theadId,tbodyId){
	  var thead = "";
	  thead +="<th></th>";
	  
	  for(var j=0;j<itemData.length;j++){
		  thead +="<th>" + itemData[j] + "</th>";
	  }
	  thead +="<td>合计</th>";
	  document.getElementById(theadId).innerHTML = thead;
	  
	  var tbody = "";
	    for(var i=0;i<bugType.length;i++){
	    	var count = 0
	    	var context = "<tr><td>" + bugType[i] + "</td>";
	    		for(var h=0;h<seriesData.length;h++){
		    		if(seriesData[h].name==bugType[i]){
		    			for(var t=0;t<seriesData[h].data.length;t++){
		    				if(seriesData[h].data[t] == null){
		    					context += "<td>-</td>";
		    				}else{
		    					context += "<td>" + seriesData[h].data[t] + "</td>";
		    					count += seriesData[h].data[t];
		    				}
		    			}
		    		
		    		}
		    	}
	    	
	    	tbody += context +  "<td  style='background: #f0f1b3;'>" + count + "</td></tr>";
	    }
	    
	    document.getElementById(tbodyId).innerHTML = tbody;
}
//@ sourceURL=bugExistWeek4NoFixStat.js
