package reuse.rick.tws.com;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.paic.lib.workspace.Model.HotseatDisplayItem;
import com.paic.lib.workspace.ToastFragment;
import com.paic.lib.workspace.widget.Hotseat;
import com.paic.lib.workspace.widget.HotseatItemView;
import com.pasc.lib.webpage.behavior.BehaviorManager;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkspaceActivity extends FragmentActivity implements Hotseat.OnHotseatClickListener {
    //onSaveInstanceState接口对fragment现场保存的标识，为解决单activity多fragment界面现场恢复异常【这个标识copy from system】
    protected static final String FRAGMENTS_TAG = "android:support:fragments";

    private static final String TAG = WorkspaceActivity.class.getSimpleName();

    private static WorkspaceActivity mInstance = null;
    private final int INIT_CAPACITY_HOTSEAT_ITME_COUNT = 3;

    protected Hotseat mHotseat;
    protected ArrayList<HotseatDisplayItem> mHotseatDisplayInfos = new ArrayList<>(INIT_CAPACITY_HOTSEAT_ITME_COUNT);

    //首页tab fragment缓存
    private HashMap<String, Fragment> mHotseatFragmentsCache = new HashMap<>(INIT_CAPACITY_HOTSEAT_ITME_COUNT);
    //需要记切换前的tagIndex,方便隐藏之前的然后显示新的
    private String mSaveClassId = "";

    //工作台下标
    public static final String CLASS_ID_WORK = "workspace_tab";
    public static final int TAB_WORK = 0;
    //消息下标
    public static final String CLASS_ID_MESSAGE = "session_tab";
    public static final int TAB_MESSAGE = TAB_WORK + 1;
    //消息下标
    public static final String CLASS_ID_SETTINGS = "settings_tab";
    public static final int TAB_SETTINGS = TAB_WORK + 1;
    // 默认的TAB下标
    public static final String DEFAULT_TAB_CLASS_ID = CLASS_ID_WORK;
    public static final int DEFAULT_TAB_INDEX = TAB_WORK;
    // 设置下标参数
    protected static final String BUNDLE_TAB_INDEX = "bundle_tab_index";

    private Handler mH;

    public static WorkspaceActivity getInstance() {
        return mInstance;
    }

    public void hideTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        hideTitle();

        setContentView(R.layout.activity_workspace);

        mInstance = this;

        sureH();
        BehaviorManager.getInstance().setUIHandler(mH);

        init(savedInstanceState);
    }

    private void sureH() {
        if (mH == null) {
            mH = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case BehaviorManager.ACTION_BEHAVIOR_TOAST:
                            Toast.makeText(WorkspaceActivity.this, "" + msg.obj, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };
        }
    }

    @Override
    public void onItemClick(int tagIndex) {
        Log.i(TAG, "onItemClick:" + tagIndex);
        switchFragment(tagIndex);
    }

    @Override
    public boolean onItemDoubleClick(int tagIndex) {
        final String classId = mHotseat.getFouceTabClassId();
        if (TextUtils.isEmpty(classId)) {
            Log.e(TAG, "当前 FouceTabClassId 怎么会为空咧？？");
            return false;
        }

        Fragment fragment = mHotseatFragmentsCache.get(classId);
        if (null == fragment) {
            Log.e(TAG, "当前 FouceTabClassId的Fragment怎么会为空咧？？");
            return false;
        }

        return false;
    }

    @Override
    public void updateActionBar(HotseatItemView.ActionBarInfo actionBarInfo) {
        // Title
    }

    private void switchFragment(int tagIndex) {
        Log.i(TAG, "switchFragment:" + tagIndex);
        switchFragment(tagIndex, false);
    }

    protected void switchFragment(int tagIndex, boolean anim) {
        if (tagIndex < 0) {
            Log.e(TAG, "我的乖乖，怎么会有位置是：" + tagIndex + " 的内容可以切换咧，得check一下是否Hotseat没有内容？");
            return;
        }

        final HotseatItemView.ComponentName componentName = getComponentNameByTabIndex(tagIndex);
        final String classId = componentName.getClassId();
        if (mSaveClassId.endsWith(classId)) {
            Log.e(TAG, "同一个fragment不需要切换，直接return ~");
            return;
        }

        Log.i(TAG, "switchFragment:" + tagIndex);
        Fragment fragment = getFragment(componentName);
        if (null == fragment) {
            Toast.makeText(this, "switchFragment:" + tagIndex + " getFragment return null classId", Toast.LENGTH_LONG).show();
            return;
        }

        //注意这里不能缓存FragmentTransaction 需要每次都去get
        FragmentTransaction ft = anim ? getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right) :
                getSupportFragmentManager().beginTransaction();

        if (null != mHotseatFragmentsCache.get(mSaveClassId)) {
            ft.hide(mHotseatFragmentsCache.get(mSaveClassId));
        }

        if (!fragment.isAdded()) {
            ft.add(getContentId(), fragment, fragment.getClass().getSimpleName());
        }

        ft.show(fragment).commit();
        mSaveClassId = classId;
    }

    private HotseatItemView.ComponentName getComponentNameByTabIndex(int tagIndex) {
        return mHotseat.getComponentNameByTagIndex(tagIndex);
    }


    //index是不固定的，唯一固定的是classID,因此需要将tab的index转成对应的classID
    private Fragment getFragment(HotseatItemView.ComponentName componentName) {
        Log.i(TAG, "getFragment:" + componentName);
        final String classId = componentName.getClassId();
        if (TextUtils.isEmpty(classId)) {
            return null;
        }

        if (mHotseatFragmentsCache.get(componentName.getClassId()) != null) {
            return mHotseatFragmentsCache.get(componentName.getClassId());
        }

        Fragment fragment = null;
        String msg = "";

        Log.i(TAG, "getFragmentByPos to get Plugin fragement:" + classId);
        Class<?> cls = null;
        String clsName = componentName.getFullName();
        if (clsName != null) {
            try {
                cls = getClassLoader().loadClass(clsName);
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "loadPluginFragmentClassById:" + classId + " ClassNotFound:" + clsName + "Exception", e);
                Log.w(TAG, "没有找到：" + clsName + " 是不是被混淆了~");
            }
        }

        if (cls != null) {
            try {
                fragment = (Fragment) cls.newInstance();
            } catch (InstantiationException e) {
                Log.e(TAG, "InstantiationException", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "IllegalAccessException", e);
            }
        }

        msg = "Not found classId：" + classId + "，请先check提供这个Fragment的插件是否有安装哈(⊙o⊙)~";

        if (fragment == null) {
            fragment = new ToastFragment();
            ((ToastFragment) fragment).setToastMsg(msg);
        }

        mHotseatFragmentsCache.put(classId, fragment);

        return fragment;// new Fragment();
    }

    // for child
    protected void init(Bundle savedInstanceState) {
        //初始化插件的显示info
        initDisplayInfo();
        initHotseat();

        //初始化View
        initView(savedInstanceState);
    }

    protected void initHotseat() {
        // step1：get到控件对象
        if (mHotseat == null) {
            mHotseat = (Hotseat) findViewById(getHotseatId());
        }

        if (mHotseat == null) {
            throw new IllegalArgumentException("mHotseat can not be null!");
        }

        // step2：初始化内容
        mHotseat.clear();
        // 根据具体的MainActivity来配置
        for (HotseatDisplayItem item : mHotseatDisplayInfos) {
            // 当前Hotseat上暂时之放置fragment
            if (item.action_type != HotseatDisplayItem.TYPE_FRAGMENT) {
                continue;
            }

            mHotseat.addCellItem(item);
        }

        // step3：设置监听
        mHotseat.addHotseatClickObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, " onNewIntent");
        setIntent(intent);
        int index = intent.getIntExtra(BUNDLE_TAB_INDEX, -1);
        if (index >= 0) {
            switchFragment(index);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void initDisplayInfo() {
        mHotseatDisplayInfos.clear();

        // 工作台
        HotseatDisplayItem mainTab = new HotseatDisplayItem(CLASS_ID_WORK, HotseatDisplayItem.TYPE_FRAGMENT, WorkspaceFragment.class.getName(),
                R.string.main_workspace, R.drawable.ic_nav_workspace, R.drawable.ic_nav_workspace_h,
                R.color.lotter_fiter_text, R.color.backgroud_common, null, null, TAB_WORK);
        mHotseatDisplayInfos.add(mainTab);

        // 消息会话
        HotseatDisplayItem sessionTab = new HotseatDisplayItem(CLASS_ID_MESSAGE,
                HotseatDisplayItem.TYPE_FRAGMENT, ConversationFragment.class.getName(),
                null, null, TAB_MESSAGE, MainConstant.SESSION_RES_IDS);
        mHotseatDisplayInfos.add(sessionTab);

        // 个人设置
        HotseatDisplayItem meTab = new HotseatDisplayItem(CLASS_ID_SETTINGS,
                HotseatDisplayItem.TYPE_FRAGMENT, "my.settings.fragment",
                null, null, TAB_SETTINGS, MainConstant.SETTINGS_RES_IDS);
        mHotseatDisplayInfos.add(meTab);
    }

    protected String getDefaultFouceTabClassId() {
        return DEFAULT_TAB_CLASS_ID;
    }

    //初始化View
    protected void initView(Bundle savedInstanceState) {
        // step3：默认聚焦位置
        final int index = savedInstanceState != null ? savedInstanceState.getInt(BUNDLE_TAB_INDEX,
                DEFAULT_TAB_INDEX) : getIntent().getIntExtra(BUNDLE_TAB_INDEX, DEFAULT_TAB_INDEX);

        switchFragment(index, false);
    }

    private int getContentId() {
        return R.id.home_fragment_container;
    }

    private int getHotseatId() {
        return R.id.home_bottom_tab;
    }
}
