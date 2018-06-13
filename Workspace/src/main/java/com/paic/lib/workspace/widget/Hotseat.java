package com.paic.lib.workspace.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.paic.lib.workspace.Model.HotseatDisplayItem;

import java.util.ArrayList;

public class Hotseat extends LinearLayout implements OnClickListener, View.OnTouchListener {
    private final String TAG = "rick_Print_dm:Hotseat";

    private ArrayList<OnHotseatClickListener> onListeners = new ArrayList<OnHotseatClickListener>();
    private ArrayList<HotseatItemView> mHomeBottomButtons = new ArrayList<HotseatItemView>();
    private HotseatItemView mFoucsButton = null;
    private static int mAddChildIndex = 0;

    public Hotseat(Context context) {
        this(context, null);
    }

    public Hotseat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Hotseat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void clear() {
        removeAllViews();
        mFoucsButton = null;
        mAddChildIndex = 0;
        mHomeBottomButtons.clear();
    }

    public interface OnHotseatClickListener {
        void onItemClick(int tagIndex);

        boolean onItemDoubleClick(int tagIndex);

        void updateActionBar(final HotseatItemView.ActionBarInfo actionBarInfo);
    }

    public void addHotseatClickObserver(OnHotseatClickListener onClickListener) {
        if (null == onListeners) {
            throw new IllegalArgumentException("Invalid parameters onClickListener:null");
        }

        if (onListeners.contains(onClickListener)) {
            Log.i(TAG, "onClickListener already exist, ignore");
            return;
        }

        onListeners.add(onClickListener);
    }

    public void removeHotseatClickObserver(OnHotseatClickListener onClickListener) {
        if (null == onListeners) {
            throw new IllegalArgumentException("Invalid parameters onClickListener:null");
        }

        if (onListeners.contains(onClickListener)) {
            onListeners.remove(onClickListener);
        }
    }

    // type 1:fragment 2:activity 3:service
    public void addCellItem(String classId, String packageName, int componentType, CharSequence title,
                            Drawable normalBackground, Drawable focusBackground, int textNormalColor,
                            int textFocusColor, int location, int tagIndex) {
        HotseatItemView button = new HotseatItemView(getContext());
        button.setActionClass(classId, packageName, componentType);
        button.setText(title);
        button.setNormalBackground(normalBackground);
        button.setFocusBackground(focusBackground);
        button.setTextColorNormal(textNormalColor);
        button.setTextColorFocus(textFocusColor);
        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        button.setLayoutParams(params);
        button.setOnClickListener(this);
        button.setLocation(location);
        button.setTagIndex(mAddChildIndex);
        ++mAddChildIndex;
        addView(button);
        // 这里需要入队列跟My_fragment有些差异，因为fragment的切换需要依赖这个队列
        mHomeBottomButtons.add(button);
    }

    public int addCellItem(final HotseatDisplayItem displayItem) {
        // 当前显示在Hotseat的内容暂只接收fragment
        if (displayItem.action_type != HotseatDisplayItem.TYPE_FRAGMENT) {
            return -1;
        }

        if (!isEnabledDisplayItem(displayItem)) {
            Log.e(TAG, "info is illegal(already exists), This will be ignored!");
            return -1;
        }

        HotseatItemView button = new HotseatItemView(getContext());
        button.setActionClass(displayItem.action_id, displayItem.fullName, displayItem.action_type);

        button.setText(displayItem.title_res_id);

        button.setNormalBackground(displayItem.normal_icon_id);
        button.setFocusBackground(displayItem.focus_icon_id);
        button.setTextColorNormal(getResources().getColor(displayItem.normal_textColor_res_id));
        button.setTextColorFocus(getResources().getColor(displayItem.focus_textColor_res_id));
        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        params.gravity = Gravity.CENTER_VERTICAL;
        button.setLayoutParams(params);
        button.setOnClickListener(this);
        button.setLocation(displayItem.x);
        button.setTagIndex(displayItem.tagIndex);
        ++mAddChildIndex;
        button.setActionBarInfo(displayItem.actionBarDisplayItem);

        Log.i(TAG, "addOneBottomButton:" + button.getText());
        mHomeBottomButtons.add(button);
        addView(button);

        return -1;
    }

    public boolean isEnabledDisplayItem(final HotseatDisplayItem displayItem) {
        if (displayItem == null || TextUtils.isEmpty(displayItem.fullName) || TextUtils.isEmpty(displayItem.action_id))
            return false;

        for (HotseatItemView item : mHomeBottomButtons) {
            if (displayItem.fullName.equals(item.getFullName()) && displayItem.action_id.equals(item.getClassId())) {
                return false;
            }
        }

        return true;
    }

    public int childCount() {
        return mHomeBottomButtons.size();
    }

    @Override
    public void onClick(View view) {
        if (view instanceof HotseatItemView) {
            if (view == mFoucsButton)
                return;

            setFocus((HotseatItemView) view);
//            ((HotseatItemView) view).setHighlight(0);

            if (onListeners != null && mFoucsButton != null) {
                for (OnHotseatClickListener listener : onListeners) {
                    listener.updateActionBar(mFoucsButton.mActionBarInfo);
                    listener.onItemClick(mFoucsButton.getTagIndex());
                }
            }
        } else {
            Log.i(TAG, "onClick view=" + view);
        }
    }

    /**
     * 上一次点击时间
     */
    private long mLastClickTime = -1;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean rltVlaue = false;
        if (view instanceof HotseatItemView && view == mFoucsButton) {
            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                long curClickTime = SystemClock.elapsedRealtime();
                if (mLastClickTime > 0) {
                    if (curClickTime - mLastClickTime < 300) {//双击事件
                        if (null != onListeners) {
                            for (OnHotseatClickListener listener : onListeners) {
                                rltVlaue |= listener.onItemDoubleClick(mFoucsButton.getTagIndex());
                            }
                        }
                    }
                }

                mLastClickTime = curClickTime;
            }
        }

        return rltVlaue;
    }

    public String getFouceTabClassId() {
        return mFoucsButton == null ? "" : mFoucsButton.getClassId();
    }

    public int getFouceTagIndex() {
        return mFoucsButton == null ? 0 : mFoucsButton.getTagIndex();
    }

    private void setFocus(final HotseatItemView button) {
        Log.i(TAG, "call setFocus HomeBottomButton");
        if (null == button) {
            Exception here = new Exception("call setFouce on null object");
            here.fillInStackTrace();
            Log.i(TAG, "call setFouce on null object", here);
            return;
        }

        boolean matched = false;
        for (HotseatItemView btn : mHomeBottomButtons) {
            if (btn.equals(button)) {
                button.setFocus(true);
                mFoucsButton = button;
                matched = true;
            } else {
                btn.setFocus(false);
            }
        }

        if (!matched) {
            mFoucsButton = null;
            Log.i(TAG, "setFouce:HomeBottomButton - classId is " + button.getClassId()
                    + " no matching to the right!!!");
            printHomeBottomButtonsInfo();
        }
    }

    /**
     * @return FoucsButton TagIndex
     */
    public int setFocusIndex(int arrayIndex) {
        if (mHomeBottomButtons.size() < 1)
            return -1;

        int focusTagIndex = -1;
        mFoucsButton = null;

        if (arrayIndex < 0) {
            arrayIndex = 0;
        } else if (mHomeBottomButtons.size() <= arrayIndex) {
            arrayIndex = mHomeBottomButtons.size() - 1;
        }

        for (int index = 0; index < mHomeBottomButtons.size(); index++) {
            if (index == arrayIndex) {
                mFoucsButton = mHomeBottomButtons.get(index);
                mFoucsButton.setFocus(true);
                focusTagIndex = mFoucsButton.getTagIndex();
            } else {
                mHomeBottomButtons.get(index).setFocus(false);
            }
        }

        if (mFoucsButton != null && onListeners != null) {
            for (OnHotseatClickListener listener : onListeners) {
                listener.updateActionBar(mFoucsButton.mActionBarInfo);
            }
        }

        return focusTagIndex;
    }

    private void printHomeBottomButtonsInfo() {
        Log.i(TAG, "===========================printHomeBottomButtonsInfo begin===========================");
        int index = 0;
        for (HotseatItemView button : mHomeBottomButtons) {
            Log.i(TAG, "[" + index + "] classId:" + button.getClassId() + " type is " + button.getComponentType());
        }
        Log.i(TAG, "===========================printHomeBottomButtonsInfo end===========================");
    }

    public HotseatItemView.ComponentName getComponentNameByTagIndex(int tagIndex) {
        if (mFoucsButton == null) {
            setFocusIndex(0);
        }

        if (mFoucsButton != null && mFoucsButton.getTagIndex() == tagIndex) {
            return mFoucsButton.getComponentName();
        }

        for (HotseatItemView item : mHomeBottomButtons) {
            if (item.getTagIndex() == tagIndex) {
                return item.getComponentName();
            }
        }

        return null;
    }

    public int getPosByClassId(String classId) {
        if (TextUtils.isEmpty(classId))
            return 0;

        for (int index = 0; index < mHomeBottomButtons.size(); index++) {
            if (classId.equals(mHomeBottomButtons.get(index).getClassId()))
                return index;
        }

        return 0;
    }

    public void switchToFragment(String classId) {
        for (HotseatItemView item : mHomeBottomButtons) {
            if (item.getClassId().equals(classId)) {
                setFocus(item);

                if (onListeners != null && mFoucsButton != null) {
                    for (OnHotseatClickListener listener : onListeners) {
                        listener.updateActionBar(mFoucsButton.mActionBarInfo);
                        listener.onItemClick(mFoucsButton.getTagIndex());
                    }
                }
                return;
            }
        }
    }

    public void setHighlightCellItem(String classId, int withNum) {
        for (HotseatItemView item : mHomeBottomButtons) {
            if (item.getClassId().equals(classId)) {
                item.setHighlight(withNum);
                return;
            }
        }
    }
}