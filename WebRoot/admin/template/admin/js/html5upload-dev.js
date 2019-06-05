//单任务上传版本

Date.prototype.dateDiff = function (interval, objDate) {
    //若參數不足或 objDate 不是日期物件則回傳 undefined
    if (arguments.length < 2 || objDate.constructor != Date) return undefined;
    switch (interval) {
        //計算秒差 
        case "s": return parseInt((objDate - this) / 1000);
            //計算分差
        case "n": return parseInt((objDate - this) / 60000);
            //計算時差
        case "h": return parseInt((objDate - this) / 3600000);
            //計算日差
        case "d": return parseInt((objDate - this) / 86400000);
            //計算週差
        case "w": return parseInt((objDate - this) / (86400000 * 7));
            //計算月差
        case "m": return (objDate.getMonth() + 1) + ((objDate.getFullYear() - this.getFullYear()) * 12) - (this.getMonth() + 1);
            //計算年差
        case "y": return objDate.getFullYear() - this.getFullYear();
            //輸入有誤
        default: return undefined;
    }
}
Date.prototype.dateAdd = function (strInterval, Number) {
    var dtTmp = this;
    switch (strInterval) {
        case 's': return new Date(Date.parse(dtTmp) + (1000 * Number));
        case 'n': return new Date(Date.parse(dtTmp) + (60000 * Number));
        case 'h': return new Date(Date.parse(dtTmp) + (3600000 * Number));
        case 'd': return new Date(Date.parse(dtTmp) + (86400000 * Number));
        case 'w': return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));
        case 'q': return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number * 3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
        case 'm': return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
        case 'y': return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
    }
}

fileSizeToString = function (_size) {
    _size = parseFloat(_size);
    if (_size >= Math.pow(1024, 3)) {
        return (_size / Math.pow(1024, 3)).toFixed(2) + "GB";
    }

    if (_size >= Math.pow(1024, 2)) {
        return (_size / Math.pow(1024, 2)).toFixed(2) + "MB";
    }
    if (_size >= 1024) {
        return (_size / (1024)).toFixed(2) + "KB";
    }
    else {
        return _size.toFixed(2) + "B";
    }
}

function secondToDate(msd) {
    var time = parseFloat(msd);
    if (null != time && "" != time) {
        if (time > 60 && time < 60 * 60) {
            time = parseInt(time / 60.0) + "分钟" + parseInt((parseFloat(time / 60.0) -
            parseInt(time / 60.0)) * 60) + "秒";
        }
        else if (time >= 60 * 60 && time < 60 * 60 * 24) {
            time = parseInt(time / 3600.0) + "小时" + parseInt((parseFloat(time / 3600.0) -
            parseInt(time / 3600.0)) * 60) + "分钟" +
            parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) -
            parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + "秒";
        }
        else {
            time = parseInt(time) + "秒";
        }
    }
    return time;
}

var html5FileInfo = {
    fileobj: null,       //本地文件指针
    startPos: 0,
    endPos: 0,
    name: "",
    rename:"",
    extName: null,
    md5: null,     //本地文件md5值
    size: 0,
    type: "",
    md5StartTime: "",
    md5readSpeed: 0,
    uploadStartTime: "", 	//开始上传时刻
    uploadSpeed: 0,     //上传速率
    totalBytes: 0,  //当前文件需要上传的字节数
    sentBytes: 0,   //已经传送的字节数
    getExtName: function () {
        return this.name.substring(this.name.lastIndexOf('.') + 1);
    },
    speedToText: function () {
        var __now = new Date();
        var v = parseFloat(this.uploadStartTime.dateDiff("s", __now));
        if (v > 0) {
            v = parseFloat(this.sentBytes / v);
        } else {
            v = this.sentBytes;
        }
        this.uploadSpeed = v;
        return fileSizeToString(v) + "/s"
    },
    getTimeRemainText: function () {
        var v = (this.totalBytes - this.sentBytes) / this.uploadSpeed;
        return secondToDate(v);
    }
};
var html5upload = {
    config:{
        postUrl: null,              //保存文件的请求url
        partSize: 1024 * 1024,        //分段文件数据大小
        maxPartSize: 1024 * 1024 * 5,    //最大文件分段大小
        minPartSize: 1024 * 200,    //最小文件分段大小
        saveAsFullMd5Name: false,
        saveAsPartName: false,
        multiple: false,
        singleFileMaxSize: 0,
        allowFileExt:"*"
    },
    init: function () {
        document.getElementById("html5FileUploader").onchange = function () {
            html5upload.addFiles(this.files);
        };
        $("#btnRemoveUploadFiles").hide();
        //初始化进度条样式
        $(".progress progress").each(function () {
            $(this).outerWidth($(this).parent().width());
        });
        $(window).resize(function () {
            $(".progress progress").each(function () {
                $(this).outerWidth($(this).parent().width());
            });
        });
    },
    task: {
        //上传任务对象
        state: 0,    //state=1 上传中，0停止
        fileArray: [],       //文件队列
        sentBytes: 0,
        totalBytes: 0,
        prototype: html5upload,
        startTime: null,
        md5NeedReadBytes: 0,
        startUpload: function () {
            if (this.fileArray.length <= 0) {
                alert("请先选择文件，然后再点击上传");
                return;
            }
			html5upload.progress(0);
            this.totalBytes = 0;

            var i;
            for (i = 0; i < this.fileArray.length; i++) {

                this.totalBytes += this.fileArray[i].size;
                //__showlog("第" + i + "个文件字节数：" + this.fileArray[i].size +"/"+ this.totalBytes);
            }
            this.md5NeedReadBytes = this.totalBytes;
            this.sentBytes = 0;
            html5upload.progress(0);
            html5upload.progressText("");
            html5upload.task.state = 1;
            //__showlog("上传任务总字节数：" + this.totalBytes);
            __showlog("开始上传时刻：" + (new Date()).format("yyyy-MM-dd hh:mm:ss"));
            html5upload.beforeSingleFileUpload();       //拉起第一个文件的上传进程
        },
        getTimeRemain: function () {
            var v = (this.totalBytes - this.sentBytes) / html5FileInfo.uploadSpeed;
            if (html5upload.config.saveAsFullMd5Name) {
                var v1 = this.md5NeedReadBytes / this.md5readSpeed;     //计算剩余文件字节md5耗时时间
                return v + v1;
            }
            else {
                return v;
            }
            
            //return secondToDate(v + v1) + "<br>html5upload.task.totalBytes：" + html5upload.task.totalBytes + "<br>html5upload.task.sentBytes：" + html5upload.task.sentBytes + "<br>剩余：" + (html5upload.task.totalBytes - html5upload.task.sentBytes)+ "<br>上传耗时：" + secondToDate(v) + "<br>还需分析：" + this.md5NeedReadBytes + "md5耗时：" + secondToDate(v1);
        }
    },
    getSingleFileMd5: function (_index) {
        var fileReader = new FileReader();
        var blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;
        var chunkSize = 1024 * 1024 * 5;  //分段读取本地文件大小
        var file = this.task.fileArray[_index];
        var chunks = Math.ceil(html5FileInfo.fileobj.size / chunkSize);
        var currentChunk = 0;
        var spark = new SparkMD5.ArrayBuffer();

        html5FileInfo.md5StartTime = new Date();

        fileReader.onload = function (e) {
            spark.append(e.target.result);
            currentChunk++;
            if (html5upload.task.state == 0) {
                return;
            }
            if (currentChunk < chunks)//判断是否读取完成
            {
                getSingleFileMd5Progress(_index);

            }
            else {
                html5FileInfo.md5 = spark.end().toUpperCase();
                html5uploadEvents.onGetSingleFileMd5(_index, file, file.size, html5FileInfo.md5);
                html5upload.task.md5NeedReadBytes = html5upload.task.md5NeedReadBytes - html5FileInfo.size;
                html5FileInfo.rename = html5FileInfo.md5 + "." + html5FileInfo.extName;
                html5upload.startSingleFileUpload();
            }
        };
        var getSingleFileMd5Progress = function (_index) {
            var start = currentChunk * chunkSize;
            var end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;
            if (end < file.size) {
                html5uploadEvents.onGetSingleFileMd5(_index, file, start);
            }
            fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
        };
        getSingleFileMd5Progress(_index);
    },
    addFiles: function (_files) {

        if (_files.length > 0) {
            var i;
            for (i = 0; i < _files.length; i++) {
                if (_files[i].size == 0) {
                    alert("不能添加上传0字节文件“" + _files[i].name + "”");
                }
                else {
                    if (this.config.singleFileMaxSize > 0) {
                        if (parseFloat(files[i].size) > parseFloat(this.config.singleFileMaxSize)) {
                            alert("不能添加上传大于" + fileSizeToString(this.config.singleFileMaxSize) + "的文件“" + files[i].name + "”");
                        }
                    }
                    else {

                        if (this.config.allowFileExt) {

                            if (this.config.allowFileExt == "*") {
                                this.__addFileToUI(_files[i]);
                            }
                            else {
                                
                                if (!this.config.allowFileExt.split(",").isInArray(getFileExtName(_files[i].name))) {
                                    alert("不能添加后缀为" + getFileExtName(_files[i].name) + "的文件");
                                }
                                else {
                                    this.__addFileToUI(_files[i]);
                                }
                            }
                        }
                        
                    }
                }
            }
            this.xUIupdateFileList();
        }
        html5uploadEvents.afterAddFiles(_files);
    },
    __addFileToUI:function(_file)
    {
        if (this.config.multiple) {
            this.task.fileArray.push(_file);
            $("#html5FileList").append("<li><div class=\"file\"><input type=\"checkbox\" name=\"check-box\" class=\"check-box\"><span>" + _file.name + "</span></div><div class=\"filesize\">" + fileSizeToString(_file.size) + "</div></li>");
        }
        else {
            if (this.task.fileArray.length == 1) {
                this.task.fileArray.pop();
            }
            this.task.fileArray.push(_file);
            $("#html5FileList").html("<li><div class=\"file\"><input type=\"checkbox\" name=\"check-box\" class=\"check-box\"><span>" + _file.name + "</span></div><div class=\"filesize\">" + fileSizeToString(_file.size) + "</div></li>");
        }
        var elems = $("#html5FileList li");

        $("#html5FileList li:last-child").find('input[name="check-box"]').wrap('<div class="check-box"><i></i></div>');
        $("#html5FileList li:last-child").find("div.check-box").on('click', function () {
            $(this).find(':checkbox').toggleCheckbox();
            $(this).toggleClass('checkedBox');
        });
    },
    xUIupdateFileList: function () {
        //更新界面列表
        if (this.task.fileArray.length > 0) {

            $("#btnRemoveUploadFiles").show();
        }
        else {
            $("#btnRemoveUploadFiles").hide();
        }

    },
    removeFile: function (_index) {
        this.task.fileArray.splice(_index, 1);
        $("#html5FileList li").eq(_index).remove();
    },
    removeSelectedFile: function () {

        if (html5upload.task.state == 0) {
            $('input[name="check-box"]').each(function () {
                if ($(this).is(":checked")) {
                    var _index = $(this).parents("li").index();
                    html5upload.removeFile(_index);
                }
            })
        }
        else {
            alert("暂停后才能移除文件");
        }
    },
    progress: function (value) {
        document.getElementById("html5progress").value = value;
    },
    progressText: function (value) {
        $(".progress span").html(value);
    },

    beforeSingleFileUpload: function () {
        html5FileInfo.fileobj = this.task.fileArray[0];
        html5FileInfo.startPos = 0;
        html5FileInfo.endPos = html5FileInfo.startPos + html5upload.config.partSize;
        html5FileInfo.name = html5FileInfo.fileobj.name;
        html5FileInfo.rename = "";
        html5FileInfo.extName = html5FileInfo.getExtName();
        html5FileInfo.size = html5FileInfo.fileobj.size;
        html5FileInfo.type = html5FileInfo.fileobj.type;
        html5FileInfo.sentBytes = 0;
        html5FileInfo.md5 = "";

        html5uploadEvents.onBeforeSingleFileUpload();
        html5uploadEvents.onStartUpload();
        
        if (html5FileInfo.md5 == undefined && html5upload.config.saveAsFullMd5Name) {
            this.task.md5NeedReadBytes = this.task.md5NeedReadBytes - html5FileInfo.size;
            this.getSingleFileMd5(0);
        }
        else {
            if (html5upload.config.saveAsPartName) {
                var fileReader = new FileReader();
                var blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice;
                var spark = new SparkMD5.ArrayBuffer();

                fileReader.onload = function (e) {

                    spark.append(e.target.result);

                    html5FileInfo.md5 = spark.end().toUpperCase();

                    if (html5FileInfo.md5 != "" && (html5upload.config.saveAsFullMd5Name || html5upload.config.saveAsPartName)) {
                        //如果采用md5值为文件名，那么向服务器提供重名后的文件名
                        if (html5upload.config.saveAsFullMd5Name) {
                            html5FileInfo.rename = html5FileInfo.md5 + "." + html5FileInfo.extName;
                        }
                        else {
                            html5FileInfo.rename = hex_md5(html5FileInfo.size.toString() + html5FileInfo.md5).toUpperCase() + "." + html5FileInfo.extName;
                        }
                        __showlog("自动文件名2:" + html5FileInfo.rename);
                    }
                    else {
                        alert("html5upload配置值有误。");
                    }
                    html5upload.startSingleFileUpload();
                };
                fileReader.readAsArrayBuffer(blobSlice.call(html5FileInfo.fileobj, html5FileInfo.size - 64, html5FileInfo.size));
            }
            else {
                this.startSingleFileUpload();
            }
        }
    },
    startSingleFileUpload: function () {
        //上传校验文件 
        html5FileInfo.uploadStartTime = new Date();

        if (html5upload.config.saveAsFullMd5Name) {
            var v = parseFloat(html5FileInfo.md5StartTime.dateDiff("s", html5FileInfo.uploadStartTime));
            html5upload.task.md5readSpeed = parseFloat(html5FileInfo.size / v);
        }

        //上传前校验文件
        var fData = new FormData();
        fData.append("localfilename", html5FileInfo.name);
        fData.append("filesize", html5FileInfo.size);
        fData.append("rename", html5FileInfo.rename);
        fData.append("act", "validate");

        $.ajax({
            url: html5upload.config.postUrl,
            type: "POST",
            data: fData,
            /**   
             * 必须false才会避开jQuery对 formdata 的默认处理   
             * XMLHttpRequest会对 formdata 进行正确的处理   
             */
            processData: false,
            /**   
             *必须false才会自动加上正确的Content-Type   
             */
            contentType: false,
            success: function (responseStr) {
                var jsonObj = $.parseJSON(responseStr);

                if (jsonObj.state == 0) {
                    if (jsonObj.serverfilesize == html5FileInfo.fileobj.size) {
                        //html5upload.task.totalBytes = html5upload.task.totalBytes - jsonObj.serverfilesize;
                        html5upload.task.sentBytes += jsonObj.serverfilesize;
  
                        var fileReader = new FileReader();
                        var blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice;
                        var spark = new SparkMD5.ArrayBuffer();

                        fileReader.onload = function (e) {
                            spark.append(e.target.result);
                            var _localFileValidataMd5 = spark.end().toUpperCase();
                            __showlog(_localFileValidataMd5);
                            __showlog(jsonObj.validatamd5);

                            if (_localFileValidataMd5 == jsonObj.validatamd5) {
                                html5upload.removeFile(0);
                                html5uploadEvents.onSingleUploaded(jsonObj, html5FileInfo);
                                if (html5upload.task.fileArray.length == 0) {
                                    html5upload.task.state = 0;
                                    html5uploadEvents.onUploadTaskOver();

                                }
                                else {
                                    html5upload.beforeSingleFileUpload();    //继续下一个文件上传
                                }
                            }
                            else {
                                alert("服务器端校验失败");
                            }
                        };
                        fileReader.readAsArrayBuffer(blobSlice.call(html5FileInfo.fileobj, jsonObj.validatepos, jsonObj.serverfilesize));

                    }
                    else {

                        var fileReader = new FileReader();
                        var blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice;

                        var start = jsonObj.validatepos;
                        var end = start + jsonObj.validatelength;
                        if (end > jsonObj.serverfilesize) {
                            end = jsonObj.serverfilesize;
                        }

                        var spark = new SparkMD5.ArrayBuffer();

                        fileReader.onload = function (e) {
                            spark.append(e.target.result);
                            var _localFileValidataMd5 = spark.end().toUpperCase();

                            if (_localFileValidataMd5 == jsonObj.validatamd5 || (jsonObj.validatepos == 0 && jsonObj.validatamd5 == "")) {
                                //如果服务器文件存在，那么进行文件部分二进制内容md5校验，校验成功的话则上传。
                                //如果服务器文件不存在，那么直接上传

                                html5FileInfo.totalBytes += jsonObj.serverfilesize;

                                __showlog("本地校验码：" + _localFileValidataMd5 + "<br>服务校验码：" + jsonObj.validatamd5);
                                __showlog("文件校验成功，开始续传。");

                                html5upload.task.sentBytes += jsonObj.serverfilesize;

                                html5FileInfo.startPos = jsonObj.serverfilesize;
                                html5FileInfo.endPos = html5FileInfo.startPos + html5upload.config.partSize;

                                html5upload.save(html5FileInfo.startPos, html5FileInfo.endPos);
                            }

                        };
                        fileReader.readAsArrayBuffer(blobSlice.call(html5FileInfo.fileobj, start, end));
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                var errortext = "";
                if (XMLHttpRequest.status == 404) {
                    errortext = "请求" + html5upload.config.postUrl + "发生404错误";
                }
                else if (XMLHttpRequest.status == 500) {
                    errortext = "请求" + html5upload.config.postUrl + "发生服务器端程序错误";
                }
                else {
                    errortext = "服务器停止服务";
                }
                __showlog("文件校验错误[" + errortext + "]");
                window.setTimeout("html5upload.beforeSingleFileUpload(); ", 5000);
            }
        });
    },

    save: function (_startPos, _endPos) {
        var fData = new FormData();
        fData.append("file", html5FileInfo.fileobj.slice(_startPos, _endPos));
        fData.append("localfilename", html5FileInfo.name);
        fData.append("rename", html5FileInfo.rename);
        fData.append("filesize", html5FileInfo.size);
        fData.append("filepos", _startPos);
        fData.append("type", html5FileInfo.type);
        fData.append("act", "save");

        $.ajax({
            url: html5upload.config.postUrl,
            type: "POST",
            data: fData,
            /**   
             * 必须false才会避开jQuery对 formdata 的默认处理   
             * XMLHttpRequest会对 formdata 进行正确的处理   
             */
            processData: false,
            /**   
             *必须false才会自动加上正确的Content-Type   
             */
            contentType: false,
            success: function (responseStr) {
                var jsonObj = $.parseJSON(responseStr);

                if (jsonObj.state == 0) {
                    //var v = parseInt(jsonObj.serverfilesize / html5FileInfo.size * 100);
                    var dataSize = _endPos - _startPos;

                    html5FileInfo.sentBytes = html5FileInfo.sentBytes + dataSize;
                    html5upload.task.sentBytes = html5upload.task.sentBytes + dataSize;

                    var v = parseInt(html5upload.task.sentBytes / html5upload.task.totalBytes * 100);

                    //__showlog("percent:"+v+",startPos:"+html5FileInfo.startPos + ",serverfilesize:" + jsonObj.serverfilesize + ",partSize:" + html5upload.config.partSize);

                    html5upload.progress(v);
                    //上传到服务器保存文件成功
                    if (jsonObj.serverfilesize < html5FileInfo.size) {
                        
                        //服务器上的文件小于客户端的文件大小

                        html5FileInfo.startPos = jsonObj.serverfilesize;
                        //计算数据范围
                        html5upload.config.partSize = html5FileInfo.uploadSpeed / 2;
                        if (html5upload.config.partSize > html5upload.config.maxPartSize) {
                            html5upload.config.partSize = html5upload.config.maxPartSize;
                        }
                        if (html5upload.config.partSize < html5upload.config.minPartSize) {
                            html5upload.config.partSize = html5upload.config.minPartSize;
                        }
                        html5FileInfo.endPos = html5FileInfo.startPos + html5upload.config.partSize;

						//__showlog("percent:"+v+",startPos:"+html5FileInfo.startPos +",endPos:"+html5FileInfo.endPos + ",serverfilesize:" + jsonObj.serverfilesize + ",partSize:" + html5upload.config.partSize);

                        if (html5FileInfo.endPos > html5FileInfo.size) {
                            html5FileInfo.endPos = html5FileInfo.size;
                        }
                        var a = html5FileInfo.speedToText();      //计算上传速率
                        var b = html5upload.task.getTimeRemain();
                        var finishedTime = new Date().dateAdd("s", b).format("yyyy-MM-dd hh:mm:ss");
                        html5upload.progressText(html5FileInfo.name + "(" + fileSizeToString(html5FileInfo.size) + ")，速率：" + a + "，分段大小：" + fileSizeToString(html5upload.config.partSize) + "<br>预计完成时间：" + finishedTime);
                        

                        if (html5upload.task.state == 1) {
                            html5upload.save(html5FileInfo.startPos, html5FileInfo.endPos);
                        }
                    }
                    else {

                        html5upload.removeFile(0);
                        html5uploadEvents.onSingleUploaded(jsonObj, html5FileInfo);

                        if (html5upload.task.fileArray.length == 0) {
                            
                            html5upload.task.state = 0;
                            html5uploadEvents.onUploadTaskOver();
                        }
                        else {
                            html5upload.beforeSingleFileUpload();    //继续下一个文件上传
                        }
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                var errortext = "";
                if (XMLHttpRequest.status == 404) {
                    errortext = "请求" + html5upload.config.postUrl + "发生404错误";
                }
                else if (XMLHttpRequest.status == 500) {
                    errortext = "请求" + html5upload.config.postUrl + "发生服务器端程序错误";
                }
                else {
                    errortext = "服务器停止服务";
                }
                __showlog("文件上传发生错误[" + errortext + "]，5秒后尝试续传。");
                window.setTimeout("html5upload.task.startUpload(); ", 5000);
                //alert(responseStr);
            }
        });
    },
    pause: function () {
        this.task.state = 0;
        $(".md5progress").hide();
        html5uploadEvents.onPause();
    }
}

    function getFileExtName(_filename) {
        return _filename.substring(_filename.lastIndexOf('.') + 1).toLowerCase();
    }

Date.prototype.format = function (format) //author: meizz 
{
    if (format == "hh") {
        //alert(this.getHours());
    }
    var o = {
        "M+": this.getMonth() + 1, //month 
        "d+": this.getDate(),    //day 
        "h+": this.getHours(),   //hour 
        "m+": this.getMinutes(), //minute 
        "s+": this.getSeconds(), //second 
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter 
        "S": this.getMilliseconds() //millisecond 
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,(this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}