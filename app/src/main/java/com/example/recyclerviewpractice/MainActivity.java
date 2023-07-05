package com.example.recyclerviewpractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
RecyclerView recyclerView;
ArrayList<ContactModel> arrContacts = new ArrayList<>();
ArrayList<ContactModel> arrayContacts = new ArrayList<>();
FloatingActionButton btnOpenDialog;
RecyclerContactsAdaptor adaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Contacts");

        MyDBHelper dbHelper = new MyDBHelper(this);                        //MyDBHelper

        recyclerView = findViewById(R.id.recyclerContact);
        btnOpenDialog = findViewById(R.id.btnOpenDialog);


        //Performing action to Add new contact
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_update_lay);

                EditText edtName,edtNumber;
                Button btnAction;

                edtName=dialog.findViewById(R.id.edtName);
                edtNumber=dialog.findViewById(R.id.edtNumber);
                btnAction=dialog.findViewById(R.id.btnAction);

                dialog.show();

                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name="",number="";
                        if(!edtName.getText().toString().equals("")){
                            name=edtName.getText().toString();
                        }else{
                            Toast.makeText(MainActivity.this, "Please enter the name", Toast.LENGTH_SHORT).show();
                        }

                        if(!edtNumber.getText().toString().equals("")){
                            number=edtNumber.getText().toString();
                        }else{
                            Toast.makeText(MainActivity.this, "Please enter the number", Toast.LENGTH_SHORT).show();
                        }

                        //arrContacts.add(new ContactModel(R.drawable.a,name,number));
                        dbHelper.addContact(name,number);
                        ContactModel model=new ContactModel();
                        model.name=name;
                        model.number=number;
                        model.img=R.drawable.a;

                        arrayContacts.add(model);

                        adaptor.notifyItemInserted(arrayContacts.size()-1);
                        adaptor.notifyDataSetChanged();

                        recyclerView.scrollToPosition(arrayContacts.size()-1);
                        dialog.dismiss();
                    }
                });

            }
        });
        //

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayContacts = dbHelper.fetchContact();

        //
        //Passing parameters to adaptor
        adaptor = new RecyclerContactsAdaptor(this, arrayContacts);
        //
        //Setting Adaptor to recycler view
        recyclerView.setAdapter(adaptor);
        //

    }


}