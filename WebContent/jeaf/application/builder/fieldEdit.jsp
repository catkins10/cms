<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">名称：</td>
		<td><ext:field property="field.name"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">英文名称：</td>
		<td>
			<ext:equal value="1" property="field.isPresetting">
				<ext:field readonly="true" property="field.englishName"/>
			</ext:equal>
			<ext:equal value="0" property="field.isPresetting">
				<ext:field property="field.englishName"/>
			</ext:equal>
		</td>
	</tr>	
	<tr>
		<td nowrap="nowrap">必填字段：</td>
		<td><ext:field property="field.required"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">数据类型：</td>
		<td>
			<ext:equal value="1" property="field.isPresetting">
				<html:hidden property="field.fieldType"/>
				<ext:field writeonly="true" property="field.fieldType" onchange="onFieldChanged()"/>
			</ext:equal>
			<ext:equal value="0" property="field.isPresetting">
				<ext:field property="field.fieldType" onchange="onFieldChanged()"/>
			</ext:equal>
		</td>
	</tr>
	<tr id="trFieldLength" style="display:none">
		<td nowrap="nowrap">字段长度：</td>
		<td>
			<ext:equal value="1" property="field.isPresetting">
				<html:hidden property="field.fieldLength"/>
				<ext:field writeonly="true" property="field.fieldLength"/>
			</ext:equal>
			<ext:equal value="0" property="field.isPresetting">
				<ext:field property="field.fieldLength"/>
			</ext:equal>
		</td>
	</tr>
	<tr id="trInputMode" style="display:none">
		<td nowrap="nowrap">输入方式：</td>
		<td><ext:field property="field.inputMode" onchange="onFieldChanged()"/></td>
	</tr>
	
	<tr id="trStringParameter" style="display:none">
		<td nowrap="nowrap">默认值：</td>
		<td><ext:field property="field.stringParameter.defaultValue" onchange="onStringDefaultValueChanged()"/></td>
	</tr>
	<tr id="trStringParameter_numerationFormat" style="display:none">
		<td nowrap="nowrap" valign="bottom" style="padding-bottom:6px">编号规则：</td>
		<td>
			<div style="padding-bottom: 3px">
				<input type="button" value="插入年" onclick="FormUtils.pasteText('field.stringParameter.numerationFormat', '&lt;年*4&gt;')" class="button" style="width:50px">
				<input type="button" value="插入月" onclick="FormUtils.pasteText('field.stringParameter.numerationFormat', '&lt;月*2&gt;')" class="button" style="width:50px">
				<input type="button" value="插入日" onclick="FormUtils.pasteText('field.stringParameter.numerationFormat', '&lt;日*2&gt;')" class="button" style="width:50px">
				<input type="button" value="插入序号" onclick="FormUtils.pasteText('field.stringParameter.numerationFormat', '&lt;序号*4&gt;')" class="button" style="width:60px">
			</div>
			<ext:field property="field.stringParameter.numerationFormat"/>
		</td>
	</tr>
	<tr id="trStringParameter" style="display:none">
		<td nowrap="nowrap">是否单字节文本：</td>
		<td><ext:field property="field.stringParameter.singleByteCharacters"/></td>
	</tr>
	
	<tr id="trNumberParameter" style="display:none">
		<td nowrap="nowrap">最小值：</td>
		<td><ext:field property="field.numberParameter.minValue"/></td>
	</tr>
	<tr id="trNumberParameter" style="display:none">
		<td nowrap="nowrap">最大值：</td>
		<td><ext:field property="field.numberParameter.maxValue"/></td>
	</tr>
	<tr id="trNumberParameter" style="display:none">
		<td nowrap="nowrap">显示格式：</td>
		<td><ext:field property="field.numberParameter.displayFormat"/></td>
	</tr>
	<tr id="trNumberParameter" style="display:none">
		<td nowrap="nowrap">默认值：</td>
		<td><ext:field property="field.numberParameter.defaultValue" onchange="onNumberDefaultValueChanged()"/></td>
	</tr>
	<tr id="trNumberParameter_numerationFormat" style="display:none">
		<td nowrap="nowrap" valign="bottom" style="padding-bottom:6px">编号规则：</td>
		<td>
			<div style="padding-bottom: 3px">
				<input type="button" value="插入年" onclick="FormUtils.pasteText('field.numberParameter.numerationFormat', '&lt;年*4&gt;')" class="button" style="width:50px">
				<input type="button" value="插入月" onclick="FormUtils.pasteText('field.numberParameter.numerationFormat', '&lt;月*2&gt;')" class="button" style="width:50px">
				<input type="button" value="插入日" onclick="FormUtils.pasteText('field.numberParameter.numerationFormat', '&lt;日*2&gt;')" class="button" style="width:50px">
				<input type="button" value="插入序号" onclick="FormUtils.pasteText('field.numberParameter.numerationFormat', '&lt;序号*4&gt;')" class="button" style="width:60px">
			</div>
			<ext:field property="field.numberParameter.numerationFormat"/>
		</td>
	</tr>

	<tr id="trDateParameter" style="display:none">
		<td nowrap="nowrap">显示格式：</td>
		<td><ext:field property="field.dateParameter.displayFormat"/></td>
	</tr>
	<tr id="trDateParameter" style="display:none">
		<td nowrap="nowrap">默认值：</td>
		<td><ext:field property="field.dateParameter.defaultValue"/></td>
	</tr>
	
	<tr id="trTimestampParameter" style="display:none">
		<td nowrap="nowrap">显示格式：</td>
		<td><ext:field property="field.timestampParameter.displayFormat"/></td>
	</tr>
	<tr id="trTimestampParameter" style="display:none">
		<td nowrap="nowrap">默认值：</td>
		<td><ext:field property="field.timestampParameter.defaultValue"/></td>
	</tr>

	<tr id="trComponentParameter" style="display:none">
		<td nowrap="nowrap">类名称：</td>
		<td><ext:field property="field.componentParameter.className"/></td>
	</tr>
	
	<tr id="trComponentsParameter" style="display:none">
		<td nowrap="nowrap">类名称：</td>
		<td><ext:field property="field.componentsParameter.className"/></td>
	</tr>
	<tr id="trComponentsParameter" style="display:none">
		<td nowrap="nowrap">预置的意见类型：</td>
		<td><ext:field property="field.componentsParameter.presettingOpinionTypes"/></td>
	</tr>
	<tr id="trComponentsParameter" style="display:none">
		<td nowrap="nowrap">是否延迟加载：</td>
		<td><ext:field property="field.componentsParameter.lazyLoad"/></td>
	</tr>
	<tr id="trComponentsParameter" style="display:none">
		<td nowrap="nowrap">显示组成元素详细信息的URL：</td>
		<td><ext:field property="field.componentsParameter.url"/></td>
	</tr>
	
	<tr id="trComponentsParameter" style="display:none">
		<td nowrap="nowrap">显示组成元素详细信息的URL：</td>
		<td><ext:field property="field.componentsParameter.url"/></td>
	</tr>

	<tr id="trOpinionParameter" style="display:none">
		<td nowrap="nowrap">未填写意见时的提示信息：</td>
		<td><ext:field property="field.opinionParameter.prompt"/></td>
	</tr>
	
	<tr id="trTextareaParameter" style="display:none">
		<td nowrap="nowrap">显示的行数：</td>
		<td><ext:field property="field.textareaParameter.rows"/></td>
	</tr>
	
	<tr id="trHtmlEditorParameter" style="display:none">
		<td nowrap="nowrap">附件选择对话框URL：</td>
		<td><ext:field property="field.htmlEditorParameter.attachmentSelector"/></td>
	</tr>
	<tr id="trHtmlEditorParameter" style="display:none">
		<td nowrap="nowrap">工具栏名称：</td>
		<td><ext:field property="field.htmlEditorParameter.commandSet"/></td>
	</tr>
	<tr id="trHtmlEditorParameter" style="display:none">
		<td nowrap="nowrap">高度：</td>
		<td><ext:field property="field.htmlEditorParameter.height"/></td>
	</tr>

	<tr id="trRadioParameter" style="display:none">
		<td nowrap="nowrap">选项列表：</td>
		<td><ext:field property="field.radioParameter.itemsText"/></td>
	</tr>
	<tr id="trRadioParameter" style="display:none">
		<td nowrap="nowrap">获取选项列表的SQL：</td>
		<td><ext:field property="field.radioParameter.itemsSql"/></td>
	</tr>
	<tr id="trRadioParameter" style="display:none">
		<td nowrap="nowrap">获取选项列表的HQL：</td>
		<td><ext:field property="field.radioParameter.itemsHql"/></td>
	</tr>
	<tr id="trRadioParameter" style="display:none">
		<td nowrap="nowrap">获取选项列表的服务名称：</td>
		<td><ext:field property="field.radioParameter.itemsServiceName"/></td>
	</tr>
	<tr id="trRadioParameter" style="display:none">
		<td nowrap="nowrap">选项列表名称：</td>
		<td><ext:field property="field.radioParameter.itemsName"/></td>
	</tr>

	<tr id="trMultiboxParameter" style="display:none">
		<td nowrap="nowrap">选项列表：</td>
		<td><ext:field property="field.multiboxParameter.itemsText"/></td>
	</tr>
	<tr id="trMultiboxParameter" style="display:none">
		<td nowrap="nowrap">获取选项列表的SQL：</td>
		<td><ext:field property="field.multiboxParameter.itemsSql"/></td>
	</tr>
	<tr id="trMultiboxParameter" style="display:none">
		<td nowrap="nowrap">获取选项列表的HQL：</td>
		<td><ext:field property="field.multiboxParameter.itemsHql"/></td>
	</tr>
	<tr id="trMultiboxParameter" style="display:none">
		<td nowrap="nowrap">获取选项列表的服务名称：</td>
		<td><ext:field property="field.multiboxParameter.itemsServiceName"/></td>
	</tr>
	<tr id="trMultiboxParameter" style="display:none">
		<td nowrap="nowrap">选项列表名称：</td>
		<td><ext:field property="field.multiboxParameter.itemsName"/></td>
	</tr>
	
	<tr id="trCheckboxParameter" style="display:none">
		<td nowrap="nowrap">值：</td>
		<td><ext:field property="field.checkboxParameter.value"/></td>
	</tr>
	<tr id="trCheckboxParameter" style="display:none">
		<td nowrap="nowrap">标题：</td>
		<td><ext:field property="field.checkboxParameter.label"/></td>
	</tr>

	<tr id="trDropdownParameter" style="display:none">
		<td nowrap="nowrap">选项列表：</td>
		<td><ext:field property="field.dropdownParameter.itemsText"/></td>
	</tr>
	<tr id="trDropdownParameter" style="display:none">
		<td nowrap="nowrap">获取选项列表的SQL：</td>
		<td><ext:field property="field.dropdownParameter.itemsSql"/></td>
	</tr>
	<tr id="trDropdownParameter" style="display:none">
		<td nowrap="nowrap">获取选项列表的HQL：</td>
		<td><ext:field property="field.dropdownParameter.itemsHql"/></td>
	</tr>
	<tr id="trDropdownParameter" style="display:none">
		<td nowrap="nowrap">获取选项列表的服务名称：</td>
		<td><ext:field property="field.dropdownParameter.itemsServiceName"/></td>
	</tr>
	<tr id="trDropdownParameter" style="display:none">
		<td nowrap="nowrap">选项列表名称：</td>
		<td><ext:field property="field.dropdownParameter.itemsName"/></td>
	</tr>
	<tr id="trDropdownParameter" style="display:none">
		<td nowrap="nowrap">值字段：</td>
		<td><ext:field property="field.dropdownParameter.valueField"/></td>
	</tr>
	<tr id="trDropdownParameter" style="display:none">
		<td nowrap="nowrap">标题字段：</td>
		<td><ext:field property="field.dropdownParameter.titleField"/></td>
	</tr>
	<tr id="trDropdownParameter" style="display:none">
		<td nowrap="nowrap">是否只能选择：</td>
		<td><ext:field property="field.dropdownParameter.selectOnly"/></td>
	</tr>
	<tr id="trDropdownParameter" style="display:none">
		<td nowrap="nowrap">下拉列表宽度：</td>
		<td><ext:field property="field.dropdownParameter.listPickerWidth"/></td>
	</tr>

	<tr id="trSelectParameter" style="display:none">
		<td nowrap="nowrap">需要引入的js文件：</td>
		<td><ext:field property="field.selectParameter.js"/></td>
	</tr>
	<tr id="trSelectParameter" style="display:none">
		<td nowrap="nowrap">选择时执行的脚本：</td>
		<td><ext:field property="field.selectParameter.execute"/></td>
	</tr>
	<tr id="trSelectParameter" style="display:none">
		<td nowrap="nowrap">是否多选：</td>
		<td><ext:field property="field.selectParameter.multiSelect"/></td>
	</tr>
	<tr id="trSelectParameter" style="display:none">
		<td nowrap="nowrap">分隔符：</td>
		<td><ext:field property="field.selectParameter.separator"/></td>
	</tr>
	<tr id="trSelectParameter" style="display:none">
		<td nowrap="nowrap">是否只能选择：</td>
		<td><ext:field property="field.selectParameter.selectOnly"/></td>
	</tr>
	<tr id="trSelectParameter" style="display:none">
		<td nowrap="nowrap">选择按钮样式：</td>
		<td><ext:field property="field.selectParameter.selectButtonStyleClass"/></td>
	</tr>
	
	<tr id="trHiddenParameter" style="display:none">
		<td nowrap="nowrap">只在创建新记录时有效：</td>
		<td><ext:field property="field.hiddenParameter.newFormOnly"/></td>
	</tr>
	
	<tr id="trTimeInputParameter" style="display:none">
		<td nowrap="nowrap">是否要输入秒：</td>
		<td><ext:field property="field.timeInputParameter.secondEnabled"/></td>
	</tr>
	
	<tr id="trAttachmentParameter" style="display:none">
		<td nowrap="nowrap">附件编辑URL：</td>
		<td><ext:field property="field.attachmentParameter.attachmentEditor"/></td>
	</tr>
	<tr id="trAttachmentParameter" style="display:none">
		<td nowrap="nowrap">附件最大大小：</td>
		<td><ext:field property="field.attachmentParameter.maxUploadSize"/></td>
	</tr>
	<tr id="trAttachmentParameter" style="display:none">
		<td nowrap="nowrap">使用的服务名称：</td>
		<td><ext:field property="field.attachmentParameter.serviceName"/></td>
	</tr>
	<tr id="trAttachmentParameter" style="display:none">
		<td nowrap="nowrap">文件后缀名列表：</td>
		<td><ext:field property="field.attachmentParameter.fileExtension"/></td>
	</tr>
	<tr id="trAttachmentParameter" style="display:none">
		<td nowrap="nowrap">附件扩展JS文件：</td>
		<td><ext:field property="field.attachmentParameter.extendJs"/></td>
	</tr>
	<tr id="trAttachmentParameter" style="display:none">
		<td nowrap="nowrap">生成动态的下载链接：</td>
		<td><ext:field property="field.attachmentParameter.dynamicUrl"/></td>
	</tr>
	<tr id="trAttachmentParameter" style="display:none">
		<td nowrap="nowrap">动态附件类型：</td>
		<td><ext:field property="field.attachmentParameter.type"/></td>
	</tr>
	<tr id="trAttachmentParameter" style="display:none">
		<td nowrap="nowrap">启用精简模式：</td>
		<td><ext:field property="field.attachmentParameter.simpleMode"/></td>
	</tr>

	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">图片编辑URL：</td>
		<td><ext:field property="field.imageParameter.attachmentEditor"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">图片最大大小：</td>
		<td><ext:field property="field.imageParameter.maxUploadSize"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">使用的服务名称：</td>
		<td><ext:field property="field.imageParameter.serviceName"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">文件后缀名列表：</td>
		<td><ext:field property="field.imageParameter.fileExtension"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">图片扩展JS文件：</td>
		<td><ext:field property="field.imageParameter.extendJs"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">生成动态的下载链接：</td>
		<td><ext:field property="field.imageParameter.dynamicUrl"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">动态附件类型：</td>
		<td><ext:field property="field.imageParameter.type"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">是否启用精简模式：</td>
		<td><ext:field property="field.imageParameter.simpleMode"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">最大存储字节数：</td>
		<td><ext:field property="field.imageParameter.maxSaveSize"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">裁剪后的图片宽度：</td>
		<td><ext:field property="field.imageParameter.imageClipWidth"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">裁剪后的图片高度：</td>
		<td><ext:field property="field.imageParameter.imageClipHeight"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">裁剪时是否允许切除图片：</td>
		<td><ext:field property="field.imageParameter.clipEnabled"/></td>
	</tr>
	<tr id="trImageParameter" style="display:none">
		<td nowrap="nowrap">允许上传的最大像素(百万)：</td>
		<td><ext:field property="field.imageParameter.maxMegaPixels"/></td>
	</tr>

	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">视频编辑URL：</td>
		<td><ext:field property="field.videoParameter.attachmentEditor"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">视频最大大小：</td>
		<td><ext:field property="field.videoParameter.maxUploadSize"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">使用的服务名称：</td>
		<td><ext:field property="field.videoParameter.serviceName"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">文件后缀名列表：</td>
		<td><ext:field property="field.videoParameter.fileExtension"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">附件扩展JS文件：</td>
		<td><ext:field property="field.videoParameter.extendJs"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">生成动态的下载链接：</td>
		<td><ext:field property="field.videoParameter.dynamicUrl"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">动态附件类型：</td>
		<td><ext:field property="field.videoParameter.type"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">启用精简模式：</td>
		<td><ext:field property="field.videoParameter.simpleMode"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">转换为FLV格式：</td>
		<td><ext:field property="field.videoParameter.convertMp4"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">视频比特率：</td>
		<td><ext:field property="field.videoParameter.videoBitrate"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">帧频：</td>
		<td><ext:field property="field.videoParameter.videoFps"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">视频宽度：</td>
		<td><ext:field property="field.videoParameter.videoWidth"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">视频高度：</td>
		<td><ext:field property="field.videoParameter.videoHeight"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">音频码率：</td>
		<td><ext:field property="field.videoParameter.audioBitrate"/></td>
	</tr>
	<tr id="trVideoParameter" style="display:none">
		<td nowrap="nowrap">音频采样率：</td>
		<td><ext:field property="field.videoParameter.audioFreq"/></td>
	</tr>
</table>
<script>
	function onFieldChanged() {
		var fieldType = document.getElementsByName("field.fieldType")[0].value;
		document.getElementById('trFieldLength').style.display = ',text,date,timestamp,opinion,'.indexOf(',' + fieldType + ',')==-1 ? '' : 'none';
		//设置字段长度下拉列表
		var numbers = "";
		if(fieldType=="number") {
			numbers = "1,0\0 2,0\0 3,0\0 4,0\0 5,0\0 6,0\0 7,0\0 8,0\0 10,0\0 20,0";
		}
		else if(fieldType=="varchar") {
			numbers = "10\0 20\0 30\0 60\0 100\0 1000\0 2000\0 4000";
		}
		else if(',attachment,image,video,'.indexOf(',' + fieldType + ',')!=-1) {
			numbers = "1\0 2\0 5\0 10";
		}
		try {
			DropdownField.setListValues("field.fieldLength", numbers);
		}
		catch(e) {
		
		}
		var dataTypeMapping =  ["varchar", "stringParameter", "文本框|text\0密码|password\0多行文本|textarea\0网页编辑器|htmleditor\0单选框|radio\0复选框|checkbox\0下拉列表|dropdown\0选择对话框|select\0时间|time\0隐藏|hidden\0只读|readonly",
								"text", "", "多行文本|textarea\0网页编辑器|htmleditor",
								"char", "charParameter", "文本框|text\0单选框|radio\0复选框|checkbox\0下拉列表|dropdown\0隐藏|hidden\0只读|readonly",
								"number", "numberParameter", "文本框|text\0密码|password\0单选框|radio\0复选框|checkbox\0下拉列表|dropdown\0选择对话框|select\0隐藏|hidden\0只读|readonly",
								"date", "dateParameter", "日期|date\0隐藏|hidden\0只读|readonly",
								"timestamp", "timestampParameter", "日期和时间|datetime\0日期|date\0隐藏|hidden\0只读|readonly",
								"attachment", "", "附件上传|attachment",
								"image", "", "图片上传|image",
								"video", "", "视频上传|video",
								"opinion", "opinionParameter", ""];
		var inputModeMapping = ["text", "", "password", "", "textarea", "textareaParameter", "htmleditor", "htmlEditorParameter", "radio", "radioParameter",
								"checkbox", "checkboxParameter", "dropdown", "dropdownParameter", "select", "selectParameter", "date", "", "datetime", "",
								"time", "timeInputParameter", "hidden", "hiddenParameter", "readonly", "",
								"attachment", "attachmentParameter", "image", "imageParameter", "video", "videoParameter"];
		var inputModes = ''; //输入方式列表
		var dataTypeParameter = ''; //数据类型对应的参数
		for(var i=0; i<dataTypeMapping.length; i+=3) {
			if(dataTypeMapping[i]==fieldType) {
				dataTypeParameter = dataTypeMapping[i+1];
				inputModes = dataTypeMapping[i+2];
				break;
			}
		}
		DropdownField.setListValues("field.inputMode", inputModes);
		var inputMode = document.getElementsByName('field.inputMode')[0].value; //输入方式
		if((inputModes + "\0").indexOf(inputMode)==-1) {
			if(inputModes.indexOf('\0')!=-1) { //不只一种输入方式
				document.getElementsByName('field.inputMode')[0].value = '';
				document.getElementsByName('field.inputMode_title')[0].value = '';
			}
			else { //仅一种输入方式
				document.getElementsByName('field.inputMode')[0].value = inputModes.split("|")[1];
				document.getElementsByName('field.inputMode_title')[0].value = inputModes.split("|")[0];
			}
		}
		inputMode = document.getElementsByName('field.inputMode')[0].value; //输入方式
		document.getElementById('trInputMode').style.display = inputModes=='' ? 'none' : '';
		var inputModeParameter = ''; //输入方式对应的参数
		for(var i=0; i<inputModeMapping.length; i+=2) {
			if(inputModeMapping[i]==inputMode) {
				inputModeParameter = inputModeMapping[i+1];
				break;
			}
		}
		for(var tr=document.getElementById("trStringParameter"); tr && tr.id; tr=tr.nextSibling) {
			tr.style.display = (tr.id.substring(2).toLowerCase()==dataTypeParameter.toLowerCase() || tr.id.substring(2).toLowerCase()==inputModeParameter.toLowerCase() ? "" : "none");
		}
		onStringDefaultValueChanged();
		onNumberDefaultValueChanged();
		DialogUtils.adjustDialogSize();
	}
	function onStringDefaultValueChanged() { //文本默认值修改
		document.getElementById('trStringParameter_numerationFormat').style.display = document.getElementById('trStringParameter').style.display!='none' && document.getElementsByName('field.stringParameter.defaultValue')[0].value=='{NUMERATION}' ? '' : 'none'; 
		DialogUtils.adjustDialogSize();
	}
	function onNumberDefaultValueChanged() { //数字默认值修改
		document.getElementById('trNumberParameter_numerationFormat').style.display = document.getElementById('trNumberParameter').style.display!='none' && document.getElementsByName('field.numberParameter.defaultValue')[0].value=='{NUMERATION}' ? '' : 'none'; 
		DialogUtils.adjustDialogSize();
	}
	function formOnSubmit() {
		if(FieldValidator.validateStringField(document.getElementsByName("field.name")[0], "&,?,=,\",'", true, "名称")=="NaN") {
			return false;
		}
		var englishName = document.getElementsByName("field.englishName")[0].value;
		if(englishName=="") {
			alert("英文名称不能为空");
			return false;
		}
		if(englishName.replace(/[\dA-Za-z]/gi, "")!="") {
			alert("英文名称必须由数字和字母组成");
			return false;
		}
		if(",select,from,order,group,by,null,where,and,or,not,is,".indexOf("," + englishName + ",")!=-1) {
			alert("不允许使用数据库关键字作为英文名称");
			return false;
		}
		if(englishName.charAt(0)>='0' && englishName.charAt(0)<='9') {
			alert("英文名称不能以数字开头");
			return false;
		}
		//检查输入方式是否设定
		var fieldType = document.getElementsByName("field.fieldType")[0].value;
		if(fieldType!="opinion" && document.getElementsByName('field.inputMode')[0].value=="") {
			alert("输入方式未设置");
			return false;
		}
		//检查字段长度是否设置正确
		var fieldLength = document.getElementsByName("field.fieldLength")[0].value;
		if(",varchar,number,".indexOf("," + fieldType + ",")!=-1) {
			if(fieldLength=="") {
				alert("字段长度未设置");
				return false;
			}
		}
		if(fieldType=="number") { //数字
			var values = fieldLength.split(",");
			if(values.length>2) {
				alert("字段长度设置不正确");
				return false;
			}
			if(values.length==1 && Number(fieldLength)!=fieldLength) {
				alert("字段长度设置不正确");
				return false;
			}
			if(values.length==2 && ("" + Number(values[0])!=values[0] || "" + Number(values[1])!=values[1] || Number(values[0])<Number(values[1]))) {
				alert("字段长度设置不正确");
				return false;
			}
		}
		else if(",varchar,attachment,image,video,".indexOf("," + fieldType + ",")!=-1 && fieldLength!="") {
			if("" + Number(fieldLength)!=fieldLength) {
				alert("字段长度设置不正确");
				return false;
			}
			if(Number(fieldLength)>=4000) {
				alert("字段长度不允许超过4000");
				return false;
			}
		}
		return true;
	}
	onFieldChanged();
</script>