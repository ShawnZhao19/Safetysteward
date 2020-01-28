package com.example.safetysteward;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safetysteward.Utils.MD5Utils;


public class MainActivity extends AppCompatActivity{
    private Button calculate;
    private Button number;
    private Button progress;
    private Button message;
    private Button software;
    private Button process;
    private Button safe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //获取布局
        View dialog = View.inflate(MainActivity.this,R.layout.dialog,null);
        //获取布局中控件
        final EditText editxt_dialog = dialog.findViewById(R.id.editxt_dialog);
        final Button dialog_btn = dialog.findViewById(R.id.dialog_btn);
        //设置参数
        builder.setTitle("safe").setIcon(R.mipmap.safe).setView(dialog);
        // 创建对话框
        final AlertDialog alertDialog = builder.create();
        final SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        dialog_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                String password = editxt_dialog.getText().toString().trim();//结尾修建空格
                if (TextUtils.isEmpty(password) ) {
                    Toast.makeText(MainActivity.this, "密码不能为空！", 0).show();
                    return;
                }else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", MD5Utils.md5Password(password));
                    editor.commit();
                    Toast.makeText(MainActivity.this, "密码保存成功！", 0).show();
                }
                alertDialog.dismiss();// 对话框消失
            }
        });
        alertDialog.show();
    }

    public void calculate(View view) {
        Intent intent=new Intent(MainActivity.this,CalculatorActivity.class);
        startActivity(intent);
    }

    public void safe(View view) {
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //获取布局
        View dialog = View.inflate(MainActivity.this,R.layout.dialog_protect,null);
        //获取布局中控件
        final EditText editxt_protect_dialog = dialog.findViewById(R.id.protect_dalog_editxt);
        final Button protect_dialog_btn = dialog.findViewById(R.id.btn_yes);
        //设置参数
        builder.setTitle("safe").setIcon(R.mipmap.safe).setView(dialog);
        // 创建对话框
        final AlertDialog alertDialog = builder.create();
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        //得到SharedPreferences.Editor对象
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        protect_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                String password = editxt_protect_dialog.getText().toString().trim();//结尾修建空格
                SharedPreferences sp = getSharedPreferences("config", 0);
                String pas = sp.getString("password", null);
                String str = MD5Utils.md5Password(password);
                if (str.equals(pas)){
                    Intent intent = new Intent(MainActivity.this,SafeActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"密码不正确",0);
                }
                alertDialog.dismiss();// 对话框消失
            }
        });
        alertDialog.show();;

    }

    public void number(View view) {
        Intent intent=new Intent(MainActivity.this,NumberActivity.class);
        startActivity(intent);
    }

    public void message(View view) {
        Intent intent=new Intent(MainActivity.this,MessageActivity.class);
        startActivity(intent);
    }

    public void software(View view) {
        Intent intent=new Intent(MainActivity.this,SoftActivity.class);
        startActivity(intent);
    }

    public void process(View view) {
        Intent intent=new Intent(MainActivity.this,ProcessActivity.class);
        startActivity(intent);
    }

    public void progress(View view) {
        Intent intent=new Intent(MainActivity.this,UpdateActivity.class);
        startActivity(intent);
    }
}


