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

package fm.pattern.jwt.server.service;

import java.util.List;

import fm.pattern.jwt.server.model.Audience;
import fm.pattern.microstructure.Result;

public interface AudienceService {

	Result<Audience> create(Audience audience);

	Result<Audience> update(Audience audience);

	Result<Audience> delete(Audience audience);

	Result<Audience> findById(String id);

	Result<Audience> findByName(String name);

	Result<List<Audience>> list();

}