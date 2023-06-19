<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="<%=request.getContextPath()%>/itest/exui//bootstrap/bootstrap-3.3.7.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/itest/exui/fileinput/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/itest/exui/bootstrap/font-awesome/css/font-awesome.min.css" media="all" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/itest/exui/fileinput/themes/explorer-fa/theme.css" media="all" rel="stylesheet" type="text/css"/>
<div class="kv-main">
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

<script>var baseUrl = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/itest/exui/bootstrap/bootstrap-3.3.7.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/plugins/sortable.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/fileinput.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/locales/zh.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/locales/fr.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/itest/exui/fileinput/js/locales/es.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/themes/explorer-fa/theme.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/fileinput/themes/fa/theme.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/itest/exui/plugins/popper.js" type="text/javascript"></script>
	
<script type="text/javascript" src="<%=request.getContextPath()%>/itest/jsp/uploadDemo/fileUpload.js"></script>
