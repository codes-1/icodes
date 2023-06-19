var dataSubmit = [];
var dataCount = [];
$(function(){
	$.parser.parse();
	var itemId="";
	itemId = $("#analyitemId").val(); 
	//加载ehcarts
	var url = baseUrl + "/analysis/analysisAction!getCommitExistBugBuildStat.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){
					if(data.length>0){
						dealEchartsData(data);
						loadCommitExistTable(data);
					}else{
					   $("#commitExistBugBuildStat").html("<div style='font-size: 25px;text-align: center;padding-top: 105px;'>暂无此项目报表数据</div>");
					}
			},'json');
});
//构造echarts数据
function dealEchartsData(obj){
	var dateArr = [];
	for(var i=0;i<obj.length;i++){
		var date = obj[i][0];
		dateArr.push(date);
		//提交BUG
		dataSubmit.push(obj[i][1]);
		//BUG总数
		dataCount.push(obj[i][4]);
	}
	
	loadEcharts(dateArr);
}

//加载echarts图表
function loadEcharts(dateArr){
	var myChart = echarts.init(document.getElementById('commitExistEcharts'));
	   // 指定图表的配置项和数据
	var option = {
		    title: {
		        text: '版本间提交及BUG总数趋势'
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['提交BUG', 'BUG总数'],
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
		        data: dateArr
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		             {
		            	  name:'提交BUG',
				          type:'bar',
				          barGap: 0,
				          data:dataSubmit
		             },
		             {
		            	  name:'BUG总数',
				          type:'bar',
				          data:dataCount
		             }
		             ]
		};

    // 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
}

//加载表格
function loadCommitExistTable(data){
    var tbody = "";
    for(var i=0;i<data.length;i++){
    	tbody += "<tr><td>" + data[i][0]  + "</td>" +
    	        "<td>" + data[i][1] + "</td>" + 
    	        "<td>" + data[i][4] + "</td></tr>";
    }
    
    $("#commitExistBody").html(tbody );
}

//@ sourceURL=commitExistBugBuildStat.js
