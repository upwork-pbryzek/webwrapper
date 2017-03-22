package android.ctm.com.ctm.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.ctm.com.ctm.CTMApp;
import android.ctm.com.ctm.R;
import android.ctm.com.ctm.adapters.SearchAdapter;
import android.ctm.com.ctm.data.SearchObj;
import android.ctm.com.ctm.fragments.HomeFragment;
import android.ctm.com.ctm.fragments.SettingsFragment;
import android.ctm.com.ctm.utils.CTMPrefs;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener, Listener<JSONObject>, ErrorListener{

    private static final String LOG_TAG = MainActivity.class.getName();

    private SearchAdapter searchAdapter;

    private Request<?> mInFlightRequest;

    @Override
    public void onResponse(JSONObject responseObj) {
        Log.e(LOG_TAG, "response = " + responseObj.toString());
        /*
            {
                "isStopped":false,
                "isStopping":false,
                "success":true,
                "customers":[{"code":"demo","name":"Demo, Inc.","description":""}],
                "error":false,"
                securityViolations":[]
            }
        */
        mInFlightRequest = null;
        searchAdapter.clear();

        try {
            if(responseObj.has("error")) {
                boolean error = responseObj.getBoolean("error");
                if(error) {
                    String error_message = "";
                    if (responseObj.has("error_message")) {
                        error_message = responseObj.getString("error_message");
                        Log.e(LOG_TAG, "handleSearchResponse error_message = " + error_message);
                    }
                    return;
                }
            }
            JSONArray customers = responseObj.getJSONArray("customers");

            for (int i = 0; i < customers.length(); i++) {
                JSONObject customer = customers.getJSONObject(i);
                String code = customer.getString("code");
                String name = customer.getString("name");
                String description = customer.getString("description");

                SearchObj searchObj = new SearchObj(code, name, description);
                searchAdapter.addRow(searchObj);
                searchAdapter.notifyDataSetChanged();
            }

        } catch(Exception e){
            Log.e(LOG_TAG, "handleSearchResponse: ", e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mInFlightRequest = null;
        String errMsg = error.getMessage();
        showNetworkAlert(errMsg);
        Log.e(LOG_TAG, "error = " + errMsg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupPage();

        searchAdapter = new SearchAdapter();

        String keyPref = CTMPrefs.getKeyUrlPref();
        if(keyPref == null) {
            launchInputDialog();
        } else {
            launchHomeFragment();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri){}

    private enum ALERT{
        INTERNET,
        URL_ERROR,
        NETWORK
    }

    public void showNetworkAlert(String networkErr) {
        showBasicAlert("Error", networkErr, ALERT.NETWORK);
    }

    public void showErrorAlert(String articleUrl) {
        String message = "Url=" + articleUrl + " could not be reached. Please check the url and try again.";
        showBasicAlert("Error", message, ALERT.URL_ERROR);
    }

    public void showNoInternetAlert() {
        showBasicAlert("Alert", "Alert message to be shown", ALERT.INTERNET);
    }

    public void showBasicAlert(String title, String message, final ALERT type) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(type.ordinal() == ALERT.URL_ERROR.ordinal()) {
                    launchInputDialog();
                }
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int curLen = s.length();
            if(curLen > 0 && curLen % 3 == 0) {
                String str = s.toString();
                mInFlightRequest = CTMApp.getInstance().getmApi().search(str, MainActivity.this, MainActivity.this);
            }
        }

        public void afterTextChanged(Editable s) {}
    };

    private void launchInputDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_dialog);

        final EditText userInput = (EditText) dialog.findViewById(R.id.userInput);
        userInput.addTextChangedListener(mTextEditorWatcher);

        String keyPref = CTMPrefs.getKeyUrlPref();
        if(keyPref != null) {
            userInput.setText(keyPref);
        }

        final TextView inputHelp = (TextView) dialog.findViewById(R.id.inputHelp);
        String clickHereStr = getString(R.string.click_here);
        String inputHelpStr = getString(R.string.input_help);
        String display = inputHelpStr + " " + clickHereStr;

        Spannable WordtoSpan = new SpannableString(display);
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), inputHelpStr.length(), display.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        inputHelp.setText(WordtoSpan);

        inputHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment frag = (HomeFragment)
                        getSupportFragmentManager().findFragmentById(R.id.flContent);
                if (frag != null && frag.isVisible()) {
                    // add your code here
                    frag.launchHelpPage();
                }
                dialog.dismiss();
            }
        });

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String userResponse = userInput.getText().toString();
                if (userResponse != null) {
                    CTMPrefs.saveKeyUrlPref(userResponse);
                }

                dialog.dismiss();
                launchHomeFragment();
            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom dialog
        dialogButtonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ListView searchListView = (ListView) dialog.findViewById(R.id.searchListView);
        searchAdapter.clear();

        searchListView.setAdapter(searchAdapter);

        searchListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchObj searchObj = (SearchObj) searchAdapter.getItem(position);
                userInput.setText(searchObj.code);
            }
        });

        dialog.show();
    }

    private void launchHomeFragment() {
        try {
            Fragment fragment = (Fragment) HomeFragment.newInstance("", "");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            Log.e(LOG_TAG, "launchTranslateFragment: ", e);
        }
    }

    private void setActionBar() {
        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("CTM");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        //abar.setIcon(Color.TRANSPARENT);
        abar.setHomeButtonEnabled(true);
    }

    private void setupPage() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.clear_hamburger);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchInputDialog();
            }
        });

        //Customize the ActionBar
        setActionBar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dialog) {
            launchInputDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            launchHomeFragment();
        } else if (id == R.id.nav_settings) {
            launchInputDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
