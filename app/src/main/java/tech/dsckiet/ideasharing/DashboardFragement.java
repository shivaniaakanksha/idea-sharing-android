package tech.dsckiet.ideasharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static tech.dsckiet.ideasharing.Config.BASE_URL;
import static tech.dsckiet.ideasharing.Config.SHOW_IDEAS_API;

public class DashboardFragement extends Fragment {

    private FirebaseAuth mAuth;
    private String TAG = DashboardFragement.class.getSimpleName();
    private ProgressDialog progressDialog;

    private static String GET_URL = BASE_URL + SHOW_IDEAS_API;

    RecyclerView recyclerView;
    JSONObject jsonObj;
    JSONArray jsonArray;
    String jsonStr;
    int len;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> descList = new ArrayList<>();
    ArrayList<String> techList = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        GET_URL += user.getEmail();

        FloatingActionButton btn = view.findViewById(R.id.floating_addBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddIdeaActivity.class));
            }
        });

        //new GetIdeas().execute();


        return view;
    }

    public void sendBundle() {
        adapter = new IdeasAdapter(getContext(), titleList, descList, techList);
        recyclerView.setAdapter(adapter);
    }

}
