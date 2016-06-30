<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("colspan", "3");%>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
		<td class="tdtitle" nowrap="nowrap">简称(英文)</td>
		<td class="tdcontent"><ext:field property="shortName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主机名</td>
		<td class="tdcontent"><ext:field property="hostName"/></td>
		<td class="tdtitle" nowrap="nowrap">LOGO</td>
		<td class="tdcontent"><ext:field property="logo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">描述</td>
		<td class="tdcontent" colspan="3"><ext:field property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否内部网站</td>
		<td class="tdcontent"><ext:field property="isInternal"/></td>
		<td class="tdtitle" nowrap="nowrap">隶属单位</td>
		<td class="tdcontent"><ext:field property="ownerUnitName"/></td>
	</tr>
	<ext:notEqual value="0" property="id">
		<tr>
			<td class="tdtitle" nowrap="nowrap">上级站点</td>
			<td class="tdcontent">
				<ext:equal value="true" property="changeParentDirectoryDisabled">
					<ext:field writeonly="true" property="parentDirectoryName"/>
				</ext:equal>
				<ext:equal value="false" property="changeParentDirectoryDisabled">
					<ext:field property="parentDirectoryName"/>
				</ext:equal>
			</td>
			<td class="tdtitle" nowrap="nowrap">同步到其他栏目</td>
			<td class="tdcontent"><ext:field property="synchToDirectoryNames"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文章发布流程</td>
		<td class="tdcontent"><ext:field property="workflowName"/></td>
		<td class="tdtitle" nowrap="nowrap">当文章不属于本站时</td>
		<td class="tdcontent"><ext:field property="useSiteTemplate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">站点编辑删除文章</td>
		<td class="tdcontent"><ext:field property="editorDeleteable"/></td>
		<td class="tdtitle" nowrap="nowrap">站点编辑撤销文章</td>
		<td class="tdcontent"><ext:field property="editorReissueable"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">匿名用户资源访问级别</td>
		<td class="tdcontent"><ext:field property="anonymousLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">同步的文章发布</td>
		<td class="tdcontent"><ext:field property="synchIssue"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">水印图片</td>
		<td class="tdcontent"><ext:field property="waterMark"/></td>
		<td class="tdtitle" nowrap="nowrap">水印位置</td>
		<td class="tdcontent"><ext:field property="waterMarkAlign"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">水印水平边距</td>
		<td class="tdcontent"><ext:field property="waterMarkXMargin"/></td>
		<td class="tdtitle" nowrap="nowrap">水印垂直边距</td>
		<td class="tdcontent"><ext:field property="waterMarkYMargin"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">视频播放器LOGO</td>
		<td class="tdcontent" colspan="3"><ext:field property="videoPlayerLogo"/></td>
	</tr>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
</table>