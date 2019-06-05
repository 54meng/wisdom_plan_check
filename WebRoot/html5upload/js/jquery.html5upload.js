(function ($) {
    //复选框特效
    $.fn.toggleCheckbox = function () {
        this.attr('checked', !this.attr('checked'));
    },
    $.fn.renderAsHtml5Uploader = function (option) {
        html5upload.config.postUrl = option.postUrl;
        html5upload.config.partSize = option.partSize || html5upload.config.partSize;
        html5upload.config.maxPartSize = option.maxPartSize || html5upload.config.maxPartSize;
		html5upload.config.minPartSize = option.minPartSize || html5upload.config.minPartSize;
        html5upload.config.saveAsFullMd5Name = option.saveAsFullMd5Name || html5upload.config.saveAsFullMd5Name;
        html5upload.config.saveAsPartName = option.saveAsPartName || html5upload.config.saveAsPartName;
        html5upload.config.multiple = option.multiple || html5upload.config.multiple;
        html5upload.config.allowFileExt = option.allowFileExt || html5upload.config.allowFileExt;
        option.btnSelectFilesText = option.btnSelectFilesText || "添加文件...";

        var _id = this.attr("id");
        var _html = "";
        _html += "<!--html5upload--><div class=\"html5upload\"><label for=\"html5FileUploader\" class=\"selectFiles state0\">" + option.btnSelectFilesText + "</label>";
        _html += "<input type=\"file\" id=\"html5FileUploader\" name=\"file\" class=\"hidden\" ";
        if (html5upload.config.multiple) {
            _html += "multiple=\"multiple\">";
        }
        else {
            _html += ">";
        }

        _html += "<a href=\"javascript:html5upload.task.startUpload();\" class=\"html5uploadbtn btn0 state0\">上传</a>";
        _html += "<a href=\"javascript:html5upload.pause();\" class=\"state1 html5uploadbtn btn1 hidden\">暂停</a>";
        _html += "<div class=\"progress hidden state0\">";
        _html += "<div>";
        _html += "<progress value=\"0\" max=\"100\" id=\"html5progress\"></progress>";
        _html += "<span></span>";
        _html += "</div>";
        _html += "</div>";

        _html += "<div>";
        _html += "<ul id=\"html5FileList\"></ul>";
        _html += "</div>";
        _html += "<a href=\"javascript:html5upload.removeSelectedFile();\" class=\"html5uploadbtn btn1 hidden\" id=\"btnRemoveUploadFiles\">从上传队列中移除选择的文件</a>";
        _html += "</div><!--html5upload-->";

        this.append(_html);
        var elem = $("#" + _id + " .selectFiles");
        elem.attr("owidth", elem.outerWidth() + "px");
        elem.attr("oheight", elem.outerHeight() + "px");
        //alert(elem.attr("owidth"));
        $(document).on({
            dragleave: function (e) {
                e.preventDefault();
            },
            drop: function (e) {
                e.preventDefault();
            },
            dragenter: function (e) {
                e.preventDefault();
                var elem = $("#" + _id + " .selectFiles");
                elem.text("请把要上传的文件释放到这里").addClass("selectFilesonDragEnter").attr("lineHeight", $(window).height() + "px");

                elem.css({ width: $(window).width(), height: $(window).height(), lineHeight: $(window).height() + "px" });
            },
            dragover: function (e) {
                e.preventDefault();
            }
        })

        $("#" + _id + " .selectFiles").on({
            dragleave: function (e) {
                e.preventDefault();
            },
            dragleave: function (e) { //拖离 
                e.preventDefault();
                var elem = $("#" + _id + " .selectFiles");
                elem.text(option.btnSelectFilesText).removeClass("selectFilesonDragEnter").css({ lineHeight: elem.attr("oheight"), width: elem.attr("owidth"), height: elem.attr("oheight") });

            },
            drop: function (e) {  //拖后放 
                e.stopPropagation();
                e.preventDefault();

                var files = e.originalEvent.dataTransfer.files;
                //alert(files.length);
                html5upload.addFiles(files);
                var elem = $("#" + _id + " .selectFiles");
                elem.text(option.btnSelectFilesText).removeClass("selectFilesonDragEnter").css({ lineHeight: elem.attr("oheight"), width: elem.attr("owidth"), height: elem.attr("oheight") });

            },
            dragover: function (e) {
                e.preventDefault();
            }
        });

        html5upload.init();
    }
})(jQuery);