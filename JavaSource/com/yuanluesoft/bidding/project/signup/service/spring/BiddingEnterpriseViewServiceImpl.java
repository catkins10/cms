package com.yuanluesoft.bidding.project.signup.service.spring;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 投标企业视图服务
 * @author linchuan
 *
 */
public class BiddingEnterpriseViewServiceImpl extends ViewServiceImpl {
	private BiddingService biddingService; //投标服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//检查开标公示是否已经存在
		String hql = "select BiddingProjectBidopening.id" +
					 " from BiddingProjectBidopening BiddingProjectBidopening" +
					 " where BiddingProjectBidopening.projectId=" + RequestUtils.getParameterLongValue(request, "projectId");
		if(getDatabaseService().findRecordByHql(hql)==null) {
			return null;
		}
		List signUps = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		for(Iterator iterator = signUps==null ? null : signUps.iterator(); iterator!=null && iterator.hasNext();) {
			BiddingSignUp signUp = (BiddingSignUp)iterator.next();
			biddingService.decryptSignUp(signUp); //企业信息解密
		}
		if(signUps==null) {
			return null;
		}
		Collections.sort(signUps, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				BiddingSignUp signUp0 = (BiddingSignUp)arg0;
				BiddingSignUp signUp1 = (BiddingSignUp)arg1;
				return Collator.getInstance(Locale.CHINA).compare(signUp0.getEnterpriseName(), signUp1.getEnterpriseName());
			}
		});
		return signUps;
	}

	/**
	 * @return the biddingService
	 */
	public BiddingService getBiddingService() {
		return biddingService;
	}

	/**
	 * @param biddingService the biddingService to set
	 */
	public void setBiddingService(BiddingService biddingService) {
		this.biddingService = biddingService;
	}
}