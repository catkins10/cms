package com.yuanluesoft.jeaf.application.builder.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.business.model.parameter.AttachmentParameter;
import com.yuanluesoft.jeaf.business.model.parameter.CharParameter;
import com.yuanluesoft.jeaf.business.model.parameter.CheckboxParameter;
import com.yuanluesoft.jeaf.business.model.parameter.ComponentParameter;
import com.yuanluesoft.jeaf.business.model.parameter.ComponentsParameter;
import com.yuanluesoft.jeaf.business.model.parameter.DateParameter;
import com.yuanluesoft.jeaf.business.model.parameter.DropdownParameter;
import com.yuanluesoft.jeaf.business.model.parameter.HiddenParameter;
import com.yuanluesoft.jeaf.business.model.parameter.HtmlEditorParameter;
import com.yuanluesoft.jeaf.business.model.parameter.ImageParameter;
import com.yuanluesoft.jeaf.business.model.parameter.MultiboxParameter;
import com.yuanluesoft.jeaf.business.model.parameter.NumberParameter;
import com.yuanluesoft.jeaf.business.model.parameter.OpinionParameter;
import com.yuanluesoft.jeaf.business.model.parameter.RadioParameter;
import com.yuanluesoft.jeaf.business.model.parameter.SelectParameter;
import com.yuanluesoft.jeaf.business.model.parameter.StringParameter;
import com.yuanluesoft.jeaf.business.model.parameter.TextareaParameter;
import com.yuanluesoft.jeaf.business.model.parameter.TimeInputParameter;
import com.yuanluesoft.jeaf.business.model.parameter.TimestampParameter;
import com.yuanluesoft.jeaf.business.model.parameter.VideoParameter;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 应用:字段配置(application_field)
 * @author linchuan
 *
 */
public class ApplicationField extends Record {
	private long formId; //表单ID
	private String name; //字段名称
	private String englishName; //英文名称
	private String fieldType; //数据类型,varchar/char/text/number/date/timesatmp
	private String fieldLength; //字段长度
	private String inputMode; //输入方式
	private int required; //是否必填字段
	private float priority; //优先级
	private int isPresetting; //是否预设字段
	private int isPersistence = 1; //是否数据库字段
	private Set parameters; //输入参数
	
	//参数配置
	private AttachmentParameter attachmentParameter = new AttachmentParameter();
	private CheckboxParameter checkboxParameter = new CheckboxParameter();
	private ComponentParameter componentParameter = new ComponentParameter();
	private ComponentsParameter componentsParameter = new ComponentsParameter();
	private DateParameter dateParameter = new DateParameter();
	private DropdownParameter dropdownParameter = new DropdownParameter();
	private HiddenParameter hiddenParameter = new HiddenParameter();
	private HtmlEditorParameter htmlEditorParameter = new HtmlEditorParameter();
	private ImageParameter imageParameter = new ImageParameter();
	private MultiboxParameter multiboxParameter = new MultiboxParameter();
	private NumberParameter numberParameter = new NumberParameter();
	private OpinionParameter opinionParameter = new OpinionParameter();
	private RadioParameter radioParameter = new RadioParameter();
	private SelectParameter selectParameter = new SelectParameter();
	private StringParameter stringParameter = new StringParameter();
	private CharParameter charParameter = new CharParameter();
	private TextareaParameter textareaParameter = new TextareaParameter();
	private TimeInputParameter timeInputParameter = new TimeInputParameter();
	private TimestampParameter timestampParameter = new TimestampParameter();
	private VideoParameter videoParameter = new VideoParameter();

	/**
	 * 获取参数值
	 * @param parameterName
	 * @return
	 */
	public String getParameterValue(String parameterName) {
		ApplicationFieldParameter parameter = (ApplicationFieldParameter)ListUtils.findObjectByProperty(parameters, "parameterName", parameterName);
		return parameter==null ? null : parameter.getParameterValue();
	}
	
	/**
	 * @return the englishName
	 */
	public String getEnglishName() {
		return englishName;
	}
	/**
	 * @param englishName the englishName to set
	 */
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	/**
	 * @return the fieldLength
	 */
	public String getFieldLength() {
		return fieldLength;
	}
	/**
	 * @param fieldLength the fieldLength to set
	 */
	public void setFieldLength(String fieldLength) {
		this.fieldLength = fieldLength;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	/**
	 * @return the inputMode
	 */
	public String getInputMode() {
		return inputMode;
	}
	/**
	 * @param inputMode the inputMode to set
	 */
	public void setInputMode(String inputMode) {
		this.inputMode = inputMode;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the required
	 */
	public int getRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(int required) {
		this.required = required;
	}
	/**
	 * @return the parameters
	 */
	public Set getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Set parameters) {
		this.parameters = parameters;
	}
	/**
	 * @return the attachmentParameter
	 */
	public AttachmentParameter getAttachmentParameter() {
		return attachmentParameter;
	}
	/**
	 * @param attachmentParameter the attachmentParameter to set
	 */
	public void setAttachmentParameter(AttachmentParameter attachmentParameter) {
		this.attachmentParameter = attachmentParameter;
	}
	/**
	 * @return the checkboxParameter
	 */
	public CheckboxParameter getCheckboxParameter() {
		return checkboxParameter;
	}
	/**
	 * @param checkboxParameter the checkboxParameter to set
	 */
	public void setCheckboxParameter(CheckboxParameter checkboxParameter) {
		this.checkboxParameter = checkboxParameter;
	}
	/**
	 * @return the componentParameter
	 */
	public ComponentParameter getComponentParameter() {
		return componentParameter;
	}
	/**
	 * @param componentParameter the componentParameter to set
	 */
	public void setComponentParameter(ComponentParameter componentParameter) {
		this.componentParameter = componentParameter;
	}
	/**
	 * @return the componentsParameter
	 */
	public ComponentsParameter getComponentsParameter() {
		return componentsParameter;
	}
	/**
	 * @param componentsParameter the componentsParameter to set
	 */
	public void setComponentsParameter(ComponentsParameter componentsParameter) {
		this.componentsParameter = componentsParameter;
	}
	/**
	 * @return the dateParameter
	 */
	public DateParameter getDateParameter() {
		return dateParameter;
	}
	/**
	 * @param dateParameter the dateParameter to set
	 */
	public void setDateParameter(DateParameter dateParameter) {
		this.dateParameter = dateParameter;
	}
	/**
	 * @return the dropdownParameter
	 */
	public DropdownParameter getDropdownParameter() {
		return dropdownParameter;
	}
	/**
	 * @param dropdownParameter the dropdownParameter to set
	 */
	public void setDropdownParameter(DropdownParameter dropdownParameter) {
		this.dropdownParameter = dropdownParameter;
	}
	/**
	 * @return the hiddenParameter
	 */
	public HiddenParameter getHiddenParameter() {
		return hiddenParameter;
	}
	/**
	 * @param hiddenParameter the hiddenParameter to set
	 */
	public void setHiddenParameter(HiddenParameter hiddenParameter) {
		this.hiddenParameter = hiddenParameter;
	}
	/**
	 * @return the htmlEditorParameter
	 */
	public HtmlEditorParameter getHtmlEditorParameter() {
		return htmlEditorParameter;
	}
	/**
	 * @param htmlEditorParameter the htmlEditorParameter to set
	 */
	public void setHtmlEditorParameter(HtmlEditorParameter htmlEditorParameter) {
		this.htmlEditorParameter = htmlEditorParameter;
	}
	/**
	 * @return the imageParameter
	 */
	public ImageParameter getImageParameter() {
		return imageParameter;
	}
	/**
	 * @param imageParameter the imageParameter to set
	 */
	public void setImageParameter(ImageParameter imageParameter) {
		this.imageParameter = imageParameter;
	}
	/**
	 * @return the multiboxParameter
	 */
	public MultiboxParameter getMultiboxParameter() {
		return multiboxParameter;
	}
	/**
	 * @param multiboxParameter the multiboxParameter to set
	 */
	public void setMultiboxParameter(MultiboxParameter multiboxParameter) {
		this.multiboxParameter = multiboxParameter;
	}
	/**
	 * @return the numberParameter
	 */
	public NumberParameter getNumberParameter() {
		return numberParameter;
	}
	/**
	 * @param numberParameter the numberParameter to set
	 */
	public void setNumberParameter(NumberParameter numberParameter) {
		this.numberParameter = numberParameter;
	}
	/**
	 * @return the opinionParameter
	 */
	public OpinionParameter getOpinionParameter() {
		return opinionParameter;
	}
	/**
	 * @param opinionParameter the opinionParameter to set
	 */
	public void setOpinionParameter(OpinionParameter opinionParameter) {
		this.opinionParameter = opinionParameter;
	}
	/**
	 * @return the radioParameter
	 */
	public RadioParameter getRadioParameter() {
		return radioParameter;
	}
	/**
	 * @param radioParameter the radioParameter to set
	 */
	public void setRadioParameter(RadioParameter radioParameter) {
		this.radioParameter = radioParameter;
	}
	/**
	 * @return the selectParameter
	 */
	public SelectParameter getSelectParameter() {
		return selectParameter;
	}
	/**
	 * @param selectParameter the selectParameter to set
	 */
	public void setSelectParameter(SelectParameter selectParameter) {
		this.selectParameter = selectParameter;
	}
	/**
	 * @return the stringParameter
	 */
	public StringParameter getStringParameter() {
		return stringParameter;
	}
	/**
	 * @param stringParameter the stringParameter to set
	 */
	public void setStringParameter(StringParameter stringParameter) {
		this.stringParameter = stringParameter;
	}
	/**
	 * @return the textareaParameter
	 */
	public TextareaParameter getTextareaParameter() {
		return textareaParameter;
	}
	/**
	 * @param textareaParameter the textareaParameter to set
	 */
	public void setTextareaParameter(TextareaParameter textareaParameter) {
		this.textareaParameter = textareaParameter;
	}
	/**
	 * @return the timeInputParameter
	 */
	public TimeInputParameter getTimeInputParameter() {
		return timeInputParameter;
	}
	/**
	 * @param timeInputParameter the timeInputParameter to set
	 */
	public void setTimeInputParameter(TimeInputParameter timeInputParameter) {
		this.timeInputParameter = timeInputParameter;
	}
	/**
	 * @return the timestampParameter
	 */
	public TimestampParameter getTimestampParameter() {
		return timestampParameter;
	}
	/**
	 * @param timestampParameter the timestampParameter to set
	 */
	public void setTimestampParameter(TimestampParameter timestampParameter) {
		this.timestampParameter = timestampParameter;
	}
	/**
	 * @return the videoParameter
	 */
	public VideoParameter getVideoParameter() {
		return videoParameter;
	}
	/**
	 * @param videoParameter the videoParameter to set
	 */
	public void setVideoParameter(VideoParameter videoParameter) {
		this.videoParameter = videoParameter;
	}
	/**
	 * @return the formId
	 */
	public long getFormId() {
		return formId;
	}
	/**
	 * @param formId the formId to set
	 */
	public void setFormId(long formId) {
		this.formId = formId;
	}

	/**
	 * @return the isPresetting
	 */
	public int getIsPresetting() {
		return isPresetting;
	}

	/**
	 * @param isPresetting the isPresetting to set
	 */
	public void setIsPresetting(int isPresetting) {
		this.isPresetting = isPresetting;
	}

	/**
	 * @return the isPersistence
	 */
	public int getIsPersistence() {
		return isPersistence;
	}

	/**
	 * @param isPersistence the isPersistence to set
	 */
	public void setIsPersistence(int isPersistence) {
		this.isPersistence = isPersistence;
	}

	/**
	 * @return the charParameter
	 */
	public CharParameter getCharParameter() {
		return charParameter;
	}

	/**
	 * @param charParameter the charParameter to set
	 */
	public void setCharParameter(CharParameter charParameter) {
		this.charParameter = charParameter;
	}
}