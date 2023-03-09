package com.example.codefestpractice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DetailsCustomAdapter.OnItemClickListener {

    private static final String TAG = "error";
    private EditText txtName, txtContact, txtDOB;
    private Button btnInsert, btnUpdate, btnDelete, btnView;

    private RecyclerView detailsRv;
    DetailsCustomAdapter detailsCustomAdapter;
    RecyclerView.LayoutManager layoutManager;


    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            txtName = findViewById(R.id.txtName);
            txtContact = findViewById(R.id.txtPhone);
            txtDOB = findViewById(R.id.txtDOB);
            btnInsert = findViewById(R.id.btnInsert);
            btnUpdate = findViewById(R.id.btnUpdate);
            btnDelete = findViewById(R.id.btnDelete);
            btnView = findViewById(R.id.btnView);
            dbHelper = new DBHelper(this); // this is to initialize the DBHelper class

            //this initialization is about the RecyclerView, it's custom Adapter, and the onItemClick listener
            detailsRv = findViewById(R.id.rvDetails);
            detailsCustomAdapter = new DetailsCustomAdapter(dbHelper.getAllData(), this);
            layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
            detailsRv.setLayoutManager(layoutManager);
            detailsRv.setAdapter(detailsCustomAdapter);
            detailsCustomAdapter.setOnItemClickListener(this);

            addDetails();
            updateDetails();
            delete();
            view();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: ", e);
        }
    }

    private void view(){
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    //this part shows only how to display data from database manually like without RecyclerView
                    Cursor cursor = dbHelper.getData();
                    if(cursor.getCount() == 0){
                        Toast.makeText(MainActivity.this, "No entries", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    StringBuffer buffer = new StringBuffer();

                    while(cursor.moveToNext()){
                        buffer.append("Name: " + cursor.getString(1) + "\n");
                        buffer.append("Phone number: " + cursor.getString(2) + "\n");
                        buffer.append("Date of birth: " + cursor.getString(3) + "\n\n");
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("User entries");
                    builder.setMessage(buffer.toString());
                    builder.show();
                } catch (Exception e){
                    Log.e(TAG, "onClick: ", e);
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addDetails() {
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = txtName.getText().toString();
                    String num = txtContact.getText().toString().trim();
                    String dob = txtDOB.getText().toString();
                    boolean add = dbHelper.addDetails(name, num, dob);

                    if (add){
                        detailsCustomAdapter.update(dbHelper.getAllData());
                        detailsCustomAdapter.setOnItemClickListener(MainActivity.this);
                        txtName.setText("");
                        txtDOB.setText("");
                        txtContact.setText("");
                        Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void delete(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = txtName.getText().toString();

                    boolean add = dbHelper.delete(name);
                    if (add){
                        detailsCustomAdapter.update(dbHelper.getAllData());
                        txtName.setText("");
                        txtDOB.setText("");
                        txtContact.setText("");
                        Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Log.e(TAG, "onClick: ", e);
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDetails(){
        try {
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String name = txtName.getText().toString();
                        String num = txtContact.getText().toString().trim();
                        String dob = txtDOB.getText().toString();
                        boolean update = dbHelper.update(name, num, dob);
                        if (update){
                            detailsCustomAdapter.update(dbHelper.getAllData());
                            detailsCustomAdapter.setOnItemClickListener(MainActivity.this);
                            txtName.setText("");
                            txtDOB.setText("");
                            txtContact.setText("");
                            Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: ", e);
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "updateDetails: ", e);
        }
    }

    @Override
    public void onItemClick(int position) {
        Details item = dbHelper.getAllData().get(position);

        txtName.setText(item.getName());
        txtDOB.setText(item.getDob());
        txtContact.setText(item.getPhone());
    }
}