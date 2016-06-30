package com.yuanluesoft.enterprise.exam.processor;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTextAreaElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.enterprise.exam.pojo.ExamPaperQuestion;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class ExamTestQuestionsProcessor extends RecordListProcessor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#writeRecordElement(com.yuanluesoft.jeaf.view.model.View, java.lang.Object, org.w3c.dom.html.HTMLAnchorElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, javax.servlet.http.HttpServletRequest, boolean, boolean)
	 */
	protected void writeRecordElement(View view, Object record, HTMLAnchorElement recordElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RecordList recordListModel, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if("field".equals(recordElement.getId())) {
			ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)record;
			Integer status = (Integer)request.getAttribute("EXAM_TEST_STATUS");
			String fieldName = StringUtils.getPropertyValue(recordElement.getAttribute("urn"), "name");
			if("examTestAnswer.answer".equals(fieldName)) { //作答字段
				if(status!=null && status.intValue()==ExamService.EXAM_TEST_STATUS_TESTING) {
					writeAnswerArea(recordElement, examPaperQuestion, sitePage, htmlParser);
					return;
				}
				else if("examPaperPrint".equals(sitePage.getName())) { //打印试卷
					if(",单选题,多选题,判断题,填空题,".indexOf("," + examPaperQuestion.getQuestion().getQuestionType() + ",")==-1) { //问答题、论述题等
						//插入6个换行,作为作答区域
						for(int i=0; i<6; i++) {
							recordElement.getParentNode().insertBefore(recordElement.getOwnerDocument().createElement("br"), recordElement);
						}
					}
					recordElement.getParentNode().removeChild(recordElement);
					return;
				}
				else if(examPaperQuestion.getExamTestAnswer().getAnswer()!=null && ",多选题,填空题,".indexOf("," + examPaperQuestion.getQuestion().getQuestionType() + ",")!=-1) {
					examPaperQuestion.getExamTestAnswer().setAnswer(examPaperQuestion.getExamTestAnswer().getAnswer().replaceAll("\\r\\n", "、"));
				}
			}
			else if("examTestAnswer.score".equals(fieldName) || "examTestAnswer.remark".equals(fieldName)) { //得分/批改说明
				if(examPaperQuestion.getExamTestAnswer().getCorrectorId()==0) { //未批改
					if("examTestCorrect".equals(sitePage.getName())) { //批改页面
						writeCorrectField(recordElement, fieldName, examPaperQuestion, false, sitePage, htmlParser);
					}
					else {
						recordElement.getParentNode().removeChild(recordElement);
					}
					return;
				}
				else if(status!=null && status.intValue()==ExamService.EXAM_TEST_STATUS_COMPLETE &&
						examPaperQuestion.getExamTestAnswer().getCorrectorId()>10 && "examTestCheck".equals(sitePage.getName())) { //已批改、不是系统自动批改、复核
					writeCorrectField(recordElement, fieldName, examPaperQuestion, true, sitePage, htmlParser);
					return;
				}
			}
		}
		super.writeRecordElement(view, record, recordElement, webDirectory, parentSite, htmlParser, sitePage, recordListModel, requestInfo, request);
	}
	
	/**
	 * 输出作答区域
	 * @param recordElement
	 * @param examPaperQuestion
	 * @param sitePage
	 * @param htmlParser
	 * @throws ServiceException
	 */
	private void writeAnswerArea(HTMLAnchorElement recordElement, ExamPaperQuestion examPaperQuestion, SitePage sitePage, HTMLParser htmlParser) throws ServiceException {
		//添加作答区域
		HTMLElement spanAnswer = (HTMLElement)recordElement.getOwnerDocument().createElement("span");
		spanAnswer.setClassName("answerArea");
		spanAnswer.setId("answerArea_" + examPaperQuestion.getId());
		spanAnswer.setTitle(examPaperQuestion.getQuestion().getQuestionType() + sitePage.getAttribute("recordNumber"));
		recordElement.getParentNode().replaceChild(spanAnswer, recordElement);
		HTMLElement spanAnswerLabel = (HTMLElement)spanAnswer.getOwnerDocument().createElement("span");
		htmlParser.setTextContent(spanAnswerLabel, "作答：");
		spanAnswerLabel.setClassName("spanAnswerLabel");
		spanAnswer.appendChild(spanAnswerLabel);
		if("单选题".equals(examPaperQuestion.getQuestion().getQuestionType()) || "多选题".equals(examPaperQuestion.getQuestion().getQuestionType())) {
			String[] options = new String[examPaperQuestion.getQuestion().getOptionNumber()*2];
			for(int i=0; i<examPaperQuestion.getQuestion().getOptionNumber(); i++) {
				options[i*2] = options[i*2+1] = "" + (char)('A' + i);
			}
			writeOptions(options, "单选题".equals(examPaperQuestion.getQuestion().getQuestionType()), spanAnswer, examPaperQuestion.getId(), htmlParser);
		}
		else if("判断题".equals(examPaperQuestion.getQuestion().getQuestionType())) {
			writeOptions(new String[]{"T", "正确", "F", "错误"}, true, spanAnswer, examPaperQuestion.getId(), htmlParser);
		}
		else if("填空题".equals(examPaperQuestion.getQuestion().getQuestionType())) {
			for(int i=0; i<examPaperQuestion.getQuestion().getBlankNumber(); i++) {
				if(examPaperQuestion.getQuestion().getBlankNumber()>1) {
					HTMLElement label = (HTMLElement)spanAnswer.getOwnerDocument().createElement("span");
					label.setClassName("blankLabel");
					htmlParser.setTextContent(label, (i+1) + ".");
					spanAnswer.appendChild(label);
				}
				HTMLInputElement input = (HTMLInputElement)spanAnswer.getOwnerDocument().createElement("input");
				input.setAttribute("type", "text");
				input.setClassName("blankInput");
				//input.setValue();
				input.setName("answer_" + examPaperQuestion.getId());
				spanAnswer.appendChild(input);
			}
		}
		else { //其它题型
			spanAnswerLabel.setClassName("spanAnswerLabel textareaLabel");
			HTMLTextAreaElement textarea = (HTMLTextAreaElement)spanAnswer.getOwnerDocument().createElement("textarea");
			textarea.setClassName("textarea");
			//input.setValue();
			textarea.setName("answer_" + examPaperQuestion.getId());
			spanAnswer.appendChild(textarea);
		}
	}
	
	/**
	 * 输出选项
	 * @param options
	 * @param radio
	 * @param spanAnswer
	 * @param examPaperQuestionId
	 * @param htmlParser
	 * @throws ServiceException
	 */
	private void writeOptions(String[] options, boolean radio, HTMLElement spanAnswer, long examPaperQuestionId, HTMLParser htmlParser) throws ServiceException {
		for(int i=0; i<options.length; i+=2) {
			HTMLInputElement input = (HTMLInputElement)spanAnswer.getOwnerDocument().createElement("input");
			input.setAttribute("type", radio ? "radio" : "checkox");
			input.setClassName(input.getAttribute("type") + " " + options[i]);
			input.setValue(options[i]);
			input.setName("answer_" + examPaperQuestionId);
			input.setId(input.getName() + "_" + options[i]);
			spanAnswer.appendChild(input);
			HTMLLabelElement label = (HTMLLabelElement)spanAnswer.getOwnerDocument().createElement("label");
			label.setAttribute("for", input.getId());
			label.setClassName("optionLabel");
			htmlParser.setTextContent(label, options[i+1] + "\u00a0");
			spanAnswer.appendChild(label);
		}
	}
	
	/**
	 * 输出批改字段
	 * @param recordElement
	 * @param fieldName
	 * @param examPaperQuestion
	 * @param check 是否复核
	 * @param sitePage
	 * @param htmlParser
	 * @throws ServiceException
	 */
	private void writeCorrectField(HTMLAnchorElement recordElement, String fieldName, ExamPaperQuestion examPaperQuestion, boolean check, SitePage sitePage, HTMLParser htmlParser) throws ServiceException {
		if("examTestAnswer.score".equals(fieldName)) { //打分
			HTMLSelectElement select = (HTMLSelectElement)recordElement.getOwnerDocument().createElement("select");
			select.setClassName("correctScore");
			select.setTitle(examPaperQuestion.getQuestion().getQuestionType() + sitePage.getAttribute("recordNumber"));
			select.setName("score_" + examPaperQuestion.getId());
			for(double score = -0.5; score<=examPaperQuestion.getQuestionScore(); score+=0.5) {
				HTMLOptionElement option = (HTMLOptionElement)recordElement.getOwnerDocument().createElement("option");
				option.setValue(score==-0.5 ? "" : "" + score);
				if(check && score==examPaperQuestion.getExamTestAnswer().getScore()) {
					option.setSelected(true);
				}
				option.setLabel(option.getValue());
				option.setTextContent(option.getValue());
				select.appendChild(option);
			}
			recordElement.getParentNode().replaceChild(select, recordElement);
		}
		else { //批改说明
			HTMLElement spanRemark = (HTMLElement)recordElement.getOwnerDocument().createElement("span");
			spanRemark.setClassName("correctRemarkLabel");
			htmlParser.setTextContent(spanRemark, "说明：");
			recordElement.getParentNode().insertBefore(spanRemark, recordElement);
			HTMLInputElement input = (HTMLInputElement)recordElement.getOwnerDocument().createElement("input");
			input.setAttribute("type", "text");
			input.setClassName("correctRemark");
			input.setName("remark_" + examPaperQuestion.getId());
			input.setValue(examPaperQuestion.getExamTestAnswer().getRemark());
			recordElement.getParentNode().replaceChild(input, recordElement);
		}
	}
}