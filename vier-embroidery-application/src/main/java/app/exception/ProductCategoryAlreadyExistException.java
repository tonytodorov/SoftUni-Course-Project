package app.exception;

public class ProductCategoryAlreadyExistException extends RuntimeException {

    public ProductCategoryAlreadyExistException(String message) {
        super(message);
    }

    public ProductCategoryAlreadyExistException() {

    }
}
