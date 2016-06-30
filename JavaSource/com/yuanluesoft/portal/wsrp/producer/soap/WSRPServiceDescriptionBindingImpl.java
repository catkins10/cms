package com.yuanluesoft.portal.wsrp.producer.soap;

import java.rmi.RemoteException;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.portal.wsrp.producer.WSRPProducer;

import oasis.names.tc.wsrp.v1.bind.WSRP_v1_ServiceDescription_Binding_SOAPImpl;
import oasis.names.tc.wsrp.v1.types.GetServiceDescription;
import oasis.names.tc.wsrp.v1.types.InvalidRegistrationFault;
import oasis.names.tc.wsrp.v1.types.OperationFailedFault;
import oasis.names.tc.wsrp.v1.types.ServiceDescription;

/**
 * 服务描述接口（必选）： 服务描述接口允许 WSRP 生产者向消费者介绍它的功能。
 * WSRP 消费者可以使用这个接口来查询生产者，以便发现其提供了哪些 portlet，以及关于生产者自身的一些其他元数据。
 * 这个接口可以作为一个发现机制来确定所提供的 portlet，但是同样重要的是让消费者可以了解关于生产者技术能力的附加信息。
 * 生产者元数据可以包含消费者与任何 portlet 交互之前，生产者是否需要注册或初始化 cookie 的信息。
 * @author linchuan
 *
 */
public class WSRPServiceDescriptionBindingImpl extends WSRP_v1_ServiceDescription_Binding_SOAPImpl {

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_ServiceDescription_Binding_SOAPImpl#getServiceDescription(oasis.names.tc.wsrp.v1.types.GetServiceDescription)
	 */
	public ServiceDescription getServiceDescription(GetServiceDescription getServiceDescription) throws RemoteException, InvalidRegistrationFault, OperationFailedFault {
		try {
			return WSRPProducer.getWSRPProducer().getServiceDescription(getServiceDescription);
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
