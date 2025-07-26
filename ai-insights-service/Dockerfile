# Use an official Python runtime as a parent image
FROM python:3.11-slim

# Set the working directory in the container
WORKDIR /app

# Copy the requirements file first to leverage Docker cache
COPY requirements.txt .

# Install any needed packages specified in requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application's source code into the container
COPY . .

# Expose the port that the ai-insights-service runs on
EXPOSE 8083

# Define the command to run the application.
# We run the Flask app directly. In a real production scenario,
# you would use a production-grade WSGI server like Gunicorn.
CMD ["python", "app.py"]
