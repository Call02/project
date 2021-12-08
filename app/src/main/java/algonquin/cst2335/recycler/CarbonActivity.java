package algonquin.cst2335.recycler;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class CarbonActivity extends AppCompatActivity {
    public ArrayList<User> usersList;
    private RecyclerView recyclerView;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         * Declare variables
         */
        setContentView(R.layout.carbon_main);
        recyclerView = findViewById(R.id.recyclerView);
        EditText distanceText = findViewById(R.id.distanceText);
        Button distanceBtn = findViewById(R.id.goBtn);
        TextView searchBtn = findViewById(R.id.searchButton);
        RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> mAdapter = new RecyclerAdapter(usersList);
        recyclerView.setAdapter(mAdapter);
        usersList = new ArrayList<>();


        setAdapter();

        /**
         * Text change for searching recycler view
         */
        EditText editText = findViewById(R.id.makeText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }

            /**
             * filter recycler view
             * @param text
             */
            private void filter(String text) {
                ArrayList<User> filteredList = new ArrayList<>();
            searchBtn.setOnClickListener(click ->{
                for (User item : usersList){
                    if(item.getID().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(item);
                        recyclerView.getRecycledViewPool().clear();
                    }
                    if(item.getType().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(item);
                        recyclerView.getRecycledViewPool().clear();
                    }
                    if(item.getAttributes().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(item);
                        recyclerView.getRecycledViewPool().clear();



                    }

                }


                ((RecyclerAdapter) mAdapter).filterList(filteredList);

            });


            }

        });


        /**
         * Start new thread
         */
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {
            /**
             * Database opener
             */
            MyOpenHelper opener = new MyOpenHelper(this);
            SQLiteDatabase db = opener.getWritableDatabase();




            /**
             * Shared preferences to store users input
             */
            SharedPreferences prefs = getSharedPreferences("Distance", Context.MODE_PRIVATE);
            String word =  prefs.getString("word", "");
            distanceText.setText(word);




            /**
             * Show alert dialog if distance text field is empty
             */
            distanceBtn.setOnClickListener(view -> {
                        String Text = distanceText.getText().toString();
                        if (TextUtils.isEmpty(Text)) {
                            Snackbar.make(view, "please enter a distance to drive", Snackbar.LENGTH_SHORT).show();
                        }

                searchBtn.setOnClickListener(view1 -> {
                    if (TextUtils.isEmpty(Text)) {
                        Snackbar.make(view, "please enter a vehicle make", Snackbar.LENGTH_SHORT).show();
                    }
                });



                /**
                 * Saves distance entered from last time of use
                 */
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("word", distanceText.getText().toString());
                edit.apply();

            });


            /**
             * Connects to the API
             */
            try {
                URL url = new URL("https://www.carboninterface.com/api/v1/vehicle_makes");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("Authorization", "Bearer YvfFiCJXmBvtAPAm2h9zQ");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.getResponseCode();


                runOnUiThread(() -> {
                    AlertDialog dlg = new AlertDialog.Builder(CarbonActivity.this)
                            .setTitle("Loading...")
                            .setMessage("Getting data from the server")
                            .setView(new ProgressBar(CarbonActivity.this))
                            .create();
                    dlg.show();
                });


                /**
                 * Input stream
                 */
                InputStream in = new BufferedInputStream(connection.getInputStream());
                String text = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));




                /**
                 * Loops through the data in the JSON array
                 */
                for (int i = 0; i < 138; i++) {
                    JSONArray theDocument = new JSONArray(text);

                    JSONObject dataArray = theDocument.getJSONObject(i);

                    JSONObject position0 = dataArray.getJSONObject("data");
                    String id = position0.getString("id");
                    String type = position0.getString("type");
                    String attributes = position0.getString("attributes");


                    /**
                     * Displays vehicle information
                     */
                    runOnUiThread(() -> usersList.add(new User("Id: " + id, "Type: " + type, "Attributes: " + attributes)));



                    /**
                     * load API data into the database
                     */
                    ContentValues newRow = new ContentValues();
                    newRow.put(MyOpenHelper.col_type, position0.getString("type"));
                    newRow.put(MyOpenHelper.col_attributes, position0.getString("attributes"));
                    newRow.put(MyOpenHelper.col_id, position0.getString("id"));
                    db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_id, newRow);
                    }

                }




            /**
             * Catches exceptions
             */
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }



        });
    }


    /**
     * sets adapter and layout manager
     */
    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(usersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Loads alert dialog for help window
     * @param view
     */
    public void showHelp(View view) {
        AlertDialog dlg = new AlertDialog.Builder(CarbonActivity.this)
                .setTitle("How to use Carbon Interface")
                .setMessage("Enter a distance to drive, select a vehicle make an model, and view the carbon emissions that it will produce.")
                .setPositiveButton("Exit", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        dlg.show();
    }




}



