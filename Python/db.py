import yaml
import psycopg2
from psycopg2 import sql

# Assuming the YAML configuration is in a file named config.yaml
with open('config.yaml', 'r') as file:
    config_data = yaml.safe_load(file)


class PostgresConnector:
    def __init__(self, dbname, user, password, host, port):
        self.dbname = dbname
        self.user = user
        self.password = password
        self.host = host
        self.port = port
        self.connection = None
        self.cursor = None

    def connect(self):
        try:
            self.connection = psycopg2.connect(
                dbname=self.dbname,
                user=self.user,
                password=self.password,
                host=self.host,
                port=self.port
            )
            self.cursor = self.connection.cursor()
            print("Connected to the database.")
        except psycopg2.Error as e:
            print("Unable to connect to the database.")
            print(e)

    def disconnect(self):
        if self.connection:
            self.cursor.close()
            self.connection.close()
            print("Connection closed.")

    def execute_query(self, query, values=None):
        try:
            if values:
                self.cursor.execute(query, values)
            else:
                self.cursor.execute(query)
            self.connection.commit()
            print("Query executed successfully.")
        except psycopg2.Error as e:
            print("Error executing the query.")
            print(e)

# Example usage:
connector = PostgresConnector(dbname=config_data['postgres']['database'], user=config_data['postgres']['user'], password=config_data['postgres']['password'],host=config_data['postgres']['host'], port=config_data['postgres']['port'])
connector.connect()

# # Example insert query
# insert_query = sql.SQL("INSERT INTO your_table (column1, column2) VALUES (%s, %s)")
# insert_values = ("value1", "value2")
# connector.execute_query(insert_query, insert_values)

# connector.disconnect()
print(connector.port)
