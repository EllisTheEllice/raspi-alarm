# raspi-alarm

This is a small home-made alarmsystem based on a raspberry pi. The alarmsystem is able to detect motion through a IR-sensor. If a motion is detected,
the raspberry starts taking images from the suspicious movements. Those images are then immediately backed-up to AWS S3, so that in case the raspberry 
gets destroyed, some images have made it to the backup. In addition to this, the raspberry send a notification to a AWS SNS topic, so that the owner
can get notified in multiple ways (email, SMS, Push etc.).
The project can work in two modes:
- Alarmsystem Photo mode
- Alarmsystem Video mode (Kinesis)

## Version
0.1

# Table of contents


1. [Used Tech](#technology)
2. [Setting up the raspberry camera](#camera)
3. [Setting up the raspberry radio module](#radio)
4. [Setting up the raspberry](#raspberry)
5. [Setting up the AWS](#aws)
6. [Setting up Netbeans for development](#netbeans)


## Used Tech<a name="technology"></a>

The project in written in Java and makes use of the following technologies:

- [raspberry pi camera]
- [raspberry pi radio module]
- [radio plugs]
- [wifi]
- [Java]
- [AWS S3]
- [AWS SNS]

## Installation

### Setting up the raspberry camera<a name="camera"></a>

### Setting up the raspberry radio module<a name="radio"></a>

### Setting up the raspberry<a name="raspberry"></a>

- Wifi connection
- wiring pi
- java

### Setting up AWS<a name="aws"></a>

- SNS
- Kinesis
- S3
- IAM

### Setting up Netbeans for development<a name="netbeans"></a>



