package com.hulian.oa.work.file.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hulian.oa.BaseActivity;
import com.hulian.oa.R;
import com.hulian.oa.bean.Notice;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.news.adapter.NoticeAdapter;
import com.hulian.oa.utils.SPUtils;
import com.hulian.oa.work.file.admin.activity.notice.NoticeIssueActivity;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通知通告
 */
public class SecondNoticeActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    @BindView(R.id.listview)
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_send)
    LinearLayout tvSend;
    @BindView(R.id.emptyBg)
    RelativeLayout emptyBg;
    private int mCount = 1;
    private RecyclerView mRecyclerView;
    NoticeAdapter mRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_notice);
        ButterKnife.bind(this);
        initList();
    }

    private void initList() {

        //获取mRecyclerView对象
        mRecyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        //代码设置scrollbar无效？未解决！
        mRecyclerView.setVerticalScrollBarEnabled(true);
        //设置下拉刷新是否可见
        //mPullLoadMoreRecyclerView.setRefreshing(true);
        //设置是否可以下拉刷新
        //mPullLoadMoreRecyclerView.setPullRefreshEnable(true);
        //设置是否可以上拉刷新
        //mPullLoadMoreRecyclerView.setPushRefreshEnable(false);
        //显示下拉刷新
        mPullLoadMoreRecyclerView.setRefreshing(true);
        //设置上拉刷新文字
        mPullLoadMoreRecyclerView.setFooterViewText("loading");
        //设置上拉刷新文字颜色
        //mPullLoadMoreRecyclerView.setFooterViewTextColor(R.color.white);
        //设置加载更多背景色
        //mPullLoadMoreRecyclerView.setFooterViewBackgroundColor(R.color.colorBackground);
        mPullLoadMoreRecyclerView.setLinearLayout();

        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        mRecyclerViewAdapter = new NoticeAdapter(SecondNoticeActivity.this);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        getData();

    }

    @Override
    public void onRefresh() {
        Log.e("wxl", "onRefresh");
        setRefresh();
        getData();
    }

    @Override
    public void onLoadMore() {
        Log.e("wxl", "onLoadMore");
        mCount = mCount + 1;
        getData();
    }

    private void setRefresh() {
        mRecyclerViewAdapter.clearData();
        mCount = 1;
    }

    private void getData() {
        RequestParams params = new RequestParams();
        params.put("pageState", mCount * 10 - 9 + "");
        params.put("pageEnd", mCount * 10 + "");
        params.put("userId", SPUtils.get(mContext, "userId", "").toString());
        HttpRequest.postNoticeListApi(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<Notice> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<Notice>>() {
                            }.getType());
                    mRecyclerViewAdapter.addAllData(memberList);
                    if (mCount == 1 && memberList.size() == 0) {
                        emptyBg.setVisibility(View.VISIBLE);
                        mPullLoadMoreRecyclerView.setVisibility(View.GONE);
                    } else {
                        emptyBg.setVisibility(View.GONE);
                        mPullLoadMoreRecyclerView.setVisibility(View.VISIBLE);
                    }
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                //   Log.e("TAG", "请求失败=" + failuer.getEmsg());
                Toast.makeText(mContext, "请求失败=" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick({R.id.iv_back, R.id.tv_send, R.id.rl_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_title:
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send:
                // startActivity(new Intent(mContext, NoticeIssueActivity.class));
                Intent intent = new Intent(mContext, NoticeIssueActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
//            onRefresh();
        }
    }

//    新改的返回刷新qgl

    @Override
    protected void onRestart() {
        onRefresh();
        super.onRestart();
    }
}
