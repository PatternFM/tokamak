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

package fm.pattern.jwt.server.endpoints;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.commons.rest.ErrorsRepresentation;
import fm.pattern.jwt.server.conversion.EgressConversionService;
import fm.pattern.microstructure.AuthenticationException;
import fm.pattern.microstructure.AuthorizationException;
import fm.pattern.microstructure.EntityNotFoundException;
import fm.pattern.microstructure.InternalErrorException;
import fm.pattern.microstructure.ResourceConflictException;
import fm.pattern.microstructure.UnprocessableEntityException;

@RestController
public class Endpoint {

	private EgressConversionService egress;

	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(UnprocessableEntityException.class)
	public ErrorsRepresentation handleUnprocessableEntity(UnprocessableEntityException exception, HttpServletRequest request) {
		return egress.convert(exception);
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(AuthenticationException.class)
	public ErrorsRepresentation handleAuthentication(AuthenticationException exception) {
		return egress.convert(exception);
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(AuthorizationException.class)
	public ErrorsRepresentation handleAuthorization(AuthorizationException exception) {
		return egress.convert(exception);
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public ErrorsRepresentation handleEntityNotFound(EntityNotFoundException exception) {
		return egress.convert(exception);
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InternalErrorException.class)
	public ErrorsRepresentation handleInternalError(EntityNotFoundException exception) {
		return egress.convert(exception);
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(ResourceConflictException.class)
	public ErrorsRepresentation handleResourceConflict(ResourceConflictException exception) {
		return egress.convert(exception);
	}

	@Autowired
	public void setEgressConversionService(EgressConversionService egress) {
		this.egress = egress;
	}

}
