<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.wpc.persistence.AppVersion"%>
<%
AppVersion appVersion = (AppVersion)request.getAttribute("appVersion");
appVersion = null == appVersion ? new AppVersion() : appVersion;
%>
<!-- BEGIN PAGE HEADER-->
<h3 class="page-title">
App版本管理 <small> </small>
</h3>
<!-- BEGIN PAGE CONTENT-->
<div class="row">
	<form id="dataForm" action="../../../appVer.sp" style="margin-left:10px" enctype="multipart/form-data" method="POST">
		<input type="hidden" name="act" value="save" />
		<input type="hidden" name="id" value="${appVersion.id}" />
		<table border="0" style="border-collapse:separate; border-spacing:0px 10px;">
			<tr align="right" >
				<td><label for="user">apk文件：&nbsp;</label></td>
				<td align="left">
					<input type="file" id="apkUrl" name="apkUrl" style="width:200px;float:left"/>
					<span style="display:inline-block;vertical-align: middle;">${appVersion.apkUrl}</span>
				</td>							
			</tr>
			<tr align="right" >
				<td><label for="user">ipa文件：&nbsp;</label></td>
				<td align="left">
					<input type="file" id="ipaUrl" name="ipaUrl" style="width:200px;float:left"/>
					<span style="display:inline-block;vertical-align: middle;">${appVersion.ipaUrl}</span>
				</td>							
			</tr>
			<tr align="right" >
				<td><label for="user">Android版本号：&nbsp;</label></td>
				<td><input type="text" id="androidVersion" name="androidVersion" value="${appVersion.androidVersion}" class="form-control" style="width:600px"/></td>							
			</tr>
			<tr align="right" >
				<td><label for="user">IOS版本号：&nbsp;</label></td>
				<td><input type="text" id="iosVersion" name="iosVersion" value="${appVersion.iosVersion}" class="form-control" style="width:600px"/></td>							
			</tr>
			<tr align="right" >
				<td><label for="user">Android更新内容：&nbsp;</label></td>
				<td><textarea id="androidUpdContent" name="androidUpdContent" class="form-control" style="width:600px;height:100px">${appVersion.androidUpdContent}</textarea></td>							
			</tr>
			<tr align="right" >
				<td><label for="user">IOS更新内容：&nbsp;</label></td>
				<td><textarea id="iosUpdContent" name="iosUpdContent" class="form-control" style="width:600px;height:100px">${appVersion.iosUpdContent}</textarea></td>							
			</tr>
			<tr align="right" >
				<td><label for="user">Android下载地址：&nbsp;</label></td>
				<td><input type="text" id="androidDownloadUrl" name="androidDownloadUrl" value="${appVersion.androidDownloadUrl}" class="form-control" style="width:600px"/></td>							
			</tr>
			<tr align="right" >
				<td><label for="user">IOS下载地址：&nbsp;</label></td>
				<td><input type="text" id="iosDownloadUrl" name="iosDownloadUrl" value="${appVersion.iosDownloadUrl}" class="form-control" style="width:600px"/></td>							
			</tr>
			<tr align="right" >
				<td><label for="user">Android强制更新：&nbsp;</label></td>
				<td align="left">
					<select class="form-control" id="androidIsForce" name="androidIsForce" style="width:100px">
						<option value="1" <c:if test='${appVersion.androidIsForce == "1"}'>selected="selected"</c:if>>是</option>
						<option value="0" <c:if test='${appVersion.androidIsForce == "0"}'>selected="selected"</c:if>>否</option>
					</select>
				</td>							
			</tr>
			<tr align="right" >
				<td><label for="user">IOS强制更新：&nbsp;</label></td>
				<td align="left">
					<select class="form-control" id="iosIsForce" name="iosIsForce" style="width:100px">
						<option value="1" <c:if test='${appVersion.iosIsForce == "1"}'>selected="selected"</c:if>>是</option>
						<option value="0" <c:if test='${appVersion.iosIsForce == "0"}'>selected="selected"</c:if>>否</option>
					</select>
				</td>							
			</tr>
		</table>
		
		<button id="submitBtn" type="submit" class="btn green" style="margin-top:20px;margin-left:305px;width:60px">保存</button>
	</form>
	
</div>
<script>
$("#dataForm").bind("submit", function(){  
	if($("#androidVersion").val() == ""){  
		alert("Android版本号不能为空");
		return false;  
	}  
	if($("#iosVersion").val() == ""){  
		alert("IOS版本号不能为空");
		return false;  
	}
	
});
$(function(){
	$("#dataForm").ajaxForm(function(data){  
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