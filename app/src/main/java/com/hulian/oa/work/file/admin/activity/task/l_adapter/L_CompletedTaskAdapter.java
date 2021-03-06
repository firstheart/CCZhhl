package com.hulian.oa.work.file.admin.activity.task.l_adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulian.oa.R;
import com.hulian.oa.bean.Notice_x;
import com.hulian.oa.work.file.admin.activity.task.l_details_activity.TaskCompletedDetailsActivity;
import com.hulian.oa.work.file.admin.activity.task.l_details_activity.TaskUndoneDetailsActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class L_CompletedTaskAdapter extends RecyclerView.Adapter <L_CompletedTaskAdapter.ViewHolder>{
    private Context mContext;
    private List<Notice_x> dataList = new ArrayList<>();


    public void addAllData(List<Notice_x> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public L_CompletedTaskAdapter(Context context) {
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TextView tv_time;
        public TextView tv_deadline_time;
        public TextView tv_deadline_week;
        public TextView tv_comp_launch_task_person;
        public ImageView iv_image;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_comp_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_comp_time);
            tv_deadline_time = (TextView) itemView.findViewById(R.id.tv_comp_deadline_time);
//            tv_deadline_week = (TextView) itemView.findViewById(R.id.tv_comp_deadline_week);
            tv_comp_launch_task_person = (TextView) itemView.findViewById(R.id.tv_comp_launch_task_person);
            iv_image=itemView.findViewById(R.id.iv_image);
        }
    }

    @Override
    public L_CompletedTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_item_completed_task, parent, false);

        return new L_CompletedTaskAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(L_CompletedTaskAdapter.ViewHolder holder, final int position) {
        holder.tv_title.setText(dataList.get(position).getTitle());
        //holder.tv_time.setText(dataList.get(position).getStartTime());
        //        开始时间
        String b = dataList.get(position).getStartTime();
        String c = getDataaa(b);
        holder.tv_time.setText(c);

        //   截止时间
        String a = dataList.get(position).getEndTime();
        holder.tv_deadline_time.setText(a);

//        holder.tv_deadline_time.setText(dataList.get(position).getEndTime());
        holder.tv_comp_launch_task_person.setText(dataList.get(position).getCreateBy());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("PORID",dataList.get(position).getProid());
                intent.putExtra("ID",dataList.get(position).getId());
                intent.setClass(mContext, TaskCompletedDetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        holder.iv_image.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private String getDataaa(String fromDate) {
        String strTime = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String toDate = df.format(curDate);
        try {
            //前的时间
            Date fd = df.parse(fromDate);
            //后的时间
            Date td = df.parse(toDate);
            //两时间差,精确到毫秒
            long diff = td.getTime() - fd.getTime();
            long day = diff / 86400000;                         //以天数为单位取整
            long hour = diff % 86400000 / 3600000;               //以小时为单位取整
            long min = diff % 86400000 % 3600000 / 60000;       //以分钟为单位取整
            long seconds = diff % 86400000 % 3600000 % 60000 / 1000;   //以秒为单位取整

            if (day<=0)
            {
                strTime = fromDate.substring(10,fromDate.length()-3);
            }
            else
            {
                System.out.println("两时间差---> " + day + "天" + hour + "小时" + min + "分" + seconds + "秒");
                strTime = fromDate.substring(0,fromDate.length()-8);

            }
            return strTime;
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }


}
