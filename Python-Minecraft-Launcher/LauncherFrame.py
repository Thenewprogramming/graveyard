import wx.html
import thread
import time
import urllib

class MainFrame(wx.Frame):
    def __init__(self, parent, name):
        wx.Frame.__init__(self, parent, -1, name, size=(854, 480))
        
        #Setting up the basic stuff of the frame
        self.panel = wx.Panel(self)
        self.panel.SetBackgroundColour("Black")
        self.webviewpanel = webviewpanel = wx.Panel(self.panel,-1,(0,0),(self.Size[0]+1,self.Size[1]+1-102))
        self.loginpanel = loginpanel = wx.Panel(self.panel,-1)
        self.Center()
        self.SetIcon(wx.Icon("./img/favicon.png",wx.BITMAP_TYPE_PNG))
        
        #Widgets go here
        self.webview = webview = wx.html.HtmlWindow(webviewpanel,-1,(0,0),self.webviewpanel.Size)
        webview.SetPage("<html><body><font color=\"#808080\"><br><br><br><br><br><br><br><center><h1>Loading update news..</h1></center></font></body></html>")
        webview.SetBackgroundColour("#404040")
        def loadpage():
            try:
                time.sleep(0.1)
                webview.LoadPage("http://mcupdate.tumblr.com/")
                webview.SetBackgroundImage(wx.Image("./img/bg_main.png",wx.BITMAP_TYPE_ANY).ConvertToBitmap())
            except Exception, e:
                webview.SetPage("<html><body><font color=\"#808080\"><br><br><br><br><br><br><br><center><h1>Failed to update news</h1><br>" + str(e) + "</center></font></body></html>")
                webview.SetBackgroundColour("#404040")
                print e
        thread.start_new_thread(loadpage, ())
        
        #Events go here
        self.Bind(wx.EVT_SIZE, self.reSize)
        
    def reSize(self,event):
        #Widget sizes and positions go here
        self.panel.SetSize((self.Size[0]+1,self.Size[1]+1))
        self.webviewpanel.SetSize((self.Size[0]+1,self.Size[1]+1-102))
        #self.webview.SetSize(self.webviewpanel.Size)
        self.loginpanel.SetSize((self.Size[0]+1,self.Size[1]+1-self.webviewpanel.GetSize()[1]-2))
        self.loginpanel.SetPosition((0,self.webviewpanel.GetSize()[1]+2))
