package com.pasc.lib.base.widget.selectbank;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pasc.lib.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ex-wuhaiping001 on 2017/11/14.
 */

public class SelectDefaultBankPop extends PopupWindow implements View.OnClickListener {

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private View mBack;
    private RecyclerView mRvBank;

    private SelectBankAdapter mAdapter;

    private List<ISelectBankItem> mDatas;

    private ISelectBank mISelectBank;

    public SelectDefaultBankPop(Context context) {
        super(context);
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }

    public SelectDefaultBankPop(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    private void initView() {
        View view = mLayoutInflater.inflate(R.layout.pop_select_card, null);
        mBack = view.findViewById(R.id.iv_back);
        mRvBank = (RecyclerView) view.findViewById(R.id.recyclerview);

        mBack.setOnClickListener(this);

        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setOutsideTouchable(true);
        this.setFocusable(false);
        this.setBackgroundDrawable(new ColorDrawable(0x55000000));
        this.setAnimationStyle(R.style.style_pop_select_bank);

        mDatas = new ArrayList<>();
        mAdapter = new SelectBankAdapter(mDatas);
        mRvBank.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRvBank.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("pop", "" + position);
                if (mISelectBank != null) {
                    mISelectBank.invokerList(resetSelectedStutas(adapter.getData(), position));
                }
                mAdapter.notifyDataSetChanged();
                dismiss();
            }
        });

    }

    public void setNewDatas(List<ISelectBankItem> datas) {
        mAdapter.setNewData(datas);
        mAdapter.notifyDataSetChanged();
    }

    public void setIselectBank(ISelectBank selectBank) {
        this.mISelectBank = selectBank;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            this.dismiss();
        }
    }

    /**
     * 重新选择默认支付银行
     *
     * @param datas
     * @param position
     * @return
     */
    private List<ISelectBankItem> resetSelectedStutas(List<ISelectBankItem> datas, int position) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setSelected(false);
        }
        datas.get(position).setSelected(true);
        return datas;
    }

    public interface ISelectBank {

        void invokerList(List<ISelectBankItem> datas);
    }
}
