package com.example.booknm;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private FirebaseFirestore db;

    public FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Syncs a local booking to Firebase Firestore.
     * This justifies the project as a modern, cloud-connected application.
     */
    public void syncBookingToCloud(Booking booking, String userName, String venueName) {
        if (booking == null) return;
        
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("bookingId", booking.getId());
        bookingData.put("userName", userName);
        bookingData.put("venueName", venueName);
        bookingData.put("date", booking.getDate());
        bookingData.put("startTime", booking.getStartTime());
        bookingData.put("endTime", booking.getEndTime());
        bookingData.put("purpose", booking.getPurpose());
        bookingData.put("status", booking.getStatus());
        bookingData.put("timestamp", System.currentTimeMillis());

        db.collection("bookings")
                .document(String.valueOf(booking.getId()))
                .set(bookingData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Booking synced to Firestore!"))
                .addOnFailureListener(e -> Log.e(TAG, "Sync failed", e));
    }

    /**
     * Update booking status in cloud when admin approves/rejects
     */
    public void updateBookingStatusInCloud(int bookingId, String status) {
        if (status == null) return;
        
        db.collection("bookings")
                .document(String.valueOf(bookingId))
                .update("status", status)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Status updated in Firestore!"))
                .addOnFailureListener(e -> Log.e(TAG, "Status update failed", e));
    }
}
