package com.yuanluesoft.jeaf.pdf;

import java.io.OutputStream;

import com.itextpdf.text.Rectangle;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface PdfService {

	/**
	 * HTML转换为PDF
	 * @param html 内容HTML
	 * @param footerHtml 尾部HTML
	 * @param pageHeaderHTML 页眉HTML,格式:<table style="..."><tr><td>左</td><td>中</td><td>右</td></tr></table>
	 * @param pageFooterHTML 页脚HTML,格式:<table style="..."><tr><td>左</td><td>中</td><td>右</td></tr></table>
	 * @param pageSize 页面大小,默认: PageSize.A4
	 * @param pageMargins 页面边距,格式:左边距,右边距,上边距,下边距,如:36,36,36,36
	 * @param pdfOutputStream
	 * @throws ServiceException
	 */
	public void htmlToPdf(String html, String footerHtml, String pageHeaderHTML, String pageFooterHTML, Rectangle pageSize, String pageMargins, OutputStream pdfOutputStream) throws ServiceException;
}