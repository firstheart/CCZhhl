package com.hulian.oa.me;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hulian.oa.BaseActivity;
import com.hulian.oa.R;
import com.hulian.oa.bean.News;
import com.hulian.oa.me.l_adapter.L_CollectionNewsAdapter;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.news.adapter.NewsViewAdapter;
import com.hulian.oa.utils.SPUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//我的-》收藏
public class CollectionActivity2 extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    @BindView(R.id.lv_news)
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    private int mCount = 1;
    private RecyclerView mRecyclerView;
    L_CollectionNewsAdapter mRecyclerViewAdapter;

    private ArrayList<String> list_path;
    private ArrayList<String> list_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_collection);
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

        mRecyclerViewAdapter = new L_CollectionNewsAdapter(this);
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
        String collectUserId = SPUtils.get(this, "userId", "-1").toString();
        RequestParams params = new RequestParams();
        params.put("pageState", mCount * 10 - 9 + "");
        params.put("pageEnd", mCount * 10 + "");
        Log.d("这是用户ID", SPUtils.get(this, "userId", "-1").toString());
        HttpRequest.postNesListApi(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<News> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<News>>() {
                            }.getType());
                    mRecyclerViewAdapter.addAllData(memberList);
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                //   Log.e("TAG", "请求失败=" + failuer.getEmsg());
                Toast.makeText(CollectionActivity2.this, "请求失败=" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick({R.id.iv_back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                break;
        }
    }
}
