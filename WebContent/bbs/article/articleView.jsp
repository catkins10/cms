<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<table style="table-layout:fixed" class="view" width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td class="viewHeader" width="43px"></td>
		<td class="viewHeader">主题</td>
		<td class="viewHeader" width="50px"></td>
		<td class="viewHeader" width="100px">作者</td>
		<td class="viewHeader" width="80px" align="center">回复/查看</td>
		<td class="viewHeader" width="100px">最后回复</td>
	</tr>
	<ext:iterate id="record" property="viewPackage.records">
		<tr valign="middle" style="padding-left:1px;padding-right:1px;padding-top:1px;padding-bottom:1px">
			<td class="viewData" align="center">
				<a href="../article/viewArticle.shtml?id=<ext:write name="record" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank">
					<img src="<%=request.getContextPath()%>/bbs/article/image/article<ext:equal value="true" name="record" property="newReply">_new</ext:equal>.gif" border="0">
				</a>
			</td>
			<td class="viewData">
				<a href="../article/viewArticle.shtml?id=<ext:write name="record" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank">
					<ext:write name="record" property="subject"/>
					<ext:iterateAttachment id="attachment" length="1" applicationName="bbs/article" attachmentType="attachment" nameRecordId="record" propertyRecordId="id">
						<img src="<%=request.getContextPath()%>/bbs/article/image/attachment.gif" border="0" align="absmiddle"/>
					</ext:iterateAttachment>
				</a>
			</td>
			<td class="viewData">
				&nbsp;
			</td>
			<td class="viewData">
				<a href="../usermanage/member.shtml?act=open&id=<ext:write name="record" property="creatorId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank">
					<ext:write name="record" property="creatorNickname"/><br>
				</a>
				<ext:write name="record" property="created" format="yyyy-M-d H:mm"/>
			</td>
			<td class="viewData" align="center">
				<ext:write name="record" property="replyCount"/>/<ext:write name="record" property="accessTimes"/>
			</td>
			<td class="viewData">
				<ext:notEmpty name="record" property="lastReply">
					<a href="../usermanage/member.shtml?act=open&id=<ext:write name="record" property="lastReply.creatorId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank">
						<ext:write name="record" property="lastReply.creatorNickname"/><br>
					</a>
					<ext:write name="record" property="lastReply.created" format="yyyy-M-d H:mm"/>
				</ext:notEmpty>
				<ext:empty name="record" property="lastReply">
					&nbsp;
				</ext:empty>
			</td>
		</tr>
	</ext:iterate>
</table>