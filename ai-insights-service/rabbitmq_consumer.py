import pika
import time
import json

# --- RabbitMQ Connection Details ---
RABBITMQ_HOST = 'localhost'
QUEUE_NAME = 'transaction_events'

def main():
    """
    Main function to set up and start the RabbitMQ consumer.
    It will attempt to connect and retry if the connection fails.
    """
    connection = None
    while not connection:
        try:
            # Attempt to connect to RabbitMQ server
            connection = pika.BlockingConnection(pika.ConnectionParameters(host=RABBITMQ_HOST))
            print("Successfully connected to RabbitMQ.")
        except pika.exceptions.AMQPConnectionError:
            print("RabbitMQ not available, retrying in 5 seconds...")
            time.sleep(5)

    channel = connection.channel()

    # Declare the queue. durable=True means the queue will survive a server restart.
    # This is a good practice for ensuring no messages are lost.
    channel.queue_declare(queue=QUEUE_NAME, durable=True)

    def callback(ch, method, properties, body):
        """
        This function is called whenever a message is received from the queue.
        """
        print(f" [x] Received message from queue '{QUEUE_NAME}'")
        
        try:
            # Decode the message body from bytes to string, then parse as JSON
            message_data = json.loads(body.decode('utf-8'))
            print(f"     Message content: {message_data}")
            
            # --- TODO: Add processing logic here ---
            # 1. Extract userId and batchId from message_data.
            # 2. Make an API call to the transaction-service to get the transaction data.
            # 3. Run the AI anomaly detection logic on the data.
            # 4. Save the generated insights to the 'insights_db' in MongoDB.
            
            # Acknowledge that the message has been successfully processed.
            # This removes the message from the queue.
            ch.basic_ack(delivery_tag=method.delivery_tag)
            print(" [x] Done. Message acknowledged.")
            
        except Exception as e:
            print(f" [!] Error processing message: {e}")
            # In a real system, you might want to reject the message and send it
            # to a dead-letter queue for later inspection.
            ch.basic_nack(delivery_tag=method.delivery_tag, requeue=False)


    # Tell the channel to use our callback function for messages on the specified queue.
    channel.basic_consume(queue=QUEUE_NAME, on_message_callback=callback)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    # Start listening for messages. This is a blocking call.
    channel.start_consuming()


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        # Gracefully exit
        exit(0)
