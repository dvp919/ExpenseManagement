package com.dvp.expensemanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardFragment extends Fragment {

    private FloatingActionButton fab_main;
    private FloatingActionButton fab_income;
    private FloatingActionButton fab_expense;

    private TextView fab_income_text;
    private TextView fab_expense_text;

    private boolean isOpen=false;

    private Animation FadeOpen,FadeClose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dashboard, container, false);

        fab_main = myview.findViewById(R.id.fb_main_plus);
        fab_income = myview.findViewById(R.id.income_fb);
        fab_expense = myview.findViewById(R.id.expense_fb);

        fab_income_text = myview.findViewById(R.id.income_fb_text);
        fab_expense_text= myview.findViewById(R.id.expense_fb_text);

        FadeOpen = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isOpen){
                    fab_income.startAnimation(FadeClose);
                    fab_expense.startAnimation(FadeClose);
                    fab_income.setClickable(false);
                    fab_expense.setClickable(false);

                    fab_income_text.startAnimation(FadeClose);
                    fab_expense_text.startAnimation(FadeClose);
                    fab_income_text.setClickable(false);
                    fab_expense_text.setClickable(false);
                    isOpen = false;
                }else{
                    fab_income.startAnimation(FadeOpen);
                    fab_expense.startAnimation(FadeOpen);
                    fab_income.setClickable(true);
                    fab_expense.setClickable(true);

                    fab_income_text.startAnimation(FadeOpen);
                    fab_expense_text.startAnimation(FadeOpen);
                    fab_income_text.setClickable(true);
                    fab_expense_text.setClickable(true);
                    isOpen = true;
                }
            }

        });

        return myview;
    }
}