'use strict';

if (typeof jQuery === "undefined") {
	throw new Error("需要引入jQuery");
}

var hover ="";
var picName = "";
var imgPath = "";
var systemMenusArr =[];//顶部导航--系统管理
var otherId = ""; //存放其他任务左边菜单li的id
var casId = ""; //存放用例库左边菜单li的id
var pwdFlag01 = '0'; 
var oldPwdFlag02 = '0';
var flagP = "";
var menus = new Object();
//项目任务标识、项目测试标识、项目迭代标识、系统设置标识
var taskFlg=0;
var sysTestFlg=0;
var sysDieFlg=0;
var sysConfigFlg=0;
// 缓存全局变量及dom元素
var mainObjs = {
	tableHeight: window.innerHeight - 91,
	$body: $("body"),
	$menu: $("#menu"),
	$homeMenu: $("#home_menu").parent('li'),
//	$treeArea: $(".tree-area"),
	$wrapPage: $("#wrap-page"),
	$switchBtn: $(".switch-btn"),
	$addOrEditWin: $("#addOrEditWinUserInfo"),
	$addOrEditForm: $("#addOrEditFormUserInfo"),
	$logoImg:$(".sidebary"),
	$systemM:$('#systemM'),
	$systemMenu:$("#systemMenu")
};
$(function() {
	$.parser.parse();
	//事先加载js，防止以后出现easyui组件加载未完成造成的问题
	exuiloader.load('datebox', null, true);
	exuiloader.load('layout', null, true);

	$("#loginNames").html("&emsp;"+$("#userName").val()+",你好！");
	
	//获取权限
	getLoginUserProManager();
	//首页的设置，无首页弹窗选择具体项目
	var flg = showHomePageWin();
	flagP = $("#projectFlag").val();
	
	
	if(flg!=false){
		var myHomeUrls = $("#loginData").data("loginData")["dto.user"].myHome;
		var myUrlSplitR = myHomeUrls.split('&')[0]; 
		if(myHomeUrls!=undefined&&myHomeUrls!=""){
			if(privilegeMap[myUrlSplitR]!="1" && myHomeUrls.indexOf("otherMission") == -1){
				logoEvent();
				tipPermission('您设置的首页所对应的权限己被收回，请联系管理员！');
				//return;
			}
		}
		
		if(flg==true){
			//加载左边菜单及效果
			loadLeftMenus();
		}else{
			$("#leftMenu").hide();
			$("#wrap-page").removeClass();
			tipPermission("首页url参数格式不匹配！");
		}
	}else{
		if(flagP!="1"){//没有参与任何项目
			//加载新用户，没参与项目
			var noHtml = newUserLoadMenu();
			mainObjs.$menu.append(noHtml);
			loadLeftMenus();
		}else{//参与了项目
			//加载左边菜单及效果
			loadLeftMenus();
		}
		$(".content").addClass('content_c');
		mainObjs.$wrapPage.load(baseUrl + "/itest/jsp/itestManual/itestManual.jsp");
		
		//加载左边菜单及效果
//		loadLeftMenus();
//		$("#leftMenu").hide();
//		$(".content").addClass('content_c');
//		mainObjs.$wrapPage.load(baseUrl + "/itest/jsp/itestManual/itestManual.jsp");
	}
	
	var sysContent = $.trim($('#systemM').html());
	if(sysContent==""){
		$("#systemMenu").hide();
	}
	
	//收缩按钮的移动样式
	sidebaryHover();
	//导航栏菜单的样式
	navMenueS();
	//判断是否为管理员，为管理员，显示测试用例库左边入库审核菜单
	if($("#isAdmin").text() == "2" || $("#isAdmin").text() == "1"){
		$("#l3").show();
		$("#m5").show();
		$("#l1").show();
	}else{
		$("#l3").hide();
		$("#m5").hide();
		$("#l1").hide();
	}
	//监听session是否过期，如果过期，则跳到登录页面
	$.ajaxSetup({
        contentType:"application/x-www-form-urlencoded;charset=utf-8",
        complete:function(XMLHttpRequest,textStatus){
            //通过XMLHttpRequest取得响应头，sessionstatus，
            var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");
            if(sessionstatus=="timeout"){
                //如果超时就处理 ，指定要跳转的页面(比如登陆页)
            	$.xconfirm({
            		msg:'会话超时，请返回登录页面',
            		okFn: function() {
            			window.parent.location.href= baseUrl + "/login.htm";
            		}
            	});
            }
         }
      });
	//自定义只能输入正数的方法
    $.extend($.fn.validatebox.defaults.rules, {
        zhengshu: {
    		validator: function(value){
    			var par = /^\d+$/;
    			return par.test(value);
    		},
    		message: '请输入非负整数'
        }
    });
  //自定义0到100的数字
    $.extend($.fn.validatebox.defaults.rules, {
        lessThan: {
    		validator: function(value){
    			var par = /^100$|^(\d|[1-9]\d)(\.\d+)*$/;
    			return par.test(value);
    		},
    		message: '请输入0到100的数'
        }
    });
});

//加载左边菜单及效果
function loadLeftMenus(){
	
	$.ajaxSettings.async = false;
	$.getJSON(
			baseUrl + "/navigate?reload=1",
			function(data) {
				var html = '',a ='',b='',c='',d='';
				var navTopHtml = "";  //保存动态顶部导航栏--系统管理的html
				if (data && data.length!=0) {
//					var flag = '0';
					flagP = $("#projectFlag").val();
//					mainObjs.$menu.empty();
					$.each(data, function(i, v) {
//						if ("1" != v.level) {
//							return true;
//						}
						if(flagP!="1"){
							if(v.functionId=="68"||v.functionId=="87"||v.functionId=="107"||v.functionId=="143" ||v.functionId=="11" || v.functionId=="13"){
								menus["menu_"+v.functionId] = v;
								/*if(v.functionId=="68"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/projectMan1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="87"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/testSin1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="107"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/defect1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="143"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/analysis1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="11"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/testcasepkg1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="13"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview" style="cursor:pointer;">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/zl1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}*/
							}
						}else if(flagP=="1" && $("#haveProject").val() == "1"){
							if(v.functionId=="68"||v.functionId=="87"||v.functionId=="107"||v.functionId=="143" ||v.functionId=="11" || v.functionId=="13"){
								menus["menu_"+v.functionId] = v;
								if(v.functionId=="68"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/projectMan1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="87"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/testSin1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="107"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/defect1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="143"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/analysis1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="11"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/testcasepkg1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="13"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview" style="cursor:pointer;">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/zl1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}
							}
							
						}else if(flagP=="1" && $("#haveProject").val() == "0"){
							if(v.functionId=="68"||v.functionId=="87"||v.functionId=="107"||v.functionId=="143" ||v.functionId=="11" || v.functionId=="13"){
								menus["menu_"+v.functionId] = v;
								if(v.functionId=="68"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview disabled">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/projectMan1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="87"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview disabled">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/testSin1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="107"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview disabled">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/defect1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="143"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview disabled">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/analysis1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="11"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview disabled">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/testcasepkg1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}else if(v.functionId=="13"){
									html += '<li id="menu_'+v.functionId+'" data-info="'+v.functionId+'" class="treeview disabled">'
									+'<a href="javascript:void(0);" title='+v.functionName+' class="firstA">'
									+'<img class="" src="'+baseUrl+'/itest/images/homeImage/zl1.png">&nbsp;&nbsp;&nbsp;'
									+'<span class="">' + v.functionName + '&nbsp;&nbsp;</span>'
									+'</a></li>';
								}
							}
						}
						
						//顶部导航栏--系统管理
						if(v.functionId=="3"||v.functionId=="20"||v.functionId=="25"||v.functionId=="44"){
							systemMenusArr[i] = v;
							if(v.functionId=="3"){
								a = "<li class='systemMLine' id='system_" + i +"'><a tabindex='-1' href='javascript:void(0);'>用户管理</a></li>";
					
							}
							if(v.functionId=="20"){
								b = "<li  class='systemMLine' id='system_" + i +"'><a tabindex='-1' href='javascript:void(0);'>用户组管理</a></li>";
					
							}
							if(v.functionId=="25"){
								c = "<li  class='systemMLine' id='system_" + i +"'><a tabindex='-1' href='javascript:void(0);'>权限管理</a></li>";
					
							}
							if(v.functionId=="44"){
							
								d = "<li  class='systemMLine' id='system_" + i +"'><a tabindex='-1' href='javascript:void(0);'>基础字典维护</a></li>";
					
							}
							
						}
					});
					
					if(flagP!="0" && $("#haveProject").val() == "1"){
						
						html += '<li id="menu_s" data-info="" class="treeview">'
							+'<a href="javascript:void(0);" class="firstA">'
							+'<img class="" src="'+baseUrl+'/itest/images/homeImage/myHome1.png">&nbsp;&nbsp;&nbsp;'
							+'<span class="">设置首页&nbsp;&nbsp;</span>'
							+'</a></li>';
					}

					navTopHtml = a+b+c+d;
					mainObjs.$systemM.empty();
					mainObjs.$systemM.append(navTopHtml);
					mainObjs.$menu.append(html);
					mainObjs.$menu.data('menus', menus);
					init();
					hoverEvent();
					//顶部导航栏--系统管理二级菜单绑定点击事件
					bindSystemMenu();
				} else {
					var notice="您没有任何权限请联系管理员,维护权限！";
					tipPermission(notice);
				}
			}
		);
}

//提示
function tipPermission(notice){
	$.xconfirm({
		msg:notice,
		okFn: function() {
			
		}
	});
}

//设置首页
function setSelfHomePage(){
	var url = "";
	var message = "";
	var cuId = "";
	var liNum = $("#menu li");
	var chaImg = "";
	var outlineSta = $("#outlineState").val();
//	$("#dataUrl").data("dataUrl","menu_s"+"^"+""+"^"+url);
	$.each(liNum,function(i,v){
			cuId = liNum[i].id;
			var attr = $("#"+cuId)[0].attributes[3];
			if(cuId!="menu_s"&&attr!=undefined){
				var back = attr.nodeValue;
				if(back=="background: rgb(30, 124, 251);"||back=="background: rgb(30, 124, 251) none repeat scroll 0% 0%;"){
					$("#menuId").val(cuId);
					chaImg =$("#"+cuId).find("img").attr("src");
					if(cuId=="menu_107"){
						url = mainObjs.$menu.data('menus')[cuId].url+"&taskId="+$("#taksIdmain").val()+ 
						"&analyprojectName=" + $("#analyprojectName").val() + "&analyproNum=" + $("#analyproNum").val()+"&outlineState="+outlineSta;
					}else{
						url = mainObjs.$menu.data('menus')[cuId].url+"&taskId="+$("#taksIdmain").val()+ 
						"&analyprojectName=" + $("#analyprojectName").val() + "&analyproNum=" + $("#analyproNum").val()+"&outlineState="+outlineSta;
					}
					message = "确认把"+$.trim($("#"+cuId).find('span').text())+"设置为首页吗?";
				}
			}
	});
	
	tipHomePage(message,url,$("#menuId").val(),chaImg);
}
//其他任务设置首页
function setSelfHomePage1(){
	var url = $("#"+otherId).attr("myUrl");
	var message = "确认把 " + $("#"+otherId).find("span").text() + "设置为首页吗？";
	tipHomePage1(message,url);
}
//获取当前登录人的信息
function getCurrentLoginPer(){
	var urls = baseUrl + "/userManager/userManagerAction!setMyInfoInitLoad.action";
	$.ajax({
		  url: urls,
		  cache: false,
		  async: false,
		  type: "POST",
		  dataType:"json",
//		  dataType:"text",
		  success: function(data){
			  if(data != null){
				  $("#loginData").data("loginData",data);
			  }else{
//				  $.xnotify('系统问题！', {type:'warning'});
				  tipPermission("系统问题！");
			  }
		  }
		});
}
//提交设置其他任务首页信息
function tipHomePage1(tip,url){
	if(tip!=""){
		$.xconfirm({
			msg:tip,
			okFn: function() {
//				$("#taksIdmain").val();
//				var myHomeUrl = url;
				var dataInfo = $("#loginData").data("loginData");
				var urls = baseUrl + "/userManager/userManagerAction!updateMyInfo.action?dto.isAjax=true";
				$.ajax({
					  url: urls,
					  cache: false,
					  async: false,
					  type: "POST",
//					  dataType:"json",
					  dataType:"text",
					  data: {
						  	"dto.user.myHome":url,
						  	"dto.user.id":$("#accountId").text(),
						  	"dto.user.loginName":$("#loginName").text(),
						  	"dto.user.name":$("#userName").val(),
						  	"dto.user.email":dataInfo["dto.user"].email,
						  	"dto.user.companyId":dataInfo["dto.user"].companyId,
						  	"dto.user.delFlag":dataInfo["dto.user"].delFlag,
						  	"dto.user.insertDate":dataInfo["dto.user"].insertDate,
						  	"dto.user.isAdmin":dataInfo["dto.user"].isAdmin,
						  	"dto.user.delFlag":dataInfo["dto.user"].delFlag,
						  	"dto.user.insertDate":dataInfo["dto.user"].insertDate,
						  	"dto.user.isAdmin":dataInfo["dto.user"].isAdmin,
						  	"dto.user.status":dataInfo["dto.user"].status,
						  	"dto.user.password":dataInfo["dto.user"].password,
						  	"dto.user.oldPwd":dataInfo["dto.user"].oldPwd
						  	},
					  success: function(data){
						  if(data.indexOf("success")>=0){
							  $("#"+otherId).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
							  $("#"+otherId).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
						  }else{
//							  $.xnotify('系统问题！', {type:'warning'});
							  tipPermission("系统问题！");
						  }
					  }
					});
			}
		});
	}else{
		tipNotify();
	}
	
}
//提交设置首页信息
function tipHomePage(tip,url,id,imgPath){
	var returnUrl = $("#dataUrl").data('dataUrl');
	var home_Url = "";
	var splitUrl = "";
	if(returnUrl!=undefined){
		home_Url = url;
		splitUrl = returnUrl.split("^");
	}else{
		home_Url = "/bugManager/bugManagerAction!loadAllMyBug.action?dto.allTestTask=true";
	}
	
	if(tip!=""){
		$.xconfirm({
			msg:tip,
			okFn: function() {
				var dataInfo = $("#loginData").data("loginData");
				var urls = baseUrl + "/userManager/userManagerAction!updateMyInfo.action?dto.isAjax=true";
				$.ajax({
					  url: urls,
					  cache: false,
					  async: false,
					  type: "POST",
//					  dataType:"json",
					  dataType:"text",
					  data: {
						  	"dto.user.myHome":home_Url,
						  	"dto.user.id":$("#accountId").text(),
						  	"dto.user.loginName":$("#loginName").text(),
						  	"dto.user.name":$("#userName").val(),
						  	"dto.user.email":dataInfo["dto.user"].email,
						  	"dto.user.companyId":dataInfo["dto.user"].companyId,
						  	"dto.user.delFlag":dataInfo["dto.user"].delFlag,
						  	"dto.user.insertDate":dataInfo["dto.user"].insertDate,
						  	"dto.user.isAdmin":dataInfo["dto.user"].isAdmin,
						  	"dto.user.delFlag":dataInfo["dto.user"].delFlag,
						  	"dto.user.insertDate":dataInfo["dto.user"].insertDate,
						  	"dto.user.isAdmin":dataInfo["dto.user"].isAdmin,
						  	"dto.user.status":dataInfo["dto.user"].status
//						  	,
//						  	"dto.user.password":dataInfo["dto.user"].password,
//						  	"dto.user.oldPwd":dataInfo["dto.user"].oldPwd
						  	},
					  success: function(data){
						  if(data.indexOf("success")>=0){
							  $("#menu_s")[0].attributes[3].nodeValue="";
							  $("#menu_s > a").find('img').attr('src',baseUrl+"/itest/images/homeImage/myHome1.png");
							  $("#menu_s > a span").css("color","#E9F6FF");
							  $("#"+id).find('img').attr('src',imgPath);
							  $("#"+id).find('span').css("color","#ffffff");
							  $("#"+id)[0].attributes[3].nodeValue="background: rgb(30, 124, 251);";
							  
							  if(splitUrl[0]!="menu_s"){
								  $("#"+splitUrl[0]).css('background','#1e7cfb');
								  $("#"+splitUrl[0]).find('img').attr('src',splitUrl[1]);
								  $("#"+splitUrl[0]).find('span').css('color','#ffffff');
							  }
							  
//							  mainObjs.$wrapPage.load(baseUrl + url);
						  }else{
//							  $.xnotify('系统问题！', {type:'warning'});
							  tipPermission("系统问题！");
						  }
					  }
					});
			},
			cancelFn: function(){
				 $("#menu_s")[0].attributes[3].nodeValue="";
				 $("#menu_s > a").find('img').attr('src',baseUrl+"/itest/images/homeImage/myHome1.png");
				 $("#menu_s > a span").css("color","#E9F6FF");
				 $("#"+id).find('img').attr('src',imgPath);
				 $("#"+id).find('span').css("color","#ffffff");
				 $("#"+id)[0].attributes[3].nodeValue="background: rgb(30, 124, 251);";
				 
				 if(splitUrl[0]!="menu_s"&&splitUrl[0]!=undefined){
					 $("#"+splitUrl[0]).css('background','#1e7cfb');
					 $("#"+splitUrl[0]).find('img').attr('src',splitUrl[1]);
					 $("#"+splitUrl[0]).find('span').css('color','#ffffff');
				 }
			}
		});
	}else{
		tipNotify();
	}
}

function tipNotify(){
//	$.xnotify('请重新选中需要设为首页的左边菜单！', {type:'warning'});
	var returnUrl = $("#dataUrl").data('dataUrl');
	$.xconfirm({
		msg:"请重新选中需要设为首页的左边菜单！",
		okFn: function() {
			
		},
		cancelFn: function(){
			
		}
		

	});
}

//首页的设置，无首页弹窗选择具体项目
function showHomePageWin(){
	getCurrentLoginPer();
	var myHome =  $("#loginData").data("loginData")["dto.user"].myHome;
	if(myHome!=undefined&&myHome!=""){
//		mainObjs.$wrapPage.load(baseUrl + myHome);
		$("#myhome").data("myHome",myHome);
		
		//将url中的参数拆分存入session
	   var proName = "",proNum="",taskId="";
	   var headerParamArr = myHome.split("&");
	   if(headerParamArr.length<=1){
		   return false;
	   }
	   var reqUrlParam = (headerParamArr.length>0)? headerParamArr.splice(0,1):headerParamArr;
	   //headerParamArr.push(reqUrlParam.toString().split("?")[1]);
	   for(var i=0;i<headerParamArr.length;i++){
		   var param = headerParamArr[i].split("=");
		       param[1] = (param[1] == null)? "":param[1];
		   switch (param[0]){
		     case "analyprojectName":{
		    	 $("#analyprojectName").val(param[1]);
                 proName = param[1];
                 break;
		     }
		     case "analyproNum":{
		    	 $("#analyproNum").val(param[1]); 
		    	 proNum = param[1];
		    	 break;
		     }
		     case "taskId":{
		    	 $("#taksIdmain").val(param[1]);
		    	 $("#analyitemId").val(param[1]);
		    	 taskId = param[1];
		    	 break;
		     }
		     case "outlineState":{
		    	 $("#outlineState").val(param[1]);
		    	 break;
		     }
		     
		   }  
	   }
		//将页面数据放入session
		putAnalysisSession(taskId,proName,proNum); 
		return true;
//		putTaskIdSession(taskId);
	}else{
		//切换项目弹窗
//		var flagW="1";
//		caseItems();
		$("#leftMenu").hide();
		$(".sidebary").hide();
		return false;
	}
}

//加载新用户，没参与项目
function newUserLoadMenu(vs,is){
	var con = "";
	if(vs!=""&&vs!=undefined){
		if(vs.functionId=="68"||vs.functionId=="87"||vs.functionId=="107"||vs.functionId=="143"){
//			menus["menu_"+i] = v;
			if(vs.functionId=="68"){
				con += '<li id="menu_'+is.functionId+'" data-info="'+vs.functionId+'" class="treeview disabled">'
				+'<a href="javascript:void(0);" class="firstA">'
				+'<img class="" src="'+baseUrl+'/itest/images/homeImage/projectMan2.png">&nbsp;&nbsp;&nbsp;'
				+'<span class="" style="color: #8b94a0;">' + vs.functionName + '&nbsp;&nbsp;</span>'
				+'</a></li>';
			}else if(vs.functionId=="87"){
				con += '<li id="menu_'+is.functionId+'" data-info="'+vs.functionId+'" class="treeview disabled">'
				+'<a href="javascript:void(0);" class="firstA">'
				+'<img class="" src="'+baseUrl+'/itest/images/homeImage/testSin2.png">&nbsp;&nbsp;&nbsp;'
				+'<span class="" style="color: #8b94a0;">' + vs.functionName + '&nbsp;&nbsp;</span>'
				+'</a></li>';
			}else if(vs.functionId=="107"){
				con += '<li id="menu_'+is.functionId+'" data-info="'+vs.functionId+'" class="treeview disabled">'
				+'<a href="javascript:void(0);" class="firstA">'
				+'<img class="" src="'+baseUrl+'/itest/images/homeImage/defect2.png">&nbsp;&nbsp;&nbsp;'
				+'<span class="" style="color: #8b94a0;">' + vs.functionName + '&nbsp;&nbsp;</span>'
				+'</a></li>';
			}else if(vs.functionId=="143"){
				con += '<li id="menu_'+is.functionId+'" data-info="'+vs.functionId+'" class="treeview disabled">'
				+'<a href="javascript:void(0);" class="firstA">'
				+'<img class="" src="'+baseUrl+'/itest/images/homeImage/analysis2.png">&nbsp;&nbsp;&nbsp;'
				+'<span class="" style="color: #8b94a0;">' + vs.functionName + '&nbsp;&nbsp;</span>'
				+'</a></li>';
			}
		}
	}else{
		con = '<li id="menu_11" data-info="" class="treeview disabled">'
		+'<a href="javascript:void(0);" class="firstA">'
		+'<img class="" src="'+baseUrl+'/itest/images/homeImage/zl1.png">&nbsp;&nbsp;&nbsp;'
		+'<span class="" style="color: #8b94a0;">总览&nbsp;&nbsp;</span>'
		+'</a></li><li id="menu_11" data-info="" class="treeview disabled">'
		+'<a href="javascript:void(0);" class="firstA">'
		+'<img class="" src="'+baseUrl+'/itest/images/homeImage/projectMan2.png">&nbsp;&nbsp;&nbsp;'
		+'<span class="" style="color: #8b94a0;">测试需求管理&nbsp;&nbsp;</span>'
		+'</a></li><li id="menu_12" data-info="" class="treeview disabled">'
		+'<a href="javascript:void(0);" class="firstA">'
		+'<img class="" src="'+baseUrl+'/itest/images/homeImage/testSin2.png">&nbsp;&nbsp;&nbsp;'
		+'<span class="" style="color: #8b94a0;">测试用例管理&nbsp;&nbsp;</span>'
		+'</a></li><li id="menu_13" data-info="" class="treeview disabled">'
		+'<a href="javascript:void(0);" class="firstA">'
		+'<img class="" src="'+baseUrl+'/itest/images/homeImage/defect2.png">&nbsp;&nbsp;&nbsp;'
		+'<span class="" style="color: #8b94a0;">缺陷管理&nbsp;&nbsp;</span></a>'
		+'</li><li id="menu_14" data-info="" class="treeview disabled">'
		+'<a href="javascript:void(0);" class="firstA">'
		+'<img class="" src="'+baseUrl+'/itest/images/homeImage/analysis2.png">&nbsp;&nbsp;&nbsp;'
		+'<span class="" style="color: #8b94a0;">分析度量&nbsp;&nbsp;</span></a></li><li id="menu_14" data-info="" class="treeview disabled">'
		+'<a href="javascript:void(0);" class="firstA">'
		+'<img class="" src="'+baseUrl+'/itest/images/homeImage/testcasepkg1.png">&nbsp;&nbsp;&nbsp;'
		+'<span class="" style="color: #8b94a0;">用例包管理&nbsp;&nbsp;</span></a></li><li id="menu_14" data-info="" class="treeview disabled">'
		+'<a href="javascript:void(0);" class="firstA">'
		+'<img class="" src="'+baseUrl+'/itest/images/homeImage/myHome1.png">&nbsp;&nbsp;&nbsp;'
		+'<span class="" style="color: #8b94a0;">设置首页&nbsp;&nbsp;</span></a></li>';
	}
	return con;
}

////导航栏菜单的样式
function navMenueS(){
	var navImg = $('.navbar-left.switchm').find("img.switch");//项目测试
	var navImg_1= $('.navbar-left.systemM').find("img.switch");//系统设置
	var navImg_2= $('.navbar-left.elseTask').find("img.switch"); //项目任务
	var navImg_3= $('.navbar-left.iterations').find("img.switch"); //项目迭代
	var switchBtn = navImg.attr('src');
	var swSplit = switchBtn.split('.png');
	var swRealPath =  swSplit[0]+"1.png";
	
	var switchBtn_1 = navImg_1.attr('src');
	var swSplit_1 = switchBtn_1.split('.png');
	var swRealPath_1 =  swSplit_1[0]+"1.png";
	
	var switchBtn_2 = navImg_2.attr('src');
	var swSplit_2 = switchBtn_2.split('.png');
	var swRealPath_2 =  swSplit_2[0]+"1.png";
	
	var switchBtn_3 = navImg_3.attr('src');
	var swSplit_3 = switchBtn_3.split('.png');
	var swRealPath_3 =  swSplit_3[0]+"1.png";
	
	//项目测试
	$('.navbar-left.switchm').hover(function(){
		var path01 = $('.navbar-left.switchm').find("img.switch").attr('src');
		navImg.attr('src',swRealPath);
		$("#changePro").css('color','#ffffff');
		navImg.next().children('img.arrows').attr('src',''+baseUrl+'/itest/images/homeImage/arrow1.png');
		navImg.next().children('img.arrows').css({width:'14',height:'8'});
	},function(){
		var flag = mainObjs.$logoImg.attr("data-show"); 
		if(sysTestFlg==0){
			navImg.attr('src',switchBtn);
			$("#changePro").css('color','#E9F6FF');	
		}
		/*if(flag!="3" && flag!="7"){
			navImg.attr('src',switchBtn);
			$("#changePro").css('color','#E9F6FF');
		}*/
		navImg.next().children('img.arrows').attr('src',''+baseUrl+'/itest/images/homeImage/arrow.png');
		navImg.next().children('img.arrows').css({width:'14',height:'8'});
	});
	
	//系统设置
	$('.navbar-left.systemM').hover(function(){
		navImg_1.attr('src',swRealPath_1);
		$("#systemManage").css('color','#ffffff');
		navImg_1.next().children('img.arrows').attr('src',''+baseUrl+'/itest/images/homeImage/arrow1.png');
		navImg_1.next().children('img.arrows').css({width:'14',height:'8'});
	},function(){
		var flag1 = mainObjs.$logoImg.attr("data-show"); 
		if(sysConfigFlg==0){
			navImg_1.attr('src',switchBtn_1);
			$("#systemManage").css('color','#E9F6FF');	
		}
		/*if(flag1!=3){
			navImg_1.attr('src',switchBtn_1);
			$("#systemManage").css('color','#E9F6FF');
		}*/
		navImg_1.next().children('img.arrows').attr('src',''+baseUrl+'/itest/images/homeImage/arrow.png');
		navImg_1.next().children('img.arrows').css({width:'14',height:'8'});
	});
	
	//项目任务
	$('.navbar-left.elseTask').hover(function(){
		navImg_2.attr('src',baseUrl+'/itest/images/homeImage/elseTs1.png');
		navImg_2.next().css('color','#ffffff');
	},function(){
		if(taskFlg==0||$("#otherMissionLeftMenu").css("display") == "none" || $("#leftMenu").css("display") == "none"){
			navImg_2.attr('src',baseUrl+'/itest/images/homeImage/elseTs.png');
			navImg_2.next().css('color','#E9F6FF');
		}
	});
	
	$(".otherMissionCl").hover(function(){
		if($(this).attr("id") == "m6"){
			$(this).css("background", "rgb(30, 124, 251) none repeat scroll 0% 0%");
		}else{
			$(this).css("background", "rgb(30, 124, 251) none repeat scroll 0% 0%");
			if($(this).attr("id") != otherId){
				$(this).children().children().attr("src",$(this).children().children().attr("src").split("1")[0]+".png");
			}
		}
	},function(){
		if($(this).attr("id") == "m6"){
			$(this).css("background", "rgb(61, 67, 81) none repeat scroll 0% 0%");
		}else{
			$(this).css("background", "rgb(61, 67, 81) none repeat scroll 0% 0%");
			if($(this).attr("id") != otherId){
				$(this).children().children().attr("src",$(this).children().children().attr("src").split(".png")[0]+"1.png");
			}
		}
		$("#"+otherId).css("background", "rgb(30, 124, 251) none repeat scroll 0% 0%");
	});
	
	$(".yongli").hover(function(){
		$(this).css("background", "rgb(30, 124, 251) none repeat scroll 0% 0%");
		if($(this).attr("id") != casId){
			$(this).children().children().attr("src",$(this).children().children().attr("src").split("1")[0]+".png");
		}
	},function(){
		$(this).css("background", "rgb(61, 67, 81) none repeat scroll 0% 0%");
		if($(this).attr("id") != casId){
			$(this).children().children().attr("src",$(this).children().children().attr("src").split(".png")[0]+"1.png");
		}
		$("#"+casId).css("background", "rgb(30, 124, 251) none repeat scroll 0% 0%");
	});
	//个人信息
	$('.pull-right.systemC').hover(function(){
		$("#arrowDown").attr('src',''+baseUrl+'/itest/images/homeImage/arrow1.png');
		$("#arrowDown").css('color','#ffffff');
		$("#userInfoMenus").css('display','block');
	},function(){
		$("#arrowDown").attr('src',''+baseUrl+'/itest/images/homeImage/arrow.png');
		$("#arrowDown").prev().css('color','#E9F6FF'); 
		$("#userInfoMenus").css('display','none');
	}); 
	//新增--1203
	$('.pull-right.new_icon').hover(function(){
		$("#new_icon").attr('src',''+baseUrl+'/itest/images/Group2.png'); 
		$("#new_icon_info").css('display','block');
	},function(){
		$("#new_icon").attr('src',''+baseUrl+'/itest/images/Group.png'); 
		$("#new_icon_info").css('display','none');
	});
	//项目迭代
	$('.navbar-left.iterations').hover(function(){
		navImg_3.attr('src',swRealPath_3);
		$("#iterL").css('color','#ffffff');
		navImg_3.next().children('img.arrows').attr('src',''+baseUrl+'/itest/images/homeImage/iterList1.png');
		/*navImg_3.next().children('img.arrows').css({width:'14',height:'8'});*/
	},function(){
		var flag = mainObjs.$logoImg.attr("data-show");
		if(sysDieFlg==0){
			navImg_3.attr('src',switchBtn_3);
			$("#iterL").css('color','#E9F6FF');
		}
		/*if(flag!=3){
			navImg_3.attr('src',switchBtn_3);
			$("#iterL").css('color','#E9F6FF');
		}*/
		/*navImg_3.next().children('img.arrows').attr('src',''+baseUrl+'/itest/images/homeImage/arrow.png');
		navImg_3.next().children('img.arrows').css({width:'14',height:'8'});*/
	});
	
}

//收缩按钮的移动样式
function sidebaryHover(){
	var btn = mainObjs.$logoImg.attr('src');
	var btnSplit = btn.split('.png');
	var realPath =  btnSplit[0]+"1.png";
	mainObjs.$logoImg.hover(function(){
		mainObjs.$logoImg.attr('src',realPath);
	},function(){
		mainObjs.$logoImg.attr('src',btn);
	});
}

//点击收缩左边菜单
mainObjs.$logoImg.click(function(){
	var showVal = mainObjs.$logoImg.attr("data-show");
	/*mainObjs.$wrapPage.load(baseUrl + "/singleTestTask/singleTestTaskAction!magrTaskList.action");*/
	var navFlg = $("#navTop").data('effect');//标志位，判断来自nav的点击事件，去除左边菜单点亮，并不进入默认的首页
//	var navFlags = $("#navFlag").data("navFlag");
	//获取点亮左边菜单的url
	var returnVal = getLeftMenuUrl();
	var myHomes =  $("#loginData").data("loginData")["dto.user"].myHome;
	
	if(showVal=="2"){
		$("#main_header > a").width(180);
		$(".l-menu").width(180);
		$("#menu a").width(180);
		$("#menu li span").show();
		$(".aaaa").show();
		$(".main-header .sidebar-toggle").css('paddingLeft','9em');
		$(".logo .logo-lg").show();
		$(".logo .logo-min").hide();
		$(".content").removeClass('content_L').addClass('content');
		setTimeout(function(){ 
			if(returnVal!=true){
				mainObjs.$wrapPage.load(baseUrl + myHomes.split("&")[0]);
			}else{
				mainObjs.$wrapPage.load(baseUrl + $("#menuUrl").data("menuUrl"));
			}
		},1500);
		mainObjs.$logoImg.attr("data-show","1");
	}else if(showVal=="1"){
		$("#menu li span").hide();
		$(".aaaa").hide();
		$("#main_header > a").width(75);
		$(".l-menu").width(75);
		$("#menu a").width(75);
		$(".main-header .sidebar-toggle").css('paddingLeft','4em');
		$(".logo .logo-lg").hide();
		$(".logo .logo-min").show();
		$(".content").addClass('content_L');
		setTimeout(function(){ 
			if(returnVal!=true){
				mainObjs.$wrapPage.load(baseUrl + myHomes.split("&")[0]);
			}else{
				mainObjs.$wrapPage.load(baseUrl + $("#menuUrl").data("menuUrl"));
			}
		},1500);
		mainObjs.$logoImg.attr("data-show","2");
	}else if(showVal=="5"){
		$("#otherMissionLeftMenu li span").hide();
		$(".aaaa").hide();
		$("#main_header > a").width(75);
		$(".l-menu").width(75);
		$("#otherMissionLeftMenu a").width(75);
		$(".main-header .sidebar-toggle").css('paddingLeft','4em');
		$(".logo .logo-lg").hide();
		$(".logo .logo-min").show();
		$(".content").addClass('content_L');
		mainObjs.$logoImg.attr("data-show","6");
		mainObjs.$wrapPage.load(baseUrl + $("#"+otherId).attr("myUrl"));
	}else if(showVal=="6"){
		$("#otherMissionLeftMenu li span").show();
		$(".aaaa").show();
		$("#main_header > a").width(180);
		$(".l-menu").width(180);
		$("#otherMissionLeftMenu a").width(180);
		$(".main-header .sidebar-toggle").css('paddingLeft','9em');
		$(".logo .logo-lg").show();
		$(".logo .logo-min").hide();
		$(".content").removeClass('content_L').addClass('content');
		mainObjs.$logoImg.attr("data-show","5");
		mainObjs.$wrapPage.load(baseUrl + $("#"+otherId).attr("myUrl"));
	}else if(showVal=="7"){
		$("#caseTypeMenu li span").hide();
		$(".aaaa").hide();
		$("#main_header > a").width(75);
		$(".l-menu").width(75);
		$("#caseTypeMenu a").width(75);
		$(".main-header .sidebar-toggle").css('paddingLeft','4em');
		$(".logo .logo-lg").hide();
		$(".logo .logo-min").show();
		$(".content").addClass('content_L');
		mainObjs.$logoImg.attr("data-show","8");
		mainObjs.$wrapPage.load(baseUrl + $("#"+casId).attr("myUrl"));
	}else if(showVal=="8"){
		$("#caseTypeMenu li span").show();
		$(".aaaa").show();
		$("#main_header > a").width(180);
		$(".l-menu").width(180);
		$("#caseTypeMenu a").width(180);
		$(".main-header .sidebar-toggle").css('paddingLeft','9em');
		$(".logo .logo-lg").show();
		$(".logo .logo-min").hide();
		$(".content").removeClass('content_L').addClass('content');
		mainObjs.$logoImg.attr("data-show","7");
		mainObjs.$wrapPage.load(baseUrl + $("#"+casId).attr("myUrl"));
	}else{
		$("#leftMenu").show();
		$("#menu").show();
		$("#otherMissionLeftMenu").hide();
		$(".content").removeClass('content_c').addClass('content');
		mainObjs.$logoImg.attr("data-show","1");
	}

	if(navFlg!="noEf"&&navFlg!=undefined){
		if(returnVal!=true){
			mainObjs.$wrapPage.load(baseUrl + myHomes.split("&")[0]);
		}else{
			mainObjs.$wrapPage.load(baseUrl + $("#menuUrl").data("menuUrl"));
		}
	}else{

		
	}
	
});



//获取点亮左边菜单的url
function getLeftMenuUrl(){
	var allLi = mainObjs.$menu.find('li');
	$.each(allLi,function(i,n){
//		var firstD ="";
		var currPic = $("#"+allLi[i].id).find('a > img').attr('src');
		var picPath = currPic.split(".png");
		var getImg = picPath[0].split("\/");
		var lastImgName = getImg[getImg.length-1];
		if($("#"+allLi[i].id).attr('style')!=undefined){
//			firstD = $("#"+allLi[i].id).attr('style').indexOf('background: rgb(30, 124, 251)');
			if(($("#"+allLi[i].id).attr('style').indexOf('background: rgb(30, 124, 251)')>=0
					||$("#"+allLi[i].id).attr('style').indexOf('background: rgb(30, 124, 251) none repeat scroll 0% 0%;')>=0)
					&&allLi[i].id!="menu_s"){
				if(lastImgName=="defect"){
					$("#menuUrl").data("menuUrl","/bugManager/bugManagerAction!loadAllMyBug.action");
				}else if(lastImgName=="projectMan"){
					$("#menuUrl").data("menuUrl","/outLineManager/outLineAction!index.action");
				}else if(lastImgName=="testSin"){
					$("#menuUrl").data("menuUrl","/caseManager/caseManagerAction!loadCase.action");
				}else if(lastImgName == "testcasepkg"){
					$("#menuUrl").data("menuUrl","/testCasePkgManager/testCasePackageAction!goTestCasePkgMain.action");
				}else if(lastImgName == "zl"){
					$("#menuUrl").data("menuUrl","/overview/overviewAction!loadInformation.action");
				}else{
					$("#menuUrl").data("menuUrl","/analysis/analysisAction!goAnalysisMain.action");
				}
				$("#homePageFlg").val("yes");
			}
		}
	});
	if($("#homePageFlg").val()!="yes"){
		return false;
	}else{
		return true;
	}
}


//移动效果
function hoverEvent(){
	mainObjs.$menu.find('li').hover(
	  function () {
//		if($("#adds").data('sty')!=0){
			var aPos =  $(this).children().filter('a[class!="childA"]');
			imgPath = aPos["0"].children["0"].attributes[1].nodeValue;
			picName = imgPath.split("/");
			var pngsp = imgPath.split("1.png");
			if(imgPath.indexOf("1")>=0){
				hover = pngsp[0]+".png";
				$("#adds").data('sty',0);	
			}else{
				hover = pngsp[0];
				$("#adds").data('sty',1);
			}
			$(this).css('background','#1E7CFB');
			aPos.find('img[src$="'+picName[5]+'"]').attr('src',hover);
			if(aPos["0"].children[2]!=undefined){
				aPos["0"].children[2].attributes[1].nodeValue=baseUrl+"/itest/images/homeImage/"+"upArr2.png";
			}
			aPos.find('span').css('color','#ffffff');
//			$("#imgs").data('img',imgPath);
//			$("#adds").data('sty',0);
//		}
	  },
	  function () {
		 if($("#adds").data('sty')!=1){
			 var outLi = $(this).children().filter('a[class!="childA"]');
			 $(this).css('background','#3d4351');
			 outLi["0"].children["0"].attributes[1].nodeValue = imgPath;
			 if(outLi["0"].children[2]!=undefined){
				outLi["0"].children[2].attributes[1].nodeValue=baseUrl+"/itest/images/homeImage/"+"upArr1.png";
			 }
			 outLi.find('span').css('color','#E9F6FF');
			 $("#adds").data('sty',0);
		 }
	  }
	);
}
function init() {
	var pFlag = $("#projectFlag").val();
	if(pFlag!="1"){
		$("#wrap-page").css('padding','0px');
		$("#noPro").show();
	}else{
		var homeUrl =$("#myhome").data("myHome");
		if(homeUrl!=""&&homeUrl!=undefined){
			//是否与项目有关，有关的话，查出项名名称
			if(homeUrl.indexOf("taskId") != -1){
				var taId = homeUrl.split("taskId=")[1].split("&")[0];
				$.post(baseUrl + "/singleTestTask/singleTestTaskAction!updInit.action",{
					"dto.singleTest.taskId":taId
				},function(data){
					$('#changePro').html("");
					if(data["dto.singleTest"].proName.length > 3){
						$('#changePro').html("项目测试 > "+data["dto.singleTest"].proName.substring(0,2)+"...");
					}else{
						$('#changePro').html("项目测试 > "+data["dto.singleTest"].proName);
					}
					$('#changePro').attr("title",data["dto.singleTest"].proName);
//					$('#changePro').css('color','#ffffff');
//					$('#changePro').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/switch1.png');
				},'json');
			}
			if(homeUrl.indexOf("otherMission") != -1){
				//跳转到设置的其他任务
				otherMissionFirstPage(homeUrl);
			}else{
				if(homeUrl.indexOf("loadAllMyBug.action")>=0){
					//缺陷管理
					$("#menu_107").attr('style','background: rgb(30, 124, 251);');
					$("#menu_107").find('img').removeAttr('src');
					$("#menu_107").find('img').attr('src',baseUrl+"/itest/images/homeImage/defect.png");
					$("#menu_107").find('span').css('color','#ffffff');
				}else if(homeUrl.indexOf("goAnalysisMain.action")>=0){
					//分析度量
					$("#menu_143").attr('style','background: rgb(30, 124, 251);');
					$("#menu_143").find('img').removeAttr('src');
					$("#menu_143").find('img').attr('src',baseUrl+"/itest/images/homeImage/analysis.png");
					$("#menu_143").find('span').css('color','#ffffff');
				}else if(homeUrl.indexOf("loadCase.action")>=0){
					//测试用例管理
					$("#menu_87").attr('style','background: rgb(30, 124, 251);');
					$("#menu_87").find('img').removeAttr('src');
					$("#menu_87").find('img').attr('src',baseUrl+"/itest/images/homeImage/testSin.png");
					$("#menu_87").find('span').css('color','#ffffff');
				}else if(homeUrl.indexOf("index.action")>=0){
					//测试需求管理
					$("#menu_68").attr('style','background: rgb(30, 124, 251);');
					$("#menu_68").find('img').removeAttr('src');
					$("#menu_68").find('img').attr('src',baseUrl+"/itest/images/homeImage/projectMan.png");
					$("#menu_68").find('span').css('color','#ffffff');
				}else if(homeUrl.indexOf("overviewAction")>=0){
					//总览
					$("#menu_13").attr('style','background: rgb(30, 124, 251);');
					$("#menu_13").find('img').removeAttr('src');
					$("#menu_13").find('img').attr('src',baseUrl+"/itest/images/homeImage/zl.png");
					$("#menu_13").find('span').css('color','#ffffff');
				}else /*if(homeUrl.indexOf("goTestCasePkgMain.action")>=0)*/{
					//缺陷管理
					$("#menu_11").attr('style','background: rgb(30, 124, 251);');
					$("#menu_11").find('img').removeAttr('src');
					$("#menu_11").find('img').attr('src',baseUrl+"/itest/images/homeImage/testcasepkg.png");
					$("#menu_11").find('span').css('color','#ffffff');
				}
				mainObjs.$wrapPage.load(baseUrl + homeUrl.split("&")[0]);
			}
		}else{
			$(".content").addClass('content_c');
			mainObjs.$wrapPage.load(baseUrl + "/itest/jsp/itestManual/itestManual.jsp");
		}
	}
	
	mainObjs.$menu.on('click', 'li', function(e) {
		var id = this.id;
		var functionId = $(this).attr('data-info');
		var url = mainObjs.$menu.data('menus')[id] ? mainObjs.$menu.data('menus')[id].url : null;
		$("#dataUrl").data("dataUrl",id+"^"+$("#"+id).find('img').attr('src')+"^"+url);
		
		if($("#outlineState").val()!="1"){//未提交测试需求
			if(id=="menu_107"||id=="menu_87"||id=="menu_143"||id=="menu_11"){
				var homepa_s = "";
				var clickData = $("#dataUrl").data("dataUrl").split("^");
				if($("#myhome").data("myHome")!=undefined){
					homepa_s = $("#myhome").data("myHome").split("&");
					if(homepa_s[0]==clickData[2]){
						$("#"+clickData[0]).css('background',' rgb(30, 124, 251);');
						$("#"+clickData[0]).find('img').attr('src',hover);
						$("#"+clickData[0]).find('span').css('color','#ffffff');
					}
					$.xalert({title:'提示',msg:'因测试需求没提交，请提交测试需求!'});
					return;
				}else{
					$.xalert({title:'提示',msg:'因测试需求没提交，请提交测试需求!'});
					return;
				}
			}
		}
		
		
			$("#"+id).css('background',' rgb(30, 124, 251);');
			$("#"+id).find('img').attr('src',hover);
			$("#"+id).find('span').css('color','#ffffff');
			$("#adds").data('sty',0);
			if(id=="menu_s"){
				setSelfHomePage();
			}
			
			var allLi = mainObjs.$menu.find('li');
			$.each(allLi,function(i,n){
				var firstD ="";
				var currPic = $("#"+allLi[i].id).find('a > img').attr('src');
				var picPath = currPic.split(".png");
				if($("#"+allLi[i].id).attr('style')!=undefined){
					firstD = $("#"+allLi[i].id).attr('style').indexOf('background: rgb(30, 124, 251)');
					if(firstD>=0){
						if(id!=allLi[i].id){
							$("#"+allLi[i].id).attr('style','');
							$("#"+allLi[i].id).find('img').attr('src',picPath[0]+"1.png");
							$("#"+allLi[i].id).find('span').css('color','#E9F6FF');
						}
					$("#adds").data('sty',1);
					}
				}
				
			});		
			
		if(url=="/userManager/userManagerAction!setMyInfoInit.action"){
			showAddWin01();
			return;
		}
		if (url) {
			destoryEle();
			mainObjs.$wrapPage.load(baseUrl + url);
		}
	});
//	mainObjs.$menu.find('li').bind( "hover" );
}

//摧毁页面残余元素
function destoryEle(){
	var destroyEle = $('body>div.window>div.window-body').not("[id='caseItemWin']").not('div[id="addOrEditWinUserInfo"]');
	//destroyEle = $('body>div.window>div.window-body').not('div[id="addOrEditWin"]');
//	$('div[class=".panel .panel-default .window .panel-htop"]').not("[id='caseItemWin']").xwindow('destroy');
	$.each(destroyEle,function(i,v){
		$("#"+v.id).parent().next().remove();
		$("#"+v.id).parent().remove();
	});
//	$('body>div.window>div.window-body').xwindow().not("[id='caseItemWin']").xwindow('destroy');
}



function dgResize() {
	clearTimeout(timer);
	var timer = setTimeout(function() {
			$('.panel-body', $(".wrapper")).panel('resize');
		}, 301);
}
function showAddWin01(){
	pwdFlag01='0';
	oldPwdFlag02='0';
	mainObjs.$addOrEditForm.xform('clear');
	mainObjs.$addOrEditWin.parent().css("border","none");
	mainObjs.$addOrEditWin.prev().css({ color: "#ffff", background: "#101010" });
	loadMyInfo01();
	mainObjs.$addOrEditWin.xwindow('setTitle','修改个人信息').xwindow('open');
	var pss = document.getElementById("password");
	$(pss).next().children().change(function(){
		pwdFlag01='1';
	});
	var pss1 = document.getElementById("rePwd01");
	$(pss1).next().children().change(function(){
		$("#oldPwd").xtextbox('setValue','');
		
	});
	
	var pss2 = document.getElementById("oldPwd");
	$(pss2).next().children().change(function(){
		oldPwdFlag02='1';
	});
	
}
function closeWin01(){
	mainObjs.$addOrEditWin.xwindow('close');
}
function submitUserInfo(){
	var objData = mainObjs.$addOrEditWin.xserialize(); 
	if(!objData["dto.user.loginName"]){ 
		tipPermission("请输入登陆号！");
        return;
	} 
	if(!objData["dto.user.name"]){ 
		tipPermission("请输入真实姓名！");
        return;
	}
	if(!objData["dto.user.password"]){ 
		tipPermission("请输入密码！");
        return;
	}
	if(!objData["repwd"]){ 
		tipPermission("请确认密码！");
        return;
	} 
	if(!objData["dto.user.oldPwd"]){ 
		tipPermission("请输入原始密码！");
        return;
	} 
	if(objData["repwd"]==objData["dto.user.password"]&&objData["dto.user.password"]!=""){
		 //upd 
		if(pwdFlag01=='1'){
			objData["dto.user.chgPwdFlg"]=$("#accountId").text();
		}
		if(pwdFlag01=='0'&&oldPwdFlag02=='1'){
			objData["dto.user.chgPwdFlg"]='2';
		}
		if(pwdFlag01=='0'&&oldPwdFlag02=='0'){
			objData["dto.user.chgPwdFlg"]="";
		}
		$.post(  
				baseUrl + "/userManager/userManagerAction!updateMyInfo.action?dto.isAjax=true",
				objData,
				function(data) { 
					if(data.indexOf("success")>-1){
//						$.xalert({title:'修改成功',msg:'修改用户成功！'});
						tipPermission("修改用户成功！");
						mainObjs.$addOrEditWin.xwindow('close');
						//mainObjs.$testTaskDg.xdatagrid('reload');  
					}else if(data.indexOf("原密码不正确")>-1){
//						$.xalert({title:'修改失败',msg:'原密码不正确！'});
						tipPermission("原密码不正确！");
					}else if(data.indexOf("existed")>-1){
						tipPermission("该登陆名已存在！"); 
					}else{
//						$.xalert({title:'修改失败',msg:'修改用户失败！'});
						tipPermission("修改用户失败！");
					}
				},"text"
			);	
//		$.xalert({title:'验证失败',msg:'两次输入的密码不一致！'});
		
	}else{
		tipPermission("两次输入的密码不一致！！");
        return
	}
	
}
function loadMyInfo01(){
	$.getJSON(
			baseUrl + "/userManager/userManagerAction!setMyInfoInitLoad.action",
			{},       
			function(data) {
				mainObjs.$addOrEditWin.xdeserialize(data);
				$("#oldPwd").textbox("setValue", data["dto.user"].password);
				$("#rePwd01").textbox("setValue",data["dto.user"].password);
				}); 
	
}
function resetInfo01(){
	mainObjs.$addOrEditForm.xform('clear');
	$.getJSON(
			baseUrl + "/userManager/userManagerAction!setMyInfoInitLoad.action",
			{},       
			function(data) {
				mainObjs.$addOrEditWin.xdeserialize(data);
				$("#oldPwd").textbox("setValue", data["dto.user"].password);
				$("#rePwd01").textbox("setValue",data["dto.user"].password);
				}); 
}

//切换项目
function caseItems(){
	destoryEle();
	//
	taskFlg = 0;
	sysTestFlg = 1;
	sysDieFlg = 0;
	sysConfigFlg = 0;
	$('#iterL').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/iterList.png');
//	var urls = baseUrl+'/itest/json/project.json';
	var menuAuthUrl =  menus['menu_13'];
	var homeUrls = $("#loginData").data("loginData")["dto.user"].myHome;
	if(homeUrls!=undefined&&homeUrls!=""){
		var urlSplitR = homeUrls.split('&')[0];
		if(privilegeMap[urlSplitR]!="1" && homeUrls.indexOf("otherMission") == -1){
			tipPermission('请联系管理员增加总览权限！');
			return;
		}else{
			//$("#caseItemWin").window("vcenter");
			if(haveJoinProject()){
				$("#caseItemWin").xwindow('open');
			}else{
				var imgPath = baseUrl+'/itest/images/homeImage/noProject.png';
//				$("#caseItemWin").xwindow('close');
//				$('.manualPage').hide();
//				$('#noPro').show();
				$("#otherMissionLeftMenu").hide();
				$('#caseTypeMenu').hide();
				$("#leftMenu").show();
				$('#menu').show();
				$('#menu').addClass("disabled");
				$("#wrap-page").find('.manualPage').remove();
				$('#wrap-page').empty();
				$("#wrap-page").append('<div id=\"noPro\" style=\"background-color:#F6F6F6;height:100%;\">'
						+'<img alt=\"\" src='+imgPath+' style=\"margin: 19% 42%;\">'
						+'<span style=\"font-size: 22px;font-weight: bold;float: right;margin: -15% 10%;\">您目前还没有参与任何测试项目，被设置到测试项目的流程才被视为参与了该项目</span></div>');
				mainObjs.$menu.parent().attr('style','display:block;');
				/*$("#wrap-page").css('padding','0px');*/
				$(".content").removeClass('content_c').addClass('content');
				//加载新用户，没参与项目
//				var htmls = newUserLoadMenu();
//				mainObjs.$menu.append(htmls);
				return;
			}
		}
	}else{
		if(menuAuthUrl!=undefined){
			//$("#caseItemWin").window("vcenter");
			if(haveJoinProject()){
				$("#caseItemWin").xwindow('open');
			}else{
				var imgPath = baseUrl+'/itest/images/homeImage/noProject.png';
//				$("#caseItemWin").xwindow('close');
//				$('.manualPage').hide();
//				$('#noPro').show();
				$("#otherMissionLeftMenu").hide();
				$('#caseTypeMenu').hide();
				$("#leftMenu").show();
				$('#menu').show();
				$('#menu').addClass("disabled");
				$("#wrap-page").find('.manualPage').remove();
				$('#wrap-page').empty();
				$("#wrap-page").append('<div id=\"noPro\" style=\"background-color:#F6F6F6;height:100%;\">'
						+'<img alt=\"\" src='+imgPath+' style=\"margin: 19% 42%;\">'
						+'<span style=\"font-size: 22px;font-weight: bold;float: right;margin: -15% 10%;\">您目前还没有参与任何测试项目，被设置到测试项目的流程才被视为参与了该项目</span></div>');
				mainObjs.$menu.parent().attr('style','display:block;');
				/*$("#wrap-page").css('padding','0px');*/
				$(".content").removeClass('content_c').addClass('content');
				//加载新用户，没参与项目
//				var htmls = newUserLoadMenu();
//				mainObjs.$menu.append(htmls);
				return;
			}
		}else{
			tipPermission('请联系管理员增加总览权限！');
			return;
		}
	}
	var urls = baseUrl + '/singleTestTask/singleTestTaskAction!swTestTaskList.action?dto.operCmd=caseList';
	$("#caseItemList").xdatagrid({
		url:urls,
		method: 'get',
		height: mainObjs.tableHeight,
		fitColumns:true,
		striped:true,
		singleSelect:true,
		checkOnSelect:true,
		scrollbarSize:100,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		pageList: [15,30,50,100],
		layout:['list','first','prev','manual', "sep",'next','last','refresh','info'],
		rowStyler: function(index,row){
			if (row.status=='0'){
//				return 'background-color:#bddecc;color:#fff;font-weight:bold;';
//				return 'background-color:#e5fff1;';
			}
		},
		onLoadSuccess:function(data){
			if(data.total>0){
				if($("#projectFlag").val()=="0"){
					$("#projectFlag").val("1");
					$("#menu").empty();
					loadLeftMenus();
				}
				/*var menuAuthUrl =  menus['menu_13'];
				var homeUrls = $("#loginData").data("loginData")["dto.user"].myHome;*/
				/*if(homeUrls!=undefined&&homeUrls!=""){
					var urlSplitR = homeUrls.split('&')[0];
					if(privilegeMap[urlSplitR]!="1" && homeUrls.indexOf("otherMission") == -1){
						tipPermission('请联系管理员增加总览权限！');
					}else{
						$("#caseItemWin").xwindow("vcenter");
						$("#caseItemWin").xwindow('open');
					}
				}else{
					if(menuAuthUrl!=undefined){
						$("#caseItemWin").xwindow("vcenter");
						$("#caseItemWin").xwindow('open');
					}else{
						tipPermission('请联系管理员增加总览权限！');
					}
				}*/
				$("#caseItemWin").window("vcenter");
//				$("#caseItemWin").window("vcenter");
//				$("#caseItemWin").xwindow('open');
			}else{
				var imgPath = baseUrl+'/itest/images/homeImage/noProject.png';
//				$("#caseItemWin").xwindow('close');
//				$('.manualPage').hide();
//				$('#noPro').show();
				$("#otherMissionLeftMenu").hide();
				$('#caseTypeMenu').hide();
				$("#leftMenu").show();
				$('#menu').show();
				$('#menu').addClass("disabled");
				$("#wrap-page").find('.manualPage').remove();
				$('#wrap-page').empty();
				$("#wrap-page").append('<div id=\"noPro\" style=\"background-color:#F6F6F6;height:100%;\">'
						+'<img alt=\"\" src='+imgPath+' style=\"margin: 19% 42%;\">'
						+'<span style=\"font-size: 22px;font-weight: bold;float: right;margin: -15% 10%;\">您目前还没有参与任何测试项目，被设置到测试项目的流程中才被视为参与了该项目</span></div>');
				mainObjs.$menu.parent().attr('style','display:block;');
				/*$("#wrap-page").css('padding','0px');*/
				$(".content").removeClass('content_c').addClass('content');
				//加载新用户，没参与项目
//				var htmls = newUserLoadMenu();
//				mainObjs.$menu.append(htmls);
			}
			//空循环1000次， 解有时候 resize 不好使的问题
			var i =1;
			for(;i<1000;i++){
				
			}
			$("#caseItemList").xdatagrid('resize');
		},
		onClickRow:function(rowIndex, rowData){
			var dataU = $("#dataUrl").data('dataUrl');
			$("#leftMenu").show();
			$(".sidebary").show();
			$("#taksIdmain").val(rowData.taskId);
			$("#outlineState").val(rowData.outlineState);
			//放taskId在session
			/*putTaskIdSession(rowData.taskId);*/

			//分析度量
			//将分析度量页面数据放入session
			$("#analyitemId").val(rowData.taskId); 
			$("#analyprojectName").val(rowData.proName); 
			$("#analyproNum").val(rowData.proNum); 
			putAnalysisSession(rowData.taskId,rowData.proName,rowData.proNum);
			$("#addMenus").css('marginLeft','5em');
			
			if($('#leftMenu').width()=='75'){
				$('#menu li span').hide();
				mainObjs.$logoImg.attr("data-show","2");
			}else{
				mainObjs.$logoImg.attr("data-show","1");
			}
		
			$('#changePro').html("");
			if(rowData.proName.length > 3){
				$('#changePro').html("项目测试 > "+rowData.proName.substring(0,2)+"...");
			}else{
				$('#changePro').html("项目测试 > "+rowData.proName);
			}
			$('#changePro').attr("title",rowData.proName);
			$('#changePro').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/switch1.png');
			$('#changePro').css('color','#ffffff');
			$(".l-menu").show();
			$(".content").removeClass('content_c').addClass('content');
			
			$("#menu").show();
			$("#otherMissionLeftMenu").hide();
			$("#caseTypeMenu").hide();
			$("#systemManage").css("color","#e9f6ff");
			$("#systemManage").html("系统设置");
			$("#otherMissionssss").css("color","#e9f6ff");
			$("#otherMissionImg").attr('src',baseUrl+'/itest/images/homeImage/elseTs.png');
			$("#systemM").css("margin","20px 0 -5px");
			
			
			//切换系统管理的效果
			systemManX();
			if(dataU!=undefined&&dataU.indexOf('menu_s')<0){
				//有值,定位到用例管理
				exampleManager(rowData,dataU);
			}else{
				//无值,根据选择的项目定位到缺陷管理
				defectManager(rowData);
			}
		},
		columns:[[
			{field:'proNum',title:'项目编号',width:'20%',align:'center'},
			{field:'proName',title:'项目名称',width:'30%',align:'left',halign:'center'},
//			{field:'devDept',title:'研发部门',width:'8%',align:'left',halign:'center'},
			//{field:'testPhase',title:'测试阶段',width:'20%',align:'center'},
			{field:'psmName',title:'项目PM',width:'25%',align:'left',halign:'center'},
//			{field:'planStartDate',title:'开始日期',width:'10%',align:'center'},
//			{field:'planEndDate',title:'结束日期',width:'10%',align:'center'},
//			{field:'planDocName',title:'测试计划',width:'7%',align:'center',formatter:planDocNameFormat},
			{field:'status',title:'状态',width:'10%',align:'center',formatter:statusFormats},
			{field:'testLdVos',title:'测试负责人',width:'28.4%',align:'left',formatter:testerFormat}
		]],
	});
}
//判断是否参与项目
function haveJoinProject(){
	$.ajaxSettings.async = false;
	var projectHaveFlag = false;
	$.post(baseUrl + '/singleTestTask/singleTestTaskAction!swTestTaskList.action?dto.operCmd=caseList',null,function(data){
		if(data.rows.length > 0){
			projectHaveFlag = true;
		}else{
			projectHaveFlag = false;
		}
	},'json');
	return projectHaveFlag;
}

function testerFormat(value,row,index) { 
	var html = new Array();
	if (row.testLdVos!=null&&row.testLdVos.length) {
		for (var i = 0; i < row.testLdVos.length; i++) {
			html.push(row.testLdVos[i].name);
		}
	}
	return html.join(',');
}

//放taskId在session
function putTaskIdSession(taskId){
	var urls = baseUrl + '/singleTestTask/singleTestTaskAction!putTaskIdSession.action?dto.isAjax=true';
	$.ajax({
		  url: urls,
		  cache: false,
		  async: false,
		  type: "POST",
//		  dataType:"json",
		  dataType:"text",
		  data: {
			  	"dto.singleTest.taskId":taskId
			  	},
		  success: function(data){
			  if(data.indexOf("success")>=0){
//				  $("#menu_s")[0].attributes[4].nodeValue="";
//				  $("#menu_s > a").find('img').attr('src',baseUrl+"/itest/images/homeImage/myHome1.png");
//				  $("#menu_s > a span").css("color","#E9F6FF");
//				  $("#"+id).find('img').attr('src',baseUrl+"/itest/images/homeImage/defect.png");
//				  $("#"+id).find('span').css("color","#ffffff");
//				  $("#"+id)[0].attributes[3].nodeValue="background: rgb(30, 124, 251);";
//				  mainObjs.$wrapPage.load(baseUrl + url);
			  }else{
//				  $.xnotify('系统问题！', {type:'warning'});
				  tipPermission("系统问题！");
			  }
		  }
		});
}

//将分析度量页面数据放入session
function putAnalysisSession(taskId,proName,proNum){
	var urls = baseUrl + '/analysis/analysisAction!putAnanysisSession.action?dto.isAjax=true';
	$.ajax({
		  url: urls,
		  cache: false,
		  async: false,
		  type: "POST",
		  dataType:"text",
		  data: {
			  	"analysisDto.taskId":taskId,
			  	"analysisDto.analyProjectName":proName,
			  	"analysisDto.analyProNum":proNum
			  	},
		  success: function(data){
			  if(data.indexOf("success")>=0){
			  }else{
//				  $.xnotify('系统问题！', {type:'warning'});
				  tipPermission("系统问题！");
			  }
		  }
		});
}

function statusFormats(value,row,index) {
	switch (value) {
	case 0:
		return '进行中';
	case 1:
		return '完成';
	case 2:
		return '结束';
	case 3:
		return '准备';
	case 5:
		return '暂停';
	case 6:
		return '终止';
	default:
		return '-';
	}
}

//定位到用例管理
function exampleManager(datas,url){
	$("#taksIdmain").val(datas.taskId);
	$("#caseItemWin").xwindow('close');
	var urlSplit = url.split("^");
	if($("#outlineState").val()!='0'){
		$("#"+urlSplit[0]).css('background','#1e7cfb');
		$("#"+urlSplit[0]).siblings().css('background','#3d4351');
		$("#"+urlSplit[0]).find('img').attr('src',urlSplit[1]);
		$("#"+urlSplit[0]).find('span').css('color','#ffffff');
		mainObjs.$wrapPage.load(baseUrl + urlSplit[2]);
	}else{
		$.xalert({title:'提示',msg:'因测试需求没提交，请提交测试需求!',okFn:function(){
			$("#"+urlSplit[0]).css('background','');
			$("#"+urlSplit[0]).find('img').attr('src',baseUrl+"/itest/images/homeImage/defect1.png");
			$("#"+urlSplit[0]).find('span').css('color','#E9F6FF');
			
			$("#menu_68").css('background','#1e7cfb');
			$("#menu_68").find('img').attr('src',baseUrl+"/itest/images/homeImage/projectMan.png");
			$("#menu_68").find('span').css('color','#ffffff');
			mainObjs.$wrapPage.load(baseUrl + "/outLineManager/outLineAction!index.action");
		}});
	}
}

//定位到缺陷管理里
function defectManager(data){
	$("#caseItemWin").xwindow('close');
	var home_Urls = $("#myhome").data("myHome");
	if(home_Urls!=undefined  && home_Urls.indexOf("otherMission") == -1){
		if(home_Urls.indexOf("loadAllMyBug.action")>=0){
			//缺陷管理
			$("#menu_107").attr('style','background: rgb(30, 124, 251);');
			$("#menu_107").find('img').removeAttr('src');
			$("#menu_107").find('img').attr('src',baseUrl+"/itest/images/homeImage/defect.png");
			$("#menu_107").find('span').css('color','#ffffff');
		}else if(home_Urls.indexOf("goAnalysisMain.action")>=0){
			//分析度量
			$("#menu_143").attr('style','background: rgb(30, 124, 251);');
			$("#menu_143").find('img').removeAttr('src');
			$("#menu_143").find('img').attr('src',baseUrl+"/itest/images/homeImage/analysis.png");
			$("#menu_143").find('span').css('color','#ffffff');
		}else if(home_Urls.indexOf("loadCase.action")>=0){
			//测试用例管理
			$("#menu_87").attr('style','background: rgb(30, 124, 251);');
			$("#menu_87").find('img').removeAttr('src');
			$("#menu_87").find('img').attr('src',baseUrl+"/itest/images/homeImage/testSin.png");
			$("#menu_87").find('span').css('color','#ffffff');
		}else if(home_Urls.indexOf("index.action")>=0){
			//测试需求管理
			$("#menu_68").attr('style','background: rgb(30, 124, 251);');
			$("#menu_68").find('img').removeAttr('src');
			$("#menu_68").find('img').attr('src',baseUrl+"/itest/images/homeImage/projectMan.png");
			$("#menu_68").find('span').css('color','#ffffff');
		}else if(home_Urls.indexOf("overviewAction")>=0){
			//总览
			$("#menu_13").attr('style','background: rgb(30, 124, 251);');
			$("#menu_13").find('img').removeAttr('src');
			$("#menu_13").find('img').attr('src',baseUrl+"/itest/images/homeImage/zl.png");
			$("#menu_13").find('span').css('color','#ffffff');
		}else /*if(homeUrl.indexOf("goTestCasePkgMain.action")>=0)*/{
			//缺陷管理
			$("#menu_11").attr('style','background: rgb(30, 124, 251);');
			$("#menu_11").find('img').removeAttr('src');
			$("#menu_11").find('img').attr('src',baseUrl+"/itest/images/homeImage/testcasepkg.png");
			$("#menu_11").find('span').css('color','#ffffff');
		}
		mainObjs.$wrapPage.load(baseUrl +$("#myhome").data("myHome").split("&")[0]);
	}else{
		$("#menu_13").css('background','#1e7cfb');
		$("#menu_13").find('img').attr('src',baseUrl + '/itest/images/homeImage/zl.png');
		$("#menu_13").find('span').css('color','#ffffff');
//		mainObjs.$wrapPage.load(baseUrl + "/bugManager/bugManagerAction!loadAllMyBug.action?dto.allTestTask=true");
		mainObjs.$wrapPage.load(baseUrl + "/overview/overviewAction!loadInformation.action");
	}
	
}

//logo跳到主页去
function logoEvent(){
	$("#leftMenu").hide();
	$(".sidebary").hide();
	
	$(".content").addClass('content_c');
	mainObjs.$wrapPage.load(baseUrl + "/itest/jsp/itestManual/itestManual.jsp");
}



function bindSystemMenu(){
	var sysMenus = $(".systemM li");
	if(sysMenus.length>=0){
		for(var i=0; i<sysMenus.length;i++){
			sysMenus[i].addEventListener("click",function(){
				var targetId = this.id;
				var id = targetId.substr(7);
				var targetUrl = systemMenusArr[id].url;
				var functionName = systemMenusArr[id].functionName;
				$("#systemM").css('marginLeft','2.5em');
				$('#changePro').html("项目测试");
				$('#changePro').removeAttr("title");
				$('#changePro').css('color','#E9F6FF');
				$("#otherMissionssss").css("color","#e9f6ff");
				$("#otherMissionImg").attr('src',baseUrl+'/itest/images/homeImage/elseTs.png');
				$('#iterL').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/iterList.png');
				$("#addMenus").css("margin","20px 0 -5px");
				$('#changePro').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/switch.png');
				$("#systemMenu > a").find('span[class="switchSt"] label').html("系统设置 > "+functionName);
				$("#systemMenu > a").find('span[class="switchSt"] label').css('color','#ffffff');
				$("#systemMenu > a").find('span[class="switchSt"]').prev().attr('src',baseUrl + '/itest/images/homeImage/systemM1.png');
				if (targetUrl) {
					$(".sidebary").hide();
					$(".l-menu").hide();
					$(".content").addClass('content_c');
					destoryEle();
					taskFlg = 0;
					sysTestFlg = 0;
					sysDieFlg = 0;
					sysConfigFlg = 1;
					mainObjs.$wrapPage.load(baseUrl + targetUrl);
					mainObjs.$logoImg.attr("data-show","3");
				}
			});
		}
	}
	
}

//点击迭代列表
function switchToDiedai(){
/*$("#iteration").click(function(){*/
	//removeLeftMenuEffect();
	$(".sidebary").hide();
	$("#leftMenu").hide();
//	$(".l-menu").hide();
//	var e = event || window.event;
	
	
	setTimeout(function(){ 
		mainObjs.$wrapPage.load(baseUrl + "/iteration/iterationAction!iterationList.action",null,function(){
//	        var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
//	        var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
//	        var x = e.pageX || e.clientX + scrollX;
//	        var y = e.pageY || e.clientY + scrollY;
//	        //根据坐标移动鼠标位置
//	        moveMousePostion(x,y);
//			$("#iteration").focus();
			$("#changePro").html("项目测试");
			$('#changePro').removeAttr("title");
			$("#changePro").css("color","#e9f6ff");
			$("#changePro").parent().prev().attr("src",baseUrl + '/itest/images/homeImage/switch.png');
			
			$("#systemManage").html("系统设置");
			$("#systemManage").css("color","#e9f6ff");
			$("#systemManage").parent().prev().attr("src",baseUrl + '/itest/images/homeImage/systemM.png');
			
			$("#otherMissionssss").html("项目任务");
			$("#otherMissionssss").css("color","#e9f6ff");
			$("#otherMissionssss").prev().attr("src",baseUrl + '/itest/images/homeImage/elseTs.png');
		});
	},1000);
	
	$(".content").addClass('content_c');
	$('#iterL').css('color','#ffffff');
	$('#iterL').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/iterList1.png');
	destoryEle();
	mainObjs.$logoImg.attr("data-show","3");
	taskFlg = 0;
	sysTestFlg = 0;
	sysDieFlg = 1;
	sysConfigFlg = 0;
	$("#navFlag").data("navFlag","itera");
	
/*});*/
}

//根据坐标移动鼠标位置


//去掉左边菜单点亮的效果
function removeLeftMenuEffect(){
	var allLi = mainObjs.$menu.find('li');
	$.each(allLi,function(i,n){
		var firstD ="";
		var currPic = $("#"+allLi[i].id).find('a > img').attr('src');
		var picPath = currPic.split(".png");
		if($("#"+allLi[i].id).attr('style')!=undefined){
			firstD = $("#"+allLi[i].id).attr('style').indexOf('background: rgb(30, 124, 251)');
			if(firstD>=0){
				if(id!=allLi[i].id){
					$("#"+allLi[i].id).attr('style','');
					$("#"+allLi[i].id).find('img').attr('src',picPath[0]+"1.png");
					$("#"+allLi[i].id).find('span').css('color','#E9F6FF');
				}
				$("#navTop").data('effect',"noEf");
			}
		}
		
	});
}


//点击测试项目管理
function testItems(){
	//去掉左边菜单点亮的效果
	//removeLeftMenuEffect();
	$("#menu").show();
	$(".sidebary").hide();
	$("#otherMissionLeftMenu").hide();
	$("#caseTypeMenu").hide();
	$("#systemManage").css("color","#e9f6ff");
	$("#systemManage").html("系统设置 ");
	$("#systemM").css("margin","20px 0 -5px");
	$("#otherMissionssss").css("color","#e9f6ff");
	$("#otherMissionImg").attr('src',baseUrl+'/itest/images/homeImage/elseTs.png');
	$(".l-menu").hide();
	$(".content").addClass('content_c');
	$("#addMenus").css('marginLeft','5em');
	$('#changePro').html("");
	$('#changePro').html("项目测试 > 测试项目维护");
	$('#changePro').removeAttr("title");
	$('#changePro').css('color','#ffffff');
	$('#changePro').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/switch1.png');
	
	$('#iterL').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/iterList.png');
	//切换系统管理的效果
	systemManX();
	destoryEle();
	setTimeout(function(){ 
		mainObjs.$wrapPage.load(baseUrl + "/singleTestTask/singleTestTaskAction!magrTaskList.action");
	},1000);
	taskFlg = 0;
	sysTestFlg = 1;
	sysDieFlg = 0;
	sysConfigFlg = 0;
	mainObjs.$logoImg.attr("data-show","3");
}
//点击测试用例库
function testLibrary(){
	$("#menu").hide();
	$("#otherMissionLeftMenu").hide();
	$("#caseTypeMenu").show();
	$("#systemManage").css("color","#e9f6ff");
	$("#systemManage").html("系统设置 ");
	$("#systemM").css("margin","20px 0 -5px");
	$("#otherMissionssss").css("color","#e9f6ff");
	$("#otherMissionImg").attr('src',baseUrl+'/itest/images/homeImage/elseTs.png');
	$(".l-menu").show();
	$(".content").removeClass('content_c').addClass('content');
	$("#addMenus").css("margin","20px 0 -5px");
	$('#changePro').html("");
	$('#changePro').html("项目测试 > 测试用例库");
	$('#changePro').removeAttr("title");
	$('#changePro').css('color','#ffffff');
	$('#changePro').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/switch1.png');
	
	$('#iterL').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/iterList.png');
	//切换系统管理的效果
	systemManX();
	destoryEle();
	if($("#isAdmin").text() == "2" || $("#isAdmin").text() == "1"){
		$("#l1").css("background","rgb(30, 124, 251) none repeat scroll 0% 0%");
		if(casId){
			$("#"+casId).children().children().attr("src",$("#"+casId).children().children().attr("src").split(".png")[0]+"1.png");
		}
		if($("#l1").children().children().attr("src").split("1").length > 1){
			$("#l1").children().children().attr("src",$("#l1").children().children().attr("src").split("1")[0]+".png");
		}
		$("#l1").siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
		mainObjs.$wrapPage.load(baseUrl + "/testLibrary/testLibraryAction!loadTree.action");
		//跳转测试用例库时，将data-show设为7，用于区分左边菜单收缩效果
		mainObjs.$logoImg.attr("data-show","7");
		taskFlg = 0;
		sysTestFlg = 1;
		sysDieFlg = 0;
		sysConfigFlg = 0;
		casId = "l1";
	}else{
		$("#l2").css("background","rgb(30, 124, 251) none repeat scroll 0% 0%");
		if(casId){
			$("#"+casId).children().children().attr("src",$("#"+casId).children().children().attr("src").split(".png")[0]+"1.png");
		}
		if($("#l2").children().children().attr("src").split("1").length > 1){
			$("#l2").children().children().attr("src",$("#l2").children().children().attr("src").split("1")[0]+".png");
		}
		$("#l2").siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
		mainObjs.$wrapPage.load(baseUrl + "/testLibrary/testLibraryAction!caseLook.action");
		//跳转测试用例库时，将data-show设为7，用于区分左边菜单收缩效果
		mainObjs.$logoImg.attr("data-show","7");
		taskFlg = 0;
		sysTestFlg = 1;
		sysDieFlg = 0;
		sysConfigFlg = 0;
		casId = "l2";
	}
}
//切换系统管理的效果
function systemManX(){
	$("#systemMenu > a").find('span[class="switchSt"] label').html("系统设置 ");
	$("#systemMenu > a").find('span[class="switchSt"] label').css('color','#E9F6FF');
	$("#systemMenu > a").find('span[class="switchSt"]').prev().attr('src',baseUrl + '/itest/images/homeImage/systemM.png');
}
	
//注销
function exitLogin(){
	$.xconfirm({
		msg:'您确定退出吗?',
		okFn: function() {
			window.parent.location.href= baseUrl + "/login.htm";
		}
	});
}
//跳转到其他任务页面
function switchToOtherMission(){

	$(".l-menu").show();
	$(".content").removeClass('content_c').addClass('content');
	//跳转到其他任务时，将data-show设为5，用于区分左边菜单收缩效果
	mainObjs.$logoImg.attr("data-show","5");
	taskFlg = 1;
	sysTestFlg = 0;
	sysDieFlg = 0;
	sysConfigFlg = 0;
	$("#menu").hide();
	$("#otherMissionLeftMenu").show();
	$("#caseTypeMenu").hide();
	$("#otherMissionssss").css("color","#ffffff");
	$("#otherMissionImg").attr('src',baseUrl+'/itest/images/homeImage/elseTs1.png');
	$("#changePro").css("color","#e9f6ff");
	$("#changePro").html("项目测试");
	$('#changePro').removeAttr("title");
	$("#addMenus").css("margin","20px 0 -5px");
	$("#systemM").css("margin","20px 0 -5px");
	$("#systemManage").css("color","#e9f6ff");
	$("#systemManage").html("系统设置 ");
	$('#iterL').css('color','#ffffff');
	$('#iterL').parent().prev().attr('src',baseUrl + '/itest/images/homeImage/iterList.png');
	$("#changePro").html("项目测试");
	$('#changePro').removeAttr("title");
	$("#changePro").css("color","#e9f6ff");
	$("#changePro").parent().prev().attr("src",baseUrl + '/itest/images/homeImage/switch.png');
	
	$("#systemManage").html("系统设置");
	$("#systemManage").css("color","#e9f6ff");
	$("#systemManage").parent().prev().attr("src",baseUrl + '/itest/images/homeImage/systemM.png');
	$("#m1").css("background","rgb(30, 124, 251) none repeat scroll 0% 0%");
	if(otherId){
		$("#"+otherId).children().children().attr("src",$("#"+otherId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	if($("#m1").children().children().attr("src").split("1").length > 1){
		$("#m1").children().children().attr("src",$("#m1").children().children().attr("src").split("1")[0]+".png");
	}
	$("#m1").siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	mainObjs.$wrapPage.load(baseUrl + "/otherMission/otherMissionAction!overview.action");
	otherId = "m1";
}
//跳转到看板页面
function overview(obj){
	$(obj).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
	$(obj).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	if(otherId != obj.id){
		$("#"+otherId).children().children().attr("src",$("#"+otherId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	mainObjs.$wrapPage.load(baseUrl + "/otherMission/otherMissionAction!overview.action");
	otherId = obj.id;
}
//跳转到我负责的其他任务页面
function toMeCharge(obj){
	$(obj).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
	$(obj).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	if(otherId != obj.id){
		$("#"+otherId).children().children().attr("src",$("#"+otherId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	mainObjs.$wrapPage.load(baseUrl + "/otherMission/otherMissionAction!toMeCharge.action");
	otherId = obj.id;
}
//跳转到我参与的其他任务页面
function toMeJoin(obj){
	$(obj).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
	$(obj).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	if(otherId != obj.id){
		$("#"+otherId).children().children().attr("src",$("#"+otherId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	mainObjs.$wrapPage.load(baseUrl + "/otherMission/otherMissionAction!toMeJoin.action");
	otherId = obj.id;
}
//跳转到我创建的其他任务页面
function toMeCreate(obj){
	$(obj).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
	$(obj).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	if(otherId != obj.id){
		$("#"+otherId).children().children().attr("src",$("#"+otherId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	mainObjs.$wrapPage.load(baseUrl + "/otherMission/otherMissionAction!otherMissionList.action");
	otherId = obj.id;
}
//跳转到我关注的其他任务页面
function toMeConcern(obj){
	$(obj).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
	$(obj).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	if(otherId != obj.id){
		$("#"+otherId).children().children().attr("src",$("#"+otherId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	mainObjs.$wrapPage.load(baseUrl + "/otherMission/otherMissionAction!toMeConcern.action");
	otherId = obj.id;
}
//跳转到所有其他任务页面
function allMission(obj){
	$(obj).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
	$(obj).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	if(otherId != obj.id){
		$("#"+otherId).children().children().attr("src",$("#"+otherId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	mainObjs.$wrapPage.load(baseUrl + "/otherMission/otherMissionAction!allMissions.action");
	otherId = obj.id;
}
//跳转到设置的其他任务
function otherMissionFirstPage(hurl){
	$(".l-menu").show();
	$(".content").removeClass('content_c').addClass('content');
	//跳转到其他任务时，将data-show设为5，用于区分左边菜单收缩效果
	mainObjs.$logoImg.attr("data-show","5");
	$("#menu").hide();
	$("#otherMissionLeftMenu").show();
	$("#caseTypeMenu").hide();
	$("#otherMissionssss").css("color","#ffffff");
	$("#otherMissionImg").attr('src',baseUrl+'/itest/images/homeImage/elseTs1.png');
	$("#changePro").css("color","#e9f6ff");
	$("#changePro").html("项目测试");
	$('#changePro').removeAttr("title");
	$("#addMenus").css("margin","20px 0 -5px");
	$("#systemM").css("margin","20px 0 -5px");
	$("#systemManage").css("color","#e9f6ff");
	$("#systemManage").html("系统设置 ");
	$("#otherMissionLeftMenu").find("li[myUrl='"+hurl+"']").css("background","rgb(30, 124, 251) none repeat scroll 0% 0%");
	if($("#otherMissionLeftMenu").find("li[myUrl='"+hurl+"']").children().children().attr("src").split("1").length > 1){
		$("#otherMissionLeftMenu").find("li[myUrl='"+hurl+"']").children().children().attr("src",$("#otherMissionLeftMenu").find("li[myUrl='"+hurl+"']").children().children().attr("src").split("1")[0]+".png");
	}
	$("#otherMissionLeftMenu").find("li[myUrl='"+hurl+"']").siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	otherId = $("#otherMissionLeftMenu").find("li[myUrl='"+hurl+"']").attr("id");
	destoryEle();
	mainObjs.$wrapPage.load(baseUrl + hurl);
}
//跳转到用例类别维护
function caseTypeEdit(obj){
	$(obj).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
	$(obj).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	if(casId != obj.id){
		$("#"+casId).children().children().attr("src",$("#"+casId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	mainObjs.$wrapPage.load(baseUrl + "/testLibrary/testLibraryAction!loadTree.action");
	casId = obj.id;
}
//跳转到用例浏览
function caseLook(obj){
	$(obj).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
	$(obj).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	if(casId != obj.id){
		$("#"+casId).children().children().attr("src",$("#"+casId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	mainObjs.$wrapPage.load(baseUrl + "/testLibrary/testLibraryAction!caseLook.action");
	casId = obj.id;
}
//跳转到入库审核
function caseExamine(obj){
	$(obj).css('background', 'rgb(30, 124, 251) none repeat scroll 0% 0%');
	$(obj).siblings().css('background', 'rgb(61, 67, 81) none repeat scroll 0% 0%');
	if(casId != obj.id){
		$("#"+casId).children().children().attr("src",$("#"+casId).children().children().attr("src").split(".png")[0]+"1.png");
	}
	mainObjs.$wrapPage.load(baseUrl + "/testLibrary/testLibraryAction!caseExamine.action");
	casId = obj.id;
}
//迭代列表
//$("#iteration").click(function(){
//	
//});

//获取当前用户的权限
function getLoginUserProManager(){
	$.ajaxSettings.async = false;
	//从后台取得权限，放入privilegeMap
	$.getJSON(
			baseUrl + '/userManager/userManagerAction!getUserUrls.action',
			function(data) {
				if(data.length > 0){
					for(var i=0;i<data.length;i++){
						privilegeMap[data[i]] ="1";
					}
					var controlButton = $('li[schkUrl]');
					$.each(controlButton,function(i,n){
						var controId = controlButton[i].id;
						var controlUrl = $(controlButton[i]).attr('schkUrl');
						if(privilegeMap[controlUrl]!="1"){
							//$("#"+controlID).removeClass("hide"); 
//							$("[schkurl='"+schkUrl+"']").removeClass("hide");
							//$(":button[schkUrl]").removeClass("hide");  
							$("#"+controId).hide(); 
						}
					});
				}
			}
	);
}
