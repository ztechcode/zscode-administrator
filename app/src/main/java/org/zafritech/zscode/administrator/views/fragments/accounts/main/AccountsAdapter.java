package org.zafritech.zscode.administrator.views.fragments.accounts.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.accounts.models.Account;
import org.zafritech.zscode.administrator.core.api.auth.AuthHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>  {

    private AuthHelper auth;
    private Context context;
    private ArrayList<Account> accountList;

    public AccountsAdapter(Context context, ArrayList<Account> accountList) {

        this.auth = new AuthHelper(context);
        this.context = context;
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accounts_account, parent,false);

        return new AccountsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder holder, int position) {

        Account account = accountList.get(position);

        GlideUrl photoUrl = new GlideUrl(account.getPhoto(), new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + auth.getTokenKey())
                .build());

        Glide.with(context).load(photoUrl)
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_question)
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imgView_icon);

        holder.txtView_name.setText(account.getFirstName() + " " + account.getLastName());
        holder.txtView_email.setText(account.getEmail());
        // TODO: To be implemented later - must implement user sessions on server
//        holder.txtView_lastonline.setText(account.getOnline());
        holder.txtView_joined.setText("Registered: " + formatDate(account.getCreated()));
        // TODO: To be implemented later - must retrieve Roles from Auth Services
//        holder.txtView_roles.setText(account.getRoles());
    }

    @Override
    public int getItemCount() {

        return accountList == null ? 0 : accountList.size();
    }

    public class AccountsViewHolder extends RecyclerView.ViewHolder {

        public AccountsViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @BindView(R.id.accounts_profile_image)
        ImageView imgView_icon;

        @BindView(R.id.accounts_profile_name)
        TextView txtView_name;

        @BindView(R.id.accounts_profile_email)
        TextView txtView_email;

        @BindView(R.id.accounts_profile_lastonline)
        TextView txtView_lastonline;

        @BindView(R.id.accounts_profile_joindate)
        TextView txtView_joined;

        @BindView(R.id.accounts_profile_roles)
        TextView txtView_roles;

    }

    private String formatDate(String dateStr) {

        try {

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr.replace("Z", "+00:00").replace("T", " "));
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM yyyy");

            return fmtOut.format(date);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        return "";
    }
}
