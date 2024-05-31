# Timer App Project

## Introduction
The Timer App Project is an Android application designed to help users manage their time efficiently. Users can set timers for tasks, receive notifications when the timer is done, and be directed to break timers. The app also includes task management features and customizable settings.

## Features
- **Timer Functionality:** Set timers for tasks and breaks, receive notifications upon completion.
- **Task Management:** Keep track of tasks, their descriptions, and completion status.
- **Settings Customization:** Modify timer and break durations, backgrounds, and time formats.
- **Persistent Storage:** Uses SQLite for storing tasks and SharedPreferences for user settings.
- **Tabbed Navigation:** Easy navigation between Timer, Task, and Settings tabs.
- **Dynamic Graphics:** Real-time countdown display.
- **Input Validation:** Ensures valid task entries and time formats.

## Installation
To install and run the Timer App on your Android device:

1. Clone the repository:
    ```bash
    git clone https://github.com/01uan/TimerAndroidProject
    ```

2. Open the project in Android Studio.

3. Build the project and start the emulator or connect an Android device.

4. Run the application by clicking the green "play" button in Android Studio.

## Usage
### Timer Tab
- **Start/Resume:** Start or resume the timer.
- **Stop:** Stop the timer.
- **Restart:** Restart the timer.
- **Breaks:** Automatically switch to breaks after the timer finishes based on settings.

### Task Tab
- **Add Task:** Enter task details and add them to the list.
- **Update Task:** Modify existing task details.
- **Delete Task:** Remove tasks from the list.
- **Completion Status:** View task completion status.

### Settings Tab
- **Timer Settings:** Set default timer, short break, and long break durations.
- **Auto Break:** Configure automatic transition to breaks.
- **Time Format:** Choose between different time formats.


## Screenshots
![timerTab](/ss/timerTab.png?raw=true "Timer Tab")
![taskTab](/ss/taskTab.png?raw=true "Task Tab")
![settingTab](/ss/settingsTab.png?raw=true "Setting Tab")



