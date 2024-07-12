package pl.mnykolaichuk.stock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CompanyAlreadyExistException extends RuntimeException{

    public CompanyAlreadyExistException(String resourceName, String fieldName, String filedValue) {
        super(String.format("%s already exist with the given input data %s: '%s'", resourceName, fieldName, filedValue));
    }
}
