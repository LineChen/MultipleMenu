package com.beiing.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chenliu on 2017/3/9.<br/>
 * 描述：
 * </br>
 */

public class MultipleMenu extends LinearLayout {

    LinearLayout tabHolderView;

    FrameLayout menuHolderView;

    View underLineView;

    View maskView;

    Config config;

    List<MenuPage> menuPages;

    public MultipleMenu(Context context) {
        this(context, null, 0);
    }

    public MultipleMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        initAttrs(context, attrs);

        initView(context);
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultipleMenu);
        try {

        } finally {
            ta.recycle();
        }

        config = new Config();
    }

    private void initView(Context context) {

        //初始化view
        tabHolderView = new LinearLayout(context);
        tabHolderView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, config.tabHolderHeight));
        tabHolderView.setOrientation(HORIZONTAL);

        underLineView = new View(context);
        underLineView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, config.underLineHeight));
        underLineView.setBackgroundColor(config.underLineColor);

        menuHolderView = new FrameLayout(context);
        menuHolderView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //添加view
        addView(tabHolderView);
        addView(underLineView);
        addView(menuHolderView);

        maskView = new View(context);
        maskView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setMultipleMenu(List<MenuPage> menuPages){
        if(menuPages.isEmpty()) return;
        this.menuPages = menuPages;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0, size = menuPages.size(); i < size; i++) {
            MenuPage mp = menuPages.get(i);
            addTab(mp.getMenuTitle(), inflater);
            if(i != size - 1){
                addDivider();
            }
            addMenuView(mp.getMenuView());
        }

        menuHolderView.addView(maskView);
    }

    private void addTab(String menuTitle, LayoutInflater inflater) {
        View itemTab = inflater.inflate(R.layout.item_tab_layout, tabHolderView, false);
        TextView tvTitle = (TextView) itemTab.findViewById(R.id.tv_tab_title);
        ImageView tvIcon = (ImageView) itemTab.findViewById(R.id.iv_tab_icon);
        tvTitle.setText(menuTitle);
        tvTitle.setTextAppearance(getContext(), config.tabTextAppearance);
        tvIcon.setImageResource(config.tabIconDefault);
        tabHolderView.addView(itemTab);
    }

    private void addDivider() {
        View view = new View(getContext());
        LayoutParams params = new LayoutParams(config.dividerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = config.dividerTopmargin;
        params.bottomMargin = config.dividerBottommargin;
        view.setLayoutParams(params);
        view.setBackgroundColor(config.dividerColor);
        tabHolderView.addView(view);
    }

    private void addMenuView(View menuView) {
        menuHolderView.addView(menuView);
    }

    public int dp2px(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    private class Config{
        /**
         * tab所在layout高度,必填
         */
        int tabHolderHeight = dp2px(30);

        /**
         * tablayout底部线条
         */
        int underLineHeight = dp2px(0.6f);

        /**
         * tablayout底部线条颜色
         */
        int underLineColor = 0xe0e0e0;

        /**
         * tabTitle样式
         */
        int tabTextAppearance = R.style.TabTextAppearance;

        /**
         * title icon
         */
        int tabIconDefault = R.mipmap.tab_icon_default;

        /**
         * tab 间隔线宽度
         */
        int dividerWidth = dp2px(0.6f);

        /**
         * tab 间隔线上边距
         */
        int dividerTopmargin = dp2px(8);

        /**
         * tab 间隔线下边距
         */
        int dividerBottommargin = dp2px(8);

        /**
         * tab 间隔线颜色
         */
        int dividerColor = 0x8d8d8d;

    }

    public static class MenuPage{
        private String menuTitle;
        private View menuView;

        public String getMenuTitle() {
            return menuTitle;
        }

        public void setMenuTitle(String menuTitle) {
            this.menuTitle = menuTitle;
        }

        public View getMenuView() {
            return menuView;
        }

        public void setMenuView(View menuView) {
            this.menuView = menuView;
        }
    }

}
