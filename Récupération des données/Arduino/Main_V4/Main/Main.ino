//Import des librairies
#include <TimeLib.h>

//Definit le header pour la synchro du temps
#define TIME_HEADER  "T"

//Definit les entress
#define DEBIMETRE_1 2
#define DEBIMETRE_2 3

//Stockage du temps de la precedente mesure
unsigned long oldMillis = 0;

//Valeur utilisateur specifiant l'intervalle de temps pour l'envoi des mesures (en ms)
unsigned long deltaMs = 1000;

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

//Fonction permettant l ecriture des minutes ou secondes avec le 0 si < 10
String getDigits(int digits){
  String result;  
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

   if( pctime >= DEFAULT_TIME) { // Vérification de la constante  (supérieure à Jan 1 2013)
     setTime(pctime);
  }
}

String print_message_capt(){
  String result = "";
  unsigned long freq_dbm_1 = (1000 / deltaMs) * high_count_dbm_1;
  unsigned long freq_dbm_2 = (1000 / deltaMs) * high_count_dbm_2;
  
  result = String("Time:" + get_time() + ";");
  result += String("DBM_1:" + String(freq_dbm_1));
  result += String(";DBM_2:" + String(freq_dbm_2));
  result += String("#");

  return result;
}

void setup()
{
    Serial.begin(9600);
    //Definit les pin digi en entrees
    pinMode(DEBIMETRE_1, INPUT);
    pinMode(DEBIMETRE_2, INPUT);

    //Initialisation de la date
    while(timeStatus() == timeNotSet){
      Serial.println("Attente du message de synchronisation date ...");
      while(Serial.available() == 0){}
      
      if (Serial.available()) {
        String readed = Serial.readString();
    
        if (readed[0] == 'T') {
          processSyncMessage(readed);
          Serial.println(String("Date synchronisee a : " + get_time()));
       }
      }
    }
    //setTime(1481463967);

    //Initialisation du delta pour les mesures
    Serial.println("Entrez l'intervalle de temps pour l'envoi des mesures (en ms) :");
    while(Serial.available() == 0){}
    if (Serial.available()) {
      deltaMs = Serial.parseInt();
    }
}
 
void loop() {
  count_dbm_1();
  count_dbm_2();
         
  if ( millis() - oldMillis >= deltaMs ) {
      oldMillis = millis();
      Serial.println(print_message_capt());
      reset_counters();
  }
}

