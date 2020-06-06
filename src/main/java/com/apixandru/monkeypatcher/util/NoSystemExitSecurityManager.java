package com.apixandru.monkeypatcher.util;

import java.security.AccessControlException;
import java.security.Permission;

public class NoSystemExitSecurityManager extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
        // I will allow everything for you
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        // I will allow everything for you
    }

    @Override
    public void checkExit(int status) {
        // But I won't allow that
        AccessControlException exception = new AccessControlException("Won't system exit!");
        exception.printStackTrace();
        throw exception;
    }

}
