/*
 *  Author: CodeFest Arduino Team
 *  Date  : November 2014
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

#define RxD 10
#define TxD 11
#define RST 5
#define KEY 4

#define TRIGGER_PIN  2  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     3  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 200 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); // NewPing setup of pins and maximum distance.
SoftwareSerial BTSerial(RxD, TxD);
//char myChar;
//char distance[1]; // Centimeters
unsigned int uS; // Microseconds

void setup()
{
  
  pinMode(RST, OUTPUT);
  pinMode(KEY, OUTPUT);
  digitalWrite(RST, LOW);
  digitalWrite(KEY, HIGH);
  //digitalWrite(KEY, LOW);
  digitalWrite(RST, HIGH);
  
  delay(500);
  
  BTSerial.flush();
  delay(500);
  //BTSerial.begin(38400);
  BTSerial.begin(9600);
  Serial.begin(9600);
  
  delay(100);

}

void loop()
{

  uS = sonar.ping(); // Send ping, get ping time in microseconds (uS).
  Serial.println(uS);
  int distance_calculated = (uS / US_ROUNDTRIP_CM); // Convert ping time to distance in cm and print result (0 = outside set distance range)
  //Serial.println(distance_calculated);
  //String tmp = distance_calculated;
  
  BTSerial.print(distance_calculated);
  Serial.println(distance_calculated);
  
  delay(500);  
  
}

