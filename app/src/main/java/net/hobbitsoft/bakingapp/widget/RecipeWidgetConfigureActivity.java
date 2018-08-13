package net.hobbitsoft.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.hobbitsoft.bakingapp.R;
import net.hobbitsoft.bakingapp.recipes.ParseRecipes;
import net.hobbitsoft.bakingapp.recipes.Recipe;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class RecipeWidgetConfigureActivity extends Activity {

    public static final String PREFS_NAME = "net.hobbitsoft.bakingapp.widget.RecipeWidget";
    public static final String PREFS_RECIPE = "preference_recipe";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    private static final String TAG = RecipeWidgetConfigureActivity.class.getSimpleName();
    private static final String RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private ProgressBar mProgressBar;

    private List<Recipe> mRecipeList = null;

    private RecipeCardWidgetRecyclerViewAdapter mRecipeCardWidgetRecyclerViewAdapter;
    private RecyclerView mRecipeCardList;
    private RecyclerView.LayoutManager mLayoutManager;


    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public void recipeClicked(Recipe recipe, int position) {
        // Create the Widget
        Log.d(TAG, recipe.getName());

        final Context context = this;
        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId, recipe);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public RecipeWidgetConfigureActivity() {
        super();
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_configure);

        mProgressBar = findViewById(R.id.widget_progress);
        mRecipeCardList = findViewById(R.id.widget_recipe_card_recycler_view);
        mRecipeCardList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecipeCardList.setLayoutManager(mLayoutManager);

        mRecipeCardWidgetRecyclerViewAdapter =
                new RecipeCardWidgetRecyclerViewAdapter(this, mRecipeList);
        mRecipeCardList.setAdapter(mRecipeCardWidgetRecyclerViewAdapter);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        getRecipes();

    }

    private void getRecipes() {
        new RecipeWidgetConfigureActivity.RetrieveRecipies().execute();
    }

    class RetrieveRecipies extends AsyncTask<Void, Void, List<Recipe>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            Uri uri = Uri.parse(RECIPE_URL).buildUpon().build();

            URL url = null;
            HttpsURLConnection httpsURLConnection = null;
            String recipeJSON = new String();

            try {
                url = new URL(uri.toString());
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = httpsURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);

                while (scanner.hasNextLine()) {
                    recipeJSON += scanner.nextLine();
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                httpsURLConnection.disconnect();
            }
            return ParseRecipes.parseRecipies(recipeJSON);

        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);

            if (recipes == null) {
                errorToast();
                finish();
            } else {
                populateRecipeView(recipes);
            }
            mProgressBar.setVisibility((View.GONE));
        }
    }


    private void populateRecipeView(List<Recipe> recipeList) {
        if (mRecipeList == null || !mRecipeList.equals(recipeList)) mRecipeList = recipeList;
        mRecipeCardWidgetRecyclerViewAdapter = new RecipeCardWidgetRecyclerViewAdapter(this, recipeList);
        mRecipeCardList.setAdapter(mRecipeCardWidgetRecyclerViewAdapter);
        mRecipeCardWidgetRecyclerViewAdapter.notifyDataSetChanged();

    }

    /*
There was an issue accessing the Movie DB and so we will show a toast to let the user know
 */
    private void errorToast() {
        String errorMsg = this.getResources().getString(R.string.er_no_recipe_found);
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }
}

