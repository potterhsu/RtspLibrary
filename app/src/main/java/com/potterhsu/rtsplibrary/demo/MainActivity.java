package com.potterhsu.rtsplibrary.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.potterhsu.rtsplibrary.NativeCallback;
import com.potterhsu.rtsplibrary.RtspClient;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ImageView ivPreview;
    private EditText edtEndpoint;

    private RtspClient rtspClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPreview = (ImageView) findViewById(R.id.ivPreview);
        edtEndpoint = (EditText) findViewById(R.id.edtEndpoint);

        rtspClient = new RtspClient(new NativeCallback() {
            @Override
            public void onFrame(final byte[] frame, final int nChannel, final int width, final int height) {
//                Log.d(TAG, String.format("onFrame: nChannel = %d, width = %d, height = %d", nChannel, width, height));

                ivPreview.post(new Runnable() {
                    @Override
                    public void run() {
                        int area = width * height;
                        int pixels[] = new int[area];
                        for (int i = 0; i < area; i++) {
                            int r = frame[3 * i];
                            int g = frame[3 * i + 1];
                            int b = frame[3 * i + 2];
                            if (r < 0) r += 255;
                            if (g < 0) g += 255;
                            if (b < 0) b += 255;
                            pixels[i] = Color.rgb(r, g, b);
                        }
                        Bitmap bmp = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
                        ivPreview.setImageBitmap(bmp);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        rtspClient.stop();
        rtspClient.dispose();
        super.onDestroy();
    }

    public void onBtnPlayClick(View view) {
        final String endpoint = edtEndpoint.getText().toString();
        if (endpoint.isEmpty()) {
            Toast.makeText(this, "Endpoint is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                rtspClient.play(endpoint);
            }
        }).start();
    }

    public void onBtnStopClick(View view) {
        rtspClient.stop();
    }
}
