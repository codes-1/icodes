
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
					loadChargeOwner(id,dateArray[0],dateArray[1]);
				}
				},'text');
}

function loadChargeOwner(itemId,startDate,endDate){

	var url = baseUrl + "/analysis/analysisAction!getChargeOwner.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId,
				"analysisDto.startDate":startDate,
				"analysisDto.endDate":endDate
				},
			function(data){
			   if(data.length>0){
				   $("#chargeOwnerDiv").css("display","block");
				   $("#chargeOwnerMask").css("display","none");
				   //构造echarts数据
				   constrEchartsData(data);
				   loadEcharts('','chargeOwnerEcharts');
				   loadTable();
			   }else{
				   $("#chargeOwnerDiv").css("display","none");
				   $("#chargeOwnerMask").css("display","block");
			   }
			},'json');
}
//构造echarts图表数据
function constrEchartsData(object){
	getType(object.concat());
	getSeriesData(object.concat());
}

//获取echarts的类别、日期
var bugtype = [];
function getType(sourceData){
	bugtype = [];
	for(var i=0;i<sourceData.length;i++){
		if(bugtype.indexOf(sourceData[i][0]) == -1){
			bugtype.push(sourceData[i][0]);
		}
	}
}

//获取echarts的seriesData数据
var seriesData = [];
function getSeriesData(sourData){
	seriesData = [];
	for(var i=0;i<bugtype.length;i++){
		var obj = {
			      name:bugtype[i]
		}
		for(var j=0;j<sourData.length;j++){
			if(sourData[j][0] == bugtype[i]){
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
		        text: '责任人分析',
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
		        data: bugtype
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
function loadTable(){
	  var tbody = "";
	  var count = 0;
	  for(var i=0;i<seriesData.length;i++){
		  if(null == seriesData[i].value){
			  seriesData[i].value = "-";
		  }else{
			  count +=  seriesData[i].value; 
		  }
		  tbody += "<tr><td>"+ seriesData[i].name + "</td>" +
		            "<td>" + seriesData[i].value + "</td></tr>";
	  } 
	  tbody +="<tr style='background: #f0f1b3;'><td>BUG总数</td><td>" + count + "</td></tr>";
	  $("#chargeOwnerTbody").html(tbody);
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
	
	loadChargeOwner(itemId,startDate,endDate);
});

//重置输入框
document.getElementById("resetInp").addEventListener('click',function(){
	$('#startDate').datebox('setValue', '');	
	$('#endDate').datebox('setValue', '');	
});
//@ sourceURL=chargeOwner.js
