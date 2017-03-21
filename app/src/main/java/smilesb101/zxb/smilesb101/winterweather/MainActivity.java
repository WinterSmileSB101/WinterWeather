package smilesb101.zxb.smilesb101.winterweather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import Enum.*;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.Inflater;

import WeatherData.GPSInfo;
import WeatherData.weatherInfoStatic;
import getMainThings.ValuesStatic;
import help.EXCELOpreate;
import help.FileStore;
import help.GPSLocation_LBSMAP;
import help.Help;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

	/**
     * 主线程的Context
     */
    public static Context mContext;
	/**
     * 主线程活动
     */
    public static Activity mAct;
    /**
     * 收到子线程的消息
     */
    public static Handler mHandler = new Handler()
    {
        public void handleMessage(Message paramAnonymousMessage)
        {
            switch (paramAnonymousMessage.what)
            {
                default:
                    return;
                case 1:
                    //Log.i("主线程收到消息：", paramAnonymousMessage.toString());
                   // MainActivity.ShowMessageInAPP(paramAnonymousMessage.obj.toString());
                    MainActivity.ShowWeatherInfo();
                    return;
                case 0:
                    //MainActivity.ShowMessageInAPP(paramAnonymousMessage.obj.toString());
                    return;
                case 2:
                    //MainActivity.ShowMessageInAPP(paramAnonymousMessage.obj.toString());
                    return;
                case 3:
                    String[] arrayOfString = paramAnonymousMessage.obj.toString().split("||");
                   // Log.i("接收到消息", "下一步写入文件");
                    MainActivity.WRITE_XML(arrayOfString[0], arrayOfString[1]);
                    return;
                case 4:
                    break;
                case 5:
                    //重新定位
                    //ShowMessageInAPP("重新定位中。。。");
                    break;
            }
           // Log.i("主线程收到GPS信息：", paramAnonymousMessage.obj.toString());
            //MainActivity.ShowMessageInAPP(paramAnonymousMessage.obj.toString());
        }
    };
    /**
     * 查询天气按键监听
     */
    private View.OnClickListener weatherSearchbtnClick = new View.OnClickListener()
    {
        public void onClick(View paramAnonymousView)
        {
            if (! ValuesStatic.searchText.getText().equals("")) {
                new Thread (){
                    public void run (){
                        /**
                         * 读取文件的方法，注意assets文件夹应该和res在同一级目录下
                         */
                        //MainActivity.this.getAssets().open("CityXML/SupportCity.xml")
                        Log.i ("城市名称：",ValuesStatic.searchText.getText ().toString ());
                            getWeather(ValuesStatic.searchText.getText().toString(),"国内",mContext);
                            return;
                    }
                }.start ();
            }
            else
            {
                ShowMessageInAPP ("欧亚，忘记输入城市了。。");
            }
        }
    };

	/**
     * 获取天气
     * @param cityName 城市名称
     * @param Where 城市在国内还是国外
     * @param context 主线程的Context
     * @return 是否成功获取
     */
    public boolean getWeather(final String cityName,final String Where,final Context context)
    {
        new Thread(

        ){
            @Override
            public void run(){
                try {
                    Help.getWeatherPro(context.getAssets().open(ValuesStatic.SupportCityFilePath),cityName,Where);
                    Help.getWeatherFuture(context.getAssets().open(ValuesStatic.SupportCityFilePath),cityName,Where,context);
                    return;
                } catch ( IOException e ) {
                    e.printStackTrace();
                }

                return;
            }
        }.start();
		/**
         * 异步方法获得背景图片并且设置
         */
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... params){
                /**
                 * 获取天气城市对应背景
                 */
                ValuesStatic.CityImageXLSRes = EXCELOpreate.getCityID_Descrip(cityName,context);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                /**
                 *设置背景图片
                 */
                setImageandAbout(ValuesStatic.CityImageXLSRes);
            }
        }.execute();
        return true;

    }

    /**
     * 设置背景图片
     * @param Res
	 */
    public void setImageandAbout(String Res){
        setXLSvalues(Res);
        int ImageRid = mContext.getResources().getIdentifier("bgimage_"+ValuesStatic.XLS_tID,"drawable",mContext.getPackageName());
        if(ImageRid!=0) {
            ValuesStatic.weatherProLinearLayout.setBackgroundResource(ImageRid);
        }
        else
        {
            ValuesStatic.weatherProLinearLayout.setBackgroundResource(R.drawable.mian_back_color);
        }
    }
	/**
     * 设置表中获得的数据到values静态类中
     * @param Res
     */
    public void setXLSvalues(String Res)
    {
        String[] s = Res.split("#");
        for(int i = 0;i<s.length;i++)
        {
            String[] t = s[i].split("=");
            switch ( t[0] )
            {
                case "tID"://地区ID(用于设置背景)
                    ValuesStatic.XLS_tID = t[1].replace(".0","");
                    break;
                case "Area"://地区名称
                    ValuesStatic.XLS_Area = t[1];
                    break;
                case "AboutCity"://关于城市
                    ValuesStatic.XLS_AboutCity = t[1];
                    break;
                case "ClimateBackground"://气候背景
                    if(t.length>1)
                    {
                        ValuesStatic.XLS_ClimateBackground = t[1];
                    }
                    else
                    {
                        ValuesStatic.XLS_ClimateBackground = "";
                    }
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 设置组件引用
     * @return
     */
    public boolean setThingsValue ( )
    {
        boolean flag = false;
            mAct = this;
            mContext = this.getApplicationContext ();
            ValuesStatic.weatherProLinearLayout = ( LinearLayout )findViewById(R.id.weatherProLinearLayout);
            ValuesStatic.city = ( EditText ) findViewById(R.id.city);
            ValuesStatic.curTemper = (EditText)findViewById ( R.id.CurTemper );
            ValuesStatic.Humi = (EditText)findViewById ( R.id.Humi );
            ValuesStatic.lastInitTime = (EditText)findViewById ( R.id.LastInitTime );
            ValuesStatic.Temper = (EditText)findViewById ( R.id.Temper);
            ValuesStatic.weather = (EditText)findViewById ( R.id.weather );
            ValuesStatic.wind = (EditText)findViewById ( R.id.Wind );
            ValuesStatic.windp = (EditText)findViewById ( R.id.Windp );
            ValuesStatic.searchText = (EditText )findViewById ( R.id.SearchCity );
            ValuesStatic.ShowGPS = ( TextView )findViewById(R.id.ShowGPS);
            ValuesStatic.searchBtn = (Button)findViewById ( R.id.SerachBtn );
            ValuesStatic.QR_View = ( ImageView )findViewById(R.id.imageView);
            /**
             * 设置按键监听
             */
            ValuesStatic.searchBtn.setOnClickListener ( weatherSearchbtnClick );

        return flag;
    }

    /**
     * 把字符串写入文件
     * @param FilePath 文件路径
     * @param InStr 写入文件的字符串
     * @return
	 */
    private static Boolean WRITE_XML(String FilePath, String InStr)
    {
        return new FileStore (mContext).WriteFileToInternal(FilePath, InStr, "UTF-8");
    }

	/**
     * 显示提示消息
     * @param Message
     */
    public static void ShowMessageInAPP(String Message)
    {
        Toast.makeText(mContext, Message, Toast.LENGTH_SHORT).show();
    }

    public static void ShowWeatherInfo()
    {
        showWeatherPro();
    }

	/**
     * 显示实况天气情况
     */
    private static void showWeatherPro()
    {
        ValuesStatic.city.setText(weatherInfoStatic.weatherInfoPro.get_citynm());
        ValuesStatic.weather.setText( weatherInfoStatic.weatherInfoPro.get_weather());
        ValuesStatic.curTemper.setText(weatherInfoStatic.weatherInfoPro.get_temperature_curr());
        ValuesStatic.Temper.setText(weatherInfoStatic.weatherInfoPro.get_temperature());
        ValuesStatic.Humi.setText(weatherInfoStatic.weatherInfoPro.get_humidity());
        ValuesStatic.wind.setText(weatherInfoStatic.weatherInfoPro.get_winp());
        ValuesStatic.windp.setText(weatherInfoStatic.weatherInfoPro.get_wind());
        ValuesStatic.lastInitTime.setText(weatherInfoStatic.weatherInfoPro.get_days() + "  "
                + weatherInfoStatic.weatherInfoPro.get_week());
    }

    public Boolean getGPSLocation(AMapLocationClientOption.AMapLocationMode paramAMapLocationMode)
    {
        Boolean localBoolean = Boolean.valueOf(false);
        ValuesStatic.dialog = new ProgressDialog(this);
        ValuesStatic.dialog.setTitle("请稍等...");
        ValuesStatic.dialog.setMessage("正在定位...");
        ValuesStatic.mLocationClient = new AMapLocationClient(getApplicationContext());
        ValuesStatic.GpsLocation = new GPSLocation_LBSMAP(getApplicationContext(), ValuesStatic.mLocationClient,this);

        /**
         * 匿名异步方法获得定位信息
         */
        new AsyncTask<AMapLocationClientOption.AMapLocationMode,String,String>()
        {
            protected String doInBackground(AMapLocationClientOption.AMapLocationMode[] paramAnonymousArrayOfAMapLocationMode)
            {
                return ValuesStatic.GpsLocation.getGPSLocation(paramAnonymousArrayOfAMapLocationMode[0]);
            }

            protected void onPostExecute(String paramAnonymousString)
            {
                if(paramAnonymousString.indexOf("哎呀")==-1) {//定位信息获取成功
                    /**
                     * 开始获取一次按照定位的天气预报
                     */
                    getWeather(GPSInfo.district.toString(),"国内",mContext);
                }
                else
                {
                    //获取天气信息失败

                    ShowMessageInAPP("欧亚，信息获取失败\n失败原因"+paramAnonymousString.split("失败原因")[1].toString()+"请检查后重试。。");
                    paramAnonymousString = paramAnonymousString.split("失败原因")[0];
                }
                /**
                 * 显示GPS信息
                 */
                ValuesStatic.ShowGPS.setText(paramAnonymousString);
                ValuesStatic.dialog.dismiss();
                super.onPostExecute(paramAnonymousString);
            }

            protected void onPreExecute()
            {
                ValuesStatic.dialog.show();
                super.onPreExecute();
            }
        }.execute(paramAMapLocationMode);
        return localBoolean;
    }

    /**
     * 权限请求回调方法
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       // Log.i("回调中：","333333333");
        switch (requestCode)
        {
            case PremissionEnum.GPS_PREMISSION_CODE://GPS权限申请成功
                ShowMessageInAPP("权限获取成功！");
                getGPSLocation(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                break;
            default:
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
    }


    /**
     * 创建了之后的更新方法
     * @param savedInstanceState 选项号码
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
		/**
         * 设置所有控件
         */
        setThingsValue ();
		/**
         * 获取定位信息
         */
        getGPSLocation(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

		/**
         * 邮箱链接
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "链接到开发者邮箱", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Help.sendEmailToDeveleper(mAct);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
		/**
         * 获得二维码或者头像图片
         */
        TextView t = (TextView)findViewById(R.id.UNAME);
		/**
         * 用户名字
         */
        String UNAME = t.getText().toString();
		/**
         * 获取头像
         */
        getImage(UNAME,"");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	/**
     * 选项变化调用
     * @param item 选项
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.i("选项点击：选项是",id+"");
        if (id == R.id.nav_camera) {
            // Handle the camera action
			/**
			 * 替换界面为实况天气
             */
            Log.i("按下我的天气","我的天气: ");
        getSupportFragmentManager().beginTransaction().replace(R.id.GPSinfoLayout,new MainActivityFragment())
                .commit();
        } else if (id == R.id.nav_gallery) {
            /**
             * 替换界面为未来天气
             */

        } else if (id == R.id.nav_slideshow) {
            //替换界面为我的位置信息
            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new GPS_Frament()).commit();

        } else if (id == R.id.nav_manage) {
            ShowMessageInAPP("按下管理");

        } else if (id == R.id.nav_share) {
            ShowMessageInAPP("按下分享");

        } else if (id == R.id.nav_send) {
            ShowMessageInAPP("按下发送");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

	/**
     * 获得图片
     * @param imageName 图片名称
     * @param url 图片网址
     */
    public void getImage(String imageName,String url)
    {
        if(url.equals("")) {
            Log.i("获取图片",imageName);
            /**
             * 设置头像或者二维码
             */
            new AsyncTask< String,Integer,Bitmap >(){
                @Override
                protected Bitmap doInBackground(String... params){
                    return Help.getQR(params[0]);
                }

                protected void onPostExecute(Bitmap bitmap){
                    /**
                     * 头像
                     */
                    if(bitmap!=null) {
                        ValuesStatic.QR_View.setImageBitmap(bitmap);
                    }
                }

                protected void onPreExecute(){
                    //View v = Inflater.inflate(R.layout.nav_header_main,mContext,false);
                    ValuesStatic.QR_View = ( ImageView )findViewById(R.id.imageView);
                }
            }.execute(imageName);
        }
        else
        {
            new AsyncTask< String,Integer,Bitmap >()
            {
                @Override
                protected Bitmap doInBackground(String... params){
                    try {

                        String url = params[0];
                        URL res = new URL(url);
                        Log.i("获取图片中：",url);

                        HttpURLConnection conn = (HttpURLConnection)res.openConnection();
                        // 设置通用的请求属性
                        conn.setRequestProperty("accept", "*/*");
                        conn.setRequestProperty("connection", "Keep-Alive");
                        conn.setRequestProperty("user-agent",
                                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

                        // 建立实际的连接
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        Log.i("获取图片中：","成功获取！");
                        conn.disconnect();
                        return bitmap;
                    } catch ( MalformedURLException e ) {
                        Log.i("获取图片中错误：",e.getMessage());
                        e.printStackTrace();
                    } catch ( IOException e ) {
                        Log.i("获取图片中错误：",e.getMessage());
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap){
                    /**
                     * 头像
                     */
                    if(bitmap!=null) {
                        ValuesStatic.QR_View.setImageBitmap(bitmap);
                    }
                }

                @Override
                protected void onPreExecute(){
                    ValuesStatic.QR_View = ( ImageView )findViewById(R.id.imageView);
                }
            }.execute(url);
        }
    }

}
