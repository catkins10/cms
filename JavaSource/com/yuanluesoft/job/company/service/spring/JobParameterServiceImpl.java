package com.yuanluesoft.job.company.service.spring;

import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.system.services.InitializeService;
import com.yuanluesoft.job.company.pojo.JobCompanyApproval;
import com.yuanluesoft.job.company.pojo.JobCompanyMailTemplate;
import com.yuanluesoft.job.company.pojo.JobParameterDirectory;
import com.yuanluesoft.job.company.service.JobParameterService;
import com.yuanluesoft.job.talent.pojo.JobTalentApproval;

/**
 * 
 * @author linchuan
 *
 */
public class JobParameterServiceImpl extends DirectoryServiceImpl implements JobParameterService, InitializeService {
	private int jobRefreshLimitation = 10; //职位刷新次数限制,默认10次

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.system.services.InitializeService#init(java.lang.String, long, java.lang.String)
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException {
		if(getDirectory(0)!=null) {
			return false;
		}
		JobParameterDirectory directory = new JobParameterDirectory();
		directory.setId(0);
		directory.setDirectoryName("参数配置");
		directory.setDirectoryType("root");
		createDirectory(directory);
		
		//添加行业目录
		JobParameterDirectory industryDirectory = new JobParameterDirectory();
		industryDirectory.setId(1);
		industryDirectory.setDirectoryName("行业");
		industryDirectory.setDirectoryType("industryRoot");
		industryDirectory.setParentDirectoryId(directory.getId());
		createDirectory(industryDirectory);

		//添加专业目录
		JobParameterDirectory specialtyDirectory = new JobParameterDirectory();
		specialtyDirectory.setId(2);
		specialtyDirectory.setDirectoryName("专业");
		specialtyDirectory.setDirectoryType("specialtyRoot");
		specialtyDirectory.setParentDirectoryId(directory.getId());
		createDirectory(specialtyDirectory);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getBaseDirectoryClass()
	 */
	public Class getBaseDirectoryClass() {
		return JobParameterDirectory.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getTreeRootNodeIcon()
	 */
	public String getTreeRootNodeIcon() {
		return "/job/company/icons/root.gif";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryLinkClass()
	 */
	public Class getDirectoryLinkClass() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryPopedomTypes()
	 */
	public DirectoryPopedomType[] getDirectoryPopedomTypes() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryTypes()
	 */
	public DirectoryType[] getDirectoryTypes() {
		return new DirectoryType[] {
				new DirectoryType("root", null, "根目录", JobParameterDirectory.class, "/job/company/icons/root.gif", null, true),
				
				new DirectoryType("industryRoot", "root", "行业根目录", JobParameterDirectory.class, "/job/company/icons/folder.gif", null, true),
				new DirectoryType("industryCategory", "industryRoot,industryCategory", "行业分类", JobParameterDirectory.class, "/job/company/icons/folder.gif", null, true),
				new DirectoryType("industry", "industryRoot,industryCategory", "行业", JobParameterDirectory.class, "/job/company/icons/item.gif", null, true),
				new DirectoryType("post", "industry", "岗位", JobParameterDirectory.class, "/job/company/icons/post.gif", null, true),
				
				new DirectoryType("specialtyRoot", "root", "专业根目录", JobParameterDirectory.class, "/job/company/icons/folder.gif", null, true),
				new DirectoryType("specialtyCategory", "specialtyRoot,specialtyCategory", "专业分类", JobParameterDirectory.class, "/job/company/icons/folder.gif", null, true),
				new DirectoryType("specialty", "specialtyRoot,specialtyCategory", "专业", JobParameterDirectory.class, "/job/company/icons/item.gif", null, true)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#resetCopiedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory, com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException {
		return newDirectory;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#deleteDataInDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public void deleteDataInDirectory(Directory directory) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobParameterService#getJobCompanyApproval()
	 */
	public JobCompanyApproval getJobCompanyApproval() throws ServiceException {
		return (JobCompanyApproval)getDatabaseService().findRecordByHql("from JobCompanyApproval JobCompanyApproval");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobParameterService#getJobTalentApproval()
	 */
	public JobTalentApproval getJobTalentApproval() throws ServiceException {
		return (JobTalentApproval)getDatabaseService().findRecordByHql("from JobTalentApproval JobTalentApproval");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobParameterService#getMailTemplateByCompanyId(long)
	 */
	public JobCompanyMailTemplate getMailTemplateByCompanyId(long companyId) throws ServiceException {
		String hql = "from JobCompanyMailTemplate JobCompanyMailTemplate where JobCompanyMailTemplate.companyId=" + companyId;
		return (JobCompanyMailTemplate)getDatabaseService().findRecordByHql(hql);
	}

	/**
	 * @return the jobRefreshLimitation
	 */
	public int getJobRefreshLimitation() {
		return jobRefreshLimitation;
	}

	/**
	 * @param jobRefreshLimitation the jobRefreshLimitation to set
	 */
	public void setJobRefreshLimitation(int jobRefreshLimitation) {
		this.jobRefreshLimitation = jobRefreshLimitation;
	}
}