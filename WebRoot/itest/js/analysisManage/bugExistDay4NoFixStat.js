var itemData = [];
var bugtype = [];
//按天龄绝对龄期
var itemDataAbs = [];
var bugtypeAbs = [];
var seriesData = [];
//按天龄绝对龄期
var seriesDataAbs = [];



$(function(){
	$.parser.parse();
	//待处理BUG按天龄期分析
	var itemId="";
	itemId = $("#analyitemId").val(); 
	getBugExistDay4(itemId);
	
	//待处理BUG按天绝对龄期分析
	getBugExistDay4Absolute(itemId);

});

//待处理BUG按天龄期分析
function getBugExistDay4(itemId){
	var url = baseUrl + "/analysis/analysisAction!getBugExistDay4NoFixStat.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data,'bugExistDay');
				   loadEcharts('待处理BUG按天龄期分析','bugExistDay4Echarts','bugExistDay');
				   loadTable(data,'bugExistDay4Thead','bugExistDay4Tbody','bugExistDay');
			   }
			},'json');
}

//待处理BUG按天绝对龄期分析
function getBugExistDay4Absolute(itemId){
	var url = baseUrl + "/analysis/analysisAction!getBugExistDay4NoFixStatAbsolute.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){
			   if(data.length>0){
				   //构造echarts数据
				   constrEchartsData(data,'bugExistDayAbsolute');
				   loadEcharts('待处理BUG按天绝对龄期分析','bugExistDayAbsoluteEcharts','bugExistDayAbsolute');
				   loadTable(data,'bugExistDayAbsoluteThead','bugExistDayAbsoluteTbody','bugExistDayAbsolute');
			   }
			},'json');
}

//构造echarts图表数据
function constrEchartsData(object,reportNm){
	
	getNameAndDate(object.concat(),reportNm);
	getSeriesData(object.concat(),reportNm);
}


//获取echarts的类别、日期

function getNameAndDate(sourceData,reportNm){
	var bugtypTmp = [];
	var itemDataTmp = [];
	for(var i=0;i<sourceData.length;i++){
	  if(bugtypTmp.indexOf(sourceData[i][1]) == -1 && sourceData[i][1] !== "80"){
		  bugtypTmp.push(sourceData[i][1]);
	  }
	  
	  if(itemDataTmp.indexOf(sourceData[i][0]) == -1){
		  itemDataTmp.push(sourceData[i][0]);
	  }
	}
	if(reportNm === 'bugExistDayAbsolute'){
		 bugtypeAbs = bugtypTmp;
		 itemDataAbs = itemDataTmp;
	}else{
		itemData = itemDataTmp;
		bugtype =  bugtypTmp;
	}
}

//获取echarts的seriesData数据

function getSeriesData(sourData,reportNm){
	var seriesDataTmp = [];
	var itemDataTmp = [];
	var bugtypeTmp = [];
	if(reportNm === 'bugExistDayAbsolute'){
		seriesDataTmp = seriesDataAbs;
		bugtypeTmp = bugtypeAbs;
		itemDataTmp = itemDataAbs;
	}else{
		seriesDataTmp = seriesData ;
		bugtypeTmp = bugtype;
		itemDataTmp = itemData;
	}
	for(var i=0;i<bugtypeTmp.length;i++){
		var obj = {
			      name:bugtypeTmp[i],
		          type:'bar',
		          data:[]	,
		          stack:'bug'
		}
		for(var j=0;j<itemDataTmp.length;j++){
			//标识是否有相等值
			var flagValue = false;
			for(var h=0;h<sourData.length;h++){
				if(bugtype[i]==sourData[h][1] && itemDataTmp[j]==sourData[h][0]){
					obj.data.push(sourData[h][2]);
					flagValue = true;
				}
			}
			//false:没有;则该位置数据为0
			if(flagValue != true){
				obj.data.push(0);
			}
		}
		seriesDataTmp.push(obj);
	}
}

function loadEcharts(title,echartsId,reportNm){
	var myChart = echarts.init(document.getElementById(echartsId));
	var seriesDataTmp  = [];
	var bugtypeTmp = [];
	if(reportNm === 'bugExistDayAbsolute'){
		 seriesDataTmp = seriesDataAbs;
		 bugtypeTmp = bugtypeAbs;
	}else{
		 seriesDataTmp = seriesData;
		 bugtypeTmp = bugtype;
	}
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
		        data: bugtypeTmp,
		        x: 'right',
		        orient: 'vertical',
		        width: 250,
		    	top: 50
		    },
		    grid: {
		        left: '3%',
		        right: '17%',
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
		        boundaryGap: false,
		        data: itemData
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: seriesDataTmp
		};

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}


//加载表格
function loadTable(data,theadId,tbodyId,reportNm){
	var seriesDataTmp = [];
	var bugtypeTmp = [];
	var itemDataTmp = [];
	if(reportNm === 'bugExistDayAbsolute'){
		seriesDataTmp = seriesDataAbs;
		bugtypeTmp = bugtypeAbs;
		itemDataTmp = itemDataAbs;
	}else{
		seriesDataTmp = seriesData ;
		bugtypeTmp = bugtype;
		itemDataTmp = itemData;
	}
	  var thead = "";
	  thead +="<th></th>";
	  
	  for(var j=0;j<itemDataTmp.length;j++){
		  thead +="<th>" + itemDataTmp[j] + "</th>";
	  }
	  thead +="<td>合计</th>";
	  document.getElementById(theadId).innerHTML = thead;
	  
	  var tbody = "";
	    for(var i=0;i<bugtypeTmp.length;i++){
	    	var count = 0
	    	var context = "<tr><td>" + bugtypeTmp[i] + "</td>";
	    		for(var h=0;h<seriesDataTmp.length;h++){
		    		if(seriesDataTmp[h].name==bugtypeTmp[i]){
		    			for(var t=0;t<seriesDataTmp[h].data.length;t++){
		    				if(seriesDataTmp[h].data[t] == null){
		    					context += "<td>-</td>";
		    				}else{
		    					context += "<td>" + seriesDataTmp[h].data[t] + "</td>";
		    					count += seriesDataTmp[h].data[t];
		    				}
		    			}
		    		
		    		}
		    	}
	    	
	    	tbody += context +  "<td  style='background: #f0f1b3;'>" + count + "</td></tr>";
	    }
	    
	    document.getElementById(tbodyId).innerHTML = tbody;
}
//@ sourceURL=bugExistDay4NoFixStat.js
