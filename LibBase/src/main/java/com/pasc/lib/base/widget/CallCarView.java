package com.pasc.lib.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.lib.base.R;

/**
 * 一键召车操作控件集合View
 * Created by duyuan797 on 18/2/10.
 * Modified by chenruihan410 on 2018/7/12. 修改依赖ButterKnife的代码。
 */
public class CallCarView extends RelativeLayout implements View.OnClickListener {

    View viewAddress;
    View viewCall;
    View viewCarInfo;
    EditText etStart;
    EditText etEnd;
    TextView tvCallCar;
    View viewRoute;
    ImageView ivEditRoute;
    TextView tvRoute;
    Button btnCancelCar;

    private OnCallCarListener listener;
    private int status;

    public @interface STATUS {
        int INIT = 0;
        int WAITING = 1;
        int CALL_SUCCESS = 2;
        int CANCEL_SUCCESS = 3;
        int READY_CALL = 4;
    }

    public CallCarView(Context context) {
        this(context, null);
    }

    public CallCarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_call_car, null);
        viewAddress = view.findViewById(R.id.view_address);
        viewCall = view.findViewById(R.id.view_call);
        viewCarInfo = view.findViewById(R.id.view_car_info);
        etStart = (EditText) view.findViewById(R.id.et_start);
        etEnd = (EditText) view.findViewById(R.id.et_end);
        tvCallCar = (TextView) view.findViewById(R.id.tv_call_car);
        viewRoute = view.findViewById(R.id.rl_route);
        ivEditRoute = (ImageView) view.findViewById(R.id.iv_edit_route);
        tvRoute = (Button) view.findViewById(R.id.tv_route);
        btnCancelCar = (Button) view.findViewById(R.id.btn_cancel_car);

        etStart.setOnClickListener(this);
        etEnd.setOnClickListener(this);
        tvCallCar.setOnClickListener(this);
        btnCancelCar.setOnClickListener(this);
        ivEditRoute.setOnClickListener(this);

        addView(view);
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }

        int id = v.getId();
        if (id == R.id.et_start) {
            listener.onStartAddrClick();
        } else if (id == R.id.et_end) {
            listener.onEndAddrClick();
        } else if (id == R.id.tv_call_car) {
            listener.onCallCarClick();
        } else if (id == R.id.btn_cancel_car) {
            listener.onCancelCarClick();
        } else if (id == R.id.iv_edit_route) {
            setStatus(STATUS.INIT);
            listener.onClickEditRoute();
        }
    }

    /**
     * 根据不同状态显示不同的操作View
     */
    public void setStatus(int status) {
        this.status = status;
        switch (status) {
            case STATUS.WAITING:
                viewCarInfo.setVisibility(GONE);
                viewAddress.setVisibility(GONE);
                viewCall.setVisibility(VISIBLE);
                btnCancelCar.setVisibility(VISIBLE);
                tvCallCar.setVisibility(GONE);
                viewRoute.setVisibility(GONE);
                break;
            case STATUS.CALL_SUCCESS:
                viewCarInfo.setVisibility(VISIBLE);
                viewAddress.setVisibility(GONE);
                viewCall.setVisibility(GONE);
                viewRoute.setVisibility(GONE);
                break;
            case STATUS.INIT:
                viewAddress.setVisibility(VISIBLE);
                viewCarInfo.setVisibility(GONE);
                viewCall.setVisibility(GONE);
                viewRoute.setVisibility(GONE);
                etEnd.setText("");
                break;
            case STATUS.READY_CALL:
                viewCall.setVisibility(VISIBLE);
                viewAddress.setVisibility(GONE);
                viewCarInfo.setVisibility(GONE);
                tvCallCar.setVisibility(VISIBLE);
                viewRoute.setVisibility(VISIBLE);
                tvRoute.setText(etStart.getText().toString() + "-" + etEnd.getText().toString());
                btnCancelCar.setVisibility(GONE);
                break;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStartAddr(String addr) {
        etStart.setText(addr);
    }

    public String getStartAddr() {
        return etStart.getText().toString();
    }

    public void setEndAddr(String addr) {
        etEnd.setText(addr);
    }

    public String getEndAddr() {
        return etEnd.getText().toString();
    }

    public void setOnCallCarListener(OnCallCarListener listener) {
        this.listener = listener;
    }

    public interface OnCallCarListener {
        void onStartAddrClick();

        void onEndAddrClick();

        void onCallCarClick();

        void onCancelCarClick();

        void onClickEditRoute();
    }
}
