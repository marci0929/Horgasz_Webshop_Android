package com.example.kotprog_horgasz_webshop;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WebshopActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private RecyclerView recyclerView;
    private ArrayList<Item> itemList;
    private ItemAdapter itemAdapter;
    private FirebaseFirestore firestore;
    private NotiHelper notiHelper;
    ConnectivityManager connManager;
    NetworkInfo wifiInfo;

    private CollectionReference itemsCollRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webshop);
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        notiHelper = new NotiHelper(this);


    }

    private void getDataFromFirestore() {
        itemList.clear();
        if(wifiInfo.isConnected()){
            firestore.collection("fishingItems").orderBy("nev").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Item item = document.toObject(Item.class);
                    itemList.add(item);
                }
                if (itemList.size() <= 1) {
                    fillUpDatas();
                    getDataFromFirestore();
                }
                itemAdapter.notifyDataSetChanged();
            });
        }
        else {
            firestore.collection("fishingItems").orderBy("nev").limit(4).get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Item item = document.toObject(Item.class);
                    itemList.add(item);
                }
                if (itemList.size() <= 1) {
                    fillUpDatas();
                    getDataFromFirestore();
                }
                itemAdapter.notifyDataSetChanged();
            });
        }

    }



    private void fillUpDatas() {
        String[] itemName = getResources().getStringArray(R.array.itemName);
        String[] itemPrice = getResources().getStringArray(R.array.itemPrice);
        TypedArray itemPicture = getResources().obtainTypedArray(R.array.itemImage);

        for (int i = 0; i < itemName.length; i++) {
            itemsCollRef.add(new Item(itemName[i], itemPrice[i], itemPicture.getResourceId(i, 0)));
        }
        itemPicture.recycle();

        itemAdapter.notifyDataSetChanged();
    }

    public void addItemToCart(Item item) {
        Kosar.setKosarbanDarab(Kosar.getKosarbanDarab()+1);

        ArrayList<Item> a = Kosar.getKosar();
        a.add(item);
        notiHelper.send(item.getNev());
        Kosar.setKosar(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.felso_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutIcon:
                Log.d(LOG_TAG, "Logout clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.cartIcon:
                Log.d(LOG_TAG, "Kosar clicked!");
                watchCart();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void watchCart() {
        Intent intent = new Intent(this, Kosar.class);
        onPause();
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_webshop);

        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter( itemList, this);
        recyclerView.setAdapter(itemAdapter);

        firestore = FirebaseFirestore.getInstance();
        itemsCollRef = firestore.collection("fishingItems");
        getDataFromFirestore();
    }
}