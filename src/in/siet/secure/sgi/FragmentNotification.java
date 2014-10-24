package in.siet.secure.sgi;

import in.siet.secure.Util.Notification;
import in.siet.secure.adapters.NotificationAdapter;
import in.siet.secure.dao.DbHelper;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentNotification extends Fragment{
	static String TAG="in.siet.secure.sgi.FragmentNotification";
	public static ArrayList<Notification> notifications=new ArrayList<Notification>();
	public static NotificationAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_notification, container,	false);
		adapter=new NotificationAdapter(getActivity(), notifications);
		new DbHelper(getActivity()).getNotifications();
		
		return rootView;
	}
	@Override
	public void onResume(){
		super.onResume();
		((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.fragemnt_title_notification);
		((MainActivity)getActivity()).getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.ic_action_notification_white));
		
	//	Utility.RaiseToast(getActivity(), "I am Notification", 0);
	}
	@Override
	public void onStart(){
		super.onStart();
		ListView listView=(ListView)getActivity().findViewById(R.id.fragment_notification_list);
		listView.setOnItemClickListener(new itemClickListener());
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	class itemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,long id) {
			Notification notify=((Notification)adapter.getItemAtPosition(position));
			Fragment fragment=getFragmentManager().findFragmentByTag(TAG+"Notification");
			
			if(fragment==null){
				fragment =new FragmentDetailNotification();
				Bundle bundle =new Bundle();
				bundle.putString(Notification.SUBJECT,notify.subject);
				bundle.putString(Notification.TEXT,notify.text);
				bundle.putString(Notification.TIME,notify.time);
				bundle.putString(Notification.SENDER,notify.sender_id);
				bundle.putIntArray(Notification.ATTACHMENT, notify.attachments_id);
				bundle.putInt(Notification.ID, notify.id);
				fragment.setArguments(bundle);
			}
				getFragmentManager()
				.beginTransaction()
				.setTransitionStyle(R.anim.abc_fade_out)
				.replace(R.id.mainFrame,fragment,TAG+"Notification")
				.addToBackStack(null)
				.commit();
		}
	}
	public static void refresh(){
		if(adapter!=null)
			adapter.notifyDataSetChanged();
	}
	public static void setData(ArrayList<Notification> data){
		adapter.clear();
		adapter.addAll(data);
	}
}
