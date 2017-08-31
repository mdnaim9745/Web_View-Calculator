package p1.webview.naim.webviewcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {


    TextView textViewV1, textViewV2, textViewSign, textViewResult;
    int flag = 0;
    String prev, next;
    String current = "";
    double memory = 0;

    double x, y, z = 0.00;


    //web

    WebView myWebView = null;
    public Handler handler = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void initLocal(){
        setContentView(R.layout.activity_main_2);
        textViewV1 = (TextView) findViewById(R.id.value1);
        textViewV2 = (TextView) findViewById(R.id.value2);
        textViewSign = (TextView) findViewById(R.id.sign1);
        textViewResult = (TextView) findViewById(R.id.result);
    }

    private void initWeb(){
        //Web View
        myWebView = (WebView) findViewById(R.id.webViewCalc);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        //Web View
        handler = new Handler();
    }


    private void load(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null) {
                if (netInfos.isConnected()) {
                    initWeb();
                    myWebView.loadUrl("file:///android_res/raw/calc.html");
                    return;
                }
            }

        }

        initLocal();
    }


    @Override
    protected void onResume() {
        super.onResume();
        load();



    }



    //local calc


    public void Click(View view) {
        Button b = (Button) view;
        String text = b.getText().toString();
        current += text;

        if (flag == -5) {
            Toast.makeText(this, "Clearing values. Please wait...", Toast.LENGTH_SHORT).show();
            clear1();

        }
        else {
            if (text.equals("+") || text.equals("-") || text.equals("x") || text.equals("÷") || text.equals("=")) {
                flag++;
                textViewSign.setText(text);
                current = "";

                if (flag == 1) {
                    prev = text;
                    next = text;
                }
                else {
                    prev = next;
                    next = text;
                }

                x = Double.valueOf(textViewV1.getText().toString());
                if (textViewV2.getText().equals("0")) {
                    y = 0;
                }
                else {
                    y = Double.valueOf(textViewV2.getText().toString());
                }

                if (prev.equals("+")) {
                    z = x + y;
                }
                else if (prev.equals("-")) {
                    z = x - y;

                }
                else if (prev.equals("x")) {
                    z = x * y;

                }
                else if (prev.equals("÷")) {
                    z = x / y;
                }
                if (flag > 1) {
                    textViewV1.setText(Double.valueOf(z).toString());
                    textViewResult.setText(Double.valueOf(z).toString());
                }
                textViewV2.setText("0");

                if (next.equals("=")) {
                    flag = -5;
                }


            }
            else {
                if (flag == 0) {
                    textViewV1.setText(current); //showing first input
                }
                else {
                    textViewV2.setText(current);
                }
            }
        }


    }


    public void clear(View view) {
        clear1();
    }

    public void clear1() {
        textViewV1.setText("0");
        textViewV2.setText("0");
        textViewResult.setText("0");
        textViewSign.setText("");
        prev = "";
        next = "";
        current = "";
        flag = 0;
    }


    public void save(View view) {
        memory = z;
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("memory", (float) memory);
        editor.commit();

        Toast.makeText(this, "Value saved in memory", Toast.LENGTH_SHORT).show();
    }

    public void memc(View view) {
        memory = 0;

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("memory", (float) memory);
        editor.commit();

        Toast.makeText(this, "Memory cleared", Toast.LENGTH_SHORT).show();
    }

    public void showM(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        double mem = sharedPreferences.getFloat("memory", (float) 0.0);
        textViewResult.setText(Double.valueOf(mem).toString());
        editor.commit();
    }

    public void memp(View view) {
        memory++;

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("memory", (float) memory);
        editor.commit();
    }

    public void memm(View view) {
        memory--;

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("memory", (float) memory);
        editor.commit();
    }

}
