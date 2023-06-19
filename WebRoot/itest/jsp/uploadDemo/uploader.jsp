<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>文件上传</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/itest/plupload/queue/css/jquery.plupload.queue.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/itest/exui/jquery/jquery-1.11.3.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/plupload/plupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/plupload/plupload.html4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/plupload/plupload.html5.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/plupload/plupload.flash.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/plupload/zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/plupload/queue/jquery.plupload.queue.js"></script>
<script>var baseUrl = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/itest/js/uploadDemo/uploader.js" type="text/javascript" charset="utf-8"></script>
<body style="padding: 0;margin: 0;">
	<div id="uploader">&nbsp;</div>
	<script type="text/javascript">
	    var files = [];
		var errors = [];
		var type = "file";
		var chunk = eval("${param.chunk}");
		var max_file_size = "100mb";
		var filters = {
			title : "文档",
			extensions : "jpg,png,bmp,zip,doc,docx,xls,xlsx,ppt,pptx"
		};
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
								var rs = $.parseJSON(response.response);
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
							target.window("close");
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
				} : {})));
	</script>
</body>
</html>
