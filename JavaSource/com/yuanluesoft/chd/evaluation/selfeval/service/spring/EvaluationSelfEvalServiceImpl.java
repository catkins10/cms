package com.yuanluesoft.chd.evaluation.selfeval.service.spring;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.yuanluesoft.chd.evaluation.selfeval.pojo.ChdEvaluationSelf;
import com.yuanluesoft.chd.evaluation.selfeval.pojo.ChdEvaluationSelfSubjection;
import com.yuanluesoft.chd.evaluation.selfeval.service.EvaluationSelfEvalService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationSelfEvalServiceImpl extends BusinessServiceImpl implements EvaluationSelfEvalService {
	private boolean selfEvalCurrentMonth = false; //是否自查当月,默认是自查前一个月

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.selfeval.service.EvaluationSelfEvalService#updateSelfEvalSubjections(com.yuanluesoft.chd.evaluation.selfeval.pojo.ChdEvaluationSelf, boolean, java.lang.String)
	 */
	public void updateSelfEvalSubjections(ChdEvaluationSelf selfEval, boolean isNew, String subjectionDirectoryIds) throws ServiceException {
		if(subjectionDirectoryIds==null || subjectionDirectoryIds.equals("")) {
			return;
		}
		if(!isNew) {
			//检查隶属目录是否发生变化
			if(subjectionDirectoryIds.equals(ListUtils.join(selfEval.getSubjections(), "directoryId", ",", false))) {
				return;
			}
			//删除旧的隶属关系
			for(Iterator iterator = selfEval.getSubjections().iterator(); iterator.hasNext();) {
				ChdEvaluationSelfSubjection subjection = (ChdEvaluationSelfSubjection)iterator.next();
				getDatabaseService().deleteRecord(subjection);
			}
		}
		//保存新的隶属关系
		String[] ids = subjectionDirectoryIds.split(",");
		selfEval.setSubjections(new LinkedHashSet());
		for(int i=0; i<ids.length; i++) {
			if(ListUtils.findObjectByProperty(selfEval.getSubjections(), "directoryId", new Long(ids[i]))!=null) { //重复
				continue;
			}
			ChdEvaluationSelfSubjection subjection = new ChdEvaluationSelfSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setEvalId(selfEval.getId());
			subjection.setDirectoryId(Long.parseLong(ids[i]));
			getDatabaseService().saveRecord(subjection);
			selfEval.getSubjections().add(subjection);
		}
	}

	/**
	 * @return the selfEvalCurrentMonth
	 */
	public boolean isSelfEvalCurrentMonth() {
		return selfEvalCurrentMonth;
	}

	/**
	 * @param selfEvalCurrentMonth the selfEvalCurrentMonth to set
	 */
	public void setSelfEvalCurrentMonth(boolean selfEvalCurrentMonth) {
		this.selfEvalCurrentMonth = selfEvalCurrentMonth;
	}
}