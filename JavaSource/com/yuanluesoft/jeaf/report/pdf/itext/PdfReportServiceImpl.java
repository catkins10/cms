/*
 * Created on 2005-3-5
 *
 */
package com.yuanluesoft.jeaf.report.pdf.itext;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.enhydra.xml.io.DOMFormatter;
import org.w3c.dom.html.HTMLDocument;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.html.HtmlParser;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.report.pdf.PdfReportService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.callback.FillParametersCallback;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;
import com.yuanluesoft.jeaf.view.util.ViewUtils;

/**
 * PDF报表服务, 视图参数如下:
 * 	<extendParameters>
 *		<parameter name="print.pageSize">A4</parameter> <!-- PDF:页面大小 -->
 *		<parameter name="print.horizontal">true</parameter> <!-- PDF:是否横向页面 -->
 *		<parameter name="print.pageMargin">30,30,30,30</parameter> <!-- PDF:页面边距,格式:上/右/下/左 -->
 *		<parameter name="print.title">{PARAMETER:unitName}{PARAMETER:year}年政府信息公开目录</parameter> <!-- PDF:标题 -->
 *		<parameter name="print.titleFirstPage">false</parameter> <!-- PDF:标题是否只在首页显示 -->
 *		<parameter name="print.font">宋体,12</parameter> <!-- PDF:字体, 格式:名称,大小 -->
 *		<parameter name="print.titleFont">宋体,18</parameter> <!-- PDF:标题字体, 格式:名称,大小 -->
 *		<parameter name="print.pageHeaderFont">宋体,12</parameter> <!-- PDF:页眉字体, 格式:名称,大小 -->
 *		<parameter name="print.pageFooterFont">宋体,12</parameter> <!-- PDF:页脚字体, 格式:名称,大小 -->
 *		<parameter name="print.pageHeader">{PARAMETER:unitName}\0\0{PARAMETER:pageNumber}/{PARAMETER:pageCount}</parameter> <!-- PDF:页眉, 格式:左\0中\0右 -->
 *		<parameter name="print.pageFooter">打印时间:{TODAY}\0第 {PARAMETER:pageNumber} 页 共 {PARAMETER:pageCount} 页\0打印:{USERNAME}"</parameter> <!-- PDF:页脚, 格式:左\0中\0右 -->
 *	</extendParameters>
 * @author LinChuan
 * 
 */
public class PdfReportServiceImpl implements PdfReportService {
	private HTMLParser htmlParser;
	private TemporaryFileManageService temporaryFileManageService; //临时文件管理
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.report.pdf.PdfReportService#createPdfReport(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void createPdfReport(View view, String currentCategories, String searchConditions, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		try {
			//获取标题
			String title = (String)view.getExtendParameter("print.title");
			if(title!=null && !title.isEmpty()) {
				title = StringUtils.fillParameters(title, false, false, false, "utf-8", null, request, null);
			}
			else {
				title = view.getTitle();
			}
	    	response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", (!"true".equals(request.getParameter("asAttachment")) ? "inline" : "attachment") + "; filename=\"" + FileUtils.encodeFileName(title + ".pdf", "utf-8") + "\"");
			
			//创建字体:宋体
			Font font = createFont((String)view.getExtendParameter("print.font"), false); //内容字体
			Font fontHead = createFont((String)view.getExtendParameter("print.font"), true); //表头字体
			
			//设置页面大小
			String pageSize = (String)view.getExtendParameter("print.pageSize");
			Rectangle pageSizeRectangle = PageSize.getRectangle(pageSize==null ? "A4" : pageSize);
			
			//设置边距
			String pageMargin = (String)view.getExtendParameter("print.pageMargin");
			String[] margins = {"50", "50", "50", "50"};
			if(pageMargin!=null && !pageMargin.isEmpty()) {
				margins = pageMargin.split(",");
			}
			
			//创建PDF
			Document pdfDocument = new Document(("true".equals(view.getExtendParameter("print.horizontal")) ? pageSizeRectangle.rotate() : pageSizeRectangle),
												 Float.parseFloat(margins[3]), //左边距
												 Float.parseFloat(margins[1]), //右边距
												 Float.parseFloat(margins[0]), //上边距
												 Float.parseFloat(margins[2])); //下边距
			PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, response.getOutputStream());
			
			//页眉
			String pageHeader = (String)view.getExtendParameter("print.pageHeader");
			if(pageHeader!=null) {
				String[] headers = pageHeader.split("\\\\0");
				HeaderFooter header = new PageCountSupportHeaderFooter(pdfDocument, pdfWriter, headers[0], (headers.length>1 ? headers[1] : null), (headers.length>2 ? headers[2] : null), true, createFont((String)view.getExtendParameter("print.pageHeaderFont"), false), 10, request);
				header.setBorder(Rectangle.NO_BORDER);
				pdfDocument.setHeader(header);
			}
			
			//页脚
			String pageFooter = (String)view.getExtendParameter("print.pageFooter");
			if(pageHeader!=null) {
				String[] footers = pageFooter.split("\\\\0");
				HeaderFooter footer = new PageCountSupportHeaderFooter(pdfDocument, pdfWriter, footers[0], (footers.length>1 ? footers[1] : null), (footers.length>2 ? footers[2] : null), false, createFont((String)view.getExtendParameter("print.pageFooterFont"), false), 8, request);
				footer.setBorder(Rectangle.NO_BORDER);
				//footer.setAlignment(Element.ALIGN_CENTER);
				pdfDocument.setFooter(footer);
			}
			
			//打开文档
			pdfDocument.open();
			
			//输出标题
			Paragraph titleParagraph = new Paragraph(title, createFont((String)view.getExtendParameter("print.titleFont"), true));
			titleParagraph.setAlignment(Element.ALIGN_CENTER);
			titleParagraph.setLeading(6);
			pdfDocument.add(titleParagraph);
			
			//显示数据
			float[] columnWidths = null;
			Table table = null;
			
			//获取业务逻辑定义
			ViewService viewService = ViewUtils.getViewService(view);
			ViewPackage viewPackage = new ViewPackage();
			view.setPageRows(100);
			viewPackage.setView(view);
			viewPackage.setSearchConditions(searchConditions);
			viewPackage.setSearchMode(searchConditions!=null);
			viewPackage.setCategories(currentCategories);
			viewPackage.setViewMode(View.VIEW_DISPLAY_MODE_PRINT);
			int rowIndex = 1;
			for(int currentPage = 1; ; currentPage++) {
				viewPackage.setCurPage(currentPage);
				viewService.retrieveViewPackage(viewPackage, view, 0, true, false, false, request, sessionInfo);
				if(table==null) { //创建表格
					columnWidths = getColumnWidths(pdfDocument, view);
					table = createTable(view, columnWidths, fontHead);
				}
				if(viewPackage.getRecords()==null || viewPackage.getRecords().isEmpty()) {
					break;
				}
				for(Iterator iteratorRecord = viewPackage.getRecords().iterator(); iteratorRecord.hasNext();) {
					Object record = iteratorRecord.next();
					addRow(table, view, record, rowIndex, font, request);
					if(!pdfWriter.fitsPage(table)) { //一页已经满了
						table.deleteLastRow();
						pdfDocument.add(table);
						pdfDocument.newPage();
						if(!"true".equals(view.getExtendParameter("print.titleFirstPage"))) { //不是在第一页显示标题
							pdfDocument.add(titleParagraph);
						}
						table = createTable(view, columnWidths, fontHead);
						pdfDocument.add(new Paragraph(" ", createFont("宋体,1", false)));
						addRow(table, view, record, rowIndex, font, request);
					}
					rowIndex++;
				}
				if(currentPage>=viewPackage.getPageCount()) {
					break;
				}
			}
			pdfDocument.add(table);
			pdfDocument.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
	
	/**
	 * 创建字体
	 * @param font
	 * @param bold
	 * @return
	 * @throws Exception
	 */
	private Font createFont(String font, boolean bold) throws Exception {
		String[] fontParameters = (font==null || font.isEmpty() ? "宋体,12" : font).split(",");
		if(fontParameters[0].isEmpty() || "宋体".equals(fontParameters[0])) {
			fontParameters[0] = "宋体.ttc,0";
		}
		else {
			fontParameters[0] += ".ttf";
		}
		BaseFont baseFont = BaseFont.createFont(Environment.getWebinfPath() + "jeaf/fonts/" + fontParameters[0], BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		return new Font(baseFont, Float.parseFloat(fontParameters[1]), (bold ? Font.BOLD : Font.NORMAL));
	}
	
	/**
	 * 创建表格并设置标头
	 * @param columns
	 * @param widths
	 * @return
	 * @throws DocumentException
	 */
	private Table createTable(View view, float[] widths, Font font) throws DocumentException {
		List columns = view.getColumns();
		Table table = new Table(widths.length);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setBorderWidth(1);
		table.setPadding(3);
		table.setSpacing(0);
		table.setWidth(100);
		table.setWidths(widths);
		
		int rowCount = 1;
		boolean multiHeadRows = (view instanceof StatisticView && (rowCount=((StatisticView)view).getHeadRowCount())>1);
		for(Iterator iteratorColumn = columns.iterator(); iteratorColumn.hasNext();) {
			Column column = (Column)iteratorColumn.next();
			Cell cell = new Cell(new Chunk(column.getTitle(), font));
			if(multiHeadRows) {
				List groupValues = ((com.yuanluesoft.jeaf.view.statisticview.model.Column)column).getGroupValues();
				if(groupValues==null) {
					cell.setRowspan(rowCount);
				}
				else {
					cell.setColspan(groupValues.size());
				}
			}
			cell.setHeader(true);
			table.addCell(cell);
		}
		if(multiHeadRows) {
			for(Iterator iteratorColumn = columns.iterator(); iteratorColumn.hasNext();) {
				com.yuanluesoft.jeaf.view.statisticview.model.Column column = (com.yuanluesoft.jeaf.view.statisticview.model.Column)iteratorColumn.next();
				if(column.getGroupValues()!=null) {
					for(Iterator iterator = column.getGroupValues().iterator(); iterator.hasNext();) {
						Cell cell = new Cell(new Chunk("" + iterator.next(), font));
						cell.setHeader(true);
						table.addCell(cell);
					}
				}
			}
		}
		table.endHeaders();
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		return table;
	}
	
	/**
	 * 添加行
	 * @param table
	 * @param view
	 * @param record
	 * @param businessObject
	 * @param rowIndex
	 * @param font
	 * @param request
	 * @throws Exception
	 */
	private void addRow(Table table, View view, Object record, int rowIndex, Font font, HttpServletRequest request) throws Exception {
		List columns = view.getColumns();
		StatisticView statisticView = (view instanceof StatisticView ? (StatisticView)view : null);
		int startColumn = 0;
		if(statisticView!=null) {
			String statisticTitle = (record instanceof Record ? (String)((Record)record).getExtendPropertyValue("statisticTitle") : null);
			if(statisticTitle!=null) {
				Cell cell = new Cell(new Chunk((String)statisticTitle, font));
				startColumn = statisticView.getHeadColCount();
				cell.setColspan(startColumn);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
			}
		}
		int columnCount = columns.size();
		for(int i=startColumn; i<columnCount; i++) {
			Column column = (Column)columns.get(i);
			if("rownum".equals(column.getType())) { //序号
				Cell cell = new Cell(new Chunk("" + rowIndex, font));
				cell.setHorizontalAlignment(column.getAlign());
				table.addCell(cell);
				continue;
			}
			//字段
			List groupValues;
			if( statisticView!=null &&
				(groupValues = ((com.yuanluesoft.jeaf.view.statisticview.model.Column)column).getGroupValues())!=null) {
				Object value = PropertyUtils.getProperty(record, column.getName());
				if(value==null) {
					for(Iterator iterator = groupValues.iterator(); iterator.hasNext();) {
						iterator.next();
						Cell cell = new Cell(new Chunk("", font));
						table.addCell(cell);
					}
				}
				else {
					for(Iterator iterator = ((List)value).iterator(); iterator.hasNext();) {
						Cell cell = new Cell(new Chunk("" + iterator.next(), font));
						cell.setHorizontalAlignment(column.getAlign());
						table.addCell(cell);
					}
				}
			}
			else {
				String value = ViewUtils.getViewFieldValue(view, column, record, false, request);
				if(column.isHideZero() && "0".equals(value)) { //隐藏"0"
					value = "";
				}
				if(column.getMaxCharCount()>0) { //有字数限制
					value = StringUtils.slice(value, column.getMaxCharCount(), column.getEllipsis());
				}
				Cell cell = new Cell(new Chunk(value, font));
				cell.setHorizontalAlignment(column.getAlign());
				table.addCell(cell);
			}
		}
	}
	
	/**
	 * 获取列宽
	 * @param document
	 * @param columns
	 * @return
	 */
	private float[] getColumnWidths(Document document, View view) {
		float pageWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
		List widthList = new ArrayList();
		float totalWidth = 0;
		int count = 0;
		boolean multiHeadRows = (view instanceof StatisticView && ((StatisticView)view).getHeadRowCount()>1);
		for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			int colSpan = 1;
			if(multiHeadRows) {
				List groupValues = ((com.yuanluesoft.jeaf.view.statisticview.model.Column)column).getGroupValues();
				colSpan = (groupValues==null ? 1 : groupValues.size());
			}
			String widthDefine = column.getWidth();
			if(widthDefine==null) {
				count += colSpan;
				for(int i=0; i<colSpan; i++) {
					widthList.add(null);
				}
			}
			else {
				float width;
				if(widthDefine.lastIndexOf('%')==-1) {
					width = new Float(widthDefine.replaceAll("px", "")).floatValue()/pageWidth * 100;
				}
				else {
					width = new Float(widthDefine.substring(0, widthDefine.length() - 1)).floatValue();
				}
				totalWidth += width;
				for(int i=0; i<colSpan; i++) {
					widthList.add(new Float(width/colSpan));
				}
			}
		}
		float widthOther = (100 - totalWidth)/count; //未指定尺寸的列,平均分配剩余的宽度
		float[] widths = new float[widthList.size()];
		for(int i=widths.length - 1; i>=0; i--) {
			Object width = widthList.get(i);
			widths[i] = width==null ? widthOther : ((Float)widthList.get(i)).floatValue();
		}
		return widths;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.report.pdf.PdfReportService#htmlToPdf(java.lang.String, java.io.OutputStream)
	 */
	public void htmlToPdf(String htmlFileName, OutputStream outputStream) throws ServiceException {
		String temporaryDirectory = temporaryFileManageService.createTemporaryDirectory(null);
		try {
			//解析HTML文件
			HTMLDocument htmlDocument = htmlParser.parseHTMLFile(htmlFileName);
			//转换为XML
			DOMFormatter formatter = new DOMFormatter(DOMFormatter.getDefaultOutputOptions(htmlDocument));
			formatter.getOutputOptions().setEnableXHTMLCompatibility(true);
			formatter.getOutputOptions().setForceHTMLLowerCase(true);
			formatter.getOutputOptions().setFormat(org.enhydra.xml.io.OutputOptions.FORMAT_XML);
			formatter.getOutputOptions().setEncoding("utf-8");
			formatter.write(htmlDocument, new File(temporaryDirectory + "temp.xhtml"));
			//输出PDF
			Document document = new Document(PageSize.A4, 1, 1, 1, 1);   
			PdfWriter.getInstance(document, outputStream);
			HtmlParser.parse(document, temporaryDirectory + "temp.xhtml");
			document.close();
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			FileUtils.deleteDirectory(temporaryDirectory);
		}
	}
	
	/**
	 * 支持总页数输出的页眉页脚, 注：{PARAMETER:pageNumber}必须在{PARAMETER:pageCount}之前,页眉页脚中只能配置一个{PARAMETER:pageCount}
	 * @author LinChuan
	 * 
	 */
	public class PageCountSupportHeaderFooter extends HeaderFooter implements DocListener {
		private Document pdfDocument; //文档
		private	PdfWriter pdfWriter; //PDF输出
		private HttpServletRequest request;
		private String leftContent; //左边内容
		private String centerContent; //中间内容
		private String rightContent; //右边内容
		private boolean isHeader; //是否页眉
		private float leading; //间距
		private Font font; //字体
		
		private PdfContentByte pdfContentByte;
		private String pageCountText; //{PARAMETER:pageCount}后的文本
		private PdfTemplate pageCountTemplate = null; //总页数模板
		
		public PageCountSupportHeaderFooter(Document pdfDocument, PdfWriter pdfWriter, String leftContent, String centerContent, String rightContent, boolean isHeader, Font font, float leading, HttpServletRequest request) {
			super(new Phrase(" ", font), true);
			this.pdfDocument = pdfDocument;
			this.pdfWriter = pdfWriter;
			this.leftContent = leftContent;
			this.centerContent = centerContent;
			this.rightContent = rightContent;
			this.leading = leading;
			this.isHeader = isHeader;
			this.font = font;
			this.request = request;
			this.pdfDocument.addDocListener(this);
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.HeaderFooter#paragraph()
		 */
		public Paragraph paragraph() {
			if(font==null) {
				Paragraph paragraph = super.paragraph();
				paragraph.setLeading(8); //行距
				return paragraph;
			}
			Paragraph paragraph = new Paragraph();
			paragraph.setLeading(leading); //行距
			paragraph.add(new Chunk(generateHeaderFooterContent(false), font));
			return paragraph;
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#open()
		 */
		public void open() {
			pdfContentByte = pdfWriter.getDirectContent();
			//初始化总页数模板
			String[] contents = {leftContent, centerContent, rightContent};
			for(int i=0; i<contents.length; i++) {
				if(contents[i]==null || contents[i].isEmpty()) {
					continue;
				}
				int pagesIndex = contents[i].indexOf("{PARAMETER:pageCount}");
				if(pagesIndex!=-1) {
					pageCountText = fillParameters(contents[i].substring(pagesIndex + "{PARAMETER:pageCount}".length()));
					pageCountTemplate = pdfContentByte.createTemplate(new Float(font.getBaseFont().getWidthPoint("9999" + pageCountText, font.getSize())).intValue(), (isHeader ? pdfDocument.top() : pdfDocument.bottom()));
					addPageCountTemplate(); //添加模板
					return;
				}
			}
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#newPage()
		 */
		public boolean newPage() {
			if(pageCountTemplate!=null) {
				addPageCountTemplate(); //添加模板
			}
			return false;
		}
		
		/**
		 * 添加模板
		 *
		 */
		private void addPageCountTemplate() {
			float left = font.getBaseFont().getWidthPoint(generateHeaderFooterContent(true), font.getSize());
			pdfContentByte.addTemplate(pageCountTemplate, pdfDocument.left() + left, isHeader ? pdfDocument.top() : pdfDocument.bottom()); //在页脚添加总页数模板
			//输出总页数
			pageCountTemplate.reset();
			pageCountTemplate.beginText();
			pageCountTemplate.setFontAndSize(font.getBaseFont(), 12);
			pageCountTemplate.setWidth(300);
			pageCountTemplate.showText(pdfWriter.getPageNumber() + pageCountText);
			pageCountTemplate.endText();
		}
		
		/**
		 * 生成页眉（页脚）内容
		 * @param leftOnly
		 * @return
		 */
		private String generateHeaderFooterContent(boolean leftOnly) {
			String headerFooterContent = "";
			String[] contents = {leftContent, centerContent, rightContent};
			for(int i=0; i<contents.length; i++) {
				if(contents[i]==null || contents[i].isEmpty()) {
					continue;
				}
				String content = fillParameters(contents[i]);
				int pageCountIndex = content.indexOf("{PARAMETER:pageCount}");
				double blankWidth = font.getBaseFont().getWidthPoint(" ", font.getSize()); //空格宽度
				if(pageCountIndex!=-1) { //替换为空格
					double width = font.getBaseFont().getWidthPoint("9999" + content.substring(pageCountIndex + "{PARAMETER:pageCount}".length()), font.getSize());
					content = content.substring(0, pageCountIndex);
					for(int j=0; j < width/blankWidth-1; j++) {
						content += " ";
					}
				}
				//插入空格,以保证居中或者局右
				String blanks = "";
				if(i>0) { //不是居左
					double leftWidth  = (pdfDocument.getPageSize().getWidth() - pdfDocument.leftMargin() - pdfDocument.rightMargin()) - font.getBaseFont().getWidthPoint(content, font.getSize());
					if(i==1) { //居中
						leftWidth /= 2;
					}
					leftWidth -= font.getBaseFont().getWidthPoint(headerFooterContent, font.getSize());
					for(int j=0; leftWidth>0 && j<leftWidth / blankWidth - 1; j++) {
						blanks += " ";
					}
				}
				headerFooterContent += blanks;
				if(pageCountIndex!=-1 && leftOnly) {
					headerFooterContent += content.substring(0, pageCountIndex);
					break;
				}
				headerFooterContent += content;
			}
			return headerFooterContent;
		}
		
		/**
		 * 填充参数
		 * @param text
		 * @return
		 */
		private String fillParameters(String text) {
			return StringUtils.fillParameters(text, false, false, false, "utf-8", null, request, new FillParametersCallback() {
				/*
				 * (non-Javadoc)
				 * @see com.yuanluesoft.jeaf.util.callback.FillParametersCallback#getParameterValue(java.lang.String, java.lang.Object, javax.servlet.http.HttpServletRequest)
				 */
				public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request) {
					if("pageNumber".equals(parameterName)) { //页码
						return pdfWriter.getPageNumber() + "";
					}
					else if("pageCount".equals(parameterName)) { //总页数
						return "{PARAMETER:pageCount}"; //维持原样
					}
					return null;
				}
			});
		}
		
		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#close()
		 */
		public void close() {
			
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#resetFooter()
		 */
		public void resetFooter() {
			
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#resetHeader()
		 */
		public void resetHeader() {
			
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#resetPageCount()
		 */
		public void resetPageCount() {
			
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#setFooter(com.lowagie.text.HeaderFooter)
		 */
		public void setFooter(HeaderFooter footer) {
			
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#setHeader(com.lowagie.text.HeaderFooter)
		 */
		public void setHeader(HeaderFooter header) {
			
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#setMarginMirroring(boolean)
		 */
		public boolean setMarginMirroring(boolean marginMirroring) {
			return false;
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#setMargins(float, float, float, float)
		 */
		public boolean setMargins(float arg0, float arg1, float arg2, float arg3) {
			return false;
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#setPageCount(int)
		 */
		public void setPageCount(int pageCount) {
			
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.DocListener#setPageSize(com.lowagie.text.Rectangle)
		 */
		public boolean setPageSize(Rectangle rectangle) {
			return false;
		}

		/* (non-Javadoc)
		 * @see com.lowagie.text.ElementListener#add(com.lowagie.text.Element)
		 */
		public boolean add(Element element) throws DocumentException {
			return false;
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

	/**
	 * @return the temporaryFileManageService
	 */
	public TemporaryFileManageService getTemporaryFileManageService() {
		return temporaryFileManageService;
	}

	/**
	 * @param temporaryFileManageService the temporaryFileManageService to set
	 */
	public void setTemporaryFileManageService(TemporaryFileManageService temporaryFileManageService) {
		this.temporaryFileManageService = temporaryFileManageService;
	}
}