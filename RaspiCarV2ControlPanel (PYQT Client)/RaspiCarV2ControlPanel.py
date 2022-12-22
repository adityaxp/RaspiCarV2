# -*- coding: utf-8 -*-

#  ui file:  '.\RaspiCarV2.ui'


from PyQt5 import QtCore, QtGui, QtWidgets, Qt
from PyQt5.QtWidgets import QMessageBox
from PyQt5.QtGui import QImage, QPixmap
from PyQt5.QtCore import QThread, pyqtSignal
import socket
import time
import sys
import sqlite3
import datetime
import cv2

class Ui_MainWindow(object):

    def setupUi(self, MainWindow):
        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(991, 636)
        MainWindow.setLayoutDirection(QtCore.Qt.LeftToRight)
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.IPAddressTextEdit = QtWidgets.QTextEdit(self.centralwidget)
        self.IPAddressTextEdit.setGeometry(QtCore.QRect(330, 10, 341, 31))
        self.IPAddressTextEdit.setObjectName("IPAddressTextEdit")
        self.IPAddressTextEdit.setAcceptRichText(False)
        self.IPAddressLabel = QtWidgets.QLabel(self.centralwidget)
        self.IPAddressLabel.setGeometry(QtCore.QRect(20, 10, 311, 31))
        font = QtGui.QFont()
        font.setBold(False)
        font.setWeight(50)
        self.IPAddressLabel.setFont(font)
        self.IPAddressLabel.setObjectName("IPAddressLabel")
        self.progressBar = QtWidgets.QProgressBar(self.centralwidget)
        self.progressBar.setGeometry(QtCore.QRect(20, 50, 511, 23))
        self.progressBar.setProperty("value", 0)
        self.progressBar.setObjectName("progressBar")
        self.connectButton = QtWidgets.QPushButton(self.centralwidget)
        self.connectButton.setGeometry(QtCore.QRect(680, 10, 101, 31))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.connectButton.setFont(font)
        self.connectButton.setCheckable(False)
        self.connectButton.setAutoDefault(False)
        self.connectButton.setDefault(False)
        self.connectButton.setFlat(False)
        self.connectButton.setObjectName("connectButton")
        self.saveButton = QtWidgets.QPushButton(self.centralwidget)
        self.saveButton.setGeometry(QtCore.QRect(790, 10, 93, 31))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.saveButton.setFont(font)
        self.saveButton.setObjectName("saveButton")
        self.loadButton = QtWidgets.QPushButton(self.centralwidget)
        self.loadButton.setGeometry(QtCore.QRect(890, 10, 93, 31))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.loadButton.setFont(font)
        self.loadButton.setObjectName("loadButton")
        self.lcdNumber = QtWidgets.QLCDNumber(self.centralwidget)
        self.lcdNumber.setGeometry(QtCore.QRect(860, 530, 121, 51))
        self.lcdNumber.setProperty("intValue", 0)
        self.lcdNumber.setObjectName("lcdNumber")
        self.uptimeLabel = QtWidgets.QLabel(self.centralwidget)
        self.uptimeLabel.setGeometry(QtCore.QRect(740, 530, 121, 51))
        self.uptimeLabel.setObjectName("uptimeLabel")
        self.statusLabel = QtWidgets.QLabel(self.centralwidget)
        self.statusLabel.setGeometry(QtCore.QRect(540, 50, 441, 21))
        self.statusLabel.setObjectName("statusLabel")
        self.horizontalSlider = QtWidgets.QSlider(self.centralwidget)
        self.horizontalSlider.setGeometry(QtCore.QRect(110, 90, 160, 22))
        self.horizontalSlider.setMinimum(0)
        self.horizontalSlider.setValue(100)
        self.horizontalSlider.setTracking(True)
        self.horizontalSlider.setOrientation(QtCore.Qt.Horizontal)
        self.horizontalSlider.setInvertedControls(False)
        self.horizontalSlider.setObjectName("horizontalSlider")
        self.motorSpeedLabel = QtWidgets.QLabel(self.centralwidget)
        self.motorSpeedLabel.setGeometry(QtCore.QRect(20, 80, 81, 41))
        self.motorSpeedLabel.setObjectName("motorSpeedLabel")
        self.label100 = QtWidgets.QLabel(self.centralwidget)
        self.label100.setGeometry(QtCore.QRect(280, 90, 41, 21))
        self.label100.setObjectName("label100")
        self.manualControlLabel = QtWidgets.QLabel(self.centralwidget)
        self.manualControlLabel.setGeometry(QtCore.QRect(90, 130, 221, 31))
        self.manualControlLabel.setLayoutDirection(QtCore.Qt.LeftToRight)
        self.manualControlLabel.setScaledContents(False)
        self.manualControlLabel.setIndent(-1)
        self.manualControlLabel.setObjectName("manualControlLabel")
        self.forwardButton = QtWidgets.QPushButton(self.centralwidget)
        self.forwardButton.setGeometry(QtCore.QRect(120, 170, 41, 41))
        font = QtGui.QFont()
        font.setPointSize(10)
        font.setBold(True)
        font.setWeight(75)
        self.forwardButton.setFont(font)
        self.forwardButton.setObjectName("forwardButton")
        self.leftButton = QtWidgets.QPushButton(self.centralwidget)
        self.leftButton.setGeometry(QtCore.QRect(70, 220, 41, 41))
        font = QtGui.QFont()
        font.setPointSize(10)
        font.setBold(True)
        font.setWeight(75)
        self.leftButton.setFont(font)
        self.leftButton.setObjectName("leftButton")
        self.backButton = QtWidgets.QPushButton(self.centralwidget)
        self.backButton.setGeometry(QtCore.QRect(120, 220, 41, 41))
        font = QtGui.QFont()
        font.setPointSize(10)
        font.setBold(True)
        font.setWeight(75)
        self.backButton.setFont(font)
        self.backButton.setObjectName("backButton")
        self.rightButton = QtWidgets.QPushButton(self.centralwidget)
        self.rightButton.setGeometry(QtCore.QRect(170, 220, 41, 41))
        font = QtGui.QFont()
        font.setPointSize(10)
        font.setBold(True)
        font.setWeight(75)
        self.rightButton.setFont(font)
        self.rightButton.setObjectName("rightButton")
        self.stopButton = QtWidgets.QPushButton(self.centralwidget)
        self.stopButton.setGeometry(QtCore.QRect(70, 280, 151, 28))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.stopButton.setFont(font)
        self.stopButton.setObjectName("stopButton")
        self.logscrollArea = QtWidgets.QLabel(self.centralwidget)
        self.logscrollArea.setGeometry(QtCore.QRect(30, 360, 921, 161))
        #self.logscrollArea.setWidgetResizable(True)
        self.logscrollArea.setObjectName("logscrollArea")
        # self.scrollAreaWidgetContents = QtWidgets.QWidget()
        # self.scrollAreaWidgetContents.setGeometry(QtCore.QRect(0, 0, 919, 159))
        # self.scrollAreaWidgetContents.setObjectName("scrollAreaWidgetContents")
        # self.logscrollArea.setWidget(self.scrollAreaWidgetContents)
        self.logsLabel = QtWidgets.QLabel(self.centralwidget)
        self.logsLabel.setGeometry(QtCore.QRect(30, 330, 201, 20))
        self.logsLabel.setObjectName("logsLabel")
        self.commandComboBox = QtWidgets.QComboBox(self.centralwidget)
        self.commandComboBox.setGeometry(QtCore.QRect(340, 170, 181, 22))
        self.commandComboBox.setObjectName("commandComboBox")
        self.guidedCarControlLabel = QtWidgets.QLabel(self.centralwidget)
        self.guidedCarControlLabel.setGeometry(QtCore.QRect(360, 135, 131, 21))
        self.guidedCarControlLabel.setObjectName("guidedCarControlLabel")
        self.commandListWidget = QtWidgets.QListWidget(self.centralwidget)
        self.commandListWidget.setGeometry(QtCore.QRect(340, 200, 181, 71))
        self.commandListWidget.setObjectName("commandListWidget")
        self.startButton = QtWidgets.QPushButton(self.centralwidget)
        self.startButton.setGeometry(QtCore.QRect(350, 280, 161, 28))
        self.clearButton = QtWidgets.QPushButton(self.centralwidget)
        self.clearButton.setGeometry(QtCore.QRect(350, 320, 161, 28))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.startButton.setFont(font)
        self.clearButton.setFont(font)
        self.startButton.setObjectName("startButton")
        self.clearButton.setObjectName("clearButton")
        self.carStateLabel = QtWidgets.QLabel(self.centralwidget)
        self.carStateLabel.setGeometry(QtCore.QRect(30, 540, 221, 41))
        self.carStateLabel.setObjectName("carStateLabel")
        self.forceStopButton = QtWidgets.QPushButton(self.centralwidget)
        self.forceStopButton.setGeometry(QtCore.QRect(460, 540, 251, 41))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.forceStopButton.setFont(font)
        self.forceStopButton.setObjectName("forceStopButton")
        self.servoAngleTextEdit = QtWidgets.QTextEdit(self.centralwidget)
        self.servoAngleTextEdit.setGeometry(QtCore.QRect(360, 90, 61, 31))
        self.servoAngleTextEdit.setObjectName("servoAngleTextEdit")
        self.servoAngleButton = QtWidgets.QPushButton(self.centralwidget)
        self.servoAngleButton.setGeometry(QtCore.QRect(440, 90, 91, 31))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.servoAngleButton.setFont(font)
        self.servoAngleButton.setObjectName("servoAngleButton")
        self.liveStreamLabel = QtWidgets.QLabel(self.centralwidget)
        self.liveStreamLabel.setGeometry(QtCore.QRect(600, 130, 341, 200))
        self.liveStreamLabel.setObjectName("liveStreamLabel")
        self.cameraStreamLabel = QtWidgets.QLabel(self.centralwidget)
        self.cameraStreamLabel.setGeometry(QtCore.QRect(730, 90, 101, 31))
        self.cameraStreamLabel.setObjectName("cameraStreamLabel")
        self.firebaseButtom = QtWidgets.QPushButton(self.centralwidget)
        self.firebaseButtom.setGeometry(QtCore.QRect(250, 540, 201, 41))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.firebaseButtom.setFont(font)
        self.firebaseButtom.setObjectName("firebaseButtom")
        MainWindow.setCentralWidget(self.centralwidget)
        self.pushButton = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton.setGeometry(QtCore.QRect(602, 350, 341, 28))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.pushButton.setFont(font)
        self.pushButton.setObjectName("pushButton")
        self.menubar = QtWidgets.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 991, 26))
        self.menubar.setObjectName("menubar")
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QtWidgets.QStatusBar(MainWindow)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)
        #MainWindow.addWidget(self.liveStreamLabel)

        self.clientObj = socket.socket()
        self.maxCommandValue = 0
        self.infoFlag = True
        self.serverData = ""


        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

    def retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate
        MainWindow.setWindowTitle(_translate("MainWindow", "RaspiCarV2 Control Panel - Coded By Aditya"))
        self.IPAddressLabel.setText(_translate("MainWindow", "Enter a valid IP address to connect to RaspiCarV2:   "))
        self.connectButton.setText(_translate("MainWindow", "Connect"))
        self.saveButton.setText(_translate("MainWindow", "Save IP"))
        self.loadButton.setText(_translate("MainWindow", "Load IP"))
        self.uptimeLabel.setText(_translate("MainWindow", "Uptime in seconds: "))
        self.statusLabel.setText(_translate("MainWindow", "Connection status:"))
        self.motorSpeedLabel.setText(_translate("MainWindow", "Motor speed: "))
        self.label100.setText(_translate("MainWindow", "100%"))
        self.manualControlLabel.setText(_translate("MainWindow", "Manual Car Control"))
        self.forwardButton.setText(_translate("MainWindow", "↑"))
        self.leftButton.setText(_translate("MainWindow", "←"))
        self.backButton.setText(_translate("MainWindow", "↓"))
        self.rightButton.setText(_translate("MainWindow", "→"))
        self.stopButton.setText(_translate("MainWindow", "Stop"))
        self.logsLabel.setText(_translate("MainWindow", "RaspiCarV2 logs and Sensor data: "))
        self.guidedCarControlLabel.setText(_translate("MainWindow", "   Guided Car Control"))
        self.startButton.setText(_translate("MainWindow", "Start"))
        self.clearButton.setText(_translate("MainWindow", "Clear"))
        self.carStateLabel.setText(_translate("MainWindow", "Car state: "))
        self.forceStopButton.setText(_translate("MainWindow", "Force Stop"))
        self.cameraStreamLabel.setText(_translate("MainWindow", "Camera Stream "))
        self.firebaseButtom.setText(_translate("MainWindow", "FireBase Intialization "))
        self.pushButton.setText(_translate("MainWindow", "Face Recognization "))
        self.servoAngleButton.setText(_translate("MainWindow", "Servo Angle"))


        #Buttons:
        self.connectButton.clicked.connect(self.connectToRaspi)
        self.saveButton.clicked.connect(self.saveIPAddress)
        self.loadButton.clicked.connect(self.loadIPAddress)
        self.forwardButton.clicked.connect(self.forwardButtonMethod)
        self.backButton.clicked.connect(self.backwardButtonMethod)
        self.rightButton.clicked.connect(self.rightButtonMethod)
        self.leftButton.clicked.connect(self.leftButtonMethod)
        self.stopButton.clicked.connect(self.stopButtonMethod)
        self.startButton.clicked.connect(self.startButtonMethod)
        self.clearButton.clicked.connect(self.clearButtonMethod)
        self.forceStopButton.clicked.connect(self.forceStopMethod)
        self.servoAngleButton.clicked.connect(self.servoAngleButtonMethod)
        self.pushButton.clicked.connect(self.faceRecMethod)

        #ComboBoxes:
        self.commandComboBox.addItem("Forward")
        self.commandComboBox.addItem("Left")
        self.commandComboBox.addItem("Right")
        self.commandComboBox.addItem("Reverse")

        indexForward = self.commandComboBox.findText("Forward", QtCore.Qt.MatchFixedString)
        # indexLeft = self.commandComboBox.findText("Left", QtCore.Qt.MatchFixedString)
        # indexRight = self.commandComboBox.findText("Right", QtCore.Qt.MatchFixedString)
        # indexReverse = self.commandComboBox.findText("Reverse", QtCore.Qt.MatchFixedString)
        
        self.commandComboBox.setCurrentIndex(indexForward)


        
    def connectToRaspi(self):
        print("Attempting to connect RaspiCarV2")
        print("Ip Address:", self.IPAddressTextEdit.toPlainText())
        

        self.worker = UptimeTimerThread()
        self.worker.start()
        self.worker.finished.connect(self.eventWorkerFinished)
        self.worker.updateUptime.connect(self.eventUpdateUptime)

        self.worker1 = LiveStreamThread()
        self.worker1.start()
        self.worker1.ImageUpdate.connect(self.ImageUpdateSlot)


        # Dumb Code
        for i in range(0, 101):
            self.progressBar.setProperty("value", i)
            time.sleep(0.005)
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(80)
        font.setPixelSize(18)

        try: 
            self.clientObj.connect((self.IPAddressTextEdit.toPlainText(), 9999))
            # self.clientObj.close()
            if(self.clientObj is not None):
                self.statusLabel.setStyleSheet("background-color: lightgreen")
                self.statusLabel.setFont(font)
                self.statusLabel.setText("Connection status: Connection Successful")
                self.update()
        except:
            self.statusLabel.setStyleSheet("background-color: red")
            self.statusLabel.setFont(font)
            self.statusLabel.setText("Connection status: Connection Unsuccessful")
            self.update()

    def saveIPAddress(self):

        dbConnection = sqlite3.connect('RaspiCarV2.db')
        cursor = dbConnection.cursor()
        createTableIPAddress = """CREATE TABLE IF NOT EXISTS IPAddressTable (IPAddress VARCHAR(255), LastConnectionOn TIMESTAMP);"""
        cursor.execute(createTableIPAddress)
        insertData = """INSERT INTO IPAddressTable VALUES (?, ?);"""
        cursor.execute(insertData, (self.IPAddressTextEdit.toPlainText(), datetime.datetime.now()))
        dbConnection.commit()
        cursor.close()
        dbConnection.close()
        self.messageBox("Save IP Event", "IP address saved!")

    def loadIPAddress(self):

        dbConnection = sqlite3.connect('RaspiCarV2.db')
        cursor = dbConnection.cursor()
        cursor.execute("SELECT * from IPAddressTable")
        fetchedData  = cursor.fetchall()

        for row in fetchedData:
            IPAddress = row[0]
            LastConnectionOn = row[1]
            loadIP = IPAddress
            print("IPAddress: ", IPAddress)
            print("LastConnectedOn: ", LastConnectionOn)

        self.IPAddressTextEdit.setText(loadIP)
        cursor.close()
        dbConnection.close()


    def servoAngleButtonMethod(self):
            angle = self.servoAngleTextEdit.toPlainText()
            self.clientObj.send(bytes(angle, 'utf-8'))
            print('Servo Angle: ', angle)

    
    
    def forwardButtonMethod(self):
        self.clientObj.send(bytes('forward', 'utf-8'))
        print('forward')
        # self.serverData = self.clientObj.recv(1024).decode()
        # print(self.serverData)
        #self.logscrollArea.setText("> " +self.serverData)


        

    def backwardButtonMethod(self):
        self.clientObj.send(bytes('reverse', 'utf-8'))
        print('reverse')

    def rightButtonMethod(self):
        self.clientObj.send(bytes('right', 'utf-8'))
        print('right')

    def leftButtonMethod(self):
        self.clientObj.send(bytes('left', 'utf-8'))
        print('left')

    def stopButtonMethod(self):
        self.clientObj.send(bytes('stop', 'utf-8'))
        print('stop')

    def startButtonMethod(self):
        self.maxCommandValue += 1

        if self.infoFlag:
            self.messageBox("Guided Control Event!", "Only 4 commands can be processed at once")
            self.infoFlag = False


        if self.maxCommandValue > 4 and self.commandListWidget.count() == 4:
            self.messageBox("Guided Control Event!", "Sucessfully fetched all 4 commands")
            carStates = []
            for index in range(self.commandListWidget.count()):
                carStates.append(self.commandListWidget.item(index))
            
            for states in carStates:
                self.clientObj.send(bytes(str(states.text()).lower(), 'utf-8'))
                time.sleep(3)

        else:
            self.commandListWidget.addItem(self.commandComboBox.currentText())

    def clearButtonMethod(self):
        self.commandListWidget.clear()

    def forceStopMethod(self):
        self.clientObj.send(bytes('stop', 'utf-8'))
        self.clientObj.close()
        self.statusLabel.setStyleSheet("background-color: yellow")
        self.statusLabel.setText("Connection status: Connection Closed")

    def faceRecMethod(self):
        self.worker = faceRecThread()
        self.worker.start()




    def messageBox(self, title, message):
        messageBoxWidget = QMessageBox()
        messageBoxWidget.setWindowTitle(title)
        messageBoxWidget.setIcon(QMessageBox.Information)
        messageBoxWidget.setText(message)
        messageBoxWidget.setStandardButtons(QMessageBox.Ok)
        messageBoxWidget.exec_()
        
    
    def eventWorkerFinished(self):
        self.messageBox("Done!", "Work finished")

    def ImageUpdateSlot(self, image):
        self.liveStreamLabel.setPixmap(QPixmap.fromImage(image))

    def eventUpdateUptime(self, value):
        self.lcdNumber.display(value)
        if(value > 601):
            self.messageBox("Battery Event", "Low Battery!")

    def update(self):
        self.statusLabel.adjustSize()
        

class UptimeTimerThread(QThread):  

    updateUptime = pyqtSignal(int)
    def run(self):
        for i in range(0, 601):
            time.sleep(1)
            self.updateUptime.emit(i)



class faceRecThread(QThread):
    def run(self):
        capture = cv2.VideoCapture('http://192.168.137.2:8000/stream.mjpg')
        humanCascade = cv2.CascadeClassifier('haarcascade_face.xml')
        while(True):
            ret, frame = capture.read()

            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            humans = humanCascade.detectMultiScale(gray, 1.9, 1)
    
            for (x,y,w,h) in humans:
                cv2.rectangle(frame,(x,y),(x+w,y+h),(255,0,0),2)
         
            cv2.imshow('frame',frame)
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        capture.release()
        cv2.destroyAllWindows()
        self.stop()

    def stop(self):
        self.ThreadActive = False
        self.quit()





class LiveStreamThread(QThread):

    ImageUpdate = pyqtSignal(QImage)
    def run(self):
        self.ThreadActive = True
        #http://192.168.137.2:8000/stream.mjpg
        capture = cv2.VideoCapture('http://192.168.137.2:8000/stream.mjpg')
        while self.ThreadActive:
            ret, frame = capture.read()
            if ret:
                Image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                FlippedImage = cv2.flip(Image, 1)
                ConvertToQtFormat = QImage(FlippedImage.data, FlippedImage.shape[1], FlippedImage.shape[0], QImage.Format_RGB888)
                Pic = ConvertToQtFormat.scaled(341, 200)
                self.ImageUpdate.emit(Pic)

    def stop(self):
        self.ThreadActive = False
        self.quit()

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    ui = Ui_MainWindow()
    ui.setupUi(MainWindow)
    MainWindow.show()
    sys.exit(app.exec_())
