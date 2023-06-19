var bugtype = [];
var names = []; 
var seriesData = [];
var seriesPieData = [];

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
	var url = baseUrl + "/analysis/analysisAction!getTesterBugStat.action";
	var url01 =  baseUrl + "/analysis/analysisAction!getTesterBugStatClose.action";
	//获取提交bug数据
	$.post(
			url,
			{	"analysisDto.taskId":itemId
				},
			function(data){ 
				if(data.length>0){
				//构造echarts数据
				constrEchartsData(data);
				loadEcharts('开发人员提交Bug统计','testerBugStatEcharts');
				loadTable(data,'0');
				loadPieEcharts('testerBugStatPieEcharts');
			  }
			},'json'); 
	//获取关闭bug数据
	$.post(
			url01,
			{	"analysisDto.taskId":itemId
				},
			function(data){ 
				if(data.length>0){
				//构造echarts数据
				constrEchartsData(data);
				loadEcharts('开发人员关闭Bug统计','testerBugStatCloseEcharts');
				loadTable(data,'1');
				loadPieEcharts('testerBugStatClosePieEcharts');
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
	bugtype = [];
	names = [];
	for(var i=0;i<sourceData.length;i++){
		if(sourceData[i][0]=="1"&&sourceData[i][1]=="80"){
			bugtype.push(sourceData[i][2]);
			}else if(sourceData[i][2]!="80"){
				names.push(sourceData[i][1]);
			}
		} 
	//名字去重
	var mm = names.length;
    var nameDis = [];
    for(var i=0;i<mm;i++){
        var flag = true;
        for(var j = i;j<names.length-1;j++){
            if(names[i]==names[j+1]){
              flag = false;
               break;
          }
       }
       if(flag){
      	 nameDis.push(names[i])
       }
   }
    names = nameDis;
}

//获取echarts的seriesData数据

function getSeriesData(sourData){
	seriesData = []
	for(var i=0;i<bugtype.length;i++){
		var obj = {
			      name:bugtype[i],
		          type:'bar',
		          stack:'bug',
		          data:[],
		          
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
	seriesPieData = []
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
		    legend: {
                x: 'right',
		        data: bugtype, 
		    	orient: 'vertical',
		    	width: 250,
		    	top: 50, 
		    	left:650
		    },
		    grid: {
		        left: '3%',
		        right: '20%',
		        bottom: '3%',
		        containLabel: true
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

function loadPieEcharts(id){
		var myChart = echarts.init(document.getElementById(id));

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
function loadTable(data,flag){
	var thead = "";
	if(flag=='0'){
		thead +="<th>等级/开发人员</th>";
	}else{
		thead +="<th>等级/测试人员</th>";
	}
		  
		for(var j=0;j<names.length;j++){
			  thead +="<th>" + names[j] + "</th>";
		 }
		  thead +="<td>合计</th>";
		if(flag=='0'){
			document.getElementById("testerBugStatThead").innerHTML = thead;
		}else{
			document.getElementById("testerBugStatCloseThead").innerHTML = thead;
		}
		  
		  
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
		if(flag=='0'){
            document.getElementById("testerBugStatTbody").innerHTML = tbody;	
		}else{
			document.getElementById("testerBugStatCloseTbody").innerHTML = tbody;	
		}
	

}
//@ sourceURL=testerBugStat.js
