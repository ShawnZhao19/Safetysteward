package com.example.safetysteward;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NumberActivity extends AppCompatActivity{
    private EditText et_phone;
    private TextView tv_phone;
    private final static int START = 0;
    private final static int FINISH = 1;
    private String phone;//待查询号码
    //号码信息
    private static String province;
    private static String city;
    private static String company;
    private static String card;

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;

    public static final String APPKEY ="a576f6978a75ef56709cf16b3b163bf4";
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @SuppressLint("SetTextI18n")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case START:
                    Toast.makeText(NumberActivity.this, "正在查询，请稍候", Toast.LENGTH_SHORT).show();
                    break;

                case FINISH:
                    //在Textview中显示查得的号码信息（子线程中不能更新UI）
                    tv_phone.setText(province +" "+ city + " " + company + " " + card);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
    }

    public void query(View view) {
        phone = et_phone.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            new Thread(){
                public void run() {
                    //开始查询
                    handler.obtainMessage(START).sendToTarget();
                    getRequest(phone);
                    //查得结果
                    handler.obtainMessage(FINISH).sendToTarget();
                }
            }.start();
        }else {
            Toast.makeText(NumberActivity.this, "输入号码不能为空", Toast.LENGTH_SHORT).show();
        }


    }

    private void getRequest(final String phone) {
        String result =null;
        String url ="http://apis.juhe.cn/mobile/get";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("phone",phone);//需要查询的手机号码或手机号码前7位
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        params.put("dtype","");//返回数据的格式,xml或json，默认json

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NumberActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            //得到JSON数据，并进行解析
            result =net(url, params, "GET");
            Log.i("1111","21564");
            JSONObject object = new JSONObject(result);
            Log.i("1111",object.toString());
            JSONObject ob = new JSONObject(object.get("result").toString()+"");
            province = ob.getString("province");
            city = ob.getString("city");
            company = ob.getString("company");
            card = ob.getString("card");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String net(String strUrl, Map params, String method)throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        Log.i("1111","21564");
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            // conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    private String urlencode(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();

    }


    private void initview() {
        setContentView(R.layout.activity_number);
        et_phone =findViewById(R.id.et_phone);
        tv_phone = findViewById(R.id.tv_phone);

    }
}