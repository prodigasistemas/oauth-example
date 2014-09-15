package com.example;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;

public class WebModule extends ServletModule {

	protected void configureServlets() {
		bind(CDIResource.class).in(Scopes.SINGLETON);
	}
}
