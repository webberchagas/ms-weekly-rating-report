# 📊 MS Weekly Rating Report – Azure Functions

![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk&logoColor=white)
![Azure Functions](https://img.shields.io/badge/Azure%20Functions-Serverless-blue?logo=azurefunctions&logoColor=white)
![Azure](https://img.shields.io/badge/Microsoft%20Azure-Cloud-0078D4?logo=microsoftazure&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-Database-47A248?logo=mongodb&logoColor=white)
![Service Bus](https://img.shields.io/badge/Azure%20Service%20Bus-Messaging-0089D6?logo=microsoftazure&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)

Este projeto faz parte do **Tech Challenge da Fase 4** e tem como objetivo buscar as avaliações realizadas pelos usuários no banco de dados, organizá-los em um relatório e enviá-los para uma fila, para que seja feito o processamento assíncrono, utilizando arquitetura **serverless na Azure**.

O serviço foi desenvolvido seguindo boas práticas de:
- separação de responsabilidades
- arquitetura serverless
- comunicação assíncrona
- código limpo e legível

---

## 🎯 Objetivo
Buscar as avaliações no banco de dados, organizá-los em um relatório semanal das avaliações contendo quantidade de críticos e não críticos e o total de avaliações ao longo de sete dias, enviando-o para uma fila para processamento assíncrono.


Fluxo simplificado:

Azure Function (Time Trigger) → MongoDB (Busca as avaliações) → Gera o relatório semanal → Service Bus Queue

---

## ⚙️ Tecnologias Utilizadas

- Java 21
- Azure Functions
- Azure Service Bus
- MongoDB
- Jackson (serialização JSON)
- Maven

---

## 📤 Processamento Assíncrona (Service Bus)

- Fila utilizada: q-ms-weekly-report
- Tipo: Queue
- Comunicação desacoplada para consumo por serviço de notificação

```json
{
  "dateTimeEmission":"2026-01-06T21:51:00Z",
  "ratingCountByDate":[
    {
      "label":"2025-12-30",
      "value":1
    },
    {
      "label":"2025-12-31",
      "value":57
    },
    {
      "label":"2026-01-01",
      "value":70
    },
    {
      "label":"2026-01-02",
      "value":72
    },
    {
      "label":"2026-01-03",
      "value":59
    },
    {
      "label":"2026-01-04",
      "value":56
    },
    {
      "label":"2026-01-05",
      "value":68
    }
  ],
  "ratingCountByUrgency":[
    {
      "label":"CRITICAL",
      "value":159
    },
    {
      "label":"NORMAL",
      "value":224
    }
  ]
}
```

---

## 🔐 Variáveis de Ambiente
**Local** (```local.settings.json```)
```json
{
  "Values": {
    "AzureWebJobsStorage": "UseDevelopmentStorage=true",
    "FUNCTIONS_WORKER_RUNTIME": "java",
    "MONGODB_URI": "...",
    "MONGO_DB": "...",
    "MONGO_COLLECTION": "...",
    "SERVICE_BUS_CONNECTION": "Endpoint=sb://..."
  }
}
```

---

## ▶️ Executando localmente

1. Configure o ```local.settings.json```
2. Inicie a Function:
```bash
  mvn clean package
  mvn azure-functions:run
```
3. O processamento será feito automaticamente
4. O cron do Time Trigger da Function está programado para rodar todo sábado as 23:59:59
