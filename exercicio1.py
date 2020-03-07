import random
import string

import cherrypy

class StringGenerator(object):

        @cherrypy.expose
        def index(self):
                return """<html>
                  <head></head>
                  <body>
                    <form method="get" action="add">
                        <p>Nome do compromisso:</p>
                      <input type="text" value=" " name="compromisso" />
                      <button type="submit">Give it now!</button>
                    </form>
                  </body>
                </html>"""


        @cherrypy.expose
        def show(self):

                return str(self.__dict__)

        @cherrypy.expose
        def add(self,data,nome,tempo,local):
                self.data=data
                self.nome=nome
                self.tempo=tempo
                self.local=local
                print self.__dict__
                return 'done!'



if __name__ == '__main__':
        cherrypy.server.socket_host = '0.0.0.0'
        cherrypy.quickstart(StringGenerator())