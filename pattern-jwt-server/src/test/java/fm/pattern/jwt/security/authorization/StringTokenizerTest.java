/*
 * Copyright 2012-2017 the original author or authors.
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

package fm.pattern.jwt.security.authorization;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;

public class StringTokenizerTest {

	@Test
	public void shouldBeAbleToTokenizeAString() {
		Set<String> set = StringTokenizer.tokenize("first,second,third");
		assertThat(set).contains("first", "second", "third");
	}

	@Test
	public void shouldBeAbleToTokenizeAStringWithSpacesBetweenItems() {
		Set<String> set = StringTokenizer.tokenize("first,  second,  third");
		assertThat(set).contains("first", "second", "third");
	}

	@Test
	public void shouldBeAbleToTokenizeAStringWithSpacesBeforeAndAfterAndBetweenItems() {
		Set<String> set = StringTokenizer.tokenize("  first,  second,  third  ");
		assertThat(set).contains("first", "second", "third");
	}

	@Test
	public void shouldBeAbleToTokenizeAStringWithAdditionalCommas() {
		Set<String> set = StringTokenizer.tokenize("first,,,second,,third,,");
		assertThat(set).contains("first", "second", "third");
	}
	
	@Test
	public void shouldReturnAnEmptyListIfTheStringToTokenizeIsNull() {
		Set<String> set = StringTokenizer.tokenize("   ");
		assertThat(set).isEmpty();
	}
	
}
