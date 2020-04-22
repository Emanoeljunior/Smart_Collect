# -*- coding: utf-8 -*-
import random
import string
import cherrypy
import json
import sqlite3
from smartCollect import bin, trucker, database


frontpage = """<html>
	<head><meta charset="UTF-8"></head>
		<body>
		<h1>Welcome to Smart Collect!</h1>
		<h3>Please use the REST api:</h3>
                </body>
                </html>
			"""

class Interface(object):
    @cherrypy.expose
    def index(self):
        return frontpage
    
    @cherrypy.expose
    def cleanup(self):
        database.cleanup_database()
        database.setup_database()       


def CORS():
	    cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"

if __name__ == '__main__':

    conf = {'/':
            {'request.dispatch': cherrypy.dispatch.MethodDispatcher(),'tools.CORS.on': True}
        }
        
    cherrypy.tree.mount(bin.DataBin(), '/api/dataBin',conf)
    cherrypy.tree.mount(trucker.Datatrucker(), '/api/dataTrucker', conf)
    cherrypy.tree.mount(Interface(), '/', conf)
    cherrypy.tools.CORS = cherrypy.Tool('before_handler', CORS)
    cherrypy.config.update({'server.socket_host': '0.0.0.0','server.socket_port': 8080})
    cherrypy.engine.subscribe('start', database.setup_database())
    cherrypy.engine.start()
    cherrypy.engine.block()



