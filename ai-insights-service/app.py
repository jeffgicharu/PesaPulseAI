import os
from flask import Flask, jsonify
from pymongo import MongoClient
from dotenv import load_dotenv

# --- Application Setup ---

# Construct the path to the .env file in the project root directory
# This makes the path relative to the current file's location.
dotenv_path = os.path.join(os.path.dirname(__file__), '..', '.env')
load_dotenv(dotenv_path=dotenv_path)

# Initialize the Flask application
app = Flask(__name__)

# --- Database Connection ---

# Get MongoDB credentials from environment variables
MONGO_USER = os.getenv('MONGO_INITDB_ROOT_USERNAME')
MONGO_PASS = os.getenv('MONGO_PASSWORD')
MONGO_HOST = 'localhost'
MONGO_PORT = 27017

# Construct the connection string
mongo_uri = f"mongodb://{MONGO_USER}:{MONGO_PASS}@{MONGO_HOST}:{MONGO_PORT}/?authSource=admin"

# Initialize the MongoDB client and select the database for this service
try:
    client = MongoClient(mongo_uri)
    # The database for this service will be named 'insights_db'
    db = client.insights_db
    # The collection will be named 'insights'
    insights_collection = db.insights
    print("Successfully connected to MongoDB.")
except Exception as e:
    print(f"Error connecting to MongoDB: {e}")
    client = None
    insights_collection = None


# --- API Routes ---

@app.route('/')
def index():
    """A simple health check endpoint to confirm the service is running."""
    return jsonify({"status": "ai-insights-service is running"}), 200

# We will add the /api/insights endpoint later.


# --- Main Execution ---

if __name__ == '__main__':
    # Run the Flask app on port 8083 to avoid conflicts with other services.
    # debug=True will automatically reload the server when you make code changes.
    app.run(host='0.0.0.0', port=8083, debug=True)
