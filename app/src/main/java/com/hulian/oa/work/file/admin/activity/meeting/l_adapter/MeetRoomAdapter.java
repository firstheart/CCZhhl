package com.hulian.oa.work.file.admin.activity.meeting.l_adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hulian.oa.R;
import com.hulian.oa.bean.MeetingRoom;
import com.hulian.oa.net.Urls;

import java.util.ArrayList;
import java.util.List;

public class MeetRoomAdapter extends BaseAdapter {
    public Context mContext;
    private List<MeetingRoom> list = new ArrayList<>();
    public MeetRoomAdapter(Context mContext, List<MeetingRoom> list){
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list.size()<3) {
            return list.size();
        }
        else
            return 3;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.l_item_meet_room,null);
       ImageView imageView= view.findViewById(R.id.iv_meet_room);
        TextView tv_meet_room_num=view.findViewById(R.id.tv_meet_room_num);
        TextView tv_meet_count=view.findViewById(R.id.tv_meet_count);
        TextView tv_sel_room=view.findViewById(R.id.tv_sel_room);
//        Glide.with(mContext).load(list.get(position).getPhotoPath()).into(imageView);
        tv_meet_room_num.setText(list.get(position).getMeetingRoomLocation());
        tv_meet_count.setText("容纳人数："+list.get(position).getGalleryful());
        if(list.get(position).getIsCheck().equals("0")){
            tv_sel_room.setText("选择");
         //   tv_sel_room.setBackgroundColor(R.color.color_a_blue);
        }
        else {
            tv_sel_room.setText("已选");
        //    tv_sel_room.setBackgroundColor(R.color.bg_yellow);
        }
        return view;
    }
}
