package com.hauduepascal.ferez96.battleship.common;

public class HtmlBuilder {
    private StringBuilder builder = new StringBuilder();
    private StringBuilder bodyBuilder = new StringBuilder();
    private String title;

    public String build() {
        addHead();
        builder.append(bodyBuilder);
        addTail();
        return builder.toString();
    }

    public HtmlBuilder title(String value) {
        this.title = value;
        return this;
    }

    public HtmlBuilder addBodyLine(String line){
        this.bodyBuilder.append(line+"\n");
        return this;
    }

    private void addHead() {
        builder.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n");
        builder.append("<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Hậu Duệ Pascal 2019 | " + title + "</title>\n" +
                "    <link rel=\"stylesheet\" href=\"css/bootstrap.min.css\">\n" +
                "    <script src=\"js/jquery.min.js\"></script>\n" +
                "    <script src=\"js/popper.min.js\"></script>\n" +
                "    <script src=\"js/bootstrap.min.js\"></script>\n" +
                "</head>\n" +
                "<body>\n");
    }

    private void addTail() {
        builder.append("</body>\n" +
                "</html>\n");
    }
}
