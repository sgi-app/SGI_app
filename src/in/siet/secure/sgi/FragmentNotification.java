package in.siet.secure.sgi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentNotification extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_notification, container,	false);
	//	getActivity().setTitle(getResources().getStringArray(R.array.panel_options)[0]);
		return rootView;
	}
	@Override
	public void onResume(){
		super.onResume();
		Utility.RaiseToast(getActivity(), "I am Notification", 0);
	}
	
}
