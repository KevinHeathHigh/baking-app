package net.hobbitsoft.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import net.hobbitsoft.bakingapp.R;
import net.hobbitsoft.bakingapp.recipes.Recipe;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeWidgetConfigureActivity RecipeWidgetConfigureActivity}
 */
public class RecipeWidget extends AppWidgetProvider {

    public static final String INGREDIENT_LIST = "ingredient_list";
    public static final String INGREDIENT_BUNDLE = "ingredient_bundle";
    public static final String APP_WIDGET_ID = "app_widget_id";

    private static final String TAG = RecipeWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        // Construct the RemoteViews object
        Intent intent = new Intent(context, ListWidgetService.class);
        Bundle bundle = new Bundle();
        // Pass the List of Ingredients as JSON object
        bundle.putSerializable(INGREDIENT_LIST, recipe.getIngredients());
        intent.putExtra(APP_WIDGET_ID, appWidgetId);
        intent.putExtra(INGREDIENT_BUNDLE, bundle);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        remoteViews.setRemoteAdapter(R.id.widget_ingredients_list, intent);
        remoteViews.setTextViewText(R.id.widget_recipe_name, recipe.getName());
        remoteViews.setEmptyView(R.layout.recipe_widget, R.id.empty_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

