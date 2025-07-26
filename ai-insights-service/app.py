import os
from flask import Flask, jsonify
from pymongo import MongoClient
from dotenv import load_dotenv

# --- Application Setup ---
dotenv_path = os.path.join(os.path.dirname(__file__), '..', '.env')
load_dotenv(dotenv_path=dotenv_path)

app = Flask(__name__)

# --- Database Connection ---
MONGO_USER = os.getenv('MONGO_INITDB_ROOT_USERNAME')
MONGO_PASS = os.getenv('MONGO_PASSWORD')
# Use the service name 'mongo' as defined in docker-compose.yml
MONGO_HOST = 'mongo' 
MONGO_PORT = 27017

mongo_uri = f"mongodb://{MONGO_USER}:{MONGO_PASS}@{MONGO_HOST}:{MONGO_PORT}/?authSource=admin"

try:
    client = MongoClient(mongo_uri)
    db = client.insights_db
    insights_collection = db.insights
    print("Successfully connected to MongoDB.")
except Exception as e:
    print(f"Error connecting to MongoDB: {e}")
    client = None
    insights_collection = None

# --- API Routes ---
@app.route('/')
def index():
    return jsonify({"status": "ai-insights-service is running"}), 200

# --- Main Execution ---
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8083, debug=True)