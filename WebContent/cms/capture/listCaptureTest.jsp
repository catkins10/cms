<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/listCaptureTest">
	<script>
		function recordCapture(url) {
			DialogUtils.openDialog('<%=request.getContextPath()%>/cms/capture/recordCaptureTest.shtml?id=' + document.getElementsByName('id')[0].value + '&url=' + StringUtils.utf8Encode(url), 800, 500);
		}
		function nextPageCapture(url) {
			location.href = 'listCaptureTest.shtml?id=' + document.getElementsByName('id')[0].value + '&url=' + StringUtils.utf8Encode(url) + '&pageIndex=' + (Number(document.getElementsByName('pageIndex')[0].value) + 1) + '&displayMode=dialog';
		}
	</script>
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr align="center">
			<td width="36px" nowrap="nowrap" class="tdtitle">序号</td>
			<td width="100%" class="tdtitle">记录</td>
		</tr>
		<ext:notEmpty property="capturedRecordList">
			<ext:iterate id="capturedRecord" indexId="capturedRecordIndex" property="capturedRecordList.records">
				<tr>
					<td class="tdcontent" align="center" nowrap="true" valign="top">
						<ext:writeNumber name="capturedRecordIndex" plus="1"/>
					</td>
					<td class="tdcontent">
						<table width="100%" border="0" cellpadding="2" cellspacing="0">
							<ext:iterate id="field" indexId="fieldIndex" name="capturedRecord" property="fields">
								<tr valign="top">
									<td nowrap="nowrap" align="right"><ext:write name="field" property="fieldTitle"/>：</td>
									<td style="word-break: break-all;" width="100%">
<%										request.setAttribute("field", org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, "field", null, "page"));
										request.setAttribute("record", org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, "capturedRecord", null, "page")); %>
										<jsp:include page="writeCaptureField.jsp"/>
									</td>
								</tr>
							</ext:iterate>
							<ext:notEmpty name="capturedRecord" property="url">
								<tr valign="top">
									<td nowrap="nowrap" align="right">记录链接：</td>
									<td style="word-break: break-all;" width="100%">
										<a href="<ext:write name="capturedRecord" property="url" filter="false"/>" target="_blank"><ext:write name="capturedRecord" property="url"/></a>
										<a href="#" onclick="recordCapture('<ext:write name="capturedRecord" property="url" filter="false"/>')">抓取</a>
									</td>
								</tr>
							</ext:notEmpty>
						</table>
					</td>
				</tr>
			</ext:iterate>
			<ext:notEmpty property="capturedRecordList.nextPageURL">
				<tr>
					<td class="tdcontent" align="center" nowrap="true"></td>
					<td class="tdcontent" style="word-break: break-all;">下一页：
						<a href="<ext:write property="capturedRecordList.nextPageURL" filter="false"/>" target="_blank"><ext:write property="capturedRecordList.nextPageURL"/></a>
						<a href="#" onclick="nextPageCapture('<ext:write property="capturedRecordList.nextPageURL" filter="false"/>')">抓取</a>
					</td>
				</tr>
			</ext:notEmpty>
		</ext:notEmpty>
	</table>
	<html:hidden property="pageIndex"/>
</ext:form>