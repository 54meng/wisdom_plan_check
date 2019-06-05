<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <title>视频上传</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/> 
        <meta name="keywords" content="HTML5断点续传,跨域上传,超大文件上传,jquery上传插件,php断点续传,c#断点续传,java断点续传,asp断点续传,silverupload,uploadify,swfupload,基于Http协议的断点续传控件">
        <meta name="description" content="HTML5upload是基于html5技术的，由jquery把上传逻辑封装起来的支持断点续传上传jquery插件。">
        <link rel="stylesheet" type="text/css" href="css/css.css?R=20170510">
<style>
		html{font-size:12px;font-family:宋体}

/*demo样式*/
fieldset {

    margin: 10px auto 10px auto;
    border:1px #ccc solid;
    -moz-border-radius:3px;
    border-radius:3px;

}
legend {
    border:1px #ccc solid;
    font-size:16px;
    line-height:23px;
    padding:0 15px 0 15px;
    -moz-border-radius:3px;
    border-radius:3px;
}
#uploader {
    width: 100%;margin:10px auto 10px auto;overflow:hidden
}

		</style>
</head>
<body>
    <fieldset><legend>视频上传</legend>
        <div id="uploader"></div>
    </fieldset>
    
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/html5upload-dev.js"></script>
    <script src="js/jquery.html5upload.js"></script>
    <script src="js/html5upload-md5-min.js"></script>
    <script src="js/md5.js"></script>
	<script src="js/browser.js"></script>
    <script>
        //上传事件
        var html5uploadEvents = {
            afterAddFiles: function (files) {
            	if(html5upload.task.fileArray.length>1)
       			{ 	       			
       				var i;
       				for(i=0;i<html5upload.task.fileArray.length;i++)
       				{

       					html5upload.removeFile(0);
       				}
       			}

            	         	
            	__fixUI();
            },
            onBeforeSingleFileUpload: function () {
                html5FileInfo.md5 = $("#html5FileList li").eq(0).find(".file").attr("md5");
                
            },
            onSingleUploaded: function (serverJson, clentfileObj) {
                //单一文件上传完毕事件
                $(".state0").hide();
                //__showlog(clentfileObj.name + " 上传完成。文件所在服务器路径为： " + serverJson.serverfileurl);
                $.ajax({
					async: false,		//估计可以是异步也可以是同步请求，请实测
					type: "post",
					dataType: "json",
					url: '',
					data: "filepath="+serverJson.serverfileurl+"&filename="+clentfileObj.name,
					success: function (data) {
					    
					}
				});
                window.parent.onSingleUploaded(serverJson, clentfileObj);
                __fixUI();
            },
            onGetSingleFileMd5: function (_index, clentfileObj, currentPos, filemd5) {
                //_index 文件列表索引号
                //currentPos当前读取文件偏移量
                //__showlog(currentPos / clentfileObj.size);
                //filemd5 当分析完毕时，得到的文件md5值
                var elem = $("#html5FileList li").eq(_index).find(".file");
                var _md5progress = elem.find(".md5progress");
                var _md5progressText = elem.find(".md5progressText");
                if (_md5progress.length == 0) {
                    elem.append("<div class=\"md5progress\"><div class=\"md5progressText\"></div></div>");
                }
                else {
                    var _percent = parseInt(currentPos / clentfileObj.size * 100);
                    _md5progress.css({ width: _percent + "%" });

                    if (_percent == 100) {
                        //当分析md5完毕
                        _md5progress.hide();
                        elem.attr("md5", filemd5);
                    }
                    else {
                        _md5progress.show();
                        _md5progressText.show().text("正在分析md5..." + _percent + "%");
                    }
                }
                __fixUI();
            },
            onUploadTaskOver: function () {
                //所有文件上传完毕事件

                $("#btnRemoveUploadFiles").hide();
                $(".state0").show();
                $(".state1").hide();
                $(".progress").hide();
                __fixUI2();
                alert("上传完毕");
            },
            onStartUpload: function () {
                $(".state0").hide();
                $(".state1").css({ display: "inline-block" }).show();
                $(".progress").show();
                $(".progress progress").each(function () {
                    $(this).outerWidth($(this).parent().width());
                });
                __fixUI3();
            },
            onPause: function () {
                //alert("暂停");
                $(".state0").show();
                $(".state1").hide();
                $(".progress").hide();
                __fixUI();
            }
        }

        function __showlog(s) {
            
        }


        $(document).ready(function () {
            $("#uploader").renderAsHtml5Uploader({
                //postUrl: "http://www.test.com/demo/uploadmd5.aspx",       //跨域上传
                postUrl: "../upload.sp",
                partSize: 1024*200,      //默认分段大小
                maxPartSize: 1024 * 1024 * 5,       //最大分段大小5M
                btnSelectFilesText:"添加视频...",    //选择文件按钮文字
                saveAsFullMd5Name: false,       //使用文件完整md5值作为服务器端文件名，如要启动服务器端md5校验，必须设置为true。如果文件很大，最好不要启动整个功能
                saveAsPartName: true,             //使用自动文件名，如saveAsFullMd5Name=false与saveAsPartName=false则服务器端文件名以客户端原文件名作为文件名
				multiple:true,
				allowFileExt:"mp4"			//多种后缀，请用半角逗号隔开.如allowFileExt:"jpg,png,jpeg,gif,mp4,mp3,avi,flv"	。如配置为 allowFileExt:"*",则表示允许所有格式的文件上传   
            });
            __fixUI();
        })
        
        function __fixUI(){
        	//修正调用页面iframe的显示高度
        	window.parent.$("#iframeupload").css({height:$(document).height()+"px"});
        }
        
        function __fixUI2(){
        	//修正调用页面iframe的显示高度
        	window.parent.$("#iframeupload").css({height:"120px"});
        }
        
        function __fixUI3(){
        	//修正调用页面iframe的显示高度
        	window.parent.$("#iframeupload").css({height:$(document).height()+36+"px"});
        }

</script>

</body>
</html>