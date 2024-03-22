# IntegralMe
This project is an application designed to teach indefinite integrals. This issue is crucial for students, who are on their way to become engineers one day. The app was designed to be minimalistic and simple to use in order to avoid distractions.

## Instalation

There are three ways to install the app. The first is to install Android Studio and open the app with it. The second (easier one) is to download the .apk file. The third one is the easiest - you can download it from the Google Play Store.

### With Android Studio
Download Android Studio and use the play button, when connected to the target device in debug mode. Here are more detailed steps to do so:

1. Enable debug mode on your Android device. Click [here](https://developer.android.com/studio/debug/dev-options "Android Studio documentation") if you want to find out about the details. 

2. Install [Android Studio](https://developer.android.com/studio) on your computer. 

3. Download this project code on your computer.

4. Open unzipped, downloaded code with Android Studio as a project.

5. Connect your mobile device via USB to the computer. Allow necessary permissions on your device (if the popup appears). 

6. Press play in Android Studio. The app will be installed and turned on. You can disconnect your device and enjoy the app. 

### .apk file

Simply download the .apk file from [here](app/release/IntegralMe.apk "IntegralMe.apk") and install it on your mobile device. [This simple tutorial](https://www.youtube.com/watch?v=N0M4XGkpCn4) might help you.

### Play Store
Click the following link on your phone and install the app: [https://play.google.com/store/apps/details?id=integral.me](https://play.google.com/store/apps/details?id=integral.me "IntegralMe")

This version might sometimes not contain all of the features available in the repository versions, but it is fully functional as well.

## Usage

The app offers four different views, which are briefly described below. 

### Main menu
<img src='https://github.com/Mir3605/IntegralMe/assets/120521300/ab9be648-e22c-4bde-9c1f-98e2c1d3fec3' width='400'>

In the main menu, there are level buttons, settings button and games history button. Numbers in the level buttons symbolize difficulty. To play the game simply choose level and difficulty and press the button. 

Settings and games history buttons move you to the settings or games history sections. 

### Level
<img src='https://github.com/Mir3605/IntegralMe/assets/120521300/a9d6b904-6ef8-42ee-8fb8-d9b4125acf41' width='400'>

On the top of the screen, there are points counter, difficulty header, and stage counter. Below them, there is a problem to solve. Some problems can be solved in one step, but some of them need more thinking. The number of steps needed to solve the problem equals the number of empty fields on the screen. There is always one field selected. To insert an answer into the selected field press one of the answers from the bottom half of the screen.

The check button on the bottom of the screen moves you to the next stage (or to the menu in the case of the last stage) if all answers are correct. If you manage to solve two or more problems in a row without wrong answers, you get extra series points. 

### Games history

In the following view, you can check games history on your device. The button at the bottom of the screen allows switching the perspective of the games history. To go back to the main menu use "back" button of your device. 

### Settings
<img src='https://github.com/Mir3605/IntegralMe/assets/120521300/42673a34-da79-409c-aade-3d2aa41031e7' width='400'>

In this view, all of the buttons are described on the screen. To go back to the main menu use "back" button of your device. 

## Tech stack, author and references

This application was entirely created in Android Studio by me. All of the math stuff is displayed thanks to the [iffanmajid](https://github.com/iffanmajid) and his [project](https://github.com/iffanmajid/Katex) based on the Khan Academy project. The tutorial was prepared with the help of [ShowCaseView library](https://github.com/mreram/ShowCaseView) by [mream](https://github.com/mreram). 

## License

License is available [here](LICENSE)
