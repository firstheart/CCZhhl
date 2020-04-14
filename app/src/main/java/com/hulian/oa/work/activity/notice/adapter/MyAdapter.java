package com.hulian.oa.work.activity.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hulian.oa.R;
import com.hulian.oa.bean.Department;
import com.hulian.oa.work.activity.notice.NoticeSelPartActivity;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<Department> data;
    private Context context;
    private NoticeSelPartActivity.AllCheckListener allCheckListener;

    public MyAdapter(List<Department> data, Context context, NoticeSelPartActivity.AllCheckListener allCheckListener) {
        this.data = data;
        this.context = context;
        this.allCheckListener = allCheckListener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHoder hd;
        if (view == null) {
            hd = new ViewHoder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.item_sel_part, null);
            hd.textView = (TextView) view.findViewById(R.id.text_title);
            hd.checkBox = (CheckBox) view.findViewById(R.id.ckb);
            view.setTag(hd);
        }
        Department mModel = data.get(i);
        hd = (ViewHoder) view.getTag();
        hd.textView.setText(mModel.getDeptName());

        final ViewHoder hdFinal = hd;
        hd.checkBox.setChecked(mModel.isIscheck());
        hd.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = hdFinal.checkBox;
                 checkBox.setChecked(checkBox.isChecked());
                 data.get(i).setIscheck(checkBox.isChecked());

                //监听每个item，若所有checkbox都为选中状态则更改main的全选checkbox状态
                for (Department model : data) {
                    if (!model.isIscheck()) {
                        allCheckListener.onCheckedChanged(false);
                        return;
                    }
                }
                allCheckListener.onCheckedChanged(true);


            }
        });


        return view;
    }

    class ViewHoder {
        TextView textView;
        CheckBox checkBox;
    }

}