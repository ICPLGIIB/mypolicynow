package com.wildma.idcardcamera.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wildma.idcardcamera.R;
import com.wildma.idcardcamera.cropper.CropImageView;
import com.wildma.idcardcamera.cropper.CropListener;
import com.wildma.idcardcamera.global.Constant;
import com.wildma.idcardcamera.utils.FileUtils;
import com.wildma.idcardcamera.utils.ImageUtils;
import com.wildma.idcardcamera.utils.PermissionUtils;
import com.wildma.idcardcamera.utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Author       wildma
 * Github       https://github.com/wildma
 * Date         2018/6/24
 * Desc	        ${拍照界面}
 */
public class CameraActivity extends Activity implements View.OnClickListener {

    public final static  int     TYPE_BACK_CAMERA     = 1;
    public final static  int     TYPE_FRONT_CAMERA      = 2;
    public final static  int     REQUEST_CODE          = 0X11;
    public final static  int     RESULT_CODE           = 0X12;
    private final        int     PERMISSION_CODE_FIRST = 0x13;
    private final static String  TAKE_TYPE             = "take_type";
    private final static String  IMAGE_PATH            = "image_path";
    private              int     mType;
    private              boolean isToast               = true;

    private CropImageView mCropImageView;
    private Bitmap        mCropBitmap;
    private CameraPreview mCameraPreview;
    private View          mLlCameraCropContainer;
    private ImageView     mIvCameraCrop;
    private ImageView     mIvCameraFlash;
    private View          mLlCameraOption;
    private View          mLlCameraResult;
    private TextView      mViewCameraCropBottom,tv_camera_review_text;
    private FrameLayout   mFlCameraOption;
    private View          mViewCameraCropLeft;

    /**
     *
     *
     * @param activity
     * @param type
     */
    public static void toCameraActivity(Activity activity, int type) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(TAKE_TYPE, type);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     *
     *
     * @param data
     * @return
     */
    public static String getImagePath(Intent data) {
        if (data != null) {
            return data.getStringExtra(IMAGE_PATH);
        }
        return "";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, PERMISSION_CODE_FIRST,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        if (checkPermissionFirst) {
            init();
        }
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPermissions = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                isPermissions = false;
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    if (isToast) {
                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                        isToast = false;
                    }
                }
            }
        }
        isToast = true;
        if (isPermissions) {
            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "Accepted");
            init();
        } else {
            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "Denied");
            finish();
        }
    }

    private void init() {
        setContentView(R.layout.activity_camera);
        mType = getIntent().getIntExtra(TAKE_TYPE, 0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initView();
        initListener();
    }

    private void initView() {
        mCameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
        mLlCameraCropContainer = findViewById(R.id.ll_camera_crop_container);
        mIvCameraCrop = (ImageView) findViewById(R.id.iv_camera_crop);
        mIvCameraFlash = (ImageView) findViewById(R.id.iv_camera_flash);
        mLlCameraOption = findViewById(R.id.ll_camera_option);
        mLlCameraResult = findViewById(R.id.ll_camera_result);
        mCropImageView = findViewById(R.id.crop_image_view);
        mViewCameraCropBottom = (TextView) findViewById(R.id.view_camera_crop_bottom);
        tv_camera_review_text = (TextView) findViewById(R.id.tv_camera_review_text);
        mFlCameraOption = (FrameLayout) findViewById(R.id.fl_camera_option);
        mViewCameraCropLeft = findViewById(R.id.view_camera_crop_left);

        float screenMinSize = Math.min(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
        float screenMaxSize = Math.max(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
        float height = (int) (screenMinSize * 0.90);
        float width = (int) (height * 75.0f / 47.0f);
        float flCameraOptionWidth = (screenMaxSize - width) / 2;
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
        LinearLayout.LayoutParams cameraOptionParams = new LinearLayout.LayoutParams((int) flCameraOptionWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        mLlCameraCropContainer.setLayoutParams(containerParams);

        mIvCameraCrop.setLayoutParams(cropParams);
        mFlCameraOption.setLayoutParams(cameraOptionParams);

        switch (mType) {
            case TYPE_BACK_CAMERA:
                //mIvCameraCrop.setImageResource(R.mipmap.camera_idcard_front);
                mIvCameraCrop.setBackground(getDrawable(R.drawable.rectangle));
                break;
            case TYPE_FRONT_CAMERA:
                //mIvCameraCrop.setImageResource(R.mipmap.camera_idcard_back);
                mIvCameraCrop.setBackground(getDrawable(R.drawable.rectangle));
                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCameraPreview.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 500);
    }

    private void initListener() {
        mCameraPreview.setOnClickListener(this);
        mIvCameraFlash.setOnClickListener(this);
        findViewById(R.id.iv_camera_close).setOnClickListener(this);
        findViewById(R.id.iv_camera_take).setOnClickListener(this);
        findViewById(R.id.iv_camera_result_ok).setOnClickListener(this);
        findViewById(R.id.iv_camera_result_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.camera_preview) {
            mCameraPreview.focus();
        } else if (id == R.id.iv_camera_close) {
            finish();
        } else if (id == R.id.iv_camera_take) {
            takePhoto();
        } else if (id == R.id.iv_camera_flash) {
            boolean isFlashOn = mCameraPreview.switchFlashLight();
            mIvCameraFlash.setImageResource(isFlashOn ? R.mipmap.camera_flash_on : R.mipmap.camera_flash_off);
        } else if (id == R.id.iv_camera_result_ok) {
            confirm();
        } else if (id == R.id.iv_camera_result_cancel) {
            mCameraPreview.setEnabled(true);
            mCameraPreview.addCallback();
            mCameraPreview.startPreview();
            mIvCameraFlash.setImageResource(R.mipmap.camera_flash_off);
            setTakePhotoLayout();
        }
    }

    /**
     *
     */
    private void takePhoto() {
        mCameraPreview.setEnabled(false);

        CameraUtils.getCamera().setOneShotPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(final byte[] bytes, Camera camera) {
                final Camera.Size size = camera.getParameters().getPreviewSize(); //
                camera.stopPreview();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final int w = size.width;
                        final int h = size.height;
                        Bitmap bitmap = ImageUtils.getBitmapFromByte(bytes, w, h);
                        cropImage(bitmap);
                    }
                }).start();
            }
        });
    }

    /**
     *
     */
    private void cropImage(Bitmap bitmap) {
        /**/
        float left = mViewCameraCropLeft.getWidth();
        float top = mIvCameraCrop.getTop();
        float right = mIvCameraCrop.getRight() + left;
        float bottom = mIvCameraCrop.getBottom();

        /**/
        float leftProportion = left / mCameraPreview.getWidth();
        float topProportion = top / mCameraPreview.getHeight();
        float rightProportion = right / mCameraPreview.getWidth();
        float bottomProportion = bottom / mCameraPreview.getBottom();

        /**/
        mCropBitmap = Bitmap.createBitmap(bitmap,
                (int) (leftProportion * (float) bitmap.getWidth()),
                (int) (topProportion * (float) bitmap.getHeight()),
                (int) ((rightProportion - leftProportion) * (float) bitmap.getWidth()),
                (int) ((bottomProportion - topProportion) * (float) bitmap.getHeight()));

        /**/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //
                mCropImageView.setLayoutParams(new LinearLayout.LayoutParams(mIvCameraCrop.getWidth(), mIvCameraCrop.getHeight()));
                setCropLayout();
                mCropImageView.setImageBitmap(mCropBitmap);
            }
        });
    }

    /**
     *
     */
    private void setCropLayout() {
        mIvCameraCrop.setVisibility(View.GONE);
        tv_camera_review_text.setVisibility(View.VISIBLE);

        mCameraPreview.setVisibility(View.GONE);
        mLlCameraOption.setVisibility(View.GONE);
        mCropImageView.setVisibility(View.VISIBLE);
        mLlCameraResult.setVisibility(View.VISIBLE);
        mViewCameraCropBottom.setText("");
        mViewCameraCropBottom.setVisibility(View.GONE);

    }

    /**
     *
     */
    private void setTakePhotoLayout() {
        mIvCameraCrop.setVisibility(View.VISIBLE);
        tv_camera_review_text.setVisibility(View.GONE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mLlCameraOption.setVisibility(View.VISIBLE);
        mCropImageView.setVisibility(View.GONE);
        mLlCameraResult.setVisibility(View.GONE);
        mViewCameraCropBottom.setVisibility(View.VISIBLE);
        mViewCameraCropBottom.setText(getString(R.string.touch_to_focus));

        mCameraPreview.focus();
    }

    /**
     */
    private void confirm() {
        mCropImageView.crop(new CropListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
                if (bitmap == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.crop_fail), Toast.LENGTH_SHORT).show();
                    finish();
                }

                // dest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

                Bitmap dest = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas cs = new Canvas(dest);
                Paint tPaint = new Paint();
                tPaint.setTextSize(35);
                tPaint.setColor(Color.RED);
                tPaint.setStyle(Paint.Style.FILL);
                cs.drawBitmap(dest, 0f, 0f, null);
                float height = tPaint.measureText("yY");
                cs.drawText(dateTime, 20f, height+15f, tPaint);



                if (FileUtils.createOrExistsDir(Constant.DIR_ROOT)) {
                    StringBuffer buffer = new StringBuffer();
                    String imagePath = "";
                    if (mType == TYPE_BACK_CAMERA) {
                        imagePath = buffer.append(Constant.DIR_ROOT).append(Constant.APP_NAME).append(".").append("idCardFrontCrop.jpg").toString();
                    } else if (mType == TYPE_FRONT_CAMERA) {
                        imagePath = buffer.append(Constant.DIR_ROOT).append(Constant.APP_NAME).append(".").append("idCardBackCrop.jpg").toString();
                    }

                    if (ImageUtils.save(dest, imagePath, Bitmap.CompressFormat.JPEG)) {
                        Intent intent = new Intent();
                        intent.putExtra(CameraActivity.IMAGE_PATH, imagePath);
                        setResult(RESULT_CODE, intent);
                        finish();
                    }
                }
            }
        }, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCameraPreview != null) {
            mCameraPreview.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraPreview != null) {
            mCameraPreview.onStop();
        }
    }
}