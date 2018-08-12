package net.hobbitsoft.bakingapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.hobbitsoft.bakingapp.adapters.RecipeCardRecyclerViewAdapter;
import net.hobbitsoft.bakingapp.recipes.ParseRecipes;
import net.hobbitsoft.bakingapp.recipes.Recipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String RECIPE_STATE = "recipe_state";

    private ProgressBar mProgressBar;

    private List<Recipe> mRecipeList = null;

    private RecipeCardRecyclerViewAdapter mRecipeCardRecyclerViewAdapter;
    private RecyclerView mRecipeCardList;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress);
        mRecipeCardList = findViewById(R.id.recipe_card_recycler_view);
        mRecipeCardList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mRecipeCardList.setLayoutManager(mLayoutManager);

        if (savedInstanceState == null || !savedInstanceState.containsKey(RECIPE_STATE)) {
            mRecipeCardRecyclerViewAdapter =
                    new RecipeCardRecyclerViewAdapter(this, mRecipeList);
            mRecipeCardList.setAdapter(mRecipeCardRecyclerViewAdapter);

            getRecipies();
        } else {
            mRecipeList = (List<Recipe>) savedInstanceState.getSerializable(RECIPE_STATE);
            populateRecipeView(mRecipeList);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(RECIPE_STATE, (Serializable) mRecipeList);
        super.onSaveInstanceState(outState);
    }

    private void getRecipies() {
        new RetrieveRecipies().execute();
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
            } else {
                populateRecipeView(recipes);
            }
            mProgressBar.setVisibility((View.GONE));
        }
    }


    private void populateRecipeView(List<Recipe> recipeList) {
        if (mRecipeList == null || !mRecipeList.equals(recipeList)) mRecipeList = recipeList;
        mRecipeCardRecyclerViewAdapter = new RecipeCardRecyclerViewAdapter(this, recipeList);
        mRecipeCardList.setAdapter(mRecipeCardRecyclerViewAdapter);
        mRecipeCardRecyclerViewAdapter.notifyDataSetChanged();
    }

    /*
There was an issue accessing the Movie DB and so we will show a toast to let the user know
 */
    private void errorToast() {
        String errorMsg = this.getResources().getString(R.string.er_no_recipe_found);
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }
}


