import csv
import sqlite3
import sys
from pathlib import Path

source = Path(sys.argv[1])
country_source = Path(sys.argv[2])
target = Path(sys.argv[3])
target.parent.mkdir(parents=True, exist_ok=True)
if target.exists():
    target.unlink()

countries = {}
with country_source.open(encoding="utf-8") as handle:
    for line in handle:
        if not line.startswith("#"):
            fields = line.rstrip("\n").split("\t")
            if len(fields) > 8:
                countries[fields[0]] = (fields[4], fields[8])

db = sqlite3.connect(target)
db.executescript("""
CREATE TABLE countries(code TEXT PRIMARY KEY, name TEXT NOT NULL, continent TEXT NOT NULL);
CREATE TABLE cities(id INTEGER PRIMARY KEY, name TEXT NOT NULL, ascii_name TEXT NOT NULL,
  alternates TEXT NOT NULL, country_code TEXT NOT NULL, admin1 TEXT NOT NULL,
  latitude REAL NOT NULL, longitude REAL NOT NULL, timezone TEXT NOT NULL, population INTEGER NOT NULL);
CREATE INDEX city_country_name ON cities(country_code, ascii_name COLLATE NOCASE);
CREATE INDEX city_ascii_name ON cities(ascii_name COLLATE NOCASE);
""")
db.executemany("INSERT INTO countries VALUES(?,?,?)", ((code, name, continent) for code, (name, continent) in countries.items()))

batch = []
with source.open(encoding="utf-8") as handle:
    for fields in csv.reader(handle, delimiter="\t"):
        if len(fields) < 18 or fields[8] not in countries or not fields[17]:
            continue
        batch.append((int(fields[0]), fields[1], fields[2], fields[3], fields[8], fields[10], float(fields[4]), float(fields[5]), fields[17], int(fields[14] or 0)))
        if len(batch) == 5000:
            db.executemany("INSERT INTO cities VALUES(?,?,?,?,?,?,?,?,?,?)", batch)
            batch.clear()
if batch:
    db.executemany("INSERT INTO cities VALUES(?,?,?,?,?,?,?,?,?,?)", batch)
db.commit()
db.execute("ANALYZE")
db.execute("VACUUM")
db.close()
