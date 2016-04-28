/**
 * This class provides a way to receive results from asynchronous database operations.
 * */

package com.andela.movit.data;

public class DbResult {

    private Object result;

    private Exception error;

    /**
     * Construct a result object.One of the parameters is set to null depending on whether
     * the operation was successful or not.
     * @param result the result of the operation, or null of unsuccessful
     * @param error an {@code Exception} object if the operation raised an error, or null
     * otherwise.
     * */

    public DbResult(Object result, Exception error) {
        this.error = error;
        this.result = result;
    }

    /**
     * Returns the result of the database operation. It is up to the calling code to cast the
     * result object to the type that was expected from the operation.
     * */

    public Object getResult() {
        return result;
    }

    /**
     * Returns the error thrown by an unsuccessful operation.
     * */

    public Exception getError() {
        return error;
    }
}
