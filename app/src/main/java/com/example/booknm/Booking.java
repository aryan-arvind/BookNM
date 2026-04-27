package com.example.booknm;

public class Booking {
    private int id;
    private int userId;
    private int venueId;
    private String date;
    private String startTime;
    private String endTime;
    private String purpose;
    private String status; // New field

    // Full constructor including status
    public Booking(int id, int userId, int venueId, String date, String startTime, String endTime, String purpose, String status) {
        this.id = id;
        this.userId = userId;
        this.venueId = venueId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
    }

    // Constructor for creating a new booking (status is handled by DB)
    public Booking(int userId, int venueId, String date, String startTime, String endTime, String purpose) {
        this(-1, userId, venueId, date, startTime, endTime, purpose, "Pending");
    }

    // --- Getters ---
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getVenueId() { return venueId; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getPurpose() { return purpose; }
    public String getStatus() { return status; } // Getter for the new field
}
