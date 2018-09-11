package com.fireblend.uitest.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import com.fireblend.uitest.R;
import com.fireblend.uitest.bd.DataBaseHelper;
import com.fireblend.uitest.bd.Contact;
import com.fireblend.uitest.adapters.ContactAdapter;
import com.fireblend.uitest.utilities.PreferenceManager;
import com.j256.ormlite.dao.Dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MAINACTIVITY";
    DataBaseHelper bdHelper;
    @BindView(R.id.recyclerview_lista_contactos) RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d(TAG, "OnCreate");
        bdHelper = DataBaseHelper.getInstance(MainActivity.this);
        setupList();
    }


    public List<Contact> getHCContacts(){
        final List<Contact> contacts = new ArrayList();
        //Lista ejemplo con datos estaticos. Normalmente, estos serían recuperados de una BD o un REST API.
        contacts.add(new Contact("Sergio", 28, "sergiome@gmail.com", "88854764"));
        contacts.add(new Contact("Andres", 1, "alex@gmail.com", "88883644"));
        contacts.add(new Contact("Andrea", 2, "andrea@gmail.com", "98714764"));
        contacts.add(new Contact("Fabian", 3, "fabian@gmail.com", "12345678"));
        contacts.add(new Contact("Ivan", 4, "ivan@gmail.com", "87654321"));
        contacts.add(new Contact("Gabriela", 5, "gabriela@gmail.com", "09871234"));
        contacts.add(new Contact("Alex", 6, "sergiome@gmail.com", "43215678"));
        return contacts;
    }

    public void setupList() {
        final List<Contact> contacts = this.getContactsFromDB();
        //Le asignamos a la lista un nuevo BaseAdapter, implementado a continuación
        boolean enableGrid = PreferenceManager.getEnableGridFromPreferences(this);

        RecyclerView.LayoutManager displayLayout = new LinearLayoutManager(this);
        if(enableGrid){
            displayLayout = new GridLayoutManager(this,2);
        }

        list.setLayoutManager(displayLayout);

        list.setAdapter(new ContactAdapter(contacts));
    }

    public List<Contact> getContactsFromDB(){

        List<Contact> contactsList = new ArrayList<>();

        try {
            Dao<Contact, Integer> contactDao = bdHelper.getContactDao();
            contactsList = contactDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactsList;
    }

    public void SwitchActivity(Class activity){
        Intent myIntent = new Intent(this, activity);
        startActivity(myIntent);
    }

    @OnClick(R.id.button_preferences)
    public void goPreferences(View v) {
        this.SwitchActivity(PreferenceActivity.class);
    }

    @OnClick(R.id.add_contact)
    public void addContact(View v) {
        this.SwitchActivity(ContactActivity.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
