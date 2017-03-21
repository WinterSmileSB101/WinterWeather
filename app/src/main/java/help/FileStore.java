package help;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2016/6/23.
 */
public class FileStore {
	private Context context;

	public FileStore(Context paramContext)
	{
		this.context = paramContext;
	}

	/**
	 * 写入内部文件
	 * @param FilePath 文件路径
	 * @param InStr 写入文件的字符串
	 * @param Charest 编码方式
	 * @return
	 */
	public Boolean WriteFileToInternal(String FilePath, String InStr, String Charest)
	{
		Boolean localBoolean = false;
		//Log.i("写入文件", InStr);
		try
		{
			FileOutputStream localFileOutputStream = context.openFileOutput ( FilePath,0 );
			OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter ( localFileOutputStream );
			localOutputStreamWriter.write(InStr);
			localOutputStreamWriter.flush();
			localFileOutputStream.flush();
			localOutputStreamWriter.close();
			localFileOutputStream.close();
			localBoolean = true;
			//Log.i("写入文件成功", InStr);
			return localBoolean;
		}
		catch (FileNotFoundException localFileNotFoundException)
		{
			Log.i("写入文件", localFileNotFoundException.getMessage());
			localFileNotFoundException.printStackTrace();
			return localBoolean;
		}
		catch (UnsupportedEncodingException localUnsupportedEncodingException)
		{
			Log.i("写入文件", localUnsupportedEncodingException.getMessage());
			localUnsupportedEncodingException.printStackTrace();
			return localBoolean;
		}
		catch (IOException localIOException)
		{
			Log.i("写入文件", localIOException.getMessage());
			localIOException.printStackTrace();
		}
		return localBoolean;
	}
}
