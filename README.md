# Movit!

##Introduction
_Why is time moving so fast?_

_What happened to the entire day?_

_How long have I been here, exactly?_

If you ask yourself these questions frequently, then Movit! is definitely the app for you!!

##Getting the App
###Google Play Store
Search for "Movit!" on the Google Play Store, or use [this link](https://play.google.com/store/apps/details?id=com.andela.movit).
###Developer Preview
To view a developer preview of the app, run the following command on your system (NB: this assumes that you have git and Android Studio 2.+ installed on your system).
```
git clone https://github.com/andela-ekarumbi/Movit.git
```
You should then launch Android Studio, navigate to the folder where you cloned the project and import it. From there you can deploy the application to your phone using Android Studio.

##App Features
Movit! uses the Gps on your Android phone to keep track of your movements, and generates a log for you. All you have to do is enable Gps and internet on your phone, then launch the app and tap on 'Start Tracking'. Movit! will continue to track your movements even after you turn off your screen!

To view your movement logs, open the side drawer and tap on 'My Movements'. You will be able to see a list of all the activities that were logged, starting with the most recent.

To view the places that you visit, open the side drawer and tap on 'My Locations'. You will be able to see a list of all the places you visit, and the total time that you have spent at each place. Tap on an item to see a list of all the activities that happened at that particular place.

By default, Movit! logs each activity that has been happening for longer than 5 minutes. To adjust that setting, tap on the Settings icon on the task bar and change the time to what you desire.

##Running Tests
The .gradle file that ships with the project lists all the dependencies necessary to run the tests. Navigate to the _androidTest_ folder on the project view (the tab on the left of Android studio), right click on it and select **Run Tests**. You will need to connect a physical device with Gps and internet enabled so as to run the tests.

##App Limitations
Currently the app is only available in portrait mode for phones only.

##App Screenshots
![Home](https://i.imgsafe.org/ff91711.jpg) ![Tracking](https://i.imgsafe.org/0320b4d.jpg) ![Nav](https://i.imgsafe.org/07892ba.jpg) ![Mov1](https://i.imgsafe.org/084a0cc.jpg)
![Mov2](https://i.imgsafe.org/05ea5fa.jpg) ![Loc1](https://i.imgsafe.org/094d447.jpg)
![Loc2](https://i.imgsafe.org/0a55a74.jpg) ![Set1](https://i.imgsafe.org/0454a9e.jpg)
![Set2](https://i.imgsafe.org/05011d4.jpg)
