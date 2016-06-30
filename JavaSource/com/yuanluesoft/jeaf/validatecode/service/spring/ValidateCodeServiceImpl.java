package com.yuanluesoft.jeaf.validatecode.service.spring;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;

import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.generator.ImageGenerator;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 * 
 * @author linchuan
 *
 */
public class ValidateCodeServiceImpl implements ValidateCodeService {
	private final String VALIDATE_CODE_ATTRIBUTE_NAME = "validateCode";
	private final int MAX_CODE_LIVE = 300000; //校验码最长存在时间5分钟
	private ImageGenerator imageGenerator; //图片生成器
	private SmsService smsService; //短信服务
	private String allCharacters = "3568abcdefhkmnpqrstuvwxABCDEFGHJKMNPRSTUVWXY";
	private int codeLength = 4;
	private int fontSize = 30;
	private String validateCodeSmsFormat = "验证码:{VALIDATECODE}";
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ValidateCodeImageService#generateValidateCodeImage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void generateValidateCodeImage(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		imageGenerator.writeValidateCodeImage(generateValidateCode(request), fontSize, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService#sendValidateCodeSms(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void sendValidateCodeSms(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		String mobile = request.getParameter("mobile");
		if(mobile==null || mobile.isEmpty()) {
			return;
		}
		mobile = mobile.trim();
		
		//把手机号码写入会话,清理时使用
		Map mobileNumbers = (Map)request.getSession().getAttribute("VALIDATECODESMS");
		if(mobileNumbers==null) {
			mobileNumbers = new HashedMap();
			request.getSession().setAttribute("VALIDATECODESMS", mobileNumbers);
		}
		mobileNumbers.put(mobile, "");
		
		//从COOKIE中获取上次发送时间和已经发送的次数
		String cookie = CookieUtils.getCookie(request, "VALIDATECODESMS_" + mobile);
		int times = 1;
		if(cookie!=null) {
			String[] values = cookie.split("/");
			long lastSent = Long.parseLong(values[0]);
			int timeInterval = RequestUtils.getParameterIntValue(request, "timeInterval");
			if(System.currentTimeMillis()-lastSent < Math.min(30, timeInterval) * 1000) { //距离上次发送时间没有超过指定时间间隔
				return;
			}
			times = Integer.parseInt(values[1]) + 1;
			int sendLimit = RequestUtils.getParameterIntValue(request, "sendLimit"); //发送次数限制
			if(times>(sendLimit<=0 ? 3 : Math.min(10, sendLimit))) { //没有设置时按3次计算，最多不允许超过10次
				return;
			}
		}
		//写COOKIE
		CookieUtils.addCookie(response, "VALIDATECODESMS_" + mobile, System.currentTimeMillis() + "/" + times, 3600, "/", null, null);
		//发送短信
		String sms = validateCodeSmsFormat.replaceAll("\\{VALIDATECODE\\}", generateValidateCode(request));
		smsService.sendShortMessage(0, "系统", ((WebSite)siteService.getDirectory(RequestUtils.getParameterLongValue(request, "siteId"))).getOwnerUnitId(), "系统消息", null, mobile, sms, null, -1, false, null, false);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ValidateCodeImageService#cleanCode(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void cleanCode(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		request.getSession().removeAttribute(VALIDATE_CODE_ATTRIBUTE_NAME);
		//从会话中获取教育短信发送号码
		Map mobileNumbers = (Map)request.getSession().getAttribute("VALIDATECODESMS");
		for(Iterator iterator = mobileNumbers==null ? null : mobileNumbers.keySet().iterator(); iterator!=null && iterator.hasNext();) {
			String mobileNumber = (String)iterator.next();
			//清理COOKIE
			CookieUtils.removeCookie(response, "VALIDATECODESMS_" + mobileNumber, "/", null);
		}
		request.getSession().removeAttribute("VALIDATECODESMS");
	}
	
	/**
	 * 生成校验码
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public String generateValidateCode(HttpServletRequest request) throws ServiceException {
		ValidateCode validateCode = null;
		long now = System.currentTimeMillis();
		if(request.getParameter("reload")==null) { //不需要重新载入
			validateCode = (ValidateCode)request.getSession().getAttribute(VALIDATE_CODE_ATTRIBUTE_NAME);
			if(validateCode!=null && now - validateCode.getCreated() > MAX_CODE_LIVE) { //超时
				validateCode = null;
			}
		}
		if(validateCode==null) {
			String code = "";
			Random random = new Random(); 
			int size = allCharacters.length();
			for(int i=0; i<codeLength; i++) {
				int index = random.nextInt(size);
				code += allCharacters.substring(index, index+1);
			}
			validateCode = new ValidateCode();
			validateCode.setCode(code);
			validateCode.setCreated(now);
			request.getSession().setAttribute(VALIDATE_CODE_ATTRIBUTE_NAME, validateCode);
		}
		return validateCode.getCode();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ValidateCodeImageService#validateCode(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public boolean validateCode(String code, HttpServletRequest request) throws ServiceException {
		ValidateCode validateCode = (ValidateCode)request.getSession().getAttribute(VALIDATE_CODE_ATTRIBUTE_NAME);
		return code!=null && validateCode!=null && validateCode.getCode().compareToIgnoreCase(code)==0;
	}

	/**
	 * @return the imageGenerator
	 */
	public ImageGenerator getImageGenerator() {
		return imageGenerator;
	}

	/**
	 * @param imageGenerator the imageGenerator to set
	 */
	public void setImageGenerator(ImageGenerator imageGenerator) {
		this.imageGenerator = imageGenerator;
	}
	
	/**
	 * 校验码
	 * @author linchuan
	 *
	 */
	private class ValidateCode implements Serializable {
		private String code; 
		private long created; //最后访问时间,当校验码超过一定时间后,自动失效
		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}
		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}
		/**
		 * @return the created
		 */
		public long getCreated() {
			return created;
		}
		/**
		 * @param created the created to set
		 */
		public void setCreated(long created) {
			this.created = created;
		}
	}

	/**
	 * @return the allCharacters
	 */
	public String getAllCharacters() {
		return allCharacters;
	}

	/**
	 * @param allCharacters the allCharacters to set
	 */
	public void setAllCharacters(String allCharacters) {
		this.allCharacters = allCharacters;
	}

	/**
	 * @return the codeLength
	 */
	public int getCodeLength() {
		return codeLength;
	}

	/**
	 * @param codeLength the codeLength to set
	 */
	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}

	/**
	 * @return the fontSize
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * @param fontSize the fontSize to set
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * @return the smsService
	 */
	public SmsService getSmsService() {
		return smsService;
	}

	/**
	 * @param smsService the smsService to set
	 */
	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	/**
	 * @return the validateCodeSmsFormat
	 */
	public String getValidateCodeSmsFormat() {
		return validateCodeSmsFormat;
	}

	/**
	 * @param validateCodeSmsFormat the validateCodeSmsFormat to set
	 */
	public void setValidateCodeSmsFormat(String validateCodeSmsFormat) {
		this.validateCodeSmsFormat = validateCodeSmsFormat;
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}