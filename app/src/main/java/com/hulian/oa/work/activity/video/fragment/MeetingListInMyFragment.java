package com.hulian.oa.work.activity.video.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hulian.oa.R;
import com.hulian.oa.bean.Report;
import com.hulian.oa.bean.VideoMeeting;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.utils.SPUtils;
import com.hulian.oa.work.activity.video.activity.VideoConferenceActivity;
import com.hulian.oa.work.activity.video.adapter.MeetingInMyAdapter;
import com.hulian.oa.work.adapter.WriteReportAdapter;
import com.hulian.oa.work.fragment.ReadReportFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * Created by 陈泽宇 on 2020/4/20
 * Describe:视频会议列表 (我参与的)
 */
public class MeetingListInMyFragment extends Fragment implements  BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.listview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private int mCount = 1;
    Unbinder unbinder;
    private MeetingInMyAdapter mAdapter;
    private List<VideoMeeting> mData = new ArrayList<>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_read_report, container, false);
        unbinder = ButterKnife.bind(this, view);
        initList();
        return view;
    }

    private void initList() {
        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new MeetingInMyAdapter(mData);
        mAdapter.openLoadAnimation();
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this,mRecyclerView);
        mAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.list_empty, null));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        getData();

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
//        RequestParams params = new RequestParams();
//        params.put("pageStart", mCount*10-10 + "");
//        params.put("pageEnd", mCount * 10 + "");
//        params.put("createBy", SPUtils.get(getActivity(), "userId", "").toString());
//        params.put("receivePerson", SPUtils.get(getActivity(), "userId", "").toString());
//        HttpRequest.getGetWorkReportList(params, new ResponseCallback() {
//            @Override
//            public void onSuccess(Object responseObj) {
//                swipeRefreshLayout.setRefreshing(false);
//                //需要转化为实体对象
//                Gson gson = new GsonBuilder().serializeNulls().create();
//
//                try {
//                    JSONObject result = new JSONObject(responseObj.toString());
//                    List<VideoMeeting> memberList = gson.fromJson(result.getJSONArray("data").toString(),
//                            new TypeToken<List<Report>>() {
//                            }.getType());
//                    mData.addAll(memberList);
//                    if (memberList.size()<10){
//                        mAdapter.loadMoreEnd();
//                    }else {
//                        mAdapter.loadMoreComplete();
//                    }
//                    mAdapter.notifyDataSetChanged();
//
//                    ((VideoConferenceActivity)getActivity()).setListSize(mData.size(),0);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(OkHttpException failuer) {
//                Toast.makeText(getActivity(), "请求失败=" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
//            }
//        });

        VideoMeeting videoMeeting = new VideoMeeting();
        videoMeeting.setDay("15");
        videoMeeting.setMonth("04月");
        videoMeeting.setTitle("王俊杰发起会议");
        videoMeeting.setNumber("00089872");
        videoMeeting.setStartTime("9:30");
        videoMeeting.setStopTime("10:03");
        videoMeeting.setState("1");
        mData.add(videoMeeting);

        VideoMeeting videoMeeting2 = new VideoMeeting();
        videoMeeting2.setDay("15");
        videoMeeting2.setMonth("04月");
        videoMeeting2.setTitle("王俊杰发起会议");
        videoMeeting2.setNumber("00089872");
        videoMeeting2.setStartTime("9:30");
        videoMeeting2.setStopTime("10:03");
        videoMeeting2.setState("2");
        mData.add(videoMeeting2);

        mAdapter.loadMoreEnd();
        mAdapter.notifyDataSetChanged();

        ((VideoConferenceActivity)getActivity()).setListSize(mData.size(),0);
    }


    @Override
    public void onLoadMoreRequested() {

        mCount = mCount + 1;
        getData();
    }
}
