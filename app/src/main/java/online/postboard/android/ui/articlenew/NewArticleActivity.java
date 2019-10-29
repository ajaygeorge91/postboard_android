package online.postboard.android.ui.articlenew;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.BaseActivity;
import online.postboard.android.ui.detail.ArticleDetailActivity;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.R;
import online.postboard.android.util.DialogFactory;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NewArticleActivity extends BaseActivity implements DetailMvpView {

    @BindView(R.id.image_article)
    ImageView image_article;

    @BindView(R.id.text_title)
    EditText text_title;

    @BindView(R.id.text_content)
    EditText text_content;

    @BindView(R.id.button_add_article)
    Button button_add_article;

    @BindView(R.id.button_add_image_cam_layout)
    LinearLayout button_add_image_cam_layout;

    @BindView(R.id.button_add_image_gallery_layout)
    LinearLayout button_add_image_gallery_layout;

    @Inject
    NewActivityPresenter newActivityPresenter;

    private static final int SELECT_PHOTO = 100;
    private static final int TAKE_PHOTO = 101;

    private Uri cameraPictureUrl;

    public static void startNewActivity(Activity activity) {
        Intent intent = new Intent(activity, NewArticleActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        newActivityPresenter.attachView(this);
        setContentView(R.layout.activity_new_article);
        ButterKnife.bind(this);

        button_add_image_cam_layout.setOnClickListener(view -> pickImageFromSource(Sources.CAMERA));
        button_add_image_gallery_layout.setOnClickListener(view -> pickImageFromSource(Sources.GALLERY));

        newActivityPresenter.setUserDetails();
        button_add_article.setEnabled(true);
    }

    private void pickImageFromSource(Sources source) {

        int chooseCode = 0;
        Intent pictureChooseIntent = null;

        switch (source) {
            case CAMERA:
                if (!checkPermission(Manifest.permission.CAMERA)) {
                    return;
                }
                cameraPictureUrl = createImageUri();
                pictureChooseIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl);
                chooseCode = TAKE_PHOTO;
                break;
            case GALLERY:
                if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    pictureChooseIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    pictureChooseIntent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pictureChooseIntent.setType("image/*");
                chooseCode = SELECT_PHOTO;
                break;
        }

        startActivityForResult(pictureChooseIntent, chooseCode);
    }

    private boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(i);
                Toast.makeText(this, "Enable " + permission, Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission, Manifest.permission.CAMERA}, 0);
            }
            return false;
        } else {
            return true;
        }
    }

    private Uri createImageUri() {
        ContentResolver contentResolver = getContentResolver();
        ContentValues cv = new ContentValues();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        cv.put(MediaStore.Images.Media.TITLE, timeStamp);
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
    }

    private void onImagePicked(Object result) {
        if (result instanceof Bitmap) {
            image_article.setImageBitmap((Bitmap) result);
        } else {
            Glide.with(this)
                    .load(result) // works for File or Uri
                    .crossFade()
                    .into(image_article);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PHOTO:
                    onImagePicked(data.getData());
                    break;
                case TAKE_PHOTO:
                    onImagePicked(cameraPictureUrl);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newActivityPresenter.detachView();
    }

    @Override
    public void articleCreated(ArticleDTO articleDTO) {
        ArticleDetailActivity.startArticleDetailActivity(this, articleDTO, true);
        finish();
    }

    @Override
    public void showError(String message) {
        button_add_article.setEnabled(true);
        DialogFactory.INSTANCE.createGenericErrorDialog(this, message).show();
    }

    @Override
    public void setUser(UserDTO userDTO) {
        String avatarUrl = ProfileUtils.getUserAvatarUrl(userDTO);

        if (userDTO == null) {

        }
    }

    @OnClick({R.id.back_button_layout, R.id.back_button_custom})
    public void backButtonClick(View view) {
        onBackPressed();
    }

    @OnClick(R.id.button_add_article)
    public void createArticleButtonClick(View view) {
        if (text_title.getText().toString().isEmpty()) {
            Toast.makeText(this, "Add title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (image_article.getDrawable() == null) {
            Toast.makeText(this, "Add Image", Toast.LENGTH_SHORT).show();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((GlideBitmapDrawable) image_article.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteArray);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image_name", requestFile);

        String titleString = text_title.getText().toString();
        String contentString = text_content.getText().toString();
        RequestBody title =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, titleString);
        RequestBody content =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, contentString);

        button_add_article.setEnabled(false);
        newActivityPresenter.addArticle(title, content, body);
    }

    public enum Sources {
        CAMERA, GALLERY
    }

}
