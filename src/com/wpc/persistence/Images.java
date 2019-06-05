package com.wpc.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rongji.dfish.engines.util.Utils;
import com.wpc.utils.SystemEnvInfo;

/**
 * 通用的images的json对象
 * 
 * @author imlzw
 *
 */
public class Images {
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	private List<Image> images;
	public Images(){
		images = new ArrayList<Image>();
	}
	
	public List<Image> getImages() {
		return images;
	}
	
	public List<Image> getFullUrlImages() {
		List<Image> fullUrlImages = null;
		if(null != images){
			fullUrlImages = new ArrayList<Image>();
			for(Image image : images){
				image.setOrgUrl(SystemEnvInfo.getBasePath()+image.getOrgUrl().replace(File.separator, "/"));
				image.setSmallUrl(SystemEnvInfo.getBasePath()+image.getSmallUrl().replace(File.separator, "/"));
				image.setLargeUrl(SystemEnvInfo.getBasePath()+image.getLargeUrl().replace(File.separator, "/"));
				image.setHdUrl(SystemEnvInfo.getBasePath()+image.getHdUrl().replace(File.separator, "/"));
				fullUrlImages.add(image);
			}
		}
		return fullUrlImages;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	/**
	 * 获取首图
	 * 
	 * @return Image
	 */
	public Image getConverImage(){
		Image image = null;
		for(Image img:images){
			if(img.isConver()){
				image = img;
				break;
			}
		}
		return image;
	}
	
	/**
	 * 转为json
	 * 
	 * @return
	 */
	public String toJson(){
		return gson.toJson(this);
	}
	
	/**
	 * json转为Images对象
	 * @param imagesJson
	 * @return
	 */
	public static Images fromJson(String imagesJson){
		Images images = null;
		try {
			if(Utils.notEmpty(imagesJson)){
				images = gson.fromJson(imagesJson, Images.class);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return images;
	}

}
