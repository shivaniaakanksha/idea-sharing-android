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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        GET_URL += user.getEmail();

        FloatingActionButton btn = container.findViewById(R.id.floating_addBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddIdeaActivity.class));
            }
        });

        new GetIdeas().execute();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = container.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setVisibility(View.INVISIBLE);

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    public void sendBundle() {
        adapter = new IdeasAdapter(getContext(), titleList, descList, techList);
        recyclerView.setAdapter(adapter);
    }

    class GetIdeas extends AsyncTask<Void, Void, Void> {
        GetIdeas() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading Ideas");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            jsonStr = sh.makeServiceCall(GET_URL);
            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);

                    jsonArray = jsonObj.getJSONArray("ideas");
                    len = jsonArray.length();

                    for (int i = 0; i < len; i++) {
                        JSONObject attendeeObj = jsonArray.getJSONObject(i);
                        titleList.add(attendeeObj.getString("tile"));
                        descList.add(attendeeObj.getString("desc"));
                        techList.add(attendeeObj.getString("technology"));
                    }
                    return null;
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    };

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Error Loading data!!\nTry Again.", Toast.LENGTH_LONG).show();
                    }
                };
            }
            return null;
        }

    }

//    private void runOnUiThread(Runnable runnable) {
//    }
}
