package com.hulian.oa.work.activity.meeting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hulian.oa.activity.BaseActivity;
import com.hulian.oa.R;
import com.hulian.oa.bean.People;
import com.hulian.oa.me.adapter.PeopleAdapter;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.work.activity.meeting.l_adapter.MyPeopleAdapter_x;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

//人员
public class SelPeopleActivity_meet_people_x extends BaseActivity {
    //创建显示列表的listView
    private ListView listView;
    //创建适配器对象
    private PeopleAdapter adapter;
    private String partId;
    private TextView tv_title;
    private Button bt_commit;
    private MyPeopleAdapter_x mMyAdapter;
    //监听来源
    public boolean mIsFromItem = false;
    List<People> memberList=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_sel_people_x);
        //初始化页面对象
        initView();
        partId=getIntent().getStringExtra("partId");
        //将数据显示在页面上
        initDatas();

    }
    /**
     * view初始化
     */
    private void initView() {
        listView = (ListView) findViewById(R.id.list_main);
        bt_commit=findViewById(R.id.bt_commit);
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<People> resultmemberList=new ArrayList<>();
                for(People people:memberList){
                    if(people.isIscheck())
                    resultmemberList.add(people);
                }
                EventBus.getDefault().post(resultmemberList);
                finish();
            }
        });
    }

    private void initDatas() {
        RequestParams params = new RequestParams();
        params.put("deptId",partId);
        HttpRequest.postUserInfoByDeptId(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                     memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<People>>() {
                            }.getType());
                    initViewOper(memberList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(mContext, "请求失败=" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 数据绑定
     */
    private void initViewOper(List<People> models ) {
        mMyAdapter = new MyPeopleAdapter_x(models, this, new SelPeopleActivity_meet_people_x.AllCheckListener() {
            @Override
            public void onCheckedChanged(boolean b) {
                //根据不同的情况对maincheckbox做处理
            }
        });
        listView.setAdapter(mMyAdapter);
        //全选的点击监听
    }
    //对item导致maincheckbox改变做监听
    public interface AllCheckListener {
        void onCheckedChanged(boolean b);
    }
}
