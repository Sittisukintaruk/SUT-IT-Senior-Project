#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>


#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include "HX711.h"
#include <Wire.h> 

float calibration_factor = 1018385.00; 
#define zero_factor 10919416
#define DOUT  D2
#define CLK   D3
#define DEC_POINT  2
#define FIREBASE_HOST "recan-7c57c.firebaseio.com"
#define FIREBASE_AUTH "YFhWTAJyJUbbx6z9SnnAZJCQ8DerZFIH0Bf2Bzv1"


float offset=0;
float get_units_kg();
const char* ssid     = "Beer";    // SSID Wifi
const char* password = "12345678";   // Password Wifi
const String deviceNumber = "Weight";
 String data;

HX711 scale(DOUT, CLK);

void setup() {
  Serial.begin(115200);
  Serial.println("Load Cell");
  scale.set_scale(calibration_factor); 
  scale.set_offset(zero_factor); 
   WiFi.begin(ssid, password);
   
   while (WiFi.status() != WL_CONNECTED) 
   {
    delay(500);
    Serial.print(".");  
   
   }

    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

}

void loop() { 
// Use WiFiClient class to create TCP connections

Serial.print("Reading: ");
 data = String(get_units_kg()+offset, DEC_POINT);
  Serial.print(data);
  Serial.println(" kg");


  Firebase.setString(deviceNumber + "/weight", data);
  
 bool isFailed = Firebase.failed();
if(isFailed) {
  Serial.println(Firebase.error());
}
 




}



float get_units_kg() 

{
  return(scale.get_units()*0.453592);
}
