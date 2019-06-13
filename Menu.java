public class Menu {

  private boolean onScreen;
  private boolean onStart;
  private boolean onMap;
  private int i;

  private String[] startOptionsNames = {"Play", "Settings", "Quit"};
  private String[] mapNames = {"Maze 1", "Maze 2", "Maze 3"};

  private boolean[] startOptions = new boolean[startOptionsNames.length];
  private boolean[] mapOptions = new boolean[mapNames.length];

  public Menu() {
    onScreen = true;
    onStart = true;
    startOptions[0] = true;
    mapOptions[0] = true;
    i = 0;
  }

  public void resetMapOptions() {
    i = 0;
    mapOptions[0] = true;
    for (int x = 1; x < mapOptions.length; x++) {
      mapOptions[x] = false;
    }
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
  public void moveRight() {
    mapOptions[i] = false;
    i++;
    if (i >= mapOptions.length)
      i = 0;
    mapOptions[i] = true;
  }
  public void moveLeft() {
    mapOptions[i] = false;
    i--;
    if (i < 0)
      i = mapOptions.length - 1;
    mapOptions[i] = true;
  }

  public void setOnScreen(boolean onScreen) {
    this.onScreen = onScreen;
  }
  public void setOnStart(boolean onStart) {
    this.onStart = onStart;
  }
  public void setOnMap(boolean onMap) {
    this.onMap = onMap;
  }
  public void setI(int i) {
    this.i = i;
  }

  public boolean isOnScreen() {
    return onScreen;
  }
  public boolean isOnStart() {
    return onStart;
  }
  public boolean isOnMap() {
    return onMap;
  }

  public String[] getStartNames() {
    return startOptionsNames;
  }
  public boolean[] getStartOptions() {
    return startOptions;
  }
  public String[] getMapNames() {
    return mapNames;
  }
  public boolean[] getMapOptions() {
    return mapOptions;
  }
}
