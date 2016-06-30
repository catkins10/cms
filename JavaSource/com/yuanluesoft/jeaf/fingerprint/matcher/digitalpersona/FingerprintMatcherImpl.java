package com.yuanluesoft.jeaf.fingerprint.matcher.digitalpersona;

import java.util.Iterator;
import java.util.List;

import sun.misc.BASE64Decoder;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.fingerprint.matcher.FingerprintMatcher;
import com.yuanluesoft.jeaf.fingerprint.pojo.FingerprintTemplate;

/**
 * 
 * @author linchuan
 *
 */
public class FingerprintMatcherImpl implements FingerprintMatcher {
	private DPFPVerification verificator = null;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.fingerprint.matcher.FingerprintMatcher#match(java.lang.String, java.util.List)
	 */
	public FingerprintTemplate match(String featureData, List templates) throws ServiceException {
		if(templates==null || templates.isEmpty()) {
			return null;
		}
		if(verificator==null) {
			try {
				verificator = DPFPGlobal.getVerificationFactory().createVerification();
			}
			catch(Error e) {
				return null;
			}
			catch(Exception e) {
				return null;
			}
		}
		//创建样本
		DPFPFeatureSet features = DPFPGlobal.getFeatureSetFactory().createFeatureSet();
		BASE64Decoder base64 = new BASE64Decoder();
		try {
			features.deserialize(base64.decodeBuffer(featureData));
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		DPFPTemplate template = DPFPGlobal.getTemplateFactory().createTemplate();
		for(Iterator iterator = templates.iterator(); iterator.hasNext();) {
			FingerprintTemplate fingerprintTemplate = (FingerprintTemplate)iterator.next();
			//创建指纹模板
			try {
				template.deserialize(base64.decodeBuffer(fingerprintTemplate.getTemplate()));
			}
			catch(Exception e) {
				Logger.exception(e);
				continue;
			}
			//指纹比对
			DPFPVerificationResult result = verificator.verify(features, template);
			if(result.isVerified()) {
				return fingerprintTemplate;
			}
		}
		return null;
	}
}
