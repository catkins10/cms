package com.yuanluesoft.jeaf.pdf.itext;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.pdf.PdfService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PdfServiceImpl implements PdfService {
	private HTMLParser htmlParser; //HTML解析器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.pdf.PdfService#htmlToPdf(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.itextpdf.text.Rectangle, java.lang.String, java.io.OutputStream)
	 */
	public void htmlToPdf(String html, String footerHtml, String pageHeaderHTML, String pageFooterHTML, Rectangle pageSize, String pageMargins, OutputStream pdfOutputStream) throws ServiceException {
		try {
			Document document;
			pageSize = pageSize==null ? PageSize.A4 : pageSize;
			if(pageMargins==null) {
				document = new Document(pageSize);
			}
			else {
				String[] margins = pageMargins.split(",");
				document = new Document(pageSize, Float.parseFloat(margins[0]), Float.parseFloat(margins[1]), Float.parseFloat(margins[2]), Float.parseFloat(margins[3]));
			}
			PdfWriter writer = PdfWriter.getInstance(document, pdfOutputStream);
			//页面事件处理
			PdfPageEventListener pageEventListener = new PdfPageEventListener(pageHeaderHTML, pageFooterHTML);
	        writer.setPageEvent(pageEventListener);
	        document.open();
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()), null, Charset.forName("utf-8"));
			if(footerHtml!=null) {
				/*
				PdfPTable table = new PdfPTable(1);
				table.getDefaultCell().setBorder(0);
				table.setWidths(new int[]{24});
				table.getDefaultCell().setFixedHeight(74);
				table.addCell("");
				document.add(table); */
				XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(footerHtml.getBytes()), null, Charset.forName("utf-8"));
			}
			document.close();
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * PDF页面事件处理
	 * @author linchuan
	 *
	 */
	private class PdfPageEventListener extends PdfPageEventHelper {
		private String pageHeaderHTML; //页眉HTML
		private String pageFooterHTML; //也脚HTML
	    private List pageTotalTemplates = null; //总页数模板
		private float lastPageHeight; //最后一页的内容高度
		
	    public PdfPageEventListener() {
			super();
		}

		public PdfPageEventListener(String pageHeaderHTML, String pageFooterHTML) {
			super();
			this.pageHeaderHTML = pageHeaderHTML;
			this.pageFooterHTML = pageFooterHTML;
		}

		/*
		 * (non-Javadoc)
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
		 */
		public void onEndPage(PdfWriter writer, Document document) {
			lastPageHeight = document.top() - ((PdfDocument)document).getVerticalPosition(false);
			System.out.println("*************currentHeight:" + ((PdfDocument)document).top() + "," + lastPageHeight + "," + ((PdfDocument)document).getVerticalPosition(false));
			try {
				writePageHeaderFooter(writer, document, pageHeaderHTML, true); //输出页眉
				writePageHeaderFooter(writer, document, pageFooterHTML, false); //输出页脚
			}
			catch(DocumentException de){
				throw new ExceptionConverter(de);
			}
		}
		
		/**
		 * 输出页面或者页脚
		 * @param writer
		 * @param document
		 * @param html
		 * @param isHeader
		 */
		private void writePageHeaderFooter(PdfWriter writer, Document document, String html, boolean isHeader) throws DocumentException {
			if(html==null) { //没有页面或页脚
				return;
			}
			//解析HTML
			HTMLDocument htmlDocument;
			try {
				htmlDocument = htmlParser.parseHTMLString(html, "utf-8");
			}
			catch (ServiceException e) {
				throw new DocumentException(e);
			}
			
			//获取表格
			HTMLTableElement htmlTable = (HTMLTableElement)htmlDocument.getElementsByTagName("table").item(0);
			HTMLCollection htmlTableCells = ((HTMLTableRowElement)htmlTable.getRows().item(0)).getCells();

			//获取宽度
			float width;
			String property = StringUtils.getStyle(htmlTable.getAttribute("style"), "width");
			if(property==null || property.isEmpty()) {
				property = htmlTable.getWidth();
			}
			if(property==null || property.isEmpty()) {
				property = "100%";
			}
			if(property.endsWith("%")) {
				width = (document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / 100.0f * Float.parseFloat(property.substring(0, property.length()-1)); 
			}
			else {
				width = Float.parseFloat(property.replace("px", ""));
			}
			//获取高度
			float height;
			property = StringUtils.getStyle(htmlTable.getAttribute("style"), "height");
			if(property==null || property.isEmpty()) {
				property = htmlTable.getAttribute("height");
			}
			if(property!=null && property.endsWith("px")) {
				height = Float.parseFloat(property.replace("px", ""));
			}
			else {
				height = 30;
			}
			//获取每列宽度
			float[] cellWidths = new float[htmlTableCells.getLength()];
			int widthNotSet = 0;
			for(int i=0; i<htmlTableCells.getLength(); i++) {
				HTMLTableCellElement cell = (HTMLTableCellElement)htmlTableCells.item(i);
				property = StringUtils.getStyle(cell.getAttribute("style"), "width");
				if(property==null || property.isEmpty()) {
					property = cell.getWidth();
				}
				if(property==null || property.isEmpty()) {
					widthNotSet++;
					cellWidths[i] = -1;
				}
				else if(property.endsWith("%")) {
					cellWidths[i] = width / 100.0f * Float.parseFloat(property.substring(0, property.length()-1));
				}
				else {
					cellWidths[i] = Float.parseFloat(property.replace("px", ""));
				}
			}
			for(int i=0; widthNotSet>0 && i<htmlTableCells.getLength(); i++) {
				if(cellWidths[i]==-1) {
					cellWidths[i] = width / widthNotSet;
				}
			}
			
			//创建PDF表格
			PdfPTable table = new PdfPTable(htmlTableCells.getLength());
			table.setTotalWidth(width); //设置表格宽度
			table.setWidths(cellWidths); //设置每列宽度
			table.setLockedWidth(true);
			table.getDefaultCell().setFixedHeight(height);
			
			//添加内容
			for(int i=0; i<htmlTableCells.getLength(); i++) {
				HTMLTableCellElement cell = (HTMLTableCellElement)htmlTableCells.item(i);
				//创建PDF单元格
				PdfPCell pdfCell = new PdfPCell(); //Image.getInstance(total)
				pdfCell.setFixedHeight(height);
				String valign = cell.getVAlign();
				pdfCell.setVerticalAlignment(valign==null || valign.isEmpty() || valign.indexOf("middle")!=-1 ? PdfPCell.ALIGN_MIDDLE : (valign.equals("top") ? PdfPCell.ALIGN_TOP : PdfPCell.ALIGN_BOTTOM)); //垂直方向对齐方式
				String align = cell.getAlign();
				pdfCell.setHorizontalAlignment(align==null || align.isEmpty() || align.indexOf("left")!=-1 ? PdfPCell.ALIGN_LEFT : (align.equals("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_RIGHT)); //水平方向对齐方式
				int border = 0;
				//设置做边框
				String borderStyle = StringUtils.getStyle(cell.getAttribute("style"), "border-left-style");
				if(borderStyle!=null && !borderStyle.isEmpty() && !"none".equals(borderStyle)) {
					border |= Rectangle.LEFT;
					String borderWidth = StringUtils.getStyle(cell.getAttribute("style"), "border-left-width");
					pdfCell.setBorderWidthLeft(borderWidth==null || borderWidth.isEmpty() ? 1 : Float.parseFloat(borderWidth.replace("px", "")));
					pdfCell.setBorderColorLeft(createColor(StringUtils.getStyle(cell.getAttribute("style"), "border-left-color")));
				}
				//设置右边框
				borderStyle = StringUtils.getStyle(cell.getAttribute("style"), "border-right-style");
				if(borderStyle!=null && !borderStyle.isEmpty() && !"none".equals(borderStyle)) {
					border |= Rectangle.RIGHT;
					String borderWidth = StringUtils.getStyle(cell.getAttribute("style"), "border-right-width");
					pdfCell.setBorderWidthRight(borderWidth==null || borderWidth.isEmpty() ? 1 : Float.parseFloat(borderWidth.replace("px", "")));
					pdfCell.setBorderColorRight(createColor(StringUtils.getStyle(cell.getAttribute("style"), "border-right-color")));
				}
				//设置上边框
				borderStyle = StringUtils.getStyle(cell.getAttribute("style"), "border-top-style");
				if(borderStyle!=null && !borderStyle.isEmpty() && !"none".equals(borderStyle)) {
					border |= Rectangle.TOP;
					String borderWidth = StringUtils.getStyle(cell.getAttribute("style"), "border-top-width");
					pdfCell.setBorderWidthTop(borderWidth==null || borderWidth.isEmpty() ? 1 : Float.parseFloat(borderWidth.replace("px", "")));
					pdfCell.setBorderColorTop(createColor(StringUtils.getStyle(cell.getAttribute("style"), "border-top-color")));
				}
				//设置下边框
				borderStyle = StringUtils.getStyle(cell.getAttribute("style"), "border-bottom-style");
				if(borderStyle!=null && !borderStyle.isEmpty() && !"none".equals(borderStyle)) {
					border |= Rectangle.BOTTOM;
					String borderWidth = StringUtils.getStyle(cell.getAttribute("style"), "border-bottom-width");
					pdfCell.setBorderWidthBottom(borderWidth==null || borderWidth.isEmpty() ? 1 : Float.parseFloat(borderWidth.replace("px", "")));
					pdfCell.setBorderColorBottom(createColor(StringUtils.getStyle(cell.getAttribute("style"), "border-bottom-color")));
				}
				pdfCell.setBorder(border);
				//获取字体
				String fontName = StringUtils.getStyle(cell.getAttribute("style"), "font-family");
				String fontSize = StringUtils.getStyle(cell.getAttribute("style"), "font-size");
				String fontColor = StringUtils.getStyle(cell.getAttribute("style"), "color");
				String fontWeight = StringUtils.getStyle(cell.getAttribute("style"), "font-weight");
				String fontStyle = StringUtils.getStyle(cell.getAttribute("style"), "font-style");
				if(fontName==null || fontName.isEmpty()) {
					fontName = StringUtils.getStyle(htmlTable.getAttribute("style"), "font-family");
				}
				if(fontSize==null || fontSize.isEmpty()) {
					fontSize = StringUtils.getStyle(htmlTable.getAttribute("style"), "font-size");
				}
				if(fontColor==null || fontColor.isEmpty()) {
					fontColor = StringUtils.getStyle(htmlTable.getAttribute("style"), "color");
				}
				if(fontWeight==null || fontWeight.isEmpty()) {
					fontWeight = StringUtils.getStyle(htmlTable.getAttribute("style"), "font-weight");
				}
				if(fontStyle==null || fontStyle.isEmpty()) {
					fontStyle = StringUtils.getStyle(htmlTable.getAttribute("style"), "font-style");
				}
				Paragraph paragraph = new Paragraph(htmlParser.getTextContent(cell), createFont(fontName, fontSize, fontColor, "bold".equals(fontWeight), "italic".equals(fontStyle)));
				pdfCell.addElement(paragraph);
				table.addCell(pdfCell);
			}
			table.writeSelectedRows(0, -1, 34, document.getPageSize().getHeight(), writer.getDirectContent());
		}

		/**
		 * 创建颜色,只支持#ffffff格式
		 * @param htmlColor
		 * @return
		 * @throws Exception
		 */
		private BaseColor createColor(String htmlColor) {
			if(htmlColor==null || !htmlColor.startsWith("#") || htmlColor.length()!=7) {
				return BaseColor.BLACK;
			}
			return new BaseColor(Integer.parseInt(htmlColor.substring(1, 3), 16), Integer.parseInt(htmlColor.substring(3, 5), 16), Integer.parseInt(htmlColor.substring(5, 7), 16));
		}

		/**
		 * 创建字体
		 * @param font
		 * @param size
		 * @param color
		 * @param bold
		 * @param italic
		 * @return
		 */
		private Font createFont(String font, String size, String color, boolean bold, boolean italic) {
			System.out.println("*********font: " + font + "," + size + "," + color);
			if(font.isEmpty() || "宋体".equals(font)) {
				font = "宋体.ttc,0";
			}
			else {
				font += ".ttf";
				if(!FileUtils.isExists(Environment.getWebinfPath() + "jeaf/fonts/" + font)) {
					font = "宋体.ttc,0";
				}
			}
			BaseFont baseFont = null;
			try {
				baseFont = BaseFont.createFont(Environment.getWebinfPath() + "jeaf/fonts/" + font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			} 
			catch (Exception e) {
				Logger.exception(e);
			}
			float fontSize = size==null || size.isEmpty() ? 12 : Float.parseFloat(size.replace("px", ""));
			return new Font(baseFont, fontSize, (bold ? Font.BOLD : Font.NORMAL) | (italic ? Font.ITALIC : Font.NORMAL), createColor(color));
		}

		/*
		 * (non-Javadoc)
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
		 */
	    public void onCloseDocument(PdfWriter writer,Document document){
	    	if(pageTotalTemplates!=null) {
	    		//ColumnText.showTextAligned(pageTotalTemplate, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber()-1)), 2, 2, 0);
	    	}
	    }

		public float getLastPageHeight() {
			return lastPageHeight;
		}
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}
}