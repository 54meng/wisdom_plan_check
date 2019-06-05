/*攻略编辑JS*/
var fileLib = {};
var dayIndex = 0;
var raidersDetail = $("#raidersDetail");
var raidersTemp = $("#raidersTemp");
$(".add-day").click(addDay);
/*添加一天事件*/
function addDay(dayData){
	var hasData = true;
	if(!dayData||!dayData.title){
		hasData = false;
		dayData = {"title":"第?天","date":""};
	}
	/*复制模板*/
	var dayTemp = $("#dayTemp",raidersTemp).clone();
	var dayTempId = "day_"+dayIndex;
	dayTemp.attr("id",dayTempId);
	$("#dayTitle",dayTemp).attr("name",dayTempId+"_dayTitle").val(dayData.title).change();
	$("#dayTime",dayTemp).attr("name",dayTempId+"_dayTime").val(dayData.date);
	dayTemp.data("dayIndex",dayIndex);
	dayTemp.data("siteIndex",0);
	raidersDetail.append(dayTemp);
	dayTemp.show("fast");
	var addSiteBtn = $("#addSiteBtn",dayTemp).attr("id",dayTempId+"_addSiteBtn");
	if(!hasData){
		//默认展开第一级站点
		addSiteBtn.click();	
	}
	dayIndex++;
}
/*标题更新事件*/
function updateDayTitle(e){
	var dayTemp = $(e).parent().parent().parent().parent();
	var dayTitle = $(".dayTitle",dayTemp);
	dayTitle.html("<i class=\"fa fa-gift\"></i>"+e.value);
}
/*添加站点事件*/
function addSite(e,siteData){
	var hasData = true;
	if(!siteData||!siteData.title){
		siteData = {"title":"站点名称","content":"站点简介"};
	}
	var dayTemp = $(e).parent().parent().parent();
	var siteIndex = dayTemp.data("siteIndex");
	var dayIndex = dayTemp.data("dayIndex");
	var sitesDiv = $(".sites",dayTemp);
	/*复制站点模板*/
	var siteTemp = $("#siteTemp",raidersTemp).clone();
	siteTemp.attr("id","day_"+dayIndex+"_site_"+siteIndex);
	$("#siteName",siteTemp).attr("name","day_"+dayIndex+"_site_"+siteIndex+"_siteName").val(siteData.title);
	$("#siteDesc",siteTemp).attr("name","day_"+dayIndex+"_site_"+siteIndex+"_siteDesc").val(siteData.content);
	/*添加到sitesDiv中去*/
	sitesDiv.append(siteTemp);
	siteTemp.show("fast");
	siteTemp.data("siteIndex",siteIndex);
	dayTemp.data("siteIndex",++siteIndex);
	siteTemp.data("imageIndex",0);
	//往站点块存天数索引
	siteTemp.data("dayIndex",dayIndex);
}
/*添加图像事件*/
function addImage(e){
	var siteTemp = $(e).parent().parent().parent();
	var dayIndex = siteTemp.data("dayIndex");
	var siteIndex = siteTemp.data("siteIndex");
	var imageIndex = siteTemp.data("imageIndex");
	var fileInput = $("#fileInput");
	fileInput.data("dayIndex",dayIndex);
	fileInput.data("siteIndex",siteIndex);
	fileInput.data("imageIndex",imageIndex);
	fileInput.click();
//	var imagesDiv = $(".images",siteTemp);
//	var imageTemp = $("#imageTemp",raidersTemp).clone();
//	/*添加到imagesDiv中去*/
//	imagesDiv.append(imageTemp);
//	imageTemp.attr("id","");
//	imageTemp.show("normal","linear");
}

/*添加图像数据事件*/
function addImageData(dayIndex,siteIndex,imageData){
	if(!imageData){
		return;
	}
	var siteTemp = $("#day_"+dayIndex+"_site_"+siteIndex);
	var dayIndex = siteTemp.data("dayIndex");
	var siteIndex = siteTemp.data("siteIndex");
	var imageIndex = siteTemp.data("imageIndex");
	var imageTemp = $("#imageTemp",raidersTemp).clone();
    $("img",imageTemp).attr("src", imageData.smallUrl?imageData.smallUrl:"#")
    .click(function(){
    	var image = $(this).parent().parent().parent();
    	image.remove()
    });;
	var imageId = "day_"+dayIndex+"_site_"+siteIndex+"_image_"+(imageIndex);
	imageTemp.attr("id",imageId);
	imageTemp.append("<input type='hidden' name='"+imageId+"_url' value='"+imageData.orgUrl+"' />");
	imageTemp.append("<input type='hidden' name='"+imageId+"_small_url' value='"+imageData.smallUrl+"' />");
	imageTemp.append("<input type='hidden' name='"+imageId+"_large_url' value='"+imageData.largeUrl+"' />");
	imageTemp.append("<input type='hidden' name='"+imageId+"_hd_url' value='"+imageData.hdUrl+"' />");
	$('#imageDesc',imageTemp).attr("name",imageId+"_desc").text(imageData.imageDesc);
	/*添加到imagesDiv中去*/
	var imagesDiv = $(".images",siteTemp);
	imagesDiv.append(imageTemp);
	imageTemp.show("fast");
	siteTemp.data("imageIndex",++imageIndex);
}
var imageIdFileList = [];
function fileChange(e){
	imageIdFileList = [];
	var fileInput = $(e);
	var files = e.files;
	if(files.length>0){
		var dayIndex = fileInput.data("dayIndex");
		var siteIndex = fileInput.data("siteIndex");
 		var imageIndex = fileInput.data("imageIndex");
 		var siteTemp = $("#day_"+dayIndex+"_site_"+siteIndex);
 		var imagesDiv = $(".images",siteTemp);
 		siteTemp.data("imageIndex",imageIndex+files.length);
		for(var i=0;i<files.length;i++){
			var file = files[i];
		 	var reader = new FileReader();
	 		var imageTemp = $("#imageTemp",raidersTemp).clone();
	 		var imageId = "day_"+dayIndex+"_site_"+siteIndex+"_image_"+(imageIndex++);
	        $("img",imageTemp)
	        .attr("id", imageId+"_image")
	        .attr("imageId", imageId)
	        .click(function(){
	        	var imageId = $(this).attr("imageId");
	        	delete fileLib[imageId+"_file"];
	        	var image = $(this).parent().parent().parent();
	        	image.remove()
	        });
	 		fileLib[imageId+"_file"] = file;
	 		imageIdFileList[imageIdFileList.length] = [imageId,file];
	 		imageTemp.attr("id",imageId);
	 		$('#imageDesc',imageTemp).attr("name",imageId+"_desc");
	 		/*添加到imagesDiv中去*/
	 		imagesDiv.append(imageTemp);
            reader.onload = function(e) {
            	var total = e.total;
         		for(var i=0;i<imageIdFileList.length;i++){
         			if(imageIdFileList[i][1].size==total){
                    	$("#"+imageIdFileList[i][0]+"_image").attr("src", e.target.result);
                    	$("#"+imageIdFileList[i][0]).show("fast");
         				break;
         			}
         		}
            }
            reader.readAsDataURL(files[i]);
		}
	}
	var fileClone = fileInput.clone(true);
	$("body").append(fileClone);
	fileInput.remove();
};

//$(function(){
	/*初始化数据库存在的值到界面上*/
	if(detailData&&detailData.length>0){
		for(var dayIdx=0;dayIdx<detailData.length;dayIdx++){
			var dayData = detailData[dayIdx];
			//添加一天
			addDay(dayData);
			//添加站点
			var siteDatas = dayData.articleList;
			if(siteDatas&&siteDatas.length>0){
				for(var siteIndex=0;siteIndex<siteDatas.length;siteIndex++){
					var siteData = siteDatas[siteIndex];
					addSite($("#day_"+dayIdx+"_addSiteBtn"),siteData);
					var images = siteData.images;
					if(!images){
						images="[]";
					}
					try {
						images = eval(images);
					} catch (e) {
					}
					if(images&&images.length>0){
						for(var imageIndex=0;imageIndex<images.length;imageIndex++){
							var imageData = images[imageIndex];
							addImageData(dayIdx,siteIndex,imageData);
						}
					}
				}	
			}
		}
	}
//});