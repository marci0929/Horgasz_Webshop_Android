package com.example.kotprog_horgasz_webshop;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Kosar extends AppCompatActivity {

    TextView itemName;
    TextView itemPrice;
    TextView textOsszeg;
    TextView textSzallitas;
    //LocationGetter locget;

    private static ArrayList<Item> kosar = new ArrayList<>();
    private static int kosarbanDarab = 0;

    public Kosar() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kosar);

        init();
    }

    public void backToWebshop(View view) {
        onBackPressed();
    }

    public void init(){
        TableLayout ll = (TableLayout) findViewById(R.id.kosarTable);

        for (int i = 0; i < kosarbanDarab; i++) {

            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setPadding(0, 5, 0, 5);
            row.setLayoutParams(lp);
            itemName = new TextView(this);
            itemPrice = new TextView(this);
            String neve = kosar.get(i).getNev() + ": ";
            itemName.setText(neve);
            itemPrice.setText(kosar.get(i).getAr());

            row.addView(itemName);
            row.addView(itemPrice);
            ll.addView(row,i);
        }
        int osszeg = 0;
        for(Item i : kosar){
            osszeg+=Integer.parseInt(i.getAr().substring(0, i.getAr().length()-3));
        }
        textOsszeg = findViewById(R.id.osszeg);
        String osszege = "Összeg: "+osszeg+" Ft";
        textOsszeg.setText(osszege);

    }

    public TextView getItemName() {
        return itemName;
    }

    public void setItemName(TextView itemName) {
        this.itemName = itemName;
    }

    public TextView getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(TextView itemPrice) {
        this.itemPrice = itemPrice;
    }

    public static ArrayList<Item> getKosar() {
        return kosar;
    }

    public static void setKosar(ArrayList<Item> kosar) {
        Kosar.kosar = kosar;
    }

    public static int getKosarbanDarab() {
        return kosarbanDarab;
    }

    public static void setKosarbanDarab(int kosarbanDarab) {
        Kosar.kosarbanDarab = kosarbanDarab;
    }

    public void placeOrder(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.megrendeles_sikeres, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                kosar.clear();
                kosarbanDarab=0;
                finish();
                return true;
            }
        });

    }

    public void deleteCart(View view) {
        kosar.clear();
        kosarbanDarab=0;
        finish();
    }
}