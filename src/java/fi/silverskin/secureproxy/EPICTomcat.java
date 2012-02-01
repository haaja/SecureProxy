package fi.silverskin.secureproxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EPICTomcat {
    private ProxyController proxy;

    
    public EPICTomcat() {
        this.proxy = new ProxyController();
    }
    
    public void handleGet(HttpServletRequest request, HttpServletResponse response) {

    }

    public void handlePost(HttpServletRequest request, HttpServletResponse response) {

    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {

    }

    public void handlePut(HttpServletRequest request, HttpServletResponse response) {

    }

    public void handleHead(HttpServletRequest request, HttpServletResponse response) {

    }

    public void handleOptions(HttpServletRequest request, HttpServletResponse response) {

    }     
}
