	var currDate=new Date().format('yyyy-MM-dd');
	var lgDate = getCookieVal("lgDate");
	if(typeof(lgDate) !='undefined' && lgDate != ''){if(lgDate==currDate){document.getElementById('mypmMainMsgDivs').style.display="none";document.getElementById('mypmMainMsgDivs').innerHTML="<iframe id='mypmMsgF' name='mypmMsgF' src='' frameborder='0' scrolling='no'></iframe>";if(_isIE){myHeight = myHeight+145;}else{ myHeight = myHeight+100;}parent.document.getElementById('mypmMain').height=myHeight;}else{document.getElementById('mypmMainMsgDivs').innerHTML="<iframe id='mypmMsgF' name='mypmMsgF' src='http://www.mypm.cc/publicMsg.html' frameborder='0' scrolling='no'  width='100%' height='90'></iframe>";}}else{document.getElementById("mypmMainMsgDivs").innerHTML="<iframe id='mypmMsgF' name='mypmMsgF' src='http://www.mypm.cc/publicMsg.html' frameborder='0' scrolling='no'  width='100%' height='90'></iframe>";}
	if(typeof(lgDate) !='undefined' && lgDate != ''){
		if(lgDate==currDate){
			document.getElementById('msgSpanId').style.display="none";
		}
	}
	setCookieVal("lgDate=123" );
	function swMsgDiv(){
		if(showMsg){
			showMsg = false;
			document.getElementById('mypmMainMsgDivs').style.display="none";
			parent.document.getElementById("mypmMain").height=(myHeight+90);
			document.getElementById("msgSpanTdDiv").innerHTML
			var imgPh = conextPath+"/dhtmlx/toolbar/images/moveDown.gif";
			document.getElementById("msgSpanTdDiv").innerHTML="<img src='"+imgPh+"'  alt='显示公告' title='显示公告' onclick='swMsgDiv();'/>";
		}else{
			showMsg = true;
			parent.document.getElementById("mypmMain").height=(myHeight-10);
			document.getElementById('mypmMainMsgDivs').style.display="";
			var imgPh = conextPath+"/dhtmlx/toolbar/images/moveUp.gif";
			document.getElementById("msgSpanTdDiv").innerHTML="<img src='"+imgPh+"'  alt='隐藏公告' title='隐藏公告' onclick='swMsgDiv();'/>";
		}
		
	}
