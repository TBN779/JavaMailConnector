package com.javamail;

import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.SimpleBeanInfo;

public class MyClassBeanInfo extends SimpleBeanInfo {
	
	public MethodDescriptor[] getMedthodDescriptors() {

		MethodDescriptor[] methods = null;

		try {
			methods = new MethodDescriptor[1];

			final ParameterDescriptor[] params = new ParameterDescriptor[2];

			params[0] = new ParameterDescriptor();

			params[0].setDisplayName("userName");
			params[0].setShortDescription("username");

			params[1] = new ParameterDescriptor();

			params[1].setDisplayName("passWord");
			params[1].setShortDescription("password");

			params[2] = new ParameterDescriptor();

			params[2].setDisplayName("ID");
			params[2].setShortDescription("id");

			methods[0] = new MethodDescriptor(Javamail.class.getMethod( "returnAllMails", new Class[] { String.class, String.class, String.class }), params);
			
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return methods;

	}
}