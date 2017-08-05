package com.rayzhang.android.materialdesign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Ray on 2017/7/9.
 */

public class AsyncTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = AsyncTaskActivity.class.getSimpleName();
    private ImageView mImgView, imageView_Two;
    private TextView mText_One;

    private ArrayList<DownloadAsyncTask> taskList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);

        mImgView = (ImageView) findViewById(R.id.mImgView);
        imageView_Two = (ImageView) findViewById(R.id.imageView_Two);
        Button mDownloadBut = (Button) findViewById(R.id.mDownloadBut);
        Button mCancelBut = (Button) findViewById(R.id.mCancelBut);
        mText_One = (TextView) findViewById(R.id.mText_One);
        mDownloadBut.setOnClickListener(this);
        mCancelBut.setOnClickListener(this);

        taskList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mDownloadBut:
                /**
                 *  AsyncTask.THREAD_POOL_EXECUTOR : 允許多個任務，在同個執行緒池，不保證順序性
                 *  AsyncTask.SERIAL_EXECUTOR : 執行單一任務，順序執行的
                 *  execute() : default is AsyncTask.SERIAL_EXECUTOR
                 */
                //new DownloadAsyncTask().execute("https://i.ytimg.com/vi/dmRgNR3WbHc/maxresdefault.jpg",
                //        "https://cdn.hk01.com/media/images/582792/xlarge/e152f34b18d92f4595f84b7ee016ccab.jpg");
                DownloadAsyncTask task_1 = new DownloadAsyncTask(0);
                DownloadAsyncTask task_2 = new DownloadAsyncTask(1);
                // 剛初始化完成，尚未執行任務
                Log.d(TAG, "TASK init:" + task_1.getStatus());

                // 順序任務
                //task_1.execute("https://i.ytimg.com/vi/dmRgNR3WbHc/maxresdefault.jpg");
                //task_2.execute("https://cdn.hk01.com/media/images/582792/xlarge/e152f34b18d92f4595f84b7ee016ccab.jpg");
                // 並行任務
                task_1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        "https://i.ytimg.com/vi/dmRgNR3WbHc/maxresdefault.jpg");
                task_2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        "https://cdn.hk01.com/media/images/582792/xlarge/e152f34b18d92f4595f84b7ee016ccab.jpg");
                taskList.add(task_1);
                taskList.add(task_2);
                break;
            case R.id.mCancelBut:
                // cancel : 調用 doInBackground() → onCancelled() → onCancelled(T t)
                // true : 會立即取消任務
                // false : 不會立即取消任務
                if (taskList.size() > 0) {
                    for (DownloadAsyncTask task : taskList) {
                        task.cancel(false);
                    }
                    taskList.clear();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (taskList.size() > 0) {
            for (DownloadAsyncTask task : taskList) {
                task.cancel(true);
            }
            taskList.clear();
        }
        super.onDestroy();
    }

    /**
     * 使用異步任務的方式
     * 1-1.聲明一個class並繼承AsyncTask 並聲明三個參數類型(若沒有要聲明參數，則要以Void表示)
     * 1-2.Not all types are always used by an asynchronous task. To mark a type as unused, simply use the type Void
     * 2-1.第1個參數表示要執行的任務，通常都是網址
     * 2-2.第2個參數表示任務的黨前進度
     * 2-3.第3個參數表示當任務完成後，要返回的型別
     */
    private class DownloadAsyncTask extends AsyncTask<String, Integer, Bitmap[]> {
        private int tag = 0;

        public DownloadAsyncTask(int tag) {
            this.tag = tag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute status:" + getStatus());
        }

        @Override
        protected Bitmap[] doInBackground(String... params) {
            Log.d(TAG, "doInBackground status:" + getStatus());
            // 先休眠3秒，在執行
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 判斷是否取消任務
            if (getStatus() == Status.RUNNING && isCancelled()) {
                Log.d(TAG, "doInBackground isCancelled :" + isCancelled());
                return null;
            }
            Bitmap[] bitmaps = new Bitmap[params.length];
            InputStream is = null;
            ByteArrayOutputStream os = null;
            try {
                for (int i = 0, j = bitmaps.length; i < j; i++) {
                    URL url = new URL(params[i]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setReadTimeout(15000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("GET");

                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        is = httpURLConnection.getInputStream();
                        os = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        long totalLen = httpURLConnection.getContentLength();
                        long nowLen = 0;
                        while ((len = is.read(buffer)) != -1) {
                            nowLen += len;
                            int value = (int) ((nowLen / (float) totalLen) * 100);
                            // 呼叫publishProgress -->去自動更新目前的進度
                            publishProgress(value);
                            os.write(buffer, 0, len);
                        }
                        bitmaps[i] = BitmapFactory.decodeByteArray(os.toByteArray(), 0, os.toByteArray().length);
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception:" + e.toString());
            } finally {
                try {
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmaps;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, String.format(Locale.TAIWAN, "onProgressUpdate value:%d", values[0]));
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            super.onPostExecute(bitmaps);
            // 待任務都完成後，才會調用此方法
            mText_One.setText("onPostExecute bitmap:" + bitmaps.length + " status:" + getStatus() + " isCanceled:" + isCancelled());
            if (tag == 0) {
                mImgView.setImageBitmap(bitmaps[0]);
            } else {
                imageView_Two.setImageBitmap(bitmaps[0]);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "onCancelled status:" + getStatus() + " isCanceled:" + isCancelled());
        }

        @Override
        protected void onCancelled(Bitmap[] bitmaps) {
            super.onCancelled(bitmaps);

            mText_One.setText("cancelled bitmaps:" + bitmaps + " status:" + getStatus() + " isCanceled:" + isCancelled());
            if (bitmaps != null) {
                mImgView.setImageBitmap(bitmaps[0]);
                imageView_Two.setImageBitmap(bitmaps[1]);
            }
        }
    }
}
