package Enum;

/**
 *
 * 项目名称：WinterWeather
 * 类名称：MessageEnum
 * 类描述：   消息提示信息枚举类
 * 创建人：SmileSB101
 * 创建时间：2016年6月22日 下午11:30:04
 * 修改人：SmileSB101
 * 修改时间：2016年6月22日 下午11:30:04
 * 修改备注：
 * @version   1.0
 *
 */
public class MessageEnum {
	/**
	 * 直接显示消息
	 */
	public static final int MSG_DIRECT_MESSAGE = 2;
	/**
	 * GPS信息
	 */
	public static final int MSG_GPSLOCATION = 4;
	/**
	 * 获取天气信息失败信息
	 */
	public static final int MSG_WEATHER_FAIL = 0;
	/**
	 * 获取天气信息成功信息
	 */
	public static final int MSG_WEATHER_SUCCESS = 1;
	/**
	 * 写入文件信息
	 */
	public static final int MSG_WRITETOXMLFILE = 3;
	/**
	 * 重新获取定位信息
	 */
	public static final int MSG_REGETGPS = 5;
}
