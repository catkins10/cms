<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/insertBbsArticles">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
	//获取配置元素标题,由继承者实现
	function getElementTitle(isSearchResults, isRssChannel) {
		return (isRssChannel ? "" : "论坛主题列表:") + document.getElementsByName("directoryNames")[0].value;
	}
	//获取默认的左侧记录格式,由继承者实现
	function getDeafultRecordLeftFormat() {
		return '<a id="field" urn="name=subject&link=true">&lt;主题&gt;</a>';
	}
	//获取默认的右侧记录格式,由继承者实现
	function getDeafultRecordRightFormat() {
		return "";
	}
	//显示扩展属性,由继承者实现
	function showExtendProperties(recordListProperties, recordListTextContent) {
		if(recordListProperties=="") {
			return;
		}
		document.getElementsByName("directoryIds")[0].value = StringUtils.getPropertyValue(recordListProperties, "directoryIds");
		document.getElementsByName("directoryNames")[0].value = recordListTextContent.replace("&lt;论坛主题列表:", "").replace("&gt;", ""); //论坛名称
		document.getElementsByName("containChildren")[0].checked = "true"==StringUtils.getPropertyValue(recordListProperties, "containChildren"); //是否包含子目录资源
	}
	//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
	function getExtendPropertiesAsText() {
		var directoryIds = document.getElementsByName("directoryIds")[0].value;
		if(directoryIds=="") {
			alert("论坛不能为空");
			return "ERROR";
		}
		return "directoryIds=" + directoryIds + 
			   (!document.getElementsByName("containChildren")[0].checked ? "" : "&containChildren=true"); //是否显示子目录数据
	}
	</script>
    <table border="0" cellspacing="5" cellpadding="0px">
		<tr>
			<td align="right">论坛选择：</td>
			<td width="272px"><ext:field property="directoryNames"/></td>
			<td><ext:field property="containChildren"/></td>
		</tr>
    </table>
</ext:form>