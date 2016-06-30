package com.yuanluesoft.jeaf.tools.codetest;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibInterceptor implements MethodInterceptor {

	/* (non-Javadoc)
	 * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
	 */
	public Object intercept(Object object, Method method, Object[] args,  MethodProxy methodProxy) throws Throwable {
		Object result = methodProxy.invokeSuper(object, args);  
		return result;
	}
}