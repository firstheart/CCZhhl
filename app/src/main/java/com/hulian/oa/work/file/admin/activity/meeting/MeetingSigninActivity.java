package com.hulian.oa.work.file.admin.activity.meeting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hulian.oa.BaseActivity;
import com.hulian.oa.R;
import com.hulian.oa.bean.Meeting;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.utils.SPUtils;
import com.hulian.oa.utils.TimeUtils;
import com.hulian.oa.utils.ToastHelper;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会议签到
 */


public class MeetingSigninActivity extends BaseActivity {

    @BindView(R.id.et_title)
    TextView et_title;
    @BindView(R.id.tv_part_time)
    TextView tv_part_time;
    @BindView(R.id.tv_part_count)
    TextView tv_part_count;
    @BindView(R.id.tv_part_person)
    TextView tv_part_person;
    @BindView(R.id.tv_back_instruct)
    TextView tvBackInstruct;
    @BindView(R.id.tv_meet_room_num)
    TextView tvMeetRoomNum;
    @BindView(R.id.tv_meet_count)
    TextView tvMeetCount;
    @BindView(R.id.et_signType)
    TextView etSignType;
    @BindView(R.id.tv_part_lianx)
    TextView tvPartLianx;
    @BindView(R.id.tv_part_lianx_phone)
    TextView tvPartLianxPhone;
    @BindView(R.id.tv_part_time2)
    TextView tvPartTime2;
    @BindView(R.id.et_content)
    TextView et_content;
    private int REQUEST_CODE_SCAN = 991;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.work_meeting_signin);
        ButterKnife.bind(this);

        getData();

    }


    @OnClick({R.id.tv_back_instruct, R.id.iv_back, R.id.rl_title})
    public void onViewClicked(View view) {
//        Intent intent=new Intent(MeetingSigninActivity.this, MainActivity.class);
//        intent.putExtra("isgowork", true);
//        startActivity(intent);
//        finish();

        switch (view.getId()) {
            case R.id.tv_back_instruct:
                // sendData();
                Intent intent = new Intent(MeetingSigninActivity.this, CaptureActivity.class);
                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                 * 也可以不传这个参数
                 * 不传的话  默认都为默认不震动  其他都为true
                 * */

                ZxingConfig config = new ZxingConfig();
                config.setShowbottomLayout(false);//底部布局（包括闪光灯和相册）
                //config.setPlayBeep(true);//是否播放提示音
                //config.setShake(true);//是否震动
                //config.setShowAlbum(true);//是否显示相册
                //config.setShowFlashLight(true);//是否显示闪光灯
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_title:
                finish();
                break;
        }
    }

    private void getData() {

        RequestParams params = new RequestParams();
        params.put("meetingId", getIntent().getStringExtra("id"));

        HttpRequest.postMeeting(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    Meeting meeting = gson.fromJson(result.getJSONObject("data").toString(),
                            new TypeToken<Meeting>() {
                            }.getType());
                    if ("0".equals(meeting.getSignType())) {
                        tvBackInstruct.setVisibility(View.VISIBLE);
                        etSignType.setText("签到");
                    } else {
                        etSignType.setText("非签到");
                        tvBackInstruct.setVisibility(View.INVISIBLE);
                    }

                    et_title.setText(meeting.getMeetingTheme());
                    tv_part_time.setText(TimeUtils.getDateToString(meeting.getMeetingTimeBegin()));
                    tvPartTime2.setText(TimeUtils.getDateToString(meeting.getMeetingTimeEnd()));
                    tvPartLianx.setText(result.getJSONObject("data").getString("meetingContacts"));
                    tvPartLianxPhone.setText(result.getJSONObject("data").getString("meetingContactsPhone"));
                    et_content.setText(result.getJSONObject("data").getString("meetingContent"));
                    tv_part_count.setText(meeting.getParticipants().size() + "人");
                    String nameListStr = "";
                    for (int i = 0; i < meeting.getParticipants().size(); i++) {
                        if (i == 0) {
                            nameListStr = meeting.getParticipants().get(i).getParticipantName();
                        } else {
                            nameListStr = nameListStr + "," + meeting.getParticipants().get(i).getParticipantName();
                        }
                        if (meeting.getParticipants().get(i).getParticipantId().equals(SPUtils.get(mContext, "userId", "-1").toString())) {
                            if (meeting.getParticipants().get(i).getSignStatus().equals("1")) {
                                tvBackInstruct.setVisibility(View.GONE);
                            }
                        }
                    }
                    tvMeetRoomNum.setText(result.getJSONObject("data").getString("meetingRoomLocation"));
                    tvMeetCount.setText("容纳人数：" + result.getJSONObject("data").getString("galleryful"));
                    tv_part_person.setText(nameListStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });

    }

    private void sendData() {
        String userid = SPUtils.get(this, "userId", "-1").toString();
        RequestParams params = new RequestParams();
        params.put("participantId", userid);
        params.put("meetingId", getIntent().getStringExtra("id"));

        HttpRequest.postMeetingsignIn(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (TextUtils.equals(result.getString("code"), "0")) {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                //  result.setText("扫描结果为：" + content);
                ToastHelper.showToast(mContext, content);
                //  sendData();
            }
        }
    }
}
