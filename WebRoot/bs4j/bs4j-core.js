/*!
 * bs4j core v1.0 (http://bs4j.imlzw.com)
 * Copyright 2015-2025 imlzw, Inc.
 * 
 */
var bs4j = {
		checkTable : function(e){
			var checkbox = $(".table tr td:first-child input[type=checkbox]");
			checkbox.prop("checked",e.checked);
		},
		/**
		 * 设置主面板
		 * @param src
		 * @param backSrc 回退地址
		 */
		setMainPanel : function(src,backSrc){
			$("#main").load(src);
		},
		ajaxJson:function(src,data,callback){
			if(src){
				var srcInfo = src.indexOf("?");
				var uri = srcInfo[0];
				if(!data){
					data = {};
				}
				if(srcInfo.length>1){
					var urlParam = srcInfo("?")[1];
					var urlParams = urlParam.split("&");
					if(urlParams.length>0){
						for(var param in urlParams){
							if(param&&param.split){
								var paramInfo = param.split("=");
								if(paramInfo.length>1){
									var dd = data[paramInfo[0]];
									if(dd){
										dd[dd.length] = paramInfo[1]; 
									} 
								}
							}
						}
					}
				}
				$.getJSON(uri,data,callback);	
			}else{
				alert("请求地址不能为空!")
			}
		}
}