//Definit l entree sur laquelle est le debitmetre
#define DEBIMETRE 2

//Stockage du temps de la precedente mesure
unsigned long oldMillis = 0;
//Stockage de l'etat de la précédente mesure (HIGH / LOW)
unsigned long lastPulse = 0;

//Represente la mesure numérique (HIGH / LOW)
unsigned long pulse = 0;

//Compteur de pulses
unsigned long highCounter = 0;
  
void setup()
{
    Serial.begin(9600);
    //Definit la pin digi en entree
    pinMode(DEBIMETRE, INPUT);
}
 
void loop() {
  //lecture de l'etat
   pulse = digitalRead(DEBIMETRE);

   //detection d'un changement d'etat
   if (pulse != lastPulse) {
     lastPulse = pulse;
     //incrementation du compteur
     if (pulse == HIGH) highCounter++;
   } 
  
   // envoi de la mesure (compteur) et reset toutes les secondes
   if ( millis() - oldMillis >= 1000 )
   {
     oldMillis = millis();
     Serial.println(highCounter);
     highCounter = 0;
   }
}

