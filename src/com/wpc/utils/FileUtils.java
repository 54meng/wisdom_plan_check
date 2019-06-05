package com.wpc.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * 文件公用处理工具类
 * 
 * <p>Title: </p>
 * <p>Description: 文件公用处理工具类</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author HQJ
 * @version 1.0
 * @since	1.0.0	HQJ		2010-07-06
 */
public final class FileUtils {
	private static final Log logger = LogFactory.getLog(FileUtils.class);
	/** 编码字符集 */
	public static final String ENCODE_UTF8 = "UTF-8";
	public static final String ENCODING_DEFAULT = "UTF-8";
	public static final String DOWNLOAD_ENCODING = "ISO8859-1";

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePathName
	 *            文件目录名
	 * @return
	 */
	public static boolean fileExists(String filePathName) {
		File file = new File(filePathName);
		return file.exists();
	}

	/**
	 * 判断文件路径是否存在
	 * 
	 * @param filePathName
	 *            文件目录名
	 * @return
	 */
	public static boolean pathExists(String filePathName) {
		String filePath = extractFilePath(filePathName);
		return fileExists(filePath);
	}

	/**
	 * 提取文件名，包括文件扩展名，如test.txt
	 * 
	 * @param filePathName
	 *            文件目录名
	 * @return
	 */
	public static String extractFileName(String filePathName) {
		return extractFileName(filePathName, File.separator);
	}

	/**
	 * 提取文件名, 包括文件扩展名，如test.txt
	 * 
	 * @param filePathName
	 *            文件目录名
	 * @param fileSeparator
	 *            文件路径分隔符
	 * @return
	 */
	public static String extractFileName(String filePathName, String fileSeparator) {
		int nPos = -1;
		if (fileSeparator == null) {
			nPos = filePathName.lastIndexOf(File.separatorChar);
			if (nPos < 0)
				nPos = filePathName.lastIndexOf((File.separatorChar == '/') ? 92 : '/');
		} else {
			nPos = filePathName.lastIndexOf(fileSeparator);
		}
		if (nPos < 0) {
			return filePathName;
		}
		return filePathName.substring(nPos + 1);
	}

	/**
	 * 提取文件名称
	 * 
	 * @param filePathName
	 *            文件目录名
	 * @return
	 */
	public static String extractHttpFileName(String filePathName) {
		int nPos = filePathName.lastIndexOf("/");
		return filePathName.substring(nPos + 1);
	}

	/**
	 * 提取文件名称，不包括文件扩展名，如XXX
	 * 
	 * @param filePathName
	 *            文件目录名
	 * @return
	 */
	public static String extractMainFileName(String filePathName) {
		String fileName = extractFileName(filePathName);
		int nPos = fileName.lastIndexOf('.');
		if (nPos > 0) {
			return fileName.substring(0, nPos);
		}
		return fileName;
	}

	/**
	 * 提取文件名称，不包括文件扩展名，如XXX
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String extractFileNameExcludeExt(String fileName) {
		int nPos = fileName.lastIndexOf('.');
		if (nPos > 0) {
			return fileName.substring(0, nPos);
		}
		return fileName;
	}

	/**
	 * 提取文件扩展名，不包括点号，如txt doc等.
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String extractFileExt(String fileName) {
		int pos = fileName.lastIndexOf('.');
		return (pos >= 0) ? fileName.substring(pos + 1) : "";
	}

	/**
	 * 提取文件路径
	 * 
	 * @param filePathName
	 *            文件目录名
	 * @return
	 */
	public static String extractFilePath(String filePathName) {
		int nPos = filePathName.lastIndexOf('/');
		if (nPos < 0) {
			nPos = filePathName.lastIndexOf('\\');
		}
		return (nPos >= 0) ? filePathName.substring(0, nPos + 1) : "";
	}

	/**
	 * 返回抽象路径名的绝对路径名字符串
	 * 
	 * @param filePathName
	 *            文件目录名
	 * @return
	 */
	public static String toAbsolutePathName(String filePathName) {
		File file = new File(filePathName);
		return file.getAbsolutePath();
	}

	/**
	 * 提取文件路径目录
	 * 
	 * @param filePathName
	 *            文件目录名
	 * @return
	 */
	public static String extractFileDrive(String filePathName) {
		int nLen = filePathName.length();
		if ((nLen > 2) && (filePathName.charAt(1) == ':')) {
			return filePathName.substring(0, 2);
		}
		if ((nLen > 2) && (filePathName.charAt(0) == File.separatorChar)
				&& (filePathName.charAt(1) == File.separatorChar)) {
			int nPos = filePathName.indexOf(File.separatorChar, 2);
			if (nPos >= 0)
				nPos = filePathName.indexOf(File.separatorChar, nPos + 1);
			return (nPos >= 0) ? filePathName.substring(0, nPos) : filePathName;
		}

		return "";
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathName
	 *            文件路径名称
	 * @return
	 */
	public static boolean deleteFile(String filePathName) {
		File file = new File(filePathName);
		return (file.exists()) ? file.delete() : false;
	}

	/**
	 * 删除文件或文件夹及该文件夹的子文件夹或文件
	 * 
	 * @param path
	 * @return
	 */
	/** @deprecated */
	public static boolean deleteDir(String dir, boolean deleteChildren) {
		File file = new File(dir);
		if (!file.exists()) {
			return false;
		}
		if (deleteChildren) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; ++i) {
				File aFile = files[i];
				if (aFile.isDirectory())
					deleteDir(aFile);
				else {
					aFile.delete();
				}
			}
		}
		return file.delete();
	}

	/**
	 * 删除文件或文件夹及该文件夹的子文件夹或文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDir(String path) {
		File root = new File(path);
		return deleteDir(root);
	}

	/**
	 * 删除文件或文件夹及该文件夹的子文件夹或文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDir(File path) {
		if (!path.exists()) {
			return false;
		}
		if (path.isDirectory()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; ++i) {
				File file = files[i];
				if (file.isDirectory()) {
					deleteDir(file);
				} else {
					file.delete();
				}
			}
		}
		return path.delete();
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 * @param ceateParentDir
	 * @return
	 */
	public static boolean makeDir(String path, boolean ceateParentDir) {
		boolean result = false;
		File file = new File(path);
		if (ceateParentDir)
			result = file.mkdirs();
		else
			result = file.mkdir();
		if (!result)
			result = file.exists();
		return result;
	}


	/**
	 * 
	 * @param dir
	 * @return
	 */
	public static File[] listSubDirectories(String dir) {
		File fDir = new File(dir);
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
		File[] files = fDir.listFiles(fileFilter);
		return files;
	}

	/**
	 * 读文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException {
		return readFile(fileName, ENCODING_DEFAULT);
	}

	/**
	 * 读文件
	 * 
	 * @param fileName
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String fileName, String encoding) throws IOException {
		FileInputStream fis = null;
		BufferedReader buffReader = null;
		FileReader fileReader = null;
		StringBuffer buffContent = null;
		if (encoding == null) {
			encoding = ENCODING_DEFAULT;
		}
		String s = null;
		try {
			fis = new FileInputStream(fileName);
			buffReader = new BufferedReader(new InputStreamReader(fis, encoding));
			String line;
			while ((line = buffReader.readLine()) != null) {
				if (buffContent == null) {
					buffContent = new StringBuffer();
				} else {
					buffContent.append("\n");
				}
				buffContent.append(line);
			}
			s = buffContent != null ? buffContent.toString() : "";
		} catch (Exception ex) {

		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (buffReader != null) {
					buffReader.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (Exception ex) {
			}
		}
		return s;
	}

	/**
	 * 从inputStrean读取文件内容
	 * 
	 * @param InputStream
	 * @return
	 * @throws IOException
	 */
	public static String readFileByInputStream(InputStream inputStream) throws IOException {
		String content = null;
		StringBuffer buffContent = null;

		BufferedReader buffReader = null;
		try {
			buffReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = buffReader.readLine()) != null) {
				if (buffContent == null) {
					buffContent = new StringBuffer();
				} else {
					buffContent.append("\\n");
				}
				buffContent.append(line);
			}
			content = buffContent != null ? buffContent.toString() : "";
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (buffReader != null) {
					buffReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 读文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytesFromFile(String fileName) throws IOException {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(fileName);
			byte[] buffer = new byte[1024];
			bos = new ByteArrayOutputStream(2048);
			int nLen = 0;
			while ((nLen = fis.read(buffer)) > 0) {
				bos.write(buffer, 0, nLen);
			}
			return bos.toByteArray();
		} catch (Exception e) {
			try {
				if (fis != null) {
					fis.close();
				}
				if (bos != null)
					bos.close();
			} catch (Exception localException) {
			}
			throw new IOException("读取文件[" + fileName + "]失败！");
		}
	}

	/**
	 * 写文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param fileContent
	 *            文件内容
	 * @return
	 * @throws IOException
	 */
	public static boolean writeFile(String fileName, String fileContent) throws IOException {
		return writeFile(fileName, fileContent, ENCODING_DEFAULT);
	}

	/**
	 * 写文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param fileContent
	 *            文件内容
	 * @param encoding
	 *            文件编码
	 * @return
	 * @throws IOException
	 */
	public static boolean writeFile(String fileName, String fileContent, String encoding) throws IOException {
		return writeFile(fileName, fileContent, encoding, false);
	}

	/**
	 * 写文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param fileContent
	 *            文件内容
	 * @param fileEncoding
	 *            文件编码
	 * @param writeUnicodeFlag
	 * @return
	 * @throws IOException
	 */
	public static boolean writeFile(String fileName, String fileContent, String fileEncoding, boolean writeUnicodeFlag)
			throws IOException {
		boolean bRet = false;
		FileOutputStream fos = null;
		Writer writer = null;
		try {
			String filePath = extractFilePath(fileName);
			if (!pathExists(filePath)) {
				makeDir(filePath, true);
			}
			fos = new FileOutputStream(fileName);
			writer = new OutputStreamWriter(fos, fileEncoding);
			if (writeUnicodeFlag) {
				writer.write(65279);
			}
			writer.write(fileContent);
			bRet = true;
		} catch (Exception ex) {
			logger.error("写文件[" + fileName + "]发生异常", ex);
			ex.printStackTrace(); // "写文件错误(CMyFile.writeFile)"
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception ex) {
			}
		}
		return bRet;
	}

	/**
	 * 向文件追加内容
	 * 
	 * @param fileName
	 * @param addContent
	 * @return
	 * @throws IOException
	 */
	public static boolean appendFile(String fileName, String addContent) throws IOException {
		boolean bResult = false;
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(fileName, "rw");
			raf.seek(raf.length());
			raf.writeBytes(addContent);
			bResult = true;
		} catch (Exception ex) {
			logger.error("向文件追加内容时发生异常(CMyFile.appendFile)", ex);
			ex.printStackTrace();
		} finally {
			try {
				if (raf != null)
					raf.close();
			} catch (Exception ex) {
			}
		}
		return bResult;
	}

	/**
	 * 移动文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param dstFile
	 *            目标文件路径
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFile(String srcFile, String dstFile) throws IOException {
		return moveFile(srcFile, dstFile, true);
	}

	/**
	 * 移动文件
	 * 
	 * @param srcFile
	 * @param dstFile
	 * @param makeDirIfNotExists
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFile(String srcFile, String dstFile, boolean makeDirIfNotExists) throws IOException {
		copyFile(srcFile, dstFile, makeDirIfNotExists);
		deleteFile(srcFile);
		return false;
	}

	/**
	 * 复制文件
	 * 
	 * @param srcFile
	 * @param dstFile
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String srcFile, String dstFile) throws IOException {
		return copyFile(srcFile, dstFile, true);
	}

	/**
	 * 复制文件
	 * 
	 * @param srcFile
	 * @param dstFile
	 * @param makeDirIfNotExists
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String srcFile, String dstFile, boolean makeDirIfNotExists) throws IOException {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(srcFile);
			try {
				fos = new FileOutputStream(dstFile);
			} catch (FileNotFoundException ex) {
				if (makeDirIfNotExists) {
					if (!makeDir(extractFilePath(dstFile), true)) {
						logger.error("为目标文件[" + dstFile + "]创建目录失败！", ex);
					}
					fos = new FileOutputStream(dstFile);
				} else {
					logger.error("指定目标文件[" + dstFile + "]所在目录不存在！", ex);
				}
			}
			byte[] buffer = new byte[4096];
			int bytes;
			while ((bytes = fis.read(buffer, 0, 4096)) > 0) {
				fos.write(buffer, 0, bytes);
			}
		} catch (FileNotFoundException ex) {
			logger.error("要复制的原文件没有发现(CMyFile.copyFile)", ex);
			ex.printStackTrace();
		} catch (IOException ex) {
			logger.error("复制文件时发生异常(CMyFile.copyFile)", ex);
			ex.printStackTrace();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (Exception localException1) {
				}
			if (fis != null)
				try {
					fis.close();
				} catch (Exception ex) {
				}
		}
		return true;
	}

	/**
	 * 复制一个文件夹下所有的文件夹和文件到另一个文件夹下
	 * 
	 * @param srcFilePath
	 *            源文件夹路径 如：c:/fqf
	 * @param dstFilePath
	 *            目标文件夹路径 如：f:/fqf/ff
	 * @return
	 */
	public static boolean copyFolder(String srcFilePath, String dstFilePath) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			File dstFileDir = new File(dstFilePath);
			if (!dstFileDir.exists()) {
				dstFileDir.mkdirs(); // 如果目标文件夹不存在，则建立新文件夹
			}
			File srcFile = new File(srcFilePath);
			String[] files = srcFile.list();
			File subFile = null;
			for (int i = 0; i < files.length; i++) {
				if (srcFilePath.endsWith(File.separator)) {
					subFile = new File(srcFilePath + files[i]);
				} else {
					subFile = new File(srcFilePath + File.separator + files[i]);
				}
				if (subFile.isFile()) {
					fis = new FileInputStream(subFile);
					fos = new FileOutputStream(dstFilePath + "/" + (subFile.getName()).toString());
					byte[] buffer = new byte[4096];
					int bytes;
					while ((bytes = fis.read(buffer, 0, 4096)) > 0) {
						fos.write(buffer, 0, bytes);
					}
				}
				if (subFile.isDirectory()) {// 如果是子文件夹
					copyFolder(srcFilePath + "/" + files[i], dstFilePath + "/" + files[i]);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (Exception localException1) {
				}
			if (fis != null)
				try {
					fis.close();
				} catch (Exception ex) {
				}
		}
		return true;
	}

	public static String mapResouceFullPath(String _resource) throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource(_resource);
		if (url == null) {
			logger.error("文件[" + _resource + "]没有找到！");
		}
		String sPath = null;
		try {
			sPath = url.getFile();
			if (sPath.indexOf('%') >= 0)
				sPath = URLDecoder.decode(url.getFile(), null);
		} catch (Exception ex) {
			logger.error("文件[" + url.getFile() + "]转换失败！", ex);
		}
		return sPath;
	}

	public static String mapResouceFullPath(String _resource, Class _currClass) throws IOException {
		URL url = _currClass.getResource(_resource);
		if (url == null) {
			logger.error("文件[" + _resource + "]没有找到！");
		}
		String sPath = null;
		try {
			sPath = url.getFile();
			if (sPath.indexOf('%') >= 0)
				sPath = URLDecoder.decode(url.getFile(), null);
		} catch (Exception ex) {
			logger.error("文件[" + url.getFile() + "]转换失败！", ex);
		}
		return sPath;
	}

	/**
	 * 生成新文件名
	 * 
	 * @param filePrefix
	 *            文件名前缀
	 * @param fileExt
	 *            文件扩展名
	 * @return
	 * @throws Exception
	 */
	public static String getNewFileName(String filePrefix, String fileExt) throws Exception {
		fileExt = fileExt.trim();
		if ((fileExt.length() > 0) && (fileExt.charAt(0) != '.')) {
			fileExt = "." + fileExt;
		}
		Date date = new Date();
		String sDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);

		String fileName = filePrefix + sDateTime + fileExt;
		return fileName;
	}

	/**
	 * 生成新文件名,包含路径
	 * 
	 * @param path
	 *            路径
	 * @param pathFlag
	 *            路径标识
	 * @param fileExt
	 *            文件扩展名
	 * @param includePath
	 *            文件名是否包含路径
	 * @return
	 * @throws Exception
	 */
	public static String getNewFileName(String path, String pathFlag, String fileExt, boolean includePath)
			throws Exception {
		if ((path == null) || (path.length() == 0)) {
			path = "/WEB-INF/upload/";
		}
		if (pathFlag == null) {
			throw new Exception("路径标识为空!");
		}
		pathFlag = pathFlag.trim().toUpperCase();

		Date date = new Date();
		long lTime = date.getTime();
		String sDate = new SimpleDateFormat("yyyyMMdd").format(date);

		String filePath = path + pathFlag + sDate.substring(0, 6) + File.separator + lTime + File.separator;
		if (!(FileUtils.pathExists(filePath))) {
			try {
				FileUtils.makeDir(filePath, true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		fileExt = fileExt.trim();
		if ((fileExt.length() > 0) && (fileExt.charAt(0) != '.')) {
			fileExt = "." + fileExt;
		}
		String fileName = pathFlag + lTime;
		if (!(FileUtils.fileExists(filePath + fileName + fileExt))) {
			return ((includePath) ? filePath : "") + fileName + fileExt;
		}
		return filePath;
	}
	
	/**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.##");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }


	public static void main(String[] args) {
		String s = "2521005005";
		
		System.out.print(formetFileSize(Long.parseLong(s)));
	}
}
