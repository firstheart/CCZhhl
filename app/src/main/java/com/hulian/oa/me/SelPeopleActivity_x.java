package com.hulian.oa.me;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hulian.oa.BaseActivity;
import com.hulian.oa.R;
import com.hulian.oa.bean.People;
import com.hulian.oa.bean.People_x;
import com.hulian.oa.me.l_adapter.PeopleAdapter;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

//人员
public class SelPeopleActivity_x extends BaseActivity {
    //创建显示列表的listView
    private ListView listView;
    //创建适配器对象
    private PeopleAdapter adapter;
    private String partId;
    private TextView tv_title;
    List<People> memberList = new ArrayList<>();
    List<People_x> aa = new ArrayList<>();
    private RelativeLayout iv_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_info);
        //初始化页面对象
        init();
        partId = getIntent().getStringExtra("partId");
        //将数据显示在页面上
        initDatas();
    }

    public void init() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.lv_text_view);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("选择人员");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventBus.getDefault().post(aa.get(i));
                finish();
            }
        });
    }

    private void initDatas() {
        RequestParams params = new RequestParams();
        params.put("deptId", partId);
        HttpRequest.postUserInfoByDeptId(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());

                    aa = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<People_x>>() {
                            }.getType());

                    memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<People>>() {
                            }.getType());
//                    修改的如果有自己剔除qgl
                    for (int i = 0;i<=memberList.size()-1;i++)
                    {
                        if (memberList.get(i).getUserId().equals(SPUtils.get(mContext, "userId", "").toString()))
                        {
                            memberList.remove(memberList.get(i));
                        }
                    }
                    Log.e("memberList",memberList+"");
                    adapter = new PeopleAdapter(memberList, mContext);
                    listView.setAdapter(adapter);

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
}
