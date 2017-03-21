package help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import UrlInfo.HttpRequest;
import WeatherData.GPSInfo;
import WeatherData.weatherInfoFuture;
import WeatherData.weatherInfoPro;
import WeatherData.weatherInfoStatic;
import getMainThings.ValuesStatic;
import smilesb101.zxb.smilesb101.winterweather.MainActivity;
import Enum.*;

/**
 * Created by Administrator on 2016/6/23.
 */
public class Help {
	private static final String Appkey = "19550";
	private static final String Sign = "4548558112fabc3d4ffcc82e89db8e9d";
	private static final String futureWeatherFilePath = "futureWeatherXML.xml";
	private static String reString = "";

	public static String getWeatherFuture( InputStream SupportCityStream, String City, String Where, final Context context)
	{
		reString = "";
		final String str = getWeatherIDByName(SupportCityStream, City, "citynm", Where, "weaid");
		if (!str.equals("")) {
			if(!str.equals(ValuesStatic.ERROR)) {
				try {
					String Res = HttpRequest.sendGet("http://api.k780.com:88/?app=weather.future&weaid="
									+ str + "&&appkey=" + Appkey + "&sign=" + Sign + "&format=json"
							,"");
					JSONObject localJSONObject1 = new JSONObject(Res);
					if( ! localJSONObject1.getString("success").toString().equals("0") ) {
						reString = "天气获取成功，";
						JSONArray localJSONArray = new JSONArray(localJSONObject1.getString("result"));
						weatherInfoStatic.WeatherInfoFutureList = new ArrayList();
						for( int i = 0; i < localJSONArray.length(); i++ ) {
							JSONObject localJSONObject2 = new JSONObject(localJSONArray.getString(i));
							weatherInfoFuture localweatherInfoFuture;
							localweatherInfoFuture = new weatherInfoFuture(localJSONObject2.getString("weaid"),
									localJSONObject2.getString("days"),localJSONObject2.getString("week"),
									localJSONObject2.getString("cityno"),localJSONObject2.getString("citynm"),
									localJSONObject2.getString("cityid"),localJSONObject2.getString("temperature"),
									localJSONObject2.getString("humidity"),localJSONObject2.getString("weather"),
									localJSONObject2.getString("weather_icon"),localJSONObject2.getString("weather_icon1"),
									localJSONObject2.getString("wind"),localJSONObject2.getString("winp"),
									localJSONObject2.getString("temp_high"),localJSONObject2.getString("temp_low"),
									localJSONObject2.getString("humi_high"),localJSONObject2.getString("humi_low"),
									localJSONObject2.getString("weatid"),localJSONObject2.getString("weatid1"),
									localJSONObject2.getString("windid"),localJSONObject2.getString("winpid"));
							weatherInfoStatic.WeatherInfoFutureList.add(localweatherInfoFuture);
						}

						FileStore localFileStore = new FileStore(context);
						if( localFileStore.WriteFileToInternal("futureWeatherXML.xml",Res,"UTF-8") ) {
							Help.reString += "文件写入成功！";
						} else {
							Help.reString += "文件写入失败！";
						}

						MainActivity.mHandler.obtainMessage(MessageEnum.MSG_WEATHER_SUCCESS,reString).sendToTarget();
						return reString;
					} else {
						Help.reString = "哎呀！未来天气情况获取失败，请稍后重试。。";
						MainActivity.mHandler.obtainMessage(MessageEnum.MSG_WEATHER_FAIL,Help.reString).sendToTarget();
					}

				} catch ( JSONException localJSONException ) {
					reString += "哎呀！未来天气情况获取失败，请稍后重试。。";
					Log.i("未来天气信息获取类中错误：",localJSONException.getMessage());
					MainActivity.mHandler.obtainMessage(0,Help.reString).sendToTarget();
					localJSONException.printStackTrace();
					return reString;
				}
			}
			else
			{
				reString = "哎呀，获取天气信息失败，请检查网络状况后再试。。。";
				MainActivity.mHandler.obtainMessage(MessageEnum.MSG_WEATHER_FAIL, reString).sendToTarget();
				return reString;
			}
		}
		else
		{
			reString += "哎呀！抱歉！暂不支持此城市，我们会很快增加支持。。";
			MainActivity.mHandler.obtainMessage(0, reString).sendToTarget();
			return reString;
		}
		return reString;
	}

	/**
	 * 获得城市ID
	 * @param SupportCityStream 支持城市流
	 * @param City 城市
	 * @param CityNode 城市节点名称
	 * @param Where 哪里（国内还是国外）
	 * @param FindNode 查找的节点
	 * @return 城市ID
	 */
	private static String getWeatherIDByName(InputStream SupportCityStream, String City,
											 String CityNode, String Where, String FindNode){
		if( Where.equals ("国内") ) {
			Where = "ChinaCity";
		} else if( Where.equals ("国外") ) {
			Where = "ForginCity";
		}
		try {
			DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = localDocumentBuilderFactory.newDocumentBuilder();
			Document document = builder.parse(SupportCityStream);
			Element localElement1 = document.getDocumentElement();
			NodeList localNodeList1 = localElement1.getElementsByTagName(Where);
			localElement1 = ( Element )localNodeList1.item(0);
			localNodeList1 = localElement1.getElementsByTagName("City");
			//NodeList localNodeList2 = ((Element )localNodeList1.item(0)).getElementsByTagName("City");
			for( int i = 0; i < localNodeList1.getLength(); i++ ) {
				Element localElement2 = ( Element )localNodeList1.item(i);
				//Log.i("XML文件读取中：",CityNode);
				if( (( Element )localElement2.getElementsByTagName(CityNode).item(0)).getTextContent().equals(City) ) {
					//Log.i("次节点：",localElement2.getElementsByTagName(FindNode).item(0).getTextContent());
					String str = localElement2.getElementsByTagName(FindNode).item(0).getTextContent();
					return str;
				}
			}
		} catch ( ParserConfigurationException localParserConfigurationException ) {
			Log.i ("错误：",localParserConfigurationException.getMessage ());
			localParserConfigurationException.printStackTrace ();
			return "";
		} catch ( SAXException localSAXException ) {
			Log.i ("错误：",localSAXException.getMessage ());
			localSAXException.printStackTrace ();
			return "";
		} catch ( IOException localIOException ) {
			Log.i ("错误：",localIOException.getMessage ());
			localIOException.printStackTrace ();
			return "";
		}
		return "";
	}

	public static String getWeatherPro(InputStream SupportCityStream, String City, String Where)
	{
		reString = "";
		String str1 = getWeatherIDByName(SupportCityStream, City, "citynm", Where, "weaid");

		String str2;
		if (!str1.equals(""))
		{
			if(!str1.equals(ValuesStatic.ERROR)) {
				str2 = HttpRequest.sendGet("http://api.k780.com:88/?app=weather.today&weaid=" +
						str1 + "&&appkey=" + "19550" + "&sign=" +
						"4548558112fabc3d4ffcc82e89db8e9d" + "&format=json","");

				try {
					JSONObject localJSONObject1 = new JSONObject(str2);
					if( localJSONObject1.getString("success").toString().equals("1") ) {
						String str3 = localJSONObject1.getString("result");
						JSONObject localJSONObject2 = new JSONObject(str3);
						weatherInfoStatic.weatherInfoPro = new weatherInfoPro(localJSONObject2.getString("weaid"),localJSONObject2.getString("days"),localJSONObject2.getString("week"),localJSONObject2.getString("cityno"),localJSONObject2.getString("citynm"),localJSONObject2.getString("cityid"),localJSONObject2.getString("temperature"),localJSONObject2.getString("humidity"),localJSONObject2.getString("weather"),localJSONObject2.getString("weather_icon"),localJSONObject2.getString("weather_icon1"),localJSONObject2.getString("wind"),localJSONObject2.getString("winp"),localJSONObject2.getString("temp_high"),localJSONObject2.getString("temp_low"),localJSONObject2.getString("humi_high"),localJSONObject2.getString("humi_low"),localJSONObject2.getString("weatid"),localJSONObject2.getString("weatid1"),localJSONObject2.getString("windid"),localJSONObject2.getString("winpid"),localJSONObject2.getString("temperature_curr"));
						reString += "实况天气信息获取成功！";
						MainActivity.mHandler.obtainMessage(MessageEnum.MSG_WEATHER_SUCCESS,reString).sendToTarget();
						return reString;
					}
					reString += "哎呀！实况天气情况获取失败，请稍后重试。。";
					MainActivity.mHandler.obtainMessage(MessageEnum.MSG_WEATHER_FAIL,reString).sendToTarget();
					return reString;
				} catch ( JSONException localJSONException ) {
					reString += "哎呀！实况天气情况获取失败，请稍后重试。。";
					Log.i("实况天气信息获取类中错误：",localJSONException.getMessage());
					MainActivity.mHandler.obtainMessage(MessageEnum.MSG_WEATHER_FAIL,reString).sendToTarget();
					localJSONException.printStackTrace();
					return reString;
				}
			}
			else
			{
				reString = "哎呀，获取天气信息失败，请检查网络状况后再试。。。";
				MainActivity.mHandler.obtainMessage(MessageEnum.MSG_WEATHER_FAIL, reString).sendToTarget();
				return reString;
			}
		}
		else
		{
			reString += "哎呀！抱歉！暂不支持此城市，我们会很快增加支持。。";
			MainActivity.mHandler.obtainMessage(MessageEnum.MSG_WEATHER_FAIL, reString).sendToTarget();
			return reString;
		}
	}

	/**
	 * 发送消息到开发者邮箱
	 * @param act 主活动
	 * @return
	 */
	public static boolean sendEmailToDeveleper(Activity act)
{
	Intent intent= new Intent();
	intent.setAction("android.intent.action.VIEW");
	Uri content_url = Uri.parse("https://mail.qq.com");
	intent.setData(content_url);
	act.startActivity(intent);
	return true;
}

	/**
	 * 获得用户对应二维码
	 * @param QRName 二维码名称
	 * @return 图片文件
	 */
	public static Bitmap getQR(String QRName)
	{
		try {

			String url = "http://api.k780.com:88/?app=qr.get&data="+QRName+"&level=L&size=6";
			URL res = new URL(url);

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


}
