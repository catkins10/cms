package com.yuanluesoft.jeaf.tools.updatepassword.actions;

import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.security.service.exception.SecurityException;
import com.yuanluesoft.jeaf.system.pojo.SystemSN;

/**
 * 
 * @author linchuan
 *
 */
public class UpdatePassword extends BaseAction {
	private SecretKey deskey = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!request.getServerName().equals("localhost")) { //只允许在服务器上操作
    		throw new Exception();
    	}
    	//更新系统用户(Person)
    	updatePassword("from Person Person", "password");
		//更新网上注册用户(Member)
    	updatePassword("from Member Member", "password");
		//更新招投标用户(BiddingEmployee)
    	updatePassword("from BiddingEmployee BiddingEmployee", "password");
		//网络联盟用户(LogisticsUser)
    	updatePassword("from LogisticsUser LogisticsUser", "password");
    	response.getWriter().write("completed");
		return null;
    }
    
    /**
     * 
     * @param hql
     * @param passwordFieldName
     * @throws Exception
     */
    private void updatePassword(String hql, String passwordFieldName) {
    	try {
	    	DatabaseService databaseService = (DatabaseService)getService("databaseService");
	    	CryptService cryptService = (CryptService)getService("cryptService");
	    	for(int i=0; ; i+=200) {
	    		List records = databaseService.findRecordsByHql(hql, i, 200);
	    		if(records==null || records.isEmpty()) {
	    			break;
	    		}
	    		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
	    			Record record = (Record)iterator.next();
	    			String password = (String)PropertyUtils.getProperty(record, passwordFieldName);
	    			if(password==null || password.isEmpty()) {
	    				continue;
	    			}
	    			try {
		    			password = decrypt(password);
		    			if(password.endsWith("" + record.getId())) {
			    			password = password.substring(0, password.length() - ("" + record.getId()).length());
			    			password = cryptService.encrypt(password, "" + record.getId(), false);
			    			PropertyUtils.setProperty(record, passwordFieldName, password);
			    			databaseService.updateRecord(record);
		    			}
	    			}
	    			catch(Exception e) {
	    				Logger.exception(e);
	    			}
	    		}
	    		if(records.size()<200) {
	    			break;
	    		}
	    	}
    	}
		catch(Exception e) {
			Logger.exception(e);
		}
    }
    
    /**
     * 解密
     * @param encrypted
     * @return
     * @throws SecurityException
     */
    public String decrypt(String encrypted) throws SecurityException {
        BASE64Decoder base64 = new BASE64Decoder();
        try {
		    //解密 
	        Cipher cipher = Cipher.getInstance("DES"); 
		    cipher.init(Cipher.DECRYPT_MODE, getSecretKey()); 
		    byte[] clearByte = cipher.doFinal(base64.decodeBuffer(encrypted)); 
		    return (new String(clearByte));
        }
        catch(Exception e) {
            Logger.exception(e);
            throw new SecurityException();
        }
    }
    
    /**
     * 获取密钥
     * @return
     * @throws SecurityException
     */
    private SecretKey getSecretKey() throws Exception {
        if(deskey!=null) {
        	return deskey;
        }
        DatabaseService databaseService = (DatabaseService)getService("databaseService");
        SystemSN systemSN = (SystemSN)databaseService.findRecordById(SystemSN.class.getName(), 0);
        try {
	        if(systemSN==null || systemSN.getAgnomen()==null || systemSN.getAgnomen().equals("")) {
	            //生成密钥 
	    	    KeyGenerator keygen = KeyGenerator.getInstance("DES"); //加密算法,可用DES,DESede,Blowfish  
	    	    //生成密钥
	    	    deskey = keygen.generateKey();
	    	    BASE64Encoder base64 = new BASE64Encoder();
	    	    boolean isNew = false;
	    	    if(systemSN==null) {
	    	    	systemSN = new SystemSN();
	    	    	systemSN.setId(0);
	    	        isNew = true;
	    	    }
	    	    systemSN.setAgnomen(base64.encode(deskey.getEncoded())); //将密钥存放在根组织表中
	    	    if(isNew) {
	    	        databaseService.saveRecord(systemSN);
	    	    }
	    	    else {
	    	        databaseService.updateRecord(systemSN);
	    	    }
	        }
	        else {
	            BASE64Decoder base64 = new BASE64Decoder();
	    	    DESKeySpec dks = new DESKeySpec(base64.decodeBuffer(systemSN.getAgnomen()));
	    	    //创建一个密钥工厂，然后用它把DESKeySpec转换成Secret Key对象
	    	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	    	    deskey = keyFactory.generateSecret(dks);
	        }
	        return deskey;
        }
        catch(Exception e) {
            Logger.exception(e);
            throw new SecurityException();
        }
    }
}