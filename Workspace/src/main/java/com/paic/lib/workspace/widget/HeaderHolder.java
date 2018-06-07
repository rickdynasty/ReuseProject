package com.paic.lib.workspace.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paic.lib.workspace.R;

public class HeaderHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView openView;
    public ImageView groupIcon;
    public View headerView;

    public HeaderHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        titleView = itemView.findViewById(R.id.header_title);
        openView = itemView.findViewById(R.id.header_switch);
        groupIcon = itemView.findViewById(R.id.g_icon);
        headerView = itemView.findViewById(R.id.workspace_group_header);
    }
}
