package com.pasc.lib.base.widget.selectbank;

/**
 * 选择银行的每一项的接口。创建此接口是为了SelectBankAdapter与PayBankInfo的解耦。
 * Created by chenruihan410 on 2018/07/14.
 */
public interface ISelectBankItem {
    // 银行名称
    String getBankName();

    // 银行卡号
    String getCardNo();

    // 银行icon
    int getDrawableId();

    // 是否选中
    boolean isSelected();

    void setSelected(boolean selected);
}
