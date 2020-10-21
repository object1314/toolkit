/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * This class is meant to be extended by all REST resource "controllers". It
 * contains exception mapping and other common REST API functionality
 * 
 * @author XuYanhang
 *
 */
@ControllerAdvice
public class HttpRestErrorHandler {

	/** Logger */
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Handles all errors in a global way.
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public RestErrorBody handleThrowable(Throwable ex, WebRequest request, HttpServletResponse response) {
		logger.info("Handle Throwable-" + ex.getClass().getSimpleName() + ":" + ex.getMessage());
		return new RestErrorBody(Code.Fail, request.toString() + ":" + ex.getMessage());
	}

	/**
	 * The class for adding error information in the response
	 */
	public static class RestErrorBody implements java.io.Serializable {

		/**
		 * This error body is serializable
		 */
		private static final long serialVersionUID = 8583916843483647367L;

		private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter
				.ofPattern("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'+0000\'");

		/** Error timeStamp */
		public final String timestamp;
		/** Error status */
		public final int status;
		/** Error name */
		public final String error;
		/** Error message */
		public final String message;

		/**
		 * Initialize this error body.
		 * 
		 * @param code    the {@link #status} and the {@link #error} to set by a
		 *                {@link Code} who is non-null
		 * @param message the {@link #message} to set
		 */
		public RestErrorBody(Code code, String message) {
			super();
			this.timestamp = TIMESTAMP_FORMATTER.format(LocalDateTime.now(ZoneId.of("+0")));
			this.status = code.status;
			this.error = code.type;
			this.message = message;
		}

		/**
		 * Returns the string of this error body.
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return getClass().getSimpleName() + "{timestamp:" + timestamp + ",status:" + status + ",error:" + error
					+ ",message:" + message + "}";
		}

	}

	/**
	 * Custom error codes.
	 */
	public static enum Code {

		/** A default fail error code */
		Fail(999);

		/** Error status */
		public final int status;
		/** Error type */
		public final String type;

		/*
		 * Initialize this code.
		 */
		private Code(int code) {
			this.status = code;
			this.type = this.name();
		}

		/*
		 * Initialize this code.
		 */
		private Code(int code, String type) {
			this.status = code;
			this.type = type;
		}

	}
}
