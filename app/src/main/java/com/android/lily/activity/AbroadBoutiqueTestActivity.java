package com.android.lily.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.lily.R;
import com.android.lily.fragment.SuperAwesomeCardFragment;
import com.android.lily.view.boutiquemuseum.BoutiqueMuseumItem;
import com.android.lily.view.boutiquemuseum.BoutiqueMuseumTabBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilei on 17/10/17.
 */
public class AbroadBoutiqueTestActivity extends FragmentActivity {

    private BoutiqueMuseumTabBar bmtTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abroad_boutique_museum);

        final List<BoutiqueMuseumItem> data = getBmtData();

        bmtTab = (BoutiqueMuseumTabBar) findViewById(R.id.bmt_tab);
        bmtTab.setFragmentManager(getSupportFragmentManager());
        bmtTab.setAdapter(data);
    }

    /**
     * 测试数据
     *
     * @return
     */
    private List<BoutiqueMuseumItem> getBmtData() {
        List<BoutiqueMuseumItem> list = new ArrayList<BoutiqueMuseumItem>();

        for (int i = 0; i < 6; i++) {
            BoutiqueMuseumItem bmt1 = new BoutiqueMuseumItem();
            bmt1.name = "德国馆" + i;
            bmt1.image = "https://img01.j1.com/upload/pic/homepage/1494300131640.jpg";
            bmt1.fragment = SuperAwesomeCardFragment.newInstance(i);
            list.add(bmt1);
        }

        return list;
    }
}
