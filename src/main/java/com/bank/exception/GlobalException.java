package com.bank.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler
    public ResponseEntity<ErrorObject>ResourceNotFoundExceptionHAndler(ResourceNotFoundException rs)
    {
        ErrorObject errorObject=new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(rs.getMessage());
        errorObject.setTimeStamped(System.currentTimeMillis());
        return  new ResponseEntity<ErrorObject>(errorObject,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject>NoDataFoundExceptionHAndler(NoDataFoundException rs)
    {
        ErrorObject errorObject=new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NO_CONTENT.value());
        errorObject.setMessage(rs.getMessage());
        errorObject.setTimeStamped(System.currentTimeMillis());
        return  new ResponseEntity<ErrorObject>(errorObject,HttpStatus.OK);
    }

}
