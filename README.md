[![CI](https://github.com/paulovieirajr/estapar/actions/workflows/build.yml/badge.svg)](https://github.com/paulovieirajr/estapar/actions/workflows/build.yml)
[![codecov](https://codecov.io/gh/paulovieirajr/estapar/branch/main/graph/badge.svg)](https://codecov.io/gh/paulovieirajr/estapar)
![Java](https://img.shields.io/badge/Java-21-green?style=plastic&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-green?logo=springboot&logoColor=%23E4D00A)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-gray?logo=spring&logoColor=%23E4D00A)
![JUnit](https://img.shields.io/badge/JUnit-5-green?style=plastic&)
![Maven](https://img.shields.io/badge/Apache_Maven-red?logo=apachemaven&logoColor=%23FFF)
![MySQL](https://shields.io/badge/MySQL-lightgrey?logo=mysql&style=plastic&logoColor=white&labelColor=blue)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?logo=github-actions&logoColor=white)

# Desafio Estapar Estacionamentos/CTC

Este é um desafio proposto para desenvolver um sistema de gestão de estacionamentos onde a aplicação vai receber requests via webhook através de um container disponibilizado pela Estapar. A instrução fornecida para o desafio
vai ficar após a estrutura de diretórios que montei para esse desafio.

A aplicação contém uma classe "starter" que faz a leitura do json no container e popular a base de dados.

Para rodar a aplicação localmente, primeiro execute o container fornecido pela Estapar.

⚠️ Eu gostaria de ter incluído no docker-compose.yml, mas tive alguns problemas para executá-lo em modo host como na descrição do desafio. Apenas com o seguinte comando é que consegui:

```bash
docker run -d --name estapar_simulator -p 3000:3000 --add-host=localhost:host-gateway cfontes0estapar/garage-sim:1.0.0
```
Pelo que pesquisei, acredito que no Linux o comando fornecido na descrição funcione.


basta fazer o clone do projeto e executá-la preferencialmente no Intellij, ou executar o comando na raiz do projeto:

> Windows(CMD)
```bash
mvnw spring-boot:run
```

> Linux/MacOS
```bash
./mvnw spring-boot:run
```

Como a aplicação já conta com a extensão do Docker Compose, ao rodar a aplicação, o container do MySQL será inicializado e populado. Por padrão, configurei a remoção do volume do container ao parar a aplicação.

Utilizei Java 21 com Spring Boot 3, um container do MySQL via docker-compose.yml, Spring Boot Actuator, Spring Data JPA, Liquibase. Para testes utilizei o JUnit5, AssertJ, Mockito, features do Spring para testes e H2 para substituir o MySQL em testes de repositório.

A aplicação contém testes na camada de controller, serviço e repositório.

Também criei uma action que verifica build, testes, coverage e vulnerabilidade das dependências.

A seguinte estrutura foi utilizada:

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── io
│   │   │       └── github
│   │   │           └── paulovieirajr
│   │   │               └── estapar
│   │   │                   ├── EstaparApplication.java
│   │   │                   ├── adapter
│   │   │                   │   ├── controller
│   │   │                   │   │   ├── revenue
│   │   │                   │   │   │   ├── RevenueController.java
│   │   │                   │   │   │   └── RevenueSwagger.java
│   │   │                   │   │   ├── spot
│   │   │                   │   │   │   ├── SpotStatusController.java
│   │   │                   │   │   │   └── SpotSwagger.java
│   │   │                   │   │   ├── vehicle
│   │   │                   │   │   │   ├── LicensePlateController.java
│   │   │                   │   │   │   └── LicensePlateSwagger.java
│   │   │                   │   │   └── webhook
│   │   │                   │   │       ├── WebhookController.java
│   │   │                   │   │       └── WebhookSwagger.java
│   │   │                   │   ├── dto
│   │   │                   │   │   ├── revenue
│   │   │                   │   │   │   ├── RevenueRequestDto.java
│   │   │                   │   │   │   └── RevenueResponseDto.java
│   │   │                   │   │   ├── simulator
│   │   │                   │   │   │   ├── EstaparDataSimulatorDto.java
│   │   │                   │   │   │   ├── SectorDto.java
│   │   │                   │   │   │   └── SpotDto.java
│   │   │                   │   │   ├── spot
│   │   │                   │   │   │   ├── SpotStatusRequestDto.java
│   │   │                   │   │   │   └── SpotStatusResponseDto.java
│   │   │                   │   │   ├── vehicle
│   │   │                   │   │   │   ├── LicensePlateRequestDto.java
│   │   │                   │   │   │   └── LicensePlateResponseDto.java
│   │   │                   │   │   └── webhook
│   │   │                   │   │       ├── enums
│   │   │                   │   │       │   └── EventType.java
│   │   │                   │   │       └── event
│   │   │                   │   │           ├── WebhookEventDto.java
│   │   │                   │   │           ├── WebhookEventEntryDto.java
│   │   │                   │   │           ├── WebhookEventExitDto.java
│   │   │                   │   │           ├── WebhookEventParkedDto.java
│   │   │                   │   │           └── WebhookEventResponseDto.java
│   │   │                   │   └── persistence
│   │   │                   │       ├── entity
│   │   │                   │       │   ├── GarageEntity.java
│   │   │                   │       │   ├── RevenueEntity.java
│   │   │                   │       │   ├── SectorEntity.java
│   │   │                   │       │   ├── SpotEntity.java
│   │   │                   │       │   ├── TicketEntity.java
│   │   │                   │       │   ├── VehicleEntity.java
│   │   │                   │       │   └── VehicleEventEntity.java
│   │   │                   │       └── repository
│   │   │                   │           ├── GarageRepository.java
│   │   │                   │           ├── RevenueRepository.java
│   │   │                   │           ├── SectorRepository.java
│   │   │                   │           ├── SpotRepository.java
│   │   │                   │           ├── TicketRepository.java
│   │   │                   │           ├── VehicleEventRepository.java
│   │   │                   │           └── VehicleRepository.java
│   │   │                   ├── config
│   │   │                   │   └── RestClientConfig.java
│   │   │                   ├── service
│   │   │                   │   ├── exception
│   │   │                   │   │   ├── revenue
│   │   │                   │   │   │   ├── RevenueAlreadyExistsException.java
│   │   │                   │   │   │   └── RevenueNotFoundException.java
│   │   │                   │   │   ├── sector
│   │   │                   │   │   │   ├── SectorAlreadyFullException.java
│   │   │                   │   │   │   └── SectorNotFoundException.java
│   │   │                   │   │   ├── spot
│   │   │                   │   │   │   ├── SpotAlreadyOccupiedException.java
│   │   │                   │   │   │   └── SpotNotFoundException.java
│   │   │                   │   │   ├── ticket
│   │   │                   │   │   │   └── TicketNotFoundException.java
│   │   │                   │   │   └── vehicle
│   │   │                   │   │       ├── VechicleNotFoundException.java
│   │   │                   │   │       └── VehicleAlreadyExistsException.java
│   │   │                   │   ├── revenue
│   │   │                   │   │   └── RevenueService.java
│   │   │                   │   ├── sector
│   │   │                   │   │   └── SectorService.java
│   │   │                   │   ├── spot
│   │   │                   │   │   └── SpotService.java
│   │   │                   │   └── vehicle
│   │   │                   │       └── VehicleService.java
│   │   │                   └── startup
│   │   │                       └── EstaparGarageSimulatorInitializer.java

```

<details>
 <summary>Descrição do desafio - Clique aqui para expandir</summary>

 # Teste Desenvolvedor Java/Kotlin Backend

Este é o teste para Desevolvedor Java/Kotlin da Estapar.

O objetivo é criar um sistema de gestão de estacionamentos, que controla o número de vagas em aberto,
entrada, saida e faturameto do setor.

O teste não precisa estar 100% completo, iremos avaliar até o ponto onde você conseguiu chegar.

## o Projeto
Criar um projeto usando git, em algum repositório da sua preferência (github, bitbucket, etc) e nos enviar o link ao
término do teste.
Favor realizar commits como faria no seu dia a dia, iaao é parte da avaliação para analisarmos como você se organiza e
documenta o seu trabalho.

## Requisitos técnicos
* Utilizar Java ou Kotlin
* Frameworks Spring ou Micronaut
* Utilize banco de dados MySQL ou Postgres
* Utilize git

### Simulador de dados de garagem, entrada e saida de veículos (Provido pela Estapar, para facilitar o desenvolvimento)
Abaixo segue a instrução para rodar o simulador de entrada e saida de veículos e dados de garagem.
Ele é iniciado dentro de um container,
após a inicialização faça uma chamada para o endpoint ```GET``` ```/garage``` e você recebe a configuração da
garagem para o setup inicial do sistema,  
após alguns segundos desta chamada o simulador começa e enviar eventos de entradas e saidas de veículos da garagem.

O Simulador pode ser inicializado com o comando:
```bash

 docker run -d --network="host" cfontes0estapar/garage-sim:1.0.0

```

## Requisitos Funcionais

O sistema deve importar os dados geolocalizados de vagas e setores e armazenar em um banco de dados.
Vagas podem ter metadados associados como preço, datas, horário de funcionamento e duração da estádia (Documentados
abaixo na API do simulador).

Crie um sistema que gerência o uso e faturamento deste setor de estacionamento, não precisamos de UI,
somete o backend e api REST.

Os dados da garagem devem ser obtidos pelo endpoint do simulador ```GET``` ```/garage```  no start da sua aplicação
(descrito abaixo na API), após isso a sua aplicação deve estar start apta a receber por webhook entradas e saidas dos
veículos com o JSON da API descrita neste documento.

Uma garagem é composta por uma ou mais cancelas automáticas, utilizadas para entrada e saida de veículos, além de
sensores de presença nas vagas que analisam a presença ou ausência de um veículo naquela posição.

Após uma chamada ao endpoint de configuração da garagem ```GET``` ```/garage``` (descrito abaixo na API) a garagem é aberta e o sistema é
liberado para entrada e saída de veículos, veja que podemos ter mais de uma cancela automática no mesmo estacionamento ou por setor.

Mesmo para sensores de solo que podem ser acionados em conjunto se varios carros estacionarem ao mesmo tempo.

Assuma que o pagamento é realizado na saida do veículo, onde o sistema calcula o valor a ser pago pelo cliente.

## Regras de negócio

### Regra de preço dinâmico.

1. Com lotação menor que 25%, desconto de 10% no preço, na hora da entrada.
2. Com lotação menor até 50%, desconto de 0% no preço, na hora da entrada.
3. Com lotação menor até 75%, aumentar o preço em 10%, na hora da entrada.
4. Com lotação menor até 100%, aumentar o preço em 25%, na hora da entrada.

### Regra de lotação
Com 100% de lotação, fechar o setor e só permitir mais carros com a saida de um já estacionado.

## O que vamos avaliar?
O que será avaliado: interpretação dos requisitos, clareza de código, testes, estrutura, escalabilidade, domínio da
linguagem, aderência aos requisitos, melhorias propostas e cobertura das regras de negócio.

## Processo de seleção

* 2 semanas, para construir o projeto (nosso time demorou em torno de 8 horas para terminar.)
* Entrevista de avaliação do projeto
* Entrevista com RH
* Proposta

------------------
# API - Simulator Webhook ESTAPAR

A sua aplicação deve aceitar conexões pelo url http://localhost:3003/webhook
Este WEBHOOK fará disparos de eventos simulando entrada e saida de veículos da garagem para o nosso teste.

## Webhook
### Entrada na garagem

**WEBHOOK - POST**
```JSON
{
  "license_plate": "ZUL0001",
  "entry_time": "2025-01-01T12:00:00.000Z",
  "event_type": "ENTRY"
}
```

------------------

### Entrada na vaga

**WEBHOOK - POST**
```JSON
{
  "license_plate": "ZUL0001",
  "lat": -23.561684,
  "lng": -46.655981,
  "event_type": "PARKED"
}
```

------------------

### Saida de garagem

**WEBHOOK - POST**
```JSON
{		
  "license_plate": "",
  "exit_time": "2025-01-01T12:00:00.000Z",
  "event_type": "EXIT"
}
```

## Simulator REST API

### Garage config -  0.0.0.0:3000/garage

Endpoint que prove dados da garagem dinâmicamente, e deve ser chamado na inicialização do sistema de gestão para
identificar qual é a configuração para aquele dia.

**GET**
`/garage`

```JSON
{
  "garage": [
    {
      "sector": "A",
      "basePrice": 10.0,
      "max_capacity": 100,
      "open_hour": "08:00",
      "close_hour": "22:00",
      "duration_limit_minutes": 240
    },
    {
      "sector": "B",
      "basePrice": 4.0,
      "max_capacity": 72,
      "open_hour": "05:00",
      "close_hour": "18:00",
      "duration_limit_minutes": 120
    },
    .
    .
    .
  ],
  "spots": [
    {
      "id": 1,
      "sector": "A",
      "lat": -23.561684,
      "lng": -46.655981
    },
    {
      "id": 2,
      "sector": "B",
      "lat": -23.561674,
      "lng": -46.655971
    },
    .
    .
    .
  ]
}
```

# API do Projeto a ser implementada

## REST API

### Consulta de Placa

**POST**
`/plate-status`
```JSON
{
  "license_plate": "ZUL0001"
}
```

Response
```JSON
{
  "license_plate": "ZUL0001",
  "price_until_now": 0.00,
  "entry_time": "2025-01-01T12:00:00.000Z", 
  "time_parked": "2025-01-01T12:00:00.000Z"
}
```

------------------

### Consulta de Vaga

**POST**
`/spot-status`

Request
```JSON
{
  "lat": -23.561684,
  "lng": -46.655981
}
```

Response - 200
```JSON
{
  "ocupied": false,
  "entry_time": "2025-01-01T12:00:00.000Z",
  "time_parked": "2025-01-01T12:00:00.000Z"
}
```

### Consulta faturamento

**GET**
`/revenue`

Request
```JSON
{
  "date": "2025-01-01",
  "sector": "A"
}
```

Response
```JSON
{
  "amount": 0.00,
  "currency": "BRL",
  "timestamp": "2025-01-01T12:00:00.000Z"
}
```
</details>
