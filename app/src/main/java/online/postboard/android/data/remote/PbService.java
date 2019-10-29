package online.postboard.android.data.remote;

import android.app.Application;

import online.postboard.android.data.local.PreferencesHelper;
import online.postboard.android.data.model.AddCommentDTO;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.data.model.CommentDTO;
import online.postboard.android.data.model.PaginatedResult;
import online.postboard.android.data.model.ResponseDTO;
import online.postboard.android.data.model.SocialAuthDTO;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.data.model.UserActivityDTO;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserCommentActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.data.model.UserProfileBundleDTO;
import online.postboard.android.ui.signin.SignInActivity;
import online.postboard.android.R;
import online.postboard.android.data.model.AddSubCommentDTO;
import online.postboard.android.data.model.AuthDTO;
import online.postboard.android.data.model.NotificationDTO;
import online.postboard.android.util.Constants;
import online.postboard.android.util.ThrowableUtil;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PbService {

    String ENDPOINT = Constants.INSTANCE.getAPI_ENDPOINT();
    String X_AUTH_TOKEN = "X-Auth-Token";

    @GET("api/user")
    Observable<ResponseDTO<UserDTO>> getUser();

    @GET("api/user/opt")
    Observable<ResponseDTO<UserDTO>> getUserOpt();

    @GET("api/user/posts")
    Observable<ResponseDTO<PaginatedResult<ArticleDTO>>> getMyArticles(@Query("pageNumber") int pageNumber);

    @GET("api/user/{userID}/posts")
    Observable<ResponseDTO<PaginatedResult<ArticleDTO>>> getUserArticles(@Path("userID") String userID, @Query("pageNumber") int pageNumber);

    @GET("api/posts/hot")
    Observable<ResponseDTO<PaginatedResult<ArticleDTO>>> getHotArticles(@Query("pageNumber") int pageNumber);

    @GET("api/posts/new")
    Observable<ResponseDTO<PaginatedResult<ArticleDTO>>> getNewArticles(@Query("pageNumber") int pageNumber);

    @GET("api/posts/q/{query}")
    Observable<ResponseDTO<List<ArticleDTO>>> getArticlesFromSearchQuery(@Path("query") String articleId, @Query("pageNumber") int pageNumber);

    @GET("api/posts/{articleId}/comments/hot")
    Observable<ResponseDTO<PaginatedResult<CommentDTO>>> getHotComments(@Path("articleId") String articleId, @Query("pageNumber") int pageNumber);

    @GET("api/posts/{articleId}/comments/new")
    Observable<ResponseDTO<PaginatedResult<CommentDTO>>> getNewComments(@Path("articleId") String articleId, @Query("pageNumber") int pageNumber);

    @GET("api/posts/hot/reaction/{page}")
    Observable<ResponseDTO<List<UserArticleActivity>>> getHotArticleUserActivity(@Path("page") int page);

    @GET("api/posts/new/reaction/{page}")
    Observable<ResponseDTO<List<UserArticleActivity>>> getNewArticleUserActivity(@Path("page") int page);

    @GET("api/posts/{articleId}/comments/{commentId}/subComments/hot?numberOfRecords=10")
    Observable<ResponseDTO<PaginatedResult<SubCommentDTO>>> getHotSubComments(@Path("articleId") String articleId, @Path("commentId") String commentId, @Query("pageNumber") int pageNumber);

    @POST("api/posts/{articleId}/comments/{commentId}/subComments")
    Observable<ResponseDTO<SubCommentDTO>> addSubComment(@Path("articleId") String articleId, @Path("commentId") String commentId, @Body() AddSubCommentDTO addSubCommentDTO);

    @Multipart
    @POST("api/posts")
    Observable<ResponseDTO<ArticleDTO>> addArticle(@Part("title") RequestBody title, @Part MultipartBody.Part image);

    @POST("api/posts/{articleId}/comments")
    Observable<ResponseDTO<CommentDTO>> addComment(@Path("articleId") String articleId, @Body() AddCommentDTO addCommentDTO);

    @POST("api/posts/{articleId}/reaction")
    Observable<ResponseDTO<UserArticleActivity>> addArticleReaction(@Path("articleId") String articleId, @Body() UserArticleActivity userArticleActivity);

    @POST("api/posts/{articleId}/comments/{commentId}/reaction")
    Observable<ResponseDTO<UserCommentActivity>> addCommentReaction(@Path("articleId") String articleId, @Path("commentId") String commentId, @Body() UserCommentActivity userCommentActivity);

    @GET("api/user/activity")
    Observable<ResponseDTO<PaginatedResult<UserActivityDTO>>> getMyActivities(@Query("pageNumber") int pageNumber);

    @GET("api/user/{userID}/profile")
    Observable<ResponseDTO<UserProfileBundleDTO>> getUserProfileBundle(@Path("userID") String userID);

    @GET("api/user/notifications/clickaction")
    Observable<ResponseDTO<String>> readNotifications();

    @GET("api/user/notifications")
    Observable<ResponseDTO<List<NotificationDTO>>> getMyNotifications(@Query("pageNumber") int pageNumber);

    @POST("api/authenticate/credentials")
    Observable<ResponseDTO<AuthResponseDTO>> login(@Body() AuthDTO authDTO);

    @POST("api/authenticate/google")
    Observable<ResponseDTO<AuthResponseDTO>> googleLogin(@Body() SocialAuthDTO socialAuthDTO);

    @POST("api/authenticate/facebook")
    Observable<ResponseDTO<AuthResponseDTO>> facebookLogin(@Body() SocialAuthDTO socialAuthDTO);

    @POST("api/signUp")
    Observable<ResponseDTO<AuthResponseDTO>> register(@Body() AuthDTO authDTO);

    @GET("api/node/{nodeID}")
    Observable<ResponseDTO<ArticleDTO>> getByNodeID(@Path("nodeID") String nodeID);

    class Creator {

        public static PbService newArticleService(final Application mApplication) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(chain -> onOnIntercept(chain, mApplication)).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(PbService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(PbService.class);
        }


        private static Response onOnIntercept(Interceptor.Chain chain, Application mApplication) throws ThrowableUtil.CustomIOException {
            okhttp3.Response response;
            Request request = chain.request().newBuilder()
                    .addHeader(X_AUTH_TOKEN, new PreferencesHelper(mApplication).getToken())
                    .build();
            try {
                response = chain.proceed(request);
                if (response.code() == 401) {
                    // do stuff
                    mApplication.startActivity(SignInActivity.getCleanStartIntent(mApplication));
                }
            } catch (SocketTimeoutException exception) {
                throw new ThrowableUtil.CustomIOException(mApplication.getString(R.string.error_timeout_content), exception);
            } catch (UnknownHostException exception) {
                throw new ThrowableUtil.CustomIOException(mApplication.getString(R.string.error_network_connect), exception);
            } catch (Exception exception) {
                throw new ThrowableUtil.CustomIOException(mApplication.getString(R.string.error_loading_content), exception);
            }
            return response;
        }

    }
}
