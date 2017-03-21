package help;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import Enum.*;
import WeatherData.GPSInfo;
import getMainThings.ValuesStatic;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/23.
 */
public class GPSLocation_LBSMAP extends AppCompatActivity{
	public AMapLocation aMapLocation;
	private Context mContext;
	public AMapLocationClientOption mLocationOption = null;
	String resString = "";
	/**
	 * 主活动
	 */
	private Activity MainActivity;
	public AMapLocationClient mLocationClient = null;
	public AMapLocationListener mLocationListener = new AMapLocationListener()
	{
		@Override
		public void onLocationChanged(AMapLocation amapLocation)
		{
			if (amapLocation != null) {
				if (amapLocation.getErrorCode() == 0) {
					//定位成功回调信息，设置相关消息
					//存储GPS信息
					storeGPSinfo(amapLocation);
					amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
					amapLocation.getAccuracy();//获取精度信息
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(amapLocation.getTime());
					df.format(date);//定位时间
					resString = amapLocation.toString();
				} else {
					//显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
					GPSLocation_LBSMAP.this.resString = "哎呀,获取定位信息失败，请稍后再试。。\n失败原因："+amapLocation.getErrorInfo();
					Log.e("定位错误：", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
					return ;
				}
				}
			}
	};


	public GPSLocation_LBSMAP(Context Context, AMapLocationClient AMapLocationClient,Activity activity)
	{
		this.mContext = Context;
		this.mLocationClient = AMapLocationClient;
		this.MainActivity = activity;
	}

	public String getGPSLocation(AMapLocationClientOption.AMapLocationMode paramAMapLocationMode)
	{
		/**
		 * 检查定位权限
		 */
		if(checkGPSPermission()) {
			//初始化定位
			mLocationClient = new AMapLocationClient(mContext);
//设置定位回调监听
			mLocationClient.setLocationListener(mLocationListener);
			//初始化定位参数
			mLocationOption = new AMapLocationClientOption();
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
			mLocationOption.setLocationMode(paramAMapLocationMode);
//设置是否返回地址信息（默认返回地址信息）
			mLocationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false
			mLocationOption.setOnceLocation(false);
//设置是否强制刷新WIFI，默认为强制刷新
			mLocationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置
			mLocationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms
			mLocationOption.setInterval(2000);

//给定位客户端对象设置定位参数
			mLocationClient.setLocationOption(mLocationOption);
//启动定位
			mLocationClient.startLocation();
			while( true ) {
				if(!resString.equals("")) {
						stopGetLocation();
						String str = this.resString;
						return str;
				}
			}
		}
		else
		{
			return "欧亚，没有定位权限，无法进行定位..";
		}
	}

	public void stopGetLocation()
	{
		this.mLocationClient.stopLocation();
	}

	/**
	 * 检查定位授权
	 */
	public boolean checkGPSPermission()
	{
		//SDK在Android 6.0下需要进行运行检测的权限如下：
		     /*  Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.READ_PHONE_STATE
*/
        //这里以ACCESS_COARSE_LOCATION为例
		if ( (ContextCompat.checkSelfPermission(MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED)||
				(ContextCompat.checkSelfPermission(MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
						!= PackageManager.PERMISSION_GRANTED)||
				(ContextCompat.checkSelfPermission(MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
				(ContextCompat.checkSelfPermission(MainActivity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)||
				(ContextCompat.checkSelfPermission(MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
			//申请WRITE_EXTERNAL_STORAGE权限
			ActivityCompat.requestPermissions(MainActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.READ_PHONE_STATE},PremissionEnum.GPS_PREMISSION_CODE);//自定义的code
		}
		else {
			//Log.i("权限检查：","true");
			return true;
		}
		//Log.i("权限检查：","false");
		return false;
	}

	/**
	 * 储存GPS定位信息
	 * @param amapLocation 信息
	 * @return
	 */
	public static boolean storeGPSinfo(AMapLocation amapLocation)
	{
		GPSInfo.latitude = amapLocation.getLatitude()+"";//获取纬度
		GPSInfo.longitude = amapLocation.getLongitude()+"";//获取经度
		GPSInfo.province = amapLocation.getProvince();//省信息
		GPSInfo.city = amapLocation.getCity();//城市信息
		GPSInfo.district = amapLocation.getDistrict();//城区信息
		GPSInfo.cityCode = amapLocation.getCityCode();//城市编码
		GPSInfo.adCode = amapLocation.getAdCode();//地区编码
		GPSInfo.address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
		GPSInfo.country = amapLocation.getCountry();//国家信息
		GPSInfo.road = amapLocation.getRoad();
		GPSInfo.poiName = amapLocation.getPoiName();
		GPSInfo.street = amapLocation.getStreet();//街道信息
		GPSInfo.streetNum = amapLocation.getStreetNum();//街道门牌号信息
		GPSInfo.aoiName = amapLocation.getAoiName();//获取当前定位点的AOI信息
		return true;
	}

}
