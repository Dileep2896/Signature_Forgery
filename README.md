# Signature_Forgery

## Project pitch link
https://docs.google.com/document/d/1xD9B3QpmfOjGfjqLKsectsYUD5NO8zbl/edit?usp=sharing&ouid=114207758597838169392&rtpof=true&sd=true

## How to clone

#### Make Sure You Have Git Installed In Your System
##### To Check On Windows, Mac or Linux
##### Just type `git` on your cmd.

#### Use The Following Code To Clone The Project
##### `git clone https://github.com/Dileep2896/Signature_Forgery`

#### >> Open The Folder Using Your Android Studio.
#### >> Wait Till The Dependencies are installed and indexed.
#### >> Run The App On Any Installed Emulator.

## Overview

Our project main focuses on banking security. We have designed a DL model integrated with an android application which can analyze and predicting whether the signature is genuine or forgery.
As we all know the signature forgery is very common in India which can cause a huge loss to customers. Our model will solve this problem efficiently.

## Goals
1.	Generating Datasets: Generating datasets is the first step we have taken to solve this problem. Currently we are using a set of 4000+ signature datasets we have found from [1]. But our application can scan new signatures and generate the dataset which will again train the model.
2.	Creating a Tensorflow Lite Model: As we know using a TensorFlow lite model is the easiest method to integrate our ML in android application. The Keras model is converted into TFLITE model.
3.	Creating an android application: The android application is created to make the user interface easy and fast. Firebase is used as the authentication and for database.
4.	Testing the model: As testing is one of the most important aspect we have tested out model with over 100 signature which gave us a 96.7% testing accuracy.

## Working Methodology
Our model is based on a CNN learning. We have used computer vision to identify and recognize the original and forgery signature. Our model is trained on over 4000+ signature images and gives and accuracy of 94%. We have then integrated out model to a android application which help easy to use the model to scan and analyze the signature.

## Specifications
*	Tensorflow Keras (CNN)
*	Tensorflow Lite (Model)
*	Python (NumPy, Matplotlib, Pandas)
*	Java (Android Application)
*	MP Charts Android
*	Firebase (Authentication & Database)

## Links and other Information:

*	https://github.com/Dileep2896/Signature_Forgery
*	https://cedar.buffalo.edu/NIJ/data/signatures.rar (Dataset Taken From )

## Application Images

Login                      |  Registration             |    Main Page              |      Important Note       |      Result Page          
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/55010518/141482079-7e350852-35ac-4ea3-9740-9e62880ab871.jpg" width="250">  |  <img src="https://user-images.githubusercontent.com/55010518/141482914-77982700-21f4-45d1-b6f0-e73588414022.jpg" width="215"> | <img src="https://user-images.githubusercontent.com/55010518/141482979-0847106d-b0df-49e9-8571-21880fa469db.jpg" width="252"> | <img src="https://user-images.githubusercontent.com/55010518/141483108-adb69027-f8ac-4494-8f37-4218538e5bbe.jpg" width="228"> | <img src="https://user-images.githubusercontent.com/55010518/141483157-9081aef6-46e6-4c63-86cd-7c1355ff46e8.jpg" width="247">

## Team Members

* Dileep Kumar Sharma (Team Leader)
* Harsha S M
* Harsha Vardhan
* Viyandra

## University
### Reva University
