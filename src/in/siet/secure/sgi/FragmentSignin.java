package in.siet.secure.sgi;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentSignin extends Fragment {
		public FragmentSignin() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_signin, container,
					false);
			return rootView;
		}
		 @Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        // retain this fragment
		        setRetainInstance(true);
		    }

		@Override
		public void onResume(){
			super.onResume();
			((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
		}
		
		
}
