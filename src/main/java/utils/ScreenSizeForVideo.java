package utils;

public enum ScreenSizeForVideo {

    SCREENSIZE();

    private int height;
    private int width;

    ScreenSizeForVideo() {
        String browserType = System.getProperty("browser");

        this.height = 900;
        if (browserType.equals("chromemobile")) {
            this.width = 500;
        } else {
            this.width = 1400;
        }
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
