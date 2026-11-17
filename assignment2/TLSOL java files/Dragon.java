
/**
 * Write a description of class Dragon here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Dragon {
    private String name;
    private String description;
    private boolean isAcquired;
    private boolean canFly;

    public Dragon(String name, String description) {
        this.name = name;
        this.description = description;
        this.isAcquired = false;
        this.canFly = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAcquired() {
        return isAcquired;
    }

    public void setAcquired(boolean acquired) {
        isAcquired = acquired;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean canFly() {
        return canFly;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }
}
