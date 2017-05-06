package com.abhi.logsbrologs.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abhi.logsbrologs.Constants;
import com.abhi.logsbrologs.R;
import com.abhi.logsbrologs.adapter.LogsItem;
import com.lapism.searchview.SearchView;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.commons.BuildConfig;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by parth on 6/4/17.
 */

public class LogcatActivity extends AppCompatActivity {

    private static final String TAG = "LogcatActivity";
    private Shell.Interactive rootSession;
    private RecyclerView recyclerView;
    private FastItemAdapter<LogsItem> fastItemAdapter;
    private int count = 0;
    private boolean isScrollStateIdle = true;
    private LinearLayoutManager mLayoutManager;
    private Drawer drawer;
    private AccountHeader header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcat);
        Log.d(TAG, "onCreate");
        initViews();
        rootSession("logcat");
        fastItemAdapter.withSavedInstanceState(savedInstanceState);
        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .withProfileImagesVisible(false)
                .addProfiles(
                        new ProfileDrawerItem().withName("LogsBroLogs").withEmail(BuildConfig.VERSION_NAME)
                )
                .withCurrentProfileHiddenInList(true)
                .build();
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Logcat").withIdentifier(0),
                        new PrimaryDrawerItem().withName("Denials").withIdentifier(1)
                )
                .withCloseOnClick(true)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 0) {
                            fastItemAdapter.clear();
                            rootSession("logcat");
                        } else if (drawerItem.getIdentifier() == 1) {
                            fastItemAdapter.clear();
                            rootSession("dmesg | grep \"avc: denied\"");
                        }
                        return false;
                    }
                })
                .build();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.verbose:
                rootSession("logcat");
                break;
            case R.id.debug:
                rootSession("logcat *:D");
                break;
            case R.id.info:
                rootSession("logcat *:I");
                break;
            case R.id.warning:
                rootSession("logcat *:W");
                break;
            case R.id.error:
                rootSession("logcat *:E");
                break;
            case R.id.fatal:
                rootSession("logcat *:F");
                break;
            case R.id.clear:
                fastItemAdapter.clear();
                break;
        }
        return true;
    }

    private void initViews() {
        Log.d(TAG, "initViews");
        rootSession = new Shell.Builder().useSU().open();
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.withPositionBasedStateManagement(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(fastItemAdapter);
        recyclerView.setItemAnimator(null);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isScrollStateIdle = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                isScrollStateIdle = pastVisibleItems + visibleItemCount >= totalItemCount;
            }
        });
        fastItemAdapter.withFilterPredicate(new IItemAdapter.Predicate<LogsItem>() {
            @Override
            public boolean filter(LogsItem item, CharSequence constraint) {
                if (item.getLog() != null) {
                    return !item.getLog().toLowerCase().contains(constraint.toString().toLowerCase());
                } else {
                    return true;
                }
            }
        });
        // fastItemAdapter.getItemAdapter().withItemFilterListener(this);
        searchView.setHint("Search");
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.close(true);
                fastItemAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fastItemAdapter.filter(newText);
                return true;

            }
        });
        searchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                drawer.openDrawer();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = fastItemAdapter.saveInstanceState(outState);
        outState = drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void rootSession(String logType) {
        if (rootSession != null) {
            if (rootSession.isRunning()) {
                rootSession.kill();
            }
        }
        rootSession = new Shell.Builder().useSU().open();
        fastItemAdapter.clear();
        logsBro(logType);
    }

    private void logsBro(final String logType) {
        fastItemAdapter.clear();
        rootSession.addCommand(new String[]{logType}, 0, new Shell.OnCommandLineListener() {
            @Override
            public void onCommandResult(int commandCode, int exitCode) {
                Log.d(TAG, "onCommandResult: " + commandCode);
            }

            @Override
            public void onLine(String line) {
                if (count++ > 10000) {
                    count = 0;
                    fastItemAdapter.clear();
                }
                if (!logType.contains("denied")) {
                    String time = null;
                    String log = null;
                    String loglevelStr = null;
                    line.trim();
                    List<String> templist = new ArrayList<String>();
                    Pattern pattern = Pattern.compile("(\\S+)");
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        templist.add(matcher.group());
                    }
                    if (templist.size() > 4) {
                        time = templist.get(1);
                        loglevelStr = templist.get(4);
                        int logIndex = line.indexOf(loglevelStr);
                        log = line.substring(logIndex > -1 ? logIndex + 2 : 0);
                    }

                    Constants.LogLevel loglevel;
                    if ("I".equals(loglevelStr))
                        loglevel = Constants.LogLevel.LOGLEVEL_I;
                    else if ("V".equals(loglevelStr))
                        loglevel = Constants.LogLevel.LOGLEVEL_V;
                    else if ("W".equals(loglevelStr))
                        loglevel = Constants.LogLevel.LOGLEVEL_W;
                    else if ("D".equals(loglevelStr))
                        loglevel = Constants.LogLevel.LOGLEVEL_D;
                    else if ("E".equals(loglevelStr))
                        loglevel = Constants.LogLevel.LOGLEVEL_E;
                    else if ("F".equals(loglevelStr))
                        loglevel = Constants.LogLevel.LOGLEVEL_F;
                    else
                        loglevel = Constants.LogLevel.LOGLEVEL_UNDEFINED;
                    ((ItemAdapter.ItemFilter) fastItemAdapter.getItemFilter()).add(new LogsItem(log, time, loglevel));
                } else {
                    ((ItemAdapter.ItemFilter) fastItemAdapter.getItemFilter()).add(new LogsItem(line, null, Constants.LogLevel.LOGLEVEL_DENIALS));
                }
                if (isScrollStateIdle)
                    recyclerView.scrollToPosition(fastItemAdapter.getItemCount() - 1);
            }
        });
    }
}