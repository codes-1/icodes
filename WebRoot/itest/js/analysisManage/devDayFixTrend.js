function getParamString(name) { 
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
    var r = window.location.search.substr(1).match(reg); 
    if (r != null) return unescape(r[2]); 
    return null; 
} 
var dataList = [];
var dateList= [];
var nameDis01=[];
var reptfixItemId="",reptfixProjectName="",reptfixStartDate="",reptfixEndDate="",reptfixProNum="";
$(function(){
	$.parser.parse();
	reptfixItemId = $("#analyitemId").val(); 
	reptfixStartDate = $("#analyplanStartDate").val(); 
	reptfixEndDate = $("#analyplanEndDate").val(); 
	getBugDate(reptfixItemId);
	//加载ehcarts
	//getData(reptfixItemId,reptfixStartDate,reptfixEndDate);

});
//获取有数据的时间
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
					dateArray[1] = getNewDay(dateArray[1],'1');
					getData(id,dateArray[0],dateArray[1]);
				}
				},'text');
}
function getData(itemId,startDate,endDate){
	var url = baseUrl + "/analysis/analysisAction!getDevDayFixTrend.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId,
				"analysisDto.startDate":startDate,
				"analysisDto.endDate":endDate
				},
			function(data){
				if(data.length>0){ 
					$("#mainContent").css('display','block');
					$("#nullData").css('display','none');
					var dataDate = [];
					var name = [];
					var n = data.length;
					if(n>0){
						for(i=0;i<n;i++){
							if(data[i][1]==null){
								data[i][1]='-'
							}
							//将返回来的时间数据全部放到dataDate数组中去 
							dataDate.push(data[i][1]);
							//将返回来的姓名全部放到name数组中去
							name.push(data[i][2]);
						}
						//dataDate去重
						          var len = dataDate.length;
						          var dataDateDis = []
						          for(var i=0;i<len;i++){
						              var flag = true;
						              for(var j = i;j<dataDate.length-1;j++){
						                  if(dataDate[i]==dataDate[j+1]){
						                    flag = false;
						                     break;
						                }
						             }
						             if(flag){
						            	 dataDateDis.push(dataDate[i])
						             }
						         }
						          dateList = dataDateDis; 
					   //name去重
					        var mm = name.length;
					          var nameDis = []
					          for(var i=0;i<mm;i++){
					              var flag = true;
					              for(var j = i;j<name.length-1;j++){
					                  if(name[i]==name[j+1]){
					                    flag = false;
					                     break;
					                }
					             }
					             if(flag){
					            	 nameDis.push(name[i])
					             }
					         }  
					          nameDis01 = nameDis;
					}
					for(var a=0;a<nameDis.length;a++){
						var kkk = {
		    					name: nameDis[a],
		    					type: 'line', 
		    			};
						var lkl = [];
						for(var b=0;b<dataDateDis.length;b++){
							var hjk = 1;
							for(var c=0;c<data.length;c++){
								if(nameDis[a]==data[c][2]&&dataDateDis[b]==data[c][1]){
									lkl.push(data[c][0]);
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
						dataList.push(kkk);
					}
					loadEcharts();
					loadTable(data);
				}else{
					$("#mainContent").css('display','none');
					$("#nullData").css('display','block');
				}
				
			},'json');
}
function loadTable(data){
//	var thead = "<th>日期/开发人员</th>";
//	var tbody = "";
	/*var allData = [];
	for(i=0;i<nameDis01.length;i++){
		thead +="<th>"+nameDis01[i]+"<th>";
		var lkl = [];
		for(j=0;j<dateList.length;j++){ 
			var hjk = 1;
			for(k=0;k<data.length;k++){
				if(nameDis01[i]==data[k][2]&&dateList[j]==data[k][1]){
					lkl.push(data[k][0]);
					hjk = 1;
					break;
				}else{
					hjk = hjk + 1;
				}
			}
			allData.push(lkl);
//			for(v=0;v<lkl.length;v++){
//				tbody += "<tr><td>"+dateList[j]+"</td><td>"+lkl[v]+"</td>"
//			}
		}
		
	}*/
	var thead = "<th style='text-align:center;background-color:#dce9eb'>日期/开发人员</th>";
	var tbody = "";
	var allData = [];

	
	for(var a=0;a<nameDis01.length;a++){
		thead +="<th style='text-align:center;background-color:#dce9eb'>"+nameDis01[a]+"</th>";
		var lkl = [];
		for(var b=0;b<dateList.length;b++){
			var hjk = 1;
			for(var c=0;c<data.length;c++){
				if(nameDis01[a]==data[c][2]&&dateList[b]==data[c][1]){
					lkl.push(data[c][0]);
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
		allData.push(lkl);
	}
	
	var tableData = [];
	for(b=0;b<dateList.length;b++){
		var tabaleTr = [];
		for(a=0;a<allData.length;a++){
			tabaleTr.push(allData[a][b]);
		}
		tableData.push(tabaleTr);
	}
	
	for(v=0;v<tableData.length;v++){
		tbody += "<tr><td>"+dateList[v]+"</td>";
		/*for(p=0;p<tableData.length;p++){*/
			for(kk=0;kk<nameDis01.length;kk++){
				tbody += "<td>"+tableData[v][kk]+"</td>";
			}
		/*}*/
		tbody +="</tr>";
	}
	$("#devDayFixThead").html(thead);
	$("#devDayFixTbody").html(tbody);

	/*devDayFixTable*/
}

function loadEcharts(){
	var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
    		 
    				title: {
    					text: '开发人员日修改bug趋势',
    					subtext: '单位：个'
    				},
    				tooltip: {
    					trigger: 'axis'
    				},
    				legend: {
    					 orient: 'vertical',
    				        right: 10,
    				        top: 50,
    				        bottom: 20,
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
    			    grid: {
    			        left: '3%',
    			        right: '20%',
    			        bottom: '3%',
    			        containLabel: true
    			    },
    				calculable: true,
    				xAxis: [{
    					type: 'category',
    					boundaryGap: false,
    					data:dateList
    				}],
    				yAxis: [{
    					type: 'value',
    					axisLabel: {
    						formatter: '{value}'
    					}
    				}],
    				series: dataList
    			};

   

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}
//查看报表
document.getElementById("viewReport").addEventListener('click',function(){
	var itemId = "",startDate="",endDate=""; 
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
	endDate = getNewDay(endDate,'1');
	getData(reptfixItemId,startDate,endDate);
});

//重置输入框
document.getElementById("resetInp").addEventListener('click',function(){
	$('#startDate').datebox('setValue', '');	
	$('#endDate').datebox('setValue', '');	
});
function getNewDay(dateTemp, days) {
	   /* var dateTemp01 = dateTemp.split("-");
	    var nDate = new Date(dateTemp01[1] + '/' + dateTemp01[2] + '/' + dateTemp01[0]); //转换为MM-DD-YYYY格式  
	    */
		dateTemp = dateTemp.replace(new RegExp(/-/gm) ,"/");
		var nDate = new Date(dateTemp);
		var millSeconds = Math.abs(nDate) + (days * 24 * 60 * 60 * 1000);
	    var rDate = new Date(millSeconds);
	    var year = rDate.getFullYear();
	    var month = rDate.getMonth() + 1;
	    if (month < 10) month = "0" + month;
	    var date = rDate.getDate();
	    if (date < 10) date = "0" + date;
	    return (""+year+"" + "-" +""+ month +""+ "-"+ ""+ date+"");
	}
//@ sourceURL=devDayFixTrend.js
