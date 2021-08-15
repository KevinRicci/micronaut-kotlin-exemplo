package br.com.zupacademy.clienteViaCep

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client("\${viacep.url}")
interface ClienteViaCep {

    @Get("/{cep}/json")
    fun buscaCep(@PathVariable cep: String): HttpResponse<ViaCepResponse>
}