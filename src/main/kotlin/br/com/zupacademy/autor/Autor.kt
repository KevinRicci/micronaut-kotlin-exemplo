package br.com.zupacademy.autor

import br.com.zupacademy.endereco.Endereco
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Autor(
    var nome: String,
    @field:Column(unique = true)
    var email: String,
    var descricao: String,
    @field:Embedded val endereco: Endereco,
    val numero: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    val instante: LocalDateTime = LocalDateTime.now()
}
