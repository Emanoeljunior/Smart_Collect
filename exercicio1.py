import random
import string
import json
import cherrypy

class StringGenerator(object):

        @cherrypy.expose
        def add(self,data,nomep,tempop,localp):
                try:
                        self.data=data
                        self.nome=nomep
                        self.tempo=tempop
                        self.local=localp
                        meetings.append(self)
                        print (self.__dict__)
                        return 'done!'
                except:
                        return "erro"

        @cherrypy.expose
        def show(self):
                temp = ''
                for met in meetings:
                        temp+='<p>Data: '+met.data+' Nome: '+met.nome+' Tempo: '+met.tempo+' Local: '+met.local+'</p>'
                return temp


if __name__ == '__main__':
        meetings = []
        cherrypy.server.socket_host = '0.0.0.0'
        cherrypy.quickstart(StringGenerator())