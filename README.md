# AutoBots — cadastro de clientes

Microsserviço REST para cadastro e manutenção de clientes, documentos, telefones e endereço.

## Requisitos e execução

- Java 17 ou superior
- Maven Wrapper (`automanager/mvnw`)
- Configure credenciais fora do repositório. A senha deve ter pelo menos 12 caracteres.

PowerShell:

```powershell
$env:AUTOMANAGER_USER = "admin"
$env:AUTOMANAGER_PASSWORD = "use-uma-senha-forte-aqui"
cd automanager
./mvnw.cmd spring-boot:run
```

As rotas exigem HTTP Basic (use HTTPS fora do ambiente local). Não há usuário ou senha padrão: o serviço não inicia sem as variáveis de ambiente.

## Rotas

| Método | Rota | Operação |
| --- | --- | --- |
| GET | `/cliente` | Lista clientes |
| GET | `/cliente/{id}` | Consulta cliente |
| POST | `/cliente` | Cadastra cliente (`{"nome":"Nome"}`) |
| PUT | `/cliente/{id}` | Atualiza cliente |
| DELETE | `/cliente/{id}` | Remove cliente |
| GET/POST | `/cliente/{id}/documentos` | Lista/cadastra documentos |
| GET/PUT/DELETE | `/cliente/{id}/documentos/{documentoId}` | Consulta/altera/remove documento |
| GET/POST | `/cliente/{id}/telefones` | Lista/cadastra telefones |
| GET/PUT/DELETE | `/cliente/{id}/telefones/{telefoneId}` | Consulta/altera/remove telefone |
| GET/POST/PUT/DELETE | `/cliente/{id}/endereco` | Consulta/cadastra/altera/remove endereço |

Documentos, telefones e endereço são recursos pertencentes ao cliente; suas operações são delimitadas pelo ID do cliente. Cadastros exigem os campos obrigatórios (documento: `tipo`, `numero`; telefone: `ddd`, `numero`; endereço: `cidade`, `rua`, `numero`).

O H2 em memória é usado para execução local e testes; seus dados são voláteis. Para produção, configure um banco persistente e HTTPS.

## Testes

```powershell
cd automanager
./mvnw.cmd test
```

Os testes cobrem a exigência de autenticação e operações CRUD de clientes, documentos, telefones e endereços.

O projeto inclui o easter egg em sua configuração, respeitando o limite de 30 caracteres definido na atividade.
