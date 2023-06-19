// dom缓存，提升页面访问性能
var objs = {
	$body: $("body"),
	$switchBtn: $("#switch_btn"),
	$loginForm: $("#loginForm"),
	$loginName: $("#loginName"),
	$loginPwd: $("#loginPwd"),
	$remember: $("#remember")
};

$(function() {
	//var height = $(".login-form").height();
	//console.log(height);
	objs.$switchBtn.on('click', function() {
		$(this).toggleClass("collapse");
		objs.$body.toggleClass("collapse");
	});
	
	// 记住我
	rememberMe();
	
	// 单击回车键登录
	$(document).keypress(function(e) {
		if (e.keyCode == 13) {
			login();
		}
	});
	//判断浏览器是否为火狐或者chrome
	if(navigator.userAgent.indexOf("Firefox")==-1 && navigator.userAgent.indexOf("Chrome")==-1){
		alert("请使用火狐浏览器或者chrome浏览器登录");
	}
});

// 记住用户名和密码
function rememberMe() {
	if ("undefined" != typeof localStorage.checked && "true" == localStorage.checked) {
		objs.$loginName.on('input propertychange', function() {
			localStorage.loginName = objs.$loginName.val();
		});
		
		objs.$loginPwd.on('input propertychange', function() {
			localStorage.password = objs.$loginPwd.val();
		});
		
		objs.$loginName.val(localStorage.loginName);
		objs.$loginPwd.val(localStorage.password);
		objs.$remember.attr('checked', 'checked');
	} else {
		sessionStorage.removeItem("loginName");
		sessionStorage.removeItem("password");
	}

	objs.$remember.on('click', function() {
		if (objs.$remember.is(':checked')) {
			localStorage.checked = 'true';
			localStorage.loginName = objs.$loginName.val();
			localStorage.password = objs.$loginPwd.val();
		} else {
			localStorage.checked = 'false';
			sessionStorage.removeItem("loginName");
			sessionStorage.removeItem("password");
		}
	});
}

// 登录
function login() {
	$.ajax({
		type: 'POST',
		url: baseUrl + '/userManager/userManagerAction!login.action',
		data: /*objs.$loginForm.serialize()*/{
			'dto.user.loginName':$("#loginlName").val().trim(),
			'dto.user.password':$("#loginPwd").val().trim()
		},
		dataType: 'json'
	}).done(function(data) {
		if ("success" == data.loginItest) {
			objs.$loginForm.find('button').text('登录中...');
			//window.location.href = baseUrl + "/" + data.HomeUrl;
			window.location.href = baseUrl+"/main.htm";
			
		} else {
			$.xnotify("用户名或密码错误！", {type:'warning'});
		}
	}).fail(function(xhr, textStatus, errorThrown) {
		$.xnotify("登录失败！", {type:'danger'});
	});
	
	/*$.post(
		baseUrl + '/userManager/userManagerAction!login.action',
		objs.$loginForm.serialize(),
		function(data) {
			if ("success" == data.loginMYPM) {
				window.location.href = baseUrl + "/" + data.HomeUrl;
			} else {
				console.log("登录失败！");
			}
		},
		'json'
	);*/
}