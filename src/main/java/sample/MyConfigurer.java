/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * @author Rob Winch
 *
 */
public class MyConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	/**
	 * In our sample application.properties is used to resolve permitAllPattern
	 */
	@Value("${permitAllPattern}")
	private String permitAllPattern;

	private String loginPage = "/login";


	@SuppressWarnings("unchecked")
	@Override
	public void init(HttpSecurity http) throws Exception {
		// autowire this bean
		ApplicationContext context = http.getSharedObject(ApplicationContext.class);
		context.getAutowireCapableBeanFactory().autowireBean(this);

		// Our DSL allows to grant access to URLs defined by permitAllPattern in a property
		// and then requires authentication for any other request
		http
			.authorizeRequests()
				.antMatchers(permitAllPattern).permitAll()
				.anyRequest().authenticated();

		if (http.getConfigurer(FormLoginConfigurer.class) == null) {
			// only apply if formLogin() was not invoked by the user
			// this is a way of providing a default, but allow users to override
			http
				.formLogin()
					.loginPage(loginPage);
		}
	}

	public MyConfigurer loginPage(String loginPage) {
		this.loginPage = loginPage;
		return this;
	}

	public static MyConfigurer myDsl() {
		return new MyConfigurer();
	}
}