package com.example.safetysteward;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safetysteward.MessageManager.SmsInfo;
import com.example.safetysteward.Service.BackupSmsService;
import com.example.safetysteward.Service.SmsInfoService;

import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView btn_backup;
    private TextView btn_restore;
    private ProgressDialog mPd;
    private SmsInfoService smsInfoService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        btn_backup = findViewById(R.id.btn_backup);
        btn_backup.setOnClickListener(this);
        btn_restore = findViewById(R.id.btn_restore);
        btn_restore.setOnClickListener(this);
        smsInfoService = new SmsInfoService(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.btn_backup:
                intent = new Intent(this, BackupSmsService.class);
                startService(intent);
                break;
            case R.id.btn_restore:
                restoreSms();

            default:
                break;
        }
    }
    /**
     * 还原短信操作
     */
    private void restoreSms() {
        //1 删除所有的短信
        //2 把xml里面的数据插入到短信的数据库
        //2.1 先解析xml文件
        //2.2 插入数据

        mPd = new ProgressDialog(this);
        mPd.setTitle("正在删除原来的短信");
        mPd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mPd.show();

        new Thread(){
            @SuppressLint("WrongConstant")
            public void run() {
                try {
                    Uri uri = Uri.parse("content://sms");
                    getContentResolver().delete(uri, null, null);
                    mPd.setTitle("正在还原短信");
                    List<SmsInfo> smsInfos = smsInfoService.getSmsInfosFromXml();
                    mPd.setMax(smsInfos.size());
                    for(SmsInfo smsInfo:smsInfos){
                        ContentValues values = new ContentValues();
                        values.put("address", smsInfo.getAddress());
                        values.put("date", smsInfo.getDate());
                        values.put("type", smsInfo.getType());
                        values.put("body", smsInfo.getBody());
                        getContentResolver().insert(uri, values);
                        SystemClock.sleep(2000);
                        mPd.incrementProgressBy(1);//每次进度条刻度值加1

                    }
                    mPd.dismiss();
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "短信还原成功", 1).show();
                    Looper.loop();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mPd.dismiss();
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "短信还原失败", 1).show();
                    Looper.loop();
                }

            }
        }.start();
    }
}