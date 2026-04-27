package com.example.booknm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "booknm.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Table names
    public static final String T_USERS = "users";
    public static final String T_VENUES = "venues";
    public static final String T_BOOKINGS = "bookings";

    // Common Columns
    public static final String C_ID = "id";

    // Venues Table Columns
    public static final String C_VENUE_NAME = "name";
    public static final String C_VENUE_CAPACITY = "capacity";
    public static final String C_VENUE_LOCATION = "location";
    public static final String C_VENUE_DESCRIPTION = "description";

    // Bookings Table Columns
    public static final String C_BOOKING_USER_ID = "user_id";
    public static final String C_BOOKING_VENUE_ID = "venue_id";
    public static final String C_BOOKING_DATE = "date";
    public static final String C_BOOKING_START_TIME = "start_time";
    public static final String C_BOOKING_END_TIME = "end_time";
    public static final String C_BOOKING_PURPOSE = "purpose";
    public static final String C_BOOKING_STATUS = "status"; // The approval status column

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Drop existing tables to ensure a clean slate (good for development)
        db.execSQL("DROP TABLE IF EXISTS " + T_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + T_VENUES);
        db.execSQL("DROP TABLE IF EXISTS " + T_BOOKINGS);

        String createUsers = "CREATE TABLE " + T_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT, role TEXT)";

        String createVenues = "CREATE TABLE " + T_VENUES + " ("
                + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + C_VENUE_NAME + " TEXT, "
                + C_VENUE_CAPACITY + " INTEGER, "
                + C_VENUE_LOCATION + " TEXT, "
                + C_VENUE_DESCRIPTION + " TEXT)";

        String createBookings = "CREATE TABLE " + T_BOOKINGS + " ("
                + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + C_BOOKING_USER_ID + " INTEGER, "
                + C_BOOKING_VENUE_ID + " INTEGER, "
                + C_BOOKING_DATE + " TEXT, "
                + C_BOOKING_START_TIME + " TEXT, "
                + C_BOOKING_END_TIME + " TEXT, "
                + C_BOOKING_PURPOSE + " TEXT, "
                + C_BOOKING_STATUS + " TEXT DEFAULT 'Pending'," // New status column
                + "UNIQUE(" + C_BOOKING_VENUE_ID + ", " + C_BOOKING_DATE + ", " + C_BOOKING_START_TIME + ") ON CONFLICT FAIL"
                + ")";

        db.execSQL(createUsers);
        db.execSQL(createVenues);
        db.execSQL(createBookings);

        // Insert all the new, detailed venues
        addDefaultVenues(db);

        // Insert default admin
        ContentValues admin = new ContentValues();
        admin.put("name", "Default Admin");
        admin.put("email", "admin@nmims.in");
        admin.put("password", "Admin123");
        admin.put("role", "admin");
        db.insertWithOnConflict(T_USERS, null, admin, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void addDefaultVenues(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Labs with i3
        values.put(C_VENUE_NAME, "Lab L101");
        values.put(C_VENUE_CAPACITY, 40);
        values.put(C_VENUE_LOCATION, "1st Floor, Tech Wing");
        values.put(C_VENUE_DESCRIPTION, "40 PCs (Intel i3), Standard Projector");
        db.insert(T_VENUES, null, values);

        values.put(C_VENUE_NAME, "Lab L102");
        db.insert(T_VENUES, null, values);

        // Labs with i5
        values.put(C_VENUE_NAME, "Lab L103");
        values.put(C_VENUE_DESCRIPTION, "40 PCs (Intel i5), Standard Projector");
        db.insert(T_VENUES, null, values);

        values.put(C_VENUE_NAME, "Lab L104");
        db.insert(T_VENUES, null, values);

        values.put(C_VENUE_NAME, "Lab L105");
        db.insert(T_VENUES, null, values);

        values.put(C_VENUE_NAME, "Lab L106");
        db.insert(T_VENUES, null, values);

        // Premium Labs
        values.put(C_VENUE_NAME, "Lab L209 (Premium)");
        values.put(C_VENUE_CAPACITY, 70);
        values.put(C_VENUE_LOCATION, "2nd Floor, Main Building");
        values.put(C_VENUE_DESCRIPTION, "70 PCs (Intel i7), Pair Programming Setup, Spacious");
        db.insert(T_VENUES, null, values);

        values.put(C_VENUE_NAME, "Lab L210 (Premium)");
        db.insert(T_VENUES, null, values);

        // Seminar Room
        values.put(C_VENUE_NAME, "Seminar Room");
        values.put(C_VENUE_CAPACITY, 50);
        values.put(C_VENUE_LOCATION, "Ground Floor, Admin Block");
        values.put(C_VENUE_DESCRIPTION, "Standalone chairs with laptop stands, Yoga mats available");
        db.insert(T_VENUES, null, values);

        // Auditorium
        values.put(C_VENUE_NAME, "Auditorium");
        values.put(C_VENUE_CAPACITY, 250);
        values.put(C_VENUE_LOCATION, "Main Campus Building");
        values.put(C_VENUE_DESCRIPTION, "200+ Cushioned Seats, Dolby Atmos Audio, Large Smartboard");
        db.insert(T_VENUES, null, values);

        // Reserved Venues
        values.put(C_VENUE_NAME, "Lab L107 (Reserved)");
        values.put(C_VENUE_CAPACITY, 40);
        values.put(C_VENUE_LOCATION, "1st Floor, Exam Wing");
        values.put(C_VENUE_DESCRIPTION, "Reserved for college work/exam department");
        db.insert(T_VENUES, null, values);

        values.put(C_VENUE_NAME, "Lab L108 (Reserved)");
        db.insert(T_VENUES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // --- USER METHODS ---
    public boolean addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", user.getName());
        cv.put("email", user.getEmail());
        cv.put("password", user.getPassword());
        cv.put("role", user.getRole());
        long id = db.insert(T_USERS, null, cv);
        return id != -1;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + T_USERS + " WHERE email = ?", new String[]{email});
        if (c.moveToFirst()) {
            User u = new User(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("email")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getString(c.getColumnIndexOrThrow("role"))
            );
            c.close();
            return u;
        }
        c.close();
        return null;
    }

    public User getUserById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + T_USERS + " WHERE id = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            User u = new User(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("email")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getString(c.getColumnIndexOrThrow("role"))
            );
            c.close();
            return u;
        }
        c.close();
        return null;
    }

    // --- VENUE METHODS ---
    public long addVenue(Venue v) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(C_VENUE_NAME, v.getName());
        cv.put(C_VENUE_CAPACITY, v.getCapacity());
        cv.put(C_VENUE_LOCATION, v.getLocation());
        cv.put(C_VENUE_DESCRIPTION, v.getDescription());
        return db.insert(T_VENUES, null, cv);
    }

    public Venue getVenueById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + T_VENUES + " WHERE id = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            Venue v = new Venue(
                    c.getInt(c.getColumnIndexOrThrow(C_ID)),
                    c.getString(c.getColumnIndexOrThrow(C_VENUE_NAME)),
                    c.getInt(c.getColumnIndexOrThrow(C_VENUE_CAPACITY)),
                    c.getString(c.getColumnIndexOrThrow(C_VENUE_LOCATION)),
                    c.getString(c.getColumnIndexOrThrow(C_VENUE_DESCRIPTION))
            );
            c.close();
            return v;
        }
        c.close();
        return null;
    }

    public List<Venue> getAllVenues() {
        List<Venue> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        // Filter out reserved venues from the user-facing list
        Cursor c = db.rawQuery("SELECT * FROM " + T_VENUES + " WHERE " + C_VENUE_NAME + " NOT LIKE '%(Reserved)%' ORDER BY name", null);
        while (c.moveToNext()) {
            list.add(new Venue(
                    c.getInt(c.getColumnIndexOrThrow(C_ID)),
                    c.getString(c.getColumnIndexOrThrow(C_VENUE_NAME)),
                    c.getInt(c.getColumnIndexOrThrow(C_VENUE_CAPACITY)),
                    c.getString(c.getColumnIndexOrThrow(C_VENUE_LOCATION)),
                    c.getString(c.getColumnIndexOrThrow(C_VENUE_DESCRIPTION))
            ));
        }
        c.close();
        return list;
    }

    // --- BOOKING METHODS ---
    public long addBooking(Booking b) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(C_BOOKING_USER_ID, b.getUserId());
        cv.put(C_BOOKING_VENUE_ID, b.getVenueId());
        cv.put(C_BOOKING_DATE, b.getDate());
        cv.put(C_BOOKING_START_TIME, b.getStartTime());
        cv.put(C_BOOKING_END_TIME, b.getEndTime());
        cv.put(C_BOOKING_PURPOSE, b.getPurpose());
        cv.put(C_BOOKING_STATUS, "Pending"); // Set default status on creation
        return db.insertWithOnConflict(T_BOOKINGS, null, cv, SQLiteDatabase.CONFLICT_FAIL);
    }

    public List<Booking> getBookingsByUser(int userId) {
        List<Booking> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + T_BOOKINGS + " WHERE " + C_BOOKING_USER_ID + " = ? ORDER BY date DESC";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(userId)});
        while (c.moveToNext()) {
            list.add(new Booking(
                    c.getInt(c.getColumnIndexOrThrow(C_ID)),
                    c.getInt(c.getColumnIndexOrThrow(C_BOOKING_USER_ID)),
                    c.getInt(c.getColumnIndexOrThrow(C_BOOKING_VENUE_ID)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_DATE)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_START_TIME)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_END_TIME)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_PURPOSE)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_STATUS))
            ));
        }
        c.close();
        return list;
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + T_BOOKINGS + " ORDER BY date DESC";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            list.add(new Booking(
                    c.getInt(c.getColumnIndexOrThrow(C_ID)),
                    c.getInt(c.getColumnIndexOrThrow(C_BOOKING_USER_ID)),
                    c.getInt(c.getColumnIndexOrThrow(C_BOOKING_VENUE_ID)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_DATE)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_START_TIME)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_END_TIME)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_PURPOSE)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_STATUS))
            ));
        }
        c.close();
        return list;
    }

    public Booking getBookingById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + T_BOOKINGS + " WHERE " + C_ID + " = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            Booking b = new Booking(
                    c.getInt(c.getColumnIndexOrThrow(C_ID)),
                    c.getInt(c.getColumnIndexOrThrow(C_BOOKING_USER_ID)),
                    c.getInt(c.getColumnIndexOrThrow(C_BOOKING_VENUE_ID)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_DATE)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_START_TIME)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_END_TIME)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_PURPOSE)),
                    c.getString(c.getColumnIndexOrThrow(C_BOOKING_STATUS))
            );
            c.close();
            return b;
        }
        c.close();
        return null;
    }

    public boolean updateBookingStatus(int bookingId, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(C_BOOKING_STATUS, status);
        int rowsAffected = db.update(T_BOOKINGS, cv, C_ID + " = ?", new String[]{String.valueOf(bookingId)});
        return rowsAffected > 0;
    }

    // --- ADMIN METHODS ---
    public boolean addAdmin(User admin) {
        admin.setRole("admin");
        return addUser(admin);
    }
}
