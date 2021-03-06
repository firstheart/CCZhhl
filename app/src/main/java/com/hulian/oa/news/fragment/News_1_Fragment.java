package com.hulian.oa.news.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.library.banner.layoutmanager.CenterScrollListener;
import com.example.library.banner.layoutmanager.OverFlyingLayoutManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hulian.oa.LoginActivity;
import com.hulian.oa.MainActivity;
import com.hulian.oa.R;
import com.hulian.oa.bean.News;
import com.hulian.oa.bean.User;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.net.Urls;
import com.hulian.oa.news.adapter.LocalDataAdapter;
import com.hulian.oa.news.adapter.NewsViewAdapter;
import com.hulian.oa.utils.SPUtils;
import com.hulian.oa.work.file.admin.activity.PostOrderActivity;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class News_1_Fragment extends Fragment implements OnBannerListener, PullLoadMoreRecyclerView.PullLoadMoreListener {

    RecyclerView recyclerView;
    OverFlyingLayoutManager mOverFlyingLayoutManager;
    Handler mHandler;
    Runnable mRunnable;
    int currentPosition = 0;

    @BindView(R.id.listview)
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private int mCount = 1;
    private RecyclerView mRecyclerView;
    NewsViewAdapter mRecyclerViewAdapter;
    private ArrayList<String> list_path=new ArrayList<>();
    private ArrayList<String> list_title;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recycler_banner)
    RecyclerView recycler_banner;
    Unbinder unbinder;
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_news_1, null);
        unbinder = ButterKnife.bind(this, rootView);
       // initData();
     //   initView();

        initList();
        return rootView;
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
        mRecyclerViewAdapter = new NewsViewAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);

        getData();

    }

    private void initView() {
//        //  banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//        banner.setImageLoader(new MyLoader());
//        banner.setBannerAnimation(Transformer.Default);
//    //    banner.setBannerTitles(list_title);
//        banner.setDelayTime(3000);
//        banner.isAutoPlay(true);
//        banner.setIndicatorGravity(BannerConfig.CENTER);
//        banner.setBannerAnimation(Transformer.DepthPage);
//        banner.setImages(list_path)
//                .setOnBannerListener(this)
//                .start();
        mOverFlyingLayoutManager = new OverFlyingLayoutManager(0.75f, 385, OverFlyingLayoutManager.HORIZONTAL);

        recycler_banner.setAdapter(new LocalDataAdapter(getActivity(),list_path));
        recycler_banner.setLayoutManager(mOverFlyingLayoutManager);

        recycler_banner.addOnScrollListener(new CenterScrollListener());
        mOverFlyingLayoutManager.setOnPageChangeListener(new OverFlyingLayoutManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                currentPosition++;
                Log.d("recyclerBanner", currentPosition + " ");
                mOverFlyingLayoutManager.scrollToPosition(currentPosition);
                //  recyclerView.smoothScrollToPosition(currentPosition);
                mHandler.postDelayed(this, 3000);
            }
        };
        mHandler.postDelayed(mRunnable, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.banner)
    public void onViewClicked() {
    }

    @Override
    public void OnBannerClick(int position) {
     //   Toast.makeText(getActivity(), "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
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

    //新加的用户IDqgl
    private void getData() {
        RequestParams params = new RequestParams();
        params.put("pageState", mCount*10-9 + "");
        params.put("pageEnd", mCount * 10 + "");
        params.put("createBy", SPUtils.get(getActivity(), "userId", "").toString());
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
                    if(mCount==1){
                        list_path.removeAll(list_path);
                        list_path.add(memberList.get(0).getJournalismImage());
                        list_path.add(memberList.get(1).getJournalismImage());
                        list_path.add(memberList.get(2).getJournalismImage());
                        initView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                //   Log.e("TAG", "请求失败=" + failuer.getEmsg());
                Toast.makeText(getActivity(), "请求失败=" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    /**
     * 网络加载图片
     * 使用了Glide图片加载框架
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .into(imageView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("11111","11111");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("22222","22222");

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e("33333","333333");

    }
}
