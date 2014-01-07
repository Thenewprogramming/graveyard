import wx
import LauncherFrame

def drawFrame(frame,name):
    #This will draw a frame
    frame = frame(None, name=name)
    frame.Show()

if __name__ == "__main__":
    app = wx.App(redirect=False)
    print "asdf"
    drawFrame(LauncherFrame.MainFrame,"Minecraft Launcher")
    app.MainLoop()