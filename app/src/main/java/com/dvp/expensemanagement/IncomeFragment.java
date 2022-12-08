package com.dvp.expensemanagement;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dvp.expensemanagement.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {

    //Firebase Database
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    //RecyclerView
    private RecyclerView recyclerView;

    //Text view 
    private TextView incomeTotalSum;

    //UPDATE Edit text
    private EditText edtAmount,edtNote, edtType;
    private Button btnUpdate,btnDelete;

    //Data item value
    private String type,note;
    private int amount;

    private String post_key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_income, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotalSum = myView.findViewById(R.id.income_txt_result);


        recyclerView = myView.findViewById(R.id.recycler_id_income);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalvalue = 0;
                for (DataSnapshot mysnapshot:dataSnapshot.getChildren()){

                    Data data  = mysnapshot.getValue(Data.class);

                    totalvalue+= data.getAmount();

                    String stTotalValue = String.valueOf(totalvalue);

                    incomeTotalSum.setText(stTotalValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mIncomeDatabase, Data.class)
                        .build();


        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.income_recycler_data, parent, false);

                return new MyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder,int position, @NonNull Data model) {

                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
                holder.setType(model.getType());
                holder.setAmount(model.getAmount());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key = getRef(position).getKey();

                        type = model.getType();
                        note = model.getNote();
                        amount = model.getAmount();

                        updateDataItem();
                    }
                });
            }


        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setType(String type) {
            TextView mType = mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        private void setNote(String note) {
            TextView mNote = mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        private void setDate(String date) {
            TextView mDate = mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }

        private void setAmount(int amount) {
            TextView mAmount = mView.findViewById(R.id.amount_txt_income);
            String stateAmount = String.valueOf(amount);
            mAmount.setText(stateAmount);
        }

    }

    private void updateDataItem(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myView);

        edtAmount = myView.findViewById(R.id.ammount_edt);
        edtNote = myView.findViewById(R.id.note_edt);
        edtType = myView.findViewById(R.id.type_edt);

        edtType.setText(type);
        edtType.setSelection(type.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        btnUpdate = myView.findViewById(R.id.btnEdit);
        btnDelete = myView.findViewById(R.id.btnDelete);

        AlertDialog dialog =  mydialog.create();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = edtType.getText().toString().trim();
                note = edtNote.getText().toString().trim();
                String mdaAmount = String.valueOf(amount);
                mdaAmount = edtAmount.getText().toString().trim();

                int myAmount = Integer.parseInt(mdaAmount);

                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(myAmount,type,note,post_key,mDate);

                mIncomeDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomeDatabase.child(post_key).removeValue();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}