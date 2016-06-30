<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<xsl:template match="/">
<html>
<head>
	<title>
	RSS-<xsl:value-of select="rss/channel/title" />
	</title>
</head>
<body>
<center>
<xsl:apply-templates select="rss/channel" />
</center>

<script>
	function  GetTimeDiff(str)
	{
		if(str == '')
		{
			return '';
		}

		var pubDate = new Date(str);
		var nowDate = new Date();
		var diffMilSeconds = nowDate.valueOf()-pubDate.valueOf();
		var days = diffMilSeconds/86400000;
		days = parseInt(days);

		diffMilSeconds = diffMilSeconds-(days*86400000);
		var hours = diffMilSeconds/3600000;
		hours = parseInt(hours);

		diffMilSeconds = diffMilSeconds-(hours*3600000);
		var minutes = diffMilSeconds/60000;
		minutes = parseInt(minutes);

		diffMilSeconds = diffMilSeconds-(minutes*60000);
		var seconds = diffMilSeconds/1000;
		seconds = parseInt(seconds);
		
		var returnStr = "北京发布时间：" + pubDate.toLocaleString();

		if(days &gt; 0)
		{
			returnStr = returnStr + "&amp;nbsp;（距离现在" + days + "天" + hours + "小时" + minutes + "分钟）";
		}
		else if (hours &gt; 0)
		{
			returnStr = returnStr + "&amp;nbsp;（距离现在" + hours + "小时" + minutes + "分钟）";
		}
		else if (minutes &gt; 0)
		{
			returnStr = returnStr + "&amp;nbsp;（距离现在" + minutes + "分钟）";
		}

		return returnStr;

	}

	function GetSpanText()
	{
		var pubDate;
		var pubDateArray;
		var spanArray = document.getElementsByTagName("span");

		for(var i = 0; i &lt; spanArray.length; i++)
		{
			pubDate = spanArray[i].innerHTML;
			document.getElementsByTagName("span")[i].innerHTML = GetTimeDiff(pubDate);			
		}
	}

	GetSpanText();
</script>

</body>
</html>
</xsl:template>

<xsl:template match="channel">
<script>

function copyLink()
{
	clipboardData.setData("Text",window.location.href);
	alert("RSS链接已经复制到剪贴板");
}

</script>
<table width="750" cellpadding="0" cellspacing="0">
	<tr>
		<td align="right" style="padding-right:15px;" valign="bottom">
		
		</td>
		<td align="left" valign="bottom" style="padding-bottom:8px;">
		<B>
		 <xsl:element name="A">
		  <xsl:attribute name="href">
		   <xsl:value-of select="link"/>
		  </xsl:attribute>
		  <xsl:attribute name="target">
		   _blank
		  </xsl:attribute>
		  <xsl:value-of select="title"/>
		 </xsl:element>
		</B>
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
		<xsl:value-of select="description" />
		</td>	
	</tr>
	<tr style="font-size:12px;" bgcolor="#eeeeff">
		<td  colspan="2" style="font-size:14px;padding-top:5px;padding-bottom:5px;">
		<b><a href="javascript:copyLink();">复制此页链接</a><xsl:text>    </xsl:text><xsl:text>    </xsl:text><xsl:text>    </xsl:text><xsl:text>    </xsl:text></b>
		</td>
	</tr>
</table>

<table width="750" cellpadding="0" cellspacing="0">
<xsl:for-each select="item">
	<tr bgcolor="#eeeeee">
		<td style="padding-top:5px;padding-left:5px;" height="30"><B><xsl:value-of select="title" /></B></td>
	</tr>
	<tr bgcolor="#eeeeee">
		<td style="padding-left:5px;"><xsl:value-of select="description" /></td>
	</tr>
	<tr bgcolor="#eeeeee">
		<td style="padding-top:5px;padding-left:5px;">
		<xsl:element name="A">
			<xsl:attribute name="href"><xsl:value-of select="link" /></xsl:attribute>
			<xsl:attribute name="target">_blank</xsl:attribute>
			<xsl:value-of select="link" />
		</xsl:element>		
		</td>
	</tr>
	<tr bgcolor="#eeeeee">
		<td style="padding-top:5px;padding-left:5px;padding-bottom:5px;">
		<span><xsl:value-of select="pubDate" /></span>
		</td>
	</tr>
	<tr height="10">
		<td></td>
	</tr>
</xsl:for-each>
<tr>
	<td height="20"></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
