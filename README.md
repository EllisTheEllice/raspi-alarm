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
Of course it is possible to run this webapp on the raspberry itself. The installation section covers how to do that.

**Using AWS services imply costs. They are really low, but they will apply. See here for costs of the used services**
- [AWS SNS pricing](https://aws.amazon.com/sns/pricing/)
- [AWS S3 pricing](https://aws.amazon.com/sns/pricing/)

## Version
0.1

## Raspberry version
Tested on
- Model B Rev2

# Table of contents


1. [Used Tech](#technology)
2. [Setting up the raspberry camera](#camera)
3. [Setting up the raspberry radio module](#radio)
4. [Setting up the raspberry](#raspberry)
5. [Setting up the AWS](#aws)
6. [Setting up Netbeans for development](#netbeans)


# Used Tech<a name="technology"></a>

The project in written in Java and makes use of the following technologies:

- [raspberry pi camera](https://www.amazon.com/s/ref=nb_sb_noss_1?url=search-alias%3Daps&field-keywords=raspberry+pi+camera)
- [raspberry pi radio module](https://www.amazon.co.uk/gp/product/B01H2D2RH6/ref=as_li_tl?ie=UTF8&camp=1634&creative=6738&creativeASIN=B01H2D2RH6&linkCode=as2&tag=georgsinstr-21&linkId=b1422bd32f192f82a35523f0646ba708)
- [radio plugs](https://www.amazon.de/gp/product/B002UJKW7K/ref=oh_aui_search_detailpage?ie=UTF8&psc=1)
- [Java](https://www.java.com/)
- [Pi4J](http://pi4j.com/)
- [AWS S3](https://aws.amazon.com/de/s3/)
- [AWS SNS](https://aws.amazon.com/sns/?nc1=h_ls)


# Installation

## Setting up the raspberry camera<a name="camera"></a>
First of all, you have to connect the camera to the raspberry pi. The following pages give instructions on how to do that:
- [Install raspi camera with GUI](https://projects.raspberrypi.org/en/projects/getting-started-with-picamera/4)
- [Install raspi camera without GUI](https://www.raspberrypi.org/documentation/configuration/camera.md)

## Setting up the raspberry radio module<a name="radio"></a>
To switch on lights in an event of suspicious movement, we need radio-controllable plugs and radio sender for raspberry pi. [This page](https://www.instructables.com/id/Super-Simple-Raspberry-Pi-433MHz-Home-Automation/) explains how to set it up on the raspberry (Step 4).

## Setting up the raspberry<a name="raspberry"></a>

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

## Setting up AWS<a name="aws"></a>
Before coming to the required AWS services, you need to have an AWS account. See [here](https://aws.amazon.com/what-is-aws/) for more information on what AWS is and how to create an account.

Once you have an account, log in and do the following.

### SNS
1. Move to the SNS service section

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/sns/1.png)

2. Click on "Create topic"

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/sns/2.PNG)

3. Specify a Topic name and a Display name and click on "Create topic"

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/sns/3.PNG)

4. Now, click on "Create subscription"

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/sns/4.png)

5. Specify the way you want to get informed. In this case, I wanted to get informed by SMS, but email is also possible. 

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/sns/5.png)

Once done, you will receive a message to confirm the subscription. After you have confirmed it, you´re good to go.

### S3

1. Move to the S3 service and click on "Create bucket" to create a bucket. Give it a name and save it.

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/s3/1.png)

2. Make sure you note down the region and the bucketname as they will be required later

### IAM

Now that we have our services in place, we need to create a user with access permissions to the created recssources.

1. Switch to the IAM service, click on "Users" and click "Add user".

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/iam/2.PNG)

2. Specify a username, check the "Programmatic access" checkbox and click "Next: Permissions".

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/iam/3.PNG)


3. Click on "Attach existing policies directly", search for "Amazons3FullAccess" and check it. 

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/iam/4.PNG)


4. Now search for "SNSFullAccess" and check it. Click "Next: Preview"

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/iam/5.PNG)

5. Click on "Create user".

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/iam/6.PNG)

6. Note down your Access key ID and your Secret access key

![alt text](https://github.com/ellistheellice/alarmsystem/blob/master/doc/images/aws/iam/7.PNG)


## Setting up the app
SSH into your raspberry pi and execute the following commands to install apache 
along with PHP:

```sh
$ sudo apt-get update
$ sudo groupadd www-data
$ sudo usermod -a -G www-data www-data
$ sudo nano /etc/apt/sources.list
#Now, add the folowing line to it at save the file:
deb http://mirrordirector.raspbian.org/raspbian/ stretch main contrib non-free rpi
#Edit your preferences to control how apt-get updates its packages
$ sudo nano /etc/apt/preferences
    Package: *
    Pin: release n=jessie
    Pin-Priority: 600
$ sudo apt-get update
#Now install apache and PHP
$ sudo apt-get install -t stretch apache2 -y
$ sudo apt-get install -t stretch php7.0 php7.0-curl php7.0-fpm php7.0-cli php7.0-opcache php7.0-json -y
$ sudo apt-get install -t stretch libapache2-mod-php7.0 -y
```
Now that apache and PHP are installed, you can bring the webapp to it. Copy the contents of the XXX folder to your /var/www/html folder. You should now be able to visit the webapp using http://{raspi-ip}/raspilot

## Setting up Netbeans for development<a name="netbeans"></a>


https://lb.raspberrypi.org/forums/viewtopic.php?t=120072
