<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="../js/forum.js"></script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" class="tdtitle">名称</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">描述</td>
		<td class="tdcontent"><ext:field property="description"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">每页显示主题数</td>
		<td class="tdcontent"><ext:field property="pageArticles"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">每贴显示的回复数</td>
		<td class="tdcontent"><ext:field property="pageReplies"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">匿名访问</td>
		<td class="tdcontent"><ext:field property="anonymousEnabled"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">匿名回复</td>
		<td class="tdcontent"><ext:field property="anonymousReply"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">匿名发帖</td>
		<td class="tdcontent"><ext:field property="anonymousCreate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">匿名用户下载附件</td>
		<td class="tdcontent"><ext:field property="anonymousDownload"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">仅允许VIP访问</td>
		<td class="tdcontent"><ext:field property="vipOnly"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">仅允许版主发帖</td>
		<td class="tdcontent"><ext:field property="managerCreateOnly"/></td>
	</tr>
	<ext:notEqual value="0" property="id">
		<tr>
			<td nowrap="nowrap" class="tdtitle">上级</td>
			<td class="tdcontent">
				<ext:equal value="true" property="changeParentDirectoryDisabled">
					<ext:field writeonly="true" property="parentDirectoryName"/>
				</ext:equal>
				<ext:equal value="false" property="changeParentDirectoryDisabled">
					<ext:field property="parentDirectoryName"/>
				</ext:equal>
			</td>
		</tr>
	</ext:notEqual>
	<jsp:include page="popedomConfigEdit.jsp"/>
</table>