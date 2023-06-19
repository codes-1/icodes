
$(function(){
	$.parser.parse();
	//加载ehcarts
	var itemId="";
	itemId = $("#analyitemId").val(); 
	var url = baseUrl + "/analysis/analysisAction!getBugStatusDistbuStat.action";
	$.post(
			url,
			{"analysisDto.taskId":itemId},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data);
				   loadEcharts('BUG状态分布统计','bugStatusStatEcharts');
				   loadTable(data);
			   }else{
				   $("#bugStatusDisbuStat").html("<div style='font-size: 25px;text-align: center;padding-top: 105px;'>暂无此项目报表数据</div>");
				   
			   }
			},'json');
});

//构造echarts图表数据
var names = [];
var seriesData = [];
function constrEchartsData(object){
	for(var i=0;i<object.length;i++){
		names.push(object[i][0]);
		seriesData.push(object[i][1]);
	}
}

function loadEcharts(title,echartsId){
	var myChart = echarts.init(document.getElementById(echartsId));

    // 指定图表的配置项和数据
       var	option = {
    		   title: {
   		        text: title,
   		        x:'center'
   		      },
   		      tooltip: {
   		        trigger: 'axis'
   		      },
    		   xAxis: {
    		        type: 'category',
    		        data: names
    		    },
    		    yAxis: {
    		        type: 'value'
    		    },
    		    series: [{
    		        data:seriesData,
    		        type: 'bar'
    		    }]
		};
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

//加载表格
function loadTable(data){
	  var tbody = "";
	  var count = 0;
	  for(var i=0;i<names.length;i++){
		  count += seriesData[i];
		tbody += "<tr><td>"+ names[i] + "</td>" +
		         "<td>" + seriesData[i] + "</td>" + 
		          "</tr>";
	  } 
	  
	  tbody +="<tr style='background: #f0f1b3;'><td>合计</td><td>" + count + "</td></tr>"
	  document.getElementById("bugStatusStatTbody").innerHTML = tbody;
}
//@ sourceURL=bugStatusDisbuStat.js
