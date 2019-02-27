package study.lily.mvpro.core.utils;

import android.view.View;
import android.widget.AdapterView;

import study.lily.mvpro.core.presenter.IPresenter;

/**
 * @Author rape flower
 * @Date 2019-02-27 13:55
 * @Describe 事件帮助类
 */
public class EventHelper {

    public static void click(IPresenter iPresenter, View...views) {
        if (!(iPresenter instanceof View.OnClickListener)) {
            return;
        }
        click((View.OnClickListener) iPresenter, views);
    }

    public static void longClick(IPresenter iPresenter, View...views) {
        if (!(iPresenter instanceof View.OnLongClickListener)) {
            return;
        }
        longClick((View.OnLongClickListener) iPresenter, views);
    }

    public static void itemClick(IPresenter iPresenter, AdapterView...views) {
        if (!(iPresenter instanceof AdapterView.OnItemClickListener)) {
            return;
        }
        itemClick((AdapterView.OnItemClickListener) iPresenter, views);
    }

    public static void itemLongClick(IPresenter iPresenter, AdapterView...views) {
        if (!(iPresenter instanceof AdapterView.OnItemLongClickListener)) {
            return;
        }
        itemLongClick((AdapterView.OnItemLongClickListener) iPresenter, views);
    }

    public static void itemSelected(IPresenter iPresenter, AdapterView...views) {
        if (!(iPresenter instanceof AdapterView.OnItemSelectedListener)) {
            return;
        }
        itemSelected((AdapterView.OnItemSelectedListener) iPresenter, views);
    }

    public static void click(View.OnClickListener clickListener, View...views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (View v : views) {
            v.setOnClickListener(clickListener);
        }
    }

    public static void longClick(View.OnLongClickListener longClickListener, View...views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (View v : views) {
            v.setOnLongClickListener(longClickListener);
        }
    }

    public static void itemClick(AdapterView.OnItemClickListener itemClickListener, AdapterView...views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (AdapterView v : views) {
            v.setOnItemClickListener(itemClickListener);
        }
    }

    public static void itemLongClick(AdapterView.OnItemLongClickListener itemLongClickListener, AdapterView...views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (AdapterView v : views) {
            v.setOnItemLongClickListener(itemLongClickListener);
        }
    }

    public static void itemSelected(AdapterView.OnItemSelectedListener itemSelectedListener, AdapterView...views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (AdapterView v : views) {
            v.setOnItemSelectedListener(itemSelectedListener);
        }
    }
}
