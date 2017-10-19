package com.android.lily.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.lily.R;
import com.android.lily.view.boutiquemuseum.BoutiqueMuseumItem;
import com.android.lily.view.boutiquemuseum.BoutiqueMuseumTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilei on 17/10/17.
 */
public class AbroadBoutiqueTestActivity extends Activity {

    private BoutiqueMuseumTabView bmtBoutiqueMuseum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abroad_boutique_museum);

        bmtBoutiqueMuseum = (BoutiqueMuseumTabView) findViewById(R.id.bmt_boutique_museum);
        bmtBoutiqueMuseum.setTabPaddingLeftRight(12);

        final List<BoutiqueMuseumItem> data = getBmtData();
        bmtBoutiqueMuseum.setDataSource(data);
        //精品馆
        bmtBoutiqueMuseum.notifyDataSetChanged();
        bmtBoutiqueMuseum.setOnSelectBoutiqueMuseumListener(new BoutiqueMuseumTabView.OnSelectBoutiqueMuseumListener() {
            @Override
            public void onSelect(int position) {
                Toast.makeText(AbroadBoutiqueTestActivity.this, data.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 测试数据
     *
     * @return
     */
    private List<BoutiqueMuseumItem> getBmtData() {
        List<BoutiqueMuseumItem> list = new ArrayList<BoutiqueMuseumItem>();

        for (int i = 0; i < 10; i++) {
            BoutiqueMuseumItem bmt1 = new BoutiqueMuseumItem();
            bmt1.setName("德国馆" + i);
            bmt1.setDrawIndicatorLine(false);
            list.add(bmt1);
        }

        return list;
    }
}
