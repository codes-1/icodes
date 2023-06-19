$(function(){
	var moduleIds =  $("#moduleIds").val();
	loadBugModuleRepForNum(moduleIds);
	loadBugModuleRepForLevel(moduleIds);
	loadBugModuleRepForType(moduleIds);

});


//BUG测试需求分布--BUG数
function loadBugModuleRepForNum(moduleIds){
	//加载ehcarts
	var itemId="";
	itemId = $("#analyitemId").val(); 
	var url = baseUrl + "/analysis/analysisAction!getBugModuleDistbuForNum.action";
	$.post(
			url,
			{"analysisDto.taskId":itemId,
			 "analysisDto.moduleIds":moduleIds
			},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsDataForNum(data);
				   //加载echarts图表
				   loadEcharts('BUG测试需求分布','bugModuleNumEcharts',namesForNum,SeriesDataForN,'BUGNum');
				   //加载table表格
				   loadTable(namesForNum,SeriesDataForN,'bugModuleNumTbody');
			   }
			},'json');
}

//BUG测试需求分布--BUG级别
function loadBugModuleRepForLevel(moduleIds){
	//加载ehcarts
	var itemId="";
	itemId = $("#analyitemId").val(); 
	var url = baseUrl + "/analysis/analysisAction!getBugModuleDistbuForLevel.action";
	$.post(
			url,
			{"analysisDto.taskId":itemId,
			"analysisDto.moduleIds":moduleIds
			},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsDataForLevel(data);
				   //加载echarts图表
				   loadEcharts('测试需求BUG等级分布','bugModuleEchartsForLevel',namesForLevel,SeriesDataForL,typeForLevel);
				   //加载table表格
				   loadTableForLevel(namesForLevel,SeriesDataForL,'bugModuleTableForLevel',typeForLevel);
			   }
			},'json');
}

//BUG测试需求分布--BUG类型
function loadBugModuleRepForType(moduleIds){
	//加载ehcarts
	var itemId="";
	itemId = $("#analyitemId").val(); 
	var url = baseUrl + "/analysis/analysisAction!getBugModuleDistbuForType.action";
	$.post(
			url,
			{"analysisDto.taskId":itemId,
			"analysisDto.moduleIds":moduleIds
			},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsDataForType(data);
				   //加载echarts图表
				   loadEcharts('测试需求BUG类型分布','bugModuleEchartsForType',namesForType,SeriesDataForT,typeForType);
				   //加载table表格
				   loadTableForLevel(namesForType,SeriesDataForT,'bugModuleTableForType',typeForType);
			   }
			},'json');
}

//构造echarts图表数据--BUG数
var namesForNum = [];
var SeriesDataForN = [];
function constrEchartsDataForNum(object){
	
	namesForNum = getName(object,0);
	SeriesDataForN = getSeriesDataForLine(object,namesForNum);
}

//构造echarts图表数据--BUG等级
var namesForLevel = []; //echarts x轴name
var typeForLevel = [];  //echarts 类目name
var SeriesDataForL = [];
function constrEchartsDataForLevel(object){
	
	namesForLevel = getName(object,0);
	typeForLevel = getName(object,1);
	SeriesDataForL = getSeriesDataForBar(object,namesForLevel,typeForLevel);
}

//构造echarts图表数据--BUG类型
var namesForType = []; //echarts x轴name
var typeForType = [];  //echarts 类目name
var SeriesDataForT = [];
function constrEchartsDataForType(object){
	
	namesForType = getName(object,0);
	typeForType = getName(object,1);
	SeriesDataForT = getSeriesDataForBar(object,namesForType,typeForType);
}

//获取echarts类目--BUG数、BUG类型、BUG等级
function getName(sourceData,index){
	var names = [];
	for(var i=0;i<sourceData.length;i++){
		if(names.indexOf(sourceData[i][index]) == -1){
			names.push(sourceData[i][index]);
		}
	}
	return names;
}

//获取echarts的seriesData数据--BUG数
function getSeriesDataForLine(sourData,names){
	var seriesData=[];
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
	return seriesData;
}

//获取echarts的seriesData数据--BUG等级、BUG类型
function getSeriesDataForBar(sourData,typeName,names){
	var seriesData=[];
	for(var i=0;i<names.length;i++){
		var obj = {
			      name:names[i],
			      stack: 'bug',
			      type: 'bar',
			      data:[]
		}
		for(var h=0; h<typeName.length; h++){
			var flagValue = false;
			for(var j=0;j<sourData.length;j++){
				if(sourData[j][1] == names[i] && sourData[j][0] == typeName[h]){
					obj.data.push(sourData[j][2]);
					flagValue = true;
					break;
				}
			}
			if(!flagValue){
				obj.data.push(0);
			}
		}
		seriesData.push(obj);
	}
	return seriesData;
}

function loadEcharts(title,echartsId,names,seriesData,legendName){
	var myChart = echarts.init(document.getElementById(echartsId));
    var option = setEchartsOption(title,names,seriesData,legendName);
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

// 指定图表的配置项和数据
function setEchartsOption(title,names,seriesData,typeName){
	var option = {};
	if(typeName === 'BUGNum'){
		//BUG数
		  option = {
		  		   title: {
		 		        text: title,
		 		        x:'center'
		 		      },
		 		   tooltip: {
		 		        trigger: 'axis'
		 		      },
		 		   grid: {
		 			  bottom:'35%' 
		 		   },
		  		   xAxis: {
		  		        type: 'category',
		  		        data: names,
		  		        axisLabel: {
		                  interval:0,
		                  rotate:-40
		                }
		  		    },
		  		    yAxis: {
		  		        type: 'value'
		  		    },
		  		    series: [{
		  		        data:seriesData,
		  		        type: 'bar'
		  		    }]
				};
	}else{
		//BUG等级、BUG类型
		  option = {
		  		   title: {
		 		        text: title,
		 		        x:'center'
		 		      },
		 		   legend: {
			 		        data:typeName,
			 		        x: 'right',
					        orient: 'vertical',
					        width: 250,
					    	top: 50
			 		},
		 		   tooltip: {
		 		        trigger: 'axis'
		 		      },
		 		   grid: {
		 			  bottom:'35%' 
		 		   },
		  		   xAxis: {
		  		        type: 'category',
		  		        data: names,
		  		        axisLabel: {
		                  interval:0,
		                  rotate:-40
		                }
		  		    },
		  		    yAxis: {
		  		        type: 'value'
		  		    },
		  		    series: seriesData
				};
	}
	
	return option;
}

//加载表格
function loadTable(names,seriesData,tableId){
	  var tbody = "";
	  for(var i=0;i<names.length;i++){
		tbody += "<tr><td>"+ names[i] + "</td>" +
		         "<td>" + seriesData[i].value + "</td>" + 
		          "</tr>";
	  } 
	  
	  document.getElementById(tableId).innerHTML = tbody;
}

//加载表格
function loadTableForLevel(names,seriesData,tableId,typeName){
	  var theader = "";
	  var tbody = "";
	  theader = "<thead style='text-align:center;background-color:#dce9eb'><tr> <th>测试需求项名称</th>";
	  for(var n=0;n<typeName.length;n++){
		  theader += " <th>" + typeName[n] + "</th> " ;
	  }
	  theader +="</tr></thead>";
	  
	  for(var i=0;i<names.length;i++){
		tbody += "<tr><td>"+ names[i] + "</td>";
		for(var j=0;j<typeName.length;j++){
			for(var h=0;h<seriesData.length;h++){
				if(typeName[j] == seriesData[h].name){
					tbody += "<td>" + seriesData[h].data[i] + "</td>";
					break;
				}
			}
		}
		tbody += "</tr>";
	  } 
	  
	  document.getElementById(tableId).innerHTML = theader + "<tbody>" + tbody + "</tbody>";
}
//@ sourceURL=bugModuleDistbuStat.js
