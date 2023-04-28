package com.prashant.backgroundcallername.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prashant.backgroundcallername.Adapters.SmsAdapter;
import com.prashant.backgroundcallername.Database.DatabaseHelper;
import com.prashant.backgroundcallername.Database.SMS_Database;
import com.prashant.backgroundcallername.Models.SMS_Model;
import com.prashant.backgroundcallername.R;

import java.util.ArrayList;


public class SMSFragment extends Fragment {


    SmsAdapter smsAdapter;
    ArrayList<SMS_Model>sList = new ArrayList<>();
    SMS_Database smsdb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_s_m_s, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.smsRecyclerView);


        smsdb = new SMS_Database(requireContext());

        sList=smsdb.SMSDetail();
        smsAdapter=new SmsAdapter(sList, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(smsAdapter);



        return view;
    }
}