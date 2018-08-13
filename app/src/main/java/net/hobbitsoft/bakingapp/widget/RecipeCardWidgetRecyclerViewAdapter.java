package net.hobbitsoft.bakingapp.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.hobbitsoft.bakingapp.R;
import net.hobbitsoft.bakingapp.recipes.Recipe;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} and makes a call to the
 * specified {@link android.view.View.OnClickListener}.
 */
public class RecipeCardWidgetRecyclerViewAdapter extends RecyclerView.Adapter<RecipeCardWidgetRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecipeCardWidgetRecyclerViewAdapter.class.getSimpleName();

    private static final String PREFS_NAME = "net.hobbitsoft.bakingapp.widget.RecipeWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    private final List<Recipe> mRecipesList;
    private final Context mContext;

    public RecipeCardWidgetRecyclerViewAdapter(Context context, List<Recipe> recipes) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Recipe recipe = mRecipesList.get(position);

        if (mRecipesList != null) {
            holder.recipeName.setText(recipe.getName());
            holder.servingSize.setText(String.valueOf(recipe.getServings()));
            if (!recipe.getImage().isEmpty()) {
                Picasso.get()
                        .load(recipe.getImage())
                        .error(R.drawable.ic_menu_black_24dp)
                        .into(holder.recipeImage);
            }
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the selected recipe on to the widget
                Recipe widgetRecipe = recipe;
                ((RecipeWidgetConfigureActivity) mContext).recipeClicked(recipe, position);
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
        public final ImageView recipeImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            recipeName = view.findViewById(R.id.recipe_name);
            servingSize = view.findViewById(R.id.recipe_serving_size);
            recipeImage = view.findViewById(R.id.suggest_menu);
        }

    }

}
