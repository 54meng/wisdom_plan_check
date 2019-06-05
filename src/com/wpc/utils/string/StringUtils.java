package com.wpc.utils.string;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang.ArrayUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.wpc.dfish.util.PinyinCode;
import com.wpc.dfish.util.Utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * 字符串工具集合
 * 
 * @author hqj
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
	
	
	/**
	 * 如果系统中存在旧版本的数据，则此值不能修改，否则在进行密码解析的时候出错
	 */
	private final static String DES = "DES";
	private final static String ISO8859_1 = "8859_1";
	public final static String ENCODE_UTF8 = "UTF-8";

	/**
	 * 创建指定数量的随机字符串
	 * 
	 * @param numberFlag
	 *            是否是数字
	 * @param length
	 * 			    长度
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length) {
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (length>4?count>=2:count>0) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}
	
	/**
	 * MD5 摘要计算(byte[]).
	 * 
	 * @param src
	 *            byte[]
	 * @throws Exception
	 * @return byte[] 16 bit digest
	 */
	public static byte[] md5(byte[] src) throws Exception {
		MessageDigest alg = MessageDigest.getInstance("MD5");
		return alg.digest(src);
	}
	
	/**
	 * 转类似" field1:field1value;field2:field2value;field3:field3value; "的字符串 TO Map
	 * @param text
	 * @return
	 */
	public static Map string2Map(String text){
		Map map = new HashMap();
		if(Utils.notEmpty(text)){
			String [] sss = text.split(";");
			if(sss!=null){
				for(String ss:sss){
					String [] s = ss.split(":");
					if(s!=null&&s.length>=2){
						map.put(s[0], s[1]);
					}
				}
			}
		}
		return map;
	}
	/**
	 *  转Map TO 类似" field1:field1value;field2:field2value;field3:field3value; "的字符串  
	 * @param text
	 * @return
	 */
	public static String mapToString(Map map){
		String mapString = new String();
		if(map!=null&&map.size()>0){
			mapString = map.toString().replaceAll("\\{|\\}", "").replace("=", ":").replace(",", ";").replaceAll("\\s*", "")+";";
		}
		return mapString;
	}
	
	 /**
	   * 对url的非ACISS字符进行编码，可确保被编码的url可以访问
	   * 	<p>注：原有的URLEncoder.encode()是对url进行全部编码，编码后，url不能访问，因为:// 被编码了</p>
	   * @param URL 被编码的URL字符串
	   * @param charset 字符集
	   * @return 编码好的URL
	   * @throws UnsupportedEncodingException 不支持的字符集
	   */
	public static String encodeURL(String url,String charset){
		if(Utils.isEmpty(url)){
			return url;
		}
		//解决方式2:对URL中的非ASCII字符进行编码 ，可能会存在不兼容问题，暂未发现
		String zhPattern = "([^\\x00-\\xff]|×)+";
		// String zhPattern = "([\u4E00-\u9FA5]|[\uFE30-\uFFA0])+";
		Pattern p = Pattern.compile(zhPattern);
		Matcher m = p.matcher(url);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			try {
				m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		m.appendTail(b);
		return b.toString();
	}
	

	 /**
	   * 对URL进行分解后再进行编码，可确保被编码的url可以访问
	   * 	<p>注：原有的URLEncoder.encode()是对url进行全部编码，编码后，url不能访问，因为:// 被编码了</p>
	   * 
	   * @param URL 被编码的URL字符串
	   * @param charset 字符集
	   * @return 编码好的URL
	   * @throws UnsupportedEncodingException 不支持的字符集
	   */
	public static String encodeURL2(String URL, String charset) {
		if(Utils.notEmpty(URL)){
			//替换路径符
			URL.replace("\\", "/");
			//对URL进行分解，然后编码
			String[] urls = URL.split("\\?");
			if(urls!=null&&urls.length>0){
				String preUrl = urls[0];
				String paramUrl = null;
				if(urls.length>1){
					paramUrl = urls[1];
				}
				//1.处理url无参数部分
				if(Utils.notEmpty(preUrl)){
					String headUrl = null ;
					if(preUrl.startsWith("http://")){
						headUrl = "http://";
						preUrl = preUrl.substring("http://".length());
					}
					String[] preUrls = preUrl.split("/");
					preUrl = "";
					if(preUrls!=null&&preUrls.length>0){
						for(String str:preUrls){
							if(Utils.notEmpty(str)){
								try {
									preUrl += "/"+URLEncoder.encode(str, charset);
								} catch (UnsupportedEncodingException e) {
									preUrl += "/"+str;
								}
							}
						}
					}
					preUrl = (headUrl!=null?headUrl:"")+(preUrl.length()>0?preUrl.substring(1):"");
				}
				//2.处理url参数部分
				if(Utils.notEmpty(paramUrl)){
					String[] params = paramUrl.split("&");
					paramUrl = "";
					if(params!=null&&params.length>0){
						for(String param : params){
							if(Utils.notEmpty(param)){
								if(param.indexOf("=")>0){
									String[] paramss = param.split("=");
									param = "";
									for(String str : paramss){
										try {
											param += "="+URLEncoder.encode(str, charset);
										} catch (UnsupportedEncodingException e) {
											param += "="+ str;
										}
									}
									paramUrl += (param.length()>0?"&"+param.substring(1):"");
								}else{
									try {
										paramUrl += "&"+URLEncoder.encode(param, charset);
									} catch (UnsupportedEncodingException e) {
										paramUrl += "&"+param;
									}
								}
							}
						}
					}
					paramUrl = (paramUrl.length()>0?"?"+paramUrl.substring(1):"");
				}
				URL = preUrl + (paramUrl!=null?paramUrl:"");
			}
		}
		return URL;
	}
	
	public static void main(String[] args) {
		System.out.println(leftPad("111", 8,"0"));
	}
    /**
	 * 提取指定前后缀HTML的内容 如：extInnerHtml("<em>1</em><EM>2</EM>","<em>","</em>")=
	 * {1,2}
	 * 
	 * @param preHtml
	 *            html前缀
	 * @param endHtml
	 *            html后缀
	 * @author lzw
	 * @return 返回从指定html中取得的innerHTML列表
	 */
	public static List<String> extInnerHtml(String source,String preHtml,String endHtml){
		List<String> result = new ArrayList<String>();
		Set<String> existWords = new HashSet<String>();
		Pattern pattern = Pattern.compile(preHtml+"(.*?)"+endHtml);
		Matcher match = pattern.matcher(source);
		while(match.find()){
			String word = match.group(1);
			if(!existWords.contains(word)){
				result.add(word);
				existWords.add(word);
			}
		}
		return result;
	}
	/**
	 * 匹配words列表，将匹配成功的数据用指定的HTML前后缀包含起来
	 * @param source 待处理的原字符串
	 * @param preHtml HTML前缀
	 * @param endHtml HTML后缀
	 * @param words 指定要匹配的词组列表
	 * @author lzw
	 * @return 返回替换后的HTML字符串
	 */
	public static String impHtmlForWord(String source,String preHtml,String endHtml,List<String> words){
		String temp = source;
		if(words!=null){
			for(String word:words){
				temp = temp.replaceAll(word, preHtml+word+endHtml);
			}
		}
		return temp;
	}
	
	/**
	 * MD5 摘要计算(String).
	 * 
	 * @param src
	 *            String
	 * @throws Exception
	 * @return String
	 */
	public static String md5(String src) throws Exception {
		return byte2hex(md5(src.getBytes()));
	}

	/**
	 * 将字符串用ch分割并放入队列
	 * 
	 * @param tags
	 * @param ch
	 * @return
	 */
	public static List stringToList(String tags, String ch) {
		if (tags == null)
			return null;
		ArrayList<String> tagList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(tags, ch);
		while (st.hasMoreElements()) {
			tagList.add(st.nextToken());
		}
		return tagList;
	}

	/**
	 * 将字符串用空格分割并放入队列
	 * 
	 * @param tags
	 * @return
	 */
	public static List stringToList(String tags) {
		if (tags == null)
			return null;
		ArrayList<String> tagList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(tags);
		while (st.hasMoreElements()) {
			tagList.add(st.nextToken());
		}
		return tagList;
	}

	/**
	 * BASE64编码
	 * 
	 * @param s
	 * @return String
	 */
	public static byte[] enBASE64(byte[] bytes) {
		return Base64Code.encode(bytes);
	}

	/**
	 * BASE64反编码
	 * 
	 * @param bytes
	 * @return byte[]
	 */
	public static byte[] deBASE64(byte[] bytes) {
		return Base64Code.decode(bytes);
	}

	/**
	 * BASE64编码
	 * 
	 * @param s
	 * @return String
	 */
	public static String enBASE64(String s) {
		//使用java自带的加密方式解决乱码
		if (s == null)
            return null;
        String res = "";
        try {
            res = new BASE64Encoder().encode(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
	}

	/**
	 * BASE64反编码
	 * 
	 * @param s
	 * @return String
	 */
	public static String deBASE64(String s) {
		/*if (s != null) {
			byte abyte0[] = s.getBytes();
			abyte0 = Base64Code.decode(abyte0);
			//编码设置，解决乱码问题
			try {
				s = new String(abyte0, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			abyte0 = null;
			return s;
		}
		return null;*/
		if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b,"UTF-8");
        } catch (Exception e) {
            return null;
        }
	}

	/**
	 * HTML输出内容格式转换
	 * 
	 * @param content
	 * @return
	 */
	public static String formatContent(String content) {
		if (content == null)
			return "";
		String randomStr = String.valueOf(System.currentTimeMillis());
		String html = StringUtils.replace(content, "&nbsp;", randomStr);
		html = StringUtils.replace(html, "&", "&amp;");
		html = StringUtils.replace(html, "'", "&apos;");
		html = StringUtils.replace(html, "\"", "&quot;");
		html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
		html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
		html = StringUtils.replace(html, "<", "&lt;");
		html = StringUtils.replace(html, ">", "&gt;");
		return StringUtils.replace(html, randomStr, "&nbsp;").trim();
	}

	/**
	 * 用于提取HTML中的纯文本信息，过滤掉所有的HTML标签
	 */
	private final static NodeFilter nfilter = new NodeFilter() {
		public boolean accept(Node node) {
			// IMPORTANT: 只显示TextNode的内容
			return (node instanceof TextNode);
		}
	};

	/**
	 * 抽取纯文本信息
	 * 
	 * @param inputHtml
	 * @return
	 */
	public static String extractText(String html) throws Exception {
		if (html == null)
			return null;
		StringBuffer text = new StringBuffer();
		Parser parser = new Parser();
		parser.setInputHTML(html);
		parser.setEncoding(ISO8859_1);
		// 遍历所有的节点
		NodeList nodes;
		try {
			nodes = parser.extractAllNodesThatMatch(nfilter);
		} catch (ParserException e) {
			return html;
		}
		for (int i = 0; i < nodes.size(); i++) {
			TextNode node = (TextNode) nodes.elementAt(i);
			text.append(node.getText());
		}
		return StringUtils.remove(text.toString(), "&nbsp;");
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null)
			return false;
		email = email.trim();
		if (email.indexOf(' ') != -1)
			return false;

		int idx = email.indexOf('@');
		if (idx == -1 || idx == 0 || (idx + 1) == email.length())
			return false;
		if (email.indexOf('@', idx + 1) != -1)
			return false;
		if (email.indexOf('.') == -1)
			return false;
		return true;
		/*
		 * Pattern emailer; if(emailer==null){ String check =
		 * "^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		 * emailer = Pattern.compile(check); } Matcher matcher =
		 * emailer.matcher(email); return matcher.matches();
		 */
	}

	/**
	 * 判断字符串是否是一个IP地址
	 * 
	 * @param addr
	 * @return
	 */
	public static boolean isIPAddr(String addr) {
		if (isEmpty(addr))
			return false;
		String[] ips = split(addr, '.');
		if (ips.length != 4)
			return false;
		try {
			int ipa = Integer.parseInt(ips[0]);
			int ipb = Integer.parseInt(ips[1]);
			int ipc = Integer.parseInt(ips[2]);
			int ipd = Integer.parseInt(ips[3]);
			return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
					&& ipc <= 255 && ipd >= 0 && ipd <= 255;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 加密
	 * 
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 数据解密
	 * 
	 * @param data
	 * @param key
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public final static String decrypt(String data, String key) {
		if (data != null)
			try {
				return new String(decrypt(hex2byte(data.getBytes()), key.getBytes()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	/**
	 * 数据加密
	 * 
	 * @param data
	 * @param key
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public final static String encrypt(String data, String key) {
		if (data != null)
			try {
				return byte2hex(encrypt(data.getBytes(), key.getBytes()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 大小写无关的字符串替换策略
	 * 
	 * @param str
	 * @param src
	 * @param obj
	 * @return
	 */
	public static String replaceIgnoreCase(String str, String src, String obj) {
		String l_str = str.toLowerCase();
		String l_src = src.toLowerCase();
		int fromIdx = 0;
		StringBuffer result = new StringBuffer();
		do {
			int idx = l_str.indexOf(l_src, fromIdx);
			if (idx == -1)
				break;
			result.append(str.substring(fromIdx, idx));
			result.append(obj);
			fromIdx = idx + src.length();
		} while (true);
		result.append(str.substring(fromIdx));
		return result.toString();
	}

	/**
	 * 根据汉字字符获得笔画数,拼音和非法字符默认为0
	 * 
	 * @param charcator
	 * @return int
	 */
	public static int getStrokeCount(char charcator) {
		byte[] bytes = (String.valueOf(charcator)).getBytes();
		if (bytes == null || bytes.length > 2 || bytes.length <= 0) {
			// 错误引用,非合法字符
			return 0;
		}
		if (bytes.length == 1) {
			// 英文字符
			return 0;
		}
		if (bytes.length == 2) {
			// 中文字符
			int highByte = 256 + bytes[0];
			int lowByte = 256 + bytes[1];
			return getStrokeCount(highByte, lowByte);
		}

		// 未知错误
		return 0;
	}

	/**
	 * @param highByte
	 *            高位字节
	 * @param lowByte
	 *            低位字节
	 * @return int
	 */
	private static int getStrokeCount(int highByte, int lowByte) {
		if (highByte < 0xB0 || highByte > 0xF7 || lowByte < 0xA1
				|| lowByte > 0xFE) {
			// 非GB2312合法字符
			return -1;
		}
		int offset = (highByte - 0xB0) * (0xFE - 0xA0) + (lowByte - 0xA1);
		// return Constants.gb2312StrokeCount[offset];
		return -1;
	}

	/**
	 * 该方法返回一个字符串的拼音，对于要做敏感字 检查时应该一个字一个字来获取其拼音以免无法 得知每个字对应的拼音。
	 * 
	 * @param word
	 * @return String
	 */
	public static String getPinyin(String word) {
		String pinyin = "";
		for (int i = 0; i < word.length(); i++)
			pinyin += getPinyin2(getCode(word.charAt(i)));
		return pinyin;
	}

	/**
	 * 该方法返回一个字符的DBCS编码值
	 * 
	 * @param cc
	 * @return int
	 */
	protected static int getCode(char cc) {
		byte[] bs = String.valueOf(cc).getBytes();
		int code = (bs[0] << 8) | (bs[1] & 0x00FF);
		if (bs.length < 2)
			code = (int) cc;
		bs = null;
		return code;
	}

	/**
	 * 该方法通过DBCS的编码值到哈希表中查询得到对应的拼音串
	 * 
	 * @param hz
	 * @return String
	 */
	protected static String getPinyin2(int hz) {
		String py = "";
		if (hz > 0 && hz < 160)
			py += hz;
		// else if (hz < -20319 || hz > -10247);
		else if (hz <= -10247 && hz >= -20319) {
			PinyinCode pc = null;
			// int i = Constants.pinyin.size() - 1;
			// for (; i >= 0; i--) {
			// pc = (PinyinCode) Constants.pinyin.get(i);
			// if (pc.code <= hz)
			// break;
			// }
			// if (i >= 0)
			// py = pc.pinyin;
		}
		return py;
	}

	/**
	 * 用户名必须是数字或者字母的结合
	 * 
	 * @param username
	 * @return
	 */
	public static boolean isLegalUsername(String username) {
		for (int i = 0; i < username.length(); i++) {
			char ch = username.charAt(i);
			if (!isAscii(ch) && ch != '.' && ch != '_' && ch != '-'
					&& ch != '+' && ch != '(' && ch != ')' && ch != '*'
					&& ch != '^' && ch != '@' && ch != '%' && ch != '$'
					&& ch != '#' && ch != '~' && ch != '-')
				return false;
		}
		return true;
	}

	/**
	 * 判断是否是字母和数字的结合
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isAsciiOrDigit(String name) {
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (!isAscii(ch))
				return false;
		}
		return true;
	}

	public static boolean isAscii(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
				|| (ch >= '0' && ch <= '9');
	}

	/**
	 * 返回姓名的拼音首字母
	 * 
	 * @param username
	 * @return
	 */
	public static String getTxlUserPinyin(String username) {
		if (username.getBytes().length == (2 * username.length())) {
			// 纯中文
			StringBuffer pinyin = new StringBuffer();
			for (int i = 0; i < username.length(); i++) {
				String py = StringUtils.getPinyin(String.valueOf(username
						.charAt(i)));
				if (py != null && py.length() > 0)
					pinyin.append(py.charAt(0));
				else
					pinyin.append('V');
			}
			return pinyin.toString().toUpperCase();
		} else if (username.getBytes().length == username.length()) {
			int len = (username.length() > 3) ? 3 : username.length();
			return username.substring(0, len).toUpperCase();
		} else {
			StringBuffer pinyin = new StringBuffer();
			for (int i = 0; i < username.length(); i++) {
				char ch = username.charAt(i);
				try {
					String py = StringUtils.getPinyin(String.valueOf(ch));
					if (py != null && py.length() > 0)
						pinyin.append(py.charAt(0));
					else
						pinyin.append(ch);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
				if (pinyin.length() >= 3)
					break;
			}
			return pinyin.toString().toUpperCase();
		}
	}

	public static String htmlSpecialChars(String source) {
		if (source == null) {
			return "";
		}
		StringBuffer dest = new StringBuffer(source.length());
		for (int i = 0; i < source.length(); i++) {
			char c;
			c = source.charAt(i);
			if (c == '>') {
				dest.append("&gt;");
			} else if (c == '<') {
				dest.append("&lt;");
			} else if (c == '&') {
				dest.append("&amp;");
			} else if (c == '"') {
				dest.append("&quot;");
			} else {
				dest.append(c);
			}
		}
		return dest.toString();
	} // end method --> public String htmlSpecialChars(String source)

	public static String oppHtmlSpecialChars(String source) {
		if (source == null) {
			return null;
		}
		source = replace(source, "&gt;", ">");
		source = replace(source, "&lt;", "<");
		source = replace(source, "&quot;", "\"");
		source = replace(source, "&amp;", "&");
		source = replace(source, "&nbsp;", " ");
		return source;
	} // end method --> public String oppHtmlSpecialChars(String source)

	/**
	 * Converts \n into html equivalents <br>
	 * from a source string and returns it. (new line to break)
	 * 
	 */
	public static String nl2br(String source) {
		if (source == null) {
			return "";
		}
		StringBuffer dest = new StringBuffer(source.length());
		for (int i = 0; i < source.length(); i++) {
			char c;
			c = source.charAt(i);
			if (c == '\n') {
				dest.append("<br>");
			} else {
				dest.append(c);
			}
		}
		return dest.toString();
	} // end method --> public String nl2Br(String source)

	/**
	 * Converts ',",\,NUL into sql equivalents \',\",\\,\NUL from a source
	 * string and returns it. (new line to break)
	 */
	public static String addSlashes(String source) {
		if (source == null) {
			return "";
		}
		StringBuffer dest = new StringBuffer(source.length());
		for (int i = 0; i < source.length(); i++) {
			char c;
			c = source.charAt(i);
			if (c == '"') {
				dest.append("\\\"");
			} else if (c == '\'') {
				dest.append("\\\'");
			} else if (c == '\\') {
				dest.append("\\\\");
			} else if ((c == 'N') && ((i + 2) < source.length())
					&& (source.charAt(i + 1) == 'U')
					&& (source.charAt(i + 2) == 'L')) {
				dest.append("\\N");
			} else {
				dest.append(c);
			}
		}
		return dest.toString();
	} // end method --> public String addSlashes(String source)

	/**
	 * Converts sql \',\",\\,\NUL into ',",\,NUL from a source string and
	 * returns it. (new line to break)
	 */
	public static String stripSlashes(String source) {
		if (source == null) {
			return "";
		}
		StringBuffer dest = new StringBuffer(source.length());
		for (int i = 0; i < source.length(); i++) {
			char c;
			c = source.charAt(i);
			if ((c == '\\') && (i + 1 < source.length())
					&& (source.charAt(i + 1) == '\'')) {
				dest.append("\'");
				i++;
			} else if ((c == '\\') && (i + 1 < source.length())
					&& (source.charAt(i + 1) == '\"')) {
				dest.append("\"");
				i++;
			} else if ((c == '\\') && ((i + 3) < source.length())
					&& (source.charAt(i + 1) == 'N')
					&& (source.charAt(i + 2) == 'U')
					&& (source.charAt(i + 3) == 'L')) {
				dest.append("N");
				i++;
			} else if ((c == '\\') && (i + 1 < source.length())
					&& (source.charAt(i + 1) == '\\')) {
				dest.append("\\");
				i++;
			} else {
				dest.append(c);
			}
		}
		return dest.toString();
	} // end method --> public String nl2Br(String source)

	/**
	 * Converts a string to integer. If fails is not throwing a
	 * NumberFormatException, instead return 0.
	 */
	public static int toInt(String source) {
		try {
			return Integer.parseInt(source);
		} catch (NumberFormatException notint) {
			return 0;
		}
	}

	public static long toLong(String source) {
		try {
			return Long.parseLong(source);
		} catch (NumberFormatException notint) {
			return 0;
		}
	}

	public static float toFloat(String source) {
		try {
			return Float.parseFloat(source);
		} catch (NumberFormatException notint) {
			return 0;
		}
	}
	
	public static double toDouble(String source) {
		try {
			return Double.parseDouble(source);
		} catch (NumberFormatException notint) {
			return 0;
		}
	}

	/**
	 * Replace in a String the last occurence of another String with a
	 * replacement string. This occurence must to be at the end of the source.
	 */
	public static String replaceLast(String source, String occurence,
			String replacement) {
		if (source.lastIndexOf(occurence) == source.length()
				- occurence.length()) {
			return source.substring(0, source.lastIndexOf(occurence))
					+ replacement;
		} else {
			return source;
		}
	} // end method --> public static String replaceLast(String source, String
		// occurence, String replacement)

	/**
	 * Replace in a String all occurence of another String with a replacement
	 * string.
	 * 
	 * @param source
	 *            the source string
	 * @param occurence
	 *            the occurence string
	 * @param replacement
	 *            the replacement string
	 */
	public static String replaceAll(String source, String occurence,
			String replacement) {
		return replace(source, occurence, replacement);

		/*
		 * if (source == null || source.equals("")) return ""; while
		 * (source.indexOf(occurence) != -1) { source = source.substring(0,
		 * source.indexOf(occurence)) + replacement + source.substring(
		 * source.indexOf(occurence) + occurence.length(), source.length()); }
		 * return source;
		 */
	} // end method --> public static String replaceAll(String source, String
		// occurence, String replacement)

	/**
	 * Replace in a String all occurence of another String with a replacement
	 * string.
	 * 
	 * @param source
	 *            the source string
	 * @param occurence
	 *            the occurence string
	 * @param replacement
	 *            the replacement string
	 */
	public static String replace(String source, String occurence, String replacement) {
		if (source == null || source.equals(""))
			return "";

		StringBuffer buffer = new StringBuffer("");
		while (source.indexOf(occurence) != -1) {
			// System.out.println(temp);
			// System.out.println(source);
			buffer.append(source.substring(0, source.indexOf(occurence))
					+ replacement);
			source = source.substring(source.indexOf(occurence)
					+ occurence.length());
		}
		buffer.append(source);
		return buffer.toString();
	}

	/**
	 * Replace in a String a Substring with a replacement starting from a
	 * specified Substring or char (begin) until other specified Substring or
	 * char (end).
	 * 
	 * @param source
	 *            the source string
	 * @param begin
	 *            the begin substring
	 * @param end
	 *            the end substring
	 * @param replacement
	 *            the replacement string
	 */
	public static String replaceFromStrToStr(String source, String begin,
			String end, String replacement) {
		while ((source.indexOf(begin) != -1) && (source.indexOf(end) != -1)
				&& (source.indexOf(begin) < source.indexOf(end))) {
			source = source.substring(0, source.indexOf(begin))
					+ replacement
					+ source.substring(source.indexOf(end) + end.length(),
							source.length());
		}
		return source;
	} // end method --> public static String replaceFromStrToStr(String
		// source, String begin, String end, String replacement)

	// 转换成unicode
	public static String toUnicode(String strIn) {
		byte[] b;
		String strOut = null;
		if (strIn == null || (strIn.trim()).equals(""))
			return strIn;
		try {
			b = strIn.getBytes("GBK");
			strOut = new String(b, "ISO8859_1");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return strOut;
	}

	// 转换成汉字
	public static String toGB2312(String strIn) {
		String strOut = null;
		if (strIn == null || (strIn.trim()).equals(""))
			return strIn;
		try {
			byte[] b = strIn.getBytes("ISO8859_1");
			strOut = new String(b, "GBK");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return strOut;
	}

	public static String toUTF8(String strIn) {
		String strOut = null;
		if (strIn == null || (strIn.trim()).equals(""))
			return strIn;
		try {
			byte[] b = strIn.getBytes("ISO8859_1");
			strOut = new String(b, "UTF-8");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return strOut;
	}
	
	public static String URLDecoder(String source) {
		return URLDecoder(source, ENCODE_UTF8);
	}

	public static String URLEncoder(String source) {
		return URLEncoder(source, ENCODE_UTF8);
	}

	public static String URLDecoder(String source, String encoding) {
		String res = null;
		try {
			res = java.net.URLDecoder.decode(source, encoding);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getLocalizedMessage());
		}

		return res;
	}

	public static String URLEncoder(String source, String encoding) {
		if(source == null || "".equals(source)) {
			return source;
		}
		String res = null;
		try {
			res = java.net.URLEncoder.encode(source, encoding);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getLocalizedMessage());
		}

		return res;
	}

	public static String convertEncoding(String source, String inEncoding, String outEncoding) {
		byte[] b;
		String strOut = null;
		if (source == null || (source.trim()).equals(""))
			return source;
		try {
			b = source.getBytes(inEncoding);
			strOut = new String(b, outEncoding);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return strOut;
	}
	
	/**
	 * 将全角字符串转为半角字符串
	 * 
	 * @param str 字符串
	 * @return
	 */
	public static String toDBC(String str) {
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; ++i) {
			if (c[i] == '　') {
				c[i] = ' ';
			} else if ((c[i] > 65280) && (c[i] < 65375))
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	
	public static String toSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; ++i)
			if (c[i] == ' ') {
				c[i] = '　';
			} else {
				if ((c[i] > '@') && (c[i] < '['))
					continue;
				if ((c[i] > '`') && (c[i] < '{')) {
					continue;
				}

				if (c[i] < '')
					c[i] = (char) (c[i] + 65248);
			}
		return new String(c);
	}

	public static String toNSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; ++i) {
			if (c[i] == ' ') {
				c[i] = '　';
			} else if (c[i] < '')
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}
	
	/**
	 * 将处理字符串的空格
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String rightTrim(String str) {
		if (str != null) {
			char[] chars = str.toCharArray();
			for (int i = chars.length - 1; i > 0; --i) {
				if (chars[i] == ' ')
					continue;
				if (chars[i] != '\t') {
					return new String(ArrayUtils.subarray(chars, 0, i + 1));
				}
			}
		}
		return str;
	}


	/**
	 * str:一串以pattern隔开的字符串； 把一串以pattern隔开的字符串赋值给数组单元并返回
	 */
	public static String[] split(String str, String pattern) {
		if (str == null)
			return null;

		Vector vectors = new Vector();
		int i = str.indexOf(pattern);
		String temp = "";
		String[] results = null;
		while (i > -1) {
			temp = str.substring(0, i);
			str = str.substring(i + 1);
			vectors.addElement(temp);
			i = str.indexOf(pattern);
		}
		if (!str.equalsIgnoreCase("")) {
			vectors.addElement(str);
		}
		results = new String[vectors.size()];
		for (int j = 0; j < vectors.size(); j++) {
			results[j] = (String) vectors.get(j);
		}
		vectors.clear();
		return results;
	}

	/**
	 * str:一串以pattern隔开的字符串； 把一串以pattern隔开的字符串赋值给数组单元并返回 ignoreNull 是否忽略头尾空格、空字符
	 */
	public static String[] split(String str, String pattern, boolean ignoreNull) {
		if (str == null)
			return null;

		Vector vectors = new Vector();
		int i = str.indexOf(pattern);
		String temp = "";
		String[] results = null;
		while (i > -1) {
			temp = str.substring(0, i);
			str = str.substring(i + 1);
			vectors.addElement(temp);
			i = str.indexOf(pattern);
		}

		if (!ignoreNull) {
			vectors.addElement(str);
		} else {
			if (StringUtils.isNotBlank(str))
				vectors.addElement(str);
		}

		results = new String[vectors.size()];
		for (int j = 0; j < vectors.size(); j++) {
			results[j] = (String) vectors.get(j);
		}
		vectors.clear();
		return results;
	}

	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}

	public static boolean isNotEmpty(String str) {
		return (!(isEmpty(str)));
	}

	public static final String noNull(String string, String defaultString) {
		return ((isEmpty(string)) ? defaultString : string);
	}

	public static final String noNull(String string) {
		return noNull(string, "");
	}

	/**
	 * 为检查 null 值，如果为 null，将返回 "",不为空时将替换其非 html 符号
	 */
	public static String strnull(String strn) {
		if (strn == null || strn.toLowerCase().equals("null")) {
			strn = "";
		} else {
			// strn = this.replaceQuot(strn);
			strn = strn.trim();
		}
		return (strn);
	}

	public static String strnull(String str, String replace) {
		if (StringUtils.isNotBlank(str))
			return replace;
		else
			return str;
	}

	/**
	 * 在单元格显示时，替换所有的 "" 为 &nbsp;
	 */
	public static String getNbsp(String str) {
		str = strnull(str);
		if (str.equals(""))
			str = "&nbsp;";
		return str;
	}

	/**
	 * 把字符串数组合成一字串<br>
	 * 
	 * 参数：ARRAY 为要组合连接的字符串数组<br>
	 * flag 为各数组单元间的连接符号<br>
	 */
	public static String joinstr(String[] ARRAY, String flag) {
		if (ARRAY == null)
			return null;

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < ARRAY.length; i++) {
			if(Utils.notEmpty(ARRAY[i])){
				buffer.append(flag + ARRAY[i]);
			}
		}
		if(buffer.length()>0){
			buffer.delete(0, 1);
		}
		return buffer.toString();
	}

	/**
	 * Method compareIP. 比较两个IP ip1 = ip2 返回0 ip1 > ip2 返回1 ip1 < ip2 返回-1
	 * 
	 * @param ip1
	 * @param ip2
	 * @return int
	 */
	public static int compareIP(String ip1, String ip2) {
		// System.out.println(ip1+" - "+ip2);
		int result = 0;
		if (ip1.equals(ip2)) {
			return result;
		} else {
			String[] ipArray1 = split(ip1, ".");
			String[] ipArray2 = split(ip2, ".");
			int a = 0, b = 0;
			for (int j = 0; j < 4; j++) {
				a = toInt(ipArray1[j]);
				b = toInt(ipArray2[j]);
				if (a > b) {
					result = 1;
					break;
				} else if (a < b) {
					result = -1;
					break;
				} else
					continue;
			}
			return result;
		}
	}

	/**
	 * 比较一个IP段和一个IP 如果ALLOW_IP_SECTION包含 REMOTE_IP，则返回TRUE，否则返回FALSE
	 * ALLOW_IP_SECTION 可以是以逗号分开的多个IP段
	 */
	public static boolean isContainIP(String ALLOW_IP_SECTION, String REMOTE_IP) {
		boolean result = false, tmpres = false;

		if (ALLOW_IP_SECTION == null || ALLOW_IP_SECTION.equals(""))
			result = true;
		else {
			String[] IP_SECTION = split(ALLOW_IP_SECTION, ",");
			String[] REMOTE_IP_SECTION = split(REMOTE_IP, ".");
			String[] TMP;
			int i = 0, j = 0;
			for (i = 0; i < IP_SECTION.length; i++) {
				if (!IP_SECTION[i].trim().equals("")) {
					// 检查REMOTE_IP是否在其中一个IP段内
					TMP = split(IP_SECTION[i], ".");
					for (j = 0; j < TMP.length; j++) {
						if (REMOTE_IP_SECTION[j].equals(TMP[j])) {
							tmpres = true;
							continue;
						} else {
							tmpres = false;
							break;
						}
					} // end for

					// 如果符合其中一个IP段，则退出循环，返回
					if (tmpres) {
						result = true;
						break;
					} else {
						result = false;
						continue;
					}
				}
			} // end for
		} // end if

		return result;
	}

	/**
	 * @param ip
	 * @return
	 */
	public static String getFullIP(String ip) {
		if (ip == null || ip.length() >= 15)
			return ip;

		StringBuffer buf = new StringBuffer("");
		String[] TEMP = split(ip, ".");
		for (int j = 0; j < TEMP.length; j++) {
			buf.append("." + fillChar(TEMP[j], "0", 3));
		}
		return buf.toString().substring(1);
	}

	/**
	 * @param ip
	 * @return
	 */
	public static String getTrimIP(String ip) {
		if (ip == null || ip.length() <= 7)
			return ip;

		StringBuffer buf = new StringBuffer("");
		String[] TEMP = split(ip, ".");
		for (int j = 0; j < TEMP.length; j++) {
			buf.append("." + toInt(TEMP[j]));
		}
		return buf.toString().substring(1);
	}

	/**
	 * Method getRandomColorHexString. 取得随机颜色的十六进制字串
	 * 
	 * @return String
	 */
	public static String getRandomColorHexString() {
		long rnumber = Math.round(Math.random() * 12800000);
		String temp = Long.toHexString(rnumber);
		while (temp.length() < 6)
			temp = "0" + temp;
		return temp;
	}

	/**
	 * Method leftFillChar. 在字串SOURCE前面填充SIZE个S
	 * 
	 * @param source
	 * @param s
	 * @param size
	 * @return String
	 */
	public static String leftFillChar(String source, String s, int size) {
		if (source == null)
			source = "";
		StringBuffer buf = new StringBuffer(source);
		for (int i = 0; i < size; i++)
			buf.insert(0, s);
		return buf.toString();
	}

	/**
	 * Method rightFillChar. 在字串SOURCE后面填充SIZE个S
	 * 
	 * @param source
	 * @param s
	 * @param size
	 * @return String
	 */
	public static String rightFillChar(String source, String s, int size) {
		if (source == null)
			source = "";
		StringBuffer buf = new StringBuffer(source);
		for (int i = 0; i < size; i++)
			buf.append(s);
		return buf.toString();
	}

	/**
	 * Method fillChar. 在字串SOURCE中，从第START个字符开始填充SIZE个S
	 * 
	 * @param source
	 * @param s
	 * @param size
	 * @param start
	 * @return String
	 */
	public static String fillChar(String source, String s, int size, int start) {
		if (source == null)
			source = "";
		StringBuffer buf = new StringBuffer(source);
		for (int i = 0; i < size; i++)
			buf.insert(start, s);
		return buf.toString();
	}

	/**
	 * Method fillChar. 在字串SOURCE中，从第START个字符开始填充SIZE个S
	 * 
	 * @param source
	 * @param s
	 * @param size
	 * @param start
	 * @return String
	 */
	public static String fillChar(String source, String s, int maxLength) {
		if (source == null)
			source = "";
		StringBuffer buf = new StringBuffer(source);
		while (buf.length() < maxLength) {
			buf.insert(0, s);
		}
		return buf.toString();
	}

	/**
	 * Method containStr.
	 * 
	 * @param source
	 * @param flag
	 * @param isIgnoreCase
	 * @return int
	 */
	public static int containStr(String source, String flag,
			boolean isIgnoreCase) {
		if (source == null || flag == null)
			return 0;
		int sum = 0, index = 0;

		if (isIgnoreCase) {
			source = source.toLowerCase();
			flag = flag.toLowerCase();
		}

		while (source.indexOf(flag) != -1) {
			index = source.indexOf(flag);
			source = source.substring(index + flag.length());
			sum++;
		}
		return sum;
	}

	/**
	 * Method getImageURLs.
	 * 
	 * @param source
	 * @return String[]
	 */
	public static String[] getImageURLs(String source) {
		if (source == null || source.equals(""))
			return null;
		int size = containStr(source, "<img", true);
		if (size < 1)
			return null;
		String[] URL = new String[size];
		int index = 0, i = 0;
		int urlLength = 0, urlFirstIndex = 0, urlLastIndex = 0;
		String temp = "", tempSource = source.toLowerCase();
		while (tempSource.indexOf("<img") != -1 && i < size) {
			index = tempSource.indexOf("src");

			urlFirstIndex = tempSource.indexOf("=", index);
			urlLastIndex = tempSource.indexOf(">", index);

			if ((urlLastIndex - urlFirstIndex) < 4) {
				URL[i] = "";
				i++;
				continue;
			}

			temp = source.substring(urlFirstIndex + 1, urlLastIndex).trim();
			source = source.substring(urlLastIndex + 1);
			tempSource = source.toLowerCase();

			urlLastIndex = temp.indexOf(" ");
			if (urlLastIndex != -1)
				temp = temp.substring(0, urlLastIndex);

			temp = replaceAll(temp, "\"", "");
			temp = replaceAll(temp, "'", "");
			URL[i] = temp;
			i++;
		}

		return URL;
	}

	public static String[] getHrefURLs(String source) {
		if (source == null || source.equals(""))
			return null;
		int size = containStr(source, "<a", true);
		if (size < 1)
			return null;
		String[] URL = new String[size];
		int index = 0, i = 0;
		int urlLength = 0, urlFirstIndex = 0, urlLastIndex = 0;
		String temp = "", tempSource = source.toLowerCase();
		while (tempSource.indexOf("<a") > 0 && i < size) {
			index = tempSource.indexOf("href");

			urlFirstIndex = tempSource.indexOf("=", index);
			urlLastIndex = tempSource.indexOf(">", index);

			if ((urlLastIndex - urlFirstIndex) < 4) {
				URL[i] = "";
				i++;
				continue;
			}

			temp = source.substring(urlFirstIndex + 1, urlLastIndex).trim();
			source = source.substring(urlLastIndex + 1);
			tempSource = source.toLowerCase();

			urlLastIndex = temp.indexOf(" ");
			if (urlLastIndex != -1)
				temp = temp.substring(0, urlLastIndex);

			temp = replaceAll(temp, "\"", "");
			temp = replaceAll(temp, "'", "");
			URL[i] = temp;
			i++;
		}

		return URL;
	}

	/**
	 * Method digitalStringIncrease. 根据步长，将原始数字字串递增
	 * 
	 * @param originStr
	 *            原始字串
	 * @param strLength
	 *            所要获得的字串长度
	 * @param paceLength
	 *            步长
	 * @return String
	 */
	public static String digitalStringIncrease(String originStr, int strLength,
			int paceLength) {
		String tempstr = "";
		String currStr = "";
		int tempint = toInt(originStr) + paceLength;
		currStr = String.valueOf(tempint);
		for (int i = currStr.length(); i < strLength; i++) {
			tempstr = tempstr + "0";
		}
		currStr = tempstr + currStr;
		return currStr;
	}

	public static String digitalStringReduce(String originStr, int strLength,
			int paceLength) {
		String tempstr = "";
		String currStr = "";
		long tempint = toLong(originStr) - paceLength;
		currStr = String.valueOf(tempint);
		for (int i = currStr.length(); i < strLength; i++) {
			tempstr = tempstr + "0";
		}
		currStr = tempstr + currStr;
		return currStr;
	}

	/**
	 * Method digitalStringAutoIncrease. 数字字串自动递增（步长为1）
	 * 
	 * @param originStr
	 *            原始字串
	 * @param strLength
	 *            所要获得的字串长度
	 * @return String
	 */
	public static String digitalStringAutoIncrease(String originStr,
			int strLength) {
		return digitalStringIncrease(originStr, strLength, 1);
	}

	/**
	 * 将SOURCE根据PATTERN分成几段 然后再重组称最大长度在segMaxLength左右的几段
	 * 
	 * @param source
	 *            源字串
	 * @param pattern
	 *            分裂标识符
	 * @param segMaxLength
	 *            每段的最大长度
	 * @return
	 */
	public static String[] split(String source, String pattern, int segMaxLength) {
		if (source == null)
			return null;

		String[] TEMP = split(source, pattern);
		ArrayList list = new ArrayList();
		StringBuffer buffer = new StringBuffer("");
		int i = 0;
		for (i = 0; i < TEMP.length; i++) {
			buffer.append(pattern + TEMP[i]);
			if (buffer.length() >= segMaxLength || i == TEMP.length - 1) {
				list.add(buffer.toString().substring(1));
				buffer = new StringBuffer("");
			}
		}
		TEMP = new String[list.size()];
		for (i = 0; i < list.size(); i++) {
			TEMP[i] = (String) list.get(i);
		}
		list.clear();
		return TEMP;
	}


	/**
	 * @param source
	 * @param start
	 * @param length
	 * @return
	 */
	public static String subStringForGBK(String source, int start, int length) {
		if (source == null)
			return null;
		int len = 0;
		int c;
		length = length > source.length() ? source.length() : length;
		for (int i = 0; i < length; i++) {
			c = source.charAt(i);
			if (c < 256)
				len++;
		}
		length = start + length + len / 2;
		// length = start + length + (int) Math.ceil((double) (len / 2.0));
		length = length > source.length() ? source.length() : length;
		return source.substring(start, length);
	}

	/**
	 * 取得 source 的字串值，若source 为 null, 则返回 rep
	 * 
	 * @param source
	 * @param rep
	 * @return String
	 */
	public static String getString(Object source, String rep) {
		if (source == null)
			return rep;
		return source.toString().trim();
	}

	public static boolean isDigitalString(String source) {
		if (source == null || source.length() == 0)
			return false;

		boolean res = false;
		Pattern p = Pattern.compile("[0-9]{" + source.length() + "}");
		Matcher m = p.matcher(source);
		res = m.matches();
		return res;
	}

	/**
	 * 
	 * @param source
	 * @param start
	 * @param length
	 * @return
	 */
	public static String substring(String source, int start, int length) {
		if (source == null)
			return null;

		if (start > source.length())
			return null;

		if (length > source.length())
			return source.substring(start);
		else
			return source.substring(start, start + length);

	}

	/**
	 * 替换字符串中的参数 如：str={0}是{1}{0} ,args= {"我","中国人"};将替换成:我是中国人我
	 * 
	 * @param str
	 * @param args
	 * @return
	 */
	public static String parameterReplace(String str, String[] args) {
		Pattern p = Pattern.compile("\\{(\\d)\\}");
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, args[Integer.parseInt(m.group(1))]);
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	/**
	* 按字节长度截取字符串
	* @param str 将要截取的字符串参数
	* @param toCount 截取的字节长度
	* @param more 字符串末尾补上的字符串
	* @return 返回截取后的字符串
	*/
	public static String strCut(String str, int toCount, String more){
		if(Utils.isEmpty(str) || str.getBytes().length <= toCount) {
			return str;
		}
		if(more == null) {
			more = StringUtils.EMPTY;
		}
		toCount = toCount - more.getBytes().length;
//		if(str.getBytes().length == str.length()) {
//			return str.substring(0,toCount) + more;
//		}
		int reInt = 0;
		StringBuilder reStr = new StringBuilder(toCount);
		char[] tempChar = str.toCharArray();
		for (int k = 0; (k < tempChar.length && toCount > reInt); k++){
			String s1 = str.valueOf(tempChar[k]);
			byte[] b = s1.getBytes();
			reInt += b.length;
			reStr.append(tempChar[k]);
		}
		reStr.append(more);
		return reStr.toString();
	}
	
	public static String  escape (String src)
	 {
	  if(src==null){
		  return "";
	  }
	  int i;
	  char j;
	  StringBuffer tmp = new StringBuffer();
	  tmp.ensureCapacity(src.length()*6);

	  for (i=0;i<src.length() ;i++ )
	  {

	   j = src.charAt(i);

	   if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
	    tmp.append(j);
	   else
	    if (j<256)
	    {
	    tmp.append( "%" );
	    if (j<16)
	     tmp.append( "0" );
	    tmp.append( Integer.toString(j,16) );
	    }
	    else
	    {
	    tmp.append( "%u" );
	    tmp.append( Integer.toString(j,16) );
	    }
	  }
	  return tmp.toString();
	 }

	 public static String  unescape (String src)
	 {
	 if(src==null){
		  return "";
	  }
	  StringBuffer tmp = new StringBuffer();
	  tmp.ensureCapacity(src.length());
	  int  lastPos=0,pos=0;
	  char ch;
	  while (lastPos<src.length())
	  {
	   pos = src.indexOf("%",lastPos);
	   if (pos == lastPos)
	    {
	    if (src.charAt(pos+1)=='u')
	     {
	     ch = (char)Integer.parseInt(src.substring(pos+2,pos+6),16);
	     tmp.append(ch);
	     lastPos = pos+6;
	     }
	    else
	     {
	     ch = (char)Integer.parseInt(src.substring(pos+1,pos+3),16);
	     tmp.append(ch);
	     lastPos = pos+3;
	     }
	    }
	   else
	    {
	    if (pos == -1)
	     {
	     tmp.append(src.substring(lastPos));
	     lastPos=src.length();
	     }
	    else
	     {
	     tmp.append(src.substring(lastPos,pos));
	     lastPos=pos;
	     }
	    }
	  }
	  return tmp.toString();
	 }

}

/**
 * BASE64编码解码实现类
 * 
 * @author 
 */
class Base64Code {

	protected static byte[] _encode_map = { (byte) 'A', (byte) 'B', (byte) 'C',
			(byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H',
			(byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M',
			(byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R',
			(byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W',
			(byte) 'X', (byte) 'Y', (byte) 'Z',

			(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e',
			(byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j',
			(byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o',
			(byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't',
			(byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y',
			(byte) 'z',

			(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',

			(byte) '+', (byte) '/' };

	protected static byte _decode_map[] = new byte[128];
	static {
		/*
		 * Fill in the decode map
		 */
		for (int i = 0; i < _encode_map.length; i++) {
			_decode_map[_encode_map[i]] = (byte) i;
		}
	}

	/**
	 * This class isn't meant to be instantiated.
	 */
	private Base64Code() {

	}

	/**
	 * This method encodes the given byte[] using the Base64 encoding
	 * 
	 * 
	 * @param data
	 *            the data to encode.
	 * @return the Base64 encoded <var>data</var>
	 */
	public final static byte[] encode(byte[] data) {

		if (data == null) {
			return (null);
		}

		/*
		 * Craete a buffer to hold the results
		 */
		byte dest[] = new byte[((data.length + 2) / 3) * 4];

		/*
		 * 3-byte to 4-byte conversion and 0-63 to ascii printable conversion
		 */
		int i, j;
		int data_len = data.length - 2;
		for (i = 0, j = 0; i < data_len; i += 3) {

			dest[j++] = _encode_map[(data[i] >>> 2) & 077];
			dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017 | (data[i] << 4)
					& 077];
			dest[j++] = _encode_map[(data[i + 2] >>> 6) & 003
					| (data[i + 1] << 2) & 077];
			dest[j++] = _encode_map[data[i + 2] & 077];
		}

		if (i < data.length) {
			dest[j++] = _encode_map[(data[i] >>> 2) & 077];

			if (i < data.length - 1) {
				dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017
						| (data[i] << 4) & 077];
				dest[j++] = _encode_map[(data[i + 1] << 2) & 077];
			} else {
				dest[j++] = _encode_map[(data[i] << 4) & 077];
			}
		}

		/*
		 * Pad with "=" characters
		 */
		for (; j < dest.length; j++) {
			dest[j] = (byte) '=';
		}

		return (dest);
	}

	/**
	 * This method decodes the given byte[] using the Base64 encoding
	 * 
	 * 
	 * @param data
	 *            the Base64 encoded data to decode.
	 * @return the decoded <var>data</var>.
	 */
	public final static byte[] decode(byte[] data) {

		if (data == null)
			return (null);

		/*
		 * Remove the padding on the end
		 */
		int ending = data.length;
		if (ending < 1) {
			return (null);
		}
		while (data[ending - 1] == '=')
			ending--;

		/*
		 * Create a buffer to hold the results
		 */
		byte dest[] = new byte[ending - data.length / 4];

		/*
		 * ASCII printable to 0-63 conversion
		 */
		for (int i = 0; i < data.length; i++) {
			data[i] = _decode_map[data[i]];
		}

		/*
		 * 4-byte to 3-byte conversion
		 */
		int i, j;
		int dest_len = dest.length - 2;
		for (i = 0, j = 0; j < dest_len; i += 4, j += 3) {
			dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
			dest[j + 1] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
			dest[j + 2] = (byte) (((data[i + 2] << 6) & 255) | (data[i + 3] & 077));
		}

		if (j < dest.length) {
			dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
		}

		j++;
		if (j < dest.length) {
			dest[j] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
		}

		return (dest);
	}
	
}