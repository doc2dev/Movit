package com.andela.movit.listeners;

import com.andela.movit.models.Movement;

public interface LocationCallback {
    void onMovementDetected(Movement movement);
    void onError(String message);
}
