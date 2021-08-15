package br.com.zupacademy.endereco

import br.com.zupacademy.clienteViaCep.ViaCepResponse
import javax.persistence.Embeddable

@Embeddable
class Endereco(
    var logradouro: String,
    var bairro: String,
    var localidade: String
) {
    constructor(endereco: ViaCepResponse) : this(endereco.logradouro!!, endereco.bairro!!, endereco.localidade!!)
}