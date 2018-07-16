package com.pasc.lib.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pasc.lib.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用tag标签控件（该控件标签从左往右排，剩余宽度容纳不下时，自动移到下一行排列标签）
 * Created by chendaixi947 on 2018/05/12.
 */
public class CommonTagView extends ViewGroup {
    private int contentHorizontalPadding;
    private int contentVerticalPadding;
    private int contentMaxRows = Integer.MAX_VALUE; //最大行数，默认无限大行

    private List<List<View>> viewSession = new ArrayList<>();

    public CommonTagView(Context context) {
        this(context, null, 0);
    }

    public CommonTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonTagView);

        setContentHorizontalPadding(typedArray.getDimensionPixelSize(R.styleable.CommonTagView_contentHorizontalPadding, dipToPx(context, 12)));
        setContentVerticalPadding(typedArray.getDimensionPixelSize(R.styleable.CommonTagView_contentVerticalPadding, dipToPx(context, 8)));

        String comments = typedArray.getString(R.styleable.CommonTagView_comments);
        setContentMaxRows(typedArray.getInteger(R.styleable.CommonTagView_contentMaxRows, Integer.MAX_VALUE));

        if (comments != null) {
            setAdapter(new SimpleCommonTagViewAdapter(comments.split("\\|")));
        }
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int nextLeft = 0;
        int nextTop = 0;
        for (int i = 0; i < viewSession.size(); i++) {
            List<View> views = viewSession.get(i);
            for (View view : views) {
                int right = view.getMeasuredWidth() + nextLeft;
                view.layout(nextLeft, nextTop, right, nextTop + view.getMeasuredHeight());
                nextLeft = right + contentHorizontalPadding;
            }
            nextLeft = 0;
            nextTop = nextTop + contentVerticalPadding + views.get(0).getMeasuredHeight();
        }
    }

    private List<View> createContainer() {
        return new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewSession.clear();
        int rows = 1;
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int contentHeight = 0;
        int nextLeft = 0;

        List<View> rowViews = null;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (rows <= contentMaxRows) {
                childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.UNSPECIFIED), heightMeasureSpec);
                int currentRight = nextLeft + childView.getMeasuredWidth();
                if (nextLeft == 0) {
                    rowViews = createContainer();
                    viewSession.add(rowViews);
                    contentHeight = contentHeight + childView.getMeasuredHeight();
                } else if (currentRight > width) {
                    // 右侧顶出去了 新建一行
                    if (++rows > contentMaxRows) {
                        continue;
                    }
                    rowViews = createContainer();
                    viewSession.add(rowViews);
                    nextLeft = 0;
                    contentHeight = contentHeight + childView.getMeasuredHeight();
                }
                rowViews.add(childView);
                nextLeft = nextLeft + contentHorizontalPadding + childView.getMeasuredWidth();
            } else {
                childView.setVisibility(GONE);
            }
        }

        if (viewSession.size() > 1) {
            contentHeight = contentHeight + (viewSession.size() - 1) * contentVerticalPadding;
        }
        setMeasuredDimension(width, contentHeight);
    }

    /**
     * 设置评论控件显示的数据
     *
     * @param adapter 数据源  如果需要刷新数据，重新本方法即可
     */
    public void setAdapter(CommonTagViewAdapter adapter) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            View v = getChildAt(i);
            views.add(v);
        }
        removeAllViews();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            addView(adapter.getView(views.get(i), i, this));
        }

        requestLayout();
    }

    public void setContentHorizontalPadding(int contentHorizontalPadding) {
        this.contentHorizontalPadding = contentHorizontalPadding;
        requestLayout();
    }

    public void setContentVerticalPadding(int contentVerticalPadding) {
        this.contentVerticalPadding = contentVerticalPadding;
        requestLayout();
    }

    public void setContentMaxRows(int contentMaxRows) {
        this.contentMaxRows = contentMaxRows;
        requestLayout();
    }

    public interface CommonTagViewAdapter {
        int getCount();

        Object getItem(int position);

        View getView(View convertView, int position, CommonTagView commonTagView);
    }

    public class SimpleCommonTagViewAdapter implements CommonTagViewAdapter {
        private String[] comments;

        public SimpleCommonTagViewAdapter(String[] comments) {
            this.comments = comments;
        }

        @IntRange(from = 0)
        @Override
        public int getCount() {
            return comments.length;
        }

        @Override
        public Object getItem(int position) {
            return comments[position];
        }

        @NonNull
        @Override
        public View getView(View convertView, int position,
                            CommonTagView commonTagView) {
            View itemView;
            if (convertView == null) {
                itemView = LayoutInflater.from(getContext())
                        .inflate(R.layout.view_commet_item, commonTagView, false);
            } else {
                itemView = convertView;
            }
            TextView textView = (TextView) itemView.findViewById(R.id.contentView);
            textView.setText(comments[position]);

            return itemView;
        }
    }

    public static int dipToPx(Context context, float dip) {
        return (int) (context.getResources().getDisplayMetrics().density * dip + 0.5f);
    }
}
