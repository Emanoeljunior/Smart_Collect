# -*- coding: utf-8 -*-
import random
import string
import sys
import cherrypy
import json
import sqlite3
from pydantic import BaseModel, ValidationError, validator

from smartCollect import DB_STRING, database
class Trucker(BaseModel):
    ID =' '
    volume: float
    local: str
   
 
class Datatrucker():
 
    exposed = True
    
    @cherrypy.tools.json_out()   
    def GET(self, local=None):
        if local is None:
            l=[]
            with sqlite3.connect(DB_STRING) as c:
                
                for row in c.execute('SELECT * FROM truckers ORDER BY local'):
                    trucker = Trucker.parse_obj({'ID': row[0], 'volume': row[1], 'local': row[2]})
                    print(trucker.dict())
                    l.append(trucker.json())
                return('Truckers \n: %s' % str(l) )
        else:
            with sqlite3.connect(DB_STRING) as c:
                r = c.execute('SELECT * FROM truckers WHERE local like ?',[local])
                variavel = r.fetchone()
                if variavel is not None:
                    trucker = Trucker()
                    trucker.ID = variavel[0] 
                    trucker.volume = variavel[1]
                    trucker.local = variavel[2]
                    return(trucker.return_JSON())
                else:
                    return None
 
    @cherrypy.tools.json_in()
    def POST(self):
        data = cherrypy.request.json
        try:
            trucker = Trucker.parse_obj(data)
        except ValidationError as e:
            raise cherrypy.HTTPError(400,str(e))
        trucker.ID = ''.join(random.choice(string.ascii_uppercase 
        + string.digits + string.ascii_lowercase) for _ in range(8))
        with sqlite3.connect(DB_STRING) as c:
            c.execute("INSERT INTO truckers VALUES (?, ?, ?)",
                       [trucker.ID,trucker.volume,trucker.local])
            c.commit()
        return 'done'   
