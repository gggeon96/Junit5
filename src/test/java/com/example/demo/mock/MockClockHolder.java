package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MockClockHolder implements ClockHolder {
    private final long millis;
    @Override
    public long millis() {
        return millis;
    }
}
