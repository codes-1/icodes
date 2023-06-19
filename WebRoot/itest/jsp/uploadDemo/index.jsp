<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8"/>
    <title>文件上传</title>
    <link href="<%=request.getContextPath()%>/itest/exui/fileinput/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/itest/exui/bootstrap/font-awesome/css/font-awesome.min.css" media="all" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/itest/exui/fileinput/themes/explorer-fa/theme.css" media="all" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="container kv-main">
    <div class="page-header">
        <h2>文件上传示例</h2>
    </div>
    <form enctype="multipart/form-data">
        <div class="form-group">
            <div class="file-loading">
                <input id="uploadFileId" type="file" multiple class="file" data-overwrite-initial="false" data-min-file-count="2">
            </div>
        </div>
        <hr>
    </form>

    <br>
</div>
</body>
<script>var baseUrl = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/itest/exui/jquery/jquery-1.11.3.min.js"></script>
<script src="<%=request.getContextPath()%>/itest/exui/bootstrap/bootstrap-3.3.7.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/fileinput.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/locales/zh.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/plugins/sortable.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/themes/explorer-fa/theme.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/themes/fa/theme.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/plugins/popper.js" type="text/javascript"></script>
	
<script type="text/javascript">
 $("#uploadFileId").fileinput({
    theme: 'fa',
    language:'zh',
    uploadUrl:baseUrl + "/uploader?", // you must set a valid URL here else you will get an error
    allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx'],
    overwriteInitial: false,
    maxFileSize: 1000,
    maxFilesNum: 10,
    //allowedFileTypes: ['image', 'video', 'flash'],
    slugCallback: function (filename) {
        return filename.replace('(', '_').replace(']', '_');
    }
}); 
</script>

<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/itest/jsp/uploadDemo/fileUpload.js"></script> --%>

</html>