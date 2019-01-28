# MiniProject
An attendance system using WiFi P2P

This project is a WiFi Peer to Peer based attendance system for colleges. The gist of the project is to enable teachers to mark students present, without having to waste time in roll calls.

The app has two modes: One for the teacher and the other for the students.
The teacher has to select the time of the lecture, and the class their teaching in.

The students have to setup the app by providing details about their class and by logging their IMEI number into the system. This last step prevents "proxy" attendance.

Every time the teacher clicks on a button, his/her phone creates a network which each student's device connects to. Once connected, the device broadcasts it's details, and on a succcessful confirmation, disconnects.
