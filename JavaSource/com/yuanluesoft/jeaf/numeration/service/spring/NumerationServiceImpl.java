package com.yuanluesoft.jeaf.numeration.service.spring;

import java.util.Calendar;
import java.util.Random;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.numeration.pojo.NumerationSequence;
import com.yuanluesoft.jeaf.numeration.service.NumerationCallback;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class NumerationServiceImpl implements NumerationService {
	private DatabaseService databaseService; //数据库访问对象

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.numeration.service.NumerationService#generateNumeration(java.lang.String, java.lang.String, java.lang.String, boolean, com.yuanluesoft.jeaf.numeration.service.NumerationCallback)
	 */
	public String generateNumeration(String applicationTitle, String numerationName, String numerationConfig, boolean preview, NumerationCallback numerationCallback) throws ServiceException {
		Numeration numeration = generateNumeration(numerationConfig, numerationCallback);
		//获取序号
		int nextSequence = getNextSequenceNumber(numeration, applicationTitle, numerationName, numerationConfig, preview);
		if(nextSequence==-1) {
			return numeration.getNumeration();
		}
		String numerationValue = numeration.getNumeration();
		//替换编号中的序号,并返回
		return numerationValue.substring(0, numeration.getSequenceFieldBegin()) + (numeration.getSequenceFieldLength()==0 ? "" + nextSequence : StringUtils.formatNumber(nextSequence, numeration.getSequenceFieldLength(), true)) + numerationValue.substring(numeration.getSequenceFieldEnd());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.numeration.service.NumerationService#getNextSequence(java.lang.String, java.lang.String, java.lang.String, boolean, com.yuanluesoft.jeaf.numeration.service.NumerationCallback)
	 */
	public int getNextSequence(String applicationTitle, String numerationName, String numerationConfig, boolean preview, NumerationCallback numerationCallback) throws ServiceException {
		Numeration numeration = generateNumeration(numerationConfig, numerationCallback);
		return getNextSequenceNumber(numeration, applicationTitle, numerationName, numerationConfig, preview);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.numeration.service.NumerationService#setSequence(java.lang.String, java.lang.String, java.lang.String, int, com.yuanluesoft.jeaf.numeration.service.NumerationCallback)
	 */
	public void setSequence(String applicationTitle, String numerationName, String numerationConfig, int sequence, NumerationCallback numerationCallback) throws ServiceException {
		Numeration numeration = generateNumeration(numerationConfig, numerationCallback);
		if(numeration.getSequenceFieldBegin()==-1) {
			return;
		}
		String hql = "from NumerationSequence NumerationSequence" +
					 " where NumerationSequence.application='" + JdbcUtils.resetQuot(applicationTitle) + "'" +
					 " and NumerationSequence.name='" + JdbcUtils.resetQuot(numerationName) + "'" +
					 " and NumerationSequence.numerationRange='" + JdbcUtils.resetQuot(numeration.getNumeration()) + "'";
		NumerationSequence numerationSequence = (NumerationSequence)getDatabaseService().findRecordByHql(hql);
		//保存序号
		if(numerationSequence==null) {
			numerationSequence = new NumerationSequence();
			numerationSequence.setId(UUIDLongGenerator.generateId()); //ID
			numerationSequence.setApplication(applicationTitle); //应用名称
			numerationSequence.setName(numerationName); //编号名称
			numerationSequence.setNumerationRange(numeration.getNumeration()); //范围
			numerationSequence.setSequence(sequence);
			getDatabaseService().saveRecord(numerationSequence);
		}
		else {
			numerationSequence.setSequence(sequence);
			getDatabaseService().updateRecord(numerationSequence);
		}
	}

	/**
	 * 获取下一个顺序号
	 * @param numeration
	 * @param applicationTitle
	 * @param numerationName
	 * @param numerationConfig
	 * @param preview
	 * @return
	 * @throws ServiceException
	 */
	private int getNextSequenceNumber(Numeration numeration, String applicationTitle, String numerationName, String numerationConfig, boolean preview) throws ServiceException {
		if(numeration.getSequenceFieldBegin()==-1) { //不需要输出编号
			return -1;
		}
		String numerationValue = numeration.getNumeration();
		//获取序号
		int nextSequence;
		String hql = "from NumerationSequence NumerationSequence" +
					 " where NumerationSequence.application='" + JdbcUtils.resetQuot(applicationTitle) + "'" +
					 " and NumerationSequence.name='" + JdbcUtils.resetQuot(numerationName) + "'" +
					 " and NumerationSequence.numerationRange='" + JdbcUtils.resetQuot(numerationValue) + "'";
		NumerationSequence numerationSequence = (NumerationSequence)getDatabaseService().findRecordByHql(hql);
		if(numerationSequence==null) {
			nextSequence = 1;
		}
		else {
			nextSequence = numerationSequence.getSequence() + 1;
		}
		if(!preview) { //非预览,保存序号
			if(numerationSequence==null) {
				numerationSequence = new NumerationSequence();
				numerationSequence.setId(UUIDLongGenerator.generateId()); //ID
				numerationSequence.setApplication(applicationTitle); //应用名称
				numerationSequence.setName(numerationName); //编号名称
				numerationSequence.setNumerationRange(numerationValue); //范围
				numerationSequence.setSequence(nextSequence);
				getDatabaseService().saveRecord(numerationSequence);
			}
			else {
				numerationSequence.setSequence(nextSequence);
				getDatabaseService().updateRecord(numerationSequence);
			}
		}
		return nextSequence;
	}

	/**
	 * 生成序号
	 * @param numerationConfig 如：<项目分类><年*4>年<月*2>月<日*2>日[<序号*3>]号
	 * @param sequence -1则不替换<序号*3>
	 * @param numerationCallback
	 * @return
	 */
	private Numeration generateNumeration(String numerationConfig, NumerationCallback numerationCallback) {
		int beginIndex = 0;
		Numeration numeration = new Numeration(); 
		String numerationValue = "";
		for(int endIndex=numerationConfig.indexOf('<'); endIndex!=-1; endIndex=numerationConfig.indexOf('<', beginIndex)) {
			for(; endIndex<numerationConfig.length()-1 && numerationConfig.charAt(endIndex+1)=='<'; endIndex++); //跳过"<<<"
			if(endIndex>beginIndex) {
				numerationValue += numerationConfig.substring(beginIndex, endIndex);
			}
			beginIndex = endIndex;
			//解析字段名称和显示长度
			endIndex = numerationConfig.indexOf('>', beginIndex + 1);
			if(endIndex==-1) { //没有找到尾
				break;
			}
			String[] values = numerationConfig.substring(beginIndex + 1, endIndex).split("\\x2a");
			String fieldName = values[0]; //字段名称
			int fieldLen; //字段长度
			try {
				fieldLen = Integer.parseInt(values[1]);
				fieldLen = Math.min(fieldLen, 20);
			}
			catch(Exception e) {
				fieldLen = 0;
			}
			//输出字段值
			Object fieldValue = getFieldValue(fieldName, fieldLen, numerationCallback);
			if(fieldValue==null) {
				if("序号".equals(fieldName) || "编号".equals(fieldName)) {
					numeration.setSequenceFieldBegin(numerationValue.length());
					numeration.setSequenceFieldEnd(numeration.getSequenceFieldBegin() + (endIndex - beginIndex + 1));
					numeration.setSequenceFieldLength(fieldLen);
				}
				numerationValue += numerationConfig.substring(beginIndex, endIndex + 1);
			}
			else if(fieldLen>=0 && (fieldValue instanceof Number)) { //数字,且指定了数字长度
				numerationValue += StringUtils.formatNumber(((Number)fieldValue).longValue(), fieldLen, true);
			}
			else {
				numerationValue += fieldValue;
			}
			beginIndex = endIndex + 1;
		}
		numeration.setNumeration(numerationValue + numerationConfig.substring(beginIndex));
		return numeration;
	}
	
	/**
	 * 获取字段值
	 * @param fieldName
	 * @param fieldLen
	 * @param numerationCallback
	 * @return
	 */
	private Object getFieldValue(String fieldName, int fieldLen, NumerationCallback numerationCallback) {
		if(numerationCallback!=null) {
			Object fieldValue = numerationCallback.getFieldValue(fieldName, fieldLen);
			if(fieldValue!=null) {
				return fieldValue;
			}
		}
		if("随机数".equals(fieldName)) {
			Random random = new Random();
			int randomValue = Math.abs(fieldLen==0 ? random.nextInt() : (random.nextInt((int)Math.pow(10, fieldLen)-1)));
			return new Integer(randomValue);
		}
		Calendar now = Calendar.getInstance();
		if("年".equals(fieldName)) {
			return new Integer(now.get(Calendar.YEAR));
		}
		if("月".equals(fieldName)) {
			return new Integer(now.get(Calendar.MONTH) + 1);
		}
		if("日".equals(fieldName)) {
			return new Integer(now.get(Calendar.DAY_OF_MONTH));
		}
		if("时".equals(fieldName)) {
			return new Integer(now.get(Calendar.HOUR));
		}
		if("分".equals(fieldName)) {
			return new Integer(now.get(Calendar.MINUTE));
		}
		if("秒".equals(fieldName)) {
			return new Integer(now.get(Calendar.SECOND));
		}
		return null;
	}
	
	/**
	 * 编号
	 * @author linchuan
	 *
	 */
	private class Numeration {
		private String numeration; //编号
		private int sequenceFieldBegin = -1; //序号字段在编号中的起始位置
		private int sequenceFieldEnd = -1; //序号字段在编号中的结束位置
		private int sequenceFieldLength = 0; //序号字段显示长度
		
		/**
		 * @return the numeration
		 */
		public String getNumeration() {
			return numeration;
		}
		/**
		 * @param numeration the numeration to set
		 */
		public void setNumeration(String numeration) {
			this.numeration = numeration;
		}
		/**
		 * @return the sequenceFieldBegin
		 */
		public int getSequenceFieldBegin() {
			return sequenceFieldBegin;
		}
		/**
		 * @param sequenceFieldBegin the sequenceFieldBegin to set
		 */
		public void setSequenceFieldBegin(int sequenceFieldBegin) {
			this.sequenceFieldBegin = sequenceFieldBegin;
		}
		/**
		 * @return the sequenceFieldEnd
		 */
		public int getSequenceFieldEnd() {
			return sequenceFieldEnd;
		}
		/**
		 * @param sequenceFieldEnd the sequenceFieldEnd to set
		 */
		public void setSequenceFieldEnd(int sequenceFieldEnd) {
			this.sequenceFieldEnd = sequenceFieldEnd;
		}
		/**
		 * @return the sequenceFieldLength
		 */
		public int getSequenceFieldLength() {
			return sequenceFieldLength;
		}
		/**
		 * @param sequenceFieldLength the sequenceFieldLength to set
		 */
		public void setSequenceFieldLength(int sequenceFieldLength) {
			this.sequenceFieldLength = sequenceFieldLength;
		}
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	
	public static void main(String[] args) {
		NumerationCallback numerationCallback = new NumerationCallback() {
			public Object getFieldValue(String fieldName, int fieldLength) {
				if(fieldName.equals("项目分类")) {
					return "建安";
				}
				if(fieldName.equals("号码")) {
					return new Integer(18);
				}
				return null;
			}
		};
		Numeration numeration = new NumerationServiceImpl().generateNumeration("<项目分类><号码*7>><<年*2>年<月*5>月<日*5>日<序号*8>", numerationCallback);
		String numerationValue = numeration.getNumeration();
		System.out.println("numeration:" + numerationValue);
		System.out.println("sequenceFieldBegin:" + numeration.getSequenceFieldBegin());
		System.out.println("sequenceFieldEnd:" + numeration.getSequenceFieldEnd());
		System.out.println("sequenceFieldLength:" + numeration.getSequenceFieldLength());
		int nextSequence = 100;
		numerationValue = numerationValue.substring(0, numeration.getSequenceFieldBegin()) + (numeration.getSequenceFieldLength()==0 ? "" + nextSequence : StringUtils.formatNumber(nextSequence, numeration.getSequenceFieldLength(), true)) + numerationValue.substring(numeration.getSequenceFieldEnd());
		System.out.println("new numeration:" + numerationValue);
	}
}