# Simple Distributed Bank Services Demo
## Quickstart
1. Start Docker Engine (if necessary)
1. Clone this repository and `cd` into the repository folder
1. Open a new terminal and execute:
   ```shell
   docker-compose up account-service
   ```
1. Open a new terminal and launch the `audit-service`:
   ```shell
   SERVER_PORT=8888 ./gradlew :audit-service:bootRun
   ```
1. Open a new terminal and launch the  `debit-service`:
   ```shell
   SERVER_PORT=8889 ACCOUNT_SERVICE_URL=http://localhost:48081 AUDIT_SERVICE_URL=http://localhost:8888 ./gradlew :debit-service:bootRun
   ```
1. Use Postman with the `Simple Distributed Bank Services Demo => Debit Service => Purchase 8889` request provided in [this collection](simple-distributed-bank-services-demo.postman_collection.json), or, open a new terminal and execute:
   ```shell
   curl --verbose --location --request POST 'localhost:8889/purchase' \
   --header 'Content-Type: application/json' \
   --data-raw '{ "amount": 10000 }'
   ```
   The response to this request should be `200 OK`.
1. Observe the output from the `audit-service`. The service console should output:
   ```
   {type=DEBIT, status=SUCCESS, amount=10000}
   {type=TRANSACTION, status=SUCCESS, amount=10000}
   ```