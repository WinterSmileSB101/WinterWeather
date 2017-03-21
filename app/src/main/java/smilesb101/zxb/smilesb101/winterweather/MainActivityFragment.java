package smilesb101.zxb.smilesb101.winterweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/6/28.
 */
public class MainActivityFragment extends Fragment{
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.activity_main,container,false);

		return v;
	}
}
