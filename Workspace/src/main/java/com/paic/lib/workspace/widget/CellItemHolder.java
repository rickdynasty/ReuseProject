package com.paic.lib.workspace.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.paic.lib.workspace.R;

public class CellItemHolder extends RecyclerView.ViewHolder {
    public CellItemView card;

    public CellItemHolder(View itemView) {
        super(itemView);
        if (itemView instanceof CellItemView) {
            card = (CellItemView) itemView;
        } else {
            sureCard();
        }
    }

    private void sureCard() {
        card = itemView.findViewById(R.id.metro_card);
    }
}
