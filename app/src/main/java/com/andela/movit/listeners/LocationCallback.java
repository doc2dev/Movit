package com.andela.movit.listeners;

import com.andela.movit.models.Movement;

public interface LocationCallback {
    void onLocationDetected(Movement movement);
}
