
$(function(){
	$.parser.parse();
	//加载ehcarts
	var itemId="";
	itemId = $("#analyitemId").val(); 
	var url = baseUrl + "/analysis/analysisAction!getTesterBugQuality.action";
	$.post(
			url,
			{"analysisDto.taskId":itemId},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data);
				   loadEcharts('测试人员BUG质量分析','testerBugQualityEcharts');
				   loadTable(data);
			   }
			},'json');
});

//构造echarts图表数据
function constrEchartsData(object){
	getType(object.concat());
	getSeriesData(object.concat());
}

//获取echarts的类别、日期
var names = [];
function getType(sourceData){
	for(var i=0;i<sourceData.length;i++){
		if(names.indexOf(sourceData[i][0]) == -1){
			names.push(sourceData[i][0]);
		}
	}
}

//获取echarts的seriesData数据
var seriesData = [];
function getSeriesData(sourData){
	for(var i=0;i<names.length;i++){
		var obj = {
			      name:names[i]
		}
		for(var j=0;j<sourData.length;j++){
			if(sourData[j][0] == names[i]){
				obj.value = sourData[j][1];
				break;
			}
		}
		seriesData.push(obj);
	}
}

function loadEcharts(title,echartsId){
	var myChart = echarts.init(document.getElementById(echartsId));

    // 指定图表的配置项和数据
       var	option = {
		    title : {
		        text: title,
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        type: 'scroll',
		        orient: 'vertical',
		        right: 10,
		        top: 20,
		        bottom: 20,
		        data: names
		    },
		    series : [
		        {
		            name: '姓名',
		            type: 'pie',
		            radius : '55%',
		            center: ['40%', '50%'],
		            data: seriesData,
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            }
		        }
		    ]
		};
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

//加载表格
function loadTable(data){
	  var tbody = "";
	  var columnOne = 0;
	  var columnTwo = 0;
	  for(var i=0;i<seriesData.length;i++){
		  for(var j=0;j<data.length;j++){
			  if(seriesData[i].name == data[j][0]){
				  tbody += "<tr><td>"+ data[j][0] + "</td>" +
		                    "<td>" + data[j][1] + "</td>" + 
		                    "<td>" + data[j][2] + "</td>" + 
		                    "<td> 0.00 </td>"
		                    "</tr>";
				  columnOne += data[j][1];
				  columnTwo += data[j][2];
			  }
		  }
		  
	  } 
	  tbody +="<tr style='background: #f0f1b3;'><td>合计</td><td>" + columnOne + "</td>" +
	          "<td>" + columnTwo + "</td><td>0.00</td></tr>";
	  document.getElementById("testerBugQualityTbody").innerHTML = tbody;
}
//@ sourceURL=bugImpPhaseStat.js
