var bugLevel = [];
var itemDate = [];
var seriesData = [];


function getParamString(name) { 
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
    var r = window.location.search.substr(1).match(reg); 
    if (r != null) return unescape(r[2]); 
    return null; 
}  
$(function(){
	$.parser.parse();
	var itemId = "";
		itemId = $("#analyitemId").val(); 
	//加载ehcarts
	var url = baseUrl + "/analysis/analysisAction!getBugExistWeekStat.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
				function(data){
					   if(data.length>0){
						   //构造echarts数据
						   constrEchartsData(data);
						   loadEcharts('已关闭bug按周龄期统计','bugExitStatEcharts');
						   loadTable(data);
					   }
					},'json');
}); 
//构造echarts图表数据
function constrEchartsData(object){
	
	getNameAndDate(object);
	getSeriesData(object);
}


//获取echarts的类别、日期

function getNameAndDate(sourceData){
	for(var i=0;i<sourceData.length;i++){
		if(bugLevel.indexOf(sourceData[i][1]) == -1&&sourceData[i][1] != "80"){
			bugLevel.push(sourceData[i][1]);
		}
		if(itemDate.indexOf(sourceData[i][0]) == -1){
			itemDate.push(sourceData[i][0]);
		}
	}
}

//获取echarts的seriesData数据

function getSeriesData(data){
	
	for(var a=0;a<bugLevel.length;a++){
		var kkk = {
				name: bugLevel[a],
				type: 'bar', 
				stack:'bug'
		};
		var lkl = [];
		for(var b=0;b<itemDate.length;b++){
			var hjk = 1;
			for(var c=0;c<data.length;c++){
				if(bugLevel[a]==data[c][1]&&itemDate[b]==data[c][0]){
					lkl.push(data[c][2]);
					hjk = 1;
					break;
				}else{
					hjk = hjk + 1;
				}
			}
			if(hjk != 1){
				lkl.push(0);
			}
		}
		kkk["data"] = lkl;
		seriesData.push(kkk);
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
		    	x: 'right',
		        data: bugLevel,
		        orient: 'vertical',
		    	width: 250,
		    	top: 50, 
		    	left:600
		    },
		    grid: {
		        left: '3%',
		        right: '20%',
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
					} ,
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
	  thead +="<td>合计</th>";
	  document.getElementById("bugExitStatThead").innerHTML = thead;
	  
	  var tbody = "";
	    for(var i=0;i<bugLevel.length;i++){
	    	var context = "<tr><td>" + bugLevel[i] + "</td>";
	    	var count = 0;
	    	for(var h=0;h<seriesData.length;h++){
	    		if(seriesData[h].name == bugLevel[i]){
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
	    	tbody += context + "<td style='background: #f0f1b3;'>" + count + "</td></tr>";
	    }
	    
	    document.getElementById("bugExitStatTbody").innerHTML = tbody;
}
//@ sourceURL=bugExistWeekStat.js
