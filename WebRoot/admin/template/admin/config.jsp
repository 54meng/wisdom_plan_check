<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.wpc.persistence.Config"%>
<%
Config cfg = (Config)request.getAttribute("config");
cfg = null == cfg ? new Config() : cfg;
%>
<!-- BEGIN PAGE HEADER-->
<h3 class="page-title">
推荐配置 <small> </small>
</h3>
<!-- BEGIN PAGE CONTENT-->
<div class="row">
	<form id="configForm" action="../../../config.sp" style="margin-left:10px" enctype="multipart/form-data" method="POST">
		<input type="hidden" name="act" value="save" />
		<input type="hidden" name="id" value="${config.id}" />
		<table border="0" style="border-collapse:separate; border-spacing:0px 10px;">
			<tr align="right" >
				<td><label for="user">二维码：&nbsp;</label></td>
				<td align="left">
					<div class="fileinput fileinput-new" data-provides="fileinput">
						<div class="fileinput-new thumbnail" style="max-width: 80px; max-height: 80px;">
							<img src="<%=(cfg.getQrCodeUrl()!=null?"../../../"+cfg.getQrCodeUrl():"img/placehlodit.png") %>" alt="">
						</div>
						<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 80px; max-height: 80px; line-height: 10px;"></div>
						<div>
							<span class="btn default btn-file">
							<span class="fileinput-new">
							选择图片</span>
							<span class="fileinput-exists">
							改变图片</span>
							<input value="" type="file" name="image">
							</span>
							<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput"><span class="md-click-circle md-click-animate" style="height: 77px; width: 77px; top: 427.5px; left: 562.5px;"></span>
							移除图片</a>
						</div>
					</div>
				</td>							
			</tr>
			
			<tr align="right" >
				<td><label for="user">主链接：&nbsp;</label></td>
				<td><input type="text" id="downloadUrl" name="downloadUrl" value="${config.downloadUrl}" class="form-control" style="width:600px"/></td>							
			</tr>
			<tr align="right" >
				<td><label for="user">分享文本：&nbsp;</label></td>
				<td><textarea id="shareText" name="shareText" class="form-control" style="width:600px;height:120px">${config.shareText}</textarea></td>							
			</tr>
			<tr align="right" >
				<td><label for="user">QQ群号：&nbsp;</label></td>
				<td><input type="text" id="qqGroupNo" name="qqGroupNo" value="${config.qqGroupNo}" class="form-control" style="width:600px"/></td>							
			</tr>
		</table>
		
		<button id="submitBtn" type="submit" class="btn green" style="margin-top:20px;margin-left:305px;width:60px">保存</button>
	</form>
	
</div>
<script>
$("#configForm").bind("submit", function(){  
	if($("#downloadUrl").val() == ""){  
		alert("主链接不能为空");
		return false;  
	}  
	if($("#shareText").val() == ""){  
		alert("分享文本不能为空");
		return false;  
	}
	
});
$(function(){
	$("#configForm").ajaxForm(function(data){  
		if(data == "1"){
			alert('保存成功！');   
		}else{
			alert('保存失败');   
		}	
	});     
});

</script>
<script type="text/javascript">
	$('.form-control-chosen').chosen({
	  allow_single_deselect: true,
	  width: '100%'
	});

	$(function() {
	  $('[title="clickable_optgroup"]').addClass('chosen-container-optgroup-clickable');
	});
	$(document).on('click', '[title="clickable_optgroup"] .group-result', function() {
	  var unselected = $(this).nextUntil('.group-result').not('.result-selected');
	  if(unselected.length) {
		unselected.trigger('mouseup');
	  } else {
		$(this).nextUntil('.group-result').each(function() {
		  $('a.search-choice-close[data-option-array-index="' + $(this).data('option-array-index') + '"]').trigger('click');
		});
	  }
	});
</script>