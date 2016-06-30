package com.yuanluesoft.jeaf.distribution.soap;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.yuanluesoft.jeaf.distribution.model.RemoteMethodInvoke;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.soap.BaseSoapService;
import com.yuanluesoft.jeaf.util.Encoder;

/**
 * 
 * @author linchuan
 *
 */
public class DistributionSoapService extends BaseSoapService {

	/**
	 * 心跳检查
	 * @return
	 * @throws ServiceException
	 */
	public boolean keepAlive() throws ServiceException {
		return true;
	}
	
	/**
	 * 远程方法调用
	 * @param encodedRemoteMethodInvoke
	 * @return
	 * @throws ServiceException
	 */
	public String remoteMethodInvoke(String encodedRemoteMethodInvoke) throws ServiceException {
		try {
			RemoteMethodInvoke methodInvoke = (RemoteMethodInvoke)Encoder.getInstance().objectBase64Decode(encodedRemoteMethodInvoke);
			if(Logger.isDebugEnabled()) {
				Logger.debug("DistributionService: invode method, service name is " + methodInvoke.getServiceName() + ", method name is " + methodInvoke.getMethodName() + ".");
			}
			Object remoteService = getSpringService(methodInvoke.getServiceName());
			Method[] methods = remoteService.getClass().getMethods();
			for(int i=0; i<methods.length; i++) {
				if(!methods[i].getName().equals(methodInvoke.getMethodName())) { //名称不匹配
					continue;
				}
				if(methods[i].getParameterTypes().length!=(methodInvoke.getArgs()==null ? 0 : methodInvoke.getArgs().length)) { //参数个数不一致
					continue;
				}
				try {
					Serializable returnValue = (Serializable)methods[i].invoke(remoteService, methodInvoke.getArgs());
					return returnValue==null ? null : Encoder.getInstance().objectBase64Encode(returnValue);
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
			throw new ServiceException("invoke failed");
		}
		catch(ServiceException e) {
			throw e;
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
}