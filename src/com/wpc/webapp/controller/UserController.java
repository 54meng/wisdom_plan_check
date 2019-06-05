package com.wpc.webapp.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.User;
import com.wpc.service.ServiceLocator;
import com.wpc.utils.FileTypeUtils;
import com.wpc.utils.FileUtil;
import com.wpc.utils.SecurityUtils;
import com.wpc.utils.SystemEnvInfo;
import com.wpc.utils.string.StringUtils;
import com.wpc.webapp.controller.utils.DataTableUtils;
import com.wpc.webapp.controller.vo.CallResult;
import com.wpc.webapp.controller.vo.DTRequest;
import com.wpc.webapp.controller.vo.TableData;

/**
 * 用户管理控制类
 * 
 * @author imlzw
 *
 */
public class UserController extends MultiActionController {
	Gson gson = new Gson();
	SimpleDateFormat pathFormat = new SimpleDateFormat("yyyy"+File.separator+"MM"+File.separator+"dd");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	int index = 1;
	
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
					ServiceLocator.getUserService().deleteByIds(dtRequest.getSelectItem(), true);
				}
			}
		}
		//过滤条件
		SearchContainer searchContainer = dtRequest.getSearchContainer();
		searchContainer.orderByDesc("t.userid");
		
		List<User> userList = ServiceLocator.getUserService().findUsersByPage(page, searchContainer);
		TableData tableData = new TableData();
		if(Utils.notEmpty(userList)){
			for(User user : userList){
				tableData.addDataItem(
						"<input type='checkbox' name='userId' value='"+user.getUserid()+"'>",						
						user.getUsername(),			
						user.getPhone(),
						user.getCompany(),
						user.getIdcard(),
						user.getSexText(),
						user.getUserRoleText(),
						user.getUserStatusText(),
						user.getAllowLoginText(),
						simpleDateFormat.format(user.getCreate_time()),				
						"<a href='../../../user.sp?act=addEditUser&userId="+user.getUserid()+"' data-toggle='modal' data-target='#ajax'  class='btn btn-xs default'><i class='fa fa-search'></i>编辑</a>"
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
	
	public ModelAndView addEditUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String userId = request.getParameter("userId");
		User user = ServiceLocator.getUserService().getUserById(userId);
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/user_add_edit.jsp");
		modelAndView.addObject("user", user==null?new User():user);
		String avatar = null;
		if(user!=null){
			avatar = user.getAvatar_url();
		}
		String thumb = null;
		if(Utils.notEmpty(avatar)){
			thumb = SystemEnvInfo.getBasePath()+avatar.replace("org","thumb.80x80");
			thumb = thumb.replace(File.separator,"/")+"?nocache="+(new Date().getTime());
		}
		modelAndView.addObject("thumb", thumb);
		modelAndView.addObject("isNew", user==null);
		return modelAndView;
	}
	
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String property = request.getSession().getServletContext().getRealPath("");
		System.out.println(property);
		String userId = request.getParameter("userid");
		User user = ServiceLocator.getUserService().getUserById(userId);
		boolean isNew = false;
		if(user==null){
			isNew = true;
			user = new User();		
		}

		String username = Utils.getParameter(request, "username");
		String phone = Utils.getParameter(request, "phone");
		String password = Utils.getParameter(request, "password");
		String password2 = Utils.getParameter(request, "password2");
		String nickname = Utils.getParameter(request, "nickname");
		String userStatus = Utils.getParameter(request, "userStatus");
		String userRole = Utils.getParameter(request, "userRole");
		String company = Utils.getParameter(request, "company");
		String sex = Utils.getParameter(request, "sex");
		String idcard = Utils.getParameter(request, "idcard");
		String remark_name = Utils.getParameter(request, "remark_name");
		String allowLogin = Utils.getParameter(request, "allowLogin");
		CallResult callResult = new CallResult();
		if(!password.equals(password2)){
			callResult.setRet(1);
			callResult.setMsg("两次输入的密码不一致，请重新输入。");
			Utils.outPutJson(response,callResult);
			return null;
		}

		//判断密码是否修改
		if(!password.equals(user.getPassword())){
			String passwordMd5 = SecurityUtils.encryptPassword(StringUtils.md5(password));
			user.setPassword(passwordMd5);
		}		
		user.setUsername(username);
		user.setPhone(phone);
		user.setNickname(nickname);
		user.setCompany(company);
		user.setSex(sex);
		user.setIdcard(idcard);;
		user.setRemark_name(remark_name);
		user.setUserStatus(userStatus);
		user.setUserRole(userRole);
		user.setUserType("1"); //服务端用户
		user.setAllowLogin(allowLogin);

		try {
			if(isNew){
				user = ServiceLocator.getUserService().saveUser(user);
			}else{
				user = ServiceLocator.getUserService().updateUser(user);
			}
			callResult.setRet(0);
			callResult.setMsg("保存用户成功！");
			callResult.setRetValue(user.getUserid());
		} catch (Exception e) {
			e.printStackTrace();
			callResult.setRet(1);
			callResult.setMsg(e.getMessage());
		}
		//文件上传
		MultipartHttpServletRequest multipartRequest = null;
		String avatarUrl = null;
		if(request instanceof MultipartHttpServletRequest){
			multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile[] files = FrameworkHelper.getFiles(multipartRequest, "files");
			String relativePath = File.separator+"avatar"+File.separator+pathFormat.format(user.getCreate_time())+File.separator+user.getUserid();
			String floderPath = SystemEnvInfo.getResourcePath() + relativePath;
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					try {
						String originalFileName = files[i].getOriginalFilename();
						String ext = FileTypeUtils.getExtension(originalFileName); //得到文件的后缀名和扩展名
						String realFileName = "org."+ext;
						long size = files[i].getSize();
						size = size / (1024 * 1024);// 以兆为单位
						File orgFile = FileUtil.saveFile(files[i].getInputStream(), floderPath, realFileName);
						//生成指定的规格的图片
						String thumb80x80Name = floderPath+File.separator+"thumb.80x80."+ext;
						Thumbnails.of(orgFile).size(80, 80).toFile(thumb80x80Name);
						String thumb400x400Name = floderPath+File.separator+"thumb.400x400."+ext;
						Thumbnails.of(orgFile).size(400, 400).toFile(thumb400x400Name);
						String thumb800x800Name = floderPath+File.separator+"thumb.800x800."+ext;
						Thumbnails.of(orgFile).size(800, 800).toFile(thumb800x800Name);
						avatarUrl = SystemEnvInfo.getResourceRelativePath()+relativePath+File.separator+realFileName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(Utils.notEmpty(avatarUrl)){
			user.setAvatar_url(avatarUrl);
			ServiceLocator.getUserService().updateUser(user);
		}
		//以JSON格式输出
		Utils.outPutJson(response,callResult);
		return null;
		
	}
	
}
