var dataSubmit=[],dataOpen=[],dataWait=[],dataEdit=[],dataClose=[],dataDeal=[];

$(function(){
	$.parser.parse();
	var reptfixItemId="";
	reptfixItemId = $("#analyitemId").val(); 

	//获取时间
	getBugDate(reptfixItemId);
	
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
					loadReptFixCloseDayData(id,dateArray[0],dateArray[1]);
				}
				},'text');
}

function loadReptFixCloseDayData(itemId,startDate,endDate){
	var url = baseUrl + "/analysis/analysisAction!getReptFixCloseDayTrend.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId,
				"analysisDto.startDate":startDate,
				"analysisDto.endDate":endDate
				},
			function(data){
					if(data.length>0){
						 $("#reptFixCloseDayTrend").css("display","block");
						 $("#reptFixCloseDayMask").css("display","none");
						dealEchartsData(data);
						loadReptFixCloseTable(data);
					}else{
						 $("#reptFixCloseDayTrend").css("display","none");
						 $("#reptFixCloseDayMask").css("display","block");
					}
			  
			},'json');
}

//构造echarts数据
function dealEchartsData(obj){
	var dateArr = [];
	for(var i=0;i<obj.length;i++){
		var date = obj[i][4];
		
		dateArr.push(date);
		//提交BUG
		dataSubmit.push(getItemData(obj[i],0));
		
		//打开BUG
		dataOpen.push(getItemData(obj[i],1));
		
		//修改BUG
		dataEdit.push(getItemData(obj[i],2));
		
		//关闭BUG
		dataClose.push(getItemData(obj[i],3));
		
		//处理BUG
		dataDeal.push(getItemData(obj[i],5));
		
		//待处理BUG
		dataWait.push(getItemData(obj[i],6));
	}
	
	loadEcharts(dateArr);
}

//得到echarts每项数据
function getItemData(object,j){
	var tempArr ;
	if(object[j]==null){
		tempArr=0;
	}else{
		tempArr=object[j];
	}
	return tempArr;
}

//加载echarts图表
function loadEcharts(dateArr){
	var myChart = echarts.init(document.getElementById('repFixEcharts'));
	   // 指定图表的配置项和数据
	var option = {
		    title: {
		        text: '提交|打开|待处理|修改|关闭BUG趋势'
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		    	x: 'right', 
		        data:['提交BUG', '打开BUG', '待处理BUG','修改BUG','关闭BUG','处理BUG次数'],
		        orient: 'vertical',
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
		        boundaryGap: false,
		        data: dateArr
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		             {
		            	  name:'提交BUG',
				          type:'line',
				          data:dataSubmit
		             },
		             {
		            	  name:'打开BUG',
				          type:'line',
				          data:dataOpen
		             },
		             {
		            	  name:'待处理BUG',
				          type:'line',
				          data:dataWait
		             },
		             {
		            	  name:'修改BUG',
				          type:'line',
				          data:dataEdit
		             },
		             {
		            	  name:'关闭BUG',
				          type:'line',
				          data:dataClose
		             },
		             {
		            	  name:'处理BUG次数',
				          type:'line',
				          data:dataDeal
		             }
		             ]
		};

    // 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
}

//加载表格
function loadReptFixCloseTable(data){
	canvasTable(data);
}

function canvasTable(data){
	 var tbody = "";
	 var objArr =[];
	    for(var i=0;i<data.length;i++){
	    	if( data[i][0] == null){
	    		data[i][0] = "-";
	    	}
	    	if( data[i][1] == null){
	    		data[i][1] = "-";
	    	}
	    	if( data[i][2] == null){
	    		data[i][2] = "-";
	    	}
	    	if( data[i][3] == null){
	    		data[i][3] = "-";
	    	}
	    	if( data[i][4] == null){
	    		data[i][4] = "-";
	    	}
	    	if( data[i][5] == null){
	    		data[i][5] = "-";
	    	}
	    	if( data[i][6] == null){
	    		data[i][6] = "-";
	    	}
	    	
	    	tbody += "<tr><td>" + data[i][4]  + "</td>" +
	    	        "<td>" + data[i][0] + "</td>" + 
	    	        "<td>" + data[i][1] + "</td>" + 
	    	        "<td>" + data[i][6] + "</td>" + 
	    	        "<td>" + data[i][2] + "</td>" + 
	    	        "<td>" + data[i][3] + "</td>" + 
	    	        "<td>" + data[i][5] + "</td></tr>";    	
	    }
	    
	   
	    $("#repFixTbody").html(tbody);
	    
	
}

//查看报表
document.getElementById("viewReport").addEventListener('click',function(){
	var itemId = "",startDate="",endDate="";
	itemId = $("#analyitemId").val(); 
	startDate = $("#startDate").datebox('getValue'); 
	endDate = $("#endDate").datebox('getValue'); 
	if(null == startDate || startDate == ""){
		$.xalert("请选择开始日期");
		return;
	}
	
	if(null == endDate || endDate == ""){
		$.xalert("请选择结束日期日期");
		return;
	}
	loadReptFixCloseDayData(itemId,startDate,endDate);
});

//重置输入框
document.getElementById("resetInp").addEventListener('click',function(){
	$('#startDate').datebox('setValue', '');	
	$('#endDate').datebox('setValue', '');	
});


//@ sourceURL=reptFixCloseDayTrend.js
