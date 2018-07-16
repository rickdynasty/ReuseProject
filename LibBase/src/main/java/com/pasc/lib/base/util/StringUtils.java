package com.pasc.lib.base.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * 对字符串的处理的工具类
 * Created by ruanwei489 on 2018/1/21.
 */

public class StringUtils {

    /**
     * @param content  传入的字符串
     * @param frontNum 保留前面字符位数
     * @param endNum   保留后面字符位数
     * @return
     */
    public static String getStarString(String content, int frontNum, int endNum) {

        if (frontNum >= content.length() || frontNum < 0) {
            return content;
        }
        if (endNum >= content.length() || endNum < 0) {
            return content;
        }
        if (frontNum + endNum >= content.length()) {
            return content;
        }
        String starStr = "";
        for (int i = 0; i < (content.length() - frontNum - endNum); i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, frontNum) + starStr
                + content.substring(content.length() - endNum, content.length());

    }


    /**
     * 文本关键词变色
     *
     * @param text         文本
     * @param keyWord      关键字
     * @param keyWordColor 关键字颜色
     * @return
     */
    public static SpannableStringBuilder setKeyWordTextColor(String text, String keyWord, int keyWordColor) {
        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = keyWord.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do {
            start = temp.indexOf(keyWord);

            if (start != -1) {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
            }

        } while (start != -1);

        SpannableStringBuilder styledText = new SpannableStringBuilder(text);
        for (Integer i : sTextsStartList) {
            styledText.setSpan(
                    new ForegroundColorSpan(keyWordColor),
                    i,
                    i + sTextLength,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return styledText;
    }


}
