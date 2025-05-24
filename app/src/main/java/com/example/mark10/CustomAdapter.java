package com.example.mark10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context c;
    ArrayList<DataModel> data;

    public CustomAdapter(MainActivity mainActivity, ArrayList<DataModel> data) {
        c = mainActivity;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.view_singleitem, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataModel model = data.get(position);

        holder.id.setText("Voter ID: " + model.getVoterID());
        holder.name.setText("Name: " + model.getVoterName());
        holder.address.setText("Address: " + model.getVoterAddress());
        holder.phone.setText("Phone: " + model.getVoterPhone());

        holder.id.setText(model.getVoterID());
        holder.name.setText(model.getVoterName());
        holder.address.setText(model.getVoterAddress());
        holder.phone.setText(model.getVoterPhone());

        holder.itemView.setOnClickListener(view -> {
            AlertDialog.Builder alertbuilder = new AlertDialog.Builder(c);
            View v = LayoutInflater.from(c).inflate(R.layout.view_edit, null);
            alertbuilder.setView(v);
            AlertDialog alertDialog = alertbuilder.create();
            alertDialog.show();

            EditText edtVoterID = v.findViewById(R.id.edtVoterID);
            EditText edtVoterName = v.findViewById(R.id.edtVoterName);
            EditText edtVoterAddress = v.findViewById(R.id.edtVoterAddress);
            EditText edtVoterPhone = v.findViewById(R.id.edtVoterPhone);
            Button edtbtn = v.findViewById(R.id.edtbtn);
            Button dltbtn = v.findViewById(R.id.dltbtn);

            // Pre-fill data
            edtVoterID.setText(model.getVoterID());
            edtVoterName.setText(model.getVoterName());
            edtVoterAddress.setText(model.getVoterAddress());
            edtVoterPhone.setText(model.getVoterPhone());

            // Edit button
            edtbtn.setOnClickListener(view1 -> {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Voters").child(model.getKey());
                HashMap<String, String> map = new HashMap<>();
                map.put("voterID", edtVoterID.getText().toString());
                map.put("voterName", edtVoterName.getText().toString());
                map.put("voterAddress", edtVoterAddress.getText().toString());
                map.put("voterPhone", edtVoterPhone.getText().toString());
                ref.setValue(map);

                // Update local list
                model.setVoterID(edtVoterID.getText().toString());
                model.setVoterName(edtVoterName.getText().toString());
                model.setVoterAddress(edtVoterAddress.getText().toString());
                model.setVoterPhone(edtVoterPhone.getText().toString());

                notifyItemChanged(position);
                alertDialog.dismiss();
            });

            // Delete button
            dltbtn.setOnClickListener(view12 -> {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Voters").child(model.getKey());
                ref.removeValue();

                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, data.size());
                alertDialog.dismiss();
            });
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, name, address, phone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
        }
    }
}
