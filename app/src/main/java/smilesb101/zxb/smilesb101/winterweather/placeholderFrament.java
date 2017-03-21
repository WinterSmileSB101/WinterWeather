package smilesb101.zxb.smilesb101.winterweather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/6/26.
 */
public class placeholderFrament extends Fragment{

	public placeholderFrament()
	{

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
		//Log.i("选项：",savedInstanceState.toString());
		View v = inflater.inflate(R.layout.my_gpsinfo_layout,container,false);
		return v;
	}

	public ImageView getImageView()
	{
		return getImageView();
	}

}
