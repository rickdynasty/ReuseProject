package reuse.rick.tws.com;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.paic.lib.workspace.Model.CellLayoutAdapter;
import com.paic.lib.workspace.Model.WorkspaceData;
import com.paic.lib.workspace.Model.WorkspaceSpanSizeLookup;
import com.paic.lib.workspace.Presenter.WrokspaceJsonPresenter;
import com.paic.lib.workspace.view.DividerDecoration;
import com.paic.lib.workspace.view.IWorkspaceUI;
import com.paic.lib.workspace.widget.CellItemView;
import com.paic.lib.workspace.widget.Workspace;

public class WorkspaceActivity extends Activity implements IWorkspaceUI, View.OnClickListener {
    private static final String TAG = WorkspaceActivity.class.getSimpleName();
    private WrokspaceJsonPresenter mPresenter;
    private CellLayoutAdapter mAdapter;
    private Workspace mWorkspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workspace);
        initUI();
    }

    private void initUI() {
        mWorkspace = findViewById(R.id.navigation_area);
        mWorkspace.setBackgroundColor(Color.WHITE);
        mPresenter = new WrokspaceJsonPresenter(this);
        mAdapter = new CellLayoutAdapter(this);
        mAdapter.setCellItemOnClickListener(this);

        GridLayoutManager manager = new GridLayoutManager(this, Workspace.GRID_SPANCOUNT);
        //设置header
        manager.setSpanSizeLookup(new WorkspaceSpanSizeLookup(mAdapter, manager));

        mWorkspace.setLayoutManager(manager);
        mWorkspace.setAdapter(mAdapter);
        mPresenter.loadJsonFromAssets(this, "default_config.json");//workspace//test//default_config
    }

    @Override
    public void initWorkspace(WorkspaceData result) {
        if (result.getNeedDivider()) {
            mWorkspace.addItemDecoration(new DividerDecoration(this, Workspace.GRID_SPANCOUNT));
        }

        mAdapter.setData(result.workspaceGroups);
    }

    @Override
    public void loadJsonfailure(String errorDes, Exception e) {
        Log.e(TAG, errorDes, e);
        //失败了,就选择加载默认数据
        mPresenter.loadDefaultData();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof CellItemView) {
            Toast.makeText(this, "点击了：" + ((CellItemView) v).getTitle(), Toast.LENGTH_SHORT).show();
        }

        String jumpUri="android-app://reuse.rick.tws.com/#Intent;action=android.intent.action.SecondActivity;i.some_int=100;end";
        String jumpUri2="android-app://#Intent;action=android.intent.action.SecondActivity;category=android.intent.category.DEFAULT;S.some=systemFrom;end";
        String jumpScheme="pazwt://pamo-client/jumpClient?mt=12#retry";

        PageJump.jumpPageUri(this, jumpUri2);
    }
}
