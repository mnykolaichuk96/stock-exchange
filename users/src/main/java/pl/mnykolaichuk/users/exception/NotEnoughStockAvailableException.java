package pl.mnykolaichuk.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotEnoughStockAvailableException extends RuntimeException{

    public NotEnoughStockAvailableException(String resourceName, String fieldName, String filedValue) {
        super(String.format("%s not found with the given input data %s: '%s'", resourceName, fieldName, filedValue));
    }
}
