import RPi.GPIO as GPIO
import time
import io
import picamera
import random
import logging
import os
import socketserver
from threading import Condition
from http import server
from threading import *
from http import client
import socket
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

GlobalIRvalue = 1

"""
class IRSensorThread(Thread):
        def run(self):
                try:
                        IRvalue = GPIO.input(22)
                        if(IRvalue == 1):
                                GlobalIRvalue = 1
                                 
                        else:
                                GlobalIRvalue = 0
                                print("Obstacle!")

                except:
                        print("IR Error!")
"""

def servoControl(angle, stop=False):
        servo1 = GPIO.PWM(16, 50)
        servo1.start(0)
        if stop:
                servo1.stop()
        else:
                servo1.ChangeDutyCycle(2+(angle/18))
                time.sleep(0.5)
                servo1.ChangeDutyCycle(0)
                
        

def IRSensorValue(online=False):
        IRvalue = GPIO.input(22)
        if(IRvalue == 1):
                GlobalIRvalue = 1
                GPIO.output(7, False)
        else:
                GlobalIRvalue = 0
                GPIO.output(7, True)
                print("Obstacle!")
                stop(1)
        if online:
                logsAndSensorData('IRSensor> IR value: '+str(GlobalIRvalue))
                

                
def ultraSonicSensor(online=False):
    print("Distance measurement in progress")
    GPIO.output(31 ,False)
    print("Waiting for sensor to settle")
    time.sleep(0.2)
    GPIO.output(31 ,True)
    time.sleep(0.00001)
    GPIO.output(31 ,False)
    while GPIO.input(32)==0:
        pulse_start=time.time()
    while GPIO.input(32)==1:
        pulse_end=time.time()
    pulse_duration=pulse_end-pulse_start
    distance=pulse_duration*17150
    distance=round(distance,2)
    print("Distance:",distance,"cm")
    if(distance <= 10):
        print("Object close!")
        GPIO.output(7, True)
        stop(1)
    else:
        GPIO.output(7, False)
    if online:
        logsAndSensorData('UltraSonic Sensor> Distance:'+str(distance)+'cm')

    time.sleep(2)

def init():
	GPIO.setmode(GPIO.BOARD)

	GPIO.setup(7, GPIO.OUT)
	GPIO.setup(11, GPIO.OUT)
	GPIO.setup(12, GPIO.OUT)
	GPIO.setup(13, GPIO.OUT)
	GPIO.setup(15, GPIO.OUT)
	GPIO.setup(35, GPIO.OUT)
	GPIO.setup(37, GPIO.OUT)
	GPIO.setup(33, GPIO.OUT)
	GPIO.setup(36, GPIO.OUT)
	GPIO.setup(22, GPIO.IN)
	GPIO.setup(31,GPIO.OUT)
	GPIO.setup(16, GPIO.OUT)
	GPIO.setup(32,GPIO.IN)
	GPIO.output(7, False)
	# speed1 = GPIO.PWM(13, 1000)
	# speed2 = GPIO.PWM(15, 1000)
	# speed3 = GPIO.PWM(35, 1000)
	# speed4 = GPIO.PWM(36, 1000)
	# speed5 = GPIO.PWM(37, 1000)
	# speed6 = GPIO.PWM(11, 1000)
	# speed7 = GPIO.PWM(12, 1000)
	# speed8 = GPIO.PWM(33, 1000)

	# speed1.start(0)
	# speed2.start(0)
	# speed3.start(0)
	# speed4.start(0)
	# speed5.start(0)
	# speed6.start(0)
	# speed7.start(0)
	# speed8.start(0)

	
	
        
def forward(tf):
	GPIO.output(11, True)
	GPIO.output(12, False)
	GPIO.output(13, False)
	GPIO.output(15, True)
	GPIO.output(35, True)
	GPIO.output(37, False)
	GPIO.output(33, True)
	GPIO.output(36, False)
	
	time.sleep(tf)

def rightTurn(tf):
	
	GPIO.output(13, True)
	GPIO.output(15, False)
	GPIO.output(35, True)
	GPIO.output(36, False)
	GPIO.output(11, False)
	GPIO.output(12, False)
	GPIO.output(37, False)
        
	GPIO.output(33, False)
	time.sleep(tf)


def leftTurn(tf):
	GPIO.output(37, False)
	GPIO.output(11, False)
	GPIO.output(12, True)
	GPIO.output(33, True)
        
	GPIO.output(13, False)
	GPIO.output(15, False)
	GPIO.output(35, False)
	GPIO.output(36, False)

	
	time.sleep(tf)


def backward(tf):
	GPIO.output(37, True)
	GPIO.output(11, False)
	GPIO.output(12, True)
	GPIO.output(13, True)
	GPIO.output(15, False)
	GPIO.output(33, False)
	GPIO.output(35, False)
	GPIO.output(36, True)
	time.sleep(tf)

def stop(tf):
	GPIO.output(37, False)
	GPIO.output(11, False)
	GPIO.output(12, False)
	GPIO.output(13, False)
	GPIO.output(15, False)
	GPIO.output(33, False)
	GPIO.output(35, False)
	GPIO.output(36, False)
	time.sleep(tf)

def PyQtClient():
    while True:
        c, addr = s.accept()
        clientData = c.recv(1024).decode()
        print("Connected with", addr, clientData)
        while clientData:
                IRSensorValue()
                ultraSonicSensor()
                clientData = c.recv(1024).decode()
                print("Connected with", addr, clientData)
                if GlobalIRvalue != 0:
                        if(clientData == 'forward'):
                                forward(0.5)
                                stop(0.5)
                        elif(clientData == 'reverse'):
                                backward(0.5)
                                stop(0.5)
                        elif(clientData == 'right'):
                                rightTurn(0.00000005)
                                servoControl(30)
                                stop(0.5)
                                servoControl(0)
                        elif(clientData == 'left'):
                                leftTurn(5)
                                servoControl(-10)
                                stop(0.5)
                                servoControl(0)
                        elif(clientData == 'stop'):
                                stop(0.5)
                                servoControl(0)
                                GPIO.cleanup()
                                exit(0)
                        elif(int(clientData) >= int('-20') or int(clientData) <= int('50')):
                                #servoControl(int(clientData))
                                print("servo")
                        else:
                                clientData = 'stop'
                                stop(0.5)
                                servoControl(10)
                elif GlobalIRvalue == 1:
                        print('Obstacle Ahead cant procced!')
                        stop(1)
                else:
                        print('Null')
                        
				


def FireBaseClient():
        while True:
                ref = db.reference('RaspiCarV2MovementState')
                print('Current Movement state: ',ref.get())
                ref1 = db.reference('RaspiCarV2ServoAngle')
                print('Current Servo Angle: ', ref1.get())
                servoRef = ref1.get_if_changed(ref1.get())
                print(servoRef)
                ref2 = db.reference('RaspiCarV2BuzzerState')
                IRSensorValue(True)
                ultraSonicSensor(True)
                currentStateRef = ref.get()
                if(currentStateRef == 'W'):
                        forward(0.5)
                        logsAndSensorData('Motor Driver 1> GPIO 12 & GPIO 13: HIGH \nMotor Driver 2> GPIO 33 & GPIO 35: HIGH')
                        stop(0.5)
                        ref.set('Still')
                elif(currentStateRef == 'RV'):
                        backward(0.5)
                        logsAndSensorData('Motor Driver 1> GPIO 11 & GPIO 15: HIGH \nMotor Driver 2> GPIO 36 & GPIO 37: HIGH')
                        stop(0.5)
                        ref.set('Still')
                elif(currentStateRef == 'L'):
                        leftTurn(0.5)
                        logsAndSensorData('Motor Driver 1> GPIO 11 & GPIO 12: HIGH \nMotor Driver 2> GPIO 33 & GPIO 37: HIGH')
                        stop(0.5)
                        ref.set('Still')
                elif(currentStateRef == 'R'):
                        rightTurn(0.5)
                        logsAndSensorData('Motor Driver 1> GPIO 13 & GPIO 15: HIGH \nMotor Driver 2> GPIO 35 & GPIO 36: HIGH')
                        stop(0.5)
                        ref.set('Still')
                elif(currentStateRef == 'SP'):
                        randomNo = random.randint(0,1)
                        if(randomNo == 0):
                                rightTurn(2)
                        else:
                                leftTurn(2)
                        logsAndSensorData('Spin')
                        stop(0.5)
                        ref.set('Still')
                elif int(servoRef[1])>= int('-20') or int(servoRef[1]) <= int('50'):
                        print("Servo")
                        #servoControl(int(servoRef[1]))
                        
                elif(ref2.get() == 1):
                        GPIO.output(7, True)
                        time.sleep(2)
                        ref2.set(0)
                elif(currentStateRef == 'FS'):
                        stop(1)
                        statusRef.set("offline")
                        os.system('poweroff')
                elif(currentStateRef == "Still"):
                        stop(0.5)
                else:
                        stop(1)
                                
                                
                        
                
                        
                        
def logsAndSensorData(message):
        logRef = db.reference('RaspiCarV2LogsAndSensorData')
        logRef.set(message)
        time.sleep(0.5)
        


        

            
init()
print('-' * 50)
print('RaspiCarV2 Control Shell - By Aditya')
print('-' * 50)
print('1. Car control over Local network (Socket)')
print('2. Car control over Internet (FireBase)')
print('3. Terminate Code')
print('-' * 50)
choice = input('> Enter your choice: ')
print('-' * 50)

invalidChoiceCount = 0

while invalidChoiceCount < 3:
        if choice == '1':
                try:
                        s = socket.socket()
                        print("Server Socket Created!")
                        s.bind(('192.168.1.23', 9999))
                        s.listen()
                        print('Waiting for the connection')
                        PyQtClient()
                finally:
                        GPIO.cleanup()
                
                 
        elif choice == '2':
                print('Please make sure that you are connected to Internet')
                input('> Ok!')
                try:
                        cred = credentials.Certificate("raspicarv2-firebase-adminsdk-17vj0-daf4216150.json")
                        firebase_admin.initialize_app(cred, {"databaseURL" : "https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app"})
                        statusRef = db.reference('RaspiCarV2State')
                        statusRef.set("online")
                        logsAndSensorData('online')
                        time.sleep(1)
                        FireBaseClient()
                finally:
                        GPIO.cleanup()


        elif choice == '3':
                invalidChoiceCount = 3
                GPIO.cleanup()


        else:
                invalidChoiceCount += 1


        
                
                
                
        



        
        








    
    
    

	

    
#c.send(bytes('Motor Driver 1> GPIO 12 & GPIO 13: HIGH \nMotor Driver 2> GPIO 33 & GPIO 35: HIGH', 'utf-8'))
#c.send(bytes('Motor Driver 1> GPIO 11 & GPIO 15: HIGH \nMotor Driver 2> GPIO 36 & GPIO 37: HIGH', 'utf-8'))
#c.send(bytes('Motor Driver 1> GPIO 13 & GPIO 15: HIGH \nMotor Driver 2> GPIO 35 & GPIO 36: HIGH', 'utf-8'))
#c.send(bytes('Motor Driver 1> GPIO 11 & GPIO 12: HIGH \nMotor Driver 2> GPIO 33 & GPIO 37: HIGH', 'utf-8'))
#c.send(bytes('Motor Driver 1 & Motor Driver 2> ALL GPIO : LOW', 'utf-8'))
    
    



