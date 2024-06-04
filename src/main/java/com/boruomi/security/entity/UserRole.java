package com.boruomi.security.entity;


import java.io.Serializable;

/**
 * UserRole
 * 
 * @author vladimir.stankovic
 *
 *         Aug 18, 2016
 */

public class UserRole {

    public static class Id implements Serializable {
        private static final long serialVersionUID = 1322120000551624359L;
        

        protected Long userId;
        

        protected Role role;
        
        public Id() { }

        public Id(Long userId, Role role) {
            this.userId = userId;
            this.role = role;
        }
    }
    

    Id id = new Id();
    

    protected Role role;

    public Role getRole() {
        return role;
    }
}
