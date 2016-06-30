StringUtils = function() {

};
//把字节数转换为M或K
StringUtils.formatBytes =  function(bytes) {
	if(bytes>1000000000) {
		return (Math.round(bytes/10000000) / 100.0) + 'GB';
	}
	else if(bytes>1000000) {
		return (Math.round(bytes/10000) / 100.0) + 'MB';
	}
	if(bytes>1000) {
		return (Math.round(bytes/10)/100.0) + 'KB';
	}
	return Math.round(bytes) + "B";
};

//转换时间为xx小时xx分xx秒
StringUtils.formatSeconds = function(seconds) {
	var hours = Math.floor(seconds / 3600);
	var minutes = Math.floor(seconds % 3600 / 60);
	seconds = Math.floor(seconds % 60);
	return (hours>0 ? hours + '小时' : '') + (minutes>0 ? minutes + '分' : '') + seconds + '秒';
};

//获取大写的金额
StringUtils.getMoneyCapital = function(money) {
	money = new Number(money);
	if(isNaN(money) || money==0 || money>999999999999.99) {
        return "";
    }
    var nums = "零,壹,贰,叁,肆,伍,陆,柒,捌,玖,拾,佰,仟,萬,亿".split(",");
    var capital = "元";
    money = Math.round(money*100);
    if(money % 100 ==0) {
        capital += "整";
    }
    else {
        capital += nums[Math.floor(money % 100 / 10)] + "角";
        capital += nums[money % 10] + "分";
    }
    money = Math.floor(money/100);
    var i = 0;
    do {
        if(i%4==0) {
        	capital = (i==0 ? "" : nums[12 + i/4]) + capital;
        }
        else {
        	capital = nums[9 + i%4] + capital;
        }
        i++;
        capital = '<font style="text-decoration:underline">&nbsp;' + nums[money%10] + '&nbsp;</font>' + capital;
        money = Math.floor(money/10);
    }while(money>0);
    return capital;
};

//属性值编码
StringUtils.encodePropertyValue = function(propertyValue) {
	return !propertyValue ? propertyValue : propertyValue.replace(new RegExp("%", "g"), "%25").replace(new RegExp("&", "g"), "%26").replace(new RegExp("=", "g"), "%3D");
};

//获取属性值
StringUtils.getPropertyValue = function(properties, propertyName, defaultValue) {
	if(!properties || properties=='') {
		return defaultValue ? defaultValue : "";
	}
	var index = properties.indexOf(propertyName + "=");
	if(index==-1) {
		return defaultValue ? defaultValue : "";
	}
	index += propertyName.length + 1;
	var indexNext = properties.indexOf("&", index);
	var propertyValue = (indexNext==-1 ? properties.substring(index) : properties.substring(index, indexNext));
	return propertyValue.replace(new RegExp("%26", "g"), "&").replace(new RegExp("%3D", "g"), "=").replace(new RegExp("%25", "g"), "%");
};

StringUtils.trim = function(str) {
	return str.replace(/(^\s*)|(\s*$)/g, '');
};

StringUtils.ltrim = function(str) {
	return str.replace(/^\s*/g,'');
};

StringUtils.rtrim = function(str) {
	return str.replace(/\s*$/g,'');
};
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
String.prototype.leftTrim = function() {
	return this.replace(/(^\s*)/g, "");
};
String.prototype.rightTrim = function() {
	return this.replace(/(\s*$)/g, "");
};

//UTF-8编码
StringUtils.utf8Encode = function(text) {
	var s = escape(text).replace('+', '%2b');
	var sa = s.split("%");
    var retV ="";
    if(sa[0] != "") {
       retV = sa[0];
    }
    for(var i = 1; i < sa.length; i++) {
         if(sa[i].substring(0,1) == "u") {
             retV += StringUtils.hex2Utf8(StringUtils.str2Hex(sa[i].substring(1,5))) + sa[i].substring(5);
         }
         else {
         	retV += "%" + sa[i];
         }
    }
    return retV;
};
StringUtils.str2Hex = function(s) {
    var c = "";
    var n;
    var ss = "0123456789ABCDEF";
    var digS = "";
    for(var i = 0; i < s.length; i ++) {
       c = s.charAt(i);
       n = ss.indexOf(c);
       digS += StringUtils.dec2Dig(eval(n));
    }
    return digS;
};
StringUtils.dec2Dig = function(n1) {
    var s = "";
    var n2 = 0;
    for(var i = 0; i < 4; i++) {
		n2 = Math.pow(2,3 - i);
		if(n1 >= n2) {
			s += '1';
			n1 = n1 - n2;
       	}
       	else {
			s += '0';
       	}
    }
    return s;
};
StringUtils.dig2Dec = function(s) {
    var retV = 0;
    if(s.length == 4) {
        for(var i = 0; i < 4; i ++) {
            retV += eval(s.charAt(i)) * Math.pow(2, 3 - i);
        }
        return retV;
    }
    return -1;
};
StringUtils.hex2Utf8 = function(s) {
   var retS = "";
   var tempS = "";
   var ss = "";
   if(s.length == 16) {
       tempS = "1110" + s.substring(0, 4);
       tempS += "10" +  s.substring(4, 10); 
       tempS += "10" + s.substring(10,16); 
       var sss = "0123456789ABCDEF";
       for(var i = 0; i < 3; i ++) {
          retS += "%";
          ss = tempS.substring(i * 8, (eval(i)+1)*8);
          retS += sss.charAt(StringUtils.dig2Dec(ss.substring(0,4)));
          retS += sss.charAt(StringUtils.dig2Dec(ss.substring(4,8)));
       }
       return retS;
   }
   return "";
};

//UTF-8解码
StringUtils.utf8Decode = function(szInput) {
	var x,wch,wch1,wch2,uch="",szRet="";
	for (x=0; x<szInput.length; x++) {
		if (szInput.charAt(x)=="%") {
			wch =parseInt(szInput.charAt(++x) + szInput.charAt(++x),16);
			if (!wch) {
				break;
			}
			if (!(wch & 0x80)) {
				wch = wch;
			}
			else if (!(wch & 0x20)) {
				x++;
				wch1 = parseInt(szInput.charAt(++x) + szInput.charAt(++x),16);
				wch = (wch & 0x1F)<< 6;
				wch1 = wch1 & 0x3F;
				wch = wch + wch1;
			}
			else {
				x++;
				wch1 = parseInt(szInput.charAt(++x) + szInput.charAt(++x),16);
				x++;
				wch2 = parseInt(szInput.charAt(++x) + szInput.charAt(++x),16);
				wch = (wch & 0x0F)<< 12;
				wch1 = (wch1 & 0x3F)<< 6;
				wch2 = (wch2 & 0x3F);
				wch = wch + wch1 + wch2;
			}
			szRet += String.fromCharCode(wch);
		}
		else {
			szRet += szInput.charAt(x);
		}
	}
	return szRet;
};

//从请求参数中删除参数
StringUtils.removeQueryParameter = function(queryString, parameterNames) {
	if(!queryString || queryString=="") {
		return queryString;
	}
	var names = parameterNames.split(",");
	for(var i=0; i<names.length; i++) {
		var beginIndex = queryString.indexOf(names[i] + "=");
		if(beginIndex!=-1) {
			var endIndex = queryString.indexOf('&', beginIndex);
			queryString = (endIndex==-1 ? queryString.substring(0, beginIndex) : queryString.substring(0, beginIndex) + queryString.substring(endIndex + 1));
		}
	}
	if(queryString!="") {
		var last = queryString.substring(queryString.length-1);
		if(last=="&" || last=="?") {
			queryString = queryString.substring(0, queryString.length-1);
		}
	}
	return queryString;
};

//往请求参数中增加一个参数
StringUtils.setQueryParameter = function(queryString, parameterName, parameterValue) {
	queryString = StringUtils.removeQueryParameter(queryString, parameterName);
	if(!queryString) {
		queryString = "";
	}
	return queryString + (queryString=="" ? "" : "&") + parameterName + "=" + StringUtils.utf8Encode(parameterValue);
};

StringUtils.decToHex = function(dec) {
	var hexa="0123456789ABCDEF"; 
	var hex=""; 
	while(dec>15) { 
		tmp=dec-(Math.floor(dec/16))*16; 
		hex=hexa.charAt(tmp)+hex; 
		dec=Math.floor(dec/16); 
	} 
	hex = hexa.charAt(dec)+hex;
	return(hex);
};
StringUtils.generateHtmlContent = function(text) {
	return text.replace(/&/g, "&amp;")
			   .replace(/ /g, "&nbsp;")
			   .replace(/</g, "&lt;")
			   .replace(/>/g, "&gt;")
			   .replace(/"/g, "&quot;")
			   .replace(/\r/g, "")
			   .replace(/\n/g, "<br/>")
			   .replace(/“/g, "&ldquo;")
			   .replace(/”/g, "&rdquo;")
			   .replace(/·/g, "&middot;")
			   .replace(/￠/g, "&cent;")
			   .replace(/£/g, "&pound;")
			   .replace(/¥/g, "&yen;")
			   .replace(/§/g, "&sect;")
			   .replace(/©/g, "&copy;")
			   .replace(/®/g, "&reg;")
			   .replace(/×/g, "&times;")
			   .replace(/÷/g, "&divide;")
			   .replace(/«/g, "&laquo;")
			   .replace(/»/g, "&raquo;")
			   .replace(/±/g, "&plusmn;")
			   .replace(/°/g, "&deg;")
			   .replace(/′/g, "&prime;");
};
StringUtils.filterEscapeCharacter = function(text) {
	return text.replace(/&amp;/g, "&")
			   .replace(/&nbsp;/g, " ")
			   .replace(/&lt;/g, "<")
			   .replace(/&gt;/g, ">")
			   .replace(/&quot;/g, '"')
			   .replace(/&ldquo;/g, "“")
			   .replace(/&rdquo;/g, "”")
			   .replace(/&middot;/g, "·")
			   .replace(/&cent;/g, "￠")
			   .replace(/&pound;/g, "£")
			   .replace(/&yen;/g, "¥")
			   .replace(/&sect;/g, "§")
			   .replace(/&copy;/g, "©")
			   .replace(/&reg;/g, "®")
			   .replace(/&times;/g, "×")
			   .replace(/&divide;/g, "÷")
			   .replace(/&laquo;/g, "«")
			   .replace(/&raquo;/g, "»")
			   .replace(/&plusmn;/g, "±")
			   .replace(/&deg;/g, "°")
			   .replace(/&prime;/g, "′");
};
/**
 * 对Date的扩展，将 Date 转化为指定格式的String
 * 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
 * 例子： 
 * (new Date()).Format("yyyy-MM-dd HH:mm:ss.S") ==> 2006-07-02 08:09:04.423 
 * (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
 **/
Date.prototype.format = function(fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "H+": this.getHours(), //小时,24小时制
        "h+": (this.getHours()%12==0 ? 12 : this.getHours()%12), //小时,12小时制
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds(), //毫秒
        "t+": (this.getHours()<12 ? 'AM' : 'PM'),
        "T+": (this.getHours()<12 ? '上午' : '下午')
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};