package com.wpc.dfish.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 包含类的一些基本操作
 * 
 * @author I-TASK team
 * 
 */
public class ClassUtil {
	public static String[] getPackages() {
		Package[] pckgs = Package.getPackages();
		// 以下是已经装载的包。
		String[] names = new String[pckgs.length];
		for (int i = 0; i < pckgs.length; i++) {
			names[i] = pckgs[i].getName();
		}
		Arrays.sort(names);
		return names;
	}

	/**
	 * 找到某个包下的所有类
	 * 
	 * @param packageName
	 *            包名
	 * @param inner
	 *            是否包含内嵌类
	 * @return 找到的类名
	 * @throws Exception
	 */
	public static String[] findClasses2(String packageName, boolean inner)
			throws Exception {
		return findClasses(packageName, inner, null);
	}

	public static String[] findClasses(String packageName, boolean inner)
			throws ClassNotFoundException {

		ArrayList<String> classes = new ArrayList<String>();
		// Get a File object for the package
		URL resource = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");
			String path = packageName.replace('.', '/');
			resource=cld.getResource(path);
			if (resource == null){
				ClassLoader baseClassLoader=java.util.List.class.getClassLoader();
				System.out.println("baseClassLoader="+baseClassLoader);
//				System.out.println("无法在"+cld.getClass().getName()+"中获取"+packageName+"信息.尝试从"+ClassLoader.getSystemClassLoader().getClass().getName()+"中获取");
				resource=ClassLoader.getSystemClassLoader().getResource(path);
				if (resource == null)
				return new String[0];
			}
			
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(packageName + " (" + packageName
					+ ") does not appear to be a valid package a");
		}
	
		if(resource.getProtocol()!=null&&resource.getProtocol().toLowerCase().indexOf("jar")>=0){
			//在jar中查找类
			String urlStr=resource.getPath();
			int beginIndex = urlStr.indexOf(":/") + 2;
			int endIndex = 0;
			String low=urlStr.toLowerCase();
			if(low.lastIndexOf(".jar")>=0){
				endIndex=low.lastIndexOf(".jar")+4;
			}else if(low.lastIndexOf(".zip")>=0){
				endIndex=low.lastIndexOf(".zip")+4;
			}else{
				endIndex=urlStr.length();
			}
			File jarFile=new File(urlStr.substring(beginIndex,endIndex));
			ZipInputStream in;
			try {
				in = new ZipInputStream(new FileInputStream(
						jarFile));
			ZipEntry ze = null;
				while ((ze = in.getNextEntry()) != null) {
					String name = ze.getName();
					if ((inner || ze.getName().indexOf('$') < 0)
							&& name.startsWith(packageName.replace(".", "/"))
							&& name.endsWith(".class")
							&& (name.lastIndexOf("/") == packageName.length())) {
						classes.add(name.replace('/', '.').substring(0,
								name.length() - 6));
					}
				}
			} catch (IOException e) {}
		}else{
	
		File directory = new File(resource.getFile());
		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				String name=files[i];
					if (name.endsWith(".class")) {
					if (inner || name.indexOf('$') < 0) {
						classes.add(packageName + "."
								+ name.substring(0, name.length() - 6));
					}
				}
			}
		} else
			throw new ClassNotFoundException(packageName
					+ " does not appear to be a valid package b");
		}
		String[] classesA = new String[classes.size()];
		classes.toArray(classesA);
		return classesA;

	}
	public static void main(String[] args) throws ClassNotFoundException{
		String[] clzs=findClasses("java.util",false);
		for(String clz:clzs){
		System.out.println(clz);
		}
	}

	/**
	 * 
	 * @param packageName
	 *            包名
	 * @param inner
	 *            是否包含内嵌类
	 * @param extPaths
	 *            检察额外的路径。如${webroot}/WEB-INFO/lib下。
	 * @return 找到的类名
	 * @throws Exception
	 */
	public static String[] findClasses(String packageName, boolean inner,
			String[] extPaths) throws Exception {
		String classpath = System.getProperty("java.class.path");
		/*
		 * 1 取得java.class.path 下的目录 2
		 * 取得当前包所在的路径。如果是CLASS。则找到这个CLASS，如果是jar则找到这个jar下面的所有类包
		 */
		String[] all = classpath.split(";");
		Set<String> paths = new HashSet<String>();
		for (String path : all) {
			if (path == null || path.length() == 0)
				continue;
			if (path.endsWith("/") || path.endsWith("\\")) {
				path = path.substring(0, path.length() - 1);
			}
			paths.add(path);
		}
		if (extPaths != null) {
			for (String path : extPaths) {
				if (path == null || path.length() == 0)
					continue;
				if (path.endsWith("/") || path.endsWith("\\")) {
					path = path.substring(0, path.length() - 1);
				}
				paths.add(path);
			}
		}
		String url = getCurrentPath();
		if (url != null) {
			paths.add(url);
		}

		Set<String> result = new TreeSet<String>();

		for (String path : paths) {
			path.trim();
			if (path.endsWith(".jar")) {
				findJar(path, packageName, inner, result);
			} else {
				findFile(path, packageName, inner, result);
			}
		}
		findJar(System.getProperty("java.home") + "/lib/rt.jar", packageName,
				inner, result);

		return result.toArray(new String[result.size()]);
	}

	public static String getCurrentPath() {
		// 如果如果URL是file:/C:/.../WEB-INF/classes/com/rongji/dfish/common/ClassUtil.class
		// 或linux下的
		// file://usr/myapp/.../WEB-INF/classes/com/rongji/dfish/common/ClassUtil.class
		// 那么截取 C:/.../WEB-INF/classes 或者 /usr/myapp/.../WEB-INF/classes 当作路径
		// 如果URL 是
		// jar:file:/C:/.../WEB-INF/lib/xxxx.jar!/com/rongji/dfish/common/ClassUtil.class
		// 或者
		// webjar:/C:/.../WEB-INF/lib/xxxx.jar!/com/rongji/dfish/common/ClassUtil.class
		// 那么截取 C:/.../WEB-INF/lib 或者 /usr/myapp/.../WEB-INF/lib 当作路径
		java.net.URL url = ClassUtil.class.getClassLoader().getResource(
				ClassUtil.class.getName().replace('.', '/') + ".class");
		String urlStr = url.toString();
		int beginIndex = urlStr.indexOf(":/") + 2;
		int endIndex = urlStr.length() - ClassUtil.class.getName().length() - 7;// 7为.class的长度，加一个/的长度
		String str = urlStr.substring(beginIndex, endIndex);
		// 如果是以xxxx.jar!或xxxx.zip!结尾的还要查找前一个/
		if (str.endsWith(".jar!") || str.endsWith(".zip!")) {
			int i = str.lastIndexOf("/");
			str = str.substring(0, i);
		}
		return str;
	}

	/**
	 * find class in flat file
	 * 
	 * @param path
	 * @param packName
	 * @throws Exception
	 */
	private static void findFile(String pathStr, final String packName,
			final boolean inner, final Set<String> result) throws Exception {
		// System.out.println("searching package[" + packName + "] in file [" +
		// pathStr + "]");

		File path = new File(pathStr + "/" + packName.replace('.', '/'));
		if (path.exists() && path.isDirectory()) {
			for (File file : path.listFiles()) {
				String name = file.getName();
				if (name.endsWith(".class")) {
					if (inner || name.indexOf('$') < 0) {
						result.add(packName + "."
								+ name.substring(0, name.length() - 6));
					}
				}
			}
		}
		File path2 = new File(pathStr);
		if (path2.exists() && path2.isDirectory()) {
			for (File file : path2.listFiles()) {
				if (file.getName().endsWith(".jar")) {
					try {
						findJar(file, packName, inner, result);
					} catch (Exception e) {
						System.err.println("ignore jar [" + file.getName()
								+ "] see the exception below:");
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * find class in jar
	 * 
	 * @param jar
	 * @param packName
	 * @throws Exception
	 */
	private static void findJar(String jar, String packName, boolean inner,
			Set<String> result) throws Exception {
		ZipInputStream in = new ZipInputStream(new FileInputStream(
				new File(jar)));
		ZipEntry ze = null;
		// System.out.println("searching package[" + packName + "] in jar [" +
		// jar + "]");
		while ((ze = in.getNextEntry()) != null) {
			String name = ze.getName();
			if ((inner || ze.getName().indexOf('$') < 0)
					&& name.startsWith(packName.replace(".", "/"))
					&& name.endsWith(".class")
					&& (name.lastIndexOf("/") == packName.length())) {
				result.add(name.replace('/', '.').substring(0,
						name.length() - 6));
			}
		}
	}

	private static void findJar(File jar, String packName, boolean inner,
			Set<String> result) throws Exception {
		ZipInputStream in = new ZipInputStream(new FileInputStream(jar));
		ZipEntry ze = null;
		// System.out.println("searching package[" + packName + "] in jar [" +
		// jar + "]");
		while ((ze = in.getNextEntry()) != null) {
			String name = ze.getName();
			if ((inner || ze.getName().indexOf('$') < 0)
					&& name.startsWith(packName.replace(".", "/"))
					&& name.endsWith(".class")
					&& (name.lastIndexOf("/") == packName.length())) {
				result.add(name.replace('/', '.').substring(0,
						name.length() - 6));
			}
		}
	}
}
