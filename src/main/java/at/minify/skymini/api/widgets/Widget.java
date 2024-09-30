package at.minify.skymini.api.widgets;

import lombok.Getter;

@Getter
public abstract class Widget {

    private String text = "";
    private float x = 50;
    private float y = 50;
    private boolean enabled = true;
    private boolean center = false;

    public void setText(String text) {
        this.text = text;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setCentered(boolean center) {
        this.center = center;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
