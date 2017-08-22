package com.shuishou.digitalmenu.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.shuishou.digitalmenu.InstantValue;
import com.shuishou.digitalmenu.R;
import com.shuishou.digitalmenu.bean.Category1;
import com.shuishou.digitalmenu.bean.Category2;
import com.shuishou.digitalmenu.bean.Desk;
import com.shuishou.digitalmenu.bean.Dish;
import com.shuishou.digitalmenu.bean.MenuVersion;
import com.shuishou.digitalmenu.db.DBOperator;
import com.shuishou.digitalmenu.http.HttpOperator;
import com.shuishou.digitalmenu.io.IOOperator;
import com.shuishou.digitalmenu.uibean.ChoosedFood;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/4.
 */

public class MainActivity extends AppCompatActivity implements OperateChoosedFoodIFC{
    public final static byte LANGUAGE_ENGLISH = 1;
    public final static byte LANGUAGE_CHINESE = 2;
    private static MainActivity instance;

    private RadioButton rbChinese;
    private RadioButton rbEnglish;
    private TextView tvChoosedItems;
    private TextView tvChoosedPrice;
    private Button btnOrder;
    private ImageButton btnMaintain;
    private TextView tvChoosedItemsLabel;
    private TextView tvChoosedPriceLabel;
    private TextView tvOrdersLabel;
    private TabHost thCategory1;
    private List<Category1> category1s; // = TestData.makeCategory1();
    private List<Desk> desks;
    private ChoosedFoodAdapter adapter;
    private List<ChoosedFood> choosedFoodList= new ArrayList<ChoosedFood>();

    private HttpOperator httpOperator;
    private DBOperator dbOperator;
    private DishTabBuilder dishTabBuilder;

    private PostOrderDialog dlgPostOrder;

    private int refreshMenuPeroid = 60 * 1000;

    private Handler refreshMenuHandler;
    private Timer refreshMenuTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        ListView lvChoosedFood = (ListView) findViewById(R.id.list_choosedfood);
        adapter = new ChoosedFoodAdapter(this, R.layout.choosedfood_item, choosedFoodList);
        lvChoosedFood.setAdapter(adapter);
        tvChoosedItems = (TextView) findViewById(R.id.tvChoosedFoodItems);
        tvChoosedPrice  = (TextView) findViewById(R.id.tvChoosedFoodPrice);
        rbChinese = (RadioButton) findViewById(R.id.rbChinese);
        rbEnglish = (RadioButton) findViewById(R.id.rbEnglish);
        btnOrder = (Button) findViewById(R.id.btnOrder);
        btnMaintain = (ImageButton) findViewById(R.id.btnMaintain);
        tvChoosedItemsLabel = (TextView) findViewById(R.id.tvChoosedFoodItemLabel);
        tvChoosedPriceLabel = (TextView) findViewById(R.id.tvChoosedFoodPriceLabel);
        tvOrdersLabel = (TextView) findViewById(R.id.tvChoosedFoodLabel);
        thCategory1 = (TabHost)findViewById(R.id.thCategory1);
        thCategory1.setup();//must do before add tab

        btnMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaintainDialog dlg = new MaintainDialog(instance);
                dlg.showDialog();
            }
        });
        rbChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeLanguage(LANGUAGE_CHINESE);
            }
        });

        rbEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeLanguage(LANGUAGE_ENGLISH);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartOrder();
            }
        });

        Button btnTest1 = (Button) findViewById(R.id.btnTest1);
//        Button btnTest2 = (Button) findViewById(R.id.btnTest2);
        btnTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTest1();
            }
        });
//        btnTest2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doTest2();
//            }
//        });


        //init tool class, NoHttp
        NoHttp.initialize(this);
        Logger.setDebug(true);
        Logger.setTag("digitalmenu:nohttp");

        InstantValue.URL_TOMCAT = IOOperator.loadServerURL();
        httpOperator = new HttpOperator(this);
        dbOperator = new DBOperator(this);
        dishTabBuilder = new DishTabBuilder(this);
        //read local database to memory
        category1s = dbOperator.queryAllMenu();
        desks = dbOperator.queryDesks();
        //build menu depending on local database
        buildMenu();

        dlgPostOrder = new PostOrderDialog(this);

        startRefreshMenuTimer();
    }

    private void doTest1(){
        desks = dbOperator.queryDesks();
        System.out.println(desks);
    }
    private void doTest2(){
        Log.d("lousongtao menuversion", dbOperator.getLiteOrm().queryCount(MenuVersion.class) + "");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshMenuTimer = null;
    }

    private void startRefreshMenuTimer(){
        refreshMenuHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    List<Integer> dishIdList = (List<Integer>) msg.obj;
                    //loop to find Dish Object depending on the id, reload the data from database
                    for(Integer dishId : dishIdList){
                        Dish dish = dbOperator.queryDishById(dishId);
                        dishTabBuilder.changeDishSoldOutStatus(dishId, dish.isSoldOut());
                        //remind clients if the sold out dish are selected
                        for(ChoosedFood cf : choosedFoodList){
                            if (cf.getDish().getId() == dishId){
                                if (dish.isSoldOut()) {
                                    String errormsg = "Dish " + dish.getEnglishName() + " is Sold Out already, please remove it from your selection.";
                                    if (getLanguage() == LANGUAGE_CHINESE)
                                        errormsg = "您选择的 " + dish.getChineseName() + " 已经售完, 请从列表中将其去除.";
                                    popupWarnDialog(R.drawable.error, "Warning", errormsg);
                                }
                            }
                        }
                    }
                }
            }
        };
        //start timer
        refreshMenuTimer = new Timer();
        refreshMenuTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(InstantValue.URL_TOMCAT == null || InstantValue.URL_TOMCAT.length() == 0)
                    return;
                MenuVersion mv = (MenuVersion) dbOperator.queryObjectById(1, MenuVersion.class);
                int localVersion = 0;
                if (mv != null) {
                    localVersion = mv.getVersion();
                }

                List<Integer> dishIdList = httpOperator.chechMenuVersion(localVersion);
                if (dishIdList != null && !dishIdList.isEmpty()){
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = dishIdList;
                    refreshMenuHandler.sendMessage(msg);
                }
            }
        }, 1, refreshMenuPeroid
        );
    }

    public PostOrderDialog getPostOrderDialog(){
        return dlgPostOrder;
    }

    private void onStartOrder(){
        if (choosedFoodList == null || choosedFoodList.isEmpty())
            return;
        dlgPostOrder.clearup();
        dlgPostOrder.showDialog(httpOperator, choosedFoodList);
    }

    public void onFinishMakeOrder(int orderSequence){
        //clear data
        choosedFoodList.clear();
        adapter.clear();
        tvChoosedItems.setText("");
        tvChoosedPrice.setText("");
        dlgPostOrder.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Successful");
        builder.setMessage("Finish make order! Order Sequence : " + orderSequence);
        builder.setIcon(R.drawable.info);
//        builder.setPositiveButton("Yes", null);
        builder.setNegativeButton("OK", null);
        builder.create().show();
    }

    public void buildMenu(){
        dishTabBuilder.buildMenuPanel(thCategory1, category1s, getLanguage());
        onChangeLanguage(getLanguage());
    }

    public DBOperator getDbOperator(){
        return dbOperator;
    }

    public HttpOperator getHttpOperator(){
        return httpOperator;
    }

    public void persistMenu(){
        dbOperator.clearMenu();
        dbOperator.saveObjectsByCascade(category1s);
    }

    public void persistDesk(){
        dbOperator.clearDesk();
        dbOperator.saveObjectsByCascade(desks);
    }

    public void onFoodSelected(Dish dish) {
        if (dish.isSoldOut()){
            Toast.makeText(MainActivity.this, "This dish is sold out now.", Toast.LENGTH_LONG).show();
            return;
        }
        ChoosedFood choosedFood = null;
        //first check if the dish is exist in the list already
        for(ChoosedFood cf : choosedFoodList){
            if (cf.getDish().getId() == dish.getId()) {
                choosedFood = cf;
                break;
            }
        }
        if (choosedFood != null){
            choosedFood.setAmount(choosedFood.getAmount()+1);
        } else {
            choosedFood = new ChoosedFood(dish);
            choosedFoodList.add(choosedFood);
        }
        adapter.notifyDataSetChanged();
        calculateFoodPrice();
    }

    private void calculateFoodPrice(){
        double totalPrice = 0.0;
        for(ChoosedFood cf : choosedFoodList){
            totalPrice += cf.getAmount() * cf.getPrice();
        }
        double gst = totalPrice / 11;
        tvChoosedItems.setText(String.valueOf(choosedFoodList.size()));
        tvChoosedPrice.setText("$" + String.format("%.2f", totalPrice) + " (GST $" + (String.format("%.2f", gst)) + ")");
    }

    public static MainActivity getInstance(){
        return instance;
    }

    @Override
    public void plusDish(int position) {
        choosedFoodList.get(position).setAmount(choosedFoodList.get(position).getAmount() + 1);
        calculateFoodPrice();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void minusDish(int position) {
        choosedFoodList.get(position).setAmount(choosedFoodList.get(position).getAmount() - 1);
        if (choosedFoodList.get(position).getAmount() == 0)
            choosedFoodList.remove(position);
        calculateFoodPrice();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addRequirements(int position) {
        ChoosedFood cf = choosedFoodList.get(position);
        AddOrderRequirementsDialog dlg = new AddOrderRequirementsDialog(this);
        dlg.showDialog(cf);
    }

//    public void resetChoosedFoodRequirements(ChoosedFood cf, String req){
//        cf.setAdditionalRequirements(req);
//    }

    @Override
    public void deleteDish(int position) {
        choosedFoodList.remove(position);
        calculateFoodPrice();
        adapter.notifyDataSetChanged();
    }

    public void onRefreshData(){
        //clear all data and picture files
        IOOperator.deleteDishPicture(InstantValue.LOCAL_CATALOG_DISH_PICTURE);
        dbOperator.deleteAllData(Desk.class);
        dbOperator.deleteAllData(MenuVersion.class);
        dbOperator.deleteAllData(Dish.class);
        dbOperator.deleteAllData(Category2.class);
        dbOperator.deleteAllData(Category1.class);
        // synchronize and persist
        httpOperator.loadDeskData();
        httpOperator.loadMenuVersionData();
        httpOperator.loadMenuData();
    }

    private void onChangeLanguage(byte language){
        if (language == LANGUAGE_ENGLISH){
            tvOrdersLabel.setText(R.string.choosed_food_label_en);
            tvChoosedPriceLabel.setText(R.string.choosed_food_price_en);
            tvChoosedItemsLabel.setText(R.string.choosed_food_item_en);
            btnOrder.setText(R.string.order_button_en);
        } else if (language == LANGUAGE_CHINESE){
            tvOrdersLabel.setText(R.string.choosed_food_label_cn);
            tvChoosedPriceLabel.setText(R.string.choosed_food_price_cn);
            tvChoosedItemsLabel.setText(R.string.choosed_food_item_cn);
            btnOrder.setText(R.string.order_button_cn);
        }
        //find all ChangeLanguageTextView and invoke its change language text
        List<ChangeLanguageTextView> tvs = lookforAllChangeLanguageTextView(this.getWindow().getDecorView());
        for(ChangeLanguageTextView tv : tvs){
            tv.show(getLanguage());
        }
    }

    private List<ChangeLanguageTextView> lookforAllChangeLanguageTextView(View view){
        List<ChangeLanguageTextView> list = new ArrayList<ChangeLanguageTextView>();
        if (view instanceof ViewGroup){
            ViewGroup vg = (ViewGroup)view;
            for (int i = 0; i< vg.getChildCount(); i++){
                View child = vg.getChildAt(i);
                if (child.getClass().equals(ChangeLanguageTextView.class)){
                    list.add((ChangeLanguageTextView)child);
                }
                list.addAll(lookforAllChangeLanguageTextView(child));
            }
        }
        return list;
    }

    public byte getLanguage(){
        if (rbChinese.isChecked())
            return LANGUAGE_CHINESE;
        else return LANGUAGE_ENGLISH;
    }

    public void setMenu(List<Category1> category1s){
        this.category1s = category1s;
    }

    public void setDesk(List<Desk> desks){
        this.desks = desks;
    }

    public List<Desk> getDesks() {
        return desks;
    }

    public void popupToast(String msg, int shortlong){
        Toast.makeText(this, msg, shortlong).show();
    }

    public void popupWarnDialog(int iconId, String title, String msg){
        new AlertDialog.Builder(this)
                .setIcon(iconId)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton("OK", null)
                .create().show();
    }

    //屏蔽实体按键BACK
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //屏蔽recent task 按键
    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext() .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }
}
