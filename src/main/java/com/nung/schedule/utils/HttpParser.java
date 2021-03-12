package com.nung.schedule.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class HttpParser {
    private Element page;

    public HttpParser(String htmlPage) {
        this.page = Jsoup.parse(htmlPage.replaceAll("<br>","-")).body();
    }

    public String parse() {
        StringBuilder result = new StringBuilder();
        result.append(page.getElementsByTag("h4").get(4).text() + "\n\n");
        for(Element scheduleInfoElement : page.getElementsByTag("td")) {
            result.append(getTextForElement(scheduleInfoElement));
        }
        return result.toString();
    }

    private String getTextForElement(Element scheduleInfoElement) {
        StringBuilder answer = new StringBuilder();
        String textFromElement = scheduleInfoElement.text();

        if(textFromElement.length() == 1) {
            answer.append(textFromElement);
        } else if(textFromElement.length() == 11) {
            answer.append(" - ");
            answer.append(textFromElement);
        } else {
            if(textFromElement.contains("дистанційно")) {
                answer.append(" (дистанційно)");
            }
            answer.append("\n");
            answer.append(textFromElement.replace("дистанційно-", "Предмет: ")
                                        .replace("-http", "\nПосилання на пару: http")
                                        .replace(")- ", ")\n")
                                        .replace("- -", "\n\n"));
            answer.append("\n\n");
        }
        return answer.toString();
    }
}
