package projectapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

// importa as classes necessárias para trabalhar
// com requisições HTTP usando o java.net.http.HttpClient
// que é a API de cliente HTTP introduzida no Java 11.

public class CurrencyApi {
    public static void main(String[] args) {

//      Criando uma requisição HTTP GET para a URL
        HttpRequest request = HttpRequest.newBuilder()
//              O HttpRequest.newBuilder() constroi a requisição
                .GET()
//              É definido o método HTTP como GET com .GET()
                .uri(URI.create("http://economia.awesomeapi.com.br/json/last/USD-BRL,EUR-BRL,BTC-BRL"))
                .timeout(Duration.ofSeconds(10))
//              Se a aplicação não retornar em 3 segundos, é passado o erro
                .build();
//              A chamada .build() finaliza a construção da requisição.

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
//              Caso a requisição tenha mudado de URL, o followRedirects direciona
//              HttpClient é a classe que permite fazer requisições HTTP
                .build();

        try {
//          httpClient.send() retorna uma instância de HttpResponse que contém a resposta da requisição.
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//          Não lançará uma exceção se encontrar campos no JSON que não correspondam
//          aos campos na classe CurrencyInfo

            Map<String, CurrencyInfo> currencyInfoMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, CurrencyInfo>>() {});

            for (Map.Entry<String, CurrencyInfo> entry : currencyInfoMap.entrySet()) {
                String currencyCode = entry.getKey();
                CurrencyInfo currencyInfo = entry.getValue();
//              Variáveis usadas para armazenar temporariamente os valores da chave e do
//              valor de cada entrada

                System.out.println("Currency Code: " + currencyCode);
                System.out.println("Bid Value: " + currencyInfo.getBid());
                System.out.println("Create Date: " + currencyInfo.getCreateDate());
                System.out.println();
            }

//            for (CurrencyInfo currencyInfo : currencyInfoList) {
//                System.out.println("Currency Code: " + currencyInfo.getCode());
//                System.out.println("Bid Value: " + currencyInfo.getBid());
//                System.out.println("Create Date: " + currencyInfo.getCreateDate());
//                System.out.println();  // Adicione uma linha em branco para separar as informações de cada moeda
//
//            }

            System.out.println("Status Code: " + response.statusCode());
        } catch (Exception e) {
            e.printStackTrace();
//          Se algo der errado, o e.printStackTrace()
//          imprimirá a pilha de erros no console para ajudar na depuraç            ão
        }


//              Esta API não suporta xml -> .headers("Accept", "applicarion/xml")
    }
}
