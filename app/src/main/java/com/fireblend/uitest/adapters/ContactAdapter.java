package com.fireblend.uitest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fireblend.uitest.R;
import com.fireblend.uitest.bd.DataBaseHelper;
import com.fireblend.uitest.bd.Contact;
import com.fireblend.uitest.ui.ContactActivity;
import com.fireblend.uitest.ui.MainActivity;
import com.fireblend.uitest.utilities.PreferenceManager;
import com.j256.ormlite.stmt.query.In;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private final String TAG = "CONTACTADAPTER";
    private Context parentContext;

    List<Contact> contacts = new ArrayList();
    DataBaseHelper bdHelper;
    MaterialDialog confirmDialog;

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // es como getView del gridView
        parentContext = parent.getContext();
        bdHelper = DataBaseHelper.getInstance(parentContext);
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.assignData(this.contacts.get(position), position);
    }
    @Override
    public int getItemCount() { // como getCount
        return contacts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView phone;
        TextView email;
        Button btnDisplay;
        Button btnDelete;
        LinearLayout contactCard;
        String bgColor;
        Integer textFontSize;

        public ViewHolder(View view) {
            super(view);
            name  = view.findViewById(R.id.name);
            age   =  view.findViewById(R.id.age);
            phone = view.findViewById(R.id.phone);
            email = view.findViewById(R.id.email);
            btnDisplay = (Button) view.findViewById(R.id.row_btn);
            btnDelete = (Button) view.findViewById(R.id.button_delete_contact);
            contactCard = view.findViewById(R.id.contact_container);

            Context context = view.getContext();
            bgColor = PreferenceManager.getBgColorFromPreferences(context);
            textFontSize = PreferenceManager.getTextsizeFromPreferences(context);

            LinearLayout cardLayout = view.findViewById(R.id.layout_card);
            this.setEditTextSizeFromView(cardLayout, textFontSize);

            boolean deleteVisible = PreferenceManager.getDeleteVisibilityFromPreferences(context);

            if(deleteVisible){
                btnDelete.setVisibility(View.VISIBLE);
            } else {
                btnDelete.setVisibility(View.GONE);
            }
        }

        public void setEditTextSizeFromView(LinearLayout myLayout, Integer fontSize){

            for( int i = 0; i < myLayout.getChildCount(); i++ ) {
                if (myLayout.getChildAt(i) instanceof TextView)
                    ((TextView)myLayout.getChildAt(i)).setTextSize(fontSize);
            }
        }


        public void assignData(final Contact current, int position) {
            if(!bgColor.equals("")) {
                contactCard.setBackgroundColor(Color.parseColor(bgColor));
            }
            name.setText(current.name);
            age.setText(String.valueOf(current.age));
            phone.setText(current.phone);
            email.setText(current.email);


            //Basandonos en la posicion provista en este metodo, proveemos los datos
            //correctos para este elemento.
            final int contactPosition = position;

            btnDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(parentContext, "Hola, "+ current.name, Toast.LENGTH_SHORT).show();
                }
            });

            confirmDialog = this.createConfirmDialogue(parentContext);


            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "contactPosition");
                    Log.d(TAG, current.contactId+"");
                    showConfirmDialog(parentContext, current.contactId);
                }
            });
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

        public void showConfirmDialog(Context context, final int contactId) {
            confirmDialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    Log.d(TAG, "contactPosition");
                    com.fireblend.uitest.bd.Contact current = ContactActivity.getDBContact(bdHelper, contactId);
                    Log.d(TAG, "GOT CURRENT");
                    ContactActivity.removeDBContact(bdHelper, current);
                    ((MainActivity) parentContext).setupList();
                }
            });
            confirmDialog.show();
        }

    }
}
