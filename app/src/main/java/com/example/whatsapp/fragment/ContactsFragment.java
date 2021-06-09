package com.example.whatsapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp.R;
import com.example.whatsapp.adapter.ContactsAdapter;
import com.example.whatsapp.config.ConfigFirebase;
import com.example.whatsapp.helper.UserFirebase;
import com.example.whatsapp.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    private RecyclerView mRecyclerViewListContacts;
    private ContactsAdapter mAdapter;
    private ArrayList<User> mListContacts = new ArrayList<>();
    private DatabaseReference mUserRefFirebase;
    private ValueEventListener mValueEventListenerContacts;
    private FirebaseUser mCurrentlyUser;
    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;*/

    public ContactsFragment() {
        // Required empty public constructor
    }
/*
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    /*// TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contacts, container, false);

        //Configurações iniciais
        mRecyclerViewListContacts = view.findViewById(R.id.recyclerViewContacts);
        mUserRefFirebase = ConfigFirebase.getFirebaseDatabase().child("user");
        mCurrentlyUser = UserFirebase.getUser();
        //Configurar adapter
        mAdapter = new ContactsAdapter(mListContacts, getActivity());
        //Configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewListContacts.setLayoutManager(layoutManager);
        mRecyclerViewListContacts.setHasFixedSize(true);
        mRecyclerViewListContacts.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recoverContacts();
    }

    @Override
    public void onStop() {
        super.onStop();
        mUserRefFirebase.removeEventListener(mValueEventListenerContacts);
    }

    public void recoverContacts(){
        mValueEventListenerContacts = mUserRefFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){
                    User user = data.getValue(User.class);
                    //logica para não exibir usuario atual na listagem de usuarios disponiveis
                    String emailCurrentlyUser = mCurrentlyUser.getEmail();
                    if(!emailCurrentlyUser.equals(user.getEmail())){
                        mListContacts.add(user);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}