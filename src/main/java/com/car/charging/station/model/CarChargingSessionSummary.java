package com.car.charging.station.model;

public class CarChargingSessionSummary {

    private long totalCount;
    private long startedCount;
    private long stoppedCount;

    public CarChargingSessionSummary(long totalCount, long startedCount, long stoppedCount) {
        this.totalCount = totalCount;
        this.startedCount = startedCount;
        this.stoppedCount = stoppedCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getStartedCount() {
        return startedCount;
    }

    public void setStartedCount(long startedCount) {
        this.startedCount = startedCount;
    }

    public long getStoppedCount() {
        return stoppedCount;
    }

    public void setStoppedCount(long stoppedCount) {
        this.stoppedCount = stoppedCount;
    }
}
