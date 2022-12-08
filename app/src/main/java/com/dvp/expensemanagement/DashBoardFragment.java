package com.dvp.expensemanagement;

import android.app.AlertDialog;
import android.graphics.drawable.Animatable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dvp.expensemanagement.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {


    //Floating button
    
    private FloatingActionButton fab_main_btn,fab_income_btn,fab_expense_btn;

    //Floating Text View

    private TextView fab_income_txt,fab_expense_txt;

    //boolean
    private boolean isOpen = false;

    //Animation
    private Animation FadOpen,FadClose;

    //Dashboard income and expense result
    private TextView totalIncomeResult,totalExpenseResult;

    //Firebase

    private FirebaseAuth mAuth; //current user
    private DatabaseReference mIncomeDatabase;  //Database
    private DatabaseReference mExpenseDatabase;

    private RecyclerView mRecyclerIncome,mRecyclerExpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dash_board, container, false);


        //Firebase get current user
        mAuth = FirebaseAuth.getInstance(); //server running
        FirebaseUser mUser = mAuth.getCurrentUser();        //current user
        //get UID of user
        String uid = mUser.getUid();        //UID
        //Database Integration
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        //Connect to floating button to layout

        fab_main_btn = myview.findViewById(R.id.fb_main_plus);
        fab_income_btn = myview.findViewById(R.id.income_Ft_btn);
        fab_expense_btn = myview.findViewById(R.id.expense_Ft_btn);

        //Connect to floating text to layout
        fab_income_txt = myview.findViewById(R.id.income_ft_text);
        fab_expense_txt = myview.findViewById(R.id.expense_ft_text);

        totalIncomeResult = myview.findViewById(R.id.income_set_result);
        totalExpenseResult = myview.findViewById(R.id.expense_set_result);

        //Recycler connection
        mRecyclerExpense = myview.findViewById(R.id.recycler_expense);
        mRecyclerIncome = myview.findViewById(R.id.recycler_income);

        //Animation connect

        FadOpen = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadClose = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();

                if (isOpen) {

                    fab_income_btn.startAnimation(FadClose);
                    fab_expense_btn.startAnimation(FadClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadClose);
                    fab_expense_txt.startAnimation(FadClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen=false;
                }
                else {
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isOpen=true;
                }

            }
        });

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum =0;
                for (DataSnapshot mysnap:snapshot.getChildren()){
                    Data data = mysnap.getValue(Data.class);
                    totalsum+= data.getAmount();
                    
                    String setResult=String.valueOf(totalsum);

                    totalIncomeResult.setText(setResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum1 =0;
                for (DataSnapshot mysnap:snapshot.getChildren()){
                    Data data = mysnap.getValue(Data.class);
                    totalsum1+= data.getAmount();

                    String setResult=String.valueOf(totalsum1);

                    totalExpenseResult.setText(setResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        layoutManagerIncome.setReverseLayout(true);
        layoutManagerIncome.setStackFromEnd(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);

        return myview;
    }

    private void ftAnimation() {
        if (isOpen) {

            fab_income_btn.startAnimation(FadClose);
            fab_expense_btn.startAnimation(FadClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadClose);
            fab_expense_txt.startAnimation(FadClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen=false;
        }
        else {
            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen=true;
        }
    }

    private void addData() {

        //Fab Button income..
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }

    public void incomeDataInsert() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        EditText edtAmount = myView.findViewById(R.id.ammount_edt);
        EditText edtType = myView.findViewById(R.id.type_edt);
        EditText edtNote = myView.findViewById(R.id.note_edt);

        Button btnSave = myView.findViewById(R.id.btnSave);
        Button btnCancel = myView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = edtType.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)) {
                    edtType.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(amount)) {
                    edtAmount.setError("Required Field..");
                    return;
                }

                int ouramountint = Integer.parseInt(amount);
                if(TextUtils.isEmpty(note)) {
                    edtNote.setError("Required Field..");
                    return;
                }

                String id = mIncomeDatabase.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ouramountint,type,note,id,mDate);

                mIncomeDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Income Data Added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void expenseDataInsert(){

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        final EditText edtAmount = myView.findViewById(R.id.ammount_edt);
        EditText edtType = myView.findViewById(R.id.type_edt);
        EditText edtNote = myView.findViewById(R.id.note_edt);

        Button btnSave = myView.findViewById(R.id.btnSave);
        Button btnCancel = myView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = edtType.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)) {
                    edtType.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(amount)) {
                    edtAmount.setError("Required Field..");
                    return;
                }

                int inamountint = Integer.parseInt(amount);
                if(TextUtils.isEmpty(note)) {
                    edtNote.setError("Required Field..");
                    return;
                }

                String id = mExpenseDatabase.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(inamountint,type,note,id,mDate);

                mExpenseDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Expense Data Added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mIncomeDatabase, Data.class)
                        .build();


        FirebaseRecyclerAdapter<Data, IncomeViewHolder> adapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull IncomeViewHolder holder, int position, @NonNull Data model) {
                holder.setDate(model.getDate());
                holder.setType(model.getType());
                holder.setAmount(model.getAmount());

            }

            @NonNull
            @Override
            public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dashboard_income, parent, false);

                return new IncomeViewHolder(view);
            }

        };

        FirebaseRecyclerOptions<Data> options1 =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mExpenseDatabase, Data.class)
                        .build();


        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> adapter1 = new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Data model) {
                holder.setExpenseDate(model.getDate());
                holder.setExpenseType(model.getType());
                holder.setExpenseAmount(model.getAmount());

            }

            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dashboard_expense, parent, false);

                return new ExpenseViewHolder(view);
            }

        };

        adapter.startListening();
        adapter1.startListening();
        mRecyclerIncome.setAdapter(adapter);

        mRecyclerExpense.setAdapter(adapter1);



    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder {

        View mIncomeView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mIncomeView = itemView;
        }

        private void setType(String type) {
            TextView mType = mIncomeView.findViewById(R.id.type_income_ds);
            mType.setText(type);
        }
        private void setDate(String date) {
            TextView mDate = mIncomeView.findViewById(R.id.date_income_ds);
            mDate.setText(date);
        }

        private void setAmount(int amount) {
            TextView mAmount = mIncomeView.findViewById(R.id.amount_income_ds);
            String stateAmount = String.valueOf(amount);
            mAmount.setText(stateAmount);
        }

    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        View mExpenseView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            mExpenseView = itemView;
        }

        private void setExpenseType(String type) {
            TextView mType = mExpenseView.findViewById(R.id.type_expense_ds);
            mType.setText(type);
        }
        private void setExpenseDate(String date) {
            TextView mDate = mExpenseView.findViewById(R.id.date_expense_ds);
            mDate.setText(date);
        }

        private void setExpenseAmount(int amount) {
            TextView mAmount = mExpenseView.findViewById(R.id.amount_expense_ds);
            String stateAmount = String.valueOf(amount);
            mAmount.setText(stateAmount);
        }

    }
}