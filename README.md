# raspi-alarm

This is a small home-made alarmsystem based on a raspberry pi. The alarmsystem is 
able to detect motion through a IR-sensor. If a motion is detected, the raspberry 
starts taking images from the suspicious movements. Those images are then immediately 
backed-up to AWS [S3](https://aws.amazon.com/de/s3/), so that in case the raspberry 
gets destroyed, some images have made it to the backup. In addition to this, the 
raspberry send a notification to an AWS [SNS](https://aws.amazon.com/sns/?nc1=h_ls) 
topic, so that the owner can get notified in multiple ways (email, SMS, Push etc.).

If you have the possibility to run a web application, here are some good news:
I have included a simple webapp which can run on a webserver and viewed by a 
smartphone. With this webapp, it is possible to start or stop the alarmsystem 
without the need to SSH into your raspberry.

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

```sh
$ sudo apt-get install git-core 
$ git clone git://git.drogon.net/wiringPi
$ cd WiringPi
$ ./build
```
- pilight

aws credentials file

Some time ago, Oracle announced a JDK which runs on ARM processors, which enables
the raspberry to run java programs. Together with the [Pi4J](http://pi4j.com/) 
library, it is possible to directly access GPIO connected devices. To install java, 
simply execute the following steps:

```sh
$ sudo apt-get update 
$ sudo apt-get install oracle-java8-jdk
```

### Setting up AWS<a name="aws"></a>

- SNS
- Kinesis
- S3
- IAM

### Setting up the app
SSH into your raspberry pi and execute the following commands to install apache 
along with PHP:

```sh
$ sudo apt-get update
$ sudo groupadd www-data
$ sudo usermod -a -G www-data www-data
$ sudo nano /etc/apt/sources.list
    deb http://mirrordirector.raspbian.org/raspbian/ stretch main contrib non-free rpi
$ sudo nano /etc/apt/preferences
    Package: *
    Pin: release n=jessie
    Pin-Priority: 600
$ sudo apt-get update
$ sudo apt-get install -t stretch apache2 -y
$ sudo apt-get install -t stretch php7.0 php7.0-curl php7.0-gd php7.0-fpm php7.0-cli php7.0-opcache php7.0-json php7.0-mbstring php7.0-xml php7.0-zip php7.0-mysql -y
$ sudo apt-get install -t stretch libapache2-mod-php7.0 -y
```


### Setting up Netbeans for development<a name="netbeans"></a>


https://lb.raspberrypi.org/forums/viewtopic.php?t=120072
