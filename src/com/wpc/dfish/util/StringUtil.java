package com.wpc.dfish.util;

import java.util.Comparator;
import java.util.Random;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 字符串的一些通用方法。
 * @author ITASK team
 *
 */
public final class StringUtil {
	/**
	 * 对比汉字字符串 拼音顺序
	 * 
	 * @param s1
	 *            String
	 * @param s2
	 *            String
	 * @return int
	 */
	public static int chineseCompare(String s1, String s2) {
		return CHINESE_ORDER.compare(s1, s2);
	}
	
	/**
	 * 取得拼音
	 * @param str
	 * @param hasToneNumber 是否包含音调。音调以0(轻声)1(阴平)2(阳平)3(上声)4(去声)
	 * @return
	 */
	public static String getPinyin(String str, boolean hasToneNumber) {
		if(str==null)return null;
		StringBuilder sb=new StringBuilder();
		for(char c:str.toCharArray()){
			if(c>=32 && c<=127){
				sb.append(c);
				continue;
			}//英文字不做解析
			String[] pinyins=PinyinHelper.toHanyuPinyinStringArray(c);
			if(pinyins==null||pinyins.length==0){
				sb.append(c);
				continue;
			}
			if(hasToneNumber){
				sb.append(pinyins[0]);
			}else{
				String s=pinyins[0];
				char lastChar=s.charAt(s.length()-1);
				if(lastChar>='0'&&lastChar<='4'){
					sb.append(s.substring(0,s.length()-1));
				}else{
					sb.append(pinyins[0]);
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 同时取得全拼和简拼的字符串
	 * @param str
	 * @param hasToneNumber
	 * @return
	 */
	public static String[] getPinyinFullShortFormat(String str, boolean hasToneNumber) {
		if(str==null)return null;
		StringBuilder sb=new StringBuilder();
		StringBuilder shortSB=new StringBuilder();
		for(char c:str.toCharArray()){
			if(c>=32 && c<=127){
				sb.append(c);
				shortSB.append(c);
				continue;
			}//英文字不做解析
			String[] pinyins=PinyinHelper.toHanyuPinyinStringArray(c);
			if(pinyins==null||pinyins.length==0){
				sb.append(c);
				shortSB.append(c);
				continue;
			}
			shortSB.append(pinyins[0].charAt(0));
			if(hasToneNumber){
				sb.append(pinyins[0]);
			}else{
				String s=pinyins[0];
				char lastChar=s.charAt(s.length()-1);
				if(lastChar>='0'&&lastChar<='4'){
					sb.append(s.substring(0,s.length()-1));
				}else{
					sb.append(pinyins[0]);
				}
			}
		}
		return new String[]{sb.toString(),shortSB.toString()};
	}
	/**
	 * 排序时 不区分大小写的英文对比器
	 */
	public static final Comparator<String> CASE_INSENSITIVE_ORDER = String.CASE_INSENSITIVE_ORDER;
	/**
	 * 排序时候 中文对比器
	 */
	public static final Comparator<String> CHINESE_ORDER = new ChineseOrderComparator();

	private static class ChineseOrderComparator implements Comparator<String>, java.io.Serializable {
		private static final long serialVersionUID = 8575799808933029327L;

		public int compare(String s1, String s2) {
	    	//LinLW 2009-08-10 修订了关于非常用汉字的排序问题。
	    	if(s1==null&&s2==null)return 0;
	    	if(s1==null)return -1;
	    	if(s2==null)return 1;
	    	char[] c1=s1.toCharArray();
	    	char[] c2=s2.toCharArray();
	    	int len1=c1.length;
	    	int len2=c2.length;
	    	int len=(len1>len2)?len2:len1;
	    	for(int i=0;i<len;i++){
//	    		System.out.println("对比["+c1[i]+"]和["+c2[i]+"]");
	    		if(c1[i]==c2[i])continue;//同一个字就不必比较了
	    		String[] pinyin1 =PinyinHelper.toHanyuPinyinStringArray(c1[i]);
	    		String[] pinyin2 =PinyinHelper.toHanyuPinyinStringArray(c2[i]);
	    		if(pinyin1==null||pinyin1.length==0)return -1;
	    		if(pinyin2==null||pinyin2.length==0)return 1;
	    		String p1=pinyin1[0];
	    		String p2=pinyin2[0];
	    		int comp=p1.compareTo(p2);
	    		if(comp!=0)return comp;
	    		return c1[i]-c2[i];//音同字不同，返回也不一样
	    	}
	    	return len1-len2;
		}
	}

	/**
	 * 截取字符串content前面size个字节的内容 汉字相当于2个英文,数字相当于1个英文，每个英文字母占一个字节空间 要保证汉字不被错误分割 by
	 * YeJL 2006-08
	 * 
	 * @param content
	 *            String 将要截取的源字符串
	 * @param size
	 *            int 截取字符数
	 * @return String
	 */
	public static String shortenString(String content, int size) {
		return shortenString(content, size, 2, "...");
	}

	/**
	 * 截取字符串content前面size个字节的内容 汉字相当于chineseCharSize个英文,数字相当于1个英文，每个英文字母占一个字节空间
	 * 要保证汉字不被错误分割
	 * 
	 * @todo 英文单词，如果可能最好也不要被分割。 by YeJL 2006-08
	 * @param content
	 *            String 将要截取的源字符串
	 * @param limitSize
	 *            int 截取字符数
	 * @param chineseCharSize
	 *            汉字相当于chineseCharSize个字节，一般是GBK为2，IE显示也占2个空间。 UTF8下显示空间还是2
	 *            但字节数为3， GB18030下字节数为4
	 * @param replacePostfix
	 *            被截取的字符用该字符代替
	 * @return String
	 */
	public static String shortenString(String content, int limitSize, int chineseCharSize,
			String replacePostfix) {
		if (content == null || content.equals("") || limitSize == 0) {
			return "";
		}
		if (content.getBytes().length <= limitSize) {
			return content;
		}

		long l = 0;
		StringBuilder sb = new StringBuilder();
		char[] c = content.toCharArray();
		for (int i = 0; i < c.length; i++) {
			char ca = c[i];

			sb.append(ca);
			int ascii = ca;
			if (ascii < 0 || ascii > 255) {
				l += chineseCharSize;
			} else {
				l += 1;
			}
			if (l > (limitSize - replacePostfix.length())) {
				sb.deleteCharAt(sb.length() - 1).append(replacePostfix);
				break;
			}
		}
		return sb.toString();
	}

	private static Random r = new Random(20080815L + System.currentTimeMillis());

	/**
	 * 生成一个指定长度的随机字符串
	 * 
	 * @param length
	 *            int 指定长度
	 * @param containNum
	 *            boolean 是否包含数字
	 * @param containLowcase
	 *            boolean 是否小写数字
	 * @param containUppcase
	 *            boolean 是否包含大写字母
	 * @param containSymbol
	 *            boolean 是否包含符号
	 * @return String
	 */
	public static String getRadomString(int length, boolean containNum, boolean containLowcase,
			boolean containUppcase, boolean containSymbol) {
		if (!containNum && !containLowcase && !containUppcase && !containSymbol) {
			return "";
		}
		StringBuffer sb = new StringBuffer(length);
		if (0 < length) {
			while (sb.length() < length) {
				byte[] can = new byte[length * 2];
				r.nextBytes(can);
				for (int i = 0; i < can.length; i++) {
					if ((containNum && can[i] >= '0' && can[i] <= '9')
						|| (containLowcase && can[i] >= 'a' && can[i] <= 'z')
						|| (containUppcase && can[i] >= 'A' && can[i] <= 'Z')
						|| (containSymbol && 
								((can[i] >= '!' && can[i] <= '/')
								|| (can[i] >= ':' && can[i] <= '@')
								|| (can[i] >= '[' && can[i] <= '`')
								|| (can[i] >= '{' && can[i] <= '~'))
						)) {
						sb.append((char) can[i]);
						if (sb.length() >= length) {
							break;
						}
					}
				}
			}
			return sb.toString();
		}
		return "";
	}
	/**
	 * 判断两个字符串是否equals
	 * 空字符串也可以对比。另外把 null和""当成是一样的。
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean nullAbleEquals(String str1, String str2) {
		if (str1 == null || str1.equals("")) {
			return (str1 == null || str1.equals(""));
		} else {
			return str1.equals(str2);
		}
	}
}
