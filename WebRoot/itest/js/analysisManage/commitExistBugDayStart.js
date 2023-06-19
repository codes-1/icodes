function getParamString(name) { 
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
    var r = window.location.search.substr(1).match(reg); 
    if (r != null) return unescape(r[2]); 
    return null; 
} 
var allDateList=[];
var allExitData=[];
var commitDataList=[];
var reptfixItemId="",reptfixProjectName="",reptfixStartDate="",reptfixEndDate="",reptfixProNum="";
$(function(){
	$.parser.parse();
	reptfixItemId = $("#analyitemId").val(); 
	reptfixStartDate = $("#analyplanStartDate").val(); 
	reptfixEndDate = $("#analyplanEndDate").val(); 
/*	$("#startDate").datebox("setValue",reptfixStartDate); 
	$("#endDate").datebox("setValue",reptfixEndDate);  */
	getBugDate(reptfixItemId);
	 

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
					getAllData(id,dateArray[0],dateArray[1]);
				}
				},'text');
}
function getAllData(itemId,startDate,endDate){
	//加载ehcarts
	var url = baseUrl + "/analysis/analysisAction!getCommitExistBugAll.action";
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
					getCommiteData(reptfixItemId,startDate,endDate,data);
				}else{
					$("#mainContent").css('display','none');
					$("#nullData").css('display','block');
				}
			},'json');
}
function getCommiteData(itemId,startDate,endDate,allData){
	//加载ehcarts
	var url = baseUrl + "/analysis/analysisAction!getCommitExistBugCom.action";
	$.post(
			url,
			{	"analysisDto.taskId":itemId,
				"analysisDto.startDate":startDate,
				"analysisDto.endDate":endDate
				},
			function(data){
				if(data.length>0){
					//获得所有存在的时间（特殊节点）和对应的提交bug数目
					 for(var i=0;i<data.length;i++){
						 if(i==0){
							 if(allData[0].insdate!=data[i][1]){
								 allDateList.push(allData[i].insdate)
								 commitDataList.push(allData[i].existCount)
								} 	 
						 }
						 
						 if(i==data.length-1){
							 if(allData[allData.length-1].insdate!=data[i][1]){
								 allDateList.push(allData[allData.length-1].insdate)
								 commitDataList.push(allData[allData.length-1].existCount)
								}  
						 }
						
						 
						 allDateList.push(data[i][1])
						 commitDataList.push(data[i][0])
						 
					 }
					//获得对应时间对应的存在的bug数目
					 for(var j=0;j<allDateList.length;j++){
						 for(var k=0;k<allData.length;k++){
							 if(allData[k].insdate==allDateList[j]){
								 allExitData.push(allData[k].existCount)
							 }
						 }
					 }
				 loadEcharts(allDateList,commitDataList,allExitData);
				 loadTable(allDateList,commitDataList,allExitData)
				 }else{
					 for(i=0;i<allData.length;i++){
						 allDateList.push(allData[i].insdate); 
						 commitDataList.push('0')
					 }
					 for(var j=0;j<allDateList.length;j++){
						 for(var k=0;k<allData.length;k++){
							 if(allData[k].insdate==allDateList[j]){
								 allExitData.push(allData[k].existCount)
							 }
						 }
					 }
					 loadEcharts(allDateList,commitDataList,allExitData);
					 loadTable(allDateList,commitDataList,allExitData) 
				 }
			},'json');
}
function loadTable(allDateList,commitDataList,allExitData){
	var tbody = "";
	for(var i=0;i<allDateList.length;i++){
		tbody +="<tr><td>"+allDateList[i]+"</td><td>"+allExitData[i]+"</td><td>"+commitDataList[i]+"</td></tr>";
	}
	$("#commitExistBugTbody").html(tbody);
}
function loadEcharts(allDateList,commitDataList,allExitData){
	var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
    		 
    				title: {
    					text: '待处理bug总数及日提交bug数趋势',
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
    					data:allDateList
    				}],
    				yAxis: [{
    					type: 'value',
    					axisLabel: {
    						formatter: '{value}'
    					}
    				}],
    				series: [{
    					name:"已存在的bug",
    					type:'line',
    					data:allExitData
    				},
    				   
    				{
    					 name:"提交的bug",
    					 type:'line',
    					 data:commitDataList
    					
    				}]
    			};

   

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}
//查看报表
document.getElementById("viewReport").addEventListener('click',function(){
	var itemId = "",startDate="",endDate=""; 
	startDate = $("#startDate").datebox('getValue'); 
	endDate = $("#endDate").datebox('getValue'); 
	
	getAllData(reptfixItemId,startDate,endDate);
});
document.getElementById("resetInp").addEventListener('click',function(){
	$('#startDate').datebox('setValue', '');	
	$('#endDate').datebox('setValue', '');	
});
//@ sourceURL=commitExistBugDayStart.js