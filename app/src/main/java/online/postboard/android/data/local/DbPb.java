package online.postboard.android.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;

import online.postboard.android.data.model.NotificationDTO;

/**
 * Created by Android SD-1 on 19-04-2017.
 */

public class DbPb {


    public DbPb() {
    }

    public abstract static class NotificationTable {
        public static final String TABLE_NAME = "notifications";

        public static final String COLUMN_DATA = "data";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_DATA + " TEXT " +
                        " ); ";

        public static ContentValues toContentValues(NotificationDTO notificationDto) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DATA, new Gson().toJson(notificationDto));
            return values;
        }

        public static NotificationDTO parseCursor(Cursor cursor) {

            return new Gson().fromJson(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)), NotificationDTO.class);
        }
    }
}