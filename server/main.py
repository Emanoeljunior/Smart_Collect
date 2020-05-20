# -*- coding: utf-8 -*-
import random
import wsgiref.handlers
import string
import cherrypy
import json
import sqlite3
from smartCollect import bin, trucker, database 

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
    app = cherrypy.tree.mount(trucker.Datatrucker(), '/api/dataTrucker', conf)
    cherrypy.tools.CORS = cherrypy.Tool('before_handler', CORS)
    cherrypy.config.update({'server.socket_host': '0.0.0.0','server.socket_port': 8080})
    cherrypy.engine.subscribe('start', database.setup_database())
    wsgiref.handlers.CGIHandler().run(app)
    



