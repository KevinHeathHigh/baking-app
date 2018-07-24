package net.hobbitsoft.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hobbitsoft.bakingapp.R;
import net.hobbitsoft.bakingapp.StepActivity;
import net.hobbitsoft.bakingapp.recipes.Step;

import java.io.Serializable;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Step} and makes a call to the
 * specified {@link android.view.View.OnClickListener}.
 */
public class RecipeDetailStepRecyclerViewAdapter extends RecyclerView.Adapter<RecipeDetailStepRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecipeDetailStepRecyclerViewAdapter.class.getSimpleName();

    public static final String STEP_LIST = "step_list";
    public static final String STEP_BUNDLE = "step_bundle";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String CURRENT_STEP = "current_step";

    private final List<Step> mStepList;
    private final Context mContext;
    private String mRecipeName;

    public RecipeDetailStepRecyclerViewAdapter(Context context, List<Step> steps, String recipeName) {
        mContext = context;
        mStepList = steps;
        mRecipeName = recipeName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_recipe_detail_steps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final List<Step> stepList = mStepList;
        final int currentStep = position;

        holder.step = mStepList.get(position);
        holder.mShortDescription.setText(holder.step.getShortDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StepActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(STEP_LIST, (Serializable) stepList);
                intent.putExtra(STEP_BUNDLE, bundle);
                intent.putExtra(RECIPE_NAME, mRecipeName);
                intent.putExtra(CURRENT_STEP, currentStep);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mStepList == null) return 0;
        return mStepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mShortDescription;
        public Step step;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mShortDescription = view.findViewById(R.id.step_short_description);
        }
    }
}