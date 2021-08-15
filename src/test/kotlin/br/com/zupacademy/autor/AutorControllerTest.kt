package br.com.zupacademy.autor

import br.com.zupacademy.clienteViaCep.ClienteViaCep
import br.com.zupacademy.clienteViaCep.ViaCepResponse
import br.com.zupacademy.endereco.Endereco
import io.micronaut.http.*
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject

@MicronautTest()
internal class AutorControllerTest(private val autorRepository: AutorRepository){

    @field:Client("/") @field:Inject
    lateinit var httpClient: HttpClient
    @field:Inject
    lateinit var clienteViaCep: ClienteViaCep

    private lateinit var autor: Autor

    @BeforeEach
    fun setUp(){
        val endereco = Endereco("Rua", "Cecília", "SP")
        autor = Autor("Kevin", "kevin@gmail.com", "não sei", endereco, "8")
        autorRepository.save(autor)
    }

    @AfterEach
    fun tearDown(){
        autorRepository.deleteAll()
    }

    @Test
    fun `deve buscar um autor por email`(){
        val response = httpClient.toBlocking().exchange("/autores?email=${autor.email}", AutorResponse::class.java)

        assertEquals(200, response.status.code)
        assertNotNull(response.body)
        assertEquals("kevin@gmail.com", response.body.get().email)
    }

    @Test
    fun `deve cadastrar um autor`(){
        val autorRequest = AutorRequest("Julia", "julia@gmail.com", "sei la", "09175630", "78")
        val request = HttpRequest.POST("/autores", autorRequest)
        val response = ViaCepResponse("Carijós", "Vila linda", "SP", null)

        Mockito.`when`(clienteViaCep.buscaCep(autorRequest.cep)).thenReturn(HttpResponse.ok(response))

        val httpResponse = httpClient.toBlocking().exchange(request, AutorResponse::class.java)

        assertEquals(HttpStatus.CREATED, httpResponse.status)
        assertEquals("Julia", httpResponse.body().nome)
        assertTrue(httpResponse.headers.contains("Location"))
    }

    @MockBean(ClienteViaCep::class)
    fun mockClienteViaCep(): ClienteViaCep{
        return Mockito.mock(ClienteViaCep::class.java)
    }
}