# En enkel bemanningskalkulator for barnehager
For noen år siden ble bemanningsnormen for barnehager innført, og jeg bestemte meg for å se om jeg klarte å lage en bemanningskalkulator selv, basert på offentlig tilgjengelige data fra Utdanningsdirektoratets statistikkbank. Det viste seg etter hvert at jeg ikke hadde tilgang til all informasjonen jeg trengte, men det var gøy å prøve!

* Først lastet jeg ned XAMPP, hvor jeg hadde tilgang til en MySQL-database. 
* Deretter lastet jeg ned CSV-filer med data fra to statistikker fra Utdanningsdirektoratet. 
* Da jeg var ferdig med å lage programmet mitt, kjørte jeg først LagBhgDatabase, som oppretter en tabell kalt 'barnehage' i MySQL og fyller den med innhold fra de to CSV-filene. OBS! Krever at man har brukernavn og passord til databasen.
* Deretter kunne jeg kjøre hovedprogrammet BemanningsKalkulator, som oppretter et enkelt GUI hvor man kan velge fra en liste med barnehager og se om de oppfyller bemanningsnormen eller ikke. OBS! Krever at man har brukernavn og passord til databasen.


