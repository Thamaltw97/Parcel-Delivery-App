package com.example.parceldelivery.admin;

public class AssignDriver {

    private long driverId;
    private String driverName;
    private String driverStatus;

    public AssignDriver(long driverId, String driverName, String driverStatus) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverStatus = driverStatus;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }
}
