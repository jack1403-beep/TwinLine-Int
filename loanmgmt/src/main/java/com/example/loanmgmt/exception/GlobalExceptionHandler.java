package com.example.loanmgmt.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Global exception handler returning meaningful HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleBadRequest(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleConflict(IllegalStateException ex) {
		return ResponseEntity.status(409).body(ex.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
		return ResponseEntity.badRequest().body(ex.getBindingResult().getFieldErrors()
				.stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).toList());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleAll(Exception ex) {
		 return ResponseEntity.status(500).body("Server error: " + ex.getMessage());
	}
}