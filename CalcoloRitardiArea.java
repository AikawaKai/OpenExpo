package FunzioniSuOpenData;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.json.JSONArray;


public class CalcoloRitardiArea {

		public static void main(String[] args){
			String id_ref="1ee3ea1b-58e3-48bf-8eeb-36ac313eeaf8";
			String requestString = "http://dati.openexpo2015.it/catalog/api/action/datastore_search?resource_id="+id_ref;
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(requestString);
			int count=0;
			try{
			
				HttpResponse response = client.execute(request);
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String result = "";
				String resline = "";
				Calendar c = Calendar.getInstance();
				Date current = Date.valueOf(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH)); 
				while ((resline = rd.readLine()) != null) 
					result += resline;
				
				//System.out.println(jsonObject.toString());
				if (result != null) 
				{
					JSONObject jsonObject = new JSONObject(result);
					JSONObject resultJson = (JSONObject) jsonObject.get("result");
					JSONArray records = (JSONArray) resultJson.get("records");
					Date temp1,temp2;
					//System.out.printf(records.toString());
					long diffInizioFineLavori;
					long ritardo;
					long den = (24 * 60 * 60 * 1000);
					JSONObject temp;
					for(int i=0;i<records.length();i++){
						temp = (JSONObject) records.get(i);
						temp1 = Date.valueOf((temp.getString("Data Consegna Lavori")).substring(0, 10));
						temp2 = Date.valueOf((temp.getString("nuova data di fine lavori")).substring(0,10));
						diffInizioFineLavori = (long)(temp2.getTime() - temp1.getTime())/ den;
						if(!temp.getString("STATO").equals("ultimato") && temp2.getTime()<current.getTime())
							ritardo = (long)(current.getTime()-temp2.getTime())/ den;
						else
							ritardo = 0;
						System.out.println("Data consegna lavoro: "+temp.getString("Data Consegna Lavori")+" | Data fine lavoro: "+temp.getString("nuova data di fine lavori"));
						System.out.println("STATO: "+temp.getString("STATO"));
						System.out.println("Differenza in giorni: "+diffInizioFineLavori + " | Numero giorni contrattuali: "+temp.getString("numero di giorni contrattuali"));
						System.out.println("Ritardo accumulato: "+ritardo);
						
						System.out.println("----------------------------------");
						Date.valueOf("2014-01-11");
						
					}
				}
	
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
}
