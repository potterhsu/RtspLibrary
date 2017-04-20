# RtspLibrary
Android library based on FFMpeg for playing given RTSP endpoint.

## Pipeline
1. Open RTSP Endpoint
2. Find Video Stream
3. Read Frame
4. Convert to RGB
5. Invoke Callback
6. Java Interface

## Setup
1.  In root `build.gradle`:
  ```
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
  ````

2.  In target module `build.gradle`
  ```
  dependencies {
    compile 'com.github.potterhsu:RtspLibrary:v1.0'
  }
  ```

## Usage

1. Initialize
  ```java
  ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
  RtspClient rtspClient = new RtspClient(new NativeCallback() {
      @Override
      public void onFrame(final byte[] frame, final int nChannel, final int width, final int height) {
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
  ```

2. Play
  ```java
  new Thread(new Runnable() {
      @Override
      public void run() {
          rtspClient.play("rtsp://endpoint/to/rtsp");
      }
  }).start();
  ```

3. Stop and release
  ```java
  rtspClient.stop();
  rtspClient.dispose();
  ```

## Demo
Clone the repository and run.
