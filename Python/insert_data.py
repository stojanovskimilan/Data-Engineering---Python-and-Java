import csv
import time
import db
from psycopg2 import sql
csv_file_name = "Data/test.csv"

# Read data from CSV
with open(csv_file_name, 'r') as file:
    reader = csv.reader(file)
    header = next(reader)

    data_tuples = [tuple(row) for row in reader]

start_time = int(time.time()*1000)
for row in data_tuples:
    insert_query = sql.SQL("INSERT INTO test_table ({}) VALUES ({})").format(
        sql.SQL(', ').join(map(sql.Identifier, header)),
        sql.SQL(', ').join(sql.Placeholder() for _ in row)
    )
    db.connector.execute_query(insert_query, row)

# Measure execution time
end_time = int(time.time()*1000)
execution_time = end_time - start_time

# Disconnect from PostgreSQL
db.connector.disconnect()

with open('python_output.txt', 'w') as file:
    # Write content to the file
    file.write(f'Rows: {len(data_tuples)}\n')
    file.write(f"Data insertion using Python took {execution_time} milliseconds.\n")
    file.write('-------------------------------------------------------------------')



