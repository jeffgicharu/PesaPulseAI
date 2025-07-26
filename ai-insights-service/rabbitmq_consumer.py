import pika
import time
import json
import requests
from app import insights_collection # Import the MongoDB collection from our Flask app
from anomaly_detector import detect_anomalies # Import our analysis function

# --- Service & RabbitMQ Connection Details ---
RABBITMQ_HOST = 'localhost'
QUEUE_NAME = 'transaction_events'
TRANSACTION_SERVICE_URL = 'http://localhost:8082/api/transactions'

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

            # --- Step 1: Fetch transaction data from the transaction-service ---
            # Note: We need to add an endpoint to the transaction-service that allows
            # fetching transactions by userId for internal service-to-service communication.
            # For now, we assume an endpoint like /by-user/{userId} exists.
            # A real implementation would also require passing an auth token.
            
            # This is a placeholder for the actual API call logic.
            # In a real scenario, you'd fetch the transactions for the specific user.
            # For now, we'll simulate an empty list to test the flow.
            # transactions = requests.get(f"{TRANSACTION_SERVICE_URL}/by-user/{user_id}").json()
            print("     - NOTE: Simulating transaction fetch. In a real system, an API call would be made here.")
            transactions = [] # Placeholder

            # --- Step 2: Run the anomaly detection logic ---
            print("     - Running anomaly detection...")
            new_insights = detect_anomalies(transactions, user_id)
            print(f"     - Found {len(new_insights)} new insights.")

            # --- Step 3: Save new insights to MongoDB ---
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