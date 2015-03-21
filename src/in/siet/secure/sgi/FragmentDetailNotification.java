package in.siet.secure.sgi;

import in.siet.secure.Util.Attachment;
import in.siet.secure.Util.Utility;
import in.siet.secure.adapters.NotificationAttachmentAdapter;
import in.siet.secure.contants.Constants;
import in.siet.secure.dao.DbHelper;

import java.io.File;
import java.util.ArrayList;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class FragmentDetailNotification extends Fragment implements
		OnClickListener {
	private int not_id;
	public LinearLayout listViewAtachments;
	private ArrayList<Attachment> attachments = new ArrayList<Attachment>();
	private NotificationAttachmentAdapter adapter;
	public View rootView;
	public static final String TAG = "in.siet.secure.sgi.FragmentDetailNotification";
	private BroadcastReceiver refresh_receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			/**
			 * check for the ID so that file list correctly corresponds to the
			 * notification displayed
			 **/

			if (intent.getBooleanExtra(Constants.INTENT_EXTRA.HAS_ATTACHMENTS,
					false)) {
				if (intent.getIntExtra(Constants.INTENT_EXTRA.NOTIFICATION_ID,
						-1) == not_id) {
					ArrayList<Attachment> data = intent
							.<Attachment> getParcelableArrayListExtra(Constants.INTENT_EXTRA.ATTACHMENTS_DATA);
					if (data.size() > 0) {
						setData(data);
						haveAttachments();
					} else {
						/**
						 * no data
						 */
						noAttachments();
					}
				} else {
					/**
					 * ID not matched
					 */
					noAttachments();
				}
			} else {
				/**
				 * no attachments associated with the notifications
				 */
				noAttachments();
			}
		}
	};

	public FragmentDetailNotification() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		adapter = new NotificationAttachmentAdapter(getActivity(), attachments);
		Bundle bundle = getArguments();
		not_id = bundle.getInt(Constants.NOTIFICATION.ID);
		new DbHelper(getActivity().getApplicationContext())
				.getFilesOfNotification(not_id);

		rootView = inflater.inflate(R.layout.fragment_detailed_notification,
				container, false);

		ImageView image = (ImageView) rootView
				.findViewById(R.id.imageViewNotificationSenderImage);
		TextView subject = (TextView) rootView
				.findViewById(R.id.textViewNotificationSubject);
		TextView text = (TextView) rootView
				.findViewById(R.id.textViewNotificationBody);
		TextView time = (TextView) rootView
				.findViewById(R.id.textViewNotificationTime);
		listViewAtachments = (LinearLayout) rootView
				.findViewById(R.id.linearLayoutNotificationAttachmentsList);

		subject.setText(bundle.getString(Constants.NOTIFICATION.SUBJECT));
		text.setText(bundle.getString(Constants.NOTIFICATION.TEXT));

		time.setText(Utility.getTimeString(getActivity()
				.getApplicationContext(), bundle
				.getLong(Constants.NOTIFICATION.TIME)));

		ImageLoader.getInstance().displayImage(
				bundle.getString(Constants.NOTIFICATION.SENDER_IMAGE), image);
		// adapter=new NotificationAttachmentAdapter(getActivity(),
		// attachments);
		processAttachments();
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// retain this fragment
		setRetainInstance(true);
	}

	@Override
	public void onStart() {
		LocalBroadcastManager
				.getInstance(getActivity().getApplicationContext())
				.registerReceiver(
						refresh_receiver,
						new IntentFilter(
								Constants.LOCAL_INTENT_ACTION.RELOAD_ATTACHMENTS));
		super.onStart();
	}

	@Override
	public void onPause() {
		LocalBroadcastManager
				.getInstance(getActivity().getApplicationContext())
				.unregisterReceiver(refresh_receiver);
		super.onPause();
	}

	public void refresh() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
			listViewAtachments.removeAllViews();
			for (int i = 0; i < adapter.getCount(); i++) {
				listViewAtachments.addView(adapter.getView(i, null, null));// ;setAdapter(adapter);
			}
		}

	}

	public void setData(ArrayList<Attachment> data) {
		adapter.clear();
		adapter.addAll(data);
		refresh();
	}

	public void haveAttachments() {
		rootView.findViewById(R.id.progressBarLoadingAttachments)
				.setVisibility(View.GONE);
		listViewAtachments.setVisibility(View.VISIBLE);
	}

	public void noAttachments() {
		listViewAtachments.setVisibility(View.GONE);
		rootView.findViewById(R.id.progressBarLoadingAttachments)
				.setVisibility(View.GONE);
	}

	public void processAttachments() {
		listViewAtachments.setVisibility(View.GONE);
		rootView.findViewById(R.id.progressBarLoadingAttachments)
				.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		NotificationAttachmentAdapter.ViewHolder h = (NotificationAttachmentAdapter.ViewHolder) v
				.getTag();
		if (h.state == 1) {
			// open file
			File file = new File(Constants.pathToApp + h.name);
			MimeTypeMap map = MimeTypeMap.getSingleton();
			String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
			String type = map.getMimeTypeFromExtension(ext);

			if (type == null)
				type = "*/*";

			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri data = Uri.fromFile(file);

			intent.setDataAndType(data, type);
			try {
				startActivity(intent);
			} catch (Exception e) {
				Utility.RaiseToast(getActivity(), "cannot open file", false);
			}
		} else {
			// download file
			Utility.log("Yaha", "clicked on " + h.name.getText());
			new Utility.DownloadFile().execute(h.url,
					(String) h.name.getText(), "" + h.id);
		}
	}
}
