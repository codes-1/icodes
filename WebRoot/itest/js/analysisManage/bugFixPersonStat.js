var bugtype = [];
var names = [];
var seriesData = [];
var seriesPieData = [];
var devFixBugType = [];
var devFixNames = [];
var devFixSeriesPieData = [];
var devFixSeriesData = [];

$(function(){
	$.parser.parse();
	//加载ehcarts
	var itemId="";
	itemId = $("#analyitemId").val(); 
	var url = baseUrl + "/analysis/analysisAction!getBugFixPersonStat.action";
	$.post(
			url,
			{	
				"analysisDto.taskId":itemId
			},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data);
				   loadEcharts('开发人员待改BUG','bugFixPersonStatEcharts');
				   loadTable(data);
				   loadPieEcharts();
			   }
			},'json');

	$.post(
			 baseUrl + "/analysis/analysisAction!getDevFixDataSet.action",
			{	
				"analysisDto.taskId":itemId
			},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrDevFixEchartsData(data);
				   loadDevFixEcharts('开发人员修改BUG分析','devFixDataSetEcharts');
				   loadDevFixTable(data);
				   loadDevFixPieEcharts();
			   }
			},'json');
});

//构造echarts图表数据
function constrEchartsData(object){
	
	//折线图数据
	getNameAndDate(object.concat());
	getSeriesData(object.concat());
	
	//饼状图数据
	getPieData(object);
}

//获取echarts的类别、日期

function getNameAndDate(sourceData){
	for(var i=0;i<sourceData.length;i++){
		if(sourceData[i][0]=="1"&&sourceData[i][1]=="80"){
			bugtype.push(sourceData[i][2]);
			sourceData.splice(i,1);
			i--;
		}else if(sourceData[i][0]=="1"&&sourceData[i][2]=="80"){
			names.push(sourceData[i][1]);
			sourceData.splice(i,1);
			i--;
		}
	}
}

//获取echarts的seriesData数据

function getSeriesData(sourData){
	for(var i=0;i<bugtype.length;i++){
		var obj = {
			      name:bugtype[i],
		          type:'bar',
		          data:[],
		          stack:'bug',
		          barGap: 0,
		}
		for(var j=0;j<names.length;j++){
			//标识是否有相等值
			var flagValue = false;
			
			for(var h=0;h<sourData.length;h++){
				if(bugtype[i]==sourData[h][2] && names[j]==sourData[h][1]){
					obj.data.push(sourData[h][0]);
					flagValue = true;
				}
			}
			//false:没有;则该位置数据为0
			if(flagValue != true){
				obj.data.push(0);
			}
		}
		seriesData.push(obj);
	}
}


function getPieData(sourData) {
	for(var i=0;i<names.length;i++){
		var obj = {
			      name:names[i]
		          }
		var count = 0;
		for(var j=0;j<bugtype.length;j++){
			for(var h=0;h<sourData.length;h++){
				if(bugtype[j]==sourData[h][2] && names[i]==sourData[h][1]){
					count += sourData[h][0];
				}
			}
		}
		obj.value = count;
		seriesPieData.push(obj);
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
		        data: bugtype,
		        x: 'right',
		        orient: 'vertical',
		        width: 250,
		    	top: 50
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
		        data: names
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: seriesData
		};

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

function loadPieEcharts(){
		var myChart = echarts.init(document.getElementById('bugFixPersonPieEcharts'));

	    // 指定图表的配置项和数据
	       var	option = {
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
			            data: seriesPieData,
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
	  var thead = "";
	  thead +="<th>等级/开发人员</th>";
	  
	  for(var j=0;j<names.length;j++){
		  thead +="<th>" + names[j] + "</th>";
	  }
	  thead +="<td>合计</th>";
	  document.getElementById("bugFixPersonStatThead").innerHTML = thead;
	  
	  var tbody = "";
	    for(var i=0;i<bugtype.length;i++){
	    	var context = "<tr><td>" + bugtype[i] + "</td>";
	    	var count = 0;
	    		for(var h=0;h<seriesData.length;h++){
		    		if(seriesData[h].name==bugtype[i]){
		    			for(var t=0;t<seriesData[h].data.length;t++){
		    				if(seriesData[h].data[t] == 0 || seriesData[h].data[t] == null){
		    					context += "<td>-</td>";
		    				}else{
		    					count +=seriesData[h].data[t];
		    					context += "<td>" + seriesData[h].data[t] + "</td>";
		    				}
		    			}
		    		
		    		}
		    	}
	    	
	    	tbody += context + "<td  style='background: #f0f1b3;'>" + count + "</td></tr>";
	    }
	    
	    document.getElementById("bugFixPersonStatTbody").innerHTML = tbody;
}


//构造echarts图表数据
function constrDevFixEchartsData(object){
	
	//折线图数据
	getDevFixNameAndDate(object.concat());
	getDevFixSeriesData(object.concat());
	
	//饼状图数据
	getDevFixPieData(object);
}

//获取echarts的类别、日期

function getDevFixNameAndDate(sourceData){
	for(var i=0;i<sourceData.length;i++){
		if(sourceData[i][0]=="1"&&sourceData[i][1]=="80"){
			devFixBugType.push(sourceData[i][2]);
		}else if(sourceData[i][0]=="1"&&sourceData[i][2]=="80"){
			devFixNames.push(sourceData[i][1]);
		}
	}
}

//获取echarts的seriesData数据

function getDevFixSeriesData(sourData){
	for(var i=0;i<devFixBugType.length;i++){
		var obj = {
			      name:devFixBugType[i],
		          type:'bar',
		          data:[],
		          stack:'bug',
		          barGap: 0,
		}
		for(var j=0;j<devFixNames.length;j++){
			//标识是否有相等值
			var flagValue = false;
			
			for(var h=0;h<sourData.length;h++){
				if(devFixBugType[i]==sourData[h][2] && devFixNames[j]==sourData[h][1]){
					obj.data.push(sourData[h][0]);
					flagValue = true;
				}
			}
			//false:没有;则该位置数据为0
			if(flagValue != true){
				obj.data.push(0);
			}
		}
		devFixSeriesData.push(obj);
	}
}


function getDevFixPieData(sourData) {
	for(var i=0;i<devFixNames.length;i++){
		var obj = {
			      name:devFixNames[i]
		          }
		var count = 0;
		for(var j=0;j<devFixBugType.length;j++){
			for(var h=0;h<sourData.length;h++){
				if(devFixBugType[j]==sourData[h][2] && devFixNames[i]==sourData[h][1]){
					count += sourData[h][0];
				}
			}
		}
		obj.value = count;
		devFixSeriesPieData.push(obj);
	}
}

function loadDevFixEcharts(title,echartsId){
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
		        data: devFixBugType,
		        x: 'right',
		        orient: 'vertical',
		        width: 250,
		    	top: 50
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
		        data: devFixNames
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: devFixSeriesData
		};

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

function loadDevFixPieEcharts(){
		var myChart = echarts.init(document.getElementById('devFixDataSetPieEcharts'));

	    // 指定图表的配置项和数据
	       var	option = {
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
			        data: devFixNames
			    },
			    series : [
			        {
			            name: '姓名',
			            type: 'pie',
			            radius : '55%',
			            center: ['40%', '50%'],
			            data: devFixSeriesPieData,
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
function loadDevFixTable(data){
	  var thead = "";
	  thead +="<th>等级/开发人员</th>";
	  
	  for(var j=0;j<devFixNames.length;j++){
		  thead +="<th>" + devFixNames[j] + "</th>";
	  }
	  thead +="<td>合计</th>";
	  document.getElementById("devFixDataSetThead").innerHTML = thead;
	  
	  var tbody = "";
	    for(var i=0;i<devFixBugType.length;i++){
	    	var context = "<tr><td>" + devFixBugType[i] + "</td>";
	    	var count = 0;
	    		for(var h=0;h<devFixSeriesData.length;h++){
		    		if(devFixSeriesData[h].name==devFixBugType[i]){
		    			for(var t=0;t<devFixSeriesData[h].data.length;t++){
		    				if(devFixSeriesData[h].data[t] == 0 || devFixSeriesData[h].data[t] == null){
		    					context += "<td>-</td>";
		    				}else{
		    					count +=devFixSeriesData[h].data[t];
		    					context += "<td>" + devFixSeriesData[h].data[t] + "</td>";
		    				}
		    			}
		    		
		    		}
		    	}
	    	
	    	tbody += context + "<td  style='background: #f0f1b3;'>" + count + "</td></tr>";
	    }
	    
	    document.getElementById("devFixDataSetTbody").innerHTML = tbody;
}
//@ sourceURL=bugFixPersonStat.js
