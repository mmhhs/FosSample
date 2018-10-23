package com.little.popup.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.little.popup.R;

import java.util.List;


public class ReasonListAdapter extends BaseAdapter {
	public Context context;
	public List<ReasonEntity> list;

	public ReasonListAdapter(Context context, List<ReasonEntity> list) {
		this.context = context;
		this.list = list;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder ;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.ft_adapter_reason_list, null);
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.ft_adapter_reason_list_text);
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.ft_adapter_reason_list_icon);
			convertView.setTag(viewHolder);
		} else {

			viewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			viewHolder.text.setText(list.get(position).reason);
			if (list.get(position).isSelected){
				viewHolder.icon.setSelected(true);
			}else {
				viewHolder.icon.setSelected(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	public final static class ViewHolder {
		TextView text;
		ImageView icon;
	}

	public void setSelected(int position){
		for (ReasonEntity entity:list){
			entity.isSelected = false;
		}
		list.get(position).isSelected = true;
		notifyDataSetChanged();
	}

	public boolean hasSelected(){
		boolean result = false;
		for (ReasonEntity entity:list){
			if (entity.isSelected){
				result = true;
			}
		}
		return result;
	}

	public int getSelectedPosition(){
		int pos = 0;
		for (int i=0;i<list.size();i++){
			if (list.get(i).isSelected){
				pos = i;
			}
		}
		return pos;
	}
}
