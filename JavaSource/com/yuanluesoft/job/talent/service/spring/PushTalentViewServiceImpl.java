package com.yuanluesoft.job.talent.service.spring;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.job.company.pojo.Job;
import com.yuanluesoft.job.company.service.JobCompanyService;

/**
 * 人才推送视图服务
 * @author chuan
 *
 */
public class PushTalentViewServiceImpl extends ViewServiceImpl {
	private JobCompanyService jobCompanyService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, int, boolean, boolean, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		//获取职位记录
		long jobId = RequestUtils.getParameterLongValue(request, "jobId");
		if(jobId<=0) {
			jobId = ((Long)request.getAttribute("jobId")).longValue();
		}
		Job job = (Job)jobCompanyService.load(Job.class, jobId);
		//添加连接
		view.addJoin("left join JobTalent.intentions intentions" +
				 " left join intentions.types types" + //求职意向工作性质
				 " left join intentions.areas areas" + //求职意向地点
				 " left join intentions.posts posts" + //求职意向职能类别
				 " left join intentions.companyTypes companyTypes"); //求职意向公司性质
		//年龄
		String where = null;
		if(job.getMinAge()!=0 || job.getMaxAge()!=0) {
			int year = DateTimeUtils.getYear(DateTimeUtils.now());
			where = year + " - year(JobTalent.birthday)";
			if(job.getMinAge()==0) {
				where += "<=" + job.getMaxAge();
			}
			else if(job.getMaxAge()==0) {
				where += ">=" + job.getMinAge();
			}
			else {
				where += " between " + job.getMinAge() + " and " + job.getMaxAge();
			}
		}
		//性别要求,不限制/A,男/M,女/F
		if(job.getSex()!='A') {
			where = (where==null ? "" : where + " and ") + " JobTalent.sex='" + job.getSex() + "'";
		}
		//工作年限,在读学生/-1,应届毕业生/0,1年,2年,3年,4年,5年,6年,7年,8年,9年,10年以上,不限/-2
		if(job.getWorkYear()!=-2) {
			where = (where==null ? "" : where + " and ") + " JobTalent.workYear>=" + job.getWorkYear();
		}
		//学历要求,初中,高中,职业高中,职业中专,中专,大专,本科,MBA,硕士,博士
		if(job.getQualification()!=-1) {
			where = (where==null ? "" : where + " and ") + " schoolings.qualification>=" + job.getQualification();
		}
		//月薪
		if(job.getMinMonthlyPay()!=0 || job.getMaxMonthlyPay()!=0) {
			where = (where==null ? "" : where + " and ") + "(intentions.minMonthlyPay<=" + job.getMinMonthlyPay() +
					" or intentions.minMonthlyPay<=" + job.getMaxMonthlyPay() + ")";
		}
		//职能类别
		if(job.getPostId()>0) {
			where = (where==null ? "" : where + " and ") + "posts.postId=" + job.getPostId();
		}
		//企业规模
		where = (where==null ? "" : where + " and ") + "intentions.companyScale<=" + job.getCompany().getScale();
		//工作地点
		if(job.getAreas()!=null && !job.getAreas().isEmpty()) {
			where = (where==null ? "" : where + " and ") + "(areas.id is null" +
					" or areas.areaId in (select OrgSubjection.parentDirectoryId from OrgSubjection OrgSubjection where OrgSubjection.directoryId in (" + ListUtils.join(job.getAreas(), "areaId", ",", true) + ")))";
		}
		//工作性质
		if(job.getTypes()!=null && !job.getTypes().isEmpty()) {
			where = (where==null ? "" : where + " and ") + "(types.id is null" +
					" or types.type in (" + ListUtils.join(job.getTypes(), "type", ",", true) + "))";
		}
		view.addWhere(where);
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
	}

	/**
	 * @return the jobCompanyService
	 */
	public JobCompanyService getJobCompanyService() {
		return jobCompanyService;
	}

	/**
	 * @param jobCompanyService the jobCompanyService to set
	 */
	public void setJobCompanyService(JobCompanyService jobCompanyService) {
		this.jobCompanyService = jobCompanyService;
	}

}