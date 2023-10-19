package br.com.alura;

import br.com.alura.domain.Abrigo;
import br.com.alura.http.ClientHttpConfiguration;
import br.com.alura.service.AbrigoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbrigoServiceTest {

    private ClientHttpConfiguration client = mock(ClientHttpConfiguration.class); // simula uma instância de ClientHttpConfiguration
    private AbrigoService abrigoService = new AbrigoService(client);
    private HttpResponse<String> response = mock(HttpResponse.class); // simula uma instância de HttpResponse
    private Abrigo abrigo = new Abrigo("Teste", "61981880392", "abrigo_alura@gmail.com");

    @Test
    public void deveVerificarQuandoHaAbrigo() throws IOException, InterruptedException {
        abrigo.setId(0L);
        String expectedAbrigosCadastrados = "Abrigos cadastrados:";
        String expectedIdENome = "0 - Teste";

        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Inicializa um array de bytes para pegar os retornos do Println
        PrintStream printStream = new PrintStream(baos); // Inicializa um PrintStream para escrever no array de bytes
        System.setOut(printStream); // Isso pegará as strings do println

//      Quando este mock for chamado, então retona o abrigo.toString
        when(response.body()).thenReturn("[{"+abrigo.toString()+"}]");

//      Quando este mock for chamado, então retorna o mock de response
        when(client.dispararRequisicaoGet(anyString())).thenReturn(response);

        abrigoService.listarAbrigo();

        String[] lines = baos.toString().split(System.lineSeparator());
        String actualAbrigosCadastrados = lines[0];
        String actualIdENome = lines[1];

//      Verifica se a string de expectedAbrigosCadastrados é igual a actualAbrigosCadastrados
        Assertions.assertEquals(expectedAbrigosCadastrados, actualAbrigosCadastrados);

//      Verifica se a string de expectedIdENome é igual a actualIdENome
        Assertions.assertEquals(expectedIdENome, actualIdENome);
    }

    @Test
    public void deveVerificarQuandoNaoHaAbrigo() throws IOException, InterruptedException {
        abrigo.setId(0L);
        String expected = "Não há abrigos cadastrados";
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Inicializa um array de bytes para pegar o retorno do Println
        PrintStream printStream = new PrintStream(baos); // Inicializa um PrintStream para escrever no array de bytes
        System.setOut(printStream); // Isso pegará as strings do println

//      Quando este mock for chamado, então retona um array vazio, simulando um json sem dados
        when(response.body()).thenReturn("[]");

//      Quando este mock for chamado, então retorna o mock de response
        when(client.dispararRequisicaoGet(anyString())).thenReturn(response);

        abrigoService.listarAbrigo();

        String[] lines = baos.toString().split(System.lineSeparator());
        String actual = lines[0];

//      Verifica se a string de expectedAbrigosCadastrados é igual a actualAbrigosCadastrados
        Assertions.assertEquals(expected, actual);
    }

}
