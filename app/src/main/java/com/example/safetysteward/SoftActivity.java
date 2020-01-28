package com.example.safetysteward;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safetysteward.SoftManager.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class SoftActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    private ListView lv_main;
    private List<AppInfo> data;
    private AppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft);
        //初始化成员变量
        lv_main = (ListView) findViewById(R.id.soft_listview);
        data = getAllAppInfos();
        adapter = new AppAdapter();
        //显示列表
        lv_main.setAdapter(adapter);

        //给ListView设置item的点击监听
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * parent : ListView
             * view : 当前行的item视图对象
             * position : 当前行的下标
             */

            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //提示当前行的应用名称
                String appName = data.get(position).getAppName();
                String packageName = data.get(position).getPackageName();
                //提示
                Toast.makeText(SoftActivity.this, appName, 0).show();
                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                startActivity(intent);
            }

        });
        //给LitView设置Item的长按监听
        lv_main.setOnItemLongClickListener(this);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //删除当前行
        //删除当前行的数据
        data.remove(position);
        //更新列表
        //lv_main.setAdapter(adapter);//显示列表, 不会使用缓存的item的视图对象
        adapter.notifyDataSetChanged();//通知更新列表, 使用所有缓存的item的视图对象
        String packageName = data.get(position).getPackageName();
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        startActivity(intent);
        return true;
    }

    class  AppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size()  ;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        //返回带数据当前行的Item视图对象
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //1. 如果convertView是null, 加载item的布局文件
            if(convertView==null) {
                Log.e("TAG", "getView() load layout");
                convertView = View.inflate(SoftActivity.this, R.layout.item_soft_list, null);
            }
            //2. 得到当前行数据对象
            AppInfo appInfo = data.get(position);
            //3. 得到当前行需要更新的子View对象
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_item_name);
            //4. 给视图设置数据
            imageView.setImageDrawable(appInfo.getIcon());
            textView.setText(appInfo.getAppName());

            //返回convertView
            return convertView;
        }
    }
    /*
     * 得到手机中所有应用信息的列表
     * AppInfo
     *  Drawable icon  图片对象
     *  String appName
     *  String packageName
     */
    private List<AppInfo> getAllAppInfos() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        // 得到应用的packgeManager
        PackageManager packageManager = getPackageManager();
        // 创建一个主界面的intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 得到包含应用信息的列表
        List<ResolveInfo> ResolveInfos = packageManager.queryIntentActivities(
                intent, 0);
        // 遍历
        for (ResolveInfo ri : ResolveInfos) {
            // 得到包名
            String packageName = ri.activityInfo.packageName;
            // 得到图标
            Drawable icon = ri.loadIcon(packageManager);
            // 得到应用名称
            String appName = ri.loadLabel(packageManager).toString();
            // 封装应用信息对象
            AppInfo appInfo = new AppInfo(icon, appName, packageName);
            // 添加到list
            list.add(appInfo);
        }
        return list;

    }
}