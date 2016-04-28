/**
 * This interface defines an operation to be performed whenever a string value that was expected
 * from a given source in the future (e.g. a database, broadcast, network fetch) has arrived.
 * */

package com.andela.movit.location;

public interface IncomingStringCallback {
    void onStringArrive(String incomingString);
}
