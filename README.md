# OpenExpo
DataExtractor

Questo codice esegue le seguenti cose:

a) estrapola alcune informazioni sui singoli lavori dei lotti, e li inserisce in un .txt

b)genera un'istogramma, calcolato a runtime in base al giorno corrente, degli attuali giorni di ritardo di ogni lotto (l'id presente nel txt generato funge da indice). Questa vista era un qualcosa di non "ricavato" dal cruscotto del portale, così ho provato a generarmela io

c) un grafico a torta dello stato dei lavori (concluso, in corso, in verifica, sospeso), sempre generato runtime, in base al giorno. Questa vista è gia presente nella vista del cruscotto del portale. (E' stata un po' una verifica di correttezza dei dati mostrati...)

NOTA: I file vengono generati nella directory dell'eseguibile
