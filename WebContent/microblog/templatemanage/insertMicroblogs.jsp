<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertMicroblogs">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			return "微博列表:" + document.getElementsByName("microblogAccountName")[0].value;
		}
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties!="") {
				document.getElementsByName("microblogAccountName")[0].value = StringUtils.getPropertyValue(recordListProperties, "microblogAccountName");
				document.getElementsByName("microblogAccountId")[0].value = StringUtils.getPropertyValue(recordListProperties, "microblogAccountId");
			}
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			var microblogAccountName = document.getElementsByName("microblogAccountName")[0].value;
			if(microblogAccountName=='') {
				alert("微博帐号不能为空");
				return "ERROR";
			}
			return "microblogAccountName=" + microblogAccountName +
				   "&microblogAccountId=" + document.getElementsByName("microblogAccountId")[0].value;
		}
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return '<DIV style="MARGIN-BOTTOM: 6px">' +
				   '	<A id=field urn=name=content&amp;link=true>&lt;微博信息内容&gt;</A>' +
				   '</DIV>' +
				   '<DIV id=microblogImages width="80px" height="80px">&lt;微博配图&gt;</DIV>' +
				   '<DIV><A id=recordList urn=\'recordListName=retweetedMicroblogs&amp;privateRecordList=true&amp;recordClassName=com.yuanluesoft.microblog.model.Microblog&amp;applicationName=microblog&amp;recordCount=1&amp;recordFormat=<DIV style%3D"BORDER-BOTTOM: #d9d9d9 1px solid; BORDER-LEFT: #d9d9d9 1px solid; PADDING-BOTTOM: 6px; BACKGROUND-COLOR: #f2f2f2; MARGIN-TOP: 5px; PADDING-LEFT: 6px; PADDING-RIGHT: 6px; BORDER-TOP: #d9d9d9 1px solid; BORDER-RIGHT: #d9d9d9 1px solid; PADDING-TOP: 6px; border-radius: 3px"><SPAN style%3D"POSITION: absolute; MARGIN-TOP: -12px; FONT-FAMILY: SimSun; COLOR: #d9d9d9; MARGIN-LEFT: 15px; FONT-SIZE: 12px">◆</SPAN><SPAN style%3D"POSITION: absolute; MARGIN-TOP: -11px; FONT-FAMILY: SimSun; COLOR: #f2f2f2; MARGIN-LEFT: 15px; FONT-SIZE: 12px">◆</SPAN><B>@<A id%3Dfield urn%3Dname%3Duser.nickname%26amp;link%3Dtrue>%26lt;用户_用户昵称%26gt;</A></B> &#13;&#10;<DIV style%3D"MARGIN-TOP: 8px; MARGIN-BOTTOM: 8px"><A id%3Dfield urn%3Dname%3Dcontent%26amp;link%3Dtrue>%26lt;微博信息内容%26gt;</A></DIV>&#13;&#10;<DIV id%3DretweetedMicroblogImages width%3D"80px" height%3D"80px">%26lt;微博配图%26gt;</DIV>&#13;&#10;<TABLE style%3D"MARGIN-TOP: 6px" border%3D0 cellSpacing%3D0 cellPadding%3D0 width%3D"100%25">&#13;&#10;<TBODY>&#13;&#10;<TR>&#13;&#10;<TD><A id%3Dfield urn%3D"name%3Dcreated%26amp;link%3Dtrue%26amp;fieldFormat%3Dyyyy-MM-dd HH:mm">%26lt;微博创建时间%26gt;</A></TD>&#13;&#10;<TD align%3Dright><A id%3Dfield urn%3Dname%3DrepostsCountAsText%26amp;link%3Dtrue>%26lt;转发数%26gt;</A>%26nbsp;| <A id%3Dfield urn%3Dname%3DcommentsCountAsText%26amp;link%3Dtrue>%26lt;评论数%26gt;</A></TD></TR></TBODY></TABLE></DIV>&amp;separatorMode=none&amp;separatorHeight=5&amp;linkOpenMode=_blank&amp;simpleMode=true&amp;emptyPrompt=&amp;extendProperties=\'>&lt;被转发的原微博&gt;</A></DIV>' +
				   '<TABLE style="MARGIN-TOP: 6px" border=0 cellSpacing=0 cellPadding=0 width="100%">' +
				   '	<TR>' +
				   '		<TD><A id=field urn="name=created&amp;link=true&amp;fieldFormat=yyyy-MM-dd HH:mm">&lt;微博创建时间&gt;</A></TD>' +
				   '		<TD align=right><A id=field urn=name=repostsCountAsText&amp;link=true>&lt;转发数&gt;</A>&nbsp;| <A id=field urn=name=commentsCountAsText&amp;link=true>&lt;评论数&gt;</A></TD>' +
				   '	</TR>' +
				   '</TABLE>';
		}
		//获取默认的右侧记录格式
		function getDeafultRecordRightFormat() {
			return '';
		}
		function selectMicroblogAccount() {
			//选择单位
			DialogUtils.selectOrg(600, 400, false, '', 'doSelectAccount("{id}")', 'unit');
		}
		function doSelectAccount(unitId) {
			DialogUtils.openSelectDialog("microblog", "selectAccount", 640, 400, false, "microblogAccountId{id},microblogAccountName{name}", "document.getElementsByName('microblogAccountName')[0].value += '({platform})';", "", "", "", false, "unitId=" + unitId);
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td height="26px" nowrap="nowrap">微博帐号：</td>
			<td><ext:field property="microblogAccountName"/></td>
		</tr>
	</table>
</ext:form>