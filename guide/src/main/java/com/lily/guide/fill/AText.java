package com.lily.guide.fill;

import java.util.HashMap;
import java.util.Map;

/**
 * 文字段落
 */
public class AText {

    // 文字段落
    public String text;
    public boolean isFill = false;

    public Map<Integer, EditPostInfo> postInfoes = new HashMap<>();

    public AText(String text) {
        this.text = text;
    }

    public AText(String text, boolean isFill) {
        this.text = text;
        this.isFill = isFill;
    }

    public int getStartPos() {
        if (postInfoes.isEmpty()) {
            return -1;
        }

        int firstRow = Integer.MAX_VALUE;
        for (Map.Entry<Integer, EditPostInfo> entry : postInfoes.entrySet()) {
            int row = entry.getKey();
            if (firstRow > row) {
                firstRow = row;
            }
        }
        return firstRow;
    }
}
