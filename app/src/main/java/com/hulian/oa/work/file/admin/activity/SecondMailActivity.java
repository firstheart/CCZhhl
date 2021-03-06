package com.hulian.oa.work.file.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hulian.oa.BaseActivity;
import com.hulian.oa.R;
import com.hulian.oa.bean.SecondMail_bean_x;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.utils.SPUtils;
import com.hulian.oa.work.file.admin.activity.mail.MailWriteActivity;
import com.hulian.oa.work.file.admin.activity.mail.l_adapter.L_MailReciveAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SecondMailActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    @BindView(R.id.tv_write_mail)
    TextView tv_write_mail;
    @BindView(R.id.lv_mail)
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    @BindView(R.id.iv_back_x)
    FrameLayout iv_back_x;
    @BindView(R.id.main_drawer_layout)
    DrawerLayout mainDrawerLayout;
    @BindView(R.id.tv_receiver)
    TextView tvReceiver;
    @BindView(R.id.emptyBg)
    RelativeLayout emptyBg;
    private int mCount = 1;
    private RecyclerView mRecyclerView;
    L_MailReciveAdapter mRecyclerViewAdapter;
    Unbinder unbinder;

    /**
     * 邮件收发
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_mail);
        ButterKnife.bind(this);
//        mainDrawerLayout.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View view, DragEvent dragEvent) {
//                if(dragEvent.)
//                return false;
//            }
//        });
        initList();
        mainDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int arg0) {
                Log.e("zhangshuli", "statechange");
            }

            @Override
            public void onDrawerSlide(View arg0, float arg1) {
                Log.e("zhangshuli", "slide");

            }

            @Override
            public void onDrawerOpened(View arg0) {
                tv_write_mail.setBackgroundResource(R.mipmap.mail_shrink_icon);
            }

            @Override
            public void onDrawerClosed(View arg0) {
                tv_write_mail.setBackgroundResource(R.mipmap.mail_expand_icon);
            }
        });
    }

    @OnClick({R.id.tv_apply, R.id.iv_back_x, R.id.tv_write_mail, R.id.main_right_drawer_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_apply:
                startActivity(new Intent(SecondMailActivity.this, MailWriteActivity.class));
                break;
            case R.id.tv_write_mail:
                //     startActivity(new Intent(SecondMailActivity.this, MailWriteActivity.class));
                mainDrawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.iv_back_x:
                finish();
            case R.id.main_right_drawer_layout:
                mainDrawerLayout.closeDrawer(Gravity.RIGHT);
                break;
        }

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
        mPullLoadMoreRecyclerView.setPushRefreshEnable(false);
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
        mRecyclerViewAdapter = new L_MailReciveAdapter(this);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
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

    }

    private void setRefresh() {
        mRecyclerViewAdapter.clearData();
        mCount = 1;
    }

    private void getData() {
        RequestParams params = new RequestParams();
        params.put("username", SPUtils.get(SecondMailActivity.this, "email", "").toString());
        params.put("password", "123456");
        HttpRequest.post_FindInboxInfo(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<SecondMail_bean_x> memberList = gson.fromJson(result.getJSONArray("data").toString(), new TypeToken<List<SecondMail_bean_x>>() {
                    }.getType());
                    mRecyclerViewAdapter.addAllData(memberList);
                    tvReceiver.setText(memberList.size() + "");
                    if(memberList.size() ==0){
                        emptyBg.setVisibility(View.VISIBLE);
                        mPullLoadMoreRecyclerView.setVisibility(View.GONE);
                    }
                    else {
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
                Toast.makeText(SecondMailActivity.this, "请求失败=" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}

