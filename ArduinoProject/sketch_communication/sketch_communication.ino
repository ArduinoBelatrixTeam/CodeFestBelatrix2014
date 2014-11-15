#include <SoftwareSerial.h>

SoftwareSerial bluetooth(10, 11); // RX, TX

void setup() 
{
  bluetooth.begin(9600);
}

void loop()
{
  bluetooth.println('Hello World');
      delay(1000);
}
