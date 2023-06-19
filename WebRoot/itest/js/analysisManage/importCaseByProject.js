
$(function(){
	$.parser.parse();
	//加载ehcarts
	var itemId="";
	itemId = $("#analyitemId").val(); 
	
	//获取时间
	getBugDate(itemId);
	
});

function getBugDate(id){
	var url = baseUrl + "/singleTestTask/singleTestTaskAction!getBugDateLimit.action";
	$.post(
			url,
			{	"dto.singleTest.taskId":id
				},
			function(data){
				if(data.length>0){
					var dateArray = data.split("_");
					$("#startDate").datebox("setValue",dateArray[0]); 
					$("#endDate").datebox("setValue",dateArray[1]);
					loadImportCase(id,dateArray[0],dateArray[1]);
				}
				},'text');
}

function loadImportCase(itemId,startDate,endDate){
	var url = baseUrl + "/analysis/analysisAction!getImportCaseByProject.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId,
				"analysisDto.startDate":startDate,
				"analysisDto.endDate":endDate
				},
			function(data){
			   if(data.length>0){
				   $("#importCaseByProjectStat").css("display","block");
				   $("#importCaseByProjectMask").css("display","none");
				   //构造echarts数据
				   constrEchartsData(data);
				   loadEcharts('责任人引入原因分析','importCaseByProjectEcharts');
				   loadTable(data);
			   }else{
				   $("#importCaseByProjectStat").css("display","none");
				   $("#importCaseByProjectMask").css("display","block");
			   }
			},'json');
}

//构造echarts图表数据
function constrEchartsData(object){
	getNameAndDate(object.concat());
	getSeriesData(object.concat());
}


//获取echarts的类别、日期
var names = [];
var itemDate = [];
function getNameAndDate(sourceData){
	 names = [];
	 itemDate = [];
	for(var i=0;i<sourceData.length;i++){
		if(names.indexOf(sourceData[i][0]) == -1){
			names.push(sourceData[i][0]);
		}
		if(itemDate.indexOf(sourceData[i][1]) == -1){
			itemDate.push(sourceData[i][1]);
		}
	}
}

//获取echarts的seriesData数据
var seriesData = [];
function getSeriesData(sourData){
	seriesData = [];
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
				if(names[i]==sourData[h][0] && itemDate[j]==sourData[h][1]){
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
	/*  thead +="<td>已关闭合计</th>";*/
	  thead +="<td>合计</th>";
	 $("#importCaseByProjectThead").html(thead);
	  
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
	    	tbody += context + "<td style='background: #f0f1b3;'>" + count + "</td></tr>";
	    }
	    
	   $("#importCaseByProjectTbody").html(tbody);
}

//查看报表
document.getElementById("viewReport").addEventListener('click',function(){
	var itemId = "",startDate="",endDate="";
	itemId = $("#analyitemId").val(); 
	var startDate = $("#startDate").datebox('getValue'); 
	var endDate = $("#endDate").datebox('getValue'); 
	if(null == startDate || startDate == ""){
		$.xalert("请选择开始日期");
		return;
	}
	
	if(null == endDate || endDate == ""){
		$.xalert("请选择结束日期日期");
		return;
	}
	loadImportCase(itemId,startDate,endDate);
});

//重置输入框
document.getElementById("resetInp").addEventListener('click',function(){
	$('#startDate').datebox('setValue', '');	
	$('#endDate').datebox('setValue', '');	
});
//@ sourceURL=importCaseByProject.js

