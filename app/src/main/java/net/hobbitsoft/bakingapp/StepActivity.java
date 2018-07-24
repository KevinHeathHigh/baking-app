package net.hobbitsoft.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import net.hobbitsoft.bakingapp.adapters.RecipeDetailStepRecyclerViewAdapter;
import net.hobbitsoft.bakingapp.adapters.RecipeStepFragment;
import net.hobbitsoft.bakingapp.recipes.Step;

import java.io.Serializable;
import java.util.List;

public class StepActivity extends AppCompatActivity {

    private static final String TAG = StepActivity.class.getSimpleName();
    private static final String STEP_LIST = "step_list";
    private static final String CURRENT_STEP = "current_step";
    private static final String RECIPE_NAME = "recipe_name";
    private static final String FRAGEMENT_TAG = "fragment_tage";

    private List<Step> mStepList;
    private int mCurrentStep;
    private int mTotalSteps;
    private String mRecipeName;
    private final Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        final Intent intent = getIntent();

        RecipeStepFragment recipeStepFragment;

        if (intent == null) {
            Log.e(TAG, getResources().getString(R.string.er_no_intent_found));
            finish();
        }

        if (savedInstanceState == null) {
            Bundle stepBundle = intent.getBundleExtra(RecipeDetailStepRecyclerViewAdapter.STEP_BUNDLE);
            mStepList = (List<Step>) stepBundle.getSerializable(RecipeDetailStepRecyclerViewAdapter.STEP_LIST);
            mRecipeName = intent.getStringExtra(RecipeDetailStepRecyclerViewAdapter.RECIPE_NAME);
            mCurrentStep = intent.getIntExtra(RecipeDetailStepRecyclerViewAdapter.CURRENT_STEP, 0);
            recipeStepFragment = RecipeStepFragment.newInstance(mStepList.get(mCurrentStep));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_fragment_container, recipeStepFragment, FRAGEMENT_TAG)
                    .commit();
        } else {

            mStepList = (List<Step>) savedInstanceState.getSerializable(STEP_LIST);
            mCurrentStep = savedInstanceState.getInt(CURRENT_STEP);
            mRecipeName = savedInstanceState.getString(RECIPE_NAME);
            recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager()
                    .findFragmentByTag(FRAGEMENT_TAG);
        }

        if (mRecipeName != null) setTitle(mRecipeName);
        if (mStepList != null) {
            mTotalSteps = mStepList.size();
            final List<Step> stepList = mStepList;


            final ImageView nextStepView = findViewById(R.id.next_step);
            final ImageView previousStepView = findViewById(R.id.previous_step);

            if (mCurrentStep == 0) {
                previousStepView.setVisibility(View.INVISIBLE);
            } else {
                previousStepView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int previousStep = mCurrentStep - 1;
                        String previousDescription = stepList.get(previousStep).getDescription();
                        //Toast.makeText(mContext, previousDescription, Toast.LENGTH_SHORT).show();
                        RecipeStepFragment newRecipeStepFragment = new RecipeStepFragment();
                        newRecipeStepFragment.setStep(mStepList.get(previousStep));
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_fragment_container, newRecipeStepFragment)
                                .commit();
                        mCurrentStep = previousStep;
                        if (mCurrentStep == 0)
                            previousStepView.setVisibility(View.INVISIBLE);
                        if (nextStepView.getVisibility() == View.INVISIBLE)
                            nextStepView.setVisibility(View.VISIBLE);
                    }
                });
            }

            if (mCurrentStep + 1 == mTotalSteps) {
                nextStepView.setVisibility(View.INVISIBLE);
            } else {
                nextStepView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int nextStep = mCurrentStep + 1;
                        String nextDescription = stepList.get(nextStep).getDescription();
                        //Toast.makeText(mContext, nextDescription, Toast.LENGTH_SHORT).show();
                        RecipeStepFragment newRecipeStepFragment = new RecipeStepFragment();
                        newRecipeStepFragment.setStep(mStepList.get(nextStep));
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_fragment_container, newRecipeStepFragment)
                                .commit();
                        mCurrentStep = nextStep;
                        if (mCurrentStep + 1 == mTotalSteps)
                            nextStepView.setVisibility(View.INVISIBLE);
                        if (previousStepView.getVisibility() == View.INVISIBLE)
                            previousStepView.setVisibility(View.VISIBLE);
                    }
                });
            }
        } else {
            Log.e(TAG, getResources().getString(R.string.er_no_steps_found));
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

        outState.putSerializable(STEP_LIST, (Serializable) mStepList);
        outState.putInt(CURRENT_STEP, mCurrentStep);
        outState.putString(RECIPE_NAME, mRecipeName);
        super.onSaveInstanceState(outState);
    }
}

