package models.pages;

import models.browsers.Browser;
import models.components.WebComponent;

public abstract class Page extends WebComponent {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBrowser(Browser browser) {
        this.browser = browser;
    }

    public String url() {
        return this.url;
    }
}
