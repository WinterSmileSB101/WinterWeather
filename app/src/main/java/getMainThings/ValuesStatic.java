package getMainThings;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;

import help.GPSLocation_LBSMAP;

/**
 * 获取Mian中控件,变量静态类
 * Created by SmileSB101 on 2016/6/23.
 */
public class ValuesStatic{

	/**
	 * 天气所有城市
	 */
	public static String AllCityFilePath = "CityXML/AllCity.xml";

	/**
	 * 天气支持城市
	 */
	public static String SupportCityFilePath = "CityXML/SupportCity.xml";
	/**
	 * 支持城市所获得的信息
	 */
	public static String CityImageXLSRes = "";
	/**
	 * 表中城市名称
	 */
	public static String XLS_Area;
	/**
	 * 表中城市ID信息（用于设置图片）
	 */
	public static String XLS_tID;
	/**
	 * 表中关于城市信息
	 */
	public static String XLS_AboutCity;
	/**
	 * 表中的城市背景信息
	 */
	public static String XLS_ClimateBackground;
	/**
	 * 天气背景图片XLS文件路径
	 */
	public static String CityImageXLSFilePath = "weatherCityXLS/AboutCity.xls";

	/**
	 * 天气背景图片文件夹
	 */
	public static String CityImageFilePath = "drawable/weatherImage/";
	/**
	 * 搜索按钮
	 */
	public static Button searchBtn;
	/**
	 * 搜索天气名称
	 */
	public static EditText searchText;
	/**
	 * 天气城市名称
	 */
	public static EditText city;

	/**
	 *实况天气信息
	 */
	public static EditText weather;
	/**
	 * 实况温度
	 */
	public static EditText curTemper;

	/**
	 * 温度
	 */
	public static EditText Temper;
	/**
	 * 湿度
	 */
	public static EditText Humi;
	/**
	 * 风力
	 */
	public static EditText wind;
	/**
	 * 风向
	 */
	public static EditText windp;
	/**
	 * 最后更新时间
	 */
	public static EditText lastInitTime;

	/**
	 * 进度提示框
	 */
	public static AlertDialog dialog;
	/**
	 * 定位客户端
	 */
	public static AMapLocationClient mLocationClient = null;
	/**
	 * 定位类实例化对象
	 */
	public static GPSLocation_LBSMAP GpsLocation = null;

	/**
	 * 定位信息显示控件
	 */
	public static TextView ShowGPS;

	/**
	 * 头像/二维码显示控件
	 */
	public static ImageView QR_View;

	/**
	 * ERROR错误提示语
	 */
	public static String ERROR = "ERROR";
	/**
	 * 实况天气主布局
	 */
	public static LinearLayout weatherProLinearLayout;
}
