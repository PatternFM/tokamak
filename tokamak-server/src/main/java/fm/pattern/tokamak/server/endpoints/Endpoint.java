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

package fm.pattern.tokamak.server.endpoints;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;
import fm.pattern.tokamak.sdk.commons.ErrorsRepresentation;
import fm.pattern.valex.AuthenticationException;
import fm.pattern.valex.AuthorizationException;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.InternalErrorException;
import fm.pattern.valex.ReportableException;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.UnprocessableEntityException;

@RestController
public class Endpoint {

	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(UnprocessableEntityException.class)
	public ErrorsRepresentation handleUnprocessableEntity(UnprocessableEntityException exception, HttpServletRequest request) {
		return convert(exception);
	}

	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(AuthenticationException.class)
	public ErrorsRepresentation handleAuthentication(AuthenticationException exception) {
		return convert(exception);
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(AuthorizationException.class)
	public ErrorsRepresentation handleAuthorization(AuthorizationException exception) {
		return convert(exception);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public ErrorsRepresentation handleEntityNotFound(EntityNotFoundException exception) {
		return convert(exception);
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InternalErrorException.class)
	public ErrorsRepresentation handleInternalError(EntityNotFoundException exception) {
		return convert(exception);
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(ResourceConflictException.class)
	public ErrorsRepresentation handleResourceConflict(ResourceConflictException exception) {
		return convert(exception);
	}

	private ErrorsRepresentation convert(ReportableException exception) {
		List<ErrorRepresentation> errors = exception.getErrors().stream().map(e -> new ErrorRepresentation(e.getCode(), e.getMessage())).collect(Collectors.toList());
		return new ErrorsRepresentation(errors);
	}

}
