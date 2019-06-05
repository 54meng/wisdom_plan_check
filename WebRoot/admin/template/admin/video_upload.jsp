<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.wpc.persistence.Video"%>
<%@page import="com.wpc.persistence.Tag"%>
<%
Video video = (Video)request.getAttribute("video");
video = null == video ? new Video() : video;
List<Tag> tagList = (List<Tag>)request.getAttribute("tagList");
%>
<!-- BEGIN PAGE HEADER-->
<h3 class="page-title">
上传视频 <small> 统一上传视频，支持断点续传，目前只支持mp4格式的视频</small>
</h3>
<!-- BEGIN PAGE CONTENT-->
<div class="row">
	<iframe id="iframeupload" src="../../../html5upload/upload.jsp" frameborder="0" scrolling="none" style="width:100%;height:120px;"></iframe>
	<form id="videoForm" action="../../../video.sp" style="margin-left:10px" enctype="multipart/form-data" method="POST">
		<input type="hidden" name="act" value="save" />
		<input type="hidden" id="videoUrl" name="videoUrl" value="${video.videoUrl}" />
		<input type="hidden" id="size" name="size" value="${video.size}" />
		<input type="hidden" name="videoId" value="${video.id}" />
		<table border="0" style="border-collapse:separate; border-spacing:0px 10px;">
			<tr align="right" >
				<td><label for="user">封面(横)：&nbsp;</label></td>
				<td align="left">
					<div class="fileinput fileinput-new" data-provides="fileinput">
						<div class="fileinput-new thumbnail" style="max-width: 80px; max-height: 80px;">
							<img src="<%=(video.getCoverUrl1()!=null?"../../../"+video.getCoverUrl1():"img/placehlodit.png") %>" alt="">
						</div>
						<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 80px; max-height: 80px; line-height: 10px;"></div>
						<div>
							<span class="btn default btn-file">
							<span class="fileinput-new">
							选择图片</span>
							<span class="fileinput-exists">
							改变图片</span>
							<input value="" type="file" name="cover1">
							</span>
							<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput"><span class="md-click-circle md-click-animate" style="height: 77px; width: 77px; top: 427.5px; left: 562.5px;"></span>
							移除图片</a>
						</div>
					</div>
				</td>							
			</tr>
			<tr align="right" >
				<td><label for="user">封面(竖)：&nbsp;</label></td>
				<td align="left">
					<div class="fileinput fileinput-new" data-provides="fileinput">
						<div class="fileinput-new thumbnail" style="max-width: 80px; max-height: 80px;">
							<img src="<%=(video.getCoverUrl2()!=null?"../../../"+video.getCoverUrl2():"img/placehlodit.png") %>" alt="">
						</div>
						<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 80px; max-height: 80px; line-height: 10px;"></div>
						<div>
							<span class="btn default btn-file">
							<span class="fileinput-new">
							选择图片</span>
							<span class="fileinput-exists">
							改变图片</span>
							<input value="" type="file" name="cover2">
							</span>
							<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput"><span class="md-click-circle md-click-animate" style="height: 77px; width: 77px; top: 427.5px; left: 562.5px;"></span>
							移除图片</a>
						</div>
					</div>
				</td>							
			</tr>
			<tr align="right" >
				<td><label for="user">名称：&nbsp;</label></td>
				<td><input type="text" id="name" name="name" value="${video.name}" class="form-control" style="width:600px"/></td>							
			</tr>
			<tr align="right">
				<td><label for="user">分类：&nbsp;</label></td>
				<td>
					<select class="form-control" id="cateId" name="cateId">
						<option value="">---</option>
						<c:forEach items="${cateList}" var="item">
							<option <c:if test='${video.cateId == item.cateId}'>selected="selected"</c:if> value="${item.cateId}">${item.cateName}</option>
						</c:forEach>
					</select>
				</td>							
			</tr>
			<tr align="right">
				<td><label for="user">专题：&nbsp;</label></td>
				<td>
					<select class="form-control" id="specialId" name="specialId">
						<option value="">---</option>
						<c:forEach items="${specialList}" var="item">
							<option <c:if test='${video.specialId == item.id}'>selected="selected"</c:if> value="${item.id}">${item.name}</option>
						</c:forEach>
					</select>
				</td>							
			</tr>
			<tr align="right">
				<td><label for="user">主角：&nbsp;</label></td>
				<td>
					<select class="form-control" id="authorId" name="authorId">
						<option value="">---</option>
						<c:forEach items="${authorList}" var="item">
							<option <c:if test='${video.authorId == item.id}'>selected="selected"</c:if> value="${item.id}">${item.name}</option>
						</c:forEach>
					</select>
				</td>							
			</tr>
			<tr align="right" >
				<td><label for="user">评分：&nbsp;</label></td>
				<td><input type="text" name="score" value="${video.score}" class="form-control" style="width:600px"/></td>							
			</tr>
			
			<tr align="right">
				<td><label for="user">简介：&nbsp;</label></td>
				<td><textarea name="intros" class="form-control" style="height:100px">${video.intros}</textarea></td>							
			</tr>			
			
			<tr align="right">
				<td><label for="user">标签：&nbsp;</label></td>
				<td style="max-width:600px" >
					<select id="multiple" name="tag" class="form-control form-control-chosen" data-placeholder=" " multiple>
						<option></option>
						<%
							for(Tag tag : tagList){
								String selected = "";
								if(null != video && null != video.getTag() 
										&& video.getTag().contains(tag.getId())){
									selected = "selected";
								}
								%>
								<option <%=selected %> value="<%=tag.getId() %>"><%=tag.getName() %></option>
								<%
							}
						%>
						
					  </select>
				</td>							
			</tr>	
		</table>
		
		<button id="submitBtn" type="submit" class="btn green" style="margin-top:20px;margin-left:305px;width:60px">保存</button>
	</form>
	
</div>
<script>
$("#videoForm").bind("submit", function(){  
	if($("#videoUrl").val() == ""){  
		alert("请上传视频");
		return false;  
	} 
	if($("#name").val() == ""){  
		alert("名称不能为空");
		return false;  
	}  
	if($("#cateId").val() == ""){  
		alert("请选择分类");
		return false;  
	}
	
});
$(function(){
	$("#videoForm").ajaxForm(function(data){  
		if(data == "1"){
			alert('保存成功！');   
		}else if(data == "2"){
			alert('评分必须为数字类型');   
		}	
	});     
});
function onSingleUploaded (serverJson, clentfileObj)
{
	var name = clentfileObj.name;
	$("#name").val(name.substring(0,name.lastIndexOf('.')));
	$("#videoUrl").val(serverJson.serverfileurl);
	$("#size").val(serverJson.serverfilesize);
}
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