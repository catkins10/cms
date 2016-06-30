<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<table style="table-layout:fixed" width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td class="viewHeader" colspan="2">
			&nbsp;<ext:write property="article.subject"/>
		</td>
	</tr>
</table>
<ext:equal value="1" property="viewPackage.curPage">
	<table style="table-layout:fixed" class="view" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr class="viewRow" valign="top">
			<td class="authorCol">
				<a href="../usermanage/member.shtml?act=open&id=<ext:write property="article.creatorId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank"><ext:write property="article.creatorNickname"/></a>　
				<hr size="1" class="authorLine">
				<center style="padding-bottom:5px"><ext:personPortrait propertyPersonId="article.author.id" width="100" height="100" autoHeight="true"/></center>
				<ext:notEqual value="-1" property="article.creatorId">
					发　　帖: <ext:write property="article.author.postCount"/><br>
					回　　帖: <ext:write property="article.author.replyCount"/><br>
					注册时间: <ext:write property="article.author.created" format="yyyy-MM-dd HH:mm"/><br>
					最后登录: <ext:write property="article.author.lastLoginTime" format="yyyy-MM-dd HH:mm"/>
				</ext:notEqual>
			</td>
			<td class="bodyCol">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td nowrap="nowrap">发表于 <ext:write property="article.created" format="yyyy-M-d H:m"/></td>
						<td nowrap="nowrap" align="right">#1</td>
					</tr>
				</table>
				<hr size="1" class="bodyLine">
				<div class="content">
					<div class="articleSubject"><ext:write property="article.subject" filter="false"/></div>
					<ext:write property="article.body" filter="false"/>
					<ext:iterateAttachment id="" length="1" applicationName="bbs/article" propertyRecordId="article.id" attachmentType="attachment">
						<br><br><img src="<%=request.getContextPath()%>/bbs/article/image/attachment.gif" border="0" align="absmiddle"/>附件：
						<ext:iterateAttachment id="attachment" indexId="attachmentIndex" applicationName="bbs/article" attachmentType="attachment" propertyRecordId="article.id">
							<br>&nbsp;<a href="<ext:write name="attachment" property="urlAttachment"/>"><ext:write name="attachment" property="title"/></a>
						</ext:iterateAttachment>
					</ext:iterateAttachment>
				</div>
				<hr size="1" class="bodyLine">
				<ext:equal value="false" property="manager">
					<ext:equal property="article.creatorId" nameCompare="SessionInfo" propertyCompare="userId">
						<div style="float:left;cursor:pointer">
							<span onclick="if(confirm('删除后不可恢复，是否确定删除'))location.href='deleteArticle.shtml?id=<ext:write property="article.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>'" style="cursor:pointer;background-image:url(<%=request.getContextPath()%>/bbs/article/image/delete.gif);background-repeat:no-repeat;padding-left:20px;padding-top:2px;height:20px">
								删除
							</span>
							&nbsp;
							<span onclick="location.href='article.shtml?act=edit&id=<ext:write property="article.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>'" style="cursor:pointer;background-image:url(<%=request.getContextPath()%>/bbs/article/image/edit.gif);background-repeat:no-repeat;padding-left:20px;padding-top:2px;height:20px">
								修改
							</span>
						</div>
					</ext:equal>
				</ext:equal>
				<ext:equal value="true" property="manager">
					<div style="float:left;cursor:pointer">
						<span onclick="if(confirm('删除后不可恢复，是否确定删除'))location.href='deleteArticle.shtml?id=<ext:write property="article.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>'" style="cursor:pointer;background-image:url(<%=request.getContextPath()%>/bbs/article/image/delete.gif);background-repeat:no-repeat;padding-left:20px;padding-top:2px;height:20px">
							删除
						</span>
						&nbsp;
						<span onclick="location.href='article.shtml?act=edit&id=<ext:write property="article.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>'" style="cursor:pointer;background-image:url(<%=request.getContextPath()%>/bbs/article/image/edit.gif);background-repeat:no-repeat;padding-left:20px;padding-top:2px;height:20px">
							修改
						</span>
					</div>
				</ext:equal>
				<div style="float:right;cursor:pointer" onclick=scroll(0,0)>TOP</div>
			</td>
		</tr>
	</table>
	<div style="height:8px;font-size:1px;line-height:1px">&nbsp;</div>
</ext:equal>

<ext:iterate id="articleReply" property="viewPackage.records">
	<table style="table-layout:fixed" class="view" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr class="viewRow" valign="top">
			<td class="authorCol">
				<a href="../usermanage/member.shtml?act=open&id=<ext:write name="articleReply" property="creatorId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank"><ext:write name="articleReply" property="creatorNickname"/></a>　
				<hr size="1" class="authorLine">
				<center style="padding-bottom:5px"><ext:personPortrait namePersonId="articleReply" propertyPersonId="author.id" width="100" height="100" autoHeight="true"/></center>
				<ext:notEqual value="-1" name="articleReply" property="creatorId">
					发　　帖: <ext:write name="articleReply" property="author.postCount"/><br>
					回　　帖: <ext:write name="articleReply" property="author.replyCount"/><br>
					注册时间: <ext:write name="articleReply" property="author.created" format="yyyy-MM-dd HH:mm"/><br>
					最后登录: <ext:write name="articleReply" property="author.lastLoginTime" format="yyyy-MM-dd HH:mm"/>
				</ext:notEqual>
			</td>
			<td class="bodyCol">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td nowrap="nowrap">发表于 <ext:write name="articleReply" property="created" format="yyyy-M-d H:m"/></td>
						<td nowrap="nowrap" align="right">#<ext:writeNumber property="viewPackage.rowNum" plus="1"/></td>
					</tr>
				</table>
				<hr size="1" class="bodyLine">
				<div class="content">
					<div class="articleSubject"><ext:write name="articleReply" property="subject"/></div>
					<ext:write name="articleReply" property="body" filter="false"/>
					<ext:iterateAttachment id="" length="1" applicationName="bbs/article" nameRecordId="articleReply" propertyRecordId="id" attachmentType="attachment">
						<br><br><img src="<%=request.getContextPath()%>/bbs/article/image/attachment.gif" border="0" align="absmiddle"/>附件：
						<ext:iterateAttachment id="attachment" indexId="attachmentIndex" applicationName="bbs/article" nameRecordId="articleReply" attachmentType="attachment" propertyRecordId="id">
							<br>&nbsp;<a href="<ext:write name="attachment" property="urlAttachment"/>"><ext:write name="attachment" property="title"/></a>
						</ext:iterateAttachment>
					</ext:iterateAttachment>
				</div>
				<hr size="1" class="bodyLine">
				<ext:equal value="false" property="manager">
					<ext:equal name="articleReply" property="creatorId" nameCompare="SessionInfo" propertyCompare="userId">
						<div style="float:left;cursor:pointer">
							<span onclick="if(confirm('删除后不可恢复，是否确定删除'))location.href='deleteReply.shtml?id=<ext:write name="articleReply" property="id"/>&articleId=<ext:write property="article.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>'" style="cursor:pointer;background-image:url(<%=request.getContextPath()%>/bbs/article/image/delete.gif);background-repeat:no-repeat;padding-left:20px;padding-top:2px;height:20px">
								删除
							</span>
							&nbsp;
							<span onclick="location.href='reply.shtml?act=edit&id=<ext:write name="articleReply" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>'" style="cursor:pointer;background-image:url(<%=request.getContextPath()%>/bbs/article/image/edit.gif);background-repeat:no-repeat;padding-left:20px;padding-top:2px;height:20px">
								修改
							</span>
						</div>
					</ext:equal>
				</ext:equal>
				<ext:equal value="true" property="manager">
					<div style="float:left;cursor:pointer">
						<span onclick="if(confirm('删除后不可恢复，是否确定删除'))location.href='deleteReply.shtml?id=<ext:write name="articleReply" property="id"/>&articleId=<ext:write property="article.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>'" style="cursor:pointer;background-image:url(<%=request.getContextPath()%>/bbs/article/image/delete.gif);background-repeat:no-repeat;padding-left:20px;padding-top:2px;height:20px">
							删除
						</span>
						&nbsp;
						<span onclick="location.href='reply.shtml?act=edit&id=<ext:write name="articleReply" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>'" style="cursor:pointer;background-image:url(<%=request.getContextPath()%>/bbs/article/image/edit.gif);background-repeat:no-repeat;padding-left:20px;padding-top:2px;height:20px">
							修改
						</span>
					</div>
				</ext:equal>
				<div style="float:right;cursor:pointer" onclick=scroll(0,0)>TOP</div>
			</td>
		</tr>
	</table>
	<div style="height:8px;font-size:1px;line-height:1px">&nbsp;</div>
</ext:iterate>