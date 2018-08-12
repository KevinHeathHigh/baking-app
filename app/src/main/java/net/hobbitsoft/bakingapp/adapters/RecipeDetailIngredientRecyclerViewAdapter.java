package net.hobbitsoft.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hobbitsoft.bakingapp.R;
import net.hobbitsoft.bakingapp.recipes.Ingredient;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Ingredient}
 */
public class RecipeDetailIngredientRecyclerViewAdapter extends RecyclerView.Adapter<RecipeDetailIngredientRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecipeDetailIngredientRecyclerViewAdapter.class.getSimpleName();
    private final List<Ingredient> mIngredientList;
    private final Context mContext;

    public RecipeDetailIngredientRecyclerViewAdapter(Context context, List<Ingredient> ingredients) {
        mContext = context;
        mIngredientList = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_recipe_detail_ingredients, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ingredient = mIngredientList.get(position);
        holder.ingredient = holder.ingredient;
        holder.mQuantity.setText(holder.ingredient.getQuantity());
        holder.mMeasure.setText(holder.ingredient.getMeasure());
        holder.mIngredient.setText(holder.ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if (mIngredientList == null) return 0;
        return mIngredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mQuantity;
        public final TextView mMeasure;
        public final TextView mIngredient;
        public Ingredient ingredient;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mQuantity = view.findViewById(R.id.ingredient_quantity);
            mMeasure = view.findViewById(R.id.ingredient_measure);
            mIngredient = view.findViewById(R.id.ingredient_ingredient);
        }

    }
}
