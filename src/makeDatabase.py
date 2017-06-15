#!/usr/bin/env python3
import sys
import psycopg2
import os
from optparse import OptionParser

def connect(options):
    conn_string = "host='localhost' dbname='bpcogs' user='sven'" \
                  " password='bpcogs1617'"
    conn_string = "host='{}' dbname='{}' user='{}' password='{}'".format(options.host, options.database, options.user, options.password)

    try:
        conn = psycopg2.connect(conn_string)
    except psycopg2.Error as e:
        print("Unable to connect to database {} on {} with user {}.".format(options.database, options.host, options.user))
        #print(e.pgerror)
        #print(e.diag.message_detail)
        sys.exit(1)
    else:
        print("Connected to database {} on {} with user {}.".format(options.database, options.host, options.user))
        cursor = conn.cursor()

    return conn, cursor

def get_arguments():
	parser = OptionParser(usage="usage: %prog [options] filename")
	parser.add_option("-H", "--host", dest="host", default="localhost", help="host for the database")
	parser.add_option("-d", "--database", dest="database", default="postgres", help="the database to connect to")
	parser.add_option("-u", "--user", dest="user", default="postgres", help="the user to connect to the database with")
	parser.add_option("-p", "--password", dest="password", default="postgres", help="the password to connect to the database with")
	options, args = parser.parse_args()

	if not len(args) < 1:
		return options, args
	else:
		print("No file given.")
		sys.exit(1)

def drop_db(conn, cursor):
	cursor.execute("DROP TABLE IF EXISTS SPELERWOORD")
	cursor.execute("DROP TABLE IF EXISTS SPELER")
	cursor.execute("DROP TABLE IF EXISTS WOORD")
	
	conn.commit()

def create_database(conn, cursor):
	drop_db(conn, cursor)

	cursor.execute(
        "CREATE TABLE SPELER ("
        "Id INTEGER,"
        "Naam VARCHAR (10),"
        "Datum DATE,"
        "Tijd TIME,"
	    "CONSTRAINT pk_id PRIMARY KEY (Id))"
	    )

	cursor.execute(
        "CREATE TABLE WOORD ("
        "woord VARCHAR(25),"
        "Score SMALLINT,"
	    "CONSTRAINT pk_woord PRIMARY KEY (woord))"
	    )

	cursor.execute(
        "CREATE TABLE SPELERWOORD ("
        "Speler_Id INTEGER,"
        "woord VARCHAR(25),"
	    "CONSTRAINT pk_spelerwoord PRIMARY KEY (Speler_Id, woord),"
	    "CONSTRAINT fk_speler_id FOREIGN KEY (Speler_Id) REFERENCES SPELER(Id),"
	    "CONSTRAINT fk_woord FOREIGN KEY (woord) REFERENCES WOORD(woord))"
	    )

	conn.commit()

	print("Tables made.")

def get_woord_score(woord):
	if len(woord) < 5:
		return 1
	elif len(woord) == 5:
		return 2
	elif len(woord) == 6:
		return 3
	elif len(woord) == 7:
		return 5
	elif len(woord) > 7:
		return 11

def fill_woorden(conn, cursor, args):
	file = open(args[0], "r")
	woorden = list(set([x[:-1] for x in file.readlines()]))
	print("Woorden toevoegen.")
	for i in range(len(woorden)):
		if woorden[i] != "" and len(woorden[i]) < 26:
			cursor.execute(
				"INSERT INTO WOORD VALUES('{}', {})".format(woorden[i], get_woord_score(woorden[i]))
			)
			printProgressBar(i, len(woorden))
	print()
	conn.commit()
	print("Woorden toegevoegd.")

def printProgressBar (iteration, total, prefix = '', suffix = '', decimals = 1, length = 100, fill = '█'):
    percent = ("{0:." + str(decimals) + "f}").format(100 * (iteration / float(total)))
    filledLength = int(length * iteration // total)
    bar = fill * filledLength + '-' * (length - filledLength)
    print('\r%s |%s| %s%% %s' % (prefix, bar, percent, suffix), end = '\r')
    if iteration == total: 
        print()

def main():
	options, args = get_arguments()
	if not os.path.isfile(args[0]):
		print("File {} could not be found".format(args[0]))
		sys.exit(1)
	conn, cursor = connect(options)
	create_database(conn, cursor)
	fill_woorden(conn, cursor, args)
	conn.close()

main()