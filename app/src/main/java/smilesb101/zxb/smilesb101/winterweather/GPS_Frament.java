package smilesb101.zxb.smilesb101.winterweather;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Enum.*;
import WeatherData.GPSInfo;
import getMainThings.ValuesStatic;
import help.GPSLocation_LBSMAP;
import help.Help;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

/**
 * Created by Administrator on 2016/6/27.
 */
public class GPS_Frament extends Fragment{

	public TextView address;
	public TextView latitude;
	public TextView longitude;
	public TextView country;
	public TextView province;
	public TextView city;
	public TextView cityCode;
	public TextView district;
	public TextView districtCode;
	public TextView street;
	public Button regetGPS;
	public View mv;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
		//Log.i("选项：",savedInstanceState.toString());
		View v = inflater.inflate(R.layout.my_gpsinfo_layout,container,false);
		mv = v;
		setVaule();
		showGPS();
		return v;
	}

	public boolean setVaule()
	{
		address = ( TextView )mv.findViewById(R.id.MyLocation);
		latitude = ( TextView )mv.findViewById(R.id.Mylatitude);
		longitude = ( TextView )mv.findViewById(R.id.Mylongitude);
		country = ( TextView )mv.findViewById(R.id.MyCountry);
		province = ( TextView )mv.findViewById(R.id.MyProvince);
		city = ( TextView )mv.findViewById(R.id.MyCity);
		cityCode = ( TextView )mv.findViewById(R.id.MyCityCode);
		district = ( TextView )mv.findViewById(R.id.MyDistrict);
		districtCode = ( TextView )mv.findViewById(R.id.MyAdCode);
		street = ( TextView )mv.findViewById(R.id.MyStreet);
		regetGPS = ( Button )mv.findViewById(R.id.RegetGPS);
		//添加按键监听

		regetGPS.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				/**
				 * 重新定位
				 */
				Log.i("按下重新定位","onClick: ");
				getGPSLocation(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			}
		});
		return true;
	}
	public boolean showGPS()
	{
		address.setText(GPSInfo.address);
		latitude.setText(GPSInfo.latitude);
		longitude.setText(GPSInfo.longitude);
		country.setText(GPSInfo.country);
		province.setText(GPSInfo.province);
		city.setText(GPSInfo.city);
		cityCode.setText(GPSInfo.cityCode);
		district.setText(GPSInfo.district);
		districtCode.setText(GPSInfo.adCode);
		street.setText(GPSInfo.street+" "+GPSInfo.streetNum);
		return true;
	}

	public Boolean getGPSLocation(AMapLocationClientOption.AMapLocationMode paramAMapLocationMode)
	{
		Log.i("按下重新定位","onClick: ");
		Boolean localBoolean = Boolean.valueOf(false);
		ValuesStatic.dialog = new ProgressDialog(MainActivity.mAct);
		ValuesStatic.dialog.setTitle("请稍等...");
		ValuesStatic.dialog.setMessage("正在定位...");
		ValuesStatic.mLocationClient = new AMapLocationClient(MainActivity.mContext);
		ValuesStatic.GpsLocation = new GPSLocation_LBSMAP(MainActivity.mContext, ValuesStatic.mLocationClient,MainActivity.mAct);

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
					//getWeather(GPSInfo.district.toString(),"国内",mContext);
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
				showGPS();
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
	 * 显示提示消息
	 * @param Message
	 */
	public static void ShowMessageInAPP(String Message)
	{
		Toast.makeText(MainActivity.mContext, Message, Toast.LENGTH_SHORT).show();
	}


}
