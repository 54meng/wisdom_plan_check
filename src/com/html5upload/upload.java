package com.html5upload;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.wpc.dfish.framework.FrameworkHelper;

import sun.misc.BASE64Encoder;

import java.text.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.nio.ByteBuffer;

public class upload extends MultiActionController {
	
	private HttpServletRequest _request;
	private HttpServletResponse _response;
	
	private String localfilename;
	private String filesize;
	private String filepos;
	private String act;

	private String folderPath;
	private String saveToFilePath = "";
	private String filename;
	private String rename = "";
	private String info;

	private int state = 0;     //运行状态

	private String serverfileurl = "";
	private long serverfilesize = 0;
	private long validatepos = 0;       //校验文件时指定的文件偏移量
	private int validatelength = 64;   //校验文件时指定的文件二进制字节数
	private  String validatamd5 = "";
	private String streammd5 = "";
	private String errdescription = "";     //错误信息
    
	private long fp = 0;
	private String filerealpath="";
    
    
    private String getRealPath(String _path)
	{
		
		return _request.getSession().getServletContext().getRealPath(_path);
	}
	private String getAutoRename(String filerealpath)
	 {
		String[] temp = filerealpath.toString().split("\\\\");
		String[] temp2 = temp[temp.length - 1].split("/");
		String  filename= temp2[temp2.length - 1];
		 
		String __fileNameOnly=GetFileNameOnly(filename);
		String __extName=this.getFileExtensionName(filerealpath);
		
		String BaseReturnName=__fileNameOnly+"_[index]."+__extName;
		String ReturnName="";
		String folderPath=filerealpath.substring(0, filerealpath.lastIndexOf("\\"));
		int i;
		for(i=0;i<1000;i++)
		{
			ReturnName=BaseReturnName.replace("_[index].", "_"+ i +".");
			if(!this.FileExists(folderPath+"\\"+ReturnName))
			{
				return ReturnName;
			}
		}
		return ReturnName;
	 }
	
	public boolean FileExists(String FilePath){
		//判断文件是否存在
		File f = new File(FilePath);//定义文件路径
		return f.exists();
	}

	 private String GetFileNameOnly(String FileName){
		//sFileFullPath="abc.gif"，返回"abc.gif"
		String returnValue="";
		String[] SplitString=FileName.split("\\.");
		int i;
		
		for(i=0;i<SplitString.length-1;i++)
		{
			//SplitString
			if(returnValue=="")
				returnValue=SplitString[i];
			else
				returnValue=returnValue+"."+SplitString[i];
		}
		return returnValue;

	}
	private String getFileExtensionName(String FileFullePath){
		//获取文件的文件名
		String[] SplitString=FileFullePath.split("\\.");
		return SplitString[SplitString.length-1].toLowerCase();
	}
	
	public boolean MD(String FolderPath){
		//建立单层目录
		java.io.File dir = new java.io.File(FolderPath);  

		if(!dir.exists())
		{
			try{
				//_out.append("Build Path : "+FolderPath+"<hr>");
				dir.mkdir();
				return true;
			}
			catch(Exception e){
				errdescription = e.toString();
				return false;
			}
		}
		else{
			return true;
		}			
	}
	public boolean MD2(String FolderPath){
		//根据目录路径，一层一层建立文件夹。
		boolean returnValue;
		String CurrentFolderPath;
		int length,i;
		FolderPath=FolderPath.replace("\\","/");
		String[] SplitString=FolderPath.split("/");	
		length=SplitString.length;
		CurrentFolderPath=SplitString[0];
		for (i=1;i<length;i++)
		{
			CurrentFolderPath+="/"+SplitString[i];
			
			returnValue=MD(CurrentFolderPath);
			if (!returnValue)
			{
				return false;
			}
		}
		return true;
	}
	
    private void saveStreamToFile(String filepath, byte[] filepartstream, long offset)
    {
		try {
		   // 按读写方式创建一个随机访问文件流
		   RandomAccessFile raf = new RandomAccessFile(filepath, "rw");
		   raf.seek(offset);
		   // 按字节的形式将内容写到随机访问文件流中
		   raf.write(filepartstream);
		   // 关闭流
		   raf.close();
		}
		catch (IOException e) {
			//
			ConsoleLog(filepath+" >> 写入数据[saveStreamToFile]方法出错：" +  e.toString());
			//this.ErrDescription=e.toString();
		}
    }
	
	private void outPutJson(){
        StringBuilder returnText = new StringBuilder("");
       
        _response.addHeader("Access-Control-Allow-Origin", "*");   //允许跨域ajax请求
        returnText.append("{");
        returnText.append("\"state\":"+state+",");
        returnText.append("\"act\":\"" + act + "\",");
        returnText.append("\"serverfileurl\":\"" + serverfileurl + "\",");
        returnText.append("\"serverfilesize\":" + serverfilesize + ",");
        returnText.append("\"streammd5\":\"" + streammd5 + "\",");
        returnText.append("\"errdescription\":\"" + errdescription + "\",");
        returnText.append("\"validatamd5\":\"" + validatamd5 + "\",");
        returnText.append("\"validatepos\":" + validatepos + ",");
        returnText.append("\"validatelength\":" + validatelength+ ",");
        
        
        Date now=new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        returnText.append("\"uploadtime\":\"" + f.format(now)+"\"");
        
        returnText.append("}");
        
	    try{
	        PrintWriter pw = _response.getWriter();
	        pw.write(returnText.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	private void ResponseWrite(String s){        
	    try{
	        PrintWriter pw = _response.getWriter();
	        pw.write(s);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private boolean dealRequest() throws Exception
	{
		localfilename = _request.getParameter("localfilename");
        filesize = _request.getParameter("filesize");
        filepos = _request.getParameter("filepos");
        act = _request.getParameter("act");
        rename = _request.getParameter("rename");

        //计算上传的路径相关信息
        String[] temp = localfilename.toString().split("\\\\");
        String[] temp2 = temp[temp.length - 1].split("/");
        filename = temp2[temp2.length - 1];

        folderPath="/upload";
        
        if (rename==null)
        {
            serverfileurl = folderPath + "/" + filename;
        }
        else {
            serverfileurl = folderPath + "/" + rename;
        }
        
        filerealpath=this.getRealPath(serverfileurl);

        return true;
	}
	
    
	public ModelAndView validate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//上传前文件校验
		_request=request;
		_response=response;
		if(!this.dealRequest())
		{
			return null;
		}
		if (localfilename==null)
        {
            errdescription = "文件名不能为空";
            outPutJson();
            return null;
        }

		if(this.FileExists(filerealpath))
        {
			//ConsoleLog(filerealpath+"发送数据校验码。");
			
            this.errdescription = "file_exist";

            RandomAccessFile raf = new RandomAccessFile(filerealpath, "rw");
            serverfilesize=raf.length();
            validatepos=serverfilesize-validatelength;
			if(validatepos<0)
			{
				validatepos=0;
				validatelength=Integer.parseInt(String.valueOf(serverfilesize));
			}
			byte[] dataArray=new byte[validatelength];
			raf.seek(validatepos);
			// 按字节的形式将内容写到随机访问文件流中
			raf.read(dataArray);
			// 关闭流
			raf.close();
			BASE64Encoder encoder = new BASE64Encoder();
    		//分段MD5校验
			MD5Util _MD5Util=new MD5Util();
			validatamd5 =_MD5Util.getMD5String(dataArray);
			
        }
        else
        {
            this.errdescription = "file_not_exist";
            validatamd5 = "";
        }
        
        outPutJson();
		
		return null;
			    
	}
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//上传或续传文件
		_request=request;
		_response=response;
		if(!this.dealRequest())
		{
			return null;
		}

		if (localfilename == null || localfilename == "" || filesize == null || filesize == "" || filepos == null || filepos == "")
        {

            state = 1;
            errdescription = "无效数据";
            outPutJson();
            return null;
	        
        }
		
		
		if (!this.MD2(this.getRealPath(folderPath)))
		{
			errdescription="创建文件夹失败";
			this.outPutJson();
			return null;
		}


		if(this.FileExists(filerealpath))
        {
			File f = new File(filerealpath);
			serverfilesize=f.length();
			
			if(serverfilesize==Long.parseLong(filesize))
			{
	            errdescription = "已经上传完毕";
	            outPutJson();
	            return null;
			}
            //如果当前位置post的文件位置为0，而且服务器已经存在文件，则上传。
            if (Long.parseLong(filepos) == 0)
            {
                errdescription = "续传";
                outPutJson();
                return null;
            }
	    }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile[] files = FrameworkHelper.getFiles(multipartRequest, "file");


		if (files != null && files.length ==1) {			
			
			InputStream is=files[0].getInputStream();
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		    byte[] buffer = new byte[1024];	  
		    int len = -1;	
		    while((len = is.read(buffer)) != -1){	  
		    	outStream.write(buffer, 0, len);	  
		    }	  
		    outStream.close();	  

		    byte[] dataArray=outStream.toByteArray();	  
		    this.saveStreamToFile(filerealpath,dataArray,Long.parseLong(filepos.toString()));

			File f2 = new File(filerealpath);
			serverfilesize=f2.length();
			this.outPutJson();			
		}
		return null;
		
	}
	
	public ModelAndView md5(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取服务器文件的md5值
		_request=request;
		_response=response;
		if(!this.dealRequest())
		{
			return null;
		}
        
        File f = new File(filerealpath);
        MD5Util _MD5Util=new MD5Util();
        String info=_MD5Util.getFileMD5String(f);
        StringBuilder _out=new StringBuilder();
        _out.append("{");
        _out.append("serverfileurl:\"" + serverfileurl +"\",");
        _out.append("serverfilesize:\"" +f.length() + "\",");
        _out.append("md5:\""+info+"\"");
        _out.append("}");
        this.ResponseWrite(_out.toString());
        return null;
	}	
	
	 private void ConsoleLog(String s)
	 {
		 System.out.println(s);
	 }
}