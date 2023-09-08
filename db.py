import psycopg2
conn = psycopg2.connect(dbname="games", user="kirill", password = "123" , host = "localhost")
cursor = conn.cursor()

cursor.execute("CREATE TABLE test (test1 varchar, test2 varchar)")

cursor.execute("INSERT INTO test (test1, test2) VALUES ('1','2')")

cursor.execute("SELECT * FROM test")

print(cursor.fetchAll())

cursor.close()

conn.close()
