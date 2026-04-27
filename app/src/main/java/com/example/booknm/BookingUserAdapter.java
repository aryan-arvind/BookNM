package com.example.booknm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class BookingUserAdapter extends ArrayAdapter<Booking> {

    private DatabaseHelper db;

    public BookingUserAdapter(@NonNull Context context, @NonNull List<Booking> bookings) {
        super(context, 0, bookings);
        db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_booking_user, parent, false);
        }

        TextView tvVenueName = convertView.findViewById(R.id.tvBookingVenueName);
        TextView tvDateTime = convertView.findViewById(R.id.tvBookingDateTime);
        ImageView ivStatus = convertView.findViewById(R.id.ivBookingStatus);
        TextView tvStatus = convertView.findViewById(R.id.tvBookingStatus);

        Booking booking = getItem(position);
        if (booking != null) {
            Venue venue = db.getVenueById(booking.getVenueId());

            tvVenueName.setText(venue != null ? venue.getName() : "Unknown Venue");
            tvDateTime.setText("On " + booking.getDate() + " at " + booking.getStartTime());
            tvStatus.setText(booking.getStatus());

            // Set status icon and color
            switch (booking.getStatus()) {
                case "Approved":
                    ivStatus.setImageResource(R.drawable.ic_status_approved);
                    tvStatus.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
                    break;
                case "Denied":
                    ivStatus.setImageResource(R.drawable.ic_status_denied);
                    tvStatus.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
                    break;
                default: // Pending
                    ivStatus.setImageResource(R.drawable.ic_status_pending);
                    tvStatus.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
                    break;
            }
        }
        return convertView;
    }
}
