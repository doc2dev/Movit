package com.andela.movit.background;

import android.os.AsyncTask;

import com.andela.movit.data.DbCallback;
import com.andela.movit.data.DbOperation;
import com.andela.movit.data.DbResult;

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
