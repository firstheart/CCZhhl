package com.hulian.oa.work.file.admin.activity.meeting.l_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulian.oa.R;
import com.hulian.oa.bean.Department;
import com.hulian.oa.bean.Dept;
import com.hulian.oa.bean.People;
import com.hulian.oa.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    //视图加载器
    private LayoutInflater mInflater;
    private Context mContext;
    private int mExpandedGroupLayout;
    private int mChildLayout;
    private List<Department> mGroupArray;
    private List<List<People>> mChildArray;
    TextView tv_count;
    /**
     * 构造函数
     *
     * @param context
     * @param groupData
     * @param expandedGroupLayout 分组视图布局
     * @param childData
     * @param childLayout         详情视图布局
     */
    public ExpandableAdapter(Context context, List<Department> groupData, int expandedGroupLayout,
                             List<List<People>> childData, int childLayout) {
        mContext = context;
        mExpandedGroupLayout = expandedGroupLayout;
        mChildLayout = childLayout;
        mGroupArray = groupData;
        mChildArray = childData;
        mInflater = ( LayoutInflater ) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mChildArray.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // 取得显示给定分组给定子位置的数据用的视图。
        View v;
        if ( convertView == null ) {
            v = newChildView(parent);
        } else {
            v = convertView;
        }
        bindChildView(v, mChildArray.get(groupPosition).get(childPosition), mChildArray.get(groupPosition),mGroupArray.get(groupPosition) );
        return v;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // 取得指定分组的子元素数。
        return mChildArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // 取得与给定分组关联的数据。
        return mGroupArray.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // 取得分组数
        return mGroupArray.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // 取得指定分组的ID。该组ID必须在组中是唯一的。组合的ID （参见getCombinedGroupId(long)）
        // 必须不同于其他所有ID（分组及子项目的ID）。
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // 取得用于显示给定分组的视图。 这个方法仅返回分组的视图对象， 要想获取子元素的视图对象，
        // 就需要调用 getChildView(int, int, boolean, View, ViewGroup)。
        View v;
            v = newGroupView(parent);
        bindGroupView(v, mGroupArray.get(groupPosition), isExpanded,groupPosition);
        return v;
    }

    /**
     * 绑定组数据
     *  @param view
     * @param data
     * @param isExpanded
     * @param groupPosition
     */
    private void bindGroupView(View view, Department data, boolean isExpanded, int groupPosition) {
        // 绑定组视图的数据 当然这些都是模拟的
        TextView tv_title = ( TextView ) view.findViewById(R.id.group_title);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.bb);
        checkBox.setChecked(data.isIscheck());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = checkBox.isChecked();
                data.setIscheck(isChecked);
                for(int i=0;i<mChildArray.get(groupPosition).size();i++){
                    mChildArray.get(groupPosition).get(i).setIscheck(isChecked);
                }
                if(isChecked){
                    data.setCount(mChildArray.get(groupPosition).size()+"");
                }
                else
                {
                    data.setCount("0");
                }

                EventBus.getDefault().post(mChildArray);
                notifyDataSetChanged();
            }
        });

        tv_title.setText(data.getDeptName());
         tv_count = ( TextView ) view.findViewById(R.id.tv_count);
         if(data.getCount()==null){
             tv_count.setText("已选择0人");
         }
         else
        tv_count.setText("已选择"+data.getCount()+"人");

//        if ( !use_default_indicator ) {
//            ImageView iv_tip = ( ImageView ) view.findViewById(R.id.iv_tip);
//            if ( isExpanded ) {
//                iv_tip.setImageResource(R.mipmap.down);
//            } else {
//                iv_tip.setImageResource(R.mipmap.right);
//            }
//        }
    }

    /**
     * 绑定子数据
     *  @param view
     * @param data
     * @param peopleList
     */
    private void bindChildView(View view, People data, List<People> peopleList, Department mDapart) {
        // 绑定组视图的数据 当然这些都是模拟的
        TextView tv_name = ( TextView ) view.findViewById(R.id.tv_name);
        tv_name.setText(data.getUserName());
        TextView tv_role=( TextView ) view.findViewById(R.id.tv_role);
        if(data.getIsLead().equals("0"))
        tv_role.setText("一级领导");
        else {
            tv_role.setText("二级领导");
        }
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.aa);
        checkBox.setChecked(data.isIscheck());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = checkBox.isChecked();
                data.setIscheck(isChecked);
           //     notifyDataSetChanged();
                int j=0;
                for(int i=0;i<peopleList.size();i++){
                    if(peopleList.get(i).isIscheck()){
                        j++;
                    }
                }
                mDapart.setCount(j+"");
                if(j==peopleList.size()){
                    mDapart.setIscheck(true);
                }
                else {
                    mDapart.setIscheck(false);
                }
                EventBus.getDefault().post(mChildArray);
                notifyDataSetChanged();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // ToastHelper.showToast(mContext,""+data.isIscheck());
                boolean isChecked = data.isIscheck();
                data.setIscheck(!isChecked);
                checkBox.setChecked(!isChecked);
                //     notifyDataSetChanged();
                int j=0;
                for(int i=0;i<peopleList.size();i++){
                    if(peopleList.get(i).isIscheck()){
                        j++;
                    }
                }
                mDapart.setCount(j+"");
                if(j==peopleList.size()){
                    mDapart.setIscheck(true);
                }
                else {
                    mDapart.setIscheck(false);
                }
                EventBus.getDefault().post(mChildArray);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 创建新的组视图
     *
     * @param parent
     * @return
     */
    public View newGroupView(ViewGroup parent) {
        return mInflater.inflate(mExpandedGroupLayout, parent, false);
    }

    /**
     * 创建新的子视图
     *
     * @param parent
     * @return
     */
    public View newChildView(ViewGroup parent) {
        return mInflater.inflate(mChildLayout, parent, false);
    }

    public boolean hasStableIds() {
        // 是否指定分组视图及其子视图的ID对应的后台数据改变也会保持该ID。
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // 指定位置的子视图是否可选择。
        return true;
    }
}
