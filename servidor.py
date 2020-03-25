import random
import string
import cherrypy
import json
import sqlite3


frontpage = """<html>
	<head><meta charset="UTF-8"></head>
		<body>
		<h1>Welcome to meetings agenda!</h1>
		<h3>Please use the REST api:</h3>
		<p>Examples</p>
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
            con.execute("CREATE TABLE lixeiras (codigo varchar(255), volume int, local varchar(255))")
        except:
            print ('already exists')
            
def cleanup_database():
    """
    Destruir tabela de lixeiras
    """
    with sqlite3.connect(DB_STRING) as con:
        con.execute("DROP TABLE lixeiras")                    

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

class Data():
    exposed = True
    
    #curl -v http://192.168.0.13:8080/api/data/codigo
    #curl -v http://192.168.0.13:8080/api/data/
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
                r = c.execute('SELECT * FROM lixeiras WHERE codigo like ?',[local])
                if r.fetchone() is not None:
                    lixeira = Lixeira()
                    lixeira.codigo = r.fetchone()[0]
                    lixeira.volume = r.fetchone()[1]
                    lixeira.local = r.fetchone()[2]
                    return(lixeira.return_JSON())
                else:
                    return None
 
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
        
            
    def DELETE(self,codigo):
        with sqlite3.connect(DB_STRING) as c:        
            c.execute("DELETE FROM lixeiras WHERE codigo like ?", [codigo])    


def CORS():
	    cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"

if __name__ == '__main__':
    lixeiras = []

    conf = {
    '/': {'tools.sessions.on': True}
    }
    cherrypy.tree.mount(
    Data(), '/api/data',
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



