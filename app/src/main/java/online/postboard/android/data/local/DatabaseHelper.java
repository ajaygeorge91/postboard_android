package online.postboard.android.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import online.postboard.android.data.model.NotificationDTO;
import timber.log.Timber;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<NotificationDTO> setNotification(NotificationDTO notificationDTO) {
        return Observable.create(emitter -> {
            long result = 0;
            if (emitter.isDisposed()) return;
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                result = mDb.insert(DbPb.NotificationTable.TABLE_NAME,
                        DbPb.NotificationTable.toContentValues(notificationDTO),
                        SQLiteDatabase.CONFLICT_REPLACE);

                transaction.markSuccessful();
            } finally {
                transaction.end();
                if (result >= 0) emitter.onNext(notificationDTO);
                Timber.d("setNotification()" + notificationDTO.getId());
                emitter.onComplete();
            }
        });
    }

    public Observable<List<NotificationDTO>> setNotifications(Collection<NotificationDTO> notificationDTOs) {
        return Observable.create(emitter -> {
            long result = 0;
            List<NotificationDTO> tempList = new ArrayList<>();
            if (emitter.isDisposed()) return;
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.delete(DbPb.NotificationTable.TABLE_NAME, null);
                for (NotificationDTO notificationDTO : notificationDTOs) {
                    result = mDb.insert(DbPb.NotificationTable.TABLE_NAME,
                            DbPb.NotificationTable.toContentValues(notificationDTO),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    if (result >= 0) tempList.add(notificationDTO);
                }
                transaction.markSuccessful();

            } finally {
                transaction.end();
                if (result >= 0) emitter.onNext(tempList);
                Timber.d("setNotifications()" + notificationDTOs.size());
                emitter.onComplete();
            }
        });
    }

    public Observable<List<NotificationDTO>> getNotifications() {
        return mDb.createQuery(DbPb.NotificationTable.TABLE_NAME,
                "SELECT * FROM " + DbPb.NotificationTable.TABLE_NAME)
                .mapToList(new Function<Cursor, NotificationDTO>() {
                    @Override
                    public NotificationDTO apply(@NonNull Cursor cursor) throws Exception {
                        return DbPb.NotificationTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<List<NotificationDTO>> clearNotifications() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) return;
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.delete(DbPb.NotificationTable.TABLE_NAME, null);
                transaction.markSuccessful();
                Timber.d("clearNotifications()");
                emitter.onComplete();
            } finally {
                transaction.end();
            }
        });
    }

}
