package com.wpc.dfish.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 可以通过类名和方法名调用哪个某个方法。
 * 该方法通过反射调用。如果同一个方法名被重载过。则尝试通过参数的类型寻找一个合适的方法。
 * 注意如果参数的类型有可能有继承关系。另外参数可能为空。
 * 这个类可能不是总能找到最适合的方法。
 * @author ITASK team
 *
 */
public class MethodCaller {
	/**
	 * 调用某个方法的
	 * @param clzInstance
	 * @param methodName
	 * @param params
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(Object clzInstance,String methodName,Object... params) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class targetClz=clzInstance.getClass();
		Method mth = findBestMethod(targetClz,methodName,params);
		return mth.invoke(clzInstance, params);
	}
	private static Class[] NO_PARAM=new Class[0];
	@SuppressWarnings("unchecked")
	private static Method findBestMethod(Class targetClz,String methodName,Object... params) throws NoSuchMethodException{
		//首先如果PARAM为空，那么可以精确查找
		if(params==null||params.length==0){
			Method m=targetClz.getMethod(methodName, NO_PARAM);
			return m;
		}
		//否则看PARAM的类型取得方法
		boolean hasNullParam=false;
		Class[] clzs=new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			Object obj=params[i];
			if(obj==null){
				hasNullParam=true;
				break;
			}
			clzs[i]=obj.getClass();
		}
		if(!hasNullParam){
			//尝试找到方法
			try{
				Method m=targetClz.getMethod(methodName, NO_PARAM);
				return m;
			}catch(NoSuchMethodException nsme){}
		}
		//如果方法仍旧找不到，尝试查找
		Method[]mths=targetClz.getMethods();
//		ArrayList<Method> candidate=new ArrayList<Method>();
		outter:for (Method method : mths) {
			if(!methodName.equals(method.getName()))
				continue;
			//参数个数应该匹配
			if(method.getParameterTypes().length!=params.length){
				continue;
			}
			Class[] paramClzs=method.getParameterTypes();
			for (int i = 0; i < paramClzs.length; i++) {
				if(params[i]==null)continue;
				if(!paramClzs[i].isAssignableFrom(params[i].getClass())){
					continue outter;
				}
			}
			return method;
		}
		throw new NoSuchMethodException(methodName);
	}
	/**
	 * 用于调用静态方法
	 * @param clz
	 * @param methodName
	 * @param params
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static Object invokeMethod(Class clz,String methodName,Object... params) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Method mth = findBestMethod(clz,methodName,params);
		return mth.invoke(null, params);
	}

}
