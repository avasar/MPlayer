package com.weavedin.mplayer.ui.search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weavedin.mplayer.R;
import com.weavedin.mplayer.database.DBManager;
import com.weavedin.mplayer.models.Songs;
import com.weavedin.mplayer.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchViewInterface {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.actv_search)
    AutoCompleteTextView actv_search;
    @BindView(R.id.pbSearch)
    ProgressBar pbSearch;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tv_searchCount)
    TextView tv_searchCount;
    @BindView(R.id.lnrIndicator)
    LinearLayout lnrIndicator;
    @BindView(R.id.tv_dummy_search)
    TextView tv_dummy_search;
    private int dotscount, pagerHeight;
    private ImageView[] dots;
    private DBManager dbManager;
    SearchPresenter searchPresenter;
    SearchFragmentPageAdapter searchFragmentPageAdapter;
    private String TAG = "SearchActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setupViews();
    }

    private void setupViews() {
        dbManager = new DBManager(this);
        dbManager.open();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String[] terms = dbManager.getAllSearchTerms(s.toString());
                if (terms != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchActivity.this,
                            android.R.layout.simple_dropdown_item_1line, terms);
                    actv_search.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actv_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    if (i == position) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rectangle_4));
                    } else {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rectangle_4_copy));

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.post(new Runnable() {
            @Override
            public void run() {
                pagerHeight = pager.getHeight();
            }
        });

    }

    private void performSearch() {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(actv_search.getWindowToken(), 0);
        if (actv_search.getText().toString().length() > 0) {
            dbManager.insert(actv_search.getText().toString());
        }
        getSongs();

    }

    private void getSongs() {
        searchPresenter = new SearchPresenter(this, actv_search.getText().toString());
        pager.setAdapter(null);
        lnrIndicator.removeAllViews();
        tv_searchCount.setText("");
        showProgressBar();
        searchPresenter.getSongs();

    }

    @Override
    public void showProgressBar() {
        pbSearch.setVisibility(View.VISIBLE);
        tv_dummy_search.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        pbSearch.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(SearchActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSongs(Songs songs) {
        if (songs != null) {
            float pagerDp = Utils.convertPxToDp(SearchActivity.this, pager.getHeight());
            float rowDp = Utils.convertPxToDp(SearchActivity.this, getResources().getDimension(R.dimen.search_row_height) +
                    getResources().getDimension(R.dimen.search_row_margin));

            int rowCount = (int) (pagerDp / rowDp);
            int fragmentCount = songs.getResultCount() / rowCount;
            if(songs.getResultCount() % rowCount != 0){
                fragmentCount+=1;
            }
            searchFragmentPageAdapter = new SearchFragmentPageAdapter(SearchActivity.this, getSupportFragmentManager(), songs.getResults(), songs.getResultCount(), fragmentCount,rowCount);
            pager.setAdapter(searchFragmentPageAdapter);
            pager.getAdapter().notifyDataSetChanged();
            tv_searchCount.setText("All Songs - " + String.valueOf(songs.getResultCount()));
            dotscount = searchFragmentPageAdapter.getCount();
            dots = new ImageView[dotscount];
            for (int i = 0; i < dotscount; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rectangle_4_copy));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                lnrIndicator.addView(dots[i], params);

            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rectangle_4));


        } else {
            tv_dummy_search.setVisibility(View.VISIBLE);
            tv_dummy_search.setText("No Songs");
        }
    }

    @Override
    public void displayError(String s) {
        showToast(s);
    }
}
