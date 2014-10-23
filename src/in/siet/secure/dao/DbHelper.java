package in.siet.secure.dao;

import in.siet.secure.Util.Attachment;
import in.siet.secure.Util.Notification;
import in.siet.secure.Util.Utility;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	public static final String DATABASE_NAME="sgi_app.db";
	public static final int DATABASE_VERSION=1;
	public DbHelper(Context context){
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(DbStructure.FcultyContactsTable.COMMAND_CREATE);
		db.execSQL(DbStructure.FileMessageMapTable.COMMAND_CREATE);
		db.execSQL(DbStructure.FileNotificationMapTable.COMMAND_CREATE);
		db.execSQL(DbStructure.FileTable.COMMAND_CREATE);
		db.execSQL(DbStructure.MessageTable.COMMAND_CREATE);
		db.execSQL(DbStructure.NotificationTable.COMMAND_CREATE);
		db.execSQL(DbStructure.StudentContactsTable.COMMAND_CREATE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DbStructure.FileMessageMapTable.COMMAND_DROP);
		db.execSQL(DbStructure.FileNotificationMapTable.COMMAND_DROP);
		db.execSQL(DbStructure.FileTable.COMMAND_DROP);
		db.execSQL(DbStructure.MessageTable.COMMAND_DROP);
		db.execSQL(DbStructure.NotificationTable.COMMAND_DROP);
		db.execSQL(DbStructure.StudentContactsTable.COMMAND_DROP);
		db.execSQL(DbStructure.FcultyContactsTable.COMMAND_DROP);
		onCreate(db);
	}
	
	public ArrayList<Notification> getNotifications(){
		ArrayList<Notification> notifications=new ArrayList<Notification>();
			String[] projection={
					
					DbStructure.NotificationTable.COLUMN_SENDER,
					DbStructure.NotificationTable.COLUMN_SUBJECT,
					DbStructure.NotificationTable.COLUMN_TEXT,
					DbStructure.NotificationTable.COLUMN_TIME,
					DbStructure.NotificationTable._ID,
			};
			Cursor c=this.getReadableDatabase().query(DbStructure.NotificationTable.TABLE_NAME,projection,null,null,null,null,null);
			c.moveToFirst();
			while(c.isAfterLast()==false){
						notifications.add(new Notification(c.getInt(c.getColumnIndexOrThrow(projection[4])),
								c.getString(c.getColumnIndexOrThrow(projection[0])),
								c.getString(c.getColumnIndexOrThrow(projection[1])),
								c.getString(c.getColumnIndexOrThrow(projection[2])),
								c.getString(c.getColumnIndexOrThrow(projection[3])),
								null
								));
				c.moveToNext();
			}
		
		return notifications;
	}
	
	public ArrayList<Attachment> getFilesOfNotification(int noti_id) {
		String url;
		ArrayList<Attachment> result=new ArrayList<Attachment>();
		String[] projection={
				DbStructure.FileTable.COLUMN_PATH
		};
		Cursor c=this.getReadableDatabase().rawQuery("select path from files join file_notification_map on files._ID=file_notification_map.file_id where file_notification_map.notification_id="+noti_id+";", null);//query(DbStructure.FileTable.TABLE_NAME, projection,DbStructure.FileTable._ID+"="+DbStructure.FileNotificationMapTable.TABLE_NAME+"."+DbStructure.FileMessageMapTable.COLUMN_FILE_ID+" and "+DbStructure.FileNotificationMapTable.TABLE_NAME+"."+DbStructure.FileNotificationMapTable.COLUMN_NOTIFICATION_ID+"="+noti_id, null, null, null, null);
		c.moveToFirst();
		while(c.isAfterLast()==false){
			url=c.getString(c.getColumnIndexOrThrow(DbStructure.FileTable.COLUMN_PATH));
			try{
			URL tmpurl=new URL(url);
			result.add(new Attachment(tmpurl.getFile(), "5M", "12:00", url));
			}catch(Exception e){
				Utility.log("URL Parser","BAD URL");
			}
			
			c.moveToNext();
		}
		return result;
	}
	
/*	
	public void fill_tmp_data(){
		DbHelper dbHelper=new DbHelper(getApplicationContext());
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(DbStructure.FcultyContactsTable._ID,1);
		values.put(DbStructure.FcultyContactsTable.COLUMN_FNAME, "pogo");
		values.put(DbStructure.FcultyContactsTable.COLUMN_LNAME, "gopo");
		db.insert(DbStructure.FcultyContactsTable.TABLE_NAME,null, values);

		Utility.RaiseToast(getApplicationContext(), "inserted value", 1);
		db=dbHelper.getReadableDatabase();
		String[] projection={
				DbStructure.FcultyContactsTable._ID,
				DbStructure.FcultyContactsTable.COLUMN_FNAME,
				DbStructure.FcultyContactsTable.COLUMN_LNAME,
		};
		Cursor c=db.query(DbStructure.FcultyContactsTable.TABLE_NAME,projection, null,null,null,null,null);
		c.moveToFirst();
		Utility.RaiseToast(getApplicationContext(), c.getString(c.getColumnIndexOrThrow(DbStructure.FcultyContactsTable.COLUMN_FNAME)), 1);
	}
*/
}
