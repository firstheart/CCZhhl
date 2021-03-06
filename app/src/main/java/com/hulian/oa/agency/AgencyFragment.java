package com.hulian.oa.agency;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hulian.oa.R;
import com.hulian.oa.agency.l_fragment.L_HascomFragment;
import com.hulian.oa.agency.l_fragment.L_UpcomFragment;
import com.hulian.oa.bean.AgencyCount;
import com.hulian.oa.bean.AgencyCountFinish;
import com.hulian.oa.bean.Fab;
import com.hulian.oa.bean.Fab2;
import com.hulian.oa.news.adapter.MyViewPageAdapter;
import com.hulian.oa.utils.StatusBarUtil;
import com.hulian.oa.work.file.admin.activity.expense.ExpenseExamineActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

public class AgencyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.my_tablayout)
    TabLayout myTablayout;
    @BindView(R.id.my_viewpager)
    ViewPager myViewpager;
    Unbinder unbinder;
    @BindView(R.id.tv_mengban)
    TextView tvMengban;
    @BindView(R.id.tv_agencyCount)
    TextView tvAgencyCount;
    @BindView(R.id.tv_finishCount)
    TextView tvFinishCount;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
int  mViewPagerIndex;
    public AgencyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Main1_home.
     */
    // TODO: Rename and change types and number of parameters
    public static AgencyFragment newInstance(String requestJson) {
        AgencyFragment fragment = new AgencyFragment();
        Bundle args = new Bundle();
//        args.putString("requestJson", requestJson);
//        args.putString("gid", gid);
//        args.putString("idno", idno);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            gid = getArguments().getString("gid");
//            idno=getArguments().getString("idno");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_agency, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        init();

        return view;
    }

    private void init() {
        ArrayList<String> titleDatas = new ArrayList<>();
        titleDatas.add("待办");
        titleDatas.add("已办");
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new L_UpcomFragment());
        fragmentList.add(new L_HascomFragment());
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getActivity().getSupportFragmentManager(), titleDatas, fragmentList);
        //   myTablayout.setSelectedTabIndicator(0);
        myViewpager.setAdapter(myViewPageAdapter);
        myTablayout.setupWithViewPager(myViewpager);
        //        myTablayout.setTabsFromPagerAdapter(myViewPageAdapter);
        myViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
                if(0 ==position){
                    tvAgencyCount.setTextColor(getActivity().getResources().getColor(R.color.white));
                    tvFinishCount.setTextColor(getActivity().getResources().getColor(R.color.color_xian));
                }else{
                    tvFinishCount.setTextColor(getActivity().getResources().getColor(R.color.white));
                    tvAgencyCount.setTextColor(getActivity().getResources().getColor(R.color.color_xian));
                }
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==2){//state有三种状态下文会将，当手指刚触碰屏幕时state的值为1，我们就在这个时候给mViewPagerIndex 赋值。
             //       mViewPagerIndex = myViewpager.getCurrentItem();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Fab event) {
        if (event.getTag().equals("0")) {
            tvMengban.setVisibility(View.GONE);
        } else {
            tvMengban.setVisibility(View.VISIBLE);
        }
    }
    public void onEventMainThread(AgencyCount event) {
        if(!"".equals(event.getAgencyCount())){
            tvAgencyCount.setText(event.getAgencyCount());
        }
    }
    public void onEventMainThread(AgencyCountFinish event) {
        if(!"".equals(event.getAgencyCountFinish())){
            tvFinishCount.setText(event.getAgencyCountFinish());
        }
    }
    //
//    @OnClick(R.id.iv_news)
//    public void onViewClicked() {
//        startActivity(new Intent(getActivity(), NewsActivityInfo.class));
//    }
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), ExpenseExamineActivity.class);
        intent.putExtra("type", "agency");
        startActivity(intent);
    }


    @OnClick(R.id.tv_mengban)
    public void onViewClicked2() {
        Fab2 fab2 = new Fab2();
        fab2.setTag("0");
        EventBus.getDefault().post(fab2);
    }
}
