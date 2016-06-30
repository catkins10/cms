<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>高速公路通行费查询</title>
	<link href="<%=request.getContextPath()%>/cms/css/application.css" rel="stylesheet" type="text/css" />
</head>
<body style="border-style:none; overflow:hidden" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" >
    <ext:form action="/doQuery">
    <div style="float:left;padding-top:8px">入口：</div>
	<div style="float:left;width:100px;padding-right:10px;padding-top:3px">
		<html:select property="entry" style="font-size:12px;width:100%">
	        <html:option value="2101">闽浙省际</html:option>
			<html:option value="2102">福鼎</html:option>
			<html:option value="2103">八尺门</html:option>
			<html:option value="2104">太姥山</html:option>
			<html:option value="2301">牙城</html:option>
			<html:option value="2302">三沙</html:option>
			<html:option value="2303">霞浦</html:option>
			<html:option value="2304">盐田</html:option>
			<html:option value="2501">福安</html:option>
			<html:option value="2502">赛岐</html:option>
			<html:option value="2503">下白石</html:option>
			<html:option value="2504">漳湾</html:option>
			<html:option value="2505">宁德</html:option>
			<html:option value="2506">飞鸾岭</html:option>
			<html:option value="2507">湾坞</html:option>
			<html:option value="2701">罗源</html:option>
			<html:option value="2702">水古</html:option>
			<html:option value="2703">丹阳</html:option>
			<html:option value="2704">连江</html:option>
			<html:option value="2705">琯头</html:option>
			<html:option value="2706">马尾</html:option>
			<html:option value="2707">营前</html:option>
			<html:option value="2901">福州</html:option>
			<html:option value="2902">兰圃</html:option>
			<html:option value="2903">宏路</html:option>
			<html:option value="2904">渔溪</html:option>
			<html:option value="3101">涵江</html:option>
			<html:option value="3102">莆田</html:option>
			<html:option value="3103">仙游</html:option>
			<html:option value="3104">泉港</html:option>
			<html:option value="3105">驿坂</html:option>
			<html:option value="3106">惠安</html:option>
			<html:option value="3301">泉州</html:option>
			<html:option value="3302">晋江</html:option>
			<html:option value="3303">水头</html:option>
			<html:option value="3501">翔安</html:option>
			<html:option value="3502">同安</html:option>
			<html:option value="3503">厦门</html:option>
			<html:option value="3504">杏林</html:option>
			<html:option value="3505">海沧</html:option>
			<html:option value="3701">漳州</html:option>
			<html:option value="3702">龙海</html:option>
			<html:option value="3703">漳州北</html:option>
			<html:option value="3704">漳州西</html:option>
			<html:option value="3705">南靖</html:option>
			<html:option value="3706">金山</html:option>
			<html:option value="3707">和溪</html:option>
			<html:option value="3901">漳州港</html:option>
			<html:option value="3902">赵家堡</html:option>
			<html:option value="3903">漳浦</html:option>
			<html:option value="3904">杜浔</html:option>
			<html:option value="3905">云霄</html:option>
			<html:option value="3906">常山</html:option>
			<html:option value="3907">东山岛</html:option>
			<html:option value="3908">诏安东</html:option>
			<html:option value="3909">诏安南</html:option>
			<html:option value="3910">闽粤</html:option>
			<html:option value="5101">五夫</html:option>
			<html:option value="5102">武夷山</html:option>
			<html:option value="5103">建阳</html:option>
			<html:option value="5104">建阳南</html:option>
			<html:option value="5901">泰宁</html:option>
			<html:option value="5902">万安</html:option>
			<html:option value="5903">将乐</html:option>
			<html:option value="6101">青州</html:option>
			<html:option value="6102">沙县</html:option>
			<html:option value="6103">三明北</html:option>
			<html:option value="6104">夏茂</html:option>
			<html:option value="6301">南平</html:option>
			<html:option value="6303">塔前</html:option>
			<html:option value="6304">和平</html:option>
			<html:option value="6305">福银闽赣</html:option>
			<html:option value="6306">徐墩</html:option>
			<html:option value="6307">建瓯</html:option>
			<html:option value="6308">南雅</html:option>
			<html:option value="6309">大横</html:option>
			<html:option value="6310">南平北</html:option>
			<html:option value="6501">洋中</html:option>
			<html:option value="6502">尤溪</html:option>
			<html:option value="6701">福州西</html:option>
			<html:option value="6702">闽侯</html:option>
			<html:option value="6703">闽清</html:option>
			<html:option value="6704">金沙</html:option>
			<html:option value="6705">福州南</html:option>
			<html:option value="7101">适中</html:option>
			<html:option value="7102">龙岩</html:option>
			<html:option value="7104">古田</html:option>
			<html:option value="7105">蛟洋</html:option>
			<html:option value="7106">新泉</html:option>
			<html:option value="7107">龙岩西</html:option>
			<html:option value="7301">涂坊</html:option>
			<html:option value="7302">河田</html:option>
			<html:option value="7303">长汀</html:option>
			<html:option value="7305">厦成闽赣</html:option>
			<html:option value="7501">泉州南</html:option>
			<html:option value="7502">泉州西</html:option>
			<html:option value="7503">南安</html:option>
			<html:option value="7504">码头</html:option>
			<html:option value="7505">永春</html:option>
			<html:option value="7506">蓬壶</html:option>
			<html:option value="7507">德化</html:option>
			<html:option value="7508">下洋</html:option>
			<html:option value="7701">吴山</html:option>
			<html:option value="7702">大田</html:option>
			<html:option value="7703">桃源</html:option>
			<html:option value="7901">西洋</html:option>
			<html:option value="7902">永安南</html:option>
			<html:option value="7903">永安北</html:option>
			<html:option value="7904">莘口</html:option>
			<html:option value="7905">三明南</html:option>
			<html:option value="8701">京台闽浙</html:option>
			<html:option value="8702">仙阳</html:option>
			<html:option value="8703">浦城</html:option>
			<html:option value="8704">临江</html:option>
			<html:option value="8705">石陂</html:option>
			<html:option value="2708">漳港</html:option>
			<html:option value="2709">福州机场</html:option>
	    </html:select>
	</div>
	<div style="float:left;padding-top:8px">出口：</div>
	<div style="float:left;padding-top:3px;width:100px;">
		 <html:select property="exit" style="font-size:12px;width:100%">
		 	<html:option value="2709">福州机场</html:option>
			<html:option value="2708">漳港</html:option>
			<html:option value="8705">石陂</html:option>
			<html:option value="8704">临江</html:option>
			<html:option value="8703">浦城</html:option>
			<html:option value="8702">仙阳</html:option>
			<html:option value="8701">京台闽浙</html:option>
			<html:option value="7905">三明南</html:option>
			<html:option value="7904">莘口</html:option>
			<html:option value="7903">永安北</html:option>
			<html:option value="7902">永安南</html:option>
			<html:option value="7901">西洋</html:option>
			<html:option value="7703">桃源</html:option>
			<html:option value="7702">大田</html:option>
			<html:option value="7701">吴山</html:option>
			<html:option value="7508">下洋</html:option>
			<html:option value="7507">德化</html:option>
			<html:option value="7506">蓬壶</html:option>
			<html:option value="7505">永春</html:option>
			<html:option value="7504">码头</html:option>
			<html:option value="7503">南安</html:option>
			<html:option value="7502">泉州西</html:option>
			<html:option value="7501">泉州南</html:option>
			<html:option value="7305">厦成闽赣</html:option>
			<html:option value="7303">长汀</html:option>
			<html:option value="7302">河田</html:option>
			<html:option value="7301">涂坊</html:option>
			<html:option value="7107">龙岩西</html:option>
			<html:option value="7106">新泉</html:option>
			<html:option value="7105">蛟洋</html:option>
			<html:option value="7104">古田</html:option>
			<html:option value="7102">龙岩</html:option>
			<html:option value="7101">适中</html:option>
			<html:option value="6705">福州南</html:option>
			<html:option value="6704">金沙</html:option>
			<html:option value="6703">闽清</html:option>
			<html:option value="6702">闽侯</html:option>
			<html:option value="6701">福州西</html:option>
			<html:option value="6502">尤溪</html:option>
			<html:option value="6501">洋中</html:option>
			<html:option value="6310">南平北</html:option>
			<html:option value="6309">大横</html:option>
			<html:option value="6308">南雅</html:option>
			<html:option value="6307">建瓯</html:option>
			<html:option value="6306">徐墩</html:option>
			<html:option value="6305">福银闽赣</html:option>
			<html:option value="6304">和平</html:option>
			<html:option value="6303">塔前</html:option>
			<html:option value="6301">南平</html:option>
			<html:option value="6104">夏茂</html:option>
			<html:option value="6103">三明北</html:option>
			<html:option value="6102">沙县</html:option>
			<html:option value="6101">青州</html:option>
			<html:option value="5903">将乐</html:option>
			<html:option value="5902">万安</html:option>
			<html:option value="5901">泰宁</html:option>
			<html:option value="5104">建阳南</html:option>
			<html:option value="5103">建阳</html:option>
			<html:option value="5102">武夷山</html:option>
			<html:option value="5101">五夫</html:option>
			<html:option value="3910">闽粤</html:option>
			<html:option value="3909">诏安南</html:option>
			<html:option value="3908">诏安东</html:option>
			<html:option value="3907">东山岛</html:option>
			<html:option value="3906">常山</html:option>
			<html:option value="3905">云霄</html:option>
			<html:option value="3904">杜浔</html:option>
			<html:option value="3903">漳浦</html:option>
			<html:option value="3902">赵家堡</html:option>
			<html:option value="3901">漳州港</html:option>
			<html:option value="3707">和溪</html:option>
			<html:option value="3706">金山</html:option>
			<html:option value="3705">南靖</html:option>
			<html:option value="3704">漳州西</html:option>
			<html:option value="3703">漳州北</html:option>
			<html:option value="3702">龙海</html:option>
			<html:option value="3701">漳州</html:option>
			<html:option value="3505">海沧</html:option>
			<html:option value="3504">杏林</html:option>
			<html:option value="3503">厦门</html:option>
			<html:option value="3502">同安</html:option>
			<html:option value="3501">翔安</html:option>
			<html:option value="3303">水头</html:option>
			<html:option value="3302">晋江</html:option>
			<html:option value="3301">泉州</html:option>
			<html:option value="3106">惠安</html:option>
			<html:option value="3105">驿坂</html:option>
			<html:option value="3104">泉港</html:option>
			<html:option value="3103">仙游</html:option>
			<html:option value="3102">莆田</html:option>
			<html:option value="3101">涵江</html:option>
			<html:option value="2904">渔溪</html:option>
			<html:option value="2903">宏路</html:option>
			<html:option value="2902">兰圃</html:option>
			<html:option value="2901">福州</html:option>
			<html:option value="2707">营前</html:option>
			<html:option value="2706">马尾</html:option>
			<html:option value="2705">琯头</html:option>
			<html:option value="2704">连江</html:option>
			<html:option value="2703">丹阳</html:option>
			<html:option value="2702">水古</html:option>
			<html:option value="2701">罗源</html:option>
			<html:option value="2507">湾坞</html:option>
			<html:option value="2506">飞鸾岭</html:option>
			<html:option value="2505">宁德</html:option>
			<html:option value="2504">漳湾</html:option>
			<html:option value="2503">下白石</html:option>
			<html:option value="2502">赛岐</html:option>
			<html:option value="2501">福安</html:option>
			<html:option value="2304">盐田</html:option>
			<html:option value="2303">霞浦</html:option>
			<html:option value="2302">三沙</html:option>
			<html:option value="2301">牙城</html:option>
			<html:option value="2104">太姥山</html:option>
			<html:option value="2103">八尺门</html:option>
			<html:option value="2102">福鼎</html:option>
			<html:option value="2101">闽浙省际</html:option>
		 </html:select>
	</div>
	<div>
		&nbsp;<ext:button name="查询" width="40" onclick="submit()"/>
	</div>
	<br>
	<div>路线:<ext:write property="road"/>  距离: <ext:write property="space"/>公里<div>
	<TABLE align="center" bgcolor="#b2d0f4" border="0" cellpadding="1" cellspacing="1" width="100%">
		<TR>
		    <TD align="center" bgcolor="#69a7f2" height="20">车型</TD>
		    <TD align="center" bgcolor="#69a7f2">一类车</TD>
		    <TD align="center" bgcolor="#69a7f2">二类车</TD>
		    <TD align="center" bgcolor="#69a7f2">三类车</TD>
		    <TD align="center" bgcolor="#69a7f2">四类车</TD>
		    <TD align="center" bgcolor="#69a7f2">五类车</TD>
		</TR>
		<TR>
		    <TD align="center" bgcolor="#ffffff" height="22">收费价</TD>
		    <TD align="center" bgcolor="#ffffff"><ext:write property="type1Price"/>元</TD>
		    <TD align="center" bgcolor="#ffffff"><ext:write property="type2Price"/>元</TD>
		    <TD align="center" bgcolor="#ffffff"><ext:write property="type3Price"/>元</TD>
		    <TD align="center" bgcolor="#ffffff"><ext:write property="type4Price"/>元</TD>
		    <TD align="center" bgcolor="#ffffff"><ext:write property="type5Price"/>元</TD>
		</TR>
    </TABLE>
</ext:form>
</body>
</html:html>
