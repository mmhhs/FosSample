package com.little.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.little.picker.util.DensityUtil;
import com.little.picture.glide.GlideUtil;
import com.little.picture.util.LogUtil;
import com.little.picture.util.ToastUtil;
import com.little.popup.adapter.PopupListAdapter;
import com.little.popup.adapter.ReasonEntity;
import com.little.popup.adapter.ReasonListAdapter;
import com.little.popup.gridpasswordview.GridPasswordView;
import com.little.popup.listener.IOnDialogListener;
import com.little.popup.listener.IOnDismissListener;
import com.little.popup.listener.IOnForgetPassListener;
import com.little.popup.listener.IOnItemListener;
import com.little.popup.listener.IOnSignListener;
import com.little.popup.listener.IOnStringDialogListener;
import com.little.popup.util.AlphaUtil;
import com.little.popup.util.SignCalendar;
import com.little.popup.util.StringUtil;
import com.little.popup.util.TimeUtil;
import com.little.popup.util.ToUpperCase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PopupDialog {
    private Context context;
    private Activity activity;
    private TextView popupwindow_calendar_month;
//    private SignCalendar calendar;
//    private IOnDialogListener iOnDialogListener;
    private float showAlpha = 0.5f;//显示对话框时界面半透明度
    private float hideAlpha = 1f;//对话框关闭后界面恢复
    /**
     * 弹框半透明两种实现，0采用设置界面透明度，1采用半透明背景色
     * 设为 1时，activity不能为null
     */
    private int alphaType = 1;

    private boolean dismissOutside = false;//点击界面关闭
    private boolean dismissBackKey = false;//点击返回键关闭
    public boolean isSelectCar = false;//显示添加车辆

    private int animStyle = 0;//动画资源ID
    private String dialogTitle = "";//对话框标题
    private List<String> signTimeList;

    /**
     * 提示对话框操作数量 优先级：确定>取消>其他
     * 0不显示，1显示确定，2显示确定、取消，3显示确定、取消、其他
     */
    private int optionCount = 2;
    private String confirmStr = "";//提示对话框确定按钮名称
    private String cancelStr = "";//提示对话框取消按钮名称
    private String otherStr = "";//提示对话框其他按钮名称
    private IOnSignListener iOnSignListener;
    private IOnItemListener onItemListener;//列表对话框监听
    private IOnDialogListener onDialogListener;//提示对话框监听
    private IOnStringDialogListener onStringDialogListener;//提示对话框监听
    private IOnDismissListener onDismissListener;//对话框消失监听
    private IOnForgetPassListener onForgetPassListener;//提示对话框监听
    public PopupDialog(Context context) {
        this.context = context;
    }

    public static class Builder{

        //Required
        private Context context;
        public Builder(Context context){
            this.context = context;
        }

        //Option
        private Activity activity;
        private int alphaType = 1;//弹框半透明两种实现，0采用设置界面透明度，1采用半透明背景色
        private String dialogTitle = "";//对话框标题
        private boolean dismissOutside = false;//点击界面关闭
        private boolean dismissBackKey = false;//点击返回键关闭
        private int optionCount = 2;//提示对话框操作数量 优先级：确定>取消>其他
        private String confirmStr = "确定";//提示对话框确定按钮名称
        private String cancelStr = "取消";//提示对话框取消按钮名称
        private String otherStr = "忽略";//提示对话框其他按钮名称
        private IOnItemListener onItemListener;//列表对话框监听
        private IOnDialogListener onDialogListener;//提示对话框监听
        private IOnDismissListener onDismissListener;//对话框消失监听
        private IOnStringDialogListener onStringDialogListener;//提示对话框监听
        private IOnForgetPassListener onForgetPassListener;//提示对话框监听
        public Builder setAlphaType(int alphaType,Activity activity){
            this.alphaType = alphaType;
            this.activity = activity;
            return this;
        }

        public Builder dialogTitle(String dialogTitle){
            this.dialogTitle = dialogTitle;
            return this;
        }

        public Builder dismissOutside(boolean dismissOutside){
            this.dismissOutside = dismissOutside;
            return this;
        }

        public Builder dismissBackKey(boolean dismissBackKey){
            this.dismissBackKey = dismissBackKey;
            return this;
        }

        public Builder confirmStr(String confirmStr){
            this.confirmStr = confirmStr;
            return this;
        }

        public Builder cancelStr(String cancelStr){
            this.cancelStr = cancelStr;
            return this;
        }

        public Builder otherStr(String otherStr){
            this.otherStr = otherStr;
            return this;
        }

        public Builder optionCount(int optionCount){
            this.optionCount = optionCount;
            return this;
        }

        public Builder onItemListener(IOnItemListener onItemListener){
            this.onItemListener = onItemListener;
            return this;
        }

        public Builder onDialogListener(IOnDialogListener onDialogListener){
            this.onDialogListener = onDialogListener;
            return this;
        }
        public Builder onStringDialogListener(IOnStringDialogListener onStringDialogListener){
            this.onStringDialogListener = onStringDialogListener;
            return this;
        }
        public Builder onForgetPassListener(IOnForgetPassListener onForgetPassListener){
            this.onForgetPassListener = onForgetPassListener;
            return this;
        }
        public Builder onDismissListener(IOnDismissListener onDismissListener){
            this.onDismissListener = onDismissListener;
            return this;
        }

        public PopupDialog build(){
            return new PopupDialog(this);
        }
    }

    public PopupDialog(Builder builder){
        this.context = builder.context;
        this.alphaType = builder.alphaType;
        this.activity = builder.activity;
        this.dialogTitle = builder.dialogTitle;
        this.dismissOutside = builder.dismissOutside;
        this.dismissBackKey = builder.dismissBackKey;
        this.confirmStr = builder.confirmStr;
        this.cancelStr = builder.cancelStr;
        this.otherStr = builder.otherStr;
        this.optionCount = builder.optionCount;
        this.onItemListener = builder.onItemListener;
        this.onDialogListener = builder.onDialogListener;
        this.onStringDialogListener = builder.onStringDialogListener;
        this.onForgetPassListener = builder.onForgetPassListener;
        this.onDismissListener = builder.onDismissListener;
    }

    /**
     * 设置半透明
     * @param containerView
     */
    private void showAlpha(View containerView){
        if (alphaType==0){
            AlphaUtil.setBackgroundAlpha(activity, showAlpha);
        }else {
            containerView.setBackgroundResource(R.color.popup_transparent);
        }
    }

    /**
     * 取消半透明
     */
    private void hideAlpha(){
        if (alphaType==0){
            AlphaUtil.setBackgroundAlpha(activity, hideAlpha);
        }
    }

    /**
     * 弹框半透明两种实现-采用设置界面透明度
     * @param activity
     */
    public void setScreenAlphaStyle(Activity activity){
        this.activity = activity;
        alphaType = 0;
    }

    /**
     * 显示列表对话框
     * @param view 父视图
     * @param stringList 数据
     */
    public PopupWindow showListDialog(View view, List<String> stringList){
        PopupWindow popupWindow = getListDialog(stringList);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getListDialog(List<String> stringList){
        View view = LayoutInflater.from(context).inflate(R.layout.popup_dialog_list,null, false);
        TextView titleText = (TextView) view.findViewById(R.id.base_view_dialog_list_title);
        ListView listView = (ListView) view.findViewById(R.id.popup_dialog_list_listview);
        TextView txtAddCar = (TextView) view.findViewById(R.id.txt_add_car);
        ImageView img = (ImageView) view.findViewById(R.id.popup_dialog_header_img);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.popup_dialog_list_container_layout);
        PopupListAdapter adapter = new PopupListAdapter(context,stringList);
        listView.setAdapter(adapter);
        showAlpha(containerLayout);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        if (isSelectCar){
            txtAddCar.setVisibility(View.VISIBLE);
            titleText.setVisibility(View.GONE);
//            img.setVisibility(View.GONE);
        }else {
            titleText.setVisibility(View.GONE);
            txtAddCar.setVisibility(View.GONE);
//            img.setVisibility(View.VISIBLE);
        }
        if(dismissBackKey){
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        if(animStyle>0){
            popupWindow.setAnimationStyle(animStyle);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (onItemListener !=null){
                    onItemListener.onItem(arg2);
                }
                popupWindow.dismiss();
            }

        });
        containerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });
        txtAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogListener!=null){
                    onDialogListener.onConfirm();
                }
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideAlpha();
                if (onDismissListener !=null){
                    onDismissListener.onDismiss();
                }
            }
        });
        return popupWindow;
    }
    public PopupWindow showListDialog3(View view, List<String> stringList){
        PopupWindow popupWindow = getListDialog3(stringList);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getListDialog3(List<String> stringList){
        View view = LayoutInflater.from(context).inflate(R.layout.popup_dialog_list2,null, false);
        TextView titleText = (TextView) view.findViewById(R.id.base_view_dialog_list_title);
        ListView listView = (ListView) view.findViewById(R.id.popup_dialog_list_listview);
        TextView txtAddCar = (TextView) view.findViewById(R.id.txt_add_car);
        ImageView img = (ImageView) view.findViewById(R.id.popup_dialog_header_img);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.popup_dialog_list_container_layout);
        PopupListAdapter adapter = new PopupListAdapter(context,stringList);
        listView.setAdapter(adapter);
        showAlpha(containerLayout);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        if (isSelectCar){
            txtAddCar.setVisibility(View.VISIBLE);
            titleText.setVisibility(View.GONE);
//            img.setVisibility(View.GONE);
        }else {
            titleText.setVisibility(View.GONE);
            txtAddCar.setVisibility(View.GONE);
//            img.setVisibility(View.VISIBLE);
        }
        if(dismissBackKey){
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        if(animStyle>0){
            popupWindow.setAnimationStyle(animStyle);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (onItemListener !=null){
                    onItemListener.onItem(arg2);
                }
                popupWindow.dismiss();
            }

        });
        containerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });
        txtAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogListener!=null){
                    onDialogListener.onConfirm();
                }
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideAlpha();
                if (onDismissListener !=null){
                    onDismissListener.onDismiss();
                }
            }
        });
        return popupWindow;
    }
    public PopupWindow showListDialog1(View view, List<String> stringList){
        PopupWindow popupWindow = getListDialog2(stringList);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    String reason;

    public PopupWindow getListDialog2(List<String> strList){
        final List<ReasonEntity> list = new ArrayList<ReasonEntity>();
        View view = LayoutInflater.from(context).inflate(R.layout.base_view_dialog_list2,null, false);
        ListView listView = (ListView) view.findViewById(R.id.base_view_dialog_list_listview);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.base_view_dialog_list_container);
        TextView titleText = (TextView) view.findViewById(R.id.base_view_dialog_list_title);
        TextView confirmText = (TextView) view.findViewById(R.id.base_view_dialog_list_confirm);
        TextView cancelText = (TextView) view.findViewById(R.id.base_view_dialog_list_cancel);
        final EditText contentEdit = (EditText) view.findViewById(R.id.base_view_dialog_list_edit);
//        titleText.setText(dialogTitle);
//        if (StringUtil.isEmpty(dialogTitle)){
//            titleText.setVisibility(View.GONE);
//        }else {
//            titleText.setVisibility(View.VISIBLE);
//        }
        for (String str:strList){
            ReasonEntity reasonEntity = new ReasonEntity(str);
            list.add(reasonEntity);
        }
        final ReasonListAdapter adapter = new ReasonListAdapter(context,list);
        listView.setAdapter(adapter);
        showAlpha(containerLayout);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        if(dismissBackKey){
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        if(animStyle>0){
            popupWindow.setAnimationStyle(animStyle);
        }
        contentEdit.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                adapter.setSelected(arg2);
                reason = list.get(arg2).reason;
                LogUtil.e("reason="+reason);
                if(reason.equals("其他原因")){
                    contentEdit.setVisibility(View.VISIBLE);
                }else {
                    contentEdit.setVisibility(View.GONE);
                }
            }

        });
        confirmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.hasSelected()){
                    String content = contentEdit.getText().toString();
                    if (onStringDialogListener!=null){
                        onStringDialogListener.onConfirm(reason+"##"+content);
                    }
                    popupWindow.dismiss();
                }else {
                    ToastUtil.addToast(context,"请选择原因");
                }

            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        containerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideAlpha();
                if (onDismissListener!=null){
                    onDismissListener.onDismiss();
                }
            }
        });
        return popupWindow;
    }

    /**
     * 显示提示对话框
     * @param view 承载视图
     * @param message 提示内容
     */
    public PopupWindow showTipDialog(View view, String message){
        PopupWindow popupWindow = getTipDialog(message);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getTipDialog(String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_dialog_tip,null, false);
        TextView titleText = (TextView) view.findViewById(R.id.popup_dialog_tip_title);
        TextView messageText = (TextView) view.findViewById(R.id.popup_dialog_tip_content);
        TextView confirmText = (TextView) view.findViewById(R.id.popup_dialog_tip_confirm);
        TextView cancelText = (TextView) view.findViewById(R.id.popup_dialog_tip_cancel);
        TextView otherText = (TextView) view.findViewById(R.id.popup_dialog_tip_other);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.popup_dialog_tip_container_layout);
        TextView a = (TextView) view.findViewById(R.id.a);
        TextView b = (TextView) view.findViewById(R.id.b);
        TextView c = (TextView) view.findViewById(R.id.c);
        if(!StringUtil.isEmpty(dialogTitle)){
            titleText.setText(dialogTitle);
            titleText.setVisibility(View.VISIBLE);
        }else {
            titleText.setVisibility(View.VISIBLE);
            titleText.setText("温馨提示");
        }
        if(!StringUtil.isEmpty(message)){
            if (message.equals("提交后可在服务评价模块查询进展")){
               a.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);
                c.setVisibility(View.VISIBLE);
                messageText.setVisibility(View.GONE);
//                Html.fromHtml("提交后可在<font color='#e73706'><big><big>服务评价</big></big></font>模块查询进展")
            }else {
                messageText.setText(message);
            }

        }
        if (!StringUtil.isEmpty(confirmStr)){
            confirmText.setText(confirmStr);
        }
        if (!StringUtil.isEmpty(cancelStr)){
            cancelText.setText(cancelStr);
        }
        if (!StringUtil.isEmpty(otherStr)){
            otherText.setText(otherStr);
        }
        switch (optionCount){
            case 0:
                confirmText.setVisibility(View.GONE);
                cancelText.setVisibility(View.GONE);
                otherText.setVisibility(View.GONE);
                break;
            case 1:
                confirmText.setVisibility(View.VISIBLE);
                cancelText.setVisibility(View.GONE);
                otherText.setVisibility(View.GONE);
                break;
            case 2:
                confirmText.setVisibility(View.VISIBLE);
                cancelText.setVisibility(View.VISIBLE);
                otherText.setVisibility(View.GONE);
                break;
            case 3:
                confirmText.setVisibility(View.VISIBLE);
                cancelText.setVisibility(View.VISIBLE);
                otherText.setVisibility(View.VISIBLE);
                break;
        }
        showAlpha(containerLayout);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        if(dismissBackKey){
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        if(animStyle>0){
            popupWindow.setAnimationStyle(animStyle);
        }
        confirmText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (onDialogListener !=null){
                    onDialogListener.onConfirm();
                }
                popupWindow.dismiss();
            }

        });
        cancelText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (onDialogListener !=null){
                    onDialogListener.onCancel();
                }
                popupWindow.dismiss();
            }

        });
        otherText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (onDialogListener !=null){
                    onDialogListener.onOther();
                }
                popupWindow.dismiss();
            }

        });
        containerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideAlpha();
                if (onDismissListener !=null){
                    onDismissListener.onDismiss();
                }
            }
        });
        return popupWindow;
    }
    String cardColor = "";
    public PopupWindow showAddEngineIdDialog(View view, Context activity) {
        PopupWindow popupWindow = getAddEngineIdDialog(activity);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getAddEngineIdDialog(final Context activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_view_add_engine_id,
                null, false);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.layout_view_engine);
        TextView sure = (TextView) view.findViewById(R.id.base_view_add_engine_id_yes_button);
        TextView cancel = (TextView) view.findViewById(R.id.base_view_add_engine_id_no_button);
        final EditText editText = (EditText) view.findViewById(R.id.base_view_add_engine_id_edit);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.base_view_add_engine_id_radioGroup);
        final RadioButton radioButtonBlue = (RadioButton) view.findViewById(R.id.base_view_add_engine_id_radio_blue);
        final RadioButton radioButtonYellow = (RadioButton) view.findViewById(R.id.base_view_add_engine_id_radio_yellow);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams
                .MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,true);
        showAlpha(containerLayout);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioButtonBlue.getId()==checkedId){
                    cardColor = "02";
                    radioButtonBlue.setSelected(true);
                    radioButtonYellow.setSelected(false);
                }
                if(radioButtonYellow.getId()==checkedId){
                    cardColor = "01";
                    radioButtonBlue.setSelected(false);
                    radioButtonYellow.setSelected(true);
                }

            }
        });
        editText.setTransformationMethod(new ToUpperCase());
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        if(dismissBackKey){
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        if(animStyle>0){
            popupWindow.setAnimationStyle(animStyle);
        }
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String engineId = editText.getText().toString();
                if ( onStringDialogListener!= null) {
                    if (engineId.length()<6){
                        Toast.makeText(activity, "请输入发动机后六位",Toast.LENGTH_SHORT);
                        return;
                    }else if (StringUtil.isEmpty(cardColor)){
                        Toast.makeText(activity, "请选择车牌颜色",Toast.LENGTH_SHORT);
                        return;
                    }
                    onStringDialogListener.onConfirm(engineId+"#"+cardColor);

                }

                popupWindow.dismiss();
            }

        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onStringDialogListener != null) {
                    onStringDialogListener.onCancel();
                }
                popupWindow.dismiss();
            }

        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideAlpha();
                if (onDismissListener !=null){
                    onDismissListener.onDismiss();
                }
            }
        });
        return popupWindow;
    }
    public PopupWindow showMessageDialog(Context context,View view, String title,String content,String messageUrl,String imgUrl){
        PopupWindow popupWindow = getMessageDialog(context,title,content,messageUrl,imgUrl);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getMessageDialog(final Context activity,String title,String content, final String  messageUrl,String imgUrl) {
        View view = LayoutInflater.from(activity).inflate(R.layout.popub_dialog_message,null, false);
        ImageView draweeView = (ImageView) view.findViewById(R.id.dialog_message_draweeview);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.dialog_message_layout);
        LinearLayout closeLayout = (LinearLayout) view.findViewById(R.id.dialog_message_close_layout);
        LinearLayout textLayout = (LinearLayout) view.findViewById(R.id.dialog_message_text_layout);
        TextView txtTitle = (TextView) view.findViewById(R.id.dialog_message_title);
        TextView txtContent = (TextView) view.findViewById(R.id.dialog_message_content);
        if (!StringUtil.isEmpty(imgUrl)){
            draweeView.setVisibility(View.VISIBLE);
            textLayout.setVisibility(View.GONE);
            GlideUtil glideUtil = new GlideUtil();
            textLayout.setVisibility(View.GONE);
            txtTitle.setText(title);
            txtContent.setText(content);
            glideUtil.display(activity,imgUrl, draweeView, DensityUtil.dip2px(activity, 240), DensityUtil.dip2px(activity, 320));
//            Glide.with(activity).load(R.drawable.coupon).into(draweeView);
        }else {
//            draweeView.setVisibility(View.GONE);

        }
        showAlpha(containerLayout);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        if(dismissBackKey){
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        if(animStyle>0){
            popupWindow.setAnimationStyle(animStyle);
        }
//        draweeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!StringUtil.isEmpty(messageUrl)){
////                    Web.intent2Web(activity, messageData.getTitle(), messageData.getUrlPath());
////                    Web
//                    popupWindow.dismiss();
//                }
//            }
//        });
        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogListener!=null){
                    onDialogListener.onConfirm();
                }
                popupWindow.dismiss();
            }
        });
        closeLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }

        });
        containerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideAlpha();
                if (onDismissListener!=null){
                    onDismissListener.onDismiss();
                }
            }
        });
        return popupWindow;
    }

    public PopupWindow showIntegralSignDialog(View view, Context context, String integral,
                                              String type) {
        PopupWindow popupWindow = getIntegralSignView(context, integral, type);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getIntegralSignView(final Context context, String integral, String type) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_view,
                null, false);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.layout_view_sign_jiangli);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams
                .MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        TextView sure = (TextView) view.findViewById(R.id.base_view_sign_sure_button);
        TextView integralView = (TextView) view.findViewById(R.id.base_view_integral_update);
        TextView typeIntegral = (TextView) view.findViewById(R.id.base_view_type_integral_update);
        integralView.setText(integral);
        showAlpha(containerLayout);
        typeIntegral.setText(type);
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onDialogListener!=null){
                    onDialogListener.onConfirm();
                }
                popupWindow.dismiss();
            }

        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });
        if (dismissBackKey) {
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        return popupWindow;
    }
    /**
     * 显示提现密码弹框
     * @param view 承载视图
     * @param context 上下文
     * @param passwordType //0是提现，1是设置提现密码,2是设置提现密码确认
     * @param amount 提现金额
     * @param prePassword 上一步密码
     * @return
     */
    private int passwordType = 0;//0是提现，1是设置提现密码,2是设置提现密码确认
    private String amount = "";//提现金额
    private String prePassword = "";//上一步密码
    public PopupWindow showPasswordDialog(View view, Context context,int passwordType,String amount,String prePassword) {
        this.passwordType = passwordType;
        this.amount = amount;
        this.prePassword = prePassword;
        PopupWindow popupWindow = getPasswordDialog(context);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    private PopupWindow getPasswordDialog(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.popub_dialog_password,
                null, false);
        TextView titleText = (TextView) view.findViewById(R.id.ft_view_dialog_password_title);
        TextView messageText = (TextView) view.findViewById(R.id.ft_view_dialog_password_message);
        TextView amountText = (TextView) view.findViewById(R.id.ft_view_dialog_password_amount);
        TextView forgetText = (TextView) view.findViewById(R.id.ft_view_dialog_password_forget);
        TextView confirmText = (TextView) view.findViewById(R.id.ft_view_dialog_password_confirm);
        LinearLayout cancelLayout = (LinearLayout) view.findViewById(R.id.ft_view_dialog_password_cancel);
        final GridPasswordView gridPasswordView = (GridPasswordView) view.findViewById(R.id.ft_view_dialog_password_gridPasswordView);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.layout_view_cash_password);
        if (passwordType==0){
            forgetText.setVisibility(View.VISIBLE);
            amountText.setVisibility(View.VISIBLE);
            titleText.setText("请输入提现密码");
            messageText.setText("提现金额");
            amountText.setText(amount);
        }else if (passwordType==1){
            forgetText.setVisibility(View.GONE);
            amountText.setVisibility(View.GONE);
            titleText.setText("设置提现密码");
            messageText.setText("首次提现需要设置您的提现密码");
        }else if (passwordType==2){
            forgetText.setVisibility(View.GONE);
            amountText.setVisibility(View.GONE);
            titleText.setText("再次输入提现密码");
            messageText.setText("请再次输入提现密码");
        }
        showAlpha(containerLayout);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams
                .MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (dismissBackKey) {
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        confirmText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String passWord = gridPasswordView.getPassWord();
                if (!StringUtil.isEmpty(passWord)&&passWord.length()==6){
                    if (passwordType==1){
                        popupWindow.dismiss();
                        if (onStringDialogListener != null) {
                            onStringDialogListener.onConfirm(passWord);
                        }
                    }else if (passwordType==2){
                        if (prePassword.equals(passWord)){
                            if (onStringDialogListener != null) {
                                onStringDialogListener.onConfirm(passWord);
                            }
                            popupWindow.dismiss();
                        }else {
                            ToastUtil.addToast(context,"两次密码不一致");
                        }

                    }else {
                        if (onStringDialogListener != null) {
//                            Option.closeKeyBoard(activity);
                            onStringDialogListener.onConfirm(passWord);

                        }
                        popupWindow.dismiss();
                    }


                }else {
                    Toast.makeText(context,"请输入提现密码",Toast.LENGTH_SHORT);
                }

            }

        });
        cancelLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onStringDialogListener != null) {
                    onStringDialogListener.onCancel();
                }
                popupWindow.dismiss();
            }

        });
        containerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });
        forgetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (onForgetPassListener !=null){
                   onForgetPassListener.onClick();
               }
            }
        });

        return popupWindow;
    }
    /**
     * 签到
     */
    SignCalendar signCalendar;
    private String date1 = null;//单天日期
    private List<String> flagList = new ArrayList<String>(); //设置标记列表
    private String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式
    public PopupWindow showSignDialog(View view, Context activity) {
        PopupWindow popupWindow = getSignView(activity);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getSignView(final Context activity) {
        final View view = LayoutInflater.from(activity).inflate(R.layout.layout_view_signview,
                null, false);
        RelativeLayout containerLayout = (RelativeLayout) view.findViewById(R.id.layout_view_sign);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams
                .MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        showAlpha(containerLayout);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        // 初始化DBManager
//        dbManager = new DBManager(activity);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        date1 = formatter.format(curDate);
        popupwindow_calendar_month = (TextView) view.findViewById(R.id.popupwindow_calendar_month);
        signCalendar = (SignCalendar) view.findViewById(R.id.popupwindow_calendar);
        popupwindow_calendar_month.setText(TimeUtil.getCurrentTime("yyyy-MM-dd"));
        if (null != date) {
            int years = Integer.parseInt(date.substring(0,
                    date.indexOf("-")));
            int month = Integer.parseInt(date.substring(
                    date.indexOf("-") + 1, date.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");

            signCalendar.showCalendar(years, month);
            signCalendar.setCalendarDayBgColor(date,
                    R.drawable.calendar_date_focused);
        }

//        add("2015-11-10");
//        add("2015-11-02");
//        add("2015-12-02");
        query();


        //监听当前月份
        signCalendar.setOnCalendarDateChangedListener(new SignCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month
                        .setText(year + "年" + month + "月");
            }
        });
        if (dismissBackKey) {
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        containerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });

        return popupWindow;
    }


    public void query() {
        if (signTimeList != null) {
            List<String> signTime = signTimeList;
            for (String dateTime : signTime) {
                flagList.add(TimeUtil.formatMsgTime(dateTime));
            }

        }
        signCalendar.addMarks(signTimeList, 0);
    }
    /**
     * 红包弹框
     * @param view
     * @param activity
     * @param
     * @return
     */
    public PopupWindow showCouponDialog(final View view, Context activity) {
        PopupWindow popupWindow = getCouponView(activity);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getCouponView(final Context activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_coupon,
                null, false);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams
                .MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayout couponLayout = (LinearLayout)view.findViewById(R.id.base_view_sign_coupon_layout);
        ImageView close = (ImageView) view.findViewById(R.id.base_view_sign_coupon_close);
        couponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDialogListener!=null){
                    onDialogListener.onConfirm();
                }
                popupWindow.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });
        if (dismissBackKey) {
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        return popupWindow;
    }

    public void setSignTimeList(List<String> signTimeList) {
        this.signTimeList = signTimeList;
    }
    public void setiOnSignListener(IOnSignListener iOnSignListener) {
        this.iOnSignListener = iOnSignListener;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getAlphaType() {
        return alphaType;
    }

    public void setAlphaType(int alphaType) {
        this.alphaType = alphaType;
    }

    public boolean isDismissOutside() {
        return dismissOutside;
    }

    public void setDismissOutside(boolean dismissOutside) {
        this.dismissOutside = dismissOutside;
    }

    public boolean isDismissBackKey() {
        return dismissBackKey;
    }

    public void setDismissBackKey(boolean dismissBackKey) {
        this.dismissBackKey = dismissBackKey;
    }

    public int getAnimStyle() {
        return animStyle;
    }

    public void setAnimStyle(int animStyle) {
        this.animStyle = animStyle;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public int getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(int optionCount) {
        this.optionCount = optionCount;
    }

    public String getConfirmStr() {
        return confirmStr;
    }

    public void setConfirmStr(String confirmStr) {
        this.confirmStr = confirmStr;
    }

    public String getCancelStr() {
        return cancelStr;
    }

    public void setCancelStr(String cancelStr) {
        this.cancelStr = cancelStr;
    }

    public String getOtherStr() {
        return otherStr;
    }

    public void setOtherStr(String otherStr) {
        this.otherStr = otherStr;
    }

    public IOnItemListener getOnItemListener() {
        return onItemListener;
    }

    public void setOnItemListener(IOnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public IOnDialogListener getOnDialogListener() {
        return onDialogListener;
    }

    public void setOnDialogListener(IOnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
    }
    public IOnStringDialogListener getOnStringDialogListener() {
        return onStringDialogListener;
    }

    public void setOnStringDialogListener(IOnStringDialogListener onStringDialogListener) {
        this.onStringDialogListener = onStringDialogListener;
    }
    public void setOnForgetPassListener(IOnForgetPassListener onForgetPassListener) {
        this.onForgetPassListener = onForgetPassListener;
    }

    public void setOnDismissListener(IOnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}