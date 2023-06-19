var files = [];
var errors = [];
var type = "file";
//var chunk = eval('${param.chunk}');
var chunk = false;
var max_file_size = "100mb";
var filters = {
	title : "文档",
	extensions : "jpg,png,bmp,zip,doc,docx,xls,xlsx,ppt,pptx"
};
$(function(){
	$.parser.parse();
	uploadInit();
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
//			$(this).window("destroy");
			callBack.call(this, files);
		},
		onOpen : function() {
//			var target = $(this);
//			setTimeout(function() {
//				var fw = GetFrameWindow(upladoer[0]);
//				fw.target = target;
//			}, 100);
		}
	});
	$("#uploadWin").window("open");
 }


function makerUpload(chunk) {
	chunk = chunk;
	Uploader(chunk, function(files) {
		if (files && files.length > 0) {
			$("#res").text("成功上传：" + files.join(","));
		}
	});
}


//var files = [];
//var errors = [];
//var type = "file";
////var chunk = eval('${param.chunk}');
//var chunk = false;
//var max_file_size = "100mb";
//var filters = {
//	title : "文档",
//	extensions : "jpg,png,bmp,zip,doc,docx,xls,xlsx,ppt,pptx"
//};

function uploadInit(){
	$("#uploader").pluploadQueue(
			$.extend({
				runtimes : "flash,html4,html5",
				url :baseUrl + "/uploader?",
				max_file_size : max_file_size,
				file_data_name : "file",
				unique_names : true,
				filters : [ filters ],
				flash_swf_url : "plupload/plupload.flash.swf",
				init : {
					FileUploaded : function(uploader, file, response) {
						if (response.response) {
							var jsonStr = response.response.match(/\{.*\}/)[0];
							var rs = $.parseJSON(jsonStr);
							if (rs.status) {
								files.push(file.name);
							} else {
								errors.push(file.name);
							}
						}
					},
					UploadComplete : function(uploader, fs) {
						var e = errors.length ? ",失败" + errors.length
								+ "个(" + errors.join("、") + ")。" : "。";
								alert("上传完成！共" + fs.length + "个。成功" + files.length
										+ e);
								$("#uploadWin").window("close");
					},
					BeforeUpload: function (up, file) {
						// Called right before the upload for a given file starts, can be used to cancel it if required
						up.settings.multipart_params = {
								fileName: file.name
						};
					}
				}
			}, (chunk ? {
				chunk_size : "1mb"
			} : {
				
			})));
}


//# sourceURL=index.js