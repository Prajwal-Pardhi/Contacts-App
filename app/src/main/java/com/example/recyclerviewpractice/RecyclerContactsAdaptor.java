package com.example.recyclerviewpractice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerContactsAdaptor extends RecyclerView.Adapter<RecyclerContactsAdaptor.ViewHolder> {

    Context context;

    ArrayList<ContactModel> arrayContact;

    RecyclerContactsAdaptor(Context context,  ArrayList<ContactModel> arrayContact){
        this.context=context;
        this.arrayContact = arrayContact;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.contact_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imgContact.setImageResource(R.drawable.a);
        holder.txtName.setText(arrayContact.get(position).name);
        holder.txtNumber.setText(arrayContact.get(position).number);

        int PERMISSION_CODE =100;
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);
        }


        holder.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_contact_lay);
                dialog.show();

                Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
                Button btnDelete = dialog.findViewById(R.id.btnDelete);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnCall = dialog.findViewById(R.id.btnCall);


                //Call Option
                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phone_no = arrayContact.get(holder.getAdapterPosition()).number;

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+phone_no));
                        context.startActivity(intent);

                    }
                });

                //Update Option
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        Dialog updateDialog = new Dialog(context);
                        updateDialog.setContentView(R.layout.add_update_lay);

                        EditText edtName,edtNumber;
                        Button btnAction;
                        TextView txtTitle;

                        edtName=updateDialog.findViewById(R.id.edtName);
                        edtNumber=updateDialog.findViewById(R.id.edtNumber);
                        btnAction=updateDialog.findViewById(R.id.btnAction);
                        txtTitle = updateDialog.findViewById(R.id.txtTitle);


                        btnAction.setText("Update");
                        txtTitle.setText("Update Contact");

                        edtName.setText(arrayContact.get(holder.getAdapterPosition()).name);
                        edtNumber.setText(arrayContact.get(holder.getAdapterPosition()).number);

                        updateDialog.show();

                        btnAction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String name="",number="";
                                if(!edtName.getText().toString().equals("")){
                                    name=edtName.getText().toString();
                                }else{
                                    Toast.makeText(context, "Please enter the name", Toast.LENGTH_SHORT).show();
                                }

                                if(!edtNumber.getText().toString().equals("")){
                                    number=edtNumber.getText().toString();
                                }else{
                                    Toast.makeText(context, "Please enter the number", Toast.LENGTH_SHORT).show();
                                }

                                MyDBHelper dbHelper = new MyDBHelper(context);
                                ContactModel model = new ContactModel();
                                //model.id = position+1;
                                model.name = name;
                                model.number = number;

                                dbHelper.updateContact(model,arrayContact.get(holder.getAdapterPosition()).number);
                                arrayContact.set(holder.getAdapterPosition(),new ContactModel(arrayContact.get(holder.getAdapterPosition()).img,name,number));
                                notifyItemChanged(holder.getAdapterPosition());



                                updateDialog.dismiss();
                            }
                        });
                    }
                });

                //Delete Option
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setTitle("Delete Contact")
                                .setMessage("Are you sure you want to delete this contact?")
                                .setIcon(R.drawable.ic_baseline_delete_24)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText edtNumber;


                                        MyDBHelper dbHelper = new MyDBHelper(context);
                                        dbHelper.deleteContact(arrayContact.get(holder.getAdapterPosition()).number);
                                        arrayContact.remove(holder.getAdapterPosition());

                                        notifyItemRemoved(holder.getAdapterPosition());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.show();
                    }
                });

                //Cancel Option
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtNumber;
        ImageView imgContact;
        LinearLayout llRow;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            imgContact = itemView.findViewById(R.id.imgContact);
            llRow = itemView.findViewById(R.id.llRow);

        }
    }

}
