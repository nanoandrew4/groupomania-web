package com.greenapper.handlers;

import com.greenapper.dtos.ErrorDTO;
import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.exceptions.ValidationException;
import com.greenapper.logging.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Exception handler that converts internally thrown exceptions to a {@link ErrorDTO}, or one of its subclasses,
 * so they can be returned to the caller.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private LogManager LOG;

	@Autowired
	private MessageSource messageSource;

	private final Function<String, String> convertErrorMessage = s -> messageSource.getMessage(s, null, LocaleContextHolder.getLocale());

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handle(final RuntimeException ex) {
		LOG.warn("Attempting to handle exception: ", ex);

		HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		try {
			if (ex instanceof AccessDeniedException)
				responseStatus = HttpStatus.UNAUTHORIZED;
			else {
				final HttpStatus annotationCode = ex.getClass().getAnnotation(ResponseStatus.class).code();
				final HttpStatus annotationValue = ex.getClass().getAnnotation(ResponseStatus.class).value();

				if (annotationCode != HttpStatus.INTERNAL_SERVER_ERROR)
					responseStatus = annotationCode;
				else if (annotationValue != HttpStatus.INTERNAL_SERVER_ERROR)
					responseStatus = annotationValue;
			}
		} catch (NullPointerException e) {
			LOG.error("Unhandled exception", ex);
		}

		if (ex instanceof ValidationException) {
			final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO((ValidationException) ex);
			final List<String> convertedErrors = validationErrorDTO.getValidationErrors().stream().map(convertErrorMessage).collect(Collectors.toList());
			validationErrorDTO.setValidationErrors(convertedErrors);
			return new ResponseEntity<>(validationErrorDTO, responseStatus);
		}
		return new ResponseEntity<>(new ErrorDTO(ex), responseStatus);
	}
}