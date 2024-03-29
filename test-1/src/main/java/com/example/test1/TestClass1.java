/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.test1;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

/**
 * This is a test class.
 * <p>
 * This is some javadoc that should be deployed.
 *
 * @author Steve Riesenberg
 * @since 1.0
 */
public class TestClass1 {

	/**
	 * This is a method.
	 * @deprecated Use {@link #method2(String)} instead
	 */
	@Deprecated
	public void method1() {
		Filter filter = (request, response, chain) -> {
			HttpServletRequest req = (HttpServletRequest) request;
			System.out.println(req.getRequestURI());
		};
	}

	/**
	 * This is another method.
	 * @param param This is a parameter
	 */
	public void method2(String param) {
		System.out.println("Hello, world");
	}

}
