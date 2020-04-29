package com.fikkarnot.instagramclone;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener {
    ListView usersList;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter,afterAdapter;
    ProgressBar progressBar;
    SearchView searchView;



    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users_tab, container, false);

        usersList = view.findViewById(R.id.usersList);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchview);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
        afterAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
        usersList.setOnItemClickListener(UsersTab.this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (arrayList.contains(newText)){

                    afterAdapter.getFilter().filter(newText);
                    usersList.setAdapter(afterAdapter);
                }else{
                    usersList.setAdapter(arrayAdapter);

                }


                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                usersList.setAdapter(arrayAdapter);
                return false;
            }
        });


        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e==null){
                    if (objects.size()>0){

                        for (ParseUser user:objects){

                            arrayList.add(user.getUsername());

                        }
                        usersList.setAdapter(arrayAdapter);
                        usersList.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(getContext(),UsersPosts.class);
        intent.putExtra("username",arrayList.get(position));
        startActivity(intent);

    }
}
