//Import des librairies
#include <TimeLib.h>

//Definit le header pour la synchro du temps
#define TIME_HEADER  "T"

//Definit les entress
#define DEBIMETRE_1 2
#define DEBIMETRE_2 3

//Stockage du temps de la precedente mesure
unsigned long oldMillis = 0;
//Stockage de l'etat de la précédente mesure (HIGH / LOW)
unsigned long last_pulse_dbm_1 = 0;
unsigned long last_pulse_dbm_2 = 0;

//Represente la mesure numérique (HIGH / LOW)
unsigned long pulse_dmb_1 = 0;
unsigned long pulse_dmb_2 = 0;

//Compteur de pulses
unsigned long high_count_dbm_1 = 0;
unsigned long high_count_dbm_2 = 0;

void count_dbm_1 (){
   //lecture de l'etat
   pulse_dmb_1 = digitalRead(DEBIMETRE_1);

   //detection d'un changement d'etat
   if (pulse_dmb_1 != last_pulse_dbm_1) {
     last_pulse_dbm_1 = pulse_dmb_1;
     //incrementation du compteur
     if (pulse_dmb_1 == HIGH) high_count_dbm_1++;
   } 
}

void count_dbm_2 (){
   //lecture de l'etat
   pulse_dmb_2 = digitalRead(DEBIMETRE_2);

   //detection d'un changement d'etat
   if (pulse_dmb_2 != last_pulse_dbm_2) {
     last_pulse_dbm_2 = pulse_dmb_2;
     //incrementation du compteur
     if (pulse_dmb_2 == HIGH) high_count_dbm_2++;
   } 
}

void reset_counters(){
  high_count_dbm_1 = 0;
  high_count_dbm_2 = 0;
}

String getDigits(int digits){
  String result;  // utility function for digital clock display: prints preceding colon and leading 0
  result = "";
  if(digits < 10){
    result = String("0" + String(digits));
  }else{
    result = String(digits);
  }
  return result;
}

String get_time(){
  String result;
  
  result = String( String(hour()) + ":" + getDigits(minute()) + ":" + getDigits(second()) + "_" + day() + "/" + month() + "/" + year());

  return result;
}

void processSyncMessage(String readed) {
  unsigned long pctime;
  const unsigned long DEFAULT_TIME = 1357041600; // Jan 1 2013

   readed.remove(0,1);

   pctime = readed.toInt();

   if( pctime >= DEFAULT_TIME) { // check the integer is a valid time (greater than Jan 1 2013)
     setTime(pctime); // Sync Arduino clock to the time received on the serial port
  }
}

void setup()
{
    Serial.begin(9600);
    //Definit les pin digi en entrees
    pinMode(DEBIMETRE_1, INPUT);
    pinMode(DEBIMETRE_2, INPUT);
}
 
void loop() {
  
  count_dbm_1();
  count_dbm_2();

  if (Serial.available()) {
    String readed = Serial.readString();

    if (readed == "getAllCapt") {
      //Format de la trame à envoyer : nom_capt:valeur;nom_cappt:valeur#
      String result = "";
      result = String("Time : " + get_time() + ";");
      result = String(result + "DBM_1 : ");
      result = String(result + high_count_dbm_1);
      result = String(result + ";DBM_2 : ");
      result = String(result + high_count_dbm_2);
      result = String(result + ";#");
      Serial.println(result);
    }else if (readed[0] == 'T'){
      processSyncMessage(readed);
      Serial.println(String("Time set to : " + get_time() + "#")); 
    }
  }
}

