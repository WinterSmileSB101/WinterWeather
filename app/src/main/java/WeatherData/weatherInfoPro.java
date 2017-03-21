package WeatherData;

public class weatherInfoPro extends weatherInfoFuture
{
	private String _temperature_curr;

	public weatherInfoPro()
	{
	}

	public weatherInfoPro(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13, String paramString14, String paramString15, String paramString16, String paramString17, String paramString18, String paramString19, String paramString20, String paramString21, String paramString22)
	{
		super(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9, paramString10, paramString11, paramString12, paramString13, paramString14, paramString15, paramString16, paramString17, paramString18, paramString19, paramString20, paramString21);
		this._temperature_curr = paramString22;
	}

	public String get_temperature_curr()
	{
		return this._temperature_curr;
	}

	public void set_temperature_curr(String paramString)
	{
		this._temperature_curr = paramString;
	}
}
