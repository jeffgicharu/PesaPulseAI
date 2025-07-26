import pika
import time
import json
import requests
from app import insights_collection
from anomaly_detector import detect_anomalies

# --- Service & RabbitMQ Connection Details ---
# Use the service names as defined in docker-compose.yml
RABBITMQ_HOST = 'rabbitmq'
TRANSACTION_SERVICE_URL = 'http://transaction-service:8082/api/transactions'
QUEUE_NAME = 'transaction_events'

def main():
    """
    Main function to set up and start the RabbitMQ consumer.
    """
    connection = None
    while not connection:
        try:
            connection = pika.BlockingConnection(pika.ConnectionParameters(host=RABBITMQ_HOST))
            print(" [x] RabbitMQ Consumer: Successfully connected to RabbitMQ.")
        except pika.exceptions.AMQPConnectionError:
            print(" [!] RabbitMQ Consumer: RabbitMQ not available, retrying in 5 seconds...")
            time.sleep(5)

    channel = connection.channel()
    channel.queue_declare(queue=QUEUE_NAME, durable=True)

    def callback(ch, method, properties, body):
        """
        This function is called whenever a message is received from the queue.
        """
        print(f" [x] RabbitMQ Consumer: Received message from queue '{QUEUE_NAME}'")
        
        try:
            message_data = json.loads(body.decode('utf-8'))
            user_id = message_data.get('userId')
            
            if not user_id:
                raise ValueError("Message is missing 'userId'")

            print(f"     - Processing for userId: {user_id}")

            # This is a placeholder for the actual API call logic.
            print("     - NOTE: Simulating transaction fetch. In a real system, an API call would be made here.")
            transactions = [] 

            print("     - Running anomaly detection...")
            new_insights = detect_anomalies(transactions, user_id)
            print(f"     - Found {len(new_insights)} new insights.")

            if new_insights and insights_collection is not None:
                insights_collection.insert_many(new_insights)
                print(f"     - Successfully saved {len(new_insights)} insights to the database.")

            ch.basic_ack(delivery_tag=method.delivery_tag)
            print(" [x] Done. Message acknowledged.")
            
        except Exception as e:
            print(f" [!] RabbitMQ Consumer: Error processing message: {e}")
            ch.basic_nack(delivery_tag=method.delivery_tag, requeue=False)


    channel.basic_consume(queue=QUEUE_NAME, on_message_callback=callback)
    print(' [*] RabbitMQ Consumer: Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        exit(0)
