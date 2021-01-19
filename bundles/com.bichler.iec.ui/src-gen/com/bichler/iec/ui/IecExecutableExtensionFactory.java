/*
 * generated by Xtext
 */
package com.bichler.iec.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import com.bichler.iec.ui.internal.IecActivator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class IecExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return IecActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return IecActivator.getInstance().getInjector(IecActivator.COM_BICHLER_IEC_IEC);
	}
	
}
