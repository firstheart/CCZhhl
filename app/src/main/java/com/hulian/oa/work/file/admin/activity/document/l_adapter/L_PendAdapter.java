package com.hulian.oa.work.file.admin.activity.document.l_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hulian.oa.BuildConfig;
import com.hulian.oa.R;
import com.hulian.oa.bean.Document;
import com.hulian.oa.me.l_adapter.L_CollectionNoticeAdapter;
import com.hulian.oa.pad.PAD_gongwen_SP;
import com.hulian.oa.utils.SPUtils;
import com.hulian.oa.utils.TimeUtils;
import com.hulian.oa.views.fabVIew.FabAttributes;
import com.hulian.oa.work.file.admin.activity.document.DocumentLotusActivity;
import com.hulian.oa.work.file.admin.activity.document.DocumentLotusInfoActivity;
import com.netease.nim.uikit.common.framework.NimTaskExecutor;

import java.util.ArrayList;
import java.util.List;

public class L_PendAdapter extends RecyclerView.Adapter<L_PendAdapter.ViewHolder> {
    //qgl的分支
    private Context mContext;
    private List<Document> dataList = new ArrayList<>();


    public void addAllData(List<Document> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public L_PendAdapter(Context context) {
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TextView tv_time;
        public TextView tv_roam_person;
        public TextView faqi_person;
        //        qgl注释
//        private ImageView tv_status;
        //qgl
        private TextView ld_stuas;
        private TextView Leader_tv_title;
        private TextView Leader_tv_time;
        private TextView Leader_tv_wenhao;
        private RelativeLayout Workers_data;
        private RelativeLayout Leader_data;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_roam_person = (TextView) itemView.findViewById(R.id.tv_roam_person);
            faqi_person = (TextView) itemView.findViewById(R.id.faqi_person);
            //qgl注释
//            tv_status=(ImageView) itemView.findViewById(R.id.tv_status);
//            qgl
            ld_stuas = (TextView) itemView.findViewById(R.id.ld_stuas);
            Workers_data = (RelativeLayout) itemView.findViewById(R.id.Workers_data);
            Leader_data = (RelativeLayout) itemView.findViewById(R.id.Leader_data);
            Leader_tv_title = (TextView) itemView.findViewById(R.id.Leader_tv_title);
            Leader_tv_time = (TextView) itemView.findViewById(R.id.Leader_tv_time);
            Leader_tv_wenhao = (TextView) itemView.findViewById(R.id.Leader_tv_wenhao);
        }
    }

    @Override
    public L_PendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_item_pend, parent, false);
        return new L_PendAdapter.ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(L_PendAdapter.ViewHolder holder, final int position) {
        holder.tv_title.setText(dataList.get(position).getOfficialDocumentTitle());
        holder.tv_time.setText(dataList.get(position).getCreate_Time());
        holder.tv_roam_person.setText(dataList.get(position).getApproverIdsNames());
        holder.faqi_person.setText(dataList.get(position).getPromoterIdName());

        //领导
        holder.Leader_tv_time.setText(dataList.get(position).getCreate_Time());
        holder.Leader_tv_title.setText(dataList.get(position).getOfficialDocumentTitle());

        if (!SPUtils.get(mContext, "isLead", "").equals("0")) {
            holder.Workers_data.setVisibility(View.VISIBLE);
            holder.Leader_data.setVisibility(View.GONE);


//            qgl注释的
//            holder.tv_status.setVisibility(View.VISIBLE);
            if ("0".equals(dataList.get(position).getOfficialDocumentState())) {
//                holder.tv_status.setText("审批中");
//                holder.tv_status.setTextColor(R.color.bg_yellow);
//                qgl注释的
//                holder.tv_status.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.qj_daishenpi_icon_qgl));

            } else if ("1".equals(dataList.get(position).getOfficialDocumentState())) {
//                holder.tv_status.setText("审批通过");
//                holder.tv_status.setTextColor(R.color.colorCircle);
//                qgl注释的
//                holder.tv_status.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.qj_shenpi_tongguo_icon_qgl));

            } else if ("2".equals(dataList.get(position).getOfficialDocumentState())) {
//                holder.tv_status.setText("审批不通过");
//                holder.tv_status.setTextColor(R.color.colorAccent);
//                qgl注释的
//                holder.tv_status.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.qj_bohui_icon_qgl));
            } else {
                //qgl注释
                //holder.tv_status.setVisibility(View.GONE);
            }
        } else {
            //qgl
            //holder.tv_status.setVisibility(View.GONE);
            holder.Workers_data.setVisibility(View.GONE);
            holder.Leader_data.setVisibility(View.VISIBLE);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("offId", dataList.get(position).getOfficialDocumentId());
                if (SPUtils.get(mContext, "isLead", "").equals("0")) {
                    if (BuildConfig.IsPad) {
                        intent.setClass(mContext, PAD_gongwen_SP.class);
                    } else {
                        intent.setClass(mContext, DocumentLotusActivity.class);
                    }
                } else {
                    intent.setClass(mContext, DocumentLotusInfoActivity.class);
                }
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
