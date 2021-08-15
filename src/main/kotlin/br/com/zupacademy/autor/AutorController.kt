package br.com.zupacademy.autor

import br.com.zupacademy.clienteViaCep.ClienteViaCep
import br.com.zupacademy.endereco.Endereco
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Controller("/autores")
class AutorController(
    val autorRepository: AutorRepository,
    val clienteViaCep: ClienteViaCep
){

    @Post
    @Transactional
    fun cadastra(@Valid @Body autorRequest: AutorRequest): HttpResponse<AutorResponse> {
        if(autorRepository.findByEmail(autorRequest.email).isPresent){
            return HttpResponse.badRequest()
        }
        val enderecoResponse = clienteViaCep.buscaCep(autorRequest.cep)
        if(enderecoResponse.body().erro.equals("true")){
            return HttpResponse.notFound()
        }

        val numero = Int
        println(numero)

        val endereco = Endereco(enderecoResponse.body())
        val autor: Autor = autorRequest.toModel(endereco)
        autorRepository.save(autor)

        val uri = UriBuilder.of("/autores/{id}").expand(mutableMapOf(Pair("id", autor.id)))
        return HttpResponse.created<AutorResponse?>(uri).body(AutorResponse(autor))
    }

    @Get
    @Transactional
    fun buscaPorEmailOuTodos(@QueryValue(defaultValue = "") email: String): HttpResponse<Any>{
        if(email.isNotBlank()){
            val possivelAutor = autorRepository.findByEmail(email)
            if(possivelAutor.isEmpty){
                return HttpResponse.notFound()
            }
            return HttpResponse.ok(AutorResponse(possivelAutor.get()))
        }
        val autores = autorRepository.findAll()
        return HttpResponse.ok(autores.map { autor -> AutorResponse(autor) })
    }

    @Put("/{id}")
    @Transactional
    fun atualizaDescricao(@PathVariable id: Long, descricao: String): HttpResponse<AutorResponse>{
        val possivelAutor = autorRepository.findById(id)
        if(possivelAutor.isEmpty){
            return HttpResponse.notFound()
        }

        val autor = possivelAutor.get()
        autor.descricao = descricao
        return HttpResponse.ok(AutorResponse(autor))
    }

    @Delete("/{id}")
    @Transactional
    fun deleta(@PathVariable id: Long): HttpResponse<Any>{
        val possivelAutor = autorRepository.findById(id)
        if(possivelAutor.isEmpty){
            return HttpResponse.notFound()
        }

        autorRepository.deleteById(id)
        return HttpResponse.ok()
    }
}