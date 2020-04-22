# -*- coding: utf-8 -*-
import random
import string
import json
import sqlite3
import cherrypy

from smartCollect import DB_STRING

class Bin(object):
    def __init__(self):
        self.ID=""
        self.volume=""
        self.local=""
        
    def return_JSON(self):
        data = json.dumps(self.__dict__)
        return data


class DataBin():
    exposed = True
   
    # Método GET que será utilizado pelo admin e pela API do google
    #modos para testar:
    #curl -v http://192.168.0.13:8080/api/data/local - substituir pelo ip da máquina
    #curl -v http://192.168.0.13:8080/api/data/ - substituir pelo ip da máquina
    @cherrypy.tools.json_out()   
    def GET(self, local=None):
        if local is None:
            l=[]
            with sqlite3.connect(DB_STRING) as c:
                for row in c.execute('SELECT * FROM bins ORDER BY ID'):
                    bin = Bin()
                    bin.ID = row[0]
                    bin.volume = row[1]
                    bin.local = row[2]
                    print(bin.__dict__)
                    l.append(bin.return_JSON())
                return('Bins \n: %s' % str(l) )
        else:
            with sqlite3.connect(DB_STRING) as c:
                r = c.execute('SELECT * FROM bins WHERE local like ?',[local])
                variavel = r.fetchone()
                if variavel is not None:
                    bin = Bin()
                    bin.ID = variavel[0] 
                    bin.volume = variavel[1]
                    bin.local = variavel[2]
                    return(bin.return_JSON())
                else:
                    return None
 
    #Metodo POST utilizado pelo dispositivo IOT (bins) - enviando para o Servidor
    #modos para testar:
    #curl -H "Content-Type: application/json" -X POST -d '{"ID":"12A", "volume":"100", "local":"102"}' http://127.0.0.1:8080/api/dataBin
    #WINDOWS: curl -H "Content-Type: application/json" -X POST -d "{\"ID\":\"12A\", \"volume\":\"100\", "local\":\"102\"}" http://192.168.0.13:8080/api/data
    @cherrypy.tools.json_in()
    def POST(self):
        data = cherrypy.request.json
        bin = Bin()
        bin.__dict__ = data
        print: "is here"
            
        with sqlite3.connect(DB_STRING) as c:
            c.execute("INSERT INTO bins VALUES (?, ?, ?)",
                       [bin.ID,bin.volume,bin.local])
            c.commit()
        return 'done'
        
    #Método DELETE utilizado pelo ADMIN
    #modos para testar:
    #curl -X DELETE http://192.168.0.13:8080/api/data/ID
    def DELETE(self,ID):
        with sqlite3.connect(DB_STRING) as c:        
            c.execute("DELETE FROM bins WHERE ID like ?", [ID]) 

