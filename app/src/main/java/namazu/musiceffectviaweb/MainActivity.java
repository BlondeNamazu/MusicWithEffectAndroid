package namazu.musiceffectviaweb;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends Activity implements SensorEventListener{

    WebView myWebView;
    private SensorManager manager;
    EditText edit;
    private String key;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //レイアウトで指定したWebViewのIDを指定する。
        myWebView = (WebView)findViewById(R.id.webView1);

        //リンクをタップしたときに標準ブラウザを起動させない
        myWebView.setWebViewClient(new WebViewClient());

        //最初にgoogleのページを表示する。
//        myWebView.loadUrl("http://audiocontext.musicviaweb.namazu.trap.show/");
        myWebView.loadUrl("http://musicviaweb.namazu.trap.show/");
        //myWebView.loadUrl("http://musicviaweb.namazu.trap.show/");
       // myWebView.loadUrl("http://phiary.me/webaudio-api-getting-started/");
        //jacascriptを許可する
        myWebView.getSettings().setJavaScriptEnabled(true);

        edit = (EditText) findViewById(R.id.key);

        Button button = (Button) findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                key = edit.getText().toString();
                myWebView.loadUrl("javascript:key = "+key);
            }
        });
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        myWebView.loadUrl("http://musicviaweb.namazu.trap.show/");
        return false;
    }

    @Override
    protected void onStop(){
        super.onStop();
        manager.unregisterListener(this);
    }

    @Override

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // Listenerの登録
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_GRAVITY);
        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
        sensors = manager.getSensorList(Sensor.TYPE_LIGHT);
        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
        sensors = manager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void publish(String tag,String value){
        if(key == null || key.equals("")) return;
        String str = key+" "+ tag + " "+value;
        myWebView.loadUrl("javascript:socketio.emit('publish',{value: '" + str + "'})");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float gx = sensorEvent.values[SensorManager.DATA_X];
            float gy = sensorEvent.values[SensorManager.DATA_Y];
            float gz = sensorEvent.values[SensorManager.DATA_Z];
            publish("gx",String.valueOf(gx));
            publish("gy",String.valueOf(gy));
            publish("gz",String.valueOf(gz));
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            float lx = sensorEvent.values[SensorManager.DATA_X];
            publish("light",String.valueOf(lx));
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float ax = sensorEvent.values[SensorManager.DATA_X];
            float ay = sensorEvent.values[SensorManager.DATA_Y];
            float az = sensorEvent.values[SensorManager.DATA_Z];
            publish("ax",String.valueOf(ax));
            publish("ay",String.valueOf(ay));
            publish("az",String.valueOf(az));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}