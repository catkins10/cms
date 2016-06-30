package com.yuanluesoft.jeaf.dataimport.services.pinghe;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.inquiry.dataimporter.InquiryImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 平和县政府在线调查
 * @author linchuan
 *
 */
public class InquiryImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "平和县政府在线调查";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//站点数据导入
		dataImporters.add(new InquiryImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
					 "select " +
					 " ID as importDataId," + //被导入的记录ID
					 " Title as subject," + //调查主题
					 " null as endTime," + //调查截止时间
					 " Type as isMultiSelect," + //是否多选, '1'/多选, '0'/单选
					 " '2' as publishResult," + //投票结果公示,0/不对外公开,1/投票结束后公开,2/总是公开
					 " '1' as isAnonymous," + //是否匿名投票, '1'、'0'
					 " 1 as minVote," + //最小投票数
					 " 100 as maxVote," + //最大投票数
					 " null as creator," + //创建人
					 " VoteTime as created," + //创建时间
					 " Content as inquiryOptions," + //选项列表
					 " '@' as inquiryOptionSeparator," + //选项列表分隔符
					 " Nums as inquiryVotes," + //投票数列表
					 " '@' as inquiryVotesSeparator" + //投票数列表分隔符
					 " from Cl_Vote"
				};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.inquiry.dataimporter.InquiryImporter#generateInquiryOptionsSql(java.lang.String)
			 */
			public String generateInquiryOptionsSql(String inquirySubjectId) {
				return null;
			}
		});
		return dataImporters;
	}
}