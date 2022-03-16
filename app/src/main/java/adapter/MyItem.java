package adapter;

public class MyItem {
    private String title = "";
    private boolean checked = false;

    public MyItem(String title, boolean checked) {
        this.title = title;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
