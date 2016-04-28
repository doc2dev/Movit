package com.andela.movit.location;

import com.andela.movit.models.Movement;

public interface LocationCallback {
    void onLocationDetected(Movement movement);
}
