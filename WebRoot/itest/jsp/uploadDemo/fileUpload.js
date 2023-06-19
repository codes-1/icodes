$(function(){
	$.parser.parse();
//	uploadFile();
//	initFileInput("uploadFileId",baseUrl + "/uploader?");
//	var container = getQueryParam('uploadContainer');
//	initFileInput(container,baseUrl + "/uploader?");
});

//获取页面url参数
function getQueryParam(name) {
       var obj = $('#fileUploadWin').xwindow('options');
       var queryParams = obj["queryParams"]; 
       return queryParams[name];
}

//function uploadFile(){
//	 $("#uploadFileId").fileinput({
//	        theme: 'fa',
//			language:'zh',
//	        uploadUrl:baseUrl + "/uploader?", // you must set a valid URL here else you will get an error
//	        allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx'],
//	        overwriteInitial: false,
//	        maxFileSize: 1000,
//	        maxFilesNum: 10,
//	        showRemove:true,
//	        //allowedFileTypes: ['image', 'video', 'flash'],
//	        slugCallback: function (filename) {
//	            return filename.replace('(', '_').replace(']', '_');
//	        }
//	    });
//}

//$("#uploadFileId").fileinput({
//    theme: 'fa',
//    language:'zh',
//    uploadUrl:baseUrl + "/uploader?", // you must set a valid URL here else you will get an error
//    allowedFileExtensions: ['jpg', 'png', 'gif','bmp','ppt','pptx'],
//    overwriteInitial: false,
//    maxFileSize: 2000,
//    maxFilesNum: 5,
//    msgFilesTooMany :"选择图片超过了最大数量", 
//    showRemove:true,
//    //allowedFileTypes: ['image', 'video', 'flash'],
//    slugCallback: function (filename) {
//        return filename.replace('(', '_').replace(']', '_');
//    }
//});



//function initFileInput(ctrlName, uploadUrl) { 
//    var control = $('#' + ctrlName); 
//    control.fileinput({ 
//      resizeImage : true, 
//      maxImageWidth : 200, 
//      maxImageHeight : 200, 
//      resizePreference : 'width', 
//      language : 'zh', //设置语言 
//      uploadUrl : uploadUrl, 
//      uploadAsync : true, 
//      allowedFileExtensions : [ 'jpg','png','gif','bmp','ppt','pptx'],//接收的文件后缀 
//      showUpload : true, //是否显示上传按钮 
//      showCaption : true,//是否显示标题 
//      browseClass : "btn btn-primary", //按钮样式 
//      previewFileIcon : "<i class='glyphicon glyphicon-king'></i>", 
//      maxFileCount : 3, 
//      msgFilesTooMany : "选择图片超过了最大数量", 
//      maxFileSize : 2000, 
//    }); 
//  }; 
//  
//  initFileInput("uploadFileId",baseUrl + "/uploader?");

//$('#uploadFileId').fileinput({ 
//    resizeImage : true, 
//    maxImageWidth : 200, 
//    maxImageHeight : 200, 
//    resizePreference : 'width', 
//    language : 'zh', //设置语言 
//    uploadUrl : baseUrl + '/uploader?', 
//    uploadAsync : true, 
//    allowedFileExtensions : [ 'jpg','png','gif','bmp','ppt','pptx'],//接收的文件后缀 
//    showUpload : true, //是否显示上传按钮 
//    showCaption : true,//是否显示标题 
//    browseClass : "btn btn-primary", //按钮样式 
//    previewFileIcon : "<i class='glyphicon glyphicon-king'></i>", 
//    maxFileCount : 3, 
//    msgFilesTooMany : "选择图片超过了最大数量", 
//    maxFileSize : 2000, 
//  }); 
