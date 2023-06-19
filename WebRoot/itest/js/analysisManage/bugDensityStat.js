
$(function(){
	$.parser.parse();
	//待处理BUG按天龄期分析
	var itemId="";
		itemId = $("#analyitemId").val(); 
	getBugDensityStat(itemId);
	 
	getBugDensityStatType(itemId);
	
	getBugDensityStatBugType(itemId);

});

//待处理BUG按天龄期分析
function getBugDensityStat(itemId){
	var url = baseUrl + "/analysis/analysisAction!getBugDensityStat.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){
			  if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data,'1');
				   loadEcharts('一级测试需求bug分布','bugDensityStat01Echarts');
				   constrEchartsData(data,'2');
				   loadEcharts('一级测试需求bug密度','bugDensityStat02Echarts');
				   loadTable(data,'bugDensityStatThead','bugDensityStatTbody','0'); 
			   }
			},'json');
}

//待处理BUG按天绝对龄期分析
function getBugDensityStatType(itemId){
	var url = baseUrl + "/analysis/analysisAction!getBugDensityStatType.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){
			 if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data,'3');
				   loadEcharts('一级测试需求bug等级分布','bugDensityStat03Echarts'); 
				   loadTable(data,'bugDensityStatThead01','bugDensityStatTbody01','1'); 
			   }
			},'json');
}
function getBugDensityStatBugType(itemId){
	var url = baseUrl + "/analysis/analysisAction!getBugDensityStatBugType.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){
			 if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data,'3');
				   loadEcharts('一级测试需求bug类型分布','bugDensityStat04Echarts'); 
				   loadTable(data,'bugDensityStatThead02','bugDensityStatTbody02','1'); 
			   }
			},'json');
}
//构造echarts图表数据--两种数据
function constrEchartsData(object,flag){
    getNameAndDate(object.concat(),flag);
	getSeriesData(object.concat(),flag);
}


//获取echarts的类别、日期
var legendData = [];
var functionName = [];
var bugCount = [];
var bugDensity = [];
var klocNum = [];
var bugType = [];
function getNameAndDate(sourceData,flag){
	functionName = [];
	bugCount = [];
	bugDensity = [];
	klocNum = [];
	bugType = [];
	var bugDen = 0;
	if(flag=='3'){
		for(i=0;i<sourceData.length;i++){
			if(bugType.indexOf(sourceData[i][1]) == -1 ){
				bugType.push(sourceData[i][1]);
			}
			if(functionName.indexOf(sourceData[i][0]) == -1 ){
				functionName.push(sourceData[i][0]);
			}
		}
		legendData = bugType;
	}else{
		legendData = ['bug数']
		for(i=0;i<sourceData.length;i++){
			functionName.push(sourceData[i][0]);
			bugCount.push(sourceData[i][2]);
			klocNum.push(sourceData[i][1])
			if(sourceData[i][1]=='0'){
			   bugDen = 0;
			}else{
	           bugDen = (sourceData[i][2]/sourceData[i][1]).toFixed(2);	
			}
			bugDensity.push(bugDen);
		}
	}
 
}

//获取echarts的seriesData数据
var seriesData = []; 
function getSeriesData(data,flag){
	seriesData =[]; 
	if(flag=='1'){
		var kkk = {
		name: 'bug个数',
		type: 'bar', 
		};
		var lkl = [];
	for(i=0;i<functionName.length;i++){
		for(j=0;j<data.length;j++){
			if(data[j][0]==functionName[i]){
				lkl.push(data[j][2])
			}
		}
	}
	kkk["data"] = lkl;
	seriesData.push(kkk);
	}else if(flag=='2'){
		var kkk = {
				name: 'bug密度',
				type: 'bar', 
				
				}; 
			 
			kkk["data"] = bugDensity;
			seriesData.push(kkk);
	}else if(flag=='3'){
		for(var a=0;a<bugType.length;a++){
			var kkk = {
					name: bugType[a],
					type: 'bar', 
			};
			var lkl = [];
			for(var b=0;b<functionName.length;b++){
				var hjk = 1;
				for(var c=0;c<data.length;c++){
					if(bugType[a]==data[c][1]&&functionName[b]==data[c][0]){
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
			kkk["stack"] = 'bug'
			
			seriesData.push(kkk);
		} 
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
		        data: legendData,
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
		        data: functionName,
                interval: 0,
                formatter:function(value)
                {
                    return value.split("").join("\n");
                }
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: seriesData
		};

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

var bugPercent = [];
//加载表格
function loadTable(data,theadId,tbodyId,flag){
	var tbody = "";
	if(flag=='0'){
		bugPercent = []; 
		  
		  var sum = 0;
		  for(var i=0;i<bugCount.length;i++){
			 sum += bugCount[i]
		  }
		  var bugPercentSingle = 0;
		  for(var i=0;i<bugCount.length;i++){
			  bugPercentSingle = ((bugCount[i]/sum)*100).toFixed(2)+"%";
			  bugPercent.push(bugPercentSingle)
		  }
		  for(var i=0;i<functionName.length;i++){
			  tbody += "<tr><td>"+functionName[i]+"</td>" +
			  		"<td>"+bugCount[i]+"</td>" +
			  				"<td>"+klocNum[i]+"</td>" +
			  						"<td>"+bugDensity[i]+"</td>" +
			  								"<td>"+bugPercent[i]+"</td></tr>";
		  } 
		   document.getElementById(tbodyId).innerHTML = tbody;
	}else{
		var thead = "<tr><td>一级测试需求</td>";
		for(i=0;i<bugType.length;i++){
			thead += "<td>"+bugType[i]+"</td>";
		}
		thead += "<td>合计</td></tr>"
		document.getElementById(theadId).innerHTML = thead;
		var tbodyData = [];
		for(var i=0;i<functionName.length;i++){
			var sum = 0;
            var tbodyDataSingle = [];
			tbodyDataSingle.push(functionName[i]);
			for(j=0;j<bugType.length;j++){
				var flag01 = 1;
				for(k=0;k<data.length;k++){
					if(data[k][0]==functionName[i]&&data[k][1]==bugType[j]){
						tbodyDataSingle.push(data[k][2])
						flag01 = 1;
						sum += data[k][2]
                        break 
					}else{
						flag01 += 1;
					}
					
				}
				if(flag01!=1){
					tbodyDataSingle.push('-');
				}
				
			}
			tbodyDataSingle.push(sum);
			tbodyData.push(tbodyDataSingle);
		}
	
		for(i=0;i<tbodyData.length;i++){
			tbody +="<tr>"
			for(j=0;j<tbodyData[i].length;j++){
				if(j>=tbodyData[i].length-1){
					 tbody += "<td style='background: #f0f1b3;'>"+tbodyData[i][j]+"</td>";
				}else{
					 tbody += "<td>"+tbodyData[i][j]+"</td>";
				}
               
			}
			tbody += "</tr>"
		}
		
		document.getElementById(tbodyId).innerHTML = tbody;  
	}
	
}
//@ sourceURL=bugDensityStat.js
