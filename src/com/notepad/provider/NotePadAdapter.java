package com.notepad.provider;

import com.notepad.R;
import com.notepad.util.NotePadUtils;
import com.notepad.util.ScalePixel;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotePadAdapter extends BaseAdapter {
	private Context mContexto;
	private Long[] mIds;
	private String[] mTitles, mDates;
	private int mCount;

	public NotePadAdapter(Context context, Cursor notes) {
		mContexto = context;
		fillData(notes);
	}

	private void fillData(Cursor notes) {
		notes.moveToFirst();
		mCount = notes.getCount();
		mIds = new Long[mCount];
		mTitles = new String[mCount];
		mDates = new String[mCount];

		for (int i = 0; i < mCount; i++) {
			mIds[i] = notes.getLong(notes
					.getColumnIndexOrThrow(NotePadUtils.KEY_ROWID_DATABASE));
			mTitles[i] = notes.getString(notes
					.getColumnIndexOrThrow(NotePadUtils.KEY_TITLE_DATABASE));
			mDates[i] = notes.getString(notes
					.getColumnIndexOrThrow(NotePadUtils.KEY_DATE_DATABASE));
			notes.moveToNext();
		}
	}

	public int getCount() {
		return mCount;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		return mIds[position];
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View layout;

		if (mCount == position) {
			mTitles = null;
		}

		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContexto
					.getApplicationContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);

			layout = layoutInflater.inflate(R.layout.maingridlayout, null);

			TextView gridText = (TextView) layout
					.findViewById(R.id.gridTextView);
			TextView dateText = (TextView) layout.findViewById(R.id.date);

			// Text Padding from the image
			final float LANDSCAPE_DPI = 8.0f;
			ScalePixel scaler = new ScalePixel(mContexto);
			int paddingLeft;

			int orientation = mContexto.getResources().getConfiguration().orientation;
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				LinearLayout mainGridLinearLayout = (LinearLayout) layout
						.findViewById(R.id.mainGridLinearLayout);
				mainGridLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
				gridText = setMaxLength(gridText);

				paddingLeft = scaler.getPixels(LANDSCAPE_DPI);
				gridText.setPadding(paddingLeft, 0, 0, 0);
			} else {
			}

			// Set the text as the title
			String title = getTitle(position);
			String currentdate = getDate(position);
			gridText.setText(title);
			dateText.setText(currentdate);
		} else {
			layout = convertView;
		}
		return layout;
	}

	private TextView setMaxLength(TextView gridText) {

		InputFilter[] maxLength = new InputFilter[1];
		maxLength[maxLength.length - 1] = new InputFilter.LengthFilter(
				NotePadUtils.MAX_CHARACTERS_HORIZONTAL);
		gridText.setFilters(maxLength);
		maxLength = null;
		return gridText;
	}

	private String getTitle(int position) {
		return mTitles[position];
	}
	
	private String getDate(int position) {
		return mDates[position];
	}
}
