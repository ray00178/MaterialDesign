package com.rayzhang.android.materialdesign;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ray on 2017/10/15.
 * CompressActivity
 */

public class CompressActivity extends AppCompatActivity {
    private static final String TAG = CompressActivity.class.getSimpleName();

    private TextView mTextAfterView;
    private ImageView mImgAfterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress);

        TextView mTextBeforeView = (TextView) findViewById(R.id.mTextBeforeView);
        mTextAfterView = (TextView) findViewById(R.id.mTextAfterView);
        ImageView mImgBeforeView = (ImageView) findViewById(R.id.mImgBeforeView);
        mImgAfterView = (ImageView) findViewById(R.id.mImgAfterView);
        Button mMatrixBut = (Button) findViewById(R.id.mMatrixBut);
        Button mRGBBut = (Button) findViewById(R.id.mRGBBut);
        Button mSampleBut = (Button) findViewById(R.id.mSampleBut);

        log("###############################");
        // /05.jpg(橫的)
        final String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/05.jpg";
        final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        // 從硬碟讀取的記憶體大小 = 10664000 (10.16MB) (2000 * 1333)
        log(String.format(Locale.TAIWAN, "Disk ByteCount:%d", bitmap.getByteCount()));
        log(String.format(Locale.TAIWAN, "Disk Width:%d Heigth:%d", bitmap.getWidth(), bitmap.getHeight()));
        mTextBeforeView.setText(String.format(Locale.TAIWAN, "壓縮前：w：%dpx h：%dpx size：%.2f MB",
                bitmap.getWidth(), bitmap.getHeight(), convertMB(bitmap.getByteCount())));
        mImgBeforeView.setImageBitmap(bitmap);

        mMatrixBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapCompressByMatrix(bitmap, 0.45f);
            }
        });
        mRGBBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapCompressByRGB565(filePath);
            }
        });
        mSampleBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapCompressByFitSize(filePath, 1000, 650);
            }
        });
    }

    private void bitmapCompressByMatrix(Bitmap bitmap, float scale) {
        // Use matrix 改變圖片長、寬
        Matrix matrix = new Matrix();
        // 縮小的比例
        matrix.setScale(scale, scale);
        Bitmap after = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        log("###############################");
        log(String.format(Locale.TAIWAN, "Matrix ByteCount:%d", after.getByteCount()));
        log(String.format(Locale.TAIWAN, "Matrix Width:%d Heigth:%d", after.getWidth(), after.getHeight()));
        mTextAfterView.setText(String.format(Locale.TAIWAN, "Matrix 壓縮後：w：%dpx h：%dpx size：%.2f MB",
                after.getWidth(), after.getHeight(), convertMB(after.getByteCount())));
        mImgAfterView.setImageBitmap(after);
    }

    private void bitmapCompressByRGB565(String filePath) {
        // Use RGB_565 改變圖片質量
        // 在andorid中系統是預設使用ARGB_8888(Each pixel is stored on 4 bytes，每個畫素採用4bytes)
        // 而RGB_565(Each pixel is stored on 2 bytes，每個畫素採用2bytes)，所以2者大小相差2倍
        // 如果有透明的需求，則該方式是不適合使用。
        // 當然也還有其他質量的選擇，但因畫質太糟故不建議使用。
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap after = BitmapFactory.decodeFile(filePath, options);
        log("###############################");
        log(String.format(Locale.TAIWAN, "RGB565 ByteCount:%d", after.getByteCount()));
        log(String.format(Locale.TAIWAN, "RGB565 Width:%d Heigth:%d", after.getWidth(), after.getHeight()));
        mTextAfterView.setText(String.format(Locale.TAIWAN, "RGB_565 壓縮後：w：%dpx h：%dpx size：%.2f MB",
                after.getWidth(), after.getHeight(), convertMB(after.getByteCount())));
        mImgAfterView.setImageBitmap(after);
    }

    private void bitmapCompressByFitSize(String filePath, float reqWidth, float reqHeight) {
        // Use inSampleSize 改變圖片取樣率
        // 該方式也是大多數人所知道的方式
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateFitSize((int) reqWidth, (int) reqHeight, options);
        options.inJustDecodeBounds = false;
        Bitmap after = BitmapFactory.decodeFile(filePath, options);
        log("###############################");
        log(String.format(Locale.TAIWAN, "inSampleSize ByteCount:%d", after.getByteCount()));
        log(String.format(Locale.TAIWAN, "inSampleSize Width:%d Heigth:%d", after.getWidth(), after.getHeight()));
        mTextAfterView.setText(String.format(Locale.TAIWAN, "inSampleSize 壓縮後：w：%dpx h：%dpx \nsize：%.2f MB",
                after.getWidth(), after.getHeight(), convertMB(after.getByteCount())));
        mImgAfterView.setImageBitmap(after);
    }

    private int calculateFitSize(int reqWidth, int reqHeight, BitmapFactory.Options options) {
        // 原始圖片的寬高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // 在保證解析出的bitmap寬高分別大於目標尺寸寬高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void saveImage(Bitmap bitmap) {
        String imgName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.TAIWAN).format(new Date());
        File filesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File imageFile = new File(filesDir, imgName + ".jpg");

        OutputStream os = null;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            scanPhoto(imageFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error save bitmap：", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void scanPhoto(String imgFilePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFilePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getApplicationContext().sendBroadcast(mediaScanIntent);
    }

    private float convertMB(int size) {
        return (float) size / (1024f * 1024f);
    }

    private void log(String str) {
        Log.d(TAG, str);
    }
}