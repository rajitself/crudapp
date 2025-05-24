package com.example.mark10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class MainActivity extends AppCompatActivity {
TextView total_records;
Button add;
RecyclerView rv;
ArrayList<DataModel> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        total_records = findViewById(R.id.total_records);
        add = findViewById(R.id.add);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Voters");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();

                for(DataSnapshot ds: snapshot.getChildren()){
                    DataModel model = ds.getValue(DataModel.class);
                    model.setKey(ds.getKey());
                    data.add(model);
                }
                total_records.setText("Total Records: "+ data.size());
                rv.setAdapter(new CustomAdapter(MainActivity.this,data));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add,null);
                builder.setView(v);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                EditText edVoterID = v.findViewById(R.id.edVoterID);
                EditText edVoterName = v.findViewById(R.id.edVoterName);
                EditText edVoterAddress = v.findViewById(R.id.edVoterAddress);
                EditText edVoterPhone = v.findViewById(R.id.edVoterPhone);

                Button add_data = v.findViewById(R.id.btnadd);

                add_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = edVoterID.getText().toString().trim();
                        String name = edVoterName.getText().toString().trim();
                        String address = edVoterAddress.getText().toString().trim();
                        String phone = edVoterPhone.getText().toString().trim();

                        if (id.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                            Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        HashMap<String, String> map = new HashMap<>();
                        map.put("voterID", id);
                        map.put("voterName", name);
                        map.put("voterAddress", address);
                        map.put("voterPhone", phone);

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Voters");
                        db.push().setValue(map);
                        alertDialog.dismiss();

                    }
                });
            }
        });
    }
}