package com.example.newsaggregator.widgets;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * BaseCell for binding data and itemview to a listview.
 * @param <T>
 */
public abstract class BaseCell<T> extends ListCell<T> implements Callback<Class<?>, Object> {
    private final URL fxmlURL;

    /**
     * Constructor.
     * @param fxmlURL A fxmlUrl that you want to show on purpose.
     */
    public BaseCell(URL fxmlURL) {
        this.fxmlURL = fxmlURL;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            // if there is a null line.
            setText(null);
            setGraphic(null);
        } else if (getGraphic() == null) {
            // if it has not created a node before, then go inside this.
            if (fxmlURL == null) {
                setText("");
            } else {
                try {
                    setGraphic(FXMLLoader.load(fxmlURL, null, null, this,
                            Charset.forName(FXMLLoader.DEFAULT_CHARSET_NAME)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bindData(item);
        } else {
            // node was created before，and hence could be reused.
            bindData(item);
        }

    }

    /**
     * Bind data within the medthod, and update the listview.
     * @param item
     */
    public abstract void bindData(T item);

    @Override
    public Object call(Class<?> param) {
        return this;
    }

}
