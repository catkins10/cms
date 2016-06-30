package com.yuanluesoft.jeaf.util;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class MsWordUtils {

	/**
	 * 将word文件转换为html文件
	 * @param srcFileName
	 * @param destFileName
	 * @return
	 */
	public static boolean saveAsHtml(String docFilePath, String htmlFilePath) {
		if(Logger.isDebugEnabled()) {
			Logger.debug("MsWordUtils: save word document file " + docFilePath + " as html file " + htmlFilePath);
		}
		//启动word
		ActiveXComponent application = new ActiveXComponent("Word.Application");
		try {
			//设置word不可见
			application.setProperty("Visible", new Variant(false));
			//打开word文件
			Object docs = application.getProperty("Documents").toDispatch();
			Object doc = Dispatch.invoke((Dispatch)docs, "Open", Dispatch.Method, new Object[] {new File(docFilePath).getPath()}, new int[1]).toDispatch();
			//另存为html格式
			Dispatch.invoke((Dispatch)doc, "SaveAs", Dispatch.Method, new Object[] {new File(htmlFilePath).getPath(), new Variant(8)}, new int[1]);
			Dispatch.invoke((Dispatch)doc, "Close", Dispatch.Method, new Object[] {new Variant(false)}, new int[1]);
			return true;
		}
		catch (Exception e) {
			Logger.exception(e);
			return false;
		}
		finally {
			application.invoke("Quit", new Variant[]{});
		}
	}
}