package com.shuishou.digitalmenu.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shuishou.digitalmenu.R;
import com.shuishou.digitalmenu.bean.Desk;
import com.shuishou.digitalmenu.http.HttpOperator;
import com.shuishou.digitalmenu.uibean.ChoosedFood;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */

public class PostOrderDialog {

    private Spinner spDesk;
    private EditText txtCode;
    private Spinner spCustomerAmount;

    private List<ChoosedFood> choosedFoodList;
    private HttpOperator httpOperator;

    private AlertDialog dlg;

    private MainActivity mainActivity;

    public PostOrderDialog(@NonNull MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initUI();
    }

    private void initUI(){
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.makeorder_layout, null);
        txtCode = (EditText) view.findViewById(R.id.txt_confirmcode);
        spDesk = (Spinner) view.findViewById(R.id.spinner_desk);
        spCustomerAmount = (Spinner) view.findViewById(R.id.spinner_customer);
        spCustomerAmount.setSelection(0);
        initDeskData(mainActivity.getDesks());
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Confirm");
        builder.setMessage("Please input the CONFIRMATION CODE before post this order!");
        builder.setIcon(R.drawable.info);
        builder.setPositiveButton("Yes", null);
        builder.setNegativeButton("No", null);
        builder.setView(view);
        dlg = builder.create();
        dlg.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //add listener for YES button
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeOrder();
                    }
                });
            }
        });
    }

    public void showDialog(HttpOperator httpOperator, List<ChoosedFood> choosedFoodList){
        this.choosedFoodList = choosedFoodList;
        this.httpOperator = httpOperator;
        dlg.show();
    }

    private void makeOrder(){
        if (txtCode.getText() == null || txtCode.getText().length() == 0) {
            Toast.makeText(mainActivity, "Please input the Confirmation Code to post this order!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spDesk.getSelectedItem() == null){
            Toast.makeText(mainActivity, "Please select the desk before post this order!", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONArray os = null;
        try {
            os = generateOrderJson();
        } catch (JSONException e) {
            Toast.makeText(mainActivity, "Please select the desk before post this order!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (os != null){
            httpOperator.makeOrder(txtCode.getText().toString(), os.toString(),
                    ((Desk)spDesk.getSelectedItem()).getId(),
                    Integer.parseInt(spCustomerAmount.getSelectedItem().toString()));
        }
    }

    private JSONArray generateOrderJson() throws JSONException {
        JSONArray ja = new JSONArray();
        for(ChoosedFood cf: choosedFoodList){
            JSONObject jo = new JSONObject();
            jo.put("id", cf.getDish().getId());
            jo.put("amount", cf.getAmount());
            jo.put("addtionalRequirements", cf.getAdditionalRequirements());
            ja.put(jo);
        }
        return ja;
    }
    public void initDeskData(List<Desk> desks){
        ArrayAdapter<Desk> deskArrayAdapter = new ArrayAdapter<Desk>(mainActivity
                , android.R.layout.simple_spinner_item, desks);
        deskArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDesk.setAdapter(deskArrayAdapter);
    }

    //clear up old data in the components
    public void clearup(){
        spDesk.setSelection(-1);
        txtCode.setText("");
    }

    public void dismiss(){
        dlg.dismiss();
    }
}
