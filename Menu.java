public class Menu {

  private boolean onScreen;
  private boolean onStart;
  private boolean onMap;
  private boolean paused;
  private boolean onHow;
  private int i;

  private String[] startOptionsNames = {"Play", "How to Play", "Quit"};
  private String[] mapNames = {"Maze 1", "Maze 2", "Maze 3"};
  private String[] pauseNames = {"Resume", "Menu"};

  private boolean[] startOptions = new boolean[startOptionsNames.length];
  private boolean[] mapOptions = new boolean[mapNames.length];
  private boolean[] pauseOptions = new boolean[pauseNames.length];

  public Menu() {
    onScreen = true;
    onStart = true;
    startOptions[0] = true;
    mapOptions[0] = true;
    i = 0;
  }

  public void resetOptions() {
    i = 0;
    mapOptions[0] = true;
    for (int x = 1; x < mapOptions.length; x++) {
      mapOptions[x] = false;
    }
    pauseOptions[0] = true;
    for (int x = 1; x < pauseOptions.length; x++) {
	  pauseOptions[x] = false;
    }
    startOptions[0] = true;
    for (int x = 1; x < startOptions.length; x++) {
	  startOptions[x] = false;
    }
  }

  public void moveForward(int o) {
	  if (o == 0) {
		startOptions[i] = false;
		i++;
		if (i >= startOptions.length)
			i = 0;
		startOptions[i] = true;
	  } else if (o == 1) {
		mapOptions[i] = false;
		i++;
		if (i >= mapOptions.length)
			i = 0;
		mapOptions[i] = true;
	  } else if (o == 2) {
		pauseOptions[i] = false;
		i++;
		if (i >= pauseOptions.length)
			i = 0;
		pauseOptions[i] = true;
	  }
  }

  public void moveBackward(int o) {
	if (o == 0) {
		startOptions[i] = false;
		i--;
		if (i < 0)
			i = startOptions.length - 1;
		startOptions[i] = true;
	} else if (o == 1) {
		mapOptions[i] = false;
		i--;
		if (i < 0)
			i = mapOptions.length - 1;
		mapOptions[i] = true;
	} else if (o == 2) {
		pauseOptions[i] = false;
		i--;
		if (i < 0)
			i = pauseOptions.length - 1;
		pauseOptions[i] = true;
	}
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
  public void setPaused(boolean paused) {
	  this.paused = paused;
  }
  public void setOnHow(boolean onHow) {
	  this.onHow = onHow;
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
  public boolean isPaused() {
	return paused;
  }
  public boolean isOnHow() {
	  return onHow;
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
  public String[] getPauseNames() {
	  return pauseNames;
  }
  public boolean[] getPauseOptions() {
	  return pauseOptions;
  }
}
