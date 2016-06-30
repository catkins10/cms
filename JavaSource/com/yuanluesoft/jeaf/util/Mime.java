/*
 * Created on 2005-8-29
 *
 */
package com.yuanluesoft.jeaf.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class Mime {
	public static final String MIME_ABS = "audio/x-mpeg";
	public static final String MIME_AI = "application/postscript";
	public static final String MIME_AIF = "audio/x-aiff";
	public static final String MIME_AIFC = "audio/x-aiff";
	public static final String MIME_AIFF = "audio/x-aiff";
	public static final String MIME_AIM = "application/x-aim";
	public static final String MIME_ART = "image/x-jg";
	public static final String MIME_ASF = "video/x-ms-asf";
	public static final String MIME_ASX = "video/x-ms-asf";
	public static final String MIME_AU = "audio/basic";
	public static final String MIME_AVI = "video/x-msvideo";
	public static final String MIME_AVX = "video/x-rad-screenplay";
	public static final String MIME_BCPIO = "application/x-bcpio";
	public static final String MIME_BIN = "application/octet-stream";
	public static final String MIME_STREAM = "application/octet-stream";
	public static final String MIME_BMP = "image/bmp";
	public static final String MIME_BODY = "text/html";
	public static final String MIME_CDF = "application/x-cdf";
	public static final String MIME_CER = "application/x-x509-ca-cert";
	public static final String MIME_CLASS = "application/java";
	public static final String MIME_CPIO = "application/x-cpio";
	public static final String MIME_CSH = "application/x-csh";
	public static final String MIME_CSS = "text/css";
	public static final String MIME_DIB = "image/bmp";
	public static final String MIME_DOC = "application/msword";
	public static final String MIME_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static final String MIME_DOT = "application/msword";
	public static final String MIME_DTD = "text/plain";
	public static final String MIME_DV = "video/x-dv";
	public static final String MIME_DVI = "application/x-dvi";
	public static final String MIME_EPS = "application/postscript";
	public static final String MIME_ETX = "text/x-setext";
	public static final String MIME_GIF = "image/gif";
	public static final String MIME_GTAR = "application/x-gtar";
	public static final String MIME_GZ = "application/x-gzip";
	public static final String MIME_HDF = "application/x-hdf";
	public static final String MIME_HQX = "application/mac-binhex40";
	public static final String MIME_HTC = "text/x-component";
	public static final String MIME_HTM = "text/html";
	public static final String MIME_MHT = "message/rfc822";
	public static final String MIME_HTML = "text/html";
	public static final String MIME_IEF = "image/ief";
	public static final String MIME_JAD = "text/vnd.sun.j2me.app-descriptor";
	public static final String MIME_JAR = "application/java-archive";
	public static final String MIME_JAVA = "text/plain";
	public static final String MIME_JNLP = "application/x-java-jnlp-file";
	public static final String MIME_JPE = "image/jpeg";
	public static final String MIME_JPEG = "image/jpeg";
	public static final String MIME_JPG = "image/jpeg";
	public static final String MIME_JS = "text/javascript";
	public static final String MIME_JSF = "text/plain";
	public static final String MIME_JSPF = "text/plain";
	public static final String MIME_KAR = "audio/x-midi";
	public static final String MIME_LATEX = "application/x-latex";
	public static final String MIME_M3U = "audio/x-mpegurl";
	public static final String MIME_MAC = "image/x-macpaint";
	public static final String MIME_MAN = "application/x-troff-man";
	public static final String MIME_ME = "application/x-troff-me";
	public static final String MIME_MID = "audio/x-midi";
	public static final String MIME_MIDI = "audio/x-midi";
	public static final String MIME_MIF = "application/x-mif";
	public static final String MIME_MOV = "video/quicktime";
	public static final String MIME_MOVIE = "video/x-sgi-movie";
	public static final String MIME_MP1 = "audio/x-mpeg";
	public static final String MIME_MP2 = "audio/x-mpeg";
	public static final String MIME_MP3 = "audio/x-mpeg";
	public static final String MIME_MPA = "audio/x-mpeg";
	public static final String MIME_MPE = "video/mpeg";
	public static final String MIME_MPEG = "video/mpeg";
	public static final String MIME_MPEGA = "audio/x-mpeg";
	public static final String MIME_MPG = "video/mpeg";
	public static final String MIME_MPV2 = "video/mpeg2";
	public static final String MIME_MS = "application/x-wais-source";
	public static final String MIME_NC = "application/x-netcdf";
	public static final String MIME_ODA = "application/oda";
	public static final String MIME_PBM = "image/x-portable-bitmap";
	public static final String MIME_PCT = "image/pict";
	public static final String MIME_PDF = "application/pdf";
	public static final String MIME_PGM = "image/x-portable-graymap";
	public static final String MIME_PIC = "image/pict";
	public static final String MIME_PICT = "image/pict";
	public static final String MIME_PLS = "audio/x-scpls";
	public static final String MIME_PNG = "image/png";
	public static final String MIME_PNM = "image/x-portable-anymap";
	public static final String MIME_PNT = "image/x-macpaint";
	public static final String MIME_PPM = "image/x-portable-pixmap";
	public static final String MIME_PS = "application/postscript";
	public static final String MIME_PSD = "image/x-photoshop";
	public static final String MIME_QT = "video/quicktime";
	public static final String MIME_QTI = "image/x-quicktime";
	public static final String MIME_QTIF = "image/x-quicktime";
	public static final String MIME_RAS = "image/x-cmu-raster";
	public static final String MIME_RGB = "image/x-rgb";
	public static final String MIME_RM = "application/vnd.rn-realmedia";
	public static final String MIME_ROFF = "application/x-troff";
	public static final String MIME_RTF = "application/rtf";
	public static final String MIME_RTX = "text/richtext";
	public static final String MIME_SH = "application/x-sh";
	public static final String MIME_SHAR = "application/x-shar";
	public static final String MIME_SMF = "audio/x-midi";
	public static final String MIME_SIT = "application/x-stuffit";
	public static final String MIME_SND = "audio/basic";
	public static final String MIME_SRC = "application/x-wais-source";
	public static final String MIME_SV4CPIO = "application/x-sv4cpio";
	public static final String MIME_SV4CRC = "application/x-sv4crc";
	public static final String MIME_SWF = "application/x-shockwave-flash";
	public static final String MIME_T = "application/x-troff";
	public static final String MIME_TAR = "application/x-tar";
	public static final String MIME_TGZ = "application/x-compressed";
	public static final String MIME_TCL = "application/x-tcl";
	public static final String MIME_TEX = "application/x-tex";
	public static final String MIME_TEXI = "application/x-texinfo";
	public static final String MIME_TEXINFO = "application/x-texinfo";
	public static final String MIME_TIF = "image/tiff";
	public static final String MIME_TIFF = "image/tiff";
	public static final String MIME_TR = "application/x-troff";
	public static final String MIME_TSV = "text/tab-separated-values";
	public static final String MIME_TXT = "text/plain";
	public static final String MIME_ULW = "audio/basic";
	public static final String MIME_USTAR = "application/x-ustar";
	public static final String MIME_XBM = "image/x-xbitmap";
	public static final String MIME_XML = "text/xml";
	public static final String MIME_XPM = "image/x-xpixmap";
	public static final String MIME_XSL = "text/xml";
	public static final String MIME_XWD = "image/x-xwindowdump";
	public static final String MIME_WAV = "audio/x-wav";
	public static final String MIME_WMA = "audio/x-ms-wma";
	public static final String MIME_WMV = "video/x-ms-wmv";
	public static final String MIME_SVG = "image/svg+xml";
	public static final String MIME_SVGZ = "image/svg+xml";
	public static final String MIME_WBMP = "image/vnd.wap.wbmp";
	public static final String MIME_WML = "text/vnd.wap.wml";
	public static final String MIME_WMLC = "application/vnd.wap.wmlc";
	public static final String MIME_WMLS = "text/vnd.wap.wmlscript";
	public static final String MIME_WMLSCRIPTC = "application/vnd.wap.wmlscriptc";
	public static final String MIME_WRL = "x-world/x-vrml";
	public static final String MIME_Z = "application/x-compress";
	public static final String MIME_ZIP = "application/zip";
	public static final String MIME_XLS = "application/vnd.ms-excel";
	public static final String MIME_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String MIME_PPT = "application/vnd.ms-powerpoint";
	public static final String MIME_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	public static final String MIME_PPS = "application/vnd.ms-powerpoint";
	public static final String MIME_PPSX = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
	public static final String MIME_RAR = "application/rar"; //application/x-rar-compressed
	public static final String MIME_ICO = "image/ico";
	public static final String MIME_DLL = "application/x-msdownload";
	
	private static String[][] mimeMapping = {
		{"abs", MIME_ABS},
		{"ai", MIME_AI},
		{"aif", MIME_AIF},
		{"aifc", MIME_AIFF},
		{"aiff", MIME_AIF},
		{"aim", MIME_AIM},
		{"art", MIME_ART},
		{"asf", MIME_ASF},
		{"asx", MIME_ASX},
		{"au", MIME_AU},
		{"avi", MIME_AVI},
		{"avx", MIME_AVX},
		{"bcpio", MIME_BCPIO},
		{"bin", MIME_BIN},
		{"exe", MIME_BIN},
		{"msi", MIME_BIN},
		{"chm", MIME_BIN},
		{"cab", MIME_BIN},
		{"ocx", MIME_BIN},
		{"bmp", MIME_BMP},
		{"body", MIME_BODY},
		{"cdf", MIME_CDF},
		{"cer", MIME_CER},
		{"class", MIME_CLASS},
		{"cpio", MIME_CPIO},
		{"csh", MIME_CSH},
		{"css", MIME_CSS},
		{"dib", MIME_DIB},
		{"doc", MIME_DOC},
		{"docx", MIME_DOCX},
		{"dot", MIME_DOT},
		{"dtd", MIME_DTD},
		{"dv", MIME_DV},
		{"dvi", MIME_DVI},
		{"eps", MIME_EPS},
		{"etx", MIME_ETX},
		{"gif", MIME_GIF},
		{"gtar", MIME_GTAR},
		{"gz", MIME_GZ},
		{"hdf", MIME_HDF},
		{"hqx", MIME_HQX},
		{"htc", MIME_HTC},
		{"htm", MIME_HTM},
		{"html", MIME_HTML},
		{"mht", MIME_MHT},
		{"mhtml", MIME_MHT},
		{"ief", MIME_IEF},
		{"jad", MIME_JAD},
		{"jar", MIME_JAR},
		{"java", MIME_JAVA},
		{"jnlp", MIME_JNLP},
		{"jpe", MIME_JPE},
		{"jpeg", MIME_JPEG},
		{"jpg", MIME_JPG},
		{"js", MIME_JS},
		{"jsf", MIME_JSF},
		{"jspf", MIME_JSPF},
		{"kar", MIME_KAR},
		{"latex", MIME_LATEX},
		{"m3u", MIME_M3U},
		{"mac", MIME_MAC},
		{"man", MIME_MAN},
		{"me", MIME_ME},
		{"mid", MIME_MID},
		{"mid", "audio/mid"},
		{"midi", MIME_MIDI},
		{"midi", "audio/mid"},
		{"rmi", "audio/mid"},
		{"mif", MIME_MIF},
		{"mov", MIME_MOV},
		{"movie", MIME_MOVIE},
		{"mp1", MIME_MP1},
		{"mp2", MIME_MP2},
		{"mp2", "audio/mpeg"},
		{"mp3", MIME_MP3},
		{"mp3", "audio/mpeg"},
		{"mpa", MIME_MPA},
		{"mpe", MIME_MPE},
		{"mpe", "audio/mpeg"},
		{"mpeg", MIME_MPEG},
		{"mpeg", "audio/mpeg"},
		{"mpega", MIME_MPEGA},
		{"mpg", MIME_MPG},
		{"mpg", "audio/mpeg"},
		{"mpv2", MIME_MPV2},
		{"ms", MIME_MS},
		{"nc", MIME_NC},
		{"oda", MIME_ODA},
		{"pbm", MIME_PBM},
		{"pct", MIME_PCT},
		{"pdf", MIME_PDF},
		{"pgm", MIME_PGM},
		{"pic", MIME_PIC},
		{"pict", MIME_PICT},
		{"pls", MIME_PLS},
		{"png", MIME_PNG},
		{"pnm", MIME_PNM},
		{"pnt", MIME_PNT},
		{"ppm", MIME_PPM},
		{"ps", MIME_PS},
		{"psd", MIME_PSD},
		{"qt", MIME_QT},
		{"qti", MIME_QTI},
		{"qtif", MIME_QTIF},
		{"ras", MIME_RAS},
		{"rgb", MIME_RGB},
		{"rm", MIME_RM},
		{"roff", MIME_ROFF},
		{"rtf", MIME_RTF},
		{"rtx", MIME_RTX},
		{"sh", MIME_SH},
		{"shar", MIME_SHAR},
		{"smf", MIME_SMF},
		{"sit", MIME_SIT},
		{"snd", MIME_SND},
		{"src", MIME_SRC},
		{"sv4cpio", MIME_SV4CPIO},
		{"sv4crc", MIME_SV4CRC},
		{"swf", MIME_SWF},
		{"t", MIME_T},
		{"tar", MIME_TAR},
		{"tgz", MIME_TGZ},
		{"tcl", MIME_TCL},
		{"tex", MIME_TEX},
		{"texi", MIME_TEXI},
		{"texinfo", MIME_TEXINFO},
		{"tif", MIME_TIF},
		{"tiff", MIME_TIFF},
		{"tr", MIME_TR},
		{"tsv", MIME_TSV},
		{"txt", MIME_TXT},
		{"ulw", MIME_ULW},
		{"ustar", MIME_USTAR},
		{"xbm", MIME_XBM},
		{"xml", MIME_XML},
		{"xpm", MIME_XPM},
		{"xsl", MIME_XSL},
		{"xwd", MIME_XWD},
		{"wav", MIME_WAV},
		{"wav", "audio/wav"},
		{"wma", MIME_WMA},
		{"wmv", MIME_WMV},
		{"svg", MIME_SVG},
		{"svgz", MIME_SVGZ},
		{"wbmp", MIME_WBMP},
		{"wml", MIME_WML},
		{"wmlc", MIME_WMLC},
		{"wmls", MIME_WMLS},
		{"wmlscriptc", MIME_WMLSCRIPTC},
		{"wrl", MIME_WRL},
		{"Z", MIME_Z},
		{"zip", MIME_ZIP},
		{"zip", "application/x-zip-compressed"},
		{"xls", MIME_XLS},
		{"xls", "application/x-excel"},
		{"xlsx", MIME_XLSX},
		{"ppt", MIME_PPT},
		{"pptx", MIME_PPTX},
		{"pps", MIME_PPS},
		{"ppsx", MIME_PPSX},
		{"rar", MIME_RAR},
		{"ico", MIME_ICO},
		{"dll", MIME_DLL}
	};
	
	/**
	 * 根据文件名获得MIME类型
	 * @param fileName
	 * @return
	 */
	public static String getMimeType(String fileName) {
		int index = fileName.lastIndexOf('.');
		if(index==-1) {
			return MIME_STREAM;
		}
		String extension = fileName.substring(index+1).toLowerCase();
		for(int i = 0; i < mimeMapping.length; i++) {
			if(mimeMapping[i][0].equalsIgnoreCase(extension)) {
				return mimeMapping[i][1];
			}
		}
		return MIME_STREAM;
	}
	
	/**
	 * 根据MIME类型获得文件类型
	 * @param fileName
	 * @return
	 */
	public static List listFileTypes(String mimeType) {
		List fileTypes = new ArrayList();
		mimeType = mimeType.trim();
		for(int i = 0; i < mimeMapping.length; i++) {
			if(mimeMapping[i][1].equalsIgnoreCase(mimeType)) {
				fileTypes.add(mimeMapping[i][0]);
			}
		}
		return fileTypes.isEmpty() ? null : fileTypes;
	}
}