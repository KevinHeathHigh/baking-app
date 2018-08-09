package net.hobbitsoft.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.hobbitsoft.bakingapp.R;
import net.hobbitsoft.bakingapp.RecipeStepDetailActivity;
import net.hobbitsoft.bakingapp.recipes.Recipe;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} and makes a call to the
 * specified {@link android.view.View.OnClickListener}.
 */
public class RecipeCardRecyclerViewAdapter extends RecyclerView.Adapter<RecipeCardRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecipeCardRecyclerViewAdapter.class.getSimpleName();
    public static final String RECIPE = "recipe";
    public static final String RECIPE_BUNDLE = "recipe_bundle";

    private final List<Recipe> mRecipesList;
    private final Context mContext;

    public RecipeCardRecyclerViewAdapter(Context context, List<Recipe> recipes) {
        mRecipesList = recipes;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Recipe recipe = mRecipesList.get(position);
        if (mRecipesList != null) {
            holder.recipeName.setText(recipe.getName());
            holder.servingSize.setText(String.valueOf(recipe.getServings()));
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), recipe.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, RecipeStepDetailActivity.class);
                // Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RECIPE, recipe);
                intent.putExtra(RECIPE_BUNDLE, bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mRecipesList == null) return 0;
        return mRecipesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView recipeName;
        public final TextView servingSize;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            recipeName = view.findViewById(R.id.recipe_name);
            servingSize = view.findViewById(R.id.recipe_serving_size);
        }

    }


}
