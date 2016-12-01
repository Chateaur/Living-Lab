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

void setup()
{
    Serial.begin(9600);
    //Definit les pin digi en entrees
    pinMode(DEBIMETRE_1, INPUT);
    pinMode(DEBIMETRE_2, INPUT);
}

void reset_counters(){
  high_count_dbm_1 = 0;
  high_count_dbm_2 = 0;
}
 
void loop() {
  count_dbm_1();
  count_dbm_2();
   
  if (Serial.readString() == "a") {
    //Format de la trame à envoyer : nom_capt:valeur;nom_cappt:valeur#
    char str[200];
    strcpy(str, "DBM_1 : ");
    strcat(str, high_count_dbm_1);
    strcat(str, ";DBM_2 : ");
    strcat(str, high_count_dbm_2);
    strcat(str, "#");
    Serial.println(str);
  }
}

