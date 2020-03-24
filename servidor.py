import random
import string
import cherrypy
import json
import sqlite3


frontpage = """<html>

		<head><meta charset="UTF-8"></head>
		<body>
		<h1>Welcome!!<h1>
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
            print 'already exists'
            
            
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
        self.local=""
        self.volume=""
        
    @cherrypy.expose
    def add(self,local,volume):
        self.codigo=codigo
        self.local=local
        self.volume=volume
        lixeiras.append(self)
        return 'done!'
	
	def return_JSON(self):
		data = json.dumps(self.__dict__)
		return data

class Data():
    exposed = True
    
    #curl -v http://192.168.0.13:8080/api/data/NAME
    #curl -v http://192.168.0.13:8080/api/data/
    @cherrypy.tools.json_out()
        
    def GET(self, local=None):
        if local is None:
            l=[]
            with sqlite3.connect(DB_STRING) as c:
                for row in c.execute('SELECT * FROM lixeiras ORDER BY codigo'):
                    lixeiras = Lixeira()
                    lixeiras.codigo = row[0]
                    lixeiras.volume = row[1]
                    lixeiras.local = row[2]
                    print(meet.__dict__)
                    l.append(lixeiras.return_JSON())
                return('Lixeiras \n: %s' % str(l) )
        else:
            with sqlite3.connect(DB_STRING) as c:
                r = c.execute('SELECT * FROM lixeiras WHERE name like ?', [codigo])
                if r.fetchone() is not None:
                    lixeiras = Lixeira()
                    lixeiras.codigo = r.fetchone()[0]
                    lixeiras.volume = r.fetchone()[1]
                    lixeiras.local = r.fetchone()[2]
                    return(lixeiras.return_JSON())
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
    cherrypy.engine.start()
    cherrypy.engine.block()



