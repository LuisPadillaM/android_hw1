package com.fireblend.uitest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fireblend.uitest.R;
import com.fireblend.uitest.bd.DataBaseHelper;
import com.fireblend.uitest.entities.Contact;
import com.fireblend.uitest.ui.ContactActivity;
import com.fireblend.uitest.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends  BaseAdapter {

    private final String TAG = "CONTACTADAPTER";
    private Context parentContext;

    List<Contact> contacts = new ArrayList();
    DataBaseHelper bdHelper;
    MaterialDialog confirmDialog;

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    //Retorna el numero de elementos en la lista.
    public int getCount() {
        return contacts.size();
    }

    @Override
    //Retorna el elemento que pertenece a la posición especificada.
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    //Devuelve un identificador único para cada elemento de la lista.
    //En nuestro caso, basta con devolver la posición del elemento en la lista.
    public long getItemId(int position) {
        return position;
    }

    @Override
    //Devuelve la vista que corresponde a cada elemento de la lista
    public View getView(int position, View convertView, final ViewGroup parent) {
        parentContext = parent.getContext();
        bdHelper = DataBaseHelper.getInstance(parentContext);
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        View row = inflater.inflate(R.layout.contact_item, parent, false);

        Contact current = (Contact)this.getItem(position);

        TextView name = (TextView) row.findViewById(R.id.name);
        TextView age = (TextView) row.findViewById(R.id.age);
        TextView phone = (TextView) row.findViewById(R.id.phone);
        TextView email = (TextView) row.findViewById(R.id.email);

        name.setText(current.name);
        age.setText(String.valueOf(contacts.get(position).age));
        phone.setText(current.phone);
        email.setText(current.email);

        Button btnDisplay = (Button) row.findViewById(R.id.row_btn);
        Button btnDelete = (Button) row.findViewById(R.id.button_delete_contact);

        //Basandonos en la posicion provista en este metodo, proveemos los datos
        //correctos para este elemento.
        final int contactPosition = position;

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parentContext, "Hola, "+contacts.get(contactPosition).name, Toast.LENGTH_SHORT).show();
            }
        });

        confirmDialog = this.createConfirmDialogue(parentContext);


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "contactPosition");
                Log.d(TAG, contactPosition+"");
                showConfirmDialog(parentContext, contactPosition);
            }
        });


        return row;
    }

    private MaterialDialog createConfirmDialogue (Context context){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(R.string.dialog_confirm_title)
                .content(R.string.dialog_confirm_content)
                .positiveText(R.string.dialog_confirm_confirm)
                .negativeText(R.string.dialog_confirm_cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                });

        MaterialDialog dialog = builder.build();
        return dialog;
    }

    public void showConfirmDialog(Context context, final int contactPosition) {
        confirmDialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                Log.d(TAG, "contactPosition");
                com.fireblend.uitest.bd.Contact current = ContactActivity.getDBContact(bdHelper, contactPosition);
                Log.d(TAG, "GOT CURRENT");
                ContactActivity.removeDBContact(bdHelper, current);
                ((MainActivity) parentContext).setupList();
            }
        });
        confirmDialog.show();
    }
}
