var names = [];
var itemDate = [];
var seriesData = [];

$(function(){
	$.parser.parse();
	//加载ehcarts
	var itemId="";
	itemId = $("#analyitemId").val(); 
	var url = baseUrl + "/analysis/analysisAction!getBugExistDayStat.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data);
				   loadEcharts('已关闭BUG按天龄期统计','testerDayEcharts');
				   loadTable(data);
			   }else{
				   $("#bugExistDayStat").html("<div style='font-size: 25px;text-align: center;padding-top: 105px;'>暂无此项目报表数据</div>");
			   }
			},'json');
});

//构造echarts图表数据
function constrEchartsData(object){
	
	getNameAndDate(object.concat());
	getSeriesData(object.concat());
}


//获取echarts的类别、日期

function getNameAndDate(sourceData){
	for(var i=0;i<sourceData.length;i++){
		if(names.indexOf(sourceData[i][1]) == -1&&sourceData[i][1] != "80"){
			names.push(sourceData[i][1]);
		}
		if(itemDate.indexOf(sourceData[i][0]) == -1){
			itemDate.push(sourceData[i][0]);
		}
	}
}

//获取echarts的seriesData数据

function getSeriesData(sourData){
	for(var i=0;i<names.length;i++){
		var obj = {
			      name:names[i],
		          type:'bar',
		          data:[],
		          stack:'bug',
		          barGap: 0
		}
		for(var j=0;j<itemDate.length;j++){
			//标识是否有相等值
			var flagValue = false;
			for(var h=0;h<sourData.length;h++){
				if(names[i]==sourData[h][1] && itemDate[j]==sourData[h][0]){
					obj.data.push(sourData[h][2]);
					flagValue = true;
				}
			}
			if(!flagValue){
				obj.data.push(0);
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
		        text: title,
		        x:'center'
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
				show: true,
				feature: {
					mark: {
						show: false
					},
					dataView: {
						show: false,
						readOnly: false
					},
					magicType: {
						show: true,
						type: ['line', 'bar']
					},
					restore: {
						show: true
					},
					saveAsImage: {
						show: false
					}
				}
			},
		    xAxis: {
		        type: 'category',
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
function loadTable(data){
	  var thead = "";
	  thead +="<th>BUG等级</th>";
	  
	  for(var j=0;j<itemDate.length;j++){
		  thead +="<th>" + itemDate[j] + "</th>";
	  }
	  thead +="<td>已关闭合计</th>";
	  document.getElementById("testerDayThead").innerHTML = thead;
	  
	  var tbody = "";
	    for(var i=0;i<names.length;i++){
	    	var context = "<tr><td>" + names[i] + "</td>";
	    	var count = 0;
	    	for(var h=0;h<seriesData.length;h++){
	    		if(seriesData[h].name == names[i]){
	    			for(var n=0;n<seriesData[h].data.length;n++){
	    				if(seriesData[h].data[n]==null){
	    					context += "<td>-</td>";
	    				}else{
	    					context += "<td>" + seriesData[h].data[n] + "</td>";
	    					count += seriesData[h].data[n];
	    				}
	    			}
	    		}
	    	}
	    	tbody += context + "<td>" + count + "</td></tr>";
	    }
	    
	    document.getElementById("testerDayTbody").innerHTML = tbody;
}
//@ sourceURL=bugExistDayStat.js
