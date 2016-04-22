package com.andela.movit.data;

public class DbResult {

    private Object result;

    private Exception error;

    public Object getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }

    public DbResult(Object result, Exception error) {
        this.error = error;
        this.result = result;
    }
}
