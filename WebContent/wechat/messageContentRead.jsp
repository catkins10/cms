<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<tr>
	<td class="tdtitle" nowrap="nowrap">消息类型</td>
	<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
</tr>
<ext:equal value="video" property="type">
	<tr>
		<td class="tdtitle" nowrap="nowrap">消息标题</td>
		<td class="tdcontent"><ext:field writeonly="true" property="title"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">消息描述</td>
		<td class="tdcontent"><ext:field writeonly="true" property="description"/></td>
	</tr>
</ext:equal>
<ext:equal value="text" property="type">
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">消息内容</td>
		<td class="tdcontent"><ext:field writeonly="true" property="content"/></td>
	</tr>
</ext:equal>
<ext:equal value="image" property="type">
	<tr>
		<td class="tdtitle" nowrap="nowrap">图片</td>
		<td class="tdcontent"><ext:field writeonly="true" property="image"/></td>
	</tr>
</ext:equal>
<ext:equal value="voice" property="type">
	<tr>
		<td class="tdtitle" nowrap="nowrap">语音</td>
		<td class="tdcontent"><ext:field writeonly="true" property="voice"/></td>
	</tr>
</ext:equal>
<ext:equal value="video" property="type">
	<tr>
		<td class="tdtitle" nowrap="nowrap">视频</td>
		<td class="tdcontent"><ext:field writeonly="true" property="video"/></td>
	</tr>
</ext:equal>
<ext:equal value="news" property="type">
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">封面</td>
			<td class="tdcontent"><ext:field writeonly="true" property="showCoverPic"/></td>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">图文消息</td>
		<td class="tdcontent">
			<ext:notEmpty property="news">
				<div style="display: inline-block; border: #c1c1c1 1px solid; background-color: #ffffff; padding:10px 10px 10px 10px; width:272px !important; width:292px;">
					<ext:iterate id="news" indexId="newsIndex" property="news">
						<ext:equal value="0" name="newsIndex">
							<ext:sizeEqual value="1" property="news">
								<div onclick="editNews('<ext:write name="news" property="id"/>')">
									<div style="font-size: 18px; line-height:24px; font-family:微软雅黑,黑体;">
										<ext:write name="news" property="title" maxCharCount="56"/>
									</div>
									<div style="font-size: 14px; font-family:微软雅黑,黑体; color:#909090; line-height: 40px">
										<%=new java.text.SimpleDateFormat("M月d日").format(com.yuanluesoft.jeaf.util.DateTimeUtils.date())%>
									</div>
									<div style="cursor: pointer; width:270px; height:150px; background-color:#c0c0c0;"><ext:iterateAttachment id="image" indexId="imageIndex" applicationName="wechat" attachmentType="image" nameRecordId="news" propertyRecordId="id"><img border="0" width="270" height="150" src="<ext:write name="image" property="urlInline"/>"></ext:iterateAttachment></div>
									<div style="font-size: 14px; font-family:微软雅黑,黑体; color:#909090; line-height: 18px; padding-top: 8px;">
										<ext:write name="news" property="description"/>
									</div>
								</div>
							</ext:sizeEqual>
							<ext:sizeNotEqual value="1" property="news">
								<div onclick="editNews('<ext:write name="news" property="id"/>')" style="cursor: pointer; width:270px; height:150px; background-color:#c0c0c0;"><ext:iterateAttachment id="image" indexId="imageIndex" applicationName="wechat" attachmentType="image" nameRecordId="news" propertyRecordId="id"><img border="0" width="270" height="150" src="<ext:write name="image" property="urlInline"/>"></ext:iterateAttachment></div>
								<div style="background-color: #000000; margin-top: -32px; width:270px; height: 32px; filter: Alpha(Opacity=40); opacity: 0.4; -moz-opacity: 0.4;">
									&nbsp;								
								</div>
								<div onclick="editNews('<ext:write name="news" property="id"/>')" style="cursor: pointer; padding: 0px 3px 0px 3px; color: #ffffff; font-size: 18px; font-family:微软雅黑,黑体; font-weigth: bold;  margin-top: -32px; width:270px; height: 32px; line-height: 32px;">
									<ext:write name="news" property="title" maxCharCount="28"/>
								</div>
							</ext:sizeNotEqual>
						</ext:equal>
						<ext:notEqual value="0" name="newsIndex">
							<div onclick="editNews('<ext:write name="news" property="id"/>')" style="cursor: pointer; width:270px; height:50px; margin-top: 8px; padding: 8px 0px 0px 0px; border-top:#e1e1e1 1px solid;">
								<div style="float:left; width:218px; height:50px; color:#000000; font-size: 16px; font-family:微软雅黑,黑体; line-height: 24px; font-weigth: bold;">
									<ext:write name="news" property="title" maxCharCount="52"/>
								</div>
								<div style="float:right; width:50px; height:50px; background-color:#c0c0c0;"><ext:iterateAttachment id="image" indexId="imageIndex" applicationName="wechat" attachmentType="image" nameRecordId="news" propertyRecordId="id"><img border="0" width="50" height="50" src="<ext:write name="image" property="urlInline"/>"></ext:iterateAttachment></div>
							</div>
						</ext:notEqual>
					</ext:iterate>
				</div>
			</ext:notEmpty>
			<script>
				function editNews(id) {
					PageUtils.editrecord('wechat', '<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getName()%>News', '<ext:write property="id"/>', 'mode=dialog,width=500,height=300', '', 'messageNews.id=' + id);
				}
			</script>
		</td>
	</tr>
</ext:equal>