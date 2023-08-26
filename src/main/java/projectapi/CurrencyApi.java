package projectapi;

// Importação das classes necessárias para trabalhar
// com requisições HTTP usando o java.net.http.HttpClient
// que é a API de cliente HTTP introduzida no Java 11.

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class CurrencyApi {
    public static void main(String[] args) {

//      Criação de uma requisição HTTP GET para a URL da API de câmbio
        HttpRequest request = HttpRequest.newBuilder()
//              O HttpRequest.newBuilder() constroi a requisição
                .GET()
//              Define o método HTTP
                .uri(URI.create("http://economia.awesomeapi.com.br/json/last/USD-BRL,EUR-BRL,BTC-BRL"))
                .timeout(Duration.ofSeconds(10))
//              Passa um erro se a aplicação não retornar em 10 segundos
                .build();
//              Chamada que finaliza a construção da requisição

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
//              Caso a requisição tenha mudado de URL, o followRedirects direciona
//              HttpClient é a classe que permite fazer requisições HTTP
                .build();

        try {
//          Envio da requisição e obtenção da resposta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();

//          Configuração do ObjectMapper para ignorar propriedades desconhecidas no JSON
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//          Desserialização do JSON em um Map de moedas para informações
            Map<String, CurrencyInfo> currencyInfoMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, CurrencyInfo>>() {});

//          Iteração sobre o mapa de moedas e exibição das informações
            for (Map.Entry<String, CurrencyInfo> entry : currencyInfoMap.entrySet()) {
                String currencyCode = entry.getKey();
                CurrencyInfo currencyInfo = entry.getValue();
//              Armazenar temporariamente os valores chave-valor de cada entrada

                System.out.println("Currency Code: " + currencyCode);
                System.out.println("Bid Value: " + currencyInfo.getBid());
                System.out.println("Create Date: " + currencyInfo.getCreateDate() + "\n");
            }

//          Exibição do status code da resposta
            System.out.println("Status Code: " + response.statusCode());
        } catch (Exception e) {
            e.printStackTrace();
//          Impressão da pilha de erros em caso de exceção

        }
//Obs: Esta API não suporta xml, se suportasse poderia ter usado-> .headers("Accept", "applicarion/xml")
    }
}
