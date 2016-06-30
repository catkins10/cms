package com.yuanluesoft.jeaf.business.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.yuanluesoft.jeaf.base.model.CloneableObject;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 业务对象字段配置
 * @author linchuan
 *
 */
public class Field extends CloneableObject {
	private String name; //名称
	private String title; //标题
	private String length; //字段长度,字符类型时是整数,按汉字计算,数字类型时可以是"6,3"
	private boolean required; //是否必填项
	private boolean persistence; //是否持久字段,也就是数据库字段,默认true,参数 referenceFields: 引用的字段名称列表
	
	/*
	 * 字段类型
	 * string		字符串
	 * 				参数 defaultValue: 默认值
	 * 				参数 numerationFormat: defaultValue为{NUMERATION}时有效
	 * 				参数 singleByteCharacters: 是否单字节字符,如果不是,文本输入框的最大长度=字段长度/2
	 * char			字符
	 * 				参数 defaultValue: 默认值
	 * number		数字
	 * 				参数 minValue: 最小值
	 * 				参数 maxValue: 最大值
	 *				参数 displayFormat: 显示格式
	 *				参数 defaultValue: 默认值
	 * 				参数 numerationFormat: defaultValue为{NUMERATION}时有效
	 * date			日期
	 * 				参数 displayFormat: 显示格式
	 * 				参数 defaultValue: 默认值
	 * timestamp	时间
	 * 				参数 displayFormat: 显示格式
	 * 				参数 defaultValue: 默认值
	 * html			HTML
	 * email		电子邮件地址
	 * password		密码,按^[^\%&amp;\?\+]+$|^\x7b(.*)\x7d$做校验
	 * phone		电话号码,按[0-9_]{8,18}$做校验
	 * attachment	附件
	 * image		图片
	 * video		视频
	 * attachmentName 附件名称
	 * 				参数 referenceFields 可以指定引用的附件(图片,视频)字段,如果没有指定则图片的url默认为:[contextPath]/[applicationName]/[名称],否则调用附件(图片,视频）服务获取url
	 * 				参数 referenceRecordClass 引用的记录类名称,没有指定时,附件属于当前记录
	 *				参数 referenceRecordField 引用的记录类字段
	 * imageName	图片名称,	参数同attachmentName
	 * videoName	视频名称,参数同attachmentName
	 * component	组件
	 * 				参数 class: 对应的类名称
	 * components	组件集合
	 * 	 			参数 class: 对应的类名称
	 * 				参数 presettingOpinionTypes: 预置的意见类型,仅对options有效,如：部门意见,批示意见|required
	 * 				参数 lazyLoad: 是否延迟加载,默认true,记录很多时应该设为false
	 * 				参数 url: 显示组成元素详细信息的URL
	 * opinion		意见,根据表opinion_type和opinions字段动态创建
	 * 				参数 prompt: 没有填写意见时的提示信息
	 * binary		二进制
	 */
	private String type;
	
	/*
	 * 字段输入方式
	 * text			文本
	 * password		密码
	 * textarea		多行文本
	 * 				参数 rows: 显示的行数
	 * htmleditor	网页编辑器
	 * 				参数 attachmentSelector: 附件选择对话框URL, 默认为selectAttachment.shtml
	 * 				参数 commands: 命令列表
	 * 				参数 height: 高度,默认100%
	 * 				参数 plugins: 插件列表
	 * 				参数 autoIndentation: 是否自动缩进
	 * 				参数 fullPage: 是否完整HTML页面
	 * radio		单选框
	 * 				参数 itemsText: 条目列表,格式:名称1|值1\0名称2|值2...
	 * 				参数 itemsHql: 获取条目列表的hql,第一个值是条目名称,第二个是条目值, 如: select Site.name, Site.id from Site Site
	 * 				参数 itemsSql: 获取条目列表的sql,第一个值是条目名称,第二个是条目值, 如: select name, id from cms_site
	 * 				参数 itemsServiceName: 获取条目列表的服务名称,必须实现BusinessService接口
	 * 				参数 itemsName: 条目列表名称,让BusinessService识别当前要获取的内容,默认等于Field.name
	 * multibox		多选框,对应的属性必须是数组类型
	 * 				参数和radio相同
	 * checkbox		单个复选框
	 * 				参数 value: 值
	 * 				参数 label: 标题
	 * dropdown		下拉列表
	 * 				参数 valueField: 对应的值字段,默认为字段自己(Field.name)
	 * 				参数 titleField: 对应的标题字段,默认为字段自己(Field.name)
	 * 				参数 selectOnly: 是否只能选择
	 * 				参数 listPickerWidth: 列表宽度
	 * 				其他参数和radio相同
	 * select		选择对话框
	 * 				参数 js: 需要引入的js文件
	 * 				参数 execute: 选择时执行的脚本
	 * 				参数 selectOnly: 是否只能选择
	 * 				参数 selectButtonStyleClass: 选择按钮样式
	 * date			日期
	 * datetime		日期和时间
	 * time			时间
	 * 				参数 secondEnabled: 是否要输入秒,默认false
	 * hidden		自动插入hidden域到表单中,不需要在jsp和模板中添加
	 * 		  		参数 newFormOnly: 是否只在创建新记录时有效,默认false
	 * readonly		只读,不需要用户输入
	 * attachment	附件
	 * 			  	参数 attachmentEditor: 附件编辑URL,默认为attachmentEditor.shtml
	 * 				参数 maxUploadSize: 附件最大大小,超出时不允许上传,默认为100000000(100M)
	 * 				参数 serviceName: 使用的服务名称,默认为attachmentService
	 * 				参数 fileExtension: 文件后缀名列表,如:所有图片|*.jpg;*.jpeg;*.jpe;*.bmp;*.gif;*.png|
	 * 				参数 extendJs: 附件扩展JS文件
	 * 				参数 dynamicUrl: 是否生成动态的下载链接,默认为false(生成静态的下载链接)
	 * 				参数 type: 动态附件类型,默认为空
	 * 				参数 simpleMode: 是否启用精简模式,默认为false
	 * image		图片
	 * 				参数 maxSaveSize: 最大存储字节数,超出以后自动裁剪
	 * 				参数 clipEnabled: 图片裁剪时是否允许切除图片,默认false
	 * 				参数 maxMegaPixels: 允许上传的最大像素,以百万为单位,默认8.0,超出后图片自动删除
	 * 				其他参数和attachment相同,maxUploadSize默认1000000(1M)
	 * video		视频
	 * 			  	参数 convertMp4: 是否转换为MP4格式,默认为true
	 * 				参数 videoBitrate: 视频比特率,默认为473
	 *				参数 videoFps: 帧频,默认为25
	 *				参数 videoWidth: 视频宽度,默认400,0表示不调整
	 *				参数 videoHeight: 视频高度,默认300,0表示不调整
	 *				参数 audioBitrate: 音频码率,单位kbps,默认56
	 *				参数 audioFreq: 音频采样率,默认22050
	 *				参数 previewImageDynamicUrl: 预览图是否动态地址,默认false
	 * 				其他参数和attachment相同,maxUploadSize默认100000000(100M)
	 * none			不需要输入和输出
	 */
	private String inputMode;
	private Map parameters; //参数列表,为字段类型、输入方式配置所需要的参数
		
	public Field() {
		
	}

	public Field(String name, String title, String type, String length, String inputMode, boolean required, boolean persistence) {
		super();
		this.name = name;
		this.title = title;
		this.length = length;
		this.required = required;
		this.persistence = persistence;
		this.type = type;
		this.inputMode = inputMode;
	}
	
	/**
	 * 获取参数值
	 * @param parameterName
	 * @return
	 */
	public Object getParameter(String parameterName) {
		if(parameters==null) {
			return null;
		}
		return parameters.get(parameterName); 
	}
	
	/**
	 * 设置参数值
	 * @param parameterName
	 * @param parameterValue
	 */
	public void setParameter(String parameterName, Object parameterValue) {
		if(parameters==null) {
			parameters = new HashMap();
		}
		if(parameterValue==null) {
			parameters.remove(parameterName);
		}
		else {
			parameters.put(parameterName, parameterValue);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		Field field = (Field)obj;
		if(!StringUtils.isEquals(name, field.getName())) { //名称
			return false;
		}
		if(!StringUtils.isEquals(title, field.getTitle())) { //标题
			return false;
		}
		if(!StringUtils.isEquals(length, field.getLength())) { //字段长度,字符类型时是整数,按汉字计算,数字类型时可以是"6,3"
			return false;
		}
		if(required!=field.isRequired()) { //是否必填项
			return false;
		}
		if(persistence!=field.isPersistence()) { //是否持久字段,也就是数据库字段,默认true,参数 referenceFields: 引用的字段名称列表
			return false;
		}
		if(!StringUtils.isEquals(type, field.getType())) { //类型
			return false;
		}
		if(!StringUtils.isEquals(inputMode, field.getInputMode())) { //输入方式
			return false;
		}
		//比较参数
		if(parameters==null && field.getParameters()==null) {
			return true;
		}
		else if(parameters==null || field.getParameters()==null) {
			return false;
		}
		else if(parameters.size()!=field.getParameters().size()) {
			return false;
		}
		for(Iterator iterator = parameters.keySet().iterator(); iterator.hasNext();) {
			String parameterName = (String)iterator.next();
			try {
				if(!StringUtils.isEquals((String)getParameter(parameterName), (String)field.getParameter(parameterName))) { //参数不一致
					return false;
				}
			}
			catch(Exception e) {
				
			}
		}
		return super.equals(obj);
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(String length) {
		this.length = length;
	}

	/**
	 * @return the persistence
	 */
	public boolean isPersistence() {
		return persistence;
	}

	/**
	 * @param persistence the persistence to set
	 */
	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
	}

	/**
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}
}