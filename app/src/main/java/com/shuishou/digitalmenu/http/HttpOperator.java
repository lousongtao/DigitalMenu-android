package com.shuishou.digitalmenu.http;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.shuishou.digitalmenu.InstantValue;
import com.shuishou.digitalmenu.R;
import com.shuishou.digitalmenu.bean.Category1;
import com.shuishou.digitalmenu.bean.Category2;
import com.shuishou.digitalmenu.bean.Desk;
import com.shuishou.digitalmenu.bean.Dish;
import com.shuishou.digitalmenu.bean.MenuVersion;
import com.shuishou.digitalmenu.db.DBOperator;
import com.shuishou.digitalmenu.ui.MainActivity;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/9.
 */

public class HttpOperator {

    private ProgressDialog pd;
    //record the picture files need to be download, after all finish, then rebuild the UI
    private Hashtable<String, Boolean> flagFinishLoadDishPictures = new Hashtable<String, Boolean>();

    private MainActivity mainActivity;
    private List<String> listDishPictures = new ArrayList<String>();
    private static final int WHAT_VALUE_QUERYMENU = 1;
    private static final int WHAT_VALUE_CONFIRMCODE = 2;
    private static final int WHAT_VALUE_MAKEORDER = 3;
    private static final int WHAT_VALUE_QUERYDESK = 4;
    private static final int WHAT_VALUE_QUERYMENUVERSION = 5;
    private static final int WHAT_VALUE_DOWNLOADIMAGE = 10;
//    private StoreOrderData storeOrderData;
//    private class StoreOrderData{
//        String orders;
//        int deskid;
//        String confirmCode;
//        StoreOrderData(String _confirmCode, String _orders, int _deskid){
//            orders = _orders;
//            deskid = _deskid;
//            confirmCode = _confirmCode;
//        }
//    }

    private OnResponseListener responseListener =  new OnResponseListener<JSONObject>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            switch (what){
                case WHAT_VALUE_QUERYMENU :
                    doResponseQueryMenu(response);
                    break;
//                case WHAT_VALUE_CONFIRMCODE:
//                    doResponseConfirmCode4RefreshData(response);
//                    break;
                case WHAT_VALUE_MAKEORDER :
                    doResponseMakeOrder(response);
                    break;
                case WHAT_VALUE_QUERYDESK :
                    doResponseQueryDesk(response);
                    break;
                case WHAT_VALUE_QUERYMENUVERSION:
                    doResponseQueryMenuVersion(response);
                    break;
                default:
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            Log.e("Http failed", "what = "+ what + "\nresponse = "+ response.get());
            String msg = "";
            switch (what){
                case WHAT_VALUE_QUERYMENU :
                    msg = "Failed to load Menu data. Please restart app!";
                    break;
//                case WHAT_VALUE_CONFIRMCODE:
//                    msg = "The input code is wrong.";
//                    break;
                case WHAT_VALUE_MAKEORDER :
                    msg = "Failed to make order. Please try again!";
                    break;
                case WHAT_VALUE_QUERYDESK :
                    msg = "Failed to load Desk data. Please restart app!";
                    break;
            }
            new AlertDialog.Builder(mainActivity)
                    .setIcon(R.drawable.error)
                    .setTitle("WRONG")
                    .setMessage(msg)
                    .setNegativeButton("OK", null)
                    .create().show();
        }

        @Override
        public void onFinish(int what) {
        }
    };

    private DownloadListener downloadDishListener = new DownloadListener() {
        @Override
        public void onDownloadError(int what, Exception exception) {
            new AlertDialog.Builder(mainActivity)
                    .setIcon(R.drawable.error)
                    .setTitle("WRONG")
                    .setMessage("Failed to load dish image. Please restart app!")
                    .setNegativeButton("OK", null)
                    .create().show();
        }

        @Override
        public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
        }

        @Override
        public void onProgress(int what, int progress, long fileCount, long speed) {

        }

        @Override
        public void onFinish(int what, String filePath) {
            flagFinishLoadDishPictures.put(filePath, true);
            if (!flagFinishLoadDishPictures.containsValue(false)){
                mainActivity.buildMenu();
            }
        }

        @Override
        public void onCancel(int what) {

        }
    };

    private RequestQueue requestQueue = NoHttp.newRequestQueue();

    public HttpOperator(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    private void doResponseQueryMenu(Response<JSONObject> response){
        try {
            List<Category1> c1s = analyseJsonForMenu(response.get());
            mainActivity.setMenu(c1s);
            mainActivity.persistMenu();
            loadDishPictureFromServer();
            //mainActivity.buildMenu();
        } catch (JSONException e) {
            onFailedLoadMenu();
            e.printStackTrace();
        }
    }

    private void doResponseQueryDesk(Response<JSONObject> response){
        JSONObject o = response.get();
        try {
            if (o.getBoolean("success")){
                JSONArray jsonDesks = o.getJSONArray("desks");
                if (jsonDesks != null && jsonDesks.length() > 0){
                    List<Desk> desks = new ArrayList<Desk>(jsonDesks.length());
                    for (int i = 0; i < jsonDesks.length(); i++) {
                        JSONObject jsonDesk = jsonDesks.getJSONObject(i);
                        Desk desk = new Desk(jsonDesk.getInt("id"), jsonDesk.getString("name"));
                        desks.add(desk);
                    }
                    mainActivity.setDesk(desks);
                    mainActivity.persistDesk();
                    mainActivity.getPostOrderDialog().initDeskData(desks);
                }
            } else {
                new AlertDialog.Builder(mainActivity)
                        .setIcon(R.drawable.error)
                        .setTitle("WRONG")
                        .setMessage("Failed to load Desk data. Please restart app!")
                        .setNegativeButton("OK", null)
                        .create().show();
            }
        } catch (JSONException e) {
            Log.e("doResponseQueryDesk","failed to parse json object for query desk");
            e.printStackTrace();
        }
    }

    private void doResponseQueryMenuVersion(Response<JSONObject> response){
        JSONObject o = response.get();
        try {
            if (o.getBoolean("success")){
                int version = o.getInt("value");
                mainActivity.getDbOperator().saveObjectByCascade(new MenuVersion(1, version));
            } else {
                new AlertDialog.Builder(mainActivity)
                        .setIcon(R.drawable.error)
                        .setTitle("WRONG")
                        .setMessage("Failed to load Menu version data. Please redo synchronization action!")
                        .setNegativeButton("OK", null)
                        .create().show();
            }
        } catch (JSONException e) {
            Log.e("QueryMenuVersion","failed to parse json object for query MenuVersion");
            e.printStackTrace();
        }
    }

    //load desk
    public void loadDeskData(){
        Request<JSONObject> deskRequest = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/common/getdesks");
        requestQueue.add(WHAT_VALUE_QUERYDESK, deskRequest, responseListener);
    }

    //load desk
    public void loadMenuData(){
        Request<JSONObject> menuRequest = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/menu/querymenu");
        requestQueue.add(WHAT_VALUE_QUERYMENU, menuRequest, responseListener);
    }

    //load menu version
    public void loadMenuVersionData(){
        Request<JSONObject> mvRequest = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/menu/getlastmenuversion", RequestMethod.POST);
        requestQueue.add(WHAT_VALUE_QUERYMENUVERSION, mvRequest, responseListener);
    }

    public boolean checkConfirmCodeSync(String code){
        Request<JSONObject> codeRequest = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/common/checkconfirmcode", RequestMethod.POST);
        codeRequest.add("code", code);
        Response<JSONObject> response = NoHttp.startRequestSync(codeRequest);
        try{
            if (response.get() == null) {
                Log.e("HttpError", "Error occur while synchronize check confirm code. response.get() is null.");
                return false;
            }
            boolean success = response.get().getBoolean("success");
            return success;
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage(), e);
        }
        return false;
    }

    @Nullable
    private List<Category1> analyseJsonForMenu(JSONObject o) throws JSONException {
        if (o.getBoolean("success")){
            listDishPictures.clear();
            JSONArray jsonCategory1s = o.getJSONArray("children");
            if (jsonCategory1s != null && jsonCategory1s.length() > 0){
                List<Category1> listC1 = new ArrayList<Category1>(jsonCategory1s.length());
                for (int i = 0; i < jsonCategory1s.length(); i++) {
                    JSONObject oC1 = jsonCategory1s.getJSONObject(i);
                    Category1 c1 = new Category1(oC1.getInt("objectid"),
                            oC1.getString("chineseName"),
                            oC1.getString("englishName"),
                            oC1.getInt("sequence"));
                    listC1.add(c1);
                    if (oC1.getJSONArray("children") != null){
                        JSONArray jsonCategory2s = oC1.getJSONArray("children");
                        for (int j = 0; j < jsonCategory2s.length(); j++) {
                            JSONObject oC2 = (JSONObject) jsonCategory2s.get(j);
                            Category2 c2 = new Category2(oC2.getInt("objectid"),
                                    oC2.getString("chineseName"),
                                    oC2.getString("englishName"),
                                    oC2.getInt("sequence"),
                                    c1);
                            c1.addCategory2(c2);
                            if (oC2.getJSONArray("children") != null){
                                JSONArray jsonDishes = oC2.getJSONArray("children");
                                for (int k = 0; k < jsonDishes.length(); k++) {
                                    JSONObject oDish = (JSONObject) jsonDishes.get(k);
                                    Dish dish = new Dish();

                                    String pictureName = oDish.getString("pictureName");
                                    if (pictureName != null && !pictureName.equals("null")){
                                        listDishPictures.add(pictureName);
                                        dish.setPictureName(pictureName);
                                    }

                                    dish.setId(oDish.getInt("objectid"));
                                    dish.setChineseName(oDish.getString("chineseName"));
                                    dish.setEnglishName(oDish.getString("englishName"));
                                    dish.setSequence(oDish.getInt("sequence"));
                                    dish.setPrice(oDish.getDouble("price"));
                                    dish.setSoldOut(oDish.getBoolean("isSoldOut"));
                                    dish.setNew(oDish.getBoolean("isNew"));
                                    dish.setSpecial(oDish.getBoolean("isSpecial"));
                                    dish.setSoldOut(oDish.getBoolean("isSoldOut"));
                                    dish.setHotLevel(oDish.getInt("hotLevel"));
                                    dish.setCategory2(c2);
                                    c2.addDish(dish);
                                }
                            }
                        }
                    }
                }
                sortAllMenu(listC1);
                return listC1;
            }
            return null;
        } else {
            //TODO: do what if return false from server
            return null;
        }
    }

    //sort by sequence
    private void sortAllMenu(List<Category1> c1s){
        if (c1s != null){
            Collections.sort(c1s, new Comparator<Category1>() {
                @Override
                public int compare(Category1 o1, Category1 o2) {
                    return o1.getSequence() - o2.getSequence();
                }
            });
            for (Category1 c1 : c1s) {
                if (c1.getCategory2s() != null){
                    Collections.sort(c1.getCategory2s(), new Comparator<Category2>() {
                        @Override
                        public int compare(Category2 o1, Category2 o2) {
                            return o1.getSequence() - o2.getSequence();
                        }
                    });
                    for (Category2 c2 : c1.getCategory2s()) {
                        if(c2.getDishes() != null){
                            Collections.sort(c2.getDishes(), new Comparator<Dish>() {
                                @Override
                                public int compare(Dish o1, Dish o2) {
                                    return o1.getSequence() - o2.getSequence();
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void onFailedLoadMenu(){
        //TODO: require restart app
    }

    private void loadDishPictureFromServer(){
        flagFinishLoadDishPictures.clear();
        DownloadQueue queue = NoHttp.newDownloadQueue();
        for (String filename : listDishPictures) {
            flagFinishLoadDishPictures.put(InstantValue.LOCAL_CATALOG_DISH_PICTURE + filename, false);
            String url = InstantValue.URL_TOMCAT + "/../"+ InstantValue.CATALOG_DISH_PICTURE+"/"+ filename;
            DownloadRequest request = NoHttp.createDownloadRequest(url, RequestMethod.GET, InstantValue.LOCAL_CATALOG_DISH_PICTURE, filename, true, true);
            queue.add(WHAT_VALUE_DOWNLOADIMAGE, request, downloadDishListener);
        }
    }

    private void doResponseMakeOrder(Response<JSONObject> response){
        JSONObject result = response.get();
        try {
            boolean b = Boolean.valueOf(result.get("success").toString());
            if (b){
                int orderSequence = Integer.parseInt(result.get("sequence").toString());
                mainActivity.onFinishMakeOrder(orderSequence);
            } else {
                mainActivity.popupWarnDialog(R.drawable.error, "WRONG", "Something wrong happened while making order! \n\nError message : " + result.get("result"));
            }
        } catch (JSONException e) {
            Log.e("HttpOperator", "JSON exception in doResponseMakeOrder.");
            e.printStackTrace();
        }
    }

    /**
     * deal return for check confirm code. if code is right,
     * @param response
     */
//    private void doResponseConfirmCode4RefreshData(Response<JSONObject> response){
//        JSONObject result = response.get();
//        try {
//            boolean b = Boolean.valueOf(result.get("success").toString());
//            if (b){
//                mainActivity.onRefreshData();
//            } else {
//                mainActivity.popupWarnDialog(R.drawable.error, "WRONG", "Confirmation Code is wrong!");
//            }
//        } catch (JSONException e) {
//            Log.e("HttpOperator", "JSON exception in checkConfirmCode");
//            e.printStackTrace();
//        }
//    }

    /**
     * first check the CONFIRM CODE, if it is right, make order
     * @param orders
     * @param deskid
     */
    public void makeOrder(String code, String orders, int deskid, int customerAmount){
        Request<JSONObject> makeOrderRequest = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/indent/makeindent", RequestMethod.POST);
        makeOrderRequest.add("confirmCode", code);
        makeOrderRequest.add("indents", orders);
        makeOrderRequest.add("deskid", deskid);
        makeOrderRequest.add("customerAmount", customerAmount);
        requestQueue.add(WHAT_VALUE_MAKEORDER, makeOrderRequest, responseListener);
    }

    /**
     * check the menu version difference between client and server
     * @param localVersion
     * @return if same return null, otherwise return the List of Changed DishId
     */
    public List<Integer> chechMenuVersion(int localVersion){
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/menu/checkmenuversion", RequestMethod.POST);
        request.add("versionId", localVersion);
        Response<JSONObject> response = NoHttp.startRequestSync(request);
        try{
            if (response.get() == null) {
                Log.e("HttpError", "Error occur while synchronize menu. response.get() is null.");
                return null;
            }
            boolean success = response.get().getBoolean("success");
            if (success){
                if ("null".equals(response.get().getString("infos")))
                    return null;
                JSONArray infos = response.get().getJSONArray("infos");
                if (infos == null || infos.length() == 0) {
                    return null; // if no change, return null
                } else {
                    DBOperator dbOpr = mainActivity.getDbOperator();
                    //collect all change into a set to remove the duplicate dishid
                    Set<Integer> dishIdSet = new HashSet<Integer>();
                    int newVersion = 0;
                    for (int i = 0; i < infos.length(); i++) {
                        JSONObject info = (JSONObject) infos.get(i);
                        dishIdSet.add(info.getInt("dishId"));
                        if (info.getInt("id") > newVersion)
                            newVersion = info.getInt("id");
                    }
                    //reload info about dishes in dishIdSet
                    List<Integer> dishIdList = new ArrayList<Integer>();
                    dishIdList.addAll(dishIdSet);
                    for (int i = 0; i < dishIdList.size(); i++) {
                        Request<JSONObject> reqDish = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/menu/querydishbyid", RequestMethod.POST);
                        reqDish.add("dishId", dishIdList.get(i));
                        Response<JSONObject> respDish = NoHttp.startRequestSync(reqDish);
                        if (respDish.get().getBoolean("success")){
                            //only do SOLDOUT property here
                            if (respDish.get().getBoolean("success")){
                                JSONObject joDishInfo = respDish.get().getJSONArray("dishes").getJSONObject(0);
                                Dish dish = dbOpr.queryDishById(dishIdList.get(i));
                                if (dish != null){
                                    dish.setSoldOut(joDishInfo.getBoolean("isSoldOut"));
                                    dbOpr.updateObject(dish);
                                }
                            }
                        } else {
                            Log.e("HttpError", "get a fail response while call menu/querydishbyid for dishid = "+ dishIdList.get(i));
                        }
                    }
                    //update menu version.
                    dbOpr.deleteAllData(MenuVersion.class);
                    MenuVersion mv = new MenuVersion(1, newVersion);
                    dbOpr.saveObjectByCascade(mv);

                    return dishIdList;
                }
            } else {
                Log.e("HttpError", "Check Menu Version Error");
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage(), e);
        }
        return null;
    }


}
