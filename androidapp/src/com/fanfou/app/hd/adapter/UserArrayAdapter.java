package com.fanfou.app.hd.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fanfou.app.hd.App;
import com.fanfou.app.hd.R;
import com.fanfou.app.hd.cache.ImageLoader;
import com.fanfou.app.hd.dao.model.UserModel;
import com.fanfou.app.hd.ui.widget.ItemView;
import com.fanfou.app.hd.util.OptionHelper;

/**
 * @author mcxiaoke
 * @version 1.0 2011.06.01
 * @version 1.5 2011.10.24
 * @version 1.6 2011.10.30
 * @version 1.7 2011.11.10
 * @version 1.8 2011.12.06
 * @version 2.0 2012.02.22
 * @version 2.1 2012.02.27
 * 
 */
public abstract class UserArrayAdapter extends BaseAdapter implements
		OnScrollListener {
	private static final String TAG = UserArrayAdapter.class.getSimpleName();
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected ImageLoader mLoader;

	protected List<UserModel> mData;
	protected int fontSize;
	protected boolean busy;

	public UserArrayAdapter(Context context, List<UserModel> data) {
		super();
		initialize(context, data);
	}

	private void initialize(Context context, List<UserModel> data) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mLoader = App.getImageLoader();
		this.fontSize = OptionHelper.readInt(mContext,
				R.string.option_fontsize,
				context.getResources().getInteger(R.integer.defaultFontSize));
		mData = new ArrayList<UserModel>();
		if (data != null) {
			mData.addAll(data);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemView view = (ItemView) convertView;
		if (view == null) {
			view = new ItemView(mContext);
			view.setId(R.id.list_item);
			if (App.DEBUG) {
				Log.d(TAG, "getView newView=" + view);
			}
		}

		final UserModel u = getData().get(position);

		String headUrl = u.getProfileImageUrl();
		if (busy) {
			Bitmap bitmap = mLoader.getImage(headUrl, null);
			if (bitmap != null) {
				view.setImage(bitmap);
			} else {
				view.setImage(R.drawable.ic_head);
			}
		} else {
			ImageView head = view.getImageView();
			head.setTag(headUrl);
			mLoader.displayImage(headUrl, head, R.drawable.ic_head);
		}

		UIHelper.setContent(view, u);
		return convertView;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public UserModel getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected List<UserModel> getData() {
		return mData;
	}

	public void setData(List<UserModel> data) {
		mData.clear();
		mData.addAll(data);
	}

	public void addData(List<UserModel> data) {
		mData.addAll(data);
	}

	protected int getLayoutId() {
		// return R.layout.list_item_user;
		return -1;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int size) {
		fontSize = size;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			busy = false;
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			busy = true;
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			busy = true;
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

}
