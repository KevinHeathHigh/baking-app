package net.hobbitsoft.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.hobbitsoft.bakingapp.R;
import net.hobbitsoft.bakingapp.recipes.Ingredient;

import java.util.List;


public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = ListRemoteViewsFactory.class.getSimpleName();

    Context mContext;
    List<Ingredient> mIngredientList;
    int mAppWidgetId;

    public ListRemoteViewsFactory(Context context, Intent intent) {

        this.mContext = context;
        this.mAppWidgetId = intent.getIntExtra(RecipeWidget.APP_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Bundle bundle = new Bundle();
        bundle = intent.getBundleExtra(RecipeWidget.INGREDIENT_BUNDLE);

        mIngredientList = (List<Ingredient>) bundle.getSerializable(RecipeWidget.INGREDIENT_LIST);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredientList == null) return 0;
        return mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_ingredient_list);

        remoteViews.setTextViewText(R.id.widget_quantity,
                mIngredientList.get(position).getQuantity());
        remoteViews.setTextViewText(R.id.widget_measure,
                mIngredientList.get(position).getMeasure());
        remoteViews.setTextViewText(R.id.widget_ingredient,
                mIngredientList.get(position).getIngredient());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
