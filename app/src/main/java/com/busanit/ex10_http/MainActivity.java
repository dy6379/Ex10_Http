package com.busanit.ex10_http;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlStr = editText.getText().toString();
                //네트워크 요청은 스레드에서
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(urlStr);
                    }
                }).start();
            }
        });
    }

    private void request(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn!=null){
                //커넥션 연결
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                //BufferedReader로 읽음
                int resCode = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //한줄씩 읽는다
                String line = null;
                while(true){
                    line = reader.readLine();
                    if (line == null) break;
                    output.append(line+"\n");
                }
                reader.close();
                conn.disconnect();
            }
        } catch (IOException e) {
            println("예외 발생함 : "+e.toString());
        }
        println("응답 -> "+output.toString());
    }
    public void println(final String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data+"\n");
            }
        });
    }
}