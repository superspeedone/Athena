package com.superspeed.common.util;

import java.beans.PropertyEditorSupport;

import org.springframework.web.util.HtmlUtils;

/**
 * 字符串过滤类
 * @ClassName: StringEscapeEditor
 * @Description: （采用spring的HtmlUtils）
 * @author xc.yanww
 * @date 2017-5-26 下午2:46:21
 * @version 1.0
 */
public class StringEscapeEditor extends PropertyEditorSupport {
	
    public StringEscapeEditor() {}

    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            setValue(null);
        } else {
            setValue(HtmlUtils.htmlEscape(text));
        }
    }

}
