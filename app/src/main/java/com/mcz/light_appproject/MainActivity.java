package com.mcz.light_appproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.mcz.light_appproject.app.model.DataInfo;
import com.mcz.light_appproject.app.model.PullrefreshListviewAdapter;
//import com.mcz.hwappproject.app.ui.activity.HistoryActivity;
import com.mcz.light_appproject.app.ui.activity.InputManualActivity;
import com.mcz.light_appproject.app.ui.activity.LoginActivity;
import com.mcz.light_appproject.app.ui.zxing.zxing.new_CaptureActivity;
import com.mcz.light_appproject.app.utils.Config;
import com.mcz.light_appproject.app.utils.DataManager;
import com.mcz.light_appproject.app.view.view.IPullToRefresh;
import com.mcz.light_appproject.app.view.view.LoadingLayout;
import com.mcz.light_appproject.app.view.view.PullToRefreshBase;
import com.mcz.light_appproject.app.view.view.PullToRefreshFooter;
import com.mcz.light_appproject.app.view.view.PullToRefreshHeader;
import com.mcz.light_appproject.app.view.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//implements AdapterView.OnItemClickListener
public class MainActivity extends AppCompatActivity {

    public final static int POSITION_INPUTMANUAL = 100;
    String datajson = "";
    @BindView(R.id.edt_dvid_search)
    EditText edtDvidSearch;
    @BindView(R.id.search_delete)
    ImageView searchDelete;
    @BindView(R.id.img_srearch)
    ImageView imgSrearch;
    //监听back键点击时间
    private long backfirsttime = 0;
    //    TextView txt;
    String token = "";
    SharedPreferences sp;
    @BindView(R.id.main_pull_refresh_lv)
    PullToRefreshListView mListView;
    @BindView(R.id.img_choose)
    ImageView imgchoose;

    @BindView(R.id.main_relative)
    RelativeLayout rela_nodata;

    private View mNoMoreView;
    private String login_appid;
    private PullrefreshListviewAdapter adapter;

    private List<DataInfo> mlist = null;

    boolean type_choose = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);//開始運行main_activity_layout界面
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .statusBarColor(R.color.blues)
                .fitsSystemWindows(true)
                .init();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        init();
        mListView=(PullToRefreshListView)findViewById(R.id.main_pull_refresh_lv) ;
      //  mListView.setOnItemClickListener(this);


    }



    int item_position = 0;

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    private void init() {

        login_appid = sp.getString("appId","");
        token = sp.getString("token", "");

        hintKbTwo();
       // Log.i("token", "-------=======----------" + token);
        rela_nodata.setVisibility(View.GONE);
        imgchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popupWindow.showAsDropDown(v);// 选择弹出框
                Choose_item_ADD();
                item_position++;
                if (item_position == 1) {
                    popupWindow.showAsDropDown(v);// 选择弹出框，
                } else if (item_position == 2) {
                    popupWindow.dismiss();
                    item_position = 0;
                }

            }
        });
        //Bean
//        Button butsub1 = (Button) convertView.findViewById(R.id.butsub1);
//        final EditText editin1 = (EditText) convertView.findViewById(R.id.editin1);
//        editin1.requestFocus();
//
//
//        butsub1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        String add_url = Config.all_url + "/iocm/app/cmd/v1.4.0/dev0iceCommands?appId=" + login_appid;
//                        try {
//
//                            json = DataManager.Comened_DEVICEID2(mContext, add_url, login_appid, token, deviceId.get(position), editin1.getText().toString());
//                            if (json.equals(tuatus)) {
//                                Looper.prepare();
//                                Toast.makeText(editin1.getContext(), editin1.getText().toString() + " 下发成功", Toast.LENGTH_SHORT).show();
//                                Looper.loop();
//                            } else {
//                                Looper.prepare();
//                                Toast.makeText(editin1.getContext(), editin1.getText().toString() + " 下发失败", Toast.LENGTH_SHORT).show();
//                                Looper.loop();
//                            }
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//
//            }
//
//        });
        //搜索設備時候監聽圖片
/*
        imgSrearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtDvidSearch.getText().toString().trim().equals("")){
                        adapter.clearItem();
                        mlist=new ArrayList<DataInfo>();
                    datajson=edtDvidSearch.getText().toString().trim();
                    new LoadDataAsyncTask(true,true).execute();
                    hintKbTwo();

                }else{
                    new LoadDataAsyncTask(true, false).execute();//查询所有
                    hintKbTwo();
                }

            }
        });*/
      bofang();
/*
        searchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDvidSearch.setText("");
            }
        });//刪除編輯款内容
*/
        ///listview
        mNoMoreView = getLayoutInflater().inflate(R.layout.no_device_more_footer, null);
       // mListView.setOnItemClickListener(this);
        mListView.setLoadingLayoutCreator(new PullToRefreshBase.LoadingLayoutCreator() {
            @Override
            public LoadingLayout create(Context context, boolean headerOrFooter,
                                        PullToRefreshBase.Orientation orientation) {
                if (headerOrFooter)
                    return new PullToRefreshHeader(context);
                else
                    return new PullToRefreshFooter(context, PullToRefreshFooter.Style.EMPTY_NO_MORE.EMPTY_NO_MORE);

            }
        });
//        Drawable drawable = getResources().getDrawable(R.color.transparent);
        mListView.getRefreshableView().setSelector(getResources().getDrawable(R.color.transparent));
        //设置可上拉刷新和下拉刷新
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        //异步加载数据
        mListView.setOnRefreshListener(new IPullToRefresh.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView,
                                  boolean headerOrFooter) {
                //填充数据
//                getList_AsyncTask(headerOrFooter);
                new LoadDataAsyncTask(headerOrFooter, false).execute();
            }
        });

        adapter = new PullrefreshListviewAdapter( this);
        adapter.setlogin_appid(login_appid);
        adapter.settoken(token);
        mListView.getRefreshableView().addFooterView(mNoMoreView);
        mListView.setAdapter(adapter);
        mListView.getRefreshableView().removeFooterView(mNoMoreView);


    }  ////end init
    /**
     * listview添加数据
     *
     * @param Fnum
     * @param Onum
     * @return
     */
    private List<DataInfo> ListviewADD_Data(int Fnum, int Onum) {
        try {

            String add_url = Config.all_url + "/iocm/app/dm/v1.3.0/devices?appId=" + login_appid
                    + "&pageNo=" + Fnum + "&pageSize=" + Onum;
            String json = DataManager.Txt_REQUSET(MainActivity.this, add_url, login_appid, token);
           mlist = new ArrayList<DataInfo>();

            JSONObject jo = new JSONObject(json);
            JSONArray jsonArray = jo.getJSONArray("devices");
            for (int i = 0; i < jsonArray.length(); i++) {
                DataInfo dataInfo = new DataInfo();
                String deviceinfo = jsonArray.getJSONObject(i).getString("deviceInfo");
                String servicesinfo = jsonArray.getJSONObject(i).getString("services");
                if (!servicesinfo.equals("null")){
                    JSONArray jsa=new JSONArray(servicesinfo);
                    for (int j = 0; j < jsa.length(); j++) {
                        String ser_data = jsa.getJSONObject(j).getString("data");
                        JSONObject jsonObject = new JSONObject(ser_data);

                      dataInfo.setDevicelight(jsonObject.optString("light"));


                    }
//
                    dataInfo.setDeviceId(jsonArray.getJSONObject(i).optString("deviceId"));
                    dataInfo.setGatewayId(jsonArray.getJSONObject(i).optString("gatewayId"));
                    dataInfo.setLasttime(jsonArray.getJSONObject(i).optString("lastModifiedTime"));
                    JSONObject object = new JSONObject(deviceinfo);
                    dataInfo.setDeviceName(object.optString("name"));
                  //  dataInfo.setDeviceType(object.optString("deviceType"));//model  deviceType
                    dataInfo.setDeviceStatus(object.optString("status"));

                    mlist.add(dataInfo);
                }else{
                    dataInfo.setDeviceId(jsonArray.getJSONObject(i).optString("deviceId"));
                    dataInfo.setGatewayId(jsonArray.getJSONObject(i).optString("gatewayId"));
                    dataInfo.setLasttime(jsonArray.getJSONObject(i).optString("lastModifiedTime"));
                    JSONObject object = new JSONObject(deviceinfo);
                    dataInfo.setDeviceName(object.optString("name"));
                  //  dataInfo.setDeviceType(object.optString("deviceType"));//model  deviceType
                    dataInfo.setDeviceStatus(object.optString("status"));

                  dataInfo.setDevicelight("暂无数据");


                    mlist.add(dataInfo);
                }


            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return mlist;
    }
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.i("position====", position + "");
//        Intent mIntent = new Intent(getApplicationContext(), shujuActivity.class);
//        String dvid = mlist.get(position - 1).getDeviceId();
//        String DeviceLastTime = mlist.get(position - 1).getLasttime();
//        hintKbTwo();
//        DataInfo dataInfo_item=new DataInfo();
//        dataInfo_item=mlist.get(position-1);
//        mIntent.putExtra("dvid", dvid);
//        mIntent.putExtra("DeviceLastTime", DeviceLastTime);
//        mIntent.putExtra("dataiteminfo",(Serializable)dataInfo_item);
////        mIntent.putExtra("positionid",position - 1);
//        startActivity(mIntent);
//    }


    PopupWindow popupWindow;

    private void Choose_item_ADD() {
        View popView = LayoutInflater.from(this).inflate(
                R.layout.popmenu, null);
//        View rootView =findViewById(R.id.img_choose); // 根佈局
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);


//        View rootView =findViewById(R.id.img_choose); // 根佈局
//        final PopupWindow popupWindow = new PopupWindow(popView,
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundAlpha(0.8f);//设置屏幕透明度
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
                item_position = 0;
            }
        });
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setFocusable(true);// 点击空白处时，隐藏掉pop窗口
//        // 顯示在根佈局的底部
//        popupWindow.showAtLocation(rootView, Gravity.RIGHT | Gravity.CENTER_HORIZONTAL, 0,
//                0);
//        popupWindow.showAsDropDown(view, 0, 45);
        popView.findViewById(R.id.txt_manual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //手动输入设备号
                item_position = 0;
                Intent mIntent = new Intent(MainActivity.this, InputManualActivity.class);
                startActivityForResult(mIntent, POSITION_INPUTMANUAL);
                finish();


            }
        });
        popView.findViewById(R.id.txt_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                item_position = 0;

                Intent mIntent = new Intent(MainActivity.this, new_CaptureActivity.class);
                startActivity(mIntent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }


    private List<DataInfo> Listview_Inputmanual(String dvid) {
        try {
//            https://server:port/iocm/app/dm/v1.3.0/devices/{deviceId}?appId={appId}
            String login_appid = sp.getString("appId","");
            String add_url = Config.all_url + "/iocm/app/dm/v1.3.0/devices/"+dvid+"?appId=" + login_appid;
            String json = DataManager.Txt_REQUSET(MainActivity.this, add_url, login_appid, token);
            Log.i("aaa", "josn1" + json);
            mlist = new ArrayList<DataInfo>();
            JSONObject jo = new JSONObject(json);
            String code_error=jo.optString("error_code");
            if (!code_error.equals("")){
//                DataInfo dataInfo = new DataInfo();
//                dataInfo.setError_code(code_error);
//                mlist.add(dataInfo);
                return null;
            }else{
                DataInfo dataInfo = new DataInfo();
//                JSONArray jsonArray = jo.getJSONArray("devices");
                String deviceinfo = jo.optString("deviceInfo");
                dataInfo.setDeviceId(jo.optString("deviceId"));
                dataInfo.setLasttime(jo.optString("lastModifiedTime"));
                JSONObject object = new JSONObject(deviceinfo);
                dataInfo.setDeviceName(object.optString("name"));
            //    dataInfo.setDeviceType(object.optString("deviceType"));//model  deviceType
                dataInfo.setDeviceStatus(object.optString("status"));
                String servicesinfo = jo.optString("services");
                if (!servicesinfo.equals("null")){
                    JSONArray jsa=new JSONArray(servicesinfo);
                    for (int j = 0; j <  jsa.length(); j++) {
                        String ser_data = jsa.getJSONObject(j).getString("data");
                        JSONObject jsonObject = new JSONObject(ser_data);

                        dataInfo.setDevicelight(jsonObject.optString("light"));//和profile文件一樣


                    }
                    mlist.add(dataInfo);
                }else{


                    dataInfo.setDevicelight("暂无数据");
//
                }


            }
        } catch ( Exception e ) {
            Toast.makeText(this, "请输入正确的设备ID", Toast.LENGTH_SHORT).show();
            rela_nodata.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }


        return mlist;
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 异步加载任务
     */
    //下标标识
    int numbers = 5;
    int begin = 0;

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        Intent mIntent = new Intent(getApplicationContext(), HistoricaldataActivity.class);
//        String deviceId = mlist.get(position - 1).getDeviceId();
//        String gatewayId = mlist.get(position - 1).getGatewayId();
//        mIntent.putExtra("deviceId", deviceId);
//        mIntent.putExtra("gatewayId", gatewayId);
//        startActivity(mIntent);
//    }


    private class LoadDataAsyncTask extends AsyncTask<Void, Void, List<DataInfo>> {
        private boolean mHeaderOrFooter;
        private boolean is_Add;

        public LoadDataAsyncTask(boolean headerOrFooter, boolean isadd) {
            mHeaderOrFooter = headerOrFooter;
            is_Add = isadd;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mHeaderOrFooter) {
                mListView.setVisibility(View.VISIBLE);
            }
            mListView.getRefreshableView().removeFooterView(mNoMoreView);
        }

        @Override
        protected List<DataInfo> doInBackground(Void... params) {
            if (MainActivity.this.isFinishing()) {
                return null;
            }
            List<DataInfo> result = null;

            try {
                //首先判断是否查询所有还是单个
                if (!is_Add) {
                    if (mHeaderOrFooter && type_choose) {
                        //下拉刷新走的方法
                        if (type_choose == false) {
                            adapter.clearItem();
                            mlist = new ArrayList<DataInfo>();
                        }
                        type_choose = true;
                        numbers = 5;
                        result = ListviewADD_Data(0, numbers);

                        begin=5;
//                    begin=7;
                    } else {
//                    //上拉加载走的方法
                        if (type_choose == false) {
                            adapter.clearItem();
                            mlist = new ArrayList<DataInfo>();
                            numbers = 5;
                            result = ListviewADD_Data(0, numbers);
                            begin = 5;
                            type_choose = true;
                        } else {
//                            adapter.clearItem();
                            begin = begin + 5;
                            result = ListviewADD_Data(0, begin);
                            type_choose = true;
                        }
                    }
                } else {
                    //更据ID查询
                    result = Listview_Inputmanual(datajson);
                }


                return result;
            } catch ( Exception e ) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 完成时的方法
         */
        @Override
        protected void onPostExecute(List<DataInfo> result) {
            super.onPostExecute(result);
            mListView.onRefreshComplete();
            if (MainActivity.this.isFinishing()) {
                return;
            }
            if (result != null) {
                if (mHeaderOrFooter) {
                    CharSequence dateText = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date());
                    for (LoadingLayout layout : mListView.getLoadingLayoutProxy(true, false).getLayouts()) {
                        ((PullToRefreshHeader) layout).setLastRefreshTime(":" + dateText);
                    }
                    adapter.clearItem();
                    mlist = new ArrayList<DataInfo>();
                }
                if (adapter.getCount() == 0 && result.size() == 0) {
                    mListView.setVisibility(View.GONE);
                    mListView.getRefreshableView().removeFooterView(mNoMoreView);
                    rela_nodata.setVisibility(View.VISIBLE);
                } else if (result.size()>5) {
                    //在这里判断数据的多少确定下一步能否上拉加载
//                    if (result.size()<5){
//                        mListView.getRefreshableView().removeFooterView(mNoMoreView);
//                    }else{
                    if (result.size()%5==0){
                    }else{
                        mListView.setFooterRefreshEnabled(false);
                        mListView.getRefreshableView().addFooterView(mNoMoreView);
                        rela_nodata.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }

//                    }

                } else if (mHeaderOrFooter) {

                    if (result.size()>0){
                        rela_nodata.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }
                    mListView.setFooterRefreshEnabled(true);
                    mListView.getRefreshableView().removeFooterView(mNoMoreView);
                }
//                ???
//                relat.setVisibility(View.GONE);
                //通过additem方法把刷新后拿到的数据循环添加到adapter中去
//                if ((result.size()==1)) {
////                    if (!result.get(0).getError_code().equals("")){
////                        rela_nodata.setVisibility(View.VISIBLE);
////                        Toast.makeText(MainActivity.this, "暂无该设备信息", Toast.LENGTH_SHORT).show();
////                    }else {

////                    }
//                }else {
                    addlistdata(result);
//                mListView.setFooterRefreshEnabled(false);
//                mListView.getRefreshableView().removeFooterView(mNoMoreView);
//                    rela_nodata.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();//刷新完成
//                }
            }else{
                rela_nodata.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "暂无设备信息", Toast.LENGTH_SHORT).show();

            }

        }
    }

        //该方法用了后程序后台变卡
    private class LoadDataAsyncTask_service extends AsyncTask<Void, Void, List<DataInfo>> {
        private boolean mHeaderOrFooter;
        private boolean is_Add;

        public LoadDataAsyncTask_service(boolean headerOrFooter, boolean isadd) {
            mHeaderOrFooter = headerOrFooter;
            is_Add = isadd;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mHeaderOrFooter) {
                mListView.setVisibility(View.VISIBLE);
            }
            mListView.getRefreshableView().removeFooterView(mNoMoreView);
        }

        @Override
        protected List<DataInfo> doInBackground(Void... params) {
            if (MainActivity.this.isFinishing()) {
                return null;
            }
            List<DataInfo> result = null;

            try {
                Thread.sleep(5000);
                //首先判断是否查询所有还是单个
                if (!is_Add) {
                    if (mHeaderOrFooter && type_choose) {
                        //下拉刷新走的方法
                        if (type_choose == false) {
                            adapter.clearItem();
                            mlist = new ArrayList<DataInfo>();
                        }
                        type_choose = true;
                        numbers = 5;
                        result = ListviewADD_Data(0, numbers);
                        begin=5;
//                    begin=7;
                    } else {
//                    //上拉加载走的方法
                        if (type_choose == false) {
                            adapter.clearItem();
                            mlist = new ArrayList<DataInfo>();
                            numbers = 5;
                            result = ListviewADD_Data(0, numbers);
                            begin = 5;
                            type_choose = true;
                        } else {
//                            adapter.clearItem();
                            begin = begin + 5;
                            result = ListviewADD_Data(0, begin);
                            type_choose = true;
                        }
                    }
                } else {
                    //更据ID查询
                    result = Listview_Inputmanual(datajson);
                }


                return result;
            } catch ( Exception e ) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 完成时的方法
         */
        @Override
        protected void onPostExecute(List<DataInfo> result) {
            super.onPostExecute(result);
            mListView.onRefreshComplete();
            if (MainActivity.this.isFinishing()) {
                return;
            }
            if (result != null) {
                if (mHeaderOrFooter) {
                    CharSequence dateText = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date());
                    for (LoadingLayout layout : mListView.getLoadingLayoutProxy(true, false).getLayouts()) {
                        ((PullToRefreshHeader) layout).setLastRefreshTime(":" + dateText);
                    }
                    adapter.clearItem();
                    mlist = new ArrayList<DataInfo>();
                }
                if (adapter.getCount() == 0 && result.size() == 0) {
                    mListView.setVisibility(View.GONE);
                    mListView.getRefreshableView().removeFooterView(mNoMoreView);
                    rela_nodata.setVisibility(View.VISIBLE);
                } else if (result.size()>5) {
                    //在这里判断数据的多少确定下一步能否上拉加载
//                    if (result.size()<5){
//                        mListView.getRefreshableView().removeFooterView(mNoMoreView);
//                    }else{
                    if (result.size()%5==0){
                    }else{
                        mListView.setFooterRefreshEnabled(false);
                        mListView.getRefreshableView().addFooterView(mNoMoreView);
                    }

//                    }

                } else if (mHeaderOrFooter) {

                    if (result.size()>0){
                        rela_nodata.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }
                    mListView.setFooterRefreshEnabled(true);
                    mListView.getRefreshableView().removeFooterView(mNoMoreView);
                }
//                ???
//                relat.setVisibility(View.GONE);
                //通过additem方法把刷新后拿到的数据循环添加到adapter中去
//                if ((result.size()==1)) {
////                    if (!result.get(0).getError_code().equals("")){
////                        rela_nodata.setVisibility(View.VISIBLE);
////                        Toast.makeText(MainActivity.this, "暂无该设备信息", Toast.LENGTH_SHORT).show();
////                    }else {

////                    }
//                }else {
                addlistdata(result);
//                mListView.setFooterRefreshEnabled(false);
//                mListView.getRefreshableView().removeFooterView(mNoMoreView);
//                    rela_nodata.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();//刷新完成
//                }
            }else{
                rela_nodata.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "暂无设备信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean playerIS=true;
    /**
     * 数据循环添加
     *
     * @param result
     */
    private void addlistdata(List<DataInfo> result) {
        int count = result.size();
        if(result.size()>0){
            sp.edit().putBoolean("isplayer",true).commit();
            try {
                mediaPlayer.stop();
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch(IOException e) {
                e.printStackTrace();
            }

        }

        adapter.clearItem();
        for (int i = 0; i < count; i++) {
            adapter.addItem(result.get(i));
            mlist.add(result.get(i));
            //mlist集合是用于存放界面中的值  并在跳转时传入item界面
        }
    }

    MediaPlayer mediaPlayer=null;
    private void bofang(){
//        为activity注册的默认 音频通道 。
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audioService = (AudioManager) this
                .getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
        }
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mediaPlayer.start();
//            }
//        });
//        　注册事件。当播放完毕一次后，重新指向流文件的开头，以准备下次播放。
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
        AssetFileDescriptor file =this.getResources().openRawResourceFd(
                R.raw.airpp);
        try{
            mediaPlayer.setDataSource(file.getFileDescriptor(),
                    file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(0.10f,200L);
            mediaPlayer.prepare();
        }catch (IOException ioe) {
            Log.w("eeeee", ioe);
            mediaPlayer = null;
        }
    }



    /**
     * 在进入界面的时候自动加载一遍
     * 第一次有数据显示
     */
    private void refreshButtonClicked() {
        mListView.setVisibility(View.VISIBLE);
        mListView.setMode(IPullToRefresh.Mode.BOTH);
        mListView.setRefreshing();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((adapter != null && adapter.getCount() == 0)) {
            refreshButtonClicked();
        }
        hintKbTwo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshButtonClicked();
        hintKbTwo();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * 监听返回按钮 (back键)
         * 使用系统时间里判断用户是否需要退出
         */
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - backfirsttime > 2000) {
                Toast.makeText(getApplication(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                backfirsttime = System.currentTimeMillis();
            } else {
                try {
                    mediaPlayer.stop();
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(0);
                } catch(IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.release();
                System.exit(0);
//                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     *返回键处理
     */
    //Bean
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() != KeyEvent.ACTION_UP){
            //TODO
            EditText editin1 = (EditText)findViewById(R.id.editin1);
            String text = editin1.getText().toString();
            if(text.length() > 0 ){//判断文本框是否有文字，如果有就去掉最后一位
                String newText = text.substring(0, text.length() - 1);
                editin1.setText(newText);
                editin1.setSelection(newText.length());//设置焦点在最后
            };
        }

        return super.dispatchKeyEvent(event);
}
//    @Override
//    public boolean dispatchKeyEvent( KeyEvent event) {
//        super.dispatchKeyShortcutEvent(event);
//        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){//监听到删除按钮被按下
//            EditText editin1 = (EditText)findViewById(R.id.editin1);
//            String text = editin1.getText().toString();
//            if(text.length() > 0 ){//判断文本框是否有文字，如果有就去掉最后一位
//                String newText = text.substring(0, text.length() - 1);
//                editin1.setText(newText);
//                editin1.setSelection(newText.length());//设置焦点在最后
//            };
//        }
//        return super.dispatchKeyShortcutEvent(event);
//    }

    //点击空白处隐藏键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }





}
