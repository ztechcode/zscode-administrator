package org.zafritech.zscode.administrator.views.fragments.services.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.views.fragments.services.common.ServiceItem;
import org.zafritech.zscode.administrator.views.fragments.services.common.ServicesClickInterface;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private ArrayList<ServiceItem> serviceArrayList;
    private ServicesClickInterface onClickInterface;

    public ServicesRecyclerViewAdapter(Context context, ArrayList<ServiceItem> serviceArrayList, ServicesClickInterface onClickInterface) {

        this.context = context;
        this.serviceArrayList = serviceArrayList;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_services_service, parent,false);

        return new RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ServiceItem service = serviceArrayList.get(position);
        RecyclerViewViewHolder viewHolder = (RecyclerViewViewHolder) holder;

        viewHolder.txtView_name.setText(service.getName());
        viewHolder.txtView_status.setText(service.getStatus());
        viewHolder.txtView_url.setText(service.getUrl());
        viewHolder.txtView_uptime.setText(service.getUptime());
        viewHolder.txtView_cpu.setText(service.getCpu());

        if (!service.getStatus().equals("RUNNING")) {

            viewHolder.imgView_icon.setImageResource(R.drawable.ic_server_network_off);
            viewHolder.imgView_icon.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), android.graphics.PorterDuff.Mode.SRC_IN);
            viewHolder.imgView_uptime.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), android.graphics.PorterDuff.Mode.SRC_IN);
            viewHolder.txtView_status.setBackground(context.getDrawable(R.color.colorRed));
            viewHolder.txtView_uptime.setText("ServiceItem not available");
            viewHolder.txtView_cpu.setText("");

        } else {

            viewHolder.imgView_icon.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                onClickInterface.setClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {

        return serviceArrayList.size();
    }

    private class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView_icon;
        ImageView imgView_uptime;
        TextView txtView_name;
        TextView txtView_status;
        TextView txtView_url;
        TextView txtView_uptime;
        TextView txtView_cpu;

        public RecyclerViewViewHolder(@NonNull View itemView) {

            super(itemView);

            imgView_icon = itemView.findViewById(R.id.server_image);
            imgView_uptime = itemView.findViewById(R.id.uptime_clock_image);
            txtView_name = itemView.findViewById(R.id.service_name_text);
            txtView_status = itemView.findViewById(R.id.service_status_text);
            txtView_url = itemView.findViewById(R.id.service_url_text);
            txtView_uptime = itemView.findViewById(R.id.service_uptime_text);
            txtView_cpu = itemView.findViewById(R.id.server_cpu_text);

        }
    }

}
