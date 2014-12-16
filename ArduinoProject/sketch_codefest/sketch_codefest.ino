/*
 *  Author: CodeFest Arduino Team
 *  Date  : November 2014
 *  Modified: December 14 by Luis Silva
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
#include <SoftwareSerial.h>
#include <NewPing.h>

//Proximity Sensor definition
#define TRIGGER_PIN  2  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     3  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 200 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.
unsigned int uS; // Ping time in microseconds
int distance; //distance in cm or m

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); // NewPing setup of pins and maximum distance.

//BlueTooth Definition
#define ARD_RX 10 //Arduino pin tied to BlueTooth serial TX
#define ARD_TX 11 //Arduino pin tied to BlueTooth serial RX

SoftwareSerial btSerial(ARD_RX, ARD_TX);

void setup() {
  Serial.begin(9600);
  Serial.println("Enter AT command:");
  btSerial.begin(9600);
  btSerial.flush();
}

void loop(){
  // Send ping, get ping time in microseconds (uS).
  uS = sonar.ping(); 
  
  // Convert ping time to distance in cm and print result (0 = outside set distance range)
  int distance = (uS / US_ROUNDTRIP_CM);
  
  //write distance in Serial
  Serial.println(distance);
  
  //write distance in BlueTooth
  btSerial.print(distance);
  btSerial.print("#");
  
  //wait before sending the next ping
  delay(100);

}
