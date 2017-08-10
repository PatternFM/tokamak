package fm.pattern.tokamak.console;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fm.pattern.tokamak.sdk.commons.ErrorsRepresentation;
import fm.pattern.tokamak.sdk.commons.Result;

@Component
public class Controller {

	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(AuthenticationException.class)
	public ErrorsRepresentation handleAccessDeniedException(AuthenticationException exception) {
		return new ErrorsRepresentation(exception.getErrors());
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	public ErrorsRepresentation handleBadRequestException(BadRequestException exception) {
		return new ErrorsRepresentation(exception.getErrors());
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(AuthorizationException.class)
	public ErrorsRepresentation handleAuthorizationException(AuthorizationException exception) {
		return new ErrorsRepresentation(exception.getErrors());
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(UnprocessableEntityException.class)
	public ErrorsRepresentation handleUnprocessableEntityException(UnprocessableEntityException exception) {
		return new ErrorsRepresentation(exception.getErrors());
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public ErrorsRepresentation handleEntityNotFoundException(EntityNotFoundException exception) {
		return new ErrorsRepresentation(exception.getErrors());
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(UnhandledException.class)
	public ErrorsRepresentation handleUnhandledException(UnhandledException exception) {
		return new ErrorsRepresentation(exception.getErrors());
	}

	public <T> Result<T> verify(Result<T> response) {
		if (response.getResponseCode() >= 200 && response.getResponseCode() <= 299) {
			return response;
		}

		if (response.getResponseCode() == 422) {
			throw new UnprocessableEntityException(response.getErrors());
		}
		if (response.getResponseCode() == 404) {
			throw new EntityNotFoundException(response.getErrors());
		}
		if (response.getResponseCode() == 403) {
			throw new AuthorizationException(response.getErrors());
		}
		if (response.getResponseCode() == 401) {
			throw new AuthenticationException(response.getErrors());
		}
		if (response.getResponseCode() == 400) {
			throw new BadRequestException(response.getErrors());
		}
		if (response.getResponseCode() == 500) {
			throw new UnhandledException(response.getErrors());
		}

		throw new UnhandledException(response.getErrors());
	}

}
