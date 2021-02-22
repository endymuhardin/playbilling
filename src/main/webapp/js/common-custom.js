function WindowMonitor()
{
    var targeturl='monitor.html'
    windowprops  = 'menubars=no, location=no, toolbars=no, scrollbars=no, resizable=no, '
    windowprops  = windowprops + 'titlebars=no, top=0, left=0'
    newwin=window.open("","",windowprops)
    newwin.moveTo(0,20)
    //newwin.resizeTo(w,h)
    //newwin.resizeTo(800,600)
    //newwin.resizeTo(screen.width,screen.height-40)
    newwin.resizeTo(550,350)
    newwin.location=targeturl
}

function WindowUsage()
{
    var targeturl='/member/usage.html'
    //windowprops  = 'menubars=no, location=no, toolbars=no, scrollbars=no, resizable=no, '
    //windowprops  = windowprops + 'titlebars=no, top=0, left=0'
    newwin=window.open("","")
    newwin.moveTo(0,0)
    //newwin.resizeTo(w,h)
    //newwin.resizeTo(800,600)
    newwin.resizeTo(screen.width,screen.height)
    //newwin.resizeTo(550,350)
    newwin.location=targeturl
}

