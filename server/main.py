
import os
import cherrypy
import wsgiref.handlers
import pymysql
from smartCollect import cnx,bin,trucker,usuario, database

class Interface(object):

    @cherrypy.expose
    def index(self):
        return str('Ola localhost cherrypy')


def CORS():
	    cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"

conf = {'/':
            {'request.dispatch': cherrypy.dispatch.MethodDispatcher(),'tools.CORS.on': True}
        }

cherrypy.tree.mount(usuario.DataUsuario(), '/api/dataUsuario', conf)
cherrypy.tree.mount(trucker.DataTrucker(), '/api/dataTrucker', conf)
cherrypy.tree.mount(bin.DataBin(), '/api/dataBin', conf)
cherrypy.tree.mount(Interface(), '/')
cherrypy.tools.CORS = cherrypy.Tool('before_handler', CORS)
if __name__ == '__main__':
	cherrypy.config.update({'server.socket_host': '0.0.0.0','server.socket_port': 8080})
	cherrypy.engine.start()
	cherrypy.engine.block()
else:
	app = cherrypy.tree
	wsgiref.handlers.CGIHandler().run(app)

