package com.yuanluesoft.jeaf.sensitive.service.spring;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sensitive.pojo.SensitiveWord;
import com.yuanluesoft.jeaf.sensitive.service.SensitiveWordService;
import com.yuanluesoft.jeaf.util.ChineseConverter;

/**
 * 
 * @author linchuan
 *
 */
public class SensitiveWordServiceImpl extends BusinessServiceImpl implements SensitiveWordService {
	private String presettingWords; //预置敏感词
	
	//私有属性
	private String[][] sensitiveWords; //敏感词列表

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		SensitiveWord sensitiveWord = (SensitiveWord)record;
		resetSensitiveWord(sensitiveWord); //重置
		super.save(sensitiveWord);
		loadSensitiveWords(); //更新列表
		return sensitiveWord;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		SensitiveWord sensitiveWord = (SensitiveWord)record;
		resetSensitiveWord(sensitiveWord); //重置
		super.update(sensitiveWord);
		loadSensitiveWords(); //更新列表
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		loadSensitiveWords(); //更新列表
	}
	
	/**
	 * 重置敏感词
	 * @param sensitiveWord
	 */
	private void resetSensitiveWord(SensitiveWord sensitiveWord) {
		if(sensitiveWord.getWords()==null || sensitiveWord.getWords().isEmpty()) {
			return;
		}
		Map map = new LinkedHashMap();
		String[] words = sensitiveWord.getWords().replaceAll("\\r", "").split("\\n");
		for(int i=0; i<words.length; i++) {
			words[i] = words[i].trim();
			if(!words[i].isEmpty()) {
				map.put(words[i], "");
			}
		}
		String newWords = null;
		for(Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String word = (String)iterator.next();
			newWords = (newWords==null ? "" : newWords + "\r\n") + word;
		}
		sensitiveWord.setWords(newWords);
	}

	/**
	 * 加载敏感词
	 *
	 */
	public void loadSensitiveWords() {
		SensitiveWord sensitiveWord = null;
		try {
			sensitiveWord = loadSensitiveWord();
		}
		catch(Exception e) {
			
		}
		Map wordMap = new LinkedHashMap();
		String[] words = (sensitiveWord==null || sensitiveWord.getWords()==null || sensitiveWord.getWords().isEmpty() ? presettingWords : sensitiveWord.getWords()).replaceAll("\\r", "").split("\\n");
		for(int i=0; i<words.length; i++) {
			words[i] = words[i].trim();
			if(words[i].isEmpty()) {
				continue;
			}
			//用于输出的提示信息
			String prompt = words[i].replaceAll("\\|", "、").replaceAll("\\.\\*", " ").replaceAll("\\\\s", "").replaceAll("[\\*\\^\\?]", "").replaceAll("\\[.*?\\]", " ").replaceAll("\\\\.", ".");
			//重试敏感词
			String word = words[i].replaceAll("\\.\\*", "[\\\\s\\\\S]*");
			String regx = "";
			for(int j=0; j<word.length(); j++) {
				char c = word.charAt(j);
				//转换为繁体
				char traditional = ChineseConverter.convertToTraditionalChinese(c);
				char simple = ChineseConverter.convertToSimpleChinese(c);
				regx += (traditional==simple ? "" + c : "[" + traditional + simple + "]");
				if(j<word.length()-1 && Character.isLetterOrDigit(c)  && Character.isLetterOrDigit(word.charAt(j+1))) {
					regx += "\\s*";
				}
			}
			try {
				Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
				wordMap.put(regx, prompt);
			}
			catch(Exception e) {
				try {
					Pattern.compile(words[i], Pattern.CASE_INSENSITIVE);
					wordMap.put(words[i], prompt);
				}
				catch(Exception ex) {
					
				}
			}
		}
		sensitiveWords = new String[wordMap.size()][2];
		int i = 0;
		for(Iterator iterator = wordMap.keySet().iterator(); iterator.hasNext();) {
			String word = (String)iterator.next();
			sensitiveWords[i][0] =  word;
			sensitiveWords[i++][1] =  (String)wordMap.get(word);
			if(Logger.isDebugEnabled()) {
				Logger.debug("SensitiveWordService: add sensitive word " + wordMap.get(word) + ", regx is " + word + ".");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sensitive.service.SensitiveWordService#loadSensitiveWord()
	 */
	public SensitiveWord loadSensitiveWord() throws ServiceException {
		return (SensitiveWord)getDatabaseService().findRecordByHql("from SensitiveWord SensitiveWord");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sensitive.service.SensitiveWordService#findSensitiveWord(java.lang.String)
	 */
	public String findSensitiveWord(String text) {
		if(text==null || text.isEmpty()) {
			return null;
		}
		for(int i=0; i<sensitiveWords.length; i++) {
			Pattern pattern = Pattern.compile(sensitiveWords[i][0], Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(text); 
	        if(matcher.find()) {
	        	return sensitiveWords[i][1];
	        }
		}
		return null;
	}

	/**
	 * @return the presettingWords
	 */
	public String getPresettingWords() {
		return presettingWords;
	}

	/**
	 * @param presettingWords the presettingWords to set
	 */
	public void setPresettingWords(String presettingWords) {
		this.presettingWords = presettingWords.replaceAll("\\t", "");
	}
}