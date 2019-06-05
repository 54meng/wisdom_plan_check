package com.wpc.dfish.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class APIEncode {

	/**
	 * 添加文字，改变html页面的字符集
	 */
	public static String ENCODE_GB2312 = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" />";

	/**
	 * 添加标记
	 */
	public static String INSERT_SIGN = "<HEAD>";
	
	public static void main(String[] args) {
		File f = new File("WebRoot/docs/javadoc");
		System.out.println(f.getAbsolutePath());
		batchTransfer(f);
	}

	public static void batchTransfer(File rootFile) {
		if (rootFile.isFile()) {
			String fileName = rootFile.getName();
			int dotPos = fileName.lastIndexOf(".");
			if (dotPos > 0) {
				String postfix = fileName.substring(dotPos + 1).toLowerCase();
				if ("html".equals(postfix) || "htm".equals(postfix)
						|| "css".equals(postfix)) {
					transfer(rootFile);
				}
			}
		} else if (rootFile.isDirectory()) {
			File[] subs = rootFile.listFiles();
			for (File sub : subs) {
				batchTransfer(sub);
			}
		}
	}

	private static void transfer(File file) {
		System.out.println("正在处理 - " + file.getAbsolutePath());
		try {
			StringBuffer sb = new StringBuffer();
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
         
            String str = br.readLine();    
			while (str != null) {
				sb.append(str);
				if(str.equalsIgnoreCase(APIEncode.INSERT_SIGN)) {
					sb.append("\r\n");
					sb.append(APIEncode.ENCODE_GB2312);
				}
				sb.append("\r\n"); //换行
				str = br.readLine();
			}
		
			FileWriter writer = new FileWriter(file);
			
			writer.write(sb.toString());
			
			writer.close();
			
			br.close();
			reader.close();
			

		} catch (IOException e) {
			System.out.println("处理 " + file.getAbsolutePath() + " 失败");
			e.printStackTrace();
		}finally {
			
		}
	}

}
