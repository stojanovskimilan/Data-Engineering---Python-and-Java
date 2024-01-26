import random
import csv

currency = ["ETH", "ADA", "MATIC", "DOT", "ATOM","MANA","BTC","LINK","ALGO","SAND"]

data = [
    ["id", "currency", "quantity"]
]
# Generate three random items from the list
for i in range(0, 25000):
    data.append([i, random.choice(currency), random.randint(1, 1000000)])

csv_file_name = "Data/test.csv"

# Writing data to CSV file using a for loop
with open(csv_file_name, mode='w', newline='') as file:
    writer = csv.writer(file)
    
    # Write each row in the list to the CSV file
    for row in data:
        writer.writerow(row)

print(f"Data has been written to {csv_file_name}")