//package FunzioniSuOpenData;
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
 org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class OpenDataExtractor {

	public static void main(String[] args) {

		System.out.println("\nSeleziona in che campo fare la ricerca: ");
		System.out.println("Elenco completo delle gare in formato (1)"); // cc16106a-1a65-4c34-af13-cc045d181452
		System.out.println("Composizione delle commissioni giudicatrici (2)"); // c90f1ffb-c315-4f59-b0e3-b0f2f8709127
		System.out.println("Registro delle imprese escluse dalle gare (3)"); // 2ea798cc-1f52-4fc8-a28e-f92a6f409cb8
		System.out.println("Registro delle imprese invitate alle gare (4)"); // a124b6af-ae31-428a-8ac5-bb341feb3c46
		System.out.println("Registro delle imprese che hanno partecipato alle gare (5)");// e58396cf-1145-4cb1-84a4-34311238a0c6
		System.out.println("Anagrafica completa dei contratti per l'acquisizione di beni e servizi (6)"); // aa6a8664-5ef5-43eb-a563-910e25161798
		System.out.println("Fornitori (7)"); // 253c8ac9-8335-4425-84c5-4a90be863c00
		System.out.println("Autorizzazioni subappalti per i lavori  (8)"); // 4f9ba542-5768-4b39-a92a-450c45eabd5d
		System.out.println("Aggiudicazioni delle gare per i lavori (9)"); // 34e298ad-3a99-4feb-9614-b9c4071b9d8e
		System.out.println("Dati cruscotto lavori per area (10)"); // 1ee3ea1b-58e3-48bf-8eeb-36ac313eeaf8
		System.out.println("Dati cruscotto lavori per lotto (11)"); // cbededce-269f-48d2-8c25-2359bf246f42
		int count = 0;
		int scelta;
		String id_ref = "";
		Scanner scanner;

		try {

			do {
				scanner = new Scanner(System.in);
				scelta = scanner.nextInt();
				switch (scelta) {

				case 1:
					id_ref = "cc16106a-1a65-4c34-af13-cc045d181452&q=";
					break;
				case 2:
					id_ref = "c90f1ffb-c315-4f59-b0e3-b0f2f8709127&q=";
					break;
				case 3:
					id_ref = "2ea798cc-1f52-4fc8-a28e-f92a6f409cb8&q=";
					break;
				case 4:
					id_ref = "a124b6af-ae31-428a-8ac5-bb341feb3c46&q=";
					break;
				case 5:
					id_ref = "e58396cf-1145-4cb1-84a4-34311238a0c6&q=";
					break;
				case 6:
					id_ref = "aa6a8664-5ef5-43eb-a563-910e25161798&q=";
					break;
				case 7:
					id_ref = "253c8ac9-8335-4425-84c5-4a90be863c00&q=";
					break;
				case 8:
					id_ref = "4f9ba542-5768-4b39-a92a-450c45eabd5d&q=";
					break;
				case 9:
					id_ref = "34e298ad-3a99-4feb-9614-b9c4071b9d8e&q=";
					break;
				case 10:
					id_ref = "1ee3ea1b-58e3-48bf-8eeb-36ac313eeaf8&q=";
					break;
				case 11:
					id_ref = "cbededce-269f-48d2-8c25-2359bf246f42&q=";
					break;
				default:
					System.out.println("Numero non selezionabile");
					System.out.println("Reinserisci");
					break;

				}
			} while (scelta <= 0 || scelta > 11);

			scanner = new Scanner(System.in);

			System.out.println("Inserisci un parametro di ricerca: ");
			String record = scanner.nextLine();
			String limit = "&limit=10000";
			System.out.println("id di riferimento: " + id_ref);
			System.out.println("___________________________");
			String requestString = "http://dati.openexpo2015.it/catalog/api/action/datastore_search?resource_id="
					+ id_ref + record + limit;
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(requestString);
			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String result = "";
			String resline = "";
			while ((resline = rd.readLine()) != null) {
				result += resline;
			}
			if (result != null) {
				JSONObject jsonObject = new JSONObject(result);
				JSONObject resultJson = (JSONObject) jsonObject.get("result");
				JSONArray resultJsonFields = (JSONArray) resultJson.get("fields");
				ArrayList<TypeFields> type = new ArrayList<TypeFields>();
				TypeFields type1;
				JSONObject temp;

				while (count < resultJsonFields.length()) {

					temp = (JSONObject) resultJsonFields.get(count);
					type1 = new TypeFields(temp.getString("id"),temp.getString("type"));
					type.add(type1);
					count++;
				}

				JSONArray arr = (JSONArray) resultJson.get("records");
				count = 0;

				while (count < arr.length()) {
					System.out.println("Entry numero: " + count);
					temp = (JSONObject) arr.get(count);
					for (TypeFields temp2 : type) {
						System.out.println(temp2.nameType + ": "+ temp.get(temp2.nameType));
					}
					count++;
					System.out.println("--------------------------");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}