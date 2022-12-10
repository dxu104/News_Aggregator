module com.example.newsaggregator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires javafx.swt;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpmime;
    requires com.alibaba.fastjson2;


//    opens com.example.newsaggregator to javafx.fxml;
//    exports com.example.newsaggregator;

    opens com.example.newsaggregator.ui to javafx.fxml;
    exports com.example.newsaggregator.ui;

}