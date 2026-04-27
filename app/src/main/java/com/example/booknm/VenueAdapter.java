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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.Filter;
import android.widget.Filterable;

public class VenueAdapter extends ArrayAdapter<Venue> implements Filterable {
    private List<Venue> originalVenues;
    private List<Venue> filteredVenues;

    // Map to hold icons for different venue types
    private final Map<String, Integer> venueIcons = new HashMap<>();

    public VenueAdapter(@NonNull Context context, @NonNull List<Venue> venues) {
        super(context, 0, venues);
        this.originalVenues = new ArrayList<>(venues);
        this.filteredVenues = venues;
        // Pre-populate icons...
        venueIcons.put("lab", R.drawable.ic_venue_lab);
        venueIcons.put("seminar", R.drawable.ic_venue_seminar);
        venueIcons.put("auditorium", R.drawable.ic_venue_auditorium);
    }

    @Override
    public int getCount() {
        return filteredVenues.size();
    }

    @Nullable
    @Override
    public Venue getItem(int position) {
        return filteredVenues.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate our new custom layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_venue, parent, false);
        }

        // Get the views from our custom layout
        TextView tvName = convertView.findViewById(R.id.tvVenueName);
        TextView tvDescription = convertView.findViewById(R.id.tvVenueDescription);
        ImageView ivIcon = convertView.findViewById(R.id.ivVenueIcon);

        // Get the data for the current row
        Venue venue = getItem(position);

        if (venue != null) {
            // Populate the data
            tvName.setText(venue.getName());
            tvDescription.setText(venue.getDescription());

            // Set the appropriate icon based on the venue name
            String nameLower = venue.getName().toLowerCase();
            if (nameLower.contains("lab")) {
                ivIcon.setImageResource(venueIcons.get("lab"));
            } else if (nameLower.contains("seminar")) {
                ivIcon.setImageResource(venueIcons.get("seminar"));
            } else if (nameLower.contains("auditorium")) {
                ivIcon.setImageResource(venueIcons.get("auditorium"));
            } else {
                ivIcon.setImageResource(R.drawable.ic_venue_default); // A default icon
            }
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = originalVenues;
                    results.count = originalVenues.size();
                } else {
                    String searchStr = constraint.toString().toLowerCase();
                    List<Venue> matchResults = new ArrayList<>();
                    for (Venue v : originalVenues) {
                        if (v.getName().toLowerCase().contains(searchStr) || 
                            v.getDescription().toLowerCase().contains(searchStr)) {
                            matchResults.add(v);
                        }
                    }
                    results.values = matchResults;
                    results.count = matchResults.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredVenues = (List<Venue>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
