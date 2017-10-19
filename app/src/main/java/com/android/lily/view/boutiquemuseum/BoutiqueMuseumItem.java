package com.android.lily.view.boutiquemuseum;

/**
 * @author rape flower
 * @descripe 精品馆条目对象
 */
public class BoutiqueMuseumItem {

    /**
     * 此字段测试用，到时替换为PB Model
     */
    private String name;

    /**
     * 标识当前的精品馆条目对象对象是否已经绘制过指示器横线
     * true: 已经绘制过，false: 没有绘制过
     */
    private boolean isDrawIndicatorLine = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDrawIndicatorLine() {
        return isDrawIndicatorLine;
    }

    public void setDrawIndicatorLine(boolean drawIndicatorLine) {
        this.isDrawIndicatorLine = drawIndicatorLine;
    }
}
