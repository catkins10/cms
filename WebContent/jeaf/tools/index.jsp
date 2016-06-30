<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
	<style>
		td{font-family:宋体;font-size:12px;}
		A:link{font-size:12px; color:#003366; text-decoration:none}
		A:visited{font-size:12px; color:#003366; text-decoration:none}
		A:hover{font-size:12px; color:#E00000; text-decoration:none}
		.file:link{font-size:12px; color:blue; text-decoration:none}
		.file:visited{font-size:12px; color:blue; text-decoration:none}
		.file:hover{font-size:12px; color:#E00000; text-decoration:none}
	</style>
	<script charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
</head>
<body style="overflow:auto">
	<div style="width:30px; height:30px;" ondblclick="document.getElementById('divTools').style.display = '';">&nbsp;</div>
	<div id="divTools" style="display:none">
		<a href="<%=request.getContextPath()%>/jeaf/tools/clearCache.shtml">缓存清除</a><br></br>
		<a href="<%=request.getContextPath()%>/jeaf/dataimport/importData.shtml">数据导入</a><br></br>
		<a href="javascript:DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/system/regist.shtml', 480, 200)">系统注册</a><br></br>
		<a href="javascript:DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/system/initSystem.shtml', 480, 200)">系统初始化</a><br></br>
		<a href="javascript:DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/tools/regenerateStaticPages.shtml', 480, 200)">重新生成静态页面</a><br></br>
		<a href="<%=request.getContextPath()%>/jeaf/tools/updateHostPageMapping.shtml">更新主机页面映射表</a><br></br>
		<a href="<%=request.getContextPath()%>/jeaf/tools/network/ip.jsp">获取本地IP</a><br></br>
	</div>
</body>
</html>