# raspi-alarm

This is a small home-made alarmsystem based on a raspberry pi. The alarmsystem is 
able to detect motion through a PIR-sensor. If a motion is detected, the raspberry 
starts taking images from the suspicious movements. Those images are then immediately 
backed-up to AWS [S3](https://aws.amazon.com/de/s3/), so that in case the raspberry 
gets destroyed, some images have made it to the backup. In addition to this, the 
raspberry sends a notification to an AWS [SNS](https://aws.amazon.com/sns/?nc1=h_ls) 
topic, so that the owner can get notified in multiple ways (email, SMS, Push etc.).

If you have the possibility to run a web application, here are some good news:
I have included a simple webapp which can run on a webserver and viewed on a 
smartphone. With this webapp, it is possible to start or stop the alarmsystem 
without the need to SSH into your raspberry.
Of course it is possible to run this webapp on the raspberry itself. The installation section covers how to do that.

**Using AWS services implies costs. They are really low, but they will occur. See here for pricing of the used services:**
- [AWS SNS pricing](https://aws.amazon.com/sns/pricing/)
- [AWS S3 pricing](https://aws.amazon.com/sns/pricing/)

## Version
0.1

## Raspberry version
Tested on
- Model B Rev.2

# Table of contents


1. [Used Tech](#technology)
2. [Setting up the raspberry camera](#camera)
3. [Setting up the raspberry](#raspberry)
4. [Setting up the raspberry radio module](#radio)
5. [Setting up the AWS](#aws)
6. [Setting up the AWS](#app)
7. [Setting up Netbeans for development](#netbeans)


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

## Setting up the raspberry<a name="raspberry"></a>

- Wifi connection
- wiring pi

```sh
$ sudo apt-get install git-core 
$ git clone git://git.drogon.net/wiringPi
$ cd wiringPi
$ ./build
```
- pilight



## Setting up the raspberry radio module<a name="radio"></a>
To switch on lights in an event of suspicious movement, we need radio-controllable plugs and radio sender for raspberry pi. [This page](https://www.instructables.com/id/Super-Simple-Raspberry-Pi-433MHz-Home-Automation/) explains how to set it up on the raspberry (Step 4).

## Setting up AWS<a name="aws"></a>
Before coming to the required AWS services, you need to have an AWS account. See [here](https://aws.amazon.com/what-is-aws/) for more information on what AWS is and how to create an account.

Once you have an account, log in and do the following.

### SNS
1. Move to the SNS service section

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/sns/1.png" width="450px"/>

2. Click on "Create topic"

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/sns/2.PNG" width="450px"/>

3. Specify a Topic name and a Display name and click on "Create topic"

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/sns/3.PNG" width="450px"/>

4. Now, click on "Create subscription"

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/sns/4.png" width="450px"/>

5. Specify the way you want to get informed. In this case, I wanted to get informed by SMS, but email is also possible. 

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/sns/5.png" width="450px"/>

Once done, you will receive a message to confirm the subscription. After you have confirmed it, you´re good to go.

---

### S3

1. Move to the S3 service and click on "Create bucket" to create a bucket. Give it a name and save it.

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/s3/1.PNG" width="450px"/>

2. Make sure you note down the region and the bucketname as they will be required later

---

### IAM

Now that we have our services in place, we need to create a user with access permissions to the created recssources.

1. Switch to the IAM service, click on "Users" and click "Add user".

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/iam/2.PNG" width="450px"/>

2. Specify a username, check the "Programmatic access" checkbox and click "Next: Permissions".

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/iam/3.PNG" width="450px"/>


3. Click on "Attach existing policies directly", search for "Amazons3FullAccess" and check it. 

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/iam/4.PNG" width="450px"/>


4. Now search for "SNSFullAccess" and check it. Click "Next: Preview"

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/iam/5.PNG" width="450px"/>

5. Click on "Create user".

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/iam/6.PNG" width="450px"/>

6. Note down your Access key ID and your Secret access key

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/aws/iam/7.PNG" width="450px"/>

---

Now that you have the access key id and the secret access key, you have to place your credentials on the raspberry. Create a credentials file in the pi users home dir:

```sh
$ mkdir /home/pi/.aws
$ nano /home/pi/.aws/credentials
```

The files conten should looks as follows:

```
[default]
aws_access_key_id=<your-key-here>
aws_secret_access_key=<your secret here>
```

## Setting up the app<a name="app"></a>

**Install the alarmsystem**

Some time ago, Oracle announced a JDK which runs on ARM processors,which enables the raspberry to run java programs. Together with the [Pi4J](http://pi4j.com/) 
library, it is possible to directly access GPIO connected devices. To install java, simply execute the following steps:

```sh
$ sudo apt-get install oracle-java8-jdk
```

Before you will be able to execute the alarmsystem project, you have to compile it. Execute the following steps to do so:

```sh
$ sudo apt-get update 
$ apt-get install git
$ git clone https://github.com/EllisTheEllice/raspi-alarm
$ cd raspi-alarm
$ wget http://ftp-stud.hs-esslingen.de/pub/Mirrors/ftp.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
$ tar -xzf apache-maven-3.5.4-bin.tar.gz
$ export PATH=$PATH:/home/pi/raspi-alarm/apache-maven-3.5.4/bin
$ cd alarmsystem
```
Before you compile, you should have a look at the src/main/java/github/ellisthealice/alarmsystem/util/Props.java file so that it fits to your environment.
After that, you can compile:

```sh
 $ mvn clean install
```

Now, let´s create a service. This will be needed to autostart the alarmsystem after errors as well as for the webapp.

```sh
$ cd ../
$ sudo cp linux/alarmsystem.service /etc/systemd/system/
$ sudo systemctl deamon-reload
$ sudo systemctl enable alarmsystem.service
#Now you should be able to start the service
$ sudo systemctl start alarmsystem.service
# You can check the status
$ sudo systemctl status alarmsystem.service
# You can also check the logs
$ ls /var/log/alarmsystem
```

**Install the webapp**

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

#Make the pi user part of the www-data group
$ sudo usermod -a -G www-data pi

#To allow the www-data user to start/stop the service created above, do the following
$ sudo visudo
# Add these lines:
# www-data ALL=(ALL:ALL) NOPASSWD: /bin/systemctl start alarmsystem
# www-data ALL=(ALL:ALL) NOPASSWD: /bin/systemctl stop alarmsystem
```
**Caution**: Editing the sudoers file can harm the system. Please make sure you are aware of possible problems!



Now that apache and PHP are installed, you can bring the webapp to it. Copy the contents of the raspilot folder to your /var/www/html folder.

```sh
$ sudo cp raspilot /var/www/html/
$ sudo chown -R www-data:www-data /var/www/html/rapilot
```
 You should now be able to visit the webapp using http://{raspi-ip}/raspilot

<img src="https://raw.githubusercontent.com/EllisTheEllice/raspi-alarm/master/doc/images/raspilot/1.PNG" width="350px"/>


## Setting up Netbeans for development<a name="netbeans"></a>

If you want to make changes to the project, it is also possible to set it up in a way, that made changes will be automatically transferred to the pi and exectuted remotely. This requires NetBeans as the ide. Have a look [here](https://lb.raspberrypi.org/forums/viewtopic.php?t=120072) to see how to do that.
