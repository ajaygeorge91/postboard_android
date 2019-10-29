package online.postboard.android.util

import android.text.format.DateUtils
import org.joda.time.DateTime
import java.text.ParseException
import java.util.*

/**
 * Created by Android SD-1 on 21-06-2017.
 */

object StringUtils {

    val HTTP_PROTOCOL = "http://"
    val HTTPS_PROTOCOL = "https://"

    fun getDisplayableTime(utcTime: String): String {
        val date: Date
        try {
            date = DateTime(utcTime).toDate()
            return DateUtils.getRelativeTimeSpanString(date.time).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            return utcTime
        }

    }


    /**
     * Returns the cannoncial url
     */
    fun getCanonicalPage(origUrl: String): String {

        val url: String = if (origUrl.startsWith(HTTP_PROTOCOL))
            origUrl.substring(HTTP_PROTOCOL.length)
        else if (origUrl.startsWith(HTTPS_PROTOCOL))
            origUrl.substring(HTTPS_PROTOCOL.length)
        else
            ""
        val res = url.split("/").firstOrNull()

        return if (res == null) {
            origUrl
        } else {
            res
        }
    }

    fun getFavicon(origUrl: String): String {

        val canUrl = getCanonicalPage(origUrl)

        val protocol: String = if (origUrl.startsWith(HTTP_PROTOCOL))
            origUrl.substring(0, HTTP_PROTOCOL.length)
        else if (origUrl.startsWith(HTTPS_PROTOCOL))
            origUrl.substring(0, HTTPS_PROTOCOL.length)
        else
            ""

        return protocol + canUrl + "/favicon.ico"
    }


}
