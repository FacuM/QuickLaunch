package me.facuarmo.quicklaunch;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView appDrawerRecyclerView = findViewById(R.id.app_drawer);
        appDrawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new ApplicationListLoader(this).execute();
    }

    @Override
    public void onBackPressed() {}

    public static class ApplicationListLoader extends AsyncTask<Void, Void, Void> {
        WeakReference<MainActivity> mainActivityReference;

        ApplicationListLoader(MainActivity mainActivity) {
            mainActivityReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mainActivityReference != null) {
                final MainActivity mainActivity = mainActivityReference.get();

                final List<ApplicationInformation> applicationInformationList = new ArrayList<>();

                PackageManager packageManager = mainActivity.getPackageManager();
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                List<ResolveInfo> resolveInfoList =
                        packageManager.queryIntentActivities(intent, 0);

                Collections.sort(resolveInfoList, new ResolveInfo.DisplayNameComparator(packageManager));

                for (ResolveInfo resolveInfo : resolveInfoList) {
                    ApplicationInformation applicationInformation = new ApplicationInformation(
                            resolveInfo.loadLabel(packageManager).toString(),
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.loadIcon(packageManager)
                    );

                    applicationInformationList.add(applicationInformation);
                }

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView appDrawerRecyclerView =
                                mainActivity.findViewById(R.id.app_drawer);

                        ProgressBar loadingProgressBar =
                                mainActivity.findViewById(R.id.loading_progress);

                        loadingProgressBar.setVisibility(View.GONE);

                        ApplicationDrawerAdapter applicationDrawerAdapter =
                                new ApplicationDrawerAdapter(
                                        mainActivity,
                                        applicationInformationList);

                        appDrawerRecyclerView.setAdapter(applicationDrawerAdapter);
                        appDrawerRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }

            return null;
        }
    }
}
