package com.yuanluesoft.enterprise.exam.question.service.railway;

import java.io.File;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;

import com.yuanluesoft.enterprise.exam.question.pojo.Question;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionImport;
import com.yuanluesoft.enterprise.exam.question.service.QuestionImportService;
import com.yuanluesoft.enterprise.exam.question.service.QuestionService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.ZipUtils;

/**
 * 
 * @author linchuan
 *
 */
public class QuestionImportServiceImpl extends BusinessServiceImpl implements QuestionImportService {
	private AttachmentService attachmentService; //附件服务
	private HTMLParser htmlParser; //HTML解析器
	private QuestionService questionService; //题库服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.question.service.QuestionImportService#importData(long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public QuestionImport importData(long importLogId, String source, String description, String postIds, String posts, String knowledgeIds, String knowledges, String itemIds, String items, String remark, SessionInfo sessionInfo) throws ServiceException {
		List attachments = attachmentService.list("enterprise/exam/question", "data", importLogId, false, 1, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		String filePath = ((Attachment)attachments.get(0)).getFilePath();
		String dataPath = FileUtils.createDirectory(filePath.substring(0, filePath.lastIndexOf('/') + 1) + "unzip");
		try {
			ZipUtils.unZip(filePath, dataPath);
			File[] files = new File(dataPath).listFiles();
			for(int i=0; i<files.length; i++) {
				if("南昌铁路局题库".equals(source)) { //题目用两个句号分隔
					importNanchangQuestionFile(files[i], importLogId, source, postIds, posts, knowledgeIds, knowledges, itemIds, items, sessionInfo);
				}
				else if("福州动车段题库".equals(source)) {
					importFuzhouQuestionFile(files[i], importLogId, source, postIds, posts, knowledgeIds, knowledges, itemIds, items, sessionInfo);
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			FileUtils.deleteDirectory(dataPath);
		}
		//创建导入日志
		QuestionImport questionImport = new QuestionImport();
		questionImport.setId(importLogId);
		questionImport.setSource(source); //题库来源,如:南昌铁路局
		questionImport.setDescription(description); //说明,从文件名提取
		questionImport.setPostIds(postIds); //适用的岗位ID
		questionImport.setPosts(posts); //适用的岗位
		questionImport.setKnowledgeIds(knowledgeIds); //知识类别ID
		questionImport.setKnowledges(knowledges); //知识类别
		questionImport.setItemIds(itemIds); //项目分类ID
		questionImport.setItems(items); //项目分类
		questionImport.setCreator(sessionInfo.getUserName()); //创建人
		questionImport.setCreatorId(sessionInfo.getUserId()); //创建人ID
		questionImport.setCreated(DateTimeUtils.now()); //创建时间
		questionImport.setRemark(remark); //备注
		getDatabaseService().saveRecord(questionImport);
		return questionImport;
	}
	
	/**
	 * 导入南昌铁路局题库
	 * @param questionFile
	 * @param source
	 * @param postIds
	 * @param posts
	 * @param knowledgeIds
	 * @param knowledges
	 * @param itemIds
	 * @param items
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void importNanchangQuestionFile(File questionFile, long importLogId, String source, String postIds, String posts, String knowledgeIds, String knowledges, String itemIds, String items, SessionInfo sessionInfo) throws ServiceException {
		if(questionFile.getName().toLowerCase().indexOf(".htm")==-1) { //不是HTML文件
			return;
		}
		String html = FileUtils.readStringFromFile(questionFile.getPath(), "gbk");
		int beginIndex = html.indexOf("<div");
		if(beginIndex==-1) {
			beginIndex = html.indexOf("<DIV");
		}
		if(beginIndex==-1) {
			return;
		}
		beginIndex = html.indexOf(">", beginIndex) + 1;
		int endIndex;
		while((endIndex = html.indexOf("。。", beginIndex))!=-1) {
			String questionHtml = html.substring(beginIndex, endIndex);
			beginIndex = endIndex + 2;
			try {
				String[] values = questionHtml.split("//");
				long questionId = UUIDLongGenerator.generateId();
				for(int i=0; i<values.length; i++) {
					values[i] = retrieveQuestionHtml(values[i], questionId, questionFile, true);
					if(Logger.isDebugEnabled()) {
						Logger.debug("ImportQuestion: " + values[i]);
					}
				}
				if(values[0]==null || values[0].isEmpty()) {
					continue;
				}
				//编号
				//第1位,项目类别: 1/技术业务, 2/劳动安全, 3/必知必会, 0/职业道德
				//第2位,系统分类: 5/车辆
				//第3、4位,工种类别: 02/动车组机械师	
				//第5位,岗位编码: 0/无岗位区分
				//第6位,工种等级: 1/初级, 2/中级, 3/高级, 4/技师, 5/高级技师, 6/通用	
				//第7位,题型: 1/填空题, 2/选择题, 3/判断题, 4/简答题, 5/计算题, 6/论述题, 7/绘图题, 8/多选题
				//第8位,知识分类: 1/专业知识, 2/相关知识, 3/四新知识, 0/原有题库
				//第9位,难度等级: 0/容易, 1/较易, 2/中等, 3/较难, 4/难
				//第10位,重要程度: 1/核心要素, 2/一般要素, 3/辅助要素
				//第11、12位, 知识点: 10/CRH1型规章类, 11/CRH1型高压系统	, 12/CRH1型牵引系统, 13/CRH1型辅助供电, 14/CRH1型空调系统, 15/CRH1型转向架, 16/CRH1型制动系统, 17/CRH1型网络控制系统, 18/CRH1型车体
				//					20/CRH2型规章类, 21/CRH2型高压系统	, 22/CRH2型牵引系统, 23/CRH2型辅助供电, 24/CRH2型空调系统, 25/CRH2型转向架, 26/CRH2型制动系统, 27/CRH2型网络控制系统, 28/CRH2型车体
				String no = StringUtils.filterHtmlElement(values[values.length-1], false);
				if(no==null || no.length()!=14) {
					continue;
				}
				no = no.substring(1, no.length()-1);
				
				//题型
				//单选题格式要求：试题内容//答案A//答案B//答案C//答案D//答案E//答案F//正确答案// [12位编码]。。可选答案前加//，可选答案为六个，没有六个也要输入//，
				//多选题格式要求：试题内容//答案A//答案B//答案C//答案D//答案E//答案F//正确答案#正确答案// [12位编码]。。可选答案前加//，可选答案为六个，没有六个也要输入//，多个正确答案之间用半角#号分隔
				//判断题格式要求：试题内容//正确答案// [12位编码]。。
				//填空题格式要求：试题内容//正确答案#正确答案// [12位编码]。。说明：试题内容中___表示一个空(三个下划线),一题最多支持六个空。如果一个空有多个正确答案可以用半角字符:/表示，正确答案之间用半角#号分隔
				//简答、论述、计算、绘图题格式要求：试题内容//正确答案// [12位编码]。。
				String questionType = new String[]{"填空题", "单选题", "判断题", "简答题", "计算题", "论述题", "绘图题", "多选题"}[Integer.parseInt(no.substring(6, 7))-1]; //题型
				
				int optionNumber = 0; //选项个数
				//题目内容
				String questionContent = values[0];
				if("单选题".equals(questionType) || "多选题".equals(questionType)) {
					for(int i=1; i<=6 && values[i]!=null && !values[i].isEmpty(); i++) { //可选答案前加//，可选答案为六个，没有六个也要输入//，
						optionNumber++;
						questionContent += "<br/>" + (char)('A' + i - 1) + "、" + values[i].trim();
					}
				}
				
				//答案
				int blankNumber = 1; //答案个数,填空题时有效
				String answers = values[values.length-2];
				if("多选题".equals(questionType)) {
					answers = answers.replaceAll("#", "\r\n");
				}
				else if("填空题".equals(questionType)) {
					blankNumber = answers.split("#").length;
					answers = StringUtils.filterHtmlElement(answers, false);
					answers = answers.replaceAll("#", "\r\n").replaceAll(":/", "###");
				}
				
				//创建题目
				Question question = new Question();
				question.setId(questionId); //ID
				question.setNo(no); //编号
				question.setDifficulty(Integer.parseInt(no.substring(8, 9))*20 + 20); //难度系数
				question.setQuestionType(questionType); //题型
				question.setAnswerOnComputer(1); //计算机上可作答
				//question.setComputerMarking(1); //计算机可判卷
				//question.setEachScore(eachScore); //题目单项分值
				//question.setScore(score); //题目总分值
				question.setSource(source); //试题来源
				//question.setResponseTime(responseTime); //作答时间,以秒为单位
				question.setBlankNumber(blankNumber); //答案个数,填空题时有效
				question.setOptionNumber(optionNumber); //选项个数,选择题时有效,默认为4个
				question.setCreated(DateTimeUtils.now());
				question.setCreatorId(sessionInfo.getUserId()); //创建人ID
				question.setCreator(sessionInfo.getUserName()); //创建人
				question.setImportLogId(importLogId); //导入日志ID
				question.setRemark(questionFile.getName()); //备注
				questionService.save(question);
				questionService.updateQuestionComponents(question, questionContent, null, null, null, postIds, posts, knowledgeIds, knowledges, itemIds, items, answers, answers, false, true);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 
	 * @param questionFile
	 * @param importLogId
	 * @param source
	 * @param postIds
	 * @param posts
	 * @param knowledgeIds
	 * @param knowledges
	 * @param itemIds
	 * @param items
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void importFuzhouQuestionFile(File questionFile, long importLogId, String source, String postIds, String posts, String knowledgeIds, String knowledges, String itemIds, String items, SessionInfo sessionInfo) throws ServiceException {
		if(questionFile.getName().toLowerCase().indexOf(".htm")==-1) { //不是HTML文件
			return;
		}
		String html = FileUtils.readStringFromFile(questionFile.getPath(), "gbk");
		int beginIndex = html.indexOf(">一、");
		if(beginIndex==-1) {
			return;
		}
		int typeIndex = 0;
		String questionType = null;
		String[] chineseNumbers = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
		int questionIndex = 1;
		for(;;) {
			int indexType = html.indexOf(">" + chineseNumbers[typeIndex] + "、", beginIndex);
			int indexQuestion = html.indexOf("'mso-list:Ignore'>" + questionIndex + ".", beginIndex);
			if(indexType!=-1 && (indexType<indexQuestion || indexQuestion==-1)) {
				if(typeIndex>0) {
					String questionHTML = html.substring(beginIndex, indexType+1);
					if(Logger.isDebugEnabled()) {
						Logger.debug("ImportQuestion: " + questionHTML);
					}
					saveFuzhouQuestion(questionFile, questionHTML, questionType, importLogId, source, postIds, posts, knowledgeIds, knowledges, itemIds, items, sessionInfo);
				}
				int endIndex = html.indexOf("</p>", indexType);
				questionType = StringUtils.filterHtmlElement(html.substring(indexType+3, endIndex), false).trim();
				if(questionType.equals("选择题")) {
					questionType = "单选题";
				}
				questionIndex = 1;
				beginIndex = endIndex + "</p>".length();
				typeIndex++;
				continue;
			}
			if(indexQuestion==-1) {
				break;
			}
			String questionHTML = html.substring(beginIndex, indexQuestion + "'mso-list:Ignore'>".length());
			beginIndex = indexQuestion + ("'mso-list:Ignore'>" + questionIndex + ".").length(); 
			questionIndex++;
			if(questionIndex>1) {
				if(Logger.isDebugEnabled()) {
					Logger.debug("ImportQuestion: " + questionHTML);
				}
				saveFuzhouQuestion(questionFile, questionHTML, questionType, importLogId, source, postIds, posts, knowledgeIds, knowledges, itemIds, items, sessionInfo);
			}
		}
		saveFuzhouQuestion(questionFile, html.substring(beginIndex), questionType, importLogId, source, postIds, posts, knowledgeIds, knowledges, itemIds, items, sessionInfo);
	}
	
	/**
	 * 保存题目
	 * @param questionId
	 * @param questionHTML
	 * @param questionType
	 * @param importLogId
	 * @param source
	 * @param postIds
	 * @param posts
	 * @param knowledgeIds
	 * @param knowledges
	 * @param itemIds
	 * @param items
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void saveFuzhouQuestion(File questionFile, String questionHTML, String questionType, long importLogId, String source, String postIds, String posts, String knowledgeIds, String knowledges, String itemIds, String items, SessionInfo sessionInfo) throws ServiceException {
		try {
			if(questionHTML==null || questionHTML.isEmpty()) {
				return;
			}
			long questionId = UUIDLongGenerator.generateId();
			questionHTML = retrieveQuestionHtml(questionHTML, questionId, questionFile, false);
			if(questionHTML==null || questionHTML.isEmpty()) {
				return;
			}
			
			int blankNumber = 0; //答案个数,填空题时有效
			String answers = null; //答案
			int optionNumber = 0; //选项个数
			String questionContent; //题目内容
			if("填空题,单选题,多选题,判断题".indexOf(questionType)!=-1) {
				HTMLDocument questionDocument = htmlParser.parseHTMLString(questionHTML, "utf-8");
				for(boolean hasAnswer = true; hasAnswer;) {
					hasAnswer = false;
					NodeList spans = questionDocument.getBody().getElementsByTagName("span");
					for(int i=0; i<(spans==null ? 0 : spans.getLength()); i++) {
						HTMLElement span = (HTMLElement)spans.item(i);
						String style = span.getAttribute("style");
						String text;
						if((style==null || style.indexOf("color:red")==-1) &&
						   (!"判断题".equals(questionType) || (text=htmlParser.getTextContent(span))==null || (!"×".equals(text.trim()) && !"√".equals(text.trim())))) {
							continue;
						}
						String answer = htmlParser.getElementInnerHTML(span, "utf-8").replaceAll("(?i)<span[^>]*>", "").replaceAll("(?i)</span>", "").trim();
						text = "\u00a0";
						if("填空题".equals(questionType)) {
							blankNumber++;
							text = "";
							for(int j=0; j<Math.max(12, StringUtils.filterHtmlElement(answer, false).getBytes().length); j++) {
								text += "_";
							}
						}
						else {
							answer = StringUtils.filterHtmlElement(answer, false).trim();
							if("判断题".equals(questionType)) {
								answer = (answer.indexOf("√")!=-1 ? "T" : "F");
							}
						}
						answers = (answers==null ? "" : answers + "\r\n") + answer;
						span.getParentNode().replaceChild(span.getOwnerDocument().createTextNode(text), span);
						hasAnswer = true;
						break;
					}
				}
				//题目内容
				questionContent = htmlParser.getBodyHTML(questionDocument, "utf-8", false).replaceAll("(?i)<span[^>]*>", "").replaceAll("(?i)</span>", "").trim();
				if("单选题,多选题".indexOf(questionType)!=-1) {
					for(optionNumber=0; optionNumber<26; optionNumber++) {
						char option = (char)('A' + optionNumber);
						if(questionContent.indexOf(option + ".")==-1 &&
						   questionContent.indexOf(option + "、")==-1 &&
						   questionContent.indexOf(option + "）")==-1) {
							break;
						}
					}
					if(optionNumber==0) {
						optionNumber = 4;
					}
				}
			}
			else {
				int index = questionHTML.indexOf("答：");
				if(index==-1) {
					questionContent = questionHTML.replaceAll("(?i)<span[^>]*>", "").replaceAll("(?i)</span>", "").trim();
				}
				else {
					HTMLDocument questionDocument = htmlParser.parseHTMLString(questionHTML.substring(0, index), "utf-8");
					questionContent = htmlParser.getBodyHTML(questionDocument, "utf-8", false).replaceAll("(?i)<span[^>]*>", "").replaceAll("(?i)</span>", "").trim();
					questionDocument = htmlParser.parseHTMLString(questionHTML.substring(index + 2), "utf-8");
					answers = htmlParser.getBodyHTML(questionDocument, "utf-8", false).replaceAll("(?i)<span[^>]*>", "").replaceAll("(?i)</span>", "").trim();
				}
			}
			
			for(;;) {
				if(questionContent.endsWith("<BR>")) {
					questionContent = questionContent.substring(0, questionContent.length()-"<BR>".length());
				}
				else if(questionContent.endsWith("&nbsp;")) {
					questionContent = questionContent.substring(0, questionContent.length()-"&nbsp;".length());
				}
				else {
					break;
				}
			}
			
			//创建题目
			Question question = new Question();
			question.setId(questionId); //ID
			//question.setNo(no); //编号
			question.setDifficulty(0); //难度系数
			question.setQuestionType(questionType); //题型
			question.setAnswerOnComputer(1); //计算机上可作答
			//question.setComputerMarking(1); //计算机可判卷
			//question.setEachScore(eachScore); //题目单项分值
			//question.setScore(score); //题目总分值
			question.setSource(source); //试题来源
			//question.setResponseTime(responseTime); //作答时间,以秒为单位
			question.setBlankNumber(blankNumber); //答案个数,填空题时有效
			question.setOptionNumber(optionNumber); //选项个数,选择题时有效,默认为4个
			question.setCreated(DateTimeUtils.now());
			question.setCreatorId(sessionInfo.getUserId()); //创建人ID
			question.setCreator(sessionInfo.getUserName()); //创建人
			question.setImportLogId(importLogId); //导入日志ID
			question.setRemark(questionFile.getName()); //备注
			questionService.save(question);
			questionService.updateQuestionComponents(question, questionContent, null, null, null, postIds, posts, knowledgeIds, knowledges, itemIds, items, answers, answers, false, true);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 解析题目HTML
	 * @param questionHtml
	 * @param questionId
	 * @return
	 * @throws ServiceException
	 */
	private String retrieveQuestionHtml(String questionHtml, long questionId, File questionFile, boolean removeSpan) throws ServiceException {
		if(questionHtml.startsWith("<st1:")) {
			questionHtml = "<span/>" + questionHtml;
		}
		HTMLDocument questionDocument = htmlParser.parseHTMLString(questionHtml, "utf-8");
		if(questionDocument==null) {
			return null;
		}
		NodeList nodes = questionDocument.getBody().getChildNodes();
		if(nodes==null) {
			return null;
		}
		//删除空节点
		String text;
		for(int i=nodes.getLength()-1; i>=0; i--) {
			Node node = nodes.item(i);
			NodeList images;
			if((node instanceof HTMLElement) && (images=((HTMLElement)node).getElementsByTagName("img"))!=null && images.getLength()>0) {
				continue;
			}
			if((text = htmlParser.getTextContent(node))==null || text.replaceAll("[\\r\\n\\t\\xa0 　]", "").isEmpty()) { //\\xa0对应的是&nbsp;
				node.getParentNode().removeChild(node);
			}
		}
		//处理图片
		NodeList images = questionDocument.getBody().getElementsByTagName("img");
		for(int i=0; i<(images==null ? 0 : images.getLength()); i++) {
			HTMLImageElement image = (HTMLImageElement)images.item(i);
			String path = questionFile.getPath().replace('\\', '/');
			path = path.substring(0, path.lastIndexOf('/') + 1) + image.getSrc();
			if(FileUtils.isExists(path)) {
				path = attachmentService.uploadFile("enterprise/exam/question", "images", null, questionId, path);
				image.setSrc(attachmentService.createDownload("enterprise/exam/question", "images", questionId, new File(path).getName(), false, null));
			}
		}
		questionHtml = htmlParser.getElementInnerHTML(questionDocument.getBody(), "utf-8");
		if(questionHtml==null) {
			return null;
		}
		//把段落替换为换行<o:p></o:p>
		questionHtml = questionHtml.trim().replaceAll("(?i)<p[^>]*>", "<br/>")
										  .replaceAll("(?i)</p>", "")
										  .replaceAll("(?i)<o:p[^>]*>", "")
										  .replaceAll("(?i)</o:p>", "");
		if(questionHtml.startsWith("<br/>")) {
			questionHtml = questionHtml.substring("<br/>".length());
		}
		return !removeSpan ? questionHtml : questionHtml.replaceAll("(?i)<span[^>]*>", "").replaceAll("(?i)</span>", "");
	}

	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
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
	 * @return the questionService
	 */
	public QuestionService getQuestionService() {
		return questionService;
	}

	/**
	 * @param questionService the questionService to set
	 */
	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
}