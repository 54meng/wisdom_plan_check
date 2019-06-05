<%@page import="com.wpc.persistence.Special"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
boolean isNew = (Boolean)request.getAttribute("isNew");
Special entity = (Special)request.getAttribute("special");
%>
<!-- modal-content -->
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
	<h4 class="modal-title"><%=(isNew?"新建专题":"编辑专题") %></h4>
</div>
<div class="modal-content form">
	<!-- BEGIN FORM-->
	<form action="special.sp" enctype="multipart/form-data" method="POST" id="form_sample_1" class="form-horizontal" novalidate="novalidate">
		<input type="hidden" name="act" value="save" />
		<div class="form-body">
			<div class="alert alert-danger display-hide">
				<button class="close" data-close="alert"></button>
				你的表单存在输入错误，请检查以下表单.
			</div>
			<div class="alert alert-success display-hide">
				<button class="close" data-close="alert"></button>
				你的表单验证成功!
			</div>
			<div class="form-group">
				<label class="control-label col-md-3">图片<span class="required">*</span></label>
				<div class="col-md-7">
					<div class="fileinput fileinput-new" data-provides="fileinput">
						<div class="fileinput-new thumbnail" style="max-width: 80px; max-height: 80px;">
							<img src="<%=(entity.getIcon()!=null?"../../../"+entity.getIcon():"img/placehlodit.png") %>" alt="">
						</div>
						<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 80px; max-height: 80px; line-height: 10px;"></div>
						<div>
							<span class="btn default btn-file">
							<span class="fileinput-new">
							选择图片</span>
							<span class="fileinput-exists">
							改变图片</span>
							<input value="" type="file" name="files">
							</span>
							<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput"><span class="md-click-circle md-click-animate" style="height: 77px; width: 77px; top: 427.5px; left: 562.5px;"></span>
							移除图片</a>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-3">名称<span class="required">
				* </span>
				</label>
				<div class="col-md-7">
					<input type="hidden" name="id" value="${special.id }" />
					<input type="text" name="name" value="${special.name }" data-required="1" class="form-control"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">简介&nbsp;</label>
				<div class="col-md-7">
					<textarea name="desc" class="form-control" rows="3">${special.desc}</textarea>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<div class="row">
				<div class="col-md-offset-3 col-md-9">
					<button id="submitBtn" type="submit" class="btn green">保存</button>
					<button type="button" class="btn default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</form>
	<!-- END FORM-->
</div>
<!-- /.modal-content -->
<script>
//Html编码获取Html转义实体
function htmlEncode(value){
  return $('<div/>').text(value).html();
}
//Html解码获取Html实体
function htmlDecode(value){
  return $('<div/>').html(value).text();
}
//提示窗口
function boxAlert(title,message){
	 bootbox.dialog({
         'message': message,
         'title': title,
         'buttons': {
           main: {
             label: "关闭",
             className: "blue",
           }
         }
     });
}
//表单验证方法
var handleValidation = function() {
    	// for more info visit the official plugin documentation: 
        // http://docs.jquery.com/Plugins/Validation
        var form1 = $('#form_sample_1');
        var error1 = $('.alert-danger', form1);
        var success1 = $('.alert-success', form1);

        form1.validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block help-block-error', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",  // validate all fields including form hidden input
            messages: {
                name: {
                    minlength: jQuery.validator.format("至少{0}个字符（中文算一个字符）"),
                    maxlength: jQuery.validator.format("最多{0}个字符（中文算一个字符）"),
                    required: "名称不能为空"
                }
            },
            rules: {
                name: {
                    required: true,
                    minlength: 1,
                    maxlength: 20
                }         
            },
            invalidHandler: function (event, validator) { //display error alert on form submit              
                success1.hide();
                error1.show();
                Metronic.scrollTo(error1, -200);
            },
            highlight: function (element) { // hightlight error inputs
                $(element)
                    .closest('.form-group').addClass('has-error'); // set error class to the control group
            },
            unhighlight: function (element) { // revert the change done by hightlight
                $(element)
                    .closest('.form-group').removeClass('has-error'); // set error class to the control group
            },
            success: function (label) {
                label
                    .closest('.form-group').removeClass('has-error'); // set success class to the control group
            },
            submitHandler: function (form) {
                //success1.show();
                error1.hide();
                try{
                	var submitBtn = $("#submitBtn");
                    var action = form1.attr("action").split('?');
                    var extParam="";
                    if(action.length>1){extParam = "&"+ action[1]}
               	  	var option = {
               		   	type: "POST",
               		   	url: "../../../"+action[0],
               		   	//data: form1.serialize()+extParam,
               		 	//async: true,
               		 	dataType:"json",
               		 	beforeSend: function(XMLHttpRequest){
               		 	 	//this; // 调用本次AJAX请求时传递的options参数
               		 		submitBtn.text("正在保存...");
               		 	},
               		   	success: function(data){
               		   		console.log(data&&data.ret);
               		   		if(data.ret==0){
        	               		submitBtn.text("保存成功!");
        	           		   	setTimeout(function () {
        	           		   		$('#ajax').modal('hide');
        	           		   		var tableId = $("#tableId").val();
        	           		   		var table = $("#"+tableId).DataTable();
        	           		   		if(<%=isNew%>){
            	           		   		table.draw();
        	           		   		}else{
            	           		   		table.draw(false);
        	           		   		}
        	                   	}, 300);
               		   		}else if(data.ret==1){
               		   			submitBtn.text("保存");
	               		   		boxAlert("保存失败，错误信息",data.msg);
               		   		}else{
	               		   		submitBtn.text("保存");
	               		   		boxAlert("保存失败，返回未知数据",htmlEncode(JSON.stringify(data)));
               		   		}
              			},
            			complete: function(XMLHttpRequest, textStatus){
            		   		if(textStatus!="success"&&textStatus!="error"){
                		 		submitBtn.text("保存");
                		 		boxAlert("保存失败，错误信息",htmlEncode(XMLHttpRequest.status+":"+(XMLHttpRequest.text)));
            		   		}
            			},
            			error:function(XMLHttpRequest, textStatus, errorThrown){
            		   		submitBtn.text("保存");
            		 		boxAlert("保存失败，ajax请求存在异常",XMLHttpRequest.status+":"+errorThrown);
            		 		try{console.log('异常对象：',errorThrown);}catch(e){}
            			}
               		};
               		$(form).ajaxSubmit(option);
            	}catch(e){
                	try{console.log(e);}catch(e){}
            	}
        	}
    });
}
handleValidation();
</script>