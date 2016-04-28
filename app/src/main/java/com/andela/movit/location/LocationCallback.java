/**
 * This interface defines an operation to be performed whenever a new location is detected.
 * */

package com.andela.movit.location;

import com.andela.movit.models.Movement;

public interface LocationCallback {
    void onLocationDetected(Movement movement);
}
