package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";


    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);


        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);


        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        // The Spinner reports back on two events
        // 1. If nothing was selected
        // 2. If an item was selected
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Log.d("Bitcoin", "" + parentView.getItemAtPosition(position)); //

                String chosenCurrency = (String) parentView.getItemAtPosition(position);
                String toBeParsed = "BTC" + chosenCurrency;

                // Call "letsDoSomeNetWorking()" in onItemSelected() and pass in the final URL that includes the user's chosenCurrency (toBeParsed)
                letsDoSomeNetworking(BASE_URL + toBeParsed);
                Log.d("Bitcoin", "" + BASE_URL + toBeParsed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                Log.d("Bitcoin", "Nothing selected");
            }

        });


    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {


        AsyncHttpClient client = new AsyncHttpClient();


        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Bitcoin", "JSON: " + response.toString());

                String mPrice = null;
                try { // Try to...
                    mPrice = response.getString("ask");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                updateUI(mPrice);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.d("Bitcoin", "t Fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("ERROR", e.toString());
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show(); // Create Toast message
            }
        });
    }

    private void updateUI(String price) {
        Log.d("Clima", "updateUI() callback received");

        mPriceTextView.setText(price);

    }


}