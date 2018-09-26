package com.fos.sample.ali;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechRecognizer;
import com.alibaba.idst.util.SpeechRecognizerCallback;
import com.fos.sample.R;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

public class SpeechRecognizerActivity extends AppCompatActivity {
    private static final String TAG="AliSpeechDemo";

    private Button button;
    private EditText mFullEdit;
    private EditText mResultEdit;
    // Demo录音线程
    private MyRecorder myRecorder;
    private NlsClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognizer);
        button = (Button) findViewById(R.id.button);
        mFullEdit = (EditText) findViewById(R.id.editText2);
        mResultEdit = (EditText) findViewById(R.id.editText);

        //第一步，创建client实例，client只需要创建一次，可以用它多次创建recognizer
        client = new NlsClient();
        myRecorder = new MyRecorder();
    }

    @Override
    public void onDestroy(){
        // 等待录音线程完全停止
        if (myRecorder.getStatus() != MyRecorder.STATUS_FINISHED){
            myRecorder.stop();
            while(myRecorder.getStatus() != MyRecorder.STATUS_FINISHED) {
                try {
                    Thread.sleep(50);
                    Log.d(TAG, "waiting until finished...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // 最终，退出时释放client
        client.release();
        super.onDestroy();
    }

    // 此方法内启动录音和识别逻辑，为了代码简单便于理解，没有加防止用户重复点击的逻辑，用户应该在真实使用场景中注意
    public void startRecognizer(View view){
        button.setText("录音中");
        button.setEnabled(false);
        mFullEdit.setText("");
        mResultEdit.setText("");

        //UI在主线程更新
        Handler handler = new MyHandler(this);
        // 第二步，新建识别回调类
        SpeechRecognizerCallback callback = new MyCallback(handler);

        // 第三步，创建识别request
        SpeechRecognizer speechRecognizer = client.createRecognizerRequest(callback);

        // 第四步，设置相关参数
        // 请使用https://help.aliyun.com/document_detail/72153.html 动态生成token
        speechRecognizer.setToken(ALiMainActivity.token);
        // 请使用阿里云语音服务管控台(https://nls-portal.console.aliyun.com/)生成您的appkey OGVxXTtIAoVjsO0TZqLwUe9SIfOdWh
        speechRecognizer.setAppkey(ALiMainActivity.ks);
        // 设置返回中间结果，更多参数请参考官方文档
        speechRecognizer.enableIntermediateResult(true);

        //启动录音线程
        myRecorder.recordTo(speechRecognizer);
    }

    public void stopRecognizer(View view){
        button.setText("开始 录音");
        button.setEnabled(true);
        // 停止录音
        myRecorder.stop();
    }

    // 语音识别回调类，得到语音识别结果后在这里处理
    // 注意不要在回调方法里调用recognizer.stop()方法
    // 注意不要在回调方法里执行耗时操作
    private static class MyCallback implements SpeechRecognizerCallback {

        private Handler handler;

        public MyCallback(Handler handler) {
            this.handler = handler;
        }
        // 识别开始
        @Override
        public void onRecognizedStarted(String msg, int code)
        {
            Log.d(TAG,"OnRecognizedStarted " + msg + ": " + String.valueOf(code));
        }

        // 请求失败
        @Override
        public void onTaskFailed(String msg, int code)
        {
            Log.d(TAG,"OnTaskFailed " + msg + ": " + String.valueOf(code));
            handler.sendEmptyMessage(0);
        }

        // 识别返回中间结果，只有开启相关选项时才会回调
        @Override
        public void onRecognizedResultChanged(final String msg, int code)
        {
            Log.d(TAG,"OnRecognizedResultChanged " + msg + ": " + String.valueOf(code));
            Message message = new Message();
            message.obj = msg;
            handler.sendMessage(message);
        }

        // 第七步，识别结束，得到最终完整结果
        @Override
        public void onRecognizedCompleted(final String msg, int code)
        {
            Log.d(TAG,"OnRecognizedCompleted " + msg + ": " + String.valueOf(code));
            Message message = new Message();
            message.obj = msg;
            handler.sendMessage(message);
        }

        // 请求结束，关闭连接
        @Override
        public void onChannelClosed(String msg, int code) {
            Log.d(TAG, "OnChannelClosed " + msg + ": " + String.valueOf(code));
        }

    };

    // 根据识别结果更新界面的代码
    private static class MyHandler extends Handler {
        private final WeakReference<SpeechRecognizerActivity> mActivity;

        public MyHandler(SpeechRecognizerActivity activity) {
            mActivity = new WeakReference<SpeechRecognizerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) {
                Log.i(TAG, "Empty message received.");
                return;
            }
            String fullResult = (String)msg.obj;
            String result = null;
            if (!fullResult.equals("")){
                JSONObject jsonObject = JSONObject.parseObject(fullResult);
                if (jsonObject.containsKey("payload")){
                    result = jsonObject.getJSONObject("payload").getString("result");
                }
            }
            mActivity.get().mFullEdit.setText(fullResult);
            mActivity.get().mResultEdit.setText(result);
        }
    }

    // 录音并发送给识别的代码，客户可以直接使用
    private static class MyRecorder implements Runnable {
        // 录音的状态
        final static int STATUS_INIT = 0;
        final static int STATUS_WORKING = 1;
        final static int STATUS_STOPPING = 7;
        final static int STATUS_FINISHED = 9;
        final static int STATUS_FAILED = -1;

        final static int SAMPLE_RATE = 16000;
        final static int SAMPLES_PER_FRAME = 640;

        private AudioRecord mAudioRecorder;
        private SpeechRecognizer recognizer;
        private int status = STATUS_INIT;
        private Thread thread;

        public void recordTo(SpeechRecognizer recognizer){
            if (status != STATUS_INIT) {
                throw new IllegalStateException("Current state is: " + status);
            }
            status = STATUS_WORKING;
            this.recognizer = recognizer;

            Log.d(TAG,"Init audio recorder");
            int bufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            mAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes * 2);

            thread = new Thread(this);
            thread.start();
        }

        public int getStatus() {
            return status;
        }

        public void stop(){
            status = STATUS_STOPPING;
        }

        @Override
        public void run() {
            try {
                if (mAudioRecorder != null && mAudioRecorder.getState() == 1) {
                    Log.d(TAG, String.format("mAudioRecorder state is : %s", mAudioRecorder.getState()));
                    try {
                        mAudioRecorder.stop();
                        mAudioRecorder.startRecording();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 第五步，启动语音识别
                int startCode = recognizer.start();
                if(startCode < 0)
                {
                    Log.i(TAG, "Failed to start recognizer");
                    recognizer.stop();
                    status = STATUS_FAILED;
                    return;
                }
                Log.d(TAG,"Recognizer started");

                ByteBuffer buf = ByteBuffer.allocateDirect(SAMPLES_PER_FRAME);
                while(status == STATUS_WORKING){
                    buf.clear();
                    // 采集语音
                    int readBytes = mAudioRecorder.read(buf, SAMPLES_PER_FRAME);
                    byte[] bytes = new byte[SAMPLES_PER_FRAME];
                    buf.get(bytes, 0, SAMPLES_PER_FRAME);
                    if (readBytes>0 && status == STATUS_WORKING){
                        // 第六步，发送语音数据到识别服务
                        int code = recognizer.sendAudio(bytes, bytes.length);
                        if (code < 0) {
                            Log.i(TAG, "Failed to send audio!");
                            status = STATUS_STOPPING;
                            break;
                        }
                    }
                    buf.position(readBytes);
                    buf.flip();
                }
                mAudioRecorder.stop();
                // 第八步，停止本次识别
                recognizer.stop();
                status = STATUS_FINISHED;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
