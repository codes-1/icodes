$(function(){
	$.parser.parse();
});

/**
 * 创建上传窗口 公共方法
 * @param chunk 是否分割大文件
 * @param callBack 上传成功之后的回调
 */
function Uploader(chunk,callBack){
	/* var addWin = $("<div style='overflow: hidden;'/>"); */
	/* var addWin = $("#uploadWin"); */
//	var upladoer = $("<iframe/>");
//	upladoer.attr({"src":baseUrl+"/itest/jsp/uploadDemo/uploader.jsp?chunk=" + chunk,
//		width : "100%",
//		height : "100%",
//		frameborder : "0",
//		scrolling : "no"
//	});
	$("#uploadWin").window({
		title : "上传文件",
		height : 400,
		width : 550,
		minimizable : false,
		modal : true,
		collapsible : false,
		maximizable : false,
		resizable : false,
//		content : upladoer,
		onClose : function() {
			var fw = GetFrameWindow(upladoer[0]);
			var files = fw.files;
			$(this).window("destroy");
			callBack.call(this, files);
		},
		onOpen : function() {
			var target = $(this);
			setTimeout(function() {
				var fw = GetFrameWindow(upladoer[0]);
				fw.target = target;
			}, 100);
		}
	});
 }

/**
 * 根据iframe对象获取iframe的window对象
 * @param frame
 * @returns {Boolean}
 */
function GetFrameWindow(frame) {
	return frame && typeof (frame) == "object" && frame.tagName == "IFRAME"
			&& frame.contentWindow;
}

function makerUpload(chunk) {
	Uploader(chunk, function(files) {
		if (files && files.length > 0) {
			$("#res").text("成功上传：" + files.join(","));
		}
	});
}

//# sourceURL=index.js