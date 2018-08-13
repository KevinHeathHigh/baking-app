package net.hobbitsoft.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListWidgetService extends RemoteViewsService {

    private static final String TAG = ListWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListRemoteViewsFactory(this.getApplicationContext(), intent));
    }
}
