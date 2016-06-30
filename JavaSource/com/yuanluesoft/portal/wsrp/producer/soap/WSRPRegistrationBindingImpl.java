package com.yuanluesoft.portal.wsrp.producer.soap;

import java.rmi.RemoteException;

import oasis.names.tc.wsrp.v1.bind.WSRP_v1_Registration_Binding_SOAPImpl;
import oasis.names.tc.wsrp.v1.types.Extension;
import oasis.names.tc.wsrp.v1.types.InvalidRegistrationFault;
import oasis.names.tc.wsrp.v1.types.MissingParametersFault;
import oasis.names.tc.wsrp.v1.types.ModifyRegistration;
import oasis.names.tc.wsrp.v1.types.OperationFailedFault;
import oasis.names.tc.wsrp.v1.types.RegistrationContext;
import oasis.names.tc.wsrp.v1.types.RegistrationData;
import oasis.names.tc.wsrp.v1.types.RegistrationState;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.portal.wsrp.producer.WSRPProducer;

/**
 * 注册接口（可选）： 注册接口允许 WSRP 生产者要求 WSRP 消费者在通过服务描述和标记接口与服务进行交互之前进行某种形式的注册。
 * 通过这个机制，生产者可以为特定类型的消费者定制他的行为。
 * 例如，生产者可能基于特定的消费者过滤一些提供的 portlet。
 * 另外，注册接口提供了一个机制允许生产者和消费者进行对话，这样他们可以交换关于彼此技术能力的信息。
 * @author linchuan
 *
 */
public class WSRPRegistrationBindingImpl extends WSRP_v1_Registration_Binding_SOAPImpl {
	
	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Registration_Binding_SOAPImpl#register(oasis.names.tc.wsrp.v1.types.RegistrationData)
	 */
	public RegistrationContext register(RegistrationData registrationData) throws RemoteException, OperationFailedFault, MissingParametersFault {
		try {
			return WSRPProducer.getWSRPProducer().register(registrationData);
		}
		catch(RemoteException e) {
			throw e;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Registration_Binding_SOAPImpl#modifyRegistration(oasis.names.tc.wsrp.v1.types.ModifyRegistration)
	 */
	public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws RemoteException, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault {
		try {
			return WSRPProducer.getWSRPProducer().modifyRegistration(modifyRegistration);
		}
		catch(RemoteException e) {
			throw e;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Registration_Binding_SOAPImpl#deregister(oasis.names.tc.wsrp.v1.types.RegistrationContext)
	 */
	public Extension[] deregister(RegistrationContext deregister) throws RemoteException, InvalidRegistrationFault, OperationFailedFault {
		try {
			return WSRPProducer.getWSRPProducer().deregister(deregister);
		}
		catch(RemoteException e) {
			throw e;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}
}