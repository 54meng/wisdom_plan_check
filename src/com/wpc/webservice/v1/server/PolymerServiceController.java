package com.wpc.webservice.v1.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wpc.dfish.util.Utils;
import com.wpc.utils.HttpClientHelper;
import com.wpc.utils.string.StringUtils;
import com.wpc.webservice.v1.vo.CallResult;

/**
 * 聚合网api
 * 
 * @author whp
 *
 */
public class PolymerServiceController extends ServiceBaseController {
	
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uri = request.getRequestURI().substring(request.getContextPath().length());
		String action = uri.substring(uri.lastIndexOf("/")+1);
		if("country".equals(action)){
			country(request, response);
		}else if("province".equals(action)){
			province(request, response);
		}else if("city".equals(action)){
			city(request, response);
		}else if("location".equals(action)){
			location(request, response);
		}else if("theme".equals(action)){
			theme(request, response);
		}else if("search".equals(action)){
			search(request, response);
		}else if("staticdetail".equals(action)){
			staticdetail(request, response);
		}else if("flightsearch".equals(action)){
			flightsearch(request, response);
		}else if("cityCode".equals(action)){
			cityCode(request, response);
		}else if("ticketsAvailable".equals(action)){
			ticketsAvailable(request, response);
		}else if("citycode".equals(action)){
			citycode(request, response);
		}else if("airline".equals(action)){
			airline(request, response);
		}else if("trainStations".equals(action)){
			trainStations(request, response);
		}else{
			CallResult callResult = new CallResult();
			callResult.setCode(ERROR);
			callResult.setMessage("api地址["+uri+"]不存在!");
			responseText(response, getEncryJson(callResult));
			
		}
		return null;
	}
	
	protected void responseText(HttpServletResponse response,String text) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print(text);
	}
	
	protected String transformResult(String result, String sucStatus){		
		JSONObject out = new JSONObject();
		try {
			out.put("ResultCode", ERROR);
			out.put("ResultMessage", "空数据");
		} catch (Exception e) {}
		
		if(Utils.notEmpty(result)){
			try {
				JSONObject object = new JSONObject(result);
				Object ecode = object.get("error_code");
				Object reason = object.get("reason");
				Object rst = object.get("result");
									
				if(sucStatus.equals(ecode.toString())){
					out.put("ResultCode", SUCCESS);
				}else{
					out.put("ResultCode", ERROR);
				}
				out.put("ResultMessage", reason);
				JSONObject data = new JSONObject();
				data.put("list", rst);
				out.put("ResultData", data);
				System.out.println(out.toString());				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return "0"+StringUtils.enBASE64(out.toString());
	}
	
	/**
	 * 获取国家列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView country(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/static/country";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Key", "dcacd527ea14edf2e9bfadba3bfe0129");
		String result = HttpClientHelper.invoke(url, params);
		responseText(response, result);
		return null;
	}
	
	/**
	 * 获取省份列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView province(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/static/province";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Key", "dcacd527ea14edf2e9bfadba3bfe0129");		
		String result = HttpClientHelper.invoke(url, params);
		responseText(response, transformResult(result, "200"));
		return null;
	}
	
	/**
	 * 城市查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView city(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/static/city";
		Map<String, String> requestParams = getRequestParams();
		String Province = requestParams.get("Province");		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Key", "dcacd527ea14edf2e9bfadba3bfe0129");
		params.put("Province", Province);
		String result = HttpClientHelper.invoke(url, params);
		responseText(response, transformResult(result, "200"));
		return null;
	}
	
	/**
	 * 获取区域列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView location(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/static/location";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Key", "dcacd527ea14edf2e9bfadba3bfe0129");
		String result = HttpClientHelper.invoke(url, params);
		responseText(response, transformResult(result, "200"));
		return null;
	}
	
	/**
	 * 获取酒店主题列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView theme(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/xiecheng/hotel/static/theme";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Key", "dcacd527ea14edf2e9bfadba3bfe0129");
		String result = HttpClientHelper.invoke(url, params);
		responseText(response, transformResult(result, "200"));
		return null;
	}
	
	/**
	 * 酒店查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView search(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/xiecheng/hotel/search";
		Map<String, String> requestParams = getRequestParams();
		String HotelCityCode = requestParams.get("HotelCityCode");
		String AreaID = requestParams.get("AreaID");
		String HotelName = requestParams.get("HotelName");
		String Limit = requestParams.get("Limit");
		String Skip = requestParams.get("Skip");
		Map<String, Object> Page = new HashMap<String, Object>();
		try {
			Page.put("Limit", Integer.parseInt(Limit));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Page.put("Skip", Integer.parseInt(Skip));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String Lat = requestParams.get("Lat");	
		String Lng = requestParams.get("Lng");	
		String Distance = requestParams.get("Distance");	
		Map<String, Object> Geo = new HashMap<String, Object>();
		try {
			Geo.put("Lat", Double.parseDouble(Lat));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Geo.put("Lng", Double.parseDouble(Lng));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Geo.put("Distance", Double.parseDouble(Distance));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Key", "dcacd527ea14edf2e9bfadba3bfe0129");
		params.put("HotelCityCode", HotelCityCode);
		params.put("AreaID", AreaID);
		params.put("HotelName", HotelName);
		params.put("Page", Page);
		params.put("Geo", Geo);
		String result = HttpClientHelper.invoke(url, params);
		responseText(response, transformResult(result, "200"));
		return null;
	}
	
	/**
	 * 酒店详细信息查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView staticdetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/xiecheng/hotel/staticdetail";
		Map<String, String> requestParams = getRequestParams();
		String HotelCodes = requestParams.get("HotelCodes");
		List<String> pList = new ArrayList<String>();
		pList.add(HotelCodes);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Key", "dcacd527ea14edf2e9bfadba3bfe0129");
		params.put("HotelCodes", pList);
		String result = HttpClientHelper.invoke(url, params);
		responseText(response, transformResult(result, "200"));
		return null;
	}
	
	/**
	 * 航班信息查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView flightsearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/zteict/order/flightsearch";
		Map<String, String> requestParams = getRequestParams();
		String fromCity = requestParams.get("fromCity");
		String toCity = requestParams.get("toCity");
		String flightDate = requestParams.get("flightDate");
		String incShare = requestParams.get("incShare");
		String airline = requestParams.get("airline");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key", "4339d417f3ca31ea9bb197dabe89ab27");
		params.put("fromCity", fromCity);
		params.put("toCity", toCity);
		params.put("flightDate", flightDate);
		params.put("incShare", incShare);
		params.put("airline", airline);
		String result = HttpClientHelper.invoke(url, params, HttpClientHelper.HTTP_GET);
		responseText(response, transformResult(result, "200"));
		return null;
	}
	
	/**
	 * 站点简码查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView cityCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://op.juhe.cn/trainTickets/cityCode";
		Map<String, String> requestParams = getRequestParams();
		String dtype = requestParams.get("dtype");
		String stationName = requestParams.get("stationName");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key", "d81d75a0acde1bfa0b6206dfef864179");
		params.put("dtype", dtype);
		params.put("stationName", stationName);
		String result = HttpClientHelper.invoke(url, params, HttpClientHelper.HTTP_GET);
		responseText(response, transformResult(result, "0"));
		return null;
	}
	
	/**
	 * 余票查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView ticketsAvailable(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://op.juhe.cn/trainTickets/ticketsAvailable";
		Map<String, String> requestParams = getRequestParams();
		String dtype = requestParams.get("dtype");
		String train_date = requestParams.get("train_date");
		String from_station = requestParams.get("from_station");
		String to_station = requestParams.get("to_station");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key", "d81d75a0acde1bfa0b6206dfef864179");
		params.put("dtype", dtype);
		params.put("train_date", train_date);
		params.put("from_station", from_station);
		params.put("to_station", to_station);
		String result = HttpClientHelper.invoke(url, params, HttpClientHelper.HTTP_GET);
		responseText(response, transformResult(result, "0"));
		return null;
	}
	
	/**
	 * 获取城市三字码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView citycode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/zteict/static/citycode";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key", "4339d417f3ca31ea9bb197dabe89ab27");
		String result = HttpClientHelper.invoke(url, params, HttpClientHelper.HTTP_GET);
		responseText(response, transformResult(result, "200"));
		return null;
	}
	
	/**
	 * 获取航空公司二字码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView airline(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = "http://api2.juheapi.com/zteict/static/airline";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key", "4339d417f3ca31ea9bb197dabe89ab27");
		String result = HttpClientHelper.invoke(url, params, HttpClientHelper.HTTP_GET);
		responseText(response, transformResult(result, "200"));
		return null;
	}

	static Map<String, String> trainStations = new HashMap<String, String>();
	public ModelAndView trainStations(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CallResult callResult = new CallResult();
		callResult.setCode(SUCCESS);
		callResult.setMessage("获取数据成功");
		if(Utils.isEmpty(trainStations)){
			try {
				String encoding = "UTF-8";
				String dataPath = request.getRealPath("/")+"trainStations/list.txt";
				File file = new File(dataPath);
				if (file.isFile() && file.exists()) { //判断文件是否存在
					InputStreamReader read = new InputStreamReader(
							new FileInputStream(file), encoding);//考虑到编码格式
					BufferedReader bufferedReader = new BufferedReader(read);
					String lineTxt = null;
					while ((lineTxt = bufferedReader.readLine()) != null) {
						trainStations = new Gson().fromJson(lineTxt, new TypeToken<Map<String, String>>(){}.getType());
						System.out.println(trainStations);
					}
					read.close();
					callResult.setCode(SUCCESS);
					callResult.setMessage("获取数据成功");
				} else {
					callResult.setMessage("找不到指定的文件");
					System.out.println("找不到指定的文件");
				}
			} catch (Exception e) {
				callResult.setMessage("读取文件内容出错");
				System.out.println("读取文件内容出错");
				e.printStackTrace();
			}
		}
		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put("list", trainStations);
		callResult.setData(resultData);
		responseText(response, getEncryJson(callResult));
		return null;
	}

}

