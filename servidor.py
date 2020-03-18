import random
import string
import cherrypy
import json


frontpage = """<html>

		<head><meta charset="UTF-8"></head>
		<body>
		<h1>Welcome!!<h1>
		</body>
		</html>
	     """

class Interface(object):
	@cherrypy.expose
	def index(self):
		return frontpage

class Lixeira(object):
	
	def __init__(self):
		self.local=""
		self.volume=""
	

	@cherrypy.expose
	def add(self,local,volume):
		self.local=local
		self.volume=volume
		lixeiras.append(self)
		return 'doone!'
	
	def return_JSON(self):
		data = json.dumps(self.__dict__)
		return data

class Data():
	
	    exposed = True
	    @cherrypy.tools.json_out()
	    def GET(self, local=None):
		locais = {obj.local:obj for obj in lixeiras}
		if local is None:
		    l=[]
		    for lixeira in lixeiras:
		        l.append(lixeira.return_JSON())
		    return('Available local \n: %s' % str(l) )
       		
		elif local in locais:
			return(locais[local].return_JSON())
		else:
		     return('No lixeira')


	    @cherrypy.tools.json_in()
	    def POST(self):
		data = cherrypy.request.json
		lixeira = Lixeira()
		lixeira.__dict__ = data
		lixeiras.append(lixeira)
            
            def DELETE(self,local):
               locais = {obj.local:obj for obj in lixeiras}
               if local in locais:
                   lixeiras.remove(locais[local])
                   return('lixeira em %s removida' % local)
	       else:
                   return('nao ha lixeira nesse local')
    

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



