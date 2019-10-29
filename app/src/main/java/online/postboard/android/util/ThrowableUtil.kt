package online.postboard.android.util

import android.content.Context
import online.postboard.android.R
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by ajayg on 7/6/2017.
 */

class ThrowableUtil {

    class ThrowableSub : Consumer<Throwable> {

        @Throws(Exception::class)
        override fun accept(@NonNull throwable: Throwable) {

        }
    }

    class CustomIOException(message: String?, cause: Throwable?) : IOException(message, cause) {
        init {
            Timber.e(cause)
        }
    }

}
