package com.example.designpattern.decorator;

import org.apache.commons.lang3.StringEscapeUtils;

// 데코레이터. 컴포넌트 인터페이스를 구현한다.
public class HtmlEncodedMessage implements Message {

    private Message message;

    public HtmlEncodedMessage(Message message) {
        this.message = message;
    }

    @Override
    public String getContent() {
        return StringEscapeUtils.escapeHtml4(message.getContent());
    }
}
