package com.wpc.utils.string;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class StringUtil {
	public static String ENCODING_DEFAULT = "UTF-8";

	public static String GET_ENCODING_DEFAULT = "UTF-8";

	public static String FILE_WRITING_ENCODING = "GBK";

	public static boolean isEmpty(String str) {
		return (str == null) || (str.trim().length() == 0);
	}
	
	public static boolean isNotEmpty(String str) {
		return (!(isEmpty(str)));
	}

	public static String showObjNull(Object p_sValue) {
		return showObjNull(p_sValue, "");
	}

	public static String showObjNull(Object _sValue, String _sReplaceIfNull) {
		if (_sValue == null)
			return _sReplaceIfNull;
		return _sValue.toString();
	}

	public static String showNull(String value) {
		return showNull(value, "");
	}

	public static String showNull(String value, String _sReplaceIfNull) {
		return (value == null) ? _sReplaceIfNull : value;
	}

	public static String expandStr(String _string, int _length, char _chrFill, boolean _bFillOnLeft) {
		int nLen = _string.length();
		if (_length <= nLen) {
			return _string;
		}

		String sRet = _string;
		for (int i = 0; i < _length - nLen; ++i) {
			sRet = sRet + _chrFill;
		}
		return sRet;
	}

	public static String setStrEndWith(String _string, char _chrEnd) {
		if (_string == null)
			return null;
		if (_string.charAt(_string.length() - 1) != _chrEnd) {
			return _string + _chrEnd;
		}
		return _string;
	}

	public static String makeBlanks(int _length) {
		if (_length < 1)
			return "";
		StringBuffer buffer = new StringBuffer(_length);
		for (int i = 0; i < _length; ++i) {
			buffer.append(' ');
		}
		return buffer.toString();
	}

	public static String replaceStr(String _strSrc, String _strOld, String _strNew) {
		if ((_strSrc == null) || (_strNew == null) || (_strOld == null)) {
			return _strSrc;
		}

		char[] srcBuff = _strSrc.toCharArray();
		int nSrcLen = srcBuff.length;
		if (nSrcLen == 0) {
			return "";
		}

		char[] oldStrBuff = _strOld.toCharArray();
		int nOldStrLen = oldStrBuff.length;
		if ((nOldStrLen == 0) || (nOldStrLen > nSrcLen)) {
			return _strSrc;
		}
		StringBuffer retBuff = new StringBuffer(nSrcLen * (1 + _strNew.length() / nOldStrLen));
		boolean bIsFound = false;

		int i = 0;
		while (i < nSrcLen) {
			bIsFound = false;
			if (srcBuff[i] == oldStrBuff[0]) {
				int j;
				for (j = 1; j < nOldStrLen; j++) {
					if (i + j >= nSrcLen)
						break;
					if (srcBuff[(i + j)] != oldStrBuff[j])
						break;
				}
				bIsFound = j == nOldStrLen;
			}

			if (bIsFound) {
				retBuff.append(_strNew);
				i += nOldStrLen;
			} else {
				int nSkipTo;
				if (i + nOldStrLen >= nSrcLen)
					nSkipTo = nSrcLen - 1;
				else {
					nSkipTo = i;
				}
				for (; i <= nSkipTo; ++i) {
					retBuff.append(srcBuff[i]);
				}
			}
		}
		srcBuff = (char[]) null;
		oldStrBuff = (char[]) null;
		return retBuff.toString();
	}

	public static String replaceStr(StringBuffer _strSrc, String _strOld, String _strNew) {
		if (_strSrc == null) {
			return null;
		}

		int nSrcLen = _strSrc.length();
		if (nSrcLen == 0) {
			return "";
		}

		char[] oldStrBuff = _strOld.toCharArray();
		int nOldStrLen = oldStrBuff.length;
		if ((nOldStrLen == 0) || (nOldStrLen > nSrcLen)) {
			return _strSrc.toString();
		}
		StringBuffer retBuff = new StringBuffer(nSrcLen
				* (1 + _strNew.length() / nOldStrLen));

		boolean bIsFound = false;

		int i = 0;
		while (i < nSrcLen) {
			bIsFound = false;

			if (_strSrc.charAt(i) == oldStrBuff[0]) {
				int j;
				for (j = 1; j < nOldStrLen; j++) {
					if (i + j >= nSrcLen)
						break;
					if (_strSrc.charAt(i + j) != oldStrBuff[j])
						break;
				}
				bIsFound = j == nOldStrLen;
			}

			if (bIsFound) {
				retBuff.append(_strNew);
				i += nOldStrLen;
			} else {
				int nSkipTo;
				if (i + nOldStrLen >= nSrcLen)
					nSkipTo = nSrcLen - 1;
				else {
					nSkipTo = i;
				}
				for (; i <= nSkipTo; ++i) {
					retBuff.append(_strSrc.charAt(i));
				}
			}
		}
		oldStrBuff = (char[]) null;
		return retBuff.toString();
	}

	public static String getStr(String _strSrc) {
		return getStr(_strSrc, ENCODING_DEFAULT);
	}

	public static String getStr(String _strSrc, boolean _bPostMethod) {
		return getStr(_strSrc, (_bPostMethod) ? ENCODING_DEFAULT : GET_ENCODING_DEFAULT);
	}

	public static String getStr(String _strSrc, String _encoding) {
		if ((_encoding == null) || (_encoding.length() == 0))
			return _strSrc;
		try {
			byte[] byteStr = new byte[_strSrc.length()];
			char[] charStr = _strSrc.toCharArray();
			for (int i = byteStr.length - 1; i >= 0; --i) {
				byteStr[i] = (byte) charStr[i];
			}

			return new String(byteStr, _encoding);
		} catch (Exception ex) {
		}

		return _strSrc;
	}

	public static String toISO_8859(String _strSrc) {
		if (_strSrc == null)
			return null;
		try {
			return new String(_strSrc.getBytes(), "ISO-8859-1");
		} catch (Exception ex) {
		}
		return _strSrc;
	}

	/** @deprecated */
	public static String toUnicode(String _strSrc) {
		return toISO_8859(_strSrc);
	}

	public static byte[] getUTF8Bytes(String _string) {
		char[] c = _string.toCharArray();
		int len = c.length;

		int count = 0;
		for (int i = 0; i < len; ++i) {
			int ch = c[i];
			if (ch <= 127)
				++count;
			else if (ch <= 2047)
				count += 2;
			else {
				count += 3;
			}

		}

		byte[] b = new byte[count];
		int off = 0;
		for (int i = 0; i < len; ++i) {
			int ch = c[i];
			if (ch <= 127) {
				b[(off++)] = (byte) ch;
			} else if (ch <= 2047) {
				b[(off++)] = (byte) (ch >> 6 | 0xC0);
				b[(off++)] = (byte) (ch & 0x3F | 0x80);
			} else {
				b[(off++)] = (byte) (ch >> 12 | 0xE0);
				b[(off++)] = (byte) (ch >> 6 & 0x3F | 0x80);
				b[(off++)] = (byte) (ch & 0x3F | 0x80);
			}
		}
		return b;
	}

	public static String getUTF8String(byte[] b) {
		return getUTF8String(b, 0, b.length);
	}

	public static String getUTF8String(byte[] b, int off, int len) {
		int count = 0;
		int max = off + len;
		int i = off;
		while (i < max) {
			int c = b[(i++)] & 0xFF;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				++count;
				break;
			case 12:
			case 13:
				if ((b[(i++)] & 0xC0) != 128) {
					throw new IllegalArgumentException();
				}
				++count;
				break;
			case 14:
				if (((b[(i++)] & 0xC0) != 128) || ((b[(i++)] & 0xC0) != 128)) {
					throw new IllegalArgumentException();
				}
				++count;
				break;
			case 8:
			case 9:
			case 10:
			case 11:
			default:
				throw new IllegalArgumentException();
			}
		}
		if (i != max) {
			throw new IllegalArgumentException();
		}

		char[] cs = new char[count];
		i = 0;
		while (off < max) {
			int c = b[(off++)] & 0xFF;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				cs[(i++)] = (char) c;
				break;
			case 12:
			case 13:
				cs[(i++)] = (char) ((c & 0x1F) << 6 | b[(off++)] & 0x3F);
				break;
			case 14:
				int t = (b[(off++)] & 0x3F) << 6;
				cs[(i++)] = (char) ((c & 0xF) << 12 | t | b[(off++)] & 0x3F);
				break;
			case 8:
			case 9:
			case 10:
			case 11:
			default:
				throw new IllegalArgumentException();
			}
		}
		return new String(cs, 0, count);
	}

	public static String byteToHexString(byte[] _bytes) {
		return byteToHexString(_bytes, ',');
	}

	public static String byteToHexString(byte[] _bytes, char _delim) {
		String sRet = "";
		for (int i = 0; i < _bytes.length; ++i) {
			if (i > 0) {
				sRet = sRet + _delim;
			}
			sRet = sRet + Integer.toHexString(_bytes[i]);
		}
		return sRet;
	}

	public static String byteToString(byte[] _bytes, char _delim, int _radix) {
		String sRet = "";
		for (int i = 0; i < _bytes.length; ++i) {
			if (i > 0) {
				sRet = sRet + _delim;
			}
			sRet = sRet + Integer.toString(_bytes[i], _radix);
		}
		return sRet;
	}
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	public static String toHtml(String content) {
		return toHtml(content, true);
	}
	
	/**
	 * 
	 * @param content
	 * @param changeBlank 是否替换空格符
	 * @return
	 */
	public static String toHtml(String content, boolean changeBlank) {
		if (content == null) {
			return "";
		}
		char[] srcBuff = content.toCharArray();
		int nSrcLen = srcBuff.length;
		StringBuffer retBuff = new StringBuffer(nSrcLen * 2);
		for (int i = 0; i < nSrcLen; ++i) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case ' ': //空格符
				retBuff.append((changeBlank) ? "&nbsp;" : " ");
				break;
			case '<':
				retBuff.append("&lt;");
				break;
			case '>':
				retBuff.append("&gt;");
				break;
			case '\n': //换行符
				retBuff.append("<br>");
				break;
			case '\r': //回车符
				retBuff.append("<br/>");
			    if ((i + 1 < srcBuff.length) && (srcBuff[(i + 1)] == '\n')) {
			    	++i;
			    }
			    break;
			case '"': //引号符
				retBuff.append("&quot;");
				break;
			case '\'':
				retBuff.append("&#39;");
			    break;
			case '&':
				boolean bUnicode = false;
				for (int j = i + 1; (j < nSrcLen) && (!bUnicode); ++j) {
					cTemp = srcBuff[j];
					if ((cTemp == '#') || (cTemp == ';')) {
						retBuff.append("&");
						bUnicode = true;
					}
				}
				if (!bUnicode)
					retBuff.append("&amp;");
				break;
			case '\t': //制表符
				retBuff.append((changeBlank) ? "&nbsp;&nbsp;&nbsp;&nbsp;" : "    ");
				break;
			default:
				retBuff.append(cTemp);
			}
		}
		return retBuff.toString();
	}
	
	public static String transJsDisplay(String _sContent) {
		if (_sContent == null) {
			return "";
		}
		char[] srcBuff = _sContent.toCharArray();
		int nSrcLen = srcBuff.length;
		StringBuffer retBuff = new StringBuffer((int) (nSrcLen * 1.5D));
		for (int i = 0; i < nSrcLen; ++i) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '<':
				retBuff.append("&lt;");
				break;
			case '>':
				retBuff.append("&gt;");
				break;
			case '"':
				retBuff.append("&quot;");
				break;
			default:
				retBuff.append(cTemp);
			}
		}
		return retBuff.toString();
	}

	public static String transDisplayMark(String _strSrc, char p_chrMark) {
		if (_strSrc == null) {
			return "";
		}

		char[] buff = new char[_strSrc.length()];
		for (int i = 0; i < buff.length; ++i) {
			buff[i] = p_chrMark;
		}
		return new String(buff);
	}
	
	/**
	 * 过滤SQL
	 * @param content
	 * @return
	 */
	public static String filterForSQL(String content) {
		if (content == null) {
			return "";
		}
		int nLen = content.length();
		if (nLen == 0) {
			return "";
		}
		char[] srcBuff = content.toCharArray();
		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.5D));
		for (int i = 0; i < nLen; ++i) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '\'':
				retBuff.append("''");
				break;
			case ';':
				boolean bSkip = false;
				for (int j = i + 1; (j < nLen) && (!bSkip); ++j) {
					char cTemp2 = srcBuff[j];
					if (cTemp2 == ' ')
						continue;
					if (cTemp2 == '&')
						retBuff.append(';');
					bSkip = true;
				}
				if (!bSkip)
					retBuff.append(';');
				break;
			default:
				retBuff.append(cTemp);
			}

		}

		return retBuff.toString();
	}
	
	/**
	 * 过滤XML
	 * @param content
	 * @return
	 */
	public static String filterForXML(String content) {
		if (content == null) {
			return "";
		}
		char[] c = content.toCharArray();
		int nLen = c.length;
		if (nLen == 0) {
			return "";
		}
		StringBuffer buff = new StringBuffer((int) (nLen * 1.8D));
		for (int i = 0; i < nLen; ++i) {
			switch (c[i]) {
			case '&':
				buff.append("&amp;");
				break;
			case '<':
				buff.append("&lt;");
				break;
			case '>':
				buff.append("&gt;");
				break;
			case '"':
				buff.append("&quot;");
				break;
			case '\'':
				buff.append("&apos;");
				break;
			default:
				buff.append(c[i]);
			}
		}
		return buff.toString();
	}

	public static String filterForHTMLValue(String _sContent) {
		if (_sContent == null) {
			return "";
		}
		char[] srcBuff = _sContent.toCharArray();
		int nLen = srcBuff.length;
		if (nLen == 0) {
			return "";
		}
		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8D));
		for (int i = 0; i < nLen; ++i) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '&':
				if (i + 1 < nLen) {
					cTemp = srcBuff[(i + 1)];
					if (cTemp == '#')
						retBuff.append("&");
					else
						retBuff.append("&amp;");
				} else {
					retBuff.append("&amp;");
				}
				break;
			case '<':
				retBuff.append("&lt;");
				break;
			case '>':
				retBuff.append("&gt;");
				break;
			case '"':
				retBuff.append("&quot;");
				break;
			default:
				retBuff.append(cTemp);
			}
		}

		return retBuff.toString();
	}

	public static String filterForUrl(String _sContent) {
		if (_sContent == null) {
			return "";
		}
		char[] srcBuff = _sContent.toCharArray();
		int nLen = srcBuff.length;
		if (nLen == 0) {
			return "";
		}
		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8D));

		for (int i = 0; i < nLen; ++i) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '%':
				retBuff.append("%25");
				break;
			case '?':
				retBuff.append("%3F");
				break;
			case '#':
				retBuff.append("%23");
				break;
			case '&':
				retBuff.append("%26");
				break;
			case ' ':
				retBuff.append("%20");
				break;
			default:
				retBuff.append(cTemp);
			}
		}

		return retBuff.toString();
	}

	public static String filterForJs(String _sContent) {
		if (_sContent == null) {
			return "";
		}
		char[] srcBuff = _sContent.toCharArray();
		int nLen = srcBuff.length;
		if (nLen == 0) {
			return "";
		}
		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8D));

		for (int i = 0; i < nLen; ++i) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '"':
				retBuff.append("\\\"");
				break;
			case '\\':
				retBuff.append("\\\\");
				break;
			case '\n':
				retBuff.append("\\n");
				break;
			case '\r':
				retBuff.append("\\r");
				break;
			case '\f':
				retBuff.append("\\f");
				break;
			case '\t':
				retBuff.append("\\t");
				break;
			case '/':
				retBuff.append("\\/");
				break;
			default:
				retBuff.append(cTemp);
			}
		}

		return retBuff.toString();
	}

	public static String numberToStr(int _nValue) {
		return numberToStr(_nValue, 0);
	}

	public static String numberToStr(int _nValue, int _length) {
		return numberToStr(_nValue, _length, '0');
	}

	public static String numberToStr(int _nValue, int _length, char _chrFill) {
		String sValue = String.valueOf(_nValue);
		return expandStr(sValue, _length, _chrFill, true);
	}

	public static String numberToStr(long _lValue) {
		return numberToStr(_lValue, 0);
	}

	public static String numberToStr(long _lValue, int _length) {
		return numberToStr(_lValue, _length, '0');
	}

	public static String numberToStr(long _lValue, int _length, char _chrFill) {
		String sValue = String.valueOf(_lValue);
		return expandStr(sValue, _length, _chrFill, true);
	}

	public static String circleStr(String _strSrc) {
		if (_strSrc == null) {
			return null;
		}
		String sResult = "";
		int nLength = _strSrc.length();
		for (int i = nLength - 1; i >= 0; --i) {
			sResult = sResult + _strSrc.charAt(i);
		}
		return sResult;
	}

	public static final boolean isChineseChar(int c) {
		return c > 127;
	}

	public static final int getCharViewWidth(int c) {
		return (isChineseChar(c)) ? 2 : 1;
	}

	public static final int getStringViewWidth(String s) {
		if ((s == null) || (s.length() == 0)) {
			return 0;
		}

		int iWidth = 0;
		int iLength = s.length();

		for (int i = 0; i < iLength; ++i) {
			iWidth += getCharViewWidth(s.charAt(i));
		}

		return iWidth;
	}

	public static String truncateStr(String _string, int _maxLength) {
		return truncateStr(_string, _maxLength, "..");
	}

	public static String truncateStr(String _string, int _maxLength, String _sExt) {
		if (_string == null) {
			return null;
		}

		if (_sExt == null) {
			_sExt = "..";
		}

		int nSrcLen = getStringViewWidth(_string);
		if (nSrcLen <= _maxLength) {
			return _string;
		}

		int nExtLen = getStringViewWidth(_sExt);
		if (nExtLen >= _maxLength) {
			return _string;
		}

		int iLength = _string.length();
		int iRemain = _maxLength - nExtLen;
		StringBuffer sb = new StringBuffer(_maxLength + 2);

		for (int i = 0; i < iLength; ++i) {
			char aChar = _string.charAt(i);
			int iNeed = getCharViewWidth(aChar);
			if (iNeed > iRemain) {
				sb.append(_sExt);
				break;
			}
			sb.append(aChar);
			iRemain -= iNeed;
		}

		return sb.toString();
	}

	public static String filterForJDOM(String _string) {
		if (_string == null) {
			return null;
		}
		char[] srcBuff = _string.toCharArray();
		int nLen = srcBuff.length;

		StringBuffer dstBuff = new StringBuffer(nLen);

		for (int i = 0; i < nLen; ++i) {
			char aChar = srcBuff[i];
			if (!isValidCharOfXML(aChar)) {
				continue;
			}
			dstBuff.append(aChar);
		}
		return dstBuff.toString();
	}

	public static boolean isValidCharOfXML(char _char) {
		return (_char == '\t') || (_char == '\n') || (_char == '\r')
				|| ((' ' <= _char) && (_char <= 55295))
				|| ((57344 <= _char) && (_char <= 65533))
				|| ((65536 <= _char) && (_char <= 1114111));
	}

	public static int getBytesLength(String _string) {
		if (_string == null) {
			return 0;
		}
		char[] srcBuff = _string.toCharArray();

		int nGet = 0;
		for (int i = 0; i < srcBuff.length; ++i) {
			char aChar = srcBuff[i];
			nGet += ((aChar <= '') ? 1 : 2);
		}
		return nGet;
	}

	/** @deprecated */
	public static String cutStr(String _string, int _length) {
		return truncateStr(_string, _length);
	}
	
	/**
	 * 字符串分隔解析
	 * 
	 * @param str
	 *            字符串
	 * @param delim
	 *            分隔符
	 * @return
	 */
	public static String[] split(String str, String delim) {
		if ((str == null) || (delim == null)) {
			return new String[0];
		}

		StringTokenizer stTemp = new StringTokenizer(str, delim);
		int nSize = stTemp.countTokens();
		if (nSize == 0) {
			return new String[0];
		}

		String[] s = new String[nSize];
		int i = 0;
		while (stTemp.hasMoreElements()) {
			s[i] = stTemp.nextToken();
			++i;
		}
		return s;
	}

	public static int countTokens(String str, String delim) {
		StringTokenizer stTemp = new StringTokenizer(str, delim);
		return stTemp.countTokens();
	}

	public static int[] splitToInt(String _str, String _sDelim) {
		if (isEmpty(_str)) {
			return new int[0];
		}

		if (isEmpty(_sDelim)) {
			_sDelim = ",";
		}

		StringTokenizer stTemp = new StringTokenizer(_str, _sDelim);
		int[] arInt = new int[stTemp.countTokens()];
		int nIndex = 0;

		while (stTemp.hasMoreElements()) {
			String sValue = (String) stTemp.nextElement();
			arInt[nIndex] = Integer.parseInt(sValue.trim());
			++nIndex;
		}
		return arInt;
	}

	public static final boolean isContainChineseChar(String _str) {
		if (_str == null) {
			return false;
		}

		return _str.getBytes().length != _str.length();
	}

	public static String join(ArrayList _arColl, String _sSeparator) {
		if (_arColl == null) {
			return null;
		}
		return join(_arColl.toArray(), _sSeparator);
	}

	public static String join(Object[] _arColl, String _sSeparator) {
		if ((_arColl == null) || (_arColl.length == 0) || (_sSeparator == null)) {
			return null;
		}
		if (_arColl.length == 1) {
			return _arColl[0].toString();
		}

		StringBuffer result = new StringBuffer(_arColl[0].toString());
		for (int i = 1; i < _arColl.length; ++i) {
			result.append(_sSeparator);
			result.append(_arColl[i].toString());
		}

		return result.toString();
	}
	
	public static final String escapeForCDATA(String content) {
		if (content == null)
			return "";

		if (content.length() < 20) {
			return escapeTextNode(content);
		}
		char[] c = content.toCharArray();
		int nLen = c.length;
		if (nLen == 0) {
			return "";
		}
		StringBuffer buff = new StringBuffer((int) (nLen * 1.8D));
		buff.append("<![CDATA[");
		for (int i = 0; i < nLen; ++i) {
			if ((((c[i] < 0) || (c[i] > '\b'))) && (c[i] != '\11') && (c[i] != '\f') && ((('\14' > c[i]) || (c[i] >= ' ')))) {
				if ((c[i] == ']') && (i + 2 < c.length) && (c[(i + 1)] == ']') && (c[(i + 2)] == '>')) {
					buff.append("]]&gt;");
					i += 2;
				} else {
					buff.append(c[i]);
				}
			}
		}
		buff.append("]]>");
		return buff.toString();
	}
	
	public static final String escapeTextNode(String content) {
		if (content == null)
			return "";
		char[] c = content.toCharArray();
		int nLen = c.length;
		if (nLen == 0) {
			return "";
		}
		StringBuffer buff = new StringBuffer((int) (nLen * 1.8D));
		for (int i = 0; i < nLen; ++i) {
			switch (c[i]) {
			case '&':
				buff.append("&amp;");
				break;
			case '<':
				buff.append("&lt;");
				break;
			case '>':
				buff.append("&gt;");
				break;
			case '\t':
			case '\n':
			case '\r':
				buff.append(c[i]);
				break;
			default:
				if (c[i] < ' ')
					buff.append("&#").append(c[i]).append(';');
				else
					buff.append(c[i]);
			}
		}
		return buff.toString();
	}

	public static String replaceEx(String str, String subStr, String reStr) {
		if (str == null)
			return null;

		if ((subStr == null) || (subStr.equals(""))
				|| (subStr.length() > str.length()) || (reStr == null))
			return str;

		StringBuffer sb = new StringBuffer();
		int lastIndex = 0;
		while (true) {
			int index = str.indexOf(subStr, lastIndex);
			if (index < 0)
				break;

			sb.append(str.substring(lastIndex, index));
			sb.append(reStr);

			lastIndex = index + subStr.length();
		}
		sb.append(str.substring(lastIndex));
		return sb.toString();
	}

	public static String javaEncode(String txt) {
		if ((txt == null) || (txt.length() == 0))
			return txt;
		txt = replaceEx(txt, "\\", "\\\\");
		txt = replaceEx(txt, "\r\n", "\n");
		txt = replaceEx(txt, "\r", "\\r");
		txt = replaceEx(txt, "\n", "\\n");
		txt = replaceEx(txt, "\"", "\\\"");
		txt = replaceEx(txt, "'", "\\'");
		return txt;
	}

	public static String javaDecode(String txt) {
		if ((txt == null) || (txt.length() == 0))
			return txt;

		txt = replaceEx(txt, "\\\\", "\\");
		txt = replaceEx(txt, "\\n", "\n");
		txt = replaceEx(txt, "\\r", "\r");
		txt = replaceEx(txt, "\\\"", "\"");
		txt = replaceEx(txt, "\\'", "'");
		return txt;
	}
}
