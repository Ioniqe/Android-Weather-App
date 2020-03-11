#include <ESP8266WiFi.h>        // Include the Wi-Fi library
#include <dht.h>

//FIREBASE
#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>

#define FIREBASE_HOST "private"
#define FIREBASE_AUTH "private"

const char* ssid     = "private";         // The SSID (name) of the Wi-Fi network you want to connect to
const char* password = "private";     // The password of the Wi-Fi network

int lumina, temp, rain, humidity, temperature;
dht DHT;//pt senzor temp si umiditate

void(* resetFunc) (void) = 0; //declare reset function @ address 0

int buzzer = D8;
void setup() {
  Serial.begin(115200);         // Start the Serial communication to send messages to the computer
  delay(10);
  Serial.println('\n');

  pinMode(buzzer, OUTPUT);

  WiFi.begin(ssid, password);             // Connect to the network
  Serial.print("Connecting to ");
  Serial.print(ssid); Serial.println(" ...");

  int i = 0;
  while (WiFi.status() != WL_CONNECTED) { // Wait for the Wi-Fi to connect
    delay(1000);
    Serial.print(++i); Serial.print(' ');
    if (i == 13)
      resetFunc();
  }

  Serial.println('\n');
  Serial.println("Connection established!");
  Serial.print("IP address:\t");
  Serial.println(WiFi.localIP());         // Send the IP address of the ESP8266 to the computer

  //FIREBASE
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  delay(500);
}

void loop() {
  //-----------------|senzor lumina|-------------
  lumina = digitalRead(D0);
  Serial.println(lumina);
  //---------------------------------------------

  //-------|senzor temperatura si umiditate|-----
  temp = DHT.read11(2);
  Serial.println(DHT.humidity);
  humidity = DHT.humidity;
  Serial.println(DHT.temperature);
  temperature = DHT.temperature;
  //---------------------------------------------

  //-----------------|senzor ploaie|-------------
  rain = analogRead(A0);
  Serial.println(rain);

  //FIREBASE
  Firebase.setString("values/humidityVal", String(humidity));
  Firebase.setString("values/lightVal", String(lumina));
  Firebase.setString("values/rainVal", String(rain));
  Firebase.setString("values/tempVal", String(temperature));

  Serial.println();
}


