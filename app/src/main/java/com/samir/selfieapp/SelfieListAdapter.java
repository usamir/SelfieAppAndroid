package com.samir.selfieapp;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by usamir on 14.2.2016.
 */
public class SelfieListAdapter extends BaseAdapter {

    private List<SelfieRecord> mList;
    private Context mContext;

    public SelfieListAdapter(Context context, List<SelfieRecord> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listitem, null);
        }

        ImageView imgBitmap = (ImageView) convertView.findViewById(R.id.picture);
        TextView txtDate = (TextView) convertView.findViewById(R.id.date);

        SelfieRecord selfie_pos = mList.get(position);
        //setting the image resource and title
        imgBitmap.setImageBitmap(selfie_pos.getPicture());
        txtDate.setText(selfie_pos.getDate());

        return convertView;
    }


    public void add(List<SelfieRecord> newList) {
        mList.addAll(newList);
        notifyDataSetChanged();
    }

    public List<SelfieRecord> getList() {
        return mList;
    }


    public void removeAllViews() {
        mList.clear();
        File storageDir = new File(Environment.getExternalStoragePublicDirectory (
                Environment.DIRECTORY_PICTURES), "SelfieApp");
        for (File f : storageDir.listFiles()) {
            if (f.isFile()) {
                f.delete();
            }
        }
        this.notifyDataSetChanged();
    }

    // TODO: remove only one picture from list of selfies
    public void removeView(int id, String uri) {
        mList.remove(id);
        File f = new File(uri);
        if (f.isFile()) {
            f.delete();
        }
        this.notifyDataSetChanged();
    }

}
