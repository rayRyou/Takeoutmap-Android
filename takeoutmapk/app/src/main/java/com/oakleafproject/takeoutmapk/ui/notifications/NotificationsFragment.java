package com.oakleafproject.takeoutmapk.ui.notifications;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.oakleafproject.takeoutmapk.DetailActivity;
import com.oakleafproject.takeoutmapk.R;
import com.oakleafproject.takeoutmapk.WebActivity;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Button storageBtn = root.findViewById(R.id.storage_btn);
        Button webappBtn = root.findViewById(R.id.webapp_btn);
        Button contactBtn = root.findViewById(R.id.contact_btn);

        storageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(getContext(), WebActivity.class);
                detail.putExtra("link-url", getString(R.string.about_storage_form_url));
                startActivity(detail);
            }
        });

        webappBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(getString(R.string.about_webapp_url));
                Intent detail = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(detail);

            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(getContext(), WebActivity.class);
                detail.putExtra("link-url", getString(R.string.about_contact_url));
                startActivity(detail);
            }
        });
        return root;
    }
}
