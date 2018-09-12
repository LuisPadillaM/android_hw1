package com.fireblend.uitest.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fireblend.uitest.R;
import com.fireblend.uitest.bd.Contact;
import com.fireblend.uitest.bd.DataBaseHelper;
import com.fireblend.uitest.utilities.Utilities;
import com.j256.ormlite.dao.Dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    @BindView(R.id.button_savefile) Button saveFile;

    private static final int PERM_CODE = 1001;
    String nombre;
    Integer edad;
    String correo;
    String telefono;

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

    public void setValuesFromFields(){
        nombre = name.getText().toString();
        edad = age.getProgress();
        correo = email.getText().toString();
        telefono = phone.getText().toString();
    }

    public boolean emptyFormValues(){
        return Utilities.isEmptyField(nombre) || Utilities.isEmptyField(correo) || Utilities.isEmptyField(telefono) || edad.equals(0);
    }
    @OnClick(R.id.button_submit)
    public void addContact(View v) {
        // Recuperar los valores introducidos
        this.setValuesFromFields();
        //Si el mensaje esta vacio...
        if(this.emptyFormValues()) {
            //Mostramos un error
            Toast.makeText(ContactActivity.this,
                    R.string.empty_msg,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Contact newContact = new Contact(nombre, edad, correo, telefono);
        this.addDBContact(newContact);
    }


    @OnClick(R.id.button_savefile)
    public void saveContactToFile(View v) {  //Este método necesita permiso de escritura a almacenamiento externo
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            writeFile();
        } else {
            askForPermission();
        }

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public void writeFile(){
        this.setValuesFromFields();
        if(!this.emptyFormValues()) {
            Contact newContact = new Contact(nombre, edad, correo, telefono);
            String fileName = "contact"+newContact.contactId+".csv";

            try {

            File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));
            if (!dir.mkdirs()) {
                Log.e(TAG, "Directory not created");
            }

            File file = new File(dir, fileName);
            if(!file.exists()){
                file.createNewFile();
                Log.e(TAG, "file created");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true ));/*append*/
            writer.write(newContact.toString());
            writer.close();
            } catch (IOException e) {
                Log.e(TAG, "error creating file");
                e.printStackTrace();
            }

        }
    }

    private void askForPermission() {
        //Se solicita permiso. Llamada es asincronica, por lo que tenemos que
        //implementar el metodo callback onRequestPermissionResult para procesar la respuesta del usuario (ver abajo)
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Si recibimos al menos un permiso y su valor es igual a PERMISSION_GRANTED, tenemos permiso
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            saveFile.callOnClick();
        } else {
            Toast.makeText(this, ":(", Toast.LENGTH_SHORT).show();
        }
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
