package com.fireblend.uitest.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fireblend.uitest.R;
import com.fireblend.uitest.bd.Contact;
import com.fireblend.uitest.bd.DataBaseHelper;
import com.fireblend.uitest.utilities.Utilities;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends AppCompatActivity {

    private final String TAG = "CONTACTACTIVITY";

    DataBaseHelper bdHelper;
    @BindView(R.id.edittext_name) TextView name;
    @BindView(R.id.textview_age) TextView ageText;
    @BindView(R.id.seekbar_age) SeekBar age;
    @BindView(R.id.edittext_email) TextView email;
    @BindView(R.id.edittext_phone) TextView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        bdHelper = DataBaseHelper.getInstance(ContactActivity.this);
        Log.d(TAG, "OnCreate");
        age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ageText.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @OnClick(R.id.button_submit)
    public void addContact(View v) {
        // Recuperar los valores introducidos
        String nombre = name.getText().toString();
        Integer edad = age.getProgress();
        String correo = email.getText().toString();
        String telefono = phone.getText().toString();

        //Si el mensaje esta vacio...
        if(Utilities.isEmptyField(nombre) || Utilities.isEmptyField(correo) || Utilities.isEmptyField(telefono) || edad.equals(0)) {
            //Mostramos un error
            Toast.makeText(ContactActivity.this,
                    R.string.empty_msg,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Contact newContact = new Contact(nombre, edad, correo, telefono);
        this.addDBContact(newContact);
    }


    public void addDBContact (Contact contact) {
        try {
            Dao<Contact, Integer> contactDao = bdHelper.getContactDao();
            contactDao.create(contact);
            Intent myIntent = new Intent(ContactActivity.this, MainActivity.class);
            startActivity(myIntent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Contact getDBContact (DataBaseHelper helper, int contactId) {
        Contact foundContact = null;
        try {
            Dao<Contact, Integer> contactDao = helper.getContactDao();
            foundContact = contactDao.queryForId(contactId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundContact;
    }

    public static void removeDBContact (DataBaseHelper helper, Contact contact) {
        try {
            Dao<Contact, Integer> contactDao = helper.getContactDao();
            contactDao.delete(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
