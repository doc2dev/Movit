package com.andela.movit.data;

public interface DbCallback {
    void onOperationSuccess(Object result);
    void onOperationFail(String errorMessage);
}
