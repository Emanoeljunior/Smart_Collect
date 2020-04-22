# -*- coding: utf-8 -*-
import random
import string
import json
import sqlite3
from smartCollect import DB_STRING

def setup_database():
    """
    Criando tabela bins
    """
    with sqlite3.connect(DB_STRING) as con:
        try:
            con.execute("CREATE TABLE bins (ID varchar(255) NOT NULL, volume int, local varchar(255) NOT NULL)")
            con.execute("CREATE TABLE truckers (ID varchar(255) NOT NULL, volume int, local varchar(255) NOT NULL)")
        except:
            print ('already exists')
            
def cleanup_database():
    """
    Destruir tabela de bins
    """
    with sqlite3.connect(DB_STRING) as con:
        con.execute("DROP TABLE bins")
        con.execute("DROP TABLE truckers")                    
