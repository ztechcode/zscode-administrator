package org.zafritech.zscode.administrator.views.fragments.accounts.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.views.fragments.accounts.common.AccountItem;
import org.zafritech.zscode.administrator.views.fragments.accounts.common.AccountsClickInterface;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AccountsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private ArrayList<AccountItem> accountArrayList;
    private AccountsClickInterface onClickInterface;

    public AccountsRecyclerViewAdapter(Context context, ArrayList<AccountItem> accountArrayList, AccountsClickInterface onClickInterface) {

        this.context = context;
        this.accountArrayList = accountArrayList;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_accounts_account, parent,false);

        return new RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        AccountItem account = accountArrayList.get(position);
        RecyclerViewViewHolder viewHolder = (RecyclerViewViewHolder) holder;

        Glide.with(context).load(account.getImgIcon())
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_question)
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(viewHolder.imgView_icon);

        viewHolder.txtView_name.setText(account.getName());
        viewHolder.txtView_email.setText(account.getEmail());
        viewHolder.txtView_lastonline.setText(account.getOnline());
        viewHolder.txtView_joined.setText(account.getJoined());
        viewHolder.txtView_roles.setText(account.getRoles());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                onClickInterface.setClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {

        return accountArrayList.size();
    }

    private class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView_icon;
        TextView txtView_name;
        TextView txtView_email;
        TextView txtView_lastonline;
        TextView txtView_joined;
        TextView txtView_roles;

        public RecyclerViewViewHolder(@NonNull View itemView) {

            super(itemView);

            imgView_icon = itemView.findViewById(R.id.accounts_profile_image);
            txtView_email = itemView.findViewById(R.id.accounts_profile_email);
            txtView_name = itemView.findViewById(R.id.accounts_profile_name);
            txtView_lastonline = itemView.findViewById(R.id.accounts_profile_lastonline);
            txtView_joined = itemView.findViewById(R.id.accounts_profile_joindate);
            txtView_roles = itemView.findViewById(R.id.accounts_profile_roles);

        }
    }
}
