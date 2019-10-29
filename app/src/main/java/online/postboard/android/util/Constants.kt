package online.postboard.android.util

/**
 * Created by Android SD-1 on 16-03-2017.
 */
object Constants {

    val AVATAR_URL_ANONYMOUS = "https://s3.amazonaws.com/postboard.online.images/user_image_placeholder.png"

    val API_ENDPOINT = "https://postboard.online/"

    val MQTT_ENDPOINT = "tcp://postboard.online:1883"

    val ARTICLE_TYPE_POST = "post"
    val ARTICLE_LABEL = "Article"
    val COMMENT_LABEL = "Comment"

    val HAS_REACTION = "HAS_REACTION"
    val HAS_ARTICLE = "HAS_ARTICLE"

    val MAX_LENGTH_CONTENT_TEXT = 360

    fun getArticleLink(articleId: String): String {
        return API_ENDPOINT + "posts/" + articleId
    }

}
