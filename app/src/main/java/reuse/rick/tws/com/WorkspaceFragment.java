package reuse.rick.tws.com;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.paic.lib.workspace.Model.CellLayoutAdapter;
import com.paic.lib.workspace.Model.WorkspaceData;
import com.paic.lib.workspace.Model.WorkspaceSpanSizeLookup;
import com.paic.lib.workspace.Presenter.WrokspaceJsonPresenter;
import com.paic.lib.workspace.view.DividerDecoration;
import com.paic.lib.workspace.view.IWorkspaceUI;
import com.paic.lib.workspace.widget.CellItemView;
import com.paic.lib.workspace.widget.Workspace;
import com.pasc.lib.webpage.PascWebviewActivity;

public class WorkspaceFragment extends Fragment implements IWorkspaceUI, View.OnClickListener {
    private static final String TAG = WorkspaceFragment.class.getSimpleName();
    private WrokspaceJsonPresenter mPresenter;
    private CellLayoutAdapter mAdapter;
    private Workspace mWorkspace;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workspace, container, false);
        initView(root);

        return root;
    }

    private void initView(View root) {
        mWorkspace = root.findViewById(R.id.navigation_area);
        mWorkspace.setBackgroundColor(Color.WHITE);
        mPresenter = new WrokspaceJsonPresenter(this);
        mAdapter = new CellLayoutAdapter(getActivity());
        mAdapter.setCellItemOnClickListener(this);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), Workspace.GRID_SPANCOUNT);
        //设置header
        manager.setSpanSizeLookup(new WorkspaceSpanSizeLookup(mAdapter, manager));

        mWorkspace.setLayoutManager(manager);
        mWorkspace.setAdapter(mAdapter);
        mPresenter.loadJsonFromAssets(getActivity(), "default_config.json");//workspace//test//default_config
    }

    @Override
    public void onClick(View v) {
        if (v instanceof CellItemView) {
            Toast.makeText(getActivity(), "点击了：" + ((CellItemView) v).getTitle(), Toast.LENGTH_SHORT).show();
        }

//        String jumpUri = "android-app://reuse.rick.tws.com/#Intent;action=android.intent.action.SecondActivity;i.some_int=100;end";
////        String jumpUri2 = "android-app://#Intent;action=android.intent.action.SecondActivity;category=android.intent.category.DEFAULT;S.some=systemFrom;end";
////        String jumpScheme = "pazwt://pamo-client/jumpClient?mt=12#retry";
////
////        PageJump.jumpPageUri(getActivity(), jumpUri2);
////
////        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
////        clipboardManager.setText("wewer");

        PascWebviewActivity.startWebviewActivity(getActivity());
    }

    @Override
    public void initWorkspace(WorkspaceData result) {
        if (result.getNeedDivider()) {
            mWorkspace.addItemDecoration(new DividerDecoration(getActivity(), Workspace.GRID_SPANCOUNT));
        }

        mAdapter.setData(result.workspaceGroups);
    }

    @Override
    public void loadJsonfailure(String errorDes, Exception e) {
        Log.e(TAG, errorDes, e);
        //失败了,就选择加载默认数据
        mPresenter.loadDefaultData();
    }
}
