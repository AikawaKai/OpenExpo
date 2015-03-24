# OpenExpo
__________CalcoloRitardiLotti_______________

Questo codice esegue le seguenti cose:

a) estrapola alcune informazioni sui singoli lavori dei lotti, e li inserisce in un .txt

b)genera un'istogramma, calcolato a runtime in base al giorno corrente, degli attuali giorni di ritardo di ogni lotto (l'id presente nel txt generato funge da indice). Questa vista era un qualcosa di non "ricavato" dal cruscotto del portale, così ho provato a generarmela io

c) un grafico a torta dello stato dei lavori (concluso, in corso, in verifica, sospeso), sempre generato runtime, in base al giorno. Questa vista è gia presente nella vista del cruscotto del portale. (E' stata un po' una verifica di correttezza dei dati mostrati...)

ATTENZIONE:
Per far funzionare il tutto è necessario includere nel progetto le seguenti librerie:

	- org.json
	- org.jfreechart
	- commons-io-2.4.jar
	- commons-logging-1.1.3
	- jcommon-1.0.23 o superiore
	- httpclient-4.3.4 o superiore
	- httpcore-4.3.2 o superiore

NOTA: I file vengono generati nella directory dell'eseguibile

PER COMPILARE & ESEGUIRE:

javac -cp "Lib/*" CalcoloRitardiLotti.java

ed eseguire

java -cp "Lib/*:."  CalcoloRitardiLotti

Tutto nella dir del progetto!

____________OpenDataExtractor________________

Questo codice esegue una query su uno qualsiasi dei dataset di OpenExpo, in base ad un parametro di ricerca fornito.
Per il corretto funzionamento è necessario includere  la classe fornita "TypeFields.java" presente in repo.

_________CalcoloRitardi.jar_______________

Jar eseguibile di CalcoloRitardiLotti.

Per eseguire eseguire il comando:
java -jar CalcoloRitardi.jar

