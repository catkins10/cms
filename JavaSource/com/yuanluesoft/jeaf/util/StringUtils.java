/*
 * Created on 2006-9-13
 *
 */
package com.yuanluesoft.jeaf.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.config.ActionConfig;

import com.yuanluesoft.jeaf.base.model.Attribute;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.callback.FillParametersCallback;

/**
 * 
 * @author linchuan
 *
 */
public class StringUtils {
	private final static String[][] ESCAPE_SEQUENCES = {
		{" ", "&nbsp;", "&#160;"}, //不断行的空白格
		{" ", "&ensp;", "&#8194;"}, //半方大的空白
		{" ", "&emsp;", "&#8195;"}, //全方大的空白
		{"<", "&lt;", "&#60;"}, //小于
		{">", "&gt;", "&#62;"}, //大于
		{"&", "&amp;", "&#38;"},
		{"\"", "&quot;", "&#34;"}, //双引号
		{"©", "&copy;", "&#169;"}, //版权
		{"®", "&reg;", "&#174;"}, //已注册商标
		//{"™", "™", "&#8482;"}, //商标（美国）
		{"×", "&times;", "&#215;"}, //乘号
		{"÷", "&divide;", "&#247;"}, //除号
		{"“", "&ldquo;", "&#8220;"},
		{"”", "&rdquo;", "&#8221;"},
		{"·", "&middot;", "&#183;"},
		{"‹", "&lsaquo;", "&#8249;"},
		{"¢", "&cent;", "&#162;"},
		{"£", "&pound;", "&#163;"},
		{"¥", "&yen;", "&#165;"},
		{"§", "&sect;", "&#167;"},
		{"€", "&euro;", "&#8364;"},
		{"«", "&laquo;", "&#171;"},
		{"»", "&raquo;", "&#187;"},
		{"±", "&plusmn;", "&#177;"},
		{"°", "&deg;", "&#176;"},
		{"′", "&prime;", "&#8242;"},
		{"…", "&hellip;", "&#8230;"},
		{"ª", "&ordf;", "&#170;"},
		{"¯", "&macr;", "&#175;"},
		{"´", "&acute;", "&#180;"},
		{"¹", "&sup1;", "&#185;"},
		{"¾", "&frac34;", "&#190;"},
		{"Ã", "&Atilde;", "&#195;"},
		{"È", "&Egrave;", "&#200;"},
		{"Í", "&Iacute;", "&#205;"},
		{"Ò", "&Ograve;", "&#210;"},
		{"Ü", "&Uuml;", "&#220;"},
		{"á", "&aacute;", "&#225;"},
		{"æ", "&aelig;", "&#230;"},
		{"ë", "&euml;", "&#235;"},
		{"ð", "&eth;", "&#240;"},
		{"õ", "&otilde;", "&#245;"},
		{"ú", "&uacute;", "&#250;"},
		{"ÿ", "&yuml;", "&#255;"},
		{"¡", "&iexcl;", "&#161;"},
		{"¦", "&brvbar;", "&#166;"},
		{"µ", "&micro;", "&#181;"},
		{"º", "&ordm;", "&#186;"},
		{"¿", "&iquest;", "&#191;"},
		{"Ä", "&Auml;", "&#196;"},
		{"É", "&Eacute;", "&#201;"},
		{"Î", "&Icirc;", "&#206;"},
		{"Ó", "&Oacute;", "&#211;"},
		{"Ø", "&Oslash;", "&#216;"},
		{"Ý", "&Yacute;", "&#221;"},
		{"â", "&acirc;", "&#226;"},
		{"ç", "&ccedil;", "&#231;"},
		{"ì", "&igrave;", "&#236;"},
		{"ñ", "&ntilde;", "&#241;"},
		{"ö", "&ouml;", "&#246;"},
		{"û", "&ucirc;", "&#251;"},
		{"¬", "&not;", "&#172;"},
		{"¶", "&para;", "&#182;"},
		{"À", "&Agrave;", "&#192;"},
		{"Å", "&Aring;", "&#197;"},
		{"Ê", "&Ecirc;", "&#202;"},
		{"Ï", "&Iuml;", "&#207;"},
		{"Ô", "&Ocirc;", "&#212;"},
		{"Ù", "&Ugrave;", "&#217;"},
		{"Þ", "&THORN;", "&#222;"},
		{"ã", "&atilde;", "&#227;"},
		{"è", "&egrave;", "&#232;"},
		{"í", "&iacute;", "&#237;"},
		{"ò", "&ograve;", "&#242;"},
		{"ü", "&uuml;", "&#252;"},
		{"¨", "&uml;", "&#168;"},
		{"­", "&shy;", "&#173;"},
		{"²", "&sup2;", "&#178;"},
		{"¼", "&frac14;", "&#188;"},
		{"Á", "&Aacute;", "&#193;"},
		{"Æ", "&AElig;", "&#198;"},
		{"Ë", "&Euml;", "&#203;"},
		{"Ð", "&ETH;", "&#208;"},
		{"Õ", "&Otilde;", "&#213;"},
		{"Ú", "&Uacute;", "&#218;"},
		{"ß", "&szlig;", "&#223;"},
		{"ä", "&auml;", "&#228;"},
		{"é", "&eacute;", "&#233;"},
		{"î", "&icirc;", "&#238;"},
		{"ó", "&oacute;", "&#243;"},
		{"ø", "&oslash;", "&#248;"},
		{"ý", "&yacute;", "&#253;"},
		{"¤", "&curren;", "&#164;"},
		{"³", "&sup3;", "&#179;"},
		{"¸", "&cedil;", "&#184;"},
		{"½", "&frac12;", "&#189;"},
		{"Â", "&Acirc;", "&#194;"},
		{"Ç", "&Ccedil;", "&#199;"},
		{"Ì", "&Igrave;", "&#204;"},
		{"Ñ", "&Ntilde;", "&#209;"},
		{"Ö", "&Ouml;", "&#214;"},
		{"Û", "&Ucirc;", "&#219;"},
		{"à", "&agrave;", "&#224;"},
		{"å", "&aring;", "&#229;"},
		{"ê", "&ecirc;", "&#234;"},
		{"ï", "&iuml;", "&#239;"},
		{"ô", "&ocirc;", "&#244;"},
		{"ù", "&ugrave;", "&#249;"},
		{"þ", "&thorn;", "&#254;"},
		{"ƒ", "&fnof;", "&#402;"},
		{"Ε", "&Epsilon;", "&#917;"},
		{"Κ", "&Kappa;", "&#922;"},
		{"Ο", "&Omicron;", "&#927;"},
		{"Υ", "&Upsilon;", "&#933;"},
		{"α", "&alpha;", "&#945;"},
		{"ζ", "&zeta;", "&#950;"},
		{"λ", "&lambda;", "&#955;"},
		{"π", "&pi;", "&#960;"},
		{"υ", "&upsilon;", "&#965;"},
		{"ϑ", "&thetasym;", "&#977;"},
		{"ℑ", "&image;", "&#8465;"},
		{"↑", "&uarr;", "&#8593;"},
		{"⇐", "&lArr;", "&#8656;"},
		{"∀", "&forall;", "&#8704;"},
		{"∈", "&isin;", "&#8712;"},
		{"−", "&minus;", "&#8722;"},
		{"∠", "&ang;", "&#8736;"},
		{"∫", "&int;", "&#8747;"},
		{"≠", "&ne;", "&#8800;"},
		{"⊃", "&sup;", "&#8835;"},
		{"⊗", "&otimes;", "&#8855;"},
		{"⌊", "&lfloor;", "&#8970;"},
		{"♠", "&spades;", "&#9824;"},
		{"Α", "&Alpha;", "&#913;"},
		{"Ζ", "&Zeta;", "&#918;"},
		{"Λ", "&Lambda;", "&#923;"},
		{"Π", "&Pi;", "&#928;"},
		{"Φ", "&Phi;", "&#934;"},
		{"β", "&beta;", "&#946;"},
		{"η", "&eta;", "&#951;"},
		{"μ", "&mu;", "&#956;"},
		{"ρ", "&rho;", "&#961;"},
		{"φ", "&phi;", "&#966;"},
		{"ϒ", "&upsih;", "&#978;"},
		{"″", "&Prime;", "&#8243;"},
		{"ℜ", "&real;", "&#8476;"},
		{"→", "&rarr;", "&#8594;"},
		{"⇑", "&uArr;", "&#8657;"},
		{"∂", "&part;", "&#8706;"},
		{"∉", "&notin;", "&#8713;"},
		{"∗", "&lowast;", "&#8727;"},
		{"∧", "&and;", "&#8743;"},
		{"∴", "&there4;", "&#8756;"},
		{"≡", "&equiv;", "&#8801;"},
		{"⊄", "&nsub;", "&#8836;"},
		{"⊥", "&perp;", "&#8869;"},
		{"⌋", "&rfloor;", "&#8971;"},
		{"♣", "&clubs;", "&#9827;"},
		{"Β", "&Beta;", "&#914;"},
		{"Η", "&Eta;", "&#919;"},
		{"Μ", "&Mu;", "&#924;"},
		{"Ρ", "&Rho;", "&#929;"},
		{"Χ", "&Chi;", "&#935;"},
		{"γ", "&gamma;", "&#947;"},
		{"θ", "&theta;", "&#952;"},
		{"ν", "&nu;", "&#957;"},
		{"ς", "&sigmaf;", "&#962;"},
		{"χ", "&chi;", "&#967;"},
		{"ϖ", "&piv;", "&#982;"},
		{"‾", "&oline;", "&#8254;"},
		{"™", "&trade;", "&#8482;"},
		{"↓", "&darr;", "&#8595;"},
		{"⇒", "&rArr;", "&#8658;"},
		{"∃", "&exist;", "&#8707;"},
		{"∋", "&ni;", "&#8715;"},
		{"√", "&radic;", "&#8730;"},
		{"∨", "&or;", "&#8744;"},
		{"∼", "&sim;", "&#8764;"},
		{"≤", "&le;", "&#8804;"},
		{"⊆", "&sube;", "&#8838;"},
		{"⋅", "&sdot;", "&#8901;"},
		//{"⟨", "&lang;", "&#9001;"},
		{"♥", "&hearts;", "&#9829;"},
		{"Γ", "&Gamma;", "&#915;"},
		{"Θ", "&Theta;", "&#920;"},
		{"Ν", "&Nu;", "&#925;"},
		{"Σ", "&Sigma;", "&#931;"},
		{"Ψ", "&Psi;", "&#936;"},
		{"δ", "&delta;", "&#948;"},
		{"ι", "&iota;", "&#953;"},
		{"ξ", "&xi;", "&#958;"},
		{"σ", "&sigma;", "&#963;"},
		{"ψ", "&psi;", "&#968;"},
		{"•", "&bull;", "&#8226;"},
		{"⁄", "&frasl;", "&#8260;"},
		{"ℵ", "&alefsym;", "&#8501;"},
		{"↔", "&harr;", "&#8596;"},
		{"⇓", "&dArr;", "&#8659;"},
		{"∅", "&empty;", "&#8709;"},
		{"∏", "&prod;", "&#8719;"},
		{"∝", "&prop;", "&#8733;"},
		{"∩", "&cap;", "&#8745;"},
		{"∝", "&cong;", "&#8773;"},
		{"≥", "&ge;", "&#8805;"},
		{"⊇", "&supe;", "&#8839;"},
		{"⌈", "&lceil;", "&#8968;"},
		//{"⟩", "&rang;", "&#9002;"},
		{"♦", "&diams;", "&#9830;"},
		{"Δ", "&Delta;", "&#916;"},
		{"Ι", "&Iota;", "&#921;"},
		{"Ξ", "&Xi;", "&#926;"},
		{"Τ", "&Tau;", "&#932;"},
		{"Ω", "&Omega;", "&#937;"},
		{"ε", "&epsilon;", "&#949;"},
		{"κ", "&kappa;", "&#954;"},
		{"ο", "&omicron;", "&#959;"},
		{"τ", "&tau;", "&#964;"},
		{"ω", "&omega;", "&#969;"},
		{"℘", "&weierp;", "&#8472;"},
		{"←", "&larr;", "&#8592;"},
		{"↵", "&crarr;", "&#8629;"},
		{"⇔", "&hArr;", "&#8660;"},
		{"∇", "&nabla;", "&#8711;"},
		{"∑", "&sum;", "&#8721;"},
		{"∞", "&infin;", "&#8734;"},
		{"∪", "&cup;", "&#8746;"},
		{"≈", "&asymp;", "&#8776;"},
		{"⊂", "&sub;", "&#8834;"},
		{"⊕", "&oplus;", "&#8853;"},
		{"⌉", "&rceil;", "&#8969;"},
		{"◊", "&loz;", "&#9674;"},
		{"œ", "&oelig;", "&#339;"},
		{"˜", "&tilde;", "&#732;"},
		{"‘", "&lsquo;", "&#8216;"},
		{"„", "&bdquo;", "&#8222;"},
		{"›", "&rsaquo;", "&#8250;"},
		{"Š", "&Scaron;", "&#352;"},
		{"’", "&rsquo;", "&#8217;"},
		{"†", "&dagger;", "&#8224;"},
		{"š", "&scaron;", "&#353;"},
		{"‚", "&sbquo;", "&#8218;"},
		{"‡", "&Dagger;", "&#8225;"},
		{"Ÿ", "&Yuml;", "&#376;"},
		{"–", "&ndash;", "&#8211;"},
		{"‰", "&permil;", "&#8240;"},
		{"Œ", "&OElig;", "&#338;"},
		{"ˆ", "&circ;", "&#710;"},
		{"—", "&mdash;", "&#8212;"}
	};
	private final static int[] ESCAPE_SEQUENCE_MAX_LENGTH = {0};
		
	/**
	 * 文本转换为16进制
	 * @param text
	 * @return
	 */
	public static String toHexString(String text) {
		String encoded = "";
		byte[] data = text.getBytes();
		for(int i=0; i<data.length; i++) {
			encoded += toHexString(data[i]);
		}
		return encoded;
	}
	
	/**
     * 转换字节为16进制字符
     * @param b
     * @return
     */
    private static String toHexString(byte b) {
        int intValue = (b>=0 ? b : 256+b);
        String hex = Integer.toHexString(intValue);
        return hex.length()<2 ? "0" + hex : hex;
    }
    
    /**
     * 首字母大写
     * @param text
     * @return
     */
    public static String capitalizeFirstLetter(String text) {
    	if(text==null || text.isEmpty()) {
    		return text;
    	}
    	return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    /**
     * 获取大写的金额
     * @param money
     * @return
     */
    public static String getMoneyCapital(double money, boolean html) {
        if(money>999999999999.99) {
            return null;
        } 
        String[] nums = new String[]{"零","壹","贰","叁","肆","伍","陆","柒","捌","玖","拾","佰","仟","萬","亿"};
        String capital = "元";
        long longMonty = Math.round(money * 100);
        if(longMonty%100 == 0) {
            capital += "整";
        }
        else {
            capital += nums[(int)(longMonty % 100 / 10)] + "角";
            capital += nums[(int)(longMonty % 10)] + "分";
        }
        longMonty /= 100;
        int i = 0;
        do {
            if(i%4==0) {
                capital = (i==0 ? "" : nums[12 + i/4]) + capital;
            }
            else {
                capital = nums[9 + i%4] + capital;
            }
            i++;
            capital = (html ? "<font style=\"text-decoration:underline\">&nbsp;" + nums[(int)(longMonty%10)] + "&nbsp;</font>" : nums[(int)(longMonty%10)]) + capital;
            longMonty /= 10;
        }while(longMonty > 0);
        return capital;
    }
	
	/**
	 * 当字符串长度超过maxCharCount截取
	 * @param text
	 * @param maxCharCount
	 * @param ellipsis
	 * @return
	 */
	public static String slice(String text, int maxCharCount, String ellipsis) {
		if(text==null || maxCharCount==0 || text.getBytes().length<=maxCharCount) {
			return text;
		}
		if(ellipsis==null) {
			ellipsis = "";
		}
		int ellipsisLength = ellipsis.getBytes().length;
		int beginIndex = (maxCharCount - ellipsisLength)/2;
		int endIndex = beginIndex;
		String newOutput = text.substring(0, endIndex);
		int left = maxCharCount - ellipsisLength - newOutput.getBytes().length;
		int length = text.length();
		for(int append=0; endIndex<length; endIndex++) {
			append += text.charAt(endIndex)>255 ? 2 : 1;
			if(append==left) {
				break;
			}
			else if(append>left) {
				endIndex--;
				break;
			}
		}
		if(endIndex>=beginIndex) {
			text = newOutput + text.substring(beginIndex, endIndex + 1) + ellipsis;
		}
		else {
			text = newOutput + ellipsis;
		}
		return text;
	}
	
	/**
	 * 判断两个文本是否相同
	 * @param text1
	 * @param text2
	 * @return
	 */
	public static boolean isEquals(String text1, String text2) {
		if(text1==null && text2==null) {
			return true;
		}
		else if(text1==null || text2==null) {
			return false;
		}
		else {
			return text1.equals(text2);
		}
	}
	
	/**
	 * 格式化数字
	 * @param number
	 * @param length
	 * @param fixedLength 固定长度
	 * @return
	 */
	public static String formatNumber(long number, int length, boolean fixedLength) {
		if(length<=0) {
			return "" + number;
		}
		char[] format = new char[length];
		for(int i=0; i<length; i++) {
			format[i] = '0';
		}
		DecimalFormat formatter = new DecimalFormat(new String(format));
		String result = formatter.format(number);
		if(fixedLength && result.length() > length) {
			result = result.substring(result.length() - length);
		}
		return result;
	}
	
	/**
	 * 获取样式
	 * @param cssText
	 * @param styleName
	 * @return
	 */
	public static String getStyle(String cssText, String styleName) {
		if(cssText==null ||  cssText.equals("")) {
			return "";
		}
		String[] styles = cssText.split(";");
		for(int i=0; i<styles.length; i++) {
			int index = styles[i].indexOf(":");
			if(index!=-1 && styleName.equalsIgnoreCase(styles[i].substring(0, index).trim())) {
				return styles[i].substring(index+1).trim();
			}
		}
		return "";
	}
	
	/**
	 * 删除css样式
	 * @param cssText
	 * @param styleName
	 * @return
	 */
	public static String removeStyles(String cssText, String styleNames) {
		if(cssText==null || cssText.equals("")) {
			return "";
		}
		styleNames = "," + styleNames.toLowerCase() + ",";
		String newCssText = null;
		String[] styles = cssText.split(";");
		for(int i=0; i<styles.length; i++) {
			int index = styles[i].indexOf(":");
			if(index!=-1 && styleNames.indexOf("," + styles[i].substring(0, index).trim().toLowerCase() + ",")==-1) {
				newCssText = (newCssText==null ? "" : newCssText + ";") + styles[i];
			}
		}
		return newCssText;
	}
	
	/**
	 * 为属性值编码
	 * @return
	 */
	public static String encodePropertyValue(String propertyValue) {
		if(propertyValue==null) {
			return null;
		}
		return propertyValue.replaceAll("%", "%25").replaceAll("&", "%26").replaceAll("=", "%3D");
	}
	
	/**
	 * 获取属性(Attribute)列表
	 * @param properties 如:name=siteLink&siteId=0&siteName=远略软件
	 * @return
	 */
	public static List getProperties(String properties) {
		List result = new ArrayList();
		String[] values = properties.split("&");
		for(int i=0; i<values.length; i++) {
			int index = values[i].indexOf("=");
			if(index==-1) {
				continue;
			}
			result.add(new Attribute(values[i].substring(0, index), values[i].substring(index + 1).replaceAll("%26", "&").replaceAll("%3D", "=").replaceAll("%25", "%")));
		}
		return result;
	}
	
	/**
	 * 获取HTML元素的属性
	 * @param html
	 * @param attributeName
	 * @return
	 */
	public static String getHtmlElementAttribute(String elementHtml, String attributeName) {
		int index = elementHtml.toLowerCase().indexOf(attributeName.toLowerCase() + "=");
		if(index==-1) {
			return null;
		}
		index += attributeName.length() + 1;
		char prefix = elementHtml.charAt(index);
		if(prefix=='"' || prefix=='\'') {
			index++;
			int endIndex = elementHtml.indexOf(prefix, index);
			return endIndex==-1 ? null : decodeHtmlElementAttribute(elementHtml.substring(index, endIndex));
		}
		int endIndex = elementHtml.indexOf('>', index);
		if(endIndex==-1) {
			return null;
		}
		elementHtml = elementHtml.substring(index, endIndex);
		endIndex = elementHtml.indexOf(' ');
		return decodeHtmlElementAttribute(endIndex==-1 ? elementHtml : elementHtml.substring(0, endIndex));
	}
	
	/**
	 * HTML元素属性解码
	 * @param attributeValue
	 * @return
	 */
	public static String decodeHtmlElementAttribute(String attributeValue) {
        if(attributeValue == null) {
            return null;
        }
        return attributeValue.replaceAll("&lt;", "<")
					         .replaceAll("&gt;", ">")
					         .replaceAll("&amp;", "&")
					         .replaceAll("&quot;", "\"")
					         .replaceAll("&#39;", "'");
    }
	
	/**
	 * 获取属性值
	 * @param properties 如:name=siteLink&siteId=0&siteName=远略软件
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyValue(String properties, String propertyName) {
		if(properties==null) {
			return null;
		}
		int index = properties.indexOf(propertyName + "=");
		if(index==-1) {
			return null;
		}
		index += propertyName.length() + 1;
		int indexEnd = properties.indexOf("&", index);
		String value = indexEnd==-1 ? properties.substring(index) : properties.substring(index, indexEnd);
		if("".equals(value)) {
			return null;
		}
		return value.replaceAll("%26", "&").replaceAll("%3D", "=").replaceAll("%25", "%");
	}
	
	/**
	 * 获取长整数属性值
	 * @param properties 如:name=siteLink&siteId=0&siteName=远略软件
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	public static long getPropertyLongValue(String properties, String propertyName, long defaultValue) {
		String value = getPropertyValue(properties, propertyName);
		try {
			return Long.parseLong(value);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 获取整数属性值
	 * @param properties 如:name=siteLink&siteId=0&siteName=远略软件
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	public static int getPropertyIntValue(String properties, String propertyName, int defaultValue) {
		String value = getPropertyValue(properties, propertyName);
		try {
			return Integer.parseInt(value);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 解析整数
	 * @param text
	 * @param defaultValue
	 * @return
	 */
	public static int parseInt(String text, int defaultValue) {
		try {
			return Integer.parseInt(text);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 解析长整数
	 * @param text
	 * @param defaultValue
	 * @return
	 */
	public static long parseLong(String text, long defaultValue) {
		try {
			return Long.parseLong(text);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 解析浮点数
	 * @param text
	 * @param defaultValue
	 * @return
	 */
	public static double parseDouble(String text, double defaultValue) {
		try {
			return Double.parseDouble(text);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 格式化值
	 * @param value
	 * @param nullValue
	 * @return
	 */
	public static String format(Object value, String format, String nullValue) {
		if(value==null) {
			return nullValue;
		}
		if(value instanceof Date) {
			return DateTimeUtils.formatDate((Date)value, format);
		}
		else if(value instanceof Timestamp) {
			return DateTimeUtils.formatTimestamp((Timestamp)value, format);
		}
		else if((value instanceof Double) || (value instanceof Float)) {
			DecimalFormat formatter = new DecimalFormat(format==null || format.equals("") ? "#.###########" : format);
			return formatter.format(((Number)value).doubleValue()); 
		}
		return value.toString();
	}

	/**
	 * 数字转换为中文
	 * @param number
	 * @param capital
	 * @return
	 */
	public static String getChineseNumber(int number, boolean capital) {
        String[] nums = capital ? new String[]{"零","壹","贰","叁","肆","伍","陆","柒","捌","玖","拾","佰","仟","萬","亿"} : new String[]{"〇","一","二","三","四","五","六","七","八","九","十","百","千","万","亿"};
        String chineseNumber = "";
        int i = 0;
        do {
            if(i%4==0) {
            	chineseNumber = (i==0 ? "" : nums[12 + i/4]) + chineseNumber;
            }
            else {
            	chineseNumber = nums[9 + i%4] + chineseNumber;
            }
            i++;
            chineseNumber = nums[(int)(number%10)] + chineseNumber;
            number /= 10;
        }
        while(number > 0);
        if(capital) {
	        chineseNumber = chineseNumber.replaceAll("零仟", "零").replaceAll("零佰", "零").replaceAll("零拾", "零").replaceAll("零零零", "零").replaceAll("零零", "零").replaceAll("零萬", "萬").replaceAll("零亿", "亿").replaceAll("亿萬", "亿");
	        if(chineseNumber.endsWith("零") && !chineseNumber.equals("零")) {
	        	chineseNumber = chineseNumber.substring(0, chineseNumber.length() - 1);
	        }
	        if(chineseNumber.startsWith("壹拾")) {
	        	chineseNumber = chineseNumber.substring(1);
	        }
        }
        else {
	        chineseNumber = chineseNumber.replaceAll("〇千", "〇").replaceAll("〇百", "〇").replaceAll("〇十", "〇").replaceAll("〇〇〇", "〇").replaceAll("〇〇", "〇").replaceAll("〇万", "万").replaceAll("〇亿", "亿").replaceAll("亿万", "亿");
	        if(chineseNumber.endsWith("〇") && !chineseNumber.equals("〇")) {
	        	chineseNumber = chineseNumber.substring(0, chineseNumber.length() - 1);
	        }
	        if(chineseNumber.startsWith("一十")) {
	        	chineseNumber = chineseNumber.substring(1);
	        }
        }
        return chineseNumber;
    }
	
	/**
	 * 获取文件大小
	 * @param size
	 * @return
	 */
	public static String getFileSize(long size) {
		String[] units = {"B", "KB", "MB", "GB", "TB"};
		int index;
		for(index = 1; index < units.length && size / Math.pow(10, index * 3) >= 1; index++);
		return new DecimalFormat("#.##").format(size / Math.pow(10, (index - 1) * 3)) + units[index-1];
	}
	
	/**
	 * 解析文件大小
	 * @param fileSize
	 * @return
	 */
	public static double parseFileSize(String fileSize) {
		if(fileSize==null) {
			return 0;
		}
		fileSize = fileSize.replaceAll("b", "").toLowerCase();
		double value = Double.parseDouble(fileSize.replaceAll("[gmk]", ""));
		if(fileSize.endsWith("g")) {
			value *= Math.pow(10, 9);
		}
		else if(fileSize.endsWith("m")) {
			value *= Math.pow(10, 6);
		}
		else if(fileSize.endsWith("k")) {
			value *= Math.pow(10, 3);
		}
		return value;
	}
	
	/**
	 * 把毫秒数转换为时间,n天n小时n分钟
	 * @param millionSeconds
	 * @return
	 */
	public static String getTime(long millionSeconds) {
		millionSeconds = Math.round(millionSeconds/60000.0);
		long minutes = millionSeconds % 60;
		millionSeconds = millionSeconds / 60;
		long hours = millionSeconds % 24;
		long days = millionSeconds / 24;
		if(days>0) {
			if(hours==0 && minutes==0) {
				return days + "天";
			}
			else if(hours==0) {
				return days + "天零" + minutes + "分钟";
			}
			else {
				return days + "天" + hours + "小时" + (minutes==0 ? "" : minutes + "分钟");
			}
		}
		else if(hours==0) {
			return minutes + "分钟";
		}
		else {
			return hours + "小时" + (minutes==0 ? "" : minutes + "分钟");
		}
	}
	
	/**
	 * 替换
	 * @param text
	 * @param find
	 * @param replaced
	 * @return
	 */
	public static String replace(String text, String toReplace, String replaceAs) {
		String newText = "";
		int prevIndex = 0;
		for(int index = text.indexOf(toReplace); index!=-1; index = text.indexOf(toReplace, index)) {
			newText += text.substring(prevIndex, index) + replaceAs;
			index += toReplace.length();
			prevIndex = index;
		}
		return newText + text.substring(prevIndex);
	}
	
	/**
	 * 生成HTML内容
	 * @param html
	 * @return
	 */
	public static String generateHtmlContent(String text) {
		if(text==null) {
			return text;
		}
		return escape(text).replaceAll("\r", "").replaceAll("\n", "<br/>");
	}
	
	/**
	 * 过滤掉html元素
	 * @param html
	 * @param keepNewline
	 * @return
	 */
	public static String filterHtmlElement(String html, boolean keepNewline) {
		if(html==null) {
			return html;
		}
		html = html.replaceAll("(?i)<head(.*)>[\\s\\S]*?<\\/head>", "")
				   .replaceAll("(?i)<style(.*)>[\\s\\S]*?<\\/style>", "");
		if(keepNewline) {
			html = html.replaceAll("(?i)</p>", "\r\n")
				   	   .replaceAll("(?i)<br[^>]*>", "\r\n");
		}
		html = html.replaceAll("<[!/\\?a-zA-Z]+[^>]*>", "");
		int index = html.indexOf('>');
		if(index!=-1 &&
		   html.lastIndexOf('<', index-1)==-1 &&
		   (index==0 || isAlphaOrDigit(html.charAt(index-1)) || html.charAt(index-1)=='"' || html.charAt(index-1)=='\'' || html.charAt(index-1)==' ')) {
			html = html.substring(index + 1);
		}
		index = html.lastIndexOf('<');
		if(index!=-1 && html.indexOf('>', index+1)==-1 &&
		   (index==html.length()-1 || html.charAt(index+1)=='/' || isAlphaOrDigit(html.charAt(index+1)))) {
			html = html.substring(0, index);
		}
		return unescape(html);
	}
	
	/**
	 * 判断是否字母或数字
	 * @param characcter
	 * @return
	 */
	public static boolean isAlphaOrDigit(char characcter) {
		if(characcter>='0' && characcter<='9') {
			return true;
		}
		if(characcter>='a' && characcter<='z') {
			return true;
		}
		if(characcter>='A' && characcter<='Z') {
			return true;
		}
		return false;
	}
	
	/**
	 * 把特殊字符转换为转义码
	 * @param text
	 * @return
	 */
	public static String escape(String text) {
		if(text==null || text.isEmpty()) {
			return text;
		}
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<text.length(); i++) {
			char chr = text.charAt(i);
			int j=0;
			for(; j<ESCAPE_SEQUENCES.length; j++) {
				if(chr==ESCAPE_SEQUENCES[j][0].charAt(0)) {
					buffer.append(ESCAPE_SEQUENCES[j][1]);
					break;
				}
			}
			if(j==ESCAPE_SEQUENCES.length) {
				buffer.append(chr);
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 把转义码转换为原始字符
	 * @param text
	 * @return
	 */
	public static String unescape(String text) {
		if(text==null || text.isEmpty()) {
			return text;
		}
		if(ESCAPE_SEQUENCE_MAX_LENGTH[0]==0) {
			for(int j = 0; j<ESCAPE_SEQUENCES.length; j++) {
				ESCAPE_SEQUENCE_MAX_LENGTH[0] = Math.max(ESCAPE_SEQUENCES[j][1].length(), ESCAPE_SEQUENCE_MAX_LENGTH[0]);
			}
		}
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<text.length(); i++) {
			char chr = text.charAt(i);
			if(chr!='&') {
				buffer.append(chr);
				continue;
			}
			int endIndex = -1;
			for(int j=i+1; j < text.length() && j < i + ESCAPE_SEQUENCE_MAX_LENGTH[0]; j++) {
				if(text.charAt(j)==';') {
					endIndex = j;
					break;
				}
			}
			if(endIndex==-1) {
				buffer.append(chr);
				continue;
			}
			String escaped = text.substring(i, endIndex + 1);
			int j=0;
			for(; j<ESCAPE_SEQUENCES.length; j++) {
				if(escaped.equals(ESCAPE_SEQUENCES[j][escaped.charAt(1)=='#' ? 2 : 1])) {
					buffer.append(ESCAPE_SEQUENCES[j][0]);
					break;
				}
			}
			if(j==ESCAPE_SEQUENCES.length) {
				buffer.append(escaped);
			}
			i = endIndex;
		}
		return buffer.toString();
	}
	
	/**
	 * 替换文本中的参数,格式为{PARAMETER:参数名},如{PARAMETER:siteId},参数值的获取过程:1.bean 2.request.getParameter 3.request.getAttribute
	 * @param text
	 * @param isURL
	 * @param isScript
	 * @param isSql
	 * @param encoding
	 * @param bean
	 * @param request
	 * @param fillParametersCallback
	 * @return
	 */
	public static String fillParameters(String text, boolean isURL, boolean isScript, boolean isSql, String encoding, Object bean, HttpServletRequest request, FillParametersCallback fillParametersCallback) {
		if(text==null) {
			return text;
		}
		String newText = "";
		int beginIndex = 0;
		SessionInfo sessionInfo = request==null ? null : RequestUtils.getSessionInfo(request);
		for(int endIndex = text.indexOf("}"); endIndex>=0; endIndex = text.indexOf("}", beginIndex)) {
			int index = text.lastIndexOf('{', endIndex);
			if(index<beginIndex) {
				newText += text.substring(beginIndex, endIndex + 1);
				beginIndex = endIndex + 1;
				continue;
			}
			newText += text.substring(beginIndex, index);
			beginIndex = index;
			String value = null;
			if(text.startsWith("{CONTEXTPATH}", beginIndex)) {
				value = Environment.getContextPath();
			}
			else if(text.startsWith("{WEBAPPLICATIONURL}", beginIndex)) {
				value = Environment.getWebApplicationUrl(); //URL
			}
			else if(text.startsWith("{WEBAPPLICATIONSAFEURL}", beginIndex)) {
				value = Environment.getWebApplicationSafeUrl(); //安全的URL
			}
			else if(text.startsWith("{CURRENTURL}", beginIndex)) {
				value = RequestUtils.getRequestURL(request, true); //当前URL
			}
			else if(text.startsWith("{RANDOMNUMBER}", beginIndex)) {
				value = "" + new Random().nextInt(1000000000); //随机数
			}
			else if(text.startsWith("{USERID}", beginIndex)) {
				value = sessionInfo==null ? "" : "" + sessionInfo.getUserId(); //用户ID
			}
			else if(text.startsWith("{USERIDS}", beginIndex)) {
				value = sessionInfo==null ? "" : "" + sessionInfo.getUserIds(); //用户ID列表
			}
			else if(text.startsWith("{LOGINNAME}", beginIndex)) {
				value = sessionInfo==null ? "" : sessionInfo.getLoginName(); //用户登录名
			}
			else if(text.startsWith("{USERNAME}", beginIndex)) {
				value = sessionInfo==null ? "" : sessionInfo.getUserName(); //用户名
			}
			else if(text.startsWith("{DEPARTMENTID}", beginIndex)) {
				value = sessionInfo==null ? "" : "" + sessionInfo.getDepartmentId(); //用户部门ID
			}
			else if(text.startsWith("{DEPARTMENTIDS}", beginIndex)) {
				value = sessionInfo==null ? "" :sessionInfo.getDepartmentIds(); //用户部门ID列表,包括上级部门
			}
			else if(text.startsWith("{ROLEIDS", beginIndex)) {
				if(sessionInfo!=null && sessionInfo.getRoleIds()!=null && !sessionInfo.getRoleIds().isEmpty()) {
					value = sessionInfo.getRoleIds(); //角色
				}
				else {
					value = text.substring(beginIndex + "{ROLEIDS".length(), endIndex);
					value = (value.startsWith("|") ? value.substring(1) : "");
				}
			}
			else if(text.startsWith("{UNITID}", beginIndex)) {
				value = sessionInfo==null ? "" :"" + sessionInfo.getUnitId(); //用户单位ID
			}
			else if(text.startsWith("{NOW", beginIndex)) {
				int adjust = 0;
				if(endIndex>beginIndex+5) {
					adjust = Integer.parseInt(text.substring(beginIndex + 5, endIndex));
					adjust = (text.charAt(beginIndex + 4)=='-' ? -adjust : adjust);
				}
				value = DateTimeUtils.formatTimestamp(DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR_OF_DAY, adjust), "yyyy-MM-dd HH:mm:ss");
			}
			else if(text.startsWith("{TODAY", beginIndex)) {
				int adjust = 0;
				if(endIndex>beginIndex+7) {
					adjust = Integer.parseInt(text.substring(beginIndex + 7, endIndex));
					adjust = (text.charAt(beginIndex + 6)=='-' ? -adjust : adjust);
				}
				value = DateTimeUtils.formatDate(DateTimeUtils.add(DateTimeUtils.date(), Calendar.DAY_OF_MONTH, adjust), "yyyy-MM-dd");
			}
			else if(text.startsWith("{YEAR}", beginIndex)) {
				value = "" + DateTimeUtils.getYear(DateTimeUtils.now());
			}
			else if(text.startsWith("{YEARS}", beginIndex)) { //年份列表
				int year = DateTimeUtils.getYear(DateTimeUtils.now());
				value = "";
				for(int i = year - 20; i <= year + 20; i++) {
					value += (value.isEmpty() ? "" : "\\0 ") + i;
				}
			}
			else if(text.startsWith("{MONTH}", beginIndex)) {
				value = "" + (DateTimeUtils.getMonth(DateTimeUtils.now()) + 1);
			}
			else if(text.startsWith("{MONTHS}", beginIndex)) { //月份列表
				value = "";
				for(int i = 1; i <= 12; i++) {
					value += (value.isEmpty() ? "" : "\\0 ") + i;
				}
			}
			else if(text.startsWith("{DAY}", beginIndex)) {
				value = "" + DateTimeUtils.getDay(DateTimeUtils.now());
			}
			else if(text.startsWith("{HOUR}", beginIndex)) {
				value = "" + DateTimeUtils.getHour(DateTimeUtils.now());
			}
			else if(text.startsWith("{MINUTE}", beginIndex)) {
				value = "" + DateTimeUtils.getMinute(DateTimeUtils.now());
			}
			else if(text.startsWith("{SECOND}", beginIndex)) {
				value = "" + DateTimeUtils.getSecond(DateTimeUtils.now());
			}
			else if(text.startsWith("{PARAMETER:", beginIndex)) {
				String defaultValue = null;
				String propertyName = text.substring(beginIndex + "{PARAMETER:".length(), endIndex);
				index = propertyName.indexOf('|');
				if(index!=-1) {
					defaultValue = propertyName.substring(index + 1);
					propertyName = propertyName.substring(0, index);
				}
				Object propertyValue = getPropertyValue(bean, request, propertyName, fillParametersCallback);
				if(propertyValue!=null) {
					value = format(propertyValue, null, null);
				}
				else {
					value = defaultValue;
				}
				if(value!=null && !value.isEmpty()) {
					if(isSql) { //SQL语句
						value = JdbcUtils.resetQuot(value); 
						if(!newText.endsWith("'") && !newText.endsWith("in(") && !newText.endsWith("in (") && !newText.endsWith("%") && !newText.endsWith("DATE(") && !newText.endsWith("TIMESTAMP(")) { //数字
							try {
								Double.parseDouble(value);
							}
							catch(Exception e) {
								//值不是数字
								Logger.exception(e);
								value = defaultValue;
							}
						}
					}
					else if(isScript) { //脚本
 						if(value.toLowerCase().indexOf("<script")!=-1 || value.toLowerCase().indexOf("</script")!=-1) {
 							value = defaultValue;
 						}
					}
				}
			}
			else {
				newText += text.substring(beginIndex, endIndex + 1);
			}
			if(value!=null) {
				try {
					newText += (isURL && newText.indexOf('?')!=-1 ? URLEncoder.encode(value, (encoding==null ? "utf-8" : encoding)) : value);
				}
				catch(UnsupportedEncodingException e) {
					e.printStackTrace();
					newText += value;
				}
			}
			beginIndex = endIndex + 1;
		}
		return newText + text.substring(beginIndex);
	}
	
	/**
	 * 获取属性值
	 * @param bean
	 * @param request
	 * @param propertyName
	 * @param fillParametersCallback
	 * @return
	 */
	private static Object getPropertyValue(Object bean, HttpServletRequest request, String propertyName, FillParametersCallback fillParametersCallback) {
		if(fillParametersCallback!=null) {
			Object vlaue = fillParametersCallback.getParameterValue(propertyName, bean, request);
			if(vlaue!=null) {
				return vlaue;
			}
		}
		if(bean!=null) {
			try {
				//在bean中获取属性值
				Object value = null;
				if(bean instanceof Map) {
					value = ((Map)bean).get(propertyName);
					if(value==null) {
						int index = propertyName.indexOf(".");
						if(index!=-1) {
							bean = ((Map)bean).get(propertyName.substring(0, index));
							return StringUtils.getPropertyValue(bean, request, propertyName.substring(index + 1), fillParametersCallback);
						}
					}
				}
				else if(bean instanceof SqlResult) {
					value = ((SqlResult)bean).get(propertyName);
				}
				if(value==null) {
					try {
						value = PropertyUtils.getProperty(bean, propertyName);
					}
					catch(Exception e) {
						if(bean instanceof Record) {
							value = ((Record)bean).getExtendPropertyValue(propertyName);
						}
						else {
							throw e;
						}
					}
				}
				if(value!=null) {
					return value;
				}
			}
			catch(Exception e) {
				
			}
		}
		if(request==null) {
			return null;
		}
		//在请求参数中获取
		String propertyValue = request.getParameter(propertyName);
		if(propertyValue!=null && !propertyValue.isEmpty()) {
			return propertyValue;
		}
		Object attribute = request.getAttribute(propertyName);
		if(attribute!=null) {
			return attribute;
		}
		int index = propertyName.indexOf(".");
		if(index!=-1) {
			try {
				Object value = request.getAttribute(propertyName.substring(0, index));
				if(value!=null) {
					value = PropertyUtils.getProperty(value, propertyName.substring(index + 1));
					if(value!=null) {
						return value;
					}
				}
			}
			catch(Exception e) {
				
			}	
		}
		//从表单中获取
		ActionConfig actionConfig = (ActionConfig)request.getAttribute("org.apache.struts.action.mapping.instance");
		if(actionConfig!=null && actionConfig.getName()!=null) {
			ActionForm actionForm = (ActionForm)request.getAttribute(actionConfig.getName());
			try {
				return PropertyUtils.getProperty(actionForm, propertyName);
			}
			catch(Exception e) {
				
			}
		}
		return null;
	}
	
	/**
	 * 从URL中获取参数
	 * @param url
	 * @param parameterName
	 * @return
	 */
	public static String getQueryParameter(String url, String parameterName) {
		String value = getPropertyValue(url, parameterName);
		try {
			return value==null ? null : URLDecoder.decode(value, "utf-8");
		}
		catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * 获取参数列表,map key: 参数名称, map value: String[]
	 * @param url
	 * @return
	 */
	public static Map getQueryParameters(String url) {
		if(url==null || url.isEmpty()) {
			return null;
		}
		int beginIndex = url.indexOf('?');
		if(beginIndex==-1) {
			return null;
		}
		Map parameters = new HashMap();
		beginIndex++;
		for(int endIndex = url.indexOf('=', beginIndex); endIndex!=-1; endIndex = url.indexOf('=', beginIndex)) {
			String name = url.substring(beginIndex, endIndex);
			endIndex++;
			beginIndex = url.indexOf('&', endIndex);
			String value;
			try {
				value = URLDecoder.decode(beginIndex==-1 ? url.substring(endIndex) : url.substring(endIndex, beginIndex), "utf-8");
			} 
			catch (UnsupportedEncodingException e) {
				continue;
			}
			String[] values = (String[])parameters.get(name);
			if(values==null) {
				parameters.put(name, new String[]{value});
			}
			else {
				String[] newValues = new String[values.length + 1];
				for(int i=0; i<values.length; i++) {
					newValues[i] = values[i];
				}
				newValues[values.length] = value;
				parameters.put(name, newValues);
			}
			if(beginIndex==-1) {
				break;
			}
			beginIndex++;
		}
		return parameters;
	}
	
	/**
	 * 将URL参数转换为queryString
	 * @param parameters
	 * @return
	 */
	public static String generateQueryString(Map parameters) {
		String queryString = null;
		for(Iterator iterator = parameters==null ? null : parameters.keySet().iterator(); iterator!=null && iterator.hasNext();) {
			String parameterName = (String)iterator.next();
			String[] parameterValues = (String[])parameters.get(parameterName);
			for(int i=0; i<(parameterValues==null ? 0 :parameterValues.length); i++) {
				try {
					queryString = (queryString==null ? "" : queryString + "&") + parameterName + "=" + URLEncoder.encode(parameterValues[i], "utf-8");
				}
				catch(UnsupportedEncodingException e) {
					
				}
			}
		}
		return queryString;
	}
	
	/**
	 * 从url中删除属性
	 * @param url
	 * @param parameterName
	 * @return
	 */
	public static String removeQueryParameters(String url, String parameterNames) {
		if(url==null || url.equals("")) {
			return url;
		}
		String[] values = parameterNames.split(",");
		for(int i=0; i<values.length; i++) {
			int beginIndex = url.indexOf(values[i] + "=");
			if(beginIndex==-1) {
				continue;
			}
			int endIndex = url.indexOf('&', beginIndex + values[i].length() + 1);
			url = (endIndex==-1 ? (beginIndex<=1 ? "" : url.substring(0, beginIndex-1)) : url.substring(0, beginIndex) + url.substring(endIndex + 1));
		}
		return url;
	}
	
	/**
	 * 删除URL中的"."和".."
	 * @param url
	 * @return
	 */
	public static String removeDotFromURL(String url) {
		String[] values = url.split("/");
		url = null;
		int count = 0;
		for(int i=values.length-1; i>=0; i--) {
			if(".".equals(values[i])) {
				continue;
			}
			if("..".equals(values[i])) {
				count++;
				continue;
			}
			if(count>0) {
				count--;
				continue;
			}
			if(url==null) {
				url = values[i];
			}
			else {
				url = values[i] + "/" + url;
			}
		}
		return url;
	}
	
	/**
	 * 过滤word格式
	 * @param wordContent
	 * @param htmlBodyOnly
	 * @param keepFont
	 * @return
	 */
	public static String removeWordFormat(String wordContent, boolean htmlBodyOnly, boolean keepFont) {
		if(htmlBodyOnly) {
			Pattern pattern = Pattern.compile("(?s).*<html[^>]*>.*<head>.*</head>.*<body[^>]*>(.*)</body>.*</html>.*", Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher(wordContent);
			wordContent = match.replaceAll("$1");
		}
		wordContent = wordContent.replaceAll("(?i)<o:p>\\s*</o:p>", "");
		wordContent = wordContent.replaceAll("(?i)\\s*mso-[^:]+:[^;\"']+;?", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*MARGIN: 0cm 0cm 0pt\\s*;", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*MARGIN: 0cm 0cm 0pt\\s*(\"|')", "$1" );
		wordContent = wordContent.replaceAll("(?i)\\s*left:[\\s]*-[^\\s;]+;", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*left:[\\s]*-[^\\s;]+(\"|')", "$1" );
		wordContent = wordContent.replaceAll("(?i)\\s*right:[\\s]*-[^\\s;]+;", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*right:[\\s]*-[^\\s;]+(\"|')", "$1" );
		wordContent = wordContent.replaceAll("(?i)\\s*top:[\\s]*-[^\\s;]+;", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*top:[\\s]*-[^\\s;]+(\"|')", "$1" );
		wordContent = wordContent.replaceAll("(?i)\\s*bottom:[\\s]*-[^\\s;]+;", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*bottom:[\\s]*-[^\\s;]+(\"|')", "$1" );
		wordContent = wordContent.replaceAll("(?i)\\s*TEXT-INDENT:[\\s]*0cm\\s*;", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*TEXT-INDENT:[\\s]*0cm\\s*(\"|')", "$1" );
		//wordContent = wordContent.replaceAll("(?i)\\s*TEXT-ALIGN:[\\s]*[^\\s;]+;", "");
		//wordContent = wordContent.replaceAll("(?i)\\s*TEXT-ALIGN:[\\s]*[^\\s;]+(\"|')", "$1" );
		wordContent = wordContent.replaceAll("(?i)\\s*PAGE-BREAK-BEFORE:[\\s]*[^\\s;]+;", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*PAGE-BREAK-BEFORE:[\\s]*[^\\s;]+(\"|')", "$1" );
		wordContent = wordContent.replaceAll("(?i)\\s*FONT-VARIANT:[\\s]*[^\\s;]+;", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*FONT-VARIANT:[\\s]*[^\\s;]+(\"|')", "$1" );
		wordContent = wordContent.replaceAll("(?i)\\s*tab-stops:[\\s]*[^\\s;]+;", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*tab-stops:[\\s]*[^\\s;]+(\"|')", "$1" );
		wordContent = wordContent.replaceAll("(?i)\\s*face=\"[^\"]*\"", "" );
		wordContent = wordContent.replaceAll("(?i)\\s*face=[^ >]*", "" );
		if(!keepFont) {
			wordContent = wordContent.replaceAll("(?i)\\s*FONT-FAMILY:[\\s]*[^;\"'>]+;", "");
			wordContent = wordContent.replaceAll("(?i)\\s*FONT-FAMILY:[\\s]*[^;\"'>]+(\"|')", "$1" );
			wordContent = wordContent.replaceAll("(?i)\\s*FONT-SIZE:[\\s]*[^;\"'>]+;", "" );
			wordContent = wordContent.replaceAll("(?i)\\s*FONT-SIZE:[\\s]*[^;\"'>]+(\"|')", "$1" );
			wordContent = wordContent.replaceAll("(?i)\\s*LINE-HEIGHT:[\\s]*[^;\"'>]+;", "" );
			wordContent = wordContent.replaceAll("(?i)\\s*LINE-HEIGHT:[\\s]*[^;\"'>]+(\"|')", "$1" );
			wordContent = wordContent.replaceAll("(?i)\\s*LAYOUT-GRID:[\\s]*[^;\"'>]+;", "" );
			wordContent = wordContent.replaceAll("(?i)\\s*LAYOUT-GRID:[\\s]*[^;\"'>]+(\"|')", "$1" );
		}
		//清除class
		wordContent = wordContent.replaceAll("(?i)<(\\w[^>]*) class=([^ |>]*)([^>]*)", "<$1$3");
		//清除空样式
		wordContent = wordContent.replaceAll("(?i)\\s*style='\\s*'", "" );
		wordContent = wordContent.replaceAll("(?i)<SPAN\\s*[^>]*>\\s*&nbsp;\\s*</SPAN>", "&nbsp;" );
		wordContent = wordContent.replaceAll("(?i)<SPAN\\s*[^>]*>\\s*\\s*</SPAN>", "" );
		//Remove Lang attributes
		//wordContent = wordContent.replaceAll("(?i)<(\\w[^>]*) lang=([^ |>]*)([^>]*)", "<$1$3");
		wordContent = wordContent.replaceAll("(?i)<(\\w[^>]*) lang=([^ >]*)(\\s)", "<$1$3");
		//wordContent = wordContent.replaceAll("(?i)<(\\w[^>]*) lang=([^ >]*)>", "< $1>");
		wordContent = wordContent.replaceAll("(?si)<SPAN\\s*>([.\\d]*?)</SPAN>", "$1");
		wordContent = wordContent.replaceAll("(?si)<FONT\\s*>(.*?)</FONT>", "$1");
		wordContent = wordContent.replaceAll("(?i)<\\?xml[^>]*>", "");
		wordContent = wordContent.replaceAll("(?i)</?\\w+:[^>]*>", "");
		wordContent = wordContent.replaceAll("(?si)<H\\d([^>]*)>(.*?)</H\\d>", "<div$1>$2</div>");
		wordContent = wordContent.replaceAll("(?si)<INS\\s*[^>]*>(.*?)</INS>", "$1");
		//wordContent = wordContent.replaceAll("(?i)<H1([^>]*)>", "<div$1><b><font size=\"6\">");
		//wordContent = wordContent.replaceAll("(?i)<H2([^>]*)>", "<div$1><b><font size=\"5\">");
		//wordContent = wordContent.replaceAll("(?i)<H3([^>]*)>", "<div$1><b><font size=\"4\">");
		//wordContent = wordContent.replaceAll("(?i)<H4([^>]*)>", "<div$1><b><font size=\"3\">");
		//wordContent = wordContent.replaceAll("(?i)<H5([^>]*)>", "<div$1><b><font size=\"2\">");
		//wordContent = wordContent.replaceAll("(?i)<H6([^>]*)>", "<div$1><b><font size=\"1\">");
		//wordContent = wordContent.replaceAll("(?i)</H\\d>", "</font></b></div>");
		//wordContent = wordContent.replaceAll("(?i)<H\\d*>", "");
		//wordContent = wordContent.replaceAll("(?i)/H\\d>", "");
		wordContent = wordContent.replaceAll("(?i)<(U|I|STRIKE)>&nbsp;</\\1>", "&nbsp;");
		//清除padding
		wordContent = wordContent.replaceAll("(?i)\\s*PADDING-LEFT:[^;\"]*;?", "");
		wordContent = wordContent.replaceAll("(?i)\\s*PADDING-RIGHT:[^;\"]*;?", "");
		wordContent = wordContent.replaceAll("(?i)\\s*PADDING-TOP:[^;\"]*;?", "");
		wordContent = wordContent.replaceAll("(?i)\\s*PADDING-BOTTOM:[^;\"]*;?", "");
		// Transform <P> to <DIV>
		wordContent = wordContent.replaceAll("(?si)(<P)([^>]*>.*?)(</P>)", "<div$2</div>");
		//Remove empty tags (three times, just to be sure).
		wordContent = wordContent.replaceAll("(?i)<([^\\s>]+)[^>]*>\\s*</\\1>", "");
		wordContent = wordContent.replaceAll("(?i)<([^\\s>]+)[^>]*>\\s*</\\1>", "");
		wordContent = wordContent.replaceAll("(?i)<([^\\s>]+)[^>]*>\\s*</\\1>", "");
    	//去除<![if !vml]>...<![endif]>
		wordContent = wordContent.replaceAll("(?s)<!\\[if !vml\\]>(.*?<img.*?)<!\\[endif\\]>", "$1");
		return wordContent;
	}
	
	/**
	 * 替换文本中的当前服务器IP
	 * @param text
	 * @return
	 */
	public static String fillLocalHostIP(String text) {
		String newText = "";
		int beginIndex = 0;
		int endIndex = 0;
		for(beginIndex = text.indexOf("{LOCALHOSTIP"); beginIndex!=-1; beginIndex = text.indexOf("{LOCALHOSTIP", endIndex)) {
			newText += text.substring(endIndex, beginIndex);
			beginIndex += "{LOCALHOSTIP".length();
			endIndex = text.indexOf('}', beginIndex);
			String prefix = text.substring(beginIndex, endIndex);
			if(prefix.startsWith("|")) {
				prefix = prefix.substring(1);
			}
			newText += NetworkUtils.getLocalHostIP(prefix);
			endIndex++;
		}
		newText += text.substring(endIndex);
		return newText;
	}
	
	/**
	 * 把异常转换为文字
	 * @param e
	 * @return
	 */
	public static String exceptionToString(Exception e) {
		ByteArrayOutputStream out = null;
		PrintStream ps = null;
		try {
			out = new java.io.ByteArrayOutputStream();
			ps = new PrintStream(out, true, "UTF-8");
			e.printStackTrace(ps);
			return out.toString("UTF-8");
		}
		catch(Exception ex){
			
		}
		finally {
			try {
				out.close();
			}
			catch(Exception ex) {
				
			}
			try {
				ps.close();
			}
			catch(Exception ex) {
				
			}
		}
		return null;
	}
	
	/**
	 * 把Error转换为文字
	 * @param e
	 * @return
	 */
	public static String errorToString(Error e) {
		ByteArrayOutputStream out = null;
		PrintStream ps = null;
		try {
			out = new java.io.ByteArrayOutputStream();
			ps = new PrintStream(out, true, "UTF-8");
			e.printStackTrace(ps);
			return out.toString("UTF-8");
		}
		catch(Exception ex){
			
		}
		finally {
			try {
				out.close();
			}
			catch(Exception ex) {
				
			}
			try {
				ps.close();
			}
			catch(Exception ex) {
				
			}
		}
		return null;
	}
	
	/**
	 * 检查字符串是否匹配
	 * @param text
	 * @param key 空格表示或,加号表示与
	 * @return
	 */
	public static boolean isMatch(String text, String key) {
		if(text==null || text.trim().isEmpty()) {
			return false;
		}
		if(key==null || key.trim().isEmpty()) {
			return true;
		}
		String[] keys = key.split(" ");
		for(int j=0; j<keys.length; j++) {
			if(keys[j].trim().isEmpty()) {
				continue;
			}
			boolean match = true;
			String[] andKeys = keys[j].split("\\+"); //加号,用与的关系查询
			for(int k=0; k<andKeys.length; k++) {
				if(!andKeys[k].trim().isEmpty() && text.indexOf(andKeys[k])==-1) {
					match = false;
					break;
				}
			}
			if(match) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 清除HTML中的服务器路径
	 * @param html
	 * @param request
	 * @return
	 */
	public static String removeServerPath(String html, HttpServletRequest request) {
		if(html==null || html.isEmpty()) {
			return html;
		}
		//清除页面中被IE自动加入的服务器地址
		int port = request.getServerPort();
		String url = (request.isSecure() ? "https" : "http") + "://" + 
					 request.getServerName() + 
					 ((request.isSecure() && port!=443) || (!request.isSecure() && port!=80) ? ":" + port : "") + "/";
		url = url.replace("\\.", "\\\\.");
		return html.replaceAll("(?i)([\\t\\r\\n ]src=[\"']?)" + url, "$1/")
				   .replaceAll("(?i)([\\t\\r\\n ]href=[\"']?)" + url, "$1/")
				   .replaceAll("(?i)([\\t\\r\\n :]url\\([\"']?)" + url, "$1/")
				   .replaceAll("(?i)(background=[\"']?)" + url, "$1/")
				   .replaceAll(".css\\?seq=[\\d]*\"", ".css\"");
	}
		
	/**
	 * 替换HTML正文中图片、链接的URL等
	 * @param pageURL
	 * @param htmlContent
	 * @return
	 * @throws ServiceException
	 */
	public static String resetResorcePath(String pageURL, String htmlContent) throws ServiceException {
		//更新资源的URL
		int beginIndex = pageURL.indexOf("://");
		if(beginIndex==-1) {
			return htmlContent;
		}
		String pagePath; //页面路径
		String pageRootPath; //页面根路径
		beginIndex = pageURL.indexOf("/", beginIndex + 3);
		if(beginIndex==-1) {
			pagePath = pageURL + "/"; //追加“/”
			pageRootPath = pagePath;
		}
		else {
			pageRootPath = pageURL.substring(0, beginIndex + 1);
			beginIndex = pageURL.lastIndexOf("/");
			pagePath = pageURL.substring(0, beginIndex + 1);
		}
		return htmlContent.replaceFirst("(?i)(<head[^>]*>)", "$1\n<script type=\"text/javascript\">window.onerror=function(){return true;}</script>")
						  .replaceAll("parent.location", "parent.location_deleted") //删除脚本parent.location.href = '';
						  .replaceAll("top.location", "top.location_deleted") //删除脚本parent.location.href = '';
						  .replaceAll("(?i)(<[^>]*src=[\"']?)/([^ >]*[ >])", "$1" + pageRootPath + "$2") //src更新
					  	  .replaceAll("(?i)(<[^>]*src=[\"']?)([^>: ]*[ >])", "$1" + pagePath + "$2")
					  	  .replaceAll("(?i)(<[^>]*href=[\"']?)/([^ >]*[ >])", "$1" + pageRootPath + "$2") //href更新
					  	  .replaceAll("(?i)(<[^>]*href=[\"']?)([^>: ]*[ >])", "$1" + pagePath + "$2")
					  	  .replaceAll("(?i)(<[^>]*window.open\\(')/([^']*')", "$1" + pageRootPath + "$2") //window.open更新
					  	  .replaceAll("(?i)(<[^>]*window.open\\(')([^':]*')", "$1" + pagePath + "$2")
					  	  .replaceAll("(?i)(<[^>]*background=[\"']?)/([^ >]*[ >])", "$1" + pageRootPath + "$2") //背景更新
					  	  .replaceAll("(?i)(<[^>]*background=[\"']?)([^>: ]*[ >])", "$1" + pagePath + "$2")
					  	  .replaceAll("(?i)(background[^\\)]*url)\\(/([^\\)]*)\\)", "$1(" + pageRootPath + "$2)") //背景更新
					  	  .replaceAll("(?i)(background[^\\)]*url)\\(([^/][^\\)^:]*)\\)",  "$1(" + pagePath + "$2)");
	}
	
	/**
	 * 全角转半角
	 * @param fullWidthText
	 * @return
	 */
	public static String fullWidthToHalfAngle(String fullWidthText) {
		String chars = "１1２2３3４4５5６6７7８8９9０0" +
					   "ａaｂbｃcｄdｅeｆfｇgｈhｉiｊjｋkｌlｍmｎnｏoｐpｑqｒrｓsｔtｕuｖvｗwｘxｙyｚz" +
					   "ＡAＢBＣCＤDＥEＦFＧGＨHＩIＪJＫKＬLＭMＮNＯOＰPＱQＲRＳSＴTＵUＶVＷWＸXＹYＺZ" +
					   "～~！!＠@＃#＄$％%＾^＆&＊*（(）)＿_＋+｀`－-＝=｛{｝}｜|［[］]＼\\：:＂\"；;＇'＜<＞>？?，,．.／/　 ";
		for(int i=0; i<chars.length(); i+=2) {
			fullWidthText = fullWidthText.replace(chars.charAt(i), chars.charAt(i+1));
		}
		return fullWidthText;
	}
	
	/**
	 * 检查是否单字节字符串
	 * @param text
	 * @return
	 */
	public static boolean isSingleByteCharacters(String text) {
		if(text==null || text.isEmpty()) {
			return false;
		}
		for(int i=0; i<text.length(); i++) {
			if(((int)text.charAt(i))>255) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 字符串分割,不使用正则表达式
	 * @param text
	 * @param keys
	 * @return
	 */
	public static List split(String text, List keys) {
		if(text==null) {
			return null;
		}
		List values = new ArrayList();
		if(keys==null || keys.isEmpty()) {
			values.add(text);
			return values;
		}
		int beginIndex = 0;
		for(;;) {
			int endIndex = -1;
			String currentKey = null;
			for(Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(key==null || key.isEmpty()) {
					continue;
				}
				int index = text.indexOf(key, beginIndex);
				if(index!=-1 && (endIndex==-1 || index<endIndex)) {
					endIndex = index;
					currentKey = key;
				}
			}
			if(endIndex==-1) {
				break;
			}
			values.add(text.substring(beginIndex, endIndex));
			values.add(currentKey);
			beginIndex = endIndex + currentKey.length();
		}
		values.add(text.substring(beginIndex));
		return values;
	}
	
	/**
	 * 删除标点符号以及空格、回车、换行等
	 * @param text
	 * @return
	 */
	public static String removePunctuation(String text) {
		return text==null ? null : text.replaceAll("[-=\r\n\t~!@#$%\\^&*()_+`\\[\\]\\\\{}|;':\",./<>? 　《》？，。／；‘’：“”【】、｛｝｜－＝——＋～｀]", "");
	}
	
	/**
	 * 清理掉文本中所有的空格、换行、回车
	 * @param text
	 * @return
	 */
	public static String trim(String text) {
		return text==null ? null : text.replaceAll("[\r\n\t ]", "");
	}
	
	/**
	 * 获取62进制字典
	 * @return
	 */
	private static String getStr62keys() {
		return "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	}
	
	/**
	 * 62进制值转换为10进制
	 * @param str62 62进制值
	 * @return
	 */
	public static long str62to10(String str62) {
		long i10 = 0;
		String str62keys = getStr62keys();
		for(int i = 0; i < str62.length(); i++) {
			int n = str62.length() - i - 1;
			i10 += str62keys.indexOf(str62.charAt(i)) * Math.pow(62, n);
		}
		return i10;
	}
	
	/**
	 * 10进制值转换为62进制
	 * @param int10
	 * @return
	 */
	public static String int10to62(long int10) {
		String s62 = "";
		String str62keys = getStr62keys();
		while(int10 != 0) {
			s62 = str62keys.charAt((int)(int10 % 62)) + s62;
			int10 = int10 / 62;
		}
		return s62;
	}
	
	/**
	 * 获取BEAN的标题
	 * @param bean
	 * @return
	 */
	public static String getBeanTitle(Object bean) { 
		String[] properties = new String[] {"subject", "title", "name", "projectName", "directoryName"};
		for(int i=0; i<properties.length; i++) {
			if(!PropertyUtils.isReadable(bean, properties[i])) {
				continue;
			}
			try {
				Object value = PropertyUtils.getProperty(bean, properties[i]);
				return value==null ? null : value.toString();
			}
			catch (Exception e) {

			}
		}
		return null;
	}
	
	/**
	 * 重置正则表达式保留字符
	 * @param regex
	 * @return
	 */
	public static String resetRegexMetacharacters(String regex) {
		String metacharacters = "\\$^.|{}[]()+?*";
		regex = regex.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
		for(int i = 2; i < metacharacters.length(); i++) {
			regex = regex.replaceAll("\\" + metacharacters.charAt(i), "\\\\" + metacharacters.charAt(i));
		}
		return regex;
	}
}