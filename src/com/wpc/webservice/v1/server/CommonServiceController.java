package com.wpc.webservice.v1.server;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rongji.dfish.engines.util.Utils;
import com.wpc.commons.sql.SearchContainer;
import com.wpc.commons.sql.SearchContainer.Op;
import com.wpc.dfish.dao.Page;
import com.wpc.persistence.BaseObject;
import com.wpc.persistence.Image;
import com.wpc.persistence.Images;
import com.wpc.persistence.User;
import com.wpc.service.ServiceLocator;
import com.wpc.utils.Distance;
import com.wpc.utils.SystemEnvInfo;
import com.wpc.utils.string.StringUtils;
import com.wpc.webservice.v1.vo.CallResult;

/**
 * 通用api服务
 * 
 * @author imlzw
 *
 */
public class CommonServiceController extends ServiceBaseController {
	protected final static Gson raidersGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat pathFormat = new SimpleDateFormat("yyyy"+File.separator+"MM"+File.separator+"dd");
	private static final Logger logger = Logger.getLogger(CommonServiceController.class);
	
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uri = request.getRequestURI().substring(request.getContextPath().length());
		String action = uri.substring(uri.lastIndexOf("/")+1);
		if("indexad".equals(action)){//首页
			//return indexAd(request, response);
			return null;
		}else{
			CallResult callResult = new CallResult();
			callResult.setCode(ERROR);
			callResult.setMessage("api地址["+uri+"]不存在!");
			responseText(response, getEncryJson(callResult));
			return null;
		}
	}

	

	/**
	 * 获取攻略的数据列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private ModelAndView raidersList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*Map<String, String> requestParams = getRequestParams();
		Map<String, String> userAgent1Header = getUserAgent1Header();
		String cityId = requestParams.get("cityId");
		String month = requestParams.get("month");
		String dayCount = requestParams.get("dayCount");
		String tag = requestParams.get("tag");
		String keyword = requestParams.get("keyword");
		String cityIdTop = userAgent1Header.get("cityId");
		String cityName = userAgent1Header.get("cityName");
		CallResult callResult = new CallResult();
		callResult.setResultCode(ERROR);
		callResult.setResultMessage("服务器未知错误!");
		Page page = getPage();
		SearchContainer searchContainer = new SearchContainer();
		searchContainer.and("t.typeDesc",Op.NULL, "");
		if(Utils.isEmpty(keyword)){
			if(Utils.notEmpty(cityId)){
				searchContainer.and("t.cityId", Op.EQ, StringUtils.leftPad(cityId,8,"0"));
			}else if(Utils.notEmpty(cityIdTop)){
				searchContainer.and("t.cityId", Op.EQ, StringUtils.leftPad(cityIdTop,8,"0"));
			}
		}
		
		if(Utils.notEmpty(cityId)){
			searchContainer.and("t.cityId", Op.EQ, cityId);
		}
		
		if(Utils.notEmpty(month)){
			SimpleDateFormat msdf = new SimpleDateFormat("MM");
			month = msdf.format(msdf.parse(month));
			searchContainer.and("t.month", Op.LIKE, month);
		}
		if(Utils.notEmpty(dayCount)){
			searchContainer.and("t.dayCount", Op.EQ, dayCount);
		}
		if(Utils.notEmpty(tag)){
			searchContainer.and("t.tag", Op.EQ, tag);
		}
		if(Utils.notEmpty(keyword)){
			searchContainer.and("t.title", Op.LIKE, keyword);
		}
		Map resultData = new LinkedHashMap();
		try {
			List<String> collects = null;
			List<String> praises = null;
			String loginId = userAgent1Header.get("login");
			if(Utils.notEmpty(loginId)){
				String userId = StringUtils.leftPad(loginId, 8, '0');
				collects = ServiceLocator.getCollectService().getCollectsByUserId(userId, "4");
				praises = ServiceLocator.getPraiseService().getPraisesByUserId(userId, "4");
			}
			List list = new ArrayList();
			List<Raiders> raidersList = null;
		
			raidersList = ServiceLocator.getRaidersService().findRaiderssByPage(page, searchContainer);
			if(Utils.notEmpty(raidersList)){
				for(Raiders raiders : raidersList){
					if(raiders!=null){
						if(Utils.notEmpty(raiders.getMonth())){
							SimpleDateFormat msdf = new SimpleDateFormat("M");
							String[] ss = raiders.getMonth().split(",");
							StringBuilder monthSb = new StringBuilder();
							for(String s : ss){
								try {
									monthSb.append(",").append(msdf.format(msdf.parse(s)));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							raiders.setMonth(monthSb.substring(1));
						}
						LinkedHashMap one = new LinkedHashMap();
						one.put("id", raiders.getId());
						one.put("title", raiders.getTitle());
						one.put("cityId", raiders.getCityId());
						one.put("departureTime", raiders.getStartTime());
						one.put("dayCount", raiders.getDayCount());
						one.put("praiseCount", raiders.getPraiseCount());
						one.put("commentCount", raiders.getCommentCount());
						one.put("month", raiders.getMonth());
						one.put("tag", raiders.getTag());
						String images = raiders.getImages();
						Image imageObject = gson.fromJson(images, Image.class);
						if(imageObject!=null){
							one.put("images",imageObject.getFullUrlImage());
						}else{
							one.put("images",imageObject);
						}
						if(null != collects){
							one.put("isCollect", collects.contains(raiders.getId())?1:0);
						}
						if(null != praises){
							one.put("isPraise", praises.contains(raiders.getId())?1:0);
						}
						list.add(one);
					}
				}
			}
			resultData.put("list", list);
			resultData.put("pageIndex", page.getCurrentPage());
			resultData.put("pageSize", page.getPageSize());
			resultData.put("pageCount", page.getPageCount());
			resultData.put("total", page.getRowCount());
			callResult.setResultCode(SUCCESS);
			callResult.setResultMessage("获取攻略列表成功");
			callResult.setResultData(resultData);
		} catch (Exception e) {
			e.printStackTrace();
			callResult.setResultMessage("服务器异常："+e.getMessage());
		}
		responseText(response, getEncryJson(callResult));*/
		return null;
	}

	
	public static void main(String[] args) {
		String enBASE64 = StringUtils.enBASE64("{\"type\":\"1\",\"id\":\"1\"}");
		System.out.println("0"+enBASE64);
		System.out.println(StringUtils.deBASE64("eyJSZXN1bHRDb2RlIjoiRXJyb3IiLCJSZXN1bHRNZXNzYWdlIjoiw9zC67K71f3ItyEifQ=="));
		System.out.println(StringUtils.deBASE64("eyJwYXNzd29yZCI6IjIxMjMyRjI5N0E1N0E1QTc0Mzg5NEEwRTRBODAxRkMzIiwidXNlcm5hbWUiOiJhZG1pbiJ9"));
	}
	
}
