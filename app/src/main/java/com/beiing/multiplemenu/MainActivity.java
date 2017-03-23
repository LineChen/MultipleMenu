package com.beiing.multiplemenu;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beiing.library.MultipleMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    MultipleMenu multipleMenu;
    TextView tvTime;
    TextView tvStyle;
    TextView tvPrice;

    String[] times = new String[]{"最近一周", "最近一个月", "最近三个月", "最近半年", "最近一年"};
    String[] kinds = new String[]{"一室", "二室", "三室", "四室", "五室以上"};
    String[] prices = new String[]{"1万以下", "1-1.5万", "1.5-2万", "2万以上"};
    private ListView timeMenu;
    private ListView kindMenu;
    private ListView priceMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        multipleMenu = (MultipleMenu) findViewById(R.id.multiple_menu);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvStyle = (TextView) findViewById(R.id.tv_style);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        multipleMenu.setMultipleMenu(initMenuPages());

        initEvent();
    }

    private List<MultipleMenu.MenuPage> initMenuPages(){
        timeMenu = new ListView(this);
        timeMenu.setBackgroundColor(Color.WHITE);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, times);
        timeMenu.setAdapter(timeAdapter);

        kindMenu = new ListView(this);
        kindMenu.setBackgroundColor(Color.WHITE);
        ArrayAdapter<String> kindAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, kinds);
        kindMenu.setAdapter(kindAdapter);

        priceMenu = new ListView(this);
        priceMenu.setBackgroundColor(Color.WHITE);
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, prices);
        priceMenu.setAdapter(priceAdapter);

        List<MultipleMenu.MenuPage> menuPages = new ArrayList<>(3);
        menuPages.add(new MultipleMenu.MenuPage("时间", timeMenu));
        menuPages.add(new MultipleMenu.MenuPage("户型", kindMenu));
        menuPages.add(new MultipleMenu.MenuPage("价格", priceMenu));
        return menuPages;
    }


    private void initEvent() {
        timeMenu.setOnItemClickListener(this);
        kindMenu.setOnItemClickListener(this);
        priceMenu.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        multipleMenu.closeMenu();
        if(parent == timeMenu){
            tvTime.setText("时间:" + times[position]);
        }else if(parent == kindMenu){
            tvStyle.setText("户型:" + kinds[position]);
        } else {
            tvPrice.setText("价格:" + prices[position]);
        }
    }

}
