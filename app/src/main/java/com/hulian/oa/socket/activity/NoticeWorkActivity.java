package com.hulian.oa.socket.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hulian.oa.R;
import com.hulian.oa.activity.BaseActivity;
import com.hulian.oa.bean.Report;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.socket.adapter.NoticeWorkAdaoter;
import com.hulian.oa.utils.SPUtils;
import com.hulian.oa.views.MyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：qgl 时间： 2020/4/27 10:34
 * Describe: 工作通知
 */
public class NoticeWorkActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.listview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.notice_tv_title)
    TextView notice_tv_title;

    private int mCount = 1;
    private NoticeWorkAdaoter mAdapter;
    private List<Report> mData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticeworkactivity);
        ButterKnife.bind(this);
        initList();
    }

    private void initList() {
        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new NoticeWorkAdaoter(mData);
        mAdapter.openLoadAnimation();
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        getData();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        setRefresh();
        getData();
    }

    private void setRefresh() {
        mData.clear();
        mCount = 1;
    }

    private void getData() {
        RequestParams params = new RequestParams();
        params.put("pageStart", mCount * 10 - 10 + "");
        params.put("pageEnd", mCount * 10 + "");
        params.put("createBy", SPUtils.get(this, "userId", "").toString());
        params.put("receivePerson", SPUtils.get(this, "userId", "").toString());
        HttpRequest.getGetWorkReportList(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                swipeRefreshLayout.setRefreshing(false);
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();

                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<Report> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<Report>>() {
                            }.getType());
                    mData.addAll(memberList);
                    if (memberList.size() < 10) {
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(NoticeWorkActivity.this, "请求失败=" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onLoadMoreRequested() {
        mCount = mCount + 1;
        getData();
    }

    @OnClick({R.id.iv_back,R.id.notice_im_screen})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.notice_im_screen:
                shouDialog();
                break;
        }
    }

    public void shouDialog(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_notice_work, null);
        TextView tv_Document = view.findViewById(R.id.dialog_not_tv_gw);
        TextView tv_leave = view.findViewById(R.id.dialog_not_tv_qj);
        TextView tv_ReiBurs = view.findViewById(R.id.dialog_not_tv_bx);
        TextView tv_task = view.findViewById(R.id.dialog_not_tv_rw);
        Dialog dialog = new MyDialog(NoticeWorkActivity.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        tv_Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                notice_tv_title.setText("公文推送");
            }
        });
        tv_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                notice_tv_title.setText("请假推送");
            }
        });
        tv_ReiBurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                notice_tv_title.setText("报销推送");
            }
        });
        tv_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                notice_tv_title.setText("任务推送");
            }
        });

    }
}
