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
/*	reptfixProjectName = $("#analyprojectName").val(); */
 	reptfixStartDate = $("#analyplanStartDate").val(); 
	reptfixEndDate = $("#analyplanEndDate").val();  
/*	reptfixStartDate='2014-9-22'
	reptfixEndDate='2014-10-30'*/
	/*reptfixProNum = $("#analyproNum").val(); 
	$("#itemCode").val(reptfixProNum);
	$("#itemName").val(reptfixProjectName);*/
/*	$("#startDate").datebox("setValue",reptfixStartDate); 
	$("#endDate").datebox("setValue",reptfixEndDate); */
	//获取测试人员
	getTester(reptfixItemId);
	getBugDate(reptfixItemId);
	//加载ehcarts
	//getData(reptfixItemId,reptfixStartDate,reptfixEndDate);

});
//获取有数据的时间
function getBugDate(id){
	var url = baseUrl + "/singleTestTask/singleTestTaskAction!getExeCaseDateLimit.action";
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
function getTester(id){
	var url = baseUrl + "/analysis/analysisAction!getTester.action";
	$.post(
			url,
			{	"analysisDto.taskId":id, 
				},
			function(data){
					var thead = "";
					for(i=0;i<data.length;i++){
					thead += data[i][0]+"  ";
					}
					thead = "测试人员："+thead;
					$("#testerThead").html(thead);
				},'json')
				
}
function getData(itemId,startDate,endDate){
	var url = baseUrl + "/analysis/analysisAction!getTesterExeCaseDayTrend.action";
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
							//将返回来的时间数据全部放到dataDate数组中去 
							dataDate.push(data[i][2]);
							//将返回来的姓名全部放到name数组中去
							name.push(data[i][3]);
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
							var count = 0;
							for(var c=0;c<data.length;c++){
								if(nameDis[a]==data[c][3]&&dataDateDis[b]==data[c][2]){
									count += 1;
									hjk = 1; 
								}else{
									hjk = hjk + 1;
								}
								
							}
							if(count>0){
							lkl.push(count); 
							}
							else if(hjk != 1){
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
	var thead = "<th style=\"text-align:center;\" rowspan=\"2\">日期/人员</th>";
	var thead01 = " ";
	var tbodyData = "";
	var allData = [];

	
	for(var a=0;a<nameDis01.length;a++){
		thead +="<th style=\"text-align:center;\" colspan=\"3\">"+nameDis01[a]+"</th>";

	}
	thead +="<th rowspan=\"2\" style=\"text-align:center\">合计</th>";
	$("#testerExeTrendThead").html(thead);
	var num = document.getElementById('testerExeTrendThead').cells.length
	num = num-2;
	for(i=0;i<num;i++){
		thead01+="<td>通过</td><td>未通过</td><td>合计成本</td>"
	} 
	$("#testerExeTrendThead01").html(thead01);
	
	//铺所有数据
	var allDataList = [];
	for(i=0;i<dateList.length;i++){
		var userList = [];
		allDataList = [];
		allDataList.push(dateList[i]);
		//通过个数、未通过个数、总数、合计成本
		var passnum=0,notPassnum=0,totalnum=0,sumnum=0;
		for(j=0;j<nameDis01.length;j++){
			for(k=0;k<data.length;k++){
				if(nameDis01[j]==data[k][3]&&dateList[i]==data[k][2]){
					sumnum = data[k][4];
					if(data[k][1]=='2'){
						passnum += 1;
					}else{
						notPassnum += 1;
					}
				}
				
			}
			sumnum = sumnum*(passnum+notPassnum);
			totalnum += passnum+notPassnum;
			userList.push(passnum,notPassnum,sumnum);
			
		}
		allDataList.push(userList,totalnum);
		allData.push(allDataList);
	}
	//在表格中展示allData
	for(i=0;i<allData.length;i++){
		tbodyData += "<tr>";
		for(j=0;j<allData[i].length;j++){
			if(j=='1'){
				for(k=0;k<allData[i][j].length;k++){
					tbodyData += "<td>"+allData[i][j][k]+"</td>";
				}
			}else{
				if(j=='0'){
					tbodyData += "<td >"+allData[i][j]+"</td>";
				}else{
					tbodyData += "<td style='background: #f0f1b3;'>"+allData[i][j]+"</td>";
				}
				
			}
		}
		tbodyData += "</tr>"
	}
	
	$("#testerExeTrendTbody").html(tbodyData);
	
	/*var lkl = [];
	for(var b=0;b<dateList.length;b++){
		var hjk = 1;
		for(var c=0;c<data.length;c++){
			if(nameDis01[a]==data[c][1]&&dateList[b]==data[c][0]){
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
	allData.push(lkl);*/
/*	var tableData = [];
	for(b=0;b<dateList.length;b++){
		var tabaleTr = [];
		for(a=0;a<allData.length;a++){
			tabaleTr.push(allData[a][b]);
		}
		tableData.push(tabaleTr);
	}
	
	for(v=0;v<tableData.length;v++){
		tbody += "<tr><td>"+dateList[v]+"</td>";
		for(p=0;p<tableData.length;p++){
			for(kk=0;kk<nameDis01.length;kk++){
				tbody += "<td>"+tableData[v][kk]+"</td>";
			}
		}
		tbody +="</tr>";
	}
	$("#devDayFixThead").html(thead);
	$("#devDayFixTbody").html(tbody);*/

	/*devDayFixTable*/
}

function loadEcharts(){
	var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
    		 
    				title: {
    					text: '日执行用例趋势及明细',
    					subtext: '单位：个'
    				},
    				tooltip: {
    					trigger: 'axis'
    				},
    				legend: {
    					 
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
	/*itemId = $("#itemId").val(); */
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
//@ sourceURL=testerExeCaseDayTrend.js
