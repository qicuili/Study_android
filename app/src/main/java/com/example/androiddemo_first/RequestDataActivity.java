package com.example.androiddemo_first;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_data);
        Button btn_get = findViewById(R.id.get_request);
        Button btn_post = findViewById(R.id.post_request);
        btn_get.setOnClickListener(this::onClick);
        btn_post.setOnClickListener(this::onClick);
    }

    void onClick(View v) {
        if (v.getId() == R.id.get_request) {
            //发送get请求 httpurlconnection
//            getDataWithGetMethod();
            //使用okhttp
            requestDataWithOKHttp();

        } else {
            //发送post请求 httpurlconnection
            getDataWithPostMethod();
        }

    }


    //    get请求
    void getDataWithGetMethod() {
        //httpurlconnection
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        showRequestDataView(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //    post请求
    void getDataWithPostMethod() {
        //httpurlconnection
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes("username=admin&password=123456");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        showRequestDataView(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //使用okhttp
    void requestDataWithOKHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://console.gamebrain.io/advertise/tab_json?appid=143&country=CN")
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.e("数据请求", "" + response);
                    if (response.code() == 200) {
                        String responseData = response.body().string();
                        Log.e("data", "数据请求成功"+responseData);
                        parseResponseDataWithGSON(responseData);//通过GSON解析数据，转model
                    } else {
                        Log.e("data", "数据请求失败");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    //数据解析和展示   此处是json字符串  json->model jsonObject 这种方式有点累赘
    void parseResponseDataWithJsonObject(String responseData) {
        try {
            Log.e("data====", responseData);
            //一层一层解析
            JSONObject jsonObject = new JSONObject(responseData);
            String adjust_token = jsonObject.getString("adjust_token");
            JSONObject jsonobject_conf = jsonObject.getJSONObject("conf");
            //依次进行解析

        }catch (Exception e){

        }
    }
//数据解析 json->model GSON   创建对应的json的model类 通过字段直接对应解析出来model
    void parseResponseDataWithGSON(String responseData){
        Gson gson = new Gson();
        ResponseDataModel datamodel = gson.fromJson(responseData, ResponseDataModel.class);
        Log.e("返回数据",""+datamodel.getAdjust_token());
        //回到主线程 ，将解析的数据展示出来
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //弹出数据请求的内容
                TextView data_text = findViewById(R.id.show_request_data);
                Gson gson = new Gson();
                //model->json
                String json_str = gson.toJson(datamodel);
                data_text.setText(json_str);
            }
        });

    }
    //刷新UI要回到主线程
    void showRequestDataView(String responseStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //弹出数据请求的内容
                Toast.makeText(getApplicationContext(), responseStr, Toast.LENGTH_SHORT).show();
            }
        });
    }
}