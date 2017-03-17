package com.beiing.library;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2017/3/9.<br/>
 * 描述：
 * </br>
 */

public class MultipleMenu extends LinearLayout implements View.OnClickListener {

    /**
     * tab所在layout
     */
    private LinearLayout tabHolderView;

    /**
     * menu所在layout
     */
    private FrameLayout menuHolderView;

    /**
     * 菜单下部阴影
     */
    private View maskView;

    /**
     * 属性
     */
    private Config config;

    /**
     * tab集合
     */
    private List<View> tabList;

    /**
     * menu: tabtitle和对应view
     */
    private List<MenuPage> menuPages;

    private final static int INVALID_POSITION = -1;

    /**
     * 当前打开的菜单位置
     */
    private int currentPosition = INVALID_POSITION;

    private Animation iconOutAnimation;
    private Animation iconInAnimation;
    private Animation menuOutAnimation;
    private Animation menuInAnimation;

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

        initAnimation(context);
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        config = new Config();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultipleMenu);
        try {
            config.tabHolderHeight = ta.getDimensionPixelSize(R.styleable.MultipleMenu_mm_tabHolderHeight, dp2px(48));
            config.tabHolderColor = ta.getColor(R.styleable.MultipleMenu_mm_tabHolderColor, 0xffffffff);
            config.underLineHeight = ta.getDimensionPixelSize(R.styleable.MultipleMenu_mm_underLineHeight, dp2px(0.6f));
            config.underLineColor = ta.getColor(R.styleable.MultipleMenu_mm_underLineColor, 0xffe0e0e0);
            config.tabTitleDefaultColor = ta.getColor(R.styleable.MultipleMenu_mm_tabTitleDefaultColor, 0xff252525);
            config.tabTitletSelectedColor = ta.getColor(R.styleable.MultipleMenu_mm_tabTitletSelectedColor, 0xff5da6f0);
            config.tabTitleDefaultSize = ta.getDimensionPixelSize(R.styleable.MultipleMenu_mm_tabTitleDefaultSize, 14);
            config.tabTitleSelectedSize = ta.getDimensionPixelSize(R.styleable.MultipleMenu_mm_tabTitleSelectedSize, 14);
            config.tabIconDefault = ta.getResourceId(R.styleable.MultipleMenu_mm_tabIconDefault, R.mipmap.tab_icon_default);
            config.tabIconSelected = ta.getResourceId(R.styleable.MultipleMenu_mm_tabIconSelected, R.mipmap.tab_icon_selected);
            config.tabIconLeftmargin = ta.getDimensionPixelSize(R.styleable.MultipleMenu_mm_tabIconLeftmargin, dp2px(6));
            config.dividerWidth = ta.getDimensionPixelSize(R.styleable.MultipleMenu_mm_dividerWidth, dp2px(0.6f));
            config.dividerTopmargin = ta.getDimensionPixelSize(R.styleable.MultipleMenu_mm_dividerTopmargin, dp2px(8));
            config.dividerBottommargin = ta.getDimensionPixelSize(R.styleable.MultipleMenu_mm_dividerBottommargin, dp2px(8));
            config.dividerColor = ta.getColor(R.styleable.MultipleMenu_mm_dividerColor, 0xffe0e0e0);
            config.maskColor = ta.getColor(R.styleable.MultipleMenu_mm_maskColor, 0x40000000);
            config.menuAnimateIn = ta.getResourceId(R.styleable.MultipleMenu_mm_menuAnimateIn, R.anim.scale_in);
            config.menuAnimateOut = ta.getResourceId(R.styleable.MultipleMenu_mm_menuAnimateOut, R.anim.scale_out);
        } finally {
            ta.recycle();
        }
    }

    private void initView(Context context) {
        //初始化view
        tabHolderView = new LinearLayout(context);
        tabHolderView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, config.tabHolderHeight));
        tabHolderView.setOrientation(HORIZONTAL);
        tabHolderView.setGravity(Gravity.CENTER);
        tabHolderView.setBackgroundColor(config.tabHolderColor);

        View underLineView = new View(context);
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
        maskView.setBackgroundColor(config.maskColor);
    }

    private void initAnimation(Context context) {
        iconInAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        iconOutAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
        menuInAnimation = AnimationUtils.loadAnimation(context, config.menuAnimateIn);
        menuOutAnimation = AnimationUtils.loadAnimation(context, config.menuAnimateOut);
    }

    public void setMultipleMenu(List<MenuPage> menuPages){
        if(menuPages.isEmpty()) return;

        this.menuPages = menuPages;
        tabList = new ArrayList<>(menuPages.size());

        maskView.setOnClickListener(this);
        menuHolderView.addView(maskView);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0, size = menuPages.size(); i < size; i++) {
            MenuPage mp = menuPages.get(i);
            addTab(mp.getMenuTitle(), inflater, i);
            if(i != size - 1){
                addDivider();
            }
            addMenuView(mp.getMenuView());
        }

        maskView.setVisibility(GONE);
        menuHolderView.setVisibility(GONE);
    }

    private void addTab(String menuTitle, LayoutInflater inflater, int position) {
        View itemTab = inflater.inflate(R.layout.item_tab_layout, tabHolderView, false);
        ItemTabHolder itemTabHolder = new ItemTabHolder(itemTab, position);
        itemTabHolder.setTitleText(menuTitle)
                .setTitleTextSize(config.tabTitleDefaultSize)
                .setTitleTextColor(config.tabTitleDefaultColor)
                .setIconResource(config.tabIconDefault);
        final LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = config.tabIconLeftmargin;
        itemTabHolder.tvIcon.setLayoutParams(lp);
        itemTab.setTag(itemTabHolder);
        itemTab.setOnClickListener(this);
        tabHolderView.addView(itemTab);
        tabList.add(itemTab);
    }

    private void addDivider() {
        View view = new View(getContext());
        final LayoutParams params = new LayoutParams(config.dividerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = config.dividerTopmargin;
        params.bottomMargin = config.dividerBottommargin;
        view.setLayoutParams(params);
        view.setBackgroundColor(config.dividerColor);
        tabHolderView.addView(view);
    }

    private void addMenuView(View menuView) {
        menuView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        menuView.setVisibility(GONE);
        menuHolderView.addView(menuView);
    }


    @Override
    public void onClick(View v) {
        if(v == maskView){
            closeMenu();
        } else {
            switchMenu(v);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if(currentPosition != INVALID_POSITION){
            //tab状态改变
            View itemTab = tabList.get(currentPosition);
            ItemTabHolder itemHolder = (ItemTabHolder) itemTab.getTag();
            if (itemHolder != null) {
                itemHolder.setTitleTextSize(config.tabTitleDefaultSize)
                            .setTitleTextColor(config.tabTitleDefaultColor)
                            .setIconResource(config.tabIconDefault);

                //icon动画
                itemHolder.tvIcon.startAnimation(iconOutAnimation);
            }

            //动画退出
            final View menuView = menuHolderView.getChildAt(currentPosition + 1);
            menuOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    menuView.setVisibility(GONE);
                    maskView.setVisibility(GONE);
                    menuHolderView.setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            menuHolderView.startAnimation(menuOutAnimation);
            currentPosition = INVALID_POSITION;
        }
    }

    /**
     * 打开指定位置menu
     * @param position
     */
    private void openMenu(int position) {
        View itemTab = tabList.get(position);
        ItemTabHolder itemHolder = (ItemTabHolder) itemTab.getTag();
        if (itemHolder != null) {
            itemHolder.setTitleTextSize(config.tabTitleSelectedSize)
                    .setTitleTextColor(config.tabTitletSelectedColor)
                    .setIconResource(config.tabIconSelected);

            //icon动画
            itemHolder.tvIcon.startAnimation(iconInAnimation);
        }

        for (MenuPage page :
                menuPages) {
            page.getMenuView().setVisibility(GONE);
        }

        //动画进入
        menuHolderView.setVisibility(VISIBLE);
        maskView.setVisibility(VISIBLE);
        menuHolderView.getChildAt(position + 1).setVisibility(VISIBLE);
        menuHolderView.startAnimation(menuInAnimation);
        currentPosition = position;
    }

    /**
     * 切换menu
     * @param selectV
     */
    private void switchMenu(View selectV){
        ItemTabHolder itemHolder = (ItemTabHolder) selectV.getTag();
        if(itemHolder.position == currentPosition){
            closeMenu();
        } else {
            closeMenu();
            openMenu(itemHolder.position);
        }
    }

    public void setTabTitle(String text){
        if(currentPosition != INVALID_POSITION){
            setTbaTitle(currentPosition, text);
        }
    }

    public void setTbaTitle(int position, String text){
        if(position >= 0 && position < tabList.size()){
            View itemTab = tabList.get(currentPosition);
            ItemTabHolder itemHolder = (ItemTabHolder) itemTab.getTag();
            if (itemHolder != null) {
                itemHolder.setTitleText(text);
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int dp2px(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

    private class Config{
        /**
         * tab所在layout高度,必填
         */
        int tabHolderHeight;

        /**
         * tablayout背景色
         */
        int tabHolderColor;

        /**
         * tablayout底部线条
         */
        int underLineHeight;

        /**
         * tablayout底部线条颜色
         */
        int underLineColor;

        /**
         * tab默认颜色
         */
        int tabTitleDefaultColor;

        /**
         * tab选中颜色
         */
        int tabTitletSelectedColor;

        /**
         * tab默认字体大小
         */
        int tabTitleDefaultSize;

        /**
         * tab选中大小
         */
        int tabTitleSelectedSize;

        /**
         * tab未选中时icon
         */
        int tabIconDefault;

        /**
         * tab选中时icon
         */
        int tabIconSelected;

        /**
         * icon左边距
         */
        int tabIconLeftmargin;

        /**
         * tab 间隔线宽度
         */
        int dividerWidth;

        /**
         * tab 间隔线上边距
         */
        int dividerTopmargin;

        /**
         * tab 间隔线下边距
         */
        int dividerBottommargin;

        /**
         * tab 间隔线颜色
         */
        int dividerColor;

        /**
         * 阴影颜色
         */
        int maskColor;

        /**
         * menu进入动画
         */
        int menuAnimateIn;

        /**
         * menu退出动画
         */
        int menuAnimateOut;

        public Config setTabHolderColor(int tabHolderColor) {
            this.tabHolderColor = tabHolderColor;
            return this;
        }

        public Config setTabHolderHeight(int tabHolderHeight) {
            this.tabHolderHeight = tabHolderHeight;
            return this;
        }

        public Config setUnderLineHeight(int underLineHeight) {
            this.underLineHeight = underLineHeight;
            return this;
        }

        public Config setUnderLineColor(int underLineColor) {
            this.underLineColor = underLineColor;
            return this;
        }

        public Config setTabTitleDefaultColor(int tabTitleDefaultColor) {
            this.tabTitleDefaultColor = tabTitleDefaultColor;
            return this;
        }

        public Config setTabTitletSelectedColor(int tabTitletSelectedColor) {
            this.tabTitletSelectedColor = tabTitletSelectedColor;
            return this;
        }

        public Config setTabTitleDefaultSize(int tabTitleDefaultSize) {
            this.tabTitleDefaultSize = tabTitleDefaultSize;
            return this;
        }

        public Config setTabTitleSelectedSize(int tabTitleSelectedSize) {
            this.tabTitleSelectedSize = tabTitleSelectedSize;
            return this;
        }

        public Config setTabIconDefault(int tabIconDefault) {
            this.tabIconDefault = tabIconDefault;
            return this;
        }

        public Config setTabIconSelected(int tabIconSelected) {
            this.tabIconSelected = tabIconSelected;
            return this;
        }

        public Config setTabIconLeftmargin(int tabIconLeftmargin) {
            this.tabIconLeftmargin = tabIconLeftmargin;
            return this;
        }

        public Config setDividerWidth(int dividerWidth) {
            this.dividerWidth = dividerWidth;
            return this;
        }

        public Config setDividerTopmargin(int dividerTopmargin) {
            this.dividerTopmargin = dividerTopmargin;
            return this;
        }

        public Config setDividerBottommargin(int dividerBottommargin) {
            this.dividerBottommargin = dividerBottommargin;
            return this;
        }

        public Config setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public Config setMaskColor(int maskColor) {
            this.maskColor = maskColor;
            return this;
        }

        public Config setMenuAnimateIn(int menuAnimateIn) {
            this.menuAnimateIn = menuAnimateIn;
            return this;
        }

        public Config setMenuAnimateOut(int menuAnimateOut) {
            this.menuAnimateOut = menuAnimateOut;
            return this;
        }
    }

    public static class MenuPage{
        private String menuTitle;
        private View menuView;

        public MenuPage(String menuTitle, View menuView) {
            this.menuTitle = menuTitle;
            this.menuView = menuView;
        }

        public MenuPage() {
        }

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

    private class ItemTabHolder{
        TextView tvTitle;
        ImageView tvIcon;
        int position;

        ItemTabHolder(View layout, int position){
            tvTitle = (TextView) layout.findViewById(R.id.tv_tab_title);
            tvIcon = (ImageView) layout.findViewById(R.id.iv_tab_icon);
            this.position = position;
        }

        ItemTabHolder setTitleText(String text){
            tvTitle.setText(text);
            return this;
        }

        ItemTabHolder setTitleTextColor(int color){
            tvTitle.setTextColor(color);
            return this;
        }

        ItemTabHolder setTitleTextSize(int size){
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            return this;
        }

        ItemTabHolder setIconResource(int res){
            tvIcon.setImageResource(res);
            return this;
        }

    }


}
