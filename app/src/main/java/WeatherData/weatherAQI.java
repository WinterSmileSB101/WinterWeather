package WeatherData;

public class weatherAQI
{
	private String _aqi;
	private String _aqi_levid;
	private String _aqi_levnm;
	private String _aqi_remark;
	private String _aqi_scope;
	private String _cityid;
	private String _citynm;
	private String _cityno;
	private String _weaid;

	public weatherAQI()
	{
	}

	public weatherAQI(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9)
	{
		this._weaid = paramString1;
		this._cityno = paramString2;
		this._citynm = paramString3;
		this._cityid = paramString4;
		this._aqi = paramString5;
		this._aqi_scope = paramString6;
		this._aqi_levid = paramString7;
		this._aqi_levnm = paramString8;
		this._aqi_remark = paramString9;
	}

	public String get_aqi()
	{
		return this._aqi;
	}

	public String get_aqi_levid()
	{
		return this._aqi_levid;
	}

	public String get_aqi_levnm()
	{
		return this._aqi_levnm;
	}

	public String get_aqi_remark()
	{
		return this._aqi_remark;
	}

	public String get_aqi_scope()
	{
		return this._aqi_scope;
	}

	public String get_cityid()
	{
		return this._cityid;
	}

	public String get_citynm()
	{
		return this._citynm;
	}

	public String get_cityno()
	{
		return this._cityno;
	}

	public String get_weaid()
	{
		return this._weaid;
	}

	public void set_aqi(String paramString)
	{
		this._aqi = paramString;
	}

	public void set_aqi_levid(String paramString)
	{
		this._aqi_levid = paramString;
	}

	public void set_aqi_levnm(String paramString)
	{
		this._aqi_levnm = paramString;
	}

	public void set_aqi_remark(String paramString)
	{
		this._aqi_remark = paramString;
	}

	public void set_aqi_scope(String paramString)
	{
		this._aqi_scope = paramString;
	}

	public void set_cityid(String paramString)
	{
		this._cityid = paramString;
	}

	public void set_citynm(String paramString)
	{
		this._citynm = paramString;
	}

	public void set_cityno(String paramString)
	{
		this._cityno = paramString;
	}

	public void set_weaid(String paramString)
	{
		this._weaid = paramString;
	}
}
