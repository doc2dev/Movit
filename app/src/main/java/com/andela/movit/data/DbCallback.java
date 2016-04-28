/**
 * This interface defines operations to be carried out after an asynchronous database operation.
 * */

package com.andela.movit.data;

public interface DbCallback {
    void onOperationSuccess(Object result);
    void onOperationFail(String errorMessage);
}
