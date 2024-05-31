# Timer App Project

## Setup Instructions

This app will require notification permission. If you have the `.apk` file, you can just run the app on your Android device. Otherwise:

1. Make the project.
2. Start up your emulator on Android Studio.
3. Click the green "play" button.

## Purpose of the Application

The purpose of the timer application is to help users manage their time efficiently in relation to their tasks. Users can set timers in sessions, receive notifications when the timer is done, and be directed to a break timer. Additionally, the app includes a task management feature to keep track of tasks that need to be completed and how many iterations of timer/break it will take. There is also a settings page to modify timer breaks, backgrounds, and time format.

## New Technologies

- This app uses SQLite to store tasks with attributes such as task name, description, and completion status.
- Schedules notifications to alert users when the timer or break ends.
- Shared Preferences are used to save user settings like the length of time/break and auto break.
- Tabbed layout to navigate between Timer, Tasks, and Settings fragments.
- Dynamic Graphics to show the countdown.
- Input validation to prevent empty task entries and non-numeric characters in the timer.

## How to Use

Overall, you can either swipe or click on the tabs to navigate throughout the app.

### Timer Tab

This is the main component of the app. This will list your current task and the total amount of "chunks" it is and how many you have left to do.

- You can start/resume, stop, and restart the timer.
- Only when the timer is stopped can you change the timer by editing the TextEdit.
- Only when the timer finishes does it put you back to one of the breaks specified in the settings.
- Only when the break ends does it put you back to the timer.

### Task Tab

This is the task tab where it will list all of your tasks and the chunks associated with it. It will also show what is completed.

- You can input into the fields below and press add. This will put it into the SQLite database.
- You can click the tasks, and it will show you the choice between updating and deleting the task.

### Settings Tab

This is the settings tab where it will list all your settings.

- You can change the default timer, short break, and long break times.
- Once the timer has been stopped and restarted, this is where it will default the times to.
- Auto Break is when the main timer finishes; it will either go to the long break, short break, or off, which is just back to the timer.

## Functionality

- If you go back to the timer tab, you can find your current task there. The current task is the first task in the list.
- Upon finishing the timer, it will send a notification indicating that you have finished your timer. It will also update the timer fragment with the updated number of chunks left.
- Upon completing the task and fulfilling the chunks, the screen will update, indicating that you have finished all your tasks.
- If you swipe back to the task tab, the task will indicate that it is completed by turning green.