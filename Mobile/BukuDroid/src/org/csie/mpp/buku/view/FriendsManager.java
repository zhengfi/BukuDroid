package org.csie.mpp.buku.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.csie.mpp.buku.App;
import org.csie.mpp.buku.R;
import org.csie.mpp.buku.Util;
import org.csie.mpp.buku.db.DBHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.android.BaseRequestListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsManager extends ViewManager {
	private List<Friend> friends;
	private ArrayAdapter<Friend> adapter;
	
	public FriendsManager(Activity activity, DBHelper helper) {
		super(activity, helper);
	}
	
	private static class Friend {
		private String id;
		private String name;
		private Bitmap icon;
		
		public Friend(String id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	private void createView(LinearLayout frame) {
		if(friends.size() == 0) {
			TextView text = (TextView)frame.findViewById(R.id.text);
			text.setText("You have no friends. QQ");
		}
		else {
			TextView text = (TextView)frame.findViewById(R.id.text);
			text.setText("");
			
			adapter = new ArrayAdapter<Friend>(activity, R.layout.list_item_friend, friends) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					Friend friend = friends.get(position);
					LayoutInflater inflater = activity.getLayoutInflater();
					View view = inflater.inflate(R.layout.list_item_friend, null);
					((ImageView)view.findViewById(R.id.list_image)).setImageBitmap(friend.icon);
					((TextView)view.findViewById(R.id.list_name)).setText(friend.name);
					return view;
				}
			};
			
			ListView list = (ListView)frame.findViewById(R.id.list);
			list.setAdapter(adapter);
		}
	}

	@Override
	protected void updateView() {
		final LinearLayout frame = getFrame();
		
		if(friends != null)
			createView(frame);
		else {
			Bundle params = new Bundle();
			params.putString("fields", "installed,id,name");
			App.fb_runner.request("me/friends", params, new BaseRequestListener() {
				@Override
				public void onComplete(String response, Object state) {
					friends = new ArrayList<Friend>();
					
					try {
						while(response != null) {
							JSONObject json = new JSONObject(response);
							JSONArray data = json.getJSONArray("data");
							
							for(int i = 0; i < data.length(); i++) {
								JSONObject p = data.getJSONObject(i);
								if(p.has("installed")) {
									Friend friend = new Friend(p.getString("id"), p.getString("name"));
									friend.icon = Util.urlToImage(new URL("http://graph.facebook.com/" + friend.id + "/picture"));
									friends.add(friend);
								}
							}
							
							JSONObject paging = json.getJSONObject("paging");
							if(!paging.has("next"))
								response = null;
							else {
								URL url = new URL(paging.getString("next"));
								response = Util.urlToString(url);
							}
						}
					}
					catch(Exception e) {
						friends.clear();
					}
					
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							createView(frame);
						}
					});
				}
			});
		}
	}
}
