package com.harpooner.start.makeitstick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Dell I5 on 19.04.2016.
 */
public class SearchActivity extends Activity implements View.OnClickListener {

    private EditText etSearch;
    private Button btnSearch;
    private static final String KEY_SEARCH_DATA = "keepinmind.search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.requestFocus();

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {
            if (etSearch.getText().length() == 0) {
                Toast.makeText(this, "INPUT TEXT TO SEARCH", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = getIntent();
                intent.putExtra(KEY_SEARCH_DATA, etSearch.getText().toString().toLowerCase());
                MainActivity.setIsSearchPerformed(true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    protected static String getKeySearchData() {
        return KEY_SEARCH_DATA;
    }
}
