package com.yuanluesoft.jeaf.word.service.spring;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.word.model.Word;
import com.yuanluesoft.jeaf.word.pojo.ChineseWord;
import com.yuanluesoft.jeaf.word.service.WordService;

/**
 * 
 * @author linchuan
 *
 */
public class WordServiceImpl extends BusinessServiceImpl implements WordService {

	/**
	 * 初始化
	 *
	 */
	public void init() {
		//30秒后启动导入
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				importChineseWords();
				timer.cancel(); 
			}
		}, 30000); //30秒
	}
	
	/**
	 * 导入中文词库
	 *
	 */
	private void importChineseWords() {
		if(getDatabaseService().findRecordByHql("from ChineseWord ChineseWord")!=null) { //已有词库
			return;
		}
		//导入词库
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			connection = DriverManager.getConnection("jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ=" + new File(Environment.getWebinfPath() + "jeaf/word/cnword/cnword.mdb").getPath(), null, null);
			statement = connection.createStatement();
			statement.execute("select * from cnword");
			resultSet = statement.getResultSet();
			while(resultSet.next()) {
				try {
					ChineseWord chineseWord = new ChineseWord();
					chineseWord.setId(UUIDLongGenerator.generateId()); //ID
					chineseWord.setWord(resultSet.getString("word").trim()); //词语
					chineseWord.setWordLength(chineseWord.getWord().length()); //长度
					chineseWord.setIsNoun("1".equals(resultSet.getString("名词")) ? 1 : 0); //是否名词
					chineseWord.setIsVerb("1".equals(resultSet.getString("动词")) ? 1 : 0); //是否动词
					chineseWord.setIsAdjective("1".equals(resultSet.getString("形容词")) ? 1 : 0); //是否形容词
					chineseWord.setIsAdverb("1".equals(resultSet.getString("副词")) ? 1 : 0); //是否副词
					chineseWord.setIsMeasure("1".equals(resultSet.getString("量词")) ? 1 : 0); //是否量词
					chineseWord.setIsOnomatopoeia("1".equals(resultSet.getString("拟声词")) ? 1 : 0); //是否拟声词
					chineseWord.setIsStructural("1".equals(resultSet.getString("结构助词")) ? 1 : 0); //是否结构助词
					chineseWord.setIsAuxiliary("1".equals(resultSet.getString("助词")) ? 1 : 0); //是否助词
					chineseWord.setIsCoordinating("1".equals(resultSet.getString("并列连词")) ? 1 : 0); //是否并列连词
					chineseWord.setIsConjunction("1".equals(resultSet.getString("连词")) ? 1 : 0); //是否连词
					chineseWord.setIsPreposition("1".equals(resultSet.getString("介词")) ? 1 : 0); //是否介词
					chineseWord.setIsPronoun("1".equals(resultSet.getString("代词")) ? 1 : 0); //是否代词
					chineseWord.setIsInterrogative("1".equals(resultSet.getString("疑问词")) ? 1 : 0); //是否疑问词
					chineseWord.setIsNumeral("1".equals(resultSet.getString("数词")) ? 1 : 0); //是否数词
					chineseWord.setIsProverbs("1".equals(resultSet.getString("成语")) ? 1 : 0); //是否成语
					getDatabaseService().saveRecord(chineseWord);
				}
				catch(Exception e) {
					
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try {
				resultSet.close();
			}
			catch(Exception e) {
				
			}
			try {
				statement.close();
			}
			catch(Exception e) {
				
			}
			try {
				connection.close();
			}
			catch(Exception e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.word.service.WordService#parseChineseSentence(java.lang.String, int[])
	 */
	public List parseChineseSentence(String sentence, int[] wordTypes) throws ServiceException {
		if(wordTypes==null || wordTypes.length==0) {
			wordTypes = new int[]{WORD_TYPE_NOUN, WORD_TYPE_VERB, WORD_TYPE_ADJECTIVE};
		}
		sentence = sentence.replaceAll("[-=\r\n\t~!@#$%\\^&*()_+`\\[\\]\\\\{}|;':\",./<>? 　《》？，。／；‘’：“”【】、｛｝｜－＝——＋～｀]", "");
		return doParseChineseSentence(sentence, wordTypes);
	}
	
	/**
	 * 解析句子
	 * @param sentence
	 * @param wordTypes
	 * @return
	 * @throws ServiceException
	 */
	private List doParseChineseSentence(String sentence, int[] wordTypes) throws ServiceException {
		List words = new ArrayList();
		for(int i=0; i<wordTypes.length; i++) {
			String word = searchWord(sentence, wordTypes[i]);
			if(word==null) {
				continue;
			}
			words.add(new Word(word, wordTypes[i]));
			int index = sentence.indexOf(word);
			if(index>0) {
				List leftWords = doParseChineseSentence(sentence.substring(0, index), wordTypes);
				words.addAll(0, leftWords);
			}
			if(index+word.length()<sentence.length()) {
				List rightWords = doParseChineseSentence(sentence.substring(index + word.length()), wordTypes);
				words.addAll(rightWords);
			}
			return words;
		}
		words.add(new Word(sentence, 0));
		return words;
	}
	
	/**
	 * 搜索词语
	 * @param sentence
	 * @param wordType
	 * @return
	 * @throws ServiceException
	 */
	private String searchWord(String sentence, int wordType) throws ServiceException {
		String wordTypeName = WORD_TYPE_NAMES[wordType];
		String hql = "select ChineseWord.word" +
					 " from ChineseWord ChineseWord" +
					 " where '" + sentence + "' like concat(concat('%', ChineseWord.word), '%')" +
					 " and ChineseWord.is" + wordTypeName.substring(0, 1).toUpperCase() + wordTypeName.substring(1) + "=1" +
					 " order by wordLength DESC, ChineseWord.word";
		return (String)getDatabaseService().findRecordByHql(hql);
	}
}