# -*- coding: utf-8 -*-
import random
import string
import json
import cherrypy

from smartCollect import cnx

class User(object):
    def __init__(self):
        self.name=""
        self.email=""
        self.password="" 
        
    def return_JSON(self):
        data = json.dumps(self.__dict__)
        return data


class DataUser():
    exposed = True

    @cherrypy.tools.json_out()
    def GET(self, name=None):
        if name is None:
            l=[]
            with cnx.cursor() as c:
                sql ='SELECT * FROM users'
                c.execute(sql,)
                rows = c.fetchall()
                
                for row in rows:
                    user = User()
                    user.name = row[0]
                    user.email = row[1]
                    user.password = row[2]
                    l.append(user.return_JSON())
            c.close()
            return( str(l) )
        else:
            with cnx.cursor() as c:
                sql = 'SELECT * FROM users WHERE name like %s'
                c.execute(sql, nome)
                variavel = c.fetchone()
            c.close()
            if variavel is not None:
                user= User()
                user.name = variavel[0] 
                user.email = variavel[1]
                user.password= variavel[2]
                return(user.return_JSON())
            else:
                return None

    @cherrypy.tools.json_in()
    def POST(self):
        data = cherrypy.request.json
        user= User()
        user.__dict__ = data
            
        with cnx.cursor() as c:
            sql = "INSERT INTO users VALUES (%s, %s, %s)"
            c.execute(sql,(user.name,user.email,user.password))
        cnx.commit()
        c.close()
        return 'done'

    def DELETE(self, name):
        with cnx.cursor() as c:
            sql = 'DELETE FROM users WHERE name = %s'
            c.execute(sql, (name,)) 
        cnx.commit()
        c.close()
                   
           
