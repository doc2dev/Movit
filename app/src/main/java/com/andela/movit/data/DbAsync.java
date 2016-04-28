/**
 * This class provides a means to asynchronously execute a database operation wrapped in a
 * {@code DbOperation} object. It takes a {@code DbCallback} callback object that will be invoked
 * with the result of the database operation.
 * */

package com.andela.movit.data;

import android.os.AsyncTask;

public class DbAsync extends AsyncTask<DbOperation, String, DbResult> {

    private DbCallback dbCallback;

    public DbAsync(DbCallback dbCallback) {
        this.dbCallback = dbCallback;
    }

    @Override
    protected void onPostExecute(DbResult dbResult) {
        if (dbResult.getError() == null) {
            dbCallback.onOperationSuccess(dbResult.getResult());
        } else {
            Exception e = dbResult.getError();
            dbCallback.onOperationFail(e.getMessage());
        }
    }

    @Override
    protected DbResult doInBackground(DbOperation... params) {
        DbOperation operation = params[0];
        try {
            return operation.execute();
        } catch (Exception e) {
            return new DbResult(null, e);
        }
    }
}
