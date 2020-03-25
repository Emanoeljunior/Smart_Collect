# -*- coding: utf-8 -*-
import random
import string
import cherrypy
import json
import sqlite3


frontpage = """<html>
	<head><meta charset="UTF-8"></head>
		<body>
		<h1>Welcome to Smart Collect!</h1>
		<h3>Please use the REST api:</h3>
                </body>
                </html>
			"""
DB_STRING = "my.db"

def setup_database():
    """
    Criando tabela lixeiras
    """
    with sqlite3.connect(DB_STRING) as con:
        try:
            con.execute("CREATE TABLE lixeiras (codigo varchar(255) NOT NULL, volume int, local varchar(255) NOT NULL)")
            con.execute("CREATE TABLE truckers (codigo varchar(255) NOT NULL, volume int, local varchar(255) NOT NULL)")
        except:
            print ('already exists')
            
def cleanup_database():
    """
    Destruir tabela de lixeiras
    """
    with sqlite3.connect(DB_STRING) as con:
        con.execute("DROP TABLE lixeiras")
        con.execute("DROP TABLE truckers")                    

class Interface(object):
    @cherrypy.expose
    def index(self):
        return frontpage
    
    @cherrypy.expose
    def cleanup(self):
        cleanup_database()
        setup_database()       

class Lixeira(object):
    def __init__(self):
        self.codigo=""
        self.volume=""
        self.local=""
        
    def return_JSON(self):
        data = json.dumps(self.__dict__)
        return data

class Trucker(object):
    def __init__(self):
        self.codigo=""
        self.volume=""
        self.local=""

    def return_JSON(self):
        data = json.dumps(self.__dict__)
        return data

class Data():
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
                for row in c.execute('SELECT * FROM lixeiras ORDER BY codigo'):
                    lixeira = Lixeira()
                    lixeira.codigo = row[0]
                    lixeira.volume = row[1]
                    lixeira.local = row[2]
                    print(lixeira.__dict__)
                    l.append(lixeira.return_JSON())
                return('Lixeiras \n: %s' % str(l) )
        else:
            with sqlite3.connect(DB_STRING) as c:
                r = c.execute('SELECT * FROM lixeiras WHERE local like ?',[local])
                variavel = r.fetchone()
                if variavel is not None:
                    lixeira = Lixeira()
                    lixeira.codigo = variavel[0] 
                    lixeira.volume = variavel[1]
                    lixeira.local = variavel[2]
                    return(lixeira.return_JSON())
                else:
                    return None
 
    #Metodo POST utilizado pelo dispositivo IOT (lixeiras) - enviando para o Servidor
    #modos para testar:
    #curl -H "Content-Type: application/json" -X POST -d '{"codigo":"12A", "volume":"100", "local":"102"}' http://192.168.0.13:8080/api/data
    #WINDOWS: curl -H "Content-Type: application/json" -X POST -d "{\"codigo\":\"12A\", \"volume\":\"100\", "local\":\"102\"}" http://192.168.0.13:8080/api/data
    @cherrypy.tools.json_in()
    def POST(self):
        data = cherrypy.request.json
        lixeira = Lixeira()
        lixeira.__dict__ = data
        lixeiras.append(lixeira)
        with sqlite3.connect(DB_STRING) as c:
            c.execute("INSERT INTO lixeiras VALUES (?, ?, ?)",
                       [lixeira.codigo,lixeira.volume,lixeira.local])
            c.commit()
        return 'done'
        
    #Método DELETE utilizado pelo ADMIN
    #modos para testar:
    #curl -X DELETE http://192.168.0.13:8080/api/data/codigo
    def DELETE(self,codigo):
        with sqlite3.connect(DB_STRING) as c:        
            c.execute("DELETE FROM lixeiras WHERE codigo like ?", [codigo]) 

class Datatrucker():
    exposed = True
    @cherrypy.tools.json_in()
    def POST(self):
        data = cherrypy.request.json
        trucker = Trucker()
        trucker.__dict__ = data
        truckers.append(trucker)
        with sqlite3.connect(DB_STRING) as c:
            c.execute("INSERT INTO truckers VALUES (?, ?, ?)",
                       [trucker.codigo,trucker.volume,trucker.local])
            c.commit()
        return 'done'   


def CORS():
	    cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"

if __name__ == '__main__':
    lixeiras = []
    truckers = []

    conf = {
    '/': {'tools.sessions.on': True}
    }
    cherrypy.tree.mount(
    Data(), '/api/dataLixeira',
        {'/':
            {'request.dispatch': cherrypy.dispatch.MethodDispatcher(),'tools.CORS.on': True}
        }
    )
    cherrypy.tree.mount(
    Datatrucker(), '/api/dataTrucker',
        {'/':
            {'request.dispatch': cherrypy.dispatch.MethodDispatcher(),'tools.CORS.on': True}
        }
    )
    cherrypy.tree.mount(Interface(), '/', conf)
    cherrypy.tools.CORS = cherrypy.Tool('before_handler', CORS)
    cherrypy.config.update({'server.socket_host': '0.0.0.0','server.socket_port': 8080})
    cherrypy.engine.subscribe('start', setup_database)
    cherrypy.engine.start()
    cherrypy.engine.block()



