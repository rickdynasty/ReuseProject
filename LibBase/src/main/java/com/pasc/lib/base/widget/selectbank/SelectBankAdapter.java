package com.pasc.lib.base.widget.selectbank;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pasc.lib.base.R;

import java.util.List;

/**
 * Created by ex-wuhaiping001 on 2017/11/14.
 */

public class SelectBankAdapter extends BaseQuickAdapter<ISelectBankItem, BaseViewHolder> {
    public SelectBankAdapter(@Nullable List<ISelectBankItem> data) {
        super(R.layout.item_bank_default_payed, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ISelectBankItem item) {

        helper.setText(R.id.tv_bank_default, item.getBankName() + "(" + item.getCardNo() + ")");
        helper.setImageResource(R.id.iv_bank_icon, item.getDrawableId());
        helper.setChecked(R.id.cb_select_default, item.isSelected());
    }
}
