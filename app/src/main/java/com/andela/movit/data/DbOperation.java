/**
 * This interface defines a way to package database operations that are to be carried out
 * asynchronously.
 * */

package com.andela.movit.data;

public interface DbOperation {
    DbResult execute();
}
