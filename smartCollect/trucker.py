# -*- coding: utf-8 -*-
import random
import string
import cherrypy
import json
import sqlite3

from smartCollect import DB_STRING, database
class Trucker(object):
    def __init__(self):
        self.ID=""
        self.volume=""
        self.local=""

    def return_JSON(self):
        data = json.dumps(self.__dict__)
        return data

class Datatrucker():
 
    exposed = True
    @cherrypy.tools.json_in()
    def POST(self):
        data = cherrypy.request.json
        trucker = Trucker()
        trucker.__dict__ = data
        with sqlite3.connect(DB_STRING) as c:
            c.execute("INSERT INTO truckers VALUES (?, ?, ?)",
                       [trucker.ID,trucker.volume,trucker.local])
            c.commit()
        return 'done'   
