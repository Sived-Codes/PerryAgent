package com.prashant.backgroundcallername.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prashant.backgroundcallername.Adapters.CallAdpater;
import com.prashant.backgroundcallername.Database.DatabaseHelper;
import com.prashant.backgroundcallername.Models.Call_Model;
import com.prashant.backgroundcallername.R;

import java.util.ArrayList;

public class CallFragment extends Fragment {
    CallAdpater calladpater;
    RecyclerView callrecyclerview;
    ArrayList<Call_Model>List = new ArrayList<>();
    DatabaseHelper db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_call, container, false);

        db = new DatabaseHelper(requireContext());

        callrecyclerview=view.findViewById(R.id.Call_Recyclerview);
        List=db.CallDetail();
        calladpater=new CallAdpater(List, requireContext());
        callrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        callrecyclerview.setAdapter(calladpater);


        return view;
    }
}