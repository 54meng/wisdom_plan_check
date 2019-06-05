package com.wpc.dfish.tool;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class API2UTF8 {
	public static void main(String[] args){
		File f=new File("WebRoot/docs/javadoc");
		System.out.println(f.getAbsolutePath());
		batchTransfer(f);
	}
	public static void batchTransfer(File rootFile){
		if(rootFile.isFile()){
			String fileName=rootFile.getName();
			int dotPos=fileName.lastIndexOf(".");
			if(dotPos>0){
				String postfix=fileName.substring(dotPos+1).toLowerCase();
				if("html".equals(postfix)||"htm".equals(postfix)||"css".equals(postfix)){
					transfer(rootFile);
				}
			}
		}else if(rootFile.isDirectory()){
			File[] subs=rootFile.listFiles();
			for(File sub:subs){
				batchTransfer(sub);
			}
		}
	}
	private static void transfer(File file) {
		System.out.println("正在处理 - "+file.getAbsolutePath());
		try {
			FileInputStream stream=new FileInputStream(file);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] buff=new byte[8192];
			int readed=0;
			while((readed=stream.read(buff))>=0){
				baos.write(buff, 0, readed);
			}
			stream.close();
			byte[] result=new String(baos.toByteArray(),"GBK").getBytes("UTF-8");
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(result);
			fos.close();
			
		} catch (IOException e) {
			System.out.println("处理 "+file.getAbsolutePath()+" 失败");
			e.printStackTrace();
		}
	}
	
}
