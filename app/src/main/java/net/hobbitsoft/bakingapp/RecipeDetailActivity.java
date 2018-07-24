package net.hobbitsoft.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import net.hobbitsoft.bakingapp.adapters.RecipeCardRecyclerViewAdapter;
import net.hobbitsoft.bakingapp.adapters.RecipeDetailIngredientRecyclerViewAdapter;
import net.hobbitsoft.bakingapp.adapters.RecipeDetailStepRecyclerViewAdapter;
import net.hobbitsoft.bakingapp.recipes.Ingredient;
import net.hobbitsoft.bakingapp.recipes.Recipe;
import net.hobbitsoft.bakingapp.recipes.Step;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private static final String RECIPE = "recipe";

    private Recipe mRecipe;
    private List<Ingredient> mIngredientList = null;
    private List<Step> mStepList = null;

    private RecipeDetailIngredientRecyclerViewAdapter mRecipeDetailIngredientRecyclerViewAdapter;
    private RecipeDetailStepRecyclerViewAdapter mRecipeDetailStepRecyclerViewAdapter;

    private RecyclerView mRecipeIngredientList;
    private RecyclerView mRecipeStepList;
    private RecyclerView.LayoutManager mIngredientLayoutManager;
    private RecyclerView.LayoutManager mStepLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        final Intent intent = getIntent();

        if (intent == null) {
            Log.e(TAG, getResources().getString(R.string.er_no_intent_found));
            finish();
        }

        if (savedInstanceState == null) {
            Bundle recipeBundle = intent.getBundleExtra(RecipeCardRecyclerViewAdapter.RECIPE_BUNDLE);
            mRecipe = (Recipe) recipeBundle.getSerializable(RecipeCardRecyclerViewAdapter.RECIPE);
        } else {
            mRecipe = (Recipe) savedInstanceState.getSerializable(RECIPE);
        }
        if (mRecipe != null) {
            setTitle(mRecipe.getName());
            mRecipeIngredientList = findViewById(R.id.recipe_detail_ingredient_recycler);
            mRecipeIngredientList.setHasFixedSize(true);
            mRecipeStepList = findViewById(R.id.recipe_detail_steps_recycler);
            mRecipeStepList.setHasFixedSize(true);
            mIngredientLayoutManager = new LinearLayoutManager(this);
            mStepLayoutManager = new LinearLayoutManager(this);
            mRecipeIngredientList.setLayoutManager(mIngredientLayoutManager);
            mRecipeStepList.setLayoutManager(mStepLayoutManager);
            mIngredientList = mRecipe.getIngredients();
            mStepList = mRecipe.getSteps();

            mRecipeDetailIngredientRecyclerViewAdapter =
                    new RecipeDetailIngredientRecyclerViewAdapter(this, mIngredientList);
            mRecipeIngredientList.setAdapter(mRecipeDetailIngredientRecyclerViewAdapter);

            mRecipeDetailStepRecyclerViewAdapter =
                    new RecipeDetailStepRecyclerViewAdapter(this, mStepList, mRecipe.getName());
            mRecipeStepList.setAdapter(mRecipeDetailStepRecyclerViewAdapter);
        } else {
            Log.d(TAG, getResources().getString(R.string.er_no_recipe_found));
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
    }
}
