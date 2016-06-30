package com.yuanluesoft.jeaf.gps.provider.lingtu;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Coordinate;
import com.yuanluesoft.jeaf.gps.provider.MapProvider;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.HttpUtils;

/**
 * 51ditu地图服务
 * @author linchuan
 *
 */
public class MapProviderImpl implements MapProvider {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.PlaceNameService#getPlaceName(java.lang.String, java.lang.String)
	 */
	public String getPlaceName(double latitude, double longitude) throws ServiceException {
		String url = "http://www.51ditu.com/p4x" +
					 "?url=http%253A%252F%252Frgc.vip.51ditu.com%252Frgc%253Fpos" +
					 "%253D" + Math.round(longitude * 100000) + 
					 "%252C" + Math.round(latitude *100000) +
					 "%2526type%253D0" +
					 "&charset=GB2312";
		String content;
		try {
			content = HttpUtils.getHttpContent(url, null, true, null, 60000, 3, 1000).getResponseBody(); //重试3次,间隔1秒
		}
		catch (Exception e) {
			Logger.exception(e);
			return null;
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("PlaceNameService: request url is " + url + ", response is " + content + ".");
		}
		//返回的内容格式:var _OLR={n:'ReverseGeocode',e:1,a:{},c:[{n:'Geo',a:{lat:'CZGtDJWoCW',lng:'CJ4uE3avCZK'},c:[]},{n:'Area',a:{regionCode:'C0'},c:[],t:'8EQSgkUVfUMCkkMVdo0'}],t:'808080'};
		int begin = content.indexOf(",t:'");
		if(begin==-1 || content.indexOf("ReverseGeocode")==-1) {
			return null;
		}
		begin += ",t:'".length();
		int end = content.indexOf("'", begin);
		content = decodeAddress(content.substring(begin, end)).replaceAll("-", "").trim();
		if(content.indexOf("失败")!=-1) {
			return null;
		}
		return content;
	}
	
	/**
	 * 地址解密,将51的js脚本转为java,51地图返回的地址结果是一串加密字符串
	 * @param encoderAddress
	 * @return 返回格式：福建省-泉州市-晋江市
	 */
	private String decodeAddress(String encodedAddress) {
		int aw=0,sw=0;
		int dw = encodedAddress.length();
		String fw = new String();
		int gw=-1;
		int hw=0;
		for(int jw=0;jw<dw;jw++) {
			int kw=encodedAddress.codePointAt(jw);
			kw=(kw==95)?63:((kw==44)?62:((kw>=97)?(kw-61):((kw>=65)?(kw-55):(kw-48))));
			sw=(sw<<6)+kw;
			aw+=6;
			while(aw>=8) {
				int lw=sw>>(aw-8);
				if(hw>0) {
					gw=(gw<<6)+(lw&(0x3f));
					hw--;
					if(hw==0) {
						fw+=(char)(gw);
					};
				}
				else {
					if(lw>=224) {
						gw=lw&(0xf);
						hw=2;
					}
					else if(lw>=128) {
						gw=lw&(0x1f);
						hw=1;
					}
					else {
						fw+=(char)(lw);
					};
				};
				sw=sw-(lw<<(aw-8));
				aw-=8;
			};
		};
		return fw;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.PlaceNameService#getCoordinateByPlaceName(java.lang.String)
	 */
	public Coordinate getCoordinateByPlaceName(String placeName) throws ServiceException {
		throw new ServiceException("not implemented");
	}
}