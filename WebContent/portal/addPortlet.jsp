<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doAddPortlet">
   	<table width="100%" height="100%" border="0" cellpadding="3" cellspacing="0">
		<col>
		<col>
		<col>
		<col>
		<col width="100%">
		<tr>
			<td colspan="4">选取内容：</td>
		</tr>
		<tr height="100%">
			<td colspan="5">
				<div style="width:100%; height: 100%; border: #bbbbbb 1px solid; background-color: #ffffff; overflow-y: auto;">
					<ext:iterate id="portletGroup" property="portletGroups">
						<div style="cursor:pointer; clear:both; padding: 8px 5px 5px 5px; font-weight: bold;" onclick="expandPortletEntities(this, '<ext:write name="portletGroup" property="group"/>')">
							<img src="icons/expand.gif" align="absmiddle">
							<ext:write name="portletGroup" property="group"/>
						</div>
						<div id="portletEntities_<ext:write name="portletGroup" property="group"/>" style="display:none; padding: 5px 5px 5px 15px">
							<ext:iterate id="portletEntity" name="portletGroup" property="portletEntities">
								<table border="0" cellpadding="3" cellspacing="0" style="float:left; width: 200px; height: 130px; border: #cccccc 1px solid; margin-right: 5px; margin-bottom: 5px;">
									<tr>
										<td colspan="2" height="22px"  style="background-color: #d0e0ff; border-bottom: #cccccc 1px solid; font-weight: bold; color:#000000; padding-top: 3px">
											<ext:write name="portletEntity" property="title"/>
										</td>
									</tr>
									<tr valign="top">
										<td colspan="2" height="100%">
											<img src="icons/info.png" style="float:left; margin-right: 3px;">
											<p style="margin-top: 5px; line-height: 16px;" title="<ext:write name="portletEntity" property="description"/>"><ext:write name="portletEntity" property="description" maxCharCount="116" ellipsis="..."/></p>
										</td>
									</tr>
									<tr>
										<td height="30px">
											<span style="cursor: pointer; margin-left: 5px" onclick="previewPortlet('<ext:write name="portletEntity" property="wsrpProducerId"/>', '<ext:write name="portletEntity" property="handle"/>', '<ext:write name="portletEntity" property="title"/>')">
												<img src="icons/preview.png" align="absmiddle"">
												预览
											</span>
										</td>
										<td align="right">
											<ext:equal value="false" name="portletEntity" property="added">
												<span style="cursor: pointer; margin-right: 5px" onclick="addPortletEntity(this)">
													<input style="display:none" type="checkbox" name="selectedWsrpProducerIds" value="<ext:write name="portletEntity" property="wsrpProducerId"/>"/>
													<input style="display:none" type="checkbox" name="selectedPortletHandles" value="<ext:write name="portletEntity" property="handle"/>"/>
													<input style="display:none" type="checkbox" name="selectedPortletTitles" value="<ext:write name="portletEntity" property="title"/>"/>
													<span>添加</span>
													<img src="icons/add.png" align="absmiddle"">
												</span>
											</ext:equal>
											<ext:equal value="true" name="portletEntity" property="added">
												<span style="margin-right: 5px"">
													<span>已添加</span>
													<img src="icons/added.png" align="absmiddle"">
												</span>
											</ext:equal>
										</td>
									</tr>
								</table>
							</ext:iterate>
						</div>
					</ext:iterate>
					<div style="clear:both; height:3px"></div>
				</div>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">添加到第</td>
			<td nowrap="nowrap"><ext:field property="columnIndex" style="width:30px"/></td>
			<td nowrap="nowrap" align="right">列</td>
			<td nowrap="nowrap" align="right">&nbsp;&nbsp;小窗口风格：</td>
			<td id="tdPortletStyle"><ext:field property="portletStyle" style="width:100px"/></td>
		</tr>
	</table>
	<script language="javascript">
		//设置列号列表
		var portal = DialogUtils.getDialogOpener().portal;
		var columnCount = portal.getColumnCount();
		var listValues = "";
		for(var i=1; i<=columnCount; i++) {
			listValues += i + (i<columnCount ? "\0" : "");
		}
		DropdownField.setListValues("columnIndex", listValues); //修改下拉列表选项
		
		//设置窗口风格列表
		listValues = "";
		for(var i=0; i<portal.portletStyles.length; i++) {
			listValues += portal.portletStyles[i] + (i<portal.portletStyles.length-1 ? "\0" : "");
		}
		DropdownField.setListValues("portletStyle", listValues); //修改下拉列表选项
		DropdownField.setValue("portletStyle", portal.portletStyles[0]); //设置值
		
		//展开或者收起PORTLET实体列表
		function expandPortletEntities(src, group) {
			var divPortletEntities = document.getElementById('portletEntities_' + group);
			var img = src.getElementsByTagName("img")[0];
			if(divPortletEntities.style.display=='none') {
				divPortletEntities.style.display = '';
				img.src = "icons/collapse.gif";
			}
			else {
				divPortletEntities.style.display = 'none';
				img.src = "icons/expand.gif";
			}
		}
		
		//预览PORTLET
		function previewPortlet(wsrpProducerId, portletHandle, portletTitle) {
			var url = DialogUtils.getDialogOpener().location.href;
			url += (url.indexOf('?')==-1 ? '?' : '&') + "previewPortlet=true" +
				   "&wsrpProducerId=" + wsrpProducerId +
				   "&portletHandle=" + portletHandle +
				   "&portletTitle=" + StringUtils.utf8Encode(portletTitle) +
				   "&portletStyle=" + StringUtils.utf8Encode(document.getElementsByName("portletStyle")[0].value);
			DialogUtils.openDialog(url, 600, 400, portletTitle);
		}
		
		//添加PORTLET
		function addPortletEntity(src) {
			var checkbox = src.getElementsByTagName("input");
			var label = src.getElementsByTagName("span")[0];
			var img = src.getElementsByTagName("img")[0];
			var checked;
			if(checkbox[0].checked) {
				checked = false;
				label.innerHTML = "添加";
				img.src = "icons/add.png";
			}
			else {
				checked = true;
				label.innerHTML = "取消";
				img.src = "icons/added.png";
			}
			for(var i=0; i<checkbox.length; i++) {
				checkbox[i].checked = checked;
			}
		}
	</script>
</ext:form>