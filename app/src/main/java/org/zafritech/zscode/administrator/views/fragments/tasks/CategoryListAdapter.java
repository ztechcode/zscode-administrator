package org.zafritech.zscode.administrator.views.fragments.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.tasks.models.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<Category> mCategories;

    CategoryListAdapter(Context context, List<Category> mCategories) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mCategories = mCategories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.row_tasks_categories, parent, false);

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.CategoryViewHolder holder, int position) {

        if (mCategories != null) {

            Category current = mCategories.get(position);
            holder.categoryItemView.setText(current.getName());

        } else {

            holder.categoryItemView.setText("No category found");
        }
    }

    @Override
    public int getItemCount() {

        if (mCategories != null)
            return mCategories.size();

        else return 0;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView categoryItemView;

        public CategoryViewHolder(@NonNull View itemView) {

            super(itemView);

            categoryItemView = itemView.findViewById(R.id.tasks_category_text);
        }
    }
}
