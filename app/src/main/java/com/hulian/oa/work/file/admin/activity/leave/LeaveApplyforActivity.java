package com.hulian.oa.work.file.admin.activity.leave;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.hulian.oa.BaseActivity;
import com.hulian.oa.R;
import com.hulian.oa.bean.People;
import com.hulian.oa.bean.People_x;
import com.hulian.oa.me.SelDepartmentActivity;
import com.hulian.oa.me.SelDepartmentActivity_x;
import com.hulian.oa.net.HttpRequest;
import com.hulian.oa.net.OkHttpException;
import com.hulian.oa.net.RequestParams;
import com.hulian.oa.net.ResponseCallback;
import com.hulian.oa.utils.SPUtils;
import com.hulian.oa.utils.TimeUtils;
import com.hulian.oa.utils.ToastHelper;
import com.hulian.oa.views.CircleImagview;
import com.hulian.oa.work.file.admin.activity.document.LauncherDocumentActivity;
import com.hulian.oa.work.file.admin.activity.document.l_adapter.FullyGridLayoutManager;
import com.hulian.oa.work.file.admin.activity.document.l_adapter.L_GridImageAdapter;
import com.hulian.oa.work.file.admin.activity.document.l_fragment.L_PendFragment;
import com.hulian.oa.work.file.admin.activity.leave.l_fragment.LeaveLaunchFragment;
import com.hulian.oa.work.file.admin.activity.mail.MailWriteActivity;
import com.hulian.oa.work.file.admin.activity.meeting.SelDepartmentActivity_meet_zb_single;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 请假申请
 */
public class LeaveApplyforActivity extends BaseActivity {

    // 审批人
    @BindView(R.id.titleView)
    ImageView titleView;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.fl_content)
    FrameLayout fl_content;
    @BindView(R.id.tv_opreator)
    TextView tvOpreator;
    String  tvOpreatorCode="121";

    //抄送人
    @BindView(R.id.title11)
    ImageView title11;
    @BindView(R.id.fl_content1)
    FrameLayout fl_content1;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.copier)
    TextView copier;
    String  copierCode="121";

    private List<People> selectList2 = new ArrayList<>();
    private List<People_x> selectList2_x = new ArrayList<>();
    private OptionsPickerView reasonPicker;//时间;
    List<String> reasonlist = new ArrayList<>();
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.iv_reason_more)
    TextView ivReasonMore;
    @BindView(R.id.tv_reaseon)
    TextView tvReaseon;
    @BindView(R.id.rl_leave_reason)
    RelativeLayout rlLeaveReason;
    @BindView(R.id.iv_start_more)
    ImageView ivStartMore;
    @BindView(R.id.tv_time_start)
    TextView tvTimeStart;
    @BindView(R.id.rl_start_time)
    RelativeLayout rlStartTime;
    @BindView(R.id.iv_end_more)
    ImageView ivEndMore;
    @BindView(R.id.tv_time_end)
    TextView tvTimeEnd;
    @BindView(R.id.tv_day)
    EditText tvDay;

    @BindView(R.id.rl_duration)
    RelativeLayout rlDuration;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.rl_leave_details)
    LinearLayout rlLeaveDetails;
    @BindView(R.id.tv_document_image)
    TextView tvDocumentImage;
    @BindView(R.id.tv_approved_person)
    TextView tvApprovedPerson;
    @BindView(R.id.ci_approved_pic)
    RelativeLayout ciApprovedPic;
    @BindView(R.id.tv_copy_person_title)
    TextView tvCopyPersonTitle;
    @BindView(R.id.ci_copy_pic)
    RelativeLayout ciCopyPic;
//    @BindView(R.id.tv_copy_person)
//    TextView tvCopyPerson;
    @BindView(R.id.tv_back_instruct)
    TextView tvBackInstruct;
    private L_GridImageAdapter adapter;
    //已经选择图片
    private List<LocalMedia> selectList = new ArrayList<>();
    //照片选择最大值
    private int maxSelectNum =1;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_leave_applyfor);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mContext = this;
        init();
        initReason();
        tvReaseon.setText("事假");
    }

    private void initReason()  {
        reasonlist.add("事假");
        reasonlist.add("病假");
        reasonlist.add("年假");
        reasonlist.add("调休");
        reasonlist.add("婚假");
        reasonlist.add("产假");
        reasonlist.add("路途假");
        reasonlist.add("其它");
        reasonPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvReaseon.setText(reasonlist.get(options1));
            }
        }).setTitleText("请假类别").setContentTextSize(22).setTitleSize(22).setSubCalSize(21).build();
        reasonPicker.setPicker(reasonlist);
    }

    private void init() {

        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new L_GridImageAdapter(mContext, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new L_GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureSelector.create(LeaveApplyforActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
            }
        });

    }

    private L_GridImageAdapter.onAddPicClickListener onAddPicClickListener = new L_GridImageAdapter.onAddPicClickListener() {

        @Override
        public void onAddPicClick() {
            initSelectImage();
        }
    };

    private void initSelectImage() {
        PictureSelector.create(LeaveApplyforActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .selectionMedia(selectList)// 是否传入已选图片
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", new File(media.getPath()).length() + "");
                        Log.i("压缩图片-----》", new File(media.getCompressPath()).length() + "");

//                        Bitmap bitmap = BitmapFactory.decodeFile(media.getCompressPath());
//                        iv_document.setImageBitmap(bitmap);
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 110:
                    List<People> mList = (List<People>) data.getSerializableExtra("mList");
                    fl_content.setVisibility(View.VISIBLE);
                    tvOpreator.setText(mList.get(0).getUserName());
                    tvOpreatorCode = mList.get(0).getUserId();
                    break;
                case 120:
                    List<People> mList1 = (List<People>) data.getSerializableExtra("mList");
                    fl_content1.setVisibility(View.VISIBLE);
                    copier.setText(mList1.get(0).getUserName());
                    copierCode = mList1.get(0).getUserId();
                    break;
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_leave_reason, R.id.rl_start_time, R.id.rl_end_time, R.id.ci_approved_pic,R.id.ci_copy_pic,R.id.tv_back_instruct,R.id.iv,R.id.iv1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_leave_reason:
                reasonPicker.show();
                break;
            case R.id.rl_start_time:
                selectTime(tvTimeStart);
                break;
            case R.id.rl_end_time:
                if (TextUtils.isEmpty(tvTimeStart.getText().toString().trim())) {
                    ToastHelper.showToast(mContext, "请先选择开始时间");
                    return;
                }
                selectTime(tvTimeEnd);
                break;
            case R.id.ci_approved_pic:
//                startActivity(new Intent(mContext, SelDepartmentActivity.class));
                Intent intent = new Intent(LeaveApplyforActivity.this, SelDepartmentActivity_meet_zb_single.class);
                startActivityForResult(intent,110);

                break;
            case R.id.ci_copy_pic:
//                startActivity(new Intent(mContext, SelDepartmentActivity_x.class));
                Intent intent1 = new Intent(LeaveApplyforActivity.this, SelDepartmentActivity_meet_zb_single.class);
                startActivityForResult(intent1,120);
                break;
            case R.id.tv_back_instruct:
                postData();
                break;
            case R.id.iv:
                fl_content.setVisibility(View.GONE);
                tvOpreatorCode = "";
                break;
            case R.id.iv1:
                fl_content1.setVisibility(View.GONE);
                copierCode = "";
                break;
        }

    }

    private void postData() {
        if("请选择请假类别".equals(tvReaseon.getText().toString().trim())){
            ToastHelper.showToast(mContext,"请选择请假类别");
            return;
        }
        if("请选择开始时间".equals(tvTimeStart.getText().toString().trim())){
            ToastHelper.showToast(mContext,"请选择开始时间");
            return;
        }
        if("请选择结束时间".equals(tvTimeStart.getText().toString().trim())){
            ToastHelper.showToast(mContext,"请选择结束时间");
            return;
        }
        if(TextUtils.isEmpty(etContent.getText().toString().trim())){
            ToastHelper.showToast(mContext,"请填写请假事由");
            return;
        }
        if(TextUtils.isEmpty(tvOpreatorCode)){
            ToastHelper.showToast(mContext,"请选择审批人");
            return;
        }
        if(TextUtils.isEmpty(copierCode)){
            ToastHelper.showToast(mContext,"请选择抄送人");
            return;
        }
        showDialogLoading();
        RequestParams params = new RequestParams();
        params.put("createBy", SPUtils.get(mContext, "userId", "").toString());
        params.put("copier", copierCode);
        params.put("approver", tvOpreatorCode);
        params.put("describe", etContent.getText().toString());
        params.put("duration", tvDay.getText().toString());
        params.put("startTime",tvTimeStart.getText().toString());
        params.put("endTime",tvTimeEnd.getText().toString());
        params.put("cause",tvReaseon.getText().toString());
        List<File> fils = new ArrayList<>();
        for (LocalMedia imgurl : selectList) {
            fils.add(new File(imgurl.getPath()));
        }
        HttpRequest.post_sendLeave(params, fils, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
dismissDialogLoading();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    ToastHelper.showToast(mContext, result.getString("msg"));
                    if (result.getString("code").equals("0")) {
                        EventBus.getDefault().post(new LeaveLaunchFragment());
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                //   Log.e("TAG", "请求失败=" + failuer.getEmsg());
                dismissDialogLoading();
                Toast.makeText(mContext, "请求失败=" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectTime(TextView textView) {
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //设置时间
                textView.setText(getTime(date));
                if (!"请选择开始时间".equals(tvTimeStart.getText().toString().trim()) && !"请选择结束时间".equals(tvTimeEnd.getText().toString().trim())) {
//                   if(!TimeUtils.compareTwoTime2(tvTimeStart.getText().toString().trim(),tvTimeEnd.getText().toString().trim())){
//                       ToastHelper.showToast(mContext,"请选择不小于开始时间的结束时间");
//                       tvDay.setText("");
//                   }
//                   else
                    if (TimeUtils.differentDaysByMillisecond(tvTimeStart.getText().toString().trim(), tvTimeEnd.getText().toString().trim()) <0) {
                        ToastHelper.showToast(mContext, "请选择不小于开始时间的结束时间");
                        tvDay.setText("");
                    } else
                        tvDay.setText((1 + TimeUtils.differentDaysByMillisecond(tvTimeStart.getText().toString().trim(), tvTimeEnd.getText().toString().trim()))+"");
                }
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();
        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    // 执行人
    public void onEventMainThread(People event) {
        selectList2.clear();
        selectList2.add(event);
        String uids = "";
        String uname = "";
        for (People params1 : selectList2) {
            uids += params1.getUserId() + ",";
            uname += params1.getUserName() + ",";
        }
//        fl_content.setVisibility(View.VISIBLE);
//        tvOpreator.setText(uname.substring(0, uname.length() - 1));
//        tvOpreatorCode = uids.substring(0, uids.length() - 1);
        //     Toast.makeText(this, uids.substring(0,uids.length()-1), Toast.LENGTH_SHORT).show();
    }
//
//    // 抄送人
    public void onEventMainThread(People_x event_x) {
        selectList2_x.clear();
        selectList2_x.add(event_x);
        String uids = "";
        String uname = "";
        for (People_x params_x1 : selectList2_x) {
            uids += params_x1.getUserId();
            uname += params_x1.getUserName();
        }
//        fl_content1.setVisibility(View.VISIBLE);
//        copier.setText(uname);
//        copierCode = uids;
        //    Toast.makeText(this, uids.substring(0,uids.length()-1), Toast.LENGTH_SHORT).show();
    }


}
