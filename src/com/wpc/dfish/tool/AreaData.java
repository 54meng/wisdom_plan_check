package com.wpc.dfish.tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class AreaData {
	public static void main(String[] args) throws Exception{
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		List<String[]> provinces=getProvinces();
//		System.out.println("所有省份如下");
//		showList(provinces);
		boolean firstProv=true;
		for(String[] prov:provinces){
			if(!firstProv)sb.append(",");
			firstProv=false;
			sb.append("\r\n{");
			sb.append("\"id\":\"");
			sb.append(prov[0]);
			sb.append("\",\"name\":\"");
			sb.append(prov[1]);
			sb.append("\",\"dist\":[");
			boolean firstDist=true;
			List<String[]> districts=getDistricts(prov[0]);
			for(String[] dist:districts){
				if(!firstDist)sb.append(",");
				firstDist=false;
				sb.append("\r\n  {");
				sb.append("\"id\":\"");
				sb.append(dist[0]);
				sb.append("\",\"name\":\"");
				sb.append(dist[1]);
				sb.append("\",\"city\":[");
				boolean firstCity=true;
				List<String[]> cities=getCities(prov[0],dist[0]);
				for(String[] city:cities){
					if(!firstCity)sb.append(",");
					firstCity=false;
					sb.append("[\"");
					sb.append(city[0]);
					sb.append("\",\"");
					sb.append(city[1]);
					sb.append("\"]");
				}
//				System.out.println("===="+prov[1]+"省 "+dist[1]+"市 包含以下县/区====");
//				showList(cities);
				sb.append("]}");
				
			}
			sb.append("\r\n]}");
		}
		sb.append("\r\n]");
		System.out.println(sb);
	}
	public static List<String[]> getProvinces() throws Exception{
		String rootUrl="http://www.weather.com.cn/data/city3jdata/china.html";
		List<String[]> data=jsonToList(getText(rootUrl));	
		return data;
	}
	public static List<String[]> getDistricts(String provinceId) throws Exception{
		String rootUrl="http://www.weather.com.cn/data/city3jdata/provshi/"+provinceId+".html";
		List<String[]> data=jsonToList(getText(rootUrl));	
		return data;
	}
	public static List<String[]> getCities(String provinceId,String cityId) throws Exception{
		String rootUrl="http://www.weather.com.cn/data/city3jdata/station/"+provinceId+cityId+".html";
		List<String[]> data=jsonToList(getText(rootUrl));	
		return data;
	}
	public static void showList(List<String[]> data){
		for(String [] item:data){
			System.out.println(item[1]+"("+item[0]+")");
		}
	}
	public static List<String[]> jsonToList(String json){
		ArrayList<String[]> result=new ArrayList<String[]>();
		json=json.replace("{", "").replace("}", "");
		String[] pairs=json.split(",");
		for(String pairStr:pairs){
			String[] pair = pairStr.split(":");
			String id=pair[0].substring(1, pair[0].length()-1);
			String name=pair[1].substring(1, pair[1].length()-1);
			result.add(new String[]{id,name});
		}
		return result;
	}
	public static String getText(String url)throws Exception{
		URL u=new java.net.URL(url);
		URLConnection conn=u.openConnection();
		conn.connect();
		InputStream is=conn.getInputStream();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] buffer=new byte[8192];
		int readed=-1;
		while((readed=is.read(buffer))>=0){
			baos.write(buffer, 0, readed);
		}
		
		return new String(baos.toByteArray(),"UTF-8");
	}

}
