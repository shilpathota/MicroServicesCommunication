# MicroServicesCommunication
 
This project demostrates how the micro services can communicate in the event-driven architecture.

Here we have considered 3 services
1. Order Service
2. Kafka Service
3. Inventory Service

### Project Flow
When the order is received to the order service using the controller, the event is triggered and publishes to the respective kafka topic that the order is placed successfully.


The inventory service which is subscribed to the event gets notification and thereby reduces the inventory by the number of items bought.

The Kafka service is the middle layer between these 2 services which supports asynchronous communication.
