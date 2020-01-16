package me.facuarmo.quicklaunch;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ApplicationDrawerAdapter extends RecyclerView.Adapter<ApplicationDrawerAdapter.ViewHolder> {
    private List<ApplicationInformation> mApplicationInformationList;
    private LayoutInflater mLayoutInflater;

    ApplicationDrawerAdapter(Context context, List<ApplicationInformation> applicationInformationList) {
        mApplicationInformationList = applicationInformationList;

        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView applicationName;
        ImageView applicationIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            applicationName = itemView.findViewById(R.id.app_name);
            applicationIcon = itemView.findViewById(R.id.app_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();

            Intent launchApplicationIntent =
                    context.
                    getPackageManager().
                    getLaunchIntentForPackage(
                            mApplicationInformationList.get(getAdapterPosition()).packageName);

            context.startActivity(launchApplicationIntent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String applicationName = mApplicationInformationList.get(position).name;
        // String applicationPackage = applicationInformationList.get(position).packageName;
        Drawable applicationIcon = mApplicationInformationList.get(position).icon;

        holder.applicationName.setText(applicationName);
        holder.applicationIcon.setImageDrawable(applicationIcon);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.app_item, parent, false);
        view.getBackground().setAlpha(80);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return mApplicationInformationList.size();
    }
}
