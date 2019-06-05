package com.wpc.webapp.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.wpc.commons.sql.SearchContainer;
import com.wpc.commons.sql.SearchContainer.Op;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Author;
import com.wpc.persistence.Category;
import com.wpc.persistence.Special;
import com.wpc.persistence.Tag;
import com.wpc.persistence.Team;
import com.wpc.persistence.User;
import com.wpc.persistence.Video;
import com.wpc.service.ServiceLocator;
import com.wpc.utils.FileTypeUtils;
import com.wpc.utils.FileUtil;
import com.wpc.utils.FileUtils;
import com.wpc.utils.SecurityUtils;
import com.wpc.utils.SystemEnvInfo;
import com.wpc.utils.string.StringUtils;
import com.wpc.webapp.controller.utils.DataTableUtils;
import com.wpc.webapp.controller.vo.CallResult;
import com.wpc.webapp.controller.vo.DTRequest;
import com.wpc.webapp.controller.vo.TableData;

/**
 * 视频管理控制类
 * 
 * @author imlzw
 *
 */
public class VideoController extends MultiActionController {
	Gson gson = new Gson();
	SimpleDateFormat pathFormat = new SimpleDateFormat("yyyy"+File.separator+"MM"+File.separator+"dd");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	int index = 1;
	
	public ModelAndView redirect(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Category> cateList = ServiceLocator.getCateService().findByPage(null, null);
		List<Special> specialList = ServiceLocator.getSpecialService().findByPage(null, null);
		List<Author> authorList = ServiceLocator.getAuthorService().findByPage(null, null);
		List<Tag> tagList = ServiceLocator.getTagService().findByPage(null, null);
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/video_upload.jsp");
		modelAndView.addObject("cateList", cateList);
		modelAndView.addObject("specialList", specialList);
		modelAndView.addObject("authorList", authorList);
		modelAndView.addObject("tagList", tagList);
		return modelAndView;
	}
	
	/**
	 * 加载数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView loadData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//获取DataTabel请求过来的数据
		DTRequest dtRequest = DataTableUtils.getDTRequest();
		Page page = dtRequest.getPage();
		//是否有批量操作？
		if(dtRequest.isHasGroupAction()){
			String action = dtRequest.getGroupAction();
			if(Utils.notEmpty(dtRequest.getSelectItem())){
				//批量删除操作
				if("delete".equals(action)){
					ServiceLocator.getVideoService().deleteByIds(dtRequest.getSelectItem());
				}
			}
		}
		//过滤条件
		SearchContainer searchContainer = dtRequest.getSearchContainer();
		searchContainer.orderByDesc("t.id");
		
		List<Video> list = ServiceLocator.getVideoService().findByPage(page, searchContainer, null);
		TableData tableData = new TableData();
		if(Utils.notEmpty(list)){
			String[] cateIdArray = new String[list.size()];
			String[] specialIdArray = new String[list.size()];
			String[] authorIdArray = new String[list.size()];
			int i = 0;
			for(Video video : list){
				cateIdArray[i] = video.getCateId();
				specialIdArray[i] = video.getSpecialId();
				authorIdArray[i] = video.getAuthorId();
				i++;
			}
			List<Category> cateList = ServiceLocator.getCateService().findByIds(cateIdArray);
			List<Special> specialList = ServiceLocator.getSpecialService().findByIds(specialIdArray);
			List<Author> authorList = ServiceLocator.getAuthorService().findByIds(authorIdArray);
			Map<String, String> cateMap = new HashMap<String, String>();
			Map<String, String> specialMap = new HashMap<String, String>();
			Map<String, String> authorMap = new HashMap<String, String>();
			if(Utils.notEmpty(cateList)){
				for(Category c : cateList){
					cateMap.put(c.getCateId(), c.getCateName());
				}
			}
			if(Utils.notEmpty(specialList)){
				for(Special s : specialList){
					specialMap.put(s.getId(), s.getName());
				}
			}
			if(Utils.notEmpty(authorList)){
				for(Author a : authorList){
					authorMap.put(a.getId(), a.getName());
				}
			}
			for(Video video : list){
				tableData.addDataItem(
						"<input type='checkbox' name='userId' value='"+video.getId()+"'>",
						video.getId(),	
						video.getName(),						
						cateMap.get(video.getCateId()),
						video.getScore(),
						video.getPlayCount(),
						video.getPraiseCount(),
						specialMap.get(video.getSpecialId()),
						authorMap.get(video.getAuthorId()),
						FileUtils.formetFileSize(Long.parseLong(video.getSize())),		
						simpleDateFormat.format(video.getCreateTime()),				
						"<a href='../../../video.sp?act=addOrEdit&videoId="+video.getId()+"'  class='btn btn-xs ajaxify default'><i class='fa fa-search'></i>编辑</a>"
						);
			}
		}
		//设置tableData的总记录与过滤记录
		tableData.setRecordsFiltered(page.getRowCount());
		tableData.setRecordsTotal(page.getRowCount());
		//以JSON格式输出
		Utils.outPutJson(response,tableData);
		return null;
		
	}
	
	public ModelAndView addOrEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String videoId = request.getParameter("videoId");
		Video video = ServiceLocator.getVideoService().findById(videoId);
		List<Category> cateList = ServiceLocator.getCateService().findByPage(null, null);
		List<Special> specialList = ServiceLocator.getSpecialService().findByPage(null, null);
		List<Author> authorList = ServiceLocator.getAuthorService().findByPage(null, null);
		List<Tag> tagList = ServiceLocator.getTagService().findByPage(null, null);
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/video_upload.jsp");
		modelAndView.addObject("video", video);		
		modelAndView.addObject("cateList", cateList);
		modelAndView.addObject("specialList", specialList);
		modelAndView.addObject("authorList", authorList);
		modelAndView.addObject("tagList", tagList);
		return modelAndView;
	}
	
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String videoId = request.getParameter("videoId");
		Video entity = ServiceLocator.getVideoService().findById(videoId);
		boolean isNew = false;
		if(entity==null){
			isNew = true;
			entity = new Video();		
		}

		String name = Utils.getParameter(request, "name");
		String cateId = Utils.getParameter(request, "cateId");
		String specialId = Utils.getParameter(request, "specialId");
		String authorId = Utils.getParameter(request, "authorId");
		String score = Utils.getParameter(request, "score");		
		String intros = Utils.getParameter(request, "intros");
		String videoUrl = Utils.getParameter(request, "videoUrl");
		String vsize = Utils.getParameter(request, "size");
		String[] tag = request.getParameterValues("tag");
		try {
			entity.setScore(Double.parseDouble(score));
		} catch (Exception e) {
			Utils.outPutJson(response, 2);
			return null;
		}	
		entity.setName(name);
		entity.setCateId(cateId);
		entity.setSpecialId(specialId);
		entity.setAuthorId(authorId);
		entity.setIntros(intros);
		entity.setVideoUrl(videoUrl);
		entity.setSize(vsize);
		entity.setCreateTime(new Date());
		entity.setCreator((String)request.getSession().getAttribute("userId"));
		if(Utils.notEmpty(tag)){
			StringBuilder sBuilder = new StringBuilder();
			for(String s : tag){
				sBuilder.append(",").append(s);
			}	
			List<Tag> tagList = ServiceLocator.getTagService().findByIds(tag);
			if(Utils.notEmpty(tagList)){
				entity.setTag(sBuilder.substring(1));
				entity.setTagJson(JSON.toJSONString(tagList));
			}
		}else{
			entity.setTag("");
			entity.setTagJson("");
		}
		
		//文件上传
		MultipartHttpServletRequest multipartRequest = null;
		String coverUrl1 = null;
		String coverUrl2 = null;
		if(request instanceof MultipartHttpServletRequest){
			multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile[] cover1 = FrameworkHelper.getFiles(multipartRequest, "cover1");
			MultipartFile[] cover2 = FrameworkHelper.getFiles(multipartRequest, "cover2");
			String relativePath = File.separator + "image";
			String floderPath = SystemEnvInfo.getResourcePath() + relativePath;
			if (cover1 != null && cover1.length > 0) {
				for (int i = 0; i < cover1.length; i++) {
					try {
						String originalFileName = cover1[i].getOriginalFilename();
						String ext = FileTypeUtils.getExtension(originalFileName); //得到文件的后缀名和扩展名
						String uuid = UUID.randomUUID().toString().replaceAll("-", "");
						String realFileName = uuid+"."+ext;
						long size = cover1[i].getSize();
						size = size / (1024 * 1024);// 以兆为单位
						File orgFile = FileUtil.saveFile(cover1[i].getInputStream(), floderPath, realFileName);						
						coverUrl1 = SystemEnvInfo.getResourceRelativePath()+relativePath+File.separator+realFileName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (cover2 != null && cover2.length > 0) {
				for (int i = 0; i < cover2.length; i++) {
					try {
						String originalFileName = cover2[i].getOriginalFilename();
						String ext = FileTypeUtils.getExtension(originalFileName); //得到文件的后缀名和扩展名
						String uuid = UUID.randomUUID().toString().replaceAll("-", "");
						String realFileName = uuid+"."+ext;
						long size = cover2[i].getSize();
						size = size / (1024 * 1024);// 以兆为单位
						File orgFile = FileUtil.saveFile(cover2[i].getInputStream(), floderPath, realFileName);						
						coverUrl2 = SystemEnvInfo.getResourceRelativePath()+relativePath+File.separator+realFileName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(Utils.notEmpty(coverUrl1)){
			entity.setCoverUrl1(coverUrl1);
		}	
		if(Utils.notEmpty(coverUrl2)){
			entity.setCoverUrl2(coverUrl2);
		}

		try {
			if(isNew){
				ServiceLocator.getVideoService().save(entity);
			}else{
				ServiceLocator.getVideoService().update(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		Utils.outPutJson(response, 1);
		return null;
		
	}
	
}
