package com.wpc.utils;

import static com.rongji.dfish.framework.FrameworkConstants.SHORT_DATEFORMAT;
import static com.wpc.dfish.util.Utils.notEmpty;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rongji.dfish.framework.FrameworkConstants;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.framework.SystemData;
import com.wpc.dfish.util.FileUtil;
import com.wpc.dfish.util.Utils;

public class FileTypeUtils {
	
	/**
	 * 后缀名与对应文件类型集合
	 */
	public final static Map<String,FileType> fileExtTypeMapping =  new HashMap<String,FileType>();
	static{// 图片扩展名
		fileExtTypeMapping.put("art", FileType.IMAGE);
		fileExtTypeMapping.put("bmp", FileType.IMAGE);
		fileExtTypeMapping.put("dib", FileType.IMAGE);
		fileExtTypeMapping.put("gif", FileType.IMAGE);
		fileExtTypeMapping.put("ico", FileType.IMAGE);
		fileExtTypeMapping.put("ief", FileType.IMAGE);
		fileExtTypeMapping.put("jpe", FileType.IMAGE);
		fileExtTypeMapping.put("jpeg", FileType.IMAGE);
		fileExtTypeMapping.put("jpg", FileType.IMAGE);
		fileExtTypeMapping.put("mac", FileType.IMAGE);
		fileExtTypeMapping.put("pbm", FileType.IMAGE);
		fileExtTypeMapping.put("pct", FileType.IMAGE);
		fileExtTypeMapping.put("pgm", FileType.IMAGE);
		fileExtTypeMapping.put("pic", FileType.IMAGE);
		fileExtTypeMapping.put("pict", FileType.IMAGE);
		fileExtTypeMapping.put("png", FileType.IMAGE);
		fileExtTypeMapping.put("pnm", FileType.IMAGE);
		fileExtTypeMapping.put("pnt", FileType.IMAGE);
		fileExtTypeMapping.put("ppm", FileType.IMAGE);
		fileExtTypeMapping.put("psd", FileType.IMAGE);
		fileExtTypeMapping.put("qti", FileType.IMAGE);
		fileExtTypeMapping.put("qtif", FileType.IMAGE);
		fileExtTypeMapping.put("ras", FileType.IMAGE);
		fileExtTypeMapping.put("rgb", FileType.IMAGE);
		fileExtTypeMapping.put("svg", FileType.IMAGE);
		fileExtTypeMapping.put("svgz", FileType.IMAGE);
		fileExtTypeMapping.put("tif", FileType.IMAGE);
		fileExtTypeMapping.put("tiff", FileType.IMAGE);
		fileExtTypeMapping.put("wbmp", FileType.IMAGE);
		fileExtTypeMapping.put("xbm", FileType.IMAGE);
		fileExtTypeMapping.put("xpm", FileType.IMAGE);
		fileExtTypeMapping.put("xwd", FileType.IMAGE);
		
		// 视频
		fileExtTypeMapping.put("asf", FileType.VIDEO);
		fileExtTypeMapping.put("asx", FileType.VIDEO);
		fileExtTypeMapping.put("avi", FileType.VIDEO);
		fileExtTypeMapping.put("avx", FileType.VIDEO);
		fileExtTypeMapping.put("dv", FileType.VIDEO);
		fileExtTypeMapping.put("flv", FileType.VIDEO);
		fileExtTypeMapping.put("mov", FileType.VIDEO);
		fileExtTypeMapping.put("movie", FileType.VIDEO);
		fileExtTypeMapping.put("mp4", FileType.VIDEO);
		fileExtTypeMapping.put("mpe", FileType.VIDEO);
		fileExtTypeMapping.put("mpeg", FileType.VIDEO);
		fileExtTypeMapping.put("mpega", FileType.VIDEO);
		fileExtTypeMapping.put("mpg", FileType.VIDEO);
		fileExtTypeMapping.put("mpv2", FileType.VIDEO);
		fileExtTypeMapping.put("qt", FileType.VIDEO);
		fileExtTypeMapping.put("wmv", FileType.VIDEO);
		fileExtTypeMapping.put("rm", FileType.VIDEO);
		fileExtTypeMapping.put("rmvb", FileType.VIDEO);

		// 文档
		fileExtTypeMapping.put("txt", FileType.DOCUMENT);
		fileExtTypeMapping.put("doc", FileType.DOCUMENT);
		fileExtTypeMapping.put("docx", FileType.DOCUMENT);
		fileExtTypeMapping.put("xls", FileType.DOCUMENT);
		fileExtTypeMapping.put("xlsx", FileType.DOCUMENT);
		fileExtTypeMapping.put("pdf", FileType.DOCUMENT);
		fileExtTypeMapping.put("ppt", FileType.DOCUMENT);
		fileExtTypeMapping.put("pptx", FileType.DOCUMENT);
		fileExtTypeMapping.put("wps", FileType.DOCUMENT);
		fileExtTypeMapping.put("dps", FileType.DOCUMENT);
		fileExtTypeMapping.put("et", FileType.DOCUMENT);
		
		//音频
		fileExtTypeMapping.put("ogg", FileType.AUDIO);
		fileExtTypeMapping.put("wav", FileType.AUDIO);
		fileExtTypeMapping.put("flac", FileType.AUDIO);
		fileExtTypeMapping.put("ape", FileType.AUDIO);
		fileExtTypeMapping.put("mp3", FileType.AUDIO);
		fileExtTypeMapping.put("wma", FileType.AUDIO);
		
		fileExtTypeMapping.put("com.rongji.filecenter.persistence.FcFiles", FileType.OTHER);
		
		}
	
	/**
	 * 文件类型
	 * @author XFB
	 *
	 */
	public enum FileType {
		IMAGE("图片", "IMAGE"), VIDEO("视频", "VIDEO"), DOCUMENT("文档", "DOCUMENT"), AUDIO("音频", "AUDIO"),  OTHER("其他", "OTHER");
		private String name = null;
		private String value = null;
	
		private FileType(String name, String value) {
			this.name = name;
			this.value = value;
		}
	
	}
	

	public static final String FILE_TYPE_TEXT = "1";// 文本文档
	public static final String FILE_TYPE_IMAGE = "2";// 图片文件
	public static final String FILE_TYPE_APPLICATION = "3";// 应用文件
	public static final String FILE_TYPE_VIDEO = "4";// 视频文件
	public static final String FILE_TYPE_AUDIO = "5";// 音频文件
	public static final String FILE_TYPE_X_WORLD = "6";// x-world文件
	public static final String FILE_TYPE_DEFAULT = "0";// 其它文件

	// TODO 文件图标待完善
	private static final String[] FILE_TYPE_IMGS = { "img/p/t/file.png", "img/p/t/file.png", "img/p/e/image.gif", "img/b/rtf.gif", "img/p/wmp4ul.gif", "img/p/wmp4ul.gif", "img/b/exam.gif" };
	
	private static final String[] EXT = { "jpg", "gif", "zip", "rar", "bmp", "png", "doc", "docx","wps", "xls","xlsx","et","ppt","pptx","dps","html", "xml", "js", "mov", "mp4", "flv", "rm", "rmvb", "wmv", "swf", "txt", "mp3" };
	private static final String[] EXT_NAME = { "JPEG 图像", "GIF 图像", "WinRAR 压缩文件", "rar", "bmp", "png", "DOC文档", "DOCX文档","WPS文字文档","XLS文档","XLSX文档","WPS表格/工作簿","PPT文档","PPTX文档","WPS演示文档","HTML 文档", "XML 文档", "JScript Script File", "mov", "mp4", "FLV 文件", "RM 文件", "RMVB 文件", "WMV 文件", "swf", "文本文档", "MP3 音频文件" };
	
	public static String getExtension(String fileName) {
		if (fileName != null && !fileName.equals("")) {
			return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase(); // 将后缀统一为小写，方便判断文件类型
		}
		return fileName;
	}
	/**
	 * 获取文件类型名称
	 * 
	 * @param fileType
	 * @return
	 */
	public static String getFileTypeName(Locale loc, String fileType) {
		String[] FILE_TYPE_NAMES = { "其它文件", "文本文档", "图片文件", "应用文件", "视频文件", "音频文件", "x-world文件" };
		if (Utils.isEmpty(fileType)) {
			return FILE_TYPE_NAMES[0];
		}
		int i = Integer.parseInt(fileType);
		return FILE_TYPE_NAMES[i];
	}
	
	/**
	 * 获取文件类型
	 * 
	 * @param fileType
	 * @return
	 */
	public static String getFileTypeName(String fileType) {
		String fileTypeName = "";
		for (int i = 0; i < EXT.length; i ++) {
			if (EXT[i].equalsIgnoreCase(fileType)) {
				fileTypeName = EXT_NAME[i];
				break;
			}
		}
		if (Utils.isEmpty(fileTypeName)) {
			fileTypeName = "未知类型";
		}
		return fileTypeName;
	}
	
	/**
	 * 获取文件后缀图标
	 * 
	 * @param suffix
	 *            文件后缀名/扩展名
	 * @return
	 */
	public static String getFileSuffixImg(String suffix) {
		String suffixImg = "";
		for (int i = 0; i < EXT.length; i ++) {
			if (EXT[i].equalsIgnoreCase(suffix)) {
				suffixImg = "img/b/res/fileType/" + EXT[i] + ".gif";
				break;
			}
		}
		if (Utils.isEmpty(suffixImg)) {
			suffixImg = "img/b/res/fileType/unknown.gif";
		}
		return suffixImg;
	}
	
	/**
	 * 获取文件对应图标
	 * 
	 * @param fileType
	 * @return
	 */
	public static String getFileTypeImg(String fileType) {
		if (Utils.isEmpty(fileType)) {
			return FILE_TYPE_IMGS[0];
		}
		int i = Integer.parseInt(fileType);
		return FILE_TYPE_IMGS[i];
	}

	/**
	 * 获取文件类型集合
	 * 
	 * @param loc
	 * @return
	 */
	public static List<String[]> getFileTypeList(Locale loc) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[] { FILE_TYPE_TEXT, getFileTypeName(loc, FILE_TYPE_TEXT) });
		list.add(new String[] { FILE_TYPE_IMAGE, getFileTypeName(loc, FILE_TYPE_IMAGE) });
		list.add(new String[] { FILE_TYPE_APPLICATION, getFileTypeName(loc, FILE_TYPE_APPLICATION) });
		list.add(new String[] { FILE_TYPE_VIDEO, getFileTypeName(loc, FILE_TYPE_VIDEO) });
		list.add(new String[] { FILE_TYPE_AUDIO, getFileTypeName(loc, FILE_TYPE_AUDIO) });
		list.add(new String[] { FILE_TYPE_X_WORLD, getFileTypeName(loc, FILE_TYPE_X_WORLD) });
		list.add(new String[] { FILE_TYPE_DEFAULT, getFileTypeName(loc, FILE_TYPE_DEFAULT) });
		return list;
	}

	/**
	 * 根据上传文件的contentType属性获取文件的文件类型（fileType）
	 * 
	 * @param contentType
	 * @return
	 */
	public static String getFileContentType(String contentType) {
		if (contentType.startsWith("text")) {
			return FILE_TYPE_TEXT;
		}
		if (contentType.startsWith("image")) {
			return FILE_TYPE_IMAGE;
		}
		if (contentType.startsWith("application")) {
			return FILE_TYPE_APPLICATION;
		}
		if (contentType.startsWith("video")) {
			return FILE_TYPE_VIDEO;
		}
		if (contentType.startsWith("audio")) {
			return FILE_TYPE_AUDIO;
		}
		if (contentType.startsWith("x-world")) {
			return FILE_TYPE_X_WORLD;
		}
		return FILE_TYPE_DEFAULT;
	}

	private static final Object SYNC_LOCK_OBJECT = new Object();
	private static HashMap<String, String> categoryNameMap;

	/**
	 * 清空栏目名称缓存
	 */
	public static void clearFileCategoryNameMap() {
		if (categoryNameMap != null)
			categoryNameMap.clear();
	}


	// private static HashMap<String, String> attachUserNameMap;
	//
	// public static void clearAttachUserNameMapNameMap() {
	// if (attachUserNameMap != null) {
	// attachUserNameMap.clear();
	// attachUserNameMap = null;
	// }
	// }
	//
	// /**
	// * 获取拥有个人附件的人员名称
	// *
	// * @param userId
	// * @return
	// */
	// public static String getAttachUserName(String userId) {
	// if (userId == null) {
	// return "------";
	// }
	// if (attachUserNameMap == null) {
	// synchronized (SYNC_LOCK_OBJECT) {
	// if (attachUserNameMap == null) {
	// attachUserNameMap = new HashMap<String, String>();
	// List list = FrameworkHelper.getDAO().getQueryList(
	// "SELECT DISTINCT(t.userId) FROM CmsPersonAttach t");
	// if (list != null) {
	// HashSet<String> set = new HashSet<String>();
	// set.addAll(list);
	// DfishSoapManagerImpl ins = DfishSoapManagerImpl.getInstance();
	// for (Iterator iterator = set.iterator(); iterator.hasNext();) {
	// String uId = (String) iterator.next();
	// attachUserNameMap.put(uId, ins.getUserNameById(uId));
	// }
	// }
	// }
	// }
	// }
	// String name = attachUserNameMap.get(userId);
	// if (name == null) {
	// name = DfishSoapManagerImpl.getInstance().getUserNameById(userId);
	// attachUserNameMap.put(userId, name);
	// return name;
	// }
	// return name;
	// }


	/**
	 * 获取用户附件在服务器上存储的根目录
	 * 
	 * @return
	 */
	public static String getUserAttachsRootPath() {
		return FrameworkHelper.getSystemConfig("sysconfig.cmsUserAttachsRootDirectory", SystemData.getInstance().getServletInfo().getServletRealPath() + "user_attachs");
	}



	public static String getUrlParametersString(String userId, String cateId, String fileType, boolean isAdmin,
			int currentPage) {
		return (isAdmin ? "&isAdmin=y" : "") + (notEmpty(userId) ? "&userId=" + userId : "")
				+ (notEmpty(cateId) ? "&cateId=" + cateId : "") + (notEmpty(fileType) ? "&fileType=" + fileType : "")
				+ (notEmpty("" + currentPage) ? "&cp=" + currentPage : "");
	}

	/**
	 * 导出所选的附件到一个zip文件中供客户端下载
	 * 
	 * @param response
	 * @param id
	 */
	public static void exportFiles2Zip(HttpServletResponse response, String[] id) throws IOException {
		if (id == null || id.length == 0)
			return;
		String todayStr = SHORT_DATEFORMAT.format(new Date());
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("Accept-Charset", "UTF-8");
		response.setHeader("Content-type", "application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=CMS-Resource_" + todayStr + ".zip");

		OutputStream os = null;
//		ZipOutputStream zipOS = null;
		String rootPath = getUserAttachsRootPath();
		try {/*
			os = response.getOutputStream();
			zipOS = new ZipOutputStream(os);
			for (int i = 0; i < id.length; i++) {
				String attachId = id[i];
				CmsPersonAttach attach = AttachMgtMethods.findAttachById(attachId);
				if (attach != null) {
					File file = new File(rootPath + attach.getAttachUrl());
					ZipHandle.compressFile2Zip(file, zipOS, attach.getAttachName());
				}
			}
		*/} catch (Exception e) {
		} finally {
//			if (zipOS != null) {
//				zipOS.close();
//			}
			if (os != null) {
				os.close();
			}
		}

	}
	public static String getFileCategory(String contentType) {
		if(Utils.notEmpty(contentType)){
			return contentType.substring(0,contentType.indexOf("/"));
		}
		return "othor";
	}

	public static int temporarySave(){
		
		return 0;
	}
	
	/**
	 * TODO 2011-3-31 lindent
	 * 判断是否是允许的图片扩展名  
	 * @param ext
	 * @return
	 */
	public static boolean isAllowImgExt(String ext){
		return "jpg,jpeg,gif,png,bmp".indexOf(ext.toLowerCase()) > 0;
	}
	
	public static Map<String, FileType> getFileExtTypeMapping() {
		return fileExtTypeMapping;
	}


	/**
	 * 根据文件后缀名取得文件所属类型
	 * @param ext
	 * @return
	 */
	public static FileType getFileType(String ext) {
		FileType ft = fileExtTypeMapping.get(ext);
		if (ft == null) {
			ft = FileType.OTHER;
		}
		return ft;
	}
	//images
//	CONTENT_TYPES.put("art", "image/x-jg");
//	CONTENT_TYPES.put("bmp", "image/bmp");
//	CONTENT_TYPES.put("dib", "image/bmp");
//	CONTENT_TYPES.put("gif", "image/gif");
//	CONTENT_TYPES.put("ico", "image/x-icon");
//	CONTENT_TYPES.put("ief", "image/ief");
//	CONTENT_TYPES.put("jpe", "image/jpeg");
//	CONTENT_TYPES.put("jpeg", "image/jpeg");
//	CONTENT_TYPES.put("jpg", "image/jpeg");
//	CONTENT_TYPES.put("mac", "image/x-macpaint");
//	CONTENT_TYPES.put("pbm", "image/x-portable-bitmap");
//	CONTENT_TYPES.put("pct", "image/pict");
//	CONTENT_TYPES.put("pgm", "image/x-portable-graymap");
//	CONTENT_TYPES.put("pic", "image/pict");
//	CONTENT_TYPES.put("pict", "image/pict");
//	CONTENT_TYPES.put("png", "image/png");
//	CONTENT_TYPES.put("pnm", "image/x-portable-anymap");
//	CONTENT_TYPES.put("pnt", "image/x-macpaint");
//	CONTENT_TYPES.put("ppm", "image/x-portable-pixmap");
//	CONTENT_TYPES.put("psd", "image/x-photoshop");
//	CONTENT_TYPES.put("qti", "image/x-quicktime");
//	CONTENT_TYPES.put("qtif", "image/x-quicktime");
//	CONTENT_TYPES.put("ras", "image/x-cmu-raster");
//	CONTENT_TYPES.put("rgb", "image/x-rgb");
//	CONTENT_TYPES.put("svg", "image/svg+xml");
//	CONTENT_TYPES.put("svgz", "image/svg+xml");
//	CONTENT_TYPES.put("tif", "image/tiff");
//	CONTENT_TYPES.put("tiff", "image/tiff");
//	CONTENT_TYPES.put("wbmp", "image/vnd.wap.wbmp");
//	CONTENT_TYPES.put("xbm", "image/x-xbitmap");
//	CONTENT_TYPES.put("xpm", "image/x-xpixmap");
//	CONTENT_TYPES.put("xwd", "image/x-xwindowdump");
	//video
//	CONTENT_TYPES.put("asf", "video/x-ms-asf");
//	CONTENT_TYPES.put("asx", "video/x-ms-asf");
//	CONTENT_TYPES.put("avi", "video/x-msvideo");
//	CONTENT_TYPES.put("avx", "video/x-rad-screenplay");
//	CONTENT_TYPES.put("dv", "video/x-dv");
//	CONTENT_TYPES.put("flv", "video/x-flv");
//	CONTENT_TYPES.put("mov", "video/quicktime");
//	CONTENT_TYPES.put("movie", "video/x-sgi-movie");
//	CONTENT_TYPES.put("mp4", "video/mp4");
//	CONTENT_TYPES.put("mpe", "video/mpeg");
//	CONTENT_TYPES.put("mpeg", "video/mpeg");
//	CONTENT_TYPES.put("mpega", "video/x-mpeg");
//	CONTENT_TYPES.put("mpg", "video/mpeg");
//	CONTENT_TYPES.put("mpv2", "video/mpeg2");
//	CONTENT_TYPES.put("qt", "video/quicktime");
//	CONTENT_TYPES.put("wmv", "video/x-ms-wmv");
	//audio
//	CONTENT_TYPES.put("abs", "audio/x-mpeg");
//	CONTENT_TYPES.put("aif", "audio/x-aiff");
//	CONTENT_TYPES.put("aifc", "audio/x-aiff");
//	CONTENT_TYPES.put("aiff", "audio/x-aiff");
//	CONTENT_TYPES.put("au", "audio/basic");
//	CONTENT_TYPES.put("kar", "audio/midi");
//	CONTENT_TYPES.put("m3u", "audio/x-mpegurl");
//	CONTENT_TYPES.put("mid", "audio/midi");
//	CONTENT_TYPES.put("midi", "audio/midi");
//	CONTENT_TYPES.put("mp1", "audio/x-mpeg");
//	CONTENT_TYPES.put("mp2", "audio/mpeg");
//	CONTENT_TYPES.put("mp3", "audio/mpeg");
//	CONTENT_TYPES.put("mpa", "audio/x-mpeg");
//	CONTENT_TYPES.put("mpega", "audio/x-mpeg");
//	CONTENT_TYPES.put("pls", "audio/x-scpls");
//	CONTENT_TYPES.put("smf", "audio/x-midi");
//	CONTENT_TYPES.put("snd", "audio/basic");
//	CONTENT_TYPES.put("ulw", "audio/basic");
//	CONTENT_TYPES.put("wav", "audio/x-wav");
	//text
//	CONTENT_TYPES.put("css", "text/css");
//	CONTENT_TYPES.put("htc", "text/x-component");
//	CONTENT_TYPES.put("htm", "text/html");
//	CONTENT_TYPES.put("html", "text/html");
//	CONTENT_TYPES.put("java", "text/plain");
//	CONTENT_TYPES.put("js", "text/javascript");
//	CONTENT_TYPES.put("rtf", "text/rtf");
//	CONTENT_TYPES.put("rtx", "text/richtext");
//	CONTENT_TYPES.put("txt", "text/plain");
//	CONTENT_TYPES.put("wml", "text/vnd.wap.wml");
//	CONTENT_TYPES.put("wmls", "text/vnd.wap.wmlscript");
	//application
//	CONTENT_TYPES.put("cer", "application/x-x509-ca-cert");
//	CONTENT_TYPES.put("doc", "application/msword");
//	CONTENT_TYPES.put("docx", "application/msword");
//	CONTENT_TYPES.put("dtd", "application/xml-dtd");
//	CONTENT_TYPES.put("pdf", "application/pdf");
//	CONTENT_TYPES.put("pps", "application/vnd.ms-powerpoint");
//	CONTENT_TYPES.put("ppt", "application/vnd.ms-powerpoint");
//	CONTENT_TYPES.put("pptx", "application/vnd.ms-powerpoint");
//	CONTENT_TYPES.put("rm", "application/vnd.rn-realmedia");
//	CONTENT_TYPES.put("swf", "application/x-shockwave-flash");
//	CONTENT_TYPES.put("vsd", "application/x-visio");
//	CONTENT_TYPES.put("xls", "application/vnd.ms-excel");
//	CONTENT_TYPES.put("xlsx", "application/vnd.ms-excel");
//	CONTENT_TYPES.put("xml", "application/xml");
//	CONTENT_TYPES.put("xsl", "application/xml");
//	CONTENT_TYPES.put("xslt", "application/xslt+xml");
}