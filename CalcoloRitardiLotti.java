package FunzioniSuOpenData;
/*		   ************Copyright**************


    This software is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This software is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

                   ****************END****************
*/


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.json.JSONArray;
import org.json.JSONObject;

public class CalcoloRitardiLotti {

	public static void main(String[] args){
		String id_ref="cbededce-269f-48d2-8c25-2359bf246f42";
		String requestString = "http://dati.openexpo2015.it/catalog/api/action/datastore_search?resource_id="+id_ref;
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(requestString);
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
				
				DefaultCategoryDataset cdata = new DefaultCategoryDataset();
			    String partialQuery;
			    DefaultPieDataset data = new DefaultPieDataset();
			    
			    String totalQuery="";
			    int countSospesi=0;
			    int countConclusi=0;
			    int countVerifica=0;
			    int countInCorso=0;
			    int countCollaudo=0;
			    String stato;
				for(int i=0;i<records.length();i++){
					temp = (JSONObject) records.get(i);
					temp1 = Date.valueOf((temp.getString("Data Consegna Lavori")).substring(0, 10));
					temp2 = Date.valueOf((temp.getString("Data Fine lavori")).substring(0,10));
					diffInizioFineLavori = (long)(temp2.getTime() - temp1.getTime())/ den;
					stato = temp.getString("STATO");
					if(stato.equals("Concluso"))
						countConclusi++;
					else if(stato.equals("In corso"))
						countInCorso++;
					else if(stato.contains("Verifiche"))
						countVerifica++;
					else if(stato.contains("Collaudo sospeso") || stato.contains("sospeso"))
						countSospesi++;
					else
						countCollaudo++;
					
					
					if(!temp.getString("STATO").equals("Concluso") && temp2.getTime()<current.getTime())
						ritardo = (long)(current.getTime()-temp2.getTime())/ den;
					else
						ritardo = 0;
					
					cdata.setValue(ritardo, String.valueOf(i+1), String.valueOf(i+1));
					System.out.println("Opera: "+temp.getString("Oggetto del lotto") + " | id: "+temp.getInt("_id"));
					System.out.println("Data consegna lavoro: "+temp.getString("Data Consegna Lavori")+" | Data fine lavoro: "+temp.getString("Data Fine lavori"));
					System.out.println("STATO: "+temp.getString("STATO"));
					System.out.println("Differenza in giorni: "+diffInizioFineLavori + " | Numero giorni contrattuali: "+temp.getString("numero di giorni contrattuali"));
					System.out.println("Ritardo accumulato: "+ritardo);
					
					System.out.println("----------------------------------");
					
					partialQuery ="\nid: "+temp.getInt("_id")
							+ "\nOpera:"+temp.getString("Oggetto del lotto") + "\n"
							+ "Data consegna lavoro: "
							+ temp.getString("Data Consegna Lavori")
							+ "Data fine lavoro: "
							+ temp.getString("Data Fine lavori") + "\n"
							+ "STATO: " + temp.getString("STATO") + "\n"
							+ "Differenza in giorni: " + diffInizioFineLavori
							+ " - Numero giorni contrattuali: "
							+ temp.getString("numero di giorni contrattuali")
							+ "\n" + "Ritardo accumulato: " + ritardo+"\n"
							+ "----------------------------------\n";
					totalQuery = totalQuery+partialQuery;
					
				}
	
				JFreeChart chart1 = ChartFactory.createBarChart3D("RITARDI AL "+current, "Id lotto", "ritardo(in giorni)", cdata);
				ChartRenderingInfo info = null;
				ChartUtilities.saveChartAsPNG(new File(System.getProperty("user.dir")+"/istogramma"+current+".png"), chart1, 1500, 1500, info, true, 10);
				FileUtils.writeStringToFile(new File(current+"_1.txt"), totalQuery);

				data.setValue("Conclusi: " +countConclusi, countConclusi);
				data.setValue("Sospeso: " +countSospesi, countSospesi);
				data.setValue("In Corso: " +countInCorso, countInCorso);
				data.setValue("Verifica: " +countVerifica, countVerifica);
				data.setValue("Collaudo: " +countCollaudo, countCollaudo);
			    JFreeChart chart2 = ChartFactory.createPieChart3D("Statistiche del "+current, data, true, true, true);
			    ChartUtilities.saveChartAsPNG(new File(System.getProperty("user.dir")+"/pie"+current+".png"), chart2, 800, 450); 

			}

		}catch(Exception e){
			e.printStackTrace();
		}	
	}
}

