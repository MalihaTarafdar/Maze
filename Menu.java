public class Menu {

  private boolean onScreen;
  private boolean onStart;
  private int i;

  private String[] startOptionsNames = {"Play", "Create a Maze", "Settings", "Quit"};
  private boolean[] startOptions = new boolean[startOptionsNames.length];

  public Menu() {
    onScreen = true;
    onStart = true;
    startOptions[0] = true;
    i = 0;
  }

  public void moveDown() {
    startOptions[i] = false;
    i++;
    if (i >= startOptions.length)
      i = 0;
    startOptions[i] = true;
  }
  public void moveUp() {
    startOptions[i] = false;
    i--;
    if (i < 0)
      i = startOptions.length - 1;
    startOptions[i] = true;
  }

  public void setOnScreen(boolean onScreen) {
    this.onScreen = onScreen;
  }
  public void setOnStart(boolean onStart) {
    this.onStart = onStart;
  }

  public boolean isOnScreen() {
    return onScreen;
  }
  public boolean isOnStart() {
    return onStart;
  }
  public String[] startNames() {
    return startOptionsNames;
  }
  public boolean[] getStartOptions() {
    return startOptions;
  }
}
